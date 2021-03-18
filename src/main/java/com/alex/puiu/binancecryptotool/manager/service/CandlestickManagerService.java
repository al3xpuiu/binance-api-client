package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

public interface CandlestickManagerService {

    void addCandlestick(Candlestick candlestick);

    void updateCandlesticks(Candlestick candlestick, Candlestick deletedCandlestick);

    boolean updateLowestCandlestick(Candlestick candlestick, Candlestick deletedCandlestick);

    boolean updateHighestCandlestick(Candlestick candlestick, Candlestick deletedCandlestick);

    boolean updateLatestCandlestick(Candlestick candlestick);

}
