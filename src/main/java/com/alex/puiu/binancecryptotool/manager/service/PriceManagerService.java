package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

public interface PriceManagerService {

    void addPrice(Candlestick candlestick);

    void updatePrices(Candlestick candlestick, Candlestick deletedCandlestick);

    boolean updateLowestPrice(Candlestick candlestick, Candlestick deletedCandlestick);

    boolean updateHighestPrice(Candlestick candlestick, Candlestick deletedCandlestick);

    boolean updateLatestPrice(Candlestick candlestick);

}
