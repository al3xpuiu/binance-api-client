package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;

@Component
public class PriceUtilsImpl implements PriceUtils {

    @Override
    public Candlestick findNewLowestPrice(Collection<Candlestick> candlestickCollection) {
        return candlestickCollection
                .stream()
                .min(Comparator.comparing(Candlestick::getClose))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Candlestick findNewHighestPrice(Collection<Candlestick> candlestickCollection) {
        return candlestickCollection
                .stream()
                .max(Comparator.comparing(Candlestick::getClose))
                .orElseThrow(IllegalStateException::new);
    }
}
