package com.eaw1805.www.controllers.payments;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.constants.AchievementConstants;
import com.eaw1805.data.constants.ProfileConstants;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.*;
import com.eaw1805.data.model.paypal.PaypalTransaction;
import com.eaw1805.data.model.paypal.PaypalUserProfile;
import com.eaw1805.www.commands.PaypalCommand;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.validators.PaypalCommandValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import urn.ebay.api.PayPalAPI.*;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Handles paypal payments using Express Checkout.
 */
@org.springframework.stereotype.Controller
public class PaypalExpressCheckoutController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PaypalExpressCheckoutController.class);

    /**
     * The model name.
     */
    protected static final String MODEL_JSTL_KEY = "paypalCommand";

    /**
     * Instance PaypalManager class to perform queries
     * about nation objects.
     */
    protected transient PaypalManager paypalManager;

    /**
     * Instance paypalUserManager class to perform queries
     * about nation objects.
     */
    private transient PaypalUserManagerBean paypalUserManager;

    /**
     * Instance paypalTransactionManager class to perform queries
     * about nation objects.
     */
    private transient PaypalTransactionManagerBean paypalTransactionManager;

    /**
     * Instance PaymentHistoryManage class to perform queries
     * about nation objects.
     */
    private transient PaymentHistoryManagerBean pmHistoryManager;

    /**
     * Instance of ProfileManagerBean.
     */
    private transient ProfileManagerBean profileManager;

    /**
     * Returns the paypal command.
     *
     * @return the Payapal Command,
     */
    @ModelAttribute(MODEL_JSTL_KEY)
    public PaypalCommand paypalCommand() {
        return new PaypalCommand();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/paypal/getExpressCheckout")
    protected ModelAndView getExpressCheckout(final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();
        LOGGER.info("GetExpressCheckout");

        final User thisUser = getUser();
        //check that users are valid.
        if (thisUser == null) {
            model.clear();
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        final String token = request.getParameter("token");
        final String payerID = request.getParameter("PayerID");
        final String creditsAmount = (String) request.getSession().getAttribute("amount");

        //Remove attribute amount from session.
        request.getSession().removeAttribute("amount");


        PaypalUserProfile paypalUser = paypalUserManager.getByPaypalID(payerID);

        if (paypalUser == null) {
            //Get User Details
            final GetExpressCheckoutDetailsReq req = new GetExpressCheckoutDetailsReq();
            final GetExpressCheckoutDetailsRequestType reqType = new GetExpressCheckoutDetailsRequestType(token);
            req.setGetExpressCheckoutDetailsRequest(reqType);

            final GetExpressCheckoutDetailsResponseType resp = paypalManager.getService().getExpressCheckoutDetails(req);
            if (resp != null) {
                if (resp.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                    final PayerInfoType payer = resp.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo();

                    final PaypalUserProfile newPaypalUser = new PaypalUserProfile();
                    newPaypalUser.setUser(thisUser);
                    newPaypalUser.setPayerID(payer.getPayerID());
                    newPaypalUser.setPayer(payer.getPayer());
                    newPaypalUser.setPayerStatus(payer.getPayerStatus().getValue());
                    newPaypalUser.setSalutation(payer.getPayerName().getSalutation());
                    newPaypalUser.setFirstName(payer.getPayerName().getFirstName());
                    newPaypalUser.setMiddleName(payer.getPayerName().getMiddleName());
                    newPaypalUser.setLastName(payer.getPayerName().getLastName());
                    newPaypalUser.setSuffix(payer.getPayerName().getSuffix());
                    newPaypalUser.setCountryCodeType(payer.getPayerCountry().getValue());
                    newPaypalUser.setName(payer.getAddress().getName());
                    newPaypalUser.setStreet1(payer.getAddress().getStreet1());
                    newPaypalUser.setStreet2(payer.getAddress().getStreet2());
                    newPaypalUser.setCityName(payer.getAddress().getCityName());

                    newPaypalUser.setStateOrProvince(payer.getAddress().getStateOrProvince());
                    newPaypalUser.setCountryName(payer.getAddress().getCountryName());

                    newPaypalUser.setPhone(payer.getAddress().getPhone());
                    newPaypalUser.setPostalCode(payer.getAddress().getPostalCode());
                    newPaypalUser.setAddressStatus(payer.getAddress().getAddressStatus().getValue());

                    paypalUserManager.add(newPaypalUser);
                    paypalUser = newPaypalUser;
                    LOGGER.debug("New Paypal User Account added.");
                } else {
                    LOGGER.error("Error " + resp.getErrors());
                }
            }
        }


        final DoExpressCheckoutPaymentRequestType doCheckoutPaymentRequestType = new DoExpressCheckoutPaymentRequestType();
        final DoExpressCheckoutPaymentRequestDetailsType details = new DoExpressCheckoutPaymentRequestDetailsType();
        details.setToken(token);
        details.setPayerID(payerID);
        details.setPaymentAction(PaypalManager.PAYMENT_ACTION_CODE_TYPE);

        final PaymentDetailsType paymentDetails = new PaymentDetailsType();
        final BasicAmountType orderTotal = new BasicAmountType();
        orderTotal.setValue(creditsAmount);
        orderTotal.setCurrencyID(PaypalManager.CURRENCY_CODE_TYPE);
        paymentDetails.setOrderTotal(orderTotal);

        final BasicAmountType itemTotal = new BasicAmountType();
        itemTotal.setValue(creditsAmount);
        itemTotal.setCurrencyID(PaypalManager.CURRENCY_CODE_TYPE);
        paymentDetails.setItemTotal(itemTotal);

        final List<PaymentDetailsItemType> paymentItems = new ArrayList<PaymentDetailsItemType>();

        final PaymentDetailsItemType paymentItem = new PaymentDetailsItemType();
        paymentItem.setName(PaypalManager.PAYMENT_ITEM_DESCRIPTION);
        paymentItem.setQuantity(1);

        final BasicAmountType amount = new BasicAmountType();
        amount.setValue(creditsAmount);
        amount.setCurrencyID(PaypalManager.CURRENCY_CODE_TYPE);

        paymentItem.setAmount(amount);
        paymentItems.add(paymentItem);
        paymentDetails.setPaymentDetailsItem(paymentItems);

        final List<PaymentDetailsType> payDetailType = new ArrayList<PaymentDetailsType>();
        payDetailType.add(paymentDetails);
        details.setPaymentDetails(payDetailType);

        //Execute the Do Checkout step.
        doCheckoutPaymentRequestType.setDoExpressCheckoutPaymentRequestDetails(details);

        final DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
        doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doCheckoutPaymentRequestType);

        final DoExpressCheckoutPaymentResponseType doCheckoutPaymentResponseType
                = paypalManager.getService().doExpressCheckoutPayment(doExpressCheckoutPaymentReq);


        if (doCheckoutPaymentResponseType != null) {

            if (doCheckoutPaymentResponseType.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                final String transactionId = doCheckoutPaymentResponseType.getDoExpressCheckoutPaymentResponseDetails()
                        .getPaymentInfo().get(0).getTransactionID();
                final GetTransactionDetailsReq req = new GetTransactionDetailsReq();
                final GetTransactionDetailsRequestType reqType = new GetTransactionDetailsRequestType();

                reqType.setTransactionID(transactionId);
                req.setGetTransactionDetailsRequest(reqType);

                final GetTransactionDetailsResponseType resp = paypalManager.getService().getTransactionDetails(req);
                if (resp != null) {
                    if (resp.getAck().toString().equalsIgnoreCase("SUCCESS")) {

                        final PaypalTransaction transaction = new PaypalTransaction();
                        transaction.setPayer(paypalUser);
                        transaction.setCorellationId(doCheckoutPaymentResponseType.getCorrelationID());
                        transaction.setTransactionId(transactionId);
                        transaction.setTimestamp(resp.getPaymentTransactionDetails().getPaymentInfo().getPaymentDate());
                        transaction.setPayerNote(resp.getPaymentTransactionDetails().getPaymentItemInfo().getMemo());
                        transaction.setTransactionType(resp.getPaymentTransactionDetails().getPaymentInfo().getTransactionType().getValue());
                        transaction.setPaymentType(resp.getPaymentTransactionDetails().getPaymentInfo().getPaymentType().getValue());
                        transaction.setGrossAmount(Double.valueOf(resp.getPaymentTransactionDetails().getPaymentInfo().getGrossAmount().getValue()));
                        transaction.setItem(resp.getPaymentTransactionDetails().getPaymentInfo().getSubject());
                        transaction.setPaymentStatusCode(resp.getPaymentTransactionDetails().getPaymentInfo().getPaymentStatus().getValue());
                        boolean pending = false;
                        if (resp.getPaymentTransactionDetails().getPaymentInfo().getPendingReason() != null
                                && resp.getPaymentTransactionDetails().getPaymentInfo().getPendingReason().getValue() != null) {
                            transaction.setPendingStatusCode(resp.getPaymentTransactionDetails().getPaymentInfo().
                                    getPendingReason().getValue());
                            if (transaction.getPaymentStatusCode().equals("Pending")) {
                                pending = true;
                            }
                        }

                        thisUser.setCreditBought(thisUser.getCreditBought() + paypalManager.getGameCoins(creditsAmount));

                        final PaymentHistory thisUserPayment = new PaymentHistory();
                        thisUserPayment.setUser(thisUser);
                        thisUserPayment.setComment("User " + thisUser.getUsername() + " bought "
                                + paypalManager.getGameCoins(creditsAmount) + " credits. Paypal transaction id: " +
                                " <a href=\"https://www.paypal.com/vst/id=" + transactionId + "\">" + transactionId + "</a>  ");
                        thisUserPayment.setDate(new Date());
                        thisUserPayment.setAgent(thisUser);
                        thisUserPayment.setType(PaymentHistory.METHOD_PAYPAL);
                        thisUserPayment.setCreditFree(thisUser.getCreditFree());
                        thisUserPayment.setCreditBought(thisUser.getCreditBought());
                        thisUserPayment.setCreditTransferred(thisUser.getCreditTransferred());
                        thisUserPayment.setChargeBought(paypalManager.getGameCoins(creditsAmount));
                        thisUserPayment.setChargeFree(0);
                        thisUserPayment.setChargeTransferred(0);

                        userManager.update(thisUser);
                        pmHistoryManager.add(thisUserPayment);
                        transaction.setPmHistory(thisUserPayment);
                        paypalTransactionManager.add(transaction);

                        final User admin = userManager.getByUserName("admin");
                        if (pending) {
                            final Message message = new Message();
                            message.setSender(admin);
                            message.setReceiver(admin);
                            message.setDate(new Date());
                            message.setOpened(false);
                            message.setRootId(0);
                            message.setSubject("Pending Paypal Transaction, " + transactionId);
                            message.setBodyMessage(thisUser.getUsername() + " bought "
                                    + creditsAmount + ". Transaction id: " +
                                    " <a href=\"https://www.paypal.com/vst/id=" + transactionId + "\">" + transactionId + "</a>");
                            messageManager.add(message);
                        }

                        // Send out mail
                        sendPaymentReceipt(thisUser, thisUserPayment.getChargeBought(), transactionId);

                        // Update user profile
                        // Update profile entry
                        final Profile entry = profileManager.getByOwnerKey(thisUser, ProfileConstants.CREDITS);
                        final int totCredits;

                        // If the entry does not exist, create
                        if (entry == null) {
                            final Profile newEntry = new Profile();
                            newEntry.setUser(thisUser);
                            newEntry.setKey(ProfileConstants.CREDITS);
                            newEntry.setValue(thisUserPayment.getChargeBought());
                            profileManager.add(newEntry);

                            totCredits = thisUserPayment.getChargeBought();

                        } else {
                            // Make sure players do not end up with negative VPs
                            entry.setValue(entry.getValue() + thisUserPayment.getChargeBought());
                            profileManager.update(entry);

                            totCredits = entry.getValue();
                        }

                        LOGGER.info("Total credits bought till now = " + totCredits);

                        // Check if achievement is reached
                        for (int level = AchievementConstants.LEVEL_3; level <= AchievementConstants.LEVEL_4; level++) {
                            if (totCredits >= AchievementConstants.SPECIAL_L[level]
                                    && !getAchievementManager().checkPlayerCategoryLevel(thisUser, AchievementConstants.SPECIAL, level)) {

                                // Generate new entry
                                final Achievement achievement = new Achievement();
                                achievement.setUser(thisUser);
                                achievement.setCategory(AchievementConstants.SPECIAL);
                                achievement.setLevel(level);
                                achievement.setAnnounced(false);
                                achievement.setFirstLoad(false);
                                achievement.setDescription(AchievementConstants.SPECIAL_STR[level]);
                                achievement.setVictoryPoints(0);
                                achievement.setAchievementPoints(AchievementConstants.SPECIAL_AP[level]);
                                getAchievementManager().add(achievement);
                            }
                        }

                    } else {
                        LOGGER.error("Error while getting Transaction Details.");
                    }
                }
            } else {
                LOGGER.error("Error," + doCheckoutPaymentResponseType.getErrors());
            }
        }

        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/home");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    /**
     * Send an e-mail.
     *
     * @param thisUser The new user.
     */
    @EawAsync
    protected void sendPaymentReceipt(final User thisUser, final int amount, final String transactionId) {
        // Send out mail
        try {
            EmailManager.getInstance().sendPaymentReceipt(thisUser, amount, transactionId);

        } catch (final MessagingException e) {
            LOGGER.error(e);
            LOGGER.error("Transaction Notification: Failed to send email");

        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e);
            LOGGER.error("Transaction Notification: Failed to send email");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/paypal")
    protected ModelAndView setExpressCheckout(@ModelAttribute(MODEL_JSTL_KEY) PaypalCommand paypalCommand,
                                              final BindingResult result,
                                              final HttpServletRequest request, final ModelMap model) throws Exception {
        try {
            System.out.println("1");
        ScenarioContextHolder.defaultScenario();

        final User thisUser = getUser();
        //check that users are valid.
        if (thisUser == null) {
            model.clear();
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }
            System.out.println("2");
        final PaypalCommandValidator paypalValidator = new PaypalCommandValidator();
        paypalValidator.validate(paypalCommand, result);
        if (result.hasErrors()) {
            model.clear();
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        final SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType();
        final SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();
            System.out.println("3");
        final StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        url.append(request.getContextPath());

        final String returnURL = url.toString() + "/paypal/getExpressCheckout";
        final String cancelURL = url.toString() + "/settings";

        details.setReturnURL(returnURL);
        details.setCancelURL(cancelURL);

        final List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();

        final PaymentDetailsItemType item = new PaymentDetailsItemType();

        final BasicAmountType amount = new BasicAmountType();
        amount.setCurrencyID(PaypalManager.CURRENCY_CODE_TYPE);
        amount.setValue(paypalCommand.getAmount());

        item.setQuantity(1);
        item.setName(PaypalManager.PAYMENT_ITEM_DESCRIPTION);
        item.setAmount(amount);
            System.out.println("4");
        item.setItemCategory(ItemCategoryType.DIGITAL);
        lineItems.add(item);


        final List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
        final PaymentDetailsType paydtl = new PaymentDetailsType();
        paydtl.setPaymentAction(PaypalManager.PAYMENT_ACTION_CODE_TYPE);

        paydtl.setOrderDescription(PaypalManager.PAYMENT_ITEM_DESCRIPTION);
        paydtl.setOrderTotal(amount);
        paydtl.setPaymentDetailsItem(lineItems);
        paydtl.setItemTotal(amount);
        payDetails.add(paydtl);
        details.setPaymentDetails(payDetails);

        details.setSolutionType(SolutionTypeType.SOLE);
        details.setLandingPage(LandingPageType.LOGIN);

        setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);

        final SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();
            System.out.println("5");
        expressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutReq);

        final SetExpressCheckoutResponseType setExpressCheckoutResponse
                = paypalManager.getService().setExpressCheckout(expressCheckoutReq);

        if (setExpressCheckoutResponse != null) {
            if (setExpressCheckoutResponse.getAck().toString()
                    .equalsIgnoreCase("SUCCESS")) {

                request.getSession().setAttribute("amount", paypalCommand.getAmount());
                request.getSession().setAttribute("token", setExpressCheckoutResponse.getToken());

                final RedirectView redirectView = new RedirectView("https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token="
                        + setExpressCheckoutResponse.getToken());
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);

            } else {
                for (final ErrorType errorType : setExpressCheckoutResponse.getErrors()) {
                    LOGGER.error(errorType.getLongMessage());
                }
                final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
                redirectView.setExposeModelAttributes(false);
                return new ModelAndView(redirectView);
            }
        }
            System.out.println("6");
        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Paypal Controller");
        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
        redirectView.setExposeModelAttributes(false);
            System.out.println("7");
        return new ModelAndView(redirectView);
        } catch (Exception e) {
            System.out.println("8");
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/paypal")
    protected ModelAndView setExpressCheckout(final HttpServletRequest request, final ModelMap model) throws Exception {
        model.clear();
        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/settings");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    /**
     * Setter method used by spring to inject a PaypalManager bean.
     *
     * @param value a PaypalManager bean.
     */
    public void setPaypalManager(final PaypalManager value) {
        this.paypalManager = value;
    }

    /**
     * Setter method used by spring to inject a PaypalUserManager  bean.
     *
     * @param value a PaypalUserManager  bean.
     */
    public void setPaypalUserManager(final PaypalUserManagerBean value) {
        this.paypalUserManager = value;
    }

    /**
     * Setter method used by spring to inject a PaypalTransactionManager bean.
     *
     * @param value a PaypalTransactionManager bean.
     */
    public void setPaypalTransactionManager(final PaypalTransactionManagerBean value) {
        this.paypalTransactionManager = value;
    }

    /**
     * Setter method used by spring to inject a PaymentHistoryManager bean.
     *
     * @param value a PaymentHistoryManager bean.
     */
    public void setPmHistoryManager(final PaymentHistoryManagerBean value) {
        this.pmHistoryManager = value;
    }

    public void setProfileManager(final ProfileManagerBean value) {
        this.profileManager = value;
    }



}


