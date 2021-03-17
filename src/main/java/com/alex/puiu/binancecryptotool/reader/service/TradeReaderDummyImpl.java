package com.alex.puiu.binancecryptotool.reader.service;

import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.service.PriceManagerService;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service()
@Profile("dummy")
public class TradeReaderDummyImpl implements TradeReader {

    private final ObjectUtils<AggTrade> aggTradeObjectUtils;
    private final PriceManagerService priceManagerService;
    public final String serializedAggTradesPath;

    @Autowired
    public TradeReaderDummyImpl(ObjectUtils<AggTrade> aggTradeObjectUtils,
                                PriceManagerService priceManagerService,
                                @Value("${dummySerializedAggTradesPath:src/main/resources/static/aggTrades.txt}") String serializedAggTradesPath) {
        this.aggTradeObjectUtils = aggTradeObjectUtils;
        this.priceManagerService = priceManagerService;
        this.serializedAggTradesPath = serializedAggTradesPath;
    }

    @Override
    public void readAggTrades() {
        List<AggTrade> trades = this.aggTradeObjectUtils.readObjectsFromFile(new File(this.serializedAggTradesPath));
        trades.forEach(aggTrade -> this.priceManagerService.addPrice(PriceMapper.INSTANCE.aggTradeToPrice(aggTrade)));
    }
}
