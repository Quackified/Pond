package com.clinicapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Doctor {
    private String doctorId;
    private String name;
    private String specialization;
    private String phoneNumber;
    private List<String> availableTimeSlots;
    
    public Doctor(String doctorId, String name, String specialization, String phoneNumber) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.availableTimeSlots = new ArrayList<>();
    }
    
    public String getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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
    
    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }
    
    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
    
    public void addAvailableTimeSlot(String timeSlot) {
        if (!this.availableTimeSlots.contains(timeSlot)) {
            this.availableTimeSlots.add(timeSlot);
        }
    }
    
    public void removeAvailableTimeSlot(String timeSlot) {
        this.availableTimeSlots.remove(timeSlot);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", availableTimeSlots=" + availableTimeSlots +
                '}';
    }
}
