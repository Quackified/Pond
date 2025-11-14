package com.clinicapp;

import com.clinicapp.gui.MainFrame;

import javax.swing.*;

/**
 * ClinicAppointmentSystemGUI - Main entry point for the GUI version of the Clinic Appointment Management System.
 * 
 * This application provides a comprehensive graphical interface using Java Swing (JForm) for managing:
 * - Patient records (registration, updates, searches)
 * - Doctor profiles (specializations, availability, schedules)
 * - Appointments (scheduling, confirmation, cancellation, completion)
 * 
 * INTEGRATION FEATURES:
 * ====================
 * 1. JAVA SWING (JFORM) GUI
 *    - Modern graphical user interface using Swing components
 *    - JFrame-based main window with menu navigation
 *    - JPanel components for Patient, Doctor, and Appointment management
 *    - JDialog forms for data entry and editing
 *    - JTable components for displaying data in tabular format
 *    - Styled buttons and interactive components
 * 
 * 2. MYSQL DATABASE WITH MYSQL CONNECTOR
 *    - All data persisted in MySQL database
 *    - MySQL Connector/J for JDBC connectivity
 *    - Data Access Object (DAO) pattern for database operations
 *    - Automatic database and table creation on first run
 *    - Connection pooling and management
 *    - Foreign key relationships for data integrity
 * 
 * DATA ARCHITECTURE:
 * =================
 * - DatabaseConnection: Singleton pattern for MySQL connection management
 * - PatientDAO: CRUD operations for patient data
 * - DoctorDAO: CRUD operations for doctor data
 * - AppointmentDAO: CRUD operations for appointment data
 * - All data stored persistently in MySQL database
 * 
 * GUI COMPONENTS:
 * ==============
 * - MainFrame: Main application window with navigation
 * - PatientPanel: Patient management interface with table view
 * - PatientFormDialog: Add/Edit patient dialog form
 * - DoctorPanel: Doctor management interface with table view
 * - DoctorFormDialog: Add/Edit doctor dialog form
 * - AppointmentPanel: Appointment management interface with table view
 * - AppointmentFormDialog: Add/Edit appointment dialog form
 * 
 * FEATURES:
 * ========
 * - Create, Read, Update, Delete (CRUD) operations for all entities
 * - Search functionality for patients and doctors
 * - Appointment scheduling with patient and doctor selection
 * - Status management for appointments (SCHEDULED, CONFIRMED, COMPLETED, CANCELLED)
 * - Doctor availability toggle
 * - Detailed view of all records
 * - Validation for all data entry
 * - User-friendly error messages
 * 
 * DATABASE REQUIREMENTS:
 * =====================
 * - MySQL Server 5.7 or higher
 * - MySQL Connector/J JAR in classpath
 * - Database will be created automatically as 'clinic_db'
 * - Default connection: localhost:3306, user: root, password: (empty)
 * - Modify database.properties file to change connection settings
 * 
 * HOW TO USE:
 * ==========
 * 1. Ensure MySQL server is running
 * 2. Place mysql-connector-java JAR in lib/ directory
 * 3. Run: ./compile.sh
 * 4. Run: ./run.sh gui
 * 5. Use the GUI to manage patients, doctors, and appointments
 * 
 * @author Clinic Development Team
 * @version 2.0 - GUI with MySQL Integration
 */
public class ClinicAppointmentSystemGUI {
    
    /**
     * Main method - entry point for the GUI application.
     * Sets up the look and feel and launches the main window.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║   Clinic Appointment Management System - GUI Version        ║");
        System.out.println("║   Integrated with MySQL Database                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Initializing database connection...");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel");
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                System.out.println("✓ Application started successfully!");
                System.out.println("Close this terminal window to keep the application running.");
            } catch (Exception e) {
                System.err.println("✗ Failed to start application:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to start application.\n" +
                    "Please ensure MySQL server is running and\n" +
                    "MySQL Connector JAR is in the classpath.\n\n" +
                    "Error: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
