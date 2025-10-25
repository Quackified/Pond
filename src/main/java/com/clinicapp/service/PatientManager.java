package com.clinicapp.service;

import com.clinicapp.model.Patient;
import java.time.LocalDate;
import java.util.*;

/**
 * PatientManager handles all patient-related operations including
 * adding, updating, deleting, and searching for patients.
 * Uses a HashMap for efficient patient lookup by ID.
 */
public class PatientManager {
    // HashMap for O(1) lookup by patient ID
    private final Map<Integer, Patient> patients;
    
    /**
     * Constructor initializes the patient storage.
     */
    public PatientManager() {
        this.patients = new HashMap<>();
    }
    
    /**
     * Add a new patient to the system.
     * 
     * @param name Patient's full name
     * @param dateOfBirth Patient's date of birth
     * @param gender Patient's gender
     * @param phoneNumber Patient's contact number
     * @param email Patient's email address
     * @param address Patient's residential address
     * @param bloodType Patient's blood type
     * @param allergies Patient's known allergies
     * @return The newly created Patient object
     */
    public Patient addPatient(String name, LocalDate dateOfBirth, String gender,
                             String phoneNumber, String email, String address,
                             String bloodType, String allergies) {
        Patient patient = new Patient(name, dateOfBirth, gender, phoneNumber, 
                                     email, address, bloodType, allergies);
        patients.put(patient.getId(), patient);
        return patient;
    }
    
    /**
     * Get a patient by their ID.
     * 
     * @param id Patient ID to look up
     * @return Patient object if found, null otherwise
     */
    public Patient getPatientById(int id) {
        return patients.get(id);
    }
    
    /**
     * Get all patients in the system.
     * 
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }
    
    /**
     * Search for patients by name (case-insensitive partial match).
     * 
     * @param name Name or partial name to search for
     * @return List of matching patients
     */
    public List<Patient> searchPatientsByName(String name) {
        List<Patient> results = new ArrayList<>();
        String searchTerm = name.toLowerCase();
        
        for (Patient patient : patients.values()) {
            if (patient.getName().toLowerCase().contains(searchTerm)) {
                results.add(patient);
            }
        }
        
        return results;
    }
    
    /**
     * Update patient information.
     * 
     * @param id Patient ID to update
     * @param name New name (null to keep existing)
     * @param dateOfBirth New date of birth (null to keep existing)
     * @param gender New gender (null to keep existing)
     * @param phoneNumber New phone number (null to keep existing)
     * @param email New email (null to keep existing)
     * @param address New address (null to keep existing)
     * @param bloodType New blood type (null to keep existing)
     * @param allergies New allergies (null to keep existing)
     * @return true if patient was found and updated, false otherwise
     */
    public boolean updatePatient(int id, String name, LocalDate dateOfBirth, 
                                String gender, String phoneNumber, String email,
                                String address, String bloodType, String allergies) {
        Patient patient = patients.get(id);
        if (patient == null) {
            return false;
        }
        
        // Update only non-null fields
        if (name != null) patient.setName(name);
        if (dateOfBirth != null) patient.setDateOfBirth(dateOfBirth);
        if (gender != null) patient.setGender(gender);
        if (phoneNumber != null) patient.setPhoneNumber(phoneNumber);
        if (email != null) patient.setEmail(email);
        if (address != null) patient.setAddress(address);
        if (bloodType != null) patient.setBloodType(bloodType);
        if (allergies != null) patient.setAllergies(allergies);
        
        return true;
    }
    
    /**
     * Delete a patient from the system.
     * 
     * @param id Patient ID to delete
     * @return true if patient was found and deleted, false otherwise
     */
    public boolean deletePatient(int id) {
        return patients.remove(id) != null;
    }
    
    /**
     * Get the total number of patients in the system.
     * 
     * @return Count of patients
     */
    public int getPatientCount() {
        return patients.size();
    }
    
    /**
     * Check if a patient exists by ID.
     * 
     * @param id Patient ID to check
     * @return true if patient exists, false otherwise
     */
    public boolean patientExists(int id) {
        return patients.containsKey(id);
    }
    
    /**
     * Get patients by gender.
     * 
     * @param gender Gender to filter by
     * @return List of patients with matching gender
     */
    public List<Patient> getPatientsByGender(String gender) {
        List<Patient> results = new ArrayList<>();
        String searchGender = gender.toLowerCase();
        
        for (Patient patient : patients.values()) {
            if (patient.getGender().toLowerCase().equals(searchGender)) {
                results.add(patient);
            }
        }
        
        return results;
    }
    
    /**
     * Get patients within a specific age range.
     * 
     * @param minAge Minimum age (inclusive)
     * @param maxAge Maximum age (inclusive)
     * @return List of patients within age range
     */
    public List<Patient> getPatientsByAgeRange(int minAge, int maxAge) {
        List<Patient> results = new ArrayList<>();
        
        for (Patient patient : patients.values()) {
            int age = patient.getAge();
            if (age >= minAge && age <= maxAge) {
                results.add(patient);
            }
        }
        
        return results;
    }
}
