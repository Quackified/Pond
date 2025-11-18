package com.clinicapp.util;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvImporter handles importing clinic data (patients, doctors, appointments) from CSV files.
 * Parses CSV format and populates the managers with imported data.
 */
public class CsvImporter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Import patients from a CSV file.
     *
     * @param patientManager The PatientManager to populate
     * @param filepath Path to the CSV file
     * @throws IOException if file reading fails
     */
    public static void importPatients(PatientManager patientManager, String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                List<String> fields = parseCSVLine(line);
                if (fields.size() < 9) {
                    continue;
                }
                
                try {
                    int id = Integer.parseInt(fields.get(0));
                    String name = fields.get(1);
                    LocalDate dateOfBirth = LocalDate.parse(fields.get(2), DATE_FORMATTER);
                    String gender = fields.get(3);
                    String phoneNumber = fields.get(4);
                    String email = fields.get(5).isEmpty() ? null : fields.get(5);
                    String address = fields.get(6).isEmpty() ? null : fields.get(6);
                    String bloodType = fields.get(7).isEmpty() ? null : fields.get(7);
                    String allergies = fields.get(8).isEmpty() ? null : fields.get(8);
                    
                    Patient patient = patientManager.addPatient(name, dateOfBirth, gender,
                        phoneNumber, email, address, bloodType, allergies);
                    
                    setPatientId(patient, id);
                } catch (Exception e) {
                    System.err.println("Error importing patient from line: " + line);
                }
            }
        }
    }
    
    /**
     * Import doctors from a CSV file.
     *
     * @param doctorManager The DoctorManager to populate
     * @param filepath Path to the CSV file
     * @throws IOException if file reading fails
     */
    public static void importDoctors(DoctorManager doctorManager, String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                List<String> fields = parseCSVLine(line);
                if (fields.size() < 9) {
                    continue;
                }
                
                try {
                    int id = Integer.parseInt(fields.get(0));
                    String name = fields.get(1);
                    String specialization = fields.get(2);
                    String phoneNumber = fields.get(3);
                    String email = fields.get(4).isEmpty() ? null : fields.get(4);
                    String availableDaysStr = fields.get(5);
                    String startTime = fields.get(6);
                    String endTime = fields.get(7);
                    boolean isAvailable = Boolean.parseBoolean(fields.get(8));
                    
                    List<String> availableDays = new ArrayList<>();
                    if (!availableDaysStr.isEmpty()) {
                        for (String day : availableDaysStr.split(";")) {
                            availableDays.add(day.trim());
                        }
                    }
                    
                    Doctor doctor = doctorManager.addDoctor(name, specialization, phoneNumber,
                        email, availableDays, startTime, endTime);
                    
                    if (!isAvailable) {
                        doctor.setAvailable(false);
                    }
                    
                    setDoctorId(doctor, id);
                } catch (Exception e) {
                    System.err.println("Error importing doctor from line: " + line);
                }
            }
        }
    }
    
    /**
     * Import appointments from a CSV file.
     * Note: This method requires that all referenced patients and doctors already exist in their respective managers.
     *
     * @param appointmentManager The AppointmentManager to populate
     * @param patientManager The PatientManager containing referenced patients
     * @param doctorManager The DoctorManager containing referenced doctors
     * @param filepath Path to the CSV file
     * @throws IOException if file reading fails
     */
    public static void importAppointments(AppointmentManager appointmentManager, 
                                         PatientManager patientManager, 
                                         DoctorManager doctorManager, 
                                         String filepath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                List<String> fields = parseCSVLine(line);
                if (fields.size() < 10) {
                    continue;
                }
                
                try {
                    int id = Integer.parseInt(fields.get(0));
                    int patientId = Integer.parseInt(fields.get(1));
                    int doctorId = Integer.parseInt(fields.get(2));
                    LocalDate date = LocalDate.parse(fields.get(3), DATE_FORMATTER);
                    LocalTime startTime = LocalTime.parse(fields.get(4), TIME_FORMATTER);
                    LocalTime endTime = LocalTime.parse(fields.get(5), TIME_FORMATTER);
                    String reason = fields.get(6);
                    String status = fields.get(7);
                    String notes = fields.get(8).isEmpty() ? null : fields.get(8);
                    
                    Patient patient = patientManager.getPatientById(patientId);
                    Doctor doctor = doctorManager.getDoctorById(doctorId);
                    
                    if (patient == null || doctor == null) {
                        System.err.println("Error: Patient or Doctor not found for appointment ID " + id);
                        continue;
                    }
                    
                    Appointment appointment = appointmentManager.scheduleAppointment(patient, doctor, date, startTime, endTime, reason);
                    
                    if (notes != null && !notes.isEmpty()) {
                        appointment.setNotes(notes);
                    }
                    
                    try {
                        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid status: " + status);
                    }
                    
                    setAppointmentId(appointment, id);
                } catch (Exception e) {
                    System.err.println("Error importing appointment from line: " + line);
                }
            }
        }
    }
    
    /**
     * Parse a CSV line handling quoted fields and commas within quotes.
     *
     * @param line The CSV line to parse
     * @return List of parsed fields
     */
    private static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    current.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        fields.add(current.toString());
        return fields;
    }
    
    /**
     * Set a patient's ID using reflection to override the auto-increment.
     * This is used when importing data to preserve original IDs.
     *
     * @param patient The patient to set ID for
     * @param id The ID to set
     */
    private static void setPatientId(Patient patient, int id) {
        try {
            java.lang.reflect.Field field = Patient.class.getDeclaredField("id");
            field.setAccessible(true);
            field.setInt(patient, id);
        } catch (Exception e) {
            System.err.println("Could not set patient ID: " + e.getMessage());
        }
    }
    
    /**
     * Set a doctor's ID using reflection to override the auto-increment.
     * This is used when importing data to preserve original IDs.
     *
     * @param doctor The doctor to set ID for
     * @param id The ID to set
     */
    private static void setDoctorId(Doctor doctor, int id) {
        try {
            java.lang.reflect.Field field = Doctor.class.getDeclaredField("id");
            field.setAccessible(true);
            field.setInt(doctor, id);
        } catch (Exception e) {
            System.err.println("Could not set doctor ID: " + e.getMessage());
        }
    }
    
    /**
     * Set an appointment's ID using reflection to override the auto-increment.
     * This is used when importing data to preserve original IDs.
     *
     * @param appointment The appointment to set ID for
     * @param id The ID to set
     */
    private static void setAppointmentId(Appointment appointment, int id) {
        try {
            java.lang.reflect.Field field = Appointment.class.getDeclaredField("id");
            field.setAccessible(true);
            field.setInt(appointment, id);
        } catch (Exception e) {
            System.err.println("Could not set appointment ID: " + e.getMessage());
        }
    }
}
