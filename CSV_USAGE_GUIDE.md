# CSV Import/Export Usage Guide

## Quick Start

### Exporting Data

1. **Launch the Application**
   ```bash
   ./run.sh
   ```

2. **Navigate to the desired tab** (Patients, Doctors, or Appointments)

3. **Click "Export to CSV"** button

4. **Success message** will show the file path, e.g.:
   ```
   Patients exported successfully!
   File: exports/patients_20241118_1700123456789.csv
   ```

5. **Open the file** in Excel, Google Sheets, or any spreadsheet application

### Importing Data

1. **Prepare your CSV file** matching the format (see templates below)

2. **Launch the Application**
   ```bash
   ./run.sh
   ```

3. **Navigate to the desired tab** (Patients, Doctors, or Appointments)

4. **Click "Import from CSV"** button

5. **Select your CSV file** in the file chooser dialog

6. **Review the results**:
   - Success count: Number of records imported
   - Error count: Number of failed records
   - Error details: Specific error messages for failed records

## CSV Templates

### Patient CSV Template
```csv
ID,Name,Date of Birth,Age,Gender,Phone Number,Email,Address,Blood Type,Allergies
1,John Doe,1990-05-15,34,Male,1234567890,john@email.com,123 Main St,A+,None
2,Jane Smith,1985-08-20,39,Female,0987654321,jane@email.com,456 Oak Ave,B-,Penicillin
```

**Notes:**
- ID column is auto-generated during import (can be left as sequential numbers)
- Age is calculated from Date of Birth
- Email, Blood Type, and Allergies are optional

### Doctor CSV Template
```csv
ID,Name,Specialization,Phone Number,Email,Available Days,Start Time,End Time,Available
1,Dr. Smith,Cardiology,9876543210,smith@clinic.com,Monday;Wednesday;Friday,09:00,17:00,true
2,Dr. Johnson,Pediatrics,5551234567,johnson@clinic.com,Tuesday;Thursday,08:00,16:00,true
```

**Notes:**
- ID column is auto-generated during import
- Available Days should be semicolon-separated (e.g., "Monday;Wednesday;Friday")
- Email, Available Days, Start Time, and End Time are optional
- Available should be "true" or "false"

### Appointment CSV Template
```csv
ID,Date,Start Time,End Time,Patient ID,Patient Name,Doctor ID,Doctor Name,Reason,Status,Notes,Created At
1,2024-11-20,09:00,09:30,1,John Doe,1,Dr. Smith,Annual Checkup,SCHEDULED,,2024-11-18 10:30:00
2,2024-11-20,10:00,10:30,2,Jane Smith,2,Dr. Johnson,Consultation,CONFIRMED,Follow-up needed,2024-11-18 11:00:00
```

**Notes:**
- ID column is auto-generated during import
- Patient Name and Doctor Name are for reference only (Patient ID and Doctor ID are used)
- Patient and Doctor must exist before importing appointments
- Status values: SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
- Notes and Created At are optional
- Time format is 24-hour (HH:mm)

## Date and Time Formats

- **Date**: `yyyy-MM-dd` (e.g., 2024-11-18)
- **Time**: `HH:mm` (e.g., 14:30 for 2:30 PM)
- **Timestamp**: `yyyy-MM-dd HH:mm:ss` (e.g., 2024-11-18 14:30:00)

## Common Scenarios

### Backup All Data
1. Go to Patients tab → Export to CSV
2. Go to Doctors tab → Export to CSV
3. Go to Appointments tab → Export to CSV
4. All files are saved in the `exports/` directory

### Bulk Import Patients
1. Create a CSV file with patient data
2. Ensure all required fields are present
3. Use the Patient CSV template format
4. Click "Import from CSV" in Patients tab
5. Select your file
6. Review import results

### Transfer Data Between Systems
1. Export from source system
2. Transfer CSV files to target system
3. Import into target system
4. Verify data integrity

### Edit Data in Spreadsheet
1. Export current data to CSV
2. Open in Excel/Google Sheets
3. Make your edits
4. Save as CSV
5. Import back into application

## Tips and Best Practices

✅ **DO:**
- Always export before making major changes (backup)
- Check import results for errors
- Validate CSV format before importing
- Test with a small dataset first
- Keep exported files organized

❌ **DON'T:**
- Modify ID columns (they're auto-generated)
- Use commas in data without quotes
- Change the CSV header row
- Import without patient/doctor data for appointments
- Delete exports/ directory (it's auto-created)

## Troubleshooting

### "Import failed with errors"
**Cause**: Invalid data format or missing required fields

**Solution**: 
1. Check the error details in the import results dialog
2. Verify your CSV matches the template format
3. Ensure all required fields are present
4. Check date/time formats

### "Patient/Doctor not found" (for appointments)
**Cause**: Referenced patient or doctor doesn't exist

**Solution**:
1. Import patients first
2. Import doctors second
3. Then import appointments
4. Ensure Patient ID and Doctor ID match existing records

### "Failed to export"
**Cause**: File write permission or disk space issue

**Solution**:
1. Check if exports/ directory exists
2. Verify disk space available
3. Check file permissions
4. Try running as administrator (if needed)

### "Invalid date format"
**Cause**: Date not in yyyy-MM-dd format

**Solution**:
1. Format dates as yyyy-MM-dd
2. Use leading zeros (e.g., 2024-01-05, not 2024-1-5)
3. Check for extra spaces

### "Invalid time format"
**Cause**: Time not in HH:mm format

**Solution**:
1. Use 24-hour format (e.g., 14:30 not 2:30 PM)
2. Include leading zeros (e.g., 09:00 not 9:00)
3. Check for extra spaces

## File Naming Convention

Exported files follow this naming pattern:
```
{type}_{YYYYMMDD}_{timestamp}.csv
```

Examples:
- `patients_20241118_1700123456789.csv`
- `doctors_20241118_1700234567890.csv`
- `appointments_20241118_1700345678901.csv`

## Spreadsheet Application Compatibility

### Microsoft Excel
- ✅ Full compatibility
- Opens CSV files directly
- Preserves formatting

### Google Sheets
- ✅ Full compatibility
- Import via File → Import → Upload
- Preserves data types

### LibreOffice Calc
- ✅ Full compatibility
- Choose UTF-8 encoding when opening
- Comma as delimiter

### Apple Numbers
- ✅ Full compatibility
- Import CSV via File → Open
- Automatic delimiter detection

## Advanced Features

### Filtering Before Export
Currently, export includes all records. To export specific records:
1. Export all data
2. Open in spreadsheet application
3. Filter/sort as needed
4. Save as new CSV

### Merging CSV Files
To combine multiple CSV files:
1. Open first file in spreadsheet
2. Copy data from additional files
3. Paste below existing data
4. Ensure headers match
5. Save as single CSV

### Data Validation
Import automatically validates:
- Email format
- Phone number format
- Date/time formats
- Blood type values
- Required fields presence

## Security and Privacy

⚠️ **Important**: CSV files contain sensitive patient information
- Store exported files securely
- Delete old exports when no longer needed
- Don't share via unsecured channels
- Consider encryption for sensitive data
- Follow HIPAA/GDPR guidelines if applicable

## Command Line Usage

For automation or testing, you can use the test script:
```bash
./test_csv.sh
```

This demonstrates programmatic CSV export functionality.

## Support

For issues or questions:
1. Check this guide first
2. Review OPENCSV_INTEGRATION.md for technical details
3. Check error messages in import results
4. Verify CSV format against templates
