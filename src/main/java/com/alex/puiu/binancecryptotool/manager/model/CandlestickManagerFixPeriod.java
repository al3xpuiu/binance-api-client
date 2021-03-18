package com.alex.puiu.binancecryptotool.manager.model;

import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import lombok.Getter;

import java.time.Duration;
import java.util.NavigableMap;
import java.util.TreeMap;

@Getter
public class CandlestickManagerFixPeriod extends CandlestickManager {

    private final NavigableMap<Long, Candlestick> candlestickMap;
    private final int maximumCollectionLength;
    private final Duration priceQueueTimeInterval;
    private final RecordDuration recordDuration;

    public CandlestickManagerFixPeriod(int intervalMinutes, RecordDuration recordDuration) {
        this.candlestickMap = new TreeMap<>();
        this.recordDuration = recordDuration;
        this.maximumCollectionLength = recordDuration.getValue() * 60 / intervalMinutes;
        this.priceQueueTimeInterval = Duration.ofMinutes(intervalMinutes);
    }
}
