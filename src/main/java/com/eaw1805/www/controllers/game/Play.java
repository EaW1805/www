package com.eaw1805.www.controllers.game;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.managers.beans.GameSettingsManagerBean;
import com.eaw1805.data.model.*;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Start the gwt-client.
 */
@Controller
public class Play
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(Play.class);

    @RequestMapping(method = RequestMethod.GET, value = "/play/scenario/{scenarioId}/game/{gameId}/nation/{nationId}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request) throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Determine scenario        
        int scenarioNum;
        if ((scenarioId == null) || (scenarioId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            // Set the scenario
            ScenarioContextHolder.setScenario(scenarioId);

            if ("1804".equals(scenarioId)) {
                scenarioNum = HibernateUtil.DB_FREE;

            } else if ("1808".equals(scenarioId)) {
                scenarioNum = HibernateUtil.DB_S3;

            } else if ("1805".equals(scenarioId)) {
                scenarioNum = HibernateUtil.DB_S2;

            } else {//else if 1802
                scenarioNum = HibernateUtil.DB_S1;
            }
        }


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

        // Retrieve Nation entity
        Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            thisNation = getNationManager().getByID(Integer.parseInt(nationId));
            if (thisNation == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        final List<UserGame> userGames = getUserGameManager().listActive(thisUser, thisGame, thisNation);

        if (thisUser.getUserType() != 3 && (userGames.isEmpty() || !userGames.get(0).isAccepted())) {
            throw new InvalidPageException("Access denied");
        }

        if (thisUser.getUserType() != 3
                && thisUser.getCreditBought() + thisUser.getCreditFree() + thisUser.getCreditTransferred() < 0) {
            throw new InvalidPageException("Negative Balance");
        }

        // be sure to update the value if the user loads the game for the first time in the current turn
        // and set it to false if true.
        final boolean firstLoad = (!userGames.isEmpty() && userGames.get(0).isTurnFirstLoad());
        if (firstLoad) {
            userGames.get(0).setTurnFirstLoad(false);
            getUserGameManager().update(userGames.get(0));
        }




        //check if settings are full screen.
        //so you can imidiately switch client to fullscreen mode.
        final List<GameSettings> settings = gameSettingsManagerBean.getByGameNation(thisGame, thisNation);
        boolean fullScreen = false;
        if (!settings.isEmpty()) {
            fullScreen = settings.get(0).isFullScreen();
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("isTutorial", (HibernateUtil.DB_FREE == scenarioNum));
        refData.put("scenarioId", scenarioNum);
        refData.put("game", thisGame);
        refData.put("username", thisUser.getUsername());
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("nationId", thisNation.getId());
        refData.put("randomId", new Random().nextLong());
        refData.put("timeId", new Date().getTime());
        refData.put("firstLoad", firstLoad);
        refData.put("fogOfWar", thisGame.isFogOfWar());
        refData.put("fullscreen", fullScreen);
        refData.put("colonyMps", thisGame.isFullMpsAtColonies());
        refData.put("fastAssignment", thisGame.isFastAppointmentOfCommanders());
        refData.put("doubleCostsArmy", thisGame.isDoubleCostsArmy());
        refData.put("doubleCostsNavy", thisGame.isDoubleCostsNavy());
        refData.put("fastShipConstruction", thisGame.isFastShipConstruction());

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Play game=" + gameId + "/turn=" + thisGame.getTurn() + "/nation=" + thisNation.getName());
        return new ModelAndView("game/play", refData);
    }

    /**
     * A GameSettingsManager bean to perform queries about GameSettings.
     */
    @Autowired
    private transient GameSettingsManagerBean gameSettingsManagerBean;


}
