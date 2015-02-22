package com.eaw1805.www.controllers.remote.hotspot;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.converters.SectorConverter;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Report;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.shared.ForeignUnits;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SetupForeignUnitsProcessor
        implements RelationConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(SetupForeignUnitsProcessor.class);

    /**
     * The service object inherited from the servlet
     */
    private transient final EmpireRpcServiceImpl service;

    /**
     * The session info variables
     */
    private transient final int gameId, nationId, turn, scenarioId;

    private transient final Map<Integer, Boolean> nationsToShow;

    private transient final List<Integer> sectorIds = new ArrayList<Integer>();

    private transient ForeignUnits foreignUnits;

    public SetupForeignUnitsProcessor(final int scenarioId,
                                      final EmpireRpcServiceImpl service,
                                      final int gameId,
                                      final int nationId,
                                      final int turn) {
        this.scenarioId = scenarioId;
        this.service = service;
        this.gameId = gameId;
        this.nationId = nationId;
        this.turn = turn;
        nationsToShow = new HashMap<Integer, Boolean>();
    }


    public int createReportedBattalions(final PositionDTO position, final String reports, int brigadesId, final List<NationDTO> nations, final SectorDTO sector) {
        final String[] battRep = reports.split("\\|");
        for (final String aBattRep : battRep) {
            final String[] items = aBattRep.split(":");
            if (!items[0].equals("")) {
                final int numOfBats = Integer.parseInt(items[1]);
                final BrigadeDTO spyBrigade = new BrigadeDTO();
                spyBrigade.setBattalions(new ArrayList<BattalionDTO>());
                spyBrigade.setBrigadeId(brigadesId);
                //we assign negative ids for this units
                brigadesId--;

                for (int index = 0; index < numOfBats; index++) {
                    final BattalionDTO bat = new BattalionDTO();
                    bat.setBrigadeId(spyBrigade.getBrigadeId());
                    bat.setTypeId(1);
                    spyBrigade.getBattalions().add(bat);
                }

                for (final NationDTO nation : nations) {
                    if (items[0].equals(nation.getName())) {
                        spyBrigade.setNationId(nation.getNationId());
                        spyBrigade.setRegionId(position.getRegionId());
                        spyBrigade.setX(position.getXStart());
                        spyBrigade.setY(position.getYStart());
                        spyBrigade.setCorpId(0);
                        spyBrigade.setName("Reported Battalions");
                        break;
                    }
                }
                // if it is in the nations to show list OR it is our sector OR it is not our unit
                // then continue with the next report....
                // you have already found these units
                if (!nationsToShow.get(spyBrigade.getNationId())
                        || sectorIds.contains(sector.getId())) {
                    continue;
                }

                if (spyBrigade.getBattalions().size() > 0) {
                    List<ArmyDTO> sectorArmies = foreignUnits.getArmies().get(sector.getId());
                    if (sectorArmies == null) {
                        sectorArmies = new ArrayList<ArmyDTO>();
                        foreignUnits.getArmies().put(sector.getId(), sectorArmies);
                    }

                    ArmyDTO zeroArmy = null;
                    for (ArmyDTO army : sectorArmies) {
                        if (army.getArmyId() == 0) {
                            zeroArmy = army;
                            break;
                        }
                    }

                    if (zeroArmy == null) {
                        zeroArmy = new ArmyDTO();
                        zeroArmy.setArmyId(0);
                        zeroArmy.setNationId(0);
                        zeroArmy.setRegionId(sector.getRegionId());
                        zeroArmy.setX(sector.getX());
                        zeroArmy.setY(sector.getY());
                        zeroArmy.setName("Army");
                        zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                        sectorArmies.add(zeroArmy);
                    }

                    CorpDTO zeroCorp = zeroArmy.getCorps().get(0);
                    if (zeroCorp == null) {
                        zeroCorp = new CorpDTO();
                        zeroCorp.setNationId(0);
                        zeroCorp.setRegionId(sector.getRegionId());
                        zeroCorp.setX(sector.getX());
                        zeroCorp.setY(sector.getY());
                        zeroCorp.setName("Corps");
                        zeroCorp.setArmyId(0);
                        zeroCorp.setCorpId(0);
                        zeroCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
                        zeroArmy.getCorps().put(0, zeroCorp);
                    }
                    zeroCorp.getBrigades().put(spyBrigade.getBrigadeId(), spyBrigade);
                }
            }
        }
        return brigadesId;
    }


    public int[] createReportedBrigades(final String reports, final List<NationDTO> nations, final SectorDTO thisSector, int corpsId, int brigadesId) {
        final String brigRep[] = reports.split("\\|");
        for (final String aBrigRep : brigRep) {
            final String[] items = aBrigRep.split(":");
            if (!items[0].equals("")) {
                final int numBrigs = Integer.parseInt(items[1]);
                if (numBrigs > 0) {

                    int thisNationId = 0;
                    for (final NationDTO nation : nations) {
                        if (items[0].equalsIgnoreCase(nation.getName())) {
                            thisNationId = nation.getNationId();
                            break;
                        }
                    }
                    // if it is in the nations to show list OR it is our sector OR it is not our unit
                    // then continue with the next report....
                    // you have already found these units
                    if (!nationsToShow.get(thisNationId)
                            || sectorIds.contains(thisSector.getId())) {
                        continue;
                    }

                    List<ArmyDTO> sectorArmies = foreignUnits.getArmies().get(thisSector.getId());
                    if (sectorArmies == null) {
                        sectorArmies = new ArrayList<ArmyDTO>();
                        foreignUnits.getArmies().put(thisSector.getId(), sectorArmies);
                    }

                    ArmyDTO zeroArmy = null;
                    for (final ArmyDTO army : sectorArmies) {
                        if (army.getArmyId() == 0) {
                            zeroArmy = army;
                            break;
                        }
                    }

                    if (zeroArmy == null) {
                        zeroArmy = new ArmyDTO();
                        zeroArmy.setArmyId(0);
                        zeroArmy.setNationId(thisNationId);
                        zeroArmy.setRegionId(thisSector.getRegionId());
                        zeroArmy.setX(thisSector.getX());
                        zeroArmy.setY(thisSector.getY());
                        zeroArmy.setName("Army");
                        zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                        sectorArmies.add(zeroArmy);
                    }

                    final CorpDTO newCorp = new CorpDTO();
                    newCorp.setNationId(thisNationId);
                    newCorp.setCorpId(corpsId);
                    corpsId--;
                    newCorp.setRegionId(thisSector.getRegionId());
                    newCorp.setX(thisSector.getX());
                    newCorp.setY(thisSector.getY());
                    newCorp.setName("Reported Brigades");
                    newCorp.setArmyId(0);
                    newCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
                    zeroArmy.getCorps().put(newCorp.getCorpId(), newCorp);

                    for (int index = 0; index < numBrigs; index++) {
                        final BrigadeDTO brigade = new BrigadeDTO();
                        brigade.setBattalions(new ArrayList<BattalionDTO>());
                        brigade.setBrigadeId(brigadesId);
                        brigadesId--;
                        brigade.setCorpId(newCorp.getCorpId());
                        brigade.setStartCorp(newCorp.getCorpId());
                        brigade.setX(thisSector.getX());
                        brigade.setY(thisSector.getY());
                        brigade.setNationId(thisNationId);
                        brigade.setRegionId(thisSector.getRegionId());
                        newCorp.getBrigades().put(brigade.getBrigadeId(), brigade);
                    }
                }
            }

        }
        return new int[]{corpsId, brigadesId};
    }

    public ForeignUnits getForeignUnits() {

        final List<RelationDTO> relations = new ArrayList<RelationDTO>();
        final Set<Integer> alliedNations = new HashSet<Integer>();

        //first of all add the current nation id.
        relations.addAll(service.getNationsRelations(scenarioId, gameId, nationId, turn));
        for (RelationDTO relation : relations) {
            if (relation.getRelation() == REL_ALLIANCE || nationId == relation.getTargetNationId()) {
                //then add every friendly nation.
                alliedNations.add(relation.getTargetNationId());
                nationsToShow.put(relation.getTargetNationId(), false);

            } else {
                nationsToShow.put(relation.getTargetNationId(), true);
            }
        }

        nationsToShow.put(nationId, false);
        alliedNations.add(nationId);

        foreignUnits = new ForeignUnits();
        for (Integer curNationId : alliedNations) {
            searchForeignUnits(curNationId);
        }

        //retrieve all spies
        final List<SpyDTO> spies = new ArrayList<SpyDTO>();
        spies.addAll(service.getSpies(scenarioId, nationId, gameId, turn, false));
        for (int curNationId : alliedNations) {
            if (curNationId != nationId) {
                spies.addAll(service.getSpies(scenarioId, curNationId, gameId, turn, false));
            }
        }

        final List<NationDTO> nations = service.getNations(scenarioId);

        int brigadesId = -1;
        int corpsId = -1;
        int shipsId = -1;
        int fleetsId = -1;
        final Game thisGame = service.gameManager.getByID(gameId);
        final List<Report> scoutReports = service.reportManager.listByOwnerTurnKey(service.nationManager.getByID(nationId), thisGame, thisGame.getTurn() - 1, "scout.%");
        for (Report report : scoutReports) {
            int sectorId = Integer.parseInt(report.getKey().split("\\.")[1]);
            final SectorDTO sector = SectorConverter.convert(service.getSectorManager().getByID(sectorId));
            if ("reportBattalions".equals(report.getKey().split("\\.")[2])) {
                if (report.getValue() != null && !report.getValue().isEmpty()) {
                    brigadesId = createReportedBattalions(sector, report.getValue(), brigadesId, nations, sector);
                }
            } else if ("reportBrigades".equals(report.getKey().split("\\.")[2])) {
                final List<SectorDTO> neighborSectors = getNeighborSectors(sector);
                for (final SectorDTO thisSector : neighborSectors) {
                    if (report.getValue() != null && !report.getValue().isEmpty()
                            && thisSector.getTerrain().getId() != 12) {
                        int[] ids = createReportedBrigades(report.getValue(), nations, thisSector, corpsId, brigadesId);
                        corpsId = ids[0];
                        brigadesId = ids[1];
                    }
                }
            }
        }
        for (final SpyDTO spy : spies) {
            final SectorDTO sector = service.getSectorByCoordinates(scenarioId, gameId, spy.getRegionId(), spy.getXStart(), spy.getYStart());

            if (spy.getReportBattalions() != null && !spy.getReportBattalions().isEmpty()) {
                brigadesId = createReportedBattalions(spy, spy.getReportBattalions(), brigadesId, nations, sector);
            }

            if (spy.getReportShips() != null && !spy.getReportShips().isEmpty()) {
                final String shipRep[] = spy.getReportShips().split("\\|");
                for (final String aShipRep : shipRep) {
                    final String[] items = aShipRep.split(":");
                    if (!items[0].equals("")) {
                        final String country = items[0];
                        int thisNationId = 0;
                        for (final NationDTO nation : nations) {
                            if (country.equalsIgnoreCase(nation.getName())) {
                                thisNationId = nation.getNationId();
                                break;
                            }
                        }

                        // if it is in the nations to show list OR it is our sector OR it is not our unit
                        // then continue with the next report....
                        // you have already found these units
                        if (!nationsToShow.get(thisNationId)
                                || sectorIds.contains(sector.getId())) {
                            continue;
                        }

                        final int merchantShips = Integer.parseInt(items[1].substring(2));
                        int warShips = 0;
                        if (items.length == 3) {
                            warShips = Integer.parseInt(items[2].substring(2));
                        }

                        if (merchantShips > 0 || warShips > 0) {
                            final FleetDTO newFleet = new FleetDTO();
                            newFleet.setShips(new HashMap<Integer, ShipDTO>());
                            newFleet.setFleetId(fleetsId);
                            newFleet.setNationId(thisNationId);
                            newFleet.setRegionId(sector.getRegionId());
                            newFleet.setX(sector.getX());
                            newFleet.setY(sector.getY());
                            newFleet.setName("Reported Ships");

                            //we assign negative ids to this fleets
                            fleetsId--;

                            List<FleetDTO> sectorFleets = foreignUnits.getFleets().get(sector.getId());
                            if (sectorFleets == null) {
                                sectorFleets = new ArrayList<FleetDTO>();
                                foreignUnits.getFleets().put(sector.getId(), sectorFleets);
                            }

                            for (int index = 0; index < merchantShips; index++) {
                                final ShipDTO ship = new ShipDTO();
                                ship.setId(shipsId);
                                ship.setRegionId(sector.getRegionId());
                                ship.setX(sector.getX());
                                ship.setY(sector.getY());
                                ship.setFleet(newFleet.getFleetId());
                                shipsId--;
                                ship.setNationId(newFleet.getNationId());

                                final ShipTypeDTO shipType = new ShipTypeDTO();
                                shipType.setCanColonies(false);
                                shipType.setCitizens(0);
                                shipType.setCost(0);
                                shipType.setFabrics(0);
                                shipType.setIntId(0);
                                shipType.setShipClass(0);
                                shipType.setName("Merchant ship");
                                ship.setType(shipType);
                                newFleet.getShips().put(ship.getId(), ship);
                            }

                            for (int index = 0; index < warShips; index++) {
                                final ShipDTO ship = new ShipDTO();
                                ship.setId(shipsId);
                                ship.setFleet(newFleet.getFleetId());
                                ship.setRegionId(sector.getRegionId());
                                ship.setX(sector.getX());
                                ship.setY(sector.getY());
                                shipsId--;
                                ship.setNationId(newFleet.getNationId());

                                final ShipTypeDTO shipType = new ShipTypeDTO();
                                shipType.setCanColonies(false);
                                shipType.setCitizens(0);
                                shipType.setCost(0);
                                shipType.setFabrics(0);
                                shipType.setIntId(0);
                                shipType.setShipClass(1);
                                shipType.setName("War ship");
                                ship.setType(shipType);
                                newFleet.getShips().put(ship.getId(), ship);
                            }
                            sectorFleets.add(newFleet);
                        }
                    }
                }
            }

            final List<SectorDTO> neighborSectors = getNeighborSectors(sector);
            for (final SectorDTO thisSector : neighborSectors) {

                if (spy.getReportNearbyShips() != null && !spy.getReportNearbyShips().isEmpty()
                        && (thisSector.getTerrain().getId() == 12 || hasShipyard(thisSector))) {

                    final String nearShipRep[] = spy.getReportNearbyShips().split("\\|");
                    for (final String aNearShipRep : nearShipRep) {
                        final String[] items = aNearShipRep.split(":");
                        if (!items[0].isEmpty()) {
                            final int warShips = Integer.parseInt(items[1]);
                            int merchantShips = 0;
                            if (items.length == 3) {
                                merchantShips = Integer.parseInt(items[2]);
                            }

                            if (warShips > 0 || merchantShips > 0) {
                                final String country = items[0];
                                int thisNationId = 0;
                                for (final NationDTO nation : nations) {
                                    if (country.equalsIgnoreCase(nation.getName())) {
                                        thisNationId = nation.getNationId();
                                        break;
                                    }
                                }

                                // if it is in the nations to show list OR it is our sector OR it is not our unit
                                // then continue with the next report....
                                // you have already found these units
                                if (!nationsToShow.get(thisNationId)) {
                                    continue;
                                }

                                if (merchantShips > 0 || warShips > 0) {
                                    final FleetDTO newFleet = new FleetDTO();
                                    newFleet.setShips(new HashMap<Integer, ShipDTO>());
                                    newFleet.setFleetId(fleetsId);
                                    newFleet.setNationId(thisNationId);
                                    newFleet.setName("Near by Reported Ships");
                                    newFleet.setRegionId(thisSector.getRegionId());
                                    newFleet.setX(thisSector.getX());
                                    newFleet.setY(thisSector.getY());

                                    //we assign negative ids to this fleets
                                    fleetsId--;

                                    List<FleetDTO> sectorFleets = foreignUnits.getFleets().get(thisSector.getId());
                                    if (sectorFleets == null) {
                                        sectorFleets = new ArrayList<FleetDTO>();
                                        foreignUnits.getFleets().put(thisSector.getId(), sectorFleets);
                                    }

                                    for (int index = 0; index < merchantShips; index++) {
                                        final ShipDTO ship = new ShipDTO();
                                        ship.setId(shipsId);
                                        ship.setRegionId(thisSector.getRegionId());
                                        ship.setX(thisSector.getX());
                                        ship.setY(thisSector.getY());
                                        ship.setFleet(newFleet.getFleetId());
                                        shipsId--;
                                        ship.setNationId(newFleet.getNationId());

                                        final ShipTypeDTO shipType = new ShipTypeDTO();
                                        shipType.setCanColonies(false);
                                        shipType.setCitizens(0);
                                        shipType.setCost(0);
                                        shipType.setFabrics(0);
                                        shipType.setIntId(0);
                                        shipType.setShipClass(0);
                                        shipType.setName("Merchant ship");
                                        ship.setType(shipType);
                                        newFleet.getShips().put(ship.getId(), ship);
                                    }

                                    for (int index = 0; index < warShips; index++) {
                                        final ShipDTO ship = new ShipDTO();
                                        ship.setId(shipsId);
                                        ship.setFleet(newFleet.getFleetId());
                                        ship.setRegionId(thisSector.getRegionId());
                                        ship.setX(thisSector.getX());
                                        ship.setY(thisSector.getY());
                                        shipsId--;
                                        ship.setNationId(newFleet.getNationId());

                                        final ShipTypeDTO shipType = new ShipTypeDTO();
                                        shipType.setCanColonies(false);
                                        shipType.setCitizens(0);
                                        shipType.setCost(0);
                                        shipType.setFabrics(0);
                                        shipType.setIntId(0);
                                        shipType.setShipClass(1);
                                        shipType.setName("War ship");
                                        ship.setType(shipType);
                                        newFleet.getShips().put(ship.getId(), ship);
                                    }
                                    sectorFleets.add(newFleet);
                                }
                            }
                        }
                    }
                }

                if (spy.getReportBrigades() != null && !spy.getReportBrigades().isEmpty()
                        && thisSector.getTerrain().getId() != 12) {
                    int[] ids = createReportedBrigades(spy.getReportBrigades(), nations, thisSector, corpsId, brigadesId);
                    corpsId = ids[0];
                    brigadesId = ids[1];
                }
            }


        }
        return foreignUnits;
    }


    public void searchForeignUnits(final int curNationId) {
        final List<SectorDTO> nationSectors = new ArrayList<SectorDTO>();
        nationSectors.addAll(service.getNationSectors(scenarioId, gameId, curNationId));
        for (SectorDTO sector : nationSectors) {
            sectorIds.add(sector.getId());
        }

        for (SectorDTO sector : nationSectors) {
            final List<FleetDTO> sectorFleets = getFleets(sector);
            if (!sectorFleets.isEmpty()) {
                foreignUnits.getFleets().put(sector.getId(), sectorFleets);
            }

            final List<ArmyDTO> sectorArmies = getArmies(sector);
            if (!sectorArmies.isEmpty()) {
                foreignUnits.getArmies().put(sector.getId(), sectorArmies);
            }

//            final List<BaggageTrainDTO> sectorBTrains = getBaggageTrains(sector);
//            if (!sectorBTrains.isEmpty()) {
//                foreignUnits.getBaggageTrains().put(sector.getId(), sectorBTrains);
//            }
        }
    }

    public boolean hasShipyard(final SectorDTO sector) {
        if (sector.getProductionSiteId() != 12
                && sector.getProductionSiteId() != 13
                && sector.getProductionSiteId() != 14
                && sector.getProductionSiteId() != 15
                && sector.getProductionSiteId() != 16) {
            return false;
        }

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (!(offsetX == 0 && offsetY == 0)) {
                    final SectorDTO thisSector = service.getSectorByCoordinates(scenarioId, gameId, sector.getRegionId(), sector.getX() + offsetX, sector.getY() + offsetY);
                    if (thisSector != null && thisSector.getTerrain().getId() == 12) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    List<SectorDTO> getNeighborSectors(final SectorDTO sector) {
        final List<SectorDTO> out = new ArrayList<SectorDTO>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    final SectorDTO thisSector = service.getSectorByCoordinates(scenarioId, gameId, sector.getRegionId(), sector.getX() + i, sector.getY() + j);
                    if (thisSector != null) {
                        out.add(thisSector);
                    }
                }
            }
        }
        return out;
    }

    /**
     * Method that gets all the allied armies and formats them
     * in a way good and proper for the presentation in the client.
     *
     * @param sector the sector to inspect.
     * @return A map of sectorId- FleetDTO.
     */
    private List<FleetDTO> getFleets(final SectorDTO sector) {
        final List<FleetDTO> out = new ArrayList<FleetDTO>();
        try {
            for (FleetDTO fleet : service.getFleetsBySector(scenarioId, sector.getId())) {
                if (fleet.getFleetId() > 0) {
                    if (nationsToShow.get(fleet.getNationId())) {
                        out.add(fleet);
                    }

                } else if (fleet.getFleetId() == 0) {
                    if (fleet.getShips() != null) {
                        final List<ShipDTO> fleetShips = new ArrayList<ShipDTO>(fleet.getShips().values());
                        for (ShipDTO ship : fleetShips) {
                            if (!nationsToShow.get(ship.getNationId())) {
                                fleet.getShips().remove(ship.getId());
                            }
                        }

                        if (!fleet.getShips().isEmpty()) {
                            out.add(fleet);
                        }
                    }

                }
            }

        } catch (Exception ignore) {
            LOGGER.debug("error loading foreign fleets", ignore);
        }

        return out;
    }

    private List<ArmyDTO> getArmies(final SectorDTO sector) {
        final List<ArmyDTO> out = new ArrayList<ArmyDTO>();
        try {
            for (ArmyDTO army : service.getArmiesBySector(scenarioId, sector.getId())) {
                if (army.getArmyId() > 0) {
                    if (nationsToShow.get(army.getNationId())) {
                        out.add(army);
                    }

                } else if (army.getArmyId() == 0) {
                    final List<CorpDTO> armyCorps = new ArrayList<CorpDTO>(army.getCorps().values());
                    for (CorpDTO corp : armyCorps) {
                        if (corp.getCorpId() > 0) {
                            if (!nationsToShow.get(corp.getNationId())) {
                                army.getCorps().remove(corp.getCorpId());
                            }

                        } else if (corp.getCorpId() == 0) {
                            final List<BrigadeDTO> corpBrigades = new ArrayList<BrigadeDTO>(corp.getBrigades().values());
                            for (BrigadeDTO brigade : corpBrigades) {
                                if (!nationsToShow.get(brigade.getNationId())) {
                                    corp.getBrigades().remove(brigade.getBrigadeId());
                                }
                            }
                            if (corp.getBrigades().isEmpty()) {
                                army.getCorps().remove(corp.getCorpId());
                            }
                        }
                    }

                    if (!army.getCorps().isEmpty()) {
                        out.add(army);
                    }
                }
            }

        } catch (Exception ignore) {
            LOGGER.debug("error loading foreign armies", ignore);
        }

        return out;
    }

    /**
     * Method that gets all the allied baggage rains and formats them
     * in a way good and proper for the presentation in the client
     *
     * @return A collection of BaggageTrainDTO
     */
    private List<BaggageTrainDTO> getBaggageTrains(SectorDTO sector) {
        List<BaggageTrainDTO> out = new ArrayList<BaggageTrainDTO>();
        try {
            for (BaggageTrainDTO bTrain : service.getBaggageTrainsBySector(scenarioId, sector.getId(), nationId, gameId)) {
                if (nationsToShow.get(bTrain.getNationId()) && bTrain.getLoadedUnitsMap().containsKey(ArmyConstants.BRIGADE)
                        && bTrain.getLoadedUnitsMap().get(ArmyConstants.BRIGADE).size() > 0) {
                    out.add(bTrain);
                }
            }
        } catch (Exception ex) {
        }
        return out;
    }


}
