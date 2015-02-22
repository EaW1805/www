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
 * News Controller.
 */
public class NewsController
        extends BaseController
        implements Controller {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(NewsController.class);

    /**
     * the name of the view to use.
     */
    private String viewName;

    /**
     * the page name of the player's handbook.
     */
    private String helpPage;

    /**
     * Default Constructor.
     */
    public NewsController() {
        super();
    }

    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        // retrieve user
        final User thisUser = getUser();

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("helpPage", helpPage);
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        if (viewName.equals("news")) {
            refData.put("news", getArticleManager().getNews());

        } else if (viewName.equals("newsNoCache")) {
            refData.put("news", getArticleManager().getNewsNoCache());
        }

        refData.put("globalMessages", getGlobalChatMessages());
        refData.put("userNewAchievements", getNewAchievements());

        refData.put("activeUsers", activeUsers());
        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] About");
        return new ModelAndView("news", refData);
    }

    /**
     * Set the page name of the player's handbook.
     *
     * @param value the page name of the player's handbook.
     */
    public void setHelpPage(final String value) {
        this.helpPage = value;
    }

    /**
     * Set the name of the view to use.
     *
     * @param value the name of the view to use.
     */
    public void setViewName(final String value) {
        this.viewName = value;
    }
}


