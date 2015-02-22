package com.eaw1805.www.controllers.game;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Responsible for showing all active games.
 */
@Controller
public class GamesController
        extends BaseController
        implements ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(GamesController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/listgames")
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Prepare data to pass to jsp
        final Map<String, Object> refData = gameHelper.prepareGamesList();

        // retrieve user
        final User thisUser = getUser();

        refData.put("user", thisUser);
        refData.put("watchedGames", getWatchedGames(thisUser));
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.putAll(gameHelper.getAllNewNations());
        refData.putAll(gameHelper.getAllFreePlayedNations());

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] List games");
        return new ModelAndView("games", refData);
    }

}
