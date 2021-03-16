package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Price;

import java.util.Collection;

public interface PriceUtils {
    Price findNewLowestPrice(Collection<Price> priceCollection);
    Price findNewHighestPrice(Collection<Price> priceCollection);
}
