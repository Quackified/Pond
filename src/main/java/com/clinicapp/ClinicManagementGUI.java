package com.clinicapp;

import com.clinicapp.gui.MainWindow;
import javax.swing.*;

public class ClinicManagementGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.out.println("Could not set system look and feel: " + e.getMessage());
                }
                
                MainWindow window = new MainWindow();
                window.setVisible(true);
            }
        });
    }
}
