package com.alex.puiu.binancecryptotool.manager.model;

import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Deque;

@Getter
@Setter
public abstract class PriceManager {

    private String symbol;
    private Candlestick lowestCandlestick;
    private Candlestick highestCandlestick;
    private Candlestick latestCandlestick;
    public PriceManager() {
    }

    public abstract Deque<Candlestick> getCandlestickDeque();
    public abstract int getMaximumCollectionLength();
    public abstract RecordDuration getRecordDuration();
}
