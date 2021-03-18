package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.mapper.PriceMapper;
import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.manager.model.CandlestickManager;
import com.alex.puiu.binancecryptotool.manager.model.CandlestickManagerFixPeriod;
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

class CandlestickManagerServiceImplTest {

    private CandlestickManagerService candlestickManagerService;
    private final CandlestickManager candlestickManager = new CandlestickManagerFixPeriod(240, RecordDuration.TWENTY_FOUR_HOURS);
    private static final BigDecimal PRICE_VALUE_ZERO = BigDecimal.ZERO;
    private static final BigDecimal PRICE_VALUE_ONE = BigDecimal.ONE;
    private static final BigDecimal LOWEST_PRICE_VALUE = new BigDecimal("0.00000001");
    private static final BigDecimal HIGHEST_PRICE_VALUE = new BigDecimal("9999999999");

    @Mock
    private PriceUtils priceUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.candlestickManagerService = new CandlestickManagerServiceImpl(this.priceUtils, this.candlestickManager);
        ObjectUtils<AggTrade> objectUtils = new ObjectUtilsImpl<>();

        List<AggTrade> trades = objectUtils.readObjectsFromFile(new File(SERIALIZED_AGG_TRADES_PATH));
        Deque<Candlestick> candlestickDeque = candlestickManager.getCandlestickDeque();
        trades
                .stream()
                .limit(6)
                .forEach(aggTrade -> candlestickDeque.offer(PriceMapper.INSTANCE.aggTradeToPrice(aggTrade)));
    }

    @Test
    void addPriceWhenQueueIsFull() {
        //Given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(PRICE_VALUE_ONE);
        candlestick.setCloseTime(new Date().getTime());
        CandlestickManagerService spiedService = Mockito.spy(candlestickManagerService);

        //When
        Mockito.doNothing().when(spiedService).updateCandlesticks(Mockito.any(), Mockito.any());
        spiedService.addCandlestick(candlestick);
        Candlestick insertedElement = this.candlestickManager.getCandlestickDeque().getLast();

        //Then
        Assertions.assertEquals(insertedElement.getClose(), candlestick.getClose(), VALUE_ERROR);
        Assertions.assertEquals(insertedElement.getCloseTime(), candlestick.getCloseTime(), DATE_ERROR);
    }

    @Test
    void addPriceWhenQueueIsNotFull() {
        //Given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(PRICE_VALUE_ONE);
        candlestick.setCloseTime(new Date().getTime());
        CandlestickManagerService spiedService = Mockito.spy(candlestickManagerService);
        this.candlestickManager.getCandlestickDeque().pop();

        //When
        Mockito.doNothing().when(spiedService).updateCandlesticks(Mockito.any(), Mockito.any());
        spiedService.addCandlestick(candlestick);
        Candlestick insertedElement = this.candlestickManager.getCandlestickDeque().getLast();

        //Then
        Assertions.assertEquals(insertedElement.getClose(), candlestick.getClose(), VALUE_ERROR);
        Assertions.assertEquals(insertedElement.getCloseTime(), candlestick.getCloseTime(), DATE_ERROR);
    }

    @Test
    void updateLowestPriceWhenNewPriceIsLowerThenTheCurrentLowestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(LOWEST_PRICE_VALUE);
        candlestick.setCloseTime(new Date().getTime());
        this.candlestickManager.getCandlestickDeque().offer(candlestick);
        this.candlestickManager.setLowestCandlestick(this.candlestickManager.getCandlestickDeque().peek());

        //when
        boolean result = this.candlestickManagerService.updateLowestCandlestick(candlestick, null);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(candlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateLowestPriceWhenCurrentLowestPriceWasDeletedFromTheQueue() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ONE);
        deletedCandlestick.setCloseTime(new Date().getTime());
        this.candlestickManager.setLowestCandlestick(deletedCandlestick);

        Candlestick newLowestCandlestick = new Candlestick();
        newLowestCandlestick.setClose(LOWEST_PRICE_VALUE);
        newLowestCandlestick.setCloseTime(new Date().getTime());

        //when
        Mockito.when(this.priceUtils.findNewLowestPrice(Mockito.any())).thenReturn(newLowestCandlestick);
        boolean result = this.candlestickManagerService.updateLowestCandlestick(null, deletedCandlestick);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(newLowestCandlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateLowestPriceLowestPriceWasNotUpdated() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ONE);
        deletedCandlestick.setCloseTime(new Date().getTime());
        Candlestick lowestCandlestick = this.candlestickManager.getCandlestickDeque().peek();
        this.candlestickManager.setLowestCandlestick(lowestCandlestick);

        Candlestick addedCandlestick = new Candlestick();
        addedCandlestick.setClose(PRICE_VALUE_ONE);
        addedCandlestick.setCloseTime(new Date().getTime());

        //when
        boolean result = this.candlestickManagerService.updateLowestCandlestick(addedCandlestick, deletedCandlestick);

        //then
        Assertions.assertFalse(result, BOOLEAN_ERROR);
        Assertions.assertNotEquals(addedCandlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);
        Assertions.assertEquals(lowestCandlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);
    }

    @Test
    void updateHighestPriceWhenNewPriceIsHigherThenTheCurrentHighestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(HIGHEST_PRICE_VALUE);
        candlestick.setCloseTime(new Date().getTime());
        this.candlestickManager.getCandlestickDeque().offer(candlestick);
        this.candlestickManager.setHighestCandlestick(this.candlestickManager.getCandlestickDeque().peek());

        //when
        boolean result = this.candlestickManagerService.updateHighestCandlestick(candlestick, null);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(candlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateHighestPriceWhenCurrentHighestPriceWasDeletedFromTheQueue() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ONE);
        deletedCandlestick.setCloseTime(new Date().getTime());
        this.candlestickManager.setHighestCandlestick(deletedCandlestick);

        Candlestick newHighestCandlestick = new Candlestick();
        newHighestCandlestick.setClose(HIGHEST_PRICE_VALUE);
        newHighestCandlestick.setCloseTime(new Date().getTime());

        //when
        Mockito.when(this.priceUtils.findNewHighestPrice(Mockito.any())).thenReturn(newHighestCandlestick);
        boolean result = this.candlestickManagerService.updateHighestCandlestick(null, deletedCandlestick);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(newHighestCandlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateHighestPriceHighestPriceWasNotUpdated() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ZERO);
        deletedCandlestick.setCloseTime(new Date().getTime());
        Candlestick highestCandlestick = this.candlestickManager.getCandlestickDeque().peek();
        this.candlestickManager.setHighestCandlestick(highestCandlestick);

        Candlestick addedCandlestick = new Candlestick();
        addedCandlestick.setClose(PRICE_VALUE_ZERO);
        addedCandlestick.setCloseTime(new Date().getTime());

        //when
        boolean result = this.candlestickManagerService.updateHighestCandlestick(addedCandlestick, deletedCandlestick);

        //then
        Assertions.assertFalse(result, BOOLEAN_ERROR);
        Assertions.assertNotEquals(addedCandlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);
        Assertions.assertEquals(highestCandlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);
    }

    @Test
    void updateLatestPrice() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(PRICE_VALUE_ONE);
        candlestick.setCloseTime(new Date().getTime());
        //when
        boolean result = this.candlestickManagerService.updateLatestCandlestick(candlestick);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(candlestick, this.candlestickManager.getLatestCandlestick(),VALUE_ERROR);
    }
}
