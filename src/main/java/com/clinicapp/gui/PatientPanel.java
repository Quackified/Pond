package com.clinicapp.gui;

import com.clinicapp.model.Patient;
import com.clinicapp.service.PatientManager;
import com.clinicapp.util.InputValidator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PatientPanel extends JPanel {
    private PatientManager patientManager;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;
    
    public PatientPanel(PatientManager patientManager) {
        this.patientManager = patientManager;
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Patient Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Name", "Date of Birth", "Age", "Gender", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Patient");
        editButton = new JButton("Edit Patient");
        deleteButton = new JButton("Delete Patient");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> addPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> refreshTable());
        
        refreshTable();
    }
    
    private void addPatient() {
        JTextField nameField = new JTextField();
        JTextField dobField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField bloodTypeField = new JTextField();
        JTextField allergiesField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date of Birth (yyyy-MM-dd):"));
        panel.add(dobField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderBox);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Blood Type:"));
        panel.add(bloodTypeField);
        panel.add(new JLabel("Allergies:"));
        panel.add(allergiesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Patient", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String dobStr = dobField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String bloodType = bloodTypeField.getText().trim();
            String allergies = allergiesField.getText().trim();
            
            if (!InputValidator.isValidString(name)) {
                JOptionPane.showMessageDialog(this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidDate(dobStr)) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.isEmpty() && !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!bloodType.isEmpty() && !InputValidator.isValidBloodType(bloodType)) {
                JOptionPane.showMessageDialog(this, "Invalid blood type!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDate dob = InputValidator.parseDate(dobStr);
            patientManager.addPatient(name, dob, gender, phone, email, address, bloodType, allergies);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
        }
    }
    
    private void editPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(patientId);
        
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JTextField nameField = new JTextField(patient.getName());
        JTextField dobField = new JTextField(patient.getDateOfBirth().toString());
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setSelectedItem(patient.getGender());
        JTextField phoneField = new JTextField(patient.getPhoneNumber());
        JTextField emailField = new JTextField(patient.getEmail() != null ? patient.getEmail() : "");
        JTextField addressField = new JTextField(patient.getAddress() != null ? patient.getAddress() : "");
        JTextField bloodTypeField = new JTextField(patient.getBloodType() != null ? patient.getBloodType() : "");
        JTextField allergiesField = new JTextField(patient.getAllergies() != null ? patient.getAllergies() : "");
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date of Birth (yyyy-MM-dd):"));
        panel.add(dobField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderBox);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Blood Type:"));
        panel.add(bloodTypeField);
        panel.add(new JLabel("Allergies:"));
        panel.add(allergiesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Patient", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String dobStr = dobField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String bloodType = bloodTypeField.getText().trim();
            String allergies = allergiesField.getText().trim();
            
            if (!InputValidator.isValidString(name)) {
                JOptionPane.showMessageDialog(this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidDate(dobStr)) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.isEmpty() && !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!bloodType.isEmpty() && !InputValidator.isValidBloodType(bloodType)) {
                JOptionPane.showMessageDialog(this, "Invalid blood type!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDate dob = InputValidator.parseDate(dobStr);
            patientManager.updatePatient(patientId, name, dob, gender, phone, email, address, bloodType, allergies);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
        }
    }
    
    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            patientManager.deletePatient(patientId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientManager.getAllPatients();
        
        for (Patient patient : patients) {
            Object[] row = {
                patient.getId(),
                patient.getName(),
                patient.getDateOfBirth().toString(),
                patient.getAge(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail() != null ? patient.getEmail() : ""
            };
            tableModel.addRow(row);
        }
    }
}
