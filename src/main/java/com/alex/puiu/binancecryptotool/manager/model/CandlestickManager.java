package com.alex.puiu.binancecryptotool.manager.model;

import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import lombok.Getter;
import lombok.Setter;

import java.util.NavigableMap;

@Getter
@Setter
public abstract class CandlestickManager {

    private String symbol;
    private Candlestick lowestCandlestick;
    private Candlestick highestCandlestick;
    private Candlestick latestCandlestick;
    public CandlestickManager() {
    }

    public abstract NavigableMap<Long, Candlestick> getCandlestickMap();
    public abstract int getMaximumCollectionLength();
    public abstract RecordDuration getRecordDuration();
}
