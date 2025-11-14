package com.clinicapp.dao;

import com.clinicapp.db.DatabaseConnection;
import com.clinicapp.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data Access Object for Doctor operations.
 * All database operations use UTF-8 encoding to prevent character encoding issues.
 */
public class DoctorDAO {
    private final DatabaseConnection dbConnection;
    
    public DoctorDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Adds a new doctor to the database with UTF-8 encoding.
     * 
     * @param doctor Doctor to add
     * @return Generated doctor ID
     * @throws SQLException if database error occurs
     */
    public int addDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (name, specialization, phone, email, schedule, is_available) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhoneNumber());
            stmt.setString(4, doctor.getEmail());
            
            String schedule = formatSchedule(doctor);
            stmt.setString(5, schedule);
            stmt.setBoolean(6, doctor.isAvailable());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    doctor.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to retrieve generated doctor ID");
    }
    
    /**
     * Retrieves all doctors from the database.
     * UTF-8 encoding ensures proper character display.
     * 
     * @return List of all doctors
     * @throws SQLException if database error occurs
     */
    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY id";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        }
        return doctors;
    }
    
    /**
     * Retrieves a doctor by ID.
     * 
     * @param id Doctor ID
     * @return Doctor object or null if not found
     * @throws SQLException if database error occurs
     */
    public Doctor getDoctorById(int id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractDoctorFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Retrieves all available doctors.
     * 
     * @return List of available doctors
     * @throws SQLException if database error occurs
     */
    public List<Doctor> getAvailableDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE is_available = true ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        }
        return doctors;
    }
    
    /**
     * Searches doctors by name (case-insensitive, partial match).
     * UTF-8 collation ensures proper international character matching.
     * 
     * @param name Name to search for
     * @return List of matching doctors
     * @throws SQLException if database error occurs
     */
    public List<Doctor> searchDoctorsByName(String name) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(extractDoctorFromResultSet(rs));
                }
            }
        }
        return doctors;
    }
    
    /**
     * Searches doctors by specialization.
     * UTF-8 collation ensures proper international character matching.
     * 
     * @param specialization Specialization to search for
     * @return List of matching doctors
     * @throws SQLException if database error occurs
     */
    public List<Doctor> searchDoctorsBySpecialization(String specialization) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization LIKE ? ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + specialization + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(extractDoctorFromResultSet(rs));
                }
            }
        }
        return doctors;
    }
    
    /**
     * Updates doctor information in the database.
     * 
     * @param doctor Doctor with updated information
     * @return true if update successful
     * @throws SQLException if database error occurs
     */
    public boolean updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET name = ?, specialization = ?, phone = ?, email = ?, " +
                    "schedule = ?, is_available = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhoneNumber());
            stmt.setString(4, doctor.getEmail());
            
            String schedule = formatSchedule(doctor);
            stmt.setString(5, schedule);
            stmt.setBoolean(6, doctor.isAvailable());
            stmt.setInt(7, doctor.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Updates doctor availability status.
     * 
     * @param id Doctor ID
     * @param isAvailable Availability status
     * @return true if update successful
     * @throws SQLException if database error occurs
     */
    public boolean updateDoctorAvailability(int id, boolean isAvailable) throws SQLException {
        String sql = "UPDATE doctors SET is_available = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, id);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Deletes a doctor from the database.
     * 
     * @param id Doctor ID to delete
     * @return true if deletion successful
     * @throws SQLException if database error occurs
     */
    public boolean deleteDoctor(int id) throws SQLException {
        String sql = "DELETE FROM doctors WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Formats doctor's schedule as a string for database storage.
     * 
     * @param doctor Doctor object
     * @return Formatted schedule string
     */
    private String formatSchedule(Doctor doctor) {
        String days = String.join(",", doctor.getAvailableDays());
        return String.format("%s|%s-%s", days, doctor.getStartTime(), doctor.getEndTime());
    }
    
    /**
     * Parses schedule string from database.
     * 
     * @param scheduleStr Schedule string from database
     * @return Array containing [days, startTime, endTime]
     */
    private String[] parseSchedule(String scheduleStr) {
        if (scheduleStr == null || scheduleStr.isEmpty()) {
            return new String[]{"", "09:00", "17:00"};
        }
        
        String[] parts = scheduleStr.split("\\|");
        if (parts.length < 2) {
            return new String[]{"", "09:00", "17:00"};
        }
        
        String[] times = parts[1].split("-");
        String startTime = times.length > 0 ? times[0] : "09:00";
        String endTime = times.length > 1 ? times[1] : "17:00";
        
        return new String[]{parts[0], startTime, endTime};
    }
    
    /**
     * Extracts Doctor object from ResultSet.
     * Handles UTF-8 encoded strings properly.
     * 
     * @param rs ResultSet positioned at doctor row
     * @return Doctor object
     * @throws SQLException if error reading data
     */
    private Doctor extractDoctorFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String specialization = rs.getString("specialization");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String scheduleStr = rs.getString("schedule");
        boolean isAvailable = rs.getBoolean("is_available");
        
        String[] schedule = parseSchedule(scheduleStr);
        List<String> availableDays = schedule[0].isEmpty() ? 
            new ArrayList<>() : Arrays.asList(schedule[0].split(","));
        String startTime = schedule[1];
        String endTime = schedule[2];
        
        return new Doctor(id, name, specialization, phone, email, availableDays, startTime, endTime, isAvailable);
    }
}
