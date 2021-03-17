package com.alex.puiu.binancecryptotool.binanceconnector.mapper;

import com.alex.puiu.binancecryptotool.binanceconnector.model.Candlestick;
import com.alex.puiu.binancecryptotool.manager.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CandlestickMapper {

    CandlestickMapper INSTANCE = Mappers.getMapper(CandlestickMapper.class);

    Candlestick toAppCandlestick(com.binance.api.client.domain.market.Candlestick candlestick);
    Price fromCandlestick(com.binance.api.client.domain.market.Candlestick candlestick);
}
