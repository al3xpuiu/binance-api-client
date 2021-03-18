package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.util.NavigableMap;

public interface PriceUtils {
    Candlestick findNewLowestPrice(NavigableMap<Long, Candlestick> candlestickNavigableMap);
    Candlestick findNewHighestPrice(NavigableMap<Long, Candlestick> candlestickNavigableMap);
}
