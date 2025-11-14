package com.clinicapp.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * DatabaseConnection - Manages MySQL database connections.
 * Provides singleton pattern for connection management and database initialization.
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    private Properties properties;
    
    private DatabaseConnection() {
        loadProperties();
        loadDriver();
        initializeDatabase();
    }
    
    /**
     * Get singleton instance of DatabaseConnection.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Load database properties from file.
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            // Try to load from file
            InputStream input = new FileInputStream("database.properties");
            properties.load(input);
            input.close();
        } catch (IOException e) {
            // Use default properties if file not found
            System.out.println("Using default database configuration...");
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/clinic_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            properties.setProperty("db.username", "root");
            properties.setProperty("db.password", "");
            properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        }
    }
    
    /**
     * Load MySQL JDBC driver.
     */
    private void loadDriver() {
        try {
            Class.forName(properties.getProperty("db.driver"));
            System.out.println("✓ MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            System.err.println("Please download mysql-connector-java JAR and add to classpath");
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize database - create database and tables if they don't exist.
     */
    private void initializeDatabase() {
        try {
            // First connect to MySQL without specifying database
            String baseUrl = properties.getProperty("db.url").split("\\?")[0];
            String serverUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
            
            Connection tempConn = DriverManager.getConnection(
                serverUrl + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
            
            Statement stmt = tempConn.createStatement();
            
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS clinic_db");
            System.out.println("✓ Database checked/created");
            
            stmt.close();
            tempConn.close();
            
            // Now connect to the clinic_db database
            connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
            
            // Create tables
            createTables();
            
            System.out.println("✓ Database connection established");
            
        } catch (SQLException e) {
            System.err.println("✗ Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create database tables if they don't exist.
     */
    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        
        // Create patients table
        String createPatients = "CREATE TABLE IF NOT EXISTS patients (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(255) NOT NULL," +
            "date_of_birth DATE NOT NULL," +
            "gender VARCHAR(20) NOT NULL," +
            "phone VARCHAR(20) NOT NULL," +
            "email VARCHAR(255)," +
            "address TEXT," +
            "blood_type VARCHAR(10)," +
            "allergies TEXT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
        stmt.executeUpdate(createPatients);
        
        // Create doctors table
        String createDoctors = "CREATE TABLE IF NOT EXISTS doctors (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(255) NOT NULL," +
            "specialization VARCHAR(255) NOT NULL," +
            "phone VARCHAR(20) NOT NULL," +
            "email VARCHAR(255)," +
            "office_location VARCHAR(255)," +
            "schedule TEXT," +
            "is_available BOOLEAN DEFAULT TRUE," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
        stmt.executeUpdate(createDoctors);
        
        // Create appointments table
        String createAppointments = "CREATE TABLE IF NOT EXISTS appointments (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "patient_id INT NOT NULL," +
            "doctor_id INT NOT NULL," +
            "appointment_date DATE NOT NULL," +
            "appointment_time TIME NOT NULL," +
            "reason TEXT," +
            "status VARCHAR(50) NOT NULL," +
            "notes TEXT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE," +
            "FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE" +
            ")";
        stmt.executeUpdate(createAppointments);
        
        stmt.close();
        System.out.println("✓ Database tables checked/created");
    }
    
    /**
     * Get active database connection.
     */
    public Connection getConnection() {
        try {
            // Check if connection is still valid, reconnect if needed
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Failed to get connection: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Close database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
