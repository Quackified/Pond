package com.clinicapp.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * InputValidator provides pure validation methods for user input.
 * All methods are static and do not perform any console I/O.
 * This class is designed to work with Java Swing GUI applications.
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
     * Validate that a string is not null and not empty after trimming.
     * 
     * @param str String to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate email address format.
     * 
     * @param email Email address to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number format (10-15 digits, optionally starting with +).
     * 
     * @param phoneNumber Phone number to validate
     * @return true if valid phone format, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }
    
    /**
     * Validate and parse date string in yyyy-MM-dd format.
     * 
     * @param dateStr Date string to parse
     * @return LocalDate if valid, null otherwise
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validate date string format.
     * 
     * @param dateStr Date string to validate
     * @return true if valid date format, false otherwise
     */
    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;
    }
    
    /**
     * Validate that date is not in the past.
     * 
     * @param date Date to validate
     * @return true if date is today or in the future, false otherwise
     */
    public static boolean isNotPastDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(LocalDate.now());
    }
    
    /**
     * Validate that date is not in the future.
     * 
     * @param date Date to validate
     * @return true if date is today or in the past, false otherwise
     */
    public static boolean isNotFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isAfter(LocalDate.now());
    }
    
    /**
     * Validate and parse time string in HH:mm format.
     * 
     * @param timeStr Time string to parse
     * @return LocalTime if valid, null otherwise
     */
    public static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validate time string format.
     * 
     * @param timeStr Time string to validate
     * @return true if valid time format, false otherwise
     */
    public static boolean isValidTime(String timeStr) {
        return parseTime(timeStr) != null;
    }
    
    /**
     * Validate gender value (Male, Female, or Other).
     * 
     * @param gender Gender string to validate
     * @return true if valid gender, false otherwise
     */
    public static boolean isValidGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return false;
        }
        String normalized = gender.trim();
        return normalized.equalsIgnoreCase("Male") || 
               normalized.equalsIgnoreCase("Female") || 
               normalized.equalsIgnoreCase("Other");
    }
    
    /**
     * Normalize gender string to proper case.
     * 
     * @param gender Gender string to normalize
     * @return Normalized gender string (Male/Female/Other) or null if invalid
     */
    public static String normalizeGender(String gender) {
        if (!isValidGender(gender)) {
            return null;
        }
        String normalized = gender.trim();
        if (normalized.equalsIgnoreCase("Male")) {
            return "Male";
        } else if (normalized.equalsIgnoreCase("Female")) {
            return "Female";
        } else {
            return "Other";
        }
    }
    
    /**
     * Validate blood type (A+, A-, B+, B-, AB+, AB-, O+, O-).
     * 
     * @param bloodType Blood type string to validate
     * @return true if valid blood type, false otherwise
     */
    public static boolean isValidBloodType(String bloodType) {
        if (bloodType == null || bloodType.trim().isEmpty()) {
            return false;
        }
        String normalized = bloodType.trim().toUpperCase();
        return normalized.equals("A+") || normalized.equals("A-") ||
               normalized.equals("B+") || normalized.equals("B-") ||
               normalized.equals("AB+") || normalized.equals("AB-") ||
               normalized.equals("O+") || normalized.equals("O-");
    }
    
    /**
     * Normalize blood type string to uppercase.
     * 
     * @param bloodType Blood type string to normalize
     * @return Normalized blood type string or null if invalid
     */
    public static String normalizeBloodType(String bloodType) {
        if (!isValidBloodType(bloodType)) {
            return null;
        }
        return bloodType.trim().toUpperCase();
    }
    
    /**
     * Validate integer within a range.
     * 
     * @param value Value to validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if value is within range, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Parse integer from string.
     * 
     * @param str String to parse
     * @return Integer value or null if invalid
     */
    public static Integer parseInt(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Validate that integer string is within a range.
     * 
     * @param str String to parse and validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if string represents a valid integer within range, false otherwise
     */
    public static boolean isValidIntInRange(String str, int min, int max) {
        Integer value = parseInt(str);
        return value != null && isInRange(value, min, max);
    }
}
