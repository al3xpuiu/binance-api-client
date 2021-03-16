package com.alex.puiu.binancecryptotool.manager.config;

import com.alex.puiu.binancecryptotool.manager.model.PriceManager;
import com.alex.puiu.binancecryptotool.manager.model.PriceManagerFixPeriod;
import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceCryptoToolConfig {

    @Bean
    public PriceManager fixPeriodPriceManager(@Value("${interval_prices_duration}") int intervalMinutes, @Value("${length_of_recorder_time}") RecordDuration recordDuration) {
        return new PriceManagerFixPeriod(intervalMinutes, recordDuration);
    }
}
