package com.clinicapp.ui;

import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;
import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.clinicapp.util.DisplayHelper;
import com.clinicapp.util.InputValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ClinicConsoleUI provides the complete console-based user interface
 * for the Clinic Appointment System. This class handles all menu operations,
 * user interactions, and coordinates between the various managers.
 * 
 * The menu system covers 28 different operations including:
 * - Patient management (add, view, update, delete, search)
 * - Doctor management (add, view, update, delete, search, availability)
 * - Appointment management (schedule, view, update, cancel, confirm, complete)
 * - Queue operations (view, process)
 * - Reporting (daily reports, history, statistics)
 * - Undo functionality for appointment operations
 */
public class ClinicConsoleUI {
    
    // Manager instances for handling business logic
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;
    private final AppointmentManager appointmentManager;
    
    // Scanner for reading user input
    private final Scanner scanner;
    
    // Flag to control main loop
    private boolean running;
    
    /**
     * Constructor initializes all managers and the scanner.
     */
    public ClinicConsoleUI() {
        this.patientManager = new PatientManager();
        this.doctorManager = new DoctorManager();
        this.appointmentManager = new AppointmentManager(patientManager, doctorManager);
        this.scanner = new Scanner(System.in);
        this.running = true;
        
        // Load sample data for demonstration purposes
        loadSampleData();
    }
    
    /**
     * Start the main menu loop.
     * This is the entry point for the user interface.
     */
    public void start() {
        // Display welcome banner
        displayWelcomeBanner();
        
        // Main menu loop - continues until user chooses to exit
        while (running) {
            try {
                displayMainMenu();
                int choice = InputValidator.readInt(scanner, "\nEnter your choice: ", 0, 28);
                processMainMenuChoice(choice);
            } catch (Exception e) {
                // Catch any unexpected errors to prevent crashes
                DisplayHelper.displayError("An unexpected error occurred: " + e.getMessage());
                DisplayHelper.pressEnterToContinue();
            }
        }
        
        // Clean up and exit
        scanner.close();
        System.out.println("\n✓ Thank you for using the Clinic Appointment System. Goodbye!\n");
    }
    
    /**
     * Display the welcome banner when application starts.
     */
    private void displayWelcomeBanner() {
        DisplayHelper.clearScreen();
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                               ║");
        System.out.println("║                     CLINIC APPOINTMENT MANAGEMENT SYSTEM                      ║");
        System.out.println("║                                                                               ║");
        System.out.println("║                        Welcome to Your Healthcare Hub                         ║");
        System.out.println("║                                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Display the main menu with all 28 options.
     * Options are organized into logical categories for easy navigation.
     */
    private void displayMainMenu() {
        DisplayHelper.clearScreen();
        DisplayHelper.printHeader("MAIN MENU");
        
        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│                     PATIENT MANAGEMENT                          │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Register New Patient                                        │");
        System.out.println("│  2. View All Patients                                           │");
        System.out.println("│  3. Search Patient by Name                                      │");
        System.out.println("│  4. View Patient Details                                        │");
        System.out.println("│  5. Update Patient Information                                  │");
        System.out.println("│  6. Delete Patient                                              │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│                     DOCTOR MANAGEMENT                           │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│  7. Add New Doctor                                              │");
        System.out.println("│  8. View All Doctors                                            │");
        System.out.println("│  9. Search Doctor by Name                                       │");
        System.out.println("│ 10. Search Doctor by Specialization                            │");
        System.out.println("│ 11. View Doctor Details                                         │");
        System.out.println("│ 12. Update Doctor Information                                   │");
        System.out.println("│ 13. Set Doctor Availability                                     │");
        System.out.println("│ 14. Delete Doctor                                               │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│                  APPOINTMENT MANAGEMENT                         │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 15. Schedule New Appointment                                    │");
        System.out.println("│ 16. View All Appointments                                       │");
        System.out.println("│ 17. View Appointments by Patient                                │");
        System.out.println("│ 18. View Appointments by Doctor                                 │");
        System.out.println("│ 19. View Appointments by Date                                   │");
        System.out.println("│ 20. View Appointment Details                                    │");
        System.out.println("│ 21. Update Appointment                                          │");
        System.out.println("│ 22. Confirm Appointment                                         │");
        System.out.println("│ 23. Cancel Appointment                                          │");
        System.out.println("│ 24. Complete Appointment                                        │");
        System.out.println("│ 25. Mark Appointment as No-Show                                 │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│                 QUEUE & REPORTING                               │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 26. View Appointment Queue                                      │");
        System.out.println("│ 27. Process Next Appointment in Queue                           │");
        System.out.println("│ 28. View Daily Report & Statistics                              │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│                    OTHER OPTIONS                                │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│  0. Exit System                                                 │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        // Display system statistics
        System.out.println("\n📊 System Overview:");
        System.out.printf("   Patients: %d | Doctors: %d | Appointments: %d | Queue: %d | Undo Available: %s\n",
                         patientManager.getPatientCount(),
                         doctorManager.getDoctorCount(),
                         appointmentManager.getAppointmentCount(),
                         appointmentManager.getQueueSize(),
                         appointmentManager.canUndo() ? "Yes" : "No");
    }
    
    /**
     * Process the user's menu choice and route to appropriate handler.
     * 
     * @param choice The menu option selected by user (0-28)
     */
    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 0:  exitSystem(); break;
            case 1:  registerNewPatient(); break;
            case 2:  viewAllPatients(); break;
            case 3:  searchPatientByName(); break;
            case 4:  viewPatientDetails(); break;
            case 5:  updatePatientInformation(); break;
            case 6:  deletePatient(); break;
            case 7:  addNewDoctor(); break;
            case 8:  viewAllDoctors(); break;
            case 9:  searchDoctorByName(); break;
            case 10: searchDoctorBySpecialization(); break;
            case 11: viewDoctorDetails(); break;
            case 12: updateDoctorInformation(); break;
            case 13: setDoctorAvailability(); break;
            case 14: deleteDoctor(); break;
            case 15: scheduleNewAppointment(); break;
            case 16: viewAllAppointments(); break;
            case 17: viewAppointmentsByPatient(); break;
            case 18: viewAppointmentsByDoctor(); break;
            case 19: viewAppointmentsByDate(); break;
            case 20: viewAppointmentDetails(); break;
            case 21: updateAppointment(); break;
            case 22: confirmAppointment(); break;
            case 23: cancelAppointment(); break;
            case 24: completeAppointment(); break;
            case 25: markAppointmentNoShow(); break;
            case 26: viewAppointmentQueue(); break;
            case 27: processNextInQueue(); break;
            case 28: viewDailyReport(); break;
            default:
                DisplayHelper.displayError("Invalid choice. Please try again.");
                DisplayHelper.pressEnterToContinue();
        }
    }
    
    // ============================================================================
    // MENU OPTION 0: EXIT SYSTEM
    // ============================================================================
    
    /**
     * Option 0: Exit the system.
     * Prompts for confirmation before exiting.
     */
    private void exitSystem() {
        DisplayHelper.printHeader("EXIT SYSTEM");
        
        boolean confirm = InputValidator.readConfirmation(scanner, 
            "\nAre you sure you want to exit the system?");
        
        if (confirm) {
            running = false;
        } else {
            DisplayHelper.displayInfo("Exit cancelled. Returning to main menu.");
            DisplayHelper.pressEnterToContinue();
        }
    }
    
    // ============================================================================
    // MENU OPTIONS 1-6: PATIENT MANAGEMENT
    // ============================================================================
    
    /**
     * Option 1: Register a new patient.
     * Collects all required patient information with validation.
     */
    private void registerNewPatient() {
        DisplayHelper.printHeader("REGISTER NEW PATIENT");
        
        try {
            // Collect patient information with validation
            String name = InputValidator.readNonEmptyString(scanner, "\nEnter patient name: ");
            LocalDate dob = InputValidator.readDate(scanner, "Enter date of birth", false, true);
            String gender = InputValidator.readGender(scanner);
            String phone = InputValidator.readPhoneNumber(scanner, "Enter phone number: ");
            String email = InputValidator.readEmail(scanner, "Enter email (optional, press ENTER to skip): ", true);
            String address = InputValidator.readOptionalString(scanner, "Enter address (optional): ");
            String bloodType = InputValidator.readBloodType(scanner, true);
            String allergies = InputValidator.readOptionalString(scanner, "Enter known allergies (optional): ");
            
            // Create the patient
            Patient patient = patientManager.addPatient(name, dob, gender, phone, 
                                                       email, address, bloodType, allergies);
            
            // Display success message with patient details
            DisplayHelper.displaySuccess("Patient registered successfully!");
            System.out.println(patient.getDetailedInfo());
            
        } catch (Exception e) {
            DisplayHelper.displayError("Failed to register patient: " + e.getMessage());
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 2: View all patients in the system.
     * Displays patients in a formatted table.
     */
    private void viewAllPatients() {
        DisplayHelper.printHeader("ALL PATIENTS");
        
        List<Patient> patients = patientManager.getAllPatients();
        DisplayHelper.displayPatientTable(patients);
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 3: Search for patients by name.
     * Performs case-insensitive partial matching.
     */
    private void searchPatientByName() {
        DisplayHelper.printHeader("SEARCH PATIENT BY NAME");
        
        String searchTerm = InputValidator.readNonEmptyString(scanner, "\nEnter patient name to search: ");
        
        List<Patient> results = patientManager.searchPatientsByName(searchTerm);
        
        if (results.isEmpty()) {
            DisplayHelper.displayError("No patients found matching '" + searchTerm + "'.");
        } else {
            DisplayHelper.displayInfo("Found " + results.size() + " patient(s):");
            DisplayHelper.displayPatientTable(results);
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 4: View detailed information for a specific patient.
     */
    private void viewPatientDetails() {
        DisplayHelper.printHeader("PATIENT DETAILS");
        
        int patientId = InputValidator.readInt(scanner, "\nEnter patient ID: ", 1, Integer.MAX_VALUE);
        
        Patient patient = patientManager.getPatientById(patientId);
        
        if (patient == null) {
            DisplayHelper.displayError("Patient with ID " + patientId + " not found.");
        } else {
            System.out.println(patient.getDetailedInfo());
            
            // Also show appointment history for this patient
            List<Appointment> appointments = appointmentManager.getAppointmentsByPatient(patientId);
            if (!appointments.isEmpty()) {
                System.out.println("\n--- Appointment History ---");
                DisplayHelper.displayAppointmentTable(appointments);
            }
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 5: Update patient information.
     * Allows selective updating of patient fields.
     */
    private void updatePatientInformation() {
        DisplayHelper.printHeader("UPDATE PATIENT INFORMATION");
        
        int patientId = InputValidator.readInt(scanner, "\nEnter patient ID: ", 1, Integer.MAX_VALUE);
        
        Patient patient = patientManager.getPatientById(patientId);
        
        if (patient == null) {
            DisplayHelper.displayError("Patient with ID " + patientId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        // Display current patient information
        System.out.println("\n--- Current Information ---");
        System.out.println(patient.getDetailedInfo());
        
        DisplayHelper.displayInfo("Leave fields empty to keep current values.");
        
        // Collect updated information
        String name = InputValidator.readOptionalString(scanner, "\nEnter new name (current: " + patient.getName() + "): ");
        String phone = InputValidator.readOptionalString(scanner, "Enter new phone (current: " + patient.getPhoneNumber() + "): ");
        String email = InputValidator.readOptionalString(scanner, "Enter new email (current: " + patient.getEmail() + "): ");
        String address = InputValidator.readOptionalString(scanner, "Enter new address (current: " + patient.getAddress() + "): ");
        
        // Update the patient
        boolean success = patientManager.updatePatient(patientId, name, null, null, 
                                                       phone, email, address, null, null);
        
        if (success) {
            DisplayHelper.displaySuccess("Patient information updated successfully!");
            System.out.println(patient.getDetailedInfo());
        } else {
            DisplayHelper.displayError("Failed to update patient information.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 6: Delete a patient from the system.
     * Requires confirmation before deletion.
     */
    private void deletePatient() {
        DisplayHelper.printHeader("DELETE PATIENT");
        
        int patientId = InputValidator.readInt(scanner, "\nEnter patient ID: ", 1, Integer.MAX_VALUE);
        
        Patient patient = patientManager.getPatientById(patientId);
        
        if (patient == null) {
            DisplayHelper.displayError("Patient with ID " + patientId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        // Display patient information
        System.out.println(patient.getDetailedInfo());
        
        // Check for existing appointments
        List<Appointment> appointments = appointmentManager.getAppointmentsByPatient(patientId);
        if (!appointments.isEmpty()) {
            DisplayHelper.displayWarning("This patient has " + appointments.size() + " appointment(s).");
        }
        
        // Confirm deletion
        boolean confirm = InputValidator.readConfirmation(scanner, 
            "\nAre you sure you want to delete this patient?");
        
        if (confirm) {
            boolean success = patientManager.deletePatient(patientId);
            if (success) {
                DisplayHelper.displaySuccess("Patient deleted successfully.");
            } else {
                DisplayHelper.displayError("Failed to delete patient.");
            }
        } else {
            DisplayHelper.displayInfo("Deletion cancelled.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    // ============================================================================
    // MENU OPTIONS 7-14: DOCTOR MANAGEMENT
    // ============================================================================
    
    /**
     * Option 7: Add a new doctor to the system.
     */
    private void addNewDoctor() {
        DisplayHelper.printHeader("ADD NEW DOCTOR");
        
        try {
            String name = InputValidator.readNonEmptyString(scanner, "\nEnter doctor name: ");
            String specialization = InputValidator.readNonEmptyString(scanner, "Enter specialization: ");
            String phone = InputValidator.readPhoneNumber(scanner, "Enter phone number: ");
            String email = InputValidator.readEmail(scanner, "Enter email: ", false);
            
            // Collect available days
            List<String> availableDays = new ArrayList<>();
            System.out.println("\nEnter available days (type 'done' when finished):");
            String[] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            for (String day : weekDays) {
                boolean available = InputValidator.readConfirmation(scanner, "Available on " + day + "?");
                if (available) {
                    availableDays.add(day);
                }
            }
            
            String startTime = InputValidator.readTime(scanner, "Enter start time").toString();
            String endTime = InputValidator.readTime(scanner, "Enter end time").toString();
            
            Doctor doctor = doctorManager.addDoctor(name, specialization, phone, email,
                                                   availableDays, startTime, endTime);
            
            DisplayHelper.displaySuccess("Doctor added successfully!");
            System.out.println(doctor.getDetailedInfo());
            
        } catch (Exception e) {
            DisplayHelper.displayError("Failed to add doctor: " + e.getMessage());
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 8: View all doctors in the system.
     */
    private void viewAllDoctors() {
        DisplayHelper.printHeader("ALL DOCTORS");
        
        List<Doctor> doctors = doctorManager.getAllDoctors();
        DisplayHelper.displayDoctorTable(doctors);
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 9: Search for doctors by name.
     */
    private void searchDoctorByName() {
        DisplayHelper.printHeader("SEARCH DOCTOR BY NAME");
        
        String searchTerm = InputValidator.readNonEmptyString(scanner, "\nEnter doctor name to search: ");
        
        List<Doctor> results = doctorManager.searchDoctorsByName(searchTerm);
        
        if (results.isEmpty()) {
            DisplayHelper.displayError("No doctors found matching '" + searchTerm + "'.");
        } else {
            DisplayHelper.displayInfo("Found " + results.size() + " doctor(s):");
            DisplayHelper.displayDoctorTable(results);
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 10: Search for doctors by specialization.
     */
    private void searchDoctorBySpecialization() {
        DisplayHelper.printHeader("SEARCH DOCTOR BY SPECIALIZATION");
        
        String searchTerm = InputValidator.readNonEmptyString(scanner, "\nEnter specialization to search: ");
        
        List<Doctor> results = doctorManager.searchDoctorsBySpecialization(searchTerm);
        
        if (results.isEmpty()) {
            DisplayHelper.displayError("No doctors found with specialization '" + searchTerm + "'.");
        } else {
            DisplayHelper.displayInfo("Found " + results.size() + " doctor(s):");
            DisplayHelper.displayDoctorTable(results);
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 11: View detailed information for a specific doctor.
     */
    private void viewDoctorDetails() {
        DisplayHelper.printHeader("DOCTOR DETAILS");
        
        int doctorId = InputValidator.readInt(scanner, "\nEnter doctor ID: ", 1, Integer.MAX_VALUE);
        
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor == null) {
            DisplayHelper.displayError("Doctor with ID " + doctorId + " not found.");
        } else {
            System.out.println(doctor.getDetailedInfo());
            
            // Show appointments for this doctor
            List<Appointment> appointments = appointmentManager.getAppointmentsByDoctor(doctorId);
            if (!appointments.isEmpty()) {
                System.out.println("\n--- Scheduled Appointments ---");
                DisplayHelper.displayAppointmentTable(appointments);
            }
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 12: Update doctor information.
     */
    private void updateDoctorInformation() {
        DisplayHelper.printHeader("UPDATE DOCTOR INFORMATION");
        
        int doctorId = InputValidator.readInt(scanner, "\nEnter doctor ID: ", 1, Integer.MAX_VALUE);
        
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor == null) {
            DisplayHelper.displayError("Doctor with ID " + doctorId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println("\n--- Current Information ---");
        System.out.println(doctor.getDetailedInfo());
        
        DisplayHelper.displayInfo("Leave fields empty to keep current values.");
        
        String name = InputValidator.readOptionalString(scanner, "\nEnter new name (current: " + doctor.getName() + "): ");
        String specialization = InputValidator.readOptionalString(scanner, "Enter new specialization (current: " + doctor.getSpecialization() + "): ");
        String phone = InputValidator.readOptionalString(scanner, "Enter new phone (current: " + doctor.getPhoneNumber() + "): ");
        String email = InputValidator.readOptionalString(scanner, "Enter new email (current: " + doctor.getEmail() + "): ");
        
        boolean success = doctorManager.updateDoctor(doctorId, name, specialization, 
                                                     phone, email, null, null, null);
        
        if (success) {
            DisplayHelper.displaySuccess("Doctor information updated successfully!");
            System.out.println(doctor.getDetailedInfo());
        } else {
            DisplayHelper.displayError("Failed to update doctor information.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 13: Set doctor availability status.
     */
    private void setDoctorAvailability() {
        DisplayHelper.printHeader("SET DOCTOR AVAILABILITY");
        
        int doctorId = InputValidator.readInt(scanner, "\nEnter doctor ID: ", 1, Integer.MAX_VALUE);
        
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor == null) {
            DisplayHelper.displayError("Doctor with ID " + doctorId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println("\nDr. " + doctor.getName() + " is currently: " + 
                          (doctor.isAvailable() ? "Available" : "Unavailable"));
        
        boolean newStatus = InputValidator.readConfirmation(scanner, 
            "\nSet doctor as available?");
        
        boolean success = doctorManager.setDoctorAvailability(doctorId, newStatus);
        
        if (success) {
            DisplayHelper.displaySuccess("Doctor availability updated successfully!");
            System.out.println("Dr. " + doctor.getName() + " is now: " + 
                             (newStatus ? "Available" : "Unavailable"));
        } else {
            DisplayHelper.displayError("Failed to update doctor availability.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 14: Delete a doctor from the system.
     */
    private void deleteDoctor() {
        DisplayHelper.printHeader("DELETE DOCTOR");
        
        int doctorId = InputValidator.readInt(scanner, "\nEnter doctor ID: ", 1, Integer.MAX_VALUE);
        
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor == null) {
            DisplayHelper.displayError("Doctor with ID " + doctorId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println(doctor.getDetailedInfo());
        
        // Check for existing appointments
        List<Appointment> appointments = appointmentManager.getAppointmentsByDoctor(doctorId);
        if (!appointments.isEmpty()) {
            DisplayHelper.displayWarning("This doctor has " + appointments.size() + " appointment(s).");
        }
        
        boolean confirm = InputValidator.readConfirmation(scanner, 
            "\nAre you sure you want to delete this doctor?");
        
        if (confirm) {
            boolean success = doctorManager.deleteDoctor(doctorId);
            if (success) {
                DisplayHelper.displaySuccess("Doctor deleted successfully.");
            } else {
                DisplayHelper.displayError("Failed to delete doctor.");
            }
        } else {
            DisplayHelper.displayInfo("Deletion cancelled.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    // ============================================================================
    // MENU OPTIONS 15-25: APPOINTMENT MANAGEMENT
    // ============================================================================
    
    /**
     * Option 15: Schedule a new appointment.
     */
    private void scheduleNewAppointment() {
        DisplayHelper.printHeader("SCHEDULE NEW APPOINTMENT");
        
        try {
            // Get patient
            int patientId = InputValidator.readInt(scanner, "\nEnter patient ID: ", 1, Integer.MAX_VALUE);
            Patient patient = patientManager.getPatientById(patientId);
            
            if (patient == null) {
                DisplayHelper.displayError("Patient with ID " + patientId + " not found.");
                DisplayHelper.pressEnterToContinue();
                return;
            }
            
            DisplayHelper.displayInfo("Patient: " + patient.getName());
            
            // Get doctor
            List<Doctor> availableDoctors = doctorManager.getAvailableDoctors();
            if (availableDoctors.isEmpty()) {
                DisplayHelper.displayError("No doctors are currently available.");
                DisplayHelper.pressEnterToContinue();
                return;
            }
            
            System.out.println("\n--- Available Doctors ---");
            DisplayHelper.displayDoctorTable(availableDoctors);
            
            int doctorId = InputValidator.readInt(scanner, "\nEnter doctor ID: ", 1, Integer.MAX_VALUE);
            Doctor doctor = doctorManager.getDoctorById(doctorId);
            
            if (doctor == null || !doctor.isAvailable()) {
                DisplayHelper.displayError("Selected doctor is not available.");
                DisplayHelper.pressEnterToContinue();
                return;
            }
            
            DisplayHelper.displayInfo("Doctor: Dr. " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
            
            // Get appointment date and time
            LocalDateTime dateTime = InputValidator.readDateTime(scanner);
            
            // Get reason for visit
            String reason = InputValidator.readNonEmptyString(scanner, "Enter reason for visit: ");
            
            // Schedule the appointment
            Appointment appointment = appointmentManager.scheduleAppointment(patient, doctor, dateTime, reason);
            
            if (appointment == null) {
                DisplayHelper.displayError("Failed to schedule appointment. There might be a time conflict.");
            } else {
                DisplayHelper.displaySuccess("Appointment scheduled successfully!");
                System.out.println(appointment.getDetailedInfo());
            }
            
        } catch (Exception e) {
            DisplayHelper.displayError("Failed to schedule appointment: " + e.getMessage());
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 16: View all appointments in the system.
     */
    private void viewAllAppointments() {
        DisplayHelper.printHeader("ALL APPOINTMENTS");
        
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        DisplayHelper.displayAppointmentTable(appointments);
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 17: View appointments for a specific patient.
     */
    private void viewAppointmentsByPatient() {
        DisplayHelper.printHeader("APPOINTMENTS BY PATIENT");
        
        int patientId = InputValidator.readInt(scanner, "\nEnter patient ID: ", 1, Integer.MAX_VALUE);
        
        Patient patient = patientManager.getPatientById(patientId);
        if (patient == null) {
            DisplayHelper.displayError("Patient with ID " + patientId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        DisplayHelper.displayInfo("Patient: " + patient.getName());
        
        List<Appointment> appointments = appointmentManager.getAppointmentsByPatient(patientId);
        DisplayHelper.displayAppointmentTable(appointments);
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 18: View appointments for a specific doctor.
     */
    private void viewAppointmentsByDoctor() {
        DisplayHelper.printHeader("APPOINTMENTS BY DOCTOR");
        
        int doctorId = InputValidator.readInt(scanner, "\nEnter doctor ID: ", 1, Integer.MAX_VALUE);
        
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        if (doctor == null) {
            DisplayHelper.displayError("Doctor with ID " + doctorId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        DisplayHelper.displayInfo("Doctor: Dr. " + doctor.getName());
        
        List<Appointment> appointments = appointmentManager.getAppointmentsByDoctor(doctorId);
        DisplayHelper.displayAppointmentTable(appointments);
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 19: View appointments for a specific date.
     */
    private void viewAppointmentsByDate() {
        DisplayHelper.printHeader("APPOINTMENTS BY DATE");
        
        LocalDate date = InputValidator.readDate(scanner, "\nEnter date", true, true);
        
        List<Appointment> appointments = appointmentManager.getAppointmentsByDate(date);
        DisplayHelper.displayDailySchedule(date, appointments);
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 20: View detailed information for a specific appointment.
     */
    private void viewAppointmentDetails() {
        DisplayHelper.printHeader("APPOINTMENT DETAILS");
        
        int appointmentId = InputValidator.readInt(scanner, "\nEnter appointment ID: ", 1, Integer.MAX_VALUE);
        
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            DisplayHelper.displayError("Appointment with ID " + appointmentId + " not found.");
        } else {
            System.out.println(appointment.getDetailedInfo());
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 21: Update appointment details.
     */
    private void updateAppointment() {
        DisplayHelper.printHeader("UPDATE APPOINTMENT");
        
        int appointmentId = InputValidator.readInt(scanner, "\nEnter appointment ID: ", 1, Integer.MAX_VALUE);
        
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            DisplayHelper.displayError("Appointment with ID " + appointmentId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println("\n--- Current Appointment ---");
        System.out.println(appointment.getDetailedInfo());
        
        DisplayHelper.displayInfo("Leave fields empty to keep current values.");
        
        // Ask if user wants to change date/time
        boolean changeDateTime = InputValidator.readConfirmation(scanner, "\nChange date/time?");
        LocalDateTime newDateTime = null;
        if (changeDateTime) {
            newDateTime = InputValidator.readDateTime(scanner);
        }
        
        // Ask for new reason
        String newReason = InputValidator.readOptionalString(scanner, 
            "Enter new reason (current: " + appointment.getReason() + "): ");
        
        // Ask for notes
        String notes = InputValidator.readOptionalString(scanner, "Add notes (optional): ");
        
        boolean success = appointmentManager.updateAppointment(appointmentId, newDateTime, newReason, notes);
        
        if (success) {
            DisplayHelper.displaySuccess("Appointment updated successfully!");
            System.out.println(appointment.getDetailedInfo());
            
            // Inform about undo availability
            DisplayHelper.displayInfo("You can undo this action from the main menu if needed.");
        } else {
            DisplayHelper.displayError("Failed to update appointment. Check for scheduling conflicts.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 22: Confirm an appointment.
     */
    private void confirmAppointment() {
        DisplayHelper.printHeader("CONFIRM APPOINTMENT");
        
        int appointmentId = InputValidator.readInt(scanner, "\nEnter appointment ID: ", 1, Integer.MAX_VALUE);
        
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            DisplayHelper.displayError("Appointment with ID " + appointmentId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println(appointment.getDetailedInfo());
        
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            DisplayHelper.displayWarning("This appointment is not in SCHEDULED status.");
        }
        
        boolean confirm = InputValidator.readConfirmation(scanner, "\nConfirm this appointment?");
        
        if (confirm) {
            boolean success = appointmentManager.confirmAppointment(appointmentId);
            if (success) {
                DisplayHelper.displaySuccess("Appointment confirmed successfully!");
            } else {
                DisplayHelper.displayError("Failed to confirm appointment.");
            }
        } else {
            DisplayHelper.displayInfo("Confirmation cancelled.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 23: Cancel an appointment.
     */
    private void cancelAppointment() {
        DisplayHelper.printHeader("CANCEL APPOINTMENT");
        
        int appointmentId = InputValidator.readInt(scanner, "\nEnter appointment ID: ", 1, Integer.MAX_VALUE);
        
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            DisplayHelper.displayError("Appointment with ID " + appointmentId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println(appointment.getDetailedInfo());
        
        boolean confirm = InputValidator.readConfirmation(scanner, "\nAre you sure you want to cancel this appointment?");
        
        if (confirm) {
            boolean success = appointmentManager.cancelAppointment(appointmentId);
            if (success) {
                DisplayHelper.displaySuccess("Appointment cancelled successfully!");
                DisplayHelper.displayInfo("You can undo this action from the main menu if needed.");
            } else {
                DisplayHelper.displayError("Failed to cancel appointment.");
            }
        } else {
            DisplayHelper.displayInfo("Cancellation aborted.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 24: Mark appointment as completed.
     */
    private void completeAppointment() {
        DisplayHelper.printHeader("COMPLETE APPOINTMENT");
        
        int appointmentId = InputValidator.readInt(scanner, "\nEnter appointment ID: ", 1, Integer.MAX_VALUE);
        
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            DisplayHelper.displayError("Appointment with ID " + appointmentId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println(appointment.getDetailedInfo());
        
        String notes = InputValidator.readOptionalString(scanner, "\nEnter completion notes (optional): ");
        
        boolean success = appointmentManager.completeAppointment(appointmentId, notes);
        
        if (success) {
            DisplayHelper.displaySuccess("Appointment marked as completed!");
            DisplayHelper.displayInfo("You can undo this action from the main menu if needed.");
        } else {
            DisplayHelper.displayError("Failed to complete appointment.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 25: Mark appointment as no-show.
     */
    private void markAppointmentNoShow() {
        DisplayHelper.printHeader("MARK APPOINTMENT AS NO-SHOW");
        
        int appointmentId = InputValidator.readInt(scanner, "\nEnter appointment ID: ", 1, Integer.MAX_VALUE);
        
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            DisplayHelper.displayError("Appointment with ID " + appointmentId + " not found.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        System.out.println(appointment.getDetailedInfo());
        
        boolean confirm = InputValidator.readConfirmation(scanner, "\nMark this appointment as no-show?");
        
        if (confirm) {
            boolean success = appointmentManager.markNoShow(appointmentId);
            if (success) {
                DisplayHelper.displaySuccess("Appointment marked as no-show.");
                DisplayHelper.displayInfo("You can undo this action from the main menu if needed.");
            } else {
                DisplayHelper.displayError("Failed to mark appointment as no-show.");
            }
        } else {
            DisplayHelper.displayInfo("Action cancelled.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    // ============================================================================
    // MENU OPTIONS 26-28: QUEUE & REPORTING
    // ============================================================================
    
    /**
     * Option 26: View the appointment queue.
     * Shows appointments waiting to be processed in FIFO order.
     */
    private void viewAppointmentQueue() {
        DisplayHelper.printHeader("APPOINTMENT QUEUE");
        
        List<Appointment> queue = appointmentManager.viewQueue();
        DisplayHelper.displayQueue(queue);
        
        if (appointmentManager.canUndo()) {
            DisplayHelper.displayInfo("\nNote: " + appointmentManager.getUndoStackSize() + 
                                     " action(s) can be undone.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 27: Process the next appointment in the queue.
     * Removes appointment from queue and marks it as IN_PROGRESS.
     */
    private void processNextInQueue() {
        DisplayHelper.printHeader("PROCESS NEXT APPOINTMENT");
        
        if (appointmentManager.getQueueSize() == 0) {
            DisplayHelper.displayInfo("Queue is empty - no appointments to process.");
            DisplayHelper.pressEnterToContinue();
            return;
        }
        
        // Show current queue
        System.out.println("\n--- Current Queue ---");
        DisplayHelper.displayQueue(appointmentManager.viewQueue());
        
        boolean confirm = InputValidator.readConfirmation(scanner, "\nProcess the next appointment in queue?");
        
        if (confirm) {
            Appointment processed = appointmentManager.processNextInQueue();
            
            if (processed != null) {
                DisplayHelper.displaySuccess("Processing appointment...");
                System.out.println(processed.getDetailedInfo());
                DisplayHelper.displayInfo("Status changed to: IN_PROGRESS");
                DisplayHelper.displayInfo("You can undo this action from the main menu if needed.");
            } else {
                DisplayHelper.displayError("Failed to process appointment.");
            }
        } else {
            DisplayHelper.displayInfo("Processing cancelled.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    /**
     * Option 28: View daily report and statistics.
     * Shows comprehensive statistics for a specific date.
     */
    private void viewDailyReport() {
        DisplayHelper.printHeader("DAILY REPORT & STATISTICS");
        
        LocalDate date = InputValidator.readDate(scanner, "\nEnter date for report", true, true);
        
        // Get appointments for the day
        List<Appointment> dailyAppointments = appointmentManager.getAppointmentsByDate(date);
        
        // Get statistics
        Map<String, Integer> statistics = appointmentManager.getDailyStatistics(date);
        
        // Display the report
        DisplayHelper.displayDailyReport(date, statistics);
        
        // Show the daily schedule
        if (!dailyAppointments.isEmpty()) {
            System.out.println("\n");
            DisplayHelper.displayDailySchedule(date, dailyAppointments);
        }
        
        // Additional insights
        System.out.println("\n--- Insights ---");
        int completed = statistics.getOrDefault("completed", 0);
        int total = statistics.getOrDefault("total", 0);
        if (total > 0) {
            double completionRate = (completed * 100.0) / total;
            System.out.printf("Completion Rate: %.1f%%\n", completionRate);
        }
        
        int cancelled = statistics.getOrDefault("cancelled", 0);
        int noShow = statistics.getOrDefault("no_show", 0);
        int missed = cancelled + noShow;
        if (missed > 0) {
            System.out.println("⚠ Warning: " + missed + " appointment(s) were cancelled or no-show.");
        }
        
        DisplayHelper.pressEnterToContinue();
    }
    
    // ============================================================================
    // HELPER METHODS
    // ============================================================================
    
    /**
     * Load sample data for demonstration purposes.
     * This makes it easier to test the system without manually entering data.
     */
    private void loadSampleData() {
        // Add sample patients
        patientManager.addPatient("John Smith", LocalDate.of(1985, 5, 15), "Male",
                                 "5551234567", "john.smith@email.com", "123 Main St",
                                 "O+", "None");
        
        patientManager.addPatient("Jane Doe", LocalDate.of(1990, 8, 22), "Female",
                                 "5559876543", "jane.doe@email.com", "456 Oak Ave",
                                 "A+", "Penicillin");
        
        patientManager.addPatient("Bob Johnson", LocalDate.of(1978, 3, 10), "Male",
                                 "5555551234", "bob.j@email.com", "789 Pine Rd",
                                 "B+", "None");
        
        // Add sample doctors
        List<String> weekdays = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        
        doctorManager.addDoctor("Sarah Williams", "Cardiology", "5551112222",
                              "dr.williams@clinic.com", weekdays, "09:00", "17:00");
        
        doctorManager.addDoctor("Michael Chen", "Pediatrics", "5553334444",
                              "dr.chen@clinic.com", weekdays, "08:00", "16:00");
        
        doctorManager.addDoctor("Emily Brown", "General Practice", "5555556666",
                              "dr.brown@clinic.com", Arrays.asList("Monday", "Wednesday", "Friday"),
                              "10:00", "18:00");
        
        // Add sample appointments
        Patient p1 = patientManager.getPatientById(1);
        Patient p2 = patientManager.getPatientById(2);
        Doctor d1 = doctorManager.getDoctorById(1);
        Doctor d2 = doctorManager.getDoctorById(2);
        
        if (p1 != null && d1 != null) {
            appointmentManager.scheduleAppointment(p1, d1,
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                "Annual checkup");
        }
        
        if (p2 != null && d2 != null) {
            appointmentManager.scheduleAppointment(p2, d2,
                LocalDateTime.now().plusDays(2).withHour(14).withMinute(30),
                "Follow-up consultation");
        }
    }
}
