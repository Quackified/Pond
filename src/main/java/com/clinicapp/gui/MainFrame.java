package com.clinicapp.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.clinicapp.db.DatabaseConnection;

/**
 * MainFrame - Main window of the Clinic Management System GUI.
 * Provides navigation to all major features.
 */
public class MainFrame extends JFrame {
    
    private JPanel mainPanel;
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private AppointmentPanel appointmentPanel;
    
    public MainFrame() {
        initializeFrame();
        initializePanels();
        initializeMenuBar();
        showWelcome();
    }
    
    /**
     * Initialize the main frame properties.
     */
    private void initializeFrame() {
        setTitle("Clinic Appointment Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
    }
    
    /**
     * Initialize all panels.
     */
    private void initializePanels() {
        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        appointmentPanel = new AppointmentPanel();
    }
    
    /**
     * Initialize the menu bar.
     */
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exitApplication());
        fileMenu.add(exitItem);
        
        JMenu patientMenu = new JMenu("Patients");
        JMenuItem viewPatientsItem = new JMenuItem("View All Patients");
        viewPatientsItem.addActionListener(e -> showPanel(patientPanel, "Patient Management"));
        patientMenu.add(viewPatientsItem);
        
        JMenu doctorMenu = new JMenu("Doctors");
        JMenuItem viewDoctorsItem = new JMenuItem("View All Doctors");
        viewDoctorsItem.addActionListener(e -> showPanel(doctorPanel, "Doctor Management"));
        doctorMenu.add(viewDoctorsItem);
        
        JMenu appointmentMenu = new JMenu("Appointments");
        JMenuItem viewAppointmentsItem = new JMenuItem("View All Appointments");
        viewAppointmentsItem.addActionListener(e -> showPanel(appointmentPanel, "Appointment Management"));
        appointmentMenu.add(viewAppointmentsItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(patientMenu);
        menuBar.add(doctorMenu);
        menuBar.add(appointmentMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Show welcome panel.
     */
    private void showWelcome() {
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        
        JLabel titleLabel = new JLabel("Clinic Appointment Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        welcomePanel.add(titleLabel, gbc);
        
        gbc.gridy++;
        JLabel subtitleLabel = new JLabel("Integrated with MySQL Database");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        welcomePanel.add(subtitleLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(40, 20, 10, 20);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton patientBtn = createStyledButton("Manage Patients", new Color(46, 125, 50));
        patientBtn.addActionListener(e -> showPanel(patientPanel, "Patient Management"));
        
        JButton doctorBtn = createStyledButton("Manage Doctors", new Color(25, 118, 210));
        doctorBtn.addActionListener(e -> showPanel(doctorPanel, "Doctor Management"));
        
        JButton appointmentBtn = createStyledButton("Manage Appointments", new Color(211, 47, 47));
        appointmentBtn.addActionListener(e -> showPanel(appointmentPanel, "Appointment Management"));
        
        buttonPanel.add(patientBtn);
        buttonPanel.add(doctorBtn);
        buttonPanel.add(appointmentBtn);
        
        welcomePanel.add(buttonPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(30, 20, 20, 20);
        JLabel infoLabel = new JLabel("<html><center>Use the menu bar or buttons above to navigate<br>All data is stored in MySQL database</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(102, 102, 102));
        welcomePanel.add(infoLabel, gbc);
        
        mainPanel.removeAll();
        mainPanel.add(welcomePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    /**
     * Create a styled button.
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    /**
     * Show a specific panel in the main area.
     */
    private void showPanel(JPanel panel, String title) {
        mainPanel.removeAll();
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        JButton homeBtn = new JButton("Home");
        homeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        homeBtn.setBackground(Color.WHITE);
        homeBtn.setForeground(new Color(0, 102, 204));
        homeBtn.setFocusPainted(false);
        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.addActionListener(e -> showWelcome());
        homeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.add(homeBtn, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        
        if (panel == patientPanel) {
            patientPanel.refreshTable();
        } else if (panel == doctorPanel) {
            doctorPanel.refreshTable();
        } else if (panel == appointmentPanel) {
            appointmentPanel.refreshTable();
        }
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    /**
     * Show about dialog.
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Clinic Appointment Management System\n" +
            "Version 2.0\n\n" +
            "Integrated with:\n" +
            "- Java Swing GUI (JForm)\n" +
            "- MySQL Database (MySQL Connector)\n\n" +
            "© 2024 Clinic Management Team",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exit the application.
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseConnection.getInstance().closeConnection();
            System.exit(0);
        }
    }
    
    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
