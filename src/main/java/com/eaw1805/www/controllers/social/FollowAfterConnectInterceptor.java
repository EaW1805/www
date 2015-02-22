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
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 12/5/11
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class FollowAfterConnectInterceptor implements ConnectInterceptor<Twitter> {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(FollowAfterConnectInterceptor.class);

    public void preConnect(final ConnectionFactory<Twitter> twitterConnectionFactory,
                           final MultiValueMap<String, String> stringStringMultiValueMap,
                           final WebRequest webRequest) {
        LOGGER.debug("preConnect");
        if (webRequest.getParameter("redirect") != null) {
            webRequest.setAttribute("redirect", webRequest.getParameter("redirect"), WebRequest.SCOPE_SESSION);
        }
    }

    public void postConnect(final Connection<Twitter> twitterConnection,
                            final WebRequest webRequest) {
        LOGGER.debug("postConnect");
        final User thisUser = getUser();

        //User connected via userProfile.
        if (((String) webRequest.getAttribute("redirect", WebRequest.SCOPE_SESSION)).contains("games")
                || ((String) webRequest.getAttribute("redirect", WebRequest.SCOPE_SESSION)).contains("s3t")) {

            String redirect = ((String) webRequest.getAttribute("redirect", WebRequest.SCOPE_SESSION));
            if (redirect.contains("s3t")) {
                redirect = redirect.replace("s3t", "settings");
            }
            webRequest.setAttribute("redirect", redirect, WebRequest.SCOPE_SESSION);

            if (thisUser.getUserId() != -1) {
                if (thisUser.getTwitterStatus() == 0) {
                    thisUser.setTwitterStatus(1);

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
                            + " ( Connecting Twitter account )");

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
                            + " ( Connecting Twitter account )");

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
        } else {
            final Twitter thisTwitter = twitterConnection.getApi();
            final List<TwitterProfile> friends = thisTwitter.friendOperations().getFriends();
            boolean alreadyFollower = false;
            for (TwitterProfile friend : friends) {
                if ("eaw1805".equals(friend.getScreenName())) {
                    alreadyFollower = true;
                }
            }
            if (!alreadyFollower) {
                //make user follow eaw1805.
                thisTwitter.friendOperations().follow("eaw1805");
                thisUser.setTwitterStatus(2);
                userManager.update(thisUser);
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
     * Connect to eaw1805 Twitter account.
     *
     * @return A new TwitterTemplate for eaw1805.
     */
    public Twitter getApplicationTwitter() {
        String consumerKey = "ox1w2P1s1QP7VjElRT8g"; // The application's consumer key
        String consumerSecret = "foCznEnOpANOgzYS6n39tt0xeAF8iW26lhOQyfjhA"; // The application's consumer secret
        String accessToken = "417188656-PCniVCUhuoGatzZVzjnoww1bo9nqQYhvBnhWiSlV"; // The access token granted after OAuth authorization
        String accessTokenSecret = "BXxHnU266epHt8a4B8C80TVtogSlNg1TtO9BTHWuyg"; // The access token secret granted after OAuth authorization
        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
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
