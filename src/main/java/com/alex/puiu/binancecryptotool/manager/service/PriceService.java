package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Price;

import java.util.List;

public interface PriceService {

    Price averageTrade(List<AggTrade> aggTrades);
}
