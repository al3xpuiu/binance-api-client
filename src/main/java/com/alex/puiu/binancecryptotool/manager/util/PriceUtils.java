package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.util.Collection;

public interface PriceUtils {
    Candlestick findNewLowestPrice(Collection<Candlestick> candlestickCollection);
    Candlestick findNewHighestPrice(Collection<Candlestick> candlestickCollection);
}
