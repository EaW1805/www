package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.common.*;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.*;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.GoodDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;

import java.util.*;

public class EditorStore {

    private static EditorStore instance;

    public static EditorStore getInstance() {
        if (instance == null) {
            instance = new EditorStore();
        }
        return instance;
    }

    private EditorStore(){}


    //the current game id that we are editing.
    private int gameId;

    private List<NationDTO> nations = new ArrayList<NationDTO>();

    private List<GoodDTO> goods = new ArrayList<GoodDTO>();

    private List<NaturalResourceDTO> resources = new ArrayList<NaturalResourceDTO>();

    private List<TerrainDTO> terrains = new ArrayList<TerrainDTO>();

    private List<ProductionSiteDTO> prSites = new ArrayList<ProductionSiteDTO>();
    private Map<Integer, ProductionSiteDTO> prSitesMap = new HashMap<Integer, ProductionSiteDTO>();

//    private Map<RegionDTO, List<SectorDTO>> regionsMap = new TreeMap<RegionDTO, List<SectorDTO>>();

    private List<RegionDTO> regions = new ArrayList<RegionDTO>();

    private Map<Integer, List<SectorDTO>> regionToSectors = new TreeMap<Integer, List<SectorDTO>>();

    private Map<Integer, TerrainDTO> idToTerrain = new TreeMap<Integer, TerrainDTO>();

    private Map<Character, NationDTO> codeToNation = new HashMap<Character, NationDTO>();

    private Map<Integer, Map<Integer, WarehouseDTO>> regionToNationToWarehouse = new HashMap<Integer, Map<Integer, WarehouseDTO>>();
               //region :     x :            y :       brigade id : brigade
    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>> brigades = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>> corps = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>> armies = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> commanders = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, SpyDTO>>>> spies = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, SpyDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>> baggageTrains = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, ShipDTO>>>> ships = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, ShipDTO>>>>();

    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>> fleets = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>>();


    private List<ArmyTypeDTO> armyTypes = new ArrayList<ArmyTypeDTO>();
    private List<ShipTypeDTO> shipTypes = new ArrayList<ShipTypeDTO>();
    private List<RankDTO> ranks = new ArrayList<RankDTO>();

    private Map<Integer, List<ArmyTypeDTO>> nationToArmyTypes = new HashMap<Integer, List<ArmyTypeDTO>>();

    private Map<Integer, List<String>> nationToCommanderNames = new HashMap<Integer, List<String>>();

    private List<JumpOffDTO> jumpOffPoints = new ArrayList<JumpOffDTO>();

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setStaticData(List<NationDTO> nations, List<GoodDTO> goods, List<NaturalResourceDTO> resources, List<TerrainDTO> terrains, List<ProductionSiteDTO> prSites,
                              final List<ArmyTypeDTO> armyTypes, final List<RankDTO> ranks, final List<ShipTypeDTO> shipTypes) {
        this.nations = nations;
        this.goods = goods;
        this.resources = resources;
        this.terrains = terrains;
        this.prSites = prSites;
        this.armyTypes = armyTypes;
        this.ranks = ranks;
        this.shipTypes = shipTypes;
        for (TerrainDTO terrain : terrains) {
            idToTerrain.put(terrain.getId(), terrain);
        }
        for (NationDTO nation : nations) {
            codeToNation.put(nation.getCode(), nation);
        }

        for (ArmyTypeDTO type : armyTypes) {
            if (!nationToArmyTypes.containsKey(type.getNationId())) {
                nationToArmyTypes.put(type.getNationId(), new ArrayList<ArmyTypeDTO>());
            }
            nationToArmyTypes.get(type.getNationId()).add(type);
        }
        for (ProductionSiteDTO prSite : prSites) {
            prSitesMap.put(prSite.getId(), prSite);
        }
    }

    public void setRegionsMap(final Map<RegionDTO, List<SectorDTO>> regionsMap) {
        for (Map.Entry<RegionDTO, List<SectorDTO>> entry : regionsMap.entrySet()) {
            regions.add(entry.getKey());
            regionToSectors.put(entry.getKey().getRegionId(), entry.getValue());

            brigades.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>());
            corps.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, CorpDTO>>>());
            armies.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, ArmyDTO>>>());
            fleets.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, FleetDTO>>>());
            ships.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, ShipDTO>>>());
            commanders.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, CommanderDTO>>>());
            spies.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, SpyDTO>>>());
            baggageTrains.put(entry.getKey().getRegionId(), new HashMap<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>());
        }
    }

    public void setWarehouseData(final Map<Integer, Map<Integer, WarehouseDTO>> in) {
        regionToNationToWarehouse = in;
    }

    public void setArmyData(final List<BrigadeDTO> inB,
                            final List<CorpDTO> inC,
                            final List<ArmyDTO> inA,
                            final List<SpyDTO> inS,
                            final List<BaggageTrainDTO> inT,
                            final List<CommanderDTO> inCo,
                            final List<ShipDTO> inSh,
                            final List<FleetDTO> inF,
                            final List<JumpOffDTO> inJ) {
        for (BrigadeDTO brigade : inB) {
            if (!brigades.get(brigade.getRegionId()).containsKey(brigade.getX())) {
                brigades.get(brigade.getRegionId()).put(brigade.getX(), new HashMap<Integer, Map<Integer, BrigadeDTO>>());
            }
            if (!brigades.get(brigade.getRegionId()).get(brigade.getX()).containsKey(brigade.getY())) {
                brigades.get(brigade.getRegionId()).get(brigade.getX()).put(brigade.getY(), new HashMap<Integer, BrigadeDTO>());
            }
            brigades.get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).put(brigade.getBrigadeId(), brigade);
        }
        for (CorpDTO corp : inC) {
            if (!corps.get(corp.getRegionId()).containsKey(corp.getX())) {
                corps.get(corp.getRegionId()).put(corp.getX(), new HashMap<Integer, Map<Integer, CorpDTO>>());
            }
            if (!corps.get(corp.getRegionId()).get(corp.getX()).containsKey(corp.getY())) {
                corps.get(corp.getRegionId()).get(corp.getX()).put(corp.getY(), new HashMap<Integer, CorpDTO>());
            }
            corps.get(corp.getRegionId()).get(corp.getX()).get(corp.getY()).put(corp.getCorpId(), corp);
        }
        for (ArmyDTO army : inA) {
            if (!armies.get(army.getRegionId()).containsKey(army.getX())) {
                armies.get(army.getRegionId()).put(army.getX(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
            }
            if (!armies.get(army.getRegionId()).get(army.getX()).containsKey(army.getY())) {
                armies.get(army.getRegionId()).get(army.getX()).put(army.getY(), new HashMap<Integer, ArmyDTO>());
            }
            armies.get(army.getRegionId()).get(army.getX()).get(army.getY()).put(army.getArmyId(), army);
        }
        for (SpyDTO spy : inS) {
            if (!spies.get(spy.getRegionId()).containsKey(spy.getX())) {
                spies.get(spy.getRegionId()).put(spy.getX(), new HashMap<Integer, Map<Integer, SpyDTO>>());
            }
            if (!spies.get(spy.getRegionId()).get(spy.getX()).containsKey(spy.getY())) {
                spies.get(spy.getRegionId()).get(spy.getX()).put(spy.getY(), new HashMap<Integer, SpyDTO>());
            }
            spies.get(spy.getRegionId()).get(spy.getX()).get(spy.getY()).put(spy.getSpyId(), spy);
        }
        for (BaggageTrainDTO bTrain : inT) {
            if (!baggageTrains.get(bTrain.getRegionId()).containsKey(bTrain.getX())) {
                baggageTrains.get(bTrain.getRegionId()).put(bTrain.getX(), new HashMap<Integer, Map<Integer, BaggageTrainDTO>>());
            }
            if (!baggageTrains.get(bTrain.getRegionId()).get(bTrain.getX()).containsKey(bTrain.getY())) {
                baggageTrains.get(bTrain.getRegionId()).get(bTrain.getX()).put(bTrain.getY(), new HashMap<Integer, BaggageTrainDTO>());
            }
            baggageTrains.get(bTrain.getRegionId()).get(bTrain.getX()).get(bTrain.getY()).put(bTrain.getId(), bTrain);
        }
        for (CommanderDTO commander : inCo) {
            if (!commanders.get(commander.getRegionId()).containsKey(commander.getX())) {
                commanders.get(commander.getRegionId()).put(commander.getX(), new HashMap<Integer, Map<Integer, CommanderDTO>>());
            }
            if (!commanders.get(commander.getRegionId()).get(commander.getX()).containsKey(commander.getY())) {
                commanders.get(commander.getRegionId()).get(commander.getX()).put(commander.getY(), new HashMap<Integer, CommanderDTO>());
            }
            commanders.get(commander.getRegionId()).get(commander.getX()).get(commander.getY()).put(commander.getId(), commander);
        }
        for (ShipDTO ship : inSh) {
            if (!ships.get(ship.getRegionId()).containsKey(ship.getX())) {
                ships.get(ship.getRegionId()).put(ship.getX(), new HashMap<Integer, Map<Integer, ShipDTO>>());
            }
            if (!ships.get(ship.getRegionId()).get(ship.getX()).containsKey(ship.getY())) {
                ships.get(ship.getRegionId()).get(ship.getX()).put(ship.getY(), new HashMap<Integer, ShipDTO>());
            }
            ships.get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).put(ship.getId(), ship);
        }
        for (FleetDTO fleet : inF) {
            if (!fleets.get(fleet.getRegionId()).containsKey(fleet.getX())) {
                fleets.get(fleet.getRegionId()).put(fleet.getX(), new HashMap<Integer, Map<Integer, FleetDTO>>());
            }
            if (!fleets.get(fleet.getRegionId()).get(fleet.getX()).containsKey(fleet.getY())) {
                fleets.get(fleet.getRegionId()).get(fleet.getX()).put(fleet.getY(), new HashMap<Integer, FleetDTO>());
            }
            fleets.get(fleet.getRegionId()).get(fleet.getX()).get(fleet.getY()).put(fleet.getFleetId(), fleet);
        }
        jumpOffPoints.addAll(inJ);
    }

    public void setCommanderNamesData(final Map<Integer, List<String>> in) {
        nationToCommanderNames = in;
    }

    public List<RegionDTO> getRegions() {
        return regions;
    }

    public Map<Integer, List<SectorDTO>> getRegionSectors() {
        return regionToSectors;
    }

    public List<NationDTO> getNations() {
        return nations;
    }


    public List<GoodDTO> getGoods() {
        return goods;
    }

    public List<NaturalResourceDTO> getResources() {
        return resources;
    }

    public List<TerrainDTO> getTerrains() {
        return terrains;
    }

    public List<ProductionSiteDTO> getPrSites() {
        return prSites;
    }

    public Map<Integer, TerrainDTO> getIdToTerrain() {
        return idToTerrain;
    }

    public NationDTO getNationByCode(char code) {
        if (codeToNation.containsKey(code)) {
            return codeToNation.get(code);
        }
        return null;
    }

    public WarehouseDTO getWarehouseByRegionNation(final int regionId, final int nationId) {
        return regionToNationToWarehouse.get(regionId).get(nationId);
    }

    public Map<Integer, Map<Integer, WarehouseDTO>> getRegionToNationToWarehouse() {
        return regionToNationToWarehouse;
    }

    public List<ArmyTypeDTO> getArmyTypesByNation(final int nationId) {
        return nationToArmyTypes.get(nationId);
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>>> getBrigades() {
        return brigades;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>> getCorps() {
        return corps;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>>> getArmies() {
        return armies;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>>> getCommanders() {
        return commanders;
    }

    public List<RankDTO> getRanks() {
        return ranks;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, SpyDTO>>>> getSpies() {
        return spies;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, BaggageTrainDTO>>>> getBaggageTrains() {
        return baggageTrains;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>> getFleets() {
        return fleets;
    }

    public List<String> getCommanderNamesByNation(final int nationId) {
        if (nationToCommanderNames.containsKey(nationId)) {
            return nationToCommanderNames.get(nationId);
        }
        return new ArrayList<String>();
    }

    public List<ShipTypeDTO> getShipTypes() {
        return shipTypes;
    }

    public Map<Integer, Map<Integer, Map<Integer, Map<Integer, ShipDTO>>>> getShips() {
        return ships;
    }

    public Map<Integer, ProductionSiteDTO> getPrSitesMap() {
        return prSitesMap;
    }

    public List<JumpOffDTO> getJumpOffPoints() {
        return jumpOffPoints;
    }

    public JumpOffDTO getJumpOffBySector(SectorDTO sector) {
        for (final JumpOffDTO jump : EditorStore.getInstance().getJumpOffPoints()) {
            if (jump.getDepartureRegion() == sector.getRegionId()
                    && jump.getDepartureX() == sector.getX()
                    && jump.getDepartureY() == sector.getY()) {
                return jump;

            }
        }
        return null;
    }

    public boolean hasSectorJumpOff(SectorDTO sector) {
        return getJumpOffBySector(sector) != null;
    }
}
