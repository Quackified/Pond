#!/bin/bash
# Script to run the Clinic Appointment System

# Change to project directory
cd "$(dirname "$0")"

# Check if running GUI or console version
MODE="${1:-console}"

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Bin directory not found. Please run ./compile.sh first"
    exit 1
fi

# Find MySQL connector JAR
MYSQL_JAR=""
if [ -f lib/mysql-connector-java-8.0.33.jar ]; then
    MYSQL_JAR="lib/mysql-connector-java-8.0.33.jar"
elif [ -f lib/mysql-connector-java.jar ]; then
    MYSQL_JAR="lib/mysql-connector-java.jar"
elif ls lib/mysql-connector-*.jar 1> /dev/null 2>&1; then
    MYSQL_JAR=$(ls lib/mysql-connector-*.jar | head -1)
fi

if [ "$MODE" = "gui" ]; then
    # Run GUI version
    if [ -z "$MYSQL_JAR" ]; then
        echo "ERROR: MySQL Connector JAR not found in lib/"
        echo "Please download MySQL Connector/J from:"
        echo "https://dev.mysql.com/downloads/connector/j/"
        echo "and place it in the lib/ directory"
        exit 1
    fi
    
    echo "Starting Clinic Appointment System (GUI)..."
    echo "Using MySQL Connector: $MYSQL_JAR"
    java -cp "bin:$MYSQL_JAR" com.clinicapp.ClinicAppointmentSystemGUI
else
    # Run console version
    echo "Starting Clinic Appointment System (Console)..."
    java -cp bin com.clinicapp.ClinicAppointmentSystem
fi
