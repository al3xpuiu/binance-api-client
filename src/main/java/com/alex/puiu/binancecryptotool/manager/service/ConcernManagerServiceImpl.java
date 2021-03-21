package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.seller.service.SellerService;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ConcernManagerServiceImpl implements ConcernManagerService {

    private final SellerService service;

    public ConcernManagerServiceImpl(SellerService service) {
        this.service = service;
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
}
