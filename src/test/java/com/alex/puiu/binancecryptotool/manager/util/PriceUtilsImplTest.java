package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Price;
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

class PriceUtilsImplTest {

    private PriceUtils priceUtils;
    private Deque<Price> priceDeque;
    @BeforeEach
    void setUp() {
        this.priceUtils = new PriceUtilsImpl();
        this.priceDeque = new ArrayDeque<>();
        ObjectUtils<AggTrade> objectUtils = new ObjectUtilsImpl<>();

        List<AggTrade> trades = objectUtils.readObjectsFromFile(new File(SERIALIZED_AGG_TRADES_PATH));
        trades
                .stream()
                .limit(6)
                .forEach(aggTrade -> this.priceDeque.offer(PriceMapper.INSTANCE.aggTradeToPrice(aggTrade)));
    }

    @Test
    void findNewLowestPrice() {
        //given
        Price price = new Price();
        price.setClose(new BigDecimal("0.00001"));
        price.setCloseTime(new Date().getTime());
        this.priceDeque.offer(price);

        //when
        Price lowestPrice = this.priceUtils.findNewLowestPrice(this.priceDeque);

        //then
        Assertions.assertEquals(price, lowestPrice, PriceErrorMessage.PRICE_ERROR);

    }

    @Test
    void findNewHighestPrice() {
        //given
        Price price = new Price();
        price.setClose(new BigDecimal("99999999"));
        price.setCloseTime(new Date().getTime());
        this.priceDeque.offer(price);

        //when
        Price highestPrice = this.priceUtils.findNewHighestPrice(this.priceDeque);

        //then
        Assertions.assertEquals(price, highestPrice, PriceErrorMessage.PRICE_ERROR);

    }
}
