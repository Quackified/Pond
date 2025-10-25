# Quick Start Guide

## Get Up and Running in 3 Steps

### Step 1: Compile
```bash
./compile.sh
```

Or manually:
```bash
mkdir -p bin
javac -d bin src/main/java/com/clinicapp/model/*.java \
              src/main/java/com/clinicapp/service/*.java \
              src/main/java/com/clinicapp/util/*.java \
              src/main/java/com/clinicapp/ui/*.java \
              src/main/java/com/clinicapp/*.java
```

### Step 2: Run
```bash
./run.sh
```

Or manually:
```bash
java -cp bin com.clinicapp.ClinicAppointmentSystem
```

### Step 3: Explore!
The application comes with sample data:
- 3 Patients (John Smith, Jane Doe, Bob Johnson)
- 3 Doctors (Dr. Williams, Dr. Chen, Dr. Brown)
- 2 Pre-scheduled Appointments

## First Steps in the Application

### Try These Actions:

1. **View Sample Data**
   - Press `2` - View All Patients
   - Press `8` - View All Doctors
   - Press `16` - View All Appointments

2. **Schedule a New Appointment**
   - Press `15` - Schedule New Appointment
   - Enter Patient ID: `1` (John Smith)
   - Enter Doctor ID: `1` (Dr. Williams)
   - Enter Date: Tomorrow's date (format: yyyy-MM-dd)
   - Enter Time: `14:00`
   - Enter Reason: `Annual checkup`

3. **View Reports**
   - Press `28` - View Daily Report
   - Enter today's date or tomorrow's date
   - See statistics and schedule

4. **Process Queue**
   - Press `26` - View Appointment Queue
   - Press `27` - Process Next Appointment
   - Press `24` - Complete Appointment

5. **Exit**
   - Press `0` - Exit System

## Menu Categories

### Patient Management (1-6)
Quick actions: View patients (2), Search (3), Register new (1)

### Doctor Management (7-14)
Quick actions: View doctors (8), Search by specialty (10), Add new (7)

### Appointment Management (15-25)
Quick actions: Schedule (15), View all (16), View by date (19)

### Queue & Reporting (26-28)
Quick actions: View queue (26), Process next (27), Daily report (28)

## Input Formats

- **Dates**: `2025-10-26` (yyyy-MM-dd)
- **Times**: `14:30` (HH:mm)
- **Phone**: `5551234567` (10-15 digits)
- **Email**: `name@example.com`
- **Gender**: Male, Female, or Other
- **Blood Type**: A+, A-, B+, B-, AB+, AB-, O+, O-

## Tips

1. **Navigation**: Just enter the number of the menu option
2. **Optional Fields**: Press ENTER to skip optional fields
3. **Confirmations**: Type `yes` or `no` when prompted
4. **Errors**: If you make a mistake, the system will guide you
5. **Undo**: Appointment actions can be undone (tracked in status)

## Common Workflows

### Register Patient → Schedule Appointment
```
1 → [patient details] → 15 → [use new patient ID] → [schedule details]
```

### View Patient History
```
4 → [patient ID] → See patient details + appointment history
```

### Daily Operations
```
26 → View queue → 27 → Process each → 24 → Complete with notes
```

### Generate Report
```
28 → [date] → View statistics and schedule
```

## Need Help?

- See `README.md` for complete documentation
- See `TESTING.md` for detailed test scenarios
- All menu options include helpful prompts
- Error messages guide you to correct input

## Example Session

```
Starting Clinic Appointment System...

╔═══════════════════════════════════════════════════════════════╗
║         CLINIC APPOINTMENT MANAGEMENT SYSTEM                  ║
╚═══════════════════════════════════════════════════════════════╝

Press ENTER to continue...

[MAIN MENU displays with 28 options]

Enter your choice: 2

[Patient table displays with 3 patients]

Press ENTER to continue...

[Back to main menu]

Enter your choice: 15

[Schedule new appointment workflow]

✓ SUCCESS: Appointment scheduled successfully!

[Appointment details display]

Press ENTER to continue...

Enter your choice: 0

Are you sure you want to exit? (yes/no): yes

✓ Thank you for using the Clinic Appointment System. Goodbye!
```

Happy Managing! 🏥
