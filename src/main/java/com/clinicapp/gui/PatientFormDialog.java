package com.clinicapp.gui;

import com.clinicapp.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialog for adding/editing patient information with UTF-8 text input support.
 */
public class PatientFormDialog extends JDialog {
    private JTextField nameField;
    private JTextField dobField;
    private JComboBox<String> genderCombo;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea addressArea;
    private JComboBox<String> bloodTypeCombo;
    private JTextArea allergiesArea;
    
    private boolean confirmed = false;
    private Patient patient;
    
    public PatientFormDialog(JFrame parent, String title, Patient patient) {
        super(parent, title, true);
        this.patient = patient;
        
        initializeComponents();
        layoutComponents();
        
        if (patient != null) {
            populateFields(patient);
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initializes form components with UTF-8 support.
     */
    private void initializeComponents() {
        // Set UTF-8 font for all text components
        Font textFont = new Font("SansSerif", Font.PLAIN, 12);
        
        nameField = new JTextField(25);
        nameField.setFont(textFont);
        
        dobField = new JTextField(10);
        dobField.setFont(textFont);
        dobField.setToolTipText("Format: yyyy-MM-dd (e.g., 1990-01-15)");
        
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(textFont);
        
        phoneField = new JTextField(15);
        phoneField.setFont(textFont);
        
        emailField = new JTextField(25);
        emailField.setFont(textFont);
        
        addressArea = new JTextArea(3, 25);
        addressArea.setFont(textFont);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        
        bloodTypeCombo = new JComboBox<>(new String[]{"", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodTypeCombo.setFont(textFont);
        
        allergiesArea = new JTextArea(3, 25);
        allergiesArea.setFont(textFont);
        allergiesArea.setLineWrap(true);
        allergiesArea.setWrapStyleWord(true);
    }
    
    /**
     * Lays out form components.
     */
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Name: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
        
        // Date of Birth
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Date of Birth: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(dobField, gbc);
        
        // Gender
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Gender: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(genderCombo, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Phone: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(new JScrollPane(addressArea), gbc);
        
        // Blood Type
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Blood Type:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(bloodTypeCombo, gbc);
        
        // Allergies
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(new JLabel("Allergies:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(new JScrollPane(allergiesArea), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> savePatient());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Populates form fields with patient data (UTF-8 text).
     */
    private void populateFields(Patient patient) {
        nameField.setText(patient.getName());
        dobField.setText(patient.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        genderCombo.setSelectedItem(patient.getGender());
        phoneField.setText(patient.getPhoneNumber());
        emailField.setText(patient.getEmail() != null ? patient.getEmail() : "");
        addressArea.setText(patient.getAddress() != null ? patient.getAddress() : "");
        bloodTypeCombo.setSelectedItem(patient.getBloodType() != null ? patient.getBloodType() : "");
        allergiesArea.setText(patient.getAllergies() != null ? patient.getAllergies() : "");
    }
    
    /**
     * Validates and saves patient data with UTF-8 encoding.
     */
    private void savePatient() {
        // Validate required fields
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Name is required.");
            return;
        }
        
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            showError("Invalid date format. Use yyyy-MM-dd (e.g., 1990-01-15)");
            return;
        }
        
        String gender = (String) genderCombo.getSelectedItem();
        
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            showError("Phone number is required.");
            return;
        }
        
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();
        String bloodType = (String) bloodTypeCombo.getSelectedItem();
        String allergies = allergiesArea.getText().trim();
        
        // Create/update patient object with UTF-8 data
        if (patient == null) {
            patient = new Patient(name, dob, gender, phone, 
                                email.isEmpty() ? null : email, 
                                address.isEmpty() ? null : address, 
                                bloodType.isEmpty() ? null : bloodType, 
                                allergies.isEmpty() ? null : allergies);
        } else {
            patient.setName(name);
            patient.setDateOfBirth(dob);
            patient.setGender(gender);
            patient.setPhoneNumber(phone);
            patient.setEmail(email.isEmpty() ? null : email);
            patient.setAddress(address.isEmpty() ? null : address);
            patient.setBloodType(bloodType.isEmpty() ? null : bloodType);
            patient.setAllergies(allergies.isEmpty() ? null : allergies);
        }
        
        confirmed = true;
        dispose();
    }
    
    /**
     * Shows error message with UTF-8 support.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Patient getPatient() {
        return patient;
    }
}
