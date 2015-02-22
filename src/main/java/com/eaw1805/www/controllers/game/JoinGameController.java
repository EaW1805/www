package com.eaw1805.www.controllers.game;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Display new and free games.
 */
@org.springframework.stereotype.Controller
public class JoinGameController
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(JoinGameController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/joingame")
    protected ModelAndView viewGames(final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();
        // Retrieve Game entity
        final User thisUser = getUser();

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] JoinGame");
        model.put("firstPage", true);
        return new ModelAndView("joinGame", model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/joingame/{type}")
    protected ModelAndView viewSpecificGame(@PathVariable("type") String type,
                                            final HttpServletRequest request, final ModelMap model) throws Exception {
        ScenarioContextHolder.defaultScenario();

        // Retrieve Game entity
        final User thisUser = getUser();
        String actionType = type;

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] JoinGame " + type + " / " + actionType);

        final Map<String, Object> freeNationsData;
        if (type.equals("running")) {
            ScenarioContextHolder.setScenario(HibernateUtil.DB_S1);
            freeNationsData = gameHelper.getAllFreePlayedNations();

        } else if (type.equals("new")) {
            ScenarioContextHolder.setScenario(HibernateUtil.DB_S1);
            freeNationsData = gameHelper.getAllNewFreeNations();

        } else if (type.equals("free")) {
            ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);
            freeNationsData = new HashMap<String, Object>();

            // Check if user has an ongoing game (only 1 is allowed)
            final List<UserGame> lstGames = getUserGameManager().list(thisUser);
            if (!lstGames.isEmpty()) {
                // check if game has started
                final UserGame userGame = lstGames.get(0);
                LOGGER.info(userGame.getGame().getGameId() + " - " + userGame.getGame().getTurn());
                if (userGame.getGame().getTurn() > -1) {
                    // On-going game. Explain that only 1 can be supported.
                    actionType = "single";

                } else {
                    // Game already started - display same page but without re-creating the game
                    actionType = "start";
                }
            }

        } else if (type.equals("start")) {
            ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);
            freeNationsData = new HashMap<String, Object>();

            // Check if user has an ongoing game (only 1 is allowed)
            final List<UserGame> lstGames = getUserGameManager().list(thisUser);
            if (!lstGames.isEmpty()) {
                // check if game has started
                final UserGame userGame = lstGames.get(0);
                LOGGER.info(userGame.getGame().getGameId() + " - " + userGame.getGame().getTurn());
                if (userGame.getGame().getTurn() > -1) {
                    // On-going game. Explain that only 1 can be supported.
                    actionType = "single";

                } else {
                    // Game already started - display same page but without re-creating the game
                    actionType = "start";
                }

            } else {

                // Construct new game instance.
                final Game newGame = new Game();
                final List<Game> allFree = getGameManager().list();
                final Game lastFree = allFree.get(allFree.size() - 1);
                newGame.setGameId(lastFree.getGameId() + 1);
                newGame.setTurn(-2);
                newGame.setScenarioId(HibernateUtil.DB_FREE);
                newGame.setEnded(false);
                newGame.setWinners("");
                newGame.setSchedule(4);
                newGame.setDiscount(0);
                newGame.setStatus(GameConstants.GAME_SCHED);
                newGame.setForumId(2);
                newGame.setDescription(thisUser.getUsername());
                newGame.setDateStart(new Date());
                newGame.setDateLastProc(new Date());
                newGame.setCronSchedule("");
                newGame.setCronScheduleDescr("");
                newGame.setFogOfWar(true);
                newGame.setRandomEvents(false);
                newGame.setFieldBattle(false);

                // Identify date of next processing
                final Calendar nextTurn = Calendar.getInstance();
                nextTurn.set(Calendar.HOUR, 0);
                nextTurn.set(Calendar.MINUTE, 0);
                nextTurn.set(Calendar.SECOND, 0);
                nextTurn.add(Calendar.DATE, -newGame.getSchedule());
                newGame.setDateNextProc(nextTurn.getTime());

                getGameManager().add(newGame);

                // Add User-Game association
                final UserGame newUserGame = new UserGame();
                newUserGame.setGame(newGame);
                newUserGame.setUserId(thisUser.getUserId());
                newUserGame.setAccepted(true);
                newUserGame.setAccepted(true);
                newUserGame.setCost(0);
                newUserGame.setTurnDrop(0);
                newUserGame.setTurnPickUp(0);
                newUserGame.setCurrent(true);
                newUserGame.setHasWon(false);
                newUserGame.setActive(true);
                newUserGame.setAlive(true);
                newUserGame.setOffer(0);
                newUserGame.setNation(getNationManager().getByID(NationConstants.NATION_FRANCE));
                getUserGameManager().add(newUserGame);

                // Request Jenkins to execute job
                getArticleManager().getBuild(HibernateUtil.DB_FREE, 0);
            }

        } else if (type.equals("finish")) {
            ScenarioContextHolder.setScenario(HibernateUtil.DB_FREE);
            freeNationsData = new HashMap<String, Object>();

            // Check if user has an ongoing game (only 1 is allowed)
            final List<UserGame> lstGames = getUserGameManager().list(thisUser);
            LOGGER.info(lstGames.size());
            if (!lstGames.isEmpty()) {
                // check if game has started
                final UserGame userGame = lstGames.get(0);
                LOGGER.info(userGame.getGame().getGameId() + " - " + userGame.getGame().getTurn());
                if (userGame.getGame().getTurn() > -1) {
                    // Delete Game
                    getGameManager().deleteGame(userGame.getGame().getGameId());

                    // Create the directory of this game
                    final File gameDir = new File("/srv/eaw1805/images/maps/s" + HibernateUtil.DB_FREE + "/" + userGame.getGame().getGameId());
                    if (gameDir.exists()) {
                        final boolean result = deleteDir(gameDir);
                        if (result) {
                            LOGGER.info("Delete Game directory.");
                        } else {
                            LOGGER.error("Failed to delete Game directory. ");
                        }
                    }

                } else {
                    // Game finished
                    actionType = "abouttostart";
                }

            } else {
                // No On-going game found.
                actionType = "notfound";
            }

        } else {
            throw new InvalidPageException("Page not found");
        }

        // Fix Free Nations List
        final LinkedList<LinkedList<Object>> freeNations;
        final Map<Integer, LinkedList<LinkedList<Object>>> nations = new HashMap<Integer, LinkedList<LinkedList<Object>>>();
        if (freeNationsData.isEmpty()) {
            freeNations = new LinkedList<LinkedList<Object>>();

        } else {
            freeNations = (LinkedList<LinkedList<Object>>) freeNationsData.get("freeNations");
            for (final LinkedList<Object> freePlayedNation : freeNations) {

                final int nationID = ((Nation) freePlayedNation.get(1)).getId();
                if (nations.containsKey(nationID)) {
                    nations.get(nationID).addLast(freePlayedNation);

                } else {
                    final LinkedList<LinkedList<Object>> newList = new LinkedList<LinkedList<Object>>();
                    newList.add(freePlayedNation);
                    nations.put(nationID, newList);
                }
            }
        }

        model.put("nations", getNationManager().list());
        model.put("monthsSmall", freeNationsData.get("monthsSmall"));
        model.put("specialOffer", freeNationsData.get("specialOffer"));
        model.put("freeNations", nations);
        model.put("pageType", actionType);
        model.put("firstPage", false);

        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] JoinGame " + type + " / " + actionType);
        return new ModelAndView("joinGame-list", model);
    }

    /**
     * Deletes all files and sub directories under dir.
     * If a deletion fails, the method stops attempting to delete and returns false.
     *
     * @param dir the directory to delete.
     * @return true if all deletions were successful.
     */

    private boolean deleteDir(final File dir) {
        if (dir.isDirectory()) {
            for (final String aChildren : dir.list()) {
                if (!deleteDir(new File(dir, aChildren))) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

}
