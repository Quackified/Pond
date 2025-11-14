#!/bin/bash
# Script to compile the Clinic Appointment System
# Supports both console and GUI versions with MySQL integration

# Change to project directory
cd "$(dirname "$0")"

echo "Compiling Clinic Appointment System..."
echo "======================================="

# Create bin directory if it doesn't exist
mkdir -p bin

# Check for MySQL Connector JAR
MYSQL_JAR=""
if [ -d "lib" ]; then
    MYSQL_JAR=$(find lib -name "mysql-connector*.jar" 2>/dev/null | head -n 1)
fi

if [ -n "$MYSQL_JAR" ]; then
    echo "Found MySQL Connector: $MYSQL_JAR"
    echo "Compiling with GUI and database support..."
    CLASSPATH="$MYSQL_JAR"
    
    # Compile all Java files including GUI and database layers
    javac -encoding UTF-8 -cp "$CLASSPATH" -d bin \
        src/main/java/com/clinicapp/model/*.java \
        src/main/java/com/clinicapp/service/*.java \
        src/main/java/com/clinicapp/util/*.java \
        src/main/java/com/clinicapp/ui/*.java \
        src/main/java/com/clinicapp/db/*.java \
        src/main/java/com/clinicapp/dao/*.java \
        src/main/java/com/clinicapp/gui/*.java \
        src/main/java/com/clinicapp/*.java
else
    echo "MySQL Connector not found in lib/ directory"
    echo "Compiling console version only..."
    echo ""
    echo "To enable GUI with database:"
    echo "  1. Download mysql-connector-j-8.0.33.jar"
    echo "  2. Place it in the lib/ directory"
    echo "  3. Run compile.sh again"
    echo ""
    
    # Compile only console version files
    javac -encoding UTF-8 -d bin \
        src/main/java/com/clinicapp/model/*.java \
        src/main/java/com/clinicapp/service/*.java \
        src/main/java/com/clinicapp/util/*.java \
        src/main/java/com/clinicapp/ui/*.java \
        src/main/java/com/clinicapp/ClinicAppointmentSystem.java
fi

# Check compilation result
if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Compilation successful!"
    echo ""
    echo "Class files created:"
    find bin -name "*.class" | wc -l
    echo ""
    echo "To run the application:"
    echo "  Console version: ./run.sh"
    if [ -n "$MYSQL_JAR" ]; then
        echo "  GUI version:     ./run.sh gui"
    fi
else
    echo ""
    echo "✗ Compilation failed!"
    exit 1
fi
