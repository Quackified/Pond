package com.clinicapp.service;

import com.clinicapp.model.Patient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PatientManager handles all CRUD operations for patients.
 * 
 * Data Structure: ArrayList<Patient>
 * - ArrayList provides O(1) access by index for fast retrieval
 * - Dynamic resizing allows for flexible patient list management
 * - Sequential access is efficient for displaying all patients
 * 
 * Requirements covered: 1-5 (CRUD and search operations for patients)
 */
public class PatientManager {
    
    /**
     * Main storage for all registered patients.
     * ArrayList is chosen for its dynamic sizing and fast index-based access.
     */
    private ArrayList<Patient> patients;
    
    public PatientManager() {
        this.patients = new ArrayList<>();
    }
    
    /**
     * Adds a new patient to the system.
     * 
     * @param patient The patient to add
     * @return true if patient was added successfully, false if patient with same ID already exists
     */
    public boolean addPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Error: Cannot add null patient");
            return false;
        }
        
        if (findPatientById(patient.getPatientId()) != null) {
            System.out.println("Error: Patient with ID " + patient.getPatientId() + " already exists");
            return false;
        }
        
        patients.add(patient);
        System.out.println("Success: Patient " + patient.getName() + " added successfully");
        return true;
    }
    
    /**
     * Retrieves a patient by their unique ID.
     * 
     * @param patientId The ID of the patient to find
     * @return The patient if found, null otherwise
     */
    public Patient findPatientById(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return null;
        }
        
        for (Patient patient : patients) {
            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }
        return null;
    }
    
    /**
     * Searches for patients by name (partial match, case-insensitive).
     * 
     * @param name The name or partial name to search for
     * @return List of patients matching the search criteria
     */
    public List<Patient> searchPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = name.toLowerCase();
        return patients.stream()
                .filter(patient -> patient.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Searches for patients by phone number (partial match).
     * 
     * @param phoneNumber The phone number or partial phone number to search for
     * @return List of patients matching the search criteria
     */
    public List<Patient> searchPatientsByPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return patients.stream()
                .filter(patient -> patient.getPhoneNumber().contains(phoneNumber))
                .collect(Collectors.toList());
    }
    
    /**
     * Updates an existing patient's information.
     * 
     * @param patientId The ID of the patient to update
     * @param updatedPatient The patient object with updated information
     * @return true if update was successful, false if patient not found
     */
    public boolean updatePatient(String patientId, Patient updatedPatient) {
        if (patientId == null || updatedPatient == null) {
            System.out.println("Error: Invalid parameters for update");
            return false;
        }
        
        Patient existingPatient = findPatientById(patientId);
        if (existingPatient == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found");
            return false;
        }
        
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setAddress(updatedPatient.getAddress());
        existingPatient.setAge(updatedPatient.getAge());
        
        System.out.println("Success: Patient " + patientId + " updated successfully");
        return true;
    }
    
    /**
     * Deletes a patient from the system.
     * 
     * @param patientId The ID of the patient to delete
     * @return true if deletion was successful, false if patient not found
     */
    public boolean deletePatient(String patientId) {
        if (patientId == null) {
            System.out.println("Error: Patient ID cannot be null");
            return false;
        }
        
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found");
            return false;
        }
        
        patients.remove(patient);
        System.out.println("Success: Patient " + patientId + " deleted successfully");
        return true;
    }
    
    /**
     * Retrieves all patients in the system.
     * 
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }
    
    /**
     * Displays all patients in a formatted manner.
     */
    public void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered in the system.");
            return;
        }
        
        System.out.println("\n===== Patient List =====");
        System.out.println(String.format("%-10s %-20s %-15s %-25s %-5s", 
                "ID", "Name", "Phone", "Email", "Age"));
        System.out.println("------------------------------------------------------------------------------");
        
        for (Patient patient : patients) {
            System.out.println(String.format("%-10s %-20s %-15s %-25s %-5d", 
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getPhoneNumber(),
                    patient.getEmail(),
                    patient.getAge()));
        }
        System.out.println("Total patients: " + patients.size());
    }
    
    /**
     * Gets the total count of registered patients.
     * 
     * @return The number of patients in the system
     */
    public int getPatientCount() {
        return patients.size();
    }
    
    /**
     * Clears all patients from the system (useful for testing).
     */
    public void clearAllPatients() {
        patients.clear();
        System.out.println("All patients cleared from the system");
    }
}
