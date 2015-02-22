package com.eaw1805.www.controllers;


import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NavigationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.managers.beans.ArmyManagerBean;
import com.eaw1805.data.managers.beans.BaggageTrainManagerBean;
import com.eaw1805.data.managers.beans.BarrackManagerBean;
import com.eaw1805.data.managers.beans.BattalionManagerBean;
import com.eaw1805.data.managers.beans.BrigadeManagerBean;
import com.eaw1805.data.managers.beans.CommanderManagerBean;
import com.eaw1805.data.managers.beans.CorpManagerBean;
import com.eaw1805.data.managers.beans.FleetManagerBean;
import com.eaw1805.data.managers.beans.GoodManagerBean;
import com.eaw1805.data.managers.beans.NewsManagerBean;
import com.eaw1805.data.managers.beans.PlayerOrderManagerBean;
import com.eaw1805.data.managers.beans.ProductionSiteManagerBean;
import com.eaw1805.data.managers.beans.SectorManagerBean;
import com.eaw1805.data.managers.beans.ShipManagerBean;
import com.eaw1805.data.managers.beans.SpyManagerBean;
import com.eaw1805.data.managers.beans.TradeCityManagerBean;
import com.eaw1805.data.managers.beans.WarehouseManagerBean;
import com.eaw1805.data.model.PlayerOrder;
import com.eaw1805.data.model.army.Army;
import com.eaw1805.data.model.army.Battalion;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.army.Commander;
import com.eaw1805.data.model.army.Corp;
import com.eaw1805.data.model.army.Spy;
import com.eaw1805.data.model.economy.BaggageTrain;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.fleet.Fleet;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.map.Barrack;

import java.util.StringTokenizer;

/**
 * Methods used by game-related reports controllers.
 */
public class GameReportsController
        extends ExtendedController
        implements OrderConstants, ArmyConstants, RelationConstants, NavigationConstants, RegionConstants {

    /**
     * Instance PlayerOrderManager class to perform queries
     * about region objects.
     */
    private transient PlayerOrderManagerBean ordersManager;

    /**
     * Setter method used by spring to inject a ordersManager bean.
     *
     * @param injPlayerOrderManager a ordersManager bean.
     */
    public void setOrdersManager(final PlayerOrderManagerBean injPlayerOrderManager) {
        ordersManager = injPlayerOrderManager;
    }

    public PlayerOrderManagerBean getOrdersManager() {
        return ordersManager;
    }

    /**
     * Instance ArmyManagerBean class to perform queries
     * about army objects.
     */
    private transient ArmyManagerBean armyManager;

    /**
     * Setter method used by spring to inject a ArmyManagerBean bean.
     *
     * @param injArmyManager a ArmyManagerBean bean.
     */
    public void setArmyManager(final ArmyManagerBean injArmyManager) {
        armyManager = injArmyManager;
    }

    /**
     * Instance BattalionManager class to perform queries
     * about battalion objects.
     */
    private transient BattalionManagerBean battalionManager;

    /**
     * Setter method used by spring to inject a BattalionManager bean.
     *
     * @param injBattalionManager a BattalionManager bean.
     */
    public void setBattalionManager(final BattalionManagerBean injBattalionManager) {
        battalionManager = injBattalionManager;
    }

    /**
     * Instance CommanderManager class to perform queries
     * about commander objects.
     */
    private transient CommanderManagerBean commanderManager;

    /**
     * Setter method used by spring to inject a CommanderManager bean.
     *
     * @param injCommanderManager a CommanderManager bean.
     */
    public void setCommanderManager(final CommanderManagerBean injCommanderManager) {
        commanderManager = injCommanderManager;
    }

    /**
     * Instance CorpManagerBean class to perform queries
     * about army objects.
     */
    private transient CorpManagerBean corpManager;

    /**
     * Setter method used by spring to inject a CorpManagerBean bean.
     *
     * @param injCorpManager a ArmyManagerBean bean.
     */
    public void setCorpManager(final CorpManagerBean injCorpManager) {
        corpManager = injCorpManager;
    }

    /**
     * Instance FleetManager class to perform queries
     * about fleet objects.
     */
    private transient FleetManagerBean fleetManager;

    /**
     * Setter method used by spring to inject a FleetManager bean.
     *
     * @param injFleetManager a fleetManager bean.
     */
    public void setFleetManager(final FleetManagerBean injFleetManager) {
        fleetManager = injFleetManager;
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

    public ProductionSiteManagerBean getProductionSiteManager() {
        return productionSiteManager;
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
     * Instance ShipManagerBean class to perform queries
     * about Ship objects.
     */
    private transient ShipManagerBean shipManager;

    /**
     * Setter method used by spring to inject a ShipManagerBean bean.
     *
     * @param injShipManagerBean a ShipManagerBean bean.
     */
    public void setShipManager(final ShipManagerBean injShipManagerBean) {
        shipManager = injShipManagerBean;
    }

    /**
     * Instance BrigadeManagerBean class to perform queries
     * about CommanderName objects.
     */
    private transient BrigadeManagerBean brigadeManager;

    /**
     * Setter method used by spring to inject a BrigadeManagerBean bean.
     *
     * @param injBrigadeManagerBean a BrigadeManagerBean bean.
     */
    public void setBrigadeManager(final BrigadeManagerBean injBrigadeManagerBean) {
        brigadeManager = injBrigadeManagerBean;
    }

    /**
     * Instance WarehouseManager class to perform queries
     * about warehouse objects.
     */
    private transient WarehouseManagerBean warehouseManager;

    /**
     * Setter method used by spring to inject a WarehouseManager bean.
     *
     * @param injWarehouseManager a WarehouseManager bean.
     */
    public void setWarehouseManager(final WarehouseManagerBean injWarehouseManager) {
        warehouseManager = injWarehouseManager;
    }

    public WarehouseManagerBean getWarehouseManager() {
        return warehouseManager;
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

    public GoodManagerBean getGoodManager() {
        return goodManager;
    }

    /**
     * Instance BarrackManagerBean class to perform queries
     * about barrack objects.
     */
    private transient BarrackManagerBean barrackManager;

    /**
     * Setter method used by spring to inject a BarrackManager bean.
     *
     * @param injBarrackManagerBean a barrackManager bean.
     */
    public void setBarrackManager(final BarrackManagerBean injBarrackManagerBean) {
        barrackManager = injBarrackManagerBean;
    }

    /**
     * Instance SpyManager class to perform queries
     * about Spy objects.
     */
    private transient SpyManagerBean spyManager;

    /**
     * Setter method used by spring to inject a SpyManagerBean bean.
     *
     * @param injSpyManager a commanderManager bean.
     */
    public void setSpyManager(final SpyManagerBean injSpyManager) {
        spyManager = injSpyManager;
    }

    /**
     * Instance BaggageTrainManager class to perform queries
     * about BaggageTrain objects.
     */
    private transient BaggageTrainManagerBean baggageTrainManager;

    /**
     * Setter method used by spring to inject a BaggageTrainManagerBean bean.
     *
     * @param injBaggageTrainManager a baggageTrainManager bean.
     */
    public void setBaggageTrainManager(final BaggageTrainManagerBean injBaggageTrainManager) {
        baggageTrainManager = injBaggageTrainManager;
    }

    /**
     * Instance tradeCityManager class to perform queries
     * about TradeCity objects.
     */
    private transient TradeCityManagerBean tradeCityManager;

    /**
     * Setter method used by spring to inject a tradeCityManager bean.
     *
     * @param injTradeCityManager a tradeCityManager bean.
     */
    public void setTradeCityManager(final TradeCityManagerBean injTradeCityManager) {
        tradeCityManager = injTradeCityManager;
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

    public NewsManagerBean getNewsManager() {
        return newsManager;
    }

    /**
     * Get the description of the order.
     *
     * @param thisOrder the order object.
     * @return the description of the order.
     */
    public String getOrderDescription(final PlayerOrder thisOrder) {
        final int[] identifier = new int[9];

        try {
            identifier[0] = Integer.parseInt(thisOrder.getParameter1());
        } catch (Exception ex) {
            identifier[0] = 0;
        }

        try {
            identifier[1] = Integer.parseInt(thisOrder.getParameter2());
        } catch (Exception ex) {
            identifier[1] = 0;
        }

        try {
            identifier[2] = Integer.parseInt(thisOrder.getParameter3());
        } catch (Exception ex) {
            identifier[2] = 0;
        }

        try {
            identifier[3] = Integer.parseInt(thisOrder.getParameter4());
        } catch (Exception ex) {
            identifier[3] = 0;
        }

        try {
            identifier[4] = Integer.parseInt(thisOrder.getParameter5());
        } catch (Exception ex) {
            identifier[4] = 0;
        }

        try {
            identifier[5] = Integer.parseInt(thisOrder.getParameter6());
        } catch (Exception ex) {
            identifier[5] = 0;
        }

        try {
            identifier[6] = Integer.parseInt(thisOrder.getParameter7());
        } catch (Exception ex) {
            identifier[6] = 0;
        }

        try {
            identifier[7] = Integer.parseInt(thisOrder.getParameter8());
        } catch (Exception ex) {
            identifier[7] = 0;
        }

        try {
            identifier[8] = Integer.parseInt(thisOrder.getParameter9());
        } catch (Exception ex) {
            identifier[8] = 0;
        }

        try {
            switch (thisOrder.getType()) {

                case ORDER_REN_BRIG: {
                    final Brigade thisBrigade = brigadeManager.getByID(identifier[0]);
                    String name;
                    if (thisBrigade == null) {
                        name = "Brigade";

                    } else {
                        name = "'" + thisBrigade.getName() + "'";

                        if (name.toLowerCase().indexOf("brigade") < 1) {
                            name = "Brigade " + name;
                        }
                    }

                    return "Rename "
                            + name
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_COMM: {
                    final Commander thisCommander = commanderManager.getByID(identifier[0]);
                    String nameCommander;
                    if (thisCommander == null) {
                        nameCommander = "Commander";

                    } else {
                        nameCommander = "'" + thisCommander.getName() + "'";
                        if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                            nameCommander = "Commander " + nameCommander;
                        }
                    }

                    return "Rename "
                            + nameCommander
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_SHIP: {
                    final Ship thisShip = shipManager.getByID(identifier[0]);
                    String nameShip;
                    if (thisShip == null) {
                        nameShip = "Ship";

                    } else {
                        nameShip = "'" + thisShip.getName() + "'";
                        if (nameShip.toLowerCase().indexOf("ship") < 1) {
                            nameShip = "Ship " + nameShip;
                        }
                    }

                    return "Rename "
                            + nameShip
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_ARMY: {
                    final Army thisArmy = armyManager.getByID(identifier[0]);
                    String name;
                    if (thisArmy == null) {
                        name = "Army";

                    } else {
                        name = "'" + thisArmy.getName() + "'";
                        if (name.toLowerCase().indexOf("army") < 1) {
                            name = "Army " + name;
                        }
                    }

                    return "Rename "
                            + name
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_CORP: {
                    final Corp thisCorps = corpManager.getByID(identifier[1]);
                    String name;
                    if (thisCorps == null) {
                        name = "Corps";

                    } else {
                        name = "'" + thisCorps.getName() + "'";
                        if (name.toLowerCase().indexOf("corp") < 1) {
                            name = "Corps " + name;
                        }
                    }

                    return "Rename "
                            + name
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_FLT: {
                    final Fleet thisFleet = fleetManager.getByID(identifier[0]);
                    String nameFleet;
                    if (thisFleet == null) {
                        nameFleet = "Fleet";

                    } else {
                        nameFleet = "'" + thisFleet.getName() + "'";
                        if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                            nameFleet = "Fleet " + nameFleet;
                        }
                    }

                    return "Rename "
                            + nameFleet
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_SPY: {
                    final Spy thisSpy = spyManager.getByID(identifier[0]);
                    String nameSpy;
                    if (thisSpy == null) {
                        nameSpy = "Spy";

                    } else {
                        nameSpy = "'" + thisSpy.getName() + "'";
                        if (nameSpy.toLowerCase().indexOf("spy") < 1) {
                            nameSpy = "Spy " + nameSpy;
                        }
                    }

                    return "Rename "
                            + nameSpy
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_BARRACK: {
                    final Barrack thisBarrack = barrackManager.getByID(identifier[0]);
                    String nameBarrack = "'" + thisBarrack.getName() + "'";
                    if (nameBarrack.toLowerCase().indexOf("barrack") < 1) {
                        nameBarrack = "Barrack " + nameBarrack;
                    }

                    return "Rename "
                            + nameBarrack
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_REN_BTRAIN: {
                    final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[0]);
                    String nameTrain;
                    if (thisTrain == null) {
                        nameTrain = "Baggage Train";

                    } else {
                        nameTrain = "'" + thisTrain.getName() + "'";
                        if (nameTrain.toLowerCase().indexOf("train") < 1) {
                            nameTrain = "Baggage Train " + nameTrain;
                        }
                    }

                    return "Rename "
                            + nameTrain
                            + " to "
                            + thisOrder.getParameter2();
                }

                case ORDER_HIRE_COM_COL:
                case ORDER_HIRE_COM: {
                    final Commander thisCommander = commanderManager.getByID(identifier[0]);
                    String nameCommander;
                    if (thisCommander == null) {
                        nameCommander = "Commander";

                    } else {
                        nameCommander = "'" + thisCommander.getName() + "'";
                        if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                            nameCommander = "Commander " + nameCommander;
                        }
                    }

                    return "Hire "
                            + nameCommander;
                }

                case ORDER_D_CORP: {
                    final Corp thisCorps = corpManager.getByID(identifier[0]);
                    String name;
                    if (thisCorps == null) {
                        name = "Corps";

                    } else {
                        name = "'" + thisCorps.getName() + "'";
                        if (name.toLowerCase().indexOf("corp") < 1) {
                            name = "Corps " + name;
                        }
                    }

                    return "Disband "
                            + name;
                }

                case ORDER_B_CORP: {
                    String name = "'" + thisOrder.getParameter3() + "'";
                    if (name.toLowerCase().indexOf("corp") < 1) {
                        name = "Corps " + name;
                    }

                    return "Form "
                            + name
                            + " at "
                            + getRegionCode(identifier[5])
                            + (identifier[3] + 1)
                            + "/"
                            + (identifier[4] + 1);
                }

                case ORDER_ADDTO_CORP: {
                    final Brigade thisBrigade = brigadeManager.getByID(identifier[0]);
                    String name;
                    if (thisBrigade == null) {
                        name = "Brigade";

                    } else {
                        name = "'" + thisBrigade.getName() + "'";

                        if (name.toLowerCase().indexOf("brigade") < 1) {
                            name = "Brigade " + name;
                        }
                    }

                    if (identifier[1] == 0) {
                        return "Remove "
                                + name
                                + " from Corps";

                    } else {
                        return "Add "
                                + name
                                + " to Corps";
                    }
                }

                case ORDER_CORP_COM: {
                    final Commander thisCommander = commanderManager.getByID(identifier[0]);
                    String nameCommander = "'" + thisCommander.getName() + "'";
                    if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                        nameCommander = "Commander " + nameCommander;
                    }

                    return "Assign  "
                            + nameCommander
                            + " to Corps";
                }

                case ORDER_D_ARMY: {
                    final Army thisArmy = armyManager.getByID(identifier[0]);
                    String name;
                    if (thisArmy == null) {
                        name = "Army";

                    } else {
                        name = "'" + thisArmy.getName() + "'";
                        if (name.toLowerCase().indexOf("army") < 1) {
                            name = "Army " + name;
                        }
                    }

                    return "Disband "
                            + name;
                }

                case ORDER_B_ARMY: {
                    String name = "'" + thisOrder.getParameter3() + "'";
                    if (name.toLowerCase().indexOf("army") < 1) {
                        name = "Army " + name;
                    }

                    return "Form "
                            + name
                            + " at "
                            + getRegionCode(identifier[5])
                            + (identifier[3] + 1)
                            + "/"
                            + (identifier[4] + 1);
                }

                case ORDER_ADDTO_ARMY: {
                    final Corp thisCorps = corpManager.getByID(identifier[0]);
                    String name;
                    if (thisCorps == null) {
                        name = "Corps";

                    } else {
                        name = "'" + thisCorps.getName() + "'";
                        if (name.toLowerCase().indexOf("corp") < 1) {
                            name = "Corps " + name;
                        }
                    }

                    if (identifier[1] == 0) {
                        return "Remove "
                                + name
                                + " from Army";

                    } else {
                        return "Add "
                                + name
                                + " to Army";
                    }
                }

                case ORDER_ARMY_COM: {
                    final Commander thisCommander = commanderManager.getByID(identifier[0]);
                    String nameCommander = "'" + thisCommander.getName() + "'";
                    if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                        nameCommander = "Commander " + nameCommander;
                    }

                    return "Assign "
                            + nameCommander
                            + " to Army";
                }

                case ORDER_LEAVE_COM: {
                    final Commander thisCommander = commanderManager.getByID(identifier[1]);
                    String nameCommander;
                    if (thisCommander == null) {
                        nameCommander = "Commander";

                    } else {
                        nameCommander = "'" + thisCommander.getName() + "'";
                        if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                            nameCommander = "Commander " + nameCommander;
                        }
                    }

                    return "Remove "
                            + nameCommander
                            + " from Army/Corps";
                }

                case ORDER_DISS_COM: {
                    final Commander thisCommander = commanderManager.getByID(identifier[0]);
                    String nameCommander = "'" + thisCommander.getName() + "'";
                    if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                        nameCommander = "Commander " + nameCommander;
                    }

                    return "Dismiss "
                            + nameCommander;
                }

                case ORDER_ADDTO_BRIGADE: {
                    final Brigade thisBrigade = brigadeManager.getByID(identifier[1]);
                    String name;
                    if (thisBrigade == null) {
                        name = "Brigade";

                    } else {
                        name = "'" + thisBrigade.getName() + "'";

                        if (name.toLowerCase().indexOf("brigade") < 1) {
                            name = "Brigade " + name;
                        }
                    }

                    return "Allocate Battalion "
                            + battalionManager.getByID(identifier[0]).getType().getName()
                            + " to "
                            + name;
                }

                case ORDER_D_FLT:
                    return "Demolish Fleet";

                case ORDER_B_FLT: {
                    final Fleet thisFleet = fleetManager.getByID(identifier[0]);
                    String nameFleet;
                    if (thisFleet == null) {
                        nameFleet = "Fleet";

                    } else {
                        nameFleet = "'" + thisFleet.getName() + "'";
                        if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                            nameFleet = "Fleet " + nameFleet;
                        }
                    }

                    return "Form "
                            + nameFleet
                            + " at "
                            + getRegionCode(identifier[5])
                            + (identifier[6] + 1)
                            + "/"
                            + (identifier[7] + 1);
                }

                case ORDER_ADDTO_FLT: {
                    final Ship thisShip = shipManager.getByID(identifier[0]);
                    String nameShip;
                    if (thisShip == null) {
                        nameShip = "Ship";

                    } else {
                        nameShip = "'" + thisShip.getName() + "'";
                        if (nameShip.toLowerCase().indexOf("ship") < 1) {
                            nameShip = "Ship " + nameShip;
                        }
                    }

                    return "Add "
                            + nameShip
                            + " to Fleet";
                }

                case ORDER_INC_POP:
                    return "Increase Population Density at "
                            + sectorManager.getByID(identifier[0]).getPosition().toString();

                case ORDER_DEC_POP:
                    return "Decrease Population Density at "
                            + sectorManager.getByID(identifier[0]).getPosition().toString();

                case ORDER_B_PRODS:
                    return "Build "
                            + productionSiteManager.getByID(identifier[1]).getName()
                            + " at "
                            + sectorManager.getByID(identifier[0]).getPosition().toString();

                case ORDER_D_PRODS:
                    return "Demolish site at "
                            + sectorManager.getByID(identifier[0]).getPosition().toString();

                case ORDER_MRG_BATT: {
                    final Battalion batt1 = battalionManager.getByID(identifier[0]);
                    final String type1;
                    if (batt1 == null) {
                        type1 = Integer.toString(identifier[0]);
                    } else {
                        type1 = batt1.getType().getName();
                    }

                    final Battalion batt2 = battalionManager.getByID(identifier[1]);
                    final String type2;
                    if (batt2 == null) {
                        type2 = Integer.toString(identifier[1]);
                    } else {
                        type2 = batt2.getType().getName();
                    }

                    return "Merge Battalion "
                            + type1
                            + " with Battalion "
                            + type2;
                }

                case ORDER_INC_HEADCNT: {
                    final Brigade thisBrigade = brigadeManager.getByID(identifier[0]);
                    String name;
                    if (thisBrigade == null) {
                        name = "Brigade";

                    } else {
                        name = "'" + thisBrigade.getName() + "'";

                        if (name.toLowerCase().indexOf("brigade") < 1) {
                            name = "Brigade " + name;
                        }
                    }

                    return "Increase Headcount of "
                            + name
                            + " by "
                            + identifier[1];
                }

                case ORDER_INC_EXP: {
                    final Brigade thisBrigade = brigadeManager.getByID(identifier[0]);
                    String name;
                    if (thisBrigade == null) {
                        name = "Brigade";

                    } else {
                        name = "'" + thisBrigade.getName() + "'";

                        if (name.toLowerCase().indexOf("brigade") < 1) {
                            name = "Brigade " + name;
                        }
                    }

                    return "Increase Experience of "
                            + name;
                }

                case ORDER_INC_HEADCNT_CORPS: {
                    final Corp thisCorps = corpManager.getByID(identifier[0]);
                    String name;
                    if (thisCorps == null) {
                        name = "Corps";

                    } else {
                        name = "'" + thisCorps.getName() + "'";
                        if (name.toLowerCase().indexOf("corp") < 1) {
                            name = "Corps " + name;
                        }
                    }

                    return "Increase Headcount of "
                            + name
                            + identifier[1];
                }

                case ORDER_INC_EXP_CORPS: {
                    final Corp thisCorps = corpManager.getByID(identifier[0]);
                    String name;
                    if (thisCorps == null) {
                        name = "Corps";

                    } else {
                        name = "'" + thisCorps.getName() + "'";
                        if (name.toLowerCase().indexOf("corp") < 1) {
                            name = "Corps " + name;
                        }
                    }

                    return "Increase Experience of "
                            + name;

                }

                case ORDER_INC_HEADCNT_ARMY: {
                    final Army thisArmy = armyManager.getByID(identifier[0]);
                    String name;
                    if (thisArmy == null) {
                        name = "Army";
                    } else {
                        name = "'" + thisArmy.getName() + "'";
                        if (name.toLowerCase().indexOf("army") < 1) {
                            name = "Army " + name;
                        }
                    }

                    return "Increase Headcount of "
                            + name
                            + identifier[1];
                }

                case ORDER_INC_EXP_ARMY: {
                    final Army thisArmy = armyManager.getByID(identifier[0]);
                    String name;
                    if (thisArmy == null) {
                        name = "Army";

                    } else {
                        name = "'" + thisArmy.getName() + "'";
                        if (name.toLowerCase().indexOf("army") < 1) {
                            name = "Army " + name;
                        }
                    }

                    return "Increase Experience of "
                            + name;
                }

                case ORDER_ADD_BATT: {
                    // Identify battalion types
                    final StringBuilder explanation = new StringBuilder();
                    explanation.append("Add&nbsp;<img src='http://static.eaw1805.com/images/armies/");
                    explanation.append(thisOrder.getNation().getId());
                    explanation.append("/");
                    explanation.append(identifier[1]);
                    explanation.append(".jpg' width=16 border=0 style='vertical-align: bottom;'>");
                    explanation.append("to Brigade");

                    return explanation.toString();
                }

                case ORDER_B_BATT_COL:
                case ORDER_B_BATT: {
                    String name = "'" + thisOrder.getParameter9() + "'";
                    if (name.toLowerCase().indexOf("brigade") < 1) {
                        name = "Brigade " + name;
                    }

                    // Identify battalion types
                    final StringBuilder explanation = new StringBuilder();
                    for (int slot = 1; slot < 7; slot++) {
                        if (identifier[slot] > 0) {
                            explanation.append("&nbsp;<img src='http://static.eaw1805.com/images/armies/");
                            explanation.append(thisOrder.getNation().getId());
                            explanation.append("/");
                            explanation.append(identifier[slot]);
                            explanation.append(".jpg' width=16 border=0 style='vertical-align: bottom;'>");
                        }
                    }

                    return "Setup "
                            + name
                            + " at "
                            + sectorManager.getByID(identifier[0]).getPosition().toString()
                            + "&nbsp;"
                            + explanation.toString();
                }

                case ORDER_B_BTRAIN:
                    return "Build new Baggage Train at"
                            + sectorManager.getByID(identifier[0]).getPosition().toString();

                case ORDER_B_SHIP: {
                    // Identify battalion types
                    final StringBuilder explanation = new StringBuilder();
                    explanation.append("Build&nbsp;<img src='http://static.eaw1805.com/images/ships/");
                    explanation.append(thisOrder.getNation().getId());
                    explanation.append("/");
                    explanation.append(identifier[1]);
                    explanation.append(".png' width=16 border=0 style='vertical-align: bottom;'>");
                    explanation.append(" '");
                    explanation.append(thisOrder.getParameter3());
                    explanation.append("' at ");
                    explanation.append(sectorManager.getByID(identifier[0]).getPosition().toString());

                    return explanation.toString();
                }

                case ORDER_R_SHP: {
                    final Ship thisShip = shipManager.getByID(identifier[0]);
                    String nameShip;
                    if (thisShip == null) {
                        nameShip = "Ship";

                    } else {
                        nameShip = "'" + thisShip.getName() + "'";
                        if (nameShip.toLowerCase().indexOf("ship") < 1) {
                            nameShip = "Ship " + nameShip;
                        }
                    }

                    return "Repair "
                            + nameShip;
                }

                case ORDER_R_BTRAIN: {
                    final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[0]);
                    String nameTrain;
                    if (thisTrain == null) {
                        nameTrain = "Baggage Train";

                    } else {
                        nameTrain = "'" + thisTrain.getName() + "'";
                        if (nameTrain.toLowerCase().indexOf("train") < 1) {
                            nameTrain = "Baggage Train " + nameTrain;
                        }
                    }

                    return "Repair "
                            + nameTrain;
                }

                case ORDER_R_FLT: {
                    final Fleet thisFleet = fleetManager.getByID(identifier[0]);
                    String nameFleet;
                    if (thisFleet == null) {
                        nameFleet = "Fleet";

                    } else {
                        nameFleet = "'" + thisFleet.getName() + "'";
                        if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                            nameFleet = "Fleet " + nameFleet;
                        }
                    }

                    return "Repair "
                            + nameFleet;
                }

                case ORDER_SCUTTLE_BTRAIN: {
                    final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[0]);
                    String nameTrain;
                    if (thisTrain == null) {
                        nameTrain = "Baggage Train";

                    } else {
                        nameTrain = "'" + thisTrain.getName() + "'";
                        if (nameTrain.toLowerCase().indexOf("train") < 1) {
                            nameTrain = "Baggage Train " + nameTrain;
                        }
                    }

                    return "Scuttle "
                            + nameTrain;
                }

                case ORDER_SCUTTLE_SHIP: {
                    final Ship thisShip = shipManager.getByID(identifier[0]);
                    String nameShip;
                    if (thisShip == null) {
                        nameShip = "Ship";

                    } else {
                        nameShip = "'" + thisShip.getName() + "'";
                        if (nameShip.toLowerCase().indexOf("ship") < 1) {
                            nameShip = "Ship " + nameShip;
                        }
                    }

                    return "Scuttle "
                            + nameShip;
                }

                case ORDER_D_BATT:
                    return "Disband Battalion ";


                case ORDER_D_BRIG: {
                    final Brigade thisBrigade = brigadeManager.getByID(identifier[0]);
                    String name;
                    if (thisBrigade == null) {
                        name = "Brigade";

                    } else {
                        name = "'" + thisBrigade.getName() + "'";

                        if (name.toLowerCase().indexOf("brigade") < 1) {
                            name = "Brigade " + name;
                        }
                    }

                    return "Disband "
                            + name;
                }

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS: {
                    final String cargoName;
                    final String transportName;
                    switch (identifier[0]) {
                        case BAGGAGETRAIN: {
                            final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[1]);
                            String nameTrain;
                            if (thisTrain == null) {
                                nameTrain = "Baggage Train";
                                transportName = nameTrain;
                            } else {
                                nameTrain = "'" + thisTrain.getName() + "'";
                                if (nameTrain.toLowerCase().indexOf("train") < 1) {
                                    nameTrain = "Baggage Train " + nameTrain;
                                }
                                transportName = nameTrain
                                        + " at "
                                        + thisTrain.getPosition().toString();
                            }


                            break;
                        }

                        case SHIP: {
                            final Ship thisShip = shipManager.getByID(identifier[1]);
                            String nameShip;
                            if (thisShip == null) {
                                nameShip = "Ship";
                                transportName = nameShip;
                            } else {
                                nameShip = "'" + thisShip.getName() + "'";
                                if (nameShip.toLowerCase().indexOf("ship") < 1) {
                                    nameShip = "Ship " + nameShip;
                                }
                                transportName = nameShip
                                        + " at "
                                        + thisShip.getPosition().toString();
                            }
                            break;
                        }

                        case FLEET: {
                            final Fleet thisFleet = fleetManager.getByID(identifier[1]);
                            String nameFleet;
                            if (thisFleet == null) {
                                nameFleet = "Fleet";
                                transportName = nameFleet;
                            } else {
                                nameFleet = "'" + thisFleet.getName() + "'";
                                if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                                    nameFleet = "Fleet " + nameFleet;
                                }
                                transportName = nameFleet
                                        + " at "
                                        + thisFleet.getPosition().toString();
                            }


                            break;
                        }

                        default:
                            transportName = "Transport Unit";
                    }

                    switch (identifier[2]) {
                        case BRIGADE: {
                            final Brigade thisBrigade = brigadeManager.getByID(identifier[3]);
                            String name;
                            if (thisBrigade == null) {
                                name = "Brigade";

                            } else {
                                name = "'" + thisBrigade.getName() + "'";

                                if (name.toLowerCase().indexOf("brigade") < 1) {
                                    name = "Brigade " + name;
                                }
                            }

                            cargoName = name;
                            break;
                        }

                        case COMMANDER: {
                            final Commander thisCommander = commanderManager.getByID(identifier[3]);
                            String nameCommander;
                            if (thisCommander == null) {
                                nameCommander = "Commander";

                            } else {
                                nameCommander = "'" + thisCommander.getName() + "'";
                                if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                                    nameCommander = "Commander " + nameCommander;
                                }
                            }

                            cargoName = nameCommander;
                            break;
                        }

                        case SPY: {
                            final Spy thisSpy = spyManager.getByID(identifier[3]);
                            String nameSpy;
                            if (thisSpy == null) {
                                nameSpy = "Spy";

                            } else {
                                nameSpy = "'" + thisSpy.getName() + "'";
                                if (nameSpy.toLowerCase().indexOf("spy") < 1) {
                                    nameSpy = "Spy " + nameSpy;
                                }
                            }

                            cargoName = nameSpy;
                            break;
                        }

                        default:
                            cargoName = "Cargo Unit";
                    }
                    return "Load " + cargoName + " on " + transportName;
                }
                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    final String cargoName;
                    final String transportName;
                    switch (identifier[0]) {
                        case BAGGAGETRAIN: {
                            final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[1]);
                            String nameTrain;
                            if (thisTrain == null) {
                                nameTrain = "Baggage Train";

                            } else {
                                nameTrain = "'" + thisTrain.getName() + "'";
                                if (nameTrain.toLowerCase().indexOf("train") < 1) {
                                    nameTrain = "Baggage Train " + nameTrain;
                                }
                            }

                            transportName = nameTrain
                                    + " towards "
                                    + getDirection(identifier[4]);
                            break;
                        }

                        case SHIP: {
                            final Ship thisShip = shipManager.getByID(identifier[1]);
                            String nameShip;
                            if (thisShip == null) {
                                nameShip = "Ship";

                            } else {
                                nameShip = "'" + thisShip.getName() + "'";
                                if (nameShip.toLowerCase().indexOf("ship") < 1) {
                                    nameShip = "Ship " + nameShip;
                                }
                            }

                            transportName = nameShip
                                    + " towards "
                                    + getDirection(identifier[4]);
                            break;
                        }

                        case FLEET: {
                            final Fleet thisFleet = fleetManager.getByID(identifier[1]);
                            String nameFleet;
                            if (thisFleet == null) {
                                nameFleet = "Fleet";

                            } else {
                                nameFleet = "'" + thisFleet.getName() + "'";
                                if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                                    nameFleet = "Fleet " + nameFleet;
                                }
                            }

                            transportName = nameFleet
                                    + " towards "
                                    + getDirection(identifier[4]);
                            break;
                        }

                        default:
                            transportName = "Transport Unit";
                    }

                    switch (identifier[2]) {
                        case BRIGADE: {
                            final Brigade thisBrigade = brigadeManager.getByID(identifier[3]);
                            String name;
                            if (thisBrigade == null) {
                                name = "Brigade";

                            } else {
                                name = "'" + thisBrigade.getName() + "'";

                                if (name.toLowerCase().indexOf("brigade") < 1) {
                                    name = "Brigade " + name;
                                }
                            }

                            cargoName = name;
                            break;
                        }

                        case COMMANDER: {
                            final Commander thisCommander = commanderManager.getByID(identifier[3]);
                            String nameCommander;
                            if (thisCommander == null) {
                                nameCommander = "Commander";

                            } else {
                                nameCommander = "'" + thisCommander.getName() + "'";
                                if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                                    nameCommander = "Commander " + nameCommander;
                                }
                            }

                            cargoName = nameCommander;
                            break;
                        }

                        case SPY: {
                            final Spy thisSpy = spyManager.getByID(identifier[3]);
                            String nameSpy;
                            if (thisSpy == null) {
                                nameSpy = "Spy";

                            } else {
                                nameSpy = "'" + thisSpy.getName() + "'";
                                if (nameSpy.toLowerCase().indexOf("spy") < 1) {
                                    nameSpy = "Spy " + nameSpy;
                                }
                            }

                            cargoName = nameSpy;
                            break;
                        }

                        default:
                            cargoName = "Cargo Unit";
                    }

                    return "Unload " + cargoName + " from " + transportName;

                case ORDER_EXCHF:
                case ORDER_EXCHS:

                    String tradeUnit1 = "";
                    String tradeUnit2 = "";
                    switch (identifier[0]) {
                        case BAGGAGETRAIN: {
                            final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[1]);
                            String nameTrain;
                            if (thisTrain == null) {
                                nameTrain = "Baggage Train";

                            } else {
                                nameTrain = "'" + thisTrain.getName() + "'";
                                if (nameTrain.toLowerCase().indexOf("train") < 1) {
                                    nameTrain = "Baggage Train " + nameTrain;
                                }
                            }

                            tradeUnit1 += " " + nameTrain;
                            break;
                        }

                        case SHIP: {
                            final Ship thisShip = shipManager.getByID(identifier[1]);
                            String nameShip;
                            if (thisShip == null) {
                                nameShip = "Ship";

                            } else {
                                nameShip = "'" + thisShip.getName() + "'";
                                if (nameShip.toLowerCase().indexOf("ship") < 1) {
                                    nameShip = "Ship " + nameShip;
                                }
                            }

                            tradeUnit1 += " " + nameShip;
                            break;
                        }

                        case TRADECITY:
                            tradeUnit1 += " "
                                    + tradeCityManager.getByID(identifier[1]).getName();
                            break;

                        case WAREHOUSE:
                            tradeUnit1 += " "
                                    + getWarehouseName(warehouseManager.getByID(identifier[1]));
                            break;

                        default:
                            tradeUnit1 = "Trade Unit";
                    }

                    switch (identifier[2]) {
                        case BAGGAGETRAIN: {
                            final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[3]);
                            if (thisTrain == null) {
                                tradeUnit2 += " Baggage Train";

                            } else {
                                String nameTrain = "'" + thisTrain.getName() + "'";
                                if (nameTrain.toLowerCase().indexOf("train") < 1) {
                                    nameTrain = "Baggage Train " + nameTrain;
                                }

                                tradeUnit2 += " "
                                        + nameTrain
                                        + " at "
                                        + thisTrain.getPosition().toString();
                            }
                            break;
                        }

                        case SHIP: {
                            final Ship thisShip = shipManager.getByID(identifier[3]);
                            if (thisShip == null) {
                                tradeUnit2 += " Ship";

                            } else {
                                String nameShip = "'" + thisShip.getName() + "'";
                                if (nameShip.toLowerCase().indexOf("ship") < 1) {
                                    nameShip = "Ship " + nameShip;
                                }

                                tradeUnit2 += " "
                                        + nameShip
                                        + " at "
                                        + thisShip.getPosition().toString();
                            }

                            break;
                        }

                        case TRADECITY:
                            tradeUnit2 += " "
                                    + tradeCityManager.getByID(identifier[3]).getName()
                                    + " at "
                                    + tradeCityManager.getByID(identifier[3]).getPosition().toString();
                            break;

                        case WAREHOUSE:
                            tradeUnit2 += " "
                                    + getWarehouseName(warehouseManager.getByID(identifier[3]));
                            break;

                        default:
                            tradeUnit2 = "Trade Unit";
                    }

                    final Good thisGood = goodManager.getByID(identifier[4]);
                    return "Transfer from " + tradeUnit1 + " to " + tradeUnit2 + " " + identifier[5]
                            + " "
                            + "<img width=16 style='padding-top: 1px; vertical-align: bottom;' "
                            + " title='"
                            + thisGood.getName()
                            + "' "
                            + " src='http://static.eaw1805.com/images/goods/good-" + identifier[4] + ".png'>";

                case ORDER_M_UNIT:
                    final String movePath = getMovementPath(thisOrder);

                    switch (identifier[4]) {
                        case ORDER_M_BRIG: {
                            final Brigade thisBrigade = brigadeManager.getByID(identifier[1]);
                            String name;
                            if (thisBrigade == null) {
                                name = "Brigade";

                            } else {
                                name = "'" + thisBrigade.getName() + "'";
                                if (name.toLowerCase().indexOf("brigade") < 1) {
                                    name = "Brigade " + name;
                                }
                            }

                            // check if this is a patrol move
                            final String engage;
                            if (identifier[3] == 1) {
                                engage = "Engage & ";
                            } else {
                                engage = "";
                            }

                            return engage + "Move "
                                    + name
                                    + " "
                                    + movePath;
                        }

                        case ORDER_FM_BRIG: {
                            final Brigade thisBrigade = brigadeManager.getByID(identifier[1]);
                            String name;
                            if (thisBrigade == null) {
                                name = "Brigade";

                            } else {
                                name = "'" + thisBrigade.getName() + "'";
                                if (name.toLowerCase().indexOf("brigade") < 1) {
                                    name = "Brigade " + name;
                                }
                            }

                            // check if this is a patrol move
                            final String engage;
                            if (identifier[3] == 1) {
                                engage = "Engage & ";
                            } else {
                                engage = "";
                            }

                            return engage + "Force March "
                                    + name
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_CORP: {
                            final Corp thisCorps = corpManager.getByID(identifier[1]);
                            String name;
                            if (thisCorps == null) {
                                name = "Corps";

                            } else {
                                name = "'" + thisCorps.getName() + "'";
                                if (name.toLowerCase().indexOf("corp") < 1) {
                                    name = "Corps " + name;
                                }
                            }

                            // check if this is a patrol move
                            final String engage;
                            if (identifier[3] == 1) {
                                engage = "Engage & ";
                            } else {
                                engage = "";
                            }

                            return engage + "Move "
                                    + name
                                    + " "
                                    + movePath;
                        }

                        case ORDER_FM_CORP: {
                            final Corp thisCorps = corpManager.getByID(identifier[1]);
                            String name;
                            if (thisCorps == null) {
                                name = "Corps";

                            } else {
                                name = "'" + thisCorps.getName() + "'";
                                if (name.toLowerCase().indexOf("corp") < 1) {
                                    name = "Corps " + name;
                                }
                            }

                            // check if this is a patrol move
                            final String engage;
                            if (identifier[3] == 1) {
                                engage = "Engage & ";
                            } else {
                                engage = "";
                            }

                            return engage + "Force March "
                                    + name
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_ARMY: {
                            final Army thisArmy = armyManager.getByID(identifier[1]);
                            String name;
                            if (thisArmy == null) {
                                name = "Army";

                            } else {
                                name = "'" + thisArmy.getName() + "'";
                                if (name.toLowerCase().indexOf("army") < 1) {
                                    name = "Army " + name;
                                }
                            }

                            // check if this is a patrol move
                            final String engage;
                            if (identifier[3] == 1) {
                                engage = "Engage & ";
                            } else {
                                engage = "";
                            }

                            return engage + "Move "
                                    + name
                                    + " "
                                    + movePath;
                        }

                        case ORDER_FM_ARMY: {
                            final Army thisArmy = armyManager.getByID(identifier[1]);
                            String name;
                            if (thisArmy == null) {
                                name = "Army";

                            } else {
                                name = "'" + thisArmy.getName() + "'";
                                if (name.toLowerCase().indexOf("army") < 1) {
                                    name = "Army " + name;
                                }
                            }

                            // check if this is a patrol move
                            final String engage;
                            if (identifier[3] == 1) {
                                engage = "Engage & ";
                            } else {
                                engage = "";
                            }

                            return engage + "Force March "
                                    + name
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_COMM: {
                            final Commander thisCommander = commanderManager.getByID(identifier[1]);
                            String nameCommander;
                            if (thisCommander == null) {
                                nameCommander = "Commander";

                            } else {
                                nameCommander = "'" + thisCommander.getName() + "'";
                                if (nameCommander.toLowerCase().indexOf("commander") < 1) {
                                    nameCommander = "Commander " + nameCommander;
                                }
                            }

                            return "Move "
                                    + nameCommander
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_SPY: {
                            final Spy thisSpy = spyManager.getByID(identifier[1]);
                            String nameSpy;
                            if (thisSpy == null) {
                                nameSpy = "Spy";

                            } else {
                                nameSpy = "'" + thisSpy.getName() + "'";
                                if (nameSpy.toLowerCase().indexOf("spy") < 1) {
                                    nameSpy = "Spy " + nameSpy;
                                }
                            }

                            return "Move "
                                    + nameSpy
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_BTRAIN: {
                            final BaggageTrain thisTrain = baggageTrainManager.getByID(identifier[1]);
                            String nameTrain;
                            if (thisTrain == null) {
                                nameTrain = "Baggage Train";

                            } else {
                                nameTrain = "'" + thisTrain.getName() + "'";
                                if (nameTrain.toLowerCase().indexOf("train") < 1) {
                                    nameTrain = "Baggage Train " + nameTrain;
                                }
                            }

                            return "Move "
                                    + nameTrain
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_MSHIP:
                        case ORDER_M_SHIP: {
                            final Ship thisShip = shipManager.getByID(identifier[1]);
                            String nameShip;
                            if (thisShip == null) {
                                nameShip = "Ship";

                            } else {
                                nameShip = "'" + thisShip.getName() + "'";
                                if (nameShip.toLowerCase().indexOf("ship") < 1) {
                                    nameShip = "Ship " + nameShip;
                                }
                            }

                            return "Move "
                                    + nameShip
                                    + " "
                                    + movePath;
                        }

                        case ORDER_M_FLEET: {
                            final Fleet thisFleet = fleetManager.getByID(identifier[1]);
                            String nameFleet;
                            if (thisFleet == null) {
                                nameFleet = "Fleet";

                            } else {
                                nameFleet = "'" + thisFleet.getName() + "'";
                                if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                                    nameFleet = "Fleet " + nameFleet;
                                }
                            }

                            return "Move "
                                    + nameFleet
                                    + " "
                                    + movePath;
                        }

                        case ORDER_P_SHIP: {
                            final Ship thisShip = shipManager.getByID(identifier[1]);
                            String nameShip;
                            if (thisShip == null) {
                                nameShip = "Ship";

                            } else {
                                nameShip = "'" + thisShip.getName() + "'";
                                if (nameShip.toLowerCase().indexOf("ship") < 1) {
                                    nameShip = "Ship " + nameShip;
                                }
                            }

                            return "Patrol "
                                    + nameShip
                                    + " "
                                    + movePath;
                        }

                        case ORDER_P_FLEET: {
                            final Fleet thisFleet = fleetManager.getByID(identifier[1]);
                            String nameFleet;
                            if (thisFleet == null) {
                                nameFleet = "Fleet";

                            } else {
                                nameFleet = "'" + thisFleet.getName() + "'";
                                if (nameFleet.toLowerCase().indexOf("fleet") < 1) {
                                    nameFleet = "Fleet " + nameFleet;
                                }
                            }

                            return "Patrol "
                                    + nameFleet
                                    + " "
                                    + movePath;
                        }

                        default:
                            return "Error retrieving movement order description (" + identifier[2] + ")";
                    }


                case ORDER_HOVER_SEC:
                    return "Hand over "
                            + sectorManager.getByID(identifier[0]).getPosition().toString()
                            + " to "
                            + getNationManager().getByID(identifier[1]).getName();

//                            + DataStore.getInstance().getNationById(
//                            Integer.parseInt(RegionStore.getInstance().getSectorOrderMap().get(identifier[0]).getParameter2())
//                    ).getName();

                case ORDER_HOVER_SHIP: {
                    final Ship thisShip = shipManager.getByID(identifier[0]);
                    String nameShip;
                    if (thisShip == null) {
                        nameShip = "Ship";

                    } else {
                        nameShip = "'" + thisShip.getName() + "'";
                        if (nameShip.toLowerCase().indexOf("ship") < 1) {
                            nameShip = "Ship " + nameShip;
                        }
                    }

                    return "Hand over "
                            + nameShip
                            + " to "
                            + getNationManager().getByID(identifier[1]).getName();
                }

                case ORDER_TAXATION:
                    switch (identifier[0]) {
                        case TAX_HARSH:
                            return "Harsh taxation";

                        case TAX_LOW:
                            return "Low taxation";

                        default:
                            final StringBuilder explanation = new StringBuilder();
                            if (identifier[1] == 1 && identifier[2] == 1) {
                                explanation.append("Use Colonial Goods & Gems to Boost Income. ");

                            } else if (identifier[1] == 1) {
                                explanation.append("Use Colonial Goods to Boost Income. ");
                            }

                            if (identifier[3] == 1) {
                                explanation.append("Use Industrial Points to gain 1 VP. ");
                            }

                            if (identifier[4] == 1) {
                                explanation.append("Use Money to gain 1 VP. ");
                            }

                            if (identifier[5] == 1) {
                                explanation.append("Use Colonials Goods to gain 1 VP. ");
                            }

                            return explanation.toString();
                    }

                case ORDER_POLITICS:
                    if (identifier[1] == REL_WAR) {
                        switch (identifier[2]) {
                            case NO_ACTION:
                                return "Change political relations towards "
                                        + getNationManager().getByID(identifier[0]).getName()
                                        + " to "
                                        + getNameRelation(identifier[1]);

                            case ACCEPT_SURR:
                                return "Accept surrender from "
                                        + getNationManager().getByID(identifier[0]).getName();

                            case OFFER_SURR:
                                return "Offer surrender to "
                                        + getNationManager().getByID(identifier[0]).getName();

                            case MAKE_PEACE:
                                return "Offer peace to "
                                        + getNationManager().getByID(identifier[0]).getName();

                            default:
                                return "Change political relations towards "
                                        + getNationManager().getByID(identifier[0]).getName()
                                        + " to "
                                        + getNameRelation(identifier[1]);
                        }

                    } else {
                        return "Change political relations towards "
                                + getNationManager().getByID(identifier[0]).getName()
                                + " to "
                                + getNameRelation(identifier[1]);
                    }

                default:
                    return thisOrder.getType() + " : pending description";
            }

        } catch (Exception e) {
            LOGGER.fatal(e);

        }

        return thisOrder.getType() + ": Error getting description";
    }

    /**
     * Describes the direction of unload.
     *
     * @param direction the target direction.
     * @return the Description.
     */
    protected String getDirection(final int direction) {
        switch (direction) {
            case NORTH:
                return "North";

            case SOUTH:
                return "South";

            case EAST:
                return "East";

            case WEST:
                return "West";

            default:
                return "current sector";
        }
    }

    private String getNameRelation(final int relation) {
        switch (relation) {
            case REL_ALLIANCE:
                return "Alliance";

            case REL_PASSAGE:
                return "Right of passage";

            case REL_TRADE:
                return "Trading";

            case REL_COLONIAL_WAR:
                return "Colonial War";

            case REL_WAR:
                return "War";

            default:
                break;
        }
        return "unknown";
    }

    private String getWarehouseName(final Warehouse thisWarehouse) {
        switch (thisWarehouse.getRegion().getId()) {
            case EUROPE:
                return "European Warehouse";

            case CARIBBEAN:
                return "Caribbean Warehouse";

            case INDIES:
                return "Indies Warehouse";

            case AFRICA:
                return "African Warehouse";

            default:
                return "";
        }
    }

    private String getRegionCode(final int thisRegionId) {
        switch (thisRegionId) {
            case EUROPE:
                return "E";

            case CARIBBEAN:
                return "C";

            case INDIES:
                return "I";

            case AFRICA:
                return "A";

            default:
                return "";
        }
    }

    /**
     * Extract from the order the parameters related to the movement path.
     *
     * @param thisOrder the player order to examine.
     * @return the concatenated parameters values.
     */
    private String getMovementPath(final PlayerOrder thisOrder) {
        final String movePath[] = new String[5];
        movePath[0] = thisOrder.getParameter3();
        movePath[1] = thisOrder.getParameter4();
        movePath[2] = thisOrder.getParameter5();
        movePath[3] = thisOrder.getParameter6();
        movePath[4] = thisOrder.getParameter7();

        // Concatenate each movement path
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String thisPath : movePath) {
            if (thisPath == null) {
                // reached end of path
                break;
            } else {
                final String newPath = thisPath.replaceAll("!", "-");
                if (newPath.contains(":")) {
                    stringBuilder.append(newPath.replaceAll("--", "-"));
                }
            }
        }

        final StringTokenizer stokSectors = new StringTokenizer(stringBuilder.toString().replaceAll("!", "-"), "-");
        final StringBuilder initPath = new StringBuilder();

        // skip first token
        if (stokSectors.hasMoreElements()) {
            initPath.append(" from ");
            final StringTokenizer stokCoords = new StringTokenizer(stokSectors.nextToken(), ":");
            final String coordX = stokCoords.nextToken();
            final String coordY = stokCoords.nextToken();

            initPath.append(Integer.parseInt(coordX) + 1);
            initPath.append("/");
            initPath.append(Integer.parseInt(coordY) + 1);
            initPath.append(", ");
        }

        final StringBuilder finalPath = new StringBuilder();
        int totSectors = 0;
        finalPath.append(" sectors (");

        int prevCoordX = 0;
        int prevCoordY = 0;

        // iterate through all other tokens
        while (stokSectors.hasMoreElements()) {
            final StringTokenizer stokCoords = new StringTokenizer(stokSectors.nextToken(), ":");
            final String coordX = stokCoords.nextToken();
            final String coordY = stokCoords.nextToken();

            final int thisCoordX = Integer.parseInt(coordX) + 1;
            final int thisCoordY = Integer.parseInt(coordY) + 1;

            if (thisCoordX != prevCoordX || thisCoordY != prevCoordY) {
                finalPath.append(thisCoordX);
                finalPath.append("/");
                finalPath.append(thisCoordY);
                finalPath.append(" ");

                prevCoordX = thisCoordX;
                prevCoordY = thisCoordY;

                totSectors++;
            }
        }

        if (totSectors > 0) {
            finalPath.replace(finalPath.length() - 1, finalPath.length(), ")");

        } else {
            finalPath.delete(finalPath.length() - 1, finalPath.length());
        }

        return initPath.toString() + totSectors + finalPath.toString();
    }


}
