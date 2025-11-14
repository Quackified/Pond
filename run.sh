#!/bin/bash
# Script to run the Clinic Appointment System
# Supports both console and GUI versions

# Change to project directory
cd "$(dirname "$0")"

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Bin directory not found. Please run ./compile.sh first"
    exit 1
fi

# Check for MySQL Connector JAR
MYSQL_JAR=""
if [ -d "lib" ]; then
    MYSQL_JAR=$(find lib -name "mysql-connector*.jar" 2>/dev/null | head -n 1)
fi

# Set UTF-8 encoding
export JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

# Determine which version to run
if [ "$1" == "gui" ]; then
    if [ -z "$MYSQL_JAR" ]; then
        echo "Error: MySQL Connector JAR not found in lib/ directory"
        echo "GUI version requires MySQL connector"
        echo ""
        echo "To use GUI version:"
        echo "  1. Download mysql-connector-j-8.0.33.jar"
        echo "  2. Place it in the lib/ directory"
        echo "  3. Run ./compile.sh"
        echo "  4. Run ./run.sh gui"
        exit 1
    fi
    
    echo "Starting Clinic Appointment System GUI..."
    echo "UTF-8 Encoding: Enabled"
    echo "Database: MySQL with UTF-8"
    echo ""
    
    # Run GUI version with MySQL connector in classpath
    java -Dfile.encoding=UTF-8 -cp "bin:$MYSQL_JAR" com.clinicapp.ClinicAppointmentSystemGUI
else
    echo "Starting Clinic Appointment System (Console)..."
    echo ""
    
    # Run console version
    java -Dfile.encoding=UTF-8 -cp bin com.clinicapp.ClinicAppointmentSystem
fi
