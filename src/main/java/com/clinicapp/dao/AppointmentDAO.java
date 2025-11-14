package com.clinicapp.dao;

import com.clinicapp.db.DatabaseConnection;
import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;
import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentDAO - Data Access Object for Appointment operations.
 * Handles all database operations related to appointments.
 */
public class AppointmentDAO {
    
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    
    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Add a new appointment to the database.
     */
    public Appointment addAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, reason, status, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPatient().getId());
            stmt.setInt(2, appointment.getDoctor().getId());
            stmt.setDate(3, Date.valueOf(appointment.getAppointmentDateTime().toLocalDate()));
            stmt.setTime(4, Time.valueOf(appointment.getAppointmentDateTime().toLocalTime()));
            stmt.setString(5, appointment.getReason());
            stmt.setString(6, appointment.getStatus().name());
            stmt.setString(7, appointment.getNotes());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getInt(1));
                }
            }
            
            return appointment;
        } catch (SQLException e) {
            System.err.println("Error adding appointment: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get appointment by ID.
     */
    public Appointment getAppointmentById(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAppointmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all appointments.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date, appointment_time";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all appointments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Get appointments by patient ID.
     */
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date, appointment_time";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments by patient: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Get appointments by doctor ID.
     */
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date, appointment_time";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments by doctor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Get appointments by date.
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE appointment_date = ? ORDER BY appointment_time";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments by date: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Update appointment information.
     */
    public boolean updateAppointment(Appointment appointment) {
        String sql = "UPDATE appointments SET appointment_date = ?, appointment_time = ?, " +
                     "reason = ?, status = ?, notes = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(appointment.getAppointmentDateTime().toLocalDate()));
            stmt.setTime(2, Time.valueOf(appointment.getAppointmentDateTime().toLocalTime()));
            stmt.setString(3, appointment.getReason());
            stmt.setString(4, appointment.getStatus().name());
            stmt.setString(5, appointment.getNotes());
            stmt.setInt(6, appointment.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete appointment by ID.
     */
    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Extract Appointment object from ResultSet.
     */
    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patient_id");
        int doctorId = rs.getInt("doctor_id");
        LocalDate date = rs.getDate("appointment_date").toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(date, rs.getTime("appointment_time").toLocalTime());
        String reason = rs.getString("reason");
        AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("status"));
        String notes = rs.getString("notes");
        Timestamp createdTimestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : LocalDateTime.now();
        
        Patient patient = patientDAO.getPatientById(patientId);
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        
        return new Appointment(id, patient, doctor, dateTime, reason, status, notes, createdAt);
    }
}
