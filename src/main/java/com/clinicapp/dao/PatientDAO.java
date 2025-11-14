package com.clinicapp.dao;

import com.clinicapp.db.DatabaseConnection;
import com.clinicapp.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Patient operations.
 * All database operations use UTF-8 encoding to prevent character encoding issues.
 */
public class PatientDAO {
    private final DatabaseConnection dbConnection;
    
    public PatientDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Adds a new patient to the database with UTF-8 encoding.
     * 
     * @param patient Patient to add
     * @return Generated patient ID
     * @throws SQLException if database error occurs
     */
    public int addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (name, date_of_birth, gender, phone, email, address, blood_type, allergies) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, patient.getName());
            stmt.setDate(2, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getBloodType());
            stmt.setString(8, patient.getAllergies());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    patient.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to retrieve generated patient ID");
    }
    
    /**
     * Retrieves all patients from the database.
     * UTF-8 encoding ensures proper character display.
     * 
     * @return List of all patients
     * @throws SQLException if database error occurs
     */
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        }
        return patients;
    }
    
    /**
     * Retrieves a patient by ID.
     * 
     * @param id Patient ID
     * @return Patient object or null if not found
     * @throws SQLException if database error occurs
     */
    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPatientFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Searches patients by name (case-insensitive, partial match).
     * UTF-8 collation ensures proper international character matching.
     * 
     * @param name Name to search for
     * @return List of matching patients
     * @throws SQLException if database error occurs
     */
    public List<Patient> searchPatientsByName(String name) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE name LIKE ? ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(extractPatientFromResultSet(rs));
                }
            }
        }
        return patients;
    }
    
    /**
     * Updates patient information in the database.
     * 
     * @param patient Patient with updated information
     * @return true if update successful
     * @throws SQLException if database error occurs
     */
    public boolean updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name = ?, date_of_birth = ?, gender = ?, phone = ?, " +
                    "email = ?, address = ?, blood_type = ?, allergies = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patient.getName());
            stmt.setDate(2, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getBloodType());
            stmt.setString(8, patient.getAllergies());
            stmt.setInt(9, patient.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Deletes a patient from the database.
     * 
     * @param id Patient ID to delete
     * @return true if deletion successful
     * @throws SQLException if database error occurs
     */
    public boolean deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Extracts Patient object from ResultSet.
     * Handles UTF-8 encoded strings properly.
     * 
     * @param rs ResultSet positioned at patient row
     * @return Patient object
     * @throws SQLException if error reading data
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
