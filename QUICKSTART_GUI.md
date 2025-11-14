# Quick Start Guide - GUI Version
## Clinic Appointment Management System with MySQL

## 🚀 Get Started in 5 Minutes

### Prerequisites Checklist
- [ ] Java JDK 8 or higher installed
- [ ] MySQL Server 5.7+ installed and running
- [ ] MySQL Connector/J JAR downloaded

### Step 1: Install MySQL (if not already installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
```

**macOS:**
```bash
brew install mysql
brew services start mysql
```

**Windows:**
Download from: https://dev.mysql.com/downloads/mysql/

### Step 2: Get MySQL Connector/J

The JAR is already included in the repository's `lib/` directory!

If you need to download it manually:
```bash
cd lib
wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
```

Or download from: https://dev.mysql.com/downloads/connector/j/

### Step 3: Configure Database (Optional)

**Default settings work out of the box:**
- Host: localhost
- Port: 3306
- User: root
- Password: (empty)
- Database: clinic_db (auto-created)

**To use custom settings:**

Edit `database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/clinic_db?useSSL=false&serverTimezone=UTC
db.username=your_username
db.password=your_password
```

### Step 4: Compile the Application

```bash
chmod +x compile.sh run.sh
./compile.sh
```

**Expected output:**
```
Compiling Clinic Appointment System...
=======================================
Using MySQL Connector: lib/mysql-connector-j-8.0.33.jar

✓ Compilation successful!

Class files created: 30
```

### Step 5: Run the GUI Application

```bash
./run.sh gui
```

**Expected output:**
```
Starting Clinic Appointment System (GUI)...
Using MySQL Connector: lib/mysql-connector-j-8.0.33.jar
✓ MySQL JDBC Driver loaded successfully
✓ Database checked/created
✓ Database tables checked/created
✓ Database connection established
✓ Application started successfully!
```

**The GUI window will open automatically!**

## 🎨 Using the GUI

### Main Window

When the application starts, you'll see:
- **Welcome Screen** with three main buttons
- **Menu Bar** at the top with quick access

### Managing Patients

1. Click **"Manage Patients"** button or use menu: **Patients → View All Patients**
2. You'll see a table with all patients
3. Use the buttons:
   - **Add Patient**: Create new patient record
   - **Edit Patient**: Modify selected patient
   - **Delete Patient**: Remove selected patient
   - **View Details**: See complete information
   - **Search**: Find patients by name

**To add a patient:**
1. Click "Add Patient"
2. Fill in the form:
   - Name (required)
   - Date of Birth in format: yyyy-MM-dd (e.g., 1990-05-15)
   - Gender: Select from dropdown
   - Phone (required)
   - Email (optional)
   - Address (optional)
   - Blood Type (optional)
   - Allergies (optional)
3. Click "Save"

### Managing Doctors

1. Click **"Manage Doctors"** or menu: **Doctors → View All Doctors**
2. Use the buttons:
   - **Add Doctor**: Register new doctor
   - **Edit Doctor**: Modify doctor information
   - **Delete Doctor**: Remove doctor
   - **View Details**: See complete information
   - **Toggle Availability**: Change availability status
   - **Search**: Find doctors by name

**To add a doctor:**
1. Click "Add Doctor"
2. Fill in:
   - Name (required)
   - Specialization (required) - e.g., "Cardiology", "Pediatrics"
   - Phone (required)
   - Email (optional)
   - Start Time (e.g., 09:00)
   - End Time (e.g., 17:00)
   - Select working days (checkboxes)
3. Click "Save"

### Managing Appointments

1. Click **"Manage Appointments"** or menu: **Appointments → View All Appointments**
2. Use the buttons:
   - **Schedule Appointment**: Create new appointment
   - **Edit Appointment**: Modify appointment
   - **Cancel Appointment**: Mark as cancelled
   - **Complete Appointment**: Mark as completed

**To schedule an appointment:**
1. Click "Schedule Appointment"
2. Select patient from dropdown
3. Select doctor from dropdown
4. Enter date in format: yyyy-MM-dd (e.g., 2024-11-20)
5. Enter time in format: HH:mm (e.g., 14:30)
6. Enter reason for visit
7. Click "Save"

**Appointment Statuses:**
- SCHEDULED: Just created
- CONFIRMED: Patient confirmed
- IN_PROGRESS: Patient currently being seen
- COMPLETED: Visit completed
- CANCELLED: Appointment cancelled
- NO_SHOW: Patient didn't show up

## 🔧 Troubleshooting

### Problem: "Cannot connect to database"

**Solution:**
```bash
# Check if MySQL is running
sudo systemctl status mysql

# If not running, start it
sudo systemctl start mysql

# Test connection
mysql -u root -p
```

### Problem: "Access denied for user 'root'"

**Solution:**
1. Reset MySQL root password:
```bash
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_new_password';
FLUSH PRIVILEGES;
EXIT;
```

2. Update `database.properties` with the new password

### Problem: "MySQL Connector not found"

**Solution:**
```bash
# Check if JAR exists
ls -la lib/

# If missing, download it
cd lib
wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
```

### Problem: GUI doesn't appear

**Solutions:**
1. Ensure you're not in a headless environment
2. Check if DISPLAY is set: `echo $DISPLAY`
3. Try: `export DISPLAY=:0`

### Problem: "Compilation failed"

**Solutions:**
1. Check Java version: `java -version` (need 8+)
2. Install JDK: `sudo apt install default-jdk`
3. Ensure all source files are present

## 📚 Common Tasks

### View Database Contents

```bash
mysql -u root -p
USE clinic_db;

-- View all patients
SELECT * FROM patients;

-- View all doctors
SELECT * FROM doctors;

-- View all appointments
SELECT * FROM appointments;

-- Exit MySQL
EXIT;
```

### Backup Database

```bash
mysqldump -u root -p clinic_db > clinic_backup.sql
```

### Restore Database

```bash
mysql -u root -p clinic_db < clinic_backup.sql
```

### Clear All Data (Start Fresh)

```bash
mysql -u root -p
DROP DATABASE clinic_db;
EXIT;

# Then restart the application - it will recreate the database
./run.sh gui
```

## 🆚 Console vs GUI Version

**To run console version (original):**
```bash
./run.sh
```

**To run GUI version:**
```bash
./run.sh gui
```

Both versions work simultaneously - console uses in-memory storage, GUI uses MySQL.

## 💡 Tips & Tricks

1. **Search is case-insensitive**: Typing "john" finds "John Smith"

2. **Date format**: Always use yyyy-MM-dd (e.g., 2024-11-20)

3. **Time format**: Always use HH:mm in 24-hour format (e.g., 14:30)

4. **Confirmation dialogs**: The system always asks before deleting

5. **Home button**: Click "Home" at any time to return to welcome screen

6. **Refresh**: Data refreshes automatically after add/edit/delete

7. **Selection**: Click on a table row to select it before edit/delete

8. **Status updates**: Appointment status can be changed by editing

9. **Doctor availability**: Use "Toggle Availability" button for quick changes

10. **Multiple appointments**: You can schedule multiple appointments for same patient/doctor

## 🎓 Sample Data

Want to test with sample data?

**Add sample patients:**
- John Smith, 1985-03-15, Male, 5551234567
- Jane Doe, 1990-07-22, Female, 5559876543
- Bob Johnson, 1978-11-30, Male, 5555551212

**Add sample doctors:**
- Dr. Sarah Williams, Cardiology, 5551111111
- Dr. Michael Chen, Pediatrics, 5552222222
- Dr. Emily Brown, General Practice, 5553333333

**Schedule appointments:**
- Pick any patient + doctor
- Set date to tomorrow or next week
- Time: 10:00, 14:00, 16:00 etc.

## 📖 Next Steps

- See `README_MYSQL_GUI.md` for detailed documentation
- See `INTEGRATION_SUMMARY.md` for technical details
- See original `README.md` for console version documentation

## ❓ Need Help?

1. Check this guide thoroughly
2. Review error messages in terminal
3. Verify MySQL is running
4. Check database.properties configuration
5. Ensure MySQL Connector JAR is in lib/

## ✅ Success Checklist

- [ ] MySQL server is running
- [ ] MySQL Connector JAR is in lib/ directory
- [ ] Application compiled successfully (30 class files)
- [ ] GUI window opens when running `./run.sh gui`
- [ ] Can add a patient
- [ ] Can add a doctor
- [ ] Can schedule an appointment
- [ ] Can search for records
- [ ] Can view details
- [ ] Can edit records
- [ ] Can delete records

**If all checked ✅ - You're ready to go!**

---

**Enjoy using the Clinic Appointment Management System!** 🏥
