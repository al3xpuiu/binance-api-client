package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Candlestick;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class PriceServiceImpl implements PriceService {

    @Override
    public Candlestick averageTrade(List<AggTrade> aggTrades) {
        Optional<AggTrade> average = aggTrades
                .stream()
                .reduce((t1, t2) -> {
                    t1.setPrice(t1.getPrice().add(t2.getPrice()).divide(BigDecimal.valueOf(2), 8, RoundingMode.HALF_UP));
                    t1.setQuantity(t1.getQuantity().add(t2.getQuantity()).divide(BigDecimal.valueOf(2), 8, RoundingMode.HALF_UP));
                    t1.setTradeTime((t1.getTradeTime() + t2.getTradeTime())/2);
                    t1.setBuyerMaker(t1.isBuyerMaker());
                    return t1;
                });
        return PriceMapper.INSTANCE.aggTradeToPrice(average.orElseThrow(IllegalStateException::new));
    }
}
