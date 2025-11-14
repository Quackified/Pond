package com.clinicapp;

import com.clinicapp.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.charset.StandardCharsets;

/**
 * Main entry point for the Clinic Appointment System GUI application.
 * Integrates JSwing with MySQL using proper UTF-8 encoding.
 */
public class ClinicAppointmentSystemGUI {
    
    public static void main(String[] args) {
        // Set default charset to UTF-8 to prevent encoding issues
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("client.encoding.override", "UTF-8");
        
        // Ensure console output uses UTF-8
        System.out.println("Starting Clinic Appointment System GUI...");
        System.out.println("UTF-8 Encoding: " + StandardCharsets.UTF_8.displayName());
        
        // Set Look and Feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                System.out.println("GUI initialized successfully with UTF-8 support");
            } catch (Exception e) {
                System.err.println("Error initializing GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
