package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.model.AggTrade;
import com.alex.puiu.binancecryptotool.model.Price;

import java.util.List;

public interface PriceService {

    Price averageTrade(List<AggTrade> aggTrades);
}
