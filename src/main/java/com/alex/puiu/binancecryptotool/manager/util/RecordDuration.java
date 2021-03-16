package com.alex.puiu.binancecryptotool.manager.util;

import lombok.Getter;

@Getter
public enum RecordDuration {
    TWENTY_FOUR_HOURS(24);

    private final int value;
    private final int millis;
    private final int totalMinutes;
    private final int totalDays;

    RecordDuration(int valueHours) {
        this.value = valueHours;
        this.totalMinutes = 60 * valueHours;
        if (valueHours % 24 != 0) {
            throw new IllegalArgumentException("The number of hours given, but be perfectly dividable by the number of hours in a day: 24");
        }
        this.totalDays = valueHours / 24;
        this.millis = valueHours * 3600 * 1000;
    }
}
