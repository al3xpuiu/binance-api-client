package com.alex.puiu.binancecryptotool.manager.service;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class PriceServiceImplTest {

    private PriceService priceService;
    private List<AggTrade> aggTrades;
    private ObjectUtils<AggTrade> objectUtils;
    private File file = new File("src/main/resources/static/aggTrades.txt");

    private static final String PRICE_1 = "0.01000000";
    private static final String PRICE_2 = "0.02000000";
    private static final BigDecimal EXPECTED_PRICE = new BigDecimal("0.01500000");

    private static final String QUANTITY_1 = "0.00300000";
    private static final String QUANTITY_2 = "0.00600000";
    private static final BigDecimal EXPECTED_QUANTITY = new BigDecimal( "0.00450000");

    private static final long DATE_1 = new Date().getTime();
    private static final long DATE_2 = DATE_1 + 2000;
    private static final long EXPECTED_DATE = (DATE_1 + DATE_2)/2 + 1000;

    @BeforeEach
    void setUp() {
        this.priceService = new PriceServiceImpl();
        this.objectUtils = new ObjectUtilsImpl<>();
        this.aggTrades = new ArrayList<>();
        AggTrade aggTrade = new AggTrade();
        aggTrade.setPrice(new BigDecimal(PRICE_1));
        aggTrade.setQuantity(new BigDecimal(QUANTITY_1));
        long date1 = new Date().getTime();
        aggTrade.setTradeTime(date1);
        aggTrade.setBuyerMaker(true);
        this.aggTrades.add(aggTrade);

        aggTrade = new AggTrade();
        aggTrade.setPrice(new BigDecimal(PRICE_2));
        aggTrade.setQuantity(new BigDecimal(QUANTITY_2));
        aggTrade.setTradeTime(date1 + 2000);
        aggTrade.setBuyerMaker(true);
        this.aggTrades.add(aggTrade);
    }

    @Test()
    void averageTrade() {
        //give

        //when
        Price price = this.priceService.averageTrade(this.aggTrades);

        //then
        Assertions.assertEquals(EXPECTED_PRICE, price.getClose(), PriceErrorMessage.VALUE_ERROR);
    }
}
