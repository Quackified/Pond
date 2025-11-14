#!/bin/bash
# Script to compile the Clinic Appointment System

# Change to project directory
cd "$(dirname "$0")"

echo "Compiling Clinic Appointment System..."
echo "======================================="

# Create bin directory if it doesn't exist
mkdir -p bin
mkdir -p lib

# Download MySQL Connector if not present
if [ ! -f lib/mysql-connector-java-8.0.33.jar ]; then
    echo "MySQL Connector not found. Attempting to download..."
    echo "Note: You may need to download manually from:"
    echo "https://dev.mysql.com/downloads/connector/j/"
    echo ""
fi

# Check if MySQL connector exists
MYSQL_JAR=""
if [ -f lib/mysql-connector-java-8.0.33.jar ]; then
    MYSQL_JAR="lib/mysql-connector-java-8.0.33.jar"
elif [ -f lib/mysql-connector-java.jar ]; then
    MYSQL_JAR="lib/mysql-connector-java.jar"
elif ls lib/mysql-connector-*.jar 1> /dev/null 2>&1; then
    MYSQL_JAR=$(ls lib/mysql-connector-*.jar | head -1)
fi

# Set classpath
if [ -n "$MYSQL_JAR" ]; then
    CLASSPATH=".:$MYSQL_JAR"
    echo "Using MySQL Connector: $MYSQL_JAR"
else
    CLASSPATH="."
    echo "Warning: MySQL Connector JAR not found in lib/"
    echo "GUI version will not work without MySQL Connector"
    echo "Console version will still compile and run"
fi

# Compile all Java files
javac -cp "$CLASSPATH" -d bin \
    src/main/java/com/clinicapp/model/*.java \
    src/main/java/com/clinicapp/service/*.java \
    src/main/java/com/clinicapp/util/*.java \
    src/main/java/com/clinicapp/ui/*.java \
    src/main/java/com/clinicapp/db/*.java \
    src/main/java/com/clinicapp/dao/*.java \
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
    echo "To run the console version:"
    echo "  java -cp bin com.clinicapp.ClinicAppointmentSystem"
    echo ""
    echo "To run the GUI version (requires MySQL):"
    echo "  java -cp \"bin:$MYSQL_JAR\" com.clinicapp.ClinicAppointmentSystemGUI"
    echo ""
    echo "or use:"
    echo "  ./run.sh          (console version)"
    echo "  ./run.sh gui      (GUI version)"
else
    echo ""
    echo "✗ Compilation failed!"
    exit 1
fi
