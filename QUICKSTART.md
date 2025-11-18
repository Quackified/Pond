# Quick Start Guide

## Getting Started in 3 Steps

### 1. Compile the Application

```bash
./compile.sh
```

### 2. Run the Application

```bash
./run.sh
```

### 3. Start Using

The application window will open with three tabs:
- **Patients**: Manage patient records
- **Doctors**: Manage doctor information
- **Appointments**: Schedule and manage appointments

## First Time Setup

1. **Add a Doctor First**
   - Go to the Doctors tab
   - Click "Add Doctor"
   - Fill in the doctor's information
   - Select available days
   - Click OK

2. **Add a Patient**
   - Go to the Patients tab
   - Click "Add Patient"
   - Fill in patient information
   - Click OK

3. **Schedule an Appointment**
   - Go to the Appointments tab
   - Click "Add Appointment"
   - Select a patient and doctor
   - Choose date and time
   - Enter the reason for visit
   - Click OK

## Saving Your Data

Data is automatically saved when you close the application. You can also:
- Use File → Save to manually save
- Use File → Load to reload data

## Exporting Data

To export data to CSV:
1. Click Export menu
2. Choose what to export (Patients, Doctors, or Appointments)
3. Enter a filename
4. Files are saved in the `exports/` folder

## Keyboard Shortcuts

- Click rows in tables to select items
- Double-click to view details (in some dialogs)

## Tips

- **Valid Date Format**: yyyy-MM-dd (e.g., 2024-12-25)
- **Valid Time Format**: HH:mm (e.g., 14:30)
- **Phone Numbers**: 10-15 digits (can start with +)
- **Email**: Standard email format required
- **Blood Types**: A+, A-, B+, B-, AB+, AB-, O+, O-

## Troubleshooting

**Application won't start?**
- Make sure Java 8 or higher is installed
- Run `./compile.sh` first

**Data not saving?**
- Check that the application has write permissions
- Look for a `data/` directory in the project folder

**Can't schedule appointment?**
- Make sure both patient and doctor exist
- Check for time conflicts (doctors can't have appointments within 30 minutes of each other)

## File Locations

- **Application Data**: `data/` directory
  - `patients.json`
  - `doctors.json`
  - `appointments.json`

- **CSV Exports**: `exports/` directory

## Need Help?

Check the full README.md for detailed information about all features.
