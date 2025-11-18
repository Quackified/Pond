package com.clinicapp.gui;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.PatientManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.util.InputValidator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentPanel extends JPanel {
    private AppointmentManager appointmentManager;
    private PatientManager patientManager;
    private DoctorManager doctorManager;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton, changeStatusButton;
    
    public AppointmentPanel(AppointmentManager appointmentManager, PatientManager patientManager, DoctorManager doctorManager) {
        this.appointmentManager = appointmentManager;
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Appointment Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Date & Time", "Patient", "Doctor", "Reason", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Appointment");
        editButton = new JButton("Edit Appointment");
        deleteButton = new JButton("Delete Appointment");
        changeStatusButton = new JButton("Change Status");
        refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(changeStatusButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> addAppointment());
        editButton.addActionListener(e -> editAppointment());
        deleteButton.addActionListener(e -> deleteAppointment());
        changeStatusButton.addActionListener(e -> changeStatus());
        refreshButton.addActionListener(e -> refreshTable());
        
        refreshTable();
    }
    
    private void addAppointment() {
        List<Patient> patients = patientManager.getAllPatients();
        List<Doctor> doctors = doctorManager.getAllDoctors();
        
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients found! Please add patients first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No doctors found! Please add doctors first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String[] patientNames = new String[patients.size()];
        for (int i = 0; i < patients.size(); i++) {
            patientNames[i] = patients.get(i).getId() + " - " + patients.get(i).getName();
        }
        
        String[] doctorNames = new String[doctors.size()];
        for (int i = 0; i < doctors.size(); i++) {
            doctorNames[i] = doctors.get(i).getId() + " - Dr. " + doctors.get(i).getName();
        }
        
        JComboBox<String> patientBox = new JComboBox<>(patientNames);
        JComboBox<String> doctorBox = new JComboBox<>(doctorNames);
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField timeField = new JTextField("10:00");
        JTextField reasonField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Patient:"));
        panel.add(patientBox);
        panel.add(new JLabel("Doctor:"));
        panel.add(doctorBox);
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:mm):"));
        panel.add(timeField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Appointment", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String dateStr = dateField.getText().trim();
            String timeStr = timeField.getText().trim();
            String reason = reasonField.getText().trim();
            
            if (!InputValidator.isValidDate(dateStr)) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidTime(timeStr)) {
                JOptionPane.showMessageDialog(this, "Invalid time format! Use HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidString(reason)) {
                JOptionPane.showMessageDialog(this, "Reason is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int patientIndex = patientBox.getSelectedIndex();
            int doctorIndex = doctorBox.getSelectedIndex();
            
            Patient patient = patients.get(patientIndex);
            Doctor doctor = doctors.get(doctorIndex);
            
            LocalDate date = InputValidator.parseDate(dateStr);
            LocalTime time = InputValidator.parseTime(timeStr);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            
            Appointment appointment = appointmentManager.scheduleAppointment(patient, doctor, dateTime, reason);
            
            if (appointment == null) {
                JOptionPane.showMessageDialog(this, "Failed to schedule appointment! Doctor may have a conflict.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
        }
    }
    
    private void editAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "Appointment not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        JTextField dateField = new JTextField(appointment.getAppointmentDateTime().format(dateFormatter));
        JTextField timeField = new JTextField(appointment.getAppointmentDateTime().format(timeFormatter));
        JTextField reasonField = new JTextField(appointment.getReason());
        JTextField notesField = new JTextField(appointment.getNotes() != null ? appointment.getNotes() : "");
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:mm):"));
        panel.add(timeField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String dateStr = dateField.getText().trim();
            String timeStr = timeField.getText().trim();
            String reason = reasonField.getText().trim();
            String notes = notesField.getText().trim();
            
            if (!InputValidator.isValidDate(dateStr)) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidTime(timeStr)) {
                JOptionPane.showMessageDialog(this, "Invalid time format! Use HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!InputValidator.isValidString(reason)) {
                JOptionPane.showMessageDialog(this, "Reason is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDate date = InputValidator.parseDate(dateStr);
            LocalTime time = InputValidator.parseTime(timeStr);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            
            boolean success = appointmentManager.updateAppointment(appointmentId, dateTime, reason, notes);
            
            if (!success) {
                JOptionPane.showMessageDialog(this, "Failed to update appointment! Doctor may have a conflict.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
        }
    }
    
    private void deleteAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            appointmentManager.deleteAppointment(appointmentId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment deleted successfully!");
        }
    }
    
    private void changeStatus() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment appointment = appointmentManager.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "Appointment not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String[] statuses = {"SCHEDULED", "CONFIRMED", "IN_PROGRESS", "COMPLETED", "CANCELLED", "NO_SHOW"};
        String currentStatus = appointment.getStatus().toString();
        
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Change Appointment Status",
            JOptionPane.PLAIN_MESSAGE,
            null,
            statuses,
            currentStatus
        );
        
        if (newStatus != null && !newStatus.equals(currentStatus)) {
            appointment.setStatus(Appointment.AppointmentStatus.valueOf(newStatus));
            refreshTable();
            JOptionPane.showMessageDialog(this, "Status updated successfully!");
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Appointment appointment : appointments) {
            Object[] row = {
                appointment.getId(),
                appointment.getAppointmentDateTime().format(formatter),
                appointment.getPatient().getName(),
                "Dr. " + appointment.getDoctor().getName(),
                appointment.getReason(),
                appointment.getStatus().toString()
            };
            tableModel.addRow(row);
        }
    }
}
