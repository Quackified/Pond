package com.clinicapp.util;

import com.clinicapp.model.Appointment;
import com.clinicapp.model.Doctor;
import com.clinicapp.model.Patient;
import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CsvExporter handles exporting clinic data (patients, doctors, appointments) to CSV format.
 * Uses standard CSV format with proper quoting and escaping.
 */
public class CsvExporter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Export all patients to a CSV file.
     *
     * @param patientManager The PatientManager containing patients
     * @param filepath Path where CSV file will be written
     * @throws IOException if file writing fails
     */
    public static void exportPatients(PatientManager patientManager, String filepath) throws IOException {
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("ID,Name,DateOfBirth,Gender,PhoneNumber,Email,Address,BloodType,Allergies\n");
            
            for (Patient patient : patientManager.getAllPatients()) {
                writer.write(String.format("%d,\"%s\",%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    patient.getId(),
                    escapeCsvValue(patient.getName()),
                    patient.getDateOfBirth().format(DATE_FORMATTER),
                    escapeCsvValue(patient.getGender()),
                    escapeCsvValue(patient.getPhoneNumber()),
                    escapeCsvValue(patient.getEmail() != null ? patient.getEmail() : ""),
                    escapeCsvValue(patient.getAddress() != null ? patient.getAddress() : ""),
                    escapeCsvValue(patient.getBloodType() != null ? patient.getBloodType() : ""),
                    escapeCsvValue(patient.getAllergies() != null ? patient.getAllergies() : "")
                ));
            }
        }
    }
    
    /**
     * Export all doctors to a CSV file.
     *
     * @param doctorManager The DoctorManager containing doctors
     * @param filepath Path where CSV file will be written
     * @throws IOException if file writing fails
     */
    public static void exportDoctors(DoctorManager doctorManager, String filepath) throws IOException {
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("ID,Name,Specialization,PhoneNumber,Email,AvailableDays,StartTime,EndTime,IsAvailable\n");
            
            for (Doctor doctor : doctorManager.getAllDoctors()) {
                String availableDays = String.join(";", doctor.getAvailableDays());
                writer.write(String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,%s\n",
                    doctor.getId(),
                    escapeCsvValue(doctor.getName()),
                    escapeCsvValue(doctor.getSpecialization()),
                    escapeCsvValue(doctor.getPhoneNumber()),
                    escapeCsvValue(doctor.getEmail() != null ? doctor.getEmail() : ""),
                    escapeCsvValue(availableDays),
                    doctor.getStartTime(),
                    doctor.getEndTime(),
                    doctor.isAvailable() ? "true" : "false"
                ));
            }
        }
    }
    
    /**
     * Export all appointments to a CSV file.
     *
     * @param appointmentManager The AppointmentManager containing appointments
     * @param filepath Path where CSV file will be written
     * @throws IOException if file writing fails
     */
    public static void exportAppointments(AppointmentManager appointmentManager, String filepath) throws IOException {
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write("ID,PatientID,DoctorID,Date,StartTime,EndTime,Reason,Status,Notes,CreatedAt\n");
            
            for (Appointment appointment : appointmentManager.getAllAppointments()) {
                writer.write(String.format("%d,%d,%d,%s,%s,%s,\"%s\",\"%s\",\"%s\",%s\n",
                    appointment.getId(),
                    appointment.getPatient().getId(),
                    appointment.getDoctor().getId(),
                    appointment.getAppointmentDate().format(DATE_FORMATTER),
                    appointment.getStartTime().format(TIME_FORMATTER),
                    appointment.getEndTime().format(TIME_FORMATTER),
                    escapeCsvValue(appointment.getReason()),
                    escapeCsvValue(appointment.getStatus().toString()),
                    escapeCsvValue(appointment.getNotes() != null ? appointment.getNotes() : ""),
                    appointment.getCreatedAt()
                ));
            }
        }
    }
    
    /**
     * Escape CSV values by doubling internal quotes and wrapping if necessary.
     *
     * @param value The value to escape
     * @return Escaped value safe for CSV
     */
    private static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }
}
