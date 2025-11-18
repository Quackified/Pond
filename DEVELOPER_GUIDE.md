# Developer Guide

## Project Structure

### Package Organization

```
com.clinicapp
├── model/          - Data models (Patient, Doctor, Appointment)
├── service/        - Business logic (Managers)
├── storage/        - Data persistence (JSON, CSV)
├── gui/            - User interface (Swing panels)
├── util/           - Helper utilities (Validation)
└── ClinicManagementGUI.java - Main entry point
```

## Key Design Principles

### 1. Beginner-Friendly
- No advanced Java features (streams used minimally)
- Simple, straightforward code
- Clear method names
- Minimal abstraction

### 2. Separation of Concerns
- **Models**: Data structures only
- **Services**: Business logic and data management
- **Storage**: File I/O operations
- **GUI**: User interface components
- **Utilities**: Helper functions

### 3. Data Flow
```
User Input (GUI) 
    → Validation (InputValidator)
    → Business Logic (Manager)
    → Data Storage (JsonStorage)
```

## Adding New Features

### Adding a New Field to Patient

1. **Update Model** (`Patient.java`):
   ```java
   private String newField;
   
   public String getNewField() { return newField; }
   public void setNewField(String newField) { this.newField = newField; }
   ```

2. **Update Manager** (`PatientManager.java`):
   - Add parameter to `addPatient()` method
   - Add parameter to `updatePatient()` method

3. **Update Storage** (`JsonStorage.java`):
   - Add field to JSON writing in `savePatients()`
   - Add field extraction in `loadPatients()`

4. **Update GUI** (`PatientPanel.java`):
   - Add field to add/edit dialogs
   - Add column to table if needed

5. **Update CSV Export** (`CsvExporter.java`):
   - Add column to CSV header
   - Add field to data row

### Adding a New Panel

1. Create new panel class in `gui/` package
2. Extend `JPanel`
3. Add buttons and table for CRUD operations
4. Add to `MainWindow.java` as a new tab

## JSON Format

### Patient JSON Structure
```json
{
  "id": 1,
  "name": "John Doe",
  "dateOfBirth": "1990-01-15",
  "gender": "Male",
  "phoneNumber": "1234567890",
  "email": "john@example.com",
  "address": "123 Main St",
  "bloodType": "O+",
  "allergies": "None"
}
```

### Doctor JSON Structure
```json
{
  "id": 1,
  "name": "Dr. Smith",
  "specialization": "Cardiology",
  "phoneNumber": "9876543210",
  "email": "smith@clinic.com",
  "availableDays": "Monday,Wednesday,Friday",
  "startTime": "09:00",
  "endTime": "17:00",
  "isAvailable": true
}
```

### Appointment JSON Structure
```json
{
  "id": 1,
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2024-12-25T14:30",
  "reason": "Regular checkup",
  "status": "SCHEDULED",
  "notes": ""
}
```

## Common Tasks

### Adding Validation

Add method to `InputValidator.java`:
```java
public static boolean isValidCustomField(String value) {
    // validation logic
    return true/false;
}
```

### Creating a Dialog

```java
JTextField field = new JTextField();
JPanel panel = new JPanel(new GridLayout(2, 2));
panel.add(new JLabel("Label:"));
panel.add(field);

int result = JOptionPane.showConfirmDialog(
    this, panel, "Title", JOptionPane.OK_CANCEL_OPTION
);

if (result == JOptionPane.OK_OPTION) {
    // process input
}
```

### Refreshing Table Data

```java
tableModel.setRowCount(0); // Clear table
List<Patient> patients = patientManager.getAllPatients();
for (Patient p : patients) {
    Object[] row = {p.getId(), p.getName(), ...};
    tableModel.addRow(row);
}
```

## Testing Checklist

Before committing changes:

1. **Compilation**: `./compile.sh` succeeds
2. **Add Operations**: Can add new records
3. **Edit Operations**: Can modify existing records
4. **Delete Operations**: Can remove records
5. **Data Persistence**: Data saves and loads correctly
6. **CSV Export**: Export functionality works
7. **Input Validation**: Invalid input is rejected with clear messages
8. **UI Responsiveness**: No freezing or errors in GUI

## Debugging Tips

### Data Not Saving
- Check `data/` directory exists
- Check file write permissions
- Look for exceptions in console

### Reflection Errors (Java 9+)
If you get reflection errors, you may need to:
- Use Java 8
- Or modify the setId methods to use a different approach

### GUI Not Displaying
- Check all GUI components are added to panels
- Verify layout managers are set correctly
- Call `setVisible(true)` on main window

## Code Style Guidelines

### Naming Conventions
- Classes: PascalCase (e.g., `PatientManager`)
- Methods: camelCase (e.g., `addPatient`)
- Variables: camelCase (e.g., `patientId`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_VALUE`)

### Method Organization
1. Constructor
2. Public methods
3. Private methods
4. Getters/Setters (if not at top)

### Comments
- Avoid obvious comments
- Use comments for complex logic only
- Keep code self-documenting with clear names

## Dependencies

This project uses only standard Java libraries:
- `java.util.*` - Collections, data structures
- `java.time.*` - Date and time handling
- `java.io.*` - File I/O
- `javax.swing.*` - GUI components
- `java.awt.*` - Layout and event handling
- `java.lang.reflect.*` - Reflection for ID setting

No external JARs or libraries required.

## Building and Distribution

### Compilation
```bash
./compile.sh
```

### Creating Executable JAR (Optional)
```bash
jar cvfe ClinicApp.jar com.clinicapp.ClinicManagementGUI -C bin .
```

### Running JAR
```bash
java -jar ClinicApp.jar
```

## Performance Considerations

- Current design suitable for small clinics (hundreds of records)
- For large datasets, consider:
  - Adding database backend
  - Implementing pagination in tables
  - Lazy loading of data

## Security Notes

- No authentication/authorization implemented
- JSON files stored in plain text
- Suitable for single-user desktop application
- Not suitable for multi-user or network environments

## Future Improvements

Ideas for enhancement:
- Add search functionality
- Implement sorting in tables
- Add date picker widgets
- Implement print functionality
- Add appointment reminders
- Support multiple clinics
- Add user roles and permissions
- Implement backup/restore
- Add appointment history tracking
- Generate reports (PDF)

## Getting Help

- Check README.md for user documentation
- Check QUICKSTART.md for quick start guide
- Check CHANGES.md for version changes
- Review code comments in source files
