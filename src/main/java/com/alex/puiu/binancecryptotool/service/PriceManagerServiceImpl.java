package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.model.Price;
import com.alex.puiu.binancecryptotool.model.PriceManager;
import com.alex.puiu.binancecryptotool.util.PriceUtils;
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
    public void addPrice(Price price) {
        Price deletedPrice;
        if (this.priceManager.getPriceDeque().size() > this.priceManager.getMaximumCollectionLength()) {
            deletedPrice = this.priceManager.getPriceDeque().pop();
            this.priceManager.getPriceDeque().offer(price);
            this.updatePrices(price, deletedPrice);
            return;
        }
        this.priceManager.getPriceDeque().offer(price);
        this.updatePrices(price, null);
    }

    @Override
    public void updatePrices(Price price, Price deletedPrice) {
        updateLowestPrice(price, deletedPrice);
        updateHighestPrice(price);
        updateLatestPrice(price);
        updateIndicators(price);
    }

    public boolean updateLowestPrice(Price price, Price deletedPrice) {
        if (this.priceManager.getLowestPrice().getValue().compareTo(price.getValue()) < 0) {
            this.priceManager.setLowestPrice(price);
            return true;
        }
        if (deletedPrice != null && deletedPrice.getTime().plusDays(this.priceManager.getRecordDuration().getTotalDays()).isBefore(price.getTime())) {
            this.priceManager.setLowestPrice(this.priceUtils.findNewLowestPrice(this.priceManager.getPriceDeque()));
            return true;
        }
        return false;
    }

    public boolean updateHighestPrice(Price price) {

        return false;
    }

    public boolean updateLatestPrice(Price price) {

        return false;
    }

    public boolean updateIndicators(Price price) {

        return false;
    }

//    @Override
//    public Action symbolSold() {
//
//        Action newObjective = Action.BUY;
//        this.priceManager.setCurrentObjective(newObjective);
//
//        return newObjective;
//    }
//
//    @Override
//    public Action symbolBought() {
//
//        Action newObjective = Action.SELL;
//        this.priceManager.setCurrentObjective(newObjective);
//
//        return newObjective;
//    }
}
