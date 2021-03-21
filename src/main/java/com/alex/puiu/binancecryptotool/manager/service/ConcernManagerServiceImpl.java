package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.seller.service.SellerService;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
//should have web request scope
public class ConcernManagerServiceImpl implements ConcernManagerService {

    private final SellerService sellerService;

    public ConcernManagerServiceImpl(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    private boolean sellerActive;
    private boolean buyerActive;
    private boolean priceManagerActive = true;

    @Override
    public void activateSeller(Candlestick highestCandlestick) {
        sellerActive = true;
        buyerActive = false;
        priceManagerActive = false;
    }

    @Override
    public void activateBuyer(Candlestick lowestCandlestick) {
        buyerActive = true;
        sellerActive = false;
        priceManagerActive = false;
    }

    @Override
    public void activatePriceManager() {
        priceManagerActive = true;
        sellerActive = false;
        buyerActive = false;
    }

    @Override
    public void addCandlestickToSeller(Candlestick highestCandlestick) {
        this.sellerService.studyCandlestick(highestCandlestick);
    }

    @Override
    public void addCandlestickToBuyer(Candlestick highestCandlestick) {

    }
}
