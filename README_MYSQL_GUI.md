# Clinic Appointment Management System
## MySQL Database + Java Swing GUI Integration

This document describes the integrated version of the Clinic Appointment Management System with **Java Swing GUI (JForm)** and **MySQL Database** connectivity using **MySQL Connector/J**.

## 🎯 New Features

### Java Swing GUI (JForm)
- **Modern Graphical User Interface** replacing the console-based UI
- **MainFrame**: Central application window with menu-driven navigation
- **Patient Management Panel**: Add, edit, delete, search, and view patient records
- **Doctor Management Panel**: Complete doctor management with availability toggle
- **Appointment Management Panel**: Schedule, edit, cancel, and complete appointments
- **Interactive Forms**: User-friendly dialogs for data entry
- **Table Views**: Display all records in sortable, readable tables
- **Styled Components**: Professional-looking buttons and layouts

### MySQL Database Integration
- **Persistent Data Storage**: All data stored in MySQL database
- **MySQL Connector/J**: Official MySQL JDBC driver for connectivity
- **Automatic Schema Creation**: Database and tables created automatically
- **DAO Pattern**: Clean separation between business logic and data access
- **Foreign Key Relationships**: Data integrity with proper relationships
- **Connection Management**: Singleton pattern for efficient connection handling

## 📋 System Requirements

### Software Requirements
- **Java Development Kit (JDK)** 8 or higher
- **MySQL Server** 5.7 or higher
- **MySQL Connector/J** 8.0.x (JDBC Driver)

### Hardware Requirements
- Minimum 2GB RAM
- 100MB disk space for database

## 🚀 Installation & Setup

### Step 1: Install MySQL Server

#### On Ubuntu/Debian:
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### On macOS (using Homebrew):
```bash
brew install mysql
brew services start mysql
```

#### On Windows:
Download and install from: https://dev.mysql.com/downloads/mysql/

### Step 2: Configure MySQL

```bash
# Login to MySQL as root
sudo mysql -u root -p

# Create database user (optional, or use root)
CREATE USER 'clinic_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON clinic_db.* TO 'clinic_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Step 3: Download MySQL Connector/J

1. Download MySQL Connector/J from:
   https://dev.mysql.com/downloads/connector/j/

2. Extract the JAR file (mysql-connector-java-X.X.XX.jar)

3. Place it in the `lib/` directory:
   ```bash
   mkdir -p lib
   cp /path/to/mysql-connector-java-*.jar lib/
   ```

### Step 4: Configure Database Connection

Edit `database.properties` file:

```properties
db.url=jdbc:mysql://localhost:3306/clinic_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password_here
db.driver=com.mysql.cj.jdbc.Driver
```

### Step 5: Compile the Application

```bash
./compile.sh
```

This will:
- Create the `bin/` directory for compiled classes
- Detect MySQL Connector JAR in `lib/`
- Compile all Java source files including GUI and DAO layers

### Step 6: Run the Application

#### Run GUI Version:
```bash
./run.sh gui
```

#### Run Console Version (original):
```bash
./run.sh
```

Or directly with Java:
```bash
# GUI Version
java -cp "bin:lib/mysql-connector-java-*.jar" com.clinicapp.ClinicAppointmentSystemGUI

# Console Version
java -cp bin com.clinicapp.ClinicAppointmentSystem
```

## 🗄️ Database Schema

The application automatically creates the following tables:

### patients
- `id` (INT, PRIMARY KEY, AUTO_INCREMENT)
- `name` (VARCHAR)
- `date_of_birth` (DATE)
- `gender` (VARCHAR)
- `phone` (VARCHAR)
- `email` (VARCHAR)
- `address` (TEXT)
- `blood_type` (VARCHAR)
- `allergies` (TEXT)
- `created_at` (TIMESTAMP)

### doctors
- `id` (INT, PRIMARY KEY, AUTO_INCREMENT)
- `name` (VARCHAR)
- `specialization` (VARCHAR)
- `phone` (VARCHAR)
- `email` (VARCHAR)
- `office_location` (VARCHAR)
- `schedule` (TEXT)
- `is_available` (BOOLEAN)
- `created_at` (TIMESTAMP)

### appointments
- `id` (INT, PRIMARY KEY, AUTO_INCREMENT)
- `patient_id` (INT, FOREIGN KEY → patients.id)
- `doctor_id` (INT, FOREIGN KEY → doctors.id)
- `appointment_date` (DATE)
- `appointment_time` (TIME)
- `reason` (TEXT)
- `status` (VARCHAR)
- `notes` (TEXT)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

## 🎨 GUI Features

### Main Window
- **Menu Bar**: Quick access to all features
- **Welcome Screen**: Dashboard with navigation buttons
- **Home Button**: Return to welcome screen from any panel

### Patient Management
- **View All Patients**: Table view with all patient records
- **Add Patient**: Form to register new patients
- **Edit Patient**: Modify existing patient information
- **Delete Patient**: Remove patient with confirmation
- **Search**: Find patients by name
- **View Details**: See complete patient information

### Doctor Management
- **View All Doctors**: Table view with all doctor records
- **Add Doctor**: Form to register new doctors with schedules
- **Edit Doctor**: Modify doctor information
- **Delete Doctor**: Remove doctor with confirmation
- **Search**: Find doctors by name
- **Toggle Availability**: Change doctor availability status
- **View Details**: See complete doctor information

### Appointment Management
- **View All Appointments**: Table view with all appointments
- **Schedule Appointment**: Create new appointments with patient/doctor selection
- **Edit Appointment**: Modify appointment details
- **Cancel Appointment**: Cancel with status update
- **Complete Appointment**: Mark as completed with notes
- **Status Tracking**: SCHEDULED → CONFIRMED → IN_PROGRESS → COMPLETED

## 🏗️ Architecture

### Package Structure
```
com/clinicapp/
├── model/                  # Data models (unchanged)
│   ├── Patient.java
│   ├── Doctor.java
│   └── Appointment.java
├── service/                # Business logic (existing)
│   ├── PatientManager.java
│   ├── DoctorManager.java
│   └── AppointmentManager.java
├── ui/                     # Console UI (existing)
│   └── ClinicConsoleUI.java
├── util/                   # Utilities (existing)
│   ├── DisplayHelper.java
│   └── InputValidator.java
├── db/                     # NEW: Database connectivity
│   └── DatabaseConnection.java
├── dao/                    # NEW: Data Access Objects
│   ├── PatientDAO.java
│   ├── DoctorDAO.java
│   └── AppointmentDAO.java
├── gui/                    # NEW: Swing GUI components
│   ├── MainFrame.java
│   ├── PatientPanel.java
│   ├── PatientFormDialog.java
│   ├── DoctorPanel.java
│   ├── DoctorFormDialog.java
│   ├── AppointmentPanel.java
│   └── AppointmentFormDialog.java
├── ClinicAppointmentSystem.java        # Console version entry point
└── ClinicAppointmentSystemGUI.java     # NEW: GUI version entry point
```

### Design Patterns

#### Singleton Pattern
- `DatabaseConnection`: Single shared database connection

#### DAO Pattern
- Separate data access layer from business logic
- `PatientDAO`, `DoctorDAO`, `AppointmentDAO`

#### MVC Pattern
- **Model**: Patient, Doctor, Appointment classes
- **View**: GUI panels and dialogs
- **Controller**: Event handlers in GUI components

## 🔧 Troubleshooting

### MySQL Connection Issues

**Problem**: Cannot connect to MySQL
```
Solution:
1. Ensure MySQL server is running:
   sudo systemctl status mysql
2. Check credentials in database.properties
3. Verify MySQL port (default: 3306)
4. Check firewall settings
```

**Problem**: Access denied for user
```
Solution:
1. Verify username and password in database.properties
2. Grant proper privileges in MySQL:
   GRANT ALL PRIVILEGES ON clinic_db.* TO 'user'@'localhost';
```

### Compilation Issues

**Problem**: MySQL Connector not found
```
Solution:
1. Download MySQL Connector/J JAR
2. Place in lib/ directory
3. Run ./compile.sh again
```

**Problem**: ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
Solution:
1. Ensure MySQL Connector JAR is in classpath
2. Run with: java -cp "bin:lib/*" com.clinicapp.ClinicAppointmentSystemGUI
```

### GUI Issues

**Problem**: GUI doesn't appear
```
Solution:
1. Check if DISPLAY is set (on Linux)
2. Ensure you're not running in headless mode
3. Try: export DISPLAY=:0
```

## 📊 Data Migration

To migrate existing data or import sample data:

```sql
-- Connect to MySQL
mysql -u root -p clinic_db

-- Import sample data
SOURCE database/sample_data.sql;
```

## 🔐 Security Considerations

1. **Database Credentials**: Store in secure configuration files
2. **SQL Injection**: Prevented using PreparedStatements
3. **Connection Pooling**: Implement for production use
4. **Access Control**: Add user authentication for production

## 📚 Additional Resources

- **MySQL Documentation**: https://dev.mysql.com/doc/
- **JDBC Tutorial**: https://docs.oracle.com/javase/tutorial/jdbc/
- **Swing Tutorial**: https://docs.oracle.com/javase/tutorial/uiswing/
- **MySQL Connector/J**: https://dev.mysql.com/doc/connector-j/en/

## 🆚 Version Comparison

| Feature | Console Version | GUI Version |
|---------|----------------|-------------|
| User Interface | Text-based console | Graphical Swing GUI |
| Data Storage | In-memory (HashMap) | MySQL Database |
| Data Persistence | No | Yes |
| Navigation | Menu numbers | Click buttons |
| Data Entry | Text input | Forms and dialogs |
| Data Display | Formatted text | Tables and panels |
| Search | Manual input | Search boxes |
| Multi-user | No | Possible with DB |

## 🎓 Learning Outcomes

This integrated version demonstrates:
- Java Swing GUI development
- MySQL database design
- JDBC connectivity with MySQL Connector/J
- DAO design pattern
- MVC architecture
- Event-driven programming
- Database-backed application development

## 📝 License

This project is for educational purposes.

## 👥 Support

For issues or questions:
1. Check the troubleshooting section
2. Review MySQL and JDBC documentation
3. Verify all prerequisites are installed

---

**Version**: 2.0 - MySQL + Swing GUI Integration  
**Last Updated**: 2024
