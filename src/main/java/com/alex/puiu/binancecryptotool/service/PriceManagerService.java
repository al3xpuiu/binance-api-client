package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.model.Price;

public interface PriceManagerService {

    void addPrice(Price price);

    void updatePrices(Price price, Price deletedPrice);

    boolean updateLowestPrice(Price price, Price deletedPrice);

    boolean updateHighestPrice(Price price);

    boolean updateLatestPrice(Price price);

    boolean updateIndicators(Price price);
}
