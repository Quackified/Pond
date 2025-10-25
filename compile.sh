#!/bin/bash
# Script to compile the Clinic Appointment System

# Change to project directory
cd "$(dirname "$0")"

echo "Compiling Clinic Appointment System..."
echo "======================================="

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
javac -d bin \
    src/main/java/clinic/model/*.java \
    src/main/java/clinic/manager/*.java \
    src/main/java/clinic/util/*.java \
    src/main/java/clinic/ui/*.java \
    src/main/java/clinic/*.java

# Check compilation result
if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Compilation successful!"
    echo ""
    echo "Class files created:"
    find bin -name "*.class" | wc -l
    echo ""
    echo "To run the application, use:"
    echo "  java -cp bin clinic.ClinicAppointmentSystem"
    echo "or:"
    echo "  ./run.sh"
else
    echo ""
    echo "✗ Compilation failed!"
    exit 1
fi
