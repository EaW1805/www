package com.eaw1805.www.controllers.game.helper;


import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Returns the status of the creation of a new solo game.
 */
@SuppressWarnings("restriction")
@Controller
public class SoloGameStatusController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SoloGameStatusController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/solo/user/{username}")
    @ResponseBody
    public String handleRequest(@PathVariable("username") String username,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {

        final User thisUser = getUser();

        if (thisUser == null
                || thisUser.getUserId() == -1
                || !thisUser.getUsername().equals(username)) {
            throw new InvalidPageException("Access Denied");
        }

        // Creates a new Solo Game.
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);

        //Solo Games for the specific user.
        final List<UserGame> lstGames = getUserGameManager().list(thisUser);

        if (lstGames == null || lstGames.isEmpty()) {
            LOGGER.info("SoloGameSetup: creating new solo game to new user [" + thisUser.getUsername() + "]" + request.getQueryString());
            return "-3";

        } else {
            LOGGER.info("SoloGameSetup: new solo game created for new user [" + thisUser.getUsername() + "]" + request.getQueryString());
            return String.valueOf(lstGames.get(0).getGame().getTurn());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/solo/user/{username}/game")
    @ResponseBody
    public String soloGameUrl(@PathVariable("username") String username,
                              final HttpServletRequest request,
                              final HttpServletResponse response) throws Exception {

        final User thisUser = getUser();

        if (thisUser == null
                || thisUser.getUserId() == -1
                || !thisUser.getUsername().equals(username)) {
            throw new InvalidPageException("Access Denied");
        }

        // Creates a new Solo Game.
        ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);

        //Solo Games for the specific user.
        final List<UserGame> lstGames = getUserGameManager().list(thisUser);

        if (lstGames == null || lstGames.isEmpty()) {
            LOGGER.info("SoloGamePath: new solo game is being created for new user [" + thisUser.getUsername() + "]" + request.getQueryString());
            return "/games";

        } else {
            if (lstGames.get(0).getGame().getTurn() == 0) {
                LOGGER.info("SoloGamePath: new solo game created for new user [" + thisUser.getUsername() + "]" + request.getQueryString());
                return lstGames.get(0).getGame().getGameId() + "/nation/5";

            } else {
                LOGGER.info("SoloGamePath: new solo game is being created for new user [" + thisUser.getUsername() + "]" + request.getQueryString());
                return "-2";
            }
        }
    }
}
