#!/bin/bash
# Simple test script to verify CSV export functionality

cd "$(dirname "$0")"

# Create a simple test program
cat > TestCsv.java << 'EOF'
import com.clinicapp.io.CsvExporter;
import com.clinicapp.model.Patient;
import com.clinicapp.service.PatientManager;

import java.time.LocalDate;
import java.util.List;

public class TestCsv {
    public static void main(String[] args) {
        try {
            PatientManager pm = new PatientManager();
            
            // Add test patients
            pm.addPatient("John Doe", LocalDate.of(1990, 5, 15), "Male", 
                         "1234567890", "john@test.com", "123 Main St", "A+", "None");
            pm.addPatient("Jane Smith", LocalDate.of(1985, 8, 20), "Female", 
                         "0987654321", "jane@test.com", "456 Oak Ave", "B-", "Penicillin");
            
            // Export to CSV
            List<Patient> patients = pm.getAllPatients();
            String fileName = CsvExporter.exportPatients(patients);
            
            System.out.println("✓ CSV Export Successful!");
            System.out.println("File created: " + fileName);
            System.out.println("Exported " + patients.size() + " patients");
            
            // Check if file exists
            java.io.File file = new java.io.File(fileName);
            if (file.exists()) {
                System.out.println("✓ File exists and is " + file.length() + " bytes");
            } else {
                System.out.println("✗ File not found!");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
EOF

# Compile the test
echo "Compiling test..."
javac -cp "bin:lib/*" -d . TestCsv.java

# Run the test
echo "Running CSV export test..."
java -cp ".:bin:lib/*" TestCsv

# Cleanup
rm -f TestCsv.java TestCsv.class

echo ""
echo "Test complete!"
