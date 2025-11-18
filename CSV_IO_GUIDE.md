# CSV File I/O Guide

## Overview

The Clinic Appointment Management System includes comprehensive CSV (Comma-Separated Values) file I/O functionality for bulk importing and exporting clinic data. This allows administrators to:

- **Export** all patient, doctor, and appointment data to CSV files
- **Import** data from CSV files into the system
- **Backup** data in standard CSV format
- **Share** data with external systems
- **Migrate** data between systems

## Architecture

### Components

1. **CsvExporter.java** - Exports data to CSV format
   - `exportPatients(PatientManager, filepath)` - Export all patients
   - `exportDoctors(DoctorManager, filepath)` - Export all doctors
   - `exportAppointments(AppointmentManager, filepath)` - Export all appointments

2. **CsvImporter.java** - Imports data from CSV format
   - `importPatients(PatientManager, filepath)` - Import patients
   - `importDoctors(DoctorManager, filepath)` - Import doctors
   - `importAppointments(AppointmentManager, PatientManager, DoctorManager, filepath)` - Import appointments

3. **FileIOManager.java** - High-level convenience API
   - Manages default directories (exports/, imports/)
   - Provides batch operations
   - Supports both timestamped and custom filenames

## CSV File Formats

### Patients CSV Format

```
ID,Name,DateOfBirth,Gender,PhoneNumber,Email,Address,BloodType,Allergies
1,"John Doe",1990-05-15,"Male","5551234567","john@example.com","123 Main St","O+","Penicillin"
2,"Jane Smith",1985-03-22,"Female","5559876543","jane@example.com","456 Oak Ave","A-","None"
```

**Fields:**
- **ID**: Patient identifier (auto-incremented)
- **Name**: Patient's full name
- **DateOfBirth**: Date in yyyy-MM-dd format
- **Gender**: Male, Female, or Other
- **PhoneNumber**: Contact number (10-15 digits)
- **Email**: Email address (optional)
- **Address**: Residential address (optional)
- **BloodType**: Blood type (A+, A-, B+, B-, AB+, AB-, O+, O-, optional)
- **Allergies**: Known allergies (optional)

### Doctors CSV Format

```
ID,Name,Specialization,PhoneNumber,Email,AvailableDays,StartTime,EndTime,IsAvailable
1,"Sarah Williams","Cardiology","5551111111","sarah@clinic.com","Monday;Tuesday;Wednesday;Thursday;Friday",09:00,17:00,true
2,"Michael Chen","Pediatrics","5552222222","michael@clinic.com","Monday;Wednesday;Friday",08:00,16:00,false
```

**Fields:**
- **ID**: Doctor identifier (auto-incremented)
- **Name**: Doctor's name
- **Specialization**: Medical specialization
- **PhoneNumber**: Contact number
- **Email**: Email address (optional)
- **AvailableDays**: Days separated by semicolons (Monday;Tuesday;etc.)
- **StartTime**: Work start time in HH:mm format
- **EndTime**: Work end time in HH:mm format
- **IsAvailable**: true or false

### Appointments CSV Format

```
ID,PatientID,DoctorID,Date,StartTime,EndTime,Reason,Status,Notes,CreatedAt
1,5,5,2025-12-01,10:00,10:30,"Regular checkup","SCHEDULED","",2025-11-18T13:15:25.092608131
2,5,5,2025-12-02,14:00,14:30,"Follow-up","COMPLETED","Patient in good condition",2025-11-18T13:15:25.094241446
```

**Fields:**
- **ID**: Appointment identifier (auto-incremented)
- **PatientID**: Reference to patient ID
- **DoctorID**: Reference to doctor ID
- **Date**: Appointment date in yyyy-MM-dd format
- **StartTime**: Appointment start time in HH:mm format
- **EndTime**: Appointment end time in HH:mm format
- **Reason**: Reason for appointment
- **Status**: SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
- **Notes**: Additional notes (optional)
- **CreatedAt**: ISO 8601 timestamp

## Usage Examples

### Using FileIOManager (Recommended)

#### Export All Data (Timestamped)

```java
PatientManager patientManager = new PatientManager();
DoctorManager doctorManager = new DoctorManager();
AppointmentManager appointmentManager = new AppointmentManager(patientManager, doctorManager);

// ... populate with data ...

// Export with automatic timestamping
FileIOManager.exportAllData(patientManager, doctorManager, appointmentManager);
// Creates: exports/patients_1700310925.csv, exports/doctors_1700310925.csv, etc.
```

#### Export All Data (Custom Names)

```java
// Export with custom base filename
FileIOManager.exportAllDataCustom(patientManager, doctorManager, appointmentManager, "backup_2025_01");
// Creates: exports/backup_2025_01_patients.csv, exports/backup_2025_01_doctors.csv, etc.
```

#### Export Individual Data Types

```java
// Export only patients
FileIOManager.exportPatients(patientManager, "patients.csv");

// Export only doctors
FileIOManager.exportDoctors(doctorManager, "doctors.csv");

// Export only appointments
FileIOManager.exportAppointments(appointmentManager, "appointments.csv");
```

#### Import All Data

```java
PatientManager patientManager = new PatientManager();
DoctorManager doctorManager = new DoctorManager();
AppointmentManager appointmentManager = new AppointmentManager(patientManager, doctorManager);

// Import from files in imports/ directory
FileIOManager.importAllData(patientManager, doctorManager, appointmentManager, "backup_2025_01");
// Looks for: imports/backup_2025_01_patients.csv, imports/backup_2025_01_doctors.csv, etc.
```

#### Import Individual Data Types

```java
// Import only patients
FileIOManager.importPatients(patientManager, "patients.csv");
// Looks in: imports/patients.csv

// Import only doctors
FileIOManager.importDoctors(doctorManager, "doctors.csv");
// Looks in: imports/doctors.csv

// Import only appointments
FileIOManager.importAppointments(appointmentManager, patientManager, doctorManager, "appointments.csv");
// Looks in: imports/appointments.csv
```

### Using CsvExporter Directly

```java
import com.clinicapp.util.CsvExporter;

// Export patients to specific path
CsvExporter.exportPatients(patientManager, "/path/to/patients.csv");

// Export doctors to specific path
CsvExporter.exportDoctors(doctorManager, "/path/to/doctors.csv");

// Export appointments to specific path
CsvExporter.exportAppointments(appointmentManager, "/path/to/appointments.csv");
```

### Using CsvImporter Directly

```java
import com.clinicapp.util.CsvImporter;

// Import patients from specific path
CsvImporter.importPatients(patientManager, "/path/to/patients.csv");

// Import doctors from specific path
CsvImporter.importDoctors(doctorManager, "/path/to/doctors.csv");

// Import appointments from specific path
CsvImporter.importAppointments(appointmentManager, patientManager, doctorManager, "/path/to/appointments.csv");
```

## CSV Format Details

### CSV Quoting and Escaping

The CSV exporter follows standard CSV conventions:

1. **All fields are quoted** for consistency
2. **Internal quotes are doubled** (e.g., `"Dr. ""Doc"" Smith"` becomes `"Dr. ""Doc"" Smith"`)
3. **Newlines within fields** are preserved
4. **Commas within fields** are handled by quoting

Example with special characters:
```
1,"John ""Jack"" Smith",1990-05-15,"Male",...
```

### Semicolon-Separated Lists

Some fields contain multiple values separated by semicolons:

- **Doctor AvailableDays**: `Monday;Tuesday;Wednesday;Thursday;Friday`
- These are parsed correctly during import

### Date and Time Formats

- **Dates**: `yyyy-MM-dd` format (e.g., `2025-12-01`)
- **Times**: `HH:mm` format (e.g., `14:30`)
- **DateTime**: ISO 8601 format with timezone (e.g., `2025-11-18T13:15:25.092608131`)

## Important Notes

### ID Preservation During Import

When importing data, the system uses reflection to preserve the original IDs from the CSV file. This ensures:

- Referential integrity between appointments and patient/doctor IDs
- Consistency with external systems
- No ID conflicts with existing data

### Directory Structure

The FileIOManager uses standard directories:

```
project/
├── exports/    # Default export directory
├── imports/    # Default import directory
└── ...
```

These directories are created automatically by `FileIOManager.initializeDirectories()`.

### Import Order

When importing all data:
1. Import patients first
2. Import doctors second
3. Import appointments last

This ensures that all foreign key references are satisfied. The system validates this automatically.

### Optional Fields

Some fields in CSV files can be empty:

- Patient: Email, Address, BloodType, Allergies
- Doctor: Email
- Appointment: Notes

Empty values are properly handled during import (converted to empty strings or null as appropriate).

## Error Handling

The system includes robust error handling:

### Export Errors
- File I/O errors are thrown as `IOException`
- Invalid file paths are caught and reported

### Import Errors
- Missing referenced patient/doctor IDs are logged with warnings
- Invalid date/time formats skip that record with an error message
- Line parsing errors are caught and reported with the problematic line

Example error output:
```
Error importing patient from line: 1,"John Doe",invalid-date,"Male",...
Error: Patient or Doctor not found for appointment ID 5
```

## Testing

The system includes a comprehensive test class: `CsvIOTest.java`

Run tests with:
```bash
java -cp bin com.clinicapp.util.CsvIOTest
```

Test coverage includes:
- Patient CSV export and import
- Doctor CSV export and import
- Appointment CSV export and import
- Verification of data integrity
- ID preservation
- Special character handling

Sample output:
```
=== CSV Import/Export Test ===
Testing Patient CSV Export/Import...
  Original patients: 2
  Exported to: csv_test/patients.csv
  Imported patients: 2
  ✓ Patient CSV I/O test passed
[...]
=== All tests passed! ===
```

## Best Practices

1. **Always backup before importing** - Import operations add data; they don't replace existing data
2. **Use timestamped exports** - FileIOManager.exportAllData() automatically adds timestamps
3. **Verify file paths** - Use absolute paths when possible
4. **Import in order** - Patients → Doctors → Appointments
5. **Test with small datasets first** - Before migrating large amounts of data
6. **Check for ID conflicts** - Ensure no duplicate IDs between source and destination
7. **Validate exported files** - Review CSV files before sharing or archiving

## Integration with GUI

To add CSV I/O functionality to the Swing GUI:

```java
// In your panel or dialog
import com.clinicapp.util.FileIOManager;

// Export handler
try {
    FileIOManager.exportAllDataCustom(patientManager, doctorManager, 
                                     appointmentManager, "backup_" + LocalDate.now());
    JOptionPane.showMessageDialog(this, "Data exported successfully!", "Success", 
                                 JOptionPane.INFORMATION_MESSAGE);
} catch (IOException e) {
    JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage(), "Error", 
                                 JOptionPane.ERROR_MESSAGE);
}

// Import handler
try {
    FileIOManager.importAllData(patientManager, doctorManager, 
                               appointmentManager, "backup_2025_01");
    JOptionPane.showMessageDialog(this, "Data imported successfully!", "Success", 
                                 JOptionPane.INFORMATION_MESSAGE);
} catch (IOException e) {
    JOptionPane.showMessageDialog(this, "Import failed: " + e.getMessage(), "Error", 
                                 JOptionPane.ERROR_MESSAGE);
}
```

## Limitations and Future Enhancements

### Current Limitations
- No encryption of sensitive data (PHI)
- No transaction support (partial imports could leave system in inconsistent state)
- No data validation against business rules

### Potential Enhancements
- Encrypted CSV export for HIPAA compliance
- Transaction-based import with rollback
- Data validation during import
- Differential export (only changes since last export)
- Multiple format support (JSON, Excel, etc.)
- Scheduled backups

## Summary

The CSV File I/O system provides:

✓ **Flexible export** - Export individual or all data types  
✓ **Reliable import** - With ID preservation and validation  
✓ **Standard formats** - Industry-standard CSV with proper quoting  
✓ **Error handling** - Comprehensive error reporting  
✓ **Easy integration** - Simple API through FileIOManager  
✓ **Well-tested** - Automated test suite included  

CSV I/O enables data portability and integration with other systems while maintaining data integrity.
