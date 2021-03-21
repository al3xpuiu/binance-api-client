package com.alex.puiu.binancecryptotool.binanceconnector.service;

import com.alex.puiu.binancecryptotool.binanceconnector.mapper.CandlestickMapper;
import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.service.CandlestickManagerService;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service()
@Profile("dummy")
public class BinanceConnectorDummyImpl implements BinanceConnector {

    private final ObjectUtils<AggTrade> aggTradeObjectUtils;
    private final CandlestickManagerService candlestickManagerService;
    public final String serializedAggTradesPath;
    private final BinanceApiRestClient client;

    private final String symbol;
    private final CandlestickInterval candlestickInterval;
    private final int limit;
    private final Date from;

    @Autowired
    public BinanceConnectorDummyImpl(ObjectUtils<AggTrade> aggTradeObjectUtils,
                                     CandlestickManagerService candlestickManagerService,
                                     @Value("${dummySerializedAggTradesPath:src/main/resources/static/aggTrades.txt}") String serializedAggTradesPath,
                                     @Value("${dummySymbol}") String symbol,
                                     @Value("${candlestickInterval}") CandlestickInterval candlestickInterval,
                                     @Value("${dummyLimit}") int limit,
                                     @Value("#{new java.text.SimpleDateFormat('${dateFormat}').parse('${dummyFromDate}')}") Date from) {
        this.aggTradeObjectUtils = aggTradeObjectUtils;
        this.candlestickManagerService = candlestickManagerService;
        this.serializedAggTradesPath = serializedAggTradesPath;
        this.symbol = symbol;
        this.candlestickInterval = candlestickInterval;
        this.limit = limit;
        this.from = from;
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("API-KEY", "SECRET");
        this.client = factory.newRestClient();

    }

    @Override
    public void readAggTrades() {
        List<AggTrade> trades = this.aggTradeObjectUtils.readObjectsFromFile(new File(this.serializedAggTradesPath));
        trades.forEach(aggTrade -> this.candlestickManagerService.addCandlestick(PriceMapper.INSTANCE.aggTradeToPrice(aggTrade)));
    }

    @Override
    public void readCandlesticks() {
        List<Candlestick> candlesticks = client.getCandlestickBars(this.symbol, this.candlestickInterval, this.limit, this.from.getTime(), null);
        candlesticks.forEach(candlestick -> this.candlestickManagerService.addCandlestick(CandlestickMapper.INSTANCE.fromCandlestick(candlestick)));
        System.out.println(candlesticks);
    }
}
