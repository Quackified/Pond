package com.clinicapp.model;

import java.util.Objects;

/**
 * Represents a patient in the clinic appointment system.
 * Contains patient identification, contact information, and basic details.
 */
public class Patient {
    
    // Unique identifier for the patient (e.g., "P001", "P002")
    private String patientId;
    
    // Full name of the patient
    private String name;
    
    // Contact phone number of the patient
    private String phoneNumber;
    
    // Email address of the patient
    private String email;
    
    // Patient's date of birth in format yyyy-MM-dd
    private String dateOfBirth;
    
    // Patient's address
    private String address;
    
    /**
     * Default constructor for creating an empty Patient object.
     * Used when patient details will be set later using setters.
     */
    public Patient() {
    }
    
    /**
     * Full constructor for creating a Patient with all details.
     * 
     * @param patientId Unique identifier for the patient (format: P### where # is a digit)
     * @param name Full name of the patient
     * @param phoneNumber Contact phone number
     * @param email Email address
     * @param dateOfBirth Date of birth in format yyyy-MM-dd
     * @param address Physical address
     */
    public Patient(String patientId, String name, String phoneNumber, String email, 
                   String dateOfBirth, String address) {
        this.patientId = patientId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
    
    /**
     * Gets the unique patient identifier.
     * 
     * @return The patient ID
     */
    public String getPatientId() {
        return patientId;
    }
    
    /**
     * Sets the unique patient identifier.
     * Expected format: P### (e.g., P001, P123)
     * 
     * @param patientId The patient ID to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    /**
     * Gets the patient's full name.
     * 
     * @return The patient's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the patient's full name.
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the patient's phone number.
     * 
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the patient's phone number.
     * 
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the patient's email address.
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the patient's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the patient's date of birth.
     * 
     * @return The date of birth in format yyyy-MM-dd
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    /**
     * Sets the patient's date of birth.
     * Expected format: yyyy-MM-dd (e.g., 1990-05-15)
     * 
     * @param dateOfBirth The date of birth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /**
     * Gets the patient's address.
     * 
     * @return The address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Sets the patient's address.
     * 
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Validates if the patient ID follows the expected format.
     * Expected format: P followed by one or more digits (e.g., P001, P123)
     * 
     * @param patientId The patient ID to validate
     * @return true if the ID format is valid, false otherwise
     */
    public static boolean isValidIdFormat(String patientId) {
        if (patientId == null || patientId.isEmpty()) {
            return false;
        }
        return patientId.matches("^P\\d+$");
    }
    
    /**
     * Validates if the date of birth follows the expected format.
     * Expected format: yyyy-MM-dd (e.g., 1990-05-15)
     * This performs basic format validation, not date validity.
     * 
     * @param dateOfBirth The date of birth to validate
     * @return true if the format is valid, false otherwise
     */
    public static boolean isValidDateFormat(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return false;
        }
        return dateOfBirth.matches("^\\d{4}-\\d{2}-\\d{2}$");
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
     * Returns a string representation of the patient for console output.
     * Displays all patient information in a readable format.
     * 
     * @return Formatted string containing all patient details
     */
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
    
    /**
     * Compares this patient with another object for equality.
     * Two patients are considered equal if they have the same patient ID.
     * 
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }
    
    /**
     * Generates a hash code for the patient based on the patient ID.
     * Used in hash-based collections like HashMap and HashSet.
     * 
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }
}
