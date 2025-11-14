package com.clinicapp.dao;

import com.clinicapp.db.DatabaseConnection;
import com.clinicapp.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PatientDAO - Data Access Object for Patient operations.
 * Handles all database operations related to patients.
 */
public class PatientDAO {
    
    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Add a new patient to the database.
     */
    public Patient addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, date_of_birth, gender, phone, email, address, blood_type, allergies) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getName());
            stmt.setDate(2, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getBloodType());
            stmt.setString(8, patient.getAllergies());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getInt(1));
                }
            }
            
            return patient;
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get patient by ID.
     */
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPatientFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting patient: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all patients.
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all patients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return patients;
    }
    
    /**
     * Search patients by name.
     */
    public List<Patient> searchPatientsByName(String searchTerm) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE name LIKE ? ORDER BY name";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching patients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return patients;
    }
    
    /**
     * Update patient information.
     */
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, date_of_birth = ?, gender = ?, " +
                     "phone = ?, email = ?, address = ?, blood_type = ?, allergies = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setDate(2, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getBloodType());
            stmt.setString(8, patient.getAllergies());
            stmt.setInt(9, patient.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete patient by ID.
     */
    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Extract Patient object from ResultSet.
     */
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
        String gender = rs.getString("gender");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String address = rs.getString("address");
        String bloodType = rs.getString("blood_type");
        String allergies = rs.getString("allergies");
        
        return new Patient(id, name, dateOfBirth, gender, phone, email, address, bloodType, allergies);
    }
}
