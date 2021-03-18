package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.message.PriceErrorMessage;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.alex.puiu.binancecryptotool.util.ObjectUtilsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.alex.puiu.binancecryptotool.Path.SERIALIZED_CANDLESTICKS_PATH;

class CandlestickUtilsImplTest {

    private PriceUtils priceUtils;
    private NavigableMap<Long, Candlestick> candlestickNavigableMap;
    @BeforeEach
    void setUp() {
        this.priceUtils = new PriceUtilsImpl();
        this.candlestickNavigableMap = new TreeMap<>();
        ObjectUtils<Candlestick> objectUtils = new ObjectUtilsImpl<>();

        List<Candlestick> trades = objectUtils.readObjectsFromFile(new File(SERIALIZED_CANDLESTICKS_PATH));
        trades
                .stream()
                .limit(6)
                .forEach(candlestick -> this.candlestickNavigableMap.put(candlestick.getOpenTime(), candlestick));
    }

    @Test
    void findNewLowestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(new BigDecimal("0.00001"));
        candlestick.setOpenTime(new Date().getTime());
        this.candlestickNavigableMap.put(candlestick.getOpenTime(), candlestick);

        //when
        Candlestick lowestCandlestick = this.priceUtils.findNewLowestPrice(this.candlestickNavigableMap);

        //then
        Assertions.assertEquals(candlestick, lowestCandlestick, PriceErrorMessage.PRICE_ERROR);

    }

    @Test
    void findNewHighestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(new BigDecimal("99999999"));
        candlestick.setOpenTime(new Date().getTime());
        this.candlestickNavigableMap.put(candlestick.getOpenTime(), candlestick);

        //when
        Candlestick highestCandlestick = this.priceUtils.findNewHighestPrice(this.candlestickNavigableMap);

        //then
        Assertions.assertEquals(candlestick, highestCandlestick, PriceErrorMessage.PRICE_ERROR);

    }
}
