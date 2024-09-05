package com.bca.byc.util;

import org.jsoup.Jsoup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    // apps formater
    private static final DateTimeFormatter formatterApps = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatDateTimeApps(LocalDateTime formatDate) {
        return formatDate != null ? formatDate.format(formatterApps) : null;
    }

    // cms formatter
    private static final DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatLocalDateTime(LocalDateTime formatDate) {
        return formatDate != null ? formatDate.format(formatterLocalDateTime) : null;
    }

    private static final DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatLocalDate(LocalDate dateTime) {
        return dateTime != null ? dateTime.format(formatterLocalDate) : null;
    }

    public static String formatDescription(String description) {
        return Jsoup.parse(description).text();
    }
}