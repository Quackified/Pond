package com.clinicapp;

import com.clinicapp.gui.ClinicManagementGUI;
import javax.swing.*;

/**
 * ClinicAppointmentSystem - Main entry point for the Clinic Appointment Management System.
 * 
 * This application provides a comprehensive GUI interface for managing:
 * - Patient records (registration, updates, searches)
 * - Doctor profiles (specializations, availability, schedules)
 * - Appointments (scheduling, confirmation, cancellation, completion)
 * - Queue management for appointment processing (FIFO)
 * - Undo functionality using Stack data structure
 * - Data persistence using JSON file I/O with Gson
 * - CSV export functionality
 * 
 * FEATURES:
 * ========
 * 1. PATIENT MANAGEMENT
 *    - Register new patients with complete demographic and medical information
 *    - View all patients in a formatted table
 *    - View detailed patient information
 *    - Update patient information
 *    - Delete patients with confirmation
 * 
 * 2. DOCTOR MANAGEMENT
 *    - Add new doctors with specialization and availability schedules
 *    - View all doctors with their status
 *    - View detailed doctor information
 *    - Update doctor information
 *    - Manage doctor availability status
 *    - Delete doctors with safeguards
 * 
 * 3. APPOINTMENT MANAGEMENT
 *    - Schedule new appointments with conflict detection
 *    - View all appointments
 *    - View detailed appointment information
 *    - Update appointment status (scheduled, confirmed, in progress, completed, cancelled)
 *    - Cancel appointments with undo support
 *    - Mark appointments as completed with notes
 * 
 * 4. DATA PERSISTENCE & EXPORT
 *    - Save data to JSON files using Gson
 *    - Load data from JSON files
 *    - Export patients, doctors, and appointments to CSV
 * 
 * TECHNOLOGY STACK:
 * =================
 * - Java Swing for GUI
 * - Gson for JSON serialization/deserialization
 * - LocalDate, LocalDateTime, LocalTime for date/time handling
 * - HashMap for O(1) data lookup
 * - Stack for undo functionality
 * - Queue for appointment processing
 * 
 * @author Clinic Development Team
 * @version 2.0
 */
public class ClinicAppointmentSystem {
    
    /**
     * Main method - entry point for the application.
     * Creates and starts the Swing GUI.
     * 
     * @param args Command line arguments (not used)
     */
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
