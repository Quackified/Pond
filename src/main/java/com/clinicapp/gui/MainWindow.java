package com.clinicapp.gui;

import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;
import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * MainWindow is the main entry point for the Clinic Appointment Management System GUI.
 * Provides a tabbed interface for managing patients, doctors, and appointments.
 */
public class MainWindow extends JFrame {
    
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;
    private final AppointmentManager appointmentManager;
    
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    
    public MainWindow() {
        setTitle("Clinic Appointment Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Initialize managers
        this.patientManager = new PatientManager();
        this.doctorManager = new DoctorManager();
        this.appointmentManager = new AppointmentManager(patientManager, doctorManager);
        
        // Load sample data
        loadSampleData();
        
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create panels
        this.patientPanel = new PatientPanel(patientManager, appointmentManager);
        this.doctorPanel = new DoctorPanel(doctorManager);
        this.appointmentPanel = new AppointmentPanel(patientManager, doctorManager, appointmentManager);
        
        // Add tabs
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Doctors", doctorPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        
        add(tabbedPane);
    }
    
    private void loadSampleData() {
        LocalDate date1 = LocalDate.of(1990, 5, 15);
        LocalDate date2 = LocalDate.of(1985, 8, 22);
        LocalDate date3 = LocalDate.of(1995, 3, 10);
        
        Patient p1 = patientManager.addPatient("John Smith", date1, "Male", 
                                              "5551234567", "john@example.com", 
                                              "123 Main St", "O+", "None");
        Patient p2 = patientManager.addPatient("Jane Doe", date2, "Female", 
                                              "5559876543", "jane@example.com", 
                                              "456 Oak Ave", "A+", "Pollen");
        Patient p3 = patientManager.addPatient("Bob Johnson", date3, "Male", 
                                              "5555551234", "bob@example.com", 
                                              "789 Pine St", "B+", "None");
        
        List<String> days1 = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        List<String> days2 = Arrays.asList("Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        List<String> days3 = Arrays.asList("Monday", "Wednesday", "Friday");
        
        Doctor d1 = doctorManager.addDoctor("Williams", "General Practice", 
                                           "5551111111", "williams@clinic.com", 
                                           days1, "09:00", "17:00");
        Doctor d2 = doctorManager.addDoctor("Johnson", "Cardiology", 
                                           "5552222222", "johnson@clinic.com", 
                                           days2, "10:00", "18:00");
        Doctor d3 = doctorManager.addDoctor("Smith", "Pediatrics", 
                                           "5553333333", "smith@clinic.com", 
                                           days3, "08:00", "16:00");
        
        LocalDate appointmentDate = LocalDate.now().plusDays(7);
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(14, 30);
        appointmentManager.scheduleAppointment(p1, d1, appointmentDate, startTime, endTime, "Annual checkup");
        
        LocalTime startTime2 = LocalTime.of(15, 0);
        LocalTime endTime2 = LocalTime.of(15, 30);
        appointmentManager.scheduleAppointment(p2, d2, appointmentDate, startTime2, endTime2, "Heart consultation");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow frame = new MainWindow();
            frame.setVisible(true);
        });
    }
}
