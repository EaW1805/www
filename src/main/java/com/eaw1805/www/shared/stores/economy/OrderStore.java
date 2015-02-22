package empire.webapp.shared.stores.economy;

import com.google.gwt.user.client.Window;
import empire.data.constants.ArmyConstants;
import empire.data.constants.GoodConstants;
import empire.data.constants.NavigationConstants;
import empire.data.constants.OrderConstants;
import empire.data.constants.RegionConstants;
import empire.data.constants.RelationConstants;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.ClientOrderDTO;
import empire.data.dto.web.OrderCostDTO;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BarrackDTO;
import empire.data.dto.web.army.BattalionDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.data.dto.web.army.SpyDTO;
import empire.data.dto.web.economy.BaggageTrainDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.orders.army.*;
import empire.orders.economy.BuildBaggageTrain;
import empire.orders.economy.ChangeTaxation;
import empire.orders.economy.RepairBaggageTrain;
import empire.orders.economy.ScuttleBaggageTrain;
import empire.orders.economy.TransferFirst;
import empire.orders.economy.TransferSecond;
import empire.orders.fleet.BuildShip;
import empire.orders.fleet.DemolishFleet;
import empire.orders.fleet.HandOverShip;
import empire.orders.fleet.LoadFirst;
import empire.orders.fleet.LoadSecond;
import empire.orders.fleet.RenameShip;
import empire.orders.fleet.RepairFleet;
import empire.orders.fleet.RepairShip;
import empire.orders.fleet.ScuttleShip;
import empire.orders.fleet.SetupFleet;
import empire.orders.fleet.ShipJoinFleet;
import empire.orders.fleet.UnloadFirst;
import empire.orders.fleet.UnloadSecond;
import empire.orders.map.BuildProductionSite;
import empire.orders.map.DecreasePopDensity;
import empire.orders.map.DemolishProductionSite;
import empire.orders.map.HandOverTerritoryOrderProcessor;
import empire.orders.map.IncreasePopDensity;
import empire.orders.movement.ArmyForcedMovement;
import empire.orders.movement.ArmyMovement;
import empire.orders.movement.BaggageTrainMovement;
import empire.orders.movement.BrigadeForcedMovement;
import empire.orders.movement.BrigadeMovement;
import empire.orders.movement.CommanderMovement;
import empire.orders.movement.CorpForcedMovement;
import empire.orders.movement.CorpMovement;
import empire.orders.movement.FleetMovement;
import empire.orders.movement.FleetPatrolMovement;
import empire.orders.movement.MerchantShipMovement;
import empire.orders.movement.ShipPatrolMovement;
import empire.orders.movement.SpyMovement;
import empire.orders.movement.WarShipMovement;
import empire.orders.politics.PoliticsOrderProcessor;
import empire.webapp.client.events.economy.EcoEventManager;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.stores.DataStore;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.MovementStore;
import empire.webapp.shared.stores.RegionStore;
import empire.webapp.shared.stores.RelationsStore;
import empire.webapp.shared.stores.units.AlliedUnitsStore;
import empire.webapp.shared.stores.units.ArmyStore;
import empire.webapp.shared.stores.units.BaggageTrainStore;
import empire.webapp.shared.stores.units.CommanderStore;
import empire.webapp.shared.stores.units.NavyStore;
import empire.webapp.shared.stores.units.SpyStore;
import empire.webapp.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("restriction")
public final class OrderStore
        implements OrderConstants, GoodConstants, ArmyConstants, RegionConstants, RelationConstants, NavigationConstants {


    public static final Map<Integer, String> PHASE_NAMES = new HashMap<Integer, String>();

    static {
        PHASE_NAMES.put(10, "Organize Land & Naval Forces");
        PHASE_NAMES.put(20, "Organize Territories & Population");
        PHASE_NAMES.put(30, "Troop Training at Barracks");
        PHASE_NAMES.put(40, "Build Baggage Trains");
        PHASE_NAMES.put(50, "Constructions at Shipyards");
        PHASE_NAMES.put(60, "Repair Ships & Baggage Trains");
        PHASE_NAMES.put(70, "Scuttle & Disband");
        PHASE_NAMES.put(80, "1st Loading/Unloading & Trading Phase");
        PHASE_NAMES.put(90, "Movement");
        PHASE_NAMES.put(100, "2nd Loading/Unloading & Trading Phase");
        PHASE_NAMES.put(110, "Hand-over Ships & Cede Territories");
        PHASE_NAMES.put(120, "Politics");
        PHASE_NAMES.put(130, "Taxation");
        PHASE_NAMES.put(150, "Other Orders");
    }

    public static final Map<Integer, Integer> ORDER_PHASE = new HashMap<Integer, Integer>();

    static {
        // Brigades
        ORDER_PHASE.put(AdditionalBattalions.ORDER_TYPE, 30);
        ORDER_PHASE.put(BrigadeJoinCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishBattalion.ORDER_TYPE, 70);
        ORDER_PHASE.put(DemolishBrigade.ORDER_TYPE, 70);
        ORDER_PHASE.put(ExchangeBattalions.ORDER_TYPE, 10);
        ORDER_PHASE.put(IncreaseExperience.ORDER_TYPE, 30);
        ORDER_PHASE.put(IncreaseHeadcount.ORDER_TYPE, 30);
        ORDER_PHASE.put(MergeBattalions.ORDER_TYPE, 30);
        ORDER_PHASE.put(RenameBrigade.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupBrigade.ORDER_TYPE, 30);

        // Corps
        ORDER_PHASE.put(CommanderJoinCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(CorpJoinArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupCorp.ORDER_TYPE, 10);
        ORDER_PHASE.put(IncreaseHeadcountCorps.ORDER_TYPE, 30);
        ORDER_PHASE.put(IncreaseExperienceCorps.ORDER_TYPE, 30);

        // Armies
        ORDER_PHASE.put(CommanderJoinArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupArmy.ORDER_TYPE, 10);
        ORDER_PHASE.put(IncreaseHeadcountArmy.ORDER_TYPE, 30);
        ORDER_PHASE.put(IncreaseExperienceArmy.ORDER_TYPE, 30);

        // Commanders
        ORDER_PHASE.put(HireCommander.ORDER_TYPE, 10);
        ORDER_PHASE.put(DismissCommander.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameCommander.ORDER_TYPE, 10);
        ORDER_PHASE.put(CommanderLeaveUnit.ORDER_TYPE, 10);

        // Fleets
        ORDER_PHASE.put(BuildShip.ORDER_TYPE, 50);
        ORDER_PHASE.put(RepairShip.ORDER_TYPE, 60);
        ORDER_PHASE.put(ScuttleShip.ORDER_TYPE, 70);
        ORDER_PHASE.put(ShipJoinFleet.ORDER_TYPE, 10);
        ORDER_PHASE.put(DemolishFleet.ORDER_TYPE, 10);
        ORDER_PHASE.put(RenameShip.ORDER_TYPE, 10);
        ORDER_PHASE.put(SetupFleet.ORDER_TYPE, 10);
        ORDER_PHASE.put(RepairFleet.ORDER_TYPE, 60);
        ORDER_PHASE.put(LoadFirst.ORDER_TYPE, 80);
        ORDER_PHASE.put(LoadSecond.ORDER_TYPE, 100);
        ORDER_PHASE.put(UnloadFirst.ORDER_TYPE, 80);
        ORDER_PHASE.put(UnloadSecond.ORDER_TYPE, 100);

        // Economy
        ORDER_PHASE.put(ChangeTaxation.ORDER_TYPE, 130);
        ORDER_PHASE.put(BuildBaggageTrain.ORDER_TYPE, 40);
        ORDER_PHASE.put(RepairBaggageTrain.ORDER_TYPE, 60);
        ORDER_PHASE.put(ScuttleBaggageTrain.ORDER_TYPE, 70);
        ORDER_PHASE.put(TransferFirst.ORDER_TYPE, 80);
        ORDER_PHASE.put(TransferSecond.ORDER_TYPE, 100);
        ORDER_PHASE.put(RenameBarrack.ORDER_TYPE, 20);

        // Map
        ORDER_PHASE.put(BuildProductionSite.ORDER_TYPE, 20);
        ORDER_PHASE.put(DemolishProductionSite.ORDER_TYPE, 20);
        ORDER_PHASE.put(IncreasePopDensity.ORDER_TYPE, 20);
        ORDER_PHASE.put(DecreasePopDensity.ORDER_TYPE, 20);
        ORDER_PHASE.put(HandOverTerritoryOrderProcessor.ORDER_TYPE, 110);
        ORDER_PHASE.put(HandOverShip.ORDER_TYPE, 110);

        // Movement
        ORDER_PHASE.put(OrderConstants.ORDER_M_UNIT, 90);
        ORDER_PHASE.put(BrigadeMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(BrigadeForcedMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(CorpMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(CorpForcedMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(ArmyMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(ArmyForcedMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(CommanderMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(SpyMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(BaggageTrainMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(MerchantShipMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(WarShipMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(ShipPatrolMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(FleetMovement.ORDER_TYPE, 90);
        ORDER_PHASE.put(FleetPatrolMovement.ORDER_TYPE, 90);

        // Politics
        ORDER_PHASE.put(PoliticsOrderProcessor.ORDER_TYPE, 120);
    }

    public static final Map<Integer, String> ORDER_NAMES = new HashMap<Integer, String>();

    static {
        // Brigades
        ORDER_NAMES.put(AdditionalBattalions.ORDER_TYPE, "Setup Additional Battalions");
        ORDER_NAMES.put(BrigadeJoinCorp.ORDER_TYPE, "Brigade Join Corps");
        ORDER_NAMES.put(DemolishBattalion.ORDER_TYPE, "Demolish Battalion");
        ORDER_NAMES.put(DemolishBrigade.ORDER_TYPE, "Demolish Brigade");
        ORDER_NAMES.put(ExchangeBattalions.ORDER_TYPE, "Exchange Battalions");
        ORDER_NAMES.put(IncreaseExperience.ORDER_TYPE, "Increase Experience");
        ORDER_NAMES.put(IncreaseHeadcount.ORDER_TYPE, "Increase Headcount");
        ORDER_NAMES.put(MergeBattalions.ORDER_TYPE, "Merge Battalions");
        ORDER_NAMES.put(RenameBrigade.ORDER_TYPE, "Rename Brigade");
        ORDER_NAMES.put(SetupBrigade.ORDER_TYPE, "Setup Brigade");

        // Corps
        ORDER_NAMES.put(CommanderJoinCorp.ORDER_TYPE, "Commander Join Corps");
        ORDER_NAMES.put(CorpJoinArmy.ORDER_TYPE, "Corps Join Army");
        ORDER_NAMES.put(DemolishCorp.ORDER_TYPE, "Demolish Corps");
        ORDER_NAMES.put(RenameCorp.ORDER_TYPE, "Rename Corps");
        ORDER_NAMES.put(SetupCorp.ORDER_TYPE, "Setup Corps");
        ORDER_NAMES.put(IncreaseHeadcountCorps.ORDER_TYPE, "Increase Headcount Corps");
        ORDER_NAMES.put(IncreaseExperienceCorps.ORDER_TYPE, "Increase Experience Corps");

        // Armies
        ORDER_NAMES.put(CommanderJoinArmy.ORDER_TYPE, "Commander Join Army");
        ORDER_NAMES.put(DemolishArmy.ORDER_TYPE, "Demolish Army");
        ORDER_NAMES.put(RenameArmy.ORDER_TYPE, "Rename Army");
        ORDER_NAMES.put(SetupArmy.ORDER_TYPE, "Setup Army");
        ORDER_NAMES.put(IncreaseHeadcountArmy.ORDER_TYPE, "Increase Headcount Army");
        ORDER_NAMES.put(IncreaseExperienceArmy.ORDER_TYPE, "Increase Experience Army");

        // Commanders
        ORDER_NAMES.put(HireCommander.ORDER_TYPE, "Hire Commander");
        ORDER_NAMES.put(DismissCommander.ORDER_TYPE, "Dismiss Commander");

        ORDER_NAMES.put(RenameCommander.ORDER_TYPE, "Rename Commander");
        ORDER_NAMES.put(CommanderLeaveUnit.ORDER_TYPE, "Remove Commander from Army/Corps");


        // Fleets
        ORDER_NAMES.put(BuildShip.ORDER_TYPE, "Build Ship");
        ORDER_NAMES.put(RepairShip.ORDER_TYPE, "Repair Ship");
        ORDER_NAMES.put(ScuttleShip.ORDER_TYPE, "Remove Ship");
        ORDER_NAMES.put(ShipJoinFleet.ORDER_TYPE, "Ship Join Fleet");
        ORDER_NAMES.put(DemolishFleet.ORDER_TYPE, "Demolish Fleet");
        ORDER_NAMES.put(RenameShip.ORDER_TYPE, "Rename Ship");
        ORDER_NAMES.put(SetupFleet.ORDER_TYPE, "Setup Fleet");
        ORDER_NAMES.put(RepairFleet.ORDER_TYPE, "Repair Fleet");
        ORDER_NAMES.put(LoadFirst.ORDER_TYPE, "Load (1)");
        ORDER_NAMES.put(LoadSecond.ORDER_TYPE, "Load (2)");
        ORDER_NAMES.put(UnloadFirst.ORDER_TYPE, "Unload (1)");
        ORDER_NAMES.put(UnloadSecond.ORDER_TYPE, "Unload (2)");

        // Economy
        ORDER_NAMES.put(ChangeTaxation.ORDER_TYPE, "Change Taxation");
        ORDER_NAMES.put(BuildBaggageTrain.ORDER_TYPE, "Build Baggage Train");
        ORDER_NAMES.put(RepairBaggageTrain.ORDER_TYPE, "Repair Baggage Train");
        ORDER_NAMES.put(ScuttleBaggageTrain.ORDER_TYPE, "Scuttle Baggage Train");
        ORDER_NAMES.put(TransferFirst.ORDER_TYPE, "Load/Unload/Sell/Buy Goods (P1)");
        ORDER_NAMES.put(TransferSecond.ORDER_TYPE, "Load/Unload/Sell/Buy Goods (P2)");

        // Map
        ORDER_NAMES.put(RenameBarrack.ORDER_TYPE, "Rename Barrack");
        ORDER_NAMES.put(BuildProductionSite.ORDER_TYPE, "Build Production Site");
        ORDER_NAMES.put(DemolishProductionSite.ORDER_TYPE, "Demolish Production Site");
        ORDER_NAMES.put(IncreasePopDensity.ORDER_TYPE, "Increase Sector Population Density");
        ORDER_NAMES.put(DecreasePopDensity.ORDER_TYPE, "Decrease Sector Population Density");
        ORDER_NAMES.put(HandOverTerritoryOrderProcessor.ORDER_TYPE, "Hand-Over Territory");
        ORDER_NAMES.put(HandOverShip.ORDER_TYPE, "Hand-Over Ship");

        // Movement
        ORDER_NAMES.put(OrderConstants.ORDER_M_UNIT, "Movement");
        ORDER_NAMES.put(BrigadeMovement.ORDER_TYPE, "Brigade Movement");
        ORDER_NAMES.put(BrigadeForcedMovement.ORDER_TYPE, "Brigade Forced Movement");
        ORDER_NAMES.put(CorpMovement.ORDER_TYPE, "Corps Movement");
        ORDER_NAMES.put(CorpForcedMovement.ORDER_TYPE, "Corps Forced Movement");
        ORDER_NAMES.put(ArmyMovement.ORDER_TYPE, "Army Movement");
        ORDER_NAMES.put(ArmyForcedMovement.ORDER_TYPE, "Army Forced Movement");
        ORDER_NAMES.put(CommanderMovement.ORDER_TYPE, "Commander Movement");
        ORDER_NAMES.put(SpyMovement.ORDER_TYPE, "Spy Movement");
        ORDER_NAMES.put(BaggageTrainMovement.ORDER_TYPE, "Baggage Train Movement");
        ORDER_NAMES.put(MerchantShipMovement.ORDER_TYPE, "Merchant Ship Movement");
        ORDER_NAMES.put(WarShipMovement.ORDER_TYPE, "Ship Movement");
        ORDER_NAMES.put(ShipPatrolMovement.ORDER_TYPE, "Ship Patrol");
        ORDER_NAMES.put(FleetMovement.ORDER_TYPE, "Fleet Movement");
        ORDER_NAMES.put(FleetPatrolMovement.ORDER_TYPE, "Fleet Patrol");

        // Politics
        ORDER_NAMES.put(PoliticsOrderProcessor.ORDER_TYPE, "Change Relations");
    }


    /**
     * The orders and the corresponding costs.
     */
    private transient Map<Integer, List<ClientOrderDTO>> clientOrders = new TreeMap<Integer, List<ClientOrderDTO>>();

    /**
     * Our instance of the Manager.
     */
    private transient static OrderStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized.
     */
    private transient boolean isInitialized = false;

    /**
     * Method returning the instance of the store or
     * a new instance if none exists.
     *
     * @return the instance of the order store.
     */
    public static OrderStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new OrderStore();
        }
        return ourInstance;
    }

    /**
     * Method initializing the client side orders Map
     *
     * @param orders A list of the orders as we get them from the database
     */
    public void initDbOrders(final Map<Integer, List<ClientOrderDTO>> orders) {
        try {
            // clear any previous additions to collections
            clientOrders = orders;

            // Order the orders
            for (final int key : orders.keySet()) {
                Collections.sort(clientOrders.get(key));
            }

            if (WarehouseStore.getInstance().isInitialized()) {
                setOrderExpenses();
                WarehouseStore.getInstance().getEconomyWidget().populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(EUROPE), false);
            }

            isInitialized = true;

        } catch (Exception Ex) {

            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize orders due to unexcpected reason ", false);
        }
    }

    /**
     * Method that spends the resources of the orders up till now
     * for the warehouse only. All other expenses (for trade orders)
     * come ready from the back-end
     */
    public void setOrderExpenses() {
        for (final int key : clientOrders.keySet()) {
            for (final ClientOrderDTO order : clientOrders.get(key)) {
                if (order.getOrderTypeId() == ORDER_EXCHF || order.getOrderTypeId() == ORDER_EXCHS) {
                    if (order.getIdentifier(0) == WAREHOUSE || order.getIdentifier(2) == WAREHOUSE) {
                        if (order.getIdentifier(0) == WAREHOUSE) {
                            int goodId = 0, qte = 0;
                            for (int goodIndex = GOOD_LAST - 2; goodIndex >= GOOD_FIRST; goodIndex--) {
                                if (order.getCosts().getNumericCost(goodIndex) != 0) {
                                    goodId = goodIndex;
                                    qte = order.getCosts().getNumericCost(goodIndex);
                                    break;
                                }
                            }
                            if (goodId != 0) {
                                WarehouseStore.getInstance().spendResource(goodId, qte, order.getRegionId());
                            }
                            if (order.getIdentifier(2) == TRADECITY) {
                                WarehouseStore.getInstance().getBackResource(GOOD_MONEY, order.getCosts().getNumericCost(GOOD_MONEY), order.getRegionId());
                            }

                        } else {
                            int goodId = 0, qte = 0;
                            for (int goodIndex = GOOD_LAST - 2; goodIndex >= GOOD_FIRST; goodIndex--) {
                                if (order.getCosts().getNumericCost(goodIndex) != 0) {
                                    goodId = goodIndex;
                                    qte = order.getCosts().getNumericCost(goodIndex);
                                    break;
                                }
                            }
                            if (goodId != 0) {
                                WarehouseStore.getInstance().getBackResource(goodId, qte, order.getRegionId());
                            }

                            if (order.getIdentifier(0) == TRADECITY) {
                                WarehouseStore.getInstance().spendResource(GOOD_MONEY, order.getCosts().getNumericCost(GOOD_MONEY), order.getRegionId());
                            }
                        }
                    }

                    WarehouseStore.getInstance().spendResource(GOOD_AP, order.getCosts().getNumericCost(GOOD_AP), EUROPE);

                } else {
                    WarehouseStore.getInstance().buyOrder(order, order.getRegionId(), true);
                }
            }
        }

    }

    public ClientOrderDTO constructOrder(final int typeId, final OrderCostDTO orderCost, final int regionId, final String name, final int[] identifiers, final int position, final String comment) {
        int priority = position;
        if (clientOrders.get(typeId) == null) {
            clientOrders.put(typeId, new ArrayList<ClientOrderDTO>());
        } else {
            priority = clientOrders.get(typeId).size();
        }

        if (position == 0) {
            priority = clientOrders.get(typeId).size();
        }

        final ClientOrderDTO order = new ClientOrderDTO();
        order.setOrderTypeId(typeId);
        order.setPriority(priority);
        order.setCosts(orderCost);
        order.setName(name);
        order.setComment(comment);

        for (int index = 0; index < identifiers.length; index++) {
            order.setIdentifier(index, identifiers[index]);
        }

        order.setRegionId(regionId);
        return order;
    }

    /**
     * Method that adds a new order to our order Map
     *
     * @param typeId      The type of the order we want to add.
     * @param orderCost   The calculated cost of the order.
     * @param regionId    the region of the warehouse.
     * @param name        the name of the order.
     * @param identifiers the id of the order.
     * @param position    The position of the order.
     * @param comment     the comment of the order.
     * @return 0 if there are no available funds 1 if there are
     */
    public int addNewOrder(final int typeId, final OrderCostDTO orderCost, final int regionId, final String name, final int[] identifiers, final int position, final String comment) {

        if (GameStore.getInstance().isNationDead()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "A dead leader cannot give orders!", false);
            return 0;
        }

        if (GameStore.getInstance().isGameEnded()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot give orders.", false);
            return 0;
        }

        final ClientOrderDTO order = constructOrder(typeId,  orderCost, regionId,  name, identifiers, position, comment);

        if (needsBuying(typeId)) {
            if (WarehouseStore.getInstance().buyOrder(order, regionId, false)) {
                clientOrders.get(typeId).add(order);
                EcoEventManager.addOrder(order);
                return 1;

            } else {
                return 0;
            }

        } else {
            clientOrders.get(typeId).add(order);
            EcoEventManager.addOrder(order);
            return 1;
        }
    }

    /**
     * Checks if the order needs to be bought
     *
     * @param typeId the type of the order
     * @return true if it need to be bought
     */
    private boolean needsBuying(final int typeId) {
        return !(typeId == ORDER_EXCHF || typeId == ORDER_EXCHS
                || typeId == ORDER_REN_BRIG
                || typeId == ORDER_REN_COMM
                || typeId == ORDER_REN_SHIP
                || typeId == ORDER_REN_ARMY
                || typeId == ORDER_REN_CORP
                || typeId == ORDER_REN_FLT
                || typeId == ORDER_REN_BARRACK
                || typeId == ORDER_REN_BTRAIN);
    }

    public ClientOrderDTO getOrderByTypeIds(final int typeId, final int... identifiers) {
        // Check if there are such commands
        if (clientOrders.containsKey(typeId)) {
            for (final ClientOrderDTO order : clientOrders.get(typeId)) {
                boolean matchids = true;
                for (int index = 0; index < identifiers.length; index++) {
                    if (order.getIdentifier(index) != identifiers[index]) {
                        matchids = false;
                        break;
                    }
                }

                if (matchids) {
                    return order;
                }
            }
        }
        return null;
    }

    /**
     * Method that removes the order and refunds the
     * economy items
     *
     * @param typeId      the type of the order to remove
     * @param identifiers the id array that determines which order
     *                    we want to remove
     * @return true if there was such an order and we removed it
     *         successfully. False any other way.
     */
    public boolean removeOrder(final int typeId, final int[] identifiers) {
        // Check if there are such commands
        ClientOrderDTO tgOrder = null;
        if (clientOrders.containsKey(typeId)) {
            for (final ClientOrderDTO order : clientOrders.get(typeId)) {
                boolean matchids = true;
                for (int index = 0; index < identifiers.length; index++) {
                    if (order.getIdentifier(index) != identifiers[index]) {
                        matchids = false;
                        break;
                    }
                }

                if (matchids) {
                    tgOrder = order;
                    break;
                }
            }
        }

        if (tgOrder == null) {
            return false;
        } else {
            if (needsBuying(typeId)) {
                WarehouseStore.getInstance().refundOrder(tgOrder.getCosts(), tgOrder.getRegionId());
                clientOrders.get(typeId).remove(tgOrder);
                for (final ClientOrderDTO reArOrder : clientOrders.get(typeId)) {
                    if (reArOrder.getPriority() > tgOrder.getPriority()) {
                        reArOrder.setPriority(reArOrder.getPriority() - 1);
                    }
                }

            } else {
                clientOrders.get(typeId).remove(tgOrder);
            }
            EcoEventManager.removeOrder(tgOrder);
            return true;

        }
    }

    /**
     * Method that returns the orders of the specified type
     *
     * @param orderType: The type of the order to return
     * @return A list with the requested orders
     */
    private List<ClientOrderDTO> getOrdersByType(final int orderType) {
        if (clientOrders.containsKey(orderType)) {
            return clientOrders.get(orderType);

        } else {
            return new ArrayList<ClientOrderDTO>(0);
        }
    }

    public List<ClientOrderDTO> getOrdersByTypes(final int[] orderTypes) {
        final List<ClientOrderDTO> out = new ArrayList<ClientOrderDTO>();
        for (int orderType : orderTypes) {
            out.addAll(getOrdersByType(orderType));
        }
        return out;
    }

    public List<ClientOrderDTO> getOrdersByTypes2(final int... orderTypes) {
        return getOrdersByTypes(orderTypes);
    }

    /**
     * Method that returns all military related orders
     *
     * @return List of the requested orders
     */
    public List<ClientOrderDTO> getMilitaryOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_ADD_BATT));
        orderList.addAll(getOrdersByType(ORDER_B_BATT));
        orderList.addAll(getOrdersByType(ORDER_B_ARMY));
        orderList.addAll(getOrdersByType(ORDER_B_CORP));
        orderList.addAll(getOrdersByType(ORDER_ADDTO_CORP));
        orderList.addAll(getOrdersByType(ORDER_ADDTO_ARMY));
        orderList.addAll(getOrdersByType(ORDER_ADDTO_BRIGADE));
        orderList.addAll(getOrdersByType(ORDER_HIRE_COM));
        orderList.addAll(getOrdersByType(ORDER_ARMY_COM));
        orderList.addAll(getOrdersByType(ORDER_CORP_COM));
        orderList.addAll(getOrdersByType(ORDER_LEAVE_COM));
        orderList.addAll(getOrdersByType(ORDER_DISS_COM));
        orderList.addAll(getOrdersByType(ORDER_INC_EXP));
        orderList.addAll(getOrdersByType(ORDER_INC_EXP_CORPS));
        orderList.addAll(getOrdersByType(ORDER_INC_EXP_ARMY));
        orderList.addAll(getOrdersByType(ORDER_INC_HEADCNT));
        orderList.addAll(getOrdersByType(ORDER_INC_HEADCNT_CORPS));
        orderList.addAll(getOrdersByType(ORDER_INC_HEADCNT_ARMY));
        orderList.addAll(getOrdersByType(ORDER_MRG_BATT));
        orderList.addAll(getOrdersByType(ORDER_D_BATT));
        orderList.addAll(getOrdersByType(ORDER_D_CORP));
        orderList.addAll(getOrdersByType(ORDER_D_ARMY));
        orderList.addAll(getOrdersByType(ORDER_REN_BRIG));
        orderList.addAll(getOrdersByType(ORDER_REN_ARMY));
        orderList.addAll(getOrdersByType(ORDER_REN_CORP));
        orderList.addAll(getOrdersByType(ORDER_REN_COMM));
        return orderList;
    }

    /**
     * Method that returns all navy related orders
     *
     * @return List of the requested orders
     */
    public List<ClientOrderDTO> getNavalOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_B_FLT));
        orderList.addAll(getOrdersByType(ORDER_B_SHIP));
        orderList.addAll(getOrdersByType(ORDER_ADDTO_FLT));
        orderList.addAll(getOrdersByType(ORDER_D_FLT));
        orderList.addAll(getOrdersByType(ORDER_HOVER_SHIP));
        orderList.addAll(getOrdersByType(ORDER_REN_FLT));
        orderList.addAll(getOrdersByType(ORDER_REN_SHIP));
        orderList.addAll(getOrdersByType(ORDER_SCUTTLE_SHIP));
        orderList.addAll(getOrdersByType(ORDER_R_SHP));
        orderList.addAll(getOrdersByType(ORDER_R_FLT));
        return orderList;
    }

    /**
     * Method that changes the order of the orders.
     *
     * @param order the order to change.
     * @param up    true if the order should move upwards, otherwise downwards.
     */
    public void changeOrdersOrder(final ClientOrderDTO order, final boolean up) {
        if (order.getPriority() != 0 && up) {
            order.setPriority(order.getPriority() - 1);
            for (ClientOrderDTO prOrder : clientOrders.get(order.getOrderTypeId())) {
                if (prOrder.getPriority() == order.getPriority() && !prOrder.equals(order)) {
                    prOrder.setPriority(prOrder.getPriority() + 1);
                    break;
                }
            }
            Collections.sort(clientOrders.get(order.getOrderTypeId()));

        } else if (order.getPriority() != clientOrders.get(order.getOrderTypeId()).size() - 1 && !up) {
            order.setPriority(order.getPriority() + 1);
            for (ClientOrderDTO prOrder : clientOrders.get(order.getOrderTypeId())) {
                if (prOrder.getPriority() == order.getPriority() && !prOrder.equals(order)) {
                    prOrder.setPriority(prOrder.getPriority() - 1);
                    break;
                }
            }

            Collections.sort(clientOrders.get(order.getOrderTypeId()));
        }
    }

    /**
     * Method that returns all movements orders
     *
     * @return List of the requested orders
     */
    public List<ClientOrderDTO> getMovementOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_M_UNIT));
        return orderList;
    }

    /**
     * Method that returns all economic orders
     *
     * @return List of the requested orders
     */
    public List<ClientOrderDTO> getEconomyOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_B_BTRAIN));
        orderList.addAll(getOrdersByType(ORDER_B_PRODS));
        orderList.addAll(getOrdersByType(ORDER_D_PRODS));
        orderList.addAll(getOrdersByType(ORDER_INC_POP));
        orderList.addAll(getOrdersByType(ORDER_DEC_POP));
        orderList.addAll(getOrdersByType(ORDER_HOVER_SEC));
        orderList.addAll(getOrdersByType(ORDER_HOVER_SHIP));
        orderList.addAll(getOrdersByType(ORDER_REN_BARRACK));
        orderList.addAll(getOrdersByType(ORDER_REN_BTRAIN));
        orderList.addAll(getOrdersByType(ORDER_SCUTTLE_BTRAIN));
        orderList.addAll(getOrdersByType(ORDER_POLITICS));
        return orderList;
    }

    /**
     * Method that returns all unload troops orders
     *
     * @return List of the requested orders
     */
    public List<ClientOrderDTO> getUnloadOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_UNLOAD_TROOPSF));
        orderList.addAll(getOrdersByType(ORDER_UNLOAD_TROOPSS));
        return orderList;
    }

    /**
     * Method that returns all load & unload troops orders
     *
     * @return List of the requested orders
     */
    public List<ClientOrderDTO> getLoadUnloadOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_LOAD_TROOPSF));
        orderList.addAll(getOrdersByType(ORDER_LOAD_TROOPSS));
        orderList.addAll(getOrdersByType(ORDER_UNLOAD_TROOPSF));
        orderList.addAll(getOrdersByType(ORDER_UNLOAD_TROOPSS));
        return orderList;
    }

    /**
     * Method that returns all trade orders.
     *
     * @return List of the requested orders.
     */
    public List<ClientOrderDTO> getCargoRelatedOrders() {
        final List<ClientOrderDTO> orderList = new ArrayList<ClientOrderDTO>();
        orderList.addAll(getOrdersByType(ORDER_EXCHF));
        orderList.addAll(getOrdersByType(ORDER_LOAD_TROOPSF));
        orderList.addAll(getOrdersByType(ORDER_UNLOAD_TROOPSF));
        orderList.addAll(getOrdersByType(ORDER_EXCHS));
        orderList.addAll(getOrdersByType(ORDER_LOAD_TROOPSS));
        orderList.addAll(getOrdersByType(ORDER_UNLOAD_TROOPSS));
        return orderList;
    }

    /**
     * Get the description of the order.
     *
     * @param clientOrder the order object.
     * @return the description of the order.
     */
    public String getOrderDescription(final ClientOrderDTO clientOrder) {
        try {
            switch (clientOrder.getOrderTypeId()) {

                case ORDER_REN_BRIG: {
                    BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));
                    if (thisBrigade == null) {
                        thisBrigade = ArmyStore.getInstance().getNewBrigadeById(clientOrder.getIdentifier(0));
                    }
                    return "Rename "
                            + fixUnitName(thisBrigade.getOriginalName(), "Brigade")
                            + " to "
                            + thisBrigade.getName();
                }

                case ORDER_REN_COMM: {
                    final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisCommander.getOriginalName(), "Commander")
                            + " to "
                            + thisCommander.getName();
                }

                case ORDER_REN_SHIP: {
                    final ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisShip.getOriginalName(), "Ship")
                            + " to "
                            + thisShip.getName();
                }

                case ORDER_REN_ARMY: {
                    final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisArmy.getOriginalName(), "Army")
                            + " to "
                            + thisArmy.getName();
                }

                case ORDER_REN_CORP: {
                    final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisCorps.getOriginalName(), "Corps")
                            + " to "
                            + thisCorps.getName();
                }

                case ORDER_REN_FLT: {
                    final FleetDTO thisFleet = NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisFleet.getOriginalName(), "Fleet")
                            + " to "
                            + thisFleet.getName();
                }

                case ORDER_REN_SPY: {
                    final SpyDTO thisSpy = SpyStore.getInstance().getSpyById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisSpy.getOriginalName(), "Spy")
                            + " to "
                            + thisSpy.getName();
                }

                case ORDER_REN_BARRACK: {
                    final BarrackDTO thisBarrack = BarrackStore.getInstance().getBarrackById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisBarrack.getOriginalName(), "Barrack")
                            + " to "
                            + thisBarrack.getName();
                }

                case ORDER_REN_BTRAIN: {
                    final BaggageTrainDTO thisTrain = BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));
                    return "Rename "
                            + fixUnitName(thisTrain.getOriginalName(), "train")
                            + " to "
                            + thisTrain.getName();
                }

                case ORDER_HIRE_COM_COL:
                case ORDER_HIRE_COM: {
                    final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));
                    return "Hire "
                            + fixUnitName(thisCommander.getName(), "Commander");
                }

                case ORDER_D_CORP: {
                    final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));
                    return "Disband "
                            + fixUnitName(thisCorps.getName(), "Corps")
                            + " at "
                            + thisCorps.startPositionToString();
                }

                case ORDER_B_CORP: {
                    final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));
                    return "Form "
                            + fixUnitName(thisCorps.getName(), "Corps")
                            + " at "
                            + thisCorps.startPositionToString();
                }

                case ORDER_ADDTO_CORP: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));
                    if (clientOrder.getIdentifier(1) != 0) {
                        final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));
                        return "Add "
                                + fixUnitName(thisBrigade.getName(), "Brigade")
                                + " to "
                                + fixUnitName(thisCorps.getName(), "Corps")
                                + " at " + thisCorps.startPositionToString();
                    } else {
                        return "Set "
                                + fixUnitName(thisBrigade.getName(), "Brigade")
                                + " free at "
                                + thisBrigade.startPositionToString();
                    }


                }

                case ORDER_CORP_COM: {
                    final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));
                    final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));
                    return "Assign  "
                            + fixUnitName(thisCommander.getName(), "Commander")
                            + " to "
                            + fixUnitName(thisCorps.getName(), "Corps")
                            + " at "
                            + thisCorps.startPositionToString();
                }

                case ORDER_D_ARMY: {
                    final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));
                    return "Disband "
                            + fixUnitName(thisArmy.getName(), "Army")
                            + " at "
                            + thisArmy.startPositionToString();
                }

                case ORDER_B_ARMY: {
                    final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));
                    return "Form "
                            + fixUnitName(thisArmy.getName(), "Army")
                            + " at "
                            + thisArmy.startPositionToString();
                }

                case ORDER_ADDTO_ARMY: {
                    final CorpDTO thisCorps;
                    //check if this corps is in the deleted map or in the active map.
                    if (ArmyStore.getInstance().isCorpDeleted(clientOrder.getIdentifier(0))) {
                        thisCorps = ArmyStore.getInstance().getDeletedCorpsById(clientOrder.getIdentifier(0));
                    } else {
                        thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));
                    }

                    if (clientOrder.getIdentifier(1) != 0) {
                        final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));
                        return "Add "
                                + fixUnitName(thisCorps.getName(), "Corps")
                                + " to "
                                + fixUnitName(thisArmy.getName(), "Army")
                                + " at "
                                + thisArmy.startPositionToString();
                    } else {
                        return "Set "
                                + fixUnitName(thisCorps.getName(), "Corps")
                                + " free at "
                                + thisCorps.startPositionToString();
                    }
                }

                case ORDER_ARMY_COM: {
                    final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));
                    final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));
                    return "Assign "
                            + fixUnitName(thisCommander.getName(), "Commander")
                            + " to "
                            + fixUnitName(thisArmy.getName(), "Army")
                            + " at "
                            + thisArmy.startPositionToString();
                }

                case ORDER_LEAVE_COM: {
                    final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));
                    return "Remove "
                            + fixUnitName(thisCommander.getName(), "Commander")
                            + " from Army or Corps";
                }

                case ORDER_DISS_COM: {
                    final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));
                    return "Dismiss "
                            + fixUnitName(thisCommander.getName(), "Commander");
                }

                case ORDER_ADDTO_BRIGADE: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(1));
                    return "Allocate Battalion "
                            + ArmyStore.getInstance().getBattalionById(clientOrder.getIdentifier(0)).getEmpireArmyType().getName()
                            + " to "
                            + fixUnitName(thisBrigade.getName(), "Brigade")
                            + " at "
                            + thisBrigade.startPositionToString();
                }

                case ORDER_D_FLT:
                    return "Demolish Fleet";

                case ORDER_B_FLT: {
                    final FleetDTO thisFleet = NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));
                    return "Form "
                            + fixUnitName(thisFleet.getName(), "Fleet")
                            + " at "
                            + thisFleet.startPositionToString();
                }

                case ORDER_ADDTO_FLT: {
                    if (clientOrder.getIdentifier(1) != 0) {
                        final FleetDTO thisFleet = NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(1));
                        return "Add Ship "
                                + NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0)).getName()
                                + " to "
                                + fixUnitName(thisFleet.getName(), "Fleet")
                                + " at "
                                + thisFleet.startPositionToString();
                    } else {
                        final ShipDTO ship = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                        return "Set "
                                + fixUnitName(ship.getName(), "Ship")
                                + " free at "
                                + ship.startPositionToString();
                    }
                }

                case ORDER_INC_POP:
                    return "Increase Population Density at "
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0)).positionToString();

                case ORDER_DEC_POP:
                    return "Decrease Population Density at "
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0)).positionToString();

                case ORDER_B_PRODS:
                    return "Build "
                            + DataStore.getInstance().getProdSite(clientOrder.getIdentifier(1)).getName()
                            + " at "
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0)).positionToString();

                case ORDER_D_PRODS:
                    return "Demolish site at "
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0)).positionToString();

                case ORDER_MRG_BATT: {
                    final BattalionDTO thisBatt = ArmyStore.getInstance().getMergedBattalionById(clientOrder.getIdentifier(0));
                    final BattalionDTO thatBatt = ArmyStore.getInstance().getMergedBattalionById(clientOrder.getIdentifier(1));

                    return "Merge Battalion "
                            + thisBatt.getEmpireArmyType().getName()
                            + " with Battalion "
                            + thatBatt.getEmpireArmyType().getName()
                            + " at " + ArmyStore.getInstance().getBrigadeById(thisBatt.getBrigadeId()).startPositionToString();
                }

                case ORDER_INC_HEADCNT: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));
                    return "Increase Headcount of "
                            + fixUnitName(thisBrigade.getName(), "Brigade")
                            + " at " + thisBrigade.startPositionToString();
                }

                case ORDER_INC_EXP: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));
                    if (clientOrder.getIdentifier(1) == 0) {
                        return "Increase Experience of "
                                + fixUnitName(thisBrigade.getName(), "Brigade")
                                + " at " + thisBrigade.startPositionToString();
                    } else {
                        return "Upgrade "
                                + fixUnitName(thisBrigade.getName(), "Brigade")
                                + " at " + thisBrigade.startPositionToString();
                    }
                }

                case ORDER_INC_HEADCNT_CORPS: {
                    final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));
                    return "Increase Headcount of "
                            + fixUnitName(thisCorps.getName(), "Corps")
                            + " at " + thisCorps.startPositionToString();
                }

                case ORDER_INC_EXP_CORPS: {
                    final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));
                    if (clientOrder.getIdentifier(1) == 0) {
                        return "Increase Experience of "
                                + fixUnitName(thisCorps.getName(), "Corps")
                                + " at " + thisCorps.startPositionToString();
                    } else {
                        return "Upgrade "
                                + fixUnitName(thisCorps.getName(), "Corps")
                                + " at " + thisCorps.startPositionToString();
                    }

                }

                case ORDER_INC_HEADCNT_ARMY: {
                    final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));
                    return "Increase Headcount of "
                            + fixUnitName(thisArmy.getName(), "Army")
                            + " at " + thisArmy.positionToString();
                }

                case ORDER_INC_EXP_ARMY: {
                    final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));
                    if (clientOrder.getIdentifier(1) == 0) {
                        return "Increase Experience of "
                                + fixUnitName(thisArmy.getName(), "Army")
                                + " at " + thisArmy.startPositionToString();
                    } else {
                        return "Upgrade "
                                + fixUnitName(thisArmy.getName(), "Army")
                                + " at " + thisArmy.startPositionToString();
                    }
                }

                case ORDER_ADD_BATT: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));
                    return "Add Battalion "
                            + ArmyStore.getInstance().getArmyTypeById(clientOrder.getIdentifier(1)).getName()
                            + " to "
                            + fixUnitName(thisBrigade.getName(), "brigade")
                            + " at " + thisBrigade.startPositionToString();
                }

                case ORDER_B_BATT_COL:
                case ORDER_B_BATT: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getNewBrigadeById(clientOrder.getIdentifier(1));
                    return "Setup "
                            + fixUnitName(thisBrigade.getName(), "brigade")
                            + " at "
                            + thisBrigade.positionToString();
                }

                case ORDER_B_BTRAIN:
                    return "Build new Baggage Train at"
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0)).positionToString();

                case ORDER_B_SHIP: {
                    final ShipDTO thisShip = NavyStore.getInstance().getNewShip(clientOrder.getIdentifier(0), clientOrder.getIdentifier(1));
                    return "Build "
                            + fixUnitName(thisShip.getName(), "ship")
                            + " at "
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(1)).startPositionToString();
                }

                case ORDER_R_SHP:
                    if (clientOrder.getIdentifier(3) == GameStore.getInstance().getNationId()) {
                        final ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                        return "Repair "
                                + fixUnitName(thisShip.getName(), "ship")
                                + " at "
                                + thisShip.startPositionToString();

                    } else {
                        final ShipDTO thisShip = AlliedUnitsStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                        return "Repair "
                                + fixUnitName(thisShip.getName(), "ship")
                                + " at "
                                + thisShip.startPositionToString();
                    }

                case ORDER_R_BTRAIN: {
                    if (clientOrder.getIdentifier(3) == GameStore.getInstance().getNationId()) {
                        final BaggageTrainDTO thisTrain = BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));
                        return "Repair "
                                + fixUnitName(thisTrain.getName(), "train")
                                + " at "
                                + thisTrain.startPositionToString();

                    } else {
                        final BaggageTrainDTO thisTrain = AlliedUnitsStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));
                        return "Repair "
                                + fixUnitName(thisTrain.getName(), "train")
                                + " at "
                                + thisTrain.startPositionToString();
                    }
                }

                case ORDER_R_FLT:
                    if (clientOrder.getIdentifier(3) == GameStore.getInstance().getNationId()) {
                        final FleetDTO thisFleet = NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));
                        return "Repair "
                                + fixUnitName(thisFleet.getName(), "fleet")
                                + " at "
                                + thisFleet.startPositionToString();

                    } else {
                        final FleetDTO thisFleet = AlliedUnitsStore.getInstance().getFleetById(clientOrder.getIdentifier(0));
                        return "Repair "
                                + fixUnitName(thisFleet.getName(), "fleet")
                                + " at "
                                + thisFleet.startPositionToString();
                    }

                case ORDER_SCUTTLE_BTRAIN: {
                    final BaggageTrainDTO thisTrain = BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));
                    return "Scuttle "
                            + fixUnitName(thisTrain.getName(), "train")
                            + " at "
                            + thisTrain.startPositionToString();
                }

                case ORDER_SCUTTLE_SHIP: {
                    final ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                    return "Scuttle "
                            + fixUnitName(thisShip.getName(), "ship");
                }

                case ORDER_D_BATT:
                    return "Disband Battalion ";


                case ORDER_D_BRIG: {
                    final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));
                    return "Disband "
                            + fixUnitName(thisBrigade.getName(), "brigade")
                            + " at " + thisBrigade.startPositionToString();
                }

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    StringBuilder cargoName = new StringBuilder();
                    StringBuilder transportName = new StringBuilder();
                    switch (clientOrder.getIdentifier(clientOrder.getIdentifier(0))) {
                        case BAGGAGETRAIN:
                            final BaggageTrainDTO thisTrain = (BaggageTrainDTO) TransportStore.getInstance().getTransportUnitById(ArmyConstants.BAGGAGETRAIN, clientOrder.getIdentifier(1));
                            transportName.append(fixUnitName(thisTrain.getName(), "train"))
                                    .append(" at ")
                                    .append(thisTrain.positionToString());
                            break;
                        case SHIP:
                            final ShipDTO thisShip = (ShipDTO) TransportStore.getInstance().getTransportUnitById(ArmyConstants.SHIP, clientOrder.getIdentifier(1));
                            transportName.append(" ")
                                    .append(fixUnitName(thisShip.getName(), "ship"))
                                    .append(" at ")
                                    .append(thisShip.positionToString());
                            break;
                        case FLEET:
                            final FleetDTO thisFleet = (FleetDTO) TransportStore.getInstance().getTransportUnitById(ArmyConstants.FLEET, clientOrder.getIdentifier(1));
                            transportName.append(" ")
                                    .append(fixUnitName(thisFleet.getName(), "fleet"))
                                    .append(" towards ")
                                    .append(getDirection(clientOrder.getIdentifier(5)));
                            break;

                        default:
                            transportName.append("Transport Unit");
                    }

                    switch (clientOrder.getIdentifier(2)) {
                        case BRIGADE: {
                            final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(3));
                            cargoName.append(fixUnitName(thisBrigade.getName(), "brigade"));
                            break;
                        }

                        case COMMANDER: {
                            final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(3));
                            cargoName.append(fixUnitName(thisCommander.getName(), "commander"));
                            break;
                        }

                        case SPY: {
                            final SpyDTO thisSpy = SpyStore.getInstance().getSpyById(clientOrder.getIdentifier(3));
                            cargoName.append(fixUnitName(thisSpy.getName(), "spy"));
                            break;
                        }

                        default:
                            cargoName.append("Cargo Unit");
                    }
                    return "Load " + cargoName.toString() + " on " + transportName.toString();

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    cargoName = new StringBuilder();
                    transportName = new StringBuilder();
                    switch (clientOrder.getIdentifier(0)) {
                        case BAGGAGETRAIN:
                            final BaggageTrainDTO thisTrain = (BaggageTrainDTO)
                                    TransportStore.getInstance().getTransportUnitById(ArmyConstants.BAGGAGETRAIN, clientOrder.getIdentifier(1));
                            transportName.append(" ")
                                    .append(fixUnitName(thisTrain.getName(), "train"))
                                    .append(" towards ")
                                    .append(getDirection(clientOrder.getIdentifier(4)));
                            break;
                        case SHIP:
                            final ShipDTO thisShip =
                                    (ShipDTO) TransportStore.getInstance().getTransportUnitById(ArmyConstants.SHIP, clientOrder.getIdentifier(1));
                            transportName.append(" ")
                                    .append(fixUnitName(thisShip.getName(), "ship"))
                                    .append(" towards ")
                                    .append(getDirection(clientOrder.getIdentifier(4)));
                            break;
                        case FLEET:
                            final FleetDTO thisFleet = (FleetDTO) TransportStore.getInstance().getTransportUnitById(ArmyConstants.FLEET, clientOrder.getIdentifier(1));
                            transportName.append(" ")
                                    .append(fixUnitName(thisFleet.getName(), "fleet"))
                                    .append(" towards ")
                                    .append(getDirection(clientOrder.getIdentifier(4)));
                            break;
                        default:
                            transportName.append("Transport Unit");
                    }

                    switch (clientOrder.getIdentifier(2)) {
                        case BRIGADE: {
                            final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(3));
                            cargoName.append(fixUnitName(thisBrigade.getName(), "brigade"));
                            break;
                        }

                        case COMMANDER: {
                            final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(3));
                            cargoName.append(fixUnitName(thisCommander.getName(), "commander"));
                            break;
                        }

                        case SPY: {
                            final SpyDTO thisSpy = SpyStore.getInstance().getSpyById(clientOrder.getIdentifier(3));
                            cargoName.append(fixUnitName(thisSpy.getName(), "spy"));
                            break;
                        }

                        default:
                            cargoName.append("Cargo Unit");
                    }
                    return "Unload " + cargoName.toString() + " from " + transportName.toString();

                case ORDER_EXCHF:
                case ORDER_EXCHS:

                    final StringBuilder tradeUnit1 = new StringBuilder();
                    final StringBuilder tradeUnit2 = new StringBuilder();
                    switch (clientOrder.getIdentifier(0)) {
                        case BAGGAGETRAIN: {
                            BaggageTrainDTO thisTrain = BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(1));
                            if (thisTrain == null) {
                                thisTrain = AlliedUnitsStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(1));
                            }
                            tradeUnit1.append(" ").append(fixUnitName(thisTrain.getName(), "train"));
                            break;
                        }

                        case SHIP: {
                            ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(1));
                            if (thisShip == null) {
                                thisShip = AlliedUnitsStore.getInstance().getShipById(clientOrder.getIdentifier(1));
                            }
                            tradeUnit1.append(" ").append(fixUnitName(thisShip.getName(), "ship"));
                            break;
                        }

                        case TRADECITY:
                            tradeUnit1.append(" ").
                                    append(TradeCityStore.getInstance().getTradeCityById(clientOrder.getIdentifier(1)).getName());
                            break;

                        case WAREHOUSE:
                            tradeUnit1.append(" ").append(
                                    WarehouseStore.getInstance().getWareHouseById(clientOrder.getIdentifier(1)).getName());
                            break;

                        default:
                            tradeUnit1.append("Trade Unit");
                    }

                    switch (clientOrder.getIdentifier(2)) {
                        case BAGGAGETRAIN: {
                            BaggageTrainDTO thisTrain = BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(3));
                            if (thisTrain == null) {
                                thisTrain = AlliedUnitsStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(3));
                            }
                            tradeUnit2.append(" ")
                                    .append(fixUnitName(thisTrain.getName(), "train"))
                                    .append(" at ")
                                    .append(thisTrain.positionToString());
                            break;
                        }

                        case SHIP: {
                            ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(3));
                            if (thisShip == null) {
                                thisShip = AlliedUnitsStore.getInstance().getShipById(clientOrder.getIdentifier(3));
                            }
                            tradeUnit2.append(" ")
                                    .append(fixUnitName(thisShip.getName(), "ship"))
                                    .append(" at ")
                                    .append(thisShip.positionToString());
                            break;
                        }

                        case TRADECITY:
                            tradeUnit2.append(" ")
                                    .append(TradeCityStore.getInstance().getTradeCityById(clientOrder.getIdentifier(3)).getName())
                                    .append(" at ")
                                    .append(TradeCityStore.getInstance().getTradeCityById(clientOrder.getIdentifier(3)).positionToString());
                            break;

                        case WAREHOUSE:
                            tradeUnit2.append(" ")
                                    .append(WarehouseStore.getInstance().getWareHouseById(clientOrder.getIdentifier(3)).getName());
                            break;

                        default:
                            tradeUnit2.append("Trade Unit");
                    }

                    return "Transfer from " + tradeUnit1.toString() + " to " + tradeUnit2.toString();

                case ORDER_M_UNIT:
                    switch (clientOrder.getIdentifier(2)) {
                        case ORDER_M_BRIG: {
                            final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisBrigade.getName(), "brigade")
                                    + " from "
                                    + thisBrigade.startPositionToString()
                                    + " to "
                                    + thisBrigade.positionToString();
                        }

                        case ORDER_FM_BRIG: {
                            final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(1));
                            return "Force March "
                                    + fixUnitName(thisBrigade.getName(), "brigade")
                                    + " from "
                                    + thisBrigade.startPositionToString()
                                    + " to "
                                    + thisBrigade.positionToString();
                        }

                        case ORDER_M_CORP: {
                            final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisCorps.getName(), "corps")
                                    + " from "
                                    + thisCorps.startPositionToString()
                                    + " to "
                                    + thisCorps.positionToString();
                        }

                        case ORDER_FM_CORP: {
                            final CorpDTO thisCorps = ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));
                            return "Force March "
                                    + fixUnitName(thisCorps.getName(), "corps")
                                    + " from "
                                    + thisCorps.startPositionToString()
                                    + " to "
                                    + thisCorps.positionToString();
                        }

                        case ORDER_M_ARMY: {
                            final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisArmy.getName(), "army")
                                    + " from "
                                    + thisArmy.startPositionToString()
                                    + " to "
                                    + thisArmy.positionToString();
                        }

                        case ORDER_FM_ARMY: {
                            final ArmyDTO thisArmy = ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));
                            return "Force March "
                                    + fixUnitName(thisArmy.getName(), "army")
                                    + " from "
                                    + thisArmy.startPositionToString()
                                    + " to "
                                    + thisArmy.positionToString();
                        }

                        case ORDER_M_COMM: {
                            final CommanderDTO thisCommander = CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisCommander.getName(), "commander")
                                    + " from "
                                    + thisCommander.startPositionToString()
                                    + " to "
                                    + thisCommander.positionToString();
                        }

                        case ORDER_M_SPY: {
                            final SpyDTO thisSpy = SpyStore.getInstance().getSpyById(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisSpy.getName(), "spy")
                                    + " from "
                                    + thisSpy.startPositionToString()
                                    + " to "
                                    + thisSpy.positionToString();
                        }

                        case ORDER_M_BTRAIN: {
                            final BaggageTrainDTO thisTrain = BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisTrain.getName(), "train")
                                    + " from "
                                    + thisTrain.startPositionToString()
                                    + " to "
                                    + thisTrain.positionToString();
                        }

                        case ORDER_M_MSHIP:
                        case ORDER_M_SHIP: {
                            final ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(1));

                            return "Move "
                                    + fixUnitName(thisShip.getName(), "ship")
                                    + " from "
                                    + thisShip.startPositionToString()
                                    + " to "
                                    + thisShip.positionToString();
                        }

                        case ORDER_M_FLEET: {
                            final FleetDTO thisFleet = NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(1));
                            return "Move "
                                    + fixUnitName(thisFleet.getName(), "fleet")
                                    + " from "
                                    + thisFleet.startPositionToString()
                                    + " to "
                                    + thisFleet.positionToString();
                        }

                        case ORDER_P_SHIP: {
                            final ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(1));
                            return "Patrol "
                                    + fixUnitName(thisShip.getName(), "ship")
                                    + " from "
                                    + thisShip.startPositionToString()
                                    + " to "
                                    + thisShip.positionToString();
                        }

                        case ORDER_P_FLEET: {
                            final FleetDTO thisFleet = NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(1));
                            return "Patrol "
                                    + fixUnitName(thisFleet.getName(), "fleet")
                                    + " from "
                                    + thisFleet.startPositionToString()
                                    + " to "
                                    + thisFleet.positionToString();
                        }

                        default:
                            return "Error retrieving movement order description (" + clientOrder.getIdentifier(2) + ")";
                    }

                case ORDER_HOVER_SEC:
                    return "Hand over "
                            + RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0)).positionToString()
                            + " to "
                            + DataStore.getInstance().getNationById(
                            Integer.parseInt(RegionStore.getInstance().getSectorOrderMap().get(clientOrder.getIdentifier(0)).getParameter4())
                    ).getName();

                case ORDER_HOVER_SHIP: {
                    final ShipDTO thisShip = NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                    final StringBuilder nameShip = new StringBuilder();
                    if (!thisShip.getName().toLowerCase().contains("ship")) {
                        nameShip.append("Ship ");
                    }
                    nameShip.append("'").append(thisShip.getName()).append("'");

                    return "Hand over "
                            + nameShip.toString()
                            + " to "
                            + DataStore.getInstance().getNationNameByNationId(clientOrder.getIdentifier(1));
                }

                case ORDER_TAXATION:
                    return "Change taxation";
//            case TAX_HARSH:
//                return "Apply Harsh Taxation Rate";
//                case TAX_LOW:
//                    return "Apply Low Taxation Rate";

                case ORDER_POLITICS:
                    if (clientOrder.getIdentifier(1) == REL_WAR) {
                        switch (clientOrder.getIdentifier(2)) {
                            case NO_ACTION:
                                return "Change political relations towards "
                                        + DataStore.getInstance().getNationById(clientOrder.getIdentifier(0)).getName()
                                        + " to "
                                        + RelationsStore.getInstance().getNameRelation(clientOrder.getIdentifier(1));

                            case ACCEPT_SURR:
                                return "Accept surrender from "
                                        + DataStore.getInstance().getNationById(clientOrder.getIdentifier(0)).getName();

                            case OFFER_SURR:
                                return "Offer surrender to "
                                        + DataStore.getInstance().getNationById(clientOrder.getIdentifier(0)).getName();

                            case MAKE_PEACE:
                                return "Offer peace to "
                                        + DataStore.getInstance().getNationById(clientOrder.getIdentifier(0)).getName();

                            default:
                                return "Change political relations towards "
                                        + DataStore.getInstance().getNationById(clientOrder.getIdentifier(0)).getName()
                                        + " to "
                                        + RelationsStore.getInstance().getNameRelation(clientOrder.getIdentifier(1));
                        }

                    } else {
                        return "Change political relations towards "
                                + DataStore.getInstance().getNationById(clientOrder.getIdentifier(0)).getName()
                                + " to "
                                + RelationsStore.getInstance().getNameRelation(clientOrder.getIdentifier(1));
                    }

                default:
                    return clientOrder.getOrderTypeId() + " : pending description";
            }

        } catch (Exception e) {
            // eat it
            //Window.alert("Failed to generate order description for order : " + clientOrder.getOrderTypeId());
//            Window.alert(e.toString());
        }

        return clientOrder.getOrderTypeId() + ": Error getting description";
    }

    public String fixUnitName(final String name, final String keyword) {
        String newName = name;
        if (newName == null) {
            newName = "";
        }
        final StringBuilder nameSpy = new StringBuilder();
        if (!newName.toLowerCase().contains(keyword.toLowerCase())) {
            nameSpy.append(keyword).append(" ");
        }
        nameSpy.append("'").append(newName).append("'");
        return nameSpy.toString();
    }

    public PositionDTO getPositionRelatedToOrder(final ClientOrderDTO clientOrder) {
        try {
            switch (clientOrder.getOrderTypeId()) {
                case ORDER_INC_EXP:
                    return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));

                case ORDER_INC_EXP_CORPS:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));

                case ORDER_INC_EXP_ARMY:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));

                case ORDER_INC_HEADCNT:
                    return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));

                case ORDER_INC_HEADCNT_CORPS:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));

                case ORDER_INC_HEADCNT_ARMY:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));

                case ORDER_MRG_BATT:
                    return ArmyStore.getInstance().getBrigadeById(
                            ArmyStore.getInstance().getBattalionById(clientOrder.getIdentifier(0))
                                    .getBrigadeId());

                case ORDER_ADD_BATT:
                    return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));

                case ORDER_B_ARMY:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));

                case ORDER_B_CORP:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));

                case ORDER_ADDTO_ARMY:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));

                case ORDER_ADDTO_CORP:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));

                case ORDER_D_BATT:
                    return null;

                case ORDER_D_BRIG:
                    return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));

                case ORDER_D_CORP:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));

                case ORDER_CORP_COM:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));

                case ORDER_ARMY_COM:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));

                case ORDER_LEAVE_COM:
                    return CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));

                case ORDER_D_ARMY:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));

                case ORDER_B_SHIP:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(1));

                case ORDER_R_SHP:
                    if (clientOrder.getIdentifier(3) == GameStore.getInstance().getNationId()) {
                        return NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                    } else {
                        return AlliedUnitsStore.getInstance().getShipById(clientOrder.getIdentifier(0));
                    }

                case ORDER_B_FLT:
                    return NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));

                case ORDER_ADDTO_FLT:
                    return NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(1));

                case ORDER_D_FLT:
                    return null;

                case ORDER_B_PRODS:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0));

                case ORDER_D_PRODS:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0));

                case ORDER_INC_POP:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0));

                case ORDER_DEC_POP:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0));

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    return TransportStore.getInstance().getTransportUnitById(clientOrder.getIdentifier(0), clientOrder.getIdentifier(1));

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    return TransportStore.getInstance().getTransportUnitById(clientOrder.getIdentifier(0), clientOrder.getIdentifier(1));

                case ORDER_EXCHF:
                case ORDER_EXCHS:
                    return TradeCityStore.getInstance().getTradeCityById(clientOrder.getIdentifier(3));

                case ORDER_M_UNIT:
                    switch (clientOrder.getIdentifier(2)) {
                        case ORDER_M_BRIG:
                            return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(1));

                        case ORDER_FM_BRIG:
                            return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(1));

                        case ORDER_M_CORP:
                            return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));

                        case ORDER_FM_CORP:
                            return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(1));

                        case ORDER_M_ARMY:
                            return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));

                        case ORDER_FM_ARMY:
                            return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(1));

                        case ORDER_M_COMM:
                            return CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(1));

                        case ORDER_M_SPY:
                            return SpyStore.getInstance().getSpyById(clientOrder.getIdentifier(1));

                        case ORDER_M_BTRAIN:
                            return BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(1));

                        case ORDER_M_MSHIP:
                        case ORDER_M_SHIP:
                            return NavyStore.getInstance().getShipById(clientOrder.getIdentifier(1));

                        case ORDER_M_FLEET:
                            return NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(1));

                        default:
                            return null;
                    }

                case ORDER_P_SHIP:
                    return NavyStore.getInstance().getShipById(clientOrder.getIdentifier(1));

                case ORDER_P_FLEET:
                    return NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));

                case ORDER_HOVER_SEC:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0));

                case ORDER_HOVER_SHIP:
                    return NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));

                case ORDER_TAXATION:
                    return null;

                case TAX_LOW:
                    return null;

                case ORDER_POLITICS:
                    return null;

                case ORDER_B_BTRAIN:
                    return RegionStore.getInstance().getSectorById(clientOrder.getIdentifier(0));

                case ORDER_R_BTRAIN:
                    if (clientOrder.getIdentifier(3) == GameStore.getInstance().getNationId()) {
                        return BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));

                    } else {
                        return AlliedUnitsStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));
                    }

                case ORDER_REN_BRIG:
                    return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(0));

                case ORDER_REN_COMM:
                    return CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));

                case ORDER_REN_SHIP:
                    return NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));

                case ORDER_REN_ARMY:
                    return ArmyStore.getInstance().getArmyById(clientOrder.getIdentifier(0));

                case ORDER_REN_CORP:
                    return ArmyStore.getInstance().getCorpByID(clientOrder.getIdentifier(0));

                case ORDER_REN_FLT:
                    return NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));

                case ORDER_REN_SPY:
                    return SpyStore.getInstance().getSpyById(clientOrder.getIdentifier(0));

                case ORDER_REN_BARRACK:
                    return BarrackStore.getInstance().getBarrackById(clientOrder.getIdentifier(0));

                case ORDER_REN_BTRAIN:
                    return BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));

                case ORDER_B_BATT:
                    return ArmyStore.getInstance().getNewBrigadeById(clientOrder.getIdentifier(1));

                case ORDER_HIRE_COM:
                    return CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));

                case ORDER_ADDTO_BRIGADE:
                    return ArmyStore.getInstance().getBrigadeById(clientOrder.getIdentifier(1));

                case ORDER_SCUTTLE_BTRAIN:
                    return BaggageTrainStore.getInstance().getBaggageTrainById(clientOrder.getIdentifier(0));

                case ORDER_R_FLT:
                    if (clientOrder.getIdentifier(3) == GameStore.getInstance().getNationId()) {
                        return NavyStore.getInstance().getFleetById(clientOrder.getIdentifier(0));
                    } else {
                        return AlliedUnitsStore.getInstance().getFleetById(clientOrder.getIdentifier(0));
                    }

                case ORDER_DISS_COM:
                    return CommanderStore.getInstance().getCommanderById(clientOrder.getIdentifier(0));

                case ORDER_SCUTTLE_SHIP:
                    return NavyStore.getInstance().getShipById(clientOrder.getIdentifier(0));

                default:
                    return null;
            }
        } catch (Exception e) {
//            Window.alert("Failed to generate order description for order : " + clientOrder.getOrderTypeId());
//            Window.alert(e.toString());
        }
        return null;
    }

    /**
     * Method that cancels an order
     *
     * @param order the order to cancel.
     */
    public void cancelOrder(final ClientOrderDTO order) {
        switch (order.getOrderTypeId()) {
            case ORDER_ADD_BATT:
                ArmyStore.getInstance().removeNewBattFromBrigade(order.getIdentifier(0), order.getIdentifier(1), order.getIdentifier(2));
                break;

            case ORDER_B_ARMY:
                ArmyStore.getInstance().deleteArmy(order.getIdentifier(0));
                break;

            case ORDER_B_CORP:
                ArmyStore.getInstance().deleteCorp(order.getIdentifier(0), order.getIdentifier(1));
                break;

            case ORDER_B_BATT:
                ArmyStore.getInstance().cancelBrigade(order.getIdentifier(0), ArmyStore.getInstance().getNewBrigadeById(order.getIdentifier(1)));
                break;

            case ORDER_ADDTO_ARMY:
                final CorpDTO corp;
                if (ArmyStore.getInstance().isCorpDeleted(order.getIdentifier(0))) {
                    corp = ArmyStore.getInstance().getDeletedCorpsById(order.getIdentifier(0));
                } else {
                    corp = ArmyStore.getInstance().getCorpByID(order.getIdentifier(0));
                }
                ArmyStore.getInstance().changeCorpArmy(order.getIdentifier(0), order.getIdentifier(1), corp.getStartArmy());
                break;

            case ORDER_ADDTO_CORP:
                final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(order.getIdentifier(0));
                ArmyStore.getInstance().changeBrigadeCorp(order.getIdentifier(0), order.getIdentifier(1), brig.getStartCorp(), true);
                break;

            case ORDER_ADDTO_BRIGADE:
                final BattalionDTO batt = ArmyStore.getInstance().getBattalionById(order.getIdentifier(0));
                ArmyStore.getInstance().changeBattalionBrigade(order.getIdentifier(0), batt.getStartBrigadeId(), batt.getStartOrder(), true);
                break;

            case ORDER_ADDTO_FLT: {
                final ShipDTO ship = NavyStore.getInstance().getShipById(order.getIdentifier(0));
                NavyStore.getInstance().changeShipFleet(order.getIdentifier(0), order.getIdentifier(1), ship.getStartFleet(), true, true);
                break;
            }

            case ORDER_B_FLT:
                NavyStore.getInstance().deleteFleet(order.getIdentifier(0));
                break;

            case ORDER_B_SHIP: {
                final ShipDTO ship = NavyStore.getInstance().getNewShip(order.getIdentifier(0), order.getIdentifier(1), order.getIdentifier(2));
                NavyStore.getInstance().cancelBuildShip(order.getIdentifier(1), ship);
                break;
            }

            case ORDER_CORP_COM:
                CommanderStore.getInstance().undoAddCommanderToCorps(order.getIdentifier(1), order.getIdentifier(0));
                break;

            case ORDER_ARMY_COM:
                CommanderStore.getInstance().undoAddCommanderToArmy(order.getIdentifier(1), order.getIdentifier(0));
                break;

            case ORDER_LEAVE_COM:
                CommanderDTO com = CommanderStore.getInstance().getCommanderById(order.getIdentifier(0));
                if (com.getStartArmy() == 0) {
                    CommanderStore.getInstance().addCommanderToCorp(com.getStartCorp(), order.getIdentifier(0), true);
                } else {
                    CommanderStore.getInstance().addCommanderToArmy(com.getStartArmy(), order.getIdentifier(0), true);
                }
                break;

            case ORDER_HIRE_COM:
                com = CommanderStore.getInstance().getCommanderById(order.getIdentifier(0));
                boolean done = false;
                if (com.getArmy() != 0) {
                    try {
                    CommanderStore.getInstance().undoAddCommanderToArmy(com.getArmy(), com.getId());
                    done = true;
                    } catch (Exception e) {
                        Window.alert("F1? " + e.toString());
                    }
                }
                if (com.getCorp() != 0) {
                    try {
                    CommanderStore.getInstance().undoAddCommanderToCorps(com.getCorp(), com.getId());
                    done = true;
                    } catch (Exception e) {
                        Window.alert("F2? " + e.toString());
                    }
                }
                try {
                if (!done) {
                    CommanderStore.getInstance().undoHireCommander(order.getIdentifier(0));
                }
                } catch (Exception e) {
                    Window.alert("F3? " + e.toString());
                }
                break;

            case ORDER_DISS_COM:
                CommanderStore.getInstance().undoDismissCommander(order.getIdentifier(0));
                break;

            case ORDER_HOVER_SHIP:
                NavyStore.getInstance().undoHandOverShip(order.getIdentifier(0));
                break;

            case ORDER_B_PRODS:
            case ORDER_D_PRODS: {
                final SectorDTO sector = RegionStore.getInstance().getSectorById(order.getIdentifier(0));
                ProductionSiteStore.getInstance().cancelOrder(sector, true);
                break;
            }

            case ORDER_INC_POP:
            case ORDER_DEC_POP: {
                final SectorDTO sector = RegionStore.getInstance().getSectorById(order.getIdentifier(0));
                RegionStore.getInstance().cancelOrder(sector);
                break;
            }

            case ORDER_MRG_BATT:
                ArmyStore.getInstance().undoMerge(order.getIdentifier(0), order.getIdentifier(1));
                break;

            case ORDER_B_BTRAIN:
                final SectorDTO sector = RegionStore.getInstance().getSectorById(order.getIdentifier(0));
                BaggageTrainStore.getInstance().removeBuildOrder(sector, order.getIdentifier(1));
                break;

            case ORDER_LOAD_TROOPSF:
            case ORDER_LOAD_TROOPSS:
                TransportStore.getInstance().undoLoadUnitFromTransport(order.getIdentifier(2), order.getIdentifier(3));
                break;

            case ORDER_UNLOAD_TROOPSF:
            case ORDER_UNLOAD_TROOPSS:
                TransportStore.getInstance().undoUnloadUnitFromTransport(order.getIdentifier(2), order.getIdentifier(3));
                break;

            case ORDER_EXCHF:
            case ORDER_EXCHS:
                TradeStore.getInstance().undoExchange(order);
                break;

            case ORDER_M_UNIT:
                MovementStore.getInstance().undoMovementEntirely(order.getIdentifier(2), order.getIdentifier(1));
                break;

            case ORDER_D_ARMY:
            case ORDER_D_CORP:
            case ORDER_D_BRIG:
            case ORDER_D_FLT:
                break;

            case ORDER_REN_BRIG:
                ArmyStore.getInstance().undoRenameBrig(order.getIdentifier(0));
                break;

            case ORDER_REN_COMM:
                CommanderStore.getInstance().undoRenameCommander(order.getIdentifier(0));
                break;

            case ORDER_REN_SHIP:
                NavyStore.getInstance().undoRenameShip(order.getIdentifier(0));
                break;
            case ORDER_REN_SPY:
                SpyStore.getInstance().undoRenameSpy(order.getIdentifier(0));
                break;
            case ORDER_REN_ARMY:
                ArmyStore.getInstance().undoRenameArmy(order.getIdentifier(0));
                break;

            case ORDER_REN_CORP:
                ArmyStore.getInstance().undoRenameCorps(order.getIdentifier(0));
                break;

            case ORDER_REN_FLT:
                NavyStore.getInstance().undoRenameFleet(order.getIdentifier(0));
                break;

            case ORDER_REN_BARRACK:
                BarrackStore.getInstance().undoRenameBarrack(order.getIdentifier(0));
                break;

            case ORDER_REN_BTRAIN:
                BaggageTrainStore.getInstance().undoRenameBaggageTrain(order.getIdentifier(0));
                break;

            case ORDER_SCUTTLE_BTRAIN:
                BaggageTrainStore.getInstance().undoScuttleOrder(order.getIdentifier(0));
                break;

            case ORDER_SCUTTLE_SHIP:
                NavyStore.getInstance().undoScuttleOrder(order.getIdentifier(0));
                break;

            case ORDER_POLITICS:
                RelationsStore.getInstance().changeNationRelationship(order.getIdentifier(0), RelationsStore.getInstance().getOriginalRelationByNationId(order.getIdentifier(0)), 0);
                break;

            case ORDER_R_SHP: {
                if (GameStore.getInstance().getNationId() == order.getIdentifier(3)) {
                    final ShipDTO ship = NavyStore.getInstance().getShipById(order.getIdentifier(0));
                    NavyStore.getInstance().cancelRepairShip(ship.getFleet(), order.getIdentifier(0), order.getIdentifier(1));
                } else {
                    final ShipDTO ship = AlliedUnitsStore.getInstance().getShipById(order.getIdentifier(0));
                    AlliedUnitsStore.getInstance().cancelRepairShip(ship.getFleet(), order.getIdentifier(0), order.getIdentifier(1), order.getIdentifier(3));
                }
                break;
            }

            case ORDER_R_FLT:
                if (GameStore.getInstance().getNationId() == order.getIdentifier(3)) {
                    NavyStore.getInstance().cancelRepairFleet(order.getIdentifier(0), order.getIdentifier(1));
                } else {
                    AlliedUnitsStore.getInstance().cancelRepairFleet(order.getIdentifier(0), order.getIdentifier(1), order.getIdentifier(3));
                }
                break;

            case ORDER_R_BTRAIN:
                if (GameStore.getInstance().getNationId() == order.getIdentifier(3)) {
                    BaggageTrainStore.getInstance().cancelRepairOrder(order.getIdentifier(0), order.getIdentifier(1));

                } else {
                    AlliedUnitsStore.getInstance().cancelRepairTrain(order.getIdentifier(0), order.getIdentifier(1));
                }

                break;

            case ORDER_INC_EXP:
                ArmyStore.getInstance().cancelUpgradeBrigade(order.getIdentifier(0), order.getIdentifier(1) == 1, order.getIdentifier(2), false);
                break;

            case ORDER_INC_EXP_CORPS:
                ArmyStore.getInstance().cancelUpgradeCorps(order.getIdentifier(0), order.getIdentifier(1) == 1, order.getIdentifier(2), false);
                break;

            case ORDER_INC_EXP_ARMY:
                ArmyStore.getInstance().cancelUpgradeArmy(order.getIdentifier(0), order.getIdentifier(1) == 1, order.getIdentifier(2), false);
                break;

            case ORDER_INC_HEADCNT:
                ArmyStore.getInstance().cancelUpHeadCountBrigade(order.getIdentifier(0));
                break;

            case ORDER_INC_HEADCNT_CORPS:
                ArmyStore.getInstance().cancelUpHeadCountCorps(order.getIdentifier(0));
                break;

            case ORDER_INC_HEADCNT_ARMY:
                ArmyStore.getInstance().cancelUpHeadCountArmy(order.getIdentifier(0));
                break;

            case ORDER_TAXATION:
                //finally remove the order
                final int[] ids = new int[1];
                ids[0] = 0;
                if (removeOrder(ORDER_TAXATION, ids)) {
                    //set level to 0 = normal
                    ProductionSiteStore.getInstance().getTax().setTaxLevel(0);
                    //update cost values
                    ProductionSiteStore.getInstance().getTax().setUseGems(false);
                    ProductionSiteStore.getInstance().getTax().getCost().setNumericCost(GOOD_GEMS, 0);
                    ProductionSiteStore.getInstance().getTax().setUseColGoods(false);
                    ProductionSiteStore.getInstance().getTax().getCost().setNumericCost(GOOD_COLONIAL, 0);
                    ProductionSiteStore.getInstance().getTax().setUseIndPoints(false);
                    ProductionSiteStore.getInstance().getTax().getCost().setNumericCost(GOOD_INPT, 0);
                    ProductionSiteStore.getInstance().getTax().setUseMoney(false);
                    ProductionSiteStore.getInstance().getTax().getCost().setNumericCost(GOOD_MONEY, 0);
                    GameStore.getInstance().getLayoutView().getOptionsMenu().getTaxView().initTaxation();
                }
                break;

            case ORDER_HOVER_SEC:
                RegionStore.getInstance().undoHandOverSector(order.getIdentifier(0));
                break;
            default:
        }
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return all the orders set in this turn
     */
    public Map<Integer, List<ClientOrderDTO>> getClientOrders() {
        return clientOrders;
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


}
