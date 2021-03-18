package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.util.List;

public interface PriceService {

    Candlestick averageTrade(List<AggTrade> aggTrades);
}
