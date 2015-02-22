package com.eaw1805.www.controllers.user;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.model.Follow;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.controllers.validators.SignupValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create new user accounts.
 * Recaptcha keys:
 * public: 6LfgG9gSAAAAADjchILDor7h7DWN_Eaavg1Ei6ii
 * private: 6LfgG9gSAAAAANscM_5hqgRN5svoDSXhZ6ZWVFTD
 */
@org.springframework.stereotype.Controller
public class SignupController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(SignupController.class);

    private static final String MODEL_JSTL_KEY = "user";

    @Autowired
    @Qualifier("jdbcUserService")  // <-- this references the bean id
    public UserDetailsManager userDetailsManager;

    @ModelAttribute(MODEL_JSTL_KEY)
    public User getCommandObject() {
        return new User();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/signup")
    public ModelAndView setupContactForm() {
        final ModelMap model = new ModelMap();

        // retrieve user
        final User thisUser = getUser();

        LOGGER.debug("[" + thisUser.getUsername() + "/" + ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getHeader("CF-Connecting-IP") + "] Signup");
        return new ModelAndView("signup", model);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public String processConstactSubmit(@ModelAttribute(MODEL_JSTL_KEY) final User user,
                                        final BindingResult result,
                                        final SessionStatus status)
            throws Exception {

        ScenarioContextHolder.defaultScenario();

        // retrieve user
        final User thisUser = getUser();
        user.setRemoteAddress(thisUser.getRemoteAddress());

        // validate user data
        final SignupValidator userValidator = new SignupValidator();
        userValidator.setUserManager(userManager);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "signup";
        }

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        // retrieve captcha challenge
        final String captchaChallenge = request.getParameter("recaptcha_challenge_field");

        // retrieve captcha response
        final String captchaResponse = request.getParameter("recaptcha_response_field");

        final String response = ArticleManager.getInstance().getCAPTCHAResult(thisUser.getRemoteAddress(), captchaChallenge, captchaResponse);

        // validate CAPTCHA
        if (!response.contains("success")) {
            LOGGER.info("Signup Form: fill in CAPTCHA challenge");
            result.rejectValue("twitterSecret", "required.captcha");
            return "signup";
        }

        // proceed with registration of user
        user.setRemoteAddress(thisUser.getRemoteAddress());
        user.setPassword(convertToMD5(user.getPassword()));
        user.setEmailEncoded(convertToMD5(user.getEmail()));
        user.setCreditFree(50);
        userManager.addUser(user);

        LOGGER.info("Signup Form: new user created [" + user.getUsername() + "]");

        // Make admin follow the new player
        final User admin = userManager.getByUserName("admin");
        final User newUser = userManager.getByUserName(user.getUsername());
        final Follow adminFollow = new Follow();
        adminFollow.setLeader(newUser);
        adminFollow.setFollower(admin);
        followManager.add(adminFollow);

        //Make user subscribed in drupals database
        final String link = "http://www.oplongames.com/?q=node/183&mail=" + newUser.getEmail() + "&act=sub";
        LOGGER.debug("sending request: " + link);
        try {
            //send the data
            final URL url = new URL(link);
            final HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
            httpcon.getInputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send out mail
        sendWelcome(user);

        LOGGER.info("Signup Form: welcome sent to new user [" + user.getEmail() + "]");

        // Create new Solo game
        createSoloGame(newUser);

        //Authenticate user
        authenticateUser(user.getUsername(), userDetailsManager, request);

        // Call Jenkins
        callJenkins();

        LOGGER.info("Signup Form: creating new solo game to new user [" + user.getUsername() + "]");
        return "redirect:welcome";
    }

    /**
     * Send Email Notification to the receiver of the message.
     *
     * @param thisUser the new User.
     */
    @EawAsync
    private void sendWelcome(final User thisUser) {
        // Send out mail
        try {
            EmailManager.getInstance().sendWelcome(thisUser);

        } catch (final MessagingException e) {
            LOGGER.error(e);
            LOGGER.error("Signup Form: Failed to send email");
        } catch ( UnsupportedEncodingException e) {
            LOGGER.error(e);
            LOGGER.error("Signup Form: Failed to send email");
        }
    }

    /**
     * Creates a new Solo game.
     *
     * @param thisUser the new User.
     */
    private void createSoloGame(final User thisUser) {
        // Creates a new Solo Game.
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);

        // Check if user has an ongoing game (only 1 is allowed)
        final List<UserGame> lstGames = getUserGameManager().list(thisUser);
        if (lstGames.isEmpty()) {
            LOGGER.info("Creating Game");
            // Construct new game instance.
            final Game newGame = new Game();
            final List<Game> allFree = getGameManager().list();
            final Game lastFree = allFree.get(allFree.size() - 1);
            newGame.setGameId(lastFree.getGameId() + 1);
            newGame.setTurn(-2);
            newGame.setScenarioId(HibernateUtil.DB_FREE);
            newGame.setEnded(false);
            newGame.setWinners("");
            newGame.setSchedule(4);
            newGame.setDiscount(0);
            newGame.setStatus(GameConstants.GAME_SCHED);
            newGame.setForumId(2);
            newGame.setDescription(thisUser.getUsername());
            newGame.setDateStart(new Date());
            newGame.setDateLastProc(new Date());
            newGame.setCronSchedule("");
            newGame.setCronScheduleDescr("");
            newGame.setFogOfWar(true);
            newGame.setRandomEvents(false);
            newGame.setFieldBattle(false);
            newGame.setType(GameConstants.DURATION_NORMAL);
            newGame.setBoostedTaxation(false);
            newGame.setBoostedProduction(false);
            newGame.setFastPopulationGrowth(false);
            newGame.setBoostedCAPoints(false);
            newGame.setFierceCasualties(false);
            newGame.setFastAppointmentOfCommanders(false);
            newGame.setExtendedArrivalOfCommanders(false);
            newGame.setFullMpsAtColonies(false);
            newGame.setAlwaysSummerWeather(false);
            newGame.setFastShipConstruction(false);
            newGame.setExtendedEspionage(false);
            newGame.setFastFortressConstruction(false);
            newGame.setRumorsEnabled(false);


            // Identify date of next processing
            final Calendar nextTurn = Calendar.getInstance();
            nextTurn.set(Calendar.HOUR, 0);
            nextTurn.set(Calendar.MINUTE, 0);
            nextTurn.set(Calendar.SECOND, 0);
            nextTurn.add(Calendar.DATE, -newGame.getSchedule());
            newGame.setDateNextProc(nextTurn.getTime());
            LOGGER.info("Save Game");
            getGameManager().add(newGame);

            // Add User-Game association
            final UserGame newUserGame = new UserGame();
            newUserGame.setGame(newGame);
            newUserGame.setUserId(thisUser.getUserId());
            newUserGame.setAccepted(true);
            newUserGame.setCost(0);
            newUserGame.setTurnDrop(0);
            newUserGame.setTurnPickUp(0);
            newUserGame.setCurrent(true);
            newUserGame.setHasWon(false);
            newUserGame.setActive(true);
            newUserGame.setAlive(true);
            newUserGame.setOffer(0);
            newUserGame.setNation(getNationManager().getByID(NationConstants.NATION_FRANCE));
            LOGGER.info("Save UserGame");
            getUserGameManager().add(newUserGame);
        }

        // Change again to default Scenario
        ScenarioContextHolder.defaultScenario();
    }

    /**
     * Request Jenkins to execute job.
     */
    @EawAsync
    private void callJenkins() {
        //Request Jenkins to execute job
        getArticleManager().getBuild(HibernateUtil.DB_FREE, 0);
    }

    /**
     * Authenticates user after successful signup.
     *
     * @param username    the username of the current user.
     * @param userManager the userManager Service.
     * @param request     the HTTP Request to create a new session
     */
    public void authenticateUser(final String username,
                                 final UserDetailsManager userManager,
                                 final HttpServletRequest request) {
        try {
            // generate session if one doesn't exist
            request.getSession();

            // Authenticate the user
            UserDetails user = userManager.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
