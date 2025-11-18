package com.clinicapp.gui;

import com.clinicapp.model.Patient;
import com.clinicapp.service.PatientManager;
import com.clinicapp.util.InputValidator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for managing patient records.
 */
public class PatientPanel extends JPanel {
    
    private final PatientManager patientManager;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    
    public PatientPanel(PatientManager patientManager) {
        this.patientManager = patientManager;
        initializeUI();
        refreshTable();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        String[] columnNames = {"ID", "Name", "Date of Birth", "Age", "Gender", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Patient");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Patient");
        JButton deleteButton = new JButton("Delete Patient");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> showAddPatientDialog());
        viewButton.addActionListener(e -> viewPatientDetails());
        editButton.addActionListener(e -> showEditPatientDialog());
        deleteButton.addActionListener(e -> deletePatient());
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientManager.getAllPatients();
        for (Patient patient : patients) {
            Object[] row = {
                patient.getId(),
                patient.getName(),
                InputValidator.formatDate(patient.getDateOfBirth()),
                patient.getAge(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail() != null ? patient.getEmail() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddPatientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Patient", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField dobField = new JTextField(20);
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        JComboBox<String> bloodTypeBox = new JComboBox<>(InputValidator.getValidBloodTypes());
        JTextArea allergiesArea = new JTextArea(3, 20);
        
        int row = 0;
        addLabelAndField(panel, gbc, row++, "Name:", nameField);
        addLabelAndField(panel, gbc, row++, "Date of Birth (yyyy-MM-dd):", dobField);
        addLabelAndField(panel, gbc, row++, "Gender:", genderBox);
        addLabelAndField(panel, gbc, row++, "Phone:", phoneField);
        addLabelAndField(panel, gbc, row++, "Email:", emailField);
        addLabelAndField(panel, gbc, row++, "Address:", new JScrollPane(addressArea));
        addLabelAndField(panel, gbc, row++, "Blood Type:", bloodTypeBox);
        addLabelAndField(panel, gbc, row++, "Allergies:", new JScrollPane(allergiesArea));
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            if (validateAndAddPatient(nameField.getText(), dobField.getText(), 
                    (String) genderBox.getSelectedItem(), phoneField.getText(),
                    emailField.getText(), addressArea.getText(),
                    (String) bloodTypeBox.getSelectedItem(), allergiesArea.getText())) {
                refreshTable();
                dialog.dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private boolean validateAndAddPatient(String name, String dob, String gender, 
            String phone, String email, String address, String bloodType, String allergies) {
        
        if (!InputValidator.isNonEmptyString(name)) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        LocalDate dateOfBirth = InputValidator.parseDate(dob);
        if (dateOfBirth == null) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!InputValidator.isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (InputValidator.isNonEmptyString(email) && !InputValidator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String finalEmail = InputValidator.isNonEmptyString(email) ? email : null;
        String finalAddress = InputValidator.isNonEmptyString(address) ? address : null;
        String finalAllergies = InputValidator.isNonEmptyString(allergies) ? allergies : null;
        
        patientManager.addPatient(name, dateOfBirth, gender, phone, finalEmail, 
            finalAddress, bloodType, finalAllergies);
        
        JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    private void viewPatientDetails() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(patientId);
        
        if (patient != null) {
            String details = String.format(
                "Patient Details:\n\n" +
                "ID: %d\n" +
                "Name: %s\n" +
                "Date of Birth: %s\n" +
                "Age: %d\n" +
                "Gender: %s\n" +
                "Phone: %s\n" +
                "Email: %s\n" +
                "Address: %s\n" +
                "Blood Type: %s\n" +
                "Allergies: %s",
                patient.getId(),
                patient.getName(),
                InputValidator.formatDate(patient.getDateOfBirth()),
                patient.getAge(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail() != null ? patient.getEmail() : "N/A",
                patient.getAddress() != null ? patient.getAddress() : "N/A",
                patient.getBloodType() != null ? patient.getBloodType() : "N/A",
                patient.getAllergies() != null ? patient.getAllergies() : "None"
            );
            
            JOptionPane.showMessageDialog(this, details, "Patient Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showEditPatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientManager.getPatientById(patientId);
        
        if (patient == null) return;
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Patient", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(patient.getName(), 20);
        JTextField phoneField = new JTextField(patient.getPhoneNumber(), 20);
        JTextField emailField = new JTextField(patient.getEmail() != null ? patient.getEmail() : "", 20);
        JTextArea addressArea = new JTextArea(patient.getAddress() != null ? patient.getAddress() : "", 3, 20);
        
        int row = 0;
        addLabelAndField(panel, gbc, row++, "Name:", nameField);
        addLabelAndField(panel, gbc, row++, "Phone:", phoneField);
        addLabelAndField(panel, gbc, row++, "Email:", emailField);
        addLabelAndField(panel, gbc, row++, "Address:", new JScrollPane(addressArea));
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            if (validateAndUpdatePatient(patientId, nameField.getText(), 
                    phoneField.getText(), emailField.getText(), addressArea.getText())) {
                refreshTable();
                dialog.dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private boolean validateAndUpdatePatient(int id, String name, String phone, String email, String address) {
        if (!InputValidator.isNonEmptyString(name)) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!InputValidator.isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (InputValidator.isNonEmptyString(email) && !InputValidator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String finalEmail = InputValidator.isNonEmptyString(email) ? email : null;
        String finalAddress = InputValidator.isNonEmptyString(address) ? address : null;
        
        patientManager.updatePatient(id, name, null, null, phone, finalEmail, finalAddress, null, null);
        
        JOptionPane.showMessageDialog(this, "Patient updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this patient?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int patientId = (int) tableModel.getValueAt(selectedRow, 0);
            patientManager.deletePatient(patientId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }
}
