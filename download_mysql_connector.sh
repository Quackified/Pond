#!/bin/bash
# Script to download MySQL Connector/J

echo "MySQL Connector/J Download Helper"
echo "=================================="
echo ""

# Create lib directory
mkdir -p lib

echo "Note: MySQL Connector/J requires manual download due to Oracle license terms."
echo ""
echo "Please follow these steps:"
echo ""
echo "1. Visit: https://dev.mysql.com/downloads/connector/j/"
echo "2. Select 'Platform Independent' version"
echo "3. Download the ZIP or TAR.GZ file"
echo "4. Extract the archive"
echo "5. Copy the JAR file (mysql-connector-java-X.X.XX.jar) to the lib/ directory"
echo ""
echo "Alternative: If you have Maven installed, you can copy from local repository:"
echo "  ~/.m2/repository/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar"
echo ""

# Check if already exists
if ls lib/mysql-connector-*.jar 1> /dev/null 2>&1; then
    echo "✓ MySQL Connector JAR already exists in lib/:"
    ls lib/mysql-connector-*.jar
else
    echo "✗ MySQL Connector JAR not found in lib/"
    echo ""
    echo "Once downloaded, place it in: $(pwd)/lib/"
fi
