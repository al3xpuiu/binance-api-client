package com.alex.puiu.binancecryptotool.binanceconnector.service;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;

public class BinanceConnectorImpl implements BinanceConnector {

    private final BinanceApiWebSocketClient client;
    public BinanceConnectorImpl() {
        this.client = BinanceApiClientFactory.newInstance().newWebSocketClient();
    }

    @Override
    public void readAggTrades() {

    }

    @Override
    public void readCandlesticks() {

    }
}
