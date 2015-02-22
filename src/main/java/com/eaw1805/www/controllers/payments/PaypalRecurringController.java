package com.eaw1805.www.controllers.payments;

import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileReq;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileRequestType;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileResponseType;
import urn.ebay.api.PayPalAPI.GetBillingAgreementCustomerDetailsReq;
import urn.ebay.api.PayPalAPI.GetBillingAgreementCustomerDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetBillingAgreementCustomerDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.ActivationDetailsType;
import urn.ebay.apis.eBLBaseComponents.AutoBillType;
import urn.ebay.apis.eBLBaseComponents.BillingAgreementDetailsType;
import urn.ebay.apis.eBLBaseComponents.BillingCodeType;
import urn.ebay.apis.eBLBaseComponents.BillingPeriodDetailsType;
import urn.ebay.apis.eBLBaseComponents.BillingPeriodType;
import urn.ebay.apis.eBLBaseComponents.CreateRecurringPaymentsProfileRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ErrorType;
import urn.ebay.apis.eBLBaseComponents.FailedPaymentActionType;
import urn.ebay.apis.eBLBaseComponents.ItemCategoryType;
import urn.ebay.apis.eBLBaseComponents.LandingPageType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.RecurringPaymentsProfileDetailsType;
import urn.ebay.apis.eBLBaseComponents.ScheduleDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.SolutionTypeType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles recurring paypal payments using Express Checkout.
 */
@org.springframework.stereotype.Controller
public class PaypalRecurringController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PaypalRecurringController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/paypalRec/getExpressCheckout")
    protected ModelAndView getExpressCheckout(final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();
        LOGGER.info("GetExpressCheckout");

        final String currencyCode = request.getParameter("currencyCodeType");
        final String token = request.getParameter("token");
        final String payerID = request.getParameter("PayerID");

        LOGGER.info(currencyCode);
        LOGGER.info(token);
        LOGGER.info(payerID);


        final PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(
                request.getSession().getServletContext().getRealPath("/") + "/WEB-INF/paypal.properties");

        final GetBillingAgreementCustomerDetailsReq gReq = new GetBillingAgreementCustomerDetailsReq();
        final GetBillingAgreementCustomerDetailsRequestType gRequestType = new GetBillingAgreementCustomerDetailsRequestType();

        gRequestType.setToken(token);
        gReq.setGetBillingAgreementCustomerDetailsRequest(gRequestType);

        final GetBillingAgreementCustomerDetailsResponseType txnresponse = service.getBillingAgreementCustomerDetails(gReq);

        if (txnresponse != null) {
            if (txnresponse.getAck().toString()
                    .equalsIgnoreCase("SUCCESS")) {

                LOGGER.info("Ack," + txnresponse.getAck());
                LOGGER.info("Payer Mail," +
                        txnresponse.getGetBillingAgreementCustomerDetailsResponseDetails().getPayerInfo().getPayer());


            } else {
                LOGGER.error("Error," + txnresponse.getErrors());
                for (final ErrorType errorType : txnresponse.getErrors()) {
                    LOGGER.info(errorType.getLongMessage());
                    LOGGER.info(errorType.getShortMessage());
                    LOGGER.info(errorType.getErrorCode());
                    LOGGER.info(errorType.getSeverityCode());
                }
            }
        }

        final CreateRecurringPaymentsProfileReq req = new CreateRecurringPaymentsProfileReq();
        final CreateRecurringPaymentsProfileRequestType reqType = new CreateRecurringPaymentsProfileRequestType();

        // Populate Recurring Payments Profile Details
        final RecurringPaymentsProfileDetailsType profileDetails = new RecurringPaymentsProfileDetailsType("2012-09-24" + "T06:10:00PDT");

        //profileDetails.setSubscriberName(request.getParameter("subscriberName"));
        /*AddressType shippingAddr = new AddressType();
        shippingAddr.setName(request.getParameter("shippingName"));
        shippingAddr.setStreet1(request.getParameter("shippingStreet1"));
        shippingAddr.setStreet2(request.getParameter("shippingStreet2"));
        shippingAddr.setPhone(request.getParameter("shippingPhone"));
        shippingAddr.setCityName(request.getParameter("shippingCity"));
        shippingAddr.setStateOrProvince(request.getParameter("shippingState"));
        shippingAddr.setCountryName(request.getParameter("shippingCountry"));
        shippingAddr.setPostalCode(request.getParameter("shippingPostalCode"));
        profileDetails.setSubscriberShippingAddress(shippingAddr);*/


        // Populate schedule details
        ScheduleDetailsType scheduleDetails = new ScheduleDetailsType();
        scheduleDetails.setDescription("billingAgreementText");

        scheduleDetails.setMaxFailedPayments(3);


        scheduleDetails.setAutoBillOutstandingAmount(AutoBillType.ADDTONEXTBILLING);


        final ActivationDetailsType activationDetails
                = new ActivationDetailsType(new BasicAmountType(CurrencyCodeType.EUR, "100"));
        activationDetails.setFailedInitialAmountAction(FailedPaymentActionType.CANCELONFAILURE);

        scheduleDetails.setActivationDetails(activationDetails);


        /*if (request.getParameter("trialBillingAmount") != "") {
            int frequency = Integer.parseInt(request
                    .getParameter("trialBillingFrequency"));
            BasicAmountType paymentAmount = new BasicAmountType(
                    currency,
                    request.getParameter("trialBillingAmount"));
            BillingPeriodType period = BillingPeriodType
                    .fromValue(request
                            .getParameter("trialBillingPeriod"));
            int numCycles = Integer.parseInt(request
                    .getParameter("trialBillingCycles"));

            BillingPeriodDetailsType trialPeriod = new BillingPeriodDetailsType(
                    period, frequency, paymentAmount);
            trialPeriod.setTotalBillingCycles(numCycles);
            if (request.getParameter("trialShippingAmount") != "") {
                trialPeriod.setShippingAmount(new BasicAmountType(
                        currency, request
                        .getParameter("trialShippingAmount")));
            }
            if (request.getParameter("trialTaxAmount") != "") {
                trialPeriod.setTaxAmount(new BasicAmountType(currency,
                        request.getParameter("trialTaxAmount")));
            }

            scheduleDetails.setTrialPeriod(trialPeriod);
        }*/


        final int frequency = 1;
        BasicAmountType paymentAmount = new BasicAmountType(CurrencyCodeType.EUR, "12");
        BillingPeriodType period = BillingPeriodType.DAY;

        int numCycles = 10;

        BillingPeriodDetailsType paymentPeriod = new BillingPeriodDetailsType(
                period, frequency, paymentAmount);
        paymentPeriod.setTotalBillingCycles(numCycles);

        paymentPeriod.setShippingAmount(new BasicAmountType(CurrencyCodeType.EUR, "0"));


        paymentPeriod.setTaxAmount(new BasicAmountType(CurrencyCodeType.EUR, "0"));

        scheduleDetails.setPaymentPeriod(paymentPeriod);


        final CreateRecurringPaymentsProfileRequestDetailsType reqDetails = new CreateRecurringPaymentsProfileRequestDetailsType(
                profileDetails, scheduleDetails);

        reqDetails.setToken(token);

        reqType.setCreateRecurringPaymentsProfileRequestDetails(reqDetails);

        req.setCreateRecurringPaymentsProfileRequest(reqType);
        CreateRecurringPaymentsProfileResponseType resp = service
                .createRecurringPaymentsProfile(req);

        if (resp != null) {

            if (resp.getAck().toString().equalsIgnoreCase("SUCCESS")) {

                LOGGER.info("Ack," + resp.getAck());
                LOGGER.info("Profile ID," +
                        resp.getCreateRecurringPaymentsProfileResponseDetails()
                                .getProfileID());
                LOGGER.info("Transaction ID," +
                        resp.getCreateRecurringPaymentsProfileResponseDetails()
                                .getTransactionID());
                LOGGER.info("Profile Status," +
                        resp.getCreateRecurringPaymentsProfileResponseDetails()
                                .getProfileStatus());

            } else {
                LOGGER.info("Error," + resp.getErrors());
            }
        }

        return new ModelAndView(new RedirectView(request.getContextPath() + "/home"));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/paypalRec")
    protected ModelAndView setExpressCheckout(final HttpServletRequest request, final ModelMap model) throws Exception {
        LOGGER.info("SetExpressCheckout");

        final User thisUser = getUser();
        //check that users are valid.
        if (thisUser == null) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        final PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(

                request.getSession().getServletContext().getRealPath("/")
                        + "/WEB-INF/paypal.properties");
        final SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType();
        final SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

        final StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        url.append(request.getContextPath());

        final String returnURL = url.toString() + "/paypalRec/getExpressCheckout";
        final String cancelURL = url.toString() + "/home";

        details.setReturnURL(returnURL + "?currencyCodeType=USD");
        details.setCancelURL(cancelURL);


        final List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();

        final PaymentDetailsItemType item = new PaymentDetailsItemType();

        //10 USDo
        final BasicAmountType amount = new BasicAmountType();
        amount.setCurrencyID(CurrencyCodeType.USD);
        amount.setValue("10");


        item.setQuantity(1);
        item.setName("Eaw Credits");
        item.setAmount(amount);

        item.setItemCategory(ItemCategoryType.DIGITAL);
        lineItems.add(item);


        final List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
        final PaymentDetailsType paydtl = new PaymentDetailsType();
        paydtl.setPaymentAction(PaymentActionCodeType.SALE);

        paydtl.setOrderDescription("Eaw Credits");
        paydtl.setOrderTotal(amount);
        paydtl.setPaymentDetailsItem(lineItems);
        paydtl.setItemTotal(amount);
        payDetails.add(paydtl);

        details.setPaymentDetails(payDetails);

        details.setSolutionType(SolutionTypeType.SOLE);
        details.setLandingPage(LandingPageType.LOGIN);

        //Recurring Option
        BillingAgreementDetailsType billingAgreement = new BillingAgreementDetailsType(BillingCodeType.RECURRINGPAYMENTS);
        billingAgreement.setBillingAgreementDescription("billingAgreementText");
        List<BillingAgreementDetailsType> billList = new ArrayList<BillingAgreementDetailsType>();
        billList.add(billingAgreement);
        details.setBillingAgreementDetails(billList);

        setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);

        final SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();

        expressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutReq);

        final SetExpressCheckoutResponseType setExpressCheckoutResponse = service.setExpressCheckout(expressCheckoutReq);

        LOGGER.info(setExpressCheckoutResponse != null);
        if (setExpressCheckoutResponse != null) {
            if (setExpressCheckoutResponse.getAck().toString()
                    .equalsIgnoreCase("SUCCESS")) {

                LOGGER.info("Ack " + setExpressCheckoutResponse.getAck());
                LOGGER.info("Token" + setExpressCheckoutResponse.getToken());


                final RedirectView redirectView = new RedirectView("https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token="
                        + setExpressCheckoutResponse.getToken());
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);

            } else {
                LOGGER.info("ERROR");
                final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);
            }
        }


        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Paypal Recurring Controller");
        return new ModelAndView("home", model);
    }
}
