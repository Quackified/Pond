package com.clinicapp.gui;

import com.clinicapp.dao.DoctorDAO;
import com.clinicapp.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DoctorFormDialog - Dialog for adding or editing doctor information.
 */
public class DoctorFormDialog extends JDialog {
    
    private DoctorDAO doctorDAO;
    private Doctor doctor;
    private boolean saved = false;
    
    private JTextField nameField;
    private JTextField specializationField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JCheckBox mondayCheck, tuesdayCheck, wednesdayCheck, thursdayCheck, fridayCheck;
    
    public DoctorFormDialog(Frame parent, Doctor doctor) {
        super(parent, doctor == null ? "Add Doctor" : "Edit Doctor", true);
        this.doctorDAO = new DoctorDAO();
        this.doctor = doctor;
        
        initializeUI();
        
        if (doctor != null) {
            populateFields();
        }
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 500);
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
        formPanel.add(new JLabel("Specialization:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        specializationField = new JTextField();
        formPanel.add(specializationField, gbc);
        
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
        formPanel.add(new JLabel("Start Time (HH:mm):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        startTimeField = new JTextField("09:00");
        formPanel.add(startTimeField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("End Time (HH:mm):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        endTimeField = new JTextField("17:00");
        formPanel.add(endTimeField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel daysLabel = new JLabel("Available Days:");
        daysLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(daysLabel, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mondayCheck = new JCheckBox("Monday");
        tuesdayCheck = new JCheckBox("Tuesday");
        wednesdayCheck = new JCheckBox("Wednesday");
        thursdayCheck = new JCheckBox("Thursday");
        fridayCheck = new JCheckBox("Friday");
        
        mondayCheck.setSelected(true);
        tuesdayCheck.setSelected(true);
        wednesdayCheck.setSelected(true);
        thursdayCheck.setSelected(true);
        fridayCheck.setSelected(true);
        
        daysPanel.add(mondayCheck);
        daysPanel.add(tuesdayCheck);
        daysPanel.add(wednesdayCheck);
        daysPanel.add(thursdayCheck);
        daysPanel.add(fridayCheck);
        
        formPanel.add(daysPanel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(46, 125, 50));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> saveDoctor());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void populateFields() {
        nameField.setText(doctor.getName());
        specializationField.setText(doctor.getSpecialization());
        phoneField.setText(doctor.getPhoneNumber());
        emailField.setText(doctor.getEmail() != null ? doctor.getEmail() : "");
        startTimeField.setText(doctor.getStartTime());
        endTimeField.setText(doctor.getEndTime());
        
        List<String> availableDays = doctor.getAvailableDays();
        mondayCheck.setSelected(availableDays.contains("Monday"));
        tuesdayCheck.setSelected(availableDays.contains("Tuesday"));
        wednesdayCheck.setSelected(availableDays.contains("Wednesday"));
        thursdayCheck.setSelected(availableDays.contains("Thursday"));
        fridayCheck.setSelected(availableDays.contains("Friday"));
    }
    
    private void saveDoctor() {
        String name = nameField.getText().trim();
        String specialization = specializationField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Name is required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (specialization.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Specialization is required.",
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
        
        List<String> availableDays = new ArrayList<>();
        if (mondayCheck.isSelected()) availableDays.add("Monday");
        if (tuesdayCheck.isSelected()) availableDays.add("Tuesday");
        if (wednesdayCheck.isSelected()) availableDays.add("Wednesday");
        if (thursdayCheck.isSelected()) availableDays.add("Thursday");
        if (fridayCheck.isSelected()) availableDays.add("Friday");
        
        if (availableDays.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one available day.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (doctor == null) {
            Doctor newDoctor = new Doctor(name, specialization, phone,
                email.isEmpty() ? null : email,
                availableDays, startTime, endTime);
            
            if (doctorDAO.addDoctor(newDoctor) != null) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Doctor added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to add doctor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            doctor.setName(name);
            doctor.setSpecialization(specialization);
            doctor.setPhoneNumber(phone);
            doctor.setEmail(email.isEmpty() ? null : email);
            doctor.setAvailableDays(availableDays);
            doctor.setStartTime(startTime);
            doctor.setEndTime(endTime);
            
            if (doctorDAO.updateDoctor(doctor)) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Doctor updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update doctor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}
