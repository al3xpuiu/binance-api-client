package com.alex.puiu.binancecryptotool.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class Price {

    private BigDecimal value;
    private long time;

    public Price() {
        this.value = BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return time == price.time && Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, time);
    }
}
