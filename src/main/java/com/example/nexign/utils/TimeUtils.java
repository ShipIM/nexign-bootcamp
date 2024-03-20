package com.example.nexign.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TimeUtils {

    private final static String PATTERN = "%02d:%02d:%02d:%02d";

    public Long getStartOfMonthUnixTime(Integer year, Integer month) {
        var dateTime = LocalDateTime.of(year, month, 1, 0, 0);

        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public Long getEndOfMonthUnixTime(Integer year, Integer month) {
        var nextMonthStart = LocalDateTime.of(month == 12 ? year + 1 : year, month == 12 ? 1 : month + 1,
                1, 0, 0);

        return nextMonthStart.minusSeconds(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public String formatSeconds(Long seconds) {
        var days = seconds / (24 * 3600);
        var hours = (seconds % (24 * 3600)) / 3600;
        var minutes = (seconds % 3600) / 60;
        var remainingSeconds = seconds % 60;

        return String.format(PATTERN, days, hours, minutes, remainingSeconds);
    }

    public long parseTime(String durationString) {
        var parts = durationString.split(":");

        var days = Long.parseLong(parts[0]);
        var hours = Long.parseLong(parts[1]);
        var minutes = Long.parseLong(parts[2]);
        var seconds = Long.parseLong(parts[3]);

        return days * 24 * 3600 + hours * 3600 + minutes * 60 + seconds;
    }

}
