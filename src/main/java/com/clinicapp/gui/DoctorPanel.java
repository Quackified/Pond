package com.clinicapp.gui;

import com.clinicapp.dao.DoctorDAO;
import com.clinicapp.model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * DoctorPanel - GUI panel for managing doctors.
 */
public class DoctorPanel extends JPanel {
    
    private DoctorDAO doctorDAO;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public DoctorPanel() {
        doctorDAO = new DoctorDAO();
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
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchDoctors());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Name", "Specialization", "Phone", "Email", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        doctorTable = new JTable(tableModel);
        doctorTable.setFont(new Font("Arial", Font.PLAIN, 12));
        doctorTable.setRowHeight(25);
        doctorTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return new JScrollPane(doctorTable);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addBtn = new JButton("Add Doctor");
        addBtn.setBackground(new Color(46, 125, 50));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddDoctorDialog());
        
        JButton editBtn = new JButton("Edit Doctor");
        editBtn.setBackground(new Color(25, 118, 210));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(new Font("Arial", Font.BOLD, 12));
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> showEditDoctorDialog());
        
        JButton deleteBtn = new JButton("Delete Doctor");
        deleteBtn.setBackground(new Color(211, 47, 47));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteDoctor());
        
        JButton viewBtn = new JButton("View Details");
        viewBtn.setBackground(new Color(255, 152, 0));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> viewDoctorDetails());
        
        JButton toggleBtn = new JButton("Toggle Availability");
        toggleBtn.setBackground(new Color(103, 58, 183));
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setFont(new Font("Arial", Font.BOLD, 12));
        toggleBtn.setFocusPainted(false);
        toggleBtn.addActionListener(e -> toggleAvailability());
        
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(viewBtn);
        panel.add(toggleBtn);
        
        return panel;
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        
        for (Doctor doctor : doctors) {
            Object[] row = {
                doctor.getId(),
                "Dr. " + doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber(),
                doctor.getEmail() != null ? doctor.getEmail() : "N/A",
                doctor.isAvailable() ? "Available" : "Unavailable"
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchDoctors() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Doctor> doctors = doctorDAO.searchDoctorsByName(searchTerm);
        
        for (Doctor doctor : doctors) {
            Object[] row = {
                doctor.getId(),
                "Dr. " + doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhoneNumber(),
                doctor.getEmail() != null ? doctor.getEmail() : "N/A",
                doctor.isAvailable() ? "Available" : "Unavailable"
            };
            tableModel.addRow(row);
        }
        
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No doctors found matching: " + searchTerm,
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAddDoctorDialog() {
        DoctorFormDialog dialog = new DoctorFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            refreshTable();
        }
    }
    
    private void showEditDoctorDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a doctor to edit.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        
        if (doctor != null) {
            DoctorFormDialog dialog = new DoctorFormDialog((Frame) SwingUtilities.getWindowAncestor(this), doctor);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                refreshTable();
            }
        }
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a doctor to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        String doctorName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete " + doctorName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (doctorDAO.deleteDoctor(doctorId)) {
                JOptionPane.showMessageDialog(this,
                    "Doctor deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete doctor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewDoctorDetails() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a doctor to view details.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        
        if (doctor != null) {
            JOptionPane.showMessageDialog(this,
                doctor.getDetailedInfo(),
                "Doctor Details",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void toggleAvailability() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a doctor to toggle availability.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        
        if (doctor != null) {
            doctor.setAvailable(!doctor.isAvailable());
            
            if (doctorDAO.updateDoctor(doctor)) {
                JOptionPane.showMessageDialog(this,
                    "Doctor availability updated!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update availability.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
