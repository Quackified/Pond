package com.clinicapp.service;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demonstration and testing class for the service managers.
 * This class demonstrates the usage of PatientManager, DoctorManager, and AppointmentManager
 * along with their respective data structures (ArrayList, Queue, Stack).
 * 
 * Run this class to verify that all managers compile and work correctly together.
 */
public class ServiceManagerDemo {
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  CLINIC APPOINTMENT SYSTEM - SERVICE DEMO");
        System.out.println("=================================================\n");
        
        PatientManager patientManager = new PatientManager();
        DoctorManager doctorManager = new DoctorManager();
        AppointmentManager appointmentManager = new AppointmentManager(patientManager, doctorManager);
        
        demonstratePatientManager(patientManager);
        demonstrateDoctorManager(doctorManager);
        demonstrateAppointmentManager(appointmentManager, patientManager, doctorManager);
        demonstrateWalkInQueue(appointmentManager, patientManager);
        demonstrateHistoryAndUndo(appointmentManager, patientManager, doctorManager);
        demonstrateSorting(appointmentManager);
        
        System.out.println("\n=================================================");
        System.out.println("  DEMO COMPLETED SUCCESSFULLY");
        System.out.println("=================================================");
    }
    
    /**
     * Demonstrates PatientManager functionality using ArrayList<Patient>.
     * Shows CRUD operations and search capabilities.
     */
    private static void demonstratePatientManager(PatientManager patientManager) {
        System.out.println("\n--- PATIENT MANAGER DEMO (ArrayList<Patient>) ---");
        
        System.out.println("\n1. Adding patients to the system:");
        Patient p1 = new Patient("P001", "John Smith", "555-1234", "john@email.com", "123 Main St", 35);
        Patient p2 = new Patient("P002", "Sarah Johnson", "555-5678", "sarah@email.com", "456 Oak Ave", 28);
        Patient p3 = new Patient("P003", "Michael Brown", "555-9012", "michael@email.com", "789 Pine Rd", 42);
        
        patientManager.addPatient(p1);
        patientManager.addPatient(p2);
        patientManager.addPatient(p3);
        
        System.out.println("\n2. Displaying all patients:");
        patientManager.displayAllPatients();
        
        System.out.println("\n3. Searching for patients by name:");
        List<Patient> searchResults = patientManager.searchPatientsByName("john");
        System.out.println("Found " + searchResults.size() + " patient(s) matching 'john'");
        
        System.out.println("\n4. Updating patient information:");
        Patient updated = new Patient("P001", "John Smith Jr.", "555-1234", "john.jr@email.com", "123 Main St", 36);
        patientManager.updatePatient("P001", updated);
        
        System.out.println("\n5. Total patient count: " + patientManager.getPatientCount());
    }
    
    /**
     * Demonstrates DoctorManager functionality with availability scheduling.
     * Shows doctor management and time slot operations.
     */
    private static void demonstrateDoctorManager(DoctorManager doctorManager) {
        System.out.println("\n\n--- DOCTOR MANAGER DEMO (ArrayList<Doctor> + Availability) ---");
        
        System.out.println("\n1. Adding doctors to the system:");
        Doctor d1 = new Doctor("D001", "Dr. Emily Wilson", "Cardiology", "555-2001");
        Doctor d2 = new Doctor("D002", "Dr. Robert Lee", "Pediatrics", "555-2002");
        Doctor d3 = new Doctor("D003", "Dr. Amanda Chen", "General Practice", "555-2003");
        
        doctorManager.addDoctor(d1);
        doctorManager.addDoctor(d2);
        doctorManager.addDoctor(d3);
        
        System.out.println("\n2. Adding available time slots:");
        doctorManager.addAvailableTimeSlot("D001", "2024-01-15 09:00");
        doctorManager.addAvailableTimeSlot("D001", "2024-01-15 10:00");
        doctorManager.addAvailableTimeSlot("D001", "2024-01-15 14:00");
        doctorManager.addAvailableTimeSlot("D002", "2024-01-15 09:30");
        doctorManager.addAvailableTimeSlot("D002", "2024-01-15 11:00");
        
        System.out.println("\n3. Displaying doctor schedule:");
        doctorManager.displayDoctorSchedule("D001");
        
        System.out.println("\n4. Searching doctors by specialization:");
        List<Doctor> cardiologists = doctorManager.searchDoctorsBySpecialization("cardio");
        System.out.println("Found " + cardiologists.size() + " cardiologist(s)");
        
        System.out.println("\n5. Total doctor count: " + doctorManager.getDoctorCount());
    }
    
    /**
     * Demonstrates AppointmentManager with ArrayList<Appointment> for schedule management.
     * Shows appointment scheduling, rescheduling, and cancellation.
     */
    private static void demonstrateAppointmentManager(
            AppointmentManager appointmentManager, 
            PatientManager patientManager, 
            DoctorManager doctorManager) {
        
        System.out.println("\n\n--- APPOINTMENT MANAGER DEMO (ArrayList<Appointment>) ---");
        
        System.out.println("\n1. Scheduling appointments:");
        LocalDateTime dateTime1 = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime dateTime3 = LocalDateTime.of(2024, 1, 16, 9, 0);
        
        appointmentManager.scheduleAppointment("P001", "D001", dateTime1);
        appointmentManager.scheduleAppointment("P002", "D002", dateTime2);
        appointmentManager.scheduleAppointment("P003", "D001", dateTime3);
        
        System.out.println("\n2. Displaying all appointments:");
        appointmentManager.displayAllAppointments();
        
        System.out.println("\n3. Checking time slot availability:");
        boolean available = appointmentManager.isTimeSlotAvailable("D001", dateTime1);
        System.out.println("D001 available at " + dateTime1 + ": " + available);
        
        System.out.println("\n4. Getting appointments by patient:");
        List<Appointment> patientAppts = appointmentManager.getAppointmentsByPatient("P001");
        System.out.println("Patient P001 has " + patientAppts.size() + " appointment(s)");
    }
    
    /**
     * Demonstrates walk-in queue functionality using Queue<Patient> (LinkedList).
     * Shows FIFO (First-In-First-Out) queue operations.
     */
    private static void demonstrateWalkInQueue(
            AppointmentManager appointmentManager, 
            PatientManager patientManager) {
        
        System.out.println("\n\n--- WALK-IN QUEUE DEMO (Queue<Patient> using LinkedList) ---");
        System.out.println("Queue follows FIFO: First patient in is first patient served");
        
        System.out.println("\n1. Adding patients to walk-in queue:");
        appointmentManager.addToWalkInQueue("P002");
        appointmentManager.addToWalkInQueue("P003");
        appointmentManager.addToWalkInQueue("P001");
        
        System.out.println("\n2. Viewing walk-in queue:");
        appointmentManager.displayWalkInQueue();
        
        System.out.println("\n3. Peeking at next patient (without removing):");
        Patient next = appointmentManager.peekNextWalkIn();
        if (next != null) {
            System.out.println("Next patient to be served: " + next.getName());
        }
        
        System.out.println("\n4. Processing walk-in patients (FIFO order):");
        while (appointmentManager.getWalkInQueueSize() > 0) {
            appointmentManager.processNextWalkIn();
        }
        
        System.out.println("\n5. Queue size after processing: " + appointmentManager.getWalkInQueueSize());
    }
    
    /**
     * Demonstrates history tracking and undo functionality using Stack<Appointment>.
     * Shows LIFO (Last-In-First-Out) stack operations for undo capability.
     */
    private static void demonstrateHistoryAndUndo(
            AppointmentManager appointmentManager,
            PatientManager patientManager,
            DoctorManager doctorManager) {
        
        System.out.println("\n\n--- HISTORY & UNDO DEMO (Stack<Appointment>) ---");
        System.out.println("Stack follows LIFO: Last operation is first to be undone");
        
        System.out.println("\n1. Scheduling a new appointment for history demo:");
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 17, 14, 0);
        appointmentManager.scheduleAppointment("P001", "D002", dateTime);
        
        System.out.println("\n2. Finding the appointment ID:");
        List<Appointment> allAppts = appointmentManager.getAllAppointments();
        String appointmentId = null;
        for (Appointment appt : allAppts) {
            if (appt.getPatient().getPatientId().equals("P001") && 
                appt.getDoctor().getDoctorId().equals("D002")) {
                appointmentId = appt.getAppointmentId();
                break;
            }
        }
        
        if (appointmentId != null) {
            System.out.println("\n3. Cancelling appointment (pushed to history stack):");
            appointmentManager.cancelAppointment(appointmentId);
            
            System.out.println("\n4. History stack size: " + appointmentManager.getHistorySize());
            
            System.out.println("\n5. Undoing last operation (pop from stack):");
            appointmentManager.undoLastOperation();
            
            System.out.println("\n6. Verifying appointment status after undo:");
            Appointment restored = appointmentManager.findAppointmentById(appointmentId);
            if (restored != null) {
                System.out.println("Appointment status: " + restored.getStatus());
            }
        }
        
        System.out.println("\n7. Demonstrating reschedule with history:");
        LocalDateTime newDateTime = LocalDateTime.of(2024, 1, 17, 15, 0);
        if (appointmentId != null) {
            appointmentManager.rescheduleAppointment(appointmentId, newDateTime);
            System.out.println("History stack now contains: " + appointmentManager.getHistorySize() + " item(s)");
        }
    }
    
    /**
     * Demonstrates sorting capabilities for appointments.
     * Shows different sorting options: by time, by doctor, by patient.
     */
    private static void demonstrateSorting(AppointmentManager appointmentManager) {
        System.out.println("\n\n--- APPOINTMENT SORTING DEMO ---");
        
        System.out.println("\n1. Appointments sorted by time (earliest first):");
        List<Appointment> byTime = appointmentManager.getAppointmentsSortedByTime();
        System.out.println("Total sorted appointments: " + byTime.size());
        
        System.out.println("\n2. Appointments sorted by doctor:");
        List<Appointment> byDoctor = appointmentManager.getAppointmentsSortedByDoctor();
        System.out.println("Total sorted appointments: " + byDoctor.size());
        
        System.out.println("\n3. Appointments sorted by patient:");
        List<Appointment> byPatient = appointmentManager.getAppointmentsSortedByPatient();
        System.out.println("Total sorted appointments: " + byPatient.size());
        
        System.out.println("\n4. Displaying scheduled appointments sorted by time:");
        appointmentManager.displayScheduledAppointmentsByTime();
    }
}
