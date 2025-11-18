package com.clinicapp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * InputValidator provides utility methods for validating user input.
 * Pure validation methods with no console I/O.
 */
public class InputValidator {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10,15}$|^\\+?[0-9]{10,15}$");
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Validate integer within a range.
     */
    public static boolean isValidInt(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input.trim());
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Parse integer from string.
     */
    public static Integer parseInt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Validate non-empty string.
     */
    public static boolean isNonEmptyString(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Validate email address.
     */
    public static boolean isValidEmail(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(input.trim()).matches();
    }
    
    /**
     * Validate phone number.
     */
    public static boolean isValidPhoneNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(input.trim()).matches();
    }
    
    /**
     * Parse date in yyyy-MM-dd format.
     */
    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validate date and check if it meets future/past requirements.
     */
    public static boolean isValidDate(String input, boolean allowFuture, boolean allowPast) {
        LocalDate date = parseDate(input);
        if (date == null) {
            return false;
        }
        
        LocalDate today = LocalDate.now();
        if (!allowFuture && date.isAfter(today)) {
            return false;
        }
        if (!allowPast && date.isBefore(today)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Parse time in HH:mm format.
     */
    public static LocalTime parseTime(String input) {
        try {
            return LocalTime.parse(input.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validate time format.
     */
    public static boolean isValidTime(String input) {
        return parseTime(input) != null;
    }
    
    /**
     * Validate gender.
     */
    public static boolean isValidGender(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        String gender = input.trim().toLowerCase();
        return gender.equals("male") || gender.equals("female") || gender.equals("other");
    }
    
    /**
     * Validate blood type.
     */
    public static boolean isValidBloodType(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        String[] validTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        String bloodType = input.trim().toUpperCase();
        
        for (String type : validTypes) {
            if (type.equals(bloodType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get valid blood types.
     */
    public static String[] getValidBloodTypes() {
        return new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    }
    
    /**
     * Validate yes/no confirmation.
     */
    public static Boolean parseConfirmation(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String value = input.trim().toLowerCase();
        if (value.equals("yes") || value.equals("y")) {
            return true;
        } else if (value.equals("no") || value.equals("n")) {
            return false;
        }
        return null;
    }
    
    /**
     * Capitalize first letter of string.
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Validate that a string is not null and not empty after trimming.
     */
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Format date for display.
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }
    
    /**
     * Format time for display.
     */
    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }
    
    /**
     * Format datetime for display.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
    }
}
