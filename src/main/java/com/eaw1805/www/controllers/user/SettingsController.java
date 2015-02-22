package com.eaw1805.www.controllers.user;

import com.eaw1805.data.managers.beans.PaymentHistoryManagerBean;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.PaymentHistory;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.commands.PaypalCommand;
import com.eaw1805.www.commands.TransferCommand;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.validators.SettingsValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Display the settings of the user account.
 */
@Controller
public class SettingsController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SettingsController.class);

    protected static final String MODEL_JSTL_KEY = "user";

    /**
     * Instance of ConnectionRepository class.
     */
    protected transient ConnectionRepository conRepository;

    /**
     * Instance of PaymentHistoryManager class.
     */
    protected transient PaymentHistoryManagerBean pmHistoryManager;

    /**
     * Instance of ConnectionFactoryLocator class..
     */
    protected transient ConnectionFactoryLocator conFactoryLocator;

    @ModelAttribute(MODEL_JSTL_KEY)
    public User populateUser() {
        User user = getUser();
        if (user == null) {
            user = new User();
        }
        return user;
    }

    @ModelAttribute("transferCommand")
    public TransferCommand transferCommand() {
        return new TransferCommand();
    }

    @ModelAttribute("paypalCommand")
    public PaypalCommand paypalCommand() {
        return new PaypalCommand();
    }

    @ModelAttribute("followingListAll")
    public Set<User> getFollowingAll() {
        // get this user leaders.
        final List<Follow> leaders = followManager.listByFollower(getUser(), true);
        final TreeSet<User> leadersSet = new TreeSet<User>(new Comparator<User>() {

            public int compare(final User thisUser, final User otherUser) {
                return thisUser.getUsername().compareToIgnoreCase(otherUser.getUsername());
            }
        });
        for (final Follow leader : leaders) {
            leadersSet.add(leader.getLeader());
        }
        return leadersSet;
    }

    @ModelAttribute("page")
    public String populatePage() {
        return getPage();
    }

    @ModelAttribute("providerIds")
    public Set<String> getProviderIds() {
        return conFactoryLocator.registeredProviderIds();
    }

    @ModelAttribute("connectionMap")
    public Map<String, List<Connection<?>>> getConnectionMap() {
        return conRepository.findAllConnections();
    }

    @ModelAttribute("history")
    public List<PaymentHistory> getPaymentHistory() {
        return pmHistoryManager.list(getUser());
    }

    @ModelAttribute("dates")
    public Map<Integer, String> getGamesDates() {
        // Calculate calendar dates for each game
        final Map<Integer, String> dates = new HashMap<Integer, String>();

        for (int db = ScenarioContextHolder.FIRST_SCENARIO; db <= ScenarioContextHolder.LAST_SCENARIO; db++) {
            ScenarioContextHolder.setScenario(db);

            for (final WatchGame game : watchGameManager.listByUser(getUser())) {
                final StringBuilder strBuilder = new StringBuilder();
                final Calendar thatCal = calendar(game);
                strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
                strBuilder.append(" ");
                strBuilder.append(thatCal.get(Calendar.YEAR));
                dates.put(game.getWatchGameId(), strBuilder.toString());
            }
        }
        return dates;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/settings")
    public String setupForm(final ModelMap model) {
        return "user/settings";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/settings")
    public String processSettingsSubmit(
            @ModelAttribute(MODEL_JSTL_KEY) final User user, final BindingResult result, final SessionStatus status) {
        ScenarioContextHolder.defaultScenario();

        final SettingsValidator userValidator = new SettingsValidator();
        userValidator.setUserManager(userManager);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "user/settings";
        }

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //Get Old Password
        String oldPasswordMD5 = request.getParameter("oldPasswd");
        if (oldPasswordMD5 != null && !oldPasswordMD5.isEmpty()) {
            try {
                oldPasswordMD5 = convertToMD5(oldPasswordMD5);

            } catch (NoSuchAlgorithmException e) {
                LOGGER.debug("could not convert password to md5");
            }
        }

        //Return an error if the given old password is wrong.
        if (oldPasswordMD5 != null && !oldPasswordMD5.isEmpty()
                && !oldPasswordMD5.equals(getUser().getPassword())) {
            LOGGER.info(oldPasswordMD5);
            result.rejectValue("password", "error.oldpassword");
            return "user/settings";
        }

        //Update Notification and Public email.
        final String notification = request.getParameter("notification");
        final String publicEmail = request.getParameter("publicemail");

        if (notification != null && notification.equals("enable")) {
            user.setEnableNotifications(true);
        } else {
            user.setEnableNotifications(false);
        }
        String link = "http://www.oplongames.com/?q=node/183&mail=" + user.getEmail() + "&act=";//+"{sub|unsub} "
        if (user.getEnableNotifications()) {
            link += "sub";
        } else {
            link += "unsub";
        }
        LOGGER.debug("sending request: " + link);


        try {
            //send the data
            final URL url = new URL(link);
            final HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
            httpcon.getInputStream().close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (publicEmail != null && publicEmail.equals("enable")) {
            user.setPublicMail(true);
        } else {
            user.setPublicMail(false);
        }

        final String inboxThreadSizeStr = request.getParameter("drop1");
        try {
            final Integer inboxThreadSize = Integer.valueOf(inboxThreadSizeStr);
            user.setInboxThreadSize(inboxThreadSize);
        } catch (final NumberFormatException ex) {
            //Number Format Exception.
            //Do not update inbox thread size.
        }

        //update password
        String passwordMD5 = request.getParameter("newPassword");
        if (passwordMD5 != null && !passwordMD5.isEmpty()) {
            //Return an error if the given password is wrong.
            if (passwordMD5.length() < 6) {
                result.rejectValue("password", "required.password_short");
                return "user/settings";
            }
            try {
                passwordMD5 = convertToMD5(passwordMD5);

            } catch (NoSuchAlgorithmException e) {
                LOGGER.debug("could not convert password to md5");
            }
            user.setPassword(passwordMD5);
        }

        //Update Email
        String emailMD5 = user.getEmail();
        try {
            emailMD5 = convertToMD5(user.getEmail());

        } catch (NoSuchAlgorithmException e) {
            LOGGER.debug("could not convert email to md5");
        }

        //update user variables
        user.setEmailEncoded(emailMD5);

        //finally save user to database
        userManager.update(user);
        return "redirect:settings";
    }

    /**
     * Get the current page of settings to display.
     *
     * @return A string that shows the page.
     */
    public String getPage() {
        return "settings";
    }

    /**
     * Get the current calendar.
     *
     * @param thisGame the WatchGame instance.
     * @return the calendar.
     */
    public final Calendar calendar(final WatchGame thisGame) {
        final Calendar thisCal = Calendar.getInstance();
        thisCal.set(1805, Calendar.JANUARY, 1);
        thisCal.add(Calendar.MONTH, thisGame.getGame().getTurn());
        return thisCal;
    }

    public void setConFactoryLocator(final ConnectionFactoryLocator conFactoryLocator) {
        this.conFactoryLocator = conFactoryLocator;
    }

    public void setConRepository(final ConnectionRepository conRepository) {
        this.conRepository = conRepository;
    }

    public void setPmHistoryManager(final PaymentHistoryManagerBean pmHistoryManager) {
        this.pmHistoryManager = pmHistoryManager;
    }

}

