package com.clinicapp.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window with tabbed interface for managing patients, doctors, and appointments.
 * Uses UTF-8 encoding for all text components to prevent character encoding issues.
 */
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    
    public MainFrame() {
        initializeFrame();
        createComponents();
        layoutComponents();
    }
    
    /**
     * Initializes the main frame properties with UTF-8 support.
     */
    private void initializeFrame() {
        setTitle("Clinic Appointment Management System - UTF-8 Enabled");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Set icon if available
        try {
            setIconImage(Toolkit.getDefaultToolkit().createImage(""));
        } catch (Exception e) {
            // Icon not available, continue without it
        }
        
        // Ensure UTF-8 encoding for the frame
        setLayout(new BorderLayout());
    }
    
    /**
     * Creates all GUI components with UTF-8 text rendering.
     */
    private void createComponents() {
        tabbedPane = new JTabbedPane();
        
        // Create panels for each section
        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();
        
        // Add tabs with UTF-8 text labels
        tabbedPane.addTab("Patients", new ImageIcon(), patientPanel, "Manage patient records");
        tabbedPane.addTab("Doctors", new ImageIcon(), doctorPanel, "Manage doctor information");
        tabbedPane.addTab("Appointments", new ImageIcon(), appointmentPanel, "Schedule and manage appointments");
        
        // Set font to ensure UTF-8 characters display properly
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 12);
        tabbedPane.setFont(defaultFont);
    }
    
    /**
     * Lays out components in the frame.
     */
    private void layoutComponents() {
        // Add menu bar
        setJMenuBar(createMenuBar());
        
        // Add tabbed pane to center
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar
        add(createStatusBar(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the menu bar with UTF-8 text support.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * Creates the status bar at the bottom of the window.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel statusLabel = new JLabel("Ready - UTF-8 Encoding Active");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        statusBar.add(statusLabel);
        
        return statusBar;
    }
    
    /**
     * Shows the About dialog with UTF-8 text.
     */
    private void showAboutDialog() {
        String message = "Clinic Appointment Management System\n\n" +
                        "Version 2.0 - GUI Edition\n" +
                        "With MySQL Database Integration\n" +
                        "UTF-8 Encoding Support\n\n" +
                        "Manages patients, doctors, and appointments\n" +
                        "with full international character support.";
        
        JOptionPane.showMessageDialog(this, 
                                     message,
                                     "About",
                                     JOptionPane.INFORMATION_MESSAGE);
    }
}
