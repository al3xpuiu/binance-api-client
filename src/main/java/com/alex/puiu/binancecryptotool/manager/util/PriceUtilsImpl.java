package com.alex.puiu.binancecryptotool.manager.util;

import com.alex.puiu.binancecryptotool.manager.model.Price;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;

@Component
public class PriceUtilsImpl implements PriceUtils {

    @Override
    public Price findNewLowestPrice(Collection<Price> priceCollection) {
        return priceCollection
                .stream()
                .min(Comparator.comparing(Price::getValue))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Price findNewHighestPrice(Collection<Price> priceCollection) {
        return priceCollection
                .stream()
                .max(Comparator.comparing(Price::getValue))
                .orElseThrow(IllegalStateException::new);
    }
}
