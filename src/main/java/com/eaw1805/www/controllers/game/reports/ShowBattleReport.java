package com.eaw1805.www.controllers.game.reports;

import com.eaw1805.battles.tactical.AbstractTacticalBattleRound;
import com.eaw1805.battles.tactical.result.RoundStatistics;
import com.eaw1805.data.cache.Cachable;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.*;
import com.eaw1805.data.model.battles.TacticalBattleReport;
import com.eaw1805.data.model.map.Barrack;
import com.eaw1805.data.model.map.Sector;
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
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Controller for user requests related to specific battle reports.
 */
@SuppressWarnings("restriction")
@Controller
public class ShowBattleReport
        extends ExtendedController {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowBattleReport.class);


    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/battle/{battleId}")
    protected ModelAndView handle(@PathVariable final String scenarioId,
                                  @PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  @PathVariable final String battleId,
                                  HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.defaultScenario();

        if (scenarioId == null || scenarioId.isEmpty()) {
            System.out.println("1");
            throw new InvalidPageException("Page not found");
        } else {
            try {
                ScenarioContextHolder.setScenario(scenarioId);

            } catch (Exception e) {
                System.out.println("2");
                throw new InvalidPageException("Page not found");
            }
        }

        // Retrieve Game entity
        final Game thisGame = getGame(gameId);
        if (thisGame == null) {
            System.out.println("3");
            throw new InvalidPageException("Page not found");
        }

        // Retrieve Nation entity
        final Nation thisNation = getNation(nationId);
        if (thisNation == null) {
            System.out.println("4");
            throw new InvalidPageException("Page not found");
        }


        // Retrieve Battle entity
        final TacticalBattleReport thisReport;
        if ((battleId == null) || (battleId.isEmpty())) {
            System.out.println("5");
            throw new InvalidPageException("Page not found");

        } else {

            final int battle = Integer.parseInt(battleId);
            thisReport = tacticalBattleReportManager.getByID(battle);
            if (thisReport == null) {
                System.out.println("6");
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
            System.out.println("7");
            throw new InvalidPageException("Page not found");
        }

         try {
        // produce report and Prepare data to pass to jsp
        final Map<String, Object> refData = prepareReport(thisGame, thisNation, thisReport);

        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show battle report for game=" + gameId + "/nation=" + thisNation.getName() + "/report=" + thisReport.getBattleId());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/battlereport", refData);

        } else {
            return new ModelAndView("game/battlereportMinimal", refData);
        }
         } catch (Exception e) {
             e.printStackTrace();
             return null;
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
                                             final TacticalBattleReport thisReport) {
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

        // Identify fortification
        final String fort = thisReport.getFort();

        // Retrieve Nations
        final Nation nation1 = (Nation) thisReport.getSide1().toArray()[0];
        final Nation nation2 = (Nation) thisReport.getSide2().toArray()[0];
        final List<Nation> allies1 = new ArrayList<Nation>();
        final List<Nation> allies2 = new ArrayList<Nation>();
        boolean[] hasSideAllies = new boolean[2];
        hasSideAllies[0] = false;
        hasSideAllies[1] = false;
        for (Nation nation : thisReport.getSide1()) {
            if (nation.getId() != nation1.getId()) {
                allies1.add(nation);
                hasSideAllies[0] = true;
            }
        }

        for (Nation nation : thisReport.getSide2()) {
            if (nation.getId() != nation2.getId()) {
                allies2.add(nation);
                hasSideAllies[1] = true;
            }
        }

        // Check unknown commanders
        if (thisReport.getComm1()!=null && thisReport.getComm1().getId() == 1) {
            thisReport.getComm1().setIntId(0);
            thisReport.getComm1().setNation(nation1);
        }

        if (thisReport.getComm2() != null && thisReport.getComm2().getId() == 1) {
            thisReport.getComm2().setIntId(0);
            thisReport.getComm2().setNation(nation2);
        }


        //round : side : participating : unitType : counter
        final Map<Integer, Map<Integer, Map<Boolean, Map<Integer, Map<Integer, Object[]>>>>> sideBattalionsCounters = new HashMap<Integer, Map<Integer, Map<Boolean, Map<Integer, Map<Integer, Object[]>>>>>();

        // Retrieve round statistics
        List<RoundStatistics> stats;

        try {
            final ObjectInputStream objectIn = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(thisReport.getStats())));
            stats = (List<RoundStatistics>) objectIn.readObject();

            for (RoundStatistics stat : stats) {
                sideBattalionsCounters.put(stat.getRound(), new HashMap<Integer, Map<Boolean, Map<Integer, Map<Integer, Object[]>>>>());
                for (int side = 0; side < 2; side++) {
                    sideBattalionsCounters.get(stat.getRound()).put(side, new HashMap<Boolean, Map<Integer, Map<Integer, Object[]>>>());
                    for (BattalionDTO battalion : stat.getSideBattalions()[side]) {

                        if (stat.getRound() == AbstractTacticalBattleRound.ROUND_MORALE_1
                                || stat.getRound() == AbstractTacticalBattleRound.ROUND_MORALE_2
                                || stat.getRound() == AbstractTacticalBattleRound.ROUND_MORALE_3
                                || stat.getRound() == AbstractTacticalBattleRound.ROUND_MORALE_4) {

                            if (!battalion.isFleeing()) {
                                continue;

                            } else {
                                // make sure all fleeing battalions are reported
                                battalion.setParticipated(true);
                            }
                        }

                        Map<Integer, Map<Integer, Object[]>> nationsMap = sideBattalionsCounters.get(stat.getRound()).get(side).get(battalion.isParticipated());
                        if (nationsMap == null) {
                            nationsMap = new HashMap<Integer, Map<Integer, Object[]>>();
                            sideBattalionsCounters.get(stat.getRound()).get(side).put(battalion.isParticipated(), nationsMap);
                        }

                        Map<Integer, Object[]> unitsMap = nationsMap.get(battalion.getEmpireArmyType().getNationId());
                        if (unitsMap == null) {
                            unitsMap = new HashMap<Integer, Object[]>();
                            nationsMap.put(battalion.getEmpireArmyType().getNationId(), unitsMap);
                        }

                        final Object[] counters;
                        if (unitsMap.containsKey(battalion.getEmpireArmyType().getIntId())) {
                            counters = unitsMap.get(battalion.getEmpireArmyType().getIntId());
                            counters[0] = (Double) counters[0] + 1;
                            counters[1] = (Double) counters[1] + battalion.getExperience();
                            counters[2] = (Double) counters[2] + battalion.getHeadcount();

                            final List<BattalionDTO> batsList = (ArrayList<BattalionDTO>) counters[3];
                            batsList.add(battalion);
                            unitsMap.put(battalion.getEmpireArmyType().getIntId(), counters);

                        } else {
                            counters = new Object[4];
                            counters[0] = 1.0;
                            counters[1] = (double) battalion.getExperience();
                            counters[2] = (double) battalion.getHeadcount();

                            final List<BattalionDTO> batsList = new ArrayList<BattalionDTO>();
                            batsList.add(battalion);
                            counters[3] = batsList;
                            unitsMap.put(battalion.getEmpireArmyType().getIntId(), counters);
                        }
                    }
                }

            }

            for (Map.Entry<Integer, Map<Integer, Map<Boolean, Map<Integer, Map<Integer, Object[]>>>>> entry : sideBattalionsCounters.entrySet()) {
                final int round = entry.getKey();
                for (Map.Entry<Integer, Map<Boolean, Map<Integer, Map<Integer, Object[]>>>> entry2 : entry.getValue().entrySet()) {
                    final int side = entry2.getKey();
                    for (Map.Entry<Boolean, Map<Integer, Map<Integer, Object[]>>> entry3 : entry2.getValue().entrySet()) {
                        final boolean participated = entry3.getKey();
                        for (Map.Entry<Integer, Map<Integer, Object[]>> entry4 : entry3.getValue().entrySet()) {
                            final int curNationId = entry4.getKey();
                            for (Map.Entry<Integer, Object[]> entry5 : entry4.getValue().entrySet()) {
                                final int typeId = entry5.getKey();
                                Object[] counters = entry5.getValue();
                                counters[1] = (Double) counters[1] / (Double) counters[0];
                                sideBattalionsCounters.get(round).get(side).get(participated).get(curNationId).put(typeId, counters);
                            }
                        }
                    }
                }
            }


        } catch (Exception ex) {
            LOGGER.fatal(ex);
            return refData;
        }

        // find the sectors name.
        final Barrack bar = barrackManager.getByPosition(thisSector.getPosition());
        final String justName;
        if (thisSector.getTradeCity()) {
            // it is trade city.
            justName = tradeCityManager.getByPosition(thisSector.getPosition()).getName();

        } else if (bar != null && bar.getName() != null) {
            //then it is barrack
            justName = bar.getName();

        } else if (thisSector.getName() != null && !thisSector.getName().isEmpty()) {
            justName = thisSector.getName();

        } else {
            justName = "";
        }

        final StringBuilder sectorName = new StringBuilder();
        if (justName.length() > 0) {
            sectorName.append(justName.replaceAll(" ", "&nbsp;"));
        } else {
            sectorName.append("sector&nbsp;");
        }

        sectorName.append("&nbsp;(");
        sectorName.append(thisSector.getPosition().toString());
        sectorName.append(")");

        // decide date
        final int day = (nation1.getId() + nation2.getId() + thisReport.getBattleId()) % 27 + 1;
        final StringBuilder battleDate = new StringBuilder();
        battleDate.append(day);
        battleDate.append(ordinalNo(thisReport.getBattleId() % 27 + 1));
        battleDate.append("&nbsp;of&nbsp;");
        battleDate.append(strBuf.toString());

        // Prepare data to pass to jsp
        refData.put("months", constructMonths(thisGame));
        refData.put("game", thisGame);
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("report", thisReport);
        refData.put("turn", thisReport.getTurn());
        refData.put("fortress", fort);
        refData.put("winner", thisReport.getWinner());
        refData.put("terrainType", thisSector.getTerrain());
        refData.put("region", thisSector.getPosition().getRegion());
        refData.put("sector", thisSector);
        refData.put("nation1", nation1);
        refData.put("nation2", nation2);
        refData.put("comm1", thisReport.getComm1());
        refData.put("comm2", thisReport.getComm2());
        refData.put("stats", stats);
        refData.put("counterStats", sideBattalionsCounters);
        refData.put("vp", 0);
        refData.put("allies1", allies1);
        refData.put("allies2", allies2);
        refData.put("sectorsName", sectorName.toString());
        refData.put("justName", justName.replaceAll(" ", "&nbsp;"));
        refData.put("battleDate", battleDate.toString());
        refData.put("hasSideAllies", hasSideAllies);

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
