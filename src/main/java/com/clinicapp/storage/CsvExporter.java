package com.clinicapp.storage;

import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Appointment;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvExporter {
    private static final String EXPORT_DIR = "exports";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public CsvExporter() {
        File dir = new File(EXPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public void exportPatients(List<Patient> patients, String filename) throws IOException {
        String filepath = EXPORT_DIR + "/" + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("ID,Name,Date of Birth,Age,Gender,Phone Number,Email,Address,Blood Type,Allergies\n");
            
            for (Patient p : patients) {
                writer.write(String.format("%d,%s,%s,%d,%s,%s,%s,%s,%s,%s\n",
                    p.getId(),
                    escapeCsv(p.getName()),
                    p.getDateOfBirth().format(DATE_FORMATTER),
                    p.getAge(),
                    escapeCsv(p.getGender()),
                    escapeCsv(p.getPhoneNumber()),
                    escapeCsv(p.getEmail()),
                    escapeCsv(p.getAddress()),
                    escapeCsv(p.getBloodType()),
                    escapeCsv(p.getAllergies())
                ));
            }
        }
    }
    
    public void exportDoctors(List<Doctor> doctors, String filename) throws IOException {
        String filepath = EXPORT_DIR + "/" + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("ID,Name,Specialization,Phone Number,Email,Available Days,Start Time,End Time,Available\n");
            
            for (Doctor d : doctors) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    d.getId(),
                    escapeCsv(d.getName()),
                    escapeCsv(d.getSpecialization()),
                    escapeCsv(d.getPhoneNumber()),
                    escapeCsv(d.getEmail()),
                    escapeCsv(String.join("; ", d.getAvailableDays())),
                    escapeCsv(d.getStartTime()),
                    escapeCsv(d.getEndTime()),
                    d.isAvailable() ? "Yes" : "No"
                ));
            }
        }
    }
    
    public void exportAppointments(List<Appointment> appointments, String filename) throws IOException {
        String filepath = EXPORT_DIR + "/" + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write("ID,Date & Time,Patient ID,Patient Name,Doctor ID,Doctor Name,Reason,Status,Notes\n");
            
            for (Appointment a : appointments) {
                writer.write(String.format("%d,%s,%d,%s,%d,%s,%s,%s,%s\n",
                    a.getId(),
                    a.getAppointmentDateTime().format(DATETIME_FORMATTER),
                    a.getPatient().getId(),
                    escapeCsv(a.getPatient().getName()),
                    a.getDoctor().getId(),
                    escapeCsv(a.getDoctor().getName()),
                    escapeCsv(a.getReason()),
                    a.getStatus().toString(),
                    escapeCsv(a.getNotes())
                ));
            }
        }
    }
    
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
