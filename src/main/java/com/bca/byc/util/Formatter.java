package com.bca.byc.util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;

public class Formatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(formatter) : null;
    }

    public static String formatDescription(String description) {
        return Jsoup.parse(description).text();
    }
}