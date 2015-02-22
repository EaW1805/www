package com.eaw1805.www.controllers.payments;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton Class for PayPalAPIInterfaceServiceService.
 */
public class PaypalManager {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PaypalManager.class);

    /**
     * static instance(ourInstance) initialized as null.
     */
    private static PaypalManager ourInstance = null;

    /**
     * PayPal API interface.
     */
    private PayPalAPIInterfaceServiceService service;

    /**
     * The name of the SDK file containing the credentials..
     */
    private static final String PAYPAL_FILE = "paypal.properties";

    /**
     * The currency code type.
     */
    public static final CurrencyCodeType CURRENCY_CODE_TYPE = CurrencyCodeType.EUR;

    /**
     * The Payment action code type.
     */
    public static final PaymentActionCodeType PAYMENT_ACTION_CODE_TYPE = PaymentActionCodeType.SALE;

    /**
     * The Payment item description..
     */
    public static final String PAYMENT_ITEM_DESCRIPTION = "EaW Credits";

    /**
     * Valid amount of money in Euros.
     */
    public static final List<Integer> VALID_AMOUNTS = new ArrayList<Integer>(Arrays.asList(20, 30, 50, 100));

    /**
     * Pricing.
     */
    private final HashMap<String, Integer> pricing = new HashMap<String, Integer>();

    /**
     * Private constructor.
     */
    private PaypalManager() {
        try {
            service =
                    new PayPalAPIInterfaceServiceService(this.getClass().getClassLoader().getResource(PAYPAL_FILE).getPath());
        } catch (final IOException e) {
            LOGGER.error(e);
        }
        pricing.put("20", 200);
        pricing.put("30", 330);
        pricing.put("50", 560);
        pricing.put("100", 1200);
    }

    /**
     * Returns the PaypalManager instance.
     *
     * @return the PaypalManager
     */
    public static PaypalManager getInstance() {
        synchronized (PaypalManager.class) {
            if (ourInstance == null) {
                ourInstance = new PaypalManager();
            }
        }

        return ourInstance;
    }

    /**
     * Return the PayPal API Service.
     *
     * @return the PayPalAPIInterfaceServiceService
     */
    public PayPalAPIInterfaceServiceService getService() {
        return service;
    }

    /**
     * Return game coins according to the input credits amount(euros).
     *
     * @param value the credits amount
     * @return an Integer with the game coins
     */
    public Integer getGameCoins(final String value) {
        return pricing.get(value);
    }
}
