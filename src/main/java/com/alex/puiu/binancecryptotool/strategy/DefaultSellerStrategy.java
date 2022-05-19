package com.alex.puiu.binancecryptotool.strategy;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.strategy.model.SellerStrategyDataHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NavigableMap;

@Component
public class DefaultSellerStrategy implements Strategy {

    private final int dropLimit;
    private SellerStrategyDataHolder dataHolder;
    public DefaultSellerStrategy(@Value("${defaultStrategyDropLimit}") int dropLimit) {
        this.dropLimit = dropLimit;
        this.dataHolder = new SellerStrategyDataHolder();


    }

    @Override
    public boolean execute(Candlestick candlestick) {
        NavigableMap<Long, Candlestick> candlestickNavigableMap = this.dataHolder.getCandlestickNavigableMap();
        int mapSize = candlestickNavigableMap.size();
        if (mapSize < 1) {
            this.dataHolder.getCandlestickNavigableMap().put(candlestick.getOpenTime(), candlestick);
            return false;
        }

        boolean isNewCandlestick = !candlestickNavigableMap.containsKey(candlestick.getOpenTime());
        this.dataHolder.getCandlestickNavigableMap().put(candlestick.getOpenTime(), candlestick);
        if (isNewCandlestick) {

            int i = mapSize-2;

            Candlestick c1 = null;
            Candlestick c2 = null;

            for (Map.Entry<Long, Candlestick> entry : this.dataHolder.getCandlestickNavigableMap().descendingMap().entrySet()) {
                if (i < mapSize) {
                    if (i % 2 == 0) {
                        c1 = entry.getValue();
                    } else {
                        c2 = entry.getValue();
                    }
                } else {
                    break;
                }
                i++;
            }

            if (c1 != null && c2 != null && c1.getClose().compareTo(c2.getClose()) > 0) {
                this.dataHolder.setConsecutiveDrops(this.dataHolder.getConsecutiveDrops() + 1);
            } else {
                this.dataHolder.setConsecutiveDrops(0);
            }

            if (this.dataHolder.getConsecutiveDrops() == dropLimit) {
                //should also log the last sellerStrategyDataHolder
                this.dataHolder = new SellerStrategyDataHolder();
                return true;
            }
        }

        return false;
    }

    @Override
    public NavigableMap<Long, Candlestick> getCandlestickNavigableMap() {
        return this.dataHolder.getCandlestickNavigableMap();
    }

    @Override
    public int getConsecutiveTicks() {
        return this.dataHolder.getConsecutiveDrops();
    }
}
