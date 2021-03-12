package com.alex.puiu.binancecryptotool.config;

import com.alex.puiu.binancecryptotool.model.PriceManager;
import com.alex.puiu.binancecryptotool.model.PriceManagerFixPeriod;
import com.alex.puiu.binancecryptotool.util.RecordDuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceCryptoToolConfig {

    @Bean
    public PriceManager fixPeriodPriceManager(@Value("${}") int intervalMinutes, @Value("${}") RecordDuration recordDuration) {
        return new PriceManagerFixPeriod(intervalMinutes, recordDuration);
    }
}
