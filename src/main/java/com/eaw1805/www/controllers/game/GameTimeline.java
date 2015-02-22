package com.eaw1805.www.controllers.game;

import com.eaw1805.data.cache.GameCachable;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.User;
import com.eaw1805.www.controllers.BaseController;

import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for showing all active games.
 */
@Controller
public class GameTimeline
        extends BaseController
        implements ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(GameTimeline.class);

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/timeline")
    protected ModelAndView handle(@PathVariable("scenarioId") String scenarioId,
                                  final @PathVariable("gameId") String gameId,
                                  final HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);

        // Retrieve Game entity
        Game thisGame;

        if ((gameId == null) || (gameId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisGame = getGameManager().getByID(Integer.parseInt(gameId));
            if (thisGame == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // retrieve user
        final User thisUser = getUser();

        // produce report and prepare data to pass to jsp
        final Map<String, Object> refData = prepareTimeline(thisGame);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show Timeline " + gameId);
        return new ModelAndView("game/timeline", refData);
    }

    /**
     * Prepare report.
     *
     * @param thisGame the game to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @GameCachable()
    public Map<String, Object> prepareTimeline(final Game thisGame) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Also send public news
        refData.put("game", thisGame);
        refData.put("news", newsManager.listPublic(thisGame));

        // Fill in months with their human readable date
        final Calendar thatCal = Calendar.getInstance();
        thatCal.set(1805, Calendar.JANUARY, 1);
        final int turnId = thisGame.getTurn();
        final Map<Integer, String> months = new HashMap<Integer, String>();

        for (int turn = 0; turn <= turnId; turn++) {
            final StringBuilder strBuff = new StringBuilder();
            strBuff.append(String.valueOf(thatCal.get(Calendar.YEAR)));
            strBuff.append(",");
            strBuff.append(String.valueOf(thatCal.get(Calendar.MONTH)));
            strBuff.append(",1");

            months.put(turn, strBuff.toString());
        }

        refData.put("months", months);

        return refData;
    }

    /**
     * Instance GameManagerBean class to perform queries
     * about Game objects.
     */
    private transient GameManagerBean gameManagerBean;

    /**
     * Setter method used by spring to inject a GameManagerBean bean.
     *
     * @param injGameManagerBean a GameManagerBean bean.
     */
    public void setGameManagerBean(final GameManagerBean injGameManagerBean) {
        gameManagerBean = injGameManagerBean;
    }

    /**
     * Access the GameManagerBean bean.
     *
     * @return the GameManagerBean bean.
     */
    public GameManagerBean getGameManager() {
        return gameManagerBean;
    }

    /**
     * Instance NewsManager class to perform queries
     * about news objects.
     */
    private transient NewsManagerBean newsManager;

    /**
     * Setter method used by spring to inject a newsManager bean.
     *
     * @param injNewsManager a newsManager bean.
     */
    public void setNewsManager(final NewsManagerBean injNewsManager) {
        newsManager = injNewsManager;
    }

}
