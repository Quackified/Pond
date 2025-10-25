package com.clinicapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor model representing a medical professional in the clinic system.
 * Contains doctor's specialization, availability, and contact information.
 */
public class Doctor {
    private static int nextId = 1;
    
    private final int id;
    private String name;
    private String specialization;
    private String phoneNumber;
    private String email;
    private List<String> availableDays; // Days of the week (e.g., "Monday", "Tuesday")
    private String startTime; // e.g., "09:00"
    private String endTime;   // e.g., "17:00"
    private boolean isAvailable;
    
    /**
     * Constructor for creating a new doctor with auto-generated ID.
     */
    public Doctor(String name, String specialization, String phoneNumber, 
                  String email, List<String> availableDays, String startTime, 
                  String endTime) {
        this.id = nextId++;
        this.name = name;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.availableDays = availableDays != null ? new ArrayList<>(availableDays) : new ArrayList<>();
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<String> getAvailableDays() {
        return new ArrayList<>(availableDays);
    }
    
    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays != null ? new ArrayList<>(availableDays) : new ArrayList<>();
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    /**
     * Get formatted display string for doctor information.
     */
    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Unavailable";
        return String.format("ID: %d | Dr. %s | %s | %s | Phone: %s",
                           id, name, specialization, status, phoneNumber);
    }
    
    /**
     * Get detailed doctor information for display.
     */
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                      DOCTOR DETAILS                            ║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Doctor ID      : %-45d ║\n", id));
        sb.append(String.format("║ Name           : Dr. %-41s ║\n", name));
        sb.append(String.format("║ Specialization : %-45s ║\n", specialization));
        sb.append(String.format("║ Phone Number   : %-45s ║\n", phoneNumber));
        sb.append(String.format("║ Email          : %-45s ║\n", email != null ? email : "N/A"));
        sb.append(String.format("║ Status         : %-45s ║\n", isAvailable ? "Available" : "Unavailable"));
        sb.append(String.format("║ Working Hours  : %s - %-36s ║\n", startTime, endTime));
        sb.append(String.format("║ Available Days : %-45s ║\n", String.join(", ", availableDays)));
        sb.append("╚════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
}
