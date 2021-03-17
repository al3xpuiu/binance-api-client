package com.alex.puiu.binancecryptotool.seller.service;

import com.alex.puiu.binancecryptotool.manager.model.Price;

import java.util.Deque;

public interface SellerService {

    void startPriceStudy(Deque<Price> prices);
    boolean sellSymbol(String symbol);
}
