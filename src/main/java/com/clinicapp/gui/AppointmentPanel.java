package com.clinicapp.gui;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;
import com.clinicapp.util.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentPanel extends JPanel {
    
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;
    private final AppointmentManager appointmentManager;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    
    public AppointmentPanel(PatientManager patientManager, DoctorManager doctorManager, 
                           AppointmentManager appointmentManager) {
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
        this.appointmentManager = appointmentManager;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton scheduleButton = new JButton("Schedule");
        JButton viewButton = new JButton("View Details");
        JButton updateButton = new JButton("Update");
        JButton confirmButton = new JButton("Confirm");
        JButton completeButton = new JButton("Complete");
        JButton cancelButton = new JButton("Cancel");
        JButton refreshButton = new JButton("Refresh");
        
        scheduleButton.addActionListener(e -> scheduleAppointment());
        viewButton.addActionListener(e -> viewAppointment());
        updateButton.addActionListener(e -> updateAppointment());
        confirmButton.addActionListener(e -> confirmAppointment());
        completeButton.addActionListener(e -> completeAppointment());
        cancelButton.addActionListener(e -> cancelAppointment());
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(scheduleButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        
        // Create table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Time", "Patient", "Doctor", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Appointment apt : appointmentManager.getAllAppointments()) {
            String timeStr = apt.getStartTime().format(timeFormatter) + "-" + apt.getEndTime().format(timeFormatter);
            tableModel.addRow(new Object[]{
                apt.getId(),
                apt.getAppointmentDate().format(dateFormatter),
                timeStr,
                apt.getPatient().getName(),
                apt.getDoctor().getName(),
                apt.getStatus()
            });
        }
    }
    
    private void scheduleAppointment() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField patientIdField = new JTextField();
        JTextField doctorIdField = new JTextField();
        JTextField dateField = new JTextField("yyyy-MM-dd");
        JTextField startTimeField = new JTextField("HH:mm");
        JTextField endTimeField = new JTextField("HH:mm");
        JTextField reasonField = new JTextField();
        
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Doctor ID:"));
        panel.add(doctorIdField);
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Start Time (HH:mm):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:mm):"));
        panel.add(endTimeField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Schedule Appointment", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int patientId = Integer.parseInt(patientIdField.getText().trim());
                int doctorId = Integer.parseInt(doctorIdField.getText().trim());
                String dateStr = dateField.getText().trim();
                String startTimeStr = startTimeField.getText().trim();
                String endTimeStr = endTimeField.getText().trim();
                String reason = reasonField.getText().trim();
                
                Patient patient = patientManager.getPatientById(patientId);
                Doctor doctor = doctorManager.getDoctorById(doctorId);
                
                if (patient == null) {
                    JOptionPane.showMessageDialog(this, "Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (doctor == null) {
                    JOptionPane.showMessageDialog(this, "Doctor not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidDateFormat(dateStr)) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidTimeFormat(startTimeStr)) {
                    JOptionPane.showMessageDialog(this, "Invalid start time format. Use HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidTimeFormat(endTimeStr)) {
                    JOptionPane.showMessageDialog(this, "Invalid end time format. Use HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidString(reason)) {
                    JOptionPane.showMessageDialog(this, "Reason is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LocalDate date = LocalDate.parse(dateStr);
                LocalTime startTime = LocalTime.parse(startTimeStr);
                LocalTime endTime = LocalTime.parse(endTimeStr);
                
                if (appointmentManager.scheduleAppointment(patient, doctor, date, startTime, endTime, reason) != null) {
                    JOptionPane.showMessageDialog(this, "Appointment scheduled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to schedule appointment. Check for conflicts.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment apt = appointmentManager.getAppointmentById(id);
        if (apt != null) {
            JOptionPane.showMessageDialog(this, apt.getDetailedInfo(), "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment apt = appointmentManager.getAppointmentById(id);
        if (apt == null) return;
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        JTextField dateField = new JTextField(apt.getAppointmentDate().format(dateFormatter));
        JTextField startTimeField = new JTextField(apt.getStartTime().format(timeFormatter));
        JTextField reasonField = new JTextField(apt.getReason());
        JTextField notesField = new JTextField(apt.getNotes() != null ? apt.getNotes() : "");
        
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        panel.add(dateField);
        panel.add(new JLabel("Start Time (HH:mm):"));
        panel.add(startTimeField);
        panel.add(new JLabel("Reason:"));
        panel.add(reasonField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Update Appointment", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String dateStr = dateField.getText().trim();
                String startTimeStr = startTimeField.getText().trim();
                String reason = reasonField.getText().trim();
                String notes = notesField.getText().trim();
                
                if (!InputValidator.isValidDateFormat(dateStr)) {
                    JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!InputValidator.isValidTimeFormat(startTimeStr)) {
                    JOptionPane.showMessageDialog(this, "Invalid time format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LocalDate date = LocalDate.parse(dateStr);
                LocalTime startTime = LocalTime.parse(startTimeStr);
                LocalTime endTime = startTime.plusHours(1);
                
                if (appointmentManager.updateAppointment(id, date, startTime, endTime, reason, notes)) {
                    JOptionPane.showMessageDialog(this, "Appointment updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void confirmAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        if (appointmentManager.confirmAppointment(id)) {
            JOptionPane.showMessageDialog(this, "Appointment confirmed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to confirm appointment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void completeAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String notes = JOptionPane.showInputDialog(this, "Enter completion notes (optional):", "");
        
        if (notes != null) {
            if (appointmentManager.completeAppointment(id, notes)) {
                JOptionPane.showMessageDialog(this, "Appointment marked as completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to complete appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this appointment?", 
                                                   "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (appointmentManager.cancelAppointment(id)) {
                JOptionPane.showMessageDialog(this, "Appointment cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
