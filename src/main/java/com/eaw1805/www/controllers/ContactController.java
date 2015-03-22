package com.eaw1805.www.controllers;

import com.eaw1805.data.managers.beans.EngineProcessManagerBean;
import com.eaw1805.data.model.EngineProcess;
import com.eaw1805.data.model.Message;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Display contact & support page.
 * Recaptcha keys:
 * public: 6LfgG9gSAAAAADjchILDor7h7DWN_Eaavg1Ei6ii
 * private: 6LfgG9gSAAAAANscM_5hqgRN5svoDSXhZ6ZWVFTD
 */
@org.springframework.stereotype.Controller
public class ContactController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LogManager.getLogger(ContactController.class);

    private static final String MODEL_JSTL_KEY = "message";

    @ModelAttribute(MODEL_JSTL_KEY)
    public Message getCommandObject() {
        return new Message();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contact")
    public ModelAndView setupContactForm() {
        ScenarioContextHolder.defaultScenario();
        final ModelMap model = new ModelMap();
        // retrieve user
        final User thisUser = getUser();

        // retrieve engine status
        final EngineProcess lastAlive = engineManager.getByID(-1);

        // Prepare data to pass to jsp
        model.put("user", thisUser);
        model.put("engine", lastAlive);
        model.put("unreadMessagesCount", super.messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Contact & Support");
        return new ModelAndView("contact", model);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/contact")
    public String processConstactSubmit(@ModelAttribute(MODEL_JSTL_KEY) final Message message,
                                        final BindingResult result,
                                        final SessionStatus status) {
        ScenarioContextHolder.defaultScenario();

        final ModelMap model = new ModelMap();

        // retrieve user
        final User thisUser = getUser();

        // retrieve engine status
        final EngineProcess lastAlive = engineManager.getByID(-1);

        // Prepare data to pass to jsp
        model.put("user", thisUser);
        model.put("engine", lastAlive);
        model.put("unreadMessagesCount", super.messageManager.countUnreadMessagesByReceiver(thisUser));

        if (thisUser.getUserId() == -1) {
            LOGGER.debug("Contact Form: Anonymous User [name=" + message.getSender().getFullname() + ", email=" + message.getSender().getEmail() + "]");

            // Return an error if no name is given.
            final String recipientName = message.getSender().getFullname();
            if (recipientName == null || recipientName.isEmpty()) {
                LOGGER.info("Contact Form: Missing name");
                result.rejectValue("sender.fullname", "required.youname");
                return "contact";
            }

            // Return an error if no email is given.
            final String recipientEmail = message.getSender().getEmail();
            if (recipientEmail == null || recipientEmail.isEmpty()) {
                LOGGER.info("Contact Form: Missing email");
                result.rejectValue("sender.email", "required.email");
                return "contact";
            }

            //get http request object.
            final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // retrieve captcha challenge
            final String captchaChallenge = request.getParameter("recaptcha_challenge_field");

            // retrieve captcha response
            final String captchaResponse = request.getParameter("recaptcha_response_field");

            final String response = ArticleManager.getInstance().getCAPTCHAResult(thisUser.getRemoteAddress(), captchaChallenge, captchaResponse);
            LOGGER.debug(response);

            if (response.indexOf("success") == -1) {
                LOGGER.info("Contact Form: fill in CAPTCHA challenge");
                result.rejectValue("bodyMessage", "required.captcha");
                return "contact";
            }

        } else {
            message.setSender(thisUser);
            LOGGER.debug("Contact Form: Registered User [login=" + message.getSender().getUsername() + ", name=" + message.getSender().getFullname() + ", email=" + message.getSender().getEmail() + "]");
        }

        // Return an error if no subject is given.
        final String subject = message.getSubject();
        if (subject == null || subject.isEmpty()) {
            LOGGER.info("Contact Form: Missing Subject");
            result.rejectValue("subject", "required.subject");
            return "contact";
        }

        // Return an error if no subject is given.
        final String body = message.getBodyMessage();
        if (body == null || body.isEmpty()) {
            LOGGER.info("Contact Form: Missing Body");
            result.rejectValue("bodyMessage", "required.body");
            return "contact";
        }

        // Try to send out the contact form
        try {
            sendContact(message.getSender().getEmail(),
                    message.getSender().getFullname(),
                    message.getSubject(),
                    message.getBodyMessage());
        } catch (final MessagingException e) {
            LOGGER.error(e);
            LOGGER.error("Contact Form: Failed to send email");

        } catch (final UnsupportedEncodingException e) {
            LOGGER.error(e);
            LOGGER.error("Contact Form: Failed to send email");
        }

        return "redirect:contact/sent";
    }

    /**
     * Instance EngineProcessManager class to perform queries
     * about engineProcess objects.
     */
    private transient EngineProcessManagerBean engineManager;

    /**
     * Setter method used by spring to inject a EngineProcessManagerBean bean.
     *
     * @param injEngineManager a EngineProcessManagerBean bean.
     */
    public void setEngineProcessManager(final EngineProcessManagerBean injEngineManager) {
        engineManager = injEngineManager;
    }

}
