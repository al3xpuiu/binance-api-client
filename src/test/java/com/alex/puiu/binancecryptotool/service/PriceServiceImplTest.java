package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.model.AggTrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PriceServiceImplTest {

    private PriceService priceService;
    private List<AggTrade> aggTrades;

    @BeforeEach
    void setUp() {
        this.priceService = new PriceServiceImpl();
        this.aggTrades = new ArrayList<>();

    }

    @Test
    void averageTrade() {
        //given

        //when

        //then
    }
}
