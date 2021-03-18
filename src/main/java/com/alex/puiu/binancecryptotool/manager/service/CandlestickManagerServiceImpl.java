package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.manager.model.CandlestickManager;
import com.alex.puiu.binancecryptotool.manager.util.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandlestickManagerServiceImpl implements CandlestickManagerService {

    private final CandlestickManager candlestickManager;


    private final PriceUtils priceUtils;

    @Autowired
    public CandlestickManagerServiceImpl(PriceUtils priceUtils, CandlestickManager candlestickManager) {
        this.candlestickManager = candlestickManager;
        this.priceUtils = priceUtils;
    }

    @Override
    public void addCandlestick(Candlestick candlestick) {
        Candlestick deletedCandlestick;
        if (this.candlestickManager.getCandlestickMap().size() >= this.candlestickManager.getMaximumCollectionLength()) {
            deletedCandlestick = this.candlestickManager.getCandlestickMap().pollFirstEntry().getValue();
            this.candlestickManager.getCandlestickMap().put(candlestick.getOpenTime(), candlestick);
            this.updateCandlesticks(candlestick, deletedCandlestick);
            return;
        }
        this.candlestickManager.getCandlestickMap().put(candlestick.getOpenTime(), candlestick);
        this.updateCandlesticks(candlestick, null);
    }

    @Override
    public void updateCandlesticks(Candlestick candlestick, Candlestick deletedCandlestick) {
        updateLatestCandlestick(candlestick);
        boolean lowestPriceChanged = updateLowestCandlestick(candlestick, deletedCandlestick);
        if (lowestPriceChanged) {
            //TODO communicate with buyer?
            System.out.println("Lowest Price: " + candlestick);
            return;
        }
        boolean highestPriceChanged = updateHighestCandlestick(candlestick, deletedCandlestick);
        if (highestPriceChanged) {
            //TODO communicate with seller
            System.out.println("Highest Price" + candlestick);
            return;
        }

        System.out.println("Price " + candlestick);
    }

    @Override
    public boolean updateLowestCandlestick(Candlestick candlestick, Candlestick deletedCandlestick) {
        Candlestick currentLowestCandlestick = this.candlestickManager.getLowestCandlestick();
        if (currentLowestCandlestick == null || candlestick != null && currentLowestCandlestick.getClose().compareTo(candlestick.getClose()) > 0) {
            this.candlestickManager.setLowestCandlestick(candlestick);
            return true;
        }
        if (deletedCandlestick != null && deletedCandlestick.equals(currentLowestCandlestick)) {
            this.candlestickManager.setLowestCandlestick(this.priceUtils.findNewLowestPrice(this.candlestickManager.getCandlestickMap()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateHighestCandlestick(Candlestick candlestick, Candlestick deletedCandlestick) {
        Candlestick currentHighestCandlestick = this.candlestickManager.getHighestCandlestick();
        if (currentHighestCandlestick == null || candlestick != null && currentHighestCandlestick.getClose().compareTo(candlestick.getClose()) < 0) {
            this.candlestickManager.setHighestCandlestick(candlestick);
            return true;
        }
        if (deletedCandlestick != null && deletedCandlestick.equals(currentHighestCandlestick)) {
            this.candlestickManager.setHighestCandlestick(this.priceUtils.findNewHighestPrice(this.candlestickManager.getCandlestickMap()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLatestCandlestick(Candlestick candlestick) {
        if (candlestick != null) {
            this.candlestickManager.setLatestCandlestick(candlestick);
            return true;
        }
        return false;
    }
}
