package com.alex.puiu.binancecryptotool.mapper;

import com.alex.puiu.binancecryptotool.model.AggTrade;
import com.alex.puiu.binancecryptotool.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Mapper
public interface PriceMapper {

    PriceMapper INSTANCE = Mappers.getMapper(PriceMapper.class);

    @Mappings({
            @Mapping(target = "value", source = "price"),
            @Mapping(target = "time", source = "tradeTime", qualifiedByName = "toLocalDateTime")
    })
    Price aggTradeToPrice(AggTrade aggTrade);


    @Named("toLocalDateTime")
    default LocalDateTime fromEpochMillis(long epochMillis) {
        return LocalDateTime.ofEpochSecond(epochMillis/1000, 0, ZoneOffset.of(ZoneId.systemDefault().getId()));
    }
}
