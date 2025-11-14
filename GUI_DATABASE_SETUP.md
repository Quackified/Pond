# GUI and Database Setup Guide

## Overview

This guide explains how to set up and use the GUI version of the Clinic Appointment System with MySQL database integration and full UTF-8 encoding support.

## UTF-8 Encoding Implementation

### Database Layer
All database connections use UTF-8 encoding to prevent formatting and encoding issues:

- **JDBC URL Parameters**: `useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_unicode_ci`
- **Database Charset**: UTF8MB4 (supports all Unicode characters including emojis)
- **Table Collation**: utf8mb4_unicode_ci (case-insensitive Unicode)
- **Connection Setup**: Explicit `SET NAMES 'utf8mb4'` and `SET CHARACTER SET utf8mb4`

### GUI Layer
All Swing components are configured for UTF-8 text rendering:

- **Text Fields**: SansSerif font with UTF-8 support
- **Text Areas**: Line wrapping with word boundaries for international text
- **Tables**: UTF-8 compatible table models and renderers
- **Dialogs**: UTF-8 text input and display

### Java Application
- **File Encoding**: `-Dfile.encoding=UTF-8` JVM parameter
- **Compilation**: `-encoding UTF-8` javac parameter
- **Character Set**: StandardCharsets.UTF_8 for all I/O operations

## Prerequisites

### 1. Java Development Kit (JDK)
- JDK 8 or higher
- Verify: `java -version`

### 2. MySQL Server
- MySQL 5.7+ or MySQL 8.0+
- Running on localhost:3306 (default)
- Root access or dedicated database user

### 3. MySQL Connector/J
- Version 8.0.33 recommended
- Download from: https://dev.mysql.com/downloads/connector/j/
- Or Maven Central: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/

## Installation Steps

### Step 1: Download MySQL Connector

```bash
# Option 1: Using wget
cd /path/to/project
mkdir -p lib
cd lib
wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar

# Option 2: Manual download
# Download from https://dev.mysql.com/downloads/connector/j/
# Extract and copy mysql-connector-j-8.0.33.jar to lib/ directory
```

### Step 2: Configure Database

Edit `database.properties` in the project root:

```properties
# Database connection details
db.url=jdbc:mysql://localhost:3306/clinic_db
db.username=root
db.password=your_password_here

# UTF-8 encoding parameters (already configured)
db.useUnicode=true
db.characterEncoding=UTF-8
db.connectionCollation=utf8mb4_unicode_ci
db.serverTimezone=UTC

# Database charset settings
db.charset=utf8mb4
db.collation=utf8mb4_unicode_ci
```

### Step 3: Start MySQL Server

```bash
# Linux/Mac
sudo service mysql start
# or
sudo systemctl start mysql

# Windows
# Start MySQL from Services or MySQL Workbench
```

### Step 4: Compile the Application

```bash
cd /path/to/project
chmod +x compile.sh
./compile.sh
```

The script will:
- Detect MySQL Connector JAR
- Compile all Java files with UTF-8 encoding
- Create database and tables automatically on first run

### Step 5: Run the GUI Application

```bash
chmod +x run.sh
./run.sh gui
```

## UTF-8 Encoding Features

### Character Support
The system fully supports:
- **Latin characters**: A-Z, a-z
- **Accented characters**: á, é, í, ó, ú, ñ, ü, etc.
- **Cyrillic**: А-Я, а-я
- **Greek**: Α-Ω, α-ω
- **Asian characters**: 中文, 日本語, 한글
- **Emojis**: 😊, ❤️, 🏥
- **Special symbols**: €, £, ¥, ©, ®, ™

### Database Tables with UTF-8

All tables are created with UTF8MB4 charset:

```sql
CREATE TABLE patients (
  ...
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

This ensures:
- Correct storage of international characters
- Proper sorting and comparison
- Case-insensitive searches
- No data loss or corruption

### Text Input and Display

All GUI components handle UTF-8:
- Text fields accept international characters
- Text areas display multi-line UTF-8 text
- Tables show UTF-8 data correctly
- Search functions work with international text

## Using the GUI Application

### Main Window

The application has three main tabs:

1. **Patients**: Manage patient records
2. **Doctors**: Manage doctor information
3. **Appointments**: Schedule and manage appointments

### Patient Management

**Add Patient:**
1. Click "Add Patient"
2. Enter patient information (UTF-8 supported in all fields)
3. Click "Save"

**Edit Patient:**
1. Select a patient from the table
2. Click "Edit Patient"
3. Modify information
4. Click "Save"

**Search Patients:**
1. Enter name in search field (supports international characters)
2. Click "Search" or press Enter
3. Results are displayed with UTF-8 text

**Delete Patient:**
1. Select a patient
2. Click "Delete Patient"
3. Confirm deletion

### Doctor Management

Similar to patient management with additional fields:
- Specialization (UTF-8 supported)
- Available days (checkboxes)
- Working hours
- Availability status

### Appointment Management

**Schedule Appointment:**
1. Click "Schedule Appointment"
2. Select patient from dropdown
3. Select doctor from dropdown
4. Enter date and time (format: yyyy-MM-dd HH:mm)
5. Enter reason (UTF-8 text area)
6. Click "Save"

**Update Status:**
1. Select an appointment
2. Click "Update Status"
3. Choose new status from dropdown
4. Confirm

## Troubleshooting

### MySQL Connection Issues

**Problem**: Cannot connect to database

**Solutions**:
1. Check MySQL is running: `sudo service mysql status`
2. Verify credentials in `database.properties`
3. Check MySQL port: default is 3306
4. Ensure MySQL user has permissions:
   ```sql
   GRANT ALL PRIVILEGES ON clinic_db.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

### UTF-8 Display Issues

**Problem**: Special characters show as � or ???

**Solutions**:
1. Verify database.properties has correct UTF-8 settings
2. Check MySQL server charset:
   ```sql
   SHOW VARIABLES LIKE 'character_set%';
   SHOW VARIABLES LIKE 'collation%';
   ```
3. Ensure JVM uses UTF-8: `-Dfile.encoding=UTF-8`
4. Recompile with UTF-8: `javac -encoding UTF-8`

### MySQL Connector Not Found

**Problem**: Compilation fails or GUI won't start

**Solutions**:
1. Download MySQL Connector JAR
2. Place in `lib/` directory
3. Ensure filename matches: `mysql-connector*.jar`
4. Run `./compile.sh` again

## Testing UTF-8 Support

### Test International Characters

1. Add a patient with name: "José García"
2. Add a doctor with name: "李医生" (Chinese)
3. Add appointment reason: "Consulta médica 🏥"
4. Verify all text displays correctly in tables and forms

### Test Search with UTF-8

1. Search for "José" - should find "José García"
2. Search for "李" - should find "李医生"
3. Search for partial text in international characters

### Test Database Persistence

1. Add records with international characters
2. Close application
3. Restart application
4. Verify all international characters are preserved

## Architecture

### Layer Structure

```
GUI Layer (javax.swing)
    ↓ UTF-8 text components
DAO Layer (JDBC)
    ↓ PreparedStatements with UTF-8
Database Layer (MySQL)
    ↓ UTF8MB4 tables
Data Storage
```

### Key Classes

- **DatabaseConnection**: Singleton with UTF-8 connection setup
- **PatientDAO, DoctorDAO, AppointmentDAO**: UTF-8 data access
- **PatientPanel, DoctorPanel, AppointmentPanel**: UTF-8 UI components
- **PatientFormDialog, DoctorFormDialog, AppointmentFormDialog**: UTF-8 input forms

## Performance Considerations

- **Connection Pooling**: Can be added for better performance
- **Prepared Statements**: Used throughout for security and efficiency
- **Batch Operations**: Supported for bulk inserts
- **Index Creation**: Recommended for name columns with international text

## Security Notes

- All database queries use PreparedStatements to prevent SQL injection
- UTF-8 encoding prevents character encoding attacks
- Database passwords should be stored securely (not in properties file for production)
- Use strong passwords for MySQL users

## Additional Resources

- MySQL UTF-8 Guide: https://dev.mysql.com/doc/refman/8.0/en/charset-unicode.html
- Java Swing Tutorial: https://docs.oracle.com/javase/tutorial/uiswing/
- JDBC Best Practices: https://docs.oracle.com/javase/tutorial/jdbc/

## Support

For issues or questions:
1. Check MySQL logs: `/var/log/mysql/error.log`
2. Check Java console output for error messages
3. Verify UTF-8 encoding at each layer
4. Test with simple ASCII text first, then add international characters

## Version Information

- Java: 8+
- MySQL: 5.7+ or 8.0+
- MySQL Connector/J: 8.0.33
- Swing: Part of Java SDK
- UTF-8: Full Unicode support including emojis
