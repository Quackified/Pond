package com.clinicapp.dao;

import com.clinicapp.db.DatabaseConnection;
import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Appointment operations.
 * All database operations use UTF-8 encoding to prevent character encoding issues.
 */
public class AppointmentDAO {
    private final DatabaseConnection dbConnection;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;
    
    public AppointmentDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
    }
    
    /**
     * Adds a new appointment to the database with UTF-8 encoding.
     * 
     * @param appointment Appointment to add
     * @return Generated appointment ID
     * @throws SQLException if database error occurs
     */
    public int addAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, reason, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, appointment.getPatient().getId());
            stmt.setInt(2, appointment.getDoctor().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            stmt.setString(4, appointment.getReason());
            stmt.setString(5, appointment.getStatus().name());
            stmt.setString(6, appointment.getNotes());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    appointment.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to retrieve generated appointment ID");
    }
    
    /**
     * Retrieves all appointments from the database.
     * UTF-8 encoding ensures proper character display.
     * 
     * @return List of all appointments
     * @throws SQLException if database error occurs
     */
    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_time";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        }
        return appointments;
    }
    
    /**
     * Retrieves an appointment by ID.
     * 
     * @param id Appointment ID
     * @return Appointment object or null if not found
     * @throws SQLException if database error occurs
     */
    public Appointment getAppointmentById(int id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAppointmentFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Retrieves all appointments for a specific patient.
     * 
     * @param patientId Patient ID
     * @return List of appointments for the patient
     * @throws SQLException if database error occurs
     */
    public List<Appointment> getAppointmentsByPatient(int patientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_time";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, patientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }
        }
        return appointments;
    }
    
    /**
     * Retrieves all appointments for a specific doctor.
     * 
     * @param doctorId Doctor ID
     * @return List of appointments for the doctor
     * @throws SQLException if database error occurs
     */
    public List<Appointment> getAppointmentsByDoctor(int doctorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_time";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, doctorId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }
        }
        return appointments;
    }
    
    /**
     * Retrieves all appointments for a specific date.
     * 
     * @param date Date to search for
     * @return List of appointments on the date
     * @throws SQLException if database error occurs
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE DATE(appointment_time) = ? ORDER BY appointment_time";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }
        }
        return appointments;
    }
    
    /**
     * Retrieves appointments by status.
     * 
     * @param status Appointment status
     * @return List of appointments with the status
     * @throws SQLException if database error occurs
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE status = ? ORDER BY appointment_time";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }
        }
        return appointments;
    }
    
    /**
     * Updates appointment information in the database.
     * 
     * @param appointment Appointment with updated information
     * @return true if update successful
     * @throws SQLException if database error occurs
     */
    public boolean updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_time = ?, " +
                    "reason = ?, status = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointment.getPatient().getId());
            stmt.setInt(2, appointment.getDoctor().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentDateTime()));
            stmt.setString(4, appointment.getReason());
            stmt.setString(5, appointment.getStatus().name());
            stmt.setString(6, appointment.getNotes());
            stmt.setInt(7, appointment.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Updates appointment status.
     * 
     * @param id Appointment ID
     * @param status New status
     * @return true if update successful
     * @throws SQLException if database error occurs
     */
    public boolean updateAppointmentStatus(int id, AppointmentStatus status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Deletes an appointment from the database.
     * 
     * @param id Appointment ID to delete
     * @return true if deletion successful
     * @throws SQLException if database error occurs
     */
    public boolean deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Extracts Appointment object from ResultSet.
     * Handles UTF-8 encoded strings properly.
     * 
     * @param rs ResultSet positioned at appointment row
     * @return Appointment object
     * @throws SQLException if error reading data
     */
    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patient_id");
        int doctorId = rs.getInt("doctor_id");
        LocalDateTime appointmentTime = rs.getTimestamp("appointment_time").toLocalDateTime();
        String reason = rs.getString("reason");
        String statusStr = rs.getString("status");
        String notes = rs.getString("notes");
        
        Patient patient = patientDAO.getPatientById(patientId);
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        AppointmentStatus status = AppointmentStatus.valueOf(statusStr);
        
        return new Appointment(id, patient, doctor, appointmentTime, reason, status, notes);
    }
}
