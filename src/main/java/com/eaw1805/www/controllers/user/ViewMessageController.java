package com.eaw1805.www.controllers.user;

import com.eaw1805.core.EmailManager;
import com.eaw1805.data.model.Message;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.antisamy.AntisamyManager;
import com.eaw1805.www.controllers.cache.async.EawAsync;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.validators.InboxValidator;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * Displays the messages of a specific conversation.
 */
@org.springframework.stereotype.Controller
public class ViewMessageController
        extends BaseController {


    private static final String MODEL_JSTL_KEY = "message";

    private static final String HOME = "/home";

    /**
     * Instance of AntisamManager class.
     */
    protected transient AntisamyManager antisamyManager;

    @ModelAttribute(MODEL_JSTL_KEY)
    public Message getCommandObject() {
        return new Message();
    }

    /**
     * Send Email Notification to the receiver of the message.
     *
     * @param message the input message.
     */
    @EawAsync
    private void sendEmailNotification(final Message message) {
        // Send out mail
        EmailManager.getInstance().sendMessageNotification(message);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/inbox/private/{messageId}/view")
    public ModelAndView processPostReplyMessage(
            @ModelAttribute(MODEL_JSTL_KEY) Message message, final BindingResult result, @PathVariable final String messageId,
            final SessionStatus status, final ModelMap model) throws InvalidPageException {

        ScenarioContextHolder.defaultScenario();

        //get http request object.
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        final String recipient = request.getParameter("recipient");

        Message thisMessage = messageManager.getByID(Integer.parseInt(messageId));
        if (thisMessage == null) {
            thisMessage = new Message();
        }

        //check if user can view this message
        final User thisUser = getUser();
        if (thisUser == null
                || (thisUser.getUserId() != thisMessage.getReceiver().getUserId()
                && thisUser.getUserId() != thisMessage.getSender().getUserId())) {
            throw new InvalidPageException("Access Denied");
        }

        //retrieve message root id.
        int thisRootId = thisMessage.getRootId();
        if (thisRootId == 0) {
            thisRootId = thisMessage.getMessageId();
        }

        model.put("messageList", messageManager.retrieveHistoryDesc(thisRootId));

        //check that recipient is given.
        if (recipient == null || recipient.isEmpty()) {
            throw new InvalidPageException("Access Denied");
        }

        final User endUser = userManager.getByUserName(recipient);

        //check that users are valid.
        if (endUser == null || thisUser.equals(endUser)) {
            throw new InvalidPageException("Access Denied");
        }

        //Clean the HTML code.
        message.setBodyMessage(antisamyManager.scanWithOutImgTag(message.getBodyMessage()));

        final InboxValidator inboxValidator = new InboxValidator();
        inboxValidator.validate(message, result);
        if (result.hasErrors()) {
            return new ModelAndView("user/inbox/view", model);
        }

        final Message rootMessage =
                messageManager.getRootMessage(thisUser.getUserId(), endUser.getUserId(), message.getSubject());
        //This is a new root message
        if (rootMessage == null) {
            message.setRootId(0);
        } else {
            message.setRootId(rootMessage.getMessageId());
        }

        message.setSender(thisUser);
        message.setReceiver(endUser);
        message.setDate(new Date());
        message.setOpened(false);

        messageManager.add(message);

        if (message.getReceiver().getEnableNotifications()) {
            if (message.getRootId() == 0) {
                final Message newMessage =
                        messageManager.getRootMessage(thisUser.getUserId(), endUser.getUserId(), message.getSubject());
                message.setRootId(newMessage.getMessageId());
            }
            sendEmailNotification(message);
        }

        model.put("messageList", messageManager.retrieveHistoryDesc(thisRootId));
        model.addAttribute("message", new Message());
        return new ModelAndView("user/inbox/view", model);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/inbox/private/{messageId}/view")
    protected ModelAndView viewMessage(@PathVariable final String messageId,
                                       final HttpServletRequest request, final ModelMap model) throws Exception {

        // Retrieve Game entity
        if (messageId == null || messageId.isEmpty()) {
            throw new InvalidPageException("Access Denied");
        }

        Message thisMessage = messageManager.getByID(Integer.parseInt(messageId));
        if (thisMessage == null) {
            thisMessage = new Message();
        }

        //check if user can view this message
        final User thisUser = getUser();
        if (thisUser == null
                || (thisUser.getUserId() != thisMessage.getReceiver().getUserId()
                && thisUser.getUserId() != thisMessage.getSender().getUserId())) {
            throw new InvalidPageException("Access Denied");
        }


        //retrieve message root id.
        int thisRootId = thisMessage.getRootId();
        if (thisRootId == 0) {
            thisRootId = thisMessage.getMessageId();
        }

        model.put("messageList", messageManager.retrieveHistoryDesc(thisRootId));
        return new ModelAndView("user/inbox/view", model);
    }

    /**
     * Getter method used to access the antisamyManager bean.
     *
     * @return the antisamyManager bean.
     */
    public AntisamyManager getAntisamyManager() {
        return antisamyManager;
    }

    /**
     * Setter method used by spring to inject a AntisamyManager bean.
     *
     * @param value a antisamyManager bean.
     */
    public void setAntisamyManager(final AntisamyManager value) {
        this.antisamyManager = value;
    }
}
