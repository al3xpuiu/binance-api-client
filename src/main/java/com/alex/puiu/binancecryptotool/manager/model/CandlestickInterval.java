package com.alex.puiu.binancecryptotool.manager.model;

import lombok.Getter;

@Getter
public enum CandlestickInterval {
    ONE_MINUTE(1),
    THREE_MINUTES(3),
    FIVE_MINUTES(5),
    FIFTEEN_MINUTES(15),
    HALF_HOURLY(30),
    HOURLY(60),
    TWO_HOURLY(120),
    FOUR_HOURLY(240),
    SIX_HOURLY(360),
    EIGHT_HOURLY(480),
    TWELVE_HOURLY(720),
    DAILY(1440);

    private final int intervalMinutes;

    CandlestickInterval(int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }
}
