package com.eaw1805.www.controllers.game;

import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.WatchGame;
import com.eaw1805.www.controllers.BaseController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Enable/Disable watch game.
 */
@Controller
public class WatchGameController
        extends BaseController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(WatchGameController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/toggleWatch/{action}")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String action,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);

        //if game id not provided then return home.
        if (gameId == null || gameId.isEmpty()
                || action == null || action.isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        Game thisGame = gameManager.getByID(Integer.parseInt(gameId));

        //if game doesn't exist in database then return home.
        if (thisGame == null) {
            throw new InvalidPageException("Page not found");
        }

        final User thisUser = getUser();
        LOGGER.debug("this user is : " + thisUser.getUsername() + " : " + thisUser.getUserId());

        //search if user is already watching the project
        List<WatchGame> watchGame = watchGameManager.listByUserGame(thisUser, thisGame);

        //if requested watch and is not watching already.
        if ("watch".equalsIgnoreCase(action)) {
            if (watchGame.isEmpty()) {
                WatchGame watch = new WatchGame();
                watch.setGame(thisGame);
                watch.setUserId(thisUser.getUserId());
                watchGameManager.add(watch);
            }
        }
        //if requested unwatch and is already watching.
        if ("unwatch".equalsIgnoreCase(action)) {
            if (!watchGame.isEmpty()) {
                watchGameManager.delete(watchGame.get(0));
            }
        }

        final RedirectView redirectView = new RedirectView(request.getContextPath() + "/scenario/" + thisGame.getScenarioIdToString() + "/game/" + thisGame.getGameId() + "/info");
        redirectView.setExposeModelAttributes(false);
        return new ModelAndView(redirectView);
    }

    protected transient GameManagerBean gameManager;

    public void setGameManager(GameManagerBean gameManager) {
        this.gameManager = gameManager;
    }

}
