package com.clinicapp.gui;

import com.clinicapp.dao.PatientDAO;
import com.clinicapp.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PatientPanel - GUI panel for managing patients.
 */
public class PatientPanel extends JPanel {
    
    private PatientDAO patientDAO;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public PatientPanel() {
        patientDAO = new PatientDAO();
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
        searchBtn.addActionListener(e -> searchPatients());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Name", "DOB", "Age", "Gender", "Phone", "Email", "Blood Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 12));
        patientTable.setRowHeight(25);
        patientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return new JScrollPane(patientTable);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addBtn = new JButton("Add Patient");
        addBtn.setBackground(new Color(46, 125, 50));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddPatientDialog());
        
        JButton editBtn = new JButton("Edit Patient");
        editBtn.setBackground(new Color(25, 118, 210));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(new Font("Arial", Font.BOLD, 12));
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> showEditPatientDialog());
        
        JButton deleteBtn = new JButton("Delete Patient");
        deleteBtn.setBackground(new Color(211, 47, 47));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deletePatient());
        
        JButton viewBtn = new JButton("View Details");
        viewBtn.setBackground(new Color(255, 152, 0));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Arial", Font.BOLD, 12));
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> viewPatientDetails());
        
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(viewBtn);
        
        return panel;
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientDAO.getAllPatients();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Patient patient : patients) {
            Object[] row = {
                patient.getId(),
                patient.getName(),
                patient.getDateOfBirth().format(formatter),
                patient.getAge(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail() != null ? patient.getEmail() : "N/A",
                patient.getBloodType() != null ? patient.getBloodType() : "N/A"
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchPatients() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Patient> patients = patientDAO.searchPatientsByName(searchTerm);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Patient patient : patients) {
            Object[] row = {
                patient.getId(),
                patient.getName(),
                patient.getDateOfBirth().format(formatter),
                patient.getAge(),
                patient.getGender(),
                patient.getPhoneNumber(),
                patient.getEmail() != null ? patient.getEmail() : "N/A",
                patient.getBloodType() != null ? patient.getBloodType() : "N/A"
            };
            tableModel.addRow(row);
        }
        
        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No patients found matching: " + searchTerm,
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAddPatientDialog() {
        PatientFormDialog dialog = new PatientFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        
        if (dialog.isSaved()) {
            refreshTable();
        }
    }
    
    private void showEditPatientDialog() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a patient to edit.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientDAO.getPatientById(patientId);
        
        if (patient != null) {
            PatientFormDialog dialog = new PatientFormDialog((Frame) SwingUtilities.getWindowAncestor(this), patient);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                refreshTable();
            }
        }
    }
    
    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a patient to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete patient: " + patientName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (patientDAO.deletePatient(patientId)) {
                JOptionPane.showMessageDialog(this,
                    "Patient deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete patient.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewPatientDetails() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a patient to view details.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        Patient patient = patientDAO.getPatientById(patientId);
        
        if (patient != null) {
            JOptionPane.showMessageDialog(this,
                patient.getDetailedInfo(),
                "Patient Details",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
