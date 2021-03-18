package com.alex.puiu.binancecryptotool.manager.mapper;

import com.alex.puiu.binancecryptotool.manager.model.AggTrade;
import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    @Mappings({
            @Mapping(target = "close", source = "price"),
            @Mapping(target = "closeTime", source = "tradeTime")
    })
    Candlestick aggTradeToPrice(AggTrade aggTrade);
}
