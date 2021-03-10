package com.alex.puiu.binancecryptotool.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Price {

    private BigDecimal value;
    private LocalDateTime time;

    public Price() {
        this.value = BigDecimal.ZERO;
    }
}
