package com.clinicapp.gui;

import com.clinicapp.model.Doctor;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.util.InputValidator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing doctor records.
 */
public class DoctorPanel extends JPanel {
    
    private final DoctorManager doctorManager;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    
    public DoctorPanel(DoctorManager doctorManager) {
        this.doctorManager = doctorManager;
        initializeUI();
        refreshTable();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        String[] columnNames = {"ID", "Name", "Specialization", "Phone", "Email", "Start Time", "End Time", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Doctor");
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Doctor");
        JButton deleteButton = new JButton("Delete Doctor");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> showAddDoctorDialog());
        viewButton.addActionListener(e -> viewDoctorDetails());
        editButton.addActionListener(e -> showEditDoctorDialog());
        deleteButton.addActionListener(e -> deleteDoctor());
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
        List<Doctor> doctors = doctorManager.getAllDoctors();
        for (Doctor doctor : doctors) {
            Object[] row = {
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber(),
                doctor.getEmail() != null ? doctor.getEmail() : "",
                InputValidator.formatTime(doctor.getStartTime()),
                InputValidator.formatTime(doctor.getEndTime()),
                doctor.isAvailable() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddDoctorDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Doctor", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField specializationField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField startTimeField = new JTextField(20);
        JTextField endTimeField = new JTextField(20);
        
        JPanel daysPanel = new JPanel(new GridLayout(0, 2));
        JCheckBox monCheck = new JCheckBox("Monday");
        JCheckBox tueCheck = new JCheckBox("Tuesday");
        JCheckBox wedCheck = new JCheckBox("Wednesday");
        JCheckBox thuCheck = new JCheckBox("Thursday");
        JCheckBox friCheck = new JCheckBox("Friday");
        JCheckBox satCheck = new JCheckBox("Saturday");
        JCheckBox sunCheck = new JCheckBox("Sunday");
        
        daysPanel.add(monCheck);
        daysPanel.add(tueCheck);
        daysPanel.add(wedCheck);
        daysPanel.add(thuCheck);
        daysPanel.add(friCheck);
        daysPanel.add(satCheck);
        daysPanel.add(sunCheck);
        
        int row = 0;
        addLabelAndField(panel, gbc, row++, "Name:", nameField);
        addLabelAndField(panel, gbc, row++, "Specialization:", specializationField);
        addLabelAndField(panel, gbc, row++, "Phone:", phoneField);
        addLabelAndField(panel, gbc, row++, "Email:", emailField);
        addLabelAndField(panel, gbc, row++, "Start Time (HH:mm):", startTimeField);
        addLabelAndField(panel, gbc, row++, "End Time (HH:mm):", endTimeField);
        addLabelAndField(panel, gbc, row++, "Available Days:", daysPanel);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            List<String> selectedDays = new ArrayList<>();
            if (monCheck.isSelected()) selectedDays.add("Monday");
            if (tueCheck.isSelected()) selectedDays.add("Tuesday");
            if (wedCheck.isSelected()) selectedDays.add("Wednesday");
            if (thuCheck.isSelected()) selectedDays.add("Thursday");
            if (friCheck.isSelected()) selectedDays.add("Friday");
            if (satCheck.isSelected()) selectedDays.add("Saturday");
            if (sunCheck.isSelected()) selectedDays.add("Sunday");
            
            if (validateAndAddDoctor(nameField.getText(), specializationField.getText(),
                    phoneField.getText(), emailField.getText(), selectedDays,
                    startTimeField.getText(), endTimeField.getText())) {
                refreshTable();
                dialog.dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(new JScrollPane(panel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private boolean validateAndAddDoctor(String name, String specialization, String phone,
            String email, List<String> availableDays, String startTime, String endTime) {
        
        if (!InputValidator.isNonEmptyString(name)) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!InputValidator.isNonEmptyString(specialization)) {
            JOptionPane.showMessageDialog(this, "Specialization is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
        
        LocalTime start = InputValidator.parseTime(startTime);
        if (start == null) {
            JOptionPane.showMessageDialog(this, "Invalid start time! Use HH:mm format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        LocalTime end = InputValidator.parseTime(endTime);
        if (end == null) {
            JOptionPane.showMessageDialog(this, "Invalid end time! Use HH:mm format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String finalEmail = InputValidator.isNonEmptyString(email) ? email : null;
        
        doctorManager.addDoctor(name, specialization, phone, finalEmail, availableDays, start, end);
        
        JOptionPane.showMessageDialog(this, "Doctor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    private void viewDoctorDetails() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor != null) {
            String details = String.format(
                "Doctor Details:\n\n" +
                "ID: %d\n" +
                "Name: Dr. %s\n" +
                "Specialization: %s\n" +
                "Phone: %s\n" +
                "Email: %s\n" +
                "Working Hours: %s - %s\n" +
                "Available Days: %s\n" +
                "Status: %s",
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber(),
                doctor.getEmail() != null ? doctor.getEmail() : "N/A",
                InputValidator.formatTime(doctor.getStartTime()),
                InputValidator.formatTime(doctor.getEndTime()),
                String.join(", ", doctor.getAvailableDays()),
                doctor.isAvailable() ? "Available" : "Unavailable"
            );
            
            JOptionPane.showMessageDialog(this, details, "Doctor Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showEditDoctorDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        if (doctor == null) return;
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Doctor", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(doctor.getName(), 20);
        JTextField specializationField = new JTextField(doctor.getSpecialization(), 20);
        JTextField phoneField = new JTextField(doctor.getPhoneNumber(), 20);
        JTextField emailField = new JTextField(doctor.getEmail() != null ? doctor.getEmail() : "", 20);
        JCheckBox availableCheck = new JCheckBox("Available", doctor.isAvailable());
        
        int row = 0;
        addLabelAndField(panel, gbc, row++, "Name:", nameField);
        addLabelAndField(panel, gbc, row++, "Specialization:", specializationField);
        addLabelAndField(panel, gbc, row++, "Phone:", phoneField);
        addLabelAndField(panel, gbc, row++, "Email:", emailField);
        addLabelAndField(panel, gbc, row++, "Status:", availableCheck);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            if (validateAndUpdateDoctor(doctorId, nameField.getText(), specializationField.getText(),
                    phoneField.getText(), emailField.getText(), availableCheck.isSelected())) {
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
    
    private boolean validateAndUpdateDoctor(int id, String name, String specialization,
            String phone, String email, boolean available) {
        
        if (!InputValidator.isNonEmptyString(name)) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!InputValidator.isNonEmptyString(specialization)) {
            JOptionPane.showMessageDialog(this, "Specialization is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
        
        doctorManager.updateDoctor(id, name, specialization, phone, finalEmail, null, null, null);
        doctorManager.setDoctorAvailability(id, available);
        
        JOptionPane.showMessageDialog(this, "Doctor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this doctor?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
            doctorManager.deleteDoctor(doctorId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
