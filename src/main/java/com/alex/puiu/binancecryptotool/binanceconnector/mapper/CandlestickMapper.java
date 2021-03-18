package com.alex.puiu.binancecryptotool.binanceconnector.mapper;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CandlestickMapper {

    CandlestickMapper INSTANCE = Mappers.getMapper(CandlestickMapper.class);

    Candlestick fromCandlestick(com.binance.api.client.domain.market.Candlestick candlestick);
}
