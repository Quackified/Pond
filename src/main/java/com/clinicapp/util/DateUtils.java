package com.clinicapp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("hh:mm a");

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");

    private static final DateTimeFormatter SQL_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_COMPACT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_COMPACT_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_TIME_COMPACT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "N/A";
    }

    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "N/A";
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : "N/A";
    }

    public static String formatDateCompact(LocalDate date) {
        return date != null ? date.format(DATE_COMPACT_FORMATTER) : "N/A";
    }

    public static String formatTimeCompact(LocalTime time) {
        return time != null ? time.format(TIME_COMPACT_FORMATTER) : "N/A";
    }

    public static String formatDateTimeCompact(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_COMPACT_FORMATTER) : "N/A";
    }

    public static String formatDateTimeSQL(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(SQL_FORMATTER) : "N/A";
    }
}
