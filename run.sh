#!/bin/bash

cd "$(dirname "$0")"

if [ ! -d "bin" ]; then
    echo "Bin directory not found. Compiling..."
    mkdir -p bin
    javac -d bin src/main/java/com/clinicapp/model/*.java \
                 src/main/java/com/clinicapp/service/*.java \
                 src/main/java/com/clinicapp/util/*.java \
                 src/main/java/com/clinicapp/storage/*.java \
                 src/main/java/com/clinicapp/gui/*.java \
                 src/main/java/com/clinicapp/*.java
    
    if [ $? -ne 0 ]; then
        echo "Compilation failed!"
        exit 1
    fi
    echo "Compilation successful!"
fi

echo "Starting Clinic Appointment Management System..."
java -cp bin com.clinicapp.ClinicManagementGUI
