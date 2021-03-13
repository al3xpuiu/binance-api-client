package com.alex.puiu.binancecryptotool;

import com.alex.puiu.binancecryptotool.util.ObjectUtils;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class BinanceCryptoToolApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BinanceCryptoToolApplication.class, args);

        ObjectUtils<Candlestick> objectUtils = (ObjectUtils) applicationContext.getBean("objectUtilsImpl");
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("API-KEY", "SECRET");
        BinanceApiRestClient client = factory.newRestClient();

        client.ping();
        long serverTime = client.getServerTime();
        System.out.println(serverTime);

        BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();

        List<Candlestick> candlesticks = client.getCandlestickBars("ADAEUR", CandlestickInterval.FIFTEEN_MINUTES, 1000, null, null);
        File file = new File("src/main/resources/static/objects.txt");
        objectUtils.writeObjectsToFile(candlesticks, file);
        System.exit(0);
//        System.out.println(candlesticks);
//        webSocketClient.onCandlestickEvent("ethbtc", CandlestickInterval.FIFTEEN_MINUTES, response -> System.out.println(response));
//        webSocketClient.onAggTradeEvent("doteur".toLowerCase(), new BinanceApiCallback<AggTradeEvent>() {
//            @Override
//            public void onResponse(final AggTradeEvent response) {
//                System.out.println(response);
//            }
//
//            @Override
//            public void onFailure(final Throwable cause) {
//                System.err.println("Web socket failed");
//                cause.printStackTrace(System.err);
//            }
//        });
    }

}