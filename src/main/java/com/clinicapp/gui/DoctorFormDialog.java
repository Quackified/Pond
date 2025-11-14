package com.clinicapp.gui;

import com.clinicapp.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Dialog for adding/editing doctor information with UTF-8 text input support.
 */
public class DoctorFormDialog extends JDialog {
    private JTextField nameField;
    private JTextField specializationField;
    private JTextField phoneField;
    private JTextField emailField;
    private JCheckBox mondayCheck, tuesdayCheck, wednesdayCheck, thursdayCheck, fridayCheck, saturdayCheck, sundayCheck;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JCheckBox availableCheck;
    
    private boolean confirmed = false;
    private Doctor doctor;
    
    public DoctorFormDialog(JFrame parent, String title, Doctor doctor) {
        super(parent, title, true);
        this.doctor = doctor;
        
        initializeComponents();
        layoutComponents();
        
        if (doctor != null) {
            populateFields(doctor);
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        Font textFont = new Font("SansSerif", Font.PLAIN, 12);
        
        nameField = new JTextField(25);
        nameField.setFont(textFont);
        
        specializationField = new JTextField(25);
        specializationField.setFont(textFont);
        
        phoneField = new JTextField(15);
        phoneField.setFont(textFont);
        
        emailField = new JTextField(25);
        emailField.setFont(textFont);
        
        mondayCheck = new JCheckBox("Monday");
        tuesdayCheck = new JCheckBox("Tuesday");
        wednesdayCheck = new JCheckBox("Wednesday");
        thursdayCheck = new JCheckBox("Thursday");
        fridayCheck = new JCheckBox("Friday");
        saturdayCheck = new JCheckBox("Saturday");
        sundayCheck = new JCheckBox("Sunday");
        
        startTimeField = new JTextField(5);
        startTimeField.setFont(textFont);
        startTimeField.setToolTipText("Format: HH:mm (e.g., 09:00)");
        
        endTimeField = new JTextField(5);
        endTimeField.setFont(textFont);
        endTimeField.setToolTipText("Format: HH:mm (e.g., 17:00)");
        
        availableCheck = new JCheckBox("Currently Available");
        availableCheck.setSelected(true);
    }
    
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
        
        // Specialization
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Specialization: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(specializationField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Phone: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        
        // Available Days
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(new JLabel("Available Days:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel daysPanel = new JPanel(new GridLayout(7, 1));
        daysPanel.add(mondayCheck);
        daysPanel.add(tuesdayCheck);
        daysPanel.add(wednesdayCheck);
        daysPanel.add(thursdayCheck);
        daysPanel.add(fridayCheck);
        daysPanel.add(saturdayCheck);
        daysPanel.add(sundayCheck);
        mainPanel.add(daysPanel, gbc);
        
        // Start Time
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Start Time: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(startTimeField, gbc);
        
        // End Time
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("End Time: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(endTimeField, gbc);
        
        // Available
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        mainPanel.add(availableCheck, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> saveDoctor());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void populateFields(Doctor doctor) {
        nameField.setText(doctor.getName());
        specializationField.setText(doctor.getSpecialization());
        phoneField.setText(doctor.getPhoneNumber());
        emailField.setText(doctor.getEmail() != null ? doctor.getEmail() : "");
        
        List<String> days = doctor.getAvailableDays();
        mondayCheck.setSelected(days.contains("Monday"));
        tuesdayCheck.setSelected(days.contains("Tuesday"));
        wednesdayCheck.setSelected(days.contains("Wednesday"));
        thursdayCheck.setSelected(days.contains("Thursday"));
        fridayCheck.setSelected(days.contains("Friday"));
        saturdayCheck.setSelected(days.contains("Saturday"));
        sundayCheck.setSelected(days.contains("Sunday"));
        
        startTimeField.setText(doctor.getStartTime());
        endTimeField.setText(doctor.getEndTime());
        availableCheck.setSelected(doctor.isAvailable());
    }
    
    private void saveDoctor() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Name is required.");
            return;
        }
        
        String specialization = specializationField.getText().trim();
        if (specialization.isEmpty()) {
            showError("Specialization is required.");
            return;
        }
        
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            showError("Phone number is required.");
            return;
        }
        
        String email = emailField.getText().trim();
        
        List<String> availableDays = new ArrayList<>();
        if (mondayCheck.isSelected()) availableDays.add("Monday");
        if (tuesdayCheck.isSelected()) availableDays.add("Tuesday");
        if (wednesdayCheck.isSelected()) availableDays.add("Wednesday");
        if (thursdayCheck.isSelected()) availableDays.add("Thursday");
        if (fridayCheck.isSelected()) availableDays.add("Friday");
        if (saturdayCheck.isSelected()) availableDays.add("Saturday");
        if (sundayCheck.isSelected()) availableDays.add("Sunday");
        
        String startTime = startTimeField.getText().trim();
        if (startTime.isEmpty()) {
            showError("Start time is required.");
            return;
        }
        
        String endTime = endTimeField.getText().trim();
        if (endTime.isEmpty()) {
            showError("End time is required.");
            return;
        }
        
        boolean isAvailable = availableCheck.isSelected();
        
        if (doctor == null) {
            doctor = new Doctor(name, specialization, phone, 
                              email.isEmpty() ? null : email, 
                              availableDays, startTime, endTime);
            doctor.setAvailable(isAvailable);
        } else {
            doctor.setName(name);
            doctor.setSpecialization(specialization);
            doctor.setPhoneNumber(phone);
            doctor.setEmail(email.isEmpty() ? null : email);
            doctor.setAvailableDays(availableDays);
            doctor.setStartTime(startTime);
            doctor.setEndTime(endTime);
            doctor.setAvailable(isAvailable);
        }
        
        confirmed = true;
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
}
