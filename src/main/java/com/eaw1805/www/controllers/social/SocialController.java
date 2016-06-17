package com.eaw1805.www.controllers.social;

import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.managers.beans.UserGameManagerBean;
import com.eaw1805.data.managers.beans.UserManagerBean;
import com.eaw1805.data.model.News;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FqlResult;
import org.springframework.social.facebook.api.FqlResultMapper;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Provides basic operation on FB/Twitter..
 */
@org.springframework.stereotype.Controller
public class SocialController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SocialController.class);

    /**
     * Instance ConnectionRepository class.
     */
    private transient ConnectionRepository connectionRepository;

    /**
     * Instance UserManager class.
     */
    private transient UserManagerBean userManager;

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    protected transient UserGameManagerBean userGameManager;

    @RequestMapping(method = RequestMethod.GET, value = "/social/{type}")
    public ModelAndView handle(@PathVariable final String type,
                               final HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();

        String redirectPage = "/games";
        if (request.getParameter("redirect") != null && !request.getParameter("redirect").equals("")) {
            redirectPage = request.getParameter("redirect");
        }

        // Retrieve Path Variable
        if (type == null) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + redirectPage);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //check if user can view this message
        final User thisUser = getUser();

        if (type.equals("tw_follow") && thisUser.getTwitterStatus() == 1) {

            thisUser.setTwitterStatus(2);
            final User admin = userManager.getByUserName("admin");

            final int freeCreditsTransferred = 20;
            final int freeCreditsAmount = thisUser.getCreditFree();
            final int adminFreeCreditsAmount = admin.getCreditFree();


            thisUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
            admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(thisUser);

            receiverUserPayment.setComment("Administrator credited "
                    + freeCreditsTransferred + " free credits to " + thisUser.getUsername()
                    + " ( Follow us on Twitter)");

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
                    + " ( Follow us on Twitter)");

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

        } else if (type.equals("fb_like") && thisUser.getFacebookStatus() == 1) {

            thisUser.setFacebookStatus(2);
            final User admin = userManager.getByUserName("admin");

            final int freeCreditsTransferred = 40;
            final int freeCreditsAmount = thisUser.getCreditFree();
            final int adminFreeCreditsAmount = admin.getCreditFree();


            thisUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
            admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(thisUser);

            receiverUserPayment.setComment("Administrator credited "
                    + freeCreditsTransferred + " free credits to " + thisUser.getUsername()
                    + " ( Like our Facebook page )");

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
                    + " ( Like our Facebook page )");

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

        } else if (type.equals("tw_tweet") && thisUser.getTwitterStatus() == 2) {

            thisUser.setTwitterStatus(3);
            final User admin = userManager.getByUserName("admin");

            final int freeCreditsTransferred = 40;
            final int freeCreditsAmount = thisUser.getCreditFree();
            final int adminFreeCreditsAmount = admin.getCreditFree();


            thisUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
            admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(thisUser);

            receiverUserPayment.setComment("Administrator credited "
                    + freeCreditsTransferred + " free credits to " + thisUser.getUsername()
                    + " ( Share tweet on Twitter )");

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
                    + " ( Share tweet on Twitter )");

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
        } else if (type.equals("fb_share") && thisUser.getFacebookStatus() == 2) {

            thisUser.setFacebookStatus(3);
            final User admin = userManager.getByUserName("admin");

            final int freeCreditsTransferred = 60;
            final int freeCreditsAmount = thisUser.getCreditFree();
            final int adminFreeCreditsAmount = admin.getCreditFree();


            thisUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
            admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(thisUser);

            receiverUserPayment.setComment("Administrator credited "
                    + freeCreditsTransferred + " free credits to " + thisUser.getUsername()
                    + " ( Share Post on Facebook )");

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
                    + " ( Share Post on Facebook )");

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

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] /social");
        final RedirectView redirectView = new RedirectView(request.getContextPath() + redirectPage);
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/social/scenario/{scenarioId}/news/{newsId}")
    public ModelAndView handleNewsShare(@PathVariable final String newsId,
                                        @PathVariable final String scenarioId,
                                        final HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();


        String redirectPage = "/games";
        if (request.getParameter("redirect") != null && !request.getParameter("redirect").equals("")) {
            redirectPage = request.getParameter("redirect");
        }

        // Retrieve Path Variable
        if (newsId == null) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + redirectPage);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //check if user can view this message
        final User thisUser = getUser();

        final int id;
        try {
            id = Integer.parseInt(newsId);
        } catch (final Exception e) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + redirectPage);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        ScenarioContextHolder.setScenario(scenarioId);

        final News newsEntry = newsManager.getByID(id);

        if (newsEntry == null) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + redirectPage);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        final List<UserGame> userGames = userGameManager.listActive(thisUser, newsEntry.getGame());

        if (userGames != null
                && userGames.size() > 0
                && newsEntry.getTurn() == (newsEntry.getGame().getTurn() - 1)
                && !newsEntry.getAnnouncement()) {

            newsEntry.setAnnouncement(true);

            ScenarioContextHolder.defaultScenario();
            final User admin = userManager.getByUserName("admin");

            final int freeCreditsTransferred = 1;
            final int freeCreditsAmount = thisUser.getCreditFree();
            final int adminFreeCreditsAmount = admin.getCreditFree();

            thisUser.setCreditFree(freeCreditsAmount + freeCreditsTransferred);
            admin.setCreditFree(adminFreeCreditsAmount - freeCreditsTransferred);

            final PaymentHistory receiverUserPayment = new PaymentHistory();
            receiverUserPayment.setUser(thisUser);

            receiverUserPayment.setComment("Administrator credited "
                    + freeCreditsTransferred + " free credit to " + thisUser.getUsername()
                    + " ( Share Newsletter Entry on Facebook )");

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
                    + freeCreditsTransferred + " free credit to " + thisUser.getUsername()
                    + " ( Share Newsletter Entry on Facebook )");

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

            ScenarioContextHolder.setScenario(scenarioId);
            newsManager.update(newsEntry);
        }

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] /social");
        final RedirectView redirectView = new RedirectView(request.getContextPath() + redirectPage);
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    public SocialDTO checkUserSocialState(final User currentUser) {
        final Connection<Facebook> fbConnection = connectionRepository.findPrimaryConnection(Facebook.class);

        int facebookStatus = SocialDTO.NO_CONNECTION;
        int twitterStatus = SocialDTO.NO_CONNECTION;


        //check if connection is null
        if (fbConnection == null || fbConnection.hasExpired() || !fbConnection.test()) {
            facebookStatus = SocialDTO.NO_CONNECTION;
            if (fbConnection != null) {
                connectionRepository.removeConnection(fbConnection.getKey());
            }
        } else {
            final Facebook facebook = fbConnection.getApi();
            //check if facebook api is null return -1
            if (facebook == null) {
                facebookStatus = SocialDTO.NO_CONNECTION;
            } else {
                facebookStatus = SocialDTO.NO_LIKE;

//                final List<FqlResult> results =
//                        facebook.fqlOperations().query("SELECT uid FROM page_fan WHERE uid="
//                                + fbConnection.getKey().getProviderUserId() + " AND page_id=471451736199884",
//                                new FqlResultMapper<FqlResult>() {
//                                    @Override
//                                    public FqlResult mapObject(final FqlResult fqlResult) {
//                                        return fqlResult;
//                                    }
//                                });
//                if (results != null && !results.isEmpty()) {
//                    if (results.get(0).getInteger("uid") != null
//                            && results.get(0).getString("uid").equals(fbConnection.getKey().getProviderUserId())) {
//                        facebookStatus = SocialDTO.LIKED;
//                    }
//                }
                //TODO: Check if shared(from User object)
            }
        }

        final Connection<Twitter> twitterConnection = connectionRepository.findPrimaryConnection(Twitter.class);

        if (twitterConnection == null) {

            twitterStatus = SocialDTO.NO_CONNECTION;

        } else {
            final Twitter twitter = twitterConnection.getApi();
            //check if facebook api is null return -1
            if (twitter == null) {
                twitterStatus = SocialDTO.NO_CONNECTION;
            } else {
                twitterStatus = SocialDTO.FOLLOW;
                //TODO: Check if shared(from User object)
            }
        }

        return new SocialDTO(facebookStatus, twitterStatus);
    }

    /**
     * Setter method used by spring to inject a ConnectionRepository bean.
     *
     * @param value a ConnectionRepository bean.
     */
    public void setConnectionRepository(final ConnectionRepository value) {
        this.connectionRepository = value;
    }

    /**
     * Setter method used by spring to inject a UserManager bean.
     *
     * @param value a UserManager  bean.
     */
    public void setUserManager(final UserManagerBean value) {
        this.userManager = value;
    }

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    public void setPmHistoryManager(PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }

    /**
     * Instance NewsManager class to perform queries
     * about news objects.
     */
    private transient NewsManagerBean newsManager;

    /**
     * Setter method used by spring to inject a newsManager bean.
     *
     * @param injNewsManager a newsManager bean.
     */
    public void setNewsManager(final NewsManagerBean injNewsManager) {
        newsManager = injNewsManager;
    }

    /**
     * Getter method used to access the userGameManagerionManager bean.
     *
     * @return the userGameManagerionManager bean.
     */
    public UserGameManagerBean getUserGameManager() {
        return userGameManager;
    }

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param value a nationManager bean.
     */
    public void setUserGameManager(final UserGameManagerBean value) {
        this.userGameManager = value;
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
}
