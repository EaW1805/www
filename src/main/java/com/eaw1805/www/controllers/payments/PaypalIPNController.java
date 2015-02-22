package com.eaw1805.www.controllers.payments;

import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;


/**
 * Handles paypal IPN messages.
 */
@org.springframework.stereotype.Controller
public class PaypalIPNController
        extends BaseController {
    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PaypalIPNController.class);

    public PaypalIPNController() {
        super();
    }

    @RequestMapping(value = "/paypal/paypalIPN")
    protected void getExpressCheckout(final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();
        LOGGER.info("Paypal IPN");

        Enumeration en = request.getParameterNames();
        String str = "cmd=_notify-validate";
        while (en.hasMoreElements()) {
            String paramName = (String) en.nextElement();
            String paramValue = request.getParameter(paramName);
            str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue);
        }

        // post back to PayPal system to validate
        // NOTE: change http: to https: in the following URL to verify using SSL (for increased security).
        // using HTTPS requires either Java 1.4 or greater, or Java Secure Socket Extension (JSSE)
        // and configured for older versions.
        URL u = new URL("https://www.paypal.com/cgi-bin/webscr");
        URLConnection uc = u.openConnection();
        uc.setDoOutput(true);
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        PrintWriter pw = new PrintWriter(uc.getOutputStream());
        pw.println(str);
        pw.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(uc.getInputStream()));
        String res = in.readLine();
        in.close();

        // assign posted variables to local variables
        String itemName = request.getParameter("item_name");
        LOGGER.info(itemName);

        String itemNumber = request.getParameter("item_number");
        LOGGER.info(itemNumber);

        String paymentStatus = request.getParameter("payment_status");
        LOGGER.info(paymentStatus);

        String paymentAmount = request.getParameter("mc_gross");
        LOGGER.info(paymentAmount);

        String paymentCurrency = request.getParameter("mc_currency");
        LOGGER.info(paymentCurrency);

        String txnId = request.getParameter("txn_id");
        LOGGER.info(txnId);

        String receiverEmail = request.getParameter("receiver_email");
        LOGGER.info(receiverEmail);

        String payerEmail = request.getParameter("payer_email");
        LOGGER.info(payerEmail);

        //check notification validation
        if (res.equals("VERIFIED")) {
            // check that paymentStatus=Completed
            // check that txnId has not been previously processed
            // check that receiverEmail is your Primary PayPal email
            // check that paymentAmount/paymentCurrency are correct
            // process payment
        } else if (res.equals("INVALID")) {
            // log for investigation
        } else {
            // error
        }

    }

}


