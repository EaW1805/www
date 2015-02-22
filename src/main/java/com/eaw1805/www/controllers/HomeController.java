package com.eaw1805.www.controllers;

import com.eaw1805.data.model.User;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for main page.
 */
@org.springframework.stereotype.Controller
public class HomeController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(HomeController.class);

    @RequestMapping(method = RequestMethod.GET, value = {"/", "home"})
    public ModelAndView handleRequest(final HttpServletRequest request, final ModelMap model)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
        // retrieve user
        final User thisUser = getUser();

        // Prepare data to pass to jsp
        model.put("index", 1);
        model.put("profileUser", thisUser);
        model.put("user", thisUser);
        model.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        model.put("news", getArticleManager().getNews());

        model.put("hofProfiles", gameHelper.getTopPlayers());
        model.putAll(gameHelper.prepareTopNations());

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Home");
        return new ModelAndView("index", model);
    }


}
