package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Price;

public interface PriceManagerService {

    void addPrice(Price price);

    void updatePrices(Price price, Price deletedPrice);

    boolean updateLowestPrice(Price price, Price deletedPrice);

    boolean updateHighestPrice(Price price, Price deletedPrice);

    boolean updateLatestPrice(Price price);

}
