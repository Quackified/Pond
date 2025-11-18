package com.clinicapp.gui;

import com.clinicapp.model.Doctor;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.util.InputValidator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorPanel extends JPanel {
    private DoctorManager doctorManager;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;
    
    public DoctorPanel(DoctorManager doctorManager) {
        this.doctorManager = doctorManager;
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Doctor Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Name", "Specialization", "Phone", "Email", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Doctor");
        editButton = new JButton("Edit Doctor");
        deleteButton = new JButton("Delete Doctor");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> addDoctor());
        editButton.addActionListener(e -> editDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        refreshButton.addActionListener(e -> refreshTable());
        
        refreshTable();
    }
    
    private void addDoctor() {
        JTextField nameField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField startTimeField = new JTextField("09:00");
        JTextField endTimeField = new JTextField("17:00");
        
        JCheckBox monBox = new JCheckBox("Monday");
        JCheckBox tueBox = new JCheckBox("Tuesday");
        JCheckBox wedBox = new JCheckBox("Wednesday");
        JCheckBox thuBox = new JCheckBox("Thursday");
        JCheckBox friBox = new JCheckBox("Friday");
        JCheckBox satBox = new JCheckBox("Saturday");
        JCheckBox sunBox = new JCheckBox("Sunday");
        
        JPanel daysPanel = new JPanel(new GridLayout(2, 4));
        daysPanel.add(monBox);
        daysPanel.add(tueBox);
        daysPanel.add(wedBox);
        daysPanel.add(thuBox);
        daysPanel.add(friBox);
        daysPanel.add(satBox);
        daysPanel.add(sunBox);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Start Time (HH:mm):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:mm):"));
        panel.add(endTimeField);
        panel.add(new JLabel("Available Days:"));
        panel.add(daysPanel);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Doctor", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();
            
            if (!InputValidator.isValidString(name)) {
                JOptionPane.showMessageDialog(this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidString(specialization)) {
                JOptionPane.showMessageDialog(this, "Specialization is required!", "Error", JOptionPane.ERROR_MESSAGE);
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
            
            if (!InputValidator.isValidTime(startTime)) {
                JOptionPane.showMessageDialog(this, "Invalid start time! Use HH:mm format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidTime(endTime)) {
                JOptionPane.showMessageDialog(this, "Invalid end time! Use HH:mm format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<String> availableDays = new ArrayList<>();
            if (monBox.isSelected()) availableDays.add("Monday");
            if (tueBox.isSelected()) availableDays.add("Tuesday");
            if (wedBox.isSelected()) availableDays.add("Wednesday");
            if (thuBox.isSelected()) availableDays.add("Thursday");
            if (friBox.isSelected()) availableDays.add("Friday");
            if (satBox.isSelected()) availableDays.add("Saturday");
            if (sunBox.isSelected()) availableDays.add("Sunday");
            
            if (availableDays.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one available day!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            doctorManager.addDoctor(name, specialization, phone, email, availableDays, startTime, endTime);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
        }
    }
    
    private void editDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JTextField nameField = new JTextField(doctor.getName());
        JTextField specializationField = new JTextField(doctor.getSpecialization());
        JTextField phoneField = new JTextField(doctor.getPhoneNumber());
        JTextField emailField = new JTextField(doctor.getEmail() != null ? doctor.getEmail() : "");
        JTextField startTimeField = new JTextField(doctor.getStartTime());
        JTextField endTimeField = new JTextField(doctor.getEndTime());
        
        JCheckBox monBox = new JCheckBox("Monday");
        JCheckBox tueBox = new JCheckBox("Tuesday");
        JCheckBox wedBox = new JCheckBox("Wednesday");
        JCheckBox thuBox = new JCheckBox("Thursday");
        JCheckBox friBox = new JCheckBox("Friday");
        JCheckBox satBox = new JCheckBox("Saturday");
        JCheckBox sunBox = new JCheckBox("Sunday");
        
        List<String> currentDays = doctor.getAvailableDays();
        if (currentDays.contains("Monday")) monBox.setSelected(true);
        if (currentDays.contains("Tuesday")) tueBox.setSelected(true);
        if (currentDays.contains("Wednesday")) wedBox.setSelected(true);
        if (currentDays.contains("Thursday")) thuBox.setSelected(true);
        if (currentDays.contains("Friday")) friBox.setSelected(true);
        if (currentDays.contains("Saturday")) satBox.setSelected(true);
        if (currentDays.contains("Sunday")) sunBox.setSelected(true);
        
        JPanel daysPanel = new JPanel(new GridLayout(2, 4));
        daysPanel.add(monBox);
        daysPanel.add(tueBox);
        daysPanel.add(wedBox);
        daysPanel.add(thuBox);
        daysPanel.add(friBox);
        daysPanel.add(satBox);
        daysPanel.add(sunBox);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Start Time (HH:mm):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:mm):"));
        panel.add(endTimeField);
        panel.add(new JLabel("Available Days:"));
        panel.add(daysPanel);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Doctor", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();
            
            if (!InputValidator.isValidString(name)) {
                JOptionPane.showMessageDialog(this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidString(specialization)) {
                JOptionPane.showMessageDialog(this, "Specialization is required!", "Error", JOptionPane.ERROR_MESSAGE);
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
            
            if (!InputValidator.isValidTime(startTime)) {
                JOptionPane.showMessageDialog(this, "Invalid start time! Use HH:mm format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidTime(endTime)) {
                JOptionPane.showMessageDialog(this, "Invalid end time! Use HH:mm format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<String> availableDays = new ArrayList<>();
            if (monBox.isSelected()) availableDays.add("Monday");
            if (tueBox.isSelected()) availableDays.add("Tuesday");
            if (wedBox.isSelected()) availableDays.add("Wednesday");
            if (thuBox.isSelected()) availableDays.add("Thursday");
            if (friBox.isSelected()) availableDays.add("Friday");
            if (satBox.isSelected()) availableDays.add("Saturday");
            if (sunBox.isSelected()) availableDays.add("Sunday");
            
            if (availableDays.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one available day!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            doctorManager.updateDoctor(doctorId, name, specialization, phone, email, availableDays, startTime, endTime);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
        }
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            doctorManager.deleteDoctor(doctorId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorManager.getAllDoctors();
        
        for (Doctor doctor : doctors) {
            Object[] row = {
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber(),
                doctor.getEmail() != null ? doctor.getEmail() : "",
                doctor.isAvailable() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
}
