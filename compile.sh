#!/bin/bash
# Script to compile the Clinic Appointment System

# Change to project directory
cd "$(dirname "$0")"

echo "Compiling Clinic Appointment System..."
echo "======================================="

# Create bin directory if it doesn't exist
mkdir -p bin

# Set classpath for libraries
CLASSPATH="lib/*"

# Compile all Java files
javac -cp "$CLASSPATH" -d bin \
    src/main/java/com/clinicapp/model/*.java \
    src/main/java/com/clinicapp/service/*.java \
    src/main/java/com/clinicapp/util/*.java \
    src/main/java/com/clinicapp/io/*.java \
    src/main/java/com/clinicapp/gui/*.java \
    src/main/java/com/clinicapp/*.java

# Check compilation result
if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Compilation successful!"
    echo ""
    echo "Class files created:"
    find bin -name "*.class" | wc -l
    echo ""
    echo "To run the application, use:"
    echo "  java -cp bin:lib/* com.clinicapp.ClinicManagementGUI"
    echo "or:"
    echo "  ./run.sh"
else
    echo ""
    echo "✗ Compilation failed!"
    exit 1
fi
