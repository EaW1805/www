package com.eaw1805.www.controllers.user;

import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PostMessageController extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PostMessageController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/postMessage")
    public String processPostMessage(final HttpServletRequest request) {

        try {
            LOGGER.debug("posting message");
            User currentUser = getUser();
            currentUser.setProfileHtml(Jsoup.clean(request.getParameter("messageBody"), Whitelist.relaxed()));
            userManager.update(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:games";
    }


}
