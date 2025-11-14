package com.clinicapp.gui;

import com.clinicapp.dao.AppointmentDAO;
import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for managing appointments with UTF-8 text support.
 */
public class AppointmentPanel extends JPanel {
    private AppointmentDAO appointmentDAO;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton updateStatusButton;
    
    public AppointmentPanel() {
        appointmentDAO = new AppointmentDAO();
        initializeComponents();
        layoutComponents();
        loadAppointments();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"ID", "Date & Time", "Patient", "Doctor", "Reason", "Status", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        appointmentTable.setRowHeight(25);
        appointmentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        addButton = new JButton("Schedule Appointment");
        editButton = new JButton("Edit Appointment");
        deleteButton = new JButton("Delete Appointment");
        refreshButton = new JButton("Refresh");
        updateStatusButton = new JButton("Update Status");
        
        addButton.addActionListener(e -> showAddAppointmentDialog());
        editButton.addActionListener(e -> showEditAppointmentDialog());
        deleteButton.addActionListener(e -> deleteAppointment());
        refreshButton.addActionListener(e -> loadAppointments());
        updateStatusButton.addActionListener(e -> updateAppointmentStatus());
    }
    
    private void layoutComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("All Appointments"));
        topPanel.add(refreshButton);
        
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Appointment Records"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(deleteButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadAppointments() {
        try {
            tableModel.setRowCount(0);
            List<Appointment> appointments = appointmentDAO.getAllAppointments();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            for (Appointment appointment : appointments) {
                Object[] row = {
                    appointment.getId(),
                    appointment.getAppointmentDateTime().format(formatter),
                    appointment.getPatient().getName(),
                    "Dr. " + appointment.getDoctor().getName(),
                    appointment.getReason(),
                    appointment.getStatus().name(),
                    appointment.getNotes() != null && !appointment.getNotes().isEmpty() ? 
                        appointment.getNotes() : ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Error loading appointments: " + e.getMessage());
        }
    }
    
    private void showAddAppointmentDialog() {
        AppointmentFormDialog dialog = new AppointmentFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            "Schedule New Appointment", 
            null
        );
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Appointment appointment = dialog.getAppointment();
            try {
                appointmentDAO.addAppointment(appointment);
                loadAppointments();
                JOptionPane.showMessageDialog(this, 
                    "Appointment scheduled successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error scheduling appointment: " + e.getMessage());
            }
        }
    }
    
    private void showEditAppointmentDialog() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select an appointment to edit.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
            if (appointment == null) {
                showError("Appointment not found.");
                return;
            }
            
            AppointmentFormDialog dialog = new AppointmentFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                "Edit Appointment", 
                appointment
            );
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Appointment updatedAppointment = dialog.getAppointment();
                updatedAppointment.setId(appointmentId);
                appointmentDAO.updateAppointment(updatedAppointment);
                loadAppointments();
                JOptionPane.showMessageDialog(this, 
                    "Appointment updated successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Error updating appointment: " + e.getMessage());
        }
    }
    
    private void updateAppointmentStatus() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select an appointment to update status.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        
        AppointmentStatus[] statuses = AppointmentStatus.values();
        AppointmentStatus selectedStatus = (AppointmentStatus) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Appointment Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statuses,
            statuses[0]
        );
        
        if (selectedStatus != null) {
            try {
                appointmentDAO.updateAppointmentStatus(appointmentId, selectedStatus);
                loadAppointments();
                JOptionPane.showMessageDialog(this, 
                    "Appointment status updated successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error updating appointment status: " + e.getMessage());
            }
        }
    }
    
    private void deleteAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select an appointment to delete.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 2);
        String doctorName = (String) tableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this appointment?\n" +
            "Patient: " + patientName + "\n" +
            "Doctor: " + doctorName,
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                appointmentDAO.deleteAppointment(appointmentId);
                loadAppointments();
                JOptionPane.showMessageDialog(this, 
                    "Appointment deleted successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error deleting appointment: " + e.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
