package com.eaw1805.www.controllers;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.battles.NavalBattleReport;
import com.eaw1805.data.model.battles.TacticalBattleReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Methods used by game-related controllers.
 */
public class ExtendedController
        extends BaseController {

    /**
     * Instance GameManagerBean class to perform queries
     * about Game objects.
     */
    private transient GameManagerBean gameManager;

    /**
     * Setter method used by spring to inject a GameManagerBean bean.
     *
     * @param injGameManagerBean a GameManagerBean bean.
     */
    public void setGameManager(final GameManagerBean injGameManagerBean) {
        gameManager = injGameManagerBean;
    }

    /**
     * Access the GameManagerBean bean.
     *
     * @return the GameManagerBean bean.
     */
    public GameManagerBean getGameManager() {
        return gameManager;
    }

    /**
     * Instance NationManager class to perform queries
     * about nation objects.
     */
    private transient NationManagerBean nationManager;

    /**
     * Getter method used to access the nationManager bean injected by Spring.
     *
     * @return a nationManager bean.
     */
    public NationManagerBean getNationManager() {
        return nationManager;
    }

    /**
     * Setter method used by spring to inject a nationManager bean.
     *
     * @param injNationManager a nationManager bean.
     */
    public void setNationManager(final NationManagerBean injNationManager) {
        nationManager = injNationManager;
    }

    /**
     * Retrieves game entity. If game id does not exists in db return null.
     *
     * @param gameId a String with the Game ID.
     * @return the Game.
     */
    public Game getGame(final String gameId) {
        // Retrieve Game entity
        final Game thisGame;
        if ((gameId == null) || (gameId.isEmpty())) {
            return null;
        } else {
            try {
                final int thisGameId = Integer.parseInt(gameId);
                thisGame = getGameManager().getByID(thisGameId);
                return thisGame;
            } catch (final NumberFormatException e) {
                return null;
            }
        }
    }

    /**
     * Retrieves Nation entity. If nation id does not exists in db return null.
     *
     * @param nationId a String with the Nation ID.
     * @return the Nation.
     */
    public Nation getNation(final String nationId) {
        final Nation thisNation;
        if ((nationId == null) || (nationId.isEmpty())) {
            return null;
        } else {
            try {
                final int thisNationId = Integer.parseInt(nationId);
                thisNation = getNationManager().getByID(thisNationId);
                return thisNation;
            } catch (final NumberFormatException e) {
                return null;
            }
        }
    }

    /**
     * Return the turn.
     *
     * @param gameTurn the current turn of the game.
     * @param turnStr  the input String with the turn.
     * @return an int with the turn.
     */
    public int getTurn(final int gameTurn, final String turnStr) {
        // Retrieve Turn
        int turnInt = gameTurn;
        if (turnStr != null) {
            try {
                turnInt = Integer.parseInt(turnStr);
                if (turnInt > gameTurn) {
                    turnInt = gameTurn;
                }
            } catch (Exception ex) {
                // rejected
            }
        }
        return turnInt;
    }

    /**
     * Get the current calendar.
     *
     * @param thisReport the Battle Report.
     * @return the calendar.
     */
    public final Calendar calendar(final TacticalBattleReport thisReport) {
        return calendar(thisReport.getPosition().getGame(), thisReport.getTurn());
    }

    /**
     * Get the current calendar.
     *
     * @param thisReport the Naval Report.
     * @return the calendar.
     */
    public final Calendar calendar(final NavalBattleReport thisReport) {
        return calendar(thisReport.getPosition().getGame(), thisReport.getTurn());
    }

    protected List<Nation> getScenarioNationList(final String scenarioId) {
        // Retrieve list of nations
        final List<Nation> nationList;

        if ("1808".equals(scenarioId)) {
            nationList = new ArrayList<Nation>();

            nationList.add(getNationManager().getByID(NationConstants.NATION_SPAIN));
            nationList.add(getNationManager().getByID(NationConstants.NATION_FRANCE));
            nationList.add(getNationManager().getByID(NationConstants.NATION_GREATBRITAIN));

        } else {
            nationList = nationManager.list();
            nationList.remove(0); // remove "Free Nation" entry
        }

        return nationList;
    }

}
