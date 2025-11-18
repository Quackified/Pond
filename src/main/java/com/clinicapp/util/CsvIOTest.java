package com.clinicapp.util;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * CsvIOTest - Test class for CSV import/export functionality.
 * Verifies that data can be exported to CSV and imported back correctly.
 */
public class CsvIOTest {
    
    public static void main(String[] args) {
        System.out.println("=== CSV Import/Export Test ===\n");
        
        try {
            // Create test directory
            File exportDir = new File("csv_test");
            if (!exportDir.exists()) {
                exportDir.mkdir();
            }
            
            // Test patient export/import
            testPatientCsvIO(exportDir);
            
            // Test doctor export/import
            testDoctorCsvIO(exportDir);
            
            // Test appointment export/import
            testAppointmentCsvIO(exportDir);
            
            System.out.println("\n=== All tests passed! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testPatientCsvIO(File exportDir) throws Exception {
        System.out.println("Testing Patient CSV Export/Import...");
        
        PatientManager manager1 = new PatientManager();
        
        // Add test patients
        Patient p1 = manager1.addPatient(
            "John Doe",
            LocalDate.of(1990, 5, 15),
            "Male",
            "5551234567",
            "john@example.com",
            "123 Main St",
            "O+",
            "Penicillin"
        );
        
        Patient p2 = manager1.addPatient(
            "Jane Smith",
            LocalDate.of(1985, 3, 22),
            "Female",
            "5559876543",
            "jane@example.com",
            "456 Oak Ave",
            "A-",
            "None"
        );
        
        System.out.println("  Original patients: " + manager1.getAllPatients().size());
        
        // Export
        String exportPath = exportDir.getPath() + "/patients.csv";
        CsvExporter.exportPatients(manager1, exportPath);
        System.out.println("  Exported to: " + exportPath);
        
        // Import into new manager
        PatientManager manager2 = new PatientManager();
        CsvImporter.importPatients(manager2, exportPath);
        System.out.println("  Imported patients: " + manager2.getAllPatients().size());
        
        // Verify
        assert manager2.getAllPatients().size() == 2 : "Patient count mismatch";
        Patient imported1 = manager2.getPatientById(p1.getId());
        assert imported1 != null : "Patient 1 not found";
        assert imported1.getName().equals(p1.getName()) : "Patient name mismatch";
        assert imported1.getEmail().equals(p1.getEmail()) : "Patient email mismatch";
        
        System.out.println("  ✓ Patient CSV I/O test passed\n");
    }
    
    private static void testDoctorCsvIO(File exportDir) throws Exception {
        System.out.println("Testing Doctor CSV Export/Import...");
        
        DoctorManager manager1 = new DoctorManager();
        
        // Add test doctors
        Doctor d1 = manager1.addDoctor(
            "Sarah Williams",
            "Cardiology",
            "5551111111",
            "sarah@clinic.com",
            Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
            "09:00",
            "17:00"
        );
        
        Doctor d2 = manager1.addDoctor(
            "Michael Chen",
            "Pediatrics",
            "5552222222",
            "michael@clinic.com",
            Arrays.asList("Monday", "Wednesday", "Friday"),
            "08:00",
            "16:00"
        );
        
        d2.setAvailable(false);
        
        System.out.println("  Original doctors: " + manager1.getAllDoctors().size());
        
        // Export
        String exportPath = exportDir.getPath() + "/doctors.csv";
        CsvExporter.exportDoctors(manager1, exportPath);
        System.out.println("  Exported to: " + exportPath);
        
        // Import into new manager
        DoctorManager manager2 = new DoctorManager();
        CsvImporter.importDoctors(manager2, exportPath);
        System.out.println("  Imported doctors: " + manager2.getAllDoctors().size());
        
        // Verify
        assert manager2.getAllDoctors().size() == 2 : "Doctor count mismatch";
        Doctor imported1 = manager2.getDoctorById(d1.getId());
        assert imported1 != null : "Doctor 1 not found";
        assert imported1.getName().equals(d1.getName()) : "Doctor name mismatch";
        assert imported1.getSpecialization().equals(d1.getSpecialization()) : "Specialization mismatch";
        
        Doctor imported2 = manager2.getDoctorById(d2.getId());
        assert !imported2.isAvailable() : "Doctor availability not restored";
        
        System.out.println("  ✓ Doctor CSV I/O test passed\n");
    }
    
    private static void testAppointmentCsvIO(File exportDir) throws Exception {
        System.out.println("Testing Appointment CSV Export/Import...");
        
        // Create managers with test data
        PatientManager patientManager = new PatientManager();
        Patient patient = patientManager.addPatient(
            "Test Patient",
            LocalDate.of(1980, 1, 1),
            "Male",
            "5551234567",
            "test@example.com",
            "123 Test St",
            "O+",
            "None"
        );
        
        DoctorManager doctorManager = new DoctorManager();
        Doctor doctor = doctorManager.addDoctor(
            "Test Doctor",
            "General",
            "5559999999",
            "doctor@clinic.com",
            Arrays.asList("Monday", "Tuesday"),
            "09:00",
            "17:00"
        );
        
        AppointmentManager manager1 = new AppointmentManager(patientManager, doctorManager);
        
        // Add test appointments
        Appointment a1 = manager1.scheduleAppointment(
            patient, doctor,
            LocalDate.of(2025, 12, 1),
            LocalTime.of(10, 0),
            LocalTime.of(10, 30),
            "Regular checkup"
        );
        
        Appointment a2 = manager1.scheduleAppointment(
            patient, doctor,
            LocalDate.of(2025, 12, 2),
            LocalTime.of(14, 0),
            LocalTime.of(14, 30),
            "Follow-up"
        );
        
        a2.setStatus(Appointment.AppointmentStatus.COMPLETED);
        a2.setNotes("Patient in good condition");
        
        System.out.println("  Original appointments: " + manager1.getAllAppointments().size());
        
        // Export
        String exportPath = exportDir.getPath() + "/appointments.csv";
        CsvExporter.exportAppointments(manager1, exportPath);
        System.out.println("  Exported to: " + exportPath);
        
        // Import into new manager
        AppointmentManager manager2 = new AppointmentManager(patientManager, doctorManager);
        CsvImporter.importAppointments(manager2, patientManager, doctorManager, exportPath);
        System.out.println("  Imported appointments: " + manager2.getAllAppointments().size());
        
        // Verify
        assert manager2.getAllAppointments().size() == 2 : "Appointment count mismatch";
        Appointment imported1 = manager2.getAppointmentById(a1.getId());
        assert imported1 != null : "Appointment 1 not found";
        assert imported1.getReason().equals(a1.getReason()) : "Appointment reason mismatch";
        
        Appointment imported2 = manager2.getAppointmentById(a2.getId());
        assert imported2.getStatus() == Appointment.AppointmentStatus.COMPLETED : "Appointment status not restored";
        assert imported2.getNotes().equals("Patient in good condition") : "Appointment notes mismatch";
        
        System.out.println("  ✓ Appointment CSV I/O test passed\n");
    }
}
