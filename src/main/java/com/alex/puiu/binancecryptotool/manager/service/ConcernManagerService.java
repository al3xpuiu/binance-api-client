package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

public interface ConcernManagerService {

    public void activateSeller(Candlestick highestCandlestick);

    public void activateBuyer(Candlestick lowestCandlestick);

    public void activatePriceManager();

    public boolean isSellerActive();

    public boolean isBuyerActive();

    public boolean isPriceManagerActive();
}
