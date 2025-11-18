package com.clinicapp.storage;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.clinicapp.util.InputValidator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * CsvExporter handles exporting data to CSV files.
 */
public class CsvExporter {
    
    private static final String EXPORT_DIR = "exports";
    
    public CsvExporter() {
        ensureExportDirectoryExists();
    }
    
    private void ensureExportDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(EXPORT_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create export directory: " + e.getMessage());
        }
    }
    
    /**
     * Export patients to CSV file.
     */
    public boolean exportPatients(List<Patient> patients, String filename) {
        String filepath = EXPORT_DIR + "/" + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            writer.println("ID,Name,Date of Birth,Age,Gender,Phone Number,Email,Address,Blood Type,Allergies");
            
            for (Patient patient : patients) {
                writer.printf("%d,\"%s\",%s,%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    patient.getId(),
                    escapeCsv(patient.getName()),
                    InputValidator.formatDate(patient.getDateOfBirth()),
                    patient.getAge(),
                    escapeCsv(patient.getGender()),
                    escapeCsv(patient.getPhoneNumber()),
                    escapeCsv(patient.getEmail()),
                    escapeCsv(patient.getAddress()),
                    escapeCsv(patient.getBloodType()),
                    escapeCsv(patient.getAllergies())
                );
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Failed to export patients: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Export doctors to CSV file.
     */
    public boolean exportDoctors(List<Doctor> doctors, String filename) {
        String filepath = EXPORT_DIR + "/" + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            writer.println("ID,Name,Specialization,Phone Number,Email,Available Days,Start Time,End Time,Available");
            
            for (Doctor doctor : doctors) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,%s%n",
                    doctor.getId(),
                    escapeCsv(doctor.getName()),
                    escapeCsv(doctor.getSpecialization()),
                    escapeCsv(doctor.getPhoneNumber()),
                    escapeCsv(doctor.getEmail()),
                    escapeCsv(String.join("; ", doctor.getAvailableDays())),
                    InputValidator.formatTime(doctor.getStartTime()),
                    InputValidator.formatTime(doctor.getEndTime()),
                    doctor.isAvailable()
                );
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Failed to export doctors: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Export appointments to CSV file.
     */
    public boolean exportAppointments(List<Appointment> appointments, String filename) {
        String filepath = EXPORT_DIR + "/" + filename;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
            writer.println("ID,Patient Name,Doctor Name,Date & Time,Reason,Status,Notes,Created At");
            
            for (Appointment apt : appointments) {
                writer.printf("%d,\"%s\",\"%s\",%s,\"%s\",%s,\"%s\",%s%n",
                    apt.getId(),
                    escapeCsv(apt.getPatient().getName()),
                    escapeCsv(apt.getDoctor().getName()),
                    InputValidator.formatDateTime(apt.getAppointmentDateTime()),
                    escapeCsv(apt.getReason()),
                    apt.getStatus(),
                    escapeCsv(apt.getNotes()),
                    InputValidator.formatDateTime(apt.getCreatedAt())
                );
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Failed to export appointments: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Escape CSV special characters.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }
}
