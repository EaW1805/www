package com.eaw1805.www.shared.stores;

import com.eaw1805.data.constants.AdminCommandPoints;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.www.client.events.loading.ArmiesLoadedEvent;
import com.eaw1805.www.client.events.loading.ArmiesLoadedHandler;
import com.eaw1805.www.client.events.loading.BtrainLoadedEvent;
import com.eaw1805.www.client.events.loading.BtrainLoadedHandler;
import com.eaw1805.www.client.events.loading.CommLoadedEvent;
import com.eaw1805.www.client.events.loading.CommLoadedHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.NavyLoadedEvent;
import com.eaw1805.www.client.events.loading.NavyLoadedHandler;
import com.eaw1805.www.client.events.loading.SpiesLoadedEvent;
import com.eaw1805.www.client.events.loading.SpiesLoadedHandler;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.movement.MovementGroup;
import com.eaw1805.www.client.movement.TilesGroup;
import com.eaw1805.www.client.views.popups.OrdersViewerPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.orders.movement.AddMovementOrder;
import com.eaw1805.www.shared.orders.movement.RemoveMovementOrder;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import com.eaw1805.www.shared.stores.units.UnitService;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;
import org.vaadin.gwtgraphics.client.Group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class is final.
 * We only need one army manager for our application and only once the data.
 */
public final class MovementStore
        implements ArmyConstants, OrderConstants, GoodConstants {

    /**
     * A Map of <ARMYTYPE,<ID,MOVEMENTS>>.
     */
    private transient Map<Integer, Map<Integer, MovementDTO>> mvMap;

    /**
     * Our instance of the MovementManager.
     */
    private static transient MovementStore ourInstance = null;

    // Variable telling us if our data are initialized
    // and if the manager is being used on the client or
    // server side.
    private boolean isInitialized = false;

    /**
     * A map that contains all moves of all units
     * <regionId : <unit typeId : <unitId : vector>>>
     */
    private final transient Map<Integer, Map<Integer, Map<Integer, MovementGroup>>> mvGroupMap;

    /**
     * A map that contains all the line moves to be displayed when selecting the "movements" option
     * on settings panel
     * <regionId : <unitType : <unitId : Group>>>
     */
    private final transient Map<Integer, Map<Integer, Map<Integer, Group>>> mvLinesGroupMap;

    /**
     * Default constructor.
     */
    private MovementStore() {
        mvMap = new HashMap<Integer, Map<Integer, MovementDTO>>();
        mvGroupMap = new HashMap<Integer, Map<Integer, Map<Integer, MovementGroup>>>();
        mvLinesGroupMap = new HashMap<Integer, Map<Integer, Map<Integer, Group>>>();
    }

    // Method returning the army manager
    public static MovementStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new MovementStore();
        }
        return ourInstance;
    }

    // Method Used By the async callback to initialize  the movement manager
    public void initDbMovements(final Map<Integer, Map<Integer, MovementDTO>> typeMvMap) {
        try {
            this.mvMap = typeMvMap;
            isInitialized = true;
            //generate all movement groups and line groups
            loadPathLinesAndMovementGroups();

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize movements due to unexcpected reason", false);
        }
    }

    public void loadPathLinesAndMovementGroups() {
        mvLinesGroupMap.clear();

        if (NavyStore.getInstance().isInitialized()) {
            initNavyMovementGroups(new ArrayList<FleetDTO>(NavyStore.getInstance().getIdFleetMap().values()));
        } else {
            LoadEventManager.addNavyLoadedHandler(new NavyLoadedHandler() {
                public void onNavyLoaded(final NavyLoadedEvent event) {
                    initNavyMovementGroups(event.getFleets());
                }
            });
        }

        if (BaggageTrainStore.getInstance().isInitialized()) {
            initBaggageTrainMovementGroups(BaggageTrainStore.getInstance().getBaggageTList());
        } else {
            LoadEventManager.addBtrainLoadedHandler(new BtrainLoadedHandler() {
                public void onBtrainLoaded(final BtrainLoadedEvent event) {
                    initBaggageTrainMovementGroups(event.getBtrains());
                }
            });

        }

        if (ArmyStore.getInstance().isArmiesInitialized()) {
            initArmiesMovementGroups(ArmyStore.getInstance().getcArmiesList());
        } else {
            LoadEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
                public void onArmiesLoaded(final ArmiesLoadedEvent event) {
                    initArmiesMovementGroups(event.getArmies());
                }
            });
        }

        if (CommanderStore.getInstance().isInitialized()) {
            initCommandersMovementGroups(CommanderStore.getInstance().getCommandersList());
        } else {
            LoadEventManager.addCommLoadeddHandler(new CommLoadedHandler() {
                public void onCommLoaded(final CommLoadedEvent event) {
                    initCommandersMovementGroups(event.getCommanders());
                }
            });
        }

        if (SpyStore.getInstance().isInitialized()) {
            initSpiesMovementGroups(SpyStore.getInstance().getSpyList());
        } else {
            LoadEventManager.addSpiesLoadedHandler(new SpiesLoadedHandler() {
                public void onSpiesLoaded(final SpiesLoadedEvent event) {
                    initSpiesMovementGroups(event.getSpies());
                }
            });
        }
    }

    //load navy movements
    public void initNavyMovementGroups(final List<FleetDTO> fleets) {
        try {
            for (FleetDTO fleet : fleets) {
                if (fleet.getFleetId() == 0) {
                    for (ShipDTO ship : fleet.getShips().values()) {
                        final MovementDTO movement = getMvDTOByTypeAndId(SHIP, ship.getId());
                        if (movement != null) {
                            for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                                final PathDTO thisPath = movement.getPaths().get(index);
                                final TilesGroup tlGroup = new TilesGroup(SHIP, thisPath, movement.getForcedMarch(), movement.getPatrol());
                                addMovementLinesGroup(tlGroup.getPathTiles(), ship.getRegionId(), SHIP, ship.getId(), false);
                            }
                        }
                    }
                } else {
                    final MovementDTO movement = getMvDTOByTypeAndId(FLEET, fleet.getFleetId());
                    if (movement != null) {
                        for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                            final PathDTO thisPath = movement.getPaths().get(index);
                            final TilesGroup tlGroup = new TilesGroup(FLEET, thisPath, movement.getForcedMarch(), movement.getPatrol());
                            addMovementLinesGroup(tlGroup.getPathTiles(), fleet.getRegionId(), FLEET, fleet.getFleetId(), false);
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    public void initBaggageTrainMovementGroups(final List<BaggageTrainDTO> bTrains) {
        try {
            for (BaggageTrainDTO bTrain : bTrains) {
                final MovementDTO movement = getMvDTOByTypeAndId(BAGGAGETRAIN, bTrain.getId());
                if (movement != null) {
                    for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                        final PathDTO thisPath = movement.getPaths().get(index);
                        final TilesGroup tlGroup = new TilesGroup(BAGGAGETRAIN, thisPath, movement.getForcedMarch(), movement.getPatrol());
                        addMovementLinesGroup(tlGroup.getPathTiles(), bTrain.getRegionId(), BAGGAGETRAIN, bTrain.getId(), false);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void initArmiesMovementGroups(final Collection<ArmyDTO> armies) {
        try {
            for (final ArmyDTO army : armies) {
                if (army.getArmyId() == 0) {
                    for (final CorpDTO corp : army.getCorps().values()) {
                        if (corp.getCorpId() == 0) {
                            for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                                final MovementDTO movement = getMvDTOByTypeAndId(BRIGADE, brigade.getBrigadeId());
                                if (movement != null) {
                                    for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                                        final PathDTO thisPath = movement.getPaths().get(index);
                                        final TilesGroup tlGroup = new TilesGroup(BRIGADE, thisPath, movement.getForcedMarch(), movement.getPatrol());
                                        addMovementLinesGroup(tlGroup.getPathTiles(), brigade.getRegionId(), BRIGADE, brigade.getBrigadeId(), false);
                                    }
                                }
                            }

                        } else {
                            final MovementDTO movement = getMvDTOByTypeAndId(CORPS, corp.getCorpId());
                            if (movement != null) {
                                for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                                    final PathDTO thisPath = movement.getPaths().get(index);
                                    final TilesGroup tlGroup = new TilesGroup(CORPS, thisPath, movement.getForcedMarch(), movement.getPatrol());
                                    addMovementLinesGroup(tlGroup.getPathTiles(), corp.getRegionId(), CORPS, corp.getCorpId(), false);
                                }
                            }
                        }
                    }

                } else {
                    final MovementDTO movement = getMvDTOByTypeAndId(ARMY, army.getArmyId());
                    if (movement != null) {
                        for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                            final PathDTO thisPath = movement.getPaths().get(index);
                            final TilesGroup tlGroup = new TilesGroup(ARMY, thisPath, movement.getForcedMarch(), movement.getPatrol());
                            addMovementLinesGroup(tlGroup.getPathTiles(), army.getRegionId(), ARMY, army.getArmyId(), false);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void initCommandersMovementGroups(final List<CommanderDTO> commanders) {
        try {
            for (final CommanderDTO commander : commanders) {
                final MovementDTO movement = getMvDTOByTypeAndId(COMMANDER, commander.getId());
                if (movement != null) {
                    for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                        final PathDTO thisPath = movement.getPaths().get(index);
                        final TilesGroup tlGroup = new TilesGroup(COMMANDER, thisPath, movement.getForcedMarch(), movement.getPatrol());
                        addMovementLinesGroup(tlGroup.getPathTiles(), commander.getRegionId(), COMMANDER, commander.getId(), false);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void initSpiesMovementGroups(final List<SpyDTO> spies) {
        try {
            for (final SpyDTO spy : spies) {
                final MovementDTO movement = getMvDTOByTypeAndId(SPY, spy.getSpyId());
                if (movement != null) {
                    for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                        final PathDTO thisPath = movement.getPaths().get(index);
                        final TilesGroup tlGroup = new TilesGroup(SPY, thisPath, movement.getForcedMarch(), movement.getPatrol());
                        addMovementLinesGroup(tlGroup.getPathTiles(), spy.getRegionId(), SPY, spy.getSpyId(), false);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Method used to add a new movement order.
     *
     * @param unitType    the unit type.
     * @param unitId      the unit ID.
     * @param path        the sectors visited.
     * @param forcedMarch if this is a forced march.
     * @param patrol      if it is a patrol order.
     */
    public void addNewMovementOrder(final int unitType,
                                    final int unitId,
                                    final PathDTO path,
                                    final boolean forcedMarch,
                                    final boolean patrol) {
        final MovementDTO movementDto = new MovementDTO();
        movementDto.setArmyType(unitType);
        movementDto.setId(unitId);
        movementDto.addPaths(path);
        movementDto.setForcedMarch(forcedMarch);
        movementDto.setPatrol(patrol);
        movementDto.setRegionId(MapStore.getInstance().getActiveRegion());
        int hasFunds = 1;

        final int movementOrderId = getMovementOrderByArmyType(unitType);
        final MovementDTO thisDTO = getMvDTOByTypeAndId(unitType, unitId);
        if (thisDTO == null || thisDTO.getPaths().isEmpty()) {
            final int[] ids = new int[3];
            ids[0] = unitType;
            ids[1] = unitId;
            ids[2] = movementOrderId;
            hasFunds = OrderStore.getInstance().addNewOrder(ORDER_M_UNIT, CostCalculators.getMovementCost(movementOrderId), 1, "", ids, 0, "");
        }

        int success = 0;
        if (hasFunds == 1) {
            final AddMovementOrder addMvOrder = new AddMovementOrder(movementDto, getMvMap());
            final String previousPos = UnitService.getInstance().getUnitByTypeAndId(unitType, unitId).positionToString();
            success = addMvOrder.execute(unitId);
            UnitService.getInstance().updateIndexes(unitType, unitId, previousPos);

            MovementEventManager.doMovement(unitType, unitId);
        }

        if (success != 1) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to add movement order!", false);
            return;
        }

        MovementStore.getInstance().restartUnitMovement(movementDto.getRegionId(), unitType, unitId);
        UnitEventManager.changeUnit(unitType, unitId);
    }


    /**
     * Method used to undo a movement.
     *
     * @param unitType    the unit type.
     * @param unitId      the unit ID.
     * @param paths       the sectors visited.
     * @param forcedMarch if this is a forced march.
     */
    public void removeMovementOrder(final int unitType,
                                    final int unitId,
                                    final List<PathDTO> paths,
                                    final boolean forcedMarch) {
        final MovementDTO movementDto = new MovementDTO();
        movementDto.setArmyType(unitType);
        movementDto.setId(unitId);
        movementDto.setPaths(paths);
        movementDto.setForcedMarch(forcedMarch);

        final RemoveMovementOrder rmMvOrder = new RemoveMovementOrder(movementDto, getMvMap());

        final String previousPos = UnitService.getInstance().getUnitByTypeAndId(unitType, unitId).positionToString();
        final int success = rmMvOrder.execute(unitId);
        UnitService.getInstance().updateIndexes(unitType, unitId, previousPos);

        if (success == 1) {
            final int movementOrderId = getMovementOrderByArmyType(unitType);
            final MovementDTO mvSavedDTO = getMvDTOByTypeAndId(unitType, unitId);
            if ((mvSavedDTO == null
                    || mvSavedDTO.getPaths().isEmpty()
                    || (mvSavedDTO.getPaths().size() == 1
                    && mvSavedDTO.getPaths().get(0).getPathSectors().size() == 0))
                    && (!mvSavedDTO.getPatrol())) {
                final int[] ids = new int[3];
                ids[0] = unitType;
                ids[1] = unitId;
                ids[2] = movementOrderId;
                OrderStore.getInstance().removeOrder(ORDER_M_UNIT, ids);

            }
            MovementEventManager.unDoMovement(unitType, unitId);
        } else {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to remove movement order!", false);

        }

    }

    /**
     * Method that removes a movement in it's entirety
     *
     * @param movementOrderId the type of the unit movement
     * @param unitId          the id of the unit movement
     */
    public void undoMovementEntirely(final int movementOrderId, final int unitId) {
        try {
            final int unitType = getTypeByMovementOrder(movementOrderId);
            if (getMvDTOByTypeAndId(unitType, unitId) != null) {
                if (TradeStore.getInstance().hasInitSecondPhase(unitType, unitId)) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The unit has initiated second phase (trade/transport)", false);
                } else {
                    final int[] ids = new int[3];
                    ids[0] = unitType;
                    ids[1] = unitId;
                    ids[2] = movementOrderId;
                    final int regionId = getMvDTOByTypeAndId(unitType, unitId).getRegionId();
                    if (OrderStore.getInstance().removeOrder(ORDER_M_UNIT, ids)) {
                        if (mvGroupMap.containsKey(regionId) && mvGroupMap.get(regionId).containsKey(unitType) &&
                                mvGroupMap.get(regionId).get(unitType).containsKey(unitId)) {
                            mvGroupMap.get(regionId).get(unitType).remove(unitId);
                        }
                        mvMap.get(unitType).remove(unitId);
                        restartUnitMovement(regionId, unitType, unitId);

                        final PositionDTO pos = UnitService.getInstance().getUnitByTypeAndId(unitType, unitId);
                        final String previousPos = pos.positionToString();
                        pos.setX(pos.getXStart());
                        pos.setY(pos.getYStart());
                        UnitService.getInstance().updateIndexes(unitType, unitId, previousPos);
                        switch (unitType) {
                            case ARMY:
                                final ArmyDTO army = ArmyStore.getInstance().getArmyById(unitId);
                                if (army.getCommander() != null) {
                                    army.getCommander().setX(army.getXStart());
                                    army.getCommander().setY(army.getYStart());
                                }
                                for (CorpDTO corps : army.getCorps().values()) {
                                    corps.setX(army.getXStart());
                                    corps.setY(army.getYStart());
                                    for (BrigadeDTO brigade : corps.getBrigades().values()) {
                                        brigade.setX(army.getXStart());
                                        brigade.setY(army.getYStart());
                                    }
                                    if (corps.getCommander() != null) {
                                        corps.getCommander().setX(army.getXStart());
                                        corps.getCommander().setY(army.getYStart());
                                    }
                                }
                                break;

                            case CORPS:
                                final CorpDTO corps = ArmyStore.getInstance().getCorpByID(unitId);
                                if (corps.getCommander() != null) {
                                    corps.getCommander().setX(corps.getXStart());
                                    corps.getCommander().setY(corps.getYStart());
                                }
                                for (BrigadeDTO brigade : corps.getBrigades().values()) {
                                    brigade.setX(corps.getXStart());
                                    brigade.setY(corps.getYStart());
                                }
                                break;

                            case FLEET:
                                final FleetDTO fleet = NavyStore.getInstance().getFleetById(unitId);
                                for (ShipDTO ship : fleet.getShips().values()) {
                                    ship.setX(fleet.getXStart());
                                    ship.setY(fleet.getYStart());
                                }
                                break;
                            default:
                                break;
                        }

                        MovementEventManager.unDoMovement(unitType, unitId);
                        MovementEventManager.stopMovement(unitType, unitId);
                    }

                }
            }
            UnitEventManager.changeUnit(unitType, unitId);
        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to remove movement", false);
        }
    }

    /**
     * Method that returns movement dtos by unit type and unit id or null when the list is empty.
     *
     * @param unitType the type of unit.
     * @param unitId   the id of the unit.
     * @return the movement DTO (if exists).
     */
    public MovementDTO getMvDTOByTypeAndId(final int unitType, final int unitId) {
        try {
            return mvMap.get(unitType).get(unitId);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Method that answers if the the unit has moved this turn
     *
     * @param unitType the type of the target unit
     * @param unitId   the id of the target unit
     * @return true if it has moved and false if it hasn't
     */
    public boolean hasMovedThisTurn(final int unitType, final int unitId) {
        final MovementDTO mvDTO = getMvDTOByTypeAndId(unitType, unitId);
        return (mvDTO != null) && !mvDTO.getPaths().isEmpty();
    }

    /**
     * Method that returns the units position based on any movement done this
     * round
     *
     * @param unitType the type of the target unit
     * @param unitId   the id of the target unit
     * @param startPos the starting position of the unit
     * @return the starting position if it has not moved or the position based on the movement it
     *         has done
     */
    public PositionDTO getUnitPosition(final int unitType, final int unitId, final PositionDTO startPos) {
        PositionDTO currPoss = null;
        final MovementDTO mvDTO = getMvDTOByTypeAndId(unitType, unitId);
        if (mvDTO != null
                && !mvDTO.getPaths().isEmpty()) {
            final int totPaths = mvDTO.getPaths().size();
            final PathDTO lastPath = mvDTO.getPaths().get(totPaths - 1);

            final int totSectors = lastPath.getPathSectors().size();
            if (lastPath.getPathSectors() != null && lastPath.getPathSectors().size() > 0) {
                currPoss = lastPath.getPathSectors().get(totSectors - 1);
            }
        }
        if (currPoss == null) {
            return startPos;
        } else {
            return currPoss;
        }
    }

    /**
     * Method that returns the units position based on any movement done this
     * round
     *
     * @param unitType the type of the target unit
     * @param unitId   the id of the target unit
     * @return null if it has not moved or the position based on the movement it
     *         has done
     */
    public PositionDTO getUnitPosition(final int unitType, final int unitId) {
        PositionDTO currPoss = null;
        final MovementDTO mvDTO = getMvDTOByTypeAndId(unitType, unitId);
        if (mvDTO != null
                && !mvDTO.getPaths().isEmpty()) {
            final int totPaths = mvDTO.getPaths().size();
            final PathDTO lastPath = mvDTO.getPaths().get(totPaths - 1);

            final int totSectors = lastPath.getPathSectors().size();
            currPoss = lastPath.getPathSectors().get(totSectors - 1);
        }
        return currPoss;
    }

    /**
     * Method that returns movement order by army type
     *
     * @param armyType the unit type.
     * @return the corresponding order
     */
    private int getMovementOrderByArmyType(final int armyType) {
        switch (armyType) {
            case ARMY:
                return ORDER_M_ARMY;

            case CORPS:
                return ORDER_M_CORP;

            case BRIGADE:
                return ORDER_M_BRIG;

            case BAGGAGETRAIN:
                return ORDER_M_BTRAIN;

            case SPY:
                return ORDER_M_SPY;

            case SHIP:
                return ORDER_M_SHIP;

            case FLEET:
                return ORDER_M_FLEET;

            case COMMANDER:
                return ORDER_M_COMM;

            default:
                return 0;
        }
    }

    /**
     * Method that returns army type by movement order
     *
     * @param movementOrderId the order type.
     * @return the corresponding army type
     */
    private int getTypeByMovementOrder(final int movementOrderId) {
        switch (movementOrderId) {
            case ORDER_M_ARMY:
            case ORDER_FM_ARMY:
                return ARMY;

            case ORDER_M_CORP:
            case ORDER_FM_CORP:
                return CORPS;

            case ORDER_M_BRIG:
            case ORDER_FM_BRIG:
                return BRIGADE;

            case ORDER_M_BTRAIN:
                return BAGGAGETRAIN;

            case ORDER_M_SPY:
                return SPY;

            case ORDER_M_SHIP:
            case ORDER_P_SHIP:
                return SHIP;

            case ORDER_M_FLEET:
            case ORDER_P_FLEET:
                return FLEET;

            case ORDER_M_COMM:
                return COMMANDER;

            default:
                return 0;
        }
    }

    public Map<Integer, Map<Integer, MovementDTO>> getMvMap() {
        return mvMap;
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

    public List<Integer> getFleetsFromCoords(final PositionDTO position) {
        final List<Integer> fleetIds = new ArrayList<Integer>();
        if (getMvMap().containsKey(FLEET)) {
            for (final Integer fleetId : getMvMap().get(FLEET).keySet()) {
                final PositionDTO pos = getUnitPosition(FLEET, fleetId, position);
                if (pos.equals(position)) {
                    fleetIds.add(fleetId);
                }
            }
        }
        return fleetIds;
    }

    public List<Integer> getShipsFromCoords(final PositionDTO position) {
        final List<Integer> shipIds = new ArrayList<Integer>();
        if (getMvMap().containsKey(SHIP)) {
            for (Integer shipId : getMvMap().get(SHIP).keySet()) {
                final PositionDTO pos = getUnitPosition(SHIP, shipId, position);
                if (pos.equals(position)) {
                    shipIds.add(shipId);
                }
            }
        }
        return shipIds;
    }

    /**
     * Checks if a unit can move.
     *
     * @param unitType The unit type.
     * @param unitId   The unit id.
     * @return True if unit can move.
     */
    public boolean canUnitMove(final int unitType, final int unitId) {
        //check for conflicts
        //first check second phase trade
        List<Integer> possibleShips = new ArrayList<Integer>();
        if (unitType == FLEET) {//check if ship appears in a fleet
            final FleetDTO fleet = NavyStore.getInstance().getFleetById(unitId);
            for (ShipDTO ship : fleet.getShips().values()) {
                possibleShips.add(ship.getId());
            }
        }
        for (ClientOrderDTO order : OrderStore.getInstance().getOrdersByTypes(new int[]{ORDER_EXCHS})) {
            if (unitType == FLEET) {//special check for fleets
                if (order.getIdentifier(0) == SHIP && possibleShips.contains(Integer.valueOf(order.getIdentifier(1)))
                        || order.getIdentifier(2) == SHIP && possibleShips.contains(Integer.valueOf(order.getIdentifier(3)))) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot move a unit that has participated in a second phase trading action.", false);
                    return false;
                }
            }
            //global check
            if ((order.getIdentifier(0) == unitType && order.getIdentifier(1) == unitId)
                    || (order.getIdentifier(2) == unitType && order.getIdentifier(3) == unitId)) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot move a unit that has participated in a second phase trading action.", false);
                return false;
            }

        }

        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        final List<ClientOrderDTO> currentOrders = OrderStore.getInstance().getOrdersByTypes(new int[]{ORDER_LOAD_TROOPSS, ORDER_UNLOAD_TROOPSS});
        for (ClientOrderDTO order : currentOrders) {
            if ((order.getIdentifier(0) == unitType && order.getIdentifier(1) == unitId)
                    || (order.getIdentifier(2) == unitType && order.getIdentifier(3) == unitId)) {
                conflictOrders.add(order);
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING,
                    "Cannot move a unit that has participated in a second phase loading/unloading. Review conflict orders?", true) {
                public void onAccept() {
                    super.onAccept();
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                    viewer.show();
                    viewer.center();
                }
            };

        } else {
            if (WarehouseStore.getInstance().getWareHouseByRegion(RegionConstants.EUROPE).getGoodsDTO().get(GOOD_CP).getQte() < AdminCommandPoints.P_COM.get(getMovementOrderByArmyType(unitType))
                    && getMvDTOByTypeAndId(unitType, unitId) == null) {
                GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(false, GOOD_CP);
                return false;
            }
        }


        return conflictOrders.isEmpty();
        //done checking for conflicts
    }

    public void setPatrolState(final int unitType, final int unitId, final boolean state) {
        boolean found = false;
        if (getMvMap().containsKey(unitType)
                && getMvMap().get(unitType).containsKey(unitId)) {
            if (hasMovedThisTurn(unitType, unitId)) {
                found = true;
                // if the unit is a fleet or a ship and
                // the movement changes to patrol then
                // relocate to the first position
                // otherwise move to the proper position
                if ((unitType == FLEET || unitType == SHIP) && state) {
                    final PositionDTO pos = UnitService.getInstance().getUnitByTypeAndId(unitType, unitId);
                    pos.setX(pos.getXStart());
                    pos.setY(pos.getYStart());

                } else if ((unitType == FLEET || unitType == SHIP) && !state) {
                    final MovementDTO mvDTO = getMvMap().get(unitType).get(unitId);
                    final PositionDTO mvUnit = MovementStore.getInstance()
                            .getUnitPosObjectByTypeAndId(mvDTO.getArmyType(), unitId);
                    final int totPaths = mvDTO.getPaths().size();
                    final PathDTO lastPath = mvDTO.getPaths().get(totPaths - 1);
                    final int totSectors = lastPath.getPathSectors().size();

                    final PositionDTO currPoss = lastPath.getPathSectors()
                            .get(totSectors - 1);
                    mvUnit.setX(currPoss.getX());
                    mvUnit.setY(currPoss.getY());
                }
            }
            final MovementDTO mvDTO = getMvMap().get(unitType).get(unitId);
            mvDTO.setPatrol(state);
            if ((mvDTO.getPaths().size() == 0 || (mvDTO.getPaths().size() == 1
                    && mvDTO.getPaths().get(0).getPathSectors().size() == 0)) && !state) {
                removeMovementOrder(unitType, unitId, mvDTO.getPaths(), mvDTO.getForcedMarch());
            }
        }
        if (!found) {
            addNewMovementOrder(unitType, unitId, new PathDTO(), false, true);
        }
    }

    /**
     * Add a movement group to store.
     *
     * @param mvGrp    The movement group.
     * @param regionId The region.
     * @param typeId   The unit type.
     * @param unitId   The unit id.
     */
    public void addMovementGroup(final MovementGroup mvGrp, final int regionId, final int typeId, final int unitId) {
        Map<Integer, Map<Integer, MovementGroup>> map1 = mvGroupMap.get(regionId);
        if (map1 == null) {
            map1 = new HashMap<Integer, Map<Integer, MovementGroup>>();
            mvGroupMap.put(regionId, map1);
        }

        Map<Integer, MovementGroup> map2 = map1.get(typeId);
        if (map2 == null) {
            map2 = new HashMap<Integer, MovementGroup>();
            map1.put(typeId, map2);
        }
        map2.put(unitId, mvGrp);
    }

    /**
     * Get the movement group for the queried unit.
     *
     * @param unitType The unit type.
     * @param unitId   The unit id.
     * @param regionId The region the unit is.
     * @return The movement group for this unit.
     */
    public MovementGroup getMovementGroup(final int unitType, final int unitId, final int regionId) {
        MovementGroup out = null;
        if (mvGroupMap.containsKey(regionId)
                && mvGroupMap.get(regionId).containsKey(unitType)
                && mvGroupMap.get(regionId).get(unitType).containsKey(unitId)) {
            out = mvGroupMap.get(regionId).get(unitType).get(unitId);
        }

        return out;
    }

    public void restartUnitMovement(final int regionId, final int typeId, final int unitId) {
        final Map<Integer, Map<Integer, Group>> map1 = mvLinesGroupMap.get(regionId);
        if (map1 != null) {
            final Map<Integer, Group> map2 = map1.get(typeId);
            if (map2 != null
                    && map2.containsKey(unitId)) {
                final Group thisGroup = map2.get(unitId);
                try {
                    MapStore.getInstance().getMapsView().getAllMovements().remove(thisGroup);
                } catch (Exception ignore) {
                }
                map2.remove(unitId);
            }
        }

        //draw the new lines....
        final MovementDTO movement = getMvDTOByTypeAndId(typeId, unitId);
        if (movement != null) {
            for (int index = 0; index <= movement.getPaths().size() - 1; index++) {
                final PathDTO thisPath = movement.getPaths().get(index);
                final TilesGroup tlGroup = new TilesGroup(typeId, thisPath, movement.getForcedMarch(), movement.getPatrol());
                addMovementLinesGroup(tlGroup.getPathTiles(), regionId, typeId, unitId, false);
            }
        }
    }

    /**
     * Add a movement group to store.
     *
     * @param mvGrp      The movement group.
     * @param regionId   The region.
     * @param typeId     The unit type.
     * @param unitId     The unit id.
     * @param replaceOld to replace the old movement group.
     */
    public void addMovementLinesGroup(final Group mvGrp, final int regionId, final int typeId, final int unitId, final boolean replaceOld) {
        Map<Integer, Map<Integer, Group>> map1 = mvLinesGroupMap.get(regionId);
        if (map1 == null) {
            map1 = new HashMap<Integer, Map<Integer, Group>>();
            mvLinesGroupMap.put(regionId, map1);
        }

        Map<Integer, Group> map2 = map1.get(typeId);
        if (map2 == null) {
            map2 = new HashMap<Integer, Group>();
            map1.put(typeId, map2);
        }

        if (map2.containsKey(unitId)) {
            try {
                MapStore.getInstance().getMapsView().getAllMovements().remove(map2.get(unitId));
            } catch (Exception ignore) {
            }

            if (replaceOld) {
                //and put the new group
                map2.put(unitId, mvGrp);
            } else {
                map2.get(unitId).add(mvGrp);

            }

        } else {
            map2.put(unitId, mvGrp);
        }

        if (GameStore.getInstance().isShowMovement() && MapStore.getInstance().getActiveRegion() == regionId) {
            try {
                MapStore.getInstance().getMapsView().getAllMovements().add(map2.get(unitId));
            } catch (Exception ignore) {
            }
        }
    }

    public void highLightPath(final int regionId, final int typeId, final int unitId) {
        //first hide everything
        for (Map<Integer, Map<Integer, Group>> typeToLines : mvLinesGroupMap.values()) {
            for (Map<Integer, Group> unitToLines : typeToLines.values()) {
                for (Group line : unitToLines.values()) {
                    line.setVisible(false);
                }
            }
        }

        //then show the wanted one if exist
        if (mvLinesGroupMap.containsKey(regionId) &&
                mvLinesGroupMap.get(regionId).containsKey(typeId)
                && mvLinesGroupMap.get(regionId).get(typeId).containsKey(unitId)) {
            mvLinesGroupMap.get(regionId).get(typeId).get(unitId).setVisible(true);
        }
    }

    public void highLightAllPaths() {
        //display everything
        for (Map<Integer, Map<Integer, Group>> typeToLines : mvLinesGroupMap.values()) {
            for (Map<Integer, Group> unitToLines : typeToLines.values()) {
                for (Group line : unitToLines.values()) {
                    line.setVisible(true);
                }
            }
        }
    }

    /**
     * Get all movement groups by region.
     *
     * @param regionId The region to get the movement groups
     * @return A list with movement groups
     */
    public List<Group> getMovementLinesGroupsByRegion(final int regionId) {
        final List<Group> out = new ArrayList<Group>();
        if (mvLinesGroupMap.containsKey(regionId)) {
            final Map<Integer, Map<Integer, Group>> maps1 = mvLinesGroupMap.get(regionId);
            if (!maps1.isEmpty()) {
                for (Map<Integer, Group> maps2 : maps1.values()) {
                    if (maps2.size() > 0) {
                        out.addAll(maps2.values());
                    }
                }
            }
        }
        return out;
    }

    /**
     * Method that returns the position object
     *
     * @param unitType the type of the unit
     * @param unitId   the id of the unit
     * @return the position object
     */
    public PositionDTO getUnitPosObjectByTypeAndId(final int unitType, final int unitId) {
        switch (unitType) {
            case ARMY:
                return ArmyStore.getInstance().getcArmies().get(unitId);

            case CORPS:
                return ArmyStore.getInstance().getCorpByID(unitId);

            case BRIGADE:
                return ArmyStore.getInstance().getBrigadeById(unitId);

            case COMMANDER:
                return CommanderStore.getInstance().getCommanderById(unitId);

            case SPY:
                return SpyStore.getInstance().getSpyById(unitId);

            case BAGGAGETRAIN:
                return BaggageTrainStore.getInstance().getBaggageTMap().get(unitId);

            case FLEET:
                return NavyStore.getInstance().getIdFleetMap().get(unitId);

            case SHIP:
                return NavyStore.getInstance().getShipById(unitId);

            default:
                break;
        }

        return null;
    }

}
