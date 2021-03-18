package com.alex.puiu.binancecryptotool.manager.service;

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
import java.util.List;
import java.util.NavigableMap;

import static com.alex.puiu.binancecryptotool.Path.SERIALIZED_CANDLESTICKS_PATH;
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
        ObjectUtils<Candlestick> objectUtils = new ObjectUtilsImpl<>();

        List<Candlestick> trades = objectUtils.readObjectsFromFile(new File(SERIALIZED_CANDLESTICKS_PATH));
        NavigableMap<Long, Candlestick> candlestickNavigableMap = candlestickManager.getCandlestickMap();
        trades
                .stream()
                .limit(6)
                .forEach(candlestick -> candlestickNavigableMap.put(candlestick.getOpenTime(), candlestick));
    }

    @Test
    void addCandlestickWhenMapIsFull() {
        //Given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(PRICE_VALUE_ONE);
        candlestick.setCloseTime(new Date().getTime());
        CandlestickManagerService spiedService = Mockito.spy(candlestickManagerService);

        //When
        Mockito.doNothing().when(spiedService).updateCandlesticks(Mockito.any(), Mockito.any());
        spiedService.addCandlestick(candlestick);
        Candlestick insertedElement = this.candlestickManager.getCandlestickMap().get(candlestick.getOpenTime());

        //Then
        Assertions.assertEquals(insertedElement.getOpenTime(), candlestick.getOpenTime(), VALUE_ERROR);
        Assertions.assertEquals(insertedElement.getCloseTime(), candlestick.getCloseTime(), DATE_ERROR);
    }

    @Test
    void addCandlestickWhenMapIsNotFull() {
        //Given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(PRICE_VALUE_ONE);
        candlestick.setOpenTime(new Date().getTime());
        CandlestickManagerService spiedService = Mockito.spy(candlestickManagerService);
        this.candlestickManager.getCandlestickMap().pollFirstEntry();

        //When
        Mockito.doNothing().when(spiedService).updateCandlesticks(Mockito.any(), Mockito.any());
        spiedService.addCandlestick(candlestick);
        Candlestick insertedElement = this.candlestickManager.getCandlestickMap().get(candlestick.getOpenTime());

        //Then
        Assertions.assertEquals(insertedElement.getClose(), candlestick.getClose(), VALUE_ERROR);
        Assertions.assertEquals(insertedElement.getOpenTime(), candlestick.getOpenTime(), DATE_ERROR);
    }

    @Test
    void updateLowestCandlestickWhenNewCandlestickIsLowerThenTheCurrentLowestCandlestick() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(LOWEST_PRICE_VALUE);
        candlestick.setOpenTime(new Date().getTime());
        this.candlestickManager.setLowestCandlestick(this.candlestickManager.getCandlestickMap().firstEntry().getValue());
        this.candlestickManager.getCandlestickMap().put(candlestick.getOpenTime(), candlestick);

        //when
        boolean result = this.candlestickManagerService.updateLowestCandlestick(candlestick, null);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(candlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateLowestCandlestickWhenCurrentLowestCandlestickWasDeletedFromTheMap() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ONE);
        deletedCandlestick.setOpenTime(new Date().getTime());
        this.candlestickManager.setLowestCandlestick(deletedCandlestick);

        Candlestick newLowestCandlestick = new Candlestick();
        newLowestCandlestick.setClose(LOWEST_PRICE_VALUE);
        newLowestCandlestick.setOpenTime(new Date().getTime());

        //when
        Mockito.when(this.priceUtils.findNewLowestPrice(Mockito.any())).thenReturn(newLowestCandlestick);
        boolean result = this.candlestickManagerService.updateLowestCandlestick(null, deletedCandlestick);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(newLowestCandlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateLowestCandlestickLowestCandlestickWasNotUpdated() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ONE);
        deletedCandlestick.setOpenTime(new Date().getTime());
        Candlestick lowestCandlestick = this.candlestickManager.getCandlestickMap().firstEntry().getValue();
        this.candlestickManager.setLowestCandlestick(lowestCandlestick);

        Candlestick addedCandlestick = new Candlestick();
        addedCandlestick.setClose(PRICE_VALUE_ONE);
        addedCandlestick.setOpenTime(new Date().getTime());

        //when
        boolean result = this.candlestickManagerService.updateLowestCandlestick(addedCandlestick, deletedCandlestick);

        //then
        Assertions.assertFalse(result, BOOLEAN_ERROR);
        Assertions.assertNotEquals(addedCandlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);
        Assertions.assertEquals(lowestCandlestick, this.candlestickManager.getLowestCandlestick(),VALUE_ERROR);
    }

    @Test
    void updateHighestCandlestickWhenNewCandlestickIsHigherThenTheCurrentHighestCandlestick() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(HIGHEST_PRICE_VALUE);
        candlestick.setOpenTime(new Date().getTime());
        this.candlestickManager.getCandlestickMap().put(candlestick.getOpenTime(), candlestick);
        this.candlestickManager.setHighestCandlestick(this.candlestickManager.getCandlestickMap().firstEntry().getValue());

        //when
        boolean result = this.candlestickManagerService.updateHighestCandlestick(candlestick, null);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(candlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateHighestCandlestickWhenCurrentHighestCandlestickWasDeletedFromTheMap() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ONE);
        deletedCandlestick.setOpenTime(new Date().getTime());
        this.candlestickManager.setHighestCandlestick(deletedCandlestick);

        Candlestick newHighestCandlestick = new Candlestick();
        newHighestCandlestick.setClose(HIGHEST_PRICE_VALUE);
        newHighestCandlestick.setOpenTime(new Date().getTime());

        //when
        Mockito.when(this.priceUtils.findNewHighestPrice(Mockito.any())).thenReturn(newHighestCandlestick);
        boolean result = this.candlestickManagerService.updateHighestCandlestick(null, deletedCandlestick);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(newHighestCandlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);

    }

    @Test
    void updateHighestCandlestickHighestCandlestickWasNotUpdated() {
        //given
        Candlestick deletedCandlestick = new Candlestick();
        deletedCandlestick.setClose(PRICE_VALUE_ZERO);
        deletedCandlestick.setOpenTime(new Date().getTime());
        Candlestick highestCandlestick = this.candlestickManager.getCandlestickMap().firstEntry().getValue();
        this.candlestickManager.setHighestCandlestick(highestCandlestick);

        Candlestick addedCandlestick = new Candlestick();
        addedCandlestick.setClose(PRICE_VALUE_ZERO);
        addedCandlestick.setOpenTime(new Date().getTime());

        //when
        boolean result = this.candlestickManagerService.updateHighestCandlestick(addedCandlestick, deletedCandlestick);

        //then
        Assertions.assertFalse(result, BOOLEAN_ERROR);
        Assertions.assertNotEquals(addedCandlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);
        Assertions.assertEquals(highestCandlestick, this.candlestickManager.getHighestCandlestick(),VALUE_ERROR);
    }

    @Test
    void updateLatestCandlestick() {
        //given
        Candlestick candlestick = new Candlestick();
        candlestick.setClose(PRICE_VALUE_ONE);
        candlestick.setOpenTime(new Date().getTime());
        //when
        boolean result = this.candlestickManagerService.updateLatestCandlestick(candlestick);

        //then
        Assertions.assertTrue(result, BOOLEAN_ERROR);
        Assertions.assertEquals(candlestick, this.candlestickManager.getLatestCandlestick(),VALUE_ERROR);
    }
}
