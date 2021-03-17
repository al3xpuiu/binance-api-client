package com.alex.puiu.binancecryptotool.binanceconnector.service;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;

public class TradeReaderImpl implements TradeReader{

    private final BinanceApiWebSocketClient client;
    public TradeReaderImpl() {
        this.client = BinanceApiClientFactory.newInstance().newWebSocketClient();
    }

    @Override
    public void readAggTrades() {

    }

    @Override
    public void readCandlesticks() {

    }
}
