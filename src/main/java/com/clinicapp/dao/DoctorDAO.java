package com.clinicapp.dao;

import com.clinicapp.db.DatabaseConnection;
import com.clinicapp.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DoctorDAO - Data Access Object for Doctor operations.
 * Handles all database operations related to doctors.
 */
public class DoctorDAO {
    
    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Add a new doctor to the database.
     */
    public Doctor addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization, phone, email, office_location, schedule, is_available) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhoneNumber());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, ""); // office_location not in current model
            stmt.setString(6, serializeSchedule(doctor));
            stmt.setBoolean(7, doctor.isAvailable());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    doctor.setId(generatedKeys.getInt(1));
                }
            }
            
            return doctor;
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get doctor by ID.
     */
    public Doctor getDoctorById(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractDoctorFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting doctor: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all doctors.
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY id";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all doctors: " + e.getMessage());
            e.printStackTrace();
        }
        
        return doctors;
    }
    
    /**
     * Search doctors by name.
     */
    public List<Doctor> searchDoctorsByName(String searchTerm) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE name LIKE ? ORDER BY name";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching doctors: " + e.getMessage());
            e.printStackTrace();
        }
        
        return doctors;
    }
    
    /**
     * Search doctors by specialization.
     */
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization LIKE ? ORDER BY name";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + specialization + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching doctors by specialization: " + e.getMessage());
            e.printStackTrace();
        }
        
        return doctors;
    }
    
    /**
     * Update doctor information.
     */
    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name = ?, specialization = ?, phone = ?, " +
                     "email = ?, schedule = ?, is_available = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhoneNumber());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, serializeSchedule(doctor));
            stmt.setBoolean(6, doctor.isAvailable());
            stmt.setInt(7, doctor.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete doctor by ID.
     */
    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Extract Doctor object from ResultSet.
     */
    private Doctor extractDoctorFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String specialization = rs.getString("specialization");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String schedule = rs.getString("schedule");
        boolean isAvailable = rs.getBoolean("is_available");
        
        String[] parts = deserializeSchedule(schedule);
        List<String> availableDays = Arrays.asList(parts[0].split(","));
        String startTime = parts.length > 1 ? parts[1] : "09:00";
        String endTime = parts.length > 2 ? parts[2] : "17:00";
        
        return new Doctor(id, name, specialization, phone, email, availableDays, startTime, endTime, isAvailable);
    }
    
    /**
     * Serialize doctor schedule to string.
     */
    private String serializeSchedule(Doctor doctor) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", doctor.getAvailableDays()));
        sb.append("|").append(doctor.getStartTime());
        sb.append("|").append(doctor.getEndTime());
        return sb.toString();
    }
    
    /**
     * Deserialize schedule string.
     */
    private String[] deserializeSchedule(String schedule) {
        if (schedule == null || schedule.isEmpty()) {
            return new String[]{"Monday,Tuesday,Wednesday,Thursday,Friday", "09:00", "17:00"};
        }
        return schedule.split("\\|");
    }
}
