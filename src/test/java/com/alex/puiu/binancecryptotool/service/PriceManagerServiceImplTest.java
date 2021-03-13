package com.alex.puiu.binancecryptotool.service;

import com.alex.puiu.binancecryptotool.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.model.AggTrade;
import com.alex.puiu.binancecryptotool.model.Price;
import com.alex.puiu.binancecryptotool.model.PriceManager;
import com.alex.puiu.binancecryptotool.model.PriceManagerFixPeriod;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.alex.puiu.binancecryptotool.util.ObjectUtilsImpl;
import com.alex.puiu.binancecryptotool.util.PriceUtils;
import com.alex.puiu.binancecryptotool.util.RecordDuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Deque;
import java.util.List;

class PriceManagerServiceImplTest {

    private PriceManagerService priceManagerService;
    private ObjectUtils<AggTrade> objectUtils;
    private final PriceManager priceManager = new PriceManagerFixPeriod(240, RecordDuration.TWENTY_FOUR_HOURS);;
    @Mock
    private PriceUtils priceUtils;

    private static final String SERIALIZED_AGG_TRADES_PATH = "src/main/resources/static/aggTrades.txt";
    @BeforeEach
    void setUp() {
        this.priceManagerService = new PriceManagerServiceImpl(this.priceUtils, this.priceManager);
        this.objectUtils = new ObjectUtilsImpl<>();
        List<AggTrade> trades = this.objectUtils.readObjectsFromFile(new File(SERIALIZED_AGG_TRADES_PATH));
        Deque<Price> priceDeque = priceManager.getPriceDeque();
        trades
                .stream()
                .limit(6)
                .forEach(aggTrade -> priceDeque.offer(PriceMapper.INSTANCE.aggTradeToPrice(aggTrade)));
    }

    @Test
    void addPriceWhenQueueIsFull() {
        //Given
        Price price = new Price();
        price.setValue(BigDecimal.ONE);
        price.setTime(new Date().getTime());
        PriceManagerService spiedService = Mockito.spy(priceManagerService);

        //When
        Mockito.doNothing().when(spiedService).updatePrices(Mockito.any(), Mockito.any());
        spiedService.addPrice(price);

        //Then
        Price insertedElement = this.priceManager.getPriceDeque().getLast();
        Assertions.assertEquals(insertedElement.getValue(), price.getValue());
        Assertions.assertEquals(insertedElement.getTime(), price.getTime());
    }

    @Test
    void addPriceWhenQueueIsNotFull() {
        //Given
        Price price = new Price();
        price.setValue(BigDecimal.ONE);
        price.setTime(new Date().getTime());
        PriceManagerService spiedService = Mockito.spy(priceManagerService);
        this.priceManager.getPriceDeque().pop();

        //When
        Mockito.doNothing().when(spiedService).updatePrices(Mockito.any(), Mockito.any());
        spiedService.addPrice(price);

        //Then
        Price insertedElement = this.priceManager.getPriceDeque().getLast();
        Assertions.assertEquals(insertedElement.getValue(), price.getValue());
        Assertions.assertEquals(insertedElement.getTime(), price.getTime());
    }

    @Test
    void updatePrices() {
    }

    @Test
    void updateLowestPrice() {
    }

    @Test
    void updateHighestPrice() {
    }

    @Test
    void updateLatestPrice() {
    }

    @Test
    void updateIndicators() {
    }
}
