package com.alex.puiu.binancecryptotool.seller.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

public interface SellerService {

    boolean sellSymbol(String symbol);

    boolean studyCandlestick(Candlestick candlestick);
}
