package com.eaw1805.www.client;

import com.eaw1805.www.client.asyncs.AlliedUnitsAsyncCallback;
import com.eaw1805.www.client.asyncs.ForeignUnitsAsyncCallback;
import com.eaw1805.www.client.asyncs.MovemenentOrdersAsyncCallback;
import com.eaw1805.www.client.asyncs.OrdersAsyncCallback;
import com.eaw1805.www.client.asyncs.army.ArmyAsyncCallback;
import com.eaw1805.www.client.asyncs.army.ArmyNewAsyncCallback;
import com.eaw1805.www.client.asyncs.army.SpyAsyncCallback;
import com.eaw1805.www.client.asyncs.economy.BaggageAsyncCallback;
import com.eaw1805.www.client.asyncs.economy.NewBaggageAsyncCallback;
import com.eaw1805.www.client.asyncs.economy.TaxationAsyncCallback;
import com.eaw1805.www.client.asyncs.economy.TradeCitiesAsyncCallback;
import com.eaw1805.www.client.asyncs.map.SectorChangesAsyncCallback;
import com.eaw1805.www.client.asyncs.navy.ShipAsyncCallback;
import com.eaw1805.www.client.asyncs.navy.ShipNewAsyncCallback;
import com.eaw1805.www.client.events.loading.*;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.InfoPanelsStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

public class ApplicationManager {
    /**
     * Boolean variables indicating whether the items are loaded
     * from the database
     */
    private boolean loadedArmies = false, loadedNavy = false, loadedSpies = false,
            loadedBtrains = false, loadedCommanders = false, loadedAllied = false,
            allLoaded = false;
    /**
     * Boolean variables indicating whether the application parameters
     * are initialized
     */
    private boolean initArmies = false, initOrders = false, initRelations = false, initMapUnits = false, initAll = false;

    /**
     * Asynchronous call-backs used to get data
     */
    private final AlliedUnitsAsyncCallback alliednitsAsync = new AlliedUnitsAsyncCallback();
    private final ForeignUnitsAsyncCallback foreignUnitsAsync = new ForeignUnitsAsyncCallback();
    private final ShipAsyncCallback shipAsync = new ShipAsyncCallback();
    private final ArmyAsyncCallback armyAsync = new ArmyAsyncCallback();
    private final SectorChangesAsyncCallback sectorChAsync = new SectorChangesAsyncCallback();
    private final ArmyNewAsyncCallback newBrigsAsync = new ArmyNewAsyncCallback();
    private final ShipNewAsyncCallback newShipsAsync = new ShipNewAsyncCallback();
    private final TaxationAsyncCallback newTaxAsync = new TaxationAsyncCallback();
    private final SpyAsyncCallback spiesAsync = new SpyAsyncCallback();
    private final BaggageAsyncCallback baggageAsync = new BaggageAsyncCallback();
    private final OrdersAsyncCallback orderAsync = new OrdersAsyncCallback();
    private final TradeCitiesAsyncCallback tradeAsync = new TradeCitiesAsyncCallback();
    private final NewBaggageAsyncCallback newBtrainAsync = new NewBaggageAsyncCallback();
    private final MovemenentOrdersAsyncCallback moveAsync = new MovemenentOrdersAsyncCallback();

    private GameStore gStore = GameStore.getInstance();

    private final EmpireRpcServiceAsync empireService;

    public ApplicationManager(final EmpireRpcServiceAsync empireservice) {
        super();
        initStores();
        this.empireService = empireservice;

        // When all sectors have been loaded and added to the vectors in the map
        // region asynchronous callback and the base map is constructed in the MapsView then the latter
        // fires the event that gets the rest of the data
        LoadEventManager.addBaseMapConstructedHandler(new BaseMapConstructedHandler() {
            public void onBaseMapConstructed(final BaseMapConstructedEvent e) {
                final int scenarioId = gStore.getScenarioId();
                final int nationId = gStore.getNationId();
                final int gameId = gStore.getGameId();
                final int turn = gStore.getTurn();
                // Get all unit data
                empireService.getAlliedUnits(scenarioId, nationId, gameId, turn, alliednitsAsync);
                empireService.getForeignUnitsOnNationTerritory(scenarioId, nationId, gameId, turn, foreignUnitsAsync);
                empireService.getFleetsByNation(scenarioId, nationId, gameId, turn, shipAsync);
                empireService.getArmiesByNation(scenarioId, nationId, gameId, turn, armyAsync);
                empireService.getSpies(scenarioId, nationId, gameId, turn, true, spiesAsync);
                empireService.getBaggageTrains(scenarioId, nationId, gameId, turn, false, nationId, true, baggageAsync);
                empireService.getTradeCities(scenarioId, nationId, gameId, turn, tradeAsync);

                // Get all orders data
                empireService.getRegionOrdersByGameNationAndTurn(scenarioId, nationId, gameId, turn, sectorChAsync);
                empireService.getNewBrigadeOrdersByGameNationAndTurn(scenarioId, nationId, gameId, turn, newBrigsAsync);
                empireService.getNewShipOrdersByGameNationAndTurn(scenarioId, nationId, gameId, turn, newShipsAsync);
                empireService.getClientOrders(scenarioId, nationId, gameId, turn, orderAsync);
                empireService.getNewBaggageTrainOrdersByGameNationAndTurn(scenarioId, nationId, gameId, turn, newBtrainAsync);
                empireService.getTaxationByNationAndGame(scenarioId, nationId, gameId, turn, newTaxAsync);
                empireService.getMovementOrdersByGameNationAndTurn(scenarioId, nationId, gameId, turn, moveAsync);
            }
        });

        LoadEventManager.addRelationsLoadedHandler(new RelationsLoadedHandler() {

            public void onRelationsLoadedEvent(final RelationsLoadedEvent e) {
                initRelations = true;
                checkAppInit();

            }
        });

        LoadEventManager.addMapUnitsLoadedHandler(new MapUnitsLoadedHandler() {

            public void onMapUnitsLoadedEvent(final MapUnitsLoadedEvent e) {
                initMapUnits = true;
                checkAppInit();

            }
        });

        LoadEventManager.addArmiesInitHandler(new ArmiesInitdHandler() {
            public void onArmiesInit(final ArmiesInitEvent e) {
                initArmies = true;
                checkAppInit();
            }
        });

        LoadEventManager.addOrdersLoadedHandler(new OrdersLoadedHandler() {
            public void onOrdersLoaded(final OrdersLoadedEvent event) {
                initOrders = true;
                checkAppInit();

            }

        });

        LoadEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
            public void onArmiesLoaded(final ArmiesLoadedEvent event) {
                loadedArmies = true;
                checkIsUnitsLoadedAndInform();
            }
        });

        LoadEventManager.addNavyLoadedHandler(new NavyLoadedHandler() {
            public void onNavyLoaded(final NavyLoadedEvent event) {
                loadedNavy = true;
                checkIsUnitsLoadedAndInform();

            }
        });

        LoadEventManager.addSpiesLoadedHandler(new SpiesLoadedHandler() {
            public void onSpiesLoaded(final SpiesLoadedEvent event) {
                loadedSpies = true;
                checkIsUnitsLoadedAndInform();
            }
        });

        LoadEventManager.addCommLoadeddHandler(new CommLoadedHandler() {
            public void onCommLoaded(final CommLoadedEvent event) {
                loadedCommanders = true;
                checkIsUnitsLoadedAndInform();
            }
        });

        LoadEventManager.addBtrainLoadedHandler(new BtrainLoadedHandler() {
            public void onBtrainLoaded(final BtrainLoadedEvent event) {
                loadedBtrains = true;
                checkIsUnitsLoadedAndInform();
            }
        });

        LoadEventManager.addAlliedUnitsLoadedHandler(new AlliedUnitsLoadedHandler() {
            public void onAlliedUnitsLoaded(final AlliedUnitsLoadedEvent event) {
                loadedAllied = true;
                checkIsUnitsLoadedAndInform();
            }
        });
    }

    private void initStores() {
        InfoPanelsStore.getInstance();
        ArmyStore.getInstance();
        NavyStore.getInstance();
        SpyStore.getInstance();
        CommanderStore.getInstance();
        AlliedUnitsStore.getInstance();
    }

    /**
     * Method that checks if all the units are loaded from the
     * database
     *
     * @return true if all units are loaded else return false
     */
    public boolean checkIsUnitsLoadedAndInform() {
        if (loadedArmies && loadedNavy && loadedSpies && loadedBtrains && loadedCommanders && loadedAllied) {
            if (!allLoaded) {
                LoadEventManager.loadAllUnits();
            }
            allLoaded = true;
            return allLoaded;

        } else {
            return false;
        }
    }

    /**
     * Method that checks if all the applications is initialized
     * database
     *
     * @return true if all units are loaded else return false
     */
    private boolean checkAppInit() {
        if (initArmies && initOrders && initRelations && initMapUnits) {
            if (!initAll) {
                try {
                    LoadEventManager.initApp();
                } catch (Exception e) {
                    new ErrorPopup(ErrorPopup.Level.ERROR, "Errors initializing assets", false);
                }
            }
            initAll = true;
            return initAll;

        } else {
            return false;
        }
    }

}
