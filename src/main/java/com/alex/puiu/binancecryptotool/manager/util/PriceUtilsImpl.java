package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.NavigableMap;

@Component
public class PriceUtilsImpl implements PriceUtils {

    @Override
    public Candlestick findNewLowestPrice(NavigableMap<Long, Candlestick> candlestickNavigableMap) {
        return candlestickNavigableMap
                .values()
                .stream()
                .min(Comparator.comparing(Candlestick::getClose))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Candlestick findNewHighestPrice(NavigableMap<Long, Candlestick> candlestickNavigableMap) {
        return candlestickNavigableMap
                .values()
                .stream()
                .max(Comparator.comparing(Candlestick::getClose))
                .orElseThrow(IllegalStateException::new);
    }
}
