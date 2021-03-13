package com.alex.puiu.binancecryptotool.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Price {

    private BigDecimal value;
    private long time;

    public Price() {
        this.value = BigDecimal.ZERO;
    }
}
