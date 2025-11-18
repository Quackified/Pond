package com.clinicapp;

import com.clinicapp.gui.MainWindow;

import javax.swing.SwingUtilities;

public class ClinicManagementGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}
