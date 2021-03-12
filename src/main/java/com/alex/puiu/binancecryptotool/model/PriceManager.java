package com.alex.puiu.binancecryptotool.model;

import com.alex.puiu.binancecryptotool.util.RecordDuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Deque;

@Getter
@Setter
public abstract class PriceManager {

    private String symbol;
    private Price lowestPrice;
    private Price highestPrice;
    private Price latestPrice;

    public PriceManager() {
    }

    public abstract Deque<Price> getPriceDeque();
    public abstract int getMaximumCollectionLength();
    public abstract RecordDuration getRecordDuration();
}
