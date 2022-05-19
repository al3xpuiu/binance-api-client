package com.alex.puiu.binancecryptotool.strategy;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

class DefaultSellerStrategyTest {

    Strategy strategy;

    private static final int DROP_LIMIT = 2;

    @BeforeEach
    void setUp() {
        this.strategy = new DefaultSellerStrategy(DROP_LIMIT);
    }

    @Test
    void executeWithOnlyOneCandlestick() {
        //given
        LocalDateTime localDateTime = LocalDateTime.now().minus(20, ChronoUnit.DAYS);

        Candlestick candlestick1 = new Candlestick();
        candlestick1.setOpenTime(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        candlestick1.setCloseTime(localDateTime.plus(15, ChronoUnit.MINUTES).toInstant(ZoneOffset.UTC).toEpochMilli());
        candlestick1.setClose(new BigDecimal("1"));

        //when
        boolean result = this.strategy.execute(candlestick1);

        //then
        Assertions.assertFalse(result);
        Assertions.assertEquals(1, this.strategy.getCandlestickNavigableMap().size());
        Assertions.assertEquals(candlestick1, this.strategy.getCandlestickNavigableMap().firstEntry().getValue());
    }

    @Test
    void executeWithTwoCandlesticksFirstGreaterThenTheSecond() {

        //given
        LocalDateTime localDateTime = LocalDateTime.now().minus(20, ChronoUnit.DAYS);

        Candlestick candlestick1 = new Candlestick();
        candlestick1.setOpenTime(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        candlestick1.setCloseTime(localDateTime.plus(15, ChronoUnit.MINUTES).toInstant(ZoneOffset.UTC).toEpochMilli());
        candlestick1.setClose(new BigDecimal("1"));

        Candlestick candlestick2 = new Candlestick();
        candlestick2.setOpenTime(localDateTime.plusMinutes(15).toInstant(ZoneOffset.UTC).toEpochMilli());
        candlestick2.setCloseTime(localDateTime.plus(30, ChronoUnit.MINUTES).toInstant(ZoneOffset.UTC).toEpochMilli());
        candlestick2.setClose(new BigDecimal("0.5"));

        //when
        this.strategy.execute(candlestick1);
        boolean result = this.strategy.execute(candlestick2);

        //then
        Assertions.assertFalse(result);
        Assertions.assertEquals(2, this.strategy.getCandlestickNavigableMap().size());
        Assertions.assertEquals(candlestick1, this.strategy.getCandlestickNavigableMap().firstEntry().getValue());
        Assertions.assertEquals(candlestick2, this.strategy.getCandlestickNavigableMap().lastEntry().getValue());
        Assertions.assertEquals(1, this.strategy.getConsecutiveTicks());
    }

}
