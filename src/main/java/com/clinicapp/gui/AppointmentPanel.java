package com.clinicapp.gui;

import com.clinicapp.dao.AppointmentDAO;
import com.clinicapp.model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AppointmentPanel - GUI panel for managing appointments.
 */
public class AppointmentPanel extends JPanel {
    
    private AppointmentDAO appointmentDAO;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    
    public AppointmentPanel() {
        appointmentDAO = new AppointmentDAO();
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        
        refreshTable();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Patient", "Doctor", "Date", "Time", "Reason", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        appointmentTable.setRowHeight(25);
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return new JScrollPane(appointmentTable);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addBtn = new JButton("Schedule Appointment");
        addBtn.setBackground(new Color(46, 125, 50));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddAppointmentDialog());
        
        JButton editBtn = new JButton("Edit Appointment");
        editBtn.setBackground(new Color(25, 118, 210));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(new Font("Arial", Font.BOLD, 12));
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> showEditAppointmentDialog());
        
        JButton cancelBtn = new JButton("Cancel Appointment");
        cancelBtn.setBackground(new Color(211, 47, 47));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> cancelAppointment());
        
        JButton completeBtn = new JButton("Complete Appointment");
        completeBtn.setBackground(new Color(76, 175, 80));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        completeBtn.setFocusPainted(false);
        completeBtn.addActionListener(e -> completeAppointment());
        
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(cancelBtn);
        panel.add(completeBtn);
        
        return panel;
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Appointment appointment : appointments) {
            Object[] row = {
                appointment.getId(),
                appointment.getPatient().getName(),
                "Dr. " + appointment.getDoctor().getName(),
                appointment.getAppointmentDateTime().format(dateFormatter),
                appointment.getAppointmentDateTime().format(timeFormatter),
                appointment.getReason(),
                appointment.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddAppointmentDialog() {
        AppointmentFormDialog dialog = new AppointmentFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            refreshTable();
        }
    }
    
    private void showEditAppointmentDialog() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an appointment to edit.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        
        if (appointment != null) {
            AppointmentFormDialog dialog = new AppointmentFormDialog((Frame) SwingUtilities.getWindowAncestor(this), appointment);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                refreshTable();
            }
        }
    }
    
    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an appointment to cancel.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel the appointment for " + patientName + "?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
            if (appointment != null) {
                appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
                
                if (appointmentDAO.updateAppointment(appointment)) {
                    JOptionPane.showMessageDialog(this,
                        "Appointment cancelled successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to cancel appointment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void completeAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an appointment to complete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        
        if (appointment != null) {
            String notes = JOptionPane.showInputDialog(this,
                "Enter completion notes (optional):",
                "Complete Appointment",
                JOptionPane.PLAIN_MESSAGE);
            
            if (notes != null) {
                appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
                if (notes != null && !notes.trim().isEmpty()) {
                    appointment.setNotes(notes);
                }
                
                if (appointmentDAO.updateAppointment(appointment)) {
                    JOptionPane.showMessageDialog(this,
                        "Appointment completed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to complete appointment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
