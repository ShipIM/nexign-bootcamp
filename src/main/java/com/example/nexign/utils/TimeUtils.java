package com.example.nexign.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * The TimeUtils class provides various utility methods for handling time-related operations.
 */
@Component
public class TimeUtils {

    private final static String PATTERN = "%02d:%02d:%02d:%02d";

    /**
     * Calculates the Unix time at the start of the specified month and year.
     *
     * @param year  the year
     * @param month the month
     * @return the Unix time at the start of the specified month and year
     */
    public Long getStartOfMonthUnixTime(Integer year, Integer month) {
        var dateTime = LocalDateTime.of(year, month, 1, 0, 0);

        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * Calculates the Unix time at the end of the specified month and year.
     *
     * @param year  the year
     * @param month the month
     * @return the Unix time at the end of the specified month and year
     */
    public Long getEndOfMonthUnixTime(Integer year, Integer month) {
        var nextMonthStart = LocalDateTime.of(month == 12 ? year + 1 : year, month == 12 ? 1 : month + 1,
                1, 0, 0);

        return nextMonthStart.minusSeconds(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * Formats the given number of seconds into a human-readable duration string.
     *
     * @param seconds the number of seconds
     * @return the formatted duration string
     */
    public String formatSeconds(Long seconds) {
        var days = seconds / (24 * 3600);
        var hours = (seconds % (24 * 3600)) / 3600;
        var minutes = (seconds % 3600) / 60;
        var remainingSeconds = seconds % 60;

        return String.format(PATTERN, days, hours, minutes, remainingSeconds);
    }

    /**
     * Parses the given duration string in the format "DD:hh:mm:ss" into seconds.
     *
     * @param durationString the duration string
     * @return the number of seconds parsed from the string
     */
    public long parseTime(String durationString) {
        var parts = durationString.split(":");

        var days = Long.parseLong(parts[0]);
        var hours = Long.parseLong(parts[1]);
        var minutes = Long.parseLong(parts[2]);
        var seconds = Long.parseLong(parts[3]);

        return days * 24 * 3600 + hours * 3600 + minutes * 60 + seconds;
    }

}
