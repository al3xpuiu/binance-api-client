package com.alex.puiu.binancecryptotool.manager.config;

import com.alex.puiu.binancecryptotool.manager.model.CandlestickManager;
import com.alex.puiu.binancecryptotool.manager.model.CandlestickManagerFixPeriod;
import com.alex.puiu.binancecryptotool.manager.util.RecordDuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceCryptoToolConfig {

    @Bean
    public CandlestickManager fixPeriodPriceManager(@Value("${interval_prices_duration}") int intervalMinutes, @Value("${length_of_recorder_time}") RecordDuration recordDuration) {
        return new CandlestickManagerFixPeriod(intervalMinutes, recordDuration);
    }
}
