package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.message.PriceErrorMessage;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.alex.puiu.binancecryptotool.util.ObjectUtilsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.List;

import static com.alex.puiu.binancecryptotool.Path.SERIALIZED_AGG_TRADES_PATH;

class CandlestickUtilsImplTest {

    private PriceUtils priceUtils;
    private Deque<Candlestick> candlestickDeque;
    @BeforeEach
    void setUp() {
        this.priceUtils = new PriceUtilsImpl();
        this.candlestickDeque = new ArrayDeque<>();
        ObjectUtils<AggTrade> objectUtils = new ObjectUtilsImpl<>();

        List<AggTrade> trades = objectUtils.readObjectsFromFile(new File(SERIALIZED_AGG_TRADES_PATH));
        trades
                .stream()
                .limit(6)
                .forEach(aggTrade -> this.candlestickDeque.offer(PriceMapper.INSTANCE.aggTradeToPrice(aggTrade)));
    }

    @Test
    void findNewLowestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(new BigDecimal("0.00001"));
        candlestick.setCloseTime(new Date().getTime());
        this.candlestickDeque.offer(candlestick);

        //when
        Candlestick lowestCandlestick = this.priceUtils.findNewLowestPrice(this.candlestickDeque);

        //then
        Assertions.assertEquals(candlestick, lowestCandlestick, PriceErrorMessage.PRICE_ERROR);

    }

    @Test
    void findNewHighestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(new BigDecimal("99999999"));
        candlestick.setCloseTime(new Date().getTime());
        this.candlestickDeque.offer(candlestick);

        //when
        Candlestick highestCandlestick = this.priceUtils.findNewHighestPrice(this.candlestickDeque);

        //then
        Assertions.assertEquals(candlestick, highestCandlestick, PriceErrorMessage.PRICE_ERROR);

    }
}
