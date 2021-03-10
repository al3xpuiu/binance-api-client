package com.alex.puiu.binancecryptotool.dto;

import com.alex.puiu.binancecryptotool.util.Action;
import com.alex.puiu.binancecryptotool.util.Trend;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter
public class PriceManagerFixPeriod {

    private Deque<Price> priceQueue;
    private BigDecimal lowestPrice;
    private BigDecimal highestPrice;
    private BigDecimal latestPrice;
    private Trend trend;
    private boolean shouldBuy;
    private boolean shouldSell;
    private Action currentObjective;

    private String symbol;

    public PriceManagerFixPeriod() {
        this.priceQueue = new ArrayDeque<>();
        this.lowestPrice = BigDecimal.ZERO;
        this.highestPrice = BigDecimal.ZERO;
        this.latestPrice = BigDecimal.ZERO;
    }
}
