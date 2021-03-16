package com.alex.puiu.binancecryptotool.manager.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class AggTrade implements Serializable {

    private BigDecimal price;
    private BigDecimal quantity;
    private long tradeTime;
    private boolean isBuyerMaker;

    public AggTrade() {
        this.price = BigDecimal.ZERO;
        this.quantity = BigDecimal.ZERO;
    }
}
