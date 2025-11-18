# Clinic Appointment Management System

A simple Java Swing-based application for managing clinic appointments, patients, and doctors with JSON file storage and CSV export capabilities.

## Features

- **Patient Management**: Add, edit, delete, and view patient records
- **Doctor Management**: Manage doctor information, specializations, and availability
- **Appointment Scheduling**: Schedule and manage appointments with conflict detection
- **JSON File Storage**: Persistent data storage using JSON format
- **CSV Export**: Export patients, doctors, and appointments to CSV files
- **User-Friendly GUI**: Clean and intuitive Java Swing interface

## Requirements

- Java Development Kit (JDK) 8 or higher
- No external libraries required (uses standard Java libraries only)

## Installation

1. Clone or download the repository
2. Navigate to the project directory

## Compilation

To compile the application:

```bash
./compile.sh
```

Or manually:

```bash
javac -d bin src/main/java/com/clinicapp/**/*.java
```

## Running the Application

To run the application:

```bash
./run.sh
```

Or manually:

```bash
java -cp bin com.clinicapp.ClinicManagementGUI
```

## Usage

### Main Window

The application opens with three tabs:

1. **Patients Tab**: Manage patient records
   - Add new patients with personal and medical information
   - Edit existing patient records
   - Delete patients
   - View all patients in a table

2. **Doctors Tab**: Manage doctor information
   - Add new doctors with specializations and availability
   - Edit doctor details
   - Delete doctors
   - View all doctors in a table

3. **Appointments Tab**: Schedule and manage appointments
   - Create new appointments by selecting patient and doctor
   - Edit appointment details
   - Change appointment status (SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW)
   - Delete appointments
   - View all appointments in a table

### Menu Bar

- **File Menu**:
  - Save: Manually save all data to JSON files
  - Load: Reload data from JSON files
  - Exit: Save and exit the application

- **Export Menu**:
  - Export Patients to CSV
  - Export Doctors to CSV
  - Export Appointments to CSV

- **Help Menu**:
  - About: View application information

## Data Storage

All data is automatically saved when you close the application. Data is stored in the `data/` directory:

- `data/patients.json`: Patient records
- `data/doctors.json`: Doctor records
- `data/appointments.json`: Appointment records

## CSV Exports

CSV exports are saved in the `exports/` directory with the filename you specify.

## Input Validation

The application validates:

- Email addresses (standard email format)
- Phone numbers (10-15 digits)
- Dates (yyyy-MM-dd format)
- Times (HH:mm format)
- Blood types (A+, A-, B+, B-, AB+, AB-, O+, O-)
- Gender (Male, Female, Other)

## Appointment Conflict Detection

The system prevents scheduling conflicts by checking if a doctor has another appointment within 30 minutes of the requested time.

## Project Structure

```
src/main/java/com/clinicapp/
├── model/                      # Data models
│   ├── Patient.java
│   ├── Doctor.java
│   └── Appointment.java
├── service/                    # Business logic
│   ├── PatientManager.java
│   ├── DoctorManager.java
│   └── AppointmentManager.java
├── storage/                    # Data persistence
│   ├── JsonStorage.java
│   └── CsvExporter.java
├── gui/                        # User interface
│   ├── MainWindow.java
│   ├── PatientPanel.java
│   ├── DoctorPanel.java
│   └── AppointmentPanel.java
├── util/                       # Utilities
│   └── InputValidator.java
└── ClinicManagementGUI.java   # Main entry point
```

## License

This is a beginner-friendly educational project.

## Version History

- **Version 2.0**: Java Swing GUI with JSON storage and CSV export
- **Version 1.0**: Console-based application
