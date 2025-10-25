#!/bin/bash
# Script to run the Clinic Appointment System

# Change to project directory
cd "$(dirname "$0")"

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Bin directory not found. Compiling..."
    mkdir -p bin
    javac -d bin src/main/java/clinic/model/*.java \
                 src/main/java/clinic/manager/*.java \
                 src/main/java/clinic/util/*.java \
                 src/main/java/clinic/ui/*.java \
                 src/main/java/clinic/*.java
    
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
    echo "Compilation successful!"
fi

# Run the application
echo "Starting Clinic Appointment System..."
java -cp bin clinic.ClinicAppointmentSystem
