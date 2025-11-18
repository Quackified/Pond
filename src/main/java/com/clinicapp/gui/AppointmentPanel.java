package com.clinicapp.gui;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Panel for managing appointments.
 */
public class AppointmentPanel extends JPanel {
    
    private final AppointmentManager appointmentManager;
    private final PatientManager patientManager;
    private final DoctorManager doctorManager;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    
    public AppointmentPanel(AppointmentManager appointmentManager, PatientManager patientManager, DoctorManager doctorManager) {
        this.appointmentManager = appointmentManager;
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
        initializeUI();
        refreshTable();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        String[] columnNames = {"ID", "Patient", "Doctor", "Date & Time", "Reason", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton scheduleButton = new JButton("Schedule Appointment");
        JButton viewButton = new JButton("View Details");
        JButton updateStatusButton = new JButton("Update Status");
        JButton cancelButton = new JButton("Cancel Appointment");
        JButton refreshButton = new JButton("Refresh");
        
        scheduleButton.addActionListener(e -> showScheduleAppointmentDialog());
        viewButton.addActionListener(e -> viewAppointmentDetails());
        updateStatusButton.addActionListener(e -> showUpdateStatusDialog());
        cancelButton.addActionListener(e -> cancelAppointment());
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(scheduleButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = appointmentManager.getAllAppointments();
        for (Appointment apt : appointments) {
            Object[] row = {
                apt.getId(),
                apt.getPatient().getName(),
                "Dr. " + apt.getDoctor().getName(),
                InputValidator.formatDateTime(apt.getAppointmentDateTime()),
                apt.getReason(),
                apt.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showScheduleAppointmentDialog() {
        List<Patient> patients = patientManager.getAllPatients();
        List<Doctor> doctors = doctorManager.getAllDoctors();
        
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients in system! Please add patients first.", 
                "Cannot Schedule", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No doctors in system! Please add doctors first.", 
                "Cannot Schedule", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Schedule Appointment", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] patientOptions = patients.stream()
            .map(p -> p.getId() + " - " + p.getName())
            .toArray(String[]::new);
        JComboBox<String> patientBox = new JComboBox<>(patientOptions);
        
        String[] doctorOptions = doctors.stream()
            .map(d -> d.getId() + " - Dr. " + d.getName() + " (" + d.getSpecialization() + ")")
            .toArray(String[]::new);
        JComboBox<String> doctorBox = new JComboBox<>(doctorOptions);
        
        JTextField dateField = new JTextField(20);
        JTextField timeField = new JTextField(20);
        JTextArea reasonArea = new JTextArea(3, 20);
        
        int row = 0;
        addLabelAndField(panel, gbc, row++, "Patient:", patientBox);
        addLabelAndField(panel, gbc, row++, "Doctor:", doctorBox);
        addLabelAndField(panel, gbc, row++, "Date (yyyy-MM-dd):", dateField);
        addLabelAndField(panel, gbc, row++, "Time (HH:mm):", timeField);
        addLabelAndField(panel, gbc, row++, "Reason:", new JScrollPane(reasonArea));
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Schedule");
        JButton cancelBtn = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            int patientId = extractId((String) patientBox.getSelectedItem());
            int doctorId = extractId((String) doctorBox.getSelectedItem());
            
            if (validateAndScheduleAppointment(patientId, doctorId, dateField.getText(),
                    timeField.getText(), reasonArea.getText())) {
                refreshTable();
                dialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private int extractId(String option) {
        return Integer.parseInt(option.split(" - ")[0]);
    }
    
    private boolean validateAndScheduleAppointment(int patientId, int doctorId, 
            String dateStr, String timeStr, String reason) {
        
        if (!InputValidator.isNonEmptyString(reason)) {
            JOptionPane.showMessageDialog(this, "Reason is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        LocalDate date = InputValidator.parseDate(dateStr);
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        LocalTime time = InputValidator.parseTime(timeStr);
        if (time == null) {
            JOptionPane.showMessageDialog(this, "Invalid time format! Use HH:mm", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        Patient patient = patientManager.getPatientById(patientId);
        Doctor doctor = doctorManager.getDoctorById(doctorId);
        
        Appointment appointment = appointmentManager.scheduleAppointment(patient, doctor, dateTime, reason);
        
        if (appointment == null) {
            JOptionPane.showMessageDialog(this, "Failed to schedule appointment! Doctor may have a conflict at this time.", 
                "Schedule Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    private void viewAppointmentDetails() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment apt = appointmentManager.getAppointmentById(appointmentId);
        
        if (apt != null) {
            String details = String.format(
                "Appointment Details:\n\n" +
                "ID: %d\n" +
                "Patient: %s (ID: %d)\n" +
                "Doctor: Dr. %s (ID: %d)\n" +
                "Date & Time: %s\n" +
                "Reason: %s\n" +
                "Status: %s\n" +
                "Notes: %s\n" +
                "Created At: %s",
                apt.getId(),
                apt.getPatient().getName(),
                apt.getPatient().getId(),
                apt.getDoctor().getName(),
                apt.getDoctor().getId(),
                InputValidator.formatDateTime(apt.getAppointmentDateTime()),
                apt.getReason(),
                apt.getStatus(),
                apt.getNotes() != null && !apt.getNotes().isEmpty() ? apt.getNotes() : "None",
                InputValidator.formatDateTime(apt.getCreatedAt())
            );
            
            JOptionPane.showMessageDialog(this, details, "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showUpdateStatusDialog() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment apt = appointmentManager.getAppointmentById(appointmentId);
        
        if (apt == null) return;
        
        AppointmentStatus[] statuses = AppointmentStatus.values();
        String[] statusNames = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statusNames[i] = statuses[i].toString();
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Appointment Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusNames,
            apt.getStatus().toString()
        );
        
        if (selected != null) {
            AppointmentStatus newStatus = AppointmentStatus.valueOf(selected);
            
            if (newStatus == AppointmentStatus.COMPLETED) {
                String notes = JOptionPane.showInputDialog(this, "Enter completion notes (optional):");
                appointmentManager.completeAppointment(appointmentId, notes);
            } else if (newStatus == AppointmentStatus.CANCELLED) {
                appointmentManager.cancelAppointment(appointmentId);
            } else if (newStatus == AppointmentStatus.CONFIRMED) {
                appointmentManager.confirmAppointment(appointmentId);
            } else if (newStatus == AppointmentStatus.NO_SHOW) {
                appointmentManager.markNoShow(appointmentId);
            }
            
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment status updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this appointment?",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
            appointmentManager.cancelAppointment(appointmentId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
