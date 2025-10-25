package com.clinicapp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * InputValidator provides utility methods for validating and parsing user input.
 * Ensures data integrity and provides user-friendly error messages.
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
     * Read and validate integer input within a range.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Valid integer within range
     */
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    DisplayHelper.displayError(
                        String.format("Please enter a number between %d and %d.", min, max));
                }
            } catch (NumberFormatException e) {
                DisplayHelper.displayError("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Read non-empty string input.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @return Non-empty trimmed string
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!input.isEmpty()) {
                return input;
            } else {
                DisplayHelper.displayError("Input cannot be empty. Please try again.");
            }
        }
    }
    
    /**
     * Read optional string input (can be empty).
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @return Trimmed string or null if empty
     */
    public static String readOptionalString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }
    
    /**
     * Read and validate email address.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @param optional If true, allows empty input
     * @return Valid email or null if optional and empty
     */
    public static String readEmail(Scanner scanner, String prompt, boolean optional) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty() && optional) {
                return null;
            }
            
            if (EMAIL_PATTERN.matcher(input).matches()) {
                return input;
            } else {
                DisplayHelper.displayError("Invalid email format. Please enter a valid email address.");
            }
        }
    }
    
    /**
     * Read and validate phone number.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @return Valid phone number
     */
    public static String readPhoneNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (PHONE_PATTERN.matcher(input).matches()) {
                return input;
            } else {
                DisplayHelper.displayError(
                    "Invalid phone number. Please enter 10-15 digits (optionally starting with +).");
            }
        }
    }
    
    /**
     * Read and validate date in yyyy-MM-dd format.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @param allowFuture Whether future dates are allowed
     * @param allowPast Whether past dates are allowed
     * @return Valid LocalDate
     */
    public static LocalDate readDate(Scanner scanner, String prompt, 
                                     boolean allowFuture, boolean allowPast) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                LocalDate today = LocalDate.now();
                
                if (!allowFuture && date.isAfter(today)) {
                    DisplayHelper.displayError("Future dates are not allowed.");
                    continue;
                }
                
                if (!allowPast && date.isBefore(today)) {
                    DisplayHelper.displayError("Past dates are not allowed.");
                    continue;
                }
                
                return date;
            } catch (DateTimeParseException e) {
                DisplayHelper.displayError(
                    "Invalid date format. Please use yyyy-MM-dd (e.g., 2024-12-25).");
            }
        }
    }
    
    /**
     * Read and validate time in HH:mm format.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @return Valid LocalTime
     */
    public static LocalTime readTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (HH:mm): ");
            String input = scanner.nextLine().trim();
            
            try {
                return LocalTime.parse(input, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                DisplayHelper.displayError(
                    "Invalid time format. Please use HH:mm (e.g., 14:30).");
            }
        }
    }
    
    /**
     * Read and validate datetime.
     * 
     * @param scanner Scanner for input
     * @return Valid LocalDateTime
     */
    public static LocalDateTime readDateTime(Scanner scanner) {
        LocalDate date = readDate(scanner, "Enter date", true, false);
        LocalTime time = readTime(scanner, "Enter time");
        return LocalDateTime.of(date, time);
    }
    
    /**
     * Read gender with validation.
     * 
     * @param scanner Scanner for input
     * @return Valid gender string
     */
    public static String readGender(Scanner scanner) {
        while (true) {
            System.out.print("Enter gender (Male/Female/Other): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("Male") || 
                input.equalsIgnoreCase("Female") || 
                input.equalsIgnoreCase("Other")) {
                return capitalize(input);
            } else {
                DisplayHelper.displayError("Please enter Male, Female, or Other.");
            }
        }
    }
    
    /**
     * Read blood type with validation.
     * 
     * @param scanner Scanner for input
     * @param optional If true, allows empty input
     * @return Valid blood type or null if optional and empty
     */
    public static String readBloodType(Scanner scanner, boolean optional) {
        String[] validTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        
        while (true) {
            System.out.print("Enter blood type (A+, A-, B+, B-, AB+, AB-, O+, O-)" + 
                           (optional ? " [optional]" : "") + ": ");
            String input = scanner.nextLine().trim().toUpperCase();
            
            if (input.isEmpty() && optional) {
                return null;
            }
            
            for (String type : validTypes) {
                if (type.equals(input)) {
                    return input;
                }
            }
            
            DisplayHelper.displayError("Invalid blood type. Please enter a valid blood type.");
        }
    }
    
    /**
     * Read yes/no confirmation.
     * 
     * @param scanner Scanner for input
     * @param prompt Prompt message
     * @return true for yes, false for no
     */
    public static boolean readConfirmation(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                DisplayHelper.displayError("Please enter 'yes' or 'no'.");
            }
        }
    }
    
    /**
     * Capitalize first letter of string.
     */
    private static String capitalize(String str) {
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
}
