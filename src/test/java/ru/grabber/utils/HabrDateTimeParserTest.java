package ru.grabber.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HabrDateTimeParserTest {
    private HabrDateTimeParser parser;

    @BeforeEach
    void init() {
        parser = new HabrDateTimeParser();
    }

    @Test
    void whenParseTimeZonedDataThenYYYYmmDDtHHmmSSres() {
        parser = new HabrDateTimeParser();
        String dateTime = """
                2023-11-20T10:15:30+03:00
                """;
        String expected = "2023-11-20T10:15:30";
        assertThat(parser.parse(dateTime).toString()).isEqualTo(expected);
    }

    @Test
    void whenDateDoesntContainsTimeZoneThenLocalDateTimeIsSame() {
        String dateTime = """
                2025-11-20T10:15:30
                """;
        String expected = "2025-11-20T10:15:30";
        assertThat(parser.parse(dateTime).toString()).isEqualTo(expected);
    }

    @Test
    void whenDateHasNegativeTimeZoneThenLocalDateTimeIsSame() {
        String dateTime = """
                2025-11-20T10:15:30-03:00
                """;
        String expected = "2025-11-20T10:15:30";
        assertThat(parser.parse(dateTime).toString()).isEqualTo(expected);
    }
}