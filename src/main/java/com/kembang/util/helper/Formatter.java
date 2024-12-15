package com.kembang.util.helper;

import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Formatter {

    // apps formater
    private static final DateTimeFormatter formatterApps = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatDateTimeApps(LocalDateTime formatDate) {
        return formatDate != null ? formatDate.format(formatterApps) : null;
    }

    // apps formater with second
    private static final DateTimeFormatter formatterAppsWithSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatterAppsWithSeconds(LocalDateTime formatDate) {
        return formatDate != null ? formatDate.format(formatterAppsWithSeconds) : null;
    }

    // day Name in indonesia
    public static String formatGetDayName (LocalDate date) {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", new Locale("id", "ID"));
        return date.format(dayFormatter);
    }



    // cms formatter
    private static final DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatLocalDateTime(LocalDateTime formatDate) {
        return formatDate != null ? formatDate.format(formatterLocalDateTime) : null;
    }

    private static final DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatLocalDate(LocalDate dateTime) {
        return dateTime != null ? dateTime.format(formatterLocalDate) : null;
    }

    // string to LocalDate
    public static LocalDate stringToLocalDate(String date) {
        LocalDate parsedDate = null;
        if (date != null && !date.isEmpty()) {
            try {
                parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Invalid date format: " + date, e);
            }
        }
        return parsedDate;
    }

    // text without html
    public static String formatDescription(String description) {
        return Jsoup.parse(description).text();
    }

    // elastic
    public static LocalDateTime parseToLocalDateTime(String dateString) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString);
        return offsetDateTime.toLocalDateTime();
    }
}