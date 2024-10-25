package com.bca.byc.util.helper;

import org.jsoup.Jsoup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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



    // cms formatter
    private static final DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatLocalDateTime(LocalDateTime formatDate) {
        return formatDate != null ? formatDate.format(formatterLocalDateTime) : null;
    }

    private static final DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatLocalDate(LocalDate dateTime) {
        return dateTime != null ? dateTime.format(formatterLocalDate) : null;
    }

    public static String formatDescription(String description) {
        return Jsoup.parse(description).text();
    }

    // elastic
    public static LocalDateTime parseToLocalDateTime(String dateString) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString);
        return offsetDateTime.toLocalDateTime();
    }
}