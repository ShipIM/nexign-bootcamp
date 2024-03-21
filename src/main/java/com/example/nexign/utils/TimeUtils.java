package com.example.nexign.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * The TimeUtils class provides various utility methods for handling time-related operations.
 */
@Component
public class TimeUtils {

    private final static String PATTERN = "%02d:%02d:%02d:%02d";
    private final static ZoneOffset OFFSET = ZoneOffset.UTC;

    /**
     * Calculates the Unix time at the start of the specified date.
     *
     * @param date the date for which the Unix time is calculated
     * @return the Unix time at the start of the specified date
     */
    public Long getStartOfMonthUnixTime(LocalDate date) {
        return date.atStartOfDay().toInstant(OFFSET).toEpochMilli();
    }

    /**
     * Calculates the Unix time at the end of the specified date.
     *
     * @param date the date for which the Unix time is calculated
     * @return the Unix time at the end of the specified date
     */
    public Long getEndOfMonthUnixTime(LocalDate date) {
        return date.plusMonths(1).atStartOfDay().minusSeconds(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    /**
     * Formats the given number of seconds into a human-readable duration string.
     *
     * @param seconds the number of seconds to format
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
     * @param durationString the duration string to parse
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
