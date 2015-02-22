package com.eaw1805.www.controllers.game.economy;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.ProductionSiteManagerBean;
import com.eaw1805.data.managers.beans.RegionManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.WarehouseManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.User;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.map.ProductionSite;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.commands.ChartData;
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
 * Used to display the monthly production of a particular nation.
 */
@Controller
public class ShowProduction
        extends ExtendedController
        implements GoodConstants, RegionConstants, ProductionSiteConstants, ReportConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(ShowEconomy.class);

    @RequestMapping(method = RequestMethod.GET, value = "/report/scenario/{scenarioId}/game/{gameId}/nation/{nationId}/economy/production")
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
        if (thisUser.getUserType() != 3 && getUserGameManager().listActive(thisUser, thisGame, thisNation).isEmpty()) {
            throw new InvalidPageException("Page not found");
        }

        // produce report + Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        try {
            refData.putAll(prepareReport(thisGame, thisNation, thisGame.getTurn()));

        } catch (Exception e) {
            LOGGER.error("Show report", e);
        }
        refData.put("user", thisUser);
        refData.put("unreadMessagesCount", messageManager.countUnreadMessagesByReceiver(thisUser));

        LOGGER.debug("[" + thisUser.getUsername() + "/" + thisUser.getRemoteAddress() + "] Show production for game=" + gameId + "/nation=" + thisNation.getName());
        if (request.getParameter("eawClient") == null) {
            return new ModelAndView("game/production", refData);
        } else {
            return new ModelAndView("game/productionMinimal", refData);
        }
    }

    /**
     * Prepare production sites report.
     *
     * @param thisGame   the game to examine.
     * @param thisNation the nation to examine.
     * @param turnInt    the turn to examine.
     * @return the map with the data that will be passed to the jsp.
     */
    @Cachable(cacheName = "longGameCache")
    public Map<String, Object> prepareReport(final Game thisGame,
                                             final Nation thisNation,
                                             final int turnInt) {

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // Retrieve support tables
        final List<Region> regionList = regionManager.list();
        final List<Good> goodList = goodManager.list();
        goodList.remove(GoodConstants.GOOD_CP - 1); // Remove Command Points -- this is a virtual good
        goodList.remove(GoodConstants.GOOD_AP - 1); // Remove Command Points -- this is a virtual good

        final Map<Integer, Good> idToGood = new HashMap<Integer, Good>();
        for (Good good : goodList) {
            idToGood.put(good.getGoodId(), good);
        }

        // Retrieve statistics for all goods produced at EUROPE
        final List<List<ChartData>> goodStats = produceGoodsStats(thisGame, thisNation, EUROPE, goodList);

        // Money is reported separately along with taxation
        final List<List<ChartData>> treasuryStats = new ArrayList<List<ChartData>>();
        treasuryStats.add(produceTaxationStats(thisGame, thisNation));
        treasuryStats.add(goodStats.remove(0));

        // Citizens related production is reported separately
        goodStats.remove(0);
        final List<List<ChartData>> citizenStats = produceCitizensStats(thisGame, thisNation, regionList);

        // Retrieve statistics for all goods stored at EUROPE
        final List<List<ChartData>> warehouseStats = produceWarehouseStats(thisGame, thisNation, EUROPE, goodList);

        // Money related production is reported separately
        final List<ChartData> moneyStats = warehouseStats.remove(0);

        // Citizens are reported separately
        final List<ChartData> peopleStats = warehouseStats.remove(0);

        // Retrieve statistics for all goods stored at AFRICA
        final List<List<ChartData>> goodStatsAfrica = produceGoodsStats(thisGame, thisNation, AFRICA, goodList);
        final List<List<ChartData>> warehouseStatsAfrica = produceWarehouseStats(thisGame, thisNation, AFRICA, goodList);
        warehouseStatsAfrica.remove(0);
        warehouseStatsAfrica.remove(0);

        // Retrieve statistics for all goods stored at CARIBBEAN
        final List<List<ChartData>> goodStatsCaribbean = produceGoodsStats(thisGame, thisNation, CARIBBEAN, goodList);
        final List<List<ChartData>> warehouseStatsCaribbean = produceWarehouseStats(thisGame, thisNation, CARIBBEAN, goodList);
        warehouseStatsCaribbean.remove(0);
        warehouseStatsCaribbean.remove(0);

        // Retrieve statistics for all goods stored at INDIES
        final List<List<ChartData>> goodStatsIndies = produceGoodsStats(thisGame, thisNation, INDIES, goodList);
        final List<List<ChartData>> warehouseStatsIndies = produceWarehouseStats(thisGame, thisNation, INDIES, goodList);
        warehouseStatsIndies.remove(0);
        warehouseStatsIndies.remove(0);

        // Remove Money -- we will display this separately
        goodList.remove(GoodConstants.GOOD_MONEY - 1);

        // access Calendar object
        final Calendar thisCal = calendar(thisGame);
        final StringBuilder strBuf = new StringBuilder();
        strBuf.append(thisCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        strBuf.append(" ");
        strBuf.append(thisCal.get(Calendar.YEAR));

        // Retrieve statistics for all production sites

        final List<ProductionSite> allSites = productionSiteManager.list();
        final List<ProductionSite> barracks = new ArrayList<ProductionSite>();
        final List<ProductionSite> sites = new ArrayList<ProductionSite>();
        for (ProductionSite site : allSites) {
            if (site.getId() >= 12) {
                barracks.add(site);
            } else {
                sites.add(site);
            }
        }

        refData.put("regionToInitialGoods", listInitial(thisGame, thisNation, turnInt - 2));
        refData.put("regionToOrdersGoods", listOrderAffects(thisGame, thisNation, turnInt - 1, regionList, refData));

        refData.put("game", thisGame);
        refData.put("months", constructMonths(thisGame));
        refData.put("turn", turnInt);
        refData.put("nation", thisNation);
        refData.put("gameId", thisGame.getGameId());
        refData.put("gameDate", strBuf.toString());
        refData.put("nationId", thisNation.getId());
        refData.put("goodList", goodList);
        refData.put("moneyStats", moneyStats);
        refData.put("citizenStats", citizenStats);
        refData.put("goodStats", goodStats);
        refData.put("warehouseStats", warehouseStats);
        refData.put("treasuryStats", treasuryStats);
        refData.put("goodStatsAfrica", goodStatsAfrica);
        refData.put("warehouseStatsAfrica", warehouseStatsAfrica);
        refData.put("goodStatsCaribbean", goodStatsCaribbean);
        refData.put("warehouseStatsCaribbean", warehouseStatsCaribbean);
        refData.put("goodStatsIndies", goodStatsIndies);
        refData.put("warehouseStatsIndies", warehouseStatsIndies);
        refData.put("peopleStats", peopleStats);
        refData.put("regionList", regionList);
        refData.put("vp", 0);
        refData.put("sites", sites);
        refData.put("barracks", barracks);
        refData.put("goodMoney", new Integer(1));
        refData.put("idToGood", idToGood);

        refData.put("regionToPsToStats", producePSiteStats(thisGame, thisNation, refData));
        refData.put("siteStats", producePSiteStats2(thisGame, thisNation));

        return refData;
    }

    private int[] getPsTypeAndGoodTypeByReporteKey(final String reportKey) {
        int[] out = new int[2];
        if (reportKey.contains("estate")) {
            out[0] = PS_ESTATE;
            out[1] = GOOD_FOOD;

        } else if (reportKey.contains("factory")) {
            out[0] = PS_FACTORY;
            out[1] = GOOD_INPT;

        } else if (reportKey.contains("horsebreedingfarm")) {
            out[0] = PS_FARM_HORSE;
            out[1] = GOOD_HORSE;

        } else if (reportKey.contains("lumbercamp")) {
            out[0] = PS_LUMBERCAMP;
            out[1] = GOOD_WOOD;

        } else if (reportKey.contains("mine")) {
            out[0] = PS_MINE;
            out[1] = Integer.parseInt(reportKey.split("\\.")[3]);

        } else if (reportKey.contains("mint")) {
            out[0] = PS_MINT;
            out[1] = GOOD_MONEY;

        } else if (reportKey.contains("plantation")) {
            out[0] = PS_PLANTATION;
            out[1] = GOOD_COLONIAL;

        } else if (reportKey.contains("quarry")) {
            out[0] = PS_QUARRY;
            out[1] = GOOD_STONE;

        } else if (reportKey.contains("sheepfarm")) {
            out[0] = PS_FARM_SHEEP;
            out[1] = GOOD_WOOL;

        } else if (reportKey.contains("vineyard")) {
            out[0] = PS_VINEYARD;
            out[1] = GOOD_WINE;

        } else if (reportKey.contains("weavingmill")) {
            out[0] = PS_MILL;
            out[1] = GOOD_FABRIC;

        } else {
            out[0] = -1;
            out[1] = -1;
        }
        return out;
    }

    public List<Integer> getGoodsByPSite(int pSiteId) {
        final List<Integer> out = new ArrayList<Integer>();
        switch (pSiteId) {
            case PS_ESTATE:
                out.add(GOOD_FOOD);
                break;

            case PS_FACTORY:
                out.add(GOOD_INPT);
                break;

            case PS_FARM_HORSE:
                out.add(GOOD_HORSE);
                break;

            case PS_LUMBERCAMP:
                out.add(GOOD_WOOD);
                break;

            case PS_MINE:
                out.add(GOOD_ORE);
                out.add(GOOD_PRECIOUS);
                out.add(GOOD_GEMS);
                break;

            case PS_MINT:
                out.add(GOOD_MONEY);
                break;

            case PS_PLANTATION:
                out.add(GOOD_COLONIAL);
                break;

            case PS_QUARRY:
                out.add(GOOD_STONE);
                break;

            case PS_FARM_SHEEP:
                out.add(GOOD_WOOL);
                break;

            case PS_VINEYARD:
                out.add(GOOD_WINE);
                break;

            case PS_MILL:
                out.add(GOOD_FABRIC);
                break;
        }
        return out;
    }

    private Map<Integer, Map<Integer, Map<String, Object>>> producePSiteStats(final Game thisGame, final Nation thisNation, final Map<String, Object> refData) {
        final Map<Integer, Map<Integer, Map<String, Object>>> out = new HashMap<Integer, Map<Integer, Map<String, Object>>>();

        //init region stats
        out.put(EUROPE, new HashMap<Integer, Map<String, Object>>());//init for europe
        out.put(CARIBBEAN, new HashMap<Integer, Map<String, Object>>());//init for caribbean
        out.put(AFRICA, new HashMap<Integer, Map<String, Object>>());//init for africa
        out.put(INDIES, new HashMap<Integer, Map<String, Object>>());//init for indies

        final Map<Integer, Boolean> hasGoodsInRegion = new HashMap<Integer, Boolean>();
        hasGoodsInRegion.put(EUROPE, false);
        hasGoodsInRegion.put(CARIBBEAN, false);
        hasGoodsInRegion.put(AFRICA, false);
        hasGoodsInRegion.put(INDIES, false);

        //init pr statistics stats
        for (int index = ProductionSiteConstants.PS_FIRST; index <= ProductionSiteConstants.PS_LAST; index++) {
            out.get(EUROPE).put(index, new HashMap<String, Object>());
            out.get(EUROPE).get(index).put("number", new HashMap<Integer, Integer>());
            out.get(EUROPE).get(index).put("producedTypes", getGoodsByPSite(index));//the type of good that was produced
            out.get(EUROPE).get(index).put("producedQuantity", new HashMap<Integer, Integer>());//the amount of good that was produced.
            for (Integer good : (ArrayList<Integer>) out.get(EUROPE).get(index).get("producedTypes")) {
                ((HashMap<Integer, Integer>) out.get(EUROPE).get(index).get("producedQuantity")).put(good, 0);//the amount of good that was produced.
                ((HashMap<Integer, Integer>) out.get(EUROPE).get(index).get("number")).put(good, 0);//the amount of good that was produced.
            }
            out.get(EUROPE).get(index).put("warning", false);//the amount of good that was produced.


            out.get(CARIBBEAN).put(index, new HashMap<String, Object>());
            out.get(CARIBBEAN).get(index).put("number", new HashMap<Integer, Integer>());
            out.get(CARIBBEAN).get(index).put("producedTypes", getGoodsByPSite(index));//the type of good that was produced
            out.get(CARIBBEAN).get(index).put("producedQuantity", new HashMap<Integer, Integer>());//the amount of good that was produced.
            for (Integer good : (ArrayList<Integer>) out.get(CARIBBEAN).get(index).get("producedTypes")) {
                ((HashMap<Integer, Integer>) out.get(CARIBBEAN).get(index).get("producedQuantity")).put(good, 0);//the amount of good that was produced.
                ((HashMap<Integer, Integer>) out.get(CARIBBEAN).get(index).get("number")).put(good, 0);//the amount of good that was produced.
            }
            out.get(CARIBBEAN).get(index).put("warning", false);//the amount of good that was produced.

            out.get(AFRICA).put(index, new HashMap<String, Object>());
            out.get(AFRICA).get(index).put("number", new HashMap<Integer, Integer>());
            out.get(AFRICA).get(index).put("producedTypes", getGoodsByPSite(index));//the type of good that was produced
            out.get(AFRICA).get(index).put("producedQuantity", new HashMap<Integer, Integer>());//the amount of good that was produced.
            for (Integer good : (ArrayList<Integer>) out.get(AFRICA).get(index).get("producedTypes")) {
                ((HashMap<Integer, Integer>) out.get(AFRICA).get(index).get("producedQuantity")).put(good, 0);//the amount of good that was produced.
                ((HashMap<Integer, Integer>) out.get(AFRICA).get(index).get("number")).put(good, 0);//the amount of good that was produced.
            }
            out.get(AFRICA).get(index).put("warning", false);//the amount of good that was produced.

            out.get(INDIES).put(index, new HashMap<String, Object>());
            out.get(INDIES).get(index).put("number", new HashMap<Integer, Integer>());
            out.get(INDIES).get(index).put("producedTypes", getGoodsByPSite(index));//the type of good that was produced
            out.get(INDIES).get(index).put("producedQuantity", new HashMap<Integer, Integer>());//the amount of good that was produced.
            for (Integer good : (ArrayList<Integer>) out.get(INDIES).get(index).get("producedTypes")) {
                ((HashMap<Integer, Integer>) out.get(INDIES).get(index).get("producedQuantity")).put(good, 0);//the amount of good that was produced.
                ((HashMap<Integer, Integer>) out.get(INDIES).get(index).get("number")).put(good, 0);//the amount of good that was produced.
            }
            out.get(INDIES).get(index).put("warning", false);//the amount of good that was produced.
        }

        final List<Report> psNationReports = reportManager.listByOwnerTurnKey(thisNation, thisGame, thisGame.getTurn() - 1, "production.%");
        for (Report report : psNationReports) {
            final int regionId;
            final int producedQuantity = Integer.parseInt(report.getValue());


            int[] types = getPsTypeAndGoodTypeByReporteKey(report.getKey());
            int psType = types[0];
            int goodType = types[1];
            if (psType == -1) {
                continue;
            }

            final String positionStr;
            if (psType == PS_MINE) {
                positionStr = report.getKey().split("\\.")[4];
            } else {
                positionStr = report.getKey().split("\\.")[2];
            }

            if (positionStr.startsWith("E")) {
                regionId = EUROPE;
            } else if (positionStr.startsWith("A")) {
                regionId = AFRICA;
            } else if (positionStr.startsWith("I")) {
                regionId = INDIES;
            } else if (positionStr.startsWith("C")) {
                regionId = CARIBBEAN;
            } else {
                regionId = EUROPE;
            }

            int currentNumber = ((HashMap<Integer, Integer>) out.get(regionId).get(psType).get("number")).get(goodType);
            ((HashMap<Integer, Integer>) out.get(regionId).get(psType).get("number")).put(goodType, currentNumber + 1);
            int currentSum = ((HashMap<Integer, Integer>) out.get(regionId).get(psType).get("producedQuantity")).get(goodType);
            ((HashMap<Integer, Integer>) out.get(regionId).get(psType).get("producedQuantity")).put(goodType, currentSum + producedQuantity);
            if (producedQuantity == 0) {
                out.get(regionId).get(psType).put("warning", true);
            } else {
                hasGoodsInRegion.put(regionId, true);
            }
        }
        refData.put("hasGoodsInRegion", hasGoodsInRegion);
        final List<Warehouse> nationWarehouses = warehouseManager.listByGameNation(thisGame, thisNation);
        final Map<Integer, Warehouse> regionToWarehouse = new HashMap<Integer, Warehouse>();
        for (Warehouse warehouse : nationWarehouses) {
            regionToWarehouse.put(warehouse.getRegion().getId(), warehouse);
        }
        refData.put("regionToWarehouse", regionToWarehouse);
        return out;
    }

    private List<List<ChartData>> producePSiteStats2(final Game thisGame, final Nation thisNation) {
        final List<Sector> lstSectors = sectorManager.listByGameNation(thisGame, thisNation);
        final int[][] tmpArray = new int[RegionConstants.REGION_LAST + 1][ProductionSiteConstants.PS_LAST + 1];
        for (final Sector lstSector : lstSectors) {
            if (lstSector.getProductionSite() != null) {
                tmpArray[lstSector.getPosition().getRegion().getId()][lstSector.getProductionSite().getId()]++;
            }
        }

        // convert array to list of lists
        final List<List<ChartData>> siteStats = new ArrayList<List<ChartData>>();
        for (int region = RegionConstants.REGION_FIRST; region <= RegionConstants.REGION_LAST; region++) {
            final List<ChartData> thisList = new ArrayList<ChartData>();
            for (int site = ProductionSiteConstants.PS_FIRST; site <= ProductionSiteConstants.PS_LAST; site++) {
                final ChartData cdata = new ChartData();
                cdata.setYear(region);
                cdata.setMonth(site);
                cdata.setValue(tmpArray[region][site]);

                thisList.add(cdata);
            }
            siteStats.add(thisList);
        }
        return siteStats;
    }

    protected Map<Integer, Map<Integer, Integer>> listOrderAffects(final Game thisGame, final Nation thisNation, final int thisTurn, final List<Region> regionList, final Map<String, Object> refData) {
        final Map<Integer, Map<Integer, Integer>> out = new HashMap<Integer, Map<Integer, Integer>>();
        final List<PlayerOrder> orders = orderManager.listByGameNation(thisGame, thisNation, thisTurn);
        out.put(EUROPE, new HashMap<Integer, Integer>());
        out.put(CARIBBEAN, new HashMap<Integer, Integer>());
        out.put(INDIES, new HashMap<Integer, Integer>());
        out.put(AFRICA, new HashMap<Integer, Integer>());

        for (Region region : regionList) {
            for (int good = GOOD_MONEY; good <= GOOD_COLONIAL; good++) {
                out.get(region.getId()).put(good, 0);
            }
        }
        for (PlayerOrder order : orders) {
            if (order.getRegion() == null) {
                continue;
            }
            int regionId = order.getRegion().getId();
            for (Map.Entry<Integer, Integer> entry : order.getUsedGoodsQnt().entrySet()) {
                int goodId = entry.getKey();
                int quantity = entry.getValue();
                if (goodId == GOOD_MONEY) {
                    out.get(EUROPE).put(goodId, out.get(EUROPE).get(goodId) - quantity);
                } else {
                    out.get(regionId).put(goodId, out.get(regionId).get(goodId) - quantity);
                }
            }
        }

        final Map<Integer, Map<Integer, Map<String, Integer>>> subTotalsMap = new HashMap<Integer, Map<Integer, Map<String, Integer>>>();
        subTotalsMap.put(EUROPE, new HashMap<Integer, Map<String, Integer>>());
        subTotalsMap.put(AFRICA, new HashMap<Integer, Map<String, Integer>>());
        subTotalsMap.put(CARIBBEAN, new HashMap<Integer, Map<String, Integer>>());
        subTotalsMap.put(INDIES, new HashMap<Integer, Map<String, Integer>>());
        for (Region region : regionList) {
            for (int good = GOOD_MONEY; good <= GOOD_COLONIAL; good++) {
                subTotalsMap.get(region.getId()).put(good, new HashMap<String, Integer>());
                subTotalsMap.get(region.getId()).get(good).put("Orders Total", out.get(region.getId()).get(good));
            }
        }
        if (thisGame.getTurn() == 0) {
            refData.put("subTotals", subTotalsMap);
            return out;
        }

        final Report thisReport1 = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "taxation");
        if (thisReport1 != null) {
            int taxation = Integer.parseInt(thisReport1.getValue());
            subTotalsMap.get(EUROPE).get(GOOD_MONEY).put("Taxation", taxation);
            out.get(EUROPE).put(GOOD_MONEY, out.get(EUROPE).get(GOOD_MONEY) + taxation);
        }

        final Report thisReport2 = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "commanders.totalMoney");
        if (thisReport2 != null) {
            int commanderSalaries = Integer.parseInt(thisReport2.getValue());
            subTotalsMap.get(EUROPE).get(GOOD_MONEY).put("Commander Salaries", -commanderSalaries);
            out.get(EUROPE).put(GOOD_MONEY, out.get(EUROPE).get(GOOD_MONEY) - commanderSalaries);
        }

        final Report thisReport3 = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "armies.totalMoney");
        if (thisReport3 != null) {
            int soldierSalaries = Integer.parseInt(thisReport3.getValue());
            subTotalsMap.get(EUROPE).get(GOOD_MONEY).put("Soldier Salaries", -soldierSalaries);
            out.get(EUROPE).put(GOOD_MONEY, out.get(EUROPE).get(GOOD_MONEY) - soldierSalaries);
        }

        int marineSalaries = Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "ships.totalMoney").getValue());
        subTotalsMap.get(EUROPE).get(GOOD_MONEY).put("Marine Salaries", -marineSalaries);
        out.get(EUROPE).put(GOOD_MONEY, out.get(EUROPE).get(GOOD_MONEY) - marineSalaries);

        int btrainsCost = Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "baggagetrain.totalMoney").getValue());
        subTotalsMap.get(EUROPE).get(GOOD_MONEY).put("Baggage Trains", -btrainsCost);
        out.get(EUROPE).put(GOOD_MONEY, out.get(EUROPE).get(GOOD_MONEY) - btrainsCost);

        int maintenanceCost = Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "production.maintenance").getValue());
        subTotalsMap.get(EUROPE).get(GOOD_MONEY).put("Maintenance Cost", -maintenanceCost);
        out.get(EUROPE).put(GOOD_MONEY, out.get(EUROPE).get(GOOD_MONEY) - maintenanceCost);

        for (Region region : regionList) {
            int foodArmy = Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, A_TOT_FOOD + region.getId()).getValue());
            subTotalsMap.get(region.getId()).get(GOOD_FOOD).put("Soldiers Food", -foodArmy);
            out.get(region.getId()).put(GOOD_FOOD, out.get(region.getId()).get(GOOD_FOOD) - foodArmy);

            int wineFleet = Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, S_TOT_WINE + region.getId()).getValue());
            subTotalsMap.get(region.getId()).get(GOOD_WINE).put("Marines Wine", -wineFleet);
            out.get(region.getId()).put(GOOD_WINE, out.get(region.getId()).get(GOOD_WINE) - wineFleet);

            int foodPopulation = Integer.parseInt(reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, E_POP_FOOD + region.getId()).getValue());
            subTotalsMap.get(region.getId()).get(GOOD_FOOD).put("Population Food", -foodPopulation);
            out.get(region.getId()).put(GOOD_FOOD, out.get(region.getId()).get(GOOD_FOOD) - foodPopulation);
            final Report totFood = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, P_TOT + P_FOOD);
            int foodPow = 0;
            if (totFood != null) {
                foodPow = Integer.parseInt(totFood.getValue());
            }
            subTotalsMap.get(region.getId()).get(GOOD_FOOD).put("Prisoners Food", -foodPow);
            out.get(region.getId()).put(GOOD_FOOD, out.get(region.getId()).get(GOOD_FOOD) - foodPow);
        }
        refData.put("subTotals", subTotalsMap);
        return out;
    }

    protected Map<Integer, Map<Integer, Integer>> listInitial(final Game thisGame, final Nation thisNation, final int thisTurn) {
        final Map<Integer, Map<Integer, Integer>> production = new HashMap<Integer, Map<Integer, Integer>>();
        final List<Region> regionList = regionManager.list();
        for (final Region region : regionList) {
            final Map<Integer, Integer> mapGoods = new HashMap<Integer, Integer>();
            for (int good = GoodConstants.GOOD_MONEY; good <= GoodConstants.GOOD_COLONIAL; good++) {
                final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, thisTurn, "warehouse.region." + region.getId() + ".good." + good);
                if (thisReport != null) {
                    mapGoods.put(good, Integer.parseInt(thisReport.getValue()));
                }
            }
            production.put(region.getId(), mapGoods);
        }

        return production;
    }

    private List<List<ChartData>> produceGoodsStats(final Game thisGame, final Nation thisNation, final int regionId, List<Good> goodList) {
        // Retrieve statistics for all goods stored at EUROPE (minus 2, starting from 1 => -3 diff)
        final List<List<ChartData>> goodStats = new ArrayList<List<ChartData>>();
        for (int good = GOOD_FIRST; good <= GOOD_COLONIAL; good++) {
            final List<Report> reportEurope = reportManager.listByOwnerKey(thisNation, thisGame, "goods.region." + regionId + ".good." + good);
            final List<ChartData> thisList = new ArrayList<ChartData>();
            final Calendar thisCal = calendar(thisGame);
            for (final Report thisReport : reportEurope) {
                final ChartData cdata = new ChartData();
                cdata.setCaption(goodList.get(good - 1).getName());
                cdata.setYear(thisCal.get(Calendar.YEAR));
                cdata.setMonth(thisCal.get(Calendar.MONTH));
                cdata.setValue(Integer.parseInt(thisReport.getValue()));

                thisList.add(cdata);

                thisCal.add(Calendar.MONTH, 1);
            }
            goodStats.add(thisList);
        }

        return goodStats;
    }

    private List<List<ChartData>> produceWarehouseStats(final Game thisGame, final Nation thisNation, final int regionId, List<Good> goodList) {
        // Retrieve statistics for all goods stored at EUROPE (minus 2, starting from 1 => -3 diff)
        final List<List<ChartData>> goodStats = new ArrayList<List<ChartData>>();
        for (int good = GOOD_FIRST; good <= GOOD_COLONIAL; good++) {
            final List<Report> reportEurope = reportManager.listByOwnerKey(thisNation, thisGame, "warehouse.region." + regionId + ".good." + good);
            final List<ChartData> thisList = new ArrayList<ChartData>();
            final Calendar thisCal = calendar(thisGame);
            for (final Report thisReport : reportEurope) {
                final ChartData cdata = new ChartData();
                cdata.setCaption(goodList.get(good - 1).getName());
                cdata.setYear(thisCal.get(Calendar.YEAR));
                cdata.setMonth(thisCal.get(Calendar.MONTH));
                cdata.setValue(Integer.parseInt(thisReport.getValue()));

                thisList.add(cdata);

                thisCal.add(Calendar.MONTH, 1);
            }
            goodStats.add(thisList);
        }

        return goodStats;
    }

    private List<ChartData> produceTaxationStats(final Game thisGame, final Nation thisNation) {
        // Retrieve taxation statistics
        final List<ChartData> taxStats = new ArrayList<ChartData>();
        final List<Report> reportEurope = reportManager.listByOwnerKey(thisNation, thisGame, "taxation");
        final Calendar thisCal = calendar(thisGame);
        for (final Report thisReport : reportEurope) {
            final ChartData cdata = new ChartData();
            cdata.setCaption("Taxation");
            cdata.setYear(thisCal.get(Calendar.YEAR));
            cdata.setMonth(thisCal.get(Calendar.MONTH));
            cdata.setValue(Integer.parseInt(thisReport.getValue()));

            taxStats.add(cdata);
            thisCal.add(Calendar.MONTH, 1);
        }

        return taxStats;
    }

    private List<List<ChartData>> produceCitizensStats(final Game thisGame, final Nation thisNation, List<Region> regionList) {
        // Retrieve statistics for all goods stored at EUROPE (minus 2, starting from 1 => -3 diff)
        final List<List<ChartData>> goodStats = new ArrayList<List<ChartData>>();
        for (int region = REGION_FIRST; region <= REGION_LAST; region++) {
            int totPop = 0;
            final List<Report> reportEurope = reportManager.listByOwnerKey(thisNation, thisGame, "population.increase.region." + region);
            final List<ChartData> thisList = new ArrayList<ChartData>();
            final Calendar thisCal = calendar(thisGame);
            for (final Report thisReport : reportEurope) {
                final ChartData cdata = new ChartData();
                cdata.setCaption(regionList.get(region - 1).getName());
                cdata.setYear(thisCal.get(Calendar.YEAR));
                cdata.setMonth(thisCal.get(Calendar.MONTH));
                cdata.setValue(Integer.parseInt(thisReport.getValue()));

                totPop += cdata.getValue();

                thisList.add(cdata);

                thisCal.add(Calendar.MONTH, 1);
            }

            // Only add this region if actual data exist
            if (totPop > 0) {
                goodStats.add(thisList);
            }
        }

        return goodStats;
    }

    /**
     * Instance GoodManager class to perform queries
     * about good objects.
     */
    private transient GoodManagerBean goodManager;

    /**
     * Setter method used by spring to inject a goodManager bean.
     *
     * @param injGoodManager a goodManager bean.
     */
    public void setGoodManager(final GoodManagerBean injGoodManager) {
        goodManager = injGoodManager;
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

    /**
     * Setter method used by spring to inject a RegionManager bean.
     *
     * @param injRegionManager a RegionManager bean.
     */
    public void setRegionManager(final RegionManagerBean injRegionManager) {
        regionManager = injRegionManager;
    }

    /**
     * Instance ProductionSiteManager class to perform queries
     * about productionSite objects.
     */
    private transient ProductionSiteManagerBean productionSiteManager;

    /**
     * Setter method used by spring to inject a ProductionSiteManager bean.
     *
     * @param injProductionSiteManager a ProductionSiteManager bean.
     */
    public void setProductionSiteManager(final ProductionSiteManagerBean injProductionSiteManager) {
        productionSiteManager = injProductionSiteManager;
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
     * Instance of WarehouseManager class to perform queries
     * about warehouse objects.
     */
    private transient WarehouseManagerBean warehouseManager;

    /**
     * Setter method used by spring to inject a WarehouseManager bean.
     *
     * @param value The value to set.
     */
    public void setWarehouseManager(final WarehouseManagerBean value) {
        this.warehouseManager = value;
    }

    private transient PlayerOrderManagerBean orderManager;

    public void setOrderManager(final PlayerOrderManagerBean orderManager) {
        this.orderManager = orderManager;
    }
}

