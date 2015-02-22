package com.eaw1805.www.controllers.user;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.site.ArticleManager;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
import java.util.Random;

/**
 * Forgot password controller.
 * Recaptcha keys:
 * public: 6LfgG9gSAAAAADjchILDor7h7DWN_Eaavg1Ei6ii
 * private: 6LfgG9gSAAAAANscM_5hqgRN5svoDSXhZ6ZWVFTD
 */
@org.springframework.stereotype.Controller
public class ForgotPasswordController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(ForgotPasswordController.class);

    private static final String MODEL_JSTL_KEY = "user";

    @ModelAttribute(MODEL_JSTL_KEY)
    public User getCommandObject() {
        return new User();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/forgot_password")
    public ModelAndView setupContactForm() {
        final ModelMap model = new ModelMap();

        // retrieve user
        final User thisUser = getUser();

        LOGGER.debug("[" + thisUser.getUsername() + "/" + ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getHeader("CF-Connecting-IP") + "] Forgot Password");
        return new ModelAndView("forgotPassword", model);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgot_password")
    public String processConstactSubmit(@ModelAttribute(MODEL_JSTL_KEY) final User user,
                                        final BindingResult result,
                                        final SessionStatus status)
            throws Exception {

        // retrieve user
        final User thisUser = getUser();

        if (user.getUsername().isEmpty()) {
            result.rejectValue("username", "required.username");
            return "forgotPassword";
        }

        if (user.getUsername().contains("'")
                || user.getUsername().contains(" ")
                || user.getUsername().contains("\"")
                || user.getUsername().contains("\'")
                || user.getUsername().contains("\\")
                || user.getUsername().contains("/")) {
            result.rejectValue("username", "required.field_problem");
            return "forgotPassword";
        }


        // Check user exists
        final User thatUser = userManager.getByUserName(user.getUsername());
        if (thatUser == null) {
            result.rejectValue("username", "required.unknownUserName");
            return "forgotPassword";
        }

        if (user.getEmail().isEmpty()) {
            result.rejectValue("email", "required.email");
            return "forgotPassword";
        }

        if (user.getEmail().indexOf('@') == -1) {
            result.rejectValue("email", "required.emailRightFormat");
            return "forgotPassword";
        }

        if (user.getEmail().contains("'")
                || user.getEmail().contains(" ")
                || user.getEmail().contains("\"")
                || user.getEmail().contains("\'")
                || user.getEmail().contains("\\")
                || user.getEmail().contains("/")) {
            result.rejectValue("email", "required.field_problem");
            return "forgotPassword";
        }

        // Check if user with duplicate email exists
        if (!thatUser.getEmail().equalsIgnoreCase(user.getEmail())) {
            result.rejectValue("email", "required.unknownEmail");
            return "forgotPassword";
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
            LOGGER.info("Forgot Password: fill in CAPTCHA challenge");
            result.rejectValue("twitterSecret", "required.captcha");
            return "forgotPassword";
        }

        // Get a new - random - password
        final String newPassword = randomPassword();

        // Update user account
        thatUser.setPassword(convertToMD5(newPassword));
        userManager.update(thatUser);

        // Send out mail
        sendPassword(thatUser, newPassword);

        LOGGER.info("Password Form: sent to user [" + thatUser.getEmail() + "]");
        return "redirect:sent_password";
    }

    /**
     * Send Email Notification to the receiver of the message.
     */
    @EawAsync
    private void sendPassword(final User thisUser, final String newPassword) {
        // Send out mail
        try {
            EmailManager.getInstance().sendPassword(thisUser, newPassword);

        } catch (final MessagingException e) {
            LOGGER.error(e);
            LOGGER.error("Password Form: Failed to send email");

        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e);
            LOGGER.error("Password Form: Failed to send email");
        }
    }

    private void validateSpecial(final Errors errors, final String value, final String field) {
        if (value.contains("'")
                || value.contains(" ")
                || value.contains("\"")
                || value.contains("\'")
                || value.contains("\\")
                || value.contains("/")) {
            errors.rejectValue(field, "required.field_problem");
        }
    }

    private String randomPassword() {
        final char[][] values = {{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'},
                {'@', '&', '$', '#', '%', '*'},
                {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'},
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}};
        final Random rand = new Random();
        final StringBuilder newPasswd = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            final int type = rand.nextInt(4);
            final int idx = rand.nextInt(values[type].length);
            newPasswd.append(values[type][idx]);
        }

        return newPasswd.toString();
    }


}
