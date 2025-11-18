package com.clinicapp.storage;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonStorage handles JSON file I/O operations using Gson library.
 * Provides methods to save and load patients, doctors, and appointments.
 */
public class JsonStorage {
    
    private static final String DATA_DIR = "data";
    private static final String PATIENTS_FILE = DATA_DIR + "/patients.json";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.json";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.json";
    
    private final Gson gson;
    
    public JsonStorage() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
        
        ensureDataDirectoryExists();
    }
    
    private void ensureDataDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }
    
    /**
     * Save list of patients to JSON file.
     */
    public void savePatients(List<Patient> patients) {
        try (Writer writer = new FileWriter(PATIENTS_FILE)) {
            gson.toJson(patients, writer);
        } catch (IOException e) {
            System.err.println("Failed to save patients: " + e.getMessage());
        }
    }
    
    /**
     * Load list of patients from JSON file.
     */
    public List<Patient> loadPatients() {
        File file = new File(PATIENTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Patient>>(){}.getType();
            List<Patient> patients = gson.fromJson(reader, listType);
            return patients != null ? patients : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to load patients: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Save list of doctors to JSON file.
     */
    public void saveDoctors(List<Doctor> doctors) {
        try (Writer writer = new FileWriter(DOCTORS_FILE)) {
            gson.toJson(doctors, writer);
        } catch (IOException e) {
            System.err.println("Failed to save doctors: " + e.getMessage());
        }
    }
    
    /**
     * Load list of doctors from JSON file.
     */
    public List<Doctor> loadDoctors() {
        File file = new File(DOCTORS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Doctor>>(){}.getType();
            List<Doctor> doctors = gson.fromJson(reader, listType);
            return doctors != null ? doctors : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to load doctors: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Save list of appointments to JSON file.
     */
    public void saveAppointments(List<Appointment> appointments) {
        try (Writer writer = new FileWriter(APPOINTMENTS_FILE)) {
            gson.toJson(appointments, writer);
        } catch (IOException e) {
            System.err.println("Failed to save appointments: " + e.getMessage());
        }
    }
    
    /**
     * Load list of appointments from JSON file.
     */
    public List<Appointment> loadAppointments() {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Appointment>>(){}.getType();
            List<Appointment> appointments = gson.fromJson(reader, listType);
            return appointments != null ? appointments : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to load appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
