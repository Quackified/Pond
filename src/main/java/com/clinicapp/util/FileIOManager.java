package com.clinicapp.util;

import com.clinicapp.service.AppointmentManager;
import com.clinicapp.service.DoctorManager;
import com.clinicapp.service.PatientManager;

import java.io.File;
import java.io.IOException;

/**
 * FileIOManager provides convenient methods for saving and loading clinic data from CSV files.
 * Simplifies the process of bulk importing and exporting data.
 */
public class FileIOManager {
    private static final String DEFAULT_EXPORT_DIR = "exports";
    private static final String DEFAULT_IMPORT_DIR = "imports";
    
    /**
     * Initialize default directories if they don't exist.
     */
    public static void initializeDirectories() {
        File exportDir = new File(DEFAULT_EXPORT_DIR);
        File importDir = new File(DEFAULT_IMPORT_DIR);
        
        if (!exportDir.exists()) {
            exportDir.mkdir();
        }
        if (!importDir.exists()) {
            importDir.mkdir();
        }
    }
    
    /**
     * Export all clinic data to CSV files.
     *
     * @param patientManager The PatientManager containing patients
     * @param doctorManager The DoctorManager containing doctors
     * @param appointmentManager The AppointmentManager containing appointments
     * @throws IOException if file writing fails
     */
    public static void exportAllData(PatientManager patientManager, 
                                    DoctorManager doctorManager, 
                                    AppointmentManager appointmentManager) throws IOException {
        initializeDirectories();
        
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String patientsFile = DEFAULT_EXPORT_DIR + "/patients_" + timestamp + ".csv";
        String doctorsFile = DEFAULT_EXPORT_DIR + "/doctors_" + timestamp + ".csv";
        String appointmentsFile = DEFAULT_EXPORT_DIR + "/appointments_" + timestamp + ".csv";
        
        CsvExporter.exportPatients(patientManager, patientsFile);
        CsvExporter.exportDoctors(doctorManager, doctorsFile);
        CsvExporter.exportAppointments(appointmentManager, appointmentsFile);
    }
    
    /**
     * Export specific data to CSV files with custom names.
     *
     * @param patientManager The PatientManager containing patients
     * @param doctorManager The DoctorManager containing doctors
     * @param appointmentManager The AppointmentManager containing appointments
     * @param baseFilename Base name for output files (will have _patients, _doctors, _appointments suffixes)
     * @throws IOException if file writing fails
     */
    public static void exportAllDataCustom(PatientManager patientManager, 
                                          DoctorManager doctorManager, 
                                          AppointmentManager appointmentManager,
                                          String baseFilename) throws IOException {
        initializeDirectories();
        
        String patientsFile = DEFAULT_EXPORT_DIR + "/" + baseFilename + "_patients.csv";
        String doctorsFile = DEFAULT_EXPORT_DIR + "/" + baseFilename + "_doctors.csv";
        String appointmentsFile = DEFAULT_EXPORT_DIR + "/" + baseFilename + "_appointments.csv";
        
        CsvExporter.exportPatients(patientManager, patientsFile);
        CsvExporter.exportDoctors(doctorManager, doctorsFile);
        CsvExporter.exportAppointments(appointmentManager, appointmentsFile);
    }
    
    /**
     * Export only patients to CSV.
     *
     * @param patientManager The PatientManager containing patients
     * @param filename The filename for the export
     * @throws IOException if file writing fails
     */
    public static void exportPatients(PatientManager patientManager, String filename) throws IOException {
        initializeDirectories();
        String filepath = DEFAULT_EXPORT_DIR + "/" + filename;
        CsvExporter.exportPatients(patientManager, filepath);
    }
    
    /**
     * Export only doctors to CSV.
     *
     * @param doctorManager The DoctorManager containing doctors
     * @param filename The filename for the export
     * @throws IOException if file writing fails
     */
    public static void exportDoctors(DoctorManager doctorManager, String filename) throws IOException {
        initializeDirectories();
        String filepath = DEFAULT_EXPORT_DIR + "/" + filename;
        CsvExporter.exportDoctors(doctorManager, filepath);
    }
    
    /**
     * Export only appointments to CSV.
     *
     * @param appointmentManager The AppointmentManager containing appointments
     * @param filename The filename for the export
     * @throws IOException if file writing fails
     */
    public static void exportAppointments(AppointmentManager appointmentManager, String filename) throws IOException {
        initializeDirectories();
        String filepath = DEFAULT_EXPORT_DIR + "/" + filename;
        CsvExporter.exportAppointments(appointmentManager, filepath);
    }
    
    /**
     * Import all clinic data from CSV files.
     *
     * @param patientManager The PatientManager to populate
     * @param doctorManager The DoctorManager to populate
     * @param appointmentManager The AppointmentManager to populate
     * @param baseFilename Base name for input files (will look for _patients, _doctors, _appointments suffixes)
     * @throws IOException if file reading fails
     */
    public static void importAllData(PatientManager patientManager, 
                                    DoctorManager doctorManager, 
                                    AppointmentManager appointmentManager,
                                    String baseFilename) throws IOException {
        initializeDirectories();
        
        String patientsFile = DEFAULT_IMPORT_DIR + "/" + baseFilename + "_patients.csv";
        String doctorsFile = DEFAULT_IMPORT_DIR + "/" + baseFilename + "_doctors.csv";
        String appointmentsFile = DEFAULT_IMPORT_DIR + "/" + baseFilename + "_appointments.csv";
        
        File pf = new File(patientsFile);
        File df = new File(doctorsFile);
        File af = new File(appointmentsFile);
        
        if (pf.exists()) {
            CsvImporter.importPatients(patientManager, patientsFile);
        }
        if (df.exists()) {
            CsvImporter.importDoctors(doctorManager, doctorsFile);
        }
        if (af.exists()) {
            CsvImporter.importAppointments(appointmentManager, patientManager, doctorManager, appointmentsFile);
        }
    }
    
    /**
     * Import only patients from CSV.
     *
     * @param patientManager The PatientManager to populate
     * @param filename The filename to import from
     * @throws IOException if file reading fails
     */
    public static void importPatients(PatientManager patientManager, String filename) throws IOException {
        initializeDirectories();
        String filepath = DEFAULT_IMPORT_DIR + "/" + filename;
        CsvImporter.importPatients(patientManager, filepath);
    }
    
    /**
     * Import only doctors from CSV.
     *
     * @param doctorManager The DoctorManager to populate
     * @param filename The filename to import from
     * @throws IOException if file reading fails
     */
    public static void importDoctors(DoctorManager doctorManager, String filename) throws IOException {
        initializeDirectories();
        String filepath = DEFAULT_IMPORT_DIR + "/" + filename;
        CsvImporter.importDoctors(doctorManager, filepath);
    }
    
    /**
     * Import only appointments from CSV.
     *
     * @param appointmentManager The AppointmentManager to populate
     * @param patientManager The PatientManager containing referenced patients
     * @param doctorManager The DoctorManager containing referenced doctors
     * @param filename The filename to import from
     * @throws IOException if file reading fails
     */
    public static void importAppointments(AppointmentManager appointmentManager, 
                                         PatientManager patientManager, 
                                         DoctorManager doctorManager, 
                                         String filename) throws IOException {
        initializeDirectories();
        String filepath = DEFAULT_IMPORT_DIR + "/" + filename;
        CsvImporter.importAppointments(appointmentManager, patientManager, doctorManager, filepath);
    }
    
    /**
     * Get the default export directory.
     *
     * @return The export directory path
     */
    public static String getExportDir() {
        return DEFAULT_EXPORT_DIR;
    }
    
    /**
     * Get the default import directory.
     *
     * @return The import directory path
     */
    public static String getImportDir() {
        return DEFAULT_IMPORT_DIR;
    }
}
