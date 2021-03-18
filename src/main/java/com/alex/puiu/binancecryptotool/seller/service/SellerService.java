package com.alex.puiu.binancecryptotool.seller.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.util.Deque;

public interface SellerService {

    void startPriceStudy(Deque<Candlestick> candlesticks);
    boolean sellSymbol(String symbol);
}
