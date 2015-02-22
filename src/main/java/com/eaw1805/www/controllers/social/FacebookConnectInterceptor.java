package com.eaw1805.www.controllers.social;

import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.User;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Listens for service provider connection events.
 * Allows for custom logic to be executed before and after connections are established with a specific service provider.
 */
public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(FacebookConnectInterceptor.class);

    @Override
    public void preConnect(final ConnectionFactory<Facebook> facebookConnectionFactory,
                           final MultiValueMap<String, String> parameters,
                           final WebRequest request) {
        LOGGER.info("preConnect - " + request.getParameter("redirect"));
        if (request.getParameter("redirect") != null) {
            request.setAttribute("redirect", request.getParameter("redirect"), WebRequest.SCOPE_SESSION);
        }
    }

    @Override
    public void postConnect(final Connection<Facebook> facebookConnection,
                            final WebRequest request) {
        LOGGER.info("postConnect");

        //User connected via userProfile.
        if (((String) request.getAttribute("redirect", WebRequest.SCOPE_SESSION)).contains("games")
                || ((String) request.getAttribute("redirect", WebRequest.SCOPE_SESSION)).contains("s3t")) {

            String redirect = ((String) request.getAttribute("redirect", WebRequest.SCOPE_SESSION));
            if (redirect.contains("s3t")) {
                redirect = redirect.replace("s3t", "settings");
            }
            request.setAttribute("redirect", redirect, WebRequest.SCOPE_SESSION);

            final User thisUser = getUser();
            if (thisUser.getUserId() != -1) {
                if (thisUser.getFacebookStatus() == 0) {
                    thisUser.setFacebookStatus(1);
                    final User admin = userManager.getByUserName("admin");

                    final int freeCreditsTransferred = 10;
                    final int freeCreditsAmount = thisUser.getCreditFree();
                    final int adminFreeCreditsAmount = admin.getCreditFree();


                    thisUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
                    admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

                    final PaymentHistory receiverUserPayment = new PaymentHistory();
                    receiverUserPayment.setUser(thisUser);

                    receiverUserPayment.setComment("Administrator credited "
                            + freeCreditsTransferred + " free credits to " + thisUser.getUsername()
                            + " ( Connecting Facebook account )");

                    receiverUserPayment.setDate(new Date());
                    receiverUserPayment.setAgent(thisUser);
                    receiverUserPayment.setType(PaymentHistory.TYPE_OFFER);
                    receiverUserPayment.setCreditFree(thisUser.getCreditFree());
                    receiverUserPayment.setCreditBought(thisUser.getCreditBought());
                    receiverUserPayment.setCreditTransferred(thisUser.getCreditTransferred());
                    receiverUserPayment.setChargeBought(0);
                    receiverUserPayment.setChargeFree(freeCreditsTransferred);
                    receiverUserPayment.setChargeTransferred(0);

                    pmHistoryManager.add(receiverUserPayment);

                    final PaymentHistory adminUserPayment = new PaymentHistory();
                    adminUserPayment.setUser(admin);

                    adminUserPayment.setComment(admin.getUsername() + " credited "
                            + freeCreditsTransferred + " free credits to " + thisUser.getUsername()
                            + " ( Connecting Facebook account )");

                    adminUserPayment.setDate(new Date());
                    adminUserPayment.setAgent(thisUser);
                    adminUserPayment.setType(PaymentHistory.TYPE_OFFER);
                    adminUserPayment.setCreditFree(admin.getCreditFree());
                    adminUserPayment.setCreditBought(admin.getCreditBought());
                    adminUserPayment.setCreditTransferred(admin.getCreditTransferred());
                    adminUserPayment.setChargeBought(0);
                    adminUserPayment.setChargeFree(-freeCreditsTransferred);
                    adminUserPayment.setChargeTransferred(0);

                    pmHistoryManager.add(adminUserPayment);

                    userManager.update(admin);
                    userManager.update(thisUser);
                }
            }
        }
    }

    /**
     * Retrieve the user object from the database.
     *
     * @return the User entity.
     */
    protected final User getUser() {
        User thisUser = new User();
        try {
            // Retrieve principal object
            final UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // Lookup user based on username
            thisUser = userManager.getByUserName(principal.getUsername());

        } catch (Exception ex) {
            // do nothing
            thisUser.setUserId(-1);
            thisUser.setUsername("anonymous");
        }

        try {
            // Retrieve remote IP
            final String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("CF-Connecting-IP");
            if (ipAddress == null) {
                thisUser.setRemoteAddress(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr());

            } else {
                thisUser.setRemoteAddress(ipAddress);
            }

        } catch (Exception ex) {
            // do nothing
            thisUser.setRemoteAddress("unknown");
        }

        return thisUser;
    }

    /**
     * Instance of UserManager class to perfom queries about
     * users.
     */
    private transient UserManagerBean userManager;

    /**
     * Setter method used by spring to inject a userManager bean.
     *
     * @param injUserManager a userManager bean.
     */
    @Autowired
    @Qualifier("userManagerBean")
    public void setUserManager(final UserManagerBean injUserManager) {
        this.userManager = injUserManager;
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    @Autowired
    @Qualifier("paymentHistoryManagerBean")
    public void setPmHistoryManager(PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }
}
