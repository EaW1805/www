package empire.webapp.shared.stores.map;

import empire.data.constants.ArmyConstants;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.SpyDTO;
import empire.data.dto.web.economy.BaggageTrainDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.movement.PathDTO;
import empire.webapp.client.events.loading.AlliedUnitsLoadedEvent;
import empire.webapp.client.events.loading.AlliedUnitsLoadedHandler;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.movement.TilesGroup;
import empire.webapp.shared.AlliedMovement;
import empire.webapp.shared.AlliedUnits;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.map.units.MapArmyGroup;
import empire.webapp.shared.stores.map.units.MapBtrainGroup;
import empire.webapp.shared.stores.map.units.MapFleetGroup;
import empire.webapp.shared.stores.map.units.MapSpyCommGroup;
import empire.webapp.shared.stores.map.units.MapUnitMoveGroup;
import empire.webapp.shared.stores.support.MapConstants;
import empire.webapp.shared.stores.units.AlliedUnitsStore;
import org.vaadin.gwtgraphics.client.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlliedUnitsGroup implements MapConstants, ArmyConstants {

    private final Map<Integer, Group> regionUnitImages = new HashMap<Integer, Group>(4);
    private final Map<Integer, MapArmyGroup> regionArmyImages = new HashMap<Integer, MapArmyGroup>(4);
    private final Map<Integer, MapFleetGroup> regionFleetImages = new HashMap<Integer, MapFleetGroup>(4);
    private final Map<Integer, MapBtrainGroup> regionBaggageTrains = new HashMap<Integer, MapBtrainGroup>(4);
    private final Map<Integer, MapSpyCommGroup> regionSpyComm = new HashMap<Integer, MapSpyCommGroup>(4);
    private final Map<Integer, MapUnitMoveGroup> regionAllyMoves = new HashMap<Integer, MapUnitMoveGroup>();


    /**
     * A map that contains all the movement lines for the allied forces
     * <regionId : <nationId : <unitType : <unitId : Group>>>>
     */
    private final Map<Integer, Map<Integer, Map<Integer, Map<Integer, Group>>>> regionUnitMovement = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, Group>>>>();


    private final Map<Integer, Group> regionSpyReports = new HashMap<Integer, Group>(4);

    public AlliedUnitsGroup() {

        LoadEventManager.addAlliedUnitsLoadedHandler(new AlliedUnitsLoadedHandler() {
            public void onAlliedUnitsLoaded(final AlliedUnitsLoadedEvent event) {
                for (int i = 1; i <= 5; i++) {

                    final Map<Integer, AlliedUnits> alliedUnitsByRegion = event.getAlliedUnits().get(i);
                    if (alliedUnitsByRegion != null) {
                        //find all baggage trains.
                        final List<BaggageTrainDTO> allRegionBTrains = new ArrayList<BaggageTrainDTO>();
                        //find all commanders and spies
                        final List<CommanderDTO> allRegionCommanders = new ArrayList<CommanderDTO>();
                        final List<SpyDTO> allRegionSpies = new ArrayList<SpyDTO>();
                        //find all fleets
                        final List<FleetDTO> allRegionFleets = new ArrayList<FleetDTO>();
                        //find all armies
                        final List<ArmyDTO> allRegionArmies = new ArrayList<ArmyDTO>();
                        for (Integer nationId : alliedUnitsByRegion.keySet()) {
                            if (alliedUnitsByRegion.get(nationId).getArmies() != null
                                    && AlliedUnitsStore.getInstance().getArmiesByRegion(i, nationId) != null) {
                                allRegionArmies.addAll(AlliedUnitsStore.getInstance().getArmiesByRegion(i, nationId));
                            }

                            if (alliedUnitsByRegion.get(nationId).getFleets() != null
                                    && AlliedUnitsStore.getInstance().getFleetsByRegion(i, nationId) != null) {
                                allRegionFleets.addAll(AlliedUnitsStore.getInstance().getFleetsByRegion(i, nationId));
                            }

                            if (alliedUnitsByRegion.get(nationId).getBaggageTrains() != null) {
                                for (List<BaggageTrainDTO> btrains : alliedUnitsByRegion.get(nationId).getBaggageTrains().values()) {
                                    if (btrains != null) {
                                        allRegionBTrains.addAll(btrains);
                                    }
                                }
                            }

                            final List<Integer> sectorIds = new ArrayList<Integer>();
                            if (alliedUnitsByRegion.get(nationId).getSpies() != null) {
                                for (Integer sectorId : alliedUnitsByRegion.get(nationId).getSpies().keySet()) {
                                    sectorIds.add(sectorId);
                                }
                            }

                            if (alliedUnitsByRegion.get(nationId).getCommanders() != null) {
                                for (Integer sectorId : alliedUnitsByRegion.get(nationId).getCommanders().keySet()) {
                                    if (!alliedUnitsByRegion.get(nationId).getSpies().containsKey(sectorId)) {
                                        sectorIds.add(sectorId);
                                    }
                                }
                            }

                            for (int sectorId : sectorIds) {
                                if (alliedUnitsByRegion.get(nationId).getCommanders() != null &&
                                        alliedUnitsByRegion.get(nationId).getCommanders().get(sectorId) != null) {
                                    allRegionCommanders.addAll(alliedUnitsByRegion.get(nationId).getCommanders().get(sectorId));
                                }

                                if (alliedUnitsByRegion.get(nationId).getSpies() != null &&
                                        alliedUnitsByRegion.get(nationId).getSpies().get(sectorId) != null) {
                                    allRegionSpies.addAll(alliedUnitsByRegion.get(nationId).getSpies().get(sectorId));
                                }
                            }

                            if (alliedUnitsByRegion.get(nationId).getMovements() != null) {
                                for (List<AlliedMovement> alliedMovements : alliedUnitsByRegion.get(nationId).getMovements().values()) {
                                    for (AlliedMovement alliedMovement : alliedMovements) {
                                        for (PathDTO path : alliedMovement.getSectorPaths()) {
                                            final TilesGroup tlGroup = new TilesGroup(alliedMovement.getUnitType(), path, alliedMovement.isForcedMarch(), alliedMovement.isPatrol());
                                            addToMovementGroup(path.getRegionId(), nationId, alliedMovement.getUnitType(), alliedMovement.getId(), tlGroup.getPathTiles());
                                        }
                                    }
                                }
                            }
                        }

                        //generate armies group
                        if (!allRegionArmies.isEmpty()) {
                            final MapArmyGroup maGroup = new MapArmyGroup(allRegionArmies, i, true, false, false);
                            regionArmyImages.put(i, maGroup);
                            getByRegion(i).add(regionArmyImages.get(i));
                        }

                        //generate baggage train group
                        if (!allRegionBTrains.isEmpty()) {
                            final MapBtrainGroup mbGroup = new MapBtrainGroup(allRegionBTrains, i, true, false);
                            regionBaggageTrains.put(i, mbGroup);
                            getByRegion(i).add(regionBaggageTrains.get(i));
                        }

                        //generate commanders-spies group
                        if (!allRegionCommanders.isEmpty() || !allRegionSpies.isEmpty()) {
                            final MapSpyCommGroup mpcGroup = new MapSpyCommGroup(allRegionCommanders, allRegionSpies, i, true, false);
                            regionSpyComm.put(i, mpcGroup);
                            regionSpyReports.put(i, new Group());
                            getByRegion(i).add(regionSpyReports.get(i));
                            getByRegion(i).add(regionSpyComm.get(i));
                        }

                        //generate fleets group
                        if (!allRegionFleets.isEmpty()) {
                            final MapFleetGroup mfGroup = new MapFleetGroup(allRegionFleets, i, true, false, false);
                            regionFleetImages.put(i, mfGroup);
                            getByRegion(i).add(regionFleetImages.get(i));
                        }
                    }
                }

            }
        });
    }

    @SuppressWarnings("restriction")
    public List<Group> getMovementLinesByRegion(final int regionId) {
        final List<Group> out = new ArrayList<Group>();
        if (regionUnitMovement.containsKey(regionId)) {
            final Map<Integer, Map<Integer, Map<Integer, Group>>> map = regionUnitMovement.get(regionId);
            if (map != null) {
                for (Map.Entry<Integer, Map<Integer, Map<Integer, Group>>> entry1 : map.entrySet()) {
                    for (Map.Entry<Integer, Map<Integer, Group>> entry2 : entry1.getValue().entrySet()) {
                        out.addAll(entry2.getValue().values());
                    }
                }
            }
        }
        return out;
    }

    public void highLightPath(final int regionId, final int nationId, final int typeId, final int unitId) {
        //first hide everything
        for (Map<Integer, Map<Integer, Map<Integer, Group>>> nationsMovements : regionUnitMovement.values()) {
            for (Map<Integer, Map<Integer, Group>> typeMovements : nationsMovements.values()) {
                for (Map<Integer, Group> unitMovements : typeMovements.values()) {
                    for (Group line : unitMovements.values()) {
                        line.setVisible(false);
                    }
                }
            }
        }

        //then show the wanted one if exist
        if (regionUnitMovement.containsKey(regionId)
                && regionUnitMovement.get(regionId).containsKey(nationId)
                && regionUnitMovement.get(regionId).get(nationId).containsKey(typeId)
                && regionUnitMovement.get(regionId).get(nationId).get(typeId).containsKey(unitId)) {
            regionUnitMovement.get(regionId).get(nationId).get(typeId).get(unitId).setVisible(true);
        }
    }

    public void highLightAllPaths() {
        //display everything
        for (Map<Integer, Map<Integer, Map<Integer, Group>>> typeToLines : regionUnitMovement.values()) {
            for (Map<Integer, Map<Integer, Group>> nationLines : typeToLines.values()) {
                for (Map<Integer, Group> typeLines : nationLines.values()) {
                    for (Group line : typeLines.values()) {
                        line.setVisible(true);
                    }
                }
            }
        }
    }


    //    private Map<Integer, Map<Integer, Group>> regionUnitMovement = new HashMap<Integer, Map<Integer, Group>>();
    private void addToMovementGroup(final int regionId, final int nationId, final int typeId, final int unitId, final Group mvGrp) {
        Map<Integer, Map<Integer, Map<Integer, Group>>> regionMovements = regionUnitMovement.get(regionId);
        if (regionMovements == null) {
            regionMovements = new HashMap<Integer, Map<Integer, Map<Integer, Group>>>();
            regionUnitMovement.put(regionId, regionMovements);
        }

        Map<Integer, Map<Integer, Group>> nationMovements = regionMovements.get(nationId);
        if (nationMovements == null) {
            nationMovements = new HashMap<Integer, Map<Integer, Group>>();
            regionMovements.put(nationId, nationMovements);
        }

        Map<Integer, Group> unitTypeMovements = nationMovements.get(typeId);
        if (unitTypeMovements == null) {
            unitTypeMovements = new HashMap<Integer, Group>();
            nationMovements.put(typeId, unitTypeMovements);
        }

        if (unitTypeMovements.containsKey(unitId)) {
            try {
                MapStore.getInstance().getMapsView().getAllMovements().remove(unitTypeMovements.get(unitId));
            } catch (Exception ignore) {
                // eat it
            }

            unitTypeMovements.get(unitId).add(mvGrp);

        } else {
            unitTypeMovements.put(unitId, mvGrp);
        }

        if (GameStore.getInstance().isShowMovement() && MapStore.getInstance().getActiveRegion() == regionId) {
            try {
                MapStore.getInstance().getMapsView().getAllMovements().add(unitTypeMovements.get(unitId));
            } catch (Exception ignore) {
                // eat it
            }
        }
    }

    private Group getByRegion(final int regionId) {
        if (!regionUnitImages.containsKey(regionId)) {
            regionUnitImages.put(regionId, new Group());
            regionAllyMoves.put(regionId, new MapUnitMoveGroup());
            regionUnitImages.get(regionId).add(regionAllyMoves.get(regionId));
        }

        return regionUnitImages.get(regionId);
    }

    public Group getRegionAlliedUnitImages(final int regionId) {
        return getByRegion(regionId);
    }

    public MapArmyGroup getRegionAlliedArmyImages(final int regionId) {
        return regionArmyImages.get(regionId);
    }

    public MapBtrainGroup getBaggageTrainsByRegionId(final int regionId) {
        return regionBaggageTrains.get(regionId);
    }

    public MapSpyCommGroup getSpiesCommandersByRegionId(final int regionId) {
        return regionSpyComm.get(regionId);
    }

    public MapFleetGroup getFleetsByRegionId(final int regionId) {
        return regionFleetImages.get(regionId);
    }
}
