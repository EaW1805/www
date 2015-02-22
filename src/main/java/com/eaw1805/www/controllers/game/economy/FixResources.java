package com.eaw1805.www.controllers.game.economy;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.ReportConstants;
import com.eaw1805.data.managers.beans.RegionManagerBean;
import com.eaw1805.data.managers.beans.ReportManagerBean;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.Report;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.www.controllers.GameReportsController;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FixResources
        extends GameReportsController
        implements GoodConstants, ReportConstants, NationConstants {


    public static final Region TREASURY = new Region();

    static {
        TREASURY.setId(0);
        TREASURY.setCode('T');
        TREASURY.setName("Treasury");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fixResources")
    protected ModelAndView handleOverview(HttpServletRequest request) {
        try {
            ScenarioContextHolder.setScenario("1802");
            int[] gameIds = {4, 5, 7};

            for (int gameId : gameIds) {
                final Game game = getGameManager().getByID(gameId);

                for (int nationId = 1; nationId <= 17; nationId++) {
                    System.out.println("FIXING DB FOR GAME/" + gameId + " NATION/" + nationId);
                    final Nation nation = getNationManager().getByID(nationId);
                    Map<Integer, Warehouse> warehouseGoods = new HashMap<Integer, Warehouse>();
                    List<Report> reports = reportManager.listByOwnerTurnKey(nation, game, game.getTurn() - 1, "production.%");
                    for (int regionId = 1; regionId <= 4; regionId++) {
                        Region region = regionManager.getByID(regionId);
                        warehouseGoods.put(regionId, getWarehouseManager().getByNationRegion(game, nation, region));
                    }

                    for (Report report : reports) {
                        if ("production.maintenance".equals(report.getKey())) {
                            continue;//don't calculate this yet
                        }
                        int goodId = getGoodByKey(report.getKey());
                        int regionId = getRegionByKey(report.getKey());
                        int quantity = Integer.parseInt(report.getValue());
                        warehouseGoods.get(regionId).getStoredGoodsQnt().put(goodId, warehouseGoods.get(regionId).getStoredGoodsQnt().get(goodId) + quantity);
                        if (goodId == GOOD_INPT) {//if industrial points ~ factory calculate the expenses
                            int factoryProduction = 100;
                            if (regionId != EUROPE) {
                                factoryProduction = 75;
                            }

                            // boosted production
                            if (game.isBoostedProduction()) {
                                factoryProduction *= 1.25;
                            }

                            int wood = (quantity * -20) / factoryProduction;
                            int ore = (quantity * -1) / factoryProduction;
                            int fabric = (quantity * -5) / factoryProduction;
                            warehouseGoods.get(regionId).getStoredGoodsQnt().put(GOOD_WOOD, warehouseGoods.get(regionId).getStoredGoodsQnt().get(GOOD_WOOD) + wood);
                            warehouseGoods.get(regionId).getStoredGoodsQnt().put(GOOD_ORE, warehouseGoods.get(regionId).getStoredGoodsQnt().get(GOOD_ORE) + ore);
                            warehouseGoods.get(regionId).getStoredGoodsQnt().put(GOOD_FABRIC, warehouseGoods.get(regionId).getStoredGoodsQnt().get(GOOD_FABRIC) + fabric);

                        } else if (goodId == GOOD_MONEY) {
                            int preciousMetals = (quantity * -1) / 30000;

                            // boosted production uses less gold items
                            if (game.isBoostedProduction()) {
                                preciousMetals = (int) ((quantity * -1) / (30000d * 1.25d));
                            }
                            warehouseGoods.get(regionId).getStoredGoodsQnt().put(GOOD_PRECIOUS, warehouseGoods.get(regionId).getStoredGoodsQnt().get(GOOD_PRECIOUS) + preciousMetals);

                        } else if (goodId == GOOD_FABRIC) {
                            int goodWool = quantity * -2;
                            warehouseGoods.get(regionId).getStoredGoodsQnt().put(GOOD_WOOL, warehouseGoods.get(regionId).getStoredGoodsQnt().get(GOOD_WOOL) + goodWool);
                        }

                    }
                    individualReports(game, nation, game.getTurn() - 1, warehouseGoods);
                    for (int regionId = 1; regionId <= 4; regionId++) {
                        getWarehouseManager().update(warehouseGoods.get(regionId));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    protected Map<Integer, List<Report>> individualReports(final Game thisGame, final Nation thisNation, final int turnInt, final Map<Integer, Warehouse> warehouses) {
        final Map<Integer, List<Report>> report = new HashMap<Integer, List<Report>>();
        final List<Region> regionList = regionManager.list();

        // Initialize map
        report.put(TREASURY.getId(), new ArrayList<Report>());
        for (final Region region : regionList) {
            final List<Report> thisList = new ArrayList<Report>();
            report.put(region.getId(), thisList);
        }

        final Report moneyCommanders = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, "commanders.totalMoney");
        if (moneyCommanders != null) {
            warehouses.get(EUROPE).getStoredGoodsQnt().put(GOOD_MONEY, warehouses.get(EUROPE).getStoredGoodsQnt().get(GOOD_MONEY) - Integer.parseInt(moneyCommanders.getValue()));
        }

        final Report moneyArmy = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, "armies.totalMoney");
        if (moneyArmy != null) {
            warehouses.get(EUROPE).getStoredGoodsQnt().put(GOOD_MONEY, warehouses.get(EUROPE).getStoredGoodsQnt().get(GOOD_MONEY) - Integer.parseInt(moneyArmy.getValue()));
        }

        final Report moneyFleet = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, "ships.totalMoney");
        if (moneyFleet != null) {
            warehouses.get(EUROPE).getStoredGoodsQnt().put(GOOD_MONEY, warehouses.get(EUROPE).getStoredGoodsQnt().get(GOOD_MONEY) - Integer.parseInt(moneyFleet.getValue()));
        }

        final Report moneyBTrain = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, "baggagetrain.totalMoney");
        if (moneyBTrain != null) {
            warehouses.get(EUROPE).getStoredGoodsQnt().put(GOOD_MONEY, warehouses.get(EUROPE).getStoredGoodsQnt().get(GOOD_MONEY) - Integer.parseInt(moneyBTrain.getValue()));
        }

        for (final Region region : regionList) {
            final Report foodArmy = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, A_TOT_FOOD + region.getId());
            if ((foodArmy != null) && (Integer.parseInt(foodArmy.getValue()) > 0)) {
                warehouses.get(region.getId()).getStoredGoodsQnt().put(GOOD_FOOD, warehouses.get(region.getId()).getStoredGoodsQnt().get(GOOD_FOOD) - Integer.parseInt(foodArmy.getValue()));
            }

            final Report wineFleet = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, S_TOT_WINE + region.getId());
            if ((wineFleet != null) && (Integer.parseInt(wineFleet.getValue()) > 0)) {
                warehouses.get(region.getId()).getStoredGoodsQnt().put(GOOD_WINE, warehouses.get(region.getId()).getStoredGoodsQnt().get(GOOD_WINE) - Integer.parseInt(wineFleet.getValue()));
            }

            final Report thisReport = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, E_POP_FOOD + region.getId());
            if ((thisReport != null) && (Integer.parseInt(thisReport.getValue()) > 0)) {
                warehouses.get(region.getId()).getStoredGoodsQnt().put(GOOD_FOOD, warehouses.get(region.getId()).getStoredGoodsQnt().get(GOOD_FOOD) - Integer.parseInt(thisReport.getValue()));
            }
        }

        final Report foodPow = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, P_TOT + P_FOOD);
        if ((foodPow != null) && (Integer.parseInt(foodPow.getValue()) > 0)) {
            warehouses.get(EUROPE).getStoredGoodsQnt().put(GOOD_FOOD, warehouses.get(EUROPE).getStoredGoodsQnt().get(GOOD_FOOD) - Integer.parseInt(foodPow.getValue()));
        }

        final Report moneyProduction = reportManager.getByOwnerTurnKey(thisNation, thisGame, turnInt, "production.maintenance");
        if (moneyProduction != null) {
            warehouses.get(EUROPE).getStoredGoodsQnt().put(GOOD_MONEY, warehouses.get(EUROPE).getStoredGoodsQnt().get(GOOD_MONEY) - Integer.parseInt(moneyProduction.getValue()));
        }

        return report;
    }

    public int getGoodByKey(final String reportKey) {

        if (reportKey.contains("estate")) {
            return GOOD_FOOD;
        } else if (reportKey.contains("factory")) {
            return GOOD_INPT;
        } else if (reportKey.contains("horsebreedingfarm")) {
            return GOOD_HORSE;
        } else if (reportKey.contains("lumbercamp")) {
            return GOOD_WOOD;
        } else if (reportKey.contains("mine")) {
            return Integer.parseInt(reportKey.split("\\.")[3]);
        } else if (reportKey.contains("mint")) {
            return GOOD_MONEY;
        } else if (reportKey.contains("plantation")) {
            return GOOD_COLONIAL;
        } else if (reportKey.contains("quarry")) {
            return GOOD_STONE;
        } else if (reportKey.contains("sheepfarm")) {
            return GOOD_WOOL;
        } else if (reportKey.contains("vineyard")) {
            return GOOD_WINE;
        } else if (reportKey.contains("weavingmill")) {
            return GOOD_FABRIC;
        }
        return GOOD_MONEY;

    }


    public int getRegionByKey(final String key) {
        String[] values = key.split("\\.");
        String regionValue = values[values.length - 1];
        if (regionValue.startsWith("E")) {
            return EUROPE;
        } else if (regionValue.startsWith("A")) {
            return AFRICA;
        } else if (regionValue.startsWith("C")) {
            return CARIBBEAN;
        } else {
            return INDIES;
        }
    }

    ReportManagerBean reportManager;

    public void setReportManager(ReportManagerBean reportManager) {
        this.reportManager = reportManager;
    }

    RegionManagerBean regionManager;

    public void setRegionManager(RegionManagerBean regionManager) {
        this.regionManager = regionManager;
    }
}
