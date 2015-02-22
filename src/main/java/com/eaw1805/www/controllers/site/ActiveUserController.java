package com.eaw1805.www.controllers.site;

import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Returns active users.
 */
@Controller
public class ActiveUserController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ActiveUserController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/activeUsers")
    protected ModelAndView handle(final HttpServletRequest request, final ModelMap model) throws Exception {

        User thisUser = getUser();
        //check that users are valid.
        if (thisUser == null) {
            thisUser = new User();
            thisUser.setUserId(-1);
            thisUser.setUsername("anonymous");
        }

        //  LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Active Users");
        return new ModelAndView("activeUsers", model);
    }
}
