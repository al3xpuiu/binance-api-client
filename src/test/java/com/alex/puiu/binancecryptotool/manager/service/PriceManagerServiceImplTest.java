package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Price;
import com.alex.puiu.binancecryptotool.manager.model.PriceManager;
import com.alex.puiu.binancecryptotool.manager.model.PriceManagerFixPeriod;
import com.alex.puiu.binancecryptotool.manager.util.PriceUtils;
import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.alex.puiu.binancecryptotool.util.ObjectUtilsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Deque;
import java.util.List;

import static com.alex.puiu.binancecryptotool.Path.SERIALIZED_AGG_TRADES_PATH;
import static com.alex.puiu.binancecryptotool.message.PriceErrorMessage.*;

class PriceManagerServiceImplTest {

    private PriceManagerService priceManagerService;
    private final PriceManager priceManager = new PriceManagerFixPeriod(240, RecordDuration.TWENTY_FOUR_HOURS);
    private static final BigDecimal PRICE_VALUE_ZERO = BigDecimal.ZERO;
    private static final BigDecimal PRICE_VALUE_ONE = BigDecimal.ONE;
    private static final BigDecimal LOWEST_PRICE_VALUE = new BigDecimal("0.00000001");
    private static final BigDecimal HIGHEST_PRICE_VALUE = new BigDecimal("9999999999");

    @Mock
    private PriceUtils priceUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.priceManagerService = new PriceManagerServiceImpl(this.priceUtils, this.priceManager);
        ObjectUtils<AggTrade> objectUtils = new ObjectUtilsImpl<>();

        List<AggTrade> trades = objectUtils.readObjectsFromFile(new File(SERIALIZED_AGG_TRADES_PATH));
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
        price.setValue(PRICE_VALUE_ONE);
        price.setTime(new Date().getTime());
        PriceManagerService spiedService = Mockito.spy(priceManagerService);

        //When
        Mockito.doNothing().when(spiedService).updatePrices(Mockito.any(), Mockito.any());
        spiedService.addPrice(price);
        Price insertedElement = this.priceManager.getPriceDeque().getLast();

        //Then
        Assertions.assertEquals(insertedElement.getValue(), price.getValue(), VALUE_ERROR);
        Assertions.assertEquals(insertedElement.getTime(), price.getTime(), DATE_ERROR);
    }

    @Test
    void addPriceWhenQueueIsNotFull() {
        //Given
        Price price = new Price();
        price.setValue(PRICE_VALUE_ONE);
        price.setTime(new Date().getTime());
        PriceManagerService spiedService = Mockito.spy(priceManagerService);
        this.priceManager.getPriceDeque().pop();

        //When
        Mockito.doNothing().when(spiedService).updatePrices(Mockito.any(), Mockito.any());
        spiedService.addPrice(price);
        Price insertedElement = this.priceManager.getPriceDeque().getLast();

        //Then
        Assertions.assertEquals(insertedElement.getValue(), price.getValue(), VALUE_ERROR);
        Assertions.assertEquals(insertedElement.getTime(), price.getTime(), DATE_ERROR);
    }

    @Test
    void updateLowestPriceWhenNewPriceIsLowerThenTheCurrentLowestPrice() {
        //given
        Price price = new Price();
        price.setValue(LOWEST_PRICE_VALUE);
        price.setTime(new Date().getTime());
        this.priceManager.getPriceDeque().offer(price);
        this.priceManager.setLowestPrice(this.priceManager.getPriceDeque().peek());

        //when
        boolean result = this.priceManagerService.updateLowestPrice(price, null);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(price, this.priceManager.getLowestPrice(),VALUE_ERROR);

    }

    @Test
    void updateLowestPriceWhenCurrentLowestPriceWasDeletedFromTheQueue() {
        //given
        Price deletedPrice = new Price();
        deletedPrice.setValue(PRICE_VALUE_ONE);
        deletedPrice.setTime(new Date().getTime());
        this.priceManager.setLowestPrice(deletedPrice);

        Price newLowestPrice = new Price();
        newLowestPrice.setValue(LOWEST_PRICE_VALUE);
        newLowestPrice.setTime(new Date().getTime());

        //when
        Mockito.when(this.priceUtils.findNewLowestPrice(Mockito.any())).thenReturn(newLowestPrice);
        boolean result = this.priceManagerService.updateLowestPrice(null, deletedPrice);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(newLowestPrice, this.priceManager.getLowestPrice(),VALUE_ERROR);

    }

    @Test
    void updateLowestPriceLowestPriceWasNotUpdated() {
        //given
        Price deletedPrice = new Price();
        deletedPrice.setValue(PRICE_VALUE_ONE);
        deletedPrice.setTime(new Date().getTime());
        Price lowestPrice = this.priceManager.getPriceDeque().peek();
        this.priceManager.setLowestPrice(lowestPrice);

        Price addedPrice = new Price();
        addedPrice.setValue(PRICE_VALUE_ONE);
        addedPrice.setTime(new Date().getTime());

        //when
        boolean result = this.priceManagerService.updateLowestPrice(addedPrice, deletedPrice);

        //then
        Assertions.assertFalse(result, BOOLEAN_ERROR);
        Assertions.assertNotEquals(addedPrice, this.priceManager.getLowestPrice(),VALUE_ERROR);
        Assertions.assertEquals(lowestPrice, this.priceManager.getLowestPrice(),VALUE_ERROR);
    }

    @Test
    void updateHighestPriceWhenNewPriceIsHigherThenTheCurrentHighestPrice() {
        //given
        Price price = new Price();
        price.setValue(HIGHEST_PRICE_VALUE);
        price.setTime(new Date().getTime());
        this.priceManager.getPriceDeque().offer(price);
        this.priceManager.setHighestPrice(this.priceManager.getPriceDeque().peek());

        //when
        boolean result = this.priceManagerService.updateHighestPrice(price, null);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(price, this.priceManager.getHighestPrice(),VALUE_ERROR);

    }

    @Test
    void updateHighestPriceWhenCurrentHighestPriceWasDeletedFromTheQueue() {
        //given
        Price deletedPrice = new Price();
        deletedPrice.setValue(PRICE_VALUE_ONE);
        deletedPrice.setTime(new Date().getTime());
        this.priceManager.setHighestPrice(deletedPrice);

        Price newHighestPrice = new Price();
        newHighestPrice.setValue(HIGHEST_PRICE_VALUE);
        newHighestPrice.setTime(new Date().getTime());

        //when
        Mockito.when(this.priceUtils.findNewHighestPrice(Mockito.any())).thenReturn(newHighestPrice);
        boolean result = this.priceManagerService.updateHighestPrice(null, deletedPrice);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(newHighestPrice, this.priceManager.getHighestPrice(),VALUE_ERROR);

    }

    @Test
    void updateHighestPriceHighestPriceWasNotUpdated() {
        //given
        Price deletedPrice = new Price();
        deletedPrice.setValue(PRICE_VALUE_ZERO);
        deletedPrice.setTime(new Date().getTime());
        Price highestPrice = this.priceManager.getPriceDeque().peek();
        this.priceManager.setHighestPrice(highestPrice);

        Price addedPrice = new Price();
        addedPrice.setValue(PRICE_VALUE_ZERO);
        addedPrice.setTime(new Date().getTime());

        //when
        boolean result = this.priceManagerService.updateHighestPrice(addedPrice, deletedPrice);

        //then
        Assertions.assertFalse(result, BOOLEAN_ERROR);
        Assertions.assertNotEquals(addedPrice, this.priceManager.getHighestPrice(),VALUE_ERROR);
        Assertions.assertEquals(highestPrice, this.priceManager.getHighestPrice(),VALUE_ERROR);
    }

    @Test
    void updateLatestPrice() {
        //given
        Price price = new Price();
        price.setValue(PRICE_VALUE_ONE);
        price.setTime(new Date().getTime());
        //when
        boolean result = this.priceManagerService.updateLatestPrice(price);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(price, this.priceManager.getLatestPrice(),VALUE_ERROR);
    }
}
