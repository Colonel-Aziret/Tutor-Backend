package com.example.tutor.util;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class DateUtils {

    public static final String FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_DD_MM_YYYY_DOTS = "dd.MM.yyyy";
    public static final String FORMAT_DD_MM_YYYY_HH_MM_SS_SSS_FOR_PATH = "ddMMyyyy-HHmmssSSS";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final Map<String, DateTimeFormatter> FORMATTERS = new HashMap<>();

    static {
        FORMATTERS.put(FORMAT_DD_MM_YYYY, DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY, Locale.ENGLISH));
        FORMATTERS.put(FORMAT_YYYY_MM_DD, DateTimeFormatter.ofPattern(FORMAT_YYYY_MM_DD, Locale.ENGLISH));
        FORMATTERS.put(FORMAT_DD_MM_YYYY_DOTS, DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY_DOTS, Locale.ENGLISH));
        FORMATTERS.put(FORMAT_DD_MM_YYYY_HH_MM_SS_SSS_FOR_PATH, DateTimeFormatter.ofPattern(FORMAT_DD_MM_YYYY_HH_MM_SS_SSS_FOR_PATH, Locale.ENGLISH));
        FORMATTERS.put(FORMAT_YYYY_MM_DD_HH_MM_SS_SSS, DateTimeFormatter.ofPattern(FORMAT_YYYY_MM_DD_HH_MM_SS_SSS, Locale.ENGLISH));
    }

    public static LocalDate parseString2LocalDate(String dateStr, String format) {
        return parseDate(dateStr, format, LocalDate.class);
    }

    public static LocalDateTime parseString2LocalDateTime(String dateStr, String format) {
        return parseDate(dateStr, format, LocalDateTime.class);
    }

    public static Date parseStringToDate(String dateStr, String format) {
        LocalDateTime localDateTime = parseDate(dateStr, format, LocalDateTime.class);
        return localDateTime != null ? Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static String formatToString(Date date, String format) {
        if (date == null || format == null) return null;
        Instant instant = date.toInstant();
        return formatToString(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()), format);
    }

    public static String formatToString(TemporalAccessor date, String format) {
        if (date == null || format == null) return null;
        DateTimeFormatter formatter = FORMATTERS.get(format);
        return formatter != null ? formatter.format(date) : null;
    }

    private static <T> T parseDate(String dateStr, String format, Class<T> type) {
        if (dateStr == null || format == null) return null;
        try {
            DateTimeFormatter formatter = FORMATTERS.get(format);
            if (formatter == null) throw new IllegalArgumentException("Unsupported date format: " + format);
            if (type == LocalDate.class) return type.cast(LocalDate.parse(dateStr, formatter));
            if (type == LocalDateTime.class) return type.cast(LocalDateTime.parse(dateStr, formatter));
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateStr + ". Expected format: " + format);
        }
        return null;
    }

    public static LocalDate addDays(LocalDate start, int daysToAdd, boolean onlyWorkingDays) {
        int added = 0;
        LocalDate result = start;

        while (added < daysToAdd) {
            result = result.plusDays(1);
            if (!onlyWorkingDays || isWorkingDay(result)) {
                added++;
            }
        }

        return result;
    }

    public static int countDays(LocalDate from, LocalDate to, boolean onlyWorkingDays) {
        int days = 0;
        LocalDate date = from;

        while (!date.isAfter(to)) {
            if (!onlyWorkingDays || isWorkingDay(date)) {
                days++;
            }
            date = date.plusDays(1);
        }

        return days;
    }

    public static boolean isWorkingDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }
}

