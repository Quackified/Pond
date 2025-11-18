# Clinic Appointment Management System

A comprehensive Java Swing GUI application for managing clinic operations including patient records, doctor schedules, and appointments. Features JSON data persistence using Gson and CSV export functionality.

## 📋 Table of Contents

- [Features](#features)
- [System Architecture](#system-architecture)
- [Getting Started](#getting-started)
- [Technology Stack](#technology-stack)
- [Data Persistence](#data-persistence)
- [Code Organization](#code-organization)

## ✨ Features

### Patient Management
- **Register New Patient**: Add patients with complete demographic and medical information
- **View All Patients**: Display all registered patients in a table
- **View Patient Details**: See complete patient information
- **Update Patient Information**: Modify patient details
- **Delete Patient**: Remove patients with confirmation prompts

### Doctor Management
- **Add New Doctor**: Register doctors with specialization and availability schedules
  - Working hours using LocalTime (HH:mm format)
  - Available days selection
- **View All Doctors**: Display all doctors with their current status
- **View Doctor Details**: See complete doctor information
- **Update Doctor Information**: Modify doctor details and availability
- **Delete Doctor**: Remove doctors with confirmation

### Appointment Management
- **Schedule New Appointment**: Create appointments with automatic conflict detection
- **View All Appointments**: Display all appointments in a table
- **View Appointment Details**: Display complete appointment information
- **Update Appointment Status**: Change status (SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW)
- **Cancel Appointment**: Cancel appointments with undo support
- **Add Notes**: Add notes to completed appointments

### Data Management
- **Save Data**: Save all data to JSON files using Gson
- **Auto-save on Exit**: Prompts to save before closing
- **Export to CSV**: Export patients, doctors, or appointments to CSV files

## 🏗 System Architecture

### Package Structure
```
src/main/java/com/clinicapp/
├── model/              # Data models
│   ├── Patient.java    # Uses LocalDate for dateOfBirth
│   ├── Doctor.java     # Uses LocalTime for working hours
│   └── Appointment.java # Uses LocalDateTime
├── service/            # Business logic managers
│   ├── PatientManager.java
│   ├── DoctorManager.java
│   └── AppointmentManager.java
├── storage/            # Data persistence
│   ├── JsonStorage.java          # Gson-based JSON I/O
│   ├── LocalDateAdapter.java     # Gson type adapter
│   ├── LocalDateTimeAdapter.java # Gson type adapter
│   ├── LocalTimeAdapter.java     # Gson type adapter
│   └── CsvExporter.java          # CSV export
├── gui/                # Swing GUI components
│   ├── ClinicManagementGUI.java  # Main window
│   ├── PatientPanel.java         # Patient management
│   ├── DoctorPanel.java          # Doctor management
│   └── AppointmentPanel.java     # Appointment scheduling
├── util/               # Utilities
│   └── InputValidator.java       # Pure validation methods
└── ClinicAppointmentSystem.java  # Main entry point

lib/
└── gson-2.10.1.jar    # Gson library
```

### Design Patterns

#### Manager Pattern
- Separate managers for Patient, Doctor, and Appointment operations
- HashMap for O(1) lookup by ID
- Stack for undo functionality
- Queue for appointment processing

#### MVC Pattern
- **Model**: Patient, Doctor, Appointment classes
- **View**: Swing GUI panels (PatientPanel, DoctorPanel, AppointmentPanel)
- **Controller**: Service layer managers

#### Adapter Pattern
- Custom Gson type adapters for LocalDate, LocalDateTime, LocalTime

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Bash shell (for running scripts)

### Compilation

#### Using Shell Script (Recommended)
```bash
./compile.sh
```

#### Manual Compilation
```bash
mkdir -p bin
javac -cp "lib/gson-2.10.1.jar" -d bin \
    src/main/java/com/clinicapp/model/*.java \
    src/main/java/com/clinicapp/service/*.java \
    src/main/java/com/clinicapp/util/*.java \
    src/main/java/com/clinicapp/storage/*.java \
    src/main/java/com/clinicapp/gui/*.java \
    src/main/java/com/clinicapp/*.java
```

### Running the Application

#### Using Shell Script (Recommended)
```bash
./run.sh
```

#### Manual Execution
```bash
java -cp "bin:lib/gson-2.10.1.jar" com.clinicapp.gui.ClinicManagementGUI
```

## 💻 Technology Stack

- **Java Swing**: GUI framework
- **Gson 2.10.1**: JSON serialization/deserialization
- **Java Time API**: LocalDate, LocalDateTime, LocalTime for date/time handling
- **Collections Framework**: HashMap, Stack, Queue for data structures

## 💾 Data Persistence

### JSON Storage
Data is stored in JSON format using Gson:
- `data/patients.json`: Patient records
- `data/doctors.json`: Doctor records
- `data/appointments.json`: Appointment records

### CSV Export
Export functionality available for:
- Patients → `exports/patients.csv`
- Doctors → `exports/doctors.csv`
- Appointments → `exports/appointments.csv`

## 📁 Code Organization

### Models
- **Patient**: Contains patient demographics, medical info
- **Doctor**: Contains specialization, working hours (LocalTime), available days
- **Appointment**: Links patients and doctors with LocalDateTime

### Utilities
- **InputValidator**: Pure validation methods (no I/O)
  - Email validation
  - Phone number validation
  - Date/time parsing and validation
  - Blood type validation

### Storage
- **JsonStorage**: Handles JSON file I/O with Gson
- **CsvExporter**: Exports data to CSV format

### GUI
- **Tabbed Interface**: Separate tabs for Patients, Doctors, Appointments
- **JTable**: Display data in sortable tables
- **JDialog**: Modal dialogs for add/edit operations
- **Menu Bar**: File operations (Save, Exit) and Export options

## 📊 Data Structures

- **HashMap**: O(1) lookup for patients, doctors, appointments by ID
- **Stack**: LIFO structure for undo functionality
- **Queue**: FIFO structure for appointment processing
- **ArrayList**: Dynamic lists for managing collections

## 🔒 Validation

Input validation is handled by `InputValidator` utility:
- Email format validation
- Phone number validation (10-15 digits)
- Date format (yyyy-MM-dd)
- Time format (HH:mm)
- Blood type validation (A+, A-, B+, B-, AB+, AB-, O+, O-)
- Gender validation (Male, Female, Other)

## 🎨 GUI Features

### Main Window
- Tabbed interface with 3 tabs (Patients, Doctors, Appointments)
- Menu bar with File and Export options
- Status bar

### Patient Panel
- Table view of all patients
- Add/Edit/Delete/View buttons
- Form dialogs for data entry

### Doctor Panel
- Table view of all doctors
- Add/Edit/Delete/View buttons
- Working hours input with LocalTime
- Available days selection

### Appointment Panel
- Table view of all appointments
- Schedule appointment dialog
- Status update functionality
- Patient and doctor selection dropdowns

## 📝 Notes

- All date/time fields use Java Time API (LocalDate, LocalDateTime, LocalTime)
- Doctor working hours use LocalTime (not String)
- Data is saved to JSON files in the `data/` directory
- CSV exports are saved to the `exports/` directory
- The application uses Gson for JSON serialization with custom type adapters

## 🔧 Build Scripts

### compile.sh
Compiles all Java source files with Gson library on classpath.

### run.sh
Runs the Swing GUI application. Automatically compiles if needed.

## 🎯 Key Improvements from Console Version

1. **GUI Interface**: Modern Swing GUI replaces console UI
2. **Gson Integration**: JSON persistence with Gson library
3. **LocalTime Usage**: Doctor working hours use LocalTime instead of String
4. **CSV Export**: Export data to CSV files
5. **Pure Validation**: InputValidator refactored to pure validation (no console I/O)
6. **Better UX**: Dialog boxes, tables, and forms for better user experience
