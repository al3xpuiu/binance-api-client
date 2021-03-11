package com.alex.puiu.binancecryptotool.util;

import lombok.Getter;

@Getter
public enum RecordDuration {
    TWENTY_FOUR_HOURS(24);

    private final int value;
    private final int totalMinutes;

    RecordDuration(int valueHours) {
        this.value = valueHours;
        this.totalMinutes = 60 * valueHours;
    }
}
