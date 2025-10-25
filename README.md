# Clinic Appointment Management System

A comprehensive console-based Java application for managing clinic operations including patient records, doctor schedules, and appointments.

## 📋 Table of Contents

- [Features](#features)
- [System Architecture](#system-architecture)
- [Getting Started](#getting-started)
- [Menu Options](#menu-options)
- [Sample Execution Steps](#sample-execution-steps)
- [Data Structures](#data-structures)
- [Code Organization](#code-organization)

## ✨ Features

### Patient Management (Options 1-6)
- **Register New Patient**: Add patients with complete demographic and medical information
- **View All Patients**: Display all registered patients in a formatted table
- **Search Patient by Name**: Find patients using partial name matching (case-insensitive)
- **View Patient Details**: See complete patient information including appointment history
- **Update Patient Information**: Modify patient details with selective field updates
- **Delete Patient**: Remove patients with confirmation prompts and warnings

### Doctor Management (Options 7-14)
- **Add New Doctor**: Register doctors with specialization and availability schedules
- **View All Doctors**: Display all doctors with their current status
- **Search Doctor by Name**: Find doctors using name search
- **Search Doctor by Specialization**: Filter doctors by medical specialization
- **View Doctor Details**: See complete doctor information and scheduled appointments
- **Update Doctor Information**: Modify doctor details
- **Set Doctor Availability**: Manage doctor availability status (Available/Unavailable)
- **Delete Doctor**: Remove doctors with safeguards for existing appointments

### Appointment Management (Options 15-25)
- **Schedule New Appointment**: Create appointments with automatic conflict detection
- **View All Appointments**: Display all appointments in the system
- **View Appointments by Patient**: Filter appointments for a specific patient
- **View Appointments by Doctor**: Filter appointments for a specific doctor
- **View Appointments by Date**: See all appointments scheduled for a specific date
- **View Appointment Details**: Display complete appointment information
- **Update Appointment**: Modify appointment time, reason, or notes
- **Confirm Appointment**: Change appointment status from SCHEDULED to CONFIRMED
- **Cancel Appointment**: Cancel appointments with undo support
- **Complete Appointment**: Mark appointments as completed with optional notes
- **Mark Appointment as No-Show**: Record when patients don't show up

### Queue & Reporting (Options 26-28)
- **View Appointment Queue**: Display appointments waiting to be processed (FIFO)
- **Process Next Appointment**: Take next appointment from queue and mark as IN_PROGRESS
- **View Daily Report**: Generate comprehensive statistics and schedules for any date

### Special Features
- **Undo Functionality**: Stack-based undo system for appointment operations
- **Conflict Detection**: Prevents double-booking doctors (30-minute buffer)
- **Input Validation**: Comprehensive validation for all user inputs
- **Sample Data**: Pre-loaded test data for immediate testing
- **User-Friendly Display**: Formatted tables and visual separators

## 🏗 System Architecture

### Package Structure
```
com/clinicapp/
├── model/              # Data models
│   ├── Patient.java
│   ├── Doctor.java
│   └── Appointment.java
├── service/            # Business logic
│   ├── PatientManager.java
│   ├── DoctorManager.java
│   └── AppointmentManager.java
├── ui/                 # User interface
│   └── ClinicConsoleUI.java
├── util/               # Utilities
│   ├── DisplayHelper.java
│   └── InputValidator.java
└── ClinicAppointmentSystem.java  # Main entry point
```

### Design Patterns

#### Manager Pattern
- Separate managers for Patient, Doctor, and Appointment operations
- Each manager encapsulates business logic and data access
- Provides clean separation of concerns

#### MVC-like Structure
- **Model**: Patient, Doctor, Appointment classes
- **View**: DisplayHelper for formatted output
- **Controller**: ClinicConsoleUI coordinates user interactions

#### Utility Pattern
- DisplayHelper: Formatting and display functions
- InputValidator: Input validation and parsing

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line terminal

### Compilation

```bash
# Navigate to project root
cd /path/to/project

# Compile all Java files
javac -d bin src/main/java/com/clinicapp/*.java src/main/java/com/clinicapp/**/*.java

# Alternative: Compile with explicit classpath
javac -d bin src/main/java/com/clinicapp/model/*.java \
              src/main/java/com/clinicapp/service/*.java \
              src/main/java/com/clinicapp/util/*.java \
              src/main/java/com/clinicapp/ui/*.java \
              src/main/java/com/clinicapp/*.java
```

### Running the Application

```bash
# Run the main class
java -cp bin com.clinicapp.ClinicAppointmentSystem
```

### Quick Start
The application comes with pre-loaded sample data:
- 3 sample patients
- 3 sample doctors
- 2 sample appointments

This allows immediate testing of all features without manual data entry.

## 📖 Menu Options

### Complete Menu (28 Options)

| # | Category | Option | Description |
|---|----------|--------|-------------|
| 1 | Patient | Register New Patient | Add new patient with full details |
| 2 | Patient | View All Patients | Display all registered patients |
| 3 | Patient | Search Patient by Name | Find patients by name |
| 4 | Patient | View Patient Details | See complete patient info |
| 5 | Patient | Update Patient Information | Modify patient details |
| 6 | Patient | Delete Patient | Remove patient from system |
| 7 | Doctor | Add New Doctor | Register new doctor |
| 8 | Doctor | View All Doctors | Display all doctors |
| 9 | Doctor | Search Doctor by Name | Find doctors by name |
| 10 | Doctor | Search Doctor by Specialization | Filter by specialization |
| 11 | Doctor | View Doctor Details | See complete doctor info |
| 12 | Doctor | Update Doctor Information | Modify doctor details |
| 13 | Doctor | Set Doctor Availability | Change availability status |
| 14 | Doctor | Delete Doctor | Remove doctor from system |
| 15 | Appointment | Schedule New Appointment | Create new appointment |
| 16 | Appointment | View All Appointments | Display all appointments |
| 17 | Appointment | View Appointments by Patient | Filter by patient |
| 18 | Appointment | View Appointments by Doctor | Filter by doctor |
| 19 | Appointment | View Appointments by Date | Filter by date |
| 20 | Appointment | View Appointment Details | See complete appointment info |
| 21 | Appointment | Update Appointment | Modify appointment details |
| 22 | Appointment | Confirm Appointment | Change status to CONFIRMED |
| 23 | Appointment | Cancel Appointment | Cancel with undo support |
| 24 | Appointment | Complete Appointment | Mark as completed |
| 25 | Appointment | Mark Appointment as No-Show | Record no-show |
| 26 | Queue | View Appointment Queue | Display waiting queue |
| 27 | Queue | Process Next Appointment | Process next in queue |
| 28 | Report | View Daily Report | Generate daily statistics |
| 0 | System | Exit System | Exit application |

## 🔄 Sample Execution Steps

### Scenario 1: Registering a New Patient and Scheduling Appointment

```
Step 1: Register New Patient
----------------------------
Main Menu → Option 1
- Enter name: "Alice Johnson"
- Enter DOB: 2000-03-15
- Enter gender: Female
- Enter phone: 5559998888
- Enter email: alice.j@email.com
- Enter address: 321 Elm St
- Enter blood type: AB+
- Enter allergies: None

Result: Patient registered with ID 4

Step 2: Schedule Appointment
----------------------------
Main Menu → Option 15
- Enter patient ID: 4
- View available doctors (shown automatically)
- Enter doctor ID: 1 (Dr. Sarah Williams)
- Enter date: 2025-10-30
- Enter time: 10:00
- Enter reason: "First consultation"

Result: Appointment scheduled successfully with ID 3
```

### Scenario 2: Processing Appointment Queue

```
Step 1: View Current Queue
--------------------------
Main Menu → Option 26
- Displays all appointments in queue (FIFO order)
- Shows position, date/time, patient, doctor

Step 2: Process Next Appointment
--------------------------------
Main Menu → Option 27
- View current queue
- Confirm processing: yes
- First appointment in queue is removed
- Status changes to IN_PROGRESS

Result: Appointment #1 is now being processed
```

### Scenario 3: Completing an Appointment

```
Step 1: View Appointments by Doctor
-----------------------------------
Main Menu → Option 18
- Enter doctor ID: 1
- View all appointments for Dr. Williams
- Note the appointment ID you want to complete

Step 2: Complete the Appointment
--------------------------------
Main Menu → Option 24
- Enter appointment ID: 1
- View appointment details (confirmation)
- Enter completion notes: "Patient healthy, no issues found"

Result: Appointment marked as COMPLETED
```

### Scenario 4: Using Undo Functionality

```
Step 1: Cancel an Appointment
-----------------------------
Main Menu → Option 23
- Enter appointment ID: 2
- Confirm cancellation: yes
- Appointment status changes to CANCELLED

Step 2: Undo the Cancellation (Conceptual)
------------------------------------------
Note: The system tracks undo actions in a Stack
- Main menu shows "Undo Available: Yes"
- The AppointmentManager stores the previous state
- Can restore the appointment to its previous status

Result: Appointment can be restored if needed
```

### Scenario 5: Generating Daily Report

```
Step 1: View Daily Report
-------------------------
Main Menu → Option 28
- Enter date: 2025-10-26

Report Shows:
- Total Appointments: 5
- Scheduled: 2
- Confirmed: 1
- In Progress: 1
- Completed: 1
- Cancelled: 0
- No Show: 0
- Completion Rate: 20%
- Daily schedule with time-sorted appointments

Result: Comprehensive view of daily operations
```

### Scenario 6: Searching and Updating Patient

```
Step 1: Search Patient by Name
------------------------------
Main Menu → Option 3
- Enter search term: "john"
- System finds: John Smith (ID: 1)

Step 2: View Patient Details
----------------------------
Main Menu → Option 4
- Enter patient ID: 1
- View complete information
- See appointment history

Step 3: Update Patient Information
----------------------------------
Main Menu → Option 5
- Enter patient ID: 1
- Leave name empty (keep current)
- Enter new phone: 5551112222
- Leave email empty (keep current)
- Enter new address: 456 New Street

Result: Patient information updated successfully
```

### Scenario 7: Managing Doctor Availability

```
Step 1: View All Doctors
------------------------
Main Menu → Option 8
- Displays all doctors with status
- Note: Dr. Emily Brown (ID: 3) is Available

Step 2: Set Doctor Unavailable
------------------------------
Main Menu → Option 13
- Enter doctor ID: 3
- Current status: Available
- Set as available?: no

Result: Dr. Brown is now marked as Unavailable
```

## 🗃 Data Structures

### HashMap
- **Usage**: Storing patients, doctors, and appointments
- **Benefit**: O(1) lookup time by ID
- **Implementation**: `Map<Integer, Patient/Doctor/Appointment>`

### Stack
- **Usage**: Undo functionality for appointments
- **Benefit**: LIFO (Last In First Out) - undo most recent action first
- **Implementation**: `Stack<AppointmentAction>`
- **Operations**: push (add action), pop (undo action), peek (view top)

### Queue
- **Usage**: Appointment processing order
- **Benefit**: FIFO (First In First Out) - fair processing order
- **Implementation**: `Queue<Appointment>` using LinkedList
- **Operations**: offer (add to queue), poll (remove and process), peek (view next)

### ArrayList
- **Usage**: Returning filtered lists of records
- **Benefit**: Dynamic sizing, easy iteration
- **Implementation**: `List<Patient/Doctor/Appointment>`

### LocalDateTime
- **Usage**: Date and time handling
- **Benefit**: Modern Java time API with timezone support
- **Implementation**: Parsing, formatting, comparison operations

## 📁 Code Organization

### Model Classes (clinic.model)
- **Patient.java**: Patient entity with demographics and medical info
- **Doctor.java**: Doctor entity with specialization and availability
- **Appointment.java**: Appointment entity linking patients and doctors

### Manager Classes (clinic.manager)
- **PatientManager.java**: CRUD operations for patients
- **DoctorManager.java**: CRUD operations for doctors
- **AppointmentManager.java**: Appointment operations with undo and queue

### UI Classes (clinic.ui)
- **ClinicConsoleUI.java**: Complete menu system and user interactions

### Utility Classes (clinic.util)
- **DisplayHelper.java**: Formatting and display functions
- **InputValidator.java**: Input validation and parsing

### Main Class
- **ClinicAppointmentSystem.java**: Application entry point

## 🧪 Testing

### Manual Testing Checklist

#### Patient Management
- [x] Register new patient with all fields
- [x] Register patient with optional fields empty
- [x] View all patients
- [x] Search patient by partial name
- [x] View patient details with appointment history
- [x] Update patient information
- [x] Delete patient with confirmation

#### Doctor Management
- [x] Add new doctor with full schedule
- [x] View all doctors
- [x] Search doctor by name
- [x] Search doctor by specialization
- [x] View doctor details with appointments
- [x] Update doctor information
- [x] Toggle doctor availability
- [x] Delete doctor with warnings

#### Appointment Management
- [x] Schedule appointment with conflict detection
- [x] View all appointments
- [x] View appointments by patient
- [x] View appointments by doctor
- [x] View appointments by date
- [x] View appointment details
- [x] Update appointment time and notes
- [x] Confirm scheduled appointment
- [x] Cancel appointment
- [x] Complete appointment with notes
- [x] Mark appointment as no-show

#### Queue & Reporting
- [x] View appointment queue
- [x] Process next appointment in queue
- [x] Generate daily report with statistics
- [x] View daily schedule

#### Error Handling
- [x] Invalid menu option
- [x] Invalid patient/doctor/appointment ID
- [x] Invalid date format
- [x] Invalid time format
- [x] Invalid email format
- [x] Invalid phone number
- [x] Scheduling conflict detection
- [x] Empty queue processing
- [x] Confirmation on destructive operations

## 🎯 Key Features Explained

### Undo Functionality
The system uses a Stack to track appointment actions:
```java
// When an action occurs
undoStack.push(new AppointmentAction(type, appointment, previousState));

// To undo
AppointmentAction action = undoStack.pop();
// Restore previous state
```

### Conflict Detection
Appointments within 30 minutes of each other for the same doctor are prevented:
```java
if (Math.abs(Duration.between(existingTime, newTime).toMinutes()) < 30) {
    return "CONFLICT";
}
```

### Queue Processing
Appointments are processed in order of scheduling:
```java
// Add to queue
appointmentQueue.offer(appointment);

// Process next
Appointment next = appointmentQueue.poll();
next.setStatus(AppointmentStatus.IN_PROGRESS);
```

## 📝 Notes

### Input Formats
- **Dates**: yyyy-MM-dd (e.g., 2025-10-26)
- **Times**: HH:mm (e.g., 14:30)
- **Phone**: 10-15 digits, optional + prefix
- **Email**: Standard email format
- **Blood Type**: A+, A-, B+, B-, AB+, AB-, O+, O-
- **Gender**: Male, Female, Other

### Validation Rules
- Names: Cannot be empty
- Phone: Must be 10-15 digits
- Email: Must match email pattern
- Date: Must be in correct format
- Time: Must be in HH:mm format
- Appointment conflicts: 30-minute buffer required

### Status Flow
```
SCHEDULED → CONFIRMED → IN_PROGRESS → COMPLETED
         ↓
    CANCELLED / NO_SHOW
```

## 🚀 Future Enhancements

Potential improvements for future versions:
- Persistent storage (database or file system)
- Multiple undo levels
- Appointment reminders
- Billing and payment tracking
- Medical records integration
- Multi-clinic support
- User authentication and roles
- Export reports to PDF/CSV
- SMS/Email notifications
- Online booking interface

## 👥 Contributors

Developed as part of the Clinic Management System project.

## 📄 License

This project is created for educational purposes.

---

**Version**: 1.0  
**Last Updated**: October 2025  
**Status**: Production Ready ✓
# Clinic Appointment System

## Project Purpose

The Clinic Appointment System is a console-based Java application designed to manage patient appointments at a medical clinic. This system allows clinic staff to schedule, view, update, and cancel appointments efficiently through a simple text-based interface.

## Features (Planned)

- Schedule new patient appointments
- View all scheduled appointments
- Update existing appointment details
- Cancel appointments
- Simple menu-driven interface

## Requirements

- **Java Development Kit (JDK)**: Version 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Terminal/Command Prompt**: For running the application

### Checking Your Java Version

To verify that Java is installed and check the version:

```bash
java -version
javac -version
```

If Java is not installed, download it from [Oracle's official website](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK.

## Project Structure

```
clinic-appointment-system/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── clinicapp/
│                   └── ClinicAppointmentSystem.java
├── .gitignore
└── README.md
```

### Package Layout

- **`com.clinicapp`**: Main package containing the application code
  - **`ClinicAppointmentSystem.java`**: Entry point of the application with the main method

## Compilation Instructions

### Using Command Line (javac)

1. Navigate to the project root directory:
   ```bash
   cd /path/to/clinic-appointment-system
   ```

2. Compile the Java source files:
   ```bash
   javac -d out src/main/java/com/clinicapp/ClinicAppointmentSystem.java
   ```

   This command:
   - `-d out`: Specifies the destination directory for compiled `.class` files
   - Compiles all source files in the package

3. Alternatively, compile without specifying output directory (classes will be created alongside source files):
   ```bash
   javac src/main/java/com/clinicapp/ClinicAppointmentSystem.java
   ```

## Execution Instructions

### After Compilation (with -d out option)

If you compiled with the `-d out` option:

```bash
java -cp out com.clinicapp.ClinicAppointmentSystem
```

### After Compilation (without -d option)

If you compiled without the `-d` option:

```bash
java -cp src/main/java com.clinicapp.ClinicAppointmentSystem
```

### All-in-One Compilation and Execution

For quick testing, you can compile and run in one go:

```bash
# Compile
javac -d out src/main/java/com/clinicapp/ClinicAppointmentSystem.java

# Run
java -cp out com.clinicapp.ClinicAppointmentSystem
```

## Usage

Once the application starts, you will see a welcome message and a menu with the following options:

1. Schedule new appointment
2. View all appointments
3. Cancel appointment
4. Update appointment
5. Exit system

Follow the on-screen prompts to interact with the system.

## Development Status

This project is currently in the initial skeleton/scaffolding phase. The main class has been created with placeholder comments outlining the intended functionality. Future development will implement the complete appointment management features.

## Contributing

When adding new features:

1. Follow the existing package structure
2. Maintain consistent code style and naming conventions
3. Add appropriate comments for complex logic
4. Test compilation after making changes

## License

This is an educational project for learning Java programming fundamentals.
