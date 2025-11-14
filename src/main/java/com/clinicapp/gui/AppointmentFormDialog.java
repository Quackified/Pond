package com.clinicapp.gui;

import com.clinicapp.dao.PatientDAO;
import com.clinicapp.dao.DoctorDAO;
import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Dialog for scheduling/editing appointments with UTF-8 text input support.
 */
public class AppointmentFormDialog extends JDialog {
    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;
    
    private JComboBox<PatientItem> patientCombo;
    private JComboBox<DoctorItem> doctorCombo;
    private JTextField dateTimeField;
    private JTextArea reasonArea;
    private JComboBox<AppointmentStatus> statusCombo;
    private JTextArea notesArea;
    
    private boolean confirmed = false;
    private Appointment appointment;
    
    public AppointmentFormDialog(JFrame parent, String title, Appointment appointment) {
        super(parent, title, true);
        this.appointment = appointment;
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
        
        initializeComponents();
        layoutComponents();
        
        if (appointment != null) {
            populateFields(appointment);
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        Font textFont = new Font("SansSerif", Font.PLAIN, 12);
        
        patientCombo = new JComboBox<>();
        patientCombo.setFont(textFont);
        loadPatients();
        
        doctorCombo = new JComboBox<>();
        doctorCombo.setFont(textFont);
        loadDoctors();
        
        dateTimeField = new JTextField(20);
        dateTimeField.setFont(textFont);
        dateTimeField.setToolTipText("Format: yyyy-MM-dd HH:mm (e.g., 2024-12-15 14:30)");
        
        reasonArea = new JTextArea(3, 25);
        reasonArea.setFont(textFont);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        
        statusCombo = new JComboBox<>(AppointmentStatus.values());
        statusCombo.setFont(textFont);
        
        notesArea = new JTextArea(3, 25);
        notesArea.setFont(textFont);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
    }
    
    private void loadPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for (Patient patient : patients) {
                patientCombo.addItem(new PatientItem(patient));
            }
        } catch (SQLException e) {
            showError("Error loading patients: " + e.getMessage());
        }
    }
    
    private void loadDoctors() {
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            for (Doctor doctor : doctors) {
                doctorCombo.addItem(new DoctorItem(doctor));
            }
        } catch (SQLException e) {
            showError("Error loading doctors: " + e.getMessage());
        }
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Patient
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Patient: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(patientCombo, gbc);
        
        // Doctor
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Doctor: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(doctorCombo, gbc);
        
        // Date & Time
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Date & Time: *"), gbc);
        gbc.gridx = 1;
        mainPanel.add(dateTimeField, gbc);
        
        // Reason
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(new JLabel("Reason: *"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(new JScrollPane(reasonArea), gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(statusCombo, gbc);
        
        // Notes
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(new JScrollPane(notesArea), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> saveAppointment());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void populateFields(Appointment appointment) {
        // Select patient
        for (int i = 0; i < patientCombo.getItemCount(); i++) {
            PatientItem item = patientCombo.getItemAt(i);
            if (item.getPatient().getId() == appointment.getPatient().getId()) {
                patientCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Select doctor
        for (int i = 0; i < doctorCombo.getItemCount(); i++) {
            DoctorItem item = doctorCombo.getItemAt(i);
            if (item.getDoctor().getId() == appointment.getDoctor().getId()) {
                doctorCombo.setSelectedIndex(i);
                break;
            }
        }
        
        dateTimeField.setText(appointment.getAppointmentDateTime()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        reasonArea.setText(appointment.getReason());
        statusCombo.setSelectedItem(appointment.getStatus());
        notesArea.setText(appointment.getNotes() != null ? appointment.getNotes() : "");
    }
    
    private void saveAppointment() {
        PatientItem selectedPatient = (PatientItem) patientCombo.getSelectedItem();
        if (selectedPatient == null) {
            showError("Please select a patient.");
            return;
        }
        
        DoctorItem selectedDoctor = (DoctorItem) doctorCombo.getSelectedItem();
        if (selectedDoctor == null) {
            showError("Please select a doctor.");
            return;
        }
        
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeField.getText().trim(), 
                                          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            showError("Invalid date/time format. Use yyyy-MM-dd HH:mm (e.g., 2024-12-15 14:30)");
            return;
        }
        
        String reason = reasonArea.getText().trim();
        if (reason.isEmpty()) {
            showError("Reason is required.");
            return;
        }
        
        AppointmentStatus status = (AppointmentStatus) statusCombo.getSelectedItem();
        String notes = notesArea.getText().trim();
        
        if (appointment == null) {
            appointment = new Appointment(
                selectedPatient.getPatient(), 
                selectedDoctor.getDoctor(), 
                dateTime, 
                reason
            );
            appointment.setStatus(status);
            appointment.setNotes(notes);
        } else {
            appointment.setPatient(selectedPatient.getPatient());
            appointment.setDoctor(selectedDoctor.getDoctor());
            appointment.setAppointmentDateTime(dateTime);
            appointment.setReason(reason);
            appointment.setStatus(status);
            appointment.setNotes(notes);
        }
        
        confirmed = true;
        dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    private static class PatientItem {
        private Patient patient;
        
        public PatientItem(Patient patient) {
            this.patient = patient;
        }
        
        public Patient getPatient() {
            return patient;
        }
        
        @Override
        public String toString() {
            return patient.getName() + " (ID: " + patient.getId() + ")";
        }
    }
    
    private static class DoctorItem {
        private Doctor doctor;
        
        public DoctorItem(Doctor doctor) {
            this.doctor = doctor;
        }
        
        public Doctor getDoctor() {
            return doctor;
        }
        
        @Override
        public String toString() {
            return "Dr. " + doctor.getName() + " - " + doctor.getSpecialization() + 
                   " (ID: " + doctor.getId() + ")";
        }
    }
}
