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
