package com.alex.puiu.binancecryptotool.manager.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class Price {

    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;

    private long openTime;
    private long closeTime;

    private BigDecimal volume;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal makerBuyBaseAssetVolume;

    private int numberOfTrades;

    public Price() {
        this.open = BigDecimal.ZERO;
        this.close = BigDecimal.ZERO;
        this.low = BigDecimal.ZERO;
        this.high = BigDecimal.ZERO;
        this.volume = BigDecimal.ZERO;
        this.takerBuyBaseAssetVolume = BigDecimal.ZERO;
        this.makerBuyBaseAssetVolume = BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return closeTime == price.closeTime && Objects.equals(close, price.close);
    }

    @Override
    public int hashCode() {
        return Objects.hash(close, closeTime);
    }

    @Override
    public String toString() {
        return "Price{" +
                "close=" + close +
                ", closeTime=" + closeTime +
                '}';
    }
}
