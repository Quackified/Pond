package com.clinicapp.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * InputValidator provides pure validation methods for data validation.
 * All validation logic is decoupled from I/O operations.
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
     */
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate email address format.
     * 
     * @param email Email to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number format.
     * 
     * @param phoneNumber Phone number to validate
     * @return true if valid phone format, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }
    
    /**
     * Validate date format (yyyy-MM-dd).
     * 
     * @param dateStr Date string to validate
     * @return true if valid date format, false otherwise
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null) return false;
        try {
            LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Parse and validate a date string.
     * 
     * @param dateStr Date string in yyyy-MM-dd format
     * @param allowFuture Whether future dates are allowed
     * @param allowPast Whether past dates are allowed
     * @return LocalDate if valid, null otherwise
     */
    public static LocalDate parseAndValidateDate(String dateStr, boolean allowFuture, boolean allowPast) {
        if (dateStr == null) return null;
        
        try {
            LocalDate date = LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            
            if (!allowFuture && date.isAfter(today)) {
                return null;
            }
            
            if (!allowPast && date.isBefore(today)) {
                return null;
            }
            
            return date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validate time format (HH:mm).
     * 
     * @param timeStr Time string to validate
     * @return true if valid time format, false otherwise
     */
    public static boolean isValidTimeFormat(String timeStr) {
        if (timeStr == null) return false;
        try {
            LocalTime.parse(timeStr.trim(), TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Parse and validate a time string.
     * 
     * @param timeStr Time string in HH:mm format
     * @return LocalTime if valid, null otherwise
     */
    public static LocalTime parseAndValidateTime(String timeStr) {
        if (timeStr == null) return null;
        
        try {
            return LocalTime.parse(timeStr.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validate gender string.
     * 
     * @param gender Gender to validate
     * @return true if valid gender, false otherwise
     */
    public static boolean isValidGender(String gender) {
        if (gender == null) return false;
        String g = gender.trim().toLowerCase();
        return g.equals("male") || g.equals("female") || g.equals("other");
    }
    
    /**
     * Validate blood type.
     * 
     * @param bloodType Blood type to validate
     * @param optional If true, allows null/empty
     * @return true if valid blood type, false otherwise
     */
    public static boolean isValidBloodType(String bloodType, boolean optional) {
        if (bloodType == null || bloodType.trim().isEmpty()) {
            return optional;
        }
        
        String[] validTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        String normalized = bloodType.trim().toUpperCase();
        
        for (String type : validTypes) {
            if (type.equals(normalized)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate integer is within range.
     * 
     * @param value Integer value to validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if value is within range, false otherwise
     */
    public static boolean isValidIntegerRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
