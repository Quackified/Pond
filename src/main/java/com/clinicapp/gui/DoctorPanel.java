package com.clinicapp.gui;

import com.clinicapp.dao.DoctorDAO;
import com.clinicapp.model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for managing doctor records with UTF-8 text support.
 */
public class DoctorPanel extends JPanel {
    private DoctorDAO doctorDAO;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    
    public DoctorPanel() {
        doctorDAO = new DoctorDAO();
        initializeComponents();
        layoutComponents();
        loadDoctors();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columnNames = {"ID", "Name", "Specialization", "Phone", "Email", "Schedule", "Available"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        doctorTable = new JTable(tableModel);
        doctorTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        doctorTable.setRowHeight(25);
        doctorTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        addButton = new JButton("Add Doctor");
        editButton = new JButton("Edit Doctor");
        deleteButton = new JButton("Delete Doctor");
        refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> showAddDoctorDialog());
        editButton.addActionListener(e -> showEditDoctorDialog());
        deleteButton.addActionListener(e -> deleteDoctor());
        refreshButton.addActionListener(e -> loadDoctors());
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField.addActionListener(e -> searchDoctors());
    }
    
    private void layoutComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search by Name:"));
        topPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchDoctors());
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Doctor Records"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadDoctors() {
        try {
            tableModel.setRowCount(0);
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            
            for (Doctor doctor : doctors) {
                String schedule = String.join(", ", doctor.getAvailableDays()) + 
                                " " + doctor.getStartTime() + "-" + doctor.getEndTime();
                Object[] row = {
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getPhoneNumber(),
                    doctor.getEmail() != null ? doctor.getEmail() : "",
                    schedule,
                    doctor.isAvailable() ? "Yes" : "No"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            showError("Error loading doctors: " + e.getMessage());
        }
    }
    
    private void searchDoctors() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadDoctors();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<Doctor> doctors = doctorDAO.searchDoctorsByName(searchTerm);
            
            for (Doctor doctor : doctors) {
                String schedule = String.join(", ", doctor.getAvailableDays()) + 
                                " " + doctor.getStartTime() + "-" + doctor.getEndTime();
                Object[] row = {
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getPhoneNumber(),
                    doctor.getEmail() != null ? doctor.getEmail() : "",
                    schedule,
                    doctor.isAvailable() ? "Yes" : "No"
                };
                tableModel.addRow(row);
            }
            
            if (doctors.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No doctors found matching: " + searchTerm,
                    "Search Results", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Error searching doctors: " + e.getMessage());
        }
    }
    
    private void showAddDoctorDialog() {
        DoctorFormDialog dialog = new DoctorFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), 
            "Add New Doctor", 
            null
        );
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Doctor doctor = dialog.getDoctor();
            try {
                doctorDAO.addDoctor(doctor);
                loadDoctors();
                JOptionPane.showMessageDialog(this, 
                    "Doctor added successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error adding doctor: " + e.getMessage());
            }
        }
    }
    
    private void showEditDoctorDialog() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a doctor to edit.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            Doctor doctor = doctorDAO.getDoctorById(doctorId);
            if (doctor == null) {
                showError("Doctor not found.");
                return;
            }
            
            DoctorFormDialog dialog = new DoctorFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                "Edit Doctor", 
                doctor
            );
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Doctor updatedDoctor = dialog.getDoctor();
                updatedDoctor.setId(doctorId);
                doctorDAO.updateDoctor(updatedDoctor);
                loadDoctors();
                JOptionPane.showMessageDialog(this, 
                    "Doctor updated successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Error updating doctor: " + e.getMessage());
        }
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a doctor to delete.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        String doctorName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete doctor: " + doctorName + "?\n" +
            "This will also delete all associated appointments.",
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                doctorDAO.deleteDoctor(doctorId);
                loadDoctors();
                JOptionPane.showMessageDialog(this, 
                    "Doctor deleted successfully!",
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showError("Error deleting doctor: " + e.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
