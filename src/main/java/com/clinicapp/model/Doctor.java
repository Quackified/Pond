package com.clinicapp.model;

import java.util.Objects;

/**
 * Represents a doctor in the clinic appointment system.
 * Contains doctor identification, contact information, specialization, and schedule.
 */
public class Doctor {
    
    // Unique identifier for the doctor (e.g., "D001", "D002")
    private String doctorId;
    
    // Full name of the doctor
    private String name;
    
    // Medical specialization (e.g., "Cardiology", "Pediatrics", "General Practice")
    private String specialization;
    
    // Contact phone number of the doctor
    private String phoneNumber;
    
    // Email address of the doctor
    private String email;
    
    // Working schedule description (e.g., "Monday-Friday 9:00-17:00")
    private String schedule;
    
    // Office or room number where the doctor practices
    private String officeRoom;
    
    /**
     * Default constructor for creating an empty Doctor object.
     * Used when doctor details will be set later using setters.
     */
    public Doctor() {
    }
    
    /**
     * Full constructor for creating a Doctor with all details.
     * 
     * @param doctorId Unique identifier for the doctor (format: D### where # is a digit)
     * @param name Full name of the doctor
     * @param specialization Medical specialization field
     * @param phoneNumber Contact phone number
     * @param email Email address
     * @param schedule Working schedule description
     * @param officeRoom Office or room number
     */
    public Doctor(String doctorId, String name, String specialization, String phoneNumber,
                  String email, String schedule, String officeRoom) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.schedule = schedule;
        this.officeRoom = officeRoom;
    }
    
    /**
     * Gets the unique doctor identifier.
     * 
     * @return The doctor ID
     */
    public String getDoctorId() {
        return doctorId;
    }
    
    /**
     * Sets the unique doctor identifier.
     * Expected format: D### (e.g., D001, D123)
     * 
     * @param doctorId The doctor ID to set
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    
    /**
     * Gets the doctor's full name.
     * 
     * @return The doctor's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the doctor's full name.
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the doctor's medical specialization.
     * 
     * @return The specialization field
     */
    public String getSpecialization() {
        return specialization;
    }
    
    /**
     * Sets the doctor's medical specialization.
     * Examples: "Cardiology", "Pediatrics", "General Practice"
     * 
     * @param specialization The specialization to set
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    /**
     * Gets the doctor's phone number.
     * 
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the doctor's phone number.
     * 
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the doctor's email address.
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the doctor's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the doctor's working schedule.
     * 
     * @return The schedule description
     */
    public String getSchedule() {
        return schedule;
    }
    
    /**
     * Sets the doctor's working schedule.
     * Example format: "Monday-Friday 9:00-17:00"
     * 
     * @param schedule The schedule to set
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    /**
     * Gets the doctor's office or room number.
     * 
     * @return The office room number
     */
    public String getOfficeRoom() {
        return officeRoom;
    }
    
    /**
     * Sets the doctor's office or room number.
     * 
     * @param officeRoom The office room to set
     */
    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }
    
    /**
     * Validates if the doctor ID follows the expected format.
     * Expected format: D followed by one or more digits (e.g., D001, D123)
     * 
     * @param doctorId The doctor ID to validate
     * @return true if the ID format is valid, false otherwise
     */
    public static boolean isValidIdFormat(String doctorId) {
        if (doctorId == null || doctorId.isEmpty()) {
            return false;
        }
        return doctorId.matches("^D\\d+$");
    }
    
    /**
     * Validates if the email follows a basic email format.
     * 
     * @param email The email to validate
     * @return true if the email format is valid, false otherwise
     */
    public static boolean isValidEmailFormat(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Returns a string representation of the doctor for console output.
     * Displays all doctor information in a readable format.
     * 
     * @return Formatted string containing all doctor details
     */
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", schedule='" + schedule + '\'' +
                ", officeRoom='" + officeRoom + '\'' +
                '}';
    }
    
    /**
     * Compares this doctor with another object for equality.
     * Two doctors are considered equal if they have the same doctor ID.
     * 
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }
    
    /**
     * Generates a hash code for the doctor based on the doctor ID.
     * Used in hash-based collections like HashMap and HashSet.
     * 
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }
}
