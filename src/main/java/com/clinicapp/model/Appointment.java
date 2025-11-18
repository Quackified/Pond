package com.clinicapp.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Appointment model representing a scheduled appointment in the clinic system.
 * Links patients with doctors at specific dates and times, tracking appointment status.
 */
public class Appointment {
    private static int nextId = 1;
    
    private final int id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    
    /**
     * Enum representing the status of an appointment.
     */
    public enum AppointmentStatus {
        SCHEDULED,
        CONFIRMED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }
    
    /**
     * Constructor for creating a new appointment with auto-generated ID.
     */
    public Appointment(Patient patient, Doctor doctor, LocalDate appointmentDate, 
                      LocalTime startTime, LocalTime endTime, String reason) {
        this.id = nextId++;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = AppointmentStatus.SCHEDULED;
        this.notes = "";
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Convenience constructor that accepts LocalDateTime and calculates default end time.
     * End time is set to 30 minutes after start time by default.
     * 
     * @deprecated Use constructor with explicit startTime and endTime
     */
    @Deprecated
    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String reason) {
        this(patient, doctor, appointmentDateTime.toLocalDate(), 
             appointmentDateTime.toLocalTime(), 
             appointmentDateTime.toLocalTime().plusMinutes(30), 
             reason);
    }
    
    public int getId() {
        return id;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    /**
     * Get appointment date and time as LocalDateTime.
     * Uses the start time for the time component.
     * 
     * @return LocalDateTime combining appointmentDate and startTime
     */
    public LocalDateTime getAppointmentDateTime() {
        return LocalDateTime.of(appointmentDate, startTime);
    }
    
    /**
     * Set appointment date and start time from LocalDateTime.
     * End time is set to 30 minutes after start time.
     * 
     * @param dateTime LocalDateTime to set
     * @deprecated Use setAppointmentDate and setStartTime/setEndTime separately
     */
    @Deprecated
    public void setAppointmentDateTime(LocalDateTime dateTime) {
        this.appointmentDate = dateTime.toLocalDate();
        this.startTime = dateTime.toLocalTime();
        this.endTime = dateTime.toLocalTime().plusMinutes(30);
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("ID: %d | %s %s-%s | Patient: %s | Dr. %s | %s | Status: %s",
                           id, 
                           appointmentDate.format(dateFormatter),
                           startTime.format(timeFormatter),
                           endTime.format(timeFormatter),
                           patient.getName(), 
                           doctor.getName(), 
                           reason, 
                           status);
    }
    
    public String getDetailedInfo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    APPOINTMENT DETAILS                         ║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Appointment ID : %-45d ║\n", id));
        sb.append(String.format("║ Date           : %-45s ║\n", appointmentDate.format(dateFormatter)));
        sb.append(String.format("║ Start Time     : %-45s ║\n", startTime.format(timeFormatter)));
        sb.append(String.format("║ End Time       : %-45s ║\n", endTime.format(timeFormatter)));
        sb.append(String.format("║ Patient        : %-45s ║\n", patient.getName()));
        sb.append(String.format("║ Patient ID     : %-45d ║\n", patient.getId()));
        sb.append(String.format("║ Doctor         : Dr. %-41s ║\n", doctor.getName()));
        sb.append(String.format("║ Doctor ID      : %-45d ║\n", doctor.getId()));
        sb.append(String.format("║ Specialization : %-45s ║\n", doctor.getSpecialization()));
        sb.append(String.format("║ Reason         : %-45s ║\n", reason));
        sb.append(String.format("║ Status         : %-45s ║\n", status));
        if (notes != null && !notes.isEmpty()) {
            sb.append(String.format("║ Notes          : %-45s ║\n", notes));
        }
        sb.append(String.format("║ Created At     : %-45s ║\n", createdAt.format(dateTimeFormatter)));
        sb.append("╚════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
}
