package com.clinicapp.gui;

import com.clinicapp.model.Patient;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.PatientManager;
import com.clinicapp.util.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PatientPanel extends JPanel {
    
    private final PatientManager patientManager;
    private final AppointmentManager appointmentManager;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    
    public PatientPanel(PatientManager patientManager, AppointmentManager appointmentManager) {
        this.patientManager = patientManager;
        this.appointmentManager = appointmentManager;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> addPatient());
        viewButton.addActionListener(e -> viewPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Create table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "DOB", "Age", "Gender", "Phone"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Patient patient : patientManager.getAllPatients()) {
            tableModel.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.getDateOfBirth().format(dateFormatter),
                patient.getAge(),
                patient.getGender(),
                patient.getPhoneNumber()
            });
        }
    }
    
    private void addPatient() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField();
        JTextField dobField = new JTextField("yyyy-MM-dd");
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JComboBox<String> bloodTypeCombo = new JComboBox<>(new String[]{
            "", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        });
        JTextField allergiesField = new JTextField();
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date of Birth (yyyy-MM-dd):"));
        panel.add(dobField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Blood Type:"));
        panel.add(bloodTypeCombo);
        panel.add(new JLabel("Allergies:"));
        panel.add(allergiesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Patient", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String dobStr = dobField.getText().trim();
                String phone = phoneField.getText().trim();
                
                if (!InputValidator.isValidString(name)) {
                    JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidDateFormat(dobStr)) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidPhoneNumber(phone)) {
                    JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LocalDate dob = LocalDate.parse(dobStr);
                String email = emailField.getText().trim().isEmpty() ? null : emailField.getText().trim();
                String address = addressField.getText().trim().isEmpty() ? null : addressField.getText().trim();
                String bloodType = bloodTypeCombo.getSelectedItem().toString().isEmpty() ? null : bloodTypeCombo.getSelectedItem().toString();
                String allergies = allergiesField.getText().trim().isEmpty() ? "None" : allergiesField.getText().trim();
                
                if (email != null && !InputValidator.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                patientManager.addPatient(name, dob, genderCombo.getSelectedItem().toString(), 
                                         phone, email, address, bloodType, allergies);
                
                JOptionPane.showMessageDialog(this, "Patient added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(id);
        if (patient != null) {
            JOptionPane.showMessageDialog(this, patient.getDetailedInfo(), "Patient Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(id);
        if (patient == null) return;
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(patient.getName());
        JTextField phoneField = new JTextField(patient.getPhoneNumber());
        JTextField emailField = new JTextField(patient.getEmail() != null ? patient.getEmail() : "");
        JTextField addressField = new JTextField(patient.getAddress() != null ? patient.getAddress() : "");
        JComboBox<String> bloodTypeCombo = new JComboBox<>(new String[]{
            "", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        });
        if (patient.getBloodType() != null) {
            bloodTypeCombo.setSelectedItem(patient.getBloodType());
        }
        JTextField allergiesField = new JTextField(patient.getAllergies() != null ? patient.getAllergies() : "");
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Blood Type:"));
        panel.add(bloodTypeCombo);
        panel.add(new JLabel("Allergies:"));
        panel.add(allergiesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Update Patient", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim().isEmpty() ? null : nameField.getText().trim();
            String phone = phoneField.getText().trim().isEmpty() ? null : phoneField.getText().trim();
            String email = emailField.getText().trim().isEmpty() ? null : emailField.getText().trim();
            String address = addressField.getText().trim().isEmpty() ? null : addressField.getText().trim();
            String bloodType = bloodTypeCombo.getSelectedItem().toString().isEmpty() ? null : bloodTypeCombo.getSelectedItem().toString();
            String allergies = allergiesField.getText().trim().isEmpty() ? null : allergiesField.getText().trim();
            
            if (phone != null && !InputValidator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (email != null && !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            patientManager.updatePatient(id, name, null, null, phone, email, address, bloodType, allergies);
            JOptionPane.showMessageDialog(this, "Patient updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }
    
    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", 
                                                   "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (patientManager.deletePatient(id)) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete patient.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
