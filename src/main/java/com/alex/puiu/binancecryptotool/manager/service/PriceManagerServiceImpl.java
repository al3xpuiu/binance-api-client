package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.manager.model.PriceManager;
import com.alex.puiu.binancecryptotool.manager.util.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceManagerServiceImpl implements PriceManagerService {

    private final PriceManager priceManager;


    private final PriceUtils priceUtils;

    @Autowired
    public PriceManagerServiceImpl(PriceUtils priceUtils, PriceManager priceManager) {
        this.priceManager = priceManager;
        this.priceUtils = priceUtils;
    }

    @Override
    public void addPrice(Candlestick candlestick) {
        Candlestick deletedCandlestick;
        if (this.priceManager.getCandlestickDeque().size() >= this.priceManager.getMaximumCollectionLength()) {
            deletedCandlestick = this.priceManager.getCandlestickDeque().pop();
            this.priceManager.getCandlestickDeque().offer(candlestick);
            this.updatePrices(candlestick, deletedCandlestick);
            return;
        }
        this.priceManager.getCandlestickDeque().offer(candlestick);
        this.updatePrices(candlestick, null);
    }

    @Override
    public void updatePrices(Candlestick candlestick, Candlestick deletedCandlestick) {
        updateLatestPrice(candlestick);
        boolean lowestPriceChanged = updateLowestPrice(candlestick, deletedCandlestick);
        if (lowestPriceChanged) {
            //TODO communicate with buyer?
            System.out.println("Lowest Price: " + candlestick);
            return;
        }
        boolean highestPriceChanged = updateHighestPrice(candlestick, deletedCandlestick);
        if (highestPriceChanged) {
            //TODO communicate with seller
            System.out.println("Highest Price" + candlestick);
            return;
        }

        System.out.println("Price " + candlestick);
    }

    @Override
    public boolean updateLowestPrice(Candlestick candlestick, Candlestick deletedCandlestick) {
        Candlestick currentLowestCandlestick = this.priceManager.getLowestCandlestick();
        if (currentLowestCandlestick == null || candlestick != null && currentLowestCandlestick.getClose().compareTo(candlestick.getClose()) > 0) {
            this.priceManager.setLowestCandlestick(candlestick);
            return true;
        }
        if (deletedCandlestick != null && deletedCandlestick.equals(currentLowestCandlestick)) {
            this.priceManager.setLowestCandlestick(this.priceUtils.findNewLowestPrice(this.priceManager.getCandlestickDeque()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateHighestPrice(Candlestick candlestick, Candlestick deletedCandlestick) {
        Candlestick currentHighestCandlestick = this.priceManager.getHighestCandlestick();
        if (currentHighestCandlestick == null || candlestick != null && currentHighestCandlestick.getClose().compareTo(candlestick.getClose()) < 0) {
            this.priceManager.setHighestCandlestick(candlestick);
            return true;
        }
        if (deletedCandlestick != null && deletedCandlestick.equals(currentHighestCandlestick)) {
            this.priceManager.setHighestCandlestick(this.priceUtils.findNewHighestPrice(this.priceManager.getCandlestickDeque()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLatestPrice(Candlestick candlestick) {
        if (candlestick != null) {
            this.priceManager.setLatestCandlestick(candlestick);
            return true;
        }
        return false;
    }
}
