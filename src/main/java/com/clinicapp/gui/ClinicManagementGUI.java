package com.clinicapp.gui;

import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;
import com.clinicapp.storage.CsvExporter;
import com.clinicapp.storage.JsonStorage;
import javax.swing.*;
import java.awt.*;

/**
 * Main GUI window for the Clinic Management System.
 * Provides tabbed interface for managing patients, doctors, and appointments.
 */
public class ClinicManagementGUI extends JFrame {
    
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;
    private final AppointmentManager appointmentManager;
    private final JsonStorage jsonStorage;
    private final CsvExporter csvExporter;
    
    private JTabbedPane tabbedPane;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    
    public ClinicManagementGUI() {
        this.patientManager = new PatientManager();
        this.doctorManager = new DoctorManager();
        this.appointmentManager = new AppointmentManager(patientManager, doctorManager);
        this.jsonStorage = new JsonStorage();
        this.csvExporter = new CsvExporter();
        
        initializeUI();
        loadData();
        setupWindowListeners();
    }
    
    private void initializeUI() {
        setTitle("Clinic Appointment Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createMenuBar();
        
        tabbedPane = new JTabbedPane();
        
        patientPanel = new PatientPanel(patientManager);
        doctorPanel = new DoctorPanel(doctorManager);
        appointmentPanel = new AppointmentPanel(appointmentManager, patientManager, doctorManager);
        
        tabbedPane.addTab("Patients", new ImageIcon(), patientPanel, "Manage Patients");
        tabbedPane.addTab("Doctors", new ImageIcon(), doctorPanel, "Manage Doctors");
        tabbedPane.addTab("Appointments", new ImageIcon(), appointmentPanel, "Manage Appointments");
        
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(new JLabel("Ready"));
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        saveItem.addActionListener(e -> saveData());
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(saveItem);
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
    
    private void setupWindowListeners() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    private void loadData() {
        // Load data from JSON files
        // Note: This is a placeholder - in a real implementation,
        // we'd need to handle ID restoration properly
    }
    
    private void saveData() {
        jsonStorage.savePatients(patientManager.getAllPatients());
        jsonStorage.saveDoctors(doctorManager.getAllDoctors());
        jsonStorage.saveAppointments(appointmentManager.getAllAppointments());
        JOptionPane.showMessageDialog(this, "Data saved successfully!", 
            "Save", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportPatients() {
        String filename = JOptionPane.showInputDialog(this, 
            "Enter filename for patients export:", "patients.csv");
        if (filename != null && !filename.trim().isEmpty()) {
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }
            if (csvExporter.exportPatients(patientManager.getAllPatients(), filename)) {
                JOptionPane.showMessageDialog(this, 
                    "Patients exported successfully to exports/" + filename, 
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to export patients!", 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportDoctors() {
        String filename = JOptionPane.showInputDialog(this, 
            "Enter filename for doctors export:", "doctors.csv");
        if (filename != null && !filename.trim().isEmpty()) {
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }
            if (csvExporter.exportDoctors(doctorManager.getAllDoctors(), filename)) {
                JOptionPane.showMessageDialog(this, 
                    "Doctors exported successfully to exports/" + filename, 
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to export doctors!", 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportAppointments() {
        String filename = JOptionPane.showInputDialog(this, 
            "Enter filename for appointments export:", "appointments.csv");
        if (filename != null && !filename.trim().isEmpty()) {
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }
            if (csvExporter.exportAppointments(appointmentManager.getAllAppointments(), filename)) {
                JOptionPane.showMessageDialog(this, 
                    "Appointments exported successfully to exports/" + filename, 
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to export appointments!", 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Clinic Appointment Management System\n" +
            "Version 1.0\n\n" +
            "A comprehensive system for managing patients,\n" +
            "doctors, and appointments in a clinic setting.",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Do you want to save before exiting?",
            "Exit Confirmation",
            JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            saveData();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ClinicManagementGUI gui = new ClinicManagementGUI();
            gui.setVisible(true);
        });
    }
}
