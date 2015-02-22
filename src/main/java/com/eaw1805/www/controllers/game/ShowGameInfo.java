package com.eaw1805.www.controllers.game;

import com.eaw1805.battles.naval.result.RoundStat;
import com.eaw1805.battles.tactical.result.RoundStatistics;
import com.eaw1805.data.cache.GameCachable;
import com.eaw1805.data.constants.GameConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.EngineProcessManagerBean;
import com.eaw1805.data.managers.beans.GameManagerBean;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.NationManagerBean;
import com.eaw1805.data.managers.beans.NavalBattleReportManagerBean;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.managers.beans.TacticalBattleReportManagerBean;
import com.eaw1805.data.managers.beans.WatchGameManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.UserGame;
import com.eaw1805.data.model.battles.NavalBattleReport;
import com.eaw1805.data.model.battles.TacticalBattleReport;
import com.eaw1805.data.model.comparators.NationId;
import com.eaw1805.data.model.comparators.NationSort;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.www.commands.ChartData;
import com.eaw1805.www.controllers.BaseController;

import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Used to display basic game info.
 * <a href='<c:url value="/img/maps/s${game.scenarioId}/${game.gameId}/map-${game.gameId}-${game.turn}-1.png"/>'> </a>
 */
@Controller
public class ShowGameInfo
        extends BaseController
        implements ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowGameInfo.class);

    @RequestMapping(method = RequestMethod.GET, value = "/scenario/{scenarioId}/game/{gameId}/info")
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
        final Map<String, Object> refData = prepareReport(thisGame);
        refData.put("game", thisGame);

        // Prepare data to pass to jsp
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("watchedGames", getWatchedGames(thisUser));

        // Add extra info for game masters
        if (thisUser.getUserType() == 3) {
            // Retrieve summaries of player orders
            refData.put("nationOrders", playerOrderManager.countGameOrders(thisGame.getGameId()));

            // Create list of inactive positions
            final Map<Integer, List<UserGame>> positionData = new HashMap<Integer, List<UserGame>>();
            final Map<Integer, User> userData = new HashMap<Integer, User>();
            final List<Nation> nationList = getNationManager().list();
            nationList.remove(0); // remove "Free Nation" entry
            for (Nation nation : nationList) {
                final List<UserGame> lstPositions = getUserGameManager().listInActive(thisGame, nation);
                positionData.put(nation.getId(), lstPositions);

                // retrieve user data
                for (UserGame userGame : lstPositions) {
                    if (!userData.containsKey(userGame.getUserId())) {
                        userData.put(userGame.getUserId(), getUserManager().getByID(userGame.getUserId()));
                    }
                }
            }

            refData.put("positionData", positionData);
            refData.put("userData", userData);
        }

        boolean userHasActiveCountry = false;
        final List<UserGame> playedGames = getUserGameManager().listActive(thisUser);

        for (final UserGame playedGame : playedGames) {
            if (playedGame.getGame().equals(thisGame)) {
                userHasActiveCountry = true;
            }
        }
        refData.put("gameOwner", userManager.getByID(thisGame.getUserId()));
        refData.put("userHasActiveCountry", userHasActiveCountry);
        LOGGER.info("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show Game " + gameId);
        return new ModelAndView("game/info", refData);
    }

    /**
     * Prepare report.
     *
     * @param thisGame the game to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @GameCachable()
    public Map<String, Object> prepareReport(final Game thisGame) {
        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Retrieve list of nations
        final List<Nation> nationList = getNationManager().list();
        nationList.remove(0); // remove "Free Nation" entry

        // Fill in months with their human readable date
        final Calendar thatCal = calendar(thisGame);
        final int turnId = thisGame.getTurn();
        final Map<Integer, String> months = new HashMap<Integer, String>();
        final Map<Integer, String> monthsFull = new HashMap<Integer, String>();

        for (int turn = 0; turn <= turnId; turn++) {
            final StringBuilder strBuff = new StringBuilder();
            strBuff.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH).substring(0, 1));
            strBuff.append("/");
            strBuff.append(String.valueOf(thatCal.get(Calendar.YEAR)).substring(2));
            months.put(turn, strBuff.toString());

            final StringBuilder strBuffFull = new StringBuilder();
            strBuffFull.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBuffFull.append(" ");
            strBuffFull.append(thatCal.get(Calendar.YEAR));
            monthsFull.put(turn, strBuffFull.toString());

            thatCal.add(Calendar.MONTH, 1);
        }

        refData.put("monthsFull", monthsFull);

        // Retrieve players for this game
        final List<UserGame> userGames = getUserGameManager().list(thisGame);
        final List<Nation> aliveNations = new ArrayList<Nation>();
        final List<Nation> freeNations = new ArrayList<Nation>();
        final List<Nation> deadNations = new ArrayList<Nation>();
        final List<Nation> winnerNations = new ArrayList<Nation>();
        final List<Nation> coWinnerNations = new ArrayList<Nation>();
        final List<Nation> runnerUpNations = new ArrayList<Nation>();
        final Map<Integer, User> nationToPlayer = new HashMap<Integer, User>();
        final Map<Integer, Boolean> nationToStatus = new HashMap<Integer, Boolean>();
        final Map<Integer, Boolean> nationToAlive = new HashMap<Integer, Boolean>();
        final Map<Integer, UserGame> nationToUserGame = new HashMap<Integer, UserGame>();

        try {
            for (final UserGame userGame : userGames) {
                nationToUserGame.put(userGame.getNation().getId(), userGame);
                nationToPlayer.put(userGame.getNation().getId(), userManager.getByID(userGame.getUserId()));
                nationToStatus.put(userGame.getNation().getId(), userGame.isActive());
                nationToAlive.put(userGame.getNation().getId(), userGame.isAlive());

                // Check if nation is dead
                if (!userGame.isAlive()) {
                    deadNations.add(userGame.getNation());

                } else {
                    // Check if nation is free
                    if (!userGame.isActive()) {
                        freeNations.add(userGame.getNation());

                    } else if (thisGame.getWinners() != null && thisGame.getWinners().indexOf("*" + userGame.getNation().getId() + "*") > -1) {
                        winnerNations.add(userGame.getNation());

                    } else if (thisGame.getCoWinners() != null && thisGame.getCoWinners().indexOf("*" + userGame.getNation().getId() + "*") > -1) {
                        coWinnerNations.add(userGame.getNation());

                    } else if (thisGame.getRunnerUp() != null && thisGame.getRunnerUp().indexOf("*" + userGame.getNation().getId() + "*") > -1) {
                        runnerUpNations.add(userGame.getNation());

                    } else {
                        aliveNations.add(userGame.getNation());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Map<Integer, Integer> nationVP = reportNations(thisGame, N_VP);

        if (thisGame.getEnded()) {
            for (Nation nation : winnerNations) {
                nation.setSort(nationVP.get(nation.getId()));
            }
            Collections.sort(winnerNations, new NationSort());

            for (Nation nation : coWinnerNations) {
                nation.setSort(nationVP.get(nation.getId()));
            }
            Collections.sort(coWinnerNations, new NationSort());

            for (Nation nation : runnerUpNations) {
                nation.setSort(nationVP.get(nation.getId()));
            }
            Collections.sort(runnerUpNations, new NationSort());

            for (Nation nation : aliveNations) {
                nation.setSort(nationVP.get(nation.getId()));
            }
            Collections.sort(aliveNations, new NationSort());

            for (Nation nation : freeNations) {
                nation.setSort(nationVP.get(nation.getId()));
            }
            Collections.sort(freeNations, new NationSort());

            for (Nation nation : deadNations) {
                nation.setSort(nationVP.get(nation.getId()));
            }
            Collections.sort(deadNations, new NationSort());

        } else {
            Collections.sort(winnerNations, new NationId());
            Collections.sort(coWinnerNations, new NationId());
            Collections.sort(runnerUpNations, new NationId());
            Collections.sort(aliveNations, new NationId());
            Collections.sort(freeNations, new NationId());
            Collections.sort(deadNations, new NationId());
        }

        // To produce the ranking of production we need to do it programmatically
        final Map<Integer, Integer> mapProduction = sumProduction(thisGame);
        final Map<Integer, Integer> rankProduction = rankMap(mapProduction);

        final Map<Integer, Integer> mapAKills = grandSumNations(thisGame, A_TOT_KILLS);
        final Map<Integer, Integer> rankAKills = rankMap(mapAKills);

        final Map<Integer, Integer> mapADeaths = grandSumNations(thisGame, A_TOT_DEATHS);
        final Map<Integer, Integer> rankADeaths = rankMap(mapADeaths);

        final Map<Integer, Integer> mapSKills = grandSumNations(thisGame, "ships.sinks");
        final Map<Integer, Integer> rankSKills = rankMap(mapSKills);

        final Map<Integer, Integer> mapSDeaths = grandSumNations(thisGame, "ships.sinked");
        final Map<Integer, Integer> rankSDeaths = rankMap(mapSDeaths);

        final Map<Integer, Integer> mapBTotal = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> mapBWon = new HashMap<Integer, Integer>();
        calcTacticalBattles(thisGame, mapBTotal, mapBWon);
        final Map<Integer, Integer> rankBTotal = rankMap(mapBTotal);
        final Map<Integer, Integer> rankBWon = rankMap(mapBWon);

        final Map<Integer, Integer> mapNTotal = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> mapNWon = new HashMap<Integer, Integer>();
        calcNavalBattles(thisGame, mapNTotal, mapNWon);
        final Map<Integer, Integer> rankNTotal = rankMap(mapNTotal);
        final Map<Integer, Integer> rankNWon = rankMap(mapNWon);

        // return values

        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("winnerList", winnerNations);
        refData.put("coWinnerList", coWinnerNations);
        refData.put("runnerUpList", runnerUpNations);
        refData.put("nationList", aliveNations);
        refData.put("nationFree", freeNations);
        refData.put("nationDead", deadNations);
        refData.put("nationToPlayer", nationToPlayer);
        refData.put("nationToStatus", nationToStatus);
        refData.put("nationToAlive", nationToAlive);
        refData.put("nationVP", nationVP);
        refData.put("nationLand", sumNations(thisGame, E_SEC_SIZE_TOT));
        refData.put("nationEconomy", reportNations(thisGame, "taxation"));
        refData.put("nationProduction", mapProduction);
        refData.put("nationArmy", reportNations(thisGame, A_TOT_BAT));
        refData.put("nationNavy", reportNationsFixed(thisGame, S_TOT_SHIPS));
        refData.put("nationAKills", mapAKills);
        refData.put("nationADeaths", mapADeaths);
        refData.put("nationSKills", mapSKills);
        refData.put("nationSDeaths", mapSDeaths);
        refData.put("nationBTotal", mapBTotal);
        refData.put("nationBWon", mapBWon);
        refData.put("nationNTotal", mapNTotal);
        refData.put("nationNWon", mapNWon);
        refData.put("rankVP", rankNations(thisGame, N_VP));
        refData.put("rankEconomy", rankNations(thisGame, "taxation"));
        refData.put("rankProduction", rankProduction);
        refData.put("rankLand", rankNations(thisGame, E_SEC_SIZE_TOT));
        refData.put("rankArmy", rankNations(thisGame, A_TOT_BAT));
        refData.put("rankNavy", rankNationsFixed(thisGame, S_TOT_SHIPS));
        refData.put("rankAKills", rankAKills);
        refData.put("rankADeaths", rankADeaths);
        refData.put("rankSKills", rankSKills);
        refData.put("rankSDeaths", rankSDeaths);
        refData.put("rankBTotal", rankBTotal);
        refData.put("rankBWon", rankBWon);
        refData.put("rankNTotal", rankNTotal);
        refData.put("rankNWon", rankNWon);
        refData.put("statVP", produceRoundStats(thisGame, N_VP));
        refData.put("statDOMINATION", produceDominationStats(thisGame));

        if (thisGame.getEnded()) {
            for (int region = RegionConstants.REGION_FIRST; region <= RegionConstants.REGION_LAST; region++) {
                refData.put("statLAND" + region, produceRoundStats(thisGame, E_POP_SIZE + region));
            }
            refData.put("statARMY", produceRoundStats(thisGame, A_TOT_BAT));
            refData.put("statNAVY", produceRoundStats(thisGame, S_TOT_SHIPS));
            refData.put("statARMYKILLS", produceRunningSum(thisGame, A_TOT_KILLS));
            refData.put("statNAVYSINKS", produceRunningSum(thisGame, S_SINKS));
            refData.put("statMONEY", produceRoundStats(thisGame, W_REGION + "1" + W_GOOD + GoodConstants.GOOD_MONEY));
        }

        refData.put("totalOrders", (BigInteger) playerOrderManager.countOrdersPerGame(thisGame.getGameId()));
        refData.put("totalPopulation", sumGameStats(thisGame, "population.size"));
        refData.put("totalMoney", sumGameStats(thisGame, "warehouse.region.1.good.1"));
        refData.put("totalInPt", sumGameStats(thisGame, "warehouse.region.1.good.3"));
        refData.put("totalArmies", sumGameStats(thisGame, "armies.soldiers.total"));
        refData.put("totalCannons", sumGameStats(thisGame, "ships.cannons"));
        refData.put("totalTBattles", (BigInteger) tacticalBattleReportManager.countReportsPerGame(thisGame.getGameId()));
        refData.put("totalNBattles", (BigInteger) navalBattleReportManager.countReportsPerGame(thisGame.getGameId()));

        // Retrieve engine statistics for this game
        refData.put("stats", engineManager.listGame(thisGame.getGameId(), thisGame.getScenarioId()));

        // Also send public news
        refData.put("news", newsManager.listGame(thisGame, thisGame.getTurn() - 1));

        // Add extra info for game masters
        refData.put("nationListComplete", nationList);
        refData.put("months", months);
        refData.put("nationToUserGame", nationToUserGame);
        return refData;
    }

    /*public Map<Integer, Integer> getWatchedGames(final User thisUser) {
        final Map<Integer, Integer> watchGames = new HashMap<Integer, Integer>();

        if (thisUser == null || thisUser.getUserId() == -1) {
            return watchGames;
        }

        // Iterate through watched games
        final List<WatchGame> watchedGames = wGManager.listByUser(getUser());
        for (final WatchGame watchGame : watchedGames) {
            watchGames.put(watchGame.getGame().getGameId(), 1);
        }

        // Iterate through games played
        final List<UserGame> userGameslist = getUserGameManager().list(thisUser);
        for (final UserGame playedGame : userGameslist) {
            watchGames.put(playedGame.getGame().getGameId(), 2);
        }

        return watchGames;
    }*/

    private Map<Integer, Integer> rankMap(final Map<Integer, Integer> mapValues) {
        final Map<Integer, Integer> rankProduction = new HashMap<Integer, Integer>();
        final List<Nation> lstNation = getNationManager().list();
        lstNation.remove(0); // remove free nation
        for (final Nation nation : lstNation) {
            if (mapValues.containsKey(nation.getId())) {
                nation.setSort(mapValues.get(nation.getId()) * 100);

            } else {
                nation.setSort(nation.getId());
            }
        }
        Collections.sort(lstNation, new NationSort());
        int position = 1;
        for (final Nation nation : lstNation) {
            rankProduction.put(nation.getId(), position++);
        }
        return rankProduction;
    }

    public final Map<Integer, Integer> reportNations(final Game game, final String category) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();
        final List<Report> lstReports = reportManager.listByTurnKey(game, game.getTurn() - 1, category);
        for (final Report report : lstReports) {
            playersMap.put(report.getNation().getId(), Integer.parseInt(report.getValue()));
        }
        return playersMap;
    }

    public final Map<Integer, Integer> reportNationsFixed(final Game game, final String category) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();
        final List<Report> lstReports = reportManager.listByTurnKeyFixed(game, game.getTurn() - 1, category);
        for (final Report report : lstReports) {
            playersMap.put(report.getNation().getId(), Integer.parseInt(report.getValue()));
        }
        return playersMap;
    }

    public final Map<Integer, Integer> sumNations(final Game game, final String category) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();
        final List<Report> lstReports = reportManager.listByTurnKey(game, game.getTurn() - 1, category);
        for (final Report report : lstReports) {
            final int nationId = report.getNation().getId();
            if (playersMap.containsKey(nationId)) {
                playersMap.put(nationId, playersMap.get(nationId) + Integer.parseInt(report.getValue()));

            } else {
                playersMap.put(nationId, Integer.parseInt(report.getValue()));
            }
        }
        return playersMap;
    }

    public final Map<Integer, Integer> grandSumNations(final Game game, final String category) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();
        final List<Report> lstReports = reportManager.listByKey(game, category);
        for (final Report report : lstReports) {
            final int nationId = report.getNation().getId();
            if (playersMap.containsKey(nationId)) {
                playersMap.put(nationId, playersMap.get(nationId) + Integer.parseInt(report.getValue()));

            } else {
                playersMap.put(nationId, Integer.parseInt(report.getValue()));
            }
        }
        return playersMap;
    }

    public final Map<Integer, Integer> rankNations(final Game game, final String category) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();
        final List<Nation> lstNations = reportManager.rankNations(category, game, game.getTurn() - 1, 17);
        int position = 1;
        for (final Nation nation : lstNations) {
            playersMap.put(nation.getId(), position++);
        }
        return playersMap;
    }

    public final Map<Integer, Integer> rankNationsFixed(final Game game, final String category) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();
        final List<Nation> lstNations = reportManager.rankNationsFixed(category, game, game.getTurn() - 1, 17);
        int position = 1;
        for (final Nation nation : lstNations) {
            playersMap.put(nation.getId(), position++);
        }
        return playersMap;
    }

    protected final Map<Integer, Integer> sumProduction(final Game game) {
        final Map<Integer, Integer> playersMap = new HashMap<Integer, Integer>();

        // Retrieve goods
        final List<Good> lstGoods = goodManager.list();
        final Map<Integer, Integer> goodMap = new HashMap<Integer, Integer>();
        for (final Good good : lstGoods) {
            goodMap.put(good.getGoodId(), good.getGoodFactor());
        }

        final List<Report> lstReports = reportManager.listByTurnKey(game, game.getTurn() - 1, "goods.region.");
        for (final Report report : lstReports) {
            // identify nation
            final int nationId = report.getNation().getId();

            // identify good
            final int goodId = Integer.parseInt(report.getKey().substring(report.getKey().lastIndexOf(".") + 1));
            if (goodId != 1) {
                final int value = goodMap.get(goodId) * Integer.parseInt(report.getValue());

                if (playersMap.containsKey(nationId)) {
                    playersMap.put(nationId, playersMap.get(nationId) + value);

                } else {
                    playersMap.put(nationId, value);
                }
            }
        }

        return playersMap;
    }

    public final int sumGameStats(final Game game, final String category) {
        int totalStats = 0;
        final List<Report> lstReports = reportManager.listByTurnKey(game, game.getTurn() - 1, category);
        for (final Report report : lstReports) {
            totalStats += Integer.parseInt(report.getValue());
        }
        return totalStats;
    }

    private void calcTacticalBattles(final Game game,
                                     final Map<Integer, Integer> mapTotalBattles,
                                     final Map<Integer, Integer> mapWonBattles) {
        final List<TacticalBattleReport> lstReports = tacticalBattleReportManager.listGame(game);
        for (final TacticalBattleReport report : lstReports) {
            calcBattleSide(mapTotalBattles, mapWonBattles, report.getSide1(), report.getWinner() == RoundStatistics.SIDE_A);
            calcBattleSide(mapTotalBattles, mapWonBattles, report.getSide2(), report.getWinner() == RoundStatistics.SIDE_B);
        }
    }

    private void calcNavalBattles(final Game game,
                                  final Map<Integer, Integer> mapTotalBattles,
                                  final Map<Integer, Integer> mapWonBattles) {
        final List<NavalBattleReport> lstReports = navalBattleReportManager.listGame(game);
        for (final NavalBattleReport report : lstReports) {
            calcBattleSide(mapTotalBattles, mapWonBattles, report.getSide1(), report.getWinner() == RoundStat.SIDE_A);
            calcBattleSide(mapTotalBattles, mapWonBattles, report.getSide2(), report.getWinner() == RoundStat.SIDE_B);
        }
    }

    private void calcBattleSide(final Map<Integer, Integer> mapTotalBattles,
                                final Map<Integer, Integer> mapWonBattles,
                                final Set<Nation> side,
                                final boolean isWinner) {
        for (final Nation nation : side) {
            if (mapTotalBattles.containsKey(nation.getId())) {
                mapTotalBattles.put(nation.getId(), mapTotalBattles.get(nation.getId()) + 1);

            } else {
                mapTotalBattles.put(nation.getId(), 1);
            }

            if (isWinner) {
                if (mapWonBattles.containsKey(nation.getId())) {
                    mapWonBattles.put(nation.getId(), mapWonBattles.get(nation.getId()) + 1);

                } else {
                    mapWonBattles.put(nation.getId(), 1);
                }
            }
        }
    }

    private List<List<ChartData>> produceRoundStats(final Game thisGame, final String key) {
        final List<List<ChartData>> vpStats = new ArrayList<List<ChartData>>();
        final List<Nation> lstNations = getNationManager().list();
        lstNations.remove(0); // remove free nations
        for (final Nation nation : lstNations) {
            final List<Report> reportVP = reportManager.listByOwnerKey(nation, thisGame, key);

            final List<ChartData> thisList = new ArrayList<ChartData>();

            final Calendar thisCal = Calendar.getInstance();
            thisCal.set(1805, Calendar.JANUARY, 1);
            for (final Report thisReport : reportVP) {
                final ChartData cdata = new ChartData();
                cdata.setCaption(nation.getName());
                cdata.setYear(thisCal.get(Calendar.YEAR));
                cdata.setMonth(thisCal.get(Calendar.MONTH));
                cdata.setValue(Integer.parseInt(thisReport.getValue()));

                thisList.add(cdata);
                thisCal.add(Calendar.MONTH, 1);
            }

            vpStats.add(thisList);
        }

        return vpStats;
    }

    private List<List<ChartData>> produceDominationStats(final Game thisGame) {
        // check duration of game
        final double modifier;
        switch (thisGame.getType()) {
            case GameConstants.DURATION_SHORT:
                modifier = .7d;
                break;

            case GameConstants.DURATION_LONG:
                modifier = 1.3d;
                break;

            case GameConstants.DURATION_NORMAL:
            default:
                modifier = 1d;
        }

        final List<List<ChartData>> vpStats = new ArrayList<List<ChartData>>();
        final List<Nation> lstNations = getNationManager().list();
        lstNations.remove(0); // remove free nations
        for (final Nation nation : lstNations) {
            final List<Report> reportVP = reportManager.listByOwnerKey(nation, thisGame, N_VP);

            final List<ChartData> thisList = new ArrayList<ChartData>();

            final Calendar thisCal = Calendar.getInstance();
            thisCal.set(1805, Calendar.JANUARY, 1);
            for (final Report thisReport : reportVP) {
                final ChartData cdata = new ChartData();
                cdata.setCaption(nation.getName());
                cdata.setYear(thisCal.get(Calendar.YEAR));
                cdata.setMonth(thisCal.get(Calendar.MONTH));
                cdata.setValue((int) (100d * Double.parseDouble(thisReport.getValue()) / (nation.getVpWin() * modifier)));

                thisList.add(cdata);
                thisCal.add(Calendar.MONTH, 1);
            }

            vpStats.add(thisList);
        }

        return vpStats;
    }

    private List<List<ChartData>> produceRunningSum(final Game thisGame, final String key) {
        final List<List<ChartData>> vpStats = new ArrayList<List<ChartData>>();
        final List<Nation> lstNations = getNationManager().list();
        lstNations.remove(0); // remove free nations
        for (final Nation nation : lstNations) {
            final List<Report> reportVP = reportManager.listByOwnerKey(nation, thisGame, key);

            final List<ChartData> thisList = new ArrayList<ChartData>();

            final Map<Integer, Integer> turnSums = new HashMap<Integer, Integer>();
            for (final Report thisReport : reportVP) {
                int totValue = Integer.parseInt(thisReport.getValue());
                if (turnSums.containsKey(thisReport.getTurn())) {
                    totValue += turnSums.get(thisReport.getTurn());
                }

                turnSums.put(thisReport.getTurn(), totValue);
            }

            int runningTotal = 0;
            for (int turn = 1; turn <= thisGame.getTurn(); turn++) {
                if (turnSums.containsKey(turn)) {
                    runningTotal += turnSums.get(turn);
                }

                final Calendar thisCal = Calendar.getInstance();
                thisCal.set(1805, Calendar.JANUARY, 1);
                thisCal.add(Calendar.MONTH, turn);

                final ChartData cdata = new ChartData();
                cdata.setCaption(nation.getName());
                cdata.setYear(thisCal.get(Calendar.YEAR));
                cdata.setMonth(thisCal.get(Calendar.MONTH));
                cdata.setValue(runningTotal);
                thisList.add(cdata);
            }

            vpStats.add(thisList);
        }

        return vpStats;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/scenario/{scenarioId}/game/{gameId}/update/description")
    protected ModelAndView updateDescription(@PathVariable("scenarioId") String scenarioId,
                                             final @PathVariable("gameId") String gameId,
                                             final HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.setScenario(scenarioId);
        final Game game = gameManagerBean.getByID(Integer.parseInt(gameId));
        final User thisUser = getUser();
        if (thisUser.getUserId() != game.getUserId() && thisUser.getUserType() != 3) {
            throw new InvalidPageException("Page not found");
        }
        game.setName(Jsoup.clean(request.getParameter("name"), Whitelist.relaxed()));
        gameManagerBean.update(game);
        return new ModelAndView(new RedirectView(request.getContextPath() + "/scenario/" + scenarioId + "/game/" + gameId + "/info"));
    }


    /**
     * Instance WatchGameManagerBean class to perform queries
     * about WatchGame objects.
     */
    private transient WatchGameManagerBean wGManager;

    /**
     * Setter method used by spring to inject a WatchGameManagerBean bean.
     *
     * @param value a WatchGameManagerBean bean.
     */
    public void setwGManager(final WatchGameManagerBean value) {
        this.wGManager = value;
    }

    /**
     * Instance EngineProcessManager class to perform queries
     * about engineProcess objects.
     */
    private transient EngineProcessManagerBean engineManager;

    /**
     * Setter method used by spring to inject a EngineProcessManagerBean bean.
     *
     * @param injEngineManager a EngineProcessManagerBean bean.
     */
    public void setEngineProcessManager(final EngineProcessManagerBean injEngineManager) {
        engineManager = injEngineManager;
    }

    /**
     * Instance ReportManager class to perform queries
     * about report objects.
     */
    private transient ReportManagerBean reportManager;

    /**
     * Setter method used by spring to inject a reportManager bean.
     *
     * @param injReportManager a reportManager bean.
     */
    public void setReportManager(final ReportManagerBean injReportManager) {
        reportManager = injReportManager;
    }

    /**
     * Instance GoodManager class to perform queries
     * about Good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a GoodManager bean.
     *
     * @param injGoodManager a GoodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
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

    /**
     * Instance PlayerOrderManager class to perform queries
     * about playerOrder objects.
     */
    private transient PlayerOrderManagerBean playerOrderManager;

    /**
     * Setter method used by spring to inject a PlayerOrderManager bean.
     *
     * @param injPlayerOrderManager a playerOrderManager bean.
     */
    public void setPlayerOrderManager(final PlayerOrderManagerBean injPlayerOrderManager) {
        playerOrderManager = injPlayerOrderManager;
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

}
