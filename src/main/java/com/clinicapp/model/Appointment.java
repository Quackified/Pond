package com.clinicapp.model;

import java.util.Objects;

/**
 * Represents an appointment in the clinic appointment system.
 * Links a patient with a doctor at a specific date and time with a status.
 */
public class Appointment {
    
    // Unique identifier for the appointment (e.g., "A001", "A002")
    private String appointmentId;
    
    // The patient associated with this appointment
    private Patient patient;
    
    // The doctor assigned to this appointment
    private Doctor doctor;
    
    // Date and time of the appointment in format yyyy-MM-dd HH:mm
    private String appointmentDateTime;
    
    // Current status of the appointment (e.g., "SCHEDULED", "COMPLETED", "CANCELLED", "NO_SHOW")
    private String status;
    
    // Reason for the appointment or chief complaint
    private String reason;
    
    // Additional notes or comments about the appointment
    private String notes;
    
    /**
     * Default constructor for creating an empty Appointment object.
     * Used when appointment details will be set later using setters.
     */
    public Appointment() {
    }
    
    /**
     * Constructor for creating an Appointment with essential details.
     * 
     * @param appointmentId Unique identifier (format: A### where # is a digit)
     * @param patient The patient for this appointment
     * @param doctor The doctor for this appointment
     * @param appointmentDateTime Date and time in format yyyy-MM-dd HH:mm
     * @param status Current status of the appointment
     */
    public Appointment(String appointmentId, Patient patient, Doctor doctor, 
                      String appointmentDateTime, String status) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
    }
    
    /**
     * Full constructor for creating an Appointment with all details.
     * 
     * @param appointmentId Unique identifier (format: A### where # is a digit)
     * @param patient The patient for this appointment
     * @param doctor The doctor for this appointment
     * @param appointmentDateTime Date and time in format yyyy-MM-dd HH:mm
     * @param status Current status of the appointment
     * @param reason Reason for the appointment
     * @param notes Additional notes about the appointment
     */
    public Appointment(String appointmentId, Patient patient, Doctor doctor, 
                      String appointmentDateTime, String status, String reason, String notes) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.reason = reason;
        this.notes = notes;
    }
    
    /**
     * Gets the unique appointment identifier.
     * 
     * @return The appointment ID
     */
    public String getAppointmentId() {
        return appointmentId;
    }
    
    /**
     * Sets the unique appointment identifier.
     * Expected format: A### (e.g., A001, A123)
     * 
     * @param appointmentId The appointment ID to set
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    /**
     * Gets the patient associated with this appointment.
     * 
     * @return The patient object
     */
    public Patient getPatient() {
        return patient;
    }
    
    /**
     * Sets the patient for this appointment.
     * 
     * @param patient The patient to set
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    /**
     * Gets the doctor assigned to this appointment.
     * 
     * @return The doctor object
     */
    public Doctor getDoctor() {
        return doctor;
    }
    
    /**
     * Sets the doctor for this appointment.
     * 
     * @param doctor The doctor to set
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    /**
     * Gets the appointment date and time.
     * 
     * @return The appointment date-time string in format yyyy-MM-dd HH:mm
     */
    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }
    
    /**
     * Sets the appointment date and time.
     * Expected format: yyyy-MM-dd HH:mm (e.g., 2024-03-15 14:30)
     * 
     * @param appointmentDateTime The date-time to set
     */
    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
    
    /**
     * Gets the current status of the appointment.
     * 
     * @return The status string (e.g., "SCHEDULED", "COMPLETED", "CANCELLED")
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets the status of the appointment.
     * Common values: "SCHEDULED", "COMPLETED", "CANCELLED", "NO_SHOW"
     * 
     * @param status The status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Gets the reason for the appointment.
     * 
     * @return The reason or chief complaint
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * Sets the reason for the appointment.
     * 
     * @param reason The reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    /**
     * Gets additional notes about the appointment.
     * 
     * @return The notes string
     */
    public String getNotes() {
        return notes;
    }
    
    /**
     * Sets additional notes about the appointment.
     * 
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Validates if the appointment ID follows the expected format.
     * Expected format: A followed by one or more digits (e.g., A001, A123)
     * 
     * @param appointmentId The appointment ID to validate
     * @return true if the ID format is valid, false otherwise
     */
    public static boolean isValidIdFormat(String appointmentId) {
        if (appointmentId == null || appointmentId.isEmpty()) {
            return false;
        }
        return appointmentId.matches("^A\\d+$");
    }
    
    /**
     * Validates if the appointment date-time follows the expected format.
     * Expected format: yyyy-MM-dd HH:mm (e.g., 2024-03-15 14:30)
     * This performs basic format validation, not date-time validity.
     * 
     * @param dateTime The date-time to validate
     * @return true if the format is valid, false otherwise
     */
    public static boolean isValidDateTimeFormat(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return false;
        }
        return dateTime.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$");
    }
    
    /**
     * Validates if the status is one of the expected values.
     * Valid statuses: SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
     * 
     * @param status The status to validate
     * @return true if the status is valid, false otherwise
     */
    public static boolean isValidStatus(String status) {
        if (status == null || status.isEmpty()) {
            return false;
        }
        return status.equals("SCHEDULED") || status.equals("COMPLETED") || 
               status.equals("CANCELLED") || status.equals("NO_SHOW");
    }
    
    /**
     * Returns a string representation of the appointment for console output.
     * Displays all appointment information in a readable format.
     * 
     * @return Formatted string containing all appointment details
     */
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patient=" + (patient != null ? patient.getName() + " (ID: " + patient.getPatientId() + ")" : "null") +
                ", doctor=" + (doctor != null ? doctor.getName() + " (ID: " + doctor.getDoctorId() + ")" : "null") +
                ", appointmentDateTime='" + appointmentDateTime + '\'' +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
    
    /**
     * Compares this appointment with another object for equality.
     * Two appointments are considered equal if they have the same appointment ID.
     * 
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(appointmentId, that.appointmentId);
    }
    
    /**
     * Generates a hash code for the appointment based on the appointment ID.
     * Used in hash-based collections like HashMap and HashSet.
     * 
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}
