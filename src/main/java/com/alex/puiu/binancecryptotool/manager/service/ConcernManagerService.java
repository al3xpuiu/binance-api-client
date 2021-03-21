package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

public interface ConcernManagerService {

    void activateSeller(Candlestick highestCandlestick);
    void addCandlestickToSeller(Candlestick highestCandlestick);

    void activateBuyer(Candlestick lowestCandlestick);
    void addCandlestickToBuyer(Candlestick highestCandlestick);

    void activatePriceManager();

    boolean isSellerActive();

    boolean isBuyerActive();

    boolean isPriceManagerActive();
}
