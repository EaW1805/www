package com.eaw1805.www.shared.stores.units;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.AlliedMovement;
import com.eaw1805.www.shared.AlliedUnits;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlliedUnitsStore implements ArmyConstants, GoodConstants, OrderConstants {

    private Map<Integer, Map<Integer, AlliedUnits>> alliedUnitsMap;

    private final Map<Integer, SpyDTO> spiesMap = new HashMap<Integer, SpyDTO>();
    private final Map<Integer, CommanderDTO> commandersMap = new HashMap<Integer, CommanderDTO>();
    private final Map<Integer, BaggageTrainDTO> bTrainsMap = new HashMap<Integer, BaggageTrainDTO>();
    private final Map<Integer, FleetDTO> fleetMap = new HashMap<Integer, FleetDTO>();
    private final Map<Integer, ShipDTO> shipMap = new HashMap<Integer, ShipDTO>();
    private final Map<Integer, ArmyDTO> armiesMap = new HashMap<Integer, ArmyDTO>();
    private final Map<Integer, CorpDTO> corpsMap = new HashMap<Integer, CorpDTO>();
    private final Map<Integer, BrigadeDTO> brigadeMap = new HashMap<Integer, BrigadeDTO>();

    /**
     * Our instance of the Manager.
     */
    private static AlliedUnitsStore ourInstance = null;

    /**
     * Method returning the AlliedUnitsStore manager if already initialized
     *
     * @return the store instance
     */
    public static AlliedUnitsStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new AlliedUnitsStore();
        }
        return ourInstance;
    }

    public void loadGood(final int tradeUnitType, final int goodId, final int unitId, final int qte) {
        if (tradeUnitType == BAGGAGETRAIN) {
            bTrainsMap.get(unitId).getGoodsDTO().get(goodId).setQte(bTrainsMap.get(unitId).getGoodsDTO().get(goodId).getQte() + qte);

        } else if (tradeUnitType == SHIP) {
            shipMap.get(unitId).getGoodsDTO().get(goodId).setQte(shipMap.get(unitId).getGoodsDTO().get(goodId).getQte() + qte);
        }
    }

    public void unLoadGood(final int tradeUnitType, final Integer goodId, final int unitId, final int qte) {
        if (tradeUnitType == BAGGAGETRAIN) {
            bTrainsMap.get(unitId).getGoodsDTO().get(goodId).setQte(bTrainsMap.get(unitId).getGoodsDTO().get(goodId).getQte() - qte);

        } else if (tradeUnitType == SHIP) {
            shipMap.get(unitId).getGoodsDTO().get(goodId).setQte(shipMap.get(unitId).getGoodsDTO().get(goodId).getQte() - qte);
        }
    }

    // Initialize the allied units as we took them from the DataBase
    public void initAlliedUnits(final Map<Integer, Map<Integer, AlliedUnits>> alliedUnitsMap) {
        try {
            this.alliedUnitsMap = alliedUnitsMap;
            try {
                initIndexes();
            } catch (Exception e) {
//                Window.alert("failed to init allied indexes : " + e.toString());
            }
            LoadEventManager.loadAlliedUnits(alliedUnitsMap);
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize allied units due to unexpected reason", false);
            LoadEventManager.loadAlliedUnits(alliedUnitsMap);
        }

    }

    public void initIndexes() {
        for (int regionId = 1; regionId <= 4; regionId++) {
            for (final AlliedUnits alliedUnits : alliedUnitsMap.get(regionId).values()) {
                for (final List<SpyDTO> spies : alliedUnits.getSpies().values()) {
                    for (SpyDTO spy : spies) {
                        spiesMap.put(spy.getSpyId(), spy);
                    }
                }
                for (final List<CommanderDTO> commanders : alliedUnits.getCommanders().values()) {
                    for (final CommanderDTO commander : commanders) {
                        commandersMap.put(commander.getId(), commander);
                    }
                }
                for (final List<BaggageTrainDTO> bTrains : alliedUnits.getBaggageTrains().values()) {
                    for (final BaggageTrainDTO bTrain : bTrains) {
                        bTrainsMap.put(bTrain.getId(), bTrain);
                    }
                }
                for (final Map<Integer, FleetDTO> fleets : alliedUnits.getFleets().values()) {
                    for (final Map.Entry<Integer, FleetDTO> entry : fleets.entrySet()) {
                        if (entry.getKey() != 0) {
                            fleetMap.put(entry.getKey(), entry.getValue());
                        }
                        for (ShipDTO ship : entry.getValue().getShips().values()) {
                            shipMap.put(ship.getId(), ship);
                        }
                    }
                }
                for (final Map<Integer, ArmyDTO> armies : alliedUnits.getArmies().values()) {
                    for (final Map.Entry<Integer, ArmyDTO> entry : armies.entrySet()) {
                        if (entry.getKey() != 0) {
                            armiesMap.put(entry.getKey(), entry.getValue());
                        }
                        for (final CorpDTO corps : entry.getValue().getCorps().values()) {
                            if (corps.getCorpId() != 0) {
                                corpsMap.put(corps.getCorpId(), corps);
                            }
                            brigadeMap.putAll(corps.getBrigades());
                        }
                    }
                }
            }
        }
    }

    public List<TradeUnitAbstractDTO> getTradeUnitsByRegionTypeAndPhase(final int type, final int region) {
        final List<TradeUnitAbstractDTO> out = new ArrayList<TradeUnitAbstractDTO>();
        if (type == BAGGAGETRAIN) {
            for (BaggageTrainDTO bTrain : bTrainsMap.values()) {
                if (!bTrain.isScuttle() && bTrain.getRegionId() == region) {
                    out.add(bTrain);
                }
            }
        } else if (type == SHIP) {
            for (ShipDTO ship : shipMap.values()) {
                if (!ship.isScuttle() && ship.getRegionId() == region) {
                    out.add(ship);
                }
            }
        }
        return out;
    }


    public List<TradeUnitAbstractDTO> getTradeUnitsByRegionTypePhasePos(final int type, final int regionId, final int x, final int y, final int phase) {
        final List<TradeUnitAbstractDTO> out = new ArrayList<TradeUnitAbstractDTO>();
        if (phase == 1) {
            if (type == BAGGAGETRAIN) {
                for (BaggageTrainDTO bTrain : bTrainsMap.values()) {
                    if (!bTrain.isScuttle() && bTrain.getXStart() == x && bTrain.getYStart() == y && bTrain.getRegionId() == regionId) {
                        out.add(bTrain);
                    }
                }
            } else if (type == SHIP) {
                for (ShipDTO ship : shipMap.values()) {
                    if (!ship.isScuttle() && ship.getXStart() == x && ship.getYStart() == y && ship.getRegionId() == regionId) {
                        out.add(ship);
                    }
                }
            }
        } else {
            if (type == BAGGAGETRAIN) {
                for (BaggageTrainDTO bTrain : bTrainsMap.values()) {
                    if (!bTrain.isScuttle() && bTrain.getX() == x && bTrain.getY() == y && bTrain.getRegionId() == regionId) {
                        out.add(bTrain);
                    }
                }
            } else if (type == SHIP) {
                for (ShipDTO ship : shipMap.values()) {
                    if (!ship.isScuttle() && ship.getX() == x && ship.getY() == y && ship.getRegionId() == regionId) {
                        out.add(ship);
                    }
                }
            }
        }
        return out;
    }

    public List<ArmyDTO> getAlliedArmiesBySector(final SectorDTO sector) {
        final List<ArmyDTO> alliedArmies = new ArrayList<ArmyDTO>();
        for (Integer nationId : alliedUnitsMap.get(sector.getRegionId()).keySet()) {
            if (alliedUnitsMap.get(sector.getRegionId()).containsKey(nationId)
                    && alliedUnitsMap.get(sector.getRegionId()).get(nationId).getArmies().containsKey(sector.getId())) {
                for (ArmyDTO army : alliedUnitsMap.get(sector.getRegionId()).get(nationId).getArmies().get(sector.getId()).values()) {
                    alliedArmies.add(army);
                }
            }
        }
        return alliedArmies;
    }

    public List<ArmyDTO> getArmiesByRegion(final int regionId, final int nationId) {
        final List<ArmyDTO> alliedArmies = new ArrayList<ArmyDTO>();
        if (alliedUnitsMap.get(regionId).containsKey(nationId)
                && alliedUnitsMap.get(regionId).get(nationId).getArmies() != null) {
            for (Integer scId : alliedUnitsMap.get(regionId).get(nationId).getArmies().keySet()) {
                for (ArmyDTO army : alliedUnitsMap.get(regionId).get(nationId).getArmies().get(scId).values()) {
                    alliedArmies.add(army);
                }
            }
        }
        return alliedArmies;
    }

    public ArmyDTO getAlliedArmyById(final int infoId) {
        return armiesMap.get(infoId);
    }

    public CorpDTO getCorpByID(final int infoId) {
        return corpsMap.get(infoId);
    }

    public BrigadeDTO getBrigadeById(final int infoId) {
        return brigadeMap.get(infoId);
    }

    public List<PathDTO> getMovementByTypeAndId(final int infoType, final int infoId, final int regionId, final int nationId) {
        for (int sectorId : alliedUnitsMap.get(regionId).get(nationId).getMovements().keySet()) {
            for (AlliedMovement allMv : alliedUnitsMap.get(regionId).get(nationId).getMovements().get(sectorId)) {
                if (allMv.getUnitType() == infoType && allMv.getId() == infoId) {
                    return allMv.getSectorPaths();
                }
            }
        }
        return null;
    }

    public boolean hasUnitMoved(final int infoType, final int infoId, final int regionId, final int nationId) {
        try {
            for (int sectorId : alliedUnitsMap.get(regionId).get(nationId).getMovements().keySet()) {
                for (AlliedMovement allMv : alliedUnitsMap.get(regionId).get(nationId).getMovements().get(sectorId)) {
                    if (allMv.getUnitType() == infoType && allMv.getId() == infoId) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {}
        return false;
    }

    public List<FleetDTO> getAlliedFleetsBySector(final SectorDTO sector) {
        try {
            final List<FleetDTO> alliedFleets = new ArrayList<FleetDTO>();
            for (int nationId : alliedUnitsMap.get(sector.getRegionId()).keySet()) {
                if (alliedUnitsMap.get(sector.getRegionId()).containsKey(nationId)
                        && alliedUnitsMap.get(sector.getRegionId()).get(nationId).getFleets().containsKey(sector.getId())) {
                    for (FleetDTO fleet : alliedUnitsMap.get(sector.getRegionId()).get(nationId).getFleets().get(sector.getId()).values()) {
                        alliedFleets.add(fleet);
                    }
                }
            }
            return alliedFleets;
        } catch (Exception e) {
            return new ArrayList<FleetDTO>();
        }
    }

    public List<FleetDTO> getNavyBySector(final SectorDTO position) {
        final List<FleetDTO> fleetList = getAlliedFleetsBySector(position);
        final List<ShipDTO> freeShips = getFreeShipsByPosition(position);
        if (!freeShips.isEmpty()) {
            FleetDTO dummyFleet = new FleetDTO();
            dummyFleet.setRegionId(position.getRegionId());
            for (ShipDTO ship : freeShips) {
                dummyFleet.getShips().put(ship.getId(), ship);
            }
            fleetList.add(dummyFleet);
        }
        return fleetList;
    }

    public List<FleetDTO> getFleetsByRegion(final int regionId, final int nationId) {
        final List<FleetDTO> alliedFleets = new ArrayList<FleetDTO>();
        if (alliedUnitsMap.get(regionId).containsKey(nationId)
                && alliedUnitsMap.get(regionId).get(nationId).getFleets() != null) {
            for (int scId : alliedUnitsMap.get(regionId).get(nationId).getFleets().keySet()) {
                for (FleetDTO army : alliedUnitsMap.get(regionId).get(nationId).getFleets().get(scId).values()) {
                    alliedFleets.add(army);
                }
            }
        }
        return alliedFleets;
    }

    /**
     * Get allied fleets by region.
     *
     * @param regionId The region to lookup.
     * @return A list with fleets for the given region.
     */
    public List<FleetDTO> getFleetsByRegion(final int regionId) {
        final List<FleetDTO> alliedFleets = new ArrayList<FleetDTO>();
        for (int nationId : alliedUnitsMap.get(regionId).keySet()) {
            if (alliedUnitsMap.get(regionId).containsKey(nationId)
                    && alliedUnitsMap.get(regionId).get(nationId).getFleets() != null) {
                for (Integer scId : alliedUnitsMap.get(regionId).get(nationId).getFleets().keySet()) {
                    for (final FleetDTO fleet : alliedUnitsMap.get(regionId).get(nationId).getFleets().get(scId).values()) {
                        alliedFleets.add(fleet);
                    }
                }
            }
        }
        return alliedFleets;
    }

    public List<BaggageTrainDTO> getBaggageTrainsByRegion(final int regionId) {
        final List<BaggageTrainDTO> alliedBTrains = new ArrayList<BaggageTrainDTO>();
        for (int nationId : alliedUnitsMap.get(regionId).keySet()) {
            if (alliedUnitsMap.get(regionId).get(nationId).getBaggageTrains() != null) {
                for (List<BaggageTrainDTO> btList : alliedUnitsMap.get(regionId).get(nationId).getBaggageTrains().values()) {
                    alliedBTrains.addAll(btList);
                }
            }
        }
        return alliedBTrains;
    }

    public FleetDTO getFleetById(final int infoId) {
        return fleetMap.get(infoId);
    }

    /**
     * Lookup ship given its id and its nation.
     *
     * @param infoId The ships id.
     * @return The ship with the given id.
     */
    public ShipDTO getShipById(final int infoId) {
        return shipMap.get(infoId);
    }


    /**
     * Get the baggage train with the given id that belongs to the given nation.
     *
     * @param infoId The baggage train id.
     * @return The baggage train itself.
     */
    public BaggageTrainDTO getBaggageTrainById(final int infoId) {
        return bTrainsMap.get(infoId);
    }


    public SpyDTO getSpyById(final int infoId) {
        return spiesMap.get(infoId);
    }

    public CommanderDTO getCommanderById(final int infoId) {
        return commandersMap.get(infoId);
    }

    public List<SpyDTO> getSpiesBySector(final SectorDTO sector) {
        try {
            final List<SpyDTO> out = new ArrayList<SpyDTO>();
            final Map<Integer, AlliedUnits> map = alliedUnitsMap.get(sector.getRegionId());
            if (map != null) {
                for (AlliedUnits alliedUnitsMap : map.values()) {
                    if (alliedUnitsMap.getSpies() != null
                            && alliedUnitsMap.getSpies().containsKey(sector.getId())) {
                        out.addAll(alliedUnitsMap.getSpies().get(sector.getId()));
                    }
                }
            }
            return out;
        } catch (Exception e) {
            return new ArrayList<SpyDTO>();
        }
    }

    public List<SpyDTO> getSpies() {
        final List<SpyDTO> out = new ArrayList<SpyDTO>();
        for (Map.Entry<Integer, Map<Integer, AlliedUnits>> entry : alliedUnitsMap.entrySet()) {
            for (AlliedUnits allied : entry.getValue().values()) {
                for (List<SpyDTO> spies : allied.getSpies().values()) {
                    out.addAll(spies);
                }
            }
        }
        return out;
    }

    public List<BaggageTrainDTO> getBaggageTrainsBySector(final SectorDTO sector) {
        try {
            final List<BaggageTrainDTO> out = new ArrayList<BaggageTrainDTO>();
            final Map<Integer, AlliedUnits> nationToUnits = alliedUnitsMap.get(sector.getRegionId());
            for (int nationId : nationToUnits.keySet()) {
                if (nationId != GameStore.getInstance().getNationId()
                        && nationToUnits.get(nationId).getBaggageTrains() != null
                        && nationToUnits.get(nationId).getBaggageTrains().get(sector.getId()) != null) {
                    out.addAll(nationToUnits.get(nationId).getBaggageTrains().get(sector.getId()));
                }
            }
            return out;
        } catch (Exception e) {
            return new ArrayList<BaggageTrainDTO>();
        }
    }

    public List<CommanderDTO> getCommanderBySector(final SectorDTO sector) {
        try {
            final List<CommanderDTO> out = new ArrayList<CommanderDTO>();
            final Map<Integer, AlliedUnits> nationToUnits = alliedUnitsMap.get(sector.getRegionId());
            for (int nationId : nationToUnits.keySet()) {
                if (nationId != GameStore.getInstance().getNationId()
                        && nationToUnits.get(nationId).getCommanders() != null
                        && nationToUnits.get(nationId).getCommanders().get(sector.getId()) != null) {
                    out.addAll(nationToUnits.get(nationId).getCommanders().get(sector.getId()));
                }
            }
            return out;
        } catch (Exception e) {
            return new ArrayList<CommanderDTO>();
        }
    }

    public boolean repairFleet(final int fleetId, final int sectorId, final int nationId) {
        final FleetDTO fleet = getFleetById(fleetId);
        if (fleet.getX() != fleet.getXStart()
                || fleet.getY() != fleet.getYStart()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot repair an allied fleet after it has been moved. Review conflict orders?", false);
            return false;
        }
        final int[] ids = new int[4];
        ids[0] = fleetId;
        ids[1] = sectorId;
        ids[2] = fleet.getRegionId();
        ids[3] = nationId;
        boolean hasFleetBeenRepaired = false;
        if (cancelRepairFleet(fleetId, sectorId, nationId)) {
            hasFleetBeenRepaired = true;
        }
        final OrderCostDTO cost = CostCalculators.getFleetRepairCost(fleet);
        if (OrderStore.getInstance().addNewOrder(ORDER_R_FLT, cost, fleet.getRegionId(), "", ids, 0, cost.convertToString()) == 1) {
            for (ShipDTO ship : fleet.getShips().values()) {
                //remove ship repair for this order if exist
                //it means the ship has been repaired
                if (ship.getOriginalCondition() < ship.getCondition() && !hasFleetBeenRepaired) {
                    cancelRepairShip(fleetId, ship.getId(), sectorId, nationId);
                }
                //update the ships status to 100% since it gets repaired
                ship.setCondition(100);
                ship.setMarines(ship.getType().getCitizens());
            }

            UnitEventManager.changeUnit(FLEET, fleetId);
            return true;

        } else {
            return false;
        }
    }

    public boolean hasBaggageTrainsToEmbark(final SectorDTO sectorDTO) {
        return getBaggageTrainsBySector(sectorDTO).size() > 0;
    }

    public boolean hasShipsToEmbark(final SectorDTO sectorDTO) {
        return getShipsBySector(sectorDTO).size() > 0 || getAlliedFleetsBySector(sectorDTO).size() > 0;
    }

    public List<ShipDTO> getFreeShipsByPosition(final SectorDTO position) {
        final List<ShipDTO> shipList = new ArrayList<ShipDTO>();
        for (FleetDTO fleet : fleetMap.values()) {
            if (fleet.getFleetId() == 0) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.equals(position)) {
                        shipList.add(ship);
                    }
                }
            }
        }
        return shipList;
    }

    public List<ShipDTO> getShipsBySector(final PositionDTO sector) {
        final List<ShipDTO> shipList = new ArrayList<ShipDTO>();
        for (FleetDTO fleet : fleetMap.values()) {
            if (fleet.getFleetId() == 0 || fleet.equals(sector)) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.equals(sector)) {
                        shipList.add(ship);
                    }
                }
            }
        }
        return shipList;
    }


    public boolean cancelRepairFleet(final int fleetId, final int sectorId, final int nationId) {
        final FleetDTO fleet = getFleetById(fleetId);
        final int[] idents = new int[4];
        idents[0] = fleetId;
        idents[1] = sectorId;
        idents[2] = fleet.getRegionId();
        idents[3] = nationId;
        if (OrderStore.getInstance().removeOrder(ORDER_R_FLT, idents)) {
            for (ShipDTO ship : fleet.getShips().values()) {
                ship.setCondition(ship.getOriginalCondition());
                ship.setMarines(ship.getOriginalMarines());
            }

            UnitEventManager.changeUnit(FLEET, fleetId);
            return true;

        } else {
            return false;
        }
    }


    /**
     * Function that repairs a damaged ship.
     *
     * @param shipId   The id of the ship we want to repair.
     * @param sectorId The id of the position where the ship starts this turn.
     * @return true if ship is repaired.
     */
    public boolean repairShip(final int shipId, final int sectorId, final int nationId) {
        final ShipDTO ship = getShipById(shipId);
        if (ship.getX() != ship.getXStart()
                || ship.getY() != ship.getYStart()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot repair an allied ship after it has been moved. Review conflict orders?", false);
            return false;
        }
        int[] idents = new int[4];
        idents[0] = shipId;
        idents[1] = sectorId;
        idents[2] = ship.getRegionId();
        idents[3] = nationId;
        if (OrderStore.getInstance().addNewOrder(ORDER_R_SHP, CostCalculators.getShipRepairCost(ship.getCondition(), ship), ship.getRegionId(), "", idents, 0, "") == 1) {
            ship.setCondition(100);
            ship.setMarines(ship.getType().getCitizens());
            UnitEventManager.changeUnit(SHIP, shipId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function that cancels repairs on a damaged ship
     *
     * @param fleetId  the id of the fleet in which the ship resides
     * @param shipId   The id of the ship we want to repair
     * @param sectorId The id of the position where the ship starts
     *                 this turn
     * @return true if the order was canceled succesfully
     */
    public boolean cancelRepairShip(final int fleetId, final int shipId, final int sectorId, final int nationId) {
        final ShipDTO ship = getShipById(shipId);
        int[] idents = new int[4];
        idents[0] = shipId;
        idents[1] = sectorId;
        idents[2] = ship.getRegionId();
        idents[3] = nationId;
        if (OrderStore.getInstance().removeOrder(ORDER_R_SHP, idents)) {
            ship.setCondition(ship.getOriginalCondition());
            ship.setMarines(ship.getOriginalMarines());
            UnitEventManager.changeUnit(SHIP, shipId);
            UnitEventManager.changeUnit(FLEET, fleetId);
            return true;
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Unexpected Problem. Could not cancel command", false);
            return false;
        }
    }

    public Map<Integer, Map<Integer, AlliedUnits>> getAlliedUnitsMap() {
        return alliedUnitsMap;
    }

    /**
     * Method that repairs the target baggage train
     *
     * @param btrainId the id of the train we want to repair
     * @param sectorId the id of the sector where the repairs will happen
     */
    public void repairTrain(final int btrainId, final int sectorId, final int nationId) {
        final BaggageTrainDTO btrain = getBaggageTrainById(btrainId);
        final int[] ids = new int[4];
        ids[0] = btrainId;
        ids[1] = sectorId;
        ids[2] = btrain.getRegionId();
        ids[3] = nationId;
        if (OrderStore.getInstance().addNewOrder(ORDER_R_BTRAIN, CostCalculators.getBaggageTrainRepairCost(btrain.getCondition()), btrain.getRegionId(), "", ids, 0, "") == 1) {
            btrain.setCondition(100);
            UnitEventManager.changeUnit(BAGGAGETRAIN, btrainId);
        }
    }

    /**
     * Method that cancels repairs of a certain baggage train on
     * a specific sector
     *
     * @param btrainId the id of the train we want to repair
     * @param sectorId the id of the sector where the repairs will happen
     */
    public void cancelRepairTrain(final int btrainId, final int sectorId) {
        final BaggageTrainDTO btrain = getBaggageTrainById(btrainId);
        final int[] ids = new int[2];
        ids[0] = btrainId;
        ids[1] = sectorId;
        if (OrderStore.getInstance().removeOrder(ORDER_R_BTRAIN, ids)) {
            btrain.setCondition(btrain.getOriginalCondition());

        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Unexpected Problem. Could not cancel command", false);
        }
    }
}
