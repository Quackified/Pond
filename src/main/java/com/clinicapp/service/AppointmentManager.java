package com.clinicapp.service;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * AppointmentManager coordinates all appointment-related operations, integrating patients and doctors.
 * 
 * Data Structures Used:
 * 1. ArrayList<Appointment>: Main appointment schedule storage
 *    - Provides O(1) indexed access for efficient lookups
 *    - Dynamic resizing for flexible appointment management
 *    - Easy iteration for displaying and filtering appointments
 * 
 * 2. Queue<Patient> (LinkedList): Walk-in patient queue
 *    - FIFO (First-In-First-Out) structure ensures fair ordering
 *    - LinkedList implementation provides O(1) enqueue/dequeue operations
 *    - Perfect for managing patients waiting without scheduled appointments
 * 
 * 3. Stack<Appointment>: Appointment history for undo operations
 *    - LIFO (Last-In-First-Out) structure for tracking recent changes
 *    - Enables undo functionality by popping most recent operations
 *    - Maintains audit trail of appointment modifications
 * 
 * Requirements covered: 11-14, 16-19 (Appointment CRUD), 24 (History), 27 (Undo)
 */
public class AppointmentManager {
    
    /**
     * Main appointment schedule storage using ArrayList.
     * ArrayList provides dynamic sizing and fast index-based access for appointment management.
     */
    private ArrayList<Appointment> appointments;
    
    /**
     * Walk-in patient queue using LinkedList as Queue implementation.
     * Queue follows FIFO principle: patients who arrive first are served first.
     * LinkedList provides efficient O(1) operations for add (offer) and remove (poll).
     */
    private Queue<Patient> walkInQueue;
    
    /**
     * Appointment history stack for undo operations.
     * Stack follows LIFO principle: most recent operations can be undone first.
     * Stores cancelled or rescheduled appointments for potential restoration.
     */
    private Stack<Appointment> appointmentHistory;
    
    private PatientManager patientManager;
    private DoctorManager doctorManager;
    private int appointmentIdCounter;
    
    public AppointmentManager(PatientManager patientManager, DoctorManager doctorManager) {
        this.appointments = new ArrayList<>();
        this.walkInQueue = new LinkedList<>();
        this.appointmentHistory = new Stack<>();
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
        this.appointmentIdCounter = 1000;
    }
    
    /**
     * Schedules a new appointment.
     * Validates patient, doctor, and time slot availability before creating appointment.
     * 
     * @param patientId The ID of the patient
     * @param doctorId The ID of the doctor
     * @param appointmentDateTime The date and time for the appointment
     * @return true if appointment was scheduled successfully, false otherwise
     */
    public boolean scheduleAppointment(String patientId, String doctorId, LocalDateTime appointmentDateTime) {
        Patient patient = patientManager.findPatientById(patientId);
        if (patient == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found");
            return false;
        }
        
        Doctor doctor = doctorManager.findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Error: Doctor with ID " + doctorId + " not found");
            return false;
        }
        
        if (!isTimeSlotAvailable(doctorId, appointmentDateTime)) {
            System.out.println("Error: Time slot is not available for doctor " + doctor.getName());
            return false;
        }
        
        String appointmentId = generateAppointmentId();
        Appointment appointment = new Appointment(appointmentId, patient, doctor, appointmentDateTime);
        appointments.add(appointment);
        
        System.out.println("Success: Appointment scheduled successfully");
        System.out.println("  Appointment ID: " + appointmentId);
        System.out.println("  Patient: " + patient.getName());
        System.out.println("  Doctor: " + doctor.getName());
        System.out.println("  Time: " + appointmentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        
        return true;
    }
    
    /**
     * Checks if a time slot is available for a specific doctor.
     * A slot is available if no other scheduled appointment exists at that time.
     * 
     * @param doctorId The ID of the doctor
     * @param dateTime The date and time to check
     * @return true if the time slot is available, false otherwise
     */
    public boolean isTimeSlotAvailable(String doctorId, LocalDateTime dateTime) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getDoctorId().equals(doctorId) &&
                appointment.getAppointmentDateTime().equals(dateTime) &&
                appointment.getStatus().equals(Appointment.STATUS_SCHEDULED)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retrieves all available time slots for a doctor on a specific date.
     * 
     * @param doctorId The ID of the doctor
     * @param date The date to check
     * @return List of available time slots
     */
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDateTime date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startOfDay = date.withHour(9).withMinute(0);
        LocalDateTime endOfDay = date.withHour(17).withMinute(0);
        
        for (LocalDateTime slot = startOfDay; slot.isBefore(endOfDay); slot = slot.plusMinutes(30)) {
            if (isTimeSlotAvailable(doctorId, slot)) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Finds an appointment by its unique ID.
     * 
     * @param appointmentId The ID of the appointment
     * @return The appointment if found, null otherwise
     */
    public Appointment findAppointmentById(String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            return null;
        }
        
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }
    
    /**
     * Retrieves all appointments for a specific patient.
     * 
     * @param patientId The ID of the patient
     * @return List of appointments for the patient
     */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.stream()
                .filter(appointment -> appointment.getPatient().getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves all appointments for a specific doctor.
     * 
     * @param doctorId The ID of the doctor
     * @return List of appointments for the doctor
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.stream()
                .filter(appointment -> appointment.getDoctor().getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves all scheduled appointments (not cancelled or completed).
     * 
     * @return List of scheduled appointments
     */
    public List<Appointment> getScheduledAppointments() {
        return appointments.stream()
                .filter(appointment -> appointment.getStatus().equals(Appointment.STATUS_SCHEDULED))
                .collect(Collectors.toList());
    }
    
    /**
     * Cancels an appointment and pushes it to the history stack.
     * The Stack data structure allows for potential undo of this cancellation.
     * 
     * @param appointmentId The ID of the appointment to cancel
     * @return true if cancellation was successful, false otherwise
     */
    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Error: Appointment with ID " + appointmentId + " not found");
            return false;
        }
        
        if (appointment.getStatus().equals(Appointment.STATUS_CANCELLED)) {
            System.out.println("Warning: Appointment is already cancelled");
            return false;
        }
        
        String previousStatus = appointment.getStatus();
        appointment.setStatus(Appointment.STATUS_CANCELLED);
        
        Appointment historyCopy = createAppointmentCopy(appointment);
        historyCopy.setStatus(previousStatus);
        appointmentHistory.push(historyCopy);
        
        System.out.println("Success: Appointment " + appointmentId + " cancelled");
        System.out.println("  Patient: " + appointment.getPatient().getName());
        System.out.println("  Doctor: " + appointment.getDoctor().getName());
        System.out.println("  Original Time: " + appointment.getAppointmentDateTime().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        
        return true;
    }
    
    /**
     * Reschedules an existing appointment to a new date and time.
     * Pushes the original appointment to history stack for potential undo.
     * 
     * @param appointmentId The ID of the appointment to reschedule
     * @param newDateTime The new date and time
     * @return true if rescheduling was successful, false otherwise
     */
    public boolean rescheduleAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Error: Appointment with ID " + appointmentId + " not found");
            return false;
        }
        
        if (appointment.getStatus().equals(Appointment.STATUS_CANCELLED)) {
            System.out.println("Error: Cannot reschedule a cancelled appointment");
            return false;
        }
        
        String doctorId = appointment.getDoctor().getDoctorId();
        if (!isTimeSlotAvailable(doctorId, newDateTime)) {
            System.out.println("Error: New time slot is not available");
            return false;
        }
        
        Appointment historyCopy = createAppointmentCopy(appointment);
        appointmentHistory.push(historyCopy);
        
        LocalDateTime oldDateTime = appointment.getAppointmentDateTime();
        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(Appointment.STATUS_RESCHEDULED);
        
        System.out.println("Success: Appointment rescheduled");
        System.out.println("  Appointment ID: " + appointmentId);
        System.out.println("  Patient: " + appointment.getPatient().getName());
        System.out.println("  Doctor: " + appointment.getDoctor().getName());
        System.out.println("  Old Time: " + oldDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("  New Time: " + newDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        
        return true;
    }
    
    /**
     * Updates appointment notes or status.
     * 
     * @param appointmentId The ID of the appointment to update
     * @param notes The notes to add
     * @param status The new status (optional, can be null to keep current status)
     * @return true if update was successful, false otherwise
     */
    public boolean updateAppointment(String appointmentId, String notes, String status) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Error: Appointment with ID " + appointmentId + " not found");
            return false;
        }
        
        if (notes != null) {
            appointment.setNotes(notes);
        }
        
        if (status != null && !status.trim().isEmpty()) {
            appointment.setStatus(status);
        }
        
        System.out.println("Success: Appointment " + appointmentId + " updated");
        return true;
    }
    
    /**
     * Adds a patient to the walk-in queue.
     * Queue (FIFO) ensures patients are served in the order they arrive.
     * 
     * @param patientId The ID of the patient to add to walk-in queue
     * @return true if patient was added to queue, false otherwise
     */
    public boolean addToWalkInQueue(String patientId) {
        Patient patient = patientManager.findPatientById(patientId);
        if (patient == null) {
            System.out.println("Error: Patient with ID " + patientId + " not found");
            return false;
        }
        
        if (walkInQueue.contains(patient)) {
            System.out.println("Warning: Patient " + patient.getName() + " is already in the walk-in queue");
            return false;
        }
        
        walkInQueue.offer(patient);
        System.out.println("Success: Patient " + patient.getName() + " added to walk-in queue");
        System.out.println("  Queue position: " + walkInQueue.size());
        
        return true;
    }
    
    /**
     * Processes the next patient in the walk-in queue.
     * Removes and returns the patient at the front of the queue (FIFO).
     * 
     * @return The next patient in the queue, or null if queue is empty
     */
    public Patient processNextWalkIn() {
        if (walkInQueue.isEmpty()) {
            System.out.println("Walk-in queue is empty");
            return null;
        }
        
        Patient patient = walkInQueue.poll();
        System.out.println("Processing walk-in patient: " + patient.getName());
        System.out.println("  Patient ID: " + patient.getPatientId());
        System.out.println("  Remaining in queue: " + walkInQueue.size());
        
        return patient;
    }
    
    /**
     * Views the next patient in the walk-in queue without removing them.
     * 
     * @return The next patient in the queue, or null if queue is empty
     */
    public Patient peekNextWalkIn() {
        return walkInQueue.peek();
    }
    
    /**
     * Gets the current size of the walk-in queue.
     * 
     * @return The number of patients in the walk-in queue
     */
    public int getWalkInQueueSize() {
        return walkInQueue.size();
    }
    
    /**
     * Displays all patients currently in the walk-in queue.
     */
    public void displayWalkInQueue() {
        if (walkInQueue.isEmpty()) {
            System.out.println("Walk-in queue is empty");
            return;
        }
        
        System.out.println("\n===== Walk-In Queue =====");
        int position = 1;
        for (Patient patient : walkInQueue) {
            System.out.println(position + ". " + patient.getName() + " (ID: " + patient.getPatientId() + ")");
            position++;
        }
        System.out.println("Total in queue: " + walkInQueue.size());
    }
    
    /**
     * Undoes the last cancelled or rescheduled appointment.
     * Pops from the history stack (LIFO) to restore the most recent change.
     * 
     * @return true if undo was successful, false if history is empty
     */
    public boolean undoLastOperation() {
        if (appointmentHistory.isEmpty()) {
            System.out.println("No operations to undo");
            return false;
        }
        
        Appointment historicalAppointment = appointmentHistory.pop();
        Appointment currentAppointment = findAppointmentById(historicalAppointment.getAppointmentId());
        
        if (currentAppointment != null) {
            currentAppointment.setAppointmentDateTime(historicalAppointment.getAppointmentDateTime());
            currentAppointment.setStatus(historicalAppointment.getStatus());
            currentAppointment.setNotes(historicalAppointment.getNotes());
            
            System.out.println("Success: Undo operation completed");
            System.out.println("  Appointment ID: " + currentAppointment.getAppointmentId());
            System.out.println("  Restored to status: " + historicalAppointment.getStatus());
        }
        
        return true;
    }
    
    /**
     * Views the history of operations without modifying the stack.
     * 
     * @return List of appointments in history (most recent first)
     */
    public List<Appointment> viewHistory() {
        return new ArrayList<>(appointmentHistory);
    }
    
    /**
     * Gets the count of items in the history stack.
     * 
     * @return The number of historical appointments
     */
    public int getHistorySize() {
        return appointmentHistory.size();
    }
    
    /**
     * Retrieves all appointments in the system.
     * 
     * @return List of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
    
    /**
     * Sorts appointments by date and time (earliest first).
     * 
     * @return Sorted list of appointments
     */
    public List<Appointment> getAppointmentsSortedByTime() {
        List<Appointment> sorted = new ArrayList<>(appointments);
        sorted.sort(Comparator.comparing(Appointment::getAppointmentDateTime));
        return sorted;
    }
    
    /**
     * Sorts appointments by doctor name.
     * 
     * @return Sorted list of appointments
     */
    public List<Appointment> getAppointmentsSortedByDoctor() {
        List<Appointment> sorted = new ArrayList<>(appointments);
        sorted.sort(Comparator.comparing(appointment -> appointment.getDoctor().getName()));
        return sorted;
    }
    
    /**
     * Sorts appointments by patient name.
     * 
     * @return Sorted list of appointments
     */
    public List<Appointment> getAppointmentsSortedByPatient() {
        List<Appointment> sorted = new ArrayList<>(appointments);
        sorted.sort(Comparator.comparing(appointment -> appointment.getPatient().getName()));
        return sorted;
    }
    
    /**
     * Displays all appointments in a formatted table.
     */
    public void displayAllAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled");
            return;
        }
        
        System.out.println("\n===== Appointment Schedule =====");
        System.out.println(String.format("%-12s %-20s %-20s %-18s %-12s", 
                "Appt ID", "Patient", "Doctor", "Date & Time", "Status"));
        System.out.println("--------------------------------------------------------------------------------");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Appointment appointment : appointments) {
            System.out.println(String.format("%-12s %-20s %-20s %-18s %-12s",
                    appointment.getAppointmentId(),
                    appointment.getPatient().getName(),
                    appointment.getDoctor().getName(),
                    appointment.getAppointmentDateTime().format(formatter),
                    appointment.getStatus()));
        }
        System.out.println("Total appointments: " + appointments.size());
    }
    
    /**
     * Displays scheduled appointments sorted by time.
     */
    public void displayScheduledAppointmentsByTime() {
        List<Appointment> scheduled = getAppointmentsSortedByTime().stream()
                .filter(appointment -> appointment.getStatus().equals(Appointment.STATUS_SCHEDULED))
                .collect(Collectors.toList());
        
        if (scheduled.isEmpty()) {
            System.out.println("No scheduled appointments");
            return;
        }
        
        System.out.println("\n===== Scheduled Appointments (Sorted by Time) =====");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Appointment appointment : scheduled) {
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("  Patient: " + appointment.getPatient().getName() + 
                    " (ID: " + appointment.getPatient().getPatientId() + ")");
            System.out.println("  Doctor: " + appointment.getDoctor().getName() + 
                    " (Specialization: " + appointment.getDoctor().getSpecialization() + ")");
            System.out.println("  Date & Time: " + appointment.getAppointmentDateTime().format(formatter));
            if (!appointment.getNotes().isEmpty()) {
                System.out.println("  Notes: " + appointment.getNotes());
            }
            System.out.println("------------------------");
        }
    }
    
    /**
     * Generates a unique appointment ID.
     * 
     * @return A unique appointment ID
     */
    private String generateAppointmentId() {
        return "APT" + (appointmentIdCounter++);
    }
    
    /**
     * Creates a deep copy of an appointment for history tracking.
     * 
     * @param original The original appointment
     * @return A copy of the appointment
     */
    private Appointment createAppointmentCopy(Appointment original) {
        Appointment copy = new Appointment(
                original.getAppointmentId(),
                original.getPatient(),
                original.getDoctor(),
                original.getAppointmentDateTime(),
                original.getStatus()
        );
        copy.setNotes(original.getNotes());
        return copy;
    }
    
    /**
     * Clears all appointments (useful for testing).
     */
    public void clearAllAppointments() {
        appointments.clear();
        walkInQueue.clear();
        appointmentHistory.clear();
        System.out.println("All appointments, walk-in queue, and history cleared");
    }
}
