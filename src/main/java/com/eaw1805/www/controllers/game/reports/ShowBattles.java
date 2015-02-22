package com.eaw1805.www.controllers.game.reports;

import com.eaw1805.battles.naval.result.RoundStat;
import com.eaw1805.battles.tactical.result.RoundStatistics;
import com.eaw1805.data.cache.GameCachable;
import com.eaw1805.data.managers.beans.NavalBattleReportManagerBean;
import com.eaw1805.data.managers.beans.TacticalBattleReportManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.battles.NavalBattleReport;
import com.eaw1805.data.model.battles.TacticalBattleReport;
import com.eaw1805.www.commands.BattleData;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Controller for user requests related to battle reports.
 */
@Controller
public class ShowBattles
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowReports.class);

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/battles")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  HttpServletRequest request)
            throws Exception {

        ScenarioContextHolder.defaultScenario();
        if (scenarioId == null || scenarioId.isEmpty()) {
            throw new InvalidPageException("Page not found");
        } else {
            try {
                ScenarioContextHolder.setScenario(scenarioId);

            } catch (Exception e) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Game entity
        final Game thisGame = getGame(gameId);
        if (thisGame == null) {
            throw new InvalidPageException("Page not found");
        }

        // Retrieve Nation entity
        final Nation thisNation = getNation(nationId);
        if (thisNation == null) {
            throw new InvalidPageException("Page not found");
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        if (thisUser.getUserType() != 3 && getUserGameManager().list(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // produce report + Prepare data to pass to jsp
        final Map<String, Object> refData = prepareBattleReports(thisGame, thisNation);

        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show battles for game=" + gameId + "/nation=" + thisNation.getName());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/battles", refData);
        } else {
            return new ModelAndView("game/battlesMinimal", refData);
        }

    }

    /**
     * Prepare report.
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @GameCachable()
    public Map<String, Object> prepareBattleReports(final Game thisGame,
                                                    final Nation thisNation) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // retrieve battles
        final List<TacticalBattleReport> tacticalBattleReportList = tacticalBattleReportManager.listGameNation(thisGame, thisNation);

        // process battles
        final List<BattleData> battleList = new ArrayList<BattleData>();

        for (final TacticalBattleReport report : tacticalBattleReportList) {
            final BattleData bdata = new BattleData();
            bdata.setBattleId(report.getBattleId());
            bdata.setPosition(report.getPosition().toString());

            final StringBuilder strBuilder = new StringBuilder();
            Calendar thatCal = calendar(report);
            strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBuilder.append(" ");
            strBuilder.append(thatCal.get(Calendar.YEAR));
            bdata.setTurn(strBuilder.toString());

            // Identify which side was the player
            int side = -1;
            final List<Nation> side1 = new ArrayList<Nation>();
            for (Nation nation : report.getSide1()) {
                if (nation.getId() == thisNation.getId()) {
                    side = 0;
                } else {
                    side1.add(nation);
                }
            }

            final List<Nation> side2 = new ArrayList<Nation>();
            for (Nation nation : report.getSide2()) {
                if (nation.getId() == thisNation.getId()) {
                    side = 1;
                } else {
                    side2.add(nation);
                }
            }

            if (side == 0) {
                // The player participated in 1st side
                bdata.setSideAllies(side1);
                bdata.setSideEnemies(side2);

                // Check result of battle
                switch (report.getWinner()) {
                    case RoundStatistics.SIDE_A:
                        bdata.setWinner("Winner");
                        break;

                    case RoundStatistics.SIDE_B:
                        bdata.setWinner("Defeated");
                        break;

                    default:
                    case RoundStatistics.SIDE_NONE:
                        bdata.setWinner("Undecided");
                        break;
                }

            } else {
                // The player participated in 2nd side
                bdata.setSideAllies(side2);
                bdata.setSideEnemies(side1);

                // Check result of battle
                switch (report.getWinner()) {
                    case RoundStatistics.SIDE_A:
                        bdata.setWinner("Defeated");
                        break;

                    case RoundStatistics.SIDE_B:
                        bdata.setWinner("Winner");
                        break;

                    default:
                    case RoundStatistics.SIDE_NONE:
                        bdata.setWinner("Undecided");
                        break;
                }
            }

            battleList.add(bdata);
        }

        // retrieve naval battles
        final List<NavalBattleReport> navalBattleReportList = navalBattleReportManager.listGameNation(thisGame, thisNation);

        // process naval battles
        final List<BattleData> navalList = new ArrayList<BattleData>();

        for (final NavalBattleReport report : navalBattleReportList) {
            final BattleData bdata = new BattleData();
            bdata.setBattleId(report.getBattleId());
            bdata.setPosition(report.getPosition().toString());

            final StringBuilder strBuilder = new StringBuilder();
            Calendar thatCal = calendar(report);
            strBuilder.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBuilder.append(" ");
            strBuilder.append(thatCal.get(Calendar.YEAR));
            bdata.setTurn(strBuilder.toString());

            // Identify which side was the player
            int side = -1;
            final List<Nation> side1 = new ArrayList<Nation>();
            for (Nation nation : report.getSide1()) {
                if (nation.getId() == thisNation.getId()) {
                    side = 0;
                } else {
                    side1.add(nation);
                }
            }

            final List<Nation> side2 = new ArrayList<Nation>();
            for (Nation nation : report.getSide2()) {
                if (nation.getId() == thisNation.getId()) {
                    side = 1;
                } else {
                    if (nation.getId() == -1) {
                        side2.add(constructPirateNation());
                    } else {
                        side2.add(nation);
                    }
                }
            }

            if (side == 0) {
                // The player participated in 1st side
                bdata.setSideAllies(side1);

                // Check if this was a pirate attack
                if (report.getPiracy() && report.getWinner() != RoundStat.SIDE_A) {
                    final List<Nation> sidePirates = new ArrayList<Nation>();
                    final Nation pirates = new Nation();
                    pirates.setName("Pirates");
                    pirates.setId(0);
                    sidePirates.add(pirates);
                    bdata.setSideEnemies(sidePirates);

                } else {
                    bdata.setSideEnemies(side2);
                }

                // Check result of battle
                switch (report.getWinner()) {
                    case RoundStat.SIDE_A:
                        bdata.setWinner("Winner");
                        break;

                    case RoundStat.SIDE_B:
                        bdata.setWinner("Defeated");
                        break;

                    default:
                    case RoundStat.SIDE_NONE:
                        bdata.setWinner("Undecided");
                        break;
                }

            } else {
                // The player participated in 2nd side
                bdata.setSideAllies(side2);
                bdata.setSideEnemies(side1);

                // Check result of battle
                switch (report.getWinner()) {
                    case RoundStat.SIDE_A:
                        bdata.setWinner("Defeated");
                        break;

                    case RoundStat.SIDE_B:
                        bdata.setWinner("Winner");
                        break;

                    default:
                    case RoundStat.SIDE_NONE:
                        bdata.setWinner("Undecided");
                        break;
                }
            }

            navalList.add(bdata);
        }

        // Prepare data to pass to jsp
        refData.put("game", thisGame);
        refData.put("turn", thisGame.getTurn());
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("scenarioId", thisGame.getScenarioIdToString());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("battleList", battleList);
        refData.put("navalList", navalList);
        refData.put("vp", 0);

        return refData;
    }

    private Nation constructPirateNation() {
        Nation nation = new Nation();
        nation.setName("Pirates");
        nation.setId(0);
        return nation;
    }

    /**
     * Instance NavalBattleReportManagerBean class to perform queries
     * about navalBattleReportManager objects.
     */
    private transient NavalBattleReportManagerBean navalBattleReportManager;

    /**
     * Setter method used by spring to inject a NavalBattleReportManagerBean bean.
     *
     * @param injNavalBattleReportManager a navalBattleReportManager bean.
     */
    public void setNavalBattleReportManager(final NavalBattleReportManagerBean injNavalBattleReportManager) {
        navalBattleReportManager = injNavalBattleReportManager;
    }

    /**
     * Instance TacticalBattleReportManager class to perform queries
     * about tacticalBattleReport objects.
     */
    private transient TacticalBattleReportManagerBean tacticalBattleReportManager;

    /**
     * Setter method used by spring to inject a TacticalBattleReportManager bean.
     *
     * @param injTacticalBattleReportManager a tacticalBattleReportManager bean.
     */
    public void setTacticalBattleReportManager(final TacticalBattleReportManagerBean injTacticalBattleReportManager) {
        tacticalBattleReportManager = injTacticalBattleReportManager;
    }

}
