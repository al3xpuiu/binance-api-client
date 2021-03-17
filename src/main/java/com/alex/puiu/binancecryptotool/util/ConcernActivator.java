package com.alex.puiu.binancecryptotool.util;

import lombok.Getter;

@Getter
public class ConcernActivator {

    private static boolean sellerActive;
    private static boolean buyerActive;
    private static boolean priceManagerActive = true;

    public static synchronized void activateSeller() {
        sellerActive = true;
        buyerActive = false;
        priceManagerActive = false;
    }

    public static synchronized void activateBuyer() {
        buyerActive = true;
        sellerActive = false;
        priceManagerActive = false;
    }

    public static synchronized void activatePriceManager() {
        priceManagerActive = true;
        sellerActive = false;
        buyerActive = false;
    }
}
