package com.eaw1805.www.controllers.remote.scenario;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.common.*;
import com.eaw1805.data.dto.converters.*;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.GoodDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.managers.beans.*;
import com.eaw1805.data.model.Game;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.army.*;
import com.eaw1805.data.model.economy.BaggageTrain;
import com.eaw1805.data.model.economy.Good;
import com.eaw1805.data.model.economy.Warehouse;
import com.eaw1805.data.model.fleet.Fleet;
import com.eaw1805.data.model.fleet.Ship;
import com.eaw1805.data.model.map.*;
import com.eaw1805.www.controllers.site.ArticleManager;
import com.eaw1805.www.hibernate.ScenarioContextHolder;
import com.eaw1805.www.scenario.remote.EmpireScenarioRpcService;
import com.eaw1805.www.scenario.stores.ArmyData;
import com.eaw1805.www.scenario.stores.StaticEditorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

/**
 * RPC implementation used by fieldbattle gwt module
 * to perform queries with the server.
 */
public class EmpireScenarioRpcServiceImpl
        extends RemoteServiceServlet
        implements EmpireScenarioRpcService {



    /**
     * Retrieves all the regions for a game.
     *
     * @param gameId The game id to search the regions and sectors for.
     * @return A map of regions and their sectors.
     */
    public Map<RegionDTO, List<SectorDTO>> getRegionData(final int gameId) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        final Map<RegionDTO, List<SectorDTO>> out = new HashMap<RegionDTO, List<SectorDTO>>();

        final Game thisGame = gameManager.getByID(gameId);
        final List<Region> gameRegions = regionManager.list(thisGame);
        for (Region region : gameRegions) {
            final List<SectorDTO> regionSectors = SectorConverter.convert(sectorManager.listByGameRegion(thisGame, region));
            out.put(RegionConverter.convert(region), regionSectors);
        }
        return out;
    }

    public int saveData(final int gameId, final List<RegionDTO> regions,
                        final Map<Integer, List<SectorDTO>> regionSectors,
                        final Map<Integer, Map<Integer, WarehouseDTO>> regionNationWarehouses,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>> brigades,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>> corps,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>> armies,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> commanders,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, SpyDTO>>>> spies,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>> bTrains,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, ShipDTO>>>> ships,
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>> fleets,
                        final List<JumpOffDTO> jumpOffs
                        ) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        final Game thisGame = gameManager.getByID(gameId);

        List<Region> scenarioRegions = regionManager.list(thisGame);
        final Map<Integer, Boolean> regionToDeleted = new HashMap<Integer, Boolean>();
        for (Region region : scenarioRegions) {
            //lets assume that all regions have been deleted.
            regionToDeleted.put(region.getId(), true);
            regionManager.deleteScenarioRegionAssoc(region.getId());
        }

        final Map<Integer, Integer> customToDBId = new HashMap<Integer, Integer>();
        boolean needsRefresh = false;
        for (final RegionDTO region : regions) {
            int customId = region.getRegionId();
            final Region dbRegion;
            if (region.getRegionId() < 0) {
                needsRefresh = true;
                dbRegion = new Region();
                dbRegion.setName(region.getName());
                dbRegion.setGame(thisGame);
                regionManager.add(dbRegion);

            } else {
                dbRegion = regionManager.getByID(region.getRegionId());
                dbRegion.setName(region.getName());
                regionManager.update(dbRegion);

                //now here we are, this region has not been deleted.
                regionToDeleted.put(dbRegion.getId(), false);
            }
            System.out.println(customId + " === " + dbRegion.getId());
            customToDBId.put(customId, dbRegion.getId());
            //this means it is a newly created region and needs to be saved
            List<Sector> dbSectors = prepareDBSectors(thisGame, dbRegion, regionSectors.get(customId));
            System.out.println("match? " + dbSectors.size() + " ---- " + regionSectors.get(customId).size());
            for (int index = 0; index < regionSectors.get(customId).size(); index++) {
                final Sector dbSector = dbSectors.get(index);
                final SectorDTO sector = regionSectors.get(customId).get(index);
                if (sector.getName() == null) {
                    dbSector.setName("");
                } else {
                    dbSector.setName(sector.getName());
                }

                dbSector.setPosition(new Position());
                dbSector.getPosition().setGame(thisGame);
                dbSector.getPosition().setX(sector.getX());
                dbSector.getPosition().setY(sector.getY());
                dbSector.getPosition().setRegion(dbRegion);
                if (sector.getNationDTO() == null) {
                    dbSector.setNation(nationManager.getByID(-1));//the free nation
                } else {
                    dbSector.setNation(nationManager.getByID(sector.getNationDTO().getNationId()));
                }
                dbSector.setTempNation(dbSector.getNation());
                dbSector.setClimaticZone(sector.getClimaticZone());
                dbSector.setPoliticalSphere(sector.getPoliticalSphere());
                dbSector.setPopulation(sector.getPopulation());
                if (sector.getNatResDTO() == null) {
                    dbSector.setNaturalResource(null);
                } else {
                    dbSector.setNaturalResource(resourceManager.getByID(sector.getNatResDTO().getId()));
                }
                dbSector.setTerrain(terrainManager.getByID(sector.getTerrain().getId()));
                if (sector.getProductionSiteDTO() == null) {
                    dbSector.setProductionSite(null);
                } else {
                    dbSector.setProductionSite(prSiteManager.getByID(sector.getProductionSiteDTO().getId()));
                }
                dbSector.setTradeCity(sector.getTradeCity());
                sectorManager.update(dbSector);
            }

            for (final Map.Entry<Integer, WarehouseDTO> whs : regionNationWarehouses.get(customId).entrySet()) {
                final Nation nation = nationManager.getByID(whs.getKey());
                final WarehouseDTO wh = whs.getValue();
                final Warehouse dbWh = fixWarehouse(thisGame, dbRegion, nation);
                for (StoredGoodDTO good : wh.getGoodsDTO().values()) {
                    dbWh.getStoredGoodsQnt().put(good.getTpe(), good.getQte());
                }
                warehouseManager.update(dbWh);
            }


            //add new armies
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, ArmyDTO>>> entry : armies.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, ArmyDTO>> entry2 : entry.getValue().entrySet()) {
                    for (final ArmyDTO army : entry2.getValue().values()) {
                        Army dbArmy = new Army();
                        dbArmy.setNation(nationManager.getByID(army.getNationId()));
                        dbArmy.setPosition(new Position());
                        dbArmy.getPosition().setGame(thisGame);
                        dbArmy.getPosition().setX(army.getX());
                        dbArmy.getPosition().setY(army.getY());
                        dbArmy.getPosition().setRegion(dbRegion);
                        dbArmy.setName(army.getName());
                        dbArmy.setCorps(new HashSet<Corp>());
                        armyManager.add(dbArmy);
                        for (CorpDTO corp : army.getCorps().values()) {
                            Corp dbCorp = new Corp();
                            dbCorp.setArmy(dbArmy.getArmyId());
                            dbCorp.setNation(dbArmy.getNation());
                            dbCorp.setName(corp.getName());
                            dbCorp.setPosition((Position)dbArmy.getPosition().clone());
                            dbCorp.setBrigades(new HashSet<Brigade>());
                            corpManager.add(dbCorp);
                            dbArmy.getCorps().add(dbCorp);
                            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                                final Brigade dbBrigade = new Brigade();
                                dbBrigade.setFromInit(true);
                                dbBrigade.setNation(dbCorp.getNation());
                                dbBrigade.setPosition((Position) dbCorp.getPosition().clone());
                                dbBrigade.setName(brigade.getName());
                                dbBrigade.setCorp(dbCorp.getCorpId());
                                dbBrigade.setBattalions(new HashSet<Battalion>());
                                brigadeManager.add(dbBrigade);
                                dbCorp.getBrigades().add(dbBrigade);
                                for (BattalionDTO batt : brigade.getBattalions()) {
                                    Battalion dbBatt = new Battalion();
                                    dbBatt.setCarrierInfo(new CarrierInfo());
                                    dbBatt.getCarrierInfo().setCarrierId(0);
                                    dbBatt.getCarrierInfo().setCarrierType(0);
                                    dbBatt.setBrigade(dbBrigade);
                                    dbBatt.setExperience(batt.getExperience());
                                    dbBatt.setOrder(batt.getOrder());
                                    dbBatt.setHeadcount(batt.getHeadcount());
                                    dbBatt.setType(armyTypeManager.getByID(batt.getEmpireArmyType().getId()));
                                    battalionManager.add(dbBatt);
                                    dbBrigade.getBattalions().add(dbBatt);
                                }
                                brigadeManager.update(dbBrigade);
                            }
                            corpManager.update(dbCorp);
                        }
                        armyManager.update(dbArmy);
                    }
                }
            }

            //add new corps
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, CorpDTO>>> entry : corps.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, CorpDTO>> entry2 : entry.getValue().entrySet()) {
                    for (CorpDTO corp : entry2.getValue().values()) {
                        if (corp.getArmyId() != 0) {
                            //already added this corps when added the army
                            continue;
                        }
                        Corp dbCorp = new Corp();
                        dbCorp.setArmy(0);
                        dbCorp.setNation(nationManager.getByID(corp.getNationId()));
                        dbCorp.setName(corp.getName());
                        dbCorp.setPosition(new Position());
                        dbCorp.getPosition().setRegion(dbRegion);
                        dbCorp.getPosition().setX(corp.getX());
                        dbCorp.getPosition().setY(corp.getY());
                        dbCorp.getPosition().setGame(thisGame);
                        dbCorp.setBrigades(new HashSet<Brigade>());
                        corpManager.add(dbCorp);
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            final Brigade dbBrigade = new Brigade();
                            dbBrigade.setFromInit(true);
                            dbBrigade.setNation(dbCorp.getNation());
                            dbBrigade.setPosition((Position) dbCorp.getPosition().clone());
                            dbBrigade.setName(brigade.getName());
                            dbBrigade.setCorp(dbCorp.getCorpId());
                            dbBrigade.setBattalions(new HashSet<Battalion>());
                            brigadeManager.add(dbBrigade);
                            dbCorp.getBrigades().add(dbBrigade);
                            for (BattalionDTO batt : brigade.getBattalions()) {
                                Battalion dbBatt = new Battalion();
                                dbBatt.setCarrierInfo(new CarrierInfo());
                                dbBatt.getCarrierInfo().setCarrierId(0);
                                dbBatt.getCarrierInfo().setCarrierType(0);
                                dbBatt.setBrigade(dbBrigade);
                                dbBatt.setExperience(batt.getExperience());
                                dbBatt.setOrder(batt.getOrder());
                                dbBatt.setHeadcount(batt.getHeadcount());
                                dbBatt.setType(armyTypeManager.getByID(batt.getEmpireArmyType().getId()));
                                battalionManager.add(dbBatt);
                                dbBrigade.getBattalions().add(dbBatt);
                            }
                            brigadeManager.update(dbBrigade);
                        }
                        corpManager.update(dbCorp);
                    }

                }
            }


            //add new brigades
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, BrigadeDTO>>> entry : brigades.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, BrigadeDTO>> entry2 : entry.getValue().entrySet()) {
                    for (BrigadeDTO brigade : entry2.getValue().values()) {
                        if (brigade.getCorpId() != 0) {
                            //you already added this
                            continue;
                        }
                        final Brigade dbBrigade = new Brigade();
                        dbBrigade.setFromInit(true);
                        dbBrigade.setNation(nationManager.getByID(brigade.getNationId()));
                        dbBrigade.setPosition(new Position());
                        dbBrigade.getPosition().setRegion(dbRegion);
                        dbBrigade.getPosition().setX(brigade.getX());
                        dbBrigade.getPosition().setY(brigade.getY());
                        dbBrigade.getPosition().setGame(thisGame);
                        dbBrigade.setName(brigade.getName());
                        dbBrigade.setCorp(0);
                        dbBrigade.setBattalions(new HashSet<Battalion>());
                        brigadeManager.add(dbBrigade);
                        for (BattalionDTO batt : brigade.getBattalions()) {
                            Battalion dbBatt = new Battalion();
                            dbBatt.setCarrierInfo(new CarrierInfo());
                            dbBatt.getCarrierInfo().setCarrierId(0);
                            dbBatt.getCarrierInfo().setCarrierType(0);
                            dbBatt.setBrigade(dbBrigade);
                            dbBatt.setExperience(batt.getExperience());
                            dbBatt.setOrder(batt.getOrder());
                            dbBatt.setHeadcount(batt.getHeadcount());
                            dbBatt.setType(armyTypeManager.getByID(batt.getEmpireArmyType().getId()));
                            battalionManager.add(dbBatt);
                            dbBrigade.getBattalions().add(dbBatt);
                        }
                        brigadeManager.update(dbBrigade);
                    }
                }
            }

            //add new brigades
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, CommanderDTO>>> entry : commanders.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, CommanderDTO>> entry2 : entry.getValue().entrySet()) {
                    for (CommanderDTO commander : entry2.getValue().values()) {
                        Commander dbCommander = new Commander();
                        dbCommander.setName(commander.getName());
                        dbCommander.setNation(nationManager.getByID(commander.getNationId()));
                        dbCommander.setCaptured(dbCommander.getNation());
                        dbCommander.setPosition(new Position());
                        dbCommander.getPosition().setGame(thisGame);
                        dbCommander.getPosition().setX(commander.getX());
                        dbCommander.getPosition().setY(commander.getY());
                        dbCommander.getPosition().setRegion(dbRegion);
                        dbCommander.setArmy(0);
                        dbCommander.setCorp(0);
                        dbCommander.setCarrierInfo(new CarrierInfo());
                        dbCommander.getCarrierInfo().setCarrierId(0);
                        dbCommander.getCarrierInfo().setCarrierType(0);
                        dbCommander.setRank(rankManager.getByID(commander.getRank().getRankId()));
                        dbCommander.setStrc(commander.getStrc());
                        dbCommander.setComc(commander.getComc());
                        commanderManager.add(dbCommander);
                    }
                }
            }

            //add new spies
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, SpyDTO>>> entry : spies.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, SpyDTO>> entry2 : entry.getValue().entrySet()) {
                    for (SpyDTO spy : entry2.getValue().values()) {
                        Spy dbSpy = new Spy();
                        dbSpy.setNation(nationManager.getByID(spy.getNationId()));
                        dbSpy.setCarrierInfo(new CarrierInfo());
                        dbSpy.getCarrierInfo().setCarrierId(0);
                        dbSpy.getCarrierInfo().setCarrierType(0);
                        dbSpy.setPosition(new Position());
                        dbSpy.getPosition().setGame(thisGame);
                        dbSpy.getPosition().setRegion(dbRegion);
                        dbSpy.getPosition().setX(spy.getX());
                        dbSpy.getPosition().setY(spy.getY());
                        dbSpy.setName(spy.getName());
                        spyManager.add(dbSpy);
                    }
                }
            }

            //add new brigades
            for (final Map.Entry<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>> entry : bTrains.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, BaggageTrainDTO>> entry2 : entry.getValue().entrySet()) {
                    for (BaggageTrainDTO bTrain : entry2.getValue().values()) {
                        BaggageTrain dbBTrain = new BaggageTrain();
                        dbBTrain.setName(bTrain.getName());
                        dbBTrain.setNation(nationManager.getByID(bTrain.getNationId()));
                        dbBTrain.setPosition(new Position());
                        dbBTrain.getPosition().setGame(thisGame);
                        dbBTrain.getPosition().setRegion(dbRegion);
                        dbBTrain.getPosition().setX(bTrain.getX());
                        dbBTrain.getPosition().setY(bTrain.getY());
                        dbBTrain.setCondition(bTrain.getCondition());
                        dbBTrain.setStoredGoods(new HashMap<Integer, Integer>());
                        for (int good = GoodConstants.GOOD_MONEY; good <= GoodConstants.GOOD_COLONIAL; good++) {
                            dbBTrain.getStoredGoods().put(good, 0);
                        }
                        baggageTrainManager.add(dbBTrain);



                    }
                }
            }

            for (final Map.Entry<Integer, Map<Integer, Map<Integer, FleetDTO>>> entry : fleets.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, FleetDTO>> entry2 : entry.getValue().entrySet()) {
                    for (FleetDTO fleet : entry2.getValue().values()) {
                        Fleet dbFleet = new Fleet();
                        dbFleet.setName(fleet.getName());
                        dbFleet.setNation(nationManager.getByID(fleet.getNationId()));
                        dbFleet.setPosition(new Position());
                        dbFleet.getPosition().setGame(thisGame);
                        dbFleet.getPosition().setRegion(dbRegion);
                        dbFleet.getPosition().setX(fleet.getX());
                        dbFleet.getPosition().setY(fleet.getY());
                        fleetManager.add(dbFleet);
                        for (ShipDTO ship : fleet.getShips().values()) {
                            final Ship dbShip = new Ship();
                            dbShip.setCondition(ship.getCondition());
                            dbShip.setName(ship.getName());
                            dbShip.setPosition((Position) dbFleet.getPosition().clone());
                            dbShip.setNation(dbFleet.getNation());
                            dbShip.setCapturedByNation(0);
                            dbShip.setExp(ship.getExp());
                            dbShip.setFleet(dbFleet.getFleetId());
                            dbShip.setMarines(ship.getMarines());
                            dbShip.setType(shipTypeManager.getByID(ship.getType().getTypeId()));
                            shipManager.add(dbShip);
                        }
                    }
                }
            }

            for (final Map.Entry<Integer, Map<Integer, Map<Integer, ShipDTO>>> entry : ships.get(customId).entrySet()) {
                for (Map.Entry<Integer, Map<Integer, ShipDTO>> entry2 : entry.getValue().entrySet()) {
                    for (ShipDTO ship : entry2.getValue().values()) {
                        if (ship.getFleet() != 0) {
                            continue;
                        }
                        final Ship dbShip = new Ship();
                        dbShip.setCondition(ship.getCondition());
                        dbShip.setName(ship.getName());
                        dbShip.setPosition(new Position());
                        dbShip.getPosition().setGame(thisGame);
                        dbShip.getPosition().setRegion(dbRegion);
                        dbShip.getPosition().setX(ship.getX());
                        dbShip.getPosition().setY(ship.getY());
                        dbShip.setNation(nationManager.getByID(ship.getNationId()));
                        dbShip.setCapturedByNation(0);
                        dbShip.setExp(ship.getExp());
                        dbShip.setFleet(0);
                        dbShip.setMarines(ship.getMarines());
                        dbShip.setType(shipTypeManager.getByID(ship.getType().getTypeId()));
                        shipManager.add(dbShip);
                    }
                }
            }

        }

        for (final JumpOffDTO jump : jumpOffs) {
            JumpOff dbJump = new JumpOff();
            dbJump.setDeparture(new Position());
            dbJump.getDeparture().setGame(thisGame);
            dbJump.getDeparture().setX(jump.getDepartureX());
            dbJump.getDeparture().setY(jump.getDepartureY());
            dbJump.getDeparture().setRegion(regionManager.getByID(customToDBId.get(jump.getDepartureRegion())));
            dbJump.setDestination(new Position());
            dbJump.getDestination().setGame(thisGame);
            dbJump.getDestination().setX(jump.getDestinationX());
            dbJump.getDestination().setY(jump.getDestinationY());
            dbJump.getDestination().setRegion(regionManager.getByID(customToDBId.get(jump.getDestinationRegion())));
            jumpOffManager.add(dbJump);
        }

        //delete the client deleted regions
        for (Region region : regionManager.list(thisGame)) {
            if (regionToDeleted.containsKey(region.getId())
                    && regionToDeleted.get(region.getId())) {
                regionManager.delete(region);
            }
        }
        if (needsRefresh) {
            return 2;
        }
        return 1;

    }

    private Warehouse fixWarehouse(final Game game, final Region region, final Nation nation) {
        Warehouse wh = warehouseManager.getByNationRegion(game, nation, region);
        if (wh == null) {
            wh = new Warehouse();
            wh.setGame(game);
            wh.setRegion(region);
            wh.setNation(nation);
            wh.setStoredGoodsQnt(new HashMap<Integer, Integer>());
            for (int good = GoodConstants.GOOD_FIRST; good <= GoodConstants.GOOD_LAST; good++) {
                wh.getStoredGoodsQnt().put(good, 0);
            }
            warehouseManager.add(wh);
            return warehouseManager.getByNationRegion(game, nation, region);
        } else {
            return wh;
        }

    }

    private List<Sector> prepareDBSectors(final Game game, Region region, final List<SectorDTO> cSectors) {
        final List<Sector> dbSectors = sectorManager.listByGameRegion(game, region);
        if (dbSectors.size() < cSectors.size()) {
            for (int index = dbSectors.size() + 1; index <= cSectors.size(); index++) {
                final Sector blank = new Sector();
                blank.setName("");
                blank.setImage("");
                blank.setImageGeo("");
                blank.setPosition(new Position());
                blank.getPosition().setRegion(region);
                blank.getPosition().setGame(game);
                sectorManager.add(blank);
            }
        } else if (dbSectors.size() > cSectors.size()) {
            for (int index = cSectors.size(); index < dbSectors.size(); index++) {
                sectorManager.delete(dbSectors.get(index));
            }
        }
        return sectorManager.listByGameRegion(game, region);
    }

    public ArmyData getArmies(final int gameId) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        final Game thisGame = gameManager.getByID(gameId);
        List<BrigadeDTO> brigades = BrigadeConverter.convert(brigadeManager.listByGame(thisGame));
        List<CorpDTO> corps = CorpConverter.convert(corpManager.listGame(thisGame));
        List<ArmyDTO> armies = ArmyConverter.convert(armyManager.listGame(thisGame));
        ArmyData data = new ArmyData();
        for (BrigadeDTO brigade : brigades) {
            if (brigade.getCorpId() == 0) {
                data.getBrigades().add(brigade);
            }
        }
        for (CorpDTO corp : corps) {
            if (corp.getArmyId() == 0) {
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    data.getBrigades().add(brigade);
                }
                data.getCorps().add(corp);
            }
        }
        for (ArmyDTO army : armies) {
            for (CorpDTO corp : army.getCorps().values()) {
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    data.getBrigades().add(brigade);
                }
                data.getCorps().add(corp);
            }
            data.getArmies().add(army);
        }
        data.getSpies().addAll(SpyConverter.convert(spyManager.listByGame(thisGame)));
        data.getBaggageTrains().addAll(BaggageTrainConverter.convert(baggageTrainManager.listByGame(thisGame), goodManager));
        data.getCommanders().addAll(CommanderConverter.convert(commanderManager.listByGame(thisGame)));

        final List<FleetDTO> fleets = FleetConverter.convert(fleetManager.listByGame(thisGame), shipManager, goodManager) ;
        final List<ShipDTO> ships = ShipConverter.convert(shipManager.listByGame(thisGame), goodManager) ;
        for (FleetDTO fleet : fleets) {
            for (ShipDTO ship : fleet.getShips().values()) {
                data.getShips().add(ship);
            }
            data.getFleets().add(fleet);
        }
        for (ShipDTO ship : ships) {
            if (ship.getFleet() == 0) {
                data.getShips().add(ship);
            }
        }
        data.getJumpOffPoints().addAll(JumpOffConverter.convert(jumpOffManager.listByGame(thisGame)));
        return data;
    }

    public Map<Integer, List<String>> getCommanderNamesByNation(final int gameId) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        final Game thisGame = gameManager.getByID(gameId);
        final List<CommanderName> names = commanderNameManager.listGame(thisGame);
        final Map<Integer, List<String>> out = new HashMap<Integer, List<String>>();
        for (int index = 1; index <= 17; index++) {
            out.put(index, new ArrayList<String>());
        }
        for (final CommanderName name : names) {
            out.get(name.getNation().getId()).add(name.getName());
        }
        return out;
    }

    /**
     * This method returns to the client all the warehouses of the game.
     * They structure is region : nation : warehouse.
     *
     * @param gameId The game id to search for warehouses.
     *
     * @return The game warehouses.
     */
    public Map<Integer, Map<Integer, WarehouseDTO>> getWarehouses(final int gameId) {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        final Game thisGame = gameManager.getByID(gameId);
        final List<WarehouseDTO> gameWarehouses = WarehouseConverter.convert(warehouseManager.listByGame(thisGame), goodManager);
        final Map<Integer, Map<Integer, WarehouseDTO>> out = new HashMap<Integer, Map<Integer, WarehouseDTO>>();

        for (WarehouseDTO warehouse : gameWarehouses) {
            if (!out.containsKey(warehouse.getRegionId())) {
                out.put(warehouse.getRegionId(), new HashMap<Integer, WarehouseDTO>());
            }
            out.get(warehouse.getRegionId()).put(warehouse.getNationId(), warehouse);
        }
        return out;
    }

    public StaticEditorData getEditorStaticData() {
        ScenarioContextHolder.setScenario(HibernateUtil.DB_EDITOR);
        final List<Good> goods =  goodManager.list();
        final List<GoodDTO> goodsDto = new ArrayList<GoodDTO>();
        for (Good good : goods) {
            goodsDto.add(BaggageTrainConverter.convertGood(good));
        }
        final List<ProductionSiteDTO> prSites = ProductionSiteConverter.convert(prSiteManager.list());
        final List<TerrainDTO> terrains = TerrainConverter.convert(terrainManager.list());
        final List<NaturalResourceDTO> resources = NaturalResourceConverter.convert(resourceManager.list());
        final List<NationDTO> nations = NationConverter.convert(nationManager.list());
        final List<ArmyTypeDTO> armyTypes = ArmyTypeConverter.convert(armyTypeManager.list());
        final List<ShipTypeDTO> shipTypes = ShipTypeConverter.convert(shipTypeManager.list());
        final List<RankDTO> ranks = RankConverter.convert(rankManager.list());

        final StaticEditorData out = new StaticEditorData();
        out.setData(goodsDto, prSites, armyTypes, nations, resources, shipTypes, terrains, ranks);
        return out;
    }


    /**
     * SectorManagerBean object to perform queries with database for sectors.
     */
    @Autowired
    @Qualifier("sectorManagerBean")
    private transient SectorManagerBean sectorManager;

    /**
     * Instance of BrigadeManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("brigadeManagerBean")
    private transient BrigadeManagerBean brigadeManager;

    /**
     * Instance of NationManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("nationManagerBean")
    private transient NationManagerBean nationManager;

    /**
     * Instance of GameManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("gameManagerBean")
    private transient GameManagerBean gameManager;

    /**
     * Instance of CommanderManagerBean for transactions with the database.
     */
    @Autowired
    @Qualifier("commanderManagerBean")
    private transient CommanderManagerBean commanderManager;

    /**
     * Instance of ArticleManager.
     */
    @Autowired
    @Qualifier("articleManagerBean")
    private transient ArticleManager articleManager;

    @Autowired
    @Qualifier("regionManagerBean")
    private transient RegionManagerBean regionManager;

    @Autowired
    @Qualifier("goodManagerBean")
    private transient GoodManagerBean goodManager;

    @Autowired
    @Qualifier("productionSiteManagerBean")
    private transient ProductionSiteManagerBean prSiteManager;

    @Autowired
    @Qualifier("terrainManagerBean")
    private transient TerrainManagerBean terrainManager;

    @Autowired
    @Qualifier("naturalResourceManagerBean")
    private transient NaturalResourceManagerBean resourceManager;

    @Autowired
    @Qualifier("armyTypeManagerBean")
    private transient ArmyTypeManagerBean armyTypeManager;

    @Autowired
    @Qualifier("shipTypeManagerBean")
    private transient ShipTypeManagerBean shipTypeManager;

    @Autowired
    @Qualifier("warehouseManagerBean")
    private transient WarehouseManagerBean warehouseManager;

    @Autowired
    @Qualifier("rankManagerBean")
    private transient RankManagerBean rankManager;

    @Autowired
    @Qualifier("commanderNameManagerBean")
    private transient CommanderNameManagerBean commanderNameManager;

    @Autowired
    @Qualifier("corpManagerBean")
    private transient CorpManagerBean corpManager;

    @Autowired
    @Qualifier("armyManagerBean")
    private transient ArmyManagerBean armyManager;

    @Autowired
    @Qualifier("battalionManagerBean")
    private transient BattalionManagerBean battalionManager;

    @Autowired
    @Qualifier("spyManagerBean")
    private transient SpyManagerBean spyManager;

    @Autowired
    @Qualifier("baggageTrainManagerBean")
    private transient BaggageTrainManagerBean baggageTrainManager;

    @Autowired
    @Qualifier("fleetManagerBean")
    private transient FleetManagerBean fleetManager;

    @Autowired
    @Qualifier("shipManagerBean")
    private transient ShipManagerBean shipManager;

    @Autowired
    @Qualifier("jumpOffManagerBean")
    private transient JumpOffManagerBean jumpOffManager;

}
