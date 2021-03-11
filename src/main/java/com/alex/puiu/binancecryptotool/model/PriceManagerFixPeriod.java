package com.alex.puiu.binancecryptotool.model;

import com.alex.puiu.binancecryptotool.util.RecordDuration;
import lombok.Getter;

import java.time.Duration;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Getter
public class PriceManagerFixPeriod extends PriceManager{

    private final Deque<Price> priceDeque;
    private final int maximumCollectionLength;
    private final Duration priceQueueTimeInterval;

    public PriceManagerFixPeriod(int intervalMinutes, RecordDuration recordDuration) {
        this.priceDeque = new ConcurrentLinkedDeque<>();
        this.maximumCollectionLength = recordDuration.getValue() * 60 / intervalMinutes;
        this.priceQueueTimeInterval = Duration.ofMinutes(intervalMinutes);
    }
}
