package com.alex.puiu.binancecryptotool.seller.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.springframework.stereotype.Service;

import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class SellerServiceImpl implements SellerService {

    private NavigableMap<Long, Candlestick> candlestickNavigableMap;

    public SellerServiceImpl() {
        this.candlestickNavigableMap = new TreeMap<>();
    }

    @Override
    public boolean sellSymbol(String symbol) {
        return false;
    }

    @Override
    public boolean studyCandlestick(Candlestick candlestick) {
        return false;
    }
}
