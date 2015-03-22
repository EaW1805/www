package com.eaw1805.www.controllers.payments;

import com.eaw1805.data.managers.beans.PaypalTransactionManagerBean;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.paypal.PaypalTransaction;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.validators.ReceiptValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Request receipt for a valid payment.
 */
@org.springframework.stereotype.Controller
public class RequestReceipt
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PaypalExpressCheckoutController.class);

    /**
     * The model name.
     */
    protected static final String MODEL_JSTL_KEY = "paypalTransaction";

    /**
     * Instance paypalTransactionManager class to perform queries
     * about nation objects.
     */
    private transient PaypalTransactionManagerBean paypalTransactionManager;

    /**
     * Setter method used by spring to inject a PaypalTransactionManager bean.
     *
     * @param value a PaypalTransactionManager bean.
     */
    public void setPaypalTransactionManager(final PaypalTransactionManagerBean value) {
        this.paypalTransactionManager = value;
    }

    @ModelAttribute(MODEL_JSTL_KEY)
    public PaypalTransaction getCommandObject() {
        return new PaypalTransaction();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/receipt/id/{transactionId}")
    protected ModelAndView handle(final @PathVariable("transactionId") String transactionId,
                                  final HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        final ModelMap model = new ModelMap();

        // Retrieve user
        final User thisUser = getUser();

        // retrieve transaction
        final PaypalTransaction thisTrans = paypalTransactionManager.getByPaypalTransactionlID(transactionId);

        // check that this is a valid transaction ID
        if (thisTrans == null) {
            throw new InvalidPageException("wrong transaction ID");
        }

        // check that this is a transaction by this user
        if (thisTrans.getPmHistory().getUser().getUserId() != thisUser.getUserId()
                && thisUser.getUserType() != 3) {
            throw new InvalidPageException("wrong transaction ID");
        }

        // Fill in data from User profile
        if (thisTrans.getPayerName() == null) {
            thisTrans.setPayerName(thisUser.getFullname());
        }

        // Fill in data from User profile
        if (thisTrans.getPayerCountry() == null) {
            thisTrans.setPayerCountry(thisUser.getLocation());
        }

        // Fill in data from Paypal Profile
        if (thisTrans.getPayerAddress() == null) {
            thisTrans.setPayerAddress(thisTrans.getPayer().getStreet1());
        }

        // Fill in data from Paypal Profile
        if (thisTrans.getPayerCity() == null) {
            thisTrans.setPayerCity(thisTrans.getPayer().getCityName());
        }

        // Fill in data from Paypal Profile
        if (thisTrans.getPayerPOCode() == null) {
            thisTrans.setPayerPOCode(thisTrans.getPayer().getPostalCode());
        }

        // Prepare data to pass to jsp
        model.put("user", thisUser);
        model.put("paypalTransaction", thisTrans);
        model.put("unreadMessagesCount", super.messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Request Receipt");
        return new ModelAndView("receipt", model);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/receipt/id/{transactionId}")
    public String processRequest(@ModelAttribute(MODEL_JSTL_KEY) final PaypalTransaction thatTrans,
                                 final @PathVariable("transactionId") String transactionId,
                                 final BindingResult result,
                                 final SessionStatus status)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        final ModelMap model = new ModelMap();

        // retrieve user
        final User thisUser = getUser();

        // Prepare data to pass to jsp
        model.put("user", thisUser);
        model.put("unreadMessagesCount", super.messageManager.countUnreadMessagesByReceiver(thisUser));

        // check that this is a valid transaction ID
        if (thatTrans == null) {
            throw new InvalidPageException("wrong transaction ID");
        }

        if (!thatTrans.getTransactionId().equals(transactionId)) {
            throw new InvalidPageException("wrong transaction ID");
        }

        // validate user data
        final ReceiptValidator transValidator = new ReceiptValidator();
        transValidator.validate(thatTrans, result);
        if (result.hasErrors()) {
            return "receipt";
        }

        // retrieve transaction
        final PaypalTransaction thisTrans = paypalTransactionManager.getByPaypalTransactionlID(transactionId);

        // check that this is a transaction by this user
        if (thisTrans.getPmHistory().getUser().getUserId() != thisUser.getUserId()
                && thisUser.getUserType() != 3) {
            throw new InvalidPageException("wrong transaction ID");
        }

        // Update values
        thisTrans.setPayerName(thatTrans.getPayerName());
        thisTrans.setPayerAddress(thatTrans.getPayerAddress());
        thisTrans.setPayerCity(thatTrans.getPayerCity());
        thisTrans.setPayerPOCode(thatTrans.getPayerPOCode());
        thisTrans.setPayerCountry(thatTrans.getPayerCountry());

        // update db
        paypalTransactionManager.update(thisTrans);

        // Send out mail
        try {
            // Try to send out the contact form
            sendPaymentReceiptRequest(thisUser, thisTrans);

        } catch (final MessagingException e) {
            LOGGER.error(e);
            LOGGER.error("Receipt Form: Failed to send email");

        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e);
            LOGGER.error("Receipt Form: Failed to send email");
        }

        return "redirect:/receipt/sent";
    }

}
