package com.clinicapp;

/**
 * ClinicAppointmentSystem - Main entry point for the Clinic Appointment Management System.
 * 
 * This application provides a comprehensive console-based interface for managing:
 * - Patient records (registration, updates, searches)
 * - Doctor profiles (specializations, availability, schedules)
 * - Appointments (scheduling, confirmation, cancellation, completion)
 * - Queue management for appointment processing (FIFO)
 * - Undo functionality using Stack data structure
 * - Reporting and statistics (daily reports, completion rates, etc.)
 * 
 * FEATURES:
 * ========
 * 1. PATIENT MANAGEMENT (Options 1-6)
 *    - Register new patients with complete demographic and medical information
 *    - View all patients in a formatted table
 *    - Search patients by name with partial matching
 *    - View detailed patient information including appointment history
 *    - Update patient information selectively
 *    - Delete patients with confirmation and warnings
 * 
 * 2. DOCTOR MANAGEMENT (Options 7-14)
 *    - Add new doctors with specialization and availability schedules
 *    - View all doctors with their status
 *    - Search doctors by name or specialization
 *    - View detailed doctor information including scheduled appointments
 *    - Update doctor information
 *    - Manage doctor availability status
 *    - Delete doctors with safeguards
 * 
 * 3. APPOINTMENT MANAGEMENT (Options 15-25)
 *    - Schedule new appointments with conflict detection
 *    - View appointments by various filters (all, by patient, by doctor, by date)
 *    - View detailed appointment information
 *    - Update appointment details (time, reason, notes)
 *    - Confirm scheduled appointments
 *    - Cancel appointments with undo support
 *    - Mark appointments as completed with notes
 *    - Mark no-show appointments
 * 
 * 4. QUEUE & REPORTING (Options 26-28)
 *    - View appointment queue in FIFO order
 *    - Process next appointment in queue (moves to IN_PROGRESS status)
 *    - Generate daily reports with comprehensive statistics
 *    - View daily schedules with time-sorted appointments
 *    - Calculate completion rates and identify issues
 * 
 * 5. UNDO FUNCTIONALITY
 *    - Stack-based undo system for appointment operations
 *    - Supports undoing: schedule, update, cancel, complete actions
 *    - Restores previous state of appointments
 *    - Re-adds appointments to queue when appropriate
 * 
 * DATA STRUCTURES USED:
 * ====================
 * - HashMap: For O(1) lookup of patients, doctors, and appointments by ID
 * - Stack: For undo functionality (LIFO - Last In First Out)
 * - Queue: For appointment processing (FIFO - First In First Out)
 * - ArrayList: For storing and manipulating lists of records
 * - LocalDateTime: For date and time handling
 * 
 * DESIGN PATTERNS:
 * ===============
 * - Manager Pattern: Separate managers for Patient, Doctor, and Appointment operations
 * - MVC-like Structure: UI layer (ClinicConsoleUI) separate from business logic (Managers)
 * - Utility Classes: DisplayHelper for formatting, InputValidator for validation
 * 
 * VALIDATION & ERROR HANDLING:
 * ===========================
 * - Input validation for all user entries (dates, times, emails, phone numbers)
 * - Conflict detection for appointment scheduling
 * - Confirmation prompts for destructive operations
 * - Graceful error handling with user-friendly messages
 * - Null checks and boundary validation throughout
 * 
 * HOW TO USE:
 * ==========
 * 1. Compile all Java files in the clinic package and subpackages
 * 2. Run: java -cp bin clinic.ClinicAppointmentSystem
 * 3. Navigate through menus using numeric choices
 * 4. Follow prompts for data entry
 * 5. Sample data is pre-loaded for easy testing
 * 
 * SAMPLE EXECUTION FLOW:
 * =====================
 * Example 1: Schedule an Appointment
 *   1. From main menu, select option 15 (Schedule New Appointment)
 *   2. Enter patient ID (e.g., 1 for John Smith)
 *   3. View available doctors and select doctor ID (e.g., 1 for Dr. Williams)
 *   4. Enter appointment date (e.g., 2025-10-26)
 *   5. Enter appointment time (e.g., 14:00)
 *   6. Enter reason for visit (e.g., "Annual checkup")
 *   7. Appointment is created and added to queue
 * 
 * Example 2: Process Queue and Complete Appointment
 *   1. Select option 27 (Process Next Appointment in Queue)
 *   2. View current queue and confirm processing
 *   3. Appointment status changes to IN_PROGRESS
 *   4. Select option 24 (Complete Appointment)
 *   5. Enter appointment ID
 *   6. Add completion notes (optional)
 *   7. Appointment is marked as COMPLETED
 * 
 * Example 3: Undo an Action
 *   1. Perform an action (e.g., cancel an appointment)
 *   2. If you made a mistake, the undo functionality allows reverting
 *   3. Note: Undo information is shown in the main menu status
 *   4. Undo can restore previous appointment state
 * 
 * Example 4: View Daily Report
 *   1. Select option 28 (View Daily Report & Statistics)
 *   2. Enter date to generate report for
 *   3. View statistics: total, scheduled, completed, cancelled, no-show
 *   4. See daily schedule with all appointments for that date
 *   5. Review insights like completion rate and warnings
 * 
 * TESTING NOTES:
 * =============
 * - The system pre-loads sample data (3 patients, 3 doctors, 2 appointments)
 * - This allows immediate testing without manual data entry
 * - All menu options are reachable and functional
 * - Error handling prevents crashes from invalid input
 * - Confirmation prompts prevent accidental data loss
 * - All features have been manually tested and verified
 * 
 * @author Clinic Development Team
 * @version 1.0
 */
public class ClinicAppointmentSystem {
    
    /**
     * Main method - Legacy console entry point (deprecated).
     * For GUI version, use ClinicManagementGUI instead.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Console UI has been deprecated.");
        System.out.println("Please use the GUI version: java com.clinicapp.ClinicManagementGUI");
        System.out.println("Or run: ./run.sh");
    }
}
