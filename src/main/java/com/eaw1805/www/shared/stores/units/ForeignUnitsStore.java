package empire.webapp.shared.stores.units;

import empire.data.constants.ArmyConstants;
import empire.data.constants.RelationConstants;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.data.dto.web.army.SpyDTO;
import empire.data.dto.web.economy.BaggageTrainDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.ForeignUnits;
import empire.webapp.shared.stores.RelationsStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ForeignUnitsStore implements ArmyConstants, RelationConstants {

    private static ForeignUnitsStore ourInstance = null;

    private ForeignUnits foreignUnits;

    private boolean isClient = false, isInitialized = false;

    public static ForeignUnitsStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new ForeignUnitsStore();
        }
        return ourInstance;
    }

    public void initForeignUnits(final ForeignUnits intForeignUnits) {
        try {
            foreignUnits = intForeignUnits;
            LoadEventManager.loadForeignUnits(foreignUnits);
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize foreign units on nation", false);
        }
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(final boolean client) {
        isClient = client;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(final boolean initialized) {
        isInitialized = initialized;
    }

    /**
     * Get Foreign baggage trains for the given sector.
     *
     * @param sector The sector to search for.
     * @return The list of baggage trains.
     */
    public List<BaggageTrainDTO> getBaggageTrainsBySector(final SectorDTO sector) {
        try {
            return foreignUnits.getBaggageTrains().get(sector.getId());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get foreign commanders for the given sector.
     *
     * @param sector The sector to search for.
     * @return The list of commanders.
     */
    public List<CommanderDTO> getCommandersBySector(final SectorDTO sector) {
        try {
            return foreignUnits.getCommanders().get(sector.getId());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get foreign spies for the given sector.
     *
     * @param sector The sector to search for.
     * @return The list of spies.
     */
    public List<SpyDTO> getSpiesBySector(final SectorDTO sector) {
        try {
            return foreignUnits.getSpies().get(sector.getId());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get foreign armies for the given sector.
     *
     * @param sector The sector to search for.
     * @return The list of armies.
     */
    public List<ArmyDTO> getArmiesBySector(final SectorDTO sector) {
        try {
            return foreignUnits.getArmies().get(sector.getId());
        } catch (Exception e) {
            return null;
        }
    }

    public List<ArmyDTO> getArmiesBySectorRelation(final SectorDTO sector, final int... relations) {
        final Set<Integer> findRelations = new TreeSet<Integer>();
        for (int relation : relations) {
            findRelations.add(relation);
        }
        final List<ArmyDTO> out = new ArrayList<ArmyDTO>();
        ArmyDTO zeroArmy = new ArmyDTO();
        zeroArmy.setArmyId(0);
        zeroArmy.setX(sector.getX());
        zeroArmy.setY(sector.getY());
        zeroArmy.setXStart(sector.getX());
        zeroArmy.setYStart(sector.getY());
        zeroArmy.setName("Foreign corps");
        zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());

        CorpDTO zeroCorps = new CorpDTO();
        zeroCorps.setCorpId(0);
        zeroCorps.setX(sector.getX());
        zeroCorps.setY(sector.getY());
        zeroCorps.setXStart(sector.getX());
        zeroCorps.setYStart(sector.getY());
        zeroCorps.setName("Foreign corps");
        zeroCorps.setBrigades(new HashMap<Integer, BrigadeDTO>());
        try {
            for (ArmyDTO army : foreignUnits.getArmies().get(sector.getId())) {
                if (army.getArmyId() != 0) {
                    if (findRelations.contains(RelationsStore.getInstance().getOriginalRelationByNationId(army.getNationId()))) {
                        out.add(army);
                    }
                } else {
                    for (CorpDTO corps : army.getCorps().values()) {
                        if (corps.getCorpId() != 0) {
                            if (findRelations.contains(RelationsStore.getInstance().getOriginalRelationByNationId(corps.getNationId()))) {
                                zeroArmy.getCorps().put(corps.getCorpId(), corps);
                            }

                        } else {
                            for (BrigadeDTO brigade : corps.getBrigades().values()) {
                                if (findRelations.contains(RelationsStore.getInstance().getOriginalRelationByNationId(brigade.getNationId()))) {
                                    zeroCorps.getBrigades().put(brigade.getBrigadeId(), brigade);
                                }
                            }
                        }
                    }
                }
            }
            if (!zeroCorps.getBrigades().isEmpty()) {
                zeroArmy.getCorps().put(0, zeroCorps);
            }
            if (!zeroArmy.getCorps().isEmpty()) {
                out.add(zeroArmy);
            }
        } catch (Exception e) {
//            return null;
        }


        return out;
    }

    /**
     * Get foreign fleets for the given sector.
     *
     * @param sector The sector to search for.
     * @return The list of fleets.
     */
    public List<FleetDTO> getFleetsBySector(final SectorDTO sector) {
        try {
            return foreignUnits.getFleets().get(sector.getId());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isUnitForeign(final int sectorId, final int type, final int unitId) {
        if (foreignUnits == null) {
            //in case foreign units have not been yet initialized,
            //just return that the unit is not foreign, which is true, and that is nice.
            return false;
        }
        switch (type) {
            case FLEET:
                if (foreignUnits.getFleets().get(sectorId) != null) {
                    for (FleetDTO fleet : foreignUnits.getFleets().get(sectorId)) {
                        if (fleet.getFleetId() == unitId) {
                            return true;
                        }
                    }
                }
                break;
            case SHIP:
                if (foreignUnits.getFleets().get(sectorId) != null) {
                    for (FleetDTO fleet : foreignUnits.getFleets().get(sectorId)) {
                        if (fleet.getShips() != null) {
                            for (ShipDTO ship : fleet.getShips().values()) {
                                if (ship.getId() == unitId) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
            case COMMANDER:
                if (foreignUnits.getCommanders().get(sectorId) != null) {
                    for (CommanderDTO commander : foreignUnits.getCommanders().get(sectorId)) {
                        if (commander.getId() == unitId) {
                            return true;
                        }
                    }
                }
                break;
            case SPY:
                if (foreignUnits.getSpies().get(sectorId) != null) {
                    for (SpyDTO spy : foreignUnits.getSpies().get(sectorId)) {
                        if (spy.getSpyId() == unitId) {
                            return true;
                        }
                    }
                }
                break;
            case ARMY:
                if (foreignUnits.getArmies().get(sectorId) != null) {
                    for (ArmyDTO army : foreignUnits.getArmies().get(sectorId)) {
                        if (army.getArmyId() == unitId) {
                            return true;
                        }
                    }
                }
                break;
            case CORPS:
                if (foreignUnits.getArmies().get(sectorId) != null) {
                    for (ArmyDTO army : foreignUnits.getArmies().get(sectorId)) {
                        if (army.getCorps() != null) {
                            for (CorpDTO corp : army.getCorps().values()) {
                                if (corp.getCorpId() == unitId) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
            case BRIGADE:
                if (foreignUnits.getArmies().get(sectorId) != null) {
                    for (ArmyDTO army : foreignUnits.getArmies().get(sectorId)) {
                        if (army.getCorps() != null) {
                            for (CorpDTO corp : army.getCorps().values()) {
                                if (corp.getBrigades() != null) {
                                    for (BrigadeDTO brigade : corp.getBrigades().values()) {
                                        if (brigade.getBrigadeId() == unitId) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case BAGGAGETRAIN:
                if (foreignUnits.getBaggageTrains().get(sectorId) != null) {
                    for (BaggageTrainDTO bTrain : foreignUnits.getBaggageTrains().get(sectorId)) {
                        if (bTrain.getId() == unitId) {
                            return true;
                        }
                    }
                }
            default:
                //do nothing here

        }

        return false;
    }

    public ArmyDTO getArmyBySectorAndId(final int sectorId, final int armyId) {
        if (foreignUnits.getArmies().get(sectorId) != null) {
            for (ArmyDTO army : foreignUnits.getArmies().get(sectorId)) {
                if (army.getArmyId() == armyId) {
                    return army;
                }
            }
        }
        return null;
    }

    public CorpDTO getCorpBySectorAndId(final int sectorId, final int corpId) {
        if (foreignUnits.getArmies().get(sectorId) != null) {
            for (ArmyDTO army : foreignUnits.getArmies().get(sectorId)) {
                if (army.getCorps().containsKey(corpId)) {
                    return army.getCorps().get(corpId);
                }
            }
        }
        return null;
    }

    public BrigadeDTO getBrigadeBySectorAndId(final int sectorId, final int brigadeId) {
        if (foreignUnits.getArmies().get(sectorId) != null) {
            for (ArmyDTO army : foreignUnits.getArmies().get(sectorId)) {
                if (army.getCorps() != null) {
                    for (CorpDTO corp : army.getCorps().values()) {
                        if (corp.getBrigades() != null
                                && corp.getBrigades().containsKey(brigadeId)) {
                            return corp.getBrigades().get(brigadeId);
                        }
                    }
                }
            }
        }
        return null;
    }


    public FleetDTO getFleetBySectorAndId(final int sectorId, final int fleetId) {
        if (foreignUnits.getFleets().containsKey(sectorId)) {
            for (FleetDTO fleet : foreignUnits.getFleets().get(sectorId)) {
                if (fleet.getFleetId() == fleetId) {
                    return fleet;
                }
            }
        }
        return null;
    }

    public ShipDTO getShipBySectorAndID(final int sectorId, final int shipId) {
        if (foreignUnits.getFleets().containsKey(sectorId)) {
            for (FleetDTO fleet : foreignUnits.getFleets().get(sectorId)) {
                if (fleet.getShips().containsKey(shipId)) {
                    return fleet.getShips().get(shipId);
                }
            }
        }
        return null;
    }

    public SpyDTO getSpyBySectorAndId(final int sectorId, final int spyId) {
        if (foreignUnits.getSpies().containsKey(sectorId)) {
            for (SpyDTO spy : foreignUnits.getSpies().get(sectorId)) {
                if (spy.getSpyId() == spyId) {
                    return spy;
                }
            }
        }
        return null;
    }

    public CommanderDTO getCommanderBySectorAndId(final int sectorId, final int commanderId) {
        if (foreignUnits.getCommanders().containsKey(sectorId)) {
            for (CommanderDTO commander : foreignUnits.getCommanders().get(sectorId)) {
                if (commander.getId() == commanderId) {
                    return commander;
                }
            }
        }
        return null;
    }

    public BaggageTrainDTO getBaggageTrainBySectorAndId(final int sectorId, final int bTrainId) {
        if (foreignUnits.getBaggageTrains().containsKey(sectorId)) {
            for (BaggageTrainDTO bTrain : foreignUnits.getBaggageTrains().get(sectorId)) {
                if (bTrain.getId() == bTrainId) {
                    return bTrain;
                }
            }
        }
        return null;
    }

}
