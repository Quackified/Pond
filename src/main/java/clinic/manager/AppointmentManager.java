package clinic.manager;

import clinic.model.Appointment;
import clinic.model.Appointment.AppointmentStatus;
import clinic.model.Doctor;
import clinic.model.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AppointmentManager handles all appointment-related operations including
 * scheduling, updating, cancelling appointments, queue management, and undo functionality.
 * Uses Stack for undo operations and Queue for appointment processing.
 */
public class AppointmentManager {
    // HashMap for O(1) lookup by appointment ID
    private final Map<Integer, Appointment> appointments;
    
    // Stack to support undo functionality - stores last action details
    private final Stack<AppointmentAction> undoStack;
    
    // Queue for processing appointments in order (FIFO)
    private final Queue<Appointment> appointmentQueue;
    
    // Reference to managers for validation
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;
    
    /**
     * Inner class to represent an appointment action for undo functionality.
     */
    private static class AppointmentAction {
        enum ActionType { ADD, UPDATE, CANCEL, COMPLETE }
        
        ActionType type;
        Appointment appointment;
        Appointment previousState; // For UPDATE actions
        
        AppointmentAction(ActionType type, Appointment appointment, Appointment previousState) {
            this.type = type;
            this.appointment = appointment;
            this.previousState = previousState;
        }
    }
    
    /**
     * Constructor initializes appointment storage and undo/queue structures.
     */
    public AppointmentManager(PatientManager patientManager, DoctorManager doctorManager) {
        this.appointments = new HashMap<>();
        this.undoStack = new Stack<>();
        this.appointmentQueue = new LinkedList<>();
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
    }
    
    /**
     * Schedule a new appointment.
     * Validates that patient and doctor exist before creating appointment.
     * 
     * @param patient Patient for the appointment
     * @param doctor Doctor for the appointment
     * @param dateTime Date and time of appointment
     * @param reason Reason for visit
     * @return The newly created Appointment object, or null if validation fails
     */
    public Appointment scheduleAppointment(Patient patient, Doctor doctor, 
                                          LocalDateTime dateTime, String reason) {
        // Validate patient and doctor exist
        if (patient == null || doctor == null) {
            return null;
        }
        
        // Check for scheduling conflicts
        if (hasConflict(doctor, dateTime)) {
            return null;
        }
        
        // Create new appointment
        Appointment appointment = new Appointment(patient, doctor, dateTime, reason);
        appointments.put(appointment.getId(), appointment);
        
        // Add to queue for processing
        appointmentQueue.offer(appointment);
        
        // Record action for undo
        undoStack.push(new AppointmentAction(AppointmentAction.ActionType.ADD, 
                                            appointment, null));
        
        return appointment;
    }
    
    /**
     * Check if doctor has a scheduling conflict at the given time.
     * Considers appointments within 30 minutes as conflicts.
     */
    private boolean hasConflict(Doctor doctor, LocalDateTime dateTime) {
        for (Appointment apt : appointments.values()) {
            // Only check scheduled or confirmed appointments
            if ((apt.getStatus() == AppointmentStatus.SCHEDULED || 
                 apt.getStatus() == AppointmentStatus.CONFIRMED) &&
                apt.getDoctor().getId() == doctor.getId()) {
                
                LocalDateTime aptTime = apt.getAppointmentDateTime();
                long minutesDiff = Math.abs(java.time.Duration.between(dateTime, aptTime).toMinutes());
                
                // If within 30 minutes, it's a conflict
                if (minutesDiff < 30) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get an appointment by ID.
     */
    public Appointment getAppointmentById(int id) {
        return appointments.get(id);
    }
    
    /**
     * Get all appointments in the system.
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }
    
    /**
     * Get appointments by status.
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointments.values().stream()
                .filter(apt -> apt.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Get appointments for a specific patient.
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointments.values().stream()
                .filter(apt -> apt.getPatient().getId() == patientId)
                .collect(Collectors.toList());
    }
    
    /**
     * Get appointments for a specific doctor.
     */
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointments.values().stream()
                .filter(apt -> apt.getDoctor().getId() == doctorId)
                .collect(Collectors.toList());
    }
    
    /**
     * Get appointments for a specific date.
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointments.values().stream()
                .filter(apt -> apt.getAppointmentDateTime().toLocalDate().equals(date))
                .sorted(Comparator.comparing(Appointment::getAppointmentDateTime))
                .collect(Collectors.toList());
    }
    
    /**
     * Update appointment details.
     */
    public boolean updateAppointment(int id, LocalDateTime newDateTime, String newReason, String notes) {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            return false;
        }
        
        // Create a copy for undo
        Appointment previousState = cloneAppointment(appointment);
        
        // Update appointment
        if (newDateTime != null) {
            // Check for conflicts with new time
            if (hasConflict(appointment.getDoctor(), newDateTime)) {
                return false;
            }
            appointment.setAppointmentDateTime(newDateTime);
        }
        if (newReason != null) appointment.setReason(newReason);
        if (notes != null) appointment.setNotes(notes);
        
        // Record action for undo
        undoStack.push(new AppointmentAction(AppointmentAction.ActionType.UPDATE,
                                            appointment, previousState));
        
        return true;
    }
    
    /**
     * Confirm an appointment.
     */
    public boolean confirmAppointment(int id) {
        Appointment appointment = appointments.get(id);
        if (appointment == null || appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            return false;
        }
        
        Appointment previousState = cloneAppointment(appointment);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        
        undoStack.push(new AppointmentAction(AppointmentAction.ActionType.UPDATE,
                                            appointment, previousState));
        return true;
    }
    
    /**
     * Cancel an appointment.
     */
    public boolean cancelAppointment(int id) {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            return false;
        }
        
        Appointment previousState = cloneAppointment(appointment);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        
        // Remove from queue if present
        appointmentQueue.remove(appointment);
        
        undoStack.push(new AppointmentAction(AppointmentAction.ActionType.CANCEL,
                                            appointment, previousState));
        return true;
    }
    
    /**
     * Mark appointment as completed.
     */
    public boolean completeAppointment(int id, String notes) {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            return false;
        }
        
        Appointment previousState = cloneAppointment(appointment);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        if (notes != null) {
            appointment.setNotes(notes);
        }
        
        // Remove from queue if present
        appointmentQueue.remove(appointment);
        
        undoStack.push(new AppointmentAction(AppointmentAction.ActionType.COMPLETE,
                                            appointment, previousState));
        return true;
    }
    
    /**
     * Mark appointment as no-show.
     */
    public boolean markNoShow(int id) {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            return false;
        }
        
        Appointment previousState = cloneAppointment(appointment);
        appointment.setStatus(AppointmentStatus.NO_SHOW);
        
        // Remove from queue if present
        appointmentQueue.remove(appointment);
        
        undoStack.push(new AppointmentAction(AppointmentAction.ActionType.UPDATE,
                                            appointment, previousState));
        return true;
    }
    
    /**
     * Process next appointment in queue.
     * Changes status from SCHEDULED/CONFIRMED to IN_PROGRESS.
     */
    public Appointment processNextInQueue() {
        Appointment appointment = appointmentQueue.poll();
        if (appointment != null && appointments.containsKey(appointment.getId())) {
            Appointment previousState = cloneAppointment(appointment);
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            
            undoStack.push(new AppointmentAction(AppointmentAction.ActionType.UPDATE,
                                                appointment, previousState));
        }
        return appointment;
    }
    
    /**
     * Get current queue size.
     */
    public int getQueueSize() {
        return appointmentQueue.size();
    }
    
    /**
     * View appointments in queue without removing them.
     */
    public List<Appointment> viewQueue() {
        return new ArrayList<>(appointmentQueue);
    }
    
    /**
     * Undo the last appointment action.
     * Supports undoing add, update, cancel, and complete actions.
     * 
     * @return true if undo was successful, false if nothing to undo
     */
    public boolean undoLastAction() {
        if (undoStack.isEmpty()) {
            return false;
        }
        
        AppointmentAction action = undoStack.pop();
        
        switch (action.type) {
            case ADD:
                // Remove the appointment that was added
                appointments.remove(action.appointment.getId());
                appointmentQueue.remove(action.appointment);
                break;
                
            case UPDATE:
            case CANCEL:
            case COMPLETE:
                // Restore previous state
                if (action.previousState != null) {
                    Appointment current = appointments.get(action.appointment.getId());
                    if (current != null) {
                        restoreAppointmentState(current, action.previousState);
                        
                        // Re-add to queue if it was scheduled/confirmed
                        if (current.getStatus() == AppointmentStatus.SCHEDULED ||
                            current.getStatus() == AppointmentStatus.CONFIRMED) {
                            if (!appointmentQueue.contains(current)) {
                                appointmentQueue.offer(current);
                            }
                        }
                    }
                }
                break;
        }
        
        return true;
    }
    
    /**
     * Check if there are actions that can be undone.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    /**
     * Get count of actions that can be undone.
     */
    public int getUndoStackSize() {
        return undoStack.size();
    }
    
    /**
     * Get completed appointments.
     */
    public List<Appointment> getCompletedAppointments() {
        return getAppointmentsByStatus(AppointmentStatus.COMPLETED);
    }
    
    /**
     * Get appointment history for reporting.
     */
    public List<Appointment> getAppointmentHistory(LocalDate startDate, LocalDate endDate) {
        return appointments.values().stream()
                .filter(apt -> {
                    LocalDate aptDate = apt.getAppointmentDateTime().toLocalDate();
                    return !aptDate.isBefore(startDate) && !aptDate.isAfter(endDate);
                })
                .sorted(Comparator.comparing(Appointment::getAppointmentDateTime))
                .collect(Collectors.toList());
    }
    
    /**
     * Get daily report statistics.
     */
    public Map<String, Integer> getDailyStatistics(LocalDate date) {
        List<Appointment> dailyAppointments = getAppointmentsByDate(date);
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", dailyAppointments.size());
        stats.put("scheduled", 0);
        stats.put("confirmed", 0);
        stats.put("in_progress", 0);
        stats.put("completed", 0);
        stats.put("cancelled", 0);
        stats.put("no_show", 0);
        
        for (Appointment apt : dailyAppointments) {
            String status = apt.getStatus().toString().toLowerCase();
            stats.put(status, stats.getOrDefault(status, 0) + 1);
        }
        
        return stats;
    }
    
    /**
     * Clone an appointment for undo functionality.
     */
    private Appointment cloneAppointment(Appointment apt) {
        Appointment clone = new Appointment(apt.getPatient(), apt.getDoctor(),
                                           apt.getAppointmentDateTime(), apt.getReason());
        clone.setStatus(apt.getStatus());
        clone.setNotes(apt.getNotes());
        return clone;
    }
    
    /**
     * Restore appointment to previous state.
     */
    private void restoreAppointmentState(Appointment current, Appointment previous) {
        current.setAppointmentDateTime(previous.getAppointmentDateTime());
        current.setReason(previous.getReason());
        current.setStatus(previous.getStatus());
        current.setNotes(previous.getNotes());
    }
    
    /**
     * Delete an appointment (for administrative purposes).
     */
    public boolean deleteAppointment(int id) {
        Appointment removed = appointments.remove(id);
        if (removed != null) {
            appointmentQueue.remove(removed);
            return true;
        }
        return false;
    }
    
    /**
     * Get total appointment count.
     */
    public int getAppointmentCount() {
        return appointments.size();
    }
}
