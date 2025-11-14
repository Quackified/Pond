package com.clinicapp.gui;

import com.clinicapp.dao.PatientDAO;
import com.clinicapp.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * PatientFormDialog - Dialog for adding or editing patient information.
 */
public class PatientFormDialog extends JDialog {
    
    private PatientDAO patientDAO;
    private Patient patient;
    private boolean saved = false;
    
    private JTextField nameField;
    private JTextField dobField;
    private JComboBox<String> genderCombo;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea addressArea;
    private JComboBox<String> bloodTypeCombo;
    private JTextArea allergiesArea;
    
    public PatientFormDialog(Frame parent, Patient patient) {
        super(parent, patient == null ? "Add Patient" : "Edit Patient", true);
        this.patientDAO = new PatientDAO();
        this.patient = patient;
        
        initializeUI();
        
        if (patient != null) {
            populateFields();
        }
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField();
        formPanel.add(nameField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Date of Birth (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        dobField = new JTextField();
        formPanel.add(dobField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        formPanel.add(genderCombo, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        phoneField = new JTextField();
        formPanel.add(phoneField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailField = new JTextField();
        formPanel.add(emailField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        formPanel.add(addressScroll, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Blood Type:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        bloodTypeCombo = new JComboBox<>(new String[]{"", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        formPanel.add(bloodTypeCombo, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Allergies:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        allergiesArea = new JTextArea(3, 20);
        allergiesArea.setLineWrap(true);
        allergiesArea.setWrapStyleWord(true);
        JScrollPane allergiesScroll = new JScrollPane(allergiesArea);
        formPanel.add(allergiesScroll, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(46, 125, 50));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> savePatient());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void populateFields() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        nameField.setText(patient.getName());
        dobField.setText(patient.getDateOfBirth().format(formatter));
        genderCombo.setSelectedItem(patient.getGender());
        phoneField.setText(patient.getPhoneNumber());
        emailField.setText(patient.getEmail() != null ? patient.getEmail() : "");
        addressArea.setText(patient.getAddress() != null ? patient.getAddress() : "");
        bloodTypeCombo.setSelectedItem(patient.getBloodType() != null ? patient.getBloodType() : "");
        allergiesArea.setText(patient.getAllergies() != null ? patient.getAllergies() : "");
    }
    
    private void savePatient() {
        String name = nameField.getText().trim();
        String dobStr = dobField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();
        String bloodType = (String) bloodTypeCombo.getSelectedItem();
        String allergies = allergiesArea.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Name is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (dobStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Date of birth is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Use yyyy-MM-dd",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Phone number is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (patient == null) {
            Patient newPatient = new Patient(name, dob, gender, phone, 
                email.isEmpty() ? null : email,
                address.isEmpty() ? null : address,
                bloodType.isEmpty() ? null : bloodType,
                allergies.isEmpty() ? null : allergies);
            
            if (patientDAO.addPatient(newPatient) != null) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Patient added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to add patient.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            patient.setName(name);
            patient.setDateOfBirth(dob);
            patient.setGender(gender);
            patient.setPhoneNumber(phone);
            patient.setEmail(email.isEmpty() ? null : email);
            patient.setAddress(address.isEmpty() ? null : address);
            patient.setBloodType(bloodType.isEmpty() ? null : bloodType);
            patient.setAllergies(allergies.isEmpty() ? null : allergies);
            
            if (patientDAO.updatePatient(patient)) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Patient updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update patient.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}
