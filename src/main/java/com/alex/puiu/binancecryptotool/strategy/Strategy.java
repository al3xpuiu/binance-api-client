package com.alex.puiu.binancecryptotool.strategy;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.util.NavigableMap;

public interface Strategy {

    boolean execute(Candlestick candlestick);

    NavigableMap<Long, Candlestick> getCandlestickNavigableMap();

    int getConsecutiveTicks();
}
