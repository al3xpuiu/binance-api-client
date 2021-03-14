package com.alex.puiu.binancecryptotool.util;

import com.alex.puiu.binancecryptotool.model.Price;

import java.util.Collection;

public interface PriceUtils {
    Price findNewLowestPrice(Collection<Price> priceCollection);
    Price findNewHighestPrice(Collection<Price> priceCollection);
}
