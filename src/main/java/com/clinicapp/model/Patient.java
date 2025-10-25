package com.clinicapp.model;

import java.util.Objects;

public class Patient {
    private String patientId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private int age;
    
    public Patient(String patientId, String name, String phoneNumber, String email, String address, int age) {
        this.patientId = patientId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.age = age;
    }
    
    public Patient(String patientId, String name, String phoneNumber) {
        this(patientId, name, phoneNumber, "", "", 0);
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
