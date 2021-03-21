package com.alex.puiu.binancecryptotool.manager.service;

import com.alex.puiu.binancecryptotool.manager.model.Candlestick;
import com.alex.puiu.binancecryptotool.manager.model.CandlestickManager;
import com.alex.puiu.binancecryptotool.manager.util.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandlestickManagerServiceImpl implements CandlestickManagerService {

    private final CandlestickManager candlestickManager;
    private final ConcernManagerService concernManagerService;
    private final PriceUtils priceUtils;

    @Autowired
    public CandlestickManagerServiceImpl(PriceUtils priceUtils,
                                         CandlestickManager candlestickManager,
                                         ConcernManagerService concernManagerService) {
        this.candlestickManager = candlestickManager;
        this.priceUtils = priceUtils;
        this.concernManagerService = concernManagerService;
    }

    @Override
    public void addCandlestick(Candlestick candlestick) {
        Candlestick deletedCandlestick;
        if (this.candlestickManager.getCandlestickMap().size() >= this.candlestickManager.getMaximumCollectionLength()) {
            deletedCandlestick = this.candlestickManager.getCandlestickMap().pollFirstEntry().getValue();
            this.candlestickManager.getCandlestickMap().put(candlestick.getOpenTime(), candlestick);
            this.updateCandlesticks(candlestick, deletedCandlestick);
            return;
        }
        this.candlestickManager.getCandlestickMap().put(candlestick.getOpenTime(), candlestick);
        this.updateCandlesticks(candlestick, null);
    }

    @Override
    public void updateCandlesticks(Candlestick candlestick, Candlestick deletedCandlestick) {
        updateLatestCandlestick(candlestick);
        boolean lowestPriceChanged = updateLowestCandlestick(candlestick, deletedCandlestick);
        boolean highestPriceChanged = updateHighestCandlestick(candlestick, deletedCandlestick);

        if (this.concernManagerService.isBuyerActive()) {
            this.concernManagerService.addCandlestickToBuyer(candlestick);
            System.out.println("Lowest Price: " + candlestick);
            return;
        }

        if (this.concernManagerService.isSellerActive()) {
            this.concernManagerService.addCandlestickToSeller(candlestick);
            System.out.println("Highest Price" + candlestick);
            return;
        }

        if (highestPriceChanged) {
            this.concernManagerService.activateSeller(candlestick);
            System.out.println("Seller activated with on: " + candlestick);
        }
    }

    @Override
    public boolean updateLowestCandlestick(Candlestick candlestick, Candlestick deletedCandlestick) {
        Candlestick currentLowestCandlestick = this.candlestickManager.getLowestCandlestick();
        if (currentLowestCandlestick == null || candlestick != null && currentLowestCandlestick.getClose().compareTo(candlestick.getClose()) > 0) {
            this.candlestickManager.setLowestCandlestick(candlestick);
            return true;
        }
        if (deletedCandlestick != null && deletedCandlestick.equals(currentLowestCandlestick)) {
            this.candlestickManager.setLowestCandlestick(this.priceUtils.findNewLowestPrice(this.candlestickManager.getCandlestickMap()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateHighestCandlestick(Candlestick candlestick, Candlestick deletedCandlestick) {
        Candlestick currentHighestCandlestick = this.candlestickManager.getHighestCandlestick();
        if (currentHighestCandlestick == null || candlestick != null && currentHighestCandlestick.getClose().compareTo(candlestick.getClose()) < 0) {
            this.candlestickManager.setHighestCandlestick(candlestick);
            return true;
        }
        if (deletedCandlestick != null && deletedCandlestick.equals(currentHighestCandlestick)) {
            this.candlestickManager.setHighestCandlestick(this.priceUtils.findNewHighestPrice(this.candlestickManager.getCandlestickMap()));
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLatestCandlestick(Candlestick candlestick) {
        if (candlestick != null) {
            this.candlestickManager.setLatestCandlestick(candlestick);
            return true;
        }
        return false;
    }
}
