package empire.webapp.shared.stores.units;

import empire.data.constants.ArmyConstants;
import empire.data.constants.GoodConstants;
import empire.data.constants.NavigationConstants;
import empire.data.constants.OrderConstants;
import empire.data.constants.RegionConstants;
import empire.data.constants.WeightCalculators;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.CargoUnitDTO;
import empire.data.dto.web.ClientOrderDTO;
import empire.data.dto.web.OrderCostDTO;
import empire.data.dto.web.TradeUnitAbstractDTO;
import empire.data.dto.web.TransportUnitDTO;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BattalionDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.SpyDTO;
import empire.data.dto.web.economy.BaggageTrainDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.webapp.client.events.deploy.DeployEventManager;
import empire.webapp.client.events.units.UnitEventManager;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.orders.army.ChangeBrigadeCorpOrder;
import empire.webapp.shared.orders.army.CommanderJoinArmyOrder;
import empire.webapp.shared.orders.army.CommanderJoinCorpOrder;
import empire.webapp.shared.orders.army.CommanderLeaveFederationOrder;
import empire.webapp.shared.stores.MovementStore;
import empire.webapp.shared.stores.RegionStore;
import empire.webapp.shared.stores.economy.OrderStore;
import empire.webapp.shared.stores.economy.TradeStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Provides the client with methods used for
 * loading unloading transport-able units
 */
public class TransportStore
        implements ArmyConstants, RegionConstants, OrderConstants, NavigationConstants, GoodConstants {

    /**
     * Our instance of the Manager
     */
    private static transient TransportStore ourInstance = null;

    /**
     * Method returning the manager
     *
     * @return the store object
     */
    public static TransportStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new TransportStore();
        }
        return ourInstance;
    }

    /**
     * Method that returns transport units of a certain type for a
     * certain region.
     *
     * @param regionId the id of the region.
     * @param type     the type of the transport unit.
     * @return a List of the available units.
     */
    public List<TransportUnitDTO> getTransportUnitsByRegionAndType(final int regionId, final int type) {
        final List<TransportUnitDTO> trUnits = new ArrayList<TransportUnitDTO>();
        //boolean afterMovement = (tradePhase == 2);
        switch (type) {
            case BAGGAGETRAIN:
                trUnits.addAll(BaggageTrainStore.getInstance().getBaggageTrainsByRegion(regionId, true));
                trUnits.addAll(AlliedUnitsStore.getInstance().getBaggageTrainsByRegion(regionId));
                break;

            case SHIP:
                trUnits.addAll(NavyStore.getInstance().getShipsByRegion(regionId, true));
                break;

            case FLEET:
                trUnits.addAll(NavyStore.getInstance().getFleetsByRegion(regionId, true));
                trUnits.addAll(AlliedUnitsStore.getInstance().getFleetsByRegion(regionId));
                final Iterator<TransportUnitDTO> iter = trUnits.iterator();
                while (iter.hasNext()) {
                    final FleetDTO fleet = (FleetDTO) iter.next();
                    if (fleet.getFleetId() == 0) {
                        iter.remove();
                    }
                }
                break;

            default:
                break;
        }

        return trUnits;
    }

    public List<TransportUnitDTO> getTransportUnitsBySectorAndType(final SectorDTO sector, final int type) {
        final List<TransportUnitDTO> trUnits = new ArrayList<TransportUnitDTO>();
        //boolean afterMovement = (tradePhase == 2);
        switch (type) {
            case BAGGAGETRAIN:
                trUnits.addAll(BaggageTrainStore.getInstance().getBaggageTrainsBySector(sector, true));
                trUnits.addAll(AlliedUnitsStore.getInstance().getBaggageTrainsBySector(sector));
                break;

            case SHIP:
                trUnits.addAll(NavyStore.getInstance().getShipsBySector(sector, true));
                trUnits.addAll(AlliedUnitsStore.getInstance().getShipsBySector(sector));

                break;

            case FLEET:
                trUnits.addAll(NavyStore.getInstance().getFleetsByRegionAndTile(sector, true, true));
                trUnits.addAll(AlliedUnitsStore.getInstance().getAlliedFleetsBySector(sector));
                Iterator<TransportUnitDTO> iter = trUnits.iterator();
                while (iter.hasNext()) {
                    final FleetDTO fleet = (FleetDTO) iter.next();
                    if (fleet == null || fleet.getFleetId() == 0) {
                        iter.remove();
                    }
                }
                break;

            default:
                break;
        }
        return trUnits;
    }

    public List<TransportUnitDTO> getAllTransportUnitsBySector(final SectorDTO sector) {
        final List<TransportUnitDTO> trUnits = new ArrayList<TransportUnitDTO>();
        trUnits.addAll(getTransportUnitsBySectorAndType(sector, BAGGAGETRAIN));
        trUnits.addAll(getTransportUnitsBySectorAndType(sector, SHIP));
        trUnits.addAll(getTransportUnitsBySectorAndType(sector, FLEET));
        return trUnits;
    }

    /**
     * Method that returns cargo units of a certain type for a
     * certain position.
     *
     * @param type       the type of the cargo unit.
     * @param regionId   the target region id.
     * @param xPos       the target xPos position.
     * @param yPos       the target yPos position.
     * @param tradePhase the loading phase.
     * @return a List of the available cargo units.
     */
    public List<CargoUnitDTO> getCargoUnitsByRegionTypeAndPosition(final int transportType,
                                                                   final int type,
                                                                   final int regionId,
                                                                   final int xPos,
                                                                   final int yPos,
                                                                   final int tradePhase) {
        final boolean afterMovement = (tradePhase == 2);
        final List<CargoUnitDTO> cgUnits = new ArrayList<CargoUnitDTO>();
        switch (type) {
            case BRIGADE:
                if (transportType == BAGGAGETRAIN) {
                    for (BrigadeDTO brig : ArmyStore.getInstance().getBrigadesByPosition(regionId, xPos, yPos, afterMovement)) {
                        boolean add = true;
                        for (BattalionDTO batt : brig.getBattalions()) {
                            if (!batt.getEmpireArmyType().isInfantry()) {
                                add = false;
                                break;
                            }
                        }
                        if (!add) {
                            continue;
                        }
                        cgUnits.add(brig);
                    }
                } else {
                    cgUnits.addAll(ArmyStore.getInstance().getBrigadesByPosition(regionId, xPos, yPos, afterMovement));
                }
                break;

            case COMMANDER:
                cgUnits.addAll(CommanderStore.getInstance().getCommandersByPositionWithMovement(regionId, xPos, yPos, afterMovement, true));
                break;

            case SPY:
                cgUnits.addAll(SpyStore.getInstance().getSpiesByPositionWithMovement(regionId, xPos, yPos, afterMovement));
                break;

            default:
                break;
        }
        return cgUnits;
    }

    /**
     * Method that answers if a transport can carry a specific
     * cargo.
     *
     * @param transportType the type of the transport to carry.
     * @param transportId   the id of the transport.
     * @param cargoType     the type of the cargo to be carried.
     * @param cargoId       the id of the cargo to be carried.
     * @return if the transport can carry the specific cargo.
     */
    public boolean canCarryLoad(final int transportType,
                                final int transportId,
                                final int cargoType,
                                final int cargoId) {
        final int cargoLoaded;
        int cargoToLoad = 0;
        if (cargoType == BRIGADE) {
            cargoToLoad += WeightCalculators.getBrigadeWeight(ArmyStore.getInstance().getBrigadeById(cargoId));

        } else if (cargoType == COMMANDER || cargoType == SPY) {
            return true;
        }

        final int availableLoad;
        final TradeUnitAbstractDTO tUnit = getTransportUnitById(transportType, transportId);

        cargoLoaded = TradeStore.getInstance().getTradeUnitLoad(tUnit) +
                getUnitsLoadedWeight(transportType, transportId, true) + cargoToLoad;

        availableLoad = TradeStore.getInstance().getTradeUnitWeight(tUnit);
        return (availableLoad >= cargoLoaded);
    }

    /**
     * Return the total weight of the units loaded
     * on this transport unit
     *
     * @param transportType the type of the transport unit
     * @param transportId   the id of the transport unit
     * @return the number of the tons
     */
    public int getUnitsLoadedWeight(final int transportType, final int transportId, final boolean searchFleet) {
        int totalUnitLoad = 0;
        final TransportUnitDTO tUnit = getTransportUnitById(transportType, transportId);

        for (Integer type : tUnit.getLoadedUnitsMap().keySet()) {
            for (int i = 0; i < tUnit.getLoadedUnitsMap().get(type).size(); i++) {
                if (type == BRIGADE) {
                    final int brigadeId = tUnit.getLoadedUnitsMap().get(type).get(i);
                    totalUnitLoad += WeightCalculators.getBrigadeWeight((BrigadeDTO) getCargoUnitById(BRIGADE, brigadeId));
                }
            }
        }
        if (transportType == SHIP && searchFleet) {
            totalUnitLoad += getShipLoadByFleet((ShipDTO) TransportStore.getInstance().getTransportUnitById(SHIP, transportId));
        }
        return totalUnitLoad;
    }

    /**
     * Get how much load the ship  gets because of the fleet load.
     * This load is the minimum for every ship as all the other ships got all the other load.
     *
     * @param curShip The ship to look up.
     * @return The amount of weight the ship gets because of the fleet.
     */
    public int getShipLoadByFleet(final ShipDTO curShip) {
        int totalFleetLoad = 0;
        if (curShip.getFleet() > 0) {
            FleetDTO fleet = NavyStore.getInstance().getFleetById(curShip.getFleet());
            if (fleet == null) {//then check allied
                fleet = AlliedUnitsStore.getInstance().getFleetById(curShip.getFleet());
            }
            totalFleetLoad = TransportStore.getInstance().getUnitsLoadedWeight(FLEET, curShip.getFleet(), false);
            for (final ShipDTO ship : fleet.getShips().values()) {
                if (ship.getId() != curShip.getId()) {
                    totalFleetLoad -=
                            (TradeStore.getInstance().getTradeUnitWeight(ship) - (TradeStore.getInstance().getTradeUnitLoad(ship) + TransportStore.getInstance()
                                    .getUnitsLoadedWeight(SHIP, ship.getId(), false)));
                }
            }
        }
        if (totalFleetLoad < 0) {
            totalFleetLoad = 0;
        }
        return totalFleetLoad;
    }

    public boolean canLoadUnload1st(final int cargoType, final int cargoId) {
        final List<ClientOrderDTO> movementOrders = OrderStore.getInstance().getOrdersByTypes(new int[]{ORDER_M_UNIT});
        try {
            if (movementOrders != null) {
                for (ClientOrderDTO order : movementOrders) {
                    if (order.getIdentifier(0) == cargoType
                            && order.getIdentifier(1) == cargoId) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot load a unit that has been moved", false);
                        return false;
                    }
                }
            }
        } catch (Exception ignore) {
        }
        return true;
    }

    /**
     * Method that loads a cargo unit to a transport.
     *
     * @param transportType the type of the transport to carry.
     * @param transportId   the id of the transport.
     * @param cargoType     the type of the cargo to be carried.
     * @param cargoId       the id of the cargo to be carried.
     * @param phase         the phase of load (1st or 2nd).
     * @return the result of the operation.
     */
    public boolean loadCargoToTransport(final int transportType, final int transportId,
                                        final int cargoType, final int cargoId, final int phase) {
        //check for conflicts
        if (phase == 1) {
            if (!canLoadUnload1st(cargoType, cargoId)) {
                return false;
            }
            if (!canLoadUnload1st(transportType, transportId)) {
                return false;
            }
        }
        //done checking for conflicts

        final TransportUnitDTO tUnit = getTransportUnitById(transportType, transportId);
        final CargoUnitDTO cUnit = getCargoUnitById(cargoType, cargoId);

        final int[] ids = new int[9];
        ids[0] = transportType;
        ids[1] = transportId;
        ids[2] = cargoType;
        ids[3] = cargoId;

        if (cargoType == BRIGADE) {
            final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(cargoId);
            ids[4] = brig.getCorpId();
        } else if (cargoType == COMMANDER) {
            final CommanderDTO comm = CommanderStore.getInstance().getCommanderById(cargoId);
            ids[4] = comm.getCorp();
            ids[5] = comm.getArmy();
        }

        final int orderType;
        final OrderCostDTO cost = new OrderCostDTO();

        if (phase == 1) {
            orderType = ORDER_LOAD_TROOPSF;
        } else {
            orderType = ORDER_LOAD_TROOPSS;
        }
        final SectorDTO unitSector = RegionStore.getInstance().getSectorByPosition(tUnit);
        if (!unitSector.hasShipyardOrBarracks()
                && cargoType == BRIGADE) {
            cost.setNumericCost(GOOD_CP, 1);
        }

        if (!hasUnitLoadOrder(cargoType, cargoId) &&
                OrderStore.getInstance().addNewOrder(orderType, cost, tUnit.getRegionId(), "", ids, 0, "") == 1) {
            if (!tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                tUnit.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
            }
            tUnit.getLoadedUnitsMap().get(cargoType).add(cargoId);
            if (cargoType == BRIGADE) {
                final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(cargoId);
                if (brig.getCorpId() > 0) {
                    final ChangeBrigadeCorpOrder cbcOrder = new ChangeBrigadeCorpOrder(ArmyStore.getInstance().getcArmies(), 0);
                    cbcOrder.execute(cargoId);
                    ArmyStore.getInstance().applyChangesToPanels();
                    UnitEventManager.changeUnit(CORPS, ids[4]);
                    UnitEventManager.changeUnit(CORPS, 0);
                    UnitEventManager.changeUnit(BRIGADE, cargoId);
                }
            } else if (cargoType == COMMANDER) {
                final CommanderDTO commander = CommanderStore.getInstance().getCommanderById(cargoId);
                if (commander.getArmy() != 0 || commander.getCorp() != 0) {
                    final Map<Integer, ArmyDTO> armiesMap = ArmyStore.getInstance().getcArmies();
                    final CommanderLeaveFederationOrder cjaOrder = new CommanderLeaveFederationOrder(armiesMap, commander);
                    cjaOrder.execute(cargoId);
                    UnitEventManager.changeUnit(COMMANDER, cargoId);
                    UnitEventManager.changeUnit(CORPS, ids[4]);
                    UnitEventManager.changeUnit(ARMY, ids[5]);
                }
            }

//            UnitService.getInstance().removeFromFederation(cargoType, cargoId);
            cUnit.setLoaded(true);

            UnitEventManager.changeUnit(cargoType, cargoId);
            UnitEventManager.changeUnit(transportType, transportId);

            DeployEventManager.reportEmbark(transportType, transportId, cargoType, cargoId);
            return true;

        } else {
            UnitEventManager.changeUnit(transportType, transportId);
            UnitEventManager.changeUnit(cargoType, cargoId);
            return false;
        }
    }

    /**
     * Method that unloads a cargo unit from a transport.
     *
     * @param transportType the type of the transport.
     * @param transportId   the id of the transport.
     * @param cargoType     the type of the cargo to be unloaded.
     * @param cargoId       the id of the cargo to be unloaded.
     * @param direction     the direction to unload.
     * @param phase         the phase of unload.
     * @return the result of the operation.
     */
    public boolean unloadCargoFromTransport(final int transportType,
                                            final int transportId,
                                            final int cargoType,
                                            final int cargoId,
                                            final int direction,
                                            final int phase) {

        //check for conflicts
        if (phase == 1) {
            if (!canLoadUnload1st(cargoType, cargoId)) {
                return false;
            }
            if (!canLoadUnload1st(transportType, transportId)) {
                return false;
            }
        }

        final TransportUnitDTO tUnit = getTransportUnitById(transportType, transportId);
        final CargoUnitDTO cUnit = getCargoUnitById(cargoType, cargoId);

        final int[] ids = new int[9];
        ids[0] = transportType;
        ids[1] = transportId;
        ids[2] = cargoType;
        ids[3] = cargoId;
        ids[4] = direction;
        if (phase == 1) {
            ids[5] = tUnit.getXStart();
            ids[6] = tUnit.getYStart();

        } else {
            ids[5] = tUnit.getX();
            ids[6] = tUnit.getY();

        }

        final int orderType;
        if (phase == 1) {
            orderType = ORDER_UNLOAD_TROOPSF;
        } else {
            orderType = ORDER_UNLOAD_TROOPSS;
        }

        final OrderCostDTO cost = new OrderCostDTO();
        final SectorDTO unitSector = RegionStore.getInstance().getSectorByPosition(tUnit);
        if (!unitSector.hasShipyardOrBarracks()
                && cargoType == BRIGADE) {
            cost.setNumericCost(GOOD_CP, 1);
        }
        if (!hasUnitLoadOrder(cargoType, cargoId) &&
                OrderStore.getInstance().addNewOrder(orderType, cost, tUnit.getRegionId(), "", ids, 0, "") == 1) {
            if (!tUnit.getLoadedUnitsMap().containsKey(cargoType)) {
                tUnit.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
            }
            tUnit.getLoadedUnitsMap().get(cargoType).remove(Integer.valueOf(cargoId));
            cUnit.setLoaded(false);
            final int x;
            final int y;
            if (phase == 1) {
                x = tUnit.getXStart();
                y = tUnit.getYStart();
            } else {
                x = tUnit.getX();
                y = tUnit.getY();
            }
            switch (direction) {
                case NORTH:
                    cUnit.setX(x);
                    cUnit.setY(y - 1);
                    break;

                case SOUTH:
                    cUnit.setX(x);
                    cUnit.setY(y + 1);
                    break;

                case EAST:
                    cUnit.setX(x + 1);
                    cUnit.setY(y);
                    break;

                case WEST:
                    cUnit.setX(x - 1);
                    cUnit.setY(y);
                    break;

                default:
                    cUnit.setX(x);
                    cUnit.setY(y);
                    break;
            }
            //since it is unload. This is the first time the unit stands on land and has actual position.
            //so update its xstart/ystart
            cUnit.setXStart(cUnit.getX());
            cUnit.setYStart(cUnit.getY());

            UnitEventManager.changeUnit(cargoType, cargoId);
            UnitEventManager.changeUnit(transportType, transportId);

            DeployEventManager.reportDisembark(transportType, transportId, cargoType, cargoId);
            return true;

        } else {
            UnitEventManager.changeUnit(transportType, transportId);
            UnitEventManager.changeUnit(cargoType, cargoId);
            return false;
        }
    }

    public boolean hasUnitLoadOrder(final int cargoType, final int cargoId) {
        for (final ClientOrderDTO order : OrderStore.getInstance().getLoadUnloadOrders()) {
            if (order.getIdentifier(2) == cargoType && order.getIdentifier(3) == cargoId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that checks if the target sector has unit
     * available to transport armies
     *
     * @param pos the target sector
     * @return true if the units in questions exist and
     *         false otherwise
     */
    public boolean hasTransportUnits(final PositionDTO pos) {
        SectorDTO sector = RegionStore.getInstance().getSectorByPosition(pos);
        return NavyStore.getInstance().hasShipsToEmbark(pos, true) ||
                BaggageTrainStore.getInstance().hasBtrainsToEmbark(pos) ||
                AlliedUnitsStore.getInstance().hasBaggageTrainsToEmbark(sector) ||
                AlliedUnitsStore.getInstance().hasShipsToEmbark(sector);
    }

    public boolean hasUnitsToBoard(final PositionDTO pos) {
        return !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX() - 1, pos.getY() - 1, true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX(), pos.getY() - 1, true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX() + 1, pos.getY() - 1, true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX() - 1, pos.getY(), true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX(), pos.getY(), true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX() + 1, pos.getY(), true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX() - 1, pos.getY() + 1, true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX(), pos.getY() + 1, true).isEmpty()
                || !ArmyStore.getInstance().getBrigadesByPosition(pos.getRegionId(), pos.getX() + 1, pos.getY() + 1, true).isEmpty()

                ||!CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX() - 1, pos.getY() - 1, true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX(), pos.getY() - 1, true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX() + 1, pos.getY() - 1, true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX() - 1, pos.getY(), true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX(), pos.getY(), true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX() + 1, pos.getY(), true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX() - 1, pos.getY() + 1, true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX(), pos.getY() + 1, true, true).isEmpty()
                || !CommanderStore.getInstance().getCommandersByPositionWithMovement(pos.getRegionId(), pos.getX() + 1, pos.getY() + 1, true, true).isEmpty()

                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX() - 1, pos.getY() - 1, true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX(), pos.getY() - 1, true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX() + 1, pos.getY() - 1, true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX() - 1, pos.getY(), true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX(), pos.getY(), true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX() + 1, pos.getY(), true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX() - 1, pos.getY() + 1, true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX(), pos.getY() + 1, true).isEmpty()
                || !SpyStore.getInstance().getSpiesByPositionWithMovement(pos.getRegionId(), pos.getX() + 1, pos.getY() + 1, true).isEmpty();

    }

    /**
     * Method returning the transport unit by its id
     *
     * @param transportUnitType the type of the transport unit
     * @param transportId       the id of the transport unit
     * @return the transport unit
     */
    public TransportUnitDTO getTransportUnitById(final int transportUnitType, final int transportId) {
        switch (transportUnitType) {
            case BAGGAGETRAIN:
                BaggageTrainDTO bTrain = BaggageTrainStore.getInstance().getBaggageTMap().get(transportId);
                if (bTrain == null) {
                    bTrain = AlliedUnitsStore.getInstance().getBaggageTrainById(transportId);
                }
                return bTrain;

            case SHIP:
                ShipDTO ship = NavyStore.getInstance().getShipById(transportId);
                if (ship == null) {
                    ship = AlliedUnitsStore.getInstance().getShipById(transportId);
                }
                return ship;

            default:
                FleetDTO fleet = NavyStore.getInstance().getIdFleetMap().get(transportId);
                if (fleet == null) {
                    fleet = AlliedUnitsStore.getInstance().getFleetById(transportId);
                }
                return fleet;
        }
    }

    /**
     * Method returning the cargo unit by its id
     *
     * @param cargoUnitType the type of the cargo unit
     * @param cargoUnitId   the id of the cargo unit
     * @return the cargo unit
     */
    public CargoUnitDTO getCargoUnitById(final int cargoUnitType, final int cargoUnitId) {
        switch (cargoUnitType) {
            case BRIGADE:
                BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(cargoUnitId);
                if (brig == null) {
                    brig = AlliedUnitsStore.getInstance().getBrigadeById(cargoUnitId);
                }
                return brig;

            case COMMANDER:
                CommanderDTO comm = CommanderStore.getInstance().getCommanderById(cargoUnitId);
                if (comm == null) {
                    comm = AlliedUnitsStore.getInstance().getCommanderById(cargoUnitId);
                }
                return comm;

            default:
                SpyDTO spy = SpyStore.getInstance().getSpyById(cargoUnitId);
                if (spy == null) {
                    spy = AlliedUnitsStore.getInstance().getSpyById(cargoUnitId);
                }
                return spy;
        }
    }

    /**
     * Method that returns the horizontal and vertical coordinates by the direction.
     *
     * @param direction the target direction.
     * @param xPos      the horizontal coordinate.
     * @param yPos      the vertical coordinate.
     * @return an int[2] array with xNew at 0 and yNew at 1
     */
    public static int[] getCoordsByDirection(final int direction, final int xPos, final int yPos) {
        final int[] newCoords = new int[2];
        switch (direction) {
            case NORTH:
                newCoords[0] = xPos;
                newCoords[1] = yPos - 1;
                break;

            case SOUTH:
                newCoords[0] = xPos;
                newCoords[1] = yPos + 1;
                break;

            case EAST:
                newCoords[0] = xPos + 1;
                newCoords[1] = yPos;
                break;

            case WEST:
                newCoords[0] = xPos - 1;
                newCoords[1] = yPos;
                break;

            default:
                newCoords[0] = xPos;
                newCoords[1] = yPos;
                break;
        }
        return newCoords;
    }


    /**
     * Method that informs us if the unit has initiated the second
     * transport phase and thus cannot move
     *
     * @param unitType    the type of the unit
     * @param tradeUnitId the id of the unit
     * @return true if it has otherwise false
     */
    public boolean hasInitSecondTransportPhase(final int unitType, final int tradeUnitId) {
        for (final ClientOrderDTO order : OrderStore.getInstance().getCargoRelatedOrders()) {
            if ((order.getOrderTypeId() == ORDER_LOAD_TROOPSS || order.getOrderTypeId() == ORDER_UNLOAD_TROOPSS)
                    && ((order.getIdentifier(0) == unitType && order.getIdentifier(1) == tradeUnitId) ||
                    (order.getIdentifier(2) == unitType && order.getIdentifier(3) == tradeUnitId))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method that will undo the loading of a unit on a ship
     *
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     * @return true if the undo action was successful
     */

    public boolean undoLoadUnitFromTransport(final int cargoType, final int cargoId) {
        ClientOrderDTO targetOrder = null;
        for (ClientOrderDTO order : OrderStore.getInstance().getCargoRelatedOrders()) {
            if ((order.getOrderTypeId() == ORDER_LOAD_TROOPSF || order.getOrderTypeId() == ORDER_LOAD_TROOPSS)
                    && order.getIdentifier(2) == cargoType && order.getIdentifier(3) == cargoId) {

                // Check if the transport unit has already been moved or started
                // the second phase
                if (order.getOrderTypeId() == ORDER_LOAD_TROOPSF
                        && (MovementStore.getInstance().hasMovedThisTurn(order.getIdentifier(0), order.getIdentifier(1)) ||
                        TradeStore.getInstance().hasInitSecondPhase(order.getIdentifier(0), order.getIdentifier(1)))) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The transport unit that contains the cargo unit has moved or init the second phase", false);
                } else {
                    targetOrder = order;
                }
                break;

            }
        }
        // If there is such an order
        if (targetOrder == null) {
            return false;
        } else {
            int[] ids = new int[7];
            for (int i = 0; i < 7; i++) {
                ids[i] = targetOrder.getIdentifier(i);
            }
            // If you succeed in removing the unload order
            if (OrderStore.getInstance().removeOrder(targetOrder.getOrderTypeId(), ids)) {
                final TransportUnitDTO tUDto = getTransportUnitById(targetOrder.getIdentifier(0), targetOrder.getIdentifier(1));
                final CargoUnitDTO cUnit = getCargoUnitById(cargoType, cargoId);
                //revert cargo unit to loaded status
                //add the item into the cargo
                for (int i = 0; i < tUDto.getLoadedUnitsMap().get(cargoType).size(); i++) {
                    if (tUDto.getLoadedUnitsMap().get(cargoType).get(i) == cargoId) {
                        tUDto.getLoadedUnitsMap().get(cargoType).remove(i);
                    }
                }
                cUnit.setLoaded(false);
                if (cargoType == BRIGADE) {
                    if (targetOrder.getIdentifier(4) > 0) {

                        final ChangeBrigadeCorpOrder cbcOrder = new ChangeBrigadeCorpOrder(ArmyStore.getInstance().getcArmies(), targetOrder.getIdentifier(4));
                        cbcOrder.execute(cargoId);
                        ArmyStore.getInstance().applyChangesToPanels();
                        UnitEventManager.changeUnit(CORPS, targetOrder.getIdentifier(4));
                        UnitEventManager.changeUnit(CORPS, 0);
                        UnitEventManager.changeUnit(BRIGADE, cargoId);


                    }
                } else if (cargoType == COMMANDER) {
                    if (targetOrder.getIdentifier(4) > 0) {
                        final CommanderDTO commander = CommanderStore.getInstance().getCommandersMap().get(cargoId);
                        final Map<Integer, ArmyDTO> armiesMap = ArmyStore.getInstance().getcArmies();
                        final CommanderJoinCorpOrder cjaOrder = new CommanderJoinCorpOrder(armiesMap, commander);
                        cjaOrder.execute(targetOrder.getIdentifier(4));
                        UnitEventManager.changeUnit(COMMANDER, cargoId);
                        UnitEventManager.changeUnit(CORPS, targetOrder.getIdentifier(4));
                    }
                    if (targetOrder.getIdentifier(5) > 0) {
                        final CommanderDTO commander = CommanderStore.getInstance().getCommandersMap().get(cargoId);
                        final Map<Integer, ArmyDTO> armiesMap = ArmyStore.getInstance().getcArmies();
                        final CommanderJoinArmyOrder cjaOrder = new CommanderJoinArmyOrder(armiesMap, commander);
                        cjaOrder.execute(targetOrder.getIdentifier(5));
                        UnitEventManager.changeUnit(COMMANDER, cargoId);
                        UnitEventManager.changeUnit(ARMY, targetOrder.getIdentifier(5));
                    }
                }

                //inform all interested parties of the change
                UnitEventManager.changeUnit(targetOrder.getIdentifier(0), targetOrder.getIdentifier(1));
                UnitEventManager.changeUnit(cargoType, cargoId);
                DeployEventManager.reportEmbark(targetOrder.getIdentifier(0), targetOrder.getIdentifier(1), cargoType, cargoId);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Method that will undo the unloading of a cargo unit
     *
     * @param cargoType the type of the cargo
     * @param cargoId   the id of the cargo
     * @return true if the undo action was successful
     */
    public boolean undoUnloadUnitFromTransport(final int cargoType, final int cargoId) {
        ClientOrderDTO targetOrder = null;
        for (ClientOrderDTO order : OrderStore.getInstance().getCargoRelatedOrders()) {
            if ((order.getOrderTypeId() == ORDER_UNLOAD_TROOPSF || order.getOrderTypeId() == ORDER_UNLOAD_TROOPSS)
                    && order.getIdentifier(2) == cargoType && order.getIdentifier(3) == cargoId) {
                // Check if the unloaded unit has already been moved
                if (order.getOrderTypeId() == ORDER_UNLOAD_TROOPSF && MovementStore.getInstance().hasMovedThisTurn(cargoType, cargoId)) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The unit you are trying to load-back has already been moved", false);
                } else {
                    targetOrder = order;
                }
                break;
            }
        }
        // If there is such an order
        if (targetOrder == null) {
            return false;
        } else {
            int[] ids = new int[7];
            for (int i = 0; i < 7; i++) {
                ids[i] = targetOrder.getIdentifier(i);
            }

            // If you succeed in removing the unload order
            if (OrderStore.getInstance().removeOrder(targetOrder.getOrderTypeId(), ids)) {
                final TransportUnitDTO tUDto = getTransportUnitById(targetOrder.getIdentifier(0), targetOrder.getIdentifier(1));
                final CargoUnitDTO cUnit = getCargoUnitById(cargoType, cargoId);
                if (!tUDto.getLoadedUnitsMap().containsKey(cargoType)) {
                    tUDto.getLoadedUnitsMap().put(cargoType, new ArrayList<Integer>());
                }
                //revert cargo unit to loaded status
                //add the item into the cargo
                tUDto.getLoadedUnitsMap().get(cargoType).add(cargoId);
                cUnit.setLoaded(true);

                //revert position
                final PositionDTO prevPos = MovementStore.getInstance().getUnitPosition(cargoType, cargoId);
                if (prevPos == null) {
                    cUnit.setX(cUnit.getXStart());
                    cUnit.setY(cUnit.getYStart());
                } else {
                    cUnit.setX(prevPos.getX());
                    cUnit.setY(prevPos.getY());
                }

                //inform all interested parties of the change
                UnitEventManager.changeUnit(targetOrder.getIdentifier(0), targetOrder.getIdentifier(1));
                UnitEventManager.changeUnit(cargoType, cargoId);
                DeployEventManager.reportEmbark(targetOrder.getIdentifier(0), targetOrder.getIdentifier(1), cargoType, cargoId);

                return true;
            } else {
                return false;
            }
        }
    }

}
