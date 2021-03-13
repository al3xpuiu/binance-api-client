package com.alex.puiu.binancecryptotool.mapper;

import com.alex.puiu.binancecryptotool.model.AggTrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AggTradeMapper {
    AggTradeMapper INSTANCE = Mappers.getMapper(AggTradeMapper.class);

    List<AggTrade> toAppAggTrades(List<com.binance.api.client.domain.market.AggTrade> aggTrades);

    @Mappings({
            @Mapping(target = "price", source = "price", qualifiedByName = "priceToBigDecimal"),
            @Mapping(target = "quantity", source = "quantity", qualifiedByName = "quantityToBigDecimal")
    })
    AggTrade toAppAggTrade(com.binance.api.client.domain.market.AggTrade aggTrade);

    @Named("priceToBigDecimal")
    default BigDecimal priceToBigDecimal(String price) {
        return new BigDecimal(price);
    }

    @Named("quantityToBigDecimal")
    default BigDecimal quantityToBigDecimal(String quantity) {
        return new BigDecimal(quantity);
    }
}
