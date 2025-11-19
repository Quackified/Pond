package com.clinicapp.util;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DisplayHelper provides utility methods for formatting and displaying
 * data in a user-friendly console format with tables and visual separators.
 */
public class DisplayHelper {
    
    private static final int TABLE_WIDTH = 100;
    
    /**
     * Print a styled header banner.
     */
    public static void printHeader(String title) {
        System.out.println("\n" + "═".repeat(TABLE_WIDTH));
        System.out.println(centerText(title, TABLE_WIDTH));
        System.out.println("═".repeat(TABLE_WIDTH));
    }
    
    /**
     * Print a section divider.
     */
    public static void printDivider() {
        System.out.println("─".repeat(TABLE_WIDTH));
    }
    
    /**
     * Print a thick divider.
     */
    public static void printThickDivider() {
        System.out.println("═".repeat(TABLE_WIDTH));
    }
    
    /**
     * Center text within a given width.
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
    
    /**
     * Display a list of patients in table format.
     */
    public static void displayPatientTable(List<Patient> patients) {
        if (patients.isEmpty()) {
            System.out.println("\n❌ No patients found.");
            return;
        }
        
        System.out.println("\n┌" + "─".repeat(TABLE_WIDTH - 2) + "┐");
        System.out.println("│" + centerText("PATIENT LIST", TABLE_WIDTH - 2) + "│");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        System.out.printf("│ %-4s │ %-25s │ %-12s │ %-5s │ %-10s │ %-15s │%n", 
                         "ID", "Name", "DOB", "Age", "Gender", "Phone");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        
        for (Patient patient : patients) {
            System.out.printf("│ %-4d │ %-25s │ %-12s │ %-5d │ %-10s │ %-15s │%n",
                            patient.getId(),
                            truncate(patient.getName(), 25),
                            DateUtils.formatDateCompact(patient.getDateOfBirth()),
                            patient.getAge(),
                            truncate(patient.getGender(), 10),
                            truncate(patient.getPhoneNumber(), 15));
        }
        
        System.out.println("└" + "─".repeat(TABLE_WIDTH - 2) + "┘");
        System.out.println("Total patients: " + patients.size());
    }
    
    /**
     * Display a list of doctors in table format.
     */
    public static void displayDoctorTable(List<Doctor> doctors) {
        if (doctors.isEmpty()) {
            System.out.println("\n❌ No doctors found.");
            return;
        }
        
        System.out.println("\n┌" + "─".repeat(TABLE_WIDTH - 2) + "┐");
        System.out.println("│" + centerText("DOCTOR LIST", TABLE_WIDTH - 2) + "│");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        System.out.printf("│ %-4s │ %-25s │ %-25s │ %-15s │ %-12s │%n",
                         "ID", "Name", "Specialization", "Phone", "Status");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        
        for (Doctor doctor : doctors) {
            String status = doctor.isAvailable() ? "✓ Available" : "✗ Unavailable";
            System.out.printf("│ %-4d │ Dr. %-22s │ %-25s │ %-15s │ %-12s │%n",
                            doctor.getId(),
                            truncate(doctor.getName(), 22),
                            truncate(doctor.getSpecialization(), 25),
                            truncate(doctor.getPhoneNumber(), 15),
                            status);
        }
        
        System.out.println("└" + "─".repeat(TABLE_WIDTH - 2) + "┘");
        System.out.println("Total doctors: " + doctors.size());
    }
    
    /**
     * Display a list of appointments in table format.
     */
    public static void displayAppointmentTable(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("\n❌ No appointments found.");
            return;
        }
        
        System.out.println("\n┌" + "─".repeat(TABLE_WIDTH - 2) + "┐");
        System.out.println("│" + centerText("APPOINTMENT LIST", TABLE_WIDTH - 2) + "│");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        System.out.printf("│ %-4s │ %-16s │ %-20s │ %-20s │ %-20s │%n",
                         "ID", "Date & Time", "Patient", "Doctor", "Status");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        
        for (Appointment apt : appointments) {
            System.out.printf("│ %-4d │ %-16s │ %-20s │ Dr. %-17s │ %-20s │%n",
                            apt.getId(),
                            DateUtils.formatDateTimeCompact(apt.getAppointmentDateTime()),
                            truncate(apt.getPatient().getName(), 20),
                            truncate(apt.getDoctor().getName(), 17),
                            apt.getStatus());
        }
        
        System.out.println("└" + "─".repeat(TABLE_WIDTH - 2) + "┘");
        System.out.println("Total appointments: " + appointments.size());
    }
    
    /**
     * Display daily schedule view for a specific date.
     */
    public static void displayDailySchedule(LocalDate date, List<Appointment> appointments) {
        printHeader("DAILY SCHEDULE - " + DateUtils.formatDateCompact(date));
        
        if (appointments.isEmpty()) {
            System.out.println("\n❌ No appointments scheduled for this date.");
            return;
        }
        
        System.out.println("\n┌" + "─".repeat(TABLE_WIDTH - 2) + "┐");
        System.out.printf("│ %-8s │ %-20s │ %-20s │ %-30s │ %-12s │%n",
                         "Time", "Patient", "Doctor", "Reason", "Status");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        
        for (Appointment apt : appointments) {
            String time = DateUtils.formatTimeCompact(apt.getStartTime());
            System.out.printf("│ %-8s │ %-20s │ Dr. %-17s │ %-30s │ %-12s │%n",
                            time,
                            truncate(apt.getPatient().getName(), 20),
                            truncate(apt.getDoctor().getName(), 17),
                            truncate(apt.getReason(), 30),
                            apt.getStatus());
        }
        
        System.out.println("└" + "─".repeat(TABLE_WIDTH - 2) + "┘");
    }
    
    /**
     * Display daily statistics report.
     */
    public static void displayDailyReport(LocalDate date, Map<String, Integer> statistics) {
        printHeader("DAILY REPORT - " + DateUtils.formatDateCompact(date));
        
        System.out.println("\n┌" + "─".repeat(60) + "┐");
        System.out.println("│" + centerText("APPOINTMENT STATISTICS", 60) + "│");
        System.out.println("├" + "─".repeat(60) + "┤");
        
        System.out.printf("│ %-40s : %-15d │%n", "Total Appointments", statistics.getOrDefault("total", 0));
        System.out.printf("│ %-40s : %-15d │%n", "Scheduled", statistics.getOrDefault("scheduled", 0));
        System.out.printf("│ %-40s : %-15d │%n", "Confirmed", statistics.getOrDefault("confirmed", 0));
        System.out.printf("│ %-40s : %-15d │%n", "In Progress", statistics.getOrDefault("in_progress", 0));
        System.out.printf("│ %-40s : %-15d │%n", "Completed", statistics.getOrDefault("completed", 0));
        System.out.printf("│ %-40s : %-15d │%n", "Cancelled", statistics.getOrDefault("cancelled", 0));
        System.out.printf("│ %-40s : %-15d │%n", "No Show", statistics.getOrDefault("no_show", 0));
        
        System.out.println("└" + "─".repeat(60) + "┘");
    }
    
    /**
     * Display queue status.
     */
    public static void displayQueue(List<Appointment> queue) {
        printHeader("APPOINTMENT QUEUE");
        
        if (queue.isEmpty()) {
            System.out.println("\n✓ Queue is empty - no appointments waiting.");
            return;
        }
        
        System.out.println("\n┌" + "─".repeat(TABLE_WIDTH - 2) + "┐");
        System.out.printf("│ %-8s │ %-16s │ %-20s │ %-20s │ %-20s │%n",
                         "Position", "Date & Time", "Patient", "Doctor", "Reason");
        System.out.println("├" + "─".repeat(TABLE_WIDTH - 2) + "┤");
        
        int position = 1;
        for (Appointment apt : queue) {
            System.out.printf("│ %-8d │ %-16s │ %-20s │ Dr. %-17s │ %-20s │%n",
                            position++,
                            DateUtils.formatDateTimeCompact(apt.getAppointmentDateTime()),
                            truncate(apt.getPatient().getName(), 20),
                            truncate(apt.getDoctor().getName(), 17),
                            truncate(apt.getReason(), 20));
        }
        
        System.out.println("└" + "─".repeat(TABLE_WIDTH - 2) + "┘");
        System.out.println("Queue size: " + queue.size());
    }
    
    /**
     * Display success message with icon.
     */
    public static void displaySuccess(String message) {
        System.out.println("\n✓ SUCCESS: " + message);
    }
    
    /**
     * Display error message with icon.
     */
    public static void displayError(String message) {
        System.out.println("\n✗ ERROR: " + message);
    }
    
    /**
     * Display warning message with icon.
     */
    public static void displayWarning(String message) {
        System.out.println("\n⚠ WARNING: " + message);
    }
    
    /**
     * Display info message with icon.
     */
    public static void displayInfo(String message) {
        System.out.println("\nℹ INFO: " + message);
    }
    
    /**
     * Truncate string to specified length and add ellipsis if needed.
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Display a prompt and wait for enter key.
     */
    public static void pressEnterToContinue() {
        System.out.print("\nPress ENTER to continue...");
        try {
            System.in.read();
            // Clear the input buffer
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
            // Ignore
        }
    }
    
    /**
     * Clear console (simulate).
     */
    public static void clearScreen() {
        // Print multiple newlines to simulate clearing
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
