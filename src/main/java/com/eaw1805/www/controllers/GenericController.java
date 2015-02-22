package com.eaw1805.www.controllers;

import com.eaw1805.data.model.User;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Display all static pages.
 */
public class GenericController
        extends BaseController
        implements Controller {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(GenericController.class);

    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        // retrieve user
        final User thisUser = getUser();

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("helpPage", helpPage);
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", super.messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("globalMessages", getGlobalChatMessages());
        refData.put("userNewAchievements", getNewAchievements());
        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] " + viewName);
        return new ModelAndView(viewName, refData);
    }

    /**
     * the page name of the player's handbook.
     */
    private String helpPage;

    /**
     * Set the page name of the player's handbook.
     *
     * @param value the page name of the player's handbook.
     */
    public void setHelpPage(final String value) {
        this.helpPage = value;
    }

    /**
     * the name of the view to use.
     */
    private String viewName;

    /**
     * Set the name of the view to use.
     *
     * @param value the name of the view to use.
     */
    public void setViewName(final String value) {
        this.viewName = value;
    }

}
