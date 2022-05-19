package com.alex.puiu.binancecryptotool.strategy;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.springframework.stereotype.Component;

import java.util.NavigableMap;

@Component
public class DefaultBuyerStrategy implements Strategy {

    @Override
    public boolean execute(Candlestick candlestick) {
        return false;
    }

    @Override
    public NavigableMap<Long, Candlestick> getCandlestickNavigableMap() {
        return null;
    }

    @Override
    public int getConsecutiveTicks() {
        return 0;
    }
}
