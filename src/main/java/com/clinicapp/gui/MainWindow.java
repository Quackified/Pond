package com.clinicapp.gui;

import com.clinicapp.service.PatientManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.storage.JsonStorage;
import com.clinicapp.storage.CsvExporter;
import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Appointment;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {
    private PatientManager patientManager;
    private DoctorManager doctorManager;
    private AppointmentManager appointmentManager;
    private JsonStorage jsonStorage;
    private CsvExporter csvExporter;
    
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    
    public MainWindow() {
        patientManager = new PatientManager();
        doctorManager = new DoctorManager();
        appointmentManager = new AppointmentManager(patientManager, doctorManager);
        jsonStorage = new JsonStorage();
        csvExporter = new CsvExporter();
        
        setTitle("Clinic Appointment Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createMenuBar();
        createTabbedPane();
        
        loadData();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveData();
            }
        });
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        saveItem.addActionListener(e -> saveData());
        loadItem.addActionListener(e -> loadData());
        exitItem.addActionListener(e -> {
            saveData();
            System.exit(0);
        });
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu exportMenu = new JMenu("Export");
        JMenuItem exportPatientsItem = new JMenuItem("Export Patients to CSV");
        JMenuItem exportDoctorsItem = new JMenuItem("Export Doctors to CSV");
        JMenuItem exportAppointmentsItem = new JMenuItem("Export Appointments to CSV");
        
        exportPatientsItem.addActionListener(e -> exportPatients());
        exportDoctorsItem.addActionListener(e -> exportDoctors());
        exportAppointmentsItem.addActionListener(e -> exportAppointments());
        
        exportMenu.add(exportPatientsItem);
        exportMenu.add(exportDoctorsItem);
        exportMenu.add(exportAppointmentsItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(exportMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        patientPanel = new PatientPanel(patientManager);
        doctorPanel = new DoctorPanel(doctorManager);
        appointmentPanel = new AppointmentPanel(appointmentManager, patientManager, doctorManager);
        
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Doctors", doctorPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void saveData() {
        try {
            List<Patient> patients = patientManager.getAllPatients();
            List<Doctor> doctors = doctorManager.getAllDoctors();
            List<Appointment> appointments = appointmentManager.getAllAppointments();
            
            jsonStorage.savePatients(patients);
            jsonStorage.saveDoctors(doctors);
            jsonStorage.saveAppointments(appointments, patients, doctors);
            
            JOptionPane.showMessageDialog(this, "Data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData() {
        try {
            List<Patient> patients = jsonStorage.loadPatients();
            List<Doctor> doctors = jsonStorage.loadDoctors();
            
            for (Patient patient : patients) {
                patientManager.addPatient(
                    patient.getName(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    patient.getPhoneNumber(),
                    patient.getEmail(),
                    patient.getAddress(),
                    patient.getBloodType(),
                    patient.getAllergies()
                );
            }
            
            for (Doctor doctor : doctors) {
                Doctor newDoctor = doctorManager.addDoctor(
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getPhoneNumber(),
                    doctor.getEmail(),
                    doctor.getAvailableDays(),
                    doctor.getStartTime(),
                    doctor.getEndTime()
                );
                newDoctor.setAvailable(doctor.isAvailable());
            }
            
            List<Appointment> appointments = jsonStorage.loadAppointments(
                patientManager.getAllPatients(),
                doctorManager.getAllDoctors()
            );
            
            for (Appointment apt : appointments) {
                Appointment newApt = appointmentManager.scheduleAppointment(
                    apt.getPatient(),
                    apt.getDoctor(),
                    apt.getAppointmentDateTime(),
                    apt.getReason()
                );
                if (newApt != null) {
                    newApt.setStatus(apt.getStatus());
                    newApt.setNotes(apt.getNotes());
                }
            }
            
            patientPanel.refreshTable();
            doctorPanel.refreshTable();
            appointmentPanel.refreshTable();
            
        } catch (Exception e) {
            System.out.println("No existing data found or error loading: " + e.getMessage());
        }
    }
    
    private void exportPatients() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename (without extension):", "patients");
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                List<Patient> patients = patientManager.getAllPatients();
                csvExporter.exportPatients(patients, filename + ".csv");
                JOptionPane.showMessageDialog(this, "Patients exported successfully to exports/" + filename + ".csv", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting patients: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportDoctors() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename (without extension):", "doctors");
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                List<Doctor> doctors = doctorManager.getAllDoctors();
                csvExporter.exportDoctors(doctors, filename + ".csv");
                JOptionPane.showMessageDialog(this, "Doctors exported successfully to exports/" + filename + ".csv", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting doctors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportAppointments() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename (without extension):", "appointments");
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                List<Appointment> appointments = appointmentManager.getAllAppointments();
                csvExporter.exportAppointments(appointments, filename + ".csv");
                JOptionPane.showMessageDialog(this, "Appointments exported successfully to exports/" + filename + ".csv", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting appointments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAbout() {
        String message = "Clinic Appointment Management System\n\n" +
                        "Version 2.0\n\n" +
                        "A simple application for managing clinic appointments,\n" +
                        "patients, and doctors.\n\n" +
                        "Features:\n" +
                        "- Patient Management\n" +
                        "- Doctor Management\n" +
                        "- Appointment Scheduling\n" +
                        "- JSON File Storage\n" +
                        "- CSV Export";
        
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
