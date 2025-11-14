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
 * Singleton database connection manager with UTF-8 encoding support.
 * Ensures all database operations use proper UTF-8 character encoding
 * to prevent formatting and encoding issues.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Properties properties;
    private String jdbcUrl;
    
    private DatabaseConnection() {
        loadProperties();
        initializeDatabase();
    }
    
    /**
     * Gets the singleton instance of DatabaseConnection.
     * 
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Loads database configuration from properties file.
     * Configures UTF-8 encoding parameters.
     */
    private void loadProperties() {
        properties = new Properties();
        
        try (InputStream input = new FileInputStream("database.properties")) {
            properties.load(input);
            
            String baseUrl = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            
            // Build JDBC URL with UTF-8 encoding parameters
            // This is critical to prevent encoding issues with special characters
            StringBuilder urlBuilder = new StringBuilder(baseUrl);
            urlBuilder.append("?useUnicode=").append(properties.getProperty("db.useUnicode", "true"));
            urlBuilder.append("&characterEncoding=").append(properties.getProperty("db.characterEncoding", "UTF-8"));
            urlBuilder.append("&connectionCollation=").append(properties.getProperty("db.connectionCollation", "utf8mb4_unicode_ci"));
            urlBuilder.append("&serverTimezone=").append(properties.getProperty("db.serverTimezone", "UTC"));
            
            // Additional settings to ensure UTF-8 compatibility
            urlBuilder.append("&useServerPrepStmts=true");
            urlBuilder.append("&cachePrepStmts=true");
            urlBuilder.append("&rewriteBatchedStatements=true");
            
            jdbcUrl = urlBuilder.toString();
            
            System.out.println("Database configuration loaded with UTF-8 encoding support");
            
        } catch (IOException e) {
            System.err.println("Warning: Could not load database.properties, using defaults");
            // Fallback to default configuration with UTF-8 encoding
            jdbcUrl = "jdbc:mysql://localhost:3306/clinic_db?useUnicode=true&characterEncoding=UTF-8&connectionCollation=utf8mb4_unicode_ci&serverTimezone=UTC";
            properties.setProperty("db.username", "root");
            properties.setProperty("db.password", "");
        }
    }
    
    /**
     * Initializes the database and creates schema if not exists.
     * All tables are created with UTF8MB4 charset and utf8mb4_unicode_ci collation.
     */
    private void initializeDatabase() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // First, connect without specifying database to create it if needed
            String baseUrl = properties.getProperty("db.url", "jdbc:mysql://localhost:3306/clinic_db");
            String dbName = baseUrl.substring(baseUrl.lastIndexOf('/') + 1);
            if (dbName.contains("?")) {
                dbName = dbName.substring(0, dbName.indexOf('?'));
            }
            
            String serverUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));
            String tempUrl = serverUrl + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
            
            try (Connection tempConn = DriverManager.getConnection(
                    tempUrl,
                    properties.getProperty("db.username", "root"),
                    properties.getProperty("db.password", ""))) {
                
                Statement stmt = tempConn.createStatement();
                
                // Create database with UTF8MB4 charset
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName + 
                                 " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                
                System.out.println("Database '" + dbName + "' ready with UTF8MB4 charset");
            }
            
            // Now connect to the specific database and create tables
            connection = DriverManager.getConnection(
                    jdbcUrl,
                    properties.getProperty("db.username", "root"),
                    properties.getProperty("db.password", ""));
            
            // Set connection charset to UTF8MB4
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci'");
                stmt.execute("SET CHARACTER SET utf8mb4");
            }
            
            createTables();
            
            System.out.println("Connected to database with UTF-8 encoding enabled");
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            System.err.println("Please download mysql-connector-j-8.0.33.jar and place it in the lib/ directory");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates database tables with UTF8MB4 charset and utf8mb4_unicode_ci collation.
     * This ensures proper storage and retrieval of international characters.
     */
    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            // Patients table with UTF8MB4
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS patients (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "date_of_birth DATE NOT NULL," +
                "gender VARCHAR(50)," +
                "phone VARCHAR(20)," +
                "email VARCHAR(255)," +
                "address TEXT," +
                "blood_type VARCHAR(10)," +
                "allergies TEXT," +
                "medical_history TEXT," +
                "emergency_contact VARCHAR(255)," +
                "emergency_phone VARCHAR(20)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );
            
            // Doctors table with UTF8MB4
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS doctors (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "specialization VARCHAR(255) NOT NULL," +
                "phone VARCHAR(20)," +
                "email VARCHAR(255)," +
                "schedule TEXT," +
                "is_available BOOLEAN DEFAULT true" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );
            
            // Appointments table with UTF8MB4
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS appointments (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "patient_id INT NOT NULL," +
                "doctor_id INT NOT NULL," +
                "appointment_time DATETIME NOT NULL," +
                "reason TEXT," +
                "status VARCHAR(50) NOT NULL," +
                "notes TEXT," +
                "FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE," +
                "FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci"
            );
            
            System.out.println("Database tables created/verified with UTF8MB4 charset");
        }
    }
    
    /**
     * Gets the active database connection with UTF-8 encoding.
     * Automatically reconnects if connection is closed.
     * 
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    jdbcUrl,
                    properties.getProperty("db.username", "root"),
                    properties.getProperty("db.password", ""));
            
            // Ensure UTF8MB4 charset for new connection
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci'");
                stmt.execute("SET CHARACTER SET utf8mb4");
            }
        }
        return connection;
    }
    
    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
