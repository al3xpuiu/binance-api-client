package com.alex.puiu.binancecryptotool.binanceconnector.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Candlestick implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long openTime;

    private String open;

    private String high;

    private String low;

    private String close;

    private String volume;

    private Long closeTime;

    private String quoteAssetVolume;

    private Long numberOfTrades;

    private String takerBuyBaseAssetVolume;

    private String takerBuyQuoteAssetVolume;
}
