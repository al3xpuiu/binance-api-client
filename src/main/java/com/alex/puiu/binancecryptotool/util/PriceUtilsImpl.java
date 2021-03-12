package com.alex.puiu.binancecryptotool.util;

import com.alex.puiu.binancecryptotool.model.Price;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PriceUtilsImpl implements PriceUtils {

    public Price findNewLowestPrice(Collection<Price> priceCollection) {
        return priceCollection
                .stream()
                .min((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                .orElseThrow(IllegalStateException::new);
    }
}
