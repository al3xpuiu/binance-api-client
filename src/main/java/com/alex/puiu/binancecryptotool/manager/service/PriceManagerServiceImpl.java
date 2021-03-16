package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Price;
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
    public void addPrice(Price price) {
        Price deletedPrice;
        if (this.priceManager.getPriceDeque().size() >= this.priceManager.getMaximumCollectionLength()) {
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
        updateHighestPrice(price, deletedPrice);
        updateLatestPrice(price);
    }

    @Override
    public boolean updateLowestPrice(Price price, Price deletedPrice) {
        if (price != null && this.priceManager.getLowestPrice().getValue().compareTo(price.getValue()) > 0) {
            this.priceManager.setLowestPrice(price);
            return true;
        }
        if (deletedPrice != null && deletedPrice.equals(this.priceManager.getLowestPrice())) {
            this.priceManager.setLowestPrice(this.priceUtils.findNewLowestPrice(this.priceManager.getPriceDeque()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateHighestPrice(Price price, Price deletedPrice) {
        if (price != null && this.priceManager.getHighestPrice().getValue().compareTo(price.getValue()) < 0) {
            this.priceManager.setHighestPrice(price);
            return true;
        }
        if (deletedPrice != null && deletedPrice.equals(this.priceManager.getHighestPrice())) {
            this.priceManager.setHighestPrice(this.priceUtils.findNewHighestPrice(this.priceManager.getPriceDeque()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLatestPrice(Price price) {
        if (price != null) {
            this.priceManager.setLatestPrice(price);
            return true;
        }
        return false;
    }
}
