package com.clinicapp.service;

import com.clinicapp.model.Doctor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DoctorManager handles all CRUD operations for doctors and manages their availability schedules.
 * 
 * Data Structure: ArrayList<Doctor>
 * - ArrayList provides O(1) access by index for fast retrieval
 * - Dynamic resizing allows for flexible doctor list management
 * - Each Doctor object maintains its own List<String> for available time slots
 * 
 * Requirements covered: 6-10 (Doctor management), 15 (Schedule lookup)
 */
public class DoctorManager {
    
    /**
     * Main storage for all registered doctors.
     * ArrayList is chosen for its dynamic sizing and fast index-based access.
     */
    private ArrayList<Doctor> doctors;
    
    public DoctorManager() {
        this.doctors = new ArrayList<>();
    }
    
    /**
     * Adds a new doctor to the system.
     * 
     * @param doctor The doctor to add
     * @return true if doctor was added successfully, false if doctor with same ID already exists
     */
    public boolean addDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("Error: Cannot add null doctor");
            return false;
        }
        
        if (findDoctorById(doctor.getDoctorId()) != null) {
            System.out.println("Error: Doctor with ID " + doctor.getDoctorId() + " already exists");
            return false;
        }
        
        doctors.add(doctor);
        System.out.println("Success: Doctor " + doctor.getName() + " added successfully");
        return true;
    }
    
    /**
     * Retrieves a doctor by their unique ID.
     * 
     * @param doctorId The ID of the doctor to find
     * @return The doctor if found, null otherwise
     */
    public Doctor findDoctorById(String doctorId) {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            return null;
        }
        
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return doctor;
            }
        }
        return null;
    }
    
    /**
     * Searches for doctors by name (partial match, case-insensitive).
     * 
     * @param name The name or partial name to search for
     * @return List of doctors matching the search criteria
     */
    public List<Doctor> searchDoctorsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = name.toLowerCase();
        return doctors.stream()
                .filter(doctor -> doctor.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Searches for doctors by specialization (partial match, case-insensitive).
     * 
     * @param specialization The specialization to search for
     * @return List of doctors matching the specialization
     */
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = specialization.toLowerCase();
        return doctors.stream()
                .filter(doctor -> doctor.getSpecialization().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Updates an existing doctor's information.
     * 
     * @param doctorId The ID of the doctor to update
     * @param updatedDoctor The doctor object with updated information
     * @return true if update was successful, false if doctor not found
     */
    public boolean updateDoctor(String doctorId, Doctor updatedDoctor) {
        if (doctorId == null || updatedDoctor == null) {
            System.out.println("Error: Invalid parameters for update");
            return false;
        }
        
        Doctor existingDoctor = findDoctorById(doctorId);
        if (existingDoctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return false;
        }
        
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
        
        System.out.println("Success: Doctor " + doctorId + " updated successfully");
        return true;
    }
    
    /**
     * Deletes a doctor from the system.
     * 
     * @param doctorId The ID of the doctor to delete
     * @return true if deletion was successful, false if doctor not found
     */
    public boolean deleteDoctor(String doctorId) {
        if (doctorId == null) {
            System.out.println("Error: Doctor ID cannot be null");
            return false;
        }
        
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return false;
        }
        
        doctors.remove(doctor);
        System.out.println("Success: Doctor " + doctorId + " deleted successfully");
        return true;
    }
    
    /**
     * Retrieves all doctors in the system.
     * 
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }
    
    /**
     * Adds an available time slot to a doctor's schedule.
     * 
     * @param doctorId The ID of the doctor
     * @param timeSlot The time slot to add (e.g., "2024-01-15 09:00")
     * @return true if time slot was added successfully, false otherwise
     */
    public boolean addAvailableTimeSlot(String doctorId, String timeSlot) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return false;
        }
        
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            System.out.println("Error: Time slot cannot be empty");
            return false;
        }
        
        if (doctor.getAvailableTimeSlots().contains(timeSlot)) {
            System.out.println("Warning: Time slot " + timeSlot + " already exists for doctor " + doctor.getName());
            return false;
        }
        
        doctor.addAvailableTimeSlot(timeSlot);
        System.out.println("Success: Time slot " + timeSlot + " added for doctor " + doctor.getName());
        return true;
    }
    
    /**
     * Removes an available time slot from a doctor's schedule.
     * 
     * @param doctorId The ID of the doctor
     * @param timeSlot The time slot to remove
     * @return true if time slot was removed successfully, false otherwise
     */
    public boolean removeAvailableTimeSlot(String doctorId, String timeSlot) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return false;
        }
        
        if (!doctor.getAvailableTimeSlots().contains(timeSlot)) {
            System.out.println("Warning: Time slot " + timeSlot + " does not exist for doctor " + doctor.getName());
            return false;
        }
        
        doctor.removeAvailableTimeSlot(timeSlot);
        System.out.println("Success: Time slot " + timeSlot + " removed for doctor " + doctor.getName());
        return true;
    }
    
    /**
     * Retrieves all available time slots for a specific doctor.
     * 
     * @param doctorId The ID of the doctor
     * @return List of available time slots, or empty list if doctor not found
     */
    public List<String> getDoctorAvailableSlots(String doctorId) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return new ArrayList<>();
        }
        
        return new ArrayList<>(doctor.getAvailableTimeSlots());
    }
    
    /**
     * Checks if a doctor is available at a specific time slot.
     * 
     * @param doctorId The ID of the doctor
     * @param timeSlot The time slot to check
     * @return true if doctor is available, false otherwise
     */
    public boolean isDoctorAvailable(String doctorId, String timeSlot) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            return false;
        }
        
        return doctor.getAvailableTimeSlots().contains(timeSlot);
    }
    
    /**
     * Finds all doctors available at a specific time slot.
     * 
     * @param timeSlot The time slot to check
     * @return List of doctors available at that time
     */
    public List<Doctor> findDoctorsAvailableAt(String timeSlot) {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return doctors.stream()
                .filter(doctor -> doctor.getAvailableTimeSlots().contains(timeSlot))
                .collect(Collectors.toList());
    }
    
    /**
     * Displays all doctors with their information and available time slots.
     */
    public void displayAllDoctors() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered in the system.");
            return;
        }
        
        System.out.println("\n===== Doctor List =====");
        for (Doctor doctor : doctors) {
            System.out.println("Doctor ID: " + doctor.getDoctorId());
            System.out.println("Name: " + doctor.getName());
            System.out.println("Specialization: " + doctor.getSpecialization());
            System.out.println("Phone: " + doctor.getPhoneNumber());
            System.out.println("Available Time Slots: " + 
                    (doctor.getAvailableTimeSlots().isEmpty() ? "None" : 
                     String.join(", ", doctor.getAvailableTimeSlots())));
            System.out.println("------------------------");
        }
        System.out.println("Total doctors: " + doctors.size());
    }
    
    /**
     * Displays available time slots for a specific doctor.
     * 
     * @param doctorId The ID of the doctor
     */
    public void displayDoctorSchedule(String doctorId) {
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return;
        }
        
        System.out.println("\n===== Schedule for Dr. " + doctor.getName() + " =====");
        System.out.println("Specialization: " + doctor.getSpecialization());
        System.out.println("Available Time Slots:");
        
        if (doctor.getAvailableTimeSlots().isEmpty()) {
            System.out.println("  No available time slots");
        } else {
            for (int i = 0; i < doctor.getAvailableTimeSlots().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + doctor.getAvailableTimeSlots().get(i));
            }
        }
    }
    
    /**
     * Gets the total count of registered doctors.
     * 
     * @return The number of doctors in the system
     */
    public int getDoctorCount() {
        return doctors.size();
    }
    
    /**
     * Clears all doctors from the system (useful for testing).
     */
    public void clearAllDoctors() {
        doctors.clear();
        System.out.println("All doctors cleared from the system");
    }
}
