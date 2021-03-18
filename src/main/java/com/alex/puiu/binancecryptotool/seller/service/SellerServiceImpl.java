package com.alex.puiu.binancecryptotool.seller.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.util.Deque;

public class SellerServiceImpl implements SellerService {

    @Override
    public void startPriceStudy(Deque<Candlestick> candlesticks) {

    }

    @Override
    public boolean sellSymbol(String symbol) {
        return false;
    }
}
