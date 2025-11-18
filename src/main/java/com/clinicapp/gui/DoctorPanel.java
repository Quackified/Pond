package com.clinicapp.gui;

import com.clinicapp.model.Doctor;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.util.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DoctorPanel extends JPanel {
    
    private final DoctorManager doctorManager;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    
    public DoctorPanel(DoctorManager doctorManager) {
        this.doctorManager = doctorManager;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton addButton = new JButton("Add Doctor");
        JButton viewButton = new JButton("View Details");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton toggleAvailabilityButton = new JButton("Toggle Availability");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> addDoctor());
        viewButton.addActionListener(e -> viewDoctor());
        updateButton.addActionListener(e -> updateDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        toggleAvailabilityButton.addActionListener(e -> toggleAvailability());
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(toggleAvailabilityButton);
        buttonPanel.add(refreshButton);
        
        // Create table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Specialization", "Phone", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        
        for (Doctor doctor : doctorManager.getAllDoctors()) {
            tableModel.addRow(new Object[]{
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber(),
                doctor.isAvailable() ? "Available" : "Unavailable"
            });
        }
    }
    
    private void addDoctor() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField startTimeField = new JTextField("09:00");
        JTextField endTimeField = new JTextField("17:00");
        JTextField availableDaysField = new JTextField("Monday,Tuesday,Wednesday,Thursday,Friday");
        
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
        panel.add(new JLabel("Available Days (comma-separated):"));
        panel.add(availableDaysField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Doctor", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String specialization = specializationField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String startTime = startTimeField.getText().trim();
                String endTime = endTimeField.getText().trim();
                String daysStr = availableDaysField.getText().trim();
                
                if (!InputValidator.isValidString(name)) {
                    JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidString(specialization)) {
                    JOptionPane.showMessageDialog(this, "Specialization is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidPhoneNumber(phone)) {
                    JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidTimeFormat(startTime)) {
                    JOptionPane.showMessageDialog(this, "Invalid start time format. Use HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidTimeFormat(endTime)) {
                    JOptionPane.showMessageDialog(this, "Invalid end time format. Use HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                List<String> days = Arrays.asList(daysStr.split(","));
                
                doctorManager.addDoctor(name, specialization, phone, email.isEmpty() ? null : email, 
                                       days, startTime, endTime);
                
                JOptionPane.showMessageDialog(this, "Doctor added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(id);
        if (doctor != null) {
            JOptionPane.showMessageDialog(this, doctor.getDetailedInfo(), "Doctor Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(id);
        if (doctor == null) return;
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(doctor.getName());
        JTextField specializationField = new JTextField(doctor.getSpecialization());
        JTextField phoneField = new JTextField(doctor.getPhoneNumber());
        JTextField emailField = new JTextField(doctor.getEmail() != null ? doctor.getEmail() : "");
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Update Doctor", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim().isEmpty() ? null : nameField.getText().trim();
            String specialization = specializationField.getText().trim().isEmpty() ? null : specializationField.getText().trim();
            String phone = phoneField.getText().trim().isEmpty() ? null : phoneField.getText().trim();
            String email = emailField.getText().trim().isEmpty() ? null : emailField.getText().trim();
            
            if (phone != null && !InputValidator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (email != null && !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            doctorManager.updateDoctor(id, name, specialization, phone, email, null, null, null);
            JOptionPane.showMessageDialog(this, "Doctor updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor?", 
                                                   "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (doctorManager.deleteDoctor(id)) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void toggleAvailability() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(id);
        if (doctor != null) {
            doctor.setAvailable(!doctor.isAvailable());
            JOptionPane.showMessageDialog(this, "Availability toggled.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }
}
