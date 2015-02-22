package com.eaw1805.www.controllers.game.reports;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.RegionManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.www.controllers.ExtendedController;
import com.eaw1805.data.cache.Cachable;
import com.eaw1805.www.controllers.exception.InvalidPageException;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller for user requests related to turn reports.
 */
@SuppressWarnings("restriction")
@Controller
public class ShowReports
        extends ExtendedController
        implements ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowReports.class);

    public static final Region TREASURY = new Region();

    static {
        TREASURY.setId(0);
        TREASURY.setCode('T');
        TREASURY.setName("Treasury");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}/nation/{nationId}/reports/month/{turnStr}")
    protected ModelAndView handle(@PathVariable final String gameId,
                                  @PathVariable final String nationId,
                                  @PathVariable final String turnStr,
                                  HttpServletRequest request)
            throws Exception {
        ScenarioContextHolder.defaultScenario();
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
        if (thisUser.getUserType() != 3 && getUserGameManager().list(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // Retrieve Turn
        int turnInt = thisGame.getTurn();
        if (turnStr != null) {
            try {
                turnInt = Integer.parseInt(turnStr);
                if (turnInt > thisGame.getTurn()) {
                    turnInt = thisGame.getTurn();
                }
            } catch (Exception ex) {
                // rejected
            }
        }

        final Map<String, Object> refData = prepareReport(thisGame, thisNation, turnInt);
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));
        refData.put("months", constructMonths(thisGame));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show reports for game=" + gameId + "/turn=" + turnInt + "/nation=" + thisNation.getName());
        return new ModelAndView("game/reports", refData);
    }

    /**
     * Retrieve all data needed to produce the report.
     *
     * @param thisGame   the game.
     * @param thisNation the nation.
     * @param turnInt    the turn.
     * @return the map to send to the jsp.
     */
    @Cachable(cacheName = "longGameCache")
    protected Map<String, Object> prepareReport(final Game thisGame, final Nation thisNation, final int turnInt) {
        // Retrieve support tables
        final List<Region> regionList = regionManager.list();

        final List<Good> goodList = goodManager.list();
        goodList.remove(GoodConstants.GOOD_CP - 1); // Remove Command Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_AP - 1); // Remove Administrative Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_MONEY - 1); // Remove Money -- they are listed in a separate table

        // Retrieve Orders
        final List<PlayerOrder> orders = ordersManager.listByGameNation(thisGame, thisNation, turnInt - 1);
        final List<PlayerOrder> ordersFinal = new ArrayList<PlayerOrder>();

        int moneyConsumption = 0;

        // Compute order's consumption per region
        final Map<Region, Map<Integer, Integer>> goodConsumption = new HashMap<Region, Map<Integer, Integer>>();
        for (final Region region : regionList) {
            // Initialize list of total goods consumed
            final Map<Integer, Integer> usedGoods = new HashMap<Integer, Integer>();
            for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                usedGoods.put(good, 0);
            }

            // Calculate total goods consumed by each order
            for (final PlayerOrder order : orders) {
                final Map<Integer, Integer> thisGoods = order.getUsedGoodsQnt();
                if (order.getRegion() != null && order.getRegion().getId() == region.getId()) {
                    if (thisGoods != null) {
                        int totQte = 0;

                        // retrieve money
                        if (thisGoods.get(GoodConstants.GOOD_MONEY) != null) {
                            moneyConsumption += thisGoods.get(GoodConstants.GOOD_MONEY);
                            totQte++;
                        }

                        for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                            if (thisGoods.get(good) != null) {
                                totQte++;
                                usedGoods.put(good, usedGoods.get(good) + thisGoods.get(good));
                            }
                        }

                        if (totQte != 0) {
                            ordersFinal.add(order);
                        }
                    }
                }
            }

            goodConsumption.put(region, usedGoods);
        }

        // Retrieve available good at start and end of turn
        final Map<Region, List<Integer>> initialGoods = listInitial(thisGame, thisNation, turnInt);
        final Map<Region, List<Integer>> finalGoods = listFinal(thisGame, thisNation, turnInt);
        final Map<Region, List<Integer>> producedGoods = listProduction(thisGame, thisNation, turnInt);

        // Check if there is activity in each region
        final Map<Region, Boolean> activity = new HashMap<Region, Boolean>();
        for (final Region region : regionList) {
            for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                if ((initialGoods.containsKey(region))
                        && (initialGoods.get(region).size() > good - GoodConstants.GOOD_PEOPLE)
                        && ((initialGoods.get(region).get(good - GoodConstants.GOOD_PEOPLE) != 0)
                        || (finalGoods.get(region).get(good - GoodConstants.GOOD_PEOPLE) != 0)
                        || (producedGoods.get(region).get(good - GoodConstants.GOOD_PEOPLE) != 0))) {
                    activity.put(region, true);
                    break;
                }
            }
        }

        // Add additional support fields
        regionList.add(0, TREASURY);
        activity.put(TREASURY, true);

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        final Calendar thatCal = calendar(thisGame, turnInt);
        final StringBuilder strBuff = new StringBuilder();
        strBuff.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuff.append(" ");
        strBuff.append(thatCal.get(Calendar.YEAR));

        // Fill in previous 5 months
        List<String> months = new ArrayList<String>();
        for (int turn = Math.max(turnInt - 1, 0); turn > Math.max(turnInt - 6, 0); turn--) {
            thatCal.add(Calendar.MONTH, -1);
            final StringBuilder strBufff = new StringBuilder();
            strBufff.append(thatCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            strBufff.append(" ");
            strBufff.append(thatCal.get(Calendar.YEAR));
            months.add(strBufff.toString());
        }

        // Fill missing entries
        for (int turn = months.size(); turn < 5; turn++) {
            months.add("&nbsp;");
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("game", thisGame);
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("orders", ordersFinal);
        refData.put("orderNames", ShowOrders.ORDER_NAMES);
        refData.put("initial", initialGoods);
        refData.put("available", finalGoods);
        refData.put("production", producedGoods);
        refData.put("transfers", listTransfers(thisGame, thisNation, turnInt));
        refData.put("goodList", goodList);
        refData.put("moneyGood", new Integer(1));
        refData.put("regionList", regionList);
        refData.put("activity", activity);
        refData.put("months", months);
        refData.put("turn", turnInt);
        refData.put("moneyConsumption", moneyConsumption);
        refData.put("goodConsumption", goodConsumption);
        refData.put("individualReports", individualReports(thisGame, thisNation, turnInt));
        refData.put("productionDetails", reportManager.listByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "production.%"));
        refData.put("taxation", reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "taxation"));
        refData.put("vp", 0);

        return refData;
    }

    protected Map<Region, List<Report>> individualReports(final Game thisGame, final Nation thisNation, final int turnInt) {
        final Map<Region, List<Report>> report = new HashMap<Region, List<Report>>();
        final List<Region> regionList = regionManager.list();
        final Region europe = regionManager.getByID(RegionConstants.EUROPE);

        // Initialize map
        report.put(TREASURY, new ArrayList<Report>());
        for (final Region region : regionList) {
            final List<Report> thisList = new ArrayList<Report>();
            report.put(region, thisList);
        }

        final Report moneyCommanders = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "commanders.totalMoney");
        if (moneyCommanders != null) {
            moneyCommanders.setKey("Commanders salaries");
            report.get(TREASURY).add(moneyCommanders);
        }

        final Report moneyArmy = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "armies.totalMoney");
        if (moneyArmy != null) {
            moneyArmy.setKey("Soldiers salaries");
            report.get(TREASURY).add(moneyArmy);
        }

        final Report moneyFleet = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "ships.totalMoney");
        if (moneyFleet != null) {
            moneyFleet.setKey("Marines salaries");
            report.get(TREASURY).add(moneyFleet);
        }

        final Report moneyBTrain = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "baggagetrain.totalMoney");
        if (moneyBTrain != null) {
            moneyBTrain.setKey("Baggage Trains costs");
            report.get(TREASURY).add(moneyBTrain);
        }

        for (final Region region : regionList) {
            final Report foodArmy = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, A_TOT_FOOD + region.getId());
            if ((foodArmy != null) && (Integer.parseInt(foodArmy.getValue()) > 0)) {
                foodArmy.setKey("Soldiers food portions");
                report.get(region).add(foodArmy);
            }

            final Report wineFleet = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, S_TOT_WINE + region.getId());
            if ((wineFleet != null) && (Integer.parseInt(wineFleet.getValue()) > 0)) {
                wineFleet.setKey("Marines wine allowance");
                report.get(region).add(wineFleet);
            }

            final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, E_POP_FOOD + region.getId());
            if ((thisReport != null) && (Integer.parseInt(thisReport.getValue()) > 0)) {
                thisReport.setKey("Population food portions");
                report.get(region).add(thisReport);
            }

            final Report peopleReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, E_POP_INC + region.getId());
            if ((peopleReport != null) && (Integer.parseInt(peopleReport.getValue()) > 0)) {
                peopleReport.setKey("Population increase");
                report.get(region).add(peopleReport);
            }
        }

        final Report foodPow = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, P_TOT + P_FOOD);
        if ((foodPow != null) && (Integer.parseInt(foodPow.getValue()) > 0)) {
            foodPow.setKey("Prisoners food portions");
            report.get(europe).add(foodPow);
        }

        final Report moneyProduction = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt - 1, "production.maintenance");
        if (moneyProduction != null) {
            moneyProduction.setKey("Maintenance&nbsp;of&nbsp;Production&nbsp;sites");
            report.get(TREASURY).add(moneyProduction);
        }

        return report;
    }

    protected Map<Region, List<Integer>> listInitial(final Game thisGame, final Nation thisNation, final int thisTurn) {
        final Map<Region, List<Integer>> production = new HashMap<Region, List<Integer>>();
        final List<Region> regionList = regionManager.list();

        final List<Integer> treasuryList = new ArrayList<Integer>(1);
        final Report mainReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 2, "warehouse.region.1.good.1");
        if (mainReport != null) {
            treasuryList.add(Integer.parseInt(mainReport.getValue()));
            production.put(TREASURY, treasuryList);
        }

        for (final Region region : regionList) {
            final List<Integer> lstGoods = new ArrayList<Integer>(15);
            for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 2, "warehouse.region." + region.getId() + ".good." + good);
                if (thisReport != null) {
                    lstGoods.add(Integer.parseInt(thisReport.getValue()));
                }
            }
            production.put(region, lstGoods);
        }

        return production;
    }

    protected Map<Region, List<Integer>> listFinal(final Game thisGame, final Nation thisNation, final int thisTurn) {
        final Map<Region, List<Integer>> production = new HashMap<Region, List<Integer>>();
        final List<Region> regionList = regionManager.list();

        final List<Integer> treasuryList = new ArrayList<Integer>(1);
        treasuryList.add(Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 1, "warehouse.region.1.good.1").getValue()));
        production.put(TREASURY, treasuryList);

        for (final Region region : regionList) {
            final List<Integer> lstGoods = new ArrayList<Integer>(15);
            for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 1, "warehouse.region." + region.getId() + ".good." + good);
                if (thisReport != null) {
                    lstGoods.add(Integer.parseInt(thisReport.getValue()));
                }
            }
            production.put(region, lstGoods);
        }

        return production;
    }

    protected Map<Region, List<Integer>> listProduction(final Game thisGame, final Nation thisNation, final int thisTurn) {
        int mintProduction = 0;
        final Map<Region, List<Integer>> production = new HashMap<Region, List<Integer>>();
        final List<Region> regionList = regionManager.list();
        for (final Region region : regionList) {
            final List<Integer> lstGoods = new ArrayList<Integer>(15);
            for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 1, "goods.region." + region.getId() + ".good." + good);
                if (thisReport != null) {
                    lstGoods.add(Integer.parseInt(thisReport.getValue()));
                }
            }

            production.put(region, lstGoods);

            // Check mint production
            final Report mintReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 1, "goods.region." + region.getId() + ".good.1");
            if (mintReport != null) {
                mintProduction += Integer.parseInt(mintReport.getValue());
            }
        }

        // Mint production is listed on treasury and not warehouse.
        final List<Integer> simpleList = new ArrayList<Integer>();
        simpleList.add(mintProduction);
        production.put(TREASURY, simpleList);

        return production;
    }

    protected Map<Region, List<Integer>> listTransfers(final Game thisGame, final Nation thisNation, final int thisTurn) {
        int transFees = 0;
        final Map<Region, List<Integer>> transfers = new HashMap<Region, List<Integer>>();
        final List<Region> regionList = regionManager.list();
        for (final Region region : regionList) {
            final List<Integer> lstGoods = new ArrayList<Integer>(15);
            for (int good = GoodConstants.GOOD_PEOPLE; good <= GoodConstants.GOOD_COLONIAL; good++) {
                final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 1, "fees.region." + region.getId() + ".good." + good);
                if (thisReport != null) {
                    lstGoods.add(Integer.parseInt(thisReport.getValue()));
                } else {
                    lstGoods.add(0);
                }
            }

            transfers.put(region, lstGoods);

            // Check transaction fees
            final Report feesReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn - 1, "fees.region." + region.getId() + ".good.1");
            if (feesReport != null) {
                transFees += Integer.parseInt(feesReport.getValue());
            }
        }

        // Mint production is listed on treasury and not warehouse.
        final List<Integer> simpleList = new ArrayList<Integer>();
        simpleList.add(transFees);
        transfers.put(TREASURY, simpleList);

        return transfers;
    }

    /**
     * Instance PlayerOrderManager class to perform queries
     * about region objects.
     */
    private transient PlayerOrderManagerBean ordersManager;

    public PlayerOrderManagerBean getOrdersManager() {
        return ordersManager;
    }

    /**
     * Setter method used by spring to inject a ordersManager bean.
     *
     * @param injPlayerOrderManager a ordersManager bean.
     */
    public void setOrdersManager(final PlayerOrderManagerBean injPlayerOrderManager) {
        ordersManager = injPlayerOrderManager;
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
     * Instance RegionManager class to perform queries
     * about region objects.
     */
    private transient RegionManagerBean regionManager;

    public RegionManagerBean getRegionManager() {
        return regionManager;
    }

    public ReportManagerBean getReportManager() {
        return reportManager;
    }

    /**
     * Setter method used by spring to inject a RegionManager bean.
     *
     * @param injRegionManager a RegionManager bean.
     */
    public void setRegionManager(final RegionManagerBean injRegionManager) {
        regionManager = injRegionManager;
    }

    /**
     * Instance GoodManager class to perform queries
     * about good objects.
     */
    private transient GoodManagerBean goodManager;

    public GoodManagerBean getGoodManager() {
        return goodManager;
    }

    /**
     * Setter method used by spring to inject a goodManager bean.
     *
     * @param injGoodManager a goodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
    }

}
