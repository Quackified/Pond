#!/bin/bash
# Script to run the Clinic Appointment System

# Change to project directory
cd "$(dirname "$0")"

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Bin directory not found. Compiling..."
    mkdir -p bin
    javac -d bin src/main/java/com/clinicapp/model/*.java \
                 src/main/java/com/clinicapp/service/*.java \
                 src/main/java/com/clinicapp/util/*.java \
                 src/main/java/com/clinicapp/ui/*.java \
                 src/main/java/com/clinicapp/*.java
    
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
    echo "Compilation successful!"
fi

# Run the application
echo "Starting Clinic Appointment System..."
java -cp bin com.clinicapp.ClinicAppointmentSystem
