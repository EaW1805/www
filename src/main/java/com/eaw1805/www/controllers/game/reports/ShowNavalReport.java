package com.eaw1805.www.controllers.game.reports;

import com.eaw1805.battles.naval.result.RoundStat;
import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.*;
import com.eaw1805.data.model.battles.NavalBattleReport;
import com.eaw1805.data.model.map.Barrack;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.controllers.naval.ShowInfo;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Controller for user requests related to specific naval battle reports.
 */
@SuppressWarnings("restriction")
@Controller
public class ShowNavalReport
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowNavalReport.class);

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/naval/{battleId}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  @PathVariable final String battleId,
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


        // Retrieve Battle entity
        final NavalBattleReport thisReport;
        if ((battleId == null) || (battleId.isEmpty())) {
            throw new InvalidPageException("Page not found");

        } else {
            final int battle = Integer.parseInt(battleId);
            thisReport = navalBattleReportManager.getByID(battle);
            if (thisReport == null) {
                throw new InvalidPageException("Page not found");
            }
        }

        // Check that user is allowed to view nation
        final User thisUser = getUser();
        final List<UserGame> userGames = getUserGameManager().list(thisUser, thisGame);
        boolean hasRights = false;
        if (!userGames.isEmpty()) {
            final UserGame userGame = userGames.get(0);//there will be only one active user game per player for a game.
            final List<NationsRelation> relations = relationManager.listAlliesByGameNation(thisGame, userGames.get(0).getNation());
            //check rights for side 1
            for (Nation nation : thisReport.getSide1()) {
                //if user participated in battle for side 1
                if (userGame.getNation().getId() == nation.getId()) {
                    hasRights = true;
                    break;
                }
                //if user is alliance with someone in side 1
                for (NationsRelation relation : relations) {
                    if (nation.getId() == relation.getTarget().getId()) {
                        hasRights = true;
                        break;
                    }
                }
            }
            //check rights for side 2
            for (Nation nation : thisReport.getSide2()) {
                //if user participated in battle for side 2.
                if (userGame.getNation().getId() == nation.getId()) {
                    hasRights = true;
                    break;
                }
                //if user is alliance with someone in side 2
                for (NationsRelation relation : relations) {
                    if (nation.getId() == relation.getTarget().getId()) {
                        hasRights = true;
                        break;
                    }
                }
            }

        }
        if (thisUser.getUserType() != 3 && !hasRights) {
            throw new InvalidPageException("Page not found");
        }


        // produce report and Prepare data to pass to jsp
        final Map<String, Object> refData = prepareReport(thisGame, thisNation, thisReport);


        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show battle report for game=" + gameId + "/nation=" + thisNation.getName() + "/report=" + thisReport.getBattleId());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("naval/result", refData);
        } else {
            return new ModelAndView("naval/resultMinimal", refData);
        }
    }

    /**
     * Prepare land forces report.
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @param thisReport the battle report to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "longGameCache")
    public Map<String, Object> prepareReport(final Game thisGame,
                                             final Nation thisNation,
                                             final NavalBattleReport thisReport) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // access Calendar object
        final Calendar thisCal = calendar(thisReport);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Retrieve Sector where battle took place
        final Sector thisSector = sectorManager.getByPosition(thisReport.getPosition());

        // Retrieve Nations
        final Nation nation1 = (Nation) thisReport.getSide1().toArray()[0];
        Nation nation2 = (Nation) thisReport.getSide2().toArray()[0];
        if (nation2.getId() == -1) {
            nation2 = constructPirateNation();
        }
        final List<Nation> allies1 = new ArrayList<Nation>();
        final List<Nation> allies2 = new ArrayList<Nation>();
        for (Nation nation : thisReport.getSide1()) {
            if (nation.getId() != nation1.getId()) {
                allies1.add(nation);
            }
        }
        for (Nation nation : thisReport.getSide2()) {
            if (nation.getId() != nation2.getId()) {
                if (nation.getId() == -1) {
                    allies2.add(constructPirateNation());
                } else {
                    allies2.add(nation);
                }
            }
        }

        // Retrieve weather type
        final String weather = ShowInfo.ALL_WEATHERS[thisReport.getWeather()];

        // Retrieve round statistics
        List<RoundStat> stats;
        //round : side : participating : unitType : counter
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, Object[]>>>> sideShipCounters = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, Object[]>>>>();
        try {
            ObjectInputStream objectIn = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(thisReport.getStats())));
            stats = (List<RoundStat>) objectIn.readObject();

            // check if side B are pirates!
            if (thisReport.getPiracy() && thisReport.getWinner() != RoundStat.SIDE_A) {
                nation2.setId(0);
                nation2.setName("Pirates");

                for (final RoundStat stat : stats) {
                    // change side B nation ID
                    for (final ShipDTO shipDTO : stat.getSideShips(1)) {
                        shipDTO.setNationId(0);
                        shipDTO.setName("Pirate Ship");
                    }
                }
            }

            for (RoundStat stat : stats) {
                sideShipCounters.put(stat.getRound(), new HashMap<Integer, Map<Integer, Map<Integer, Object[]>>>());
                for (int side = 0; side < 2; side++) {
                    sideShipCounters.get(stat.getRound()).put(side, new HashMap<Integer, Map<Integer, Object[]>>());
                    for (ShipDTO ship : stat.getSideShips(side)) {
                        Map<Integer, Object[]> unitsMap = sideShipCounters.get(stat.getRound()).get(side).get(ship.getNationId());

                        if (unitsMap == null) {
                            unitsMap = new HashMap<Integer, Object[]>();
                            sideShipCounters.get(stat.getRound()).get(side).put(ship.getNationId(), unitsMap);
                        }
                        final Object[] counters;
                        if (unitsMap.containsKey(ship.getType().getIntId())) {
                            counters = unitsMap.get(ship.getType().getIntId());
                            counters[0] = (Double) counters[0] + 1;
                            counters[1] = (Double) counters[1] + ship.getCondition();
                            counters[2] = (Double) counters[2] + ship.getMarines();
                            List<ShipDTO> shipsList = (ArrayList<ShipDTO>) counters[3];
                            shipsList.add(ship);
                            unitsMap.put(ship.getType().getIntId(), counters);
                        } else {
                            counters = new Object[4];
                            counters[0] = 1.0;
                            counters[1] = (double) ship.getCondition();
                            counters[2] = (double) ship.getMarines();
                            List<ShipDTO> shipsList = new ArrayList<ShipDTO>();
                            shipsList.add(ship);
                            counters[3] = shipsList;
                            unitsMap.put(ship.getType().getIntId(), counters);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            LOGGER.fatal(ex);
            return refData;
        }

        for (Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, Object[]>>>> entry : sideShipCounters.entrySet()) {
            final int round = entry.getKey();
            for (Map.Entry<Integer, Map<Integer, Map<Integer, Object[]>>> entry2 : entry.getValue().entrySet()) {
                final int side = entry2.getKey();
                for (Map.Entry<Integer, Map<Integer, Object[]>> entry3 : entry2.getValue().entrySet()) {
                    final int curNationId = entry3.getKey();
                    for (Map.Entry<Integer, Object[]> entry4 : entry3.getValue().entrySet()) {
                        final int typeId = entry4.getKey();
                        Object[] counters = entry4.getValue();
                        counters[1] = (Double) counters[1] / (Double) counters[0];
                        sideShipCounters.get(round).get(side).get(curNationId).put(typeId, counters);
                    }
                }
            }
        }

        // find the sectors name
        Barrack bar = barrackManager.getByPosition(thisSector.getPosition());
        final String name;
        if (thisSector.getTradeCity()) {//then it is trade city.
            name = tradeCityManager.getByPosition(thisSector.getPosition()).getName() + " (" + thisSector.getPosition().toString() + ")";
        } else if (bar != null && bar.getName() != null) {//then it is barrack
            name = bar.getName() + " (" + thisSector.getPosition().toString() + ")";
        } else if (thisSector.getName() != null && !thisSector.getName().isEmpty()) {
            name = thisSector.getName() + " (" + thisSector.getPosition().toString() + ")";
        } else {
            name = "sea sector" + " " + thisSector.getPosition().toString();
        }

        // Prepare data to pass to jsp
        refData.put("months", constructMonths(thisGame));
        refData.put("game", thisGame);
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("weather", weather);
        refData.put("report", thisReport);
        refData.put("turn", thisReport.getTurn());
        refData.put("winner", thisReport.getWinner());
        refData.put("region", thisSector.getPosition().getRegion());
        refData.put("sector", thisSector);
        refData.put("nation1", nation1);
        refData.put("nation2", nation2);
        refData.put("stats", stats);
        refData.put("sideShipCounters", sideShipCounters);
        refData.put("vp", 0);
        refData.put("allies1", allies1);
        refData.put("allies2", allies2);
        refData.put("sectorsName", name);

        final int day = (nation1.getId() + nation2.getId() + thisReport.getBattleId()) % 27 + 1;
        final StringBuilder battleDate = new StringBuilder();
        battleDate.append(day);
        battleDate.append(ordinalNo(thisReport.getBattleId() % 27 + 1));
        battleDate.append("&nbsp;of&nbsp;");
        battleDate.append(strBuf.toString());
        refData.put("battleDate", battleDate);

        return refData;
    }

    public final String ordinalNo(final int value) {
        final int hunRem = value % 100;
        final int tenRem = value % 10;
        if (hunRem - tenRem == 10) {
            return "th";
        }
        switch (tenRem) {
            case 1:
                return "st";

            case 2:
                return "nd";

            case 3:
                return "rd";

            default:
                return "th";
        }
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
     * Instance SectorManager class to perform queries
     * about commander objects.
     */
    private transient SectorManagerBean sectorManager;

    /**
     * Setter method used by spring to inject a SectorManager bean.
     *
     * @param injSectorManager a SectorManager bean.
     */
    public void setSectorManager(final SectorManagerBean injSectorManager) {
        sectorManager = injSectorManager;
    }

    /**
     * Instance of TradeCityManagerBean class to perform queries about trade city objects
     */
    private transient TradeCityManagerBean tradeCityManager;

    /**
     * Setter method used by spring to inject a TradeCityManager bean.
     *
     * @param tradeCityManager The injected bean.
     */
    public void setTradeCityManager(final TradeCityManagerBean tradeCityManager) {
        this.tradeCityManager = tradeCityManager;
    }

    /**
     * Instance of BarrackManager bean to perform queries about barracks.
     */
    private transient BarrackManagerBean barrackManager;

    /**
     * Setter used by spring to inject a BarrackManager bean.
     *
     * @param barrackManager The injected bean.
     */
    public void setBarrackManager(final BarrackManagerBean barrackManager) {
        this.barrackManager = barrackManager;
    }

    /**
     * Instance of RelationManager bean to perform queries about nation relations.
     */
    private transient RelationsManagerBean relationManager;

    /**
     * Setter used by spring to inject a RelationManager bean.
     *
     * @param relationManager
     */
    public void setRelationManager(RelationsManagerBean relationManager) {
        this.relationManager = relationManager;
    }

}
