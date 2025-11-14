package com.clinicapp.gui;

import com.clinicapp.dao.PatientDAO;
import com.clinicapp.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for managing patient records with UTF-8 text support.
 * Displays patients in a table and provides add/edit/delete functionality.
 */
public class PatientPanel extends JPanel {
    private PatientDAO patientDAO;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    
    public PatientPanel() {
        patientDAO = new PatientDAO();
        initializeComponents();
        layoutComponents();
        loadPatients();
    }
    
    /**
     * Initializes all GUI components with UTF-8 support.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table with UTF-8 compatible columns
        String[] columnNames = {"ID", "Name", "Date of Birth", "Age", "Gender", "Phone", "Email", "Blood Type"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        patientTable.setRowHeight(25);
        patientTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Create buttons
        addButton = new JButton("Add Patient");
        editButton = new JButton("Edit Patient");
        deleteButton = new JButton("Delete Patient");
        refreshButton = new JButton("Refresh");
        
        // Set button actions
        addButton.addActionListener(e -> showAddPatientDialog());
        editButton.addActionListener(e -> showEditPatientDialog());
        deleteButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> loadPatients());
        
        // Create search field with UTF-8 support
        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField.addActionListener(e -> searchPatients());
    }
    
    /**
     * Lays out components in the panel.
     */
    private void layoutComponents() {
        // Top panel with search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search by Name:"));
        topPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPatients());
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        
        // Table in scroll pane
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Patient Records"));
        
        // Bottom panel with action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Add all to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Loads all patients from database with UTF-8 encoding.
     */
    private void loadPatients() {
        try {
            tableModel.setRowCount(0);
            List<Patient> patients = patientDAO.getAllPatients();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            for (Patient patient : patients) {
                Object[] row = {
                    patient.getId(),
                    patient.getName(),
                    patient.getDateOfBirth().format(dateFormatter),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getPhoneNumber(),
                    patient.getEmail() != null ? patient.getEmail() : "",
                    patient.getBloodType() != null ? patient.getBloodType() : ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Error loading patients: " + e.getMessage());
        }
    }
    
    /**
     * Searches patients by name with UTF-8 support.
     */
    private void searchPatients() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadPatients();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<Patient> patients = patientDAO.searchPatientsByName(searchTerm);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            for (Patient patient : patients) {
                Object[] row = {
                    patient.getId(),
                    patient.getName(),
                    patient.getDateOfBirth().format(dateFormatter),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getPhoneNumber(),
                    patient.getEmail() != null ? patient.getEmail() : "",
                    patient.getBloodType() != null ? patient.getBloodType() : ""
                };
                tableModel.addRow(row);
            }
            
            if (patients.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No patients found matching: " + searchTerm,
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Error searching patients: " + e.getMessage());
        }
    }
    
    /**
     * Shows dialog to add a new patient with UTF-8 input support.
     */
    private void showAddPatientDialog() {
        PatientFormDialog dialog = new PatientFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            "Add New Patient", 
            null
        );
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Patient patient = dialog.getPatient();
            try {
                patientDAO.addPatient(patient);
                loadPatients();
                JOptionPane.showMessageDialog(this, 
                    "Patient added successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error adding patient: " + e.getMessage());
            }
        }
    }
    
    /**
     * Shows dialog to edit selected patient with UTF-8 input support.
     */
    private void showEditPatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a patient to edit.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Patient patient = patientDAO.getPatientById(patientId);
            if (patient == null) {
                showError("Patient not found.");
                return;
            }
            
            PatientFormDialog dialog = new PatientFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                "Edit Patient", 
                patient
            );
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Patient updatedPatient = dialog.getPatient();
                updatedPatient.setId(patientId);
                patientDAO.updatePatient(updatedPatient);
                loadPatients();
                JOptionPane.showMessageDialog(this, 
                    "Patient updated successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Error updating patient: " + e.getMessage());
        }
    }
    
    /**
     * Deletes the selected patient.
     */
    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a patient to delete.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete patient: " + patientName + "?\n" +
            "This will also delete all associated appointments.",
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                patientDAO.deletePatient(patientId);
                loadPatients();
                JOptionPane.showMessageDialog(this, 
                    "Patient deleted successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error deleting patient: " + e.getMessage());
            }
        }
    }
    
    /**
     * Shows error message dialog with UTF-8 support.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message,
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
