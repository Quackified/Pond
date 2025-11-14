package com.clinicapp.gui;

import com.clinicapp.dao.AppointmentDAO;
import com.clinicapp.dao.PatientDAO;
import com.clinicapp.dao.DoctorDAO;
import com.clinicapp.model.Appointment;
import com.clinicapp.model.Appointment.AppointmentStatus;
import com.clinicapp.model.Patient;
import com.clinicapp.model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * AppointmentFormDialog - Dialog for adding or editing appointment information.
 */
public class AppointmentFormDialog extends JDialog {
    
    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;
    private Appointment appointment;
    private boolean saved = false;
    
    private JComboBox<String> patientCombo;
    private JComboBox<String> doctorCombo;
    private JTextField dateField;
    private JTextField timeField;
    private JTextArea reasonArea;
    private JComboBox<AppointmentStatus> statusCombo;
    private JTextArea notesArea;
    
    private List<Patient> patients;
    private List<Doctor> doctors;
    
    public AppointmentFormDialog(Frame parent, Appointment appointment) {
        super(parent, appointment == null ? "Schedule Appointment" : "Edit Appointment", true);
        this.appointmentDAO = new AppointmentDAO();
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
        this.appointment = appointment;
        
        loadData();
        initializeUI();
        
        if (appointment != null) {
            populateFields();
        }
    }
    
    private void loadData() {
        patients = patientDAO.getAllPatients();
        doctors = doctorDAO.getAllDoctors();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 550);
        setLocationRelativeTo(getParent());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Patient:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        patientCombo = new JComboBox<>();
        for (Patient patient : patients) {
            patientCombo.addItem(patient.getId() + " - " + patient.getName());
        }
        formPanel.add(patientCombo, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Doctor:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        doctorCombo = new JComboBox<>();
        for (Doctor doctor : doctors) {
            doctorCombo.addItem(doctor.getId() + " - Dr. " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
        }
        formPanel.add(doctorCombo, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        formPanel.add(dateField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Time (HH:mm):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        timeField = new JTextField("10:00");
        formPanel.add(timeField, gbc);
        
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Reason:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        reasonArea = new JTextArea(3, 20);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        formPanel.add(reasonScroll, gbc);
        
        if (appointment != null) {
            row++;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            formPanel.add(new JLabel("Status:"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            statusCombo = new JComboBox<>(AppointmentStatus.values());
            formPanel.add(statusCombo, gbc);
            
            row++;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            formPanel.add(new JLabel("Notes:"), gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            notesArea = new JTextArea(3, 20);
            notesArea.setLineWrap(true);
            notesArea.setWrapStyleWord(true);
            JScrollPane notesScroll = new JScrollPane(notesArea);
            formPanel.add(notesScroll, gbc);
        }
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(46, 125, 50));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> saveAppointment());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void populateFields() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId() == appointment.getPatient().getId()) {
                patientCombo.setSelectedIndex(i);
                break;
            }
        }
        
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId() == appointment.getDoctor().getId()) {
                doctorCombo.setSelectedIndex(i);
                break;
            }
        }
        
        dateField.setText(appointment.getAppointmentDateTime().format(dateFormatter));
        timeField.setText(appointment.getAppointmentDateTime().format(timeFormatter));
        reasonArea.setText(appointment.getReason());
        
        if (statusCombo != null) {
            statusCombo.setSelectedItem(appointment.getStatus());
        }
        
        if (notesArea != null && appointment.getNotes() != null) {
            notesArea.setText(appointment.getNotes());
        }
    }
    
    private void saveAppointment() {
        if (patientCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a patient.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (doctorCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a doctor.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();
        String reason = reasonArea.getText().trim();
        
        if (dateStr.isEmpty() || timeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Date and time are required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate date;
        LocalTime time;
        
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date or time format.\nUse yyyy-MM-dd for date and HH:mm for time.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        
        Patient selectedPatient = patients.get(patientCombo.getSelectedIndex());
        Doctor selectedDoctor = doctors.get(doctorCombo.getSelectedIndex());
        
        if (appointment == null) {
            Appointment newAppointment = new Appointment(selectedPatient, selectedDoctor, dateTime, reason);
            
            if (appointmentDAO.addAppointment(newAppointment) != null) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Appointment scheduled successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to schedule appointment.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            appointment.setPatient(selectedPatient);
            appointment.setDoctor(selectedDoctor);
            appointment.setAppointmentDateTime(dateTime);
            appointment.setReason(reason);
            
            if (statusCombo != null) {
                appointment.setStatus((AppointmentStatus) statusCombo.getSelectedItem());
            }
            
            if (notesArea != null) {
                appointment.setNotes(notesArea.getText().trim());
            }
            
            if (appointmentDAO.updateAppointment(appointment)) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Appointment updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update appointment.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}
