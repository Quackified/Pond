package com.clinicapp.storage;

import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Appointment;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {
    private static final String DATA_DIR = "data";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.json";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.json";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.json";
    
    public JsonStorage() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public void savePatients(List<Patient> patients) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            json.append("  {\n");
            json.append("    \"id\": ").append(p.getId()).append(",\n");
            json.append("    \"name\": \"").append(escape(p.getName())).append("\",\n");
            json.append("    \"dateOfBirth\": \"").append(p.getDateOfBirth()).append("\",\n");
            json.append("    \"gender\": \"").append(escape(p.getGender())).append("\",\n");
            json.append("    \"phoneNumber\": \"").append(escape(p.getPhoneNumber())).append("\",\n");
            json.append("    \"email\": \"").append(escape(p.getEmail())).append("\",\n");
            json.append("    \"address\": \"").append(escape(p.getAddress())).append("\",\n");
            json.append("    \"bloodType\": \"").append(escape(p.getBloodType())).append("\",\n");
            json.append("    \"allergies\": \"").append(escape(p.getAllergies())).append("\"\n");
            json.append("  }");
            if (i < patients.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("]\n");
        writeFile(PATIENTS_FILE, json.toString());
    }
    
    public List<Patient> loadPatients() throws IOException {
        List<Patient> patients = new ArrayList<>();
        String content = readFile(PATIENTS_FILE);
        if (content == null || content.trim().isEmpty()) {
            return patients;
        }
        
        content = content.trim();
        if (!content.startsWith("[")) {
            return patients;
        }
        
        String[] objects = content.substring(1, content.length() - 1).split("\\},");
        
        for (String obj : objects) {
            if (obj.trim().isEmpty()) continue;
            
            obj = obj.trim();
            if (!obj.endsWith("}")) {
                obj = obj + "}";
            }
            
            int id = extractInt(obj, "id");
            String name = extractString(obj, "name");
            String dobStr = extractString(obj, "dateOfBirth");
            String gender = extractString(obj, "gender");
            String phone = extractString(obj, "phoneNumber");
            String email = extractString(obj, "email");
            String address = extractString(obj, "address");
            String bloodType = extractString(obj, "bloodType");
            String allergies = extractString(obj, "allergies");
            
            LocalDate dob = LocalDate.parse(dobStr);
            Patient patient = new Patient(name, dob, gender, phone, email, address, bloodType, allergies);
            setPatientId(patient, id);
            patients.add(patient);
        }
        
        return patients;
    }
    
    public void saveDoctors(List<Doctor> doctors) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            json.append("  {\n");
            json.append("    \"id\": ").append(d.getId()).append(",\n");
            json.append("    \"name\": \"").append(escape(d.getName())).append("\",\n");
            json.append("    \"specialization\": \"").append(escape(d.getSpecialization())).append("\",\n");
            json.append("    \"phoneNumber\": \"").append(escape(d.getPhoneNumber())).append("\",\n");
            json.append("    \"email\": \"").append(escape(d.getEmail())).append("\",\n");
            json.append("    \"availableDays\": \"").append(String.join(",", d.getAvailableDays())).append("\",\n");
            json.append("    \"startTime\": \"").append(escape(d.getStartTime())).append("\",\n");
            json.append("    \"endTime\": \"").append(escape(d.getEndTime())).append("\",\n");
            json.append("    \"isAvailable\": ").append(d.isAvailable()).append("\n");
            json.append("  }");
            if (i < doctors.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("]\n");
        writeFile(DOCTORS_FILE, json.toString());
    }
    
    public List<Doctor> loadDoctors() throws IOException {
        List<Doctor> doctors = new ArrayList<>();
        String content = readFile(DOCTORS_FILE);
        if (content == null || content.trim().isEmpty()) {
            return doctors;
        }
        
        content = content.trim();
        if (!content.startsWith("[")) {
            return doctors;
        }
        
        String[] objects = content.substring(1, content.length() - 1).split("\\},");
        
        for (String obj : objects) {
            if (obj.trim().isEmpty()) continue;
            
            obj = obj.trim();
            if (!obj.endsWith("}")) {
                obj = obj + "}";
            }
            
            int id = extractInt(obj, "id");
            String name = extractString(obj, "name");
            String specialization = extractString(obj, "specialization");
            String phone = extractString(obj, "phoneNumber");
            String email = extractString(obj, "email");
            String availableDaysStr = extractString(obj, "availableDays");
            String startTime = extractString(obj, "startTime");
            String endTime = extractString(obj, "endTime");
            boolean isAvailable = extractBoolean(obj, "isAvailable");
            
            List<String> availableDays = new ArrayList<>();
            if (availableDaysStr != null && !availableDaysStr.isEmpty()) {
                String[] days = availableDaysStr.split(",");
                for (String day : days) {
                    availableDays.add(day.trim());
                }
            }
            
            Doctor doctor = new Doctor(name, specialization, phone, email, availableDays, startTime, endTime);
            setDoctorId(doctor, id);
            doctor.setAvailable(isAvailable);
            doctors.add(doctor);
        }
        
        return doctors;
    }
    
    public void saveAppointments(List<Appointment> appointments, List<Patient> patients, List<Doctor> doctors) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            json.append("  {\n");
            json.append("    \"id\": ").append(a.getId()).append(",\n");
            json.append("    \"patientId\": ").append(a.getPatient().getId()).append(",\n");
            json.append("    \"doctorId\": ").append(a.getDoctor().getId()).append(",\n");
            json.append("    \"appointmentDateTime\": \"").append(a.getAppointmentDateTime()).append("\",\n");
            json.append("    \"reason\": \"").append(escape(a.getReason())).append("\",\n");
            json.append("    \"status\": \"").append(a.getStatus()).append("\",\n");
            json.append("    \"notes\": \"").append(escape(a.getNotes())).append("\"\n");
            json.append("  }");
            if (i < appointments.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("]\n");
        writeFile(APPOINTMENTS_FILE, json.toString());
    }
    
    public List<Appointment> loadAppointments(List<Patient> patients, List<Doctor> doctors) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        String content = readFile(APPOINTMENTS_FILE);
        if (content == null || content.trim().isEmpty()) {
            return appointments;
        }
        
        content = content.trim();
        if (!content.startsWith("[")) {
            return appointments;
        }
        
        String[] objects = content.substring(1, content.length() - 1).split("\\},");
        
        for (String obj : objects) {
            if (obj.trim().isEmpty()) continue;
            
            obj = obj.trim();
            if (!obj.endsWith("}")) {
                obj = obj + "}";
            }
            
            int id = extractInt(obj, "id");
            int patientId = extractInt(obj, "patientId");
            int doctorId = extractInt(obj, "doctorId");
            String dateTimeStr = extractString(obj, "appointmentDateTime");
            String reason = extractString(obj, "reason");
            String statusStr = extractString(obj, "status");
            String notes = extractString(obj, "notes");
            
            Patient patient = findPatientById(patients, patientId);
            Doctor doctor = findDoctorById(doctors, doctorId);
            
            if (patient == null || doctor == null) {
                continue;
            }
            
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
            Appointment appointment = new Appointment(patient, doctor, dateTime, reason);
            setAppointmentId(appointment, id);
            appointment.setStatus(Appointment.AppointmentStatus.valueOf(statusStr));
            appointment.setNotes(notes);
            appointments.add(appointment);
        }
        
        return appointments;
    }
    
    private Patient findPatientById(List<Patient> patients, int id) {
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    private Doctor findDoctorById(List<Doctor> doctors, int id) {
        for (Doctor d : doctors) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }
    
    private String escape(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }
    
    private String extractString(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"";
        int startIndex = json.indexOf(pattern);
        if (startIndex == -1) return "";
        
        startIndex += pattern.length();
        int endIndex = startIndex;
        
        while (endIndex < json.length()) {
            if (json.charAt(endIndex) == '"' && (endIndex == 0 || json.charAt(endIndex - 1) != '\\')) {
                break;
            }
            endIndex++;
        }
        
        return json.substring(startIndex, endIndex);
    }
    
    private int extractInt(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*";
        int startIndex = json.indexOf(pattern);
        if (startIndex == -1) return 0;
        
        startIndex += pattern.length();
        int endIndex = startIndex;
        
        while (endIndex < json.length() && (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '-')) {
            endIndex++;
        }
        
        return Integer.parseInt(json.substring(startIndex, endIndex));
    }
    
    private boolean extractBoolean(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*";
        int startIndex = json.indexOf(pattern);
        if (startIndex == -1) return false;
        
        startIndex += pattern.length();
        return json.substring(startIndex).trim().startsWith("true");
    }
    
    private void writeFile(String filename, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        }
    }
    
    private String readFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    private void setPatientId(Patient patient, int id) {
        try {
            java.lang.reflect.Field field = Patient.class.getDeclaredField("id");
            field.setAccessible(true);
            java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            field.set(patient, id);
        } catch (Exception e) {
            System.err.println("Could not set patient ID: " + e.getMessage());
        }
    }
    
    private void setDoctorId(Doctor doctor, int id) {
        try {
            java.lang.reflect.Field field = Doctor.class.getDeclaredField("id");
            field.setAccessible(true);
            java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            field.set(doctor, id);
        } catch (Exception e) {
            System.err.println("Could not set doctor ID: " + e.getMessage());
        }
    }
    
    private void setAppointmentId(Appointment appointment, int id) {
        try {
            java.lang.reflect.Field field = Appointment.class.getDeclaredField("id");
            field.setAccessible(true);
            java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            field.set(appointment, id);
        } catch (Exception e) {
            System.err.println("Could not set appointment ID: " + e.getMessage());
        }
    }
}
