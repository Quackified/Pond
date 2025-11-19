package com.clinicapp.model;

import com.clinicapp.util.DateUtils;

import java.time.LocalDate;

/**
 * Patient model representing a patient in the clinic system.
 * Contains patient demographics and contact information.
 */
public class Patient {
    private static int nextId = 1;
    
    private final int id;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private String bloodType;
    private String allergies;
    
    /**
     * Constructor for creating a new patient with auto-generated ID.
     */
    public Patient(String name, LocalDate dateOfBirth, String gender, 
                   String phoneNumber, String email, String address, 
                   String bloodType, String allergies) {
        this.id = nextId++;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.bloodType = bloodType;
        this.allergies = allergies;
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
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getBloodType() {
        return bloodType;
    }
    
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    public String getAllergies() {
        return allergies;
    }
    
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    /**
     * Calculate patient's age based on date of birth.
     */
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    /**
     * Get formatted display string for patient information.
     */
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | DOB: %s | Age: %d | Gender: %s | Phone: %s",
                           id, name, DateUtils.formatDateCompact(dateOfBirth), getAge(), gender, phoneNumber);
    }
    
    /**
     * Get detailed patient information for display.
     */
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                      PATIENT DETAILS                           ║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Patient ID    : %-45d ║\n", id));
        sb.append(String.format("║ Name          : %-45s ║\n", name));
        sb.append(String.format("║ Date of Birth : %-45s ║\n", DateUtils.formatDateCompact(dateOfBirth)));
        sb.append(String.format("║ Age           : %-45d ║\n", getAge()));
        sb.append(String.format("║ Gender        : %-45s ║\n", gender));
        sb.append(String.format("║ Phone Number  : %-45s ║\n", phoneNumber));
        sb.append(String.format("║ Email         : %-45s ║\n", email != null ? email : "N/A"));
        sb.append(String.format("║ Address       : %-45s ║\n", address != null ? address : "N/A"));
        sb.append(String.format("║ Blood Type    : %-45s ║\n", bloodType != null ? bloodType : "N/A"));
        sb.append(String.format("║ Allergies     : %-45s ║\n", allergies != null ? allergies : "None"));
        sb.append("╚════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
}
