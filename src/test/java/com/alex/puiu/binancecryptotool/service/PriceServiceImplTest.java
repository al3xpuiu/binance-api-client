package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.model.AggTrade;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.alex.puiu.binancecryptotool.util.ObjectUtilsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class PriceServiceImplTest {

    private PriceService priceService;
    private List<AggTrade> aggTrades;
    private ObjectUtils<AggTrade> objectUtils;
    private File file = new File("src/main/resources/static/aggTrades.txt");
    @BeforeEach
    void setUp() {
        this.priceService = new PriceServiceImpl();
        this.objectUtils = new ObjectUtilsImpl<>();
        this.aggTrades = objectUtils.readObjectsFromFile(file);

    }

    @Test()
    void averageTrade() {
        //given

        //when

        //then
    }
}
