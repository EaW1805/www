package com.eaw1805.www.controllers.remote.hotspot;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.converters.RelationConverter;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.shared.AlliedMovement;
import com.eaw1805.www.shared.AlliedUnits;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupAlliedUnitsProccessor
        implements RelationConstants, OrderConstants, ArmyConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SetupAlliedUnitsProccessor.class);

    /**
     * A map of all allied nations identities and their corresponding units
     */
    private transient final Map<Integer, Map<Integer, AlliedUnits>> alliedUnitsMap = new HashMap<Integer, Map<Integer, AlliedUnits>>();

    /**
     * The service object inherited from the servlet
     */
    private transient final EmpireRpcServiceImpl service;

    /**
     * The session info variables
     */
    private transient final int gameId, nationId, turn, scenarioId;

    /**
     * Default constructor.
     *
     * @param service  the service endpoint.
     * @param gameId   the game Id.
     * @param nationId the nation Id.
     * @param turn     the turn.
     */
    public SetupAlliedUnitsProccessor(final int scenarioId,
                                      final EmpireRpcServiceImpl service,
                                      final int gameId,
                                      final int nationId,
                                      final int turn) {
        this.scenarioId = scenarioId;
        this.service = service;
        this.gameId = gameId;
        this.nationId = nationId;
        this.turn = turn;
        for (int regionId = 1; regionId <= 4; regionId++) {
            alliedUnitsMap.put(regionId, new HashMap<Integer, AlliedUnits>());
        }
    }

    /**
     * Get allied units for the given nation.
     *
     * @return all allied units, indexed by unit ID and nation ID.
     */
    public Map<Integer, Map<Integer, AlliedUnits>> getAlliedUnits() {
        final Nation thisNation = service.getNation(scenarioId, nationId);
        final Game thisGame = service.getGame(scenarioId, gameId);
        final List<RelationDTO> relations = RelationConverter.convert(service.getRelationsManager().listAlliesByGameNation(thisGame, thisNation),
                service.getRelationsManager());
        for (final RelationDTO relation : relations) {
            boolean visibleRelation = service.getRelationsManager().getByNations(thisGame, relation.getTargetNationId(), relation.getNationId()).isVisible();
            for (int regionId = RegionConstants.EUROPE; regionId <= RegionConstants.AFRICA; regionId++) {
                try {
                    final AlliedUnits regAllUnits = new AlliedUnits();
                    regAllUnits.setMovements(getMovements(relation.getTargetNationId(), regionId, visibleRelation));
                    regAllUnits.setArmies(getArmies(relation.getTargetNationId(), regionId, visibleRelation));
                    regAllUnits.setFleets(getFleets(relation.getTargetNationId(), regionId, visibleRelation));
                    regAllUnits.setBaggageTrains(getBaggageTrains(relation.getTargetNationId(), regionId, visibleRelation));
                    regAllUnits.setSpies(getSpies(relation.getTargetNationId(), regionId, visibleRelation));
                    regAllUnits.setCommanders(getCommanders(relation.getTargetNationId(), regionId));
                    alliedUnitsMap.get(regionId).put(relation.getTargetNationId(), regAllUnits);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return alliedUnitsMap;
    }

    /**
     * Method that gets all the allied armies and formats them
     * in a way good and proper for the presentation in the client.
     *
     * @param regionId       the region of the armies.
     * @param targetNationId the nation of the armies.
     * @return A map of sectorId- FleetDTO.
     */
    private Map<Integer, Map<Integer, FleetDTO>> getFleets(final int targetNationId, final int regionId, final boolean visible) {
        final Map<Integer, Map<Integer, FleetDTO>> sectorFleets = new HashMap<Integer, Map<Integer, FleetDTO>>();
        final List<FleetDTO> fleets = service.getFleetsByNationRegion(scenarioId, targetNationId, regionId, gameId, turn, true, nationId, visible);
        if (fleets == null) {
            return sectorFleets;
        }

        for (final FleetDTO fleet : fleets) {
            if (fleet.getFleetId() == 0) {
                for (final ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getRegionId() == regionId) {
                        final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, regionId, ship.getX(), ship.getY());
                        final int sectorId = sector.getId();
                        if (!sectorFleets.containsKey(sectorId)) {
                            sectorFleets.put(sectorId, new HashMap<Integer, FleetDTO>());

                        }
                        if (!sectorFleets.get(sectorId).containsKey(0)) {
                            final FleetDTO sectorFleet = new FleetDTO();
                            sectorFleet.setFleetId(0);
                            sectorFleet.setRegionId(ship.getRegionId());
                            sectorFleet.setX(ship.getX());
                            sectorFleet.setY(ship.getY());
                            sectorFleet.setNationId(targetNationId);
                            sectorFleet.setShips(new HashMap<Integer, ShipDTO>());
                            sectorFleet.setName("Individual Ships");
                            sectorFleets.get(sectorId).put(0, sectorFleet);
                        }

                        sectorFleets.get(sectorId).get(0).getShips().put(ship.getId(), ship);
                    }
                }

            } else if (fleet.getRegionId() == regionId) {
                final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, regionId, fleet.getX(), fleet.getY());
                fleet.setNationId(targetNationId);
                final int sectorId = sector.getId();
                if (!sectorFleets.containsKey(sectorId)) {
                    sectorFleets.put(sectorId, new HashMap<Integer, FleetDTO>());
                }

                sectorFleets.get(sectorId).put(fleet.getFleetId(), fleet);
            }
        }

        for (final OrderDTO order : service.getOrders(scenarioId, gameId, nationId, turn,
                new Object[]{ORDER_LOAD_TROOPSF, ORDER_LOAD_TROOPSS})) {
            if (Integer.parseInt(order.getParameter1()) == FLEET) {
                for (Map<Integer, FleetDTO> idAndFleet : sectorFleets.values()) {
                    final int fleetId = Integer.parseInt(order.getParameter2());
                    if (idAndFleet.containsKey(fleetId)) {
                        final int loadedUnitType = Integer.parseInt(order.getParameter3());
                        final int loadedUnitId = Integer.parseInt(order.getParameter4());
                        final Map<Integer, List<Integer>> loadedUnits = idAndFleet.get(fleetId).getLoadedUnitsMap();
                        if (loadedUnits.get(loadedUnitType) == null) {
                            loadedUnits.put(loadedUnitType, new ArrayList<Integer>());
                        }
                        loadedUnits.get(loadedUnitType).add(loadedUnitId);
                    }
                }
            }
        }

        return sectorFleets;
    }


    /**
     * Method that returns the allied movements
     *
     * @param targetNationId   the nation to inspect.
     * @param selectedRegionId the id of the region to inspect.
     * @return the movement of the allies in the sector-List<AlliedMovement> format.
     */
    private Map<Integer, List<AlliedMovement>> getMovements(final int targetNationId, final int selectedRegionId, final boolean visible) {
        if (!visible) {
            return new HashMap<Integer, List<AlliedMovement>>();
        }
        final Map<Integer, List<AlliedMovement>> secMvs = new HashMap<Integer, List<AlliedMovement>>();
        final Map<Integer, Map<Integer, MovementDTO>> mvs = service.getMovementOrdersByGameNationAndTurn(scenarioId, targetNationId, gameId, turn);
        for (int type : mvs.keySet()) {
            for (int id : mvs.get(type).keySet()) {
                try {
                    final AlliedMovement alMv = new AlliedMovement();
                    alMv.setUnitType(type);
                    alMv.setId(id);
                    final MovementDTO mv = mvs.get(type).get(id);
                    alMv.setForcedMarch(mv.getForcedMarch());
                    alMv.setPatrol(mv.getPatrol());
                    alMv.setUpSectorPaths(mv);

                    final int xPos = alMv.getSectorPaths().get(0).getPathSectors().get(0).getX();
                    final int yPos = alMv.getSectorPaths().get(0).getPathSectors().get(0).getY();
                    final int regionId = alMv.getSectorPaths().get(0).getRegionId();
                    if (regionId == selectedRegionId) {
                        final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, regionId, xPos, yPos);
                        final int sectorId = sector.getId();
                        if (!secMvs.containsKey(sectorId)) {
                            secMvs.put(sectorId, new ArrayList<AlliedMovement>());
                        }
                        secMvs.get(sectorId).add(alMv);
                    }

                } catch (Exception ignore) {
                    // eat it
                    LOGGER.debug(ignore);
                }
            }
        }

        return secMvs;
    }


    /**
     * Method that gets all the allied Commanders and formats them
     * in a way good and proper for the presentation in the client
     *
     * @param regionId the region of the Commanders
     * @param nationId the nation of the Commanders
     * @return A map of sectorId-Commanders<CommandersDTO>
     */
    private Map<Integer, List<CommanderDTO>> getCommanders(final int nationId, final int regionId) {
        final Map<Integer, List<CommanderDTO>> tileCommanders = new HashMap<Integer, List<CommanderDTO>>();
        for (final CommanderDTO commander : service.getCommandersByGameNation(scenarioId, gameId, nationId)) {
            if (commander.getRegionId() == regionId) {
                final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, regionId, commander.getX(), commander.getY());
                final int sectorId = sector.getId();
                if (!tileCommanders.containsKey(sectorId)) {
                    tileCommanders.put(sectorId, new ArrayList<CommanderDTO>());
                }

                tileCommanders.get(sectorId).add(commander);
            }
        }
        return tileCommanders;
    }


    /**
     * Method that gets all the allied Spies and formats them
     * in a way good and proper for the presentation in the client
     *
     * @param regionId the region of the Spies
     * @param nationId the nation of the Spies
     * @return A map of sectorId-List<SpyDTO>
     */
    private Map<Integer, List<SpyDTO>> getSpies(final int nationId, final int regionId, final boolean visible) {
        final Map<Integer, List<SpyDTO>> tileSpies = new HashMap<Integer, List<SpyDTO>>();
        for (SpyDTO spy : service.getSpies(scenarioId, nationId, gameId, turn, visible)) {
            if (spy.getRegionId() == regionId) {
                final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, regionId, spy.getX(), spy.getY());
                final int sectorId = sector.getId();
                if (!tileSpies.containsKey(sectorId)) {
                    tileSpies.put(sectorId, new ArrayList<SpyDTO>());
                }

                tileSpies.get(sectorId).add(spy);
            }
        }
        return tileSpies;
    }

    /**
     * Method that gets all the allied baggage rains and formats them
     * in a way good and proper for the presentation in the client
     *
     * @param regionId       the region of the baggage rains
     * @param targetNationId the nation of the baggage rains
     * @return A map of sectorId-List<BaggageTrainDTO>
     */
    private Map<Integer, List<BaggageTrainDTO>> getBaggageTrains(final int targetNationId, final int regionId, final boolean visible) {
        final Map<Integer, List<BaggageTrainDTO>> tileBtrains = new HashMap<Integer, List<BaggageTrainDTO>>();
        for (BaggageTrainDTO btrain : service.getBaggageTrains(scenarioId, targetNationId, gameId, turn, true, nationId, visible)) {
            if (btrain.getRegionId() == regionId) {
                final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, regionId, btrain.getX(), btrain.getY());
                int sectorId = sector.getId();
                if (!tileBtrains.containsKey(sectorId)) {
                    tileBtrains.put(sectorId, new ArrayList<BaggageTrainDTO>());
                }

                tileBtrains.get(sectorId).add(btrain);
            }
        }
        return tileBtrains;
    }

    /**
     * Method that gets all the allied armies and formats them
     * in a way good and proper for the presentation in the client
     *
     * @param regionId the region of the armies
     * @param nationId the nation of the armies
     * @return A map of sectorId-Map<armyId,ArmyDTO>
     */
    private Map<Integer, Map<Integer, ArmyDTO>> getArmies(final int nationId, final int regionId, final boolean visible) {
        final Map<Integer, Map<Integer, ArmyDTO>> tileArmies = new HashMap<Integer, Map<Integer, ArmyDTO>>();
        for (ArmyDTO army : service.getArmiesByNationRegion(scenarioId, nationId, regionId, gameId, turn, visible).getArmies()) {
            if (army.getArmyId() > 0) {
                final SectorDTO armySector = service.getSectorByCoordinates(scenarioId, gameId, regionId, army.getX(), army.getY());

                if (!tileArmies.containsKey(armySector.getId())) {
                    tileArmies.put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                }

                tileArmies.get(armySector.getId()).put(army.getArmyId(), army);

            } else {
                for (CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() > 0) {
                        final SectorDTO armySector = service.getSectorByCoordinates(
                                scenarioId, gameId, regionId, corp.getX(), corp.getY());

                        if (!tileArmies.containsKey(armySector.getId())) {
                            tileArmies.put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                        }

                        if (!tileArmies.get(armySector.getId()).containsKey(0)) {
                            final ArmyDTO zeroArmy = new ArmyDTO();
                            zeroArmy.setArmyId(0);
                            zeroArmy.setRegionId(corp.getRegionId());
                            zeroArmy.setX(corp.getX());
                            zeroArmy.setY(corp.getY());
                            tileArmies.get(armySector.getId()).put(0, zeroArmy);
                        }

                        tileArmies.get(armySector.getId()).get(0).getCorps().put(corp.getCorpId(), corp);

                    } else {
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {

                            final SectorDTO armySector = service.getSectorByCoordinates(scenarioId, gameId, regionId, brigade.getX(), brigade.getY());

                            if (!tileArmies.containsKey(armySector.getId())) {
                                tileArmies.put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                            }

                            if (!tileArmies.get(armySector.getId()).containsKey(0)) {
                                final ArmyDTO zeroArmy = new ArmyDTO();
                                zeroArmy.setArmyId(0);
                                zeroArmy.setRegionId(brigade.getRegionId());
                                zeroArmy.setX(brigade.getX());
                                zeroArmy.setY(brigade.getY());
                                tileArmies.get(armySector.getId()).put(0, zeroArmy);
                            }

                            if (!tileArmies.get(armySector.getId()).get(0).getCorps().containsKey(0)) {
                                final CorpDTO zeroCorp = new CorpDTO();
                                zeroCorp.setArmyId(0);
                                zeroCorp.setRegionId(brigade.getRegionId());
                                zeroCorp.setX(brigade.getX());
                                zeroCorp.setY(brigade.getY());
                                tileArmies.get(armySector.getId()).get(0).getCorps().put(0, zeroCorp);
                            }
                            tileArmies.get(armySector.getId()).get(0).getCorps().get(0).getBrigades().put(brigade.getBrigadeId(), brigade);
                        }
                    }
                }
            }
        }

        return tileArmies;
    }
}
