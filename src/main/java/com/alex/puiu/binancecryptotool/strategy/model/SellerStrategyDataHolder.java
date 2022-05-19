package com.alex.puiu.binancecryptotool.strategy.model;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import lombok.Getter;
import lombok.Setter;

import java.util.NavigableMap;
import java.util.TreeMap;

@Getter
@Setter
public class SellerStrategyDataHolder {

    private final NavigableMap<Long, Candlestick> candlestickNavigableMap;
    private int consecutiveDrops;
    private Candlestick firstCandlestick;
    private Candlestick beforeLastCompletedCandlestick;
    private Candlestick lastCompletedCandlestick;

    public SellerStrategyDataHolder() {
        this.candlestickNavigableMap = new TreeMap<>();
    }
}
