package com.bca.byc.util;

import org.jsoup.Jsoup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    private static final DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatterLocalDateTime) : null;
    }

    private static final DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatLocalDate(LocalDate dateTime) {
        return dateTime != null ? dateTime.format(formatterLocalDate) : null;
    }

    public static String formatDescription(String description) {
        return Jsoup.parse(description).text();
    }
}