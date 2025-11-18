# OpenCSV Integration Guide

## Overview
This document describes the integration of OpenCSV library for CSV file import/export functionality in the Clinic Appointment Management System.

## OpenCSV Library
- **Library**: OpenCSV 5.9
- **Website**: https://opencsv.sourceforge.net/
- **License**: Apache License 2.0
- **Dependencies**: 
  - commons-lang3-3.13.0.jar
  - commons-text-1.10.0.jar
  - commons-beanutils-1.9.4.jar
  - commons-logging-1.2.jar
  - commons-collections-3.2.2.jar

## Installation
All required JAR files are located in the `lib/` directory:
```
lib/
├── opencsv-5.9.jar
├── commons-lang3-3.13.0.jar
├── commons-text-1.10.0.jar
├── commons-beanutils-1.9.4.jar
├── commons-logging-1.2.jar
└── commons-collections-3.2.2.jar
```

## Components

### 1. CsvExporter Class
**Location**: `src/main/java/com/clinicapp/io/CsvExporter.java`

Provides methods for exporting data to CSV files:

#### Methods:
- `exportPatients(List<Patient> patients)` - Exports all patients to CSV
- `exportDoctors(List<Doctor> doctors)` - Exports all doctors to CSV
- `exportAppointments(List<Appointment> appointments)` - Exports all appointments to CSV
- `exportAppointmentsByDate(List<Appointment> appointments, LocalDate date)` - Exports appointments for a specific date

#### Export Format:

**Patients CSV:**
```csv
ID,Name,Date of Birth,Age,Gender,Phone Number,Email,Address,Blood Type,Allergies
1,John Doe,1990-05-15,34,Male,1234567890,john@email.com,123 Main St,A+,None
```

**Doctors CSV:**
```csv
ID,Name,Specialization,Phone Number,Email,Available Days,Start Time,End Time,Available
1,Dr. Smith,Cardiology,9876543210,smith@clinic.com,Monday;Wednesday;Friday,09:00,17:00,true
```

**Appointments CSV:**
```csv
ID,Date,Start Time,End Time,Patient ID,Patient Name,Doctor ID,Doctor Name,Reason,Status,Notes,Created At
1,2024-11-20,09:00,09:30,1,John Doe,1,Dr. Smith,Checkup,SCHEDULED,,2024-11-18 10:30:00
```

### 2. CsvImporter Class
**Location**: `src/main/java/com/clinicapp/io/CsvImporter.java`

Provides methods for importing data from CSV files:

#### Methods:
- `importPatients(String filePath, PatientManager patientManager)` - Imports patients from CSV
- `importDoctors(String filePath, DoctorManager doctorManager)` - Imports doctors from CSV
- `importAppointments(String filePath, AppointmentManager, PatientManager, DoctorManager)` - Imports appointments from CSV

#### ImportResult Class:
Each import method returns an `ImportResult` object containing:
- `successCount` - Number of successfully imported records
- `errorCount` - Number of failed records
- `errors` - List of error messages

### 3. GUI Integration

All three panels now have CSV import/export functionality:

#### PatientPanel:
- **Export to CSV** button - Exports all patients
- **Import from CSV** button - Imports patients from selected file

#### DoctorPanel:
- **Export to CSV** button - Exports all doctors
- **Import from CSV** button - Imports doctors from selected file

#### AppointmentPanel:
- **Export to CSV** button - Exports all appointments
- **Import from CSV** button - Imports appointments from selected file

## Usage

### Exporting Data

1. Open the relevant panel (Patients, Doctors, or Appointments)
2. Click the "Export to CSV" button
3. The file will be saved in the `exports/` directory
4. A success message will display the file path

Example export file name: `patients_20241118_1700123456789.csv`

### Importing Data

1. Prepare a CSV file with the correct format (see Export Format above)
2. Open the relevant panel (Patients, Doctors, or Appointments)
3. Click the "Import from CSV" button
4. Select the CSV file using the file chooser dialog
5. Review the import results showing success and error counts
6. The table will automatically refresh to show imported data

### Import Validation

The importer performs validation on each record:
- **Patients**: Validates date format, phone number format, email format (if provided), blood type (if provided)
- **Doctors**: Validates required fields and formats
- **Appointments**: Validates date/time formats, checks if referenced patient and doctor exist, detects scheduling conflicts

## File Locations

### Export Directory
- **Path**: `exports/`
- **Description**: All exported CSV files are saved here
- **Naming**: `{type}_{date}_{timestamp}.csv`
- **Git**: This directory is ignored (see `.gitignore`)

## Build Configuration

### compile.sh
Added classpath for libraries:
```bash
CLASSPATH="lib/*"
javac -cp "$CLASSPATH" -d bin ...
```

### run.sh
Added classpath for runtime:
```bash
java -cp "bin:lib/*" com.clinicapp.ClinicManagementGUI
```

## Error Handling

### Export Errors
- IOException: File write errors are caught and displayed in error dialog
- Empty data: Warning dialog if no data to export

### Import Errors
- File not found: Caught during file selection
- Invalid CSV format: Each invalid row is logged in error list
- Missing required fields: Validation errors are collected
- Duplicate data: Depends on business logic (currently allows duplicates)
- Referenced entity not found: For appointments, validates patient/doctor existence

## Features

### CSV Format
- **Delimiter**: Comma (`,`)
- **Quote Character**: Double quote (`"`)
- **Header Row**: Always included
- **Encoding**: UTF-8 (default)
- **Line Separator**: System default

### Data Types
- **Dates**: yyyy-MM-dd format
- **Times**: HH:mm format (24-hour)
- **Timestamps**: yyyy-MM-dd HH:mm:ss format
- **Lists**: Semicolon-separated (e.g., "Monday;Wednesday;Friday")
- **Booleans**: true/false

### Special Characters
OpenCSV automatically handles:
- Commas in data (quoted)
- Quotes in data (escaped)
- Line breaks in data (quoted)

## Best Practices

1. **Always export before major changes** - Use export as a backup mechanism
2. **Validate CSV format** - Ensure CSV files match the expected format before importing
3. **Check import results** - Review the success/error counts after import
4. **Test with small datasets first** - Validate CSV format with a few records before bulk import
5. **Keep exports organized** - Clean up old exports periodically from the exports/ directory

## Limitations

1. **No automatic backup** - Exports are manual; no automatic backup on data changes
2. **No conflict resolution** - Import may create duplicates if data already exists
3. **No undo for imports** - Once imported, data cannot be automatically rolled back
4. **Large file handling** - Very large CSV files may cause memory issues
5. **Character encoding** - Uses system default; international characters may need UTF-8

## Future Enhancements

1. **Scheduled exports** - Automatic daily/weekly exports
2. **Export filters** - Export filtered/selected data only
3. **Import preview** - Preview data before importing
4. **Conflict resolution** - Handle duplicate detection during import
5. **Progress indicator** - Show progress for large imports/exports
6. **Custom export formats** - Allow user to select which columns to export
7. **Data validation rules** - More comprehensive validation during import
8. **Bulk operations** - Support for multiple file import/export

## Technical Details

### OpenCSV API Usage

**Writing CSV:**
```java
try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
    writer.writeNext(headerArray);
    for (Object obj : dataList) {
        writer.writeNext(dataArray);
    }
}
```

**Reading CSV:**
```java
try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
    List<String[]> records = reader.readAll();
    for (String[] record : records) {
        // Process record
    }
}
```

### Date/Time Handling
Uses Java 8 Time API:
- `LocalDate` for dates
- `LocalTime` for times
- `LocalDateTime` for timestamps
- `DateTimeFormatter` for formatting/parsing

## Troubleshooting

### Common Issues:

1. **ClassNotFoundException: com.opencsv.CSVWriter**
   - Ensure lib/ directory contains all JAR files
   - Check classpath in compile.sh and run.sh

2. **Import fails silently**
   - Check import result error list
   - Verify CSV format matches expected headers
   - Ensure required fields are present

3. **Exported file not found**
   - Verify exports/ directory exists
   - Check file permissions
   - Review file path in success message

4. **Character encoding issues**
   - Ensure CSV file is UTF-8 encoded
   - Check for BOM (Byte Order Mark) in file

5. **Date/time parsing errors**
   - Verify date format is yyyy-MM-dd
   - Verify time format is HH:mm (24-hour)
   - Check for extra spaces or invalid characters

## Summary

The OpenCSV integration provides a robust, user-friendly way to import and export clinic data. The implementation follows best practices with proper error handling, validation, and user feedback. The CSV format is standardized and compatible with spreadsheet applications like Microsoft Excel, Google Sheets, and LibreOffice Calc.
