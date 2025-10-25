package com.clinicapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Appointment {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private String status;
    private String notes;
    
    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_RESCHEDULED = "RESCHEDULED";
    
    public Appointment(String appointmentId, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = STATUS_SCHEDULED;
        this.notes = "";
    }
    
    public Appointment(String appointmentId, Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String status) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.notes = "";
    }
    
    public String getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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
    
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }
    
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(appointmentId, that.appointmentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", patient=" + (patient != null ? patient.getName() : "null") +
                ", doctor=" + (doctor != null ? doctor.getName() : "null") +
                ", appointmentDateTime=" + (appointmentDateTime != null ? appointmentDateTime.format(formatter) : "null") +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
