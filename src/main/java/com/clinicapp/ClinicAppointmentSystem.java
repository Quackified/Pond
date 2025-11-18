package com.clinicapp;

import com.clinicapp.gui.MainWindow;

/**
 * ClinicAppointmentSystem - Main entry point for the Clinic Appointment Management System.
 * 
 * This application provides a comprehensive GUI interface for managing:
 * - Patient records (registration, updates, searches)
 * - Doctor profiles (specializations, availability, schedules)
 * - Appointments (scheduling, confirmation, cancellation, completion)
 * - Queue management for appointment processing (FIFO)
 * - Undo functionality using Stack data structure
 * - Reporting and statistics (daily reports, completion rates, etc.)
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
 *    - Delete doctors
 * 
 * 3. APPOINTMENT MANAGEMENT
 *    - Schedule new appointments with conflict detection
 *    - View appointments in a table
 *    - View detailed appointment information
 *    - Update appointment details
 *    - Confirm scheduled appointments
 *    - Cancel appointments
 *    - Mark appointments as completed
 * 
 * DATA STRUCTURES USED:
 * ====================
 * - HashMap: For O(1) lookup of patients, doctors, and appointments by ID
 * - Stack: For undo functionality (LIFO - Last In First Out)
 * - Queue: For appointment processing (FIFO - First In First Out)
 * - ArrayList: For storing and manipulating lists of records
 * - LocalDate/LocalTime: For date and time handling
 * 
 * DESIGN PATTERNS:
 * ===============
 * - Manager Pattern: Separate managers for Patient, Doctor, and Appointment operations
 * - MVC-like Structure: GUI layer (MainWindow/Panels) separate from business logic (Managers)
 * - Utility Classes: InputValidator for validation
 * 
 * VALIDATION & ERROR HANDLING:
 * ===========================
 * - Input validation for all user entries (dates, times, emails, phone numbers)
 * - Conflict detection for appointment scheduling
 * - Confirmation dialogs for destructive operations
 * - User-friendly error messages via dialogs
 * - Null checks and boundary validation throughout
 * 
 * @author Clinic Development Team
 * @version 1.0
 */
public class ClinicAppointmentSystem {
    
    /**
     * Main method - entry point for the application.
     * Creates and starts the Swing GUI.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create and show the main GUI window
        MainWindow.main(args);
    }
}
