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
 * Display Handbook's Table of Contents.
 */
@org.springframework.stereotype.Controller
public class HandbookController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(HandbookController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/handbook")
    protected ModelAndView handle(final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();
        final User thisUser = getUser();

        model.put("user", thisUser);
        model.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        model.put("titles", HelpController.titles);
        model.put("mainTitles", HelpController.mainTitles);
        model.put("staticItems", HelpController.staticItems);
        model.put("tocItems", HelpController.toc);
        model.put("chapters", HelpController.tocDescription);
        model.put("tocImages", HelpController.tocImages);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Handbook");
        return new ModelAndView("handbook", model);
    }

}
