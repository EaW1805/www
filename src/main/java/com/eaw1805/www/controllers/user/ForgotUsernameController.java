package com.eaw1805.www.controllers.user;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
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
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Forgot user-name controller.
 * Recaptcha keys:
 * public: 6LfgG9gSAAAAADjchILDor7h7DWN_Eaavg1Ei6ii
 * private: 6LfgG9gSAAAAANscM_5hqgRN5svoDSXhZ6ZWVFTD
 */
@org.springframework.stereotype.Controller
public class ForgotUsernameController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(SignupController.class);

    private static final String MODEL_JSTL_KEY = "user";

    @ModelAttribute(MODEL_JSTL_KEY)
    public User getCommandObject() {
        return new User();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/forgot_username")
    public ModelAndView setupContactForm() {
        ScenarioContextHolder.defaultScenario();
        final ModelMap model = new ModelMap();

        // retrieve user
        final User thisUser = getUser();

        LOGGER.debug("[" + thisUser.getUsername() + "/" + ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getHeader("CF-Connecting-IP") + "] Forgot Username");
        return new ModelAndView("forgotUsername", model);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgot_username")
    public String processConstactSubmit(@ModelAttribute(MODEL_JSTL_KEY) final User user,
                                        final BindingResult result,
                                        final SessionStatus status)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        // retrieve user
        final User thisUser = getUser();

        if (user.getEmail().isEmpty()) {
            result.rejectValue("email", "required.email");
            return "forgotUsername";
        }

        if (user.getEmail().indexOf('@') == -1) {
            result.rejectValue("email", "required.emailRightFormat");
            return "forgotUsername";
        }

        if (user.getEmail().contains("'")
                || user.getEmail().contains(" ")
                || user.getEmail().contains("\"")
                || user.getEmail().contains("\'")
                || user.getEmail().contains("\\")
                || user.getEmail().contains("/")) {
            result.rejectValue("email", "required.field_problem");
            return "forgotUsername";
        }

        // Check if user with duplicate email exists
        final List<User> thatUser = userManager.searchByEmail(user.getEmail());

        if (thatUser.isEmpty()) {
            result.rejectValue("email", "required.unknownEmail");
            return "forgotUsername";
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
            LOGGER.info("Forgot Username: fill in CAPTCHA challenge");
            result.rejectValue("twitterSecret", "required.captcha");
            return "forgotUsername";
        }

        // Send out mail
        sendUsername(thatUser.get(0));

        LOGGER.info("Username Form: sent to user [" + thatUser.get(0) + "]");
        return "redirect:sent_username";
    }

    /**
     * Send Email Notification to the receiver of the message.
     */
    @EawAsync
    private void sendUsername(final User thisUser) {
        // Send out mail
        try {
            EmailManager.getInstance().sendUsername(thisUser);

        } catch (final MessagingException e) {
            LOGGER.error(e);
            LOGGER.error("Username Form: Failed to send email");

        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e);
            LOGGER.error("Username Form: Failed to send email");
        }
    }

}
