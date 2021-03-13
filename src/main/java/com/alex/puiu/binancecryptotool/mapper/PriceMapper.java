package com.alex.puiu.binancecryptotool.mapper;

import com.alex.puiu.binancecryptotool.model.AggTrade;
import com.alex.puiu.binancecryptotool.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    @Mappings({
            @Mapping(target = "value", source = "price"),
            @Mapping(target = "time", source = "tradeTime")
    })
    Price aggTradeToPrice(AggTrade aggTrade);
}
