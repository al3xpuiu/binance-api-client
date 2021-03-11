package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.model.Price;
import com.alex.puiu.binancecryptotool.model.PriceManager;
import com.alex.puiu.binancecryptotool.model.PriceManagerFixPeriod;
import com.alex.puiu.binancecryptotool.util.RecordDuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PriceManagerServiceImpl implements PriceManagerService {

    private final PriceManager priceManager;

    public PriceManagerServiceImpl(@Value("${}") int intervalMinutes, @Value("${}") RecordDuration recordDuration) {
        this.priceManager = new PriceManagerFixPeriod(intervalMinutes, recordDuration);
    }

    @Override
    public void addPrice(Price price) {
        Price deletedPrice = null;
        if (this.priceManager.getPriceDeque().size() > this.priceManager.getMaximumCollectionLength()) {
            deletedPrice = this.priceManager.getPriceDeque().pop();
            this.priceManager.getPriceDeque().offer(price);
            this.updatePrices(price, deletedPrice);
            return;
        }
        this.priceManager.getPriceDeque().offer(price);
        this.updatePrices(price, deletedPrice);
    }

    @Override
    public void updatePrices(Price price, Price deletedPrice) {
        updateLowestPrice(price, deletedPrice);
        updateHighestPrice(price);
        updateLatestPrice(price);
        updateIndicators(price);
    }

    private void updateLowestPrice(Price price, Price deletedPrice) {
        if (this.priceManager.getLowestPrice().getValue().compareTo(price.getValue()) < 0) {
            this.priceManager.setLowestPrice(price);
            return;
        }
        if (deletedPrice != null && deletedPrice.getTime().plusDays(1).isBefore(price.getTime())) {
            this.priceManager.setLowestPrice(this.findNewLowestPrice());
        }
    }

    private Price findNewLowestPrice() {
        return this.priceManager
                .getPriceDeque()
                .stream()
                .min((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                .orElseThrow(IllegalStateException::new);
    }

    private void updateHighestPrice(Price price) {

    }

    private void updateLatestPrice(Price price) {

    }

    private void updateIndicators(Price price) {

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
