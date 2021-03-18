package com.alex.puiu.binancecryptotool.manager.model;

import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import lombok.Getter;

import java.time.Duration;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Getter
public class CandlestickManagerFixPeriod extends CandlestickManager {

    private final Deque<Candlestick> candlestickDeque;
    private final int maximumCollectionLength;
    private final Duration priceQueueTimeInterval;
    private final RecordDuration recordDuration;

    public CandlestickManagerFixPeriod(int intervalMinutes, RecordDuration recordDuration) {
        this.candlestickDeque = new ConcurrentLinkedDeque<>();
        this.recordDuration = recordDuration;
        this.maximumCollectionLength = recordDuration.getValue() * 60 / intervalMinutes;
        this.priceQueueTimeInterval = Duration.ofMinutes(intervalMinutes);
    }
}
