package ru.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        String dt = parse.substring(0, 19);
        return LocalDateTime.parse(dt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
