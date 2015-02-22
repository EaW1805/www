package com.eaw1805.www.controllers.user;

import com.eaw1805.data.model.Message;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


/**
 * Deletes the messages of a specific conversation.
 */
@org.springframework.stereotype.Controller
public class DeleteMessageController
        extends BaseController {

    private static final String MODEL_JSTL_KEY = "message";

    @ModelAttribute(MODEL_JSTL_KEY)
    public Message getCommandObject() {
        return new Message();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inbox/delete/{messageId}")
    public ModelAndView handleDeleteController(@PathVariable final String messageId,
                                               final HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Retrieve Game entity
        if (messageId == null || messageId.isEmpty()) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/inbox");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        final Message thisMessage = messageManager.getByID(Integer.parseInt(messageId));
        if (thisMessage == null) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/inbox");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //check if user can view this message
        final User thisUser = getUser();
        if (thisUser == null
                || (thisUser.getUserId() != thisMessage.getReceiver().getUserId()
                && thisUser.getUserId() != thisMessage.getSender().getUserId())) {
            final RedirectView redirectView = new RedirectView(request.getContextPath() + "/inbox");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        //retrieve message root id.
        int thisRootId = thisMessage.getRootId();
        if (thisRootId == 0) {
            thisRootId = thisMessage.getMessageId();
        }

        messageManager.deleteMessageThread(thisUser.getUserId(), thisRootId);

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Inbox/Delete");
        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/inbox");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }
}
