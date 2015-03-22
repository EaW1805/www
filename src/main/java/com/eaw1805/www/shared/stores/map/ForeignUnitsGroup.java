package com.eaw1805.www.shared.stores.map;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.loading.ForeignUnitsLoadedEvent;
import com.eaw1805.www.client.events.loading.ForeignUnitsLoadedHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.shared.stores.map.units.MapArmyGroup;
import com.eaw1805.www.shared.stores.map.units.MapBtrainGroup;
import com.eaw1805.www.shared.stores.map.units.MapFleetGroup;
import com.eaw1805.www.shared.stores.map.units.MapSpyCommGroup;
import org.vaadin.gwtgraphics.client.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ForeignUnitsGroup {

    private final Map<Integer, Group> regionUnitImages = new HashMap<Integer, Group>(4);

    private final Map<Integer, MapArmyGroup> regionArmyImages = new HashMap<Integer, MapArmyGroup>(4);
    private final Map<Integer, MapFleetGroup> regionFleetImages = new HashMap<Integer, MapFleetGroup>(4);
    private final Map<Integer, MapBtrainGroup> regionBaggageTrains = new HashMap<Integer, MapBtrainGroup>(4);
    private final Map<Integer, MapSpyCommGroup> regionSpyComm = new HashMap<Integer, MapSpyCommGroup>(4);

    private final Map<Integer, MapArmyGroup> regionReportedArmyImages = new HashMap<Integer, MapArmyGroup>(4);
    private final Map<Integer, MapFleetGroup> regionReportedFleetImages = new HashMap<Integer, MapFleetGroup>(4);
    private final Map<Integer, Group> regionReportedUnitImages = new HashMap<Integer, Group>(4);

    public ForeignUnitsGroup() {
        LoadEventManager.addForeignUnitsLoadedHandler(new ForeignUnitsLoadedHandler() {
            public void onForeignUnitsLoaded(final ForeignUnitsLoadedEvent event) {
                final Map<Integer, List<CommanderDTO>> regionToCommanders = new HashMap<Integer, List<CommanderDTO>>();
                final Map<Integer, List<SpyDTO>> regionToSpies = new HashMap<Integer, List<SpyDTO>>();
                final Map<Integer, List<BaggageTrainDTO>> regionToBaggageTrains = new HashMap<Integer, List<BaggageTrainDTO>>();

                for (List<CommanderDTO> commanders : event.getForeignUnits().getCommanders().values()) {
                    for (CommanderDTO commander : commanders) {
                        if (regionToCommanders.containsKey(commander.getRegionId())) {
                            regionToCommanders.get(commander.getRegionId()).add(commander);
                        } else {
                            regionToCommanders.put(commander.getRegionId(), new ArrayList<CommanderDTO>());
                            regionToCommanders.get(commander.getRegionId()).add(commander);
                        }
                    }
                }

                for (List<SpyDTO> spies : event.getForeignUnits().getSpies().values()) {
                    for (SpyDTO spy : spies) {
                        if (regionToSpies.containsKey(spy.getRegionId())) {
                            regionToSpies.get(spy.getRegionId()).add(spy);
                        } else {
                            regionToSpies.put(spy.getRegionId(), new ArrayList<SpyDTO>());
                            regionToSpies.get(spy.getRegionId()).add(spy);
                        }
                    }
                }

                for (List<BaggageTrainDTO> bTrains : event.getForeignUnits().getBaggageTrains().values()) {
                    for (BaggageTrainDTO bTrain : bTrains) {
                        if (regionToBaggageTrains.containsKey(bTrain.getRegionId())) {
                            regionToBaggageTrains.get(bTrain.getRegionId()).add(bTrain);
                        } else {
                            regionToBaggageTrains.put(bTrain.getRegionId(), new ArrayList<BaggageTrainDTO>());
                            regionToBaggageTrains.get(bTrain.getRegionId()).add(bTrain);
                        }
                    }
                }

                final Map<Integer, List<ArmyDTO>> regionToArmies = new HashMap<Integer, List<ArmyDTO>>();
                final Map<Integer, ArmyDTO> regionToZeroArmy = new HashMap<Integer, ArmyDTO>();
                final Map<Integer, CorpDTO> regionToZeroCorp = new HashMap<Integer, CorpDTO>();

                final Map<Integer, List<ArmyDTO>> regionToReportedArmies = new HashMap<Integer, List<ArmyDTO>>();
                final Map<Integer, ArmyDTO> regionToZeroReportedArmy = new HashMap<Integer, ArmyDTO>();
                final Map<Integer, CorpDTO> regionToZeroReportedCorp = new HashMap<Integer, CorpDTO>();

                for (List<ArmyDTO> armies : event.getForeignUnits().getArmies().values()) {
                    for (ArmyDTO army : armies) {
                        if (army.getArmyId() == 0) {
                            for (CorpDTO corps : army.getCorps().values()) {
                                if (corps.getCorpId() == 0) {
                                    for (BrigadeDTO brigade : corps.getBrigades().values()) {
                                        if (brigade.getBrigadeId() >= 0) {
                                            if (!regionToZeroCorp.containsKey(brigade.getRegionId())) {
                                                final CorpDTO zeroCorp = new CorpDTO();
                                                zeroCorp.setArmyId(0);
                                                zeroCorp.setCorpId(0);
                                                zeroCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
                                                regionToZeroCorp.put(brigade.getRegionId(), zeroCorp);
                                                if (!regionToZeroArmy.containsKey(brigade.getRegionId())) {
                                                    final ArmyDTO zeroArmy = new ArmyDTO();
                                                    zeroArmy.setArmyId(0);
                                                    zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                                                    regionToZeroArmy.put(brigade.getRegionId(), zeroArmy);

                                                    if (!regionToArmies.containsKey(brigade.getRegionId())) {
                                                        regionToArmies.put(brigade.getRegionId(), new ArrayList<ArmyDTO>());
                                                    }

                                                    regionToArmies.get(brigade.getRegionId()).add(zeroArmy);
                                                }
                                                regionToZeroArmy.get(brigade.getRegionId()).getCorps().put(0, zeroCorp);
                                            }

                                            regionToZeroCorp.get(brigade.getRegionId()).getBrigades().put(brigade.getBrigadeId(), brigade);

                                        } else {
                                            if (!regionToZeroReportedCorp.containsKey(brigade.getRegionId())) {
                                                final CorpDTO zeroCorp = new CorpDTO();
                                                zeroCorp.setArmyId(0);
                                                zeroCorp.setCorpId(0);
                                                zeroCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
                                                regionToZeroReportedCorp.put(brigade.getRegionId(), zeroCorp);
                                                if (!regionToZeroReportedArmy.containsKey(brigade.getRegionId())) {
                                                    final ArmyDTO zeroArmy = new ArmyDTO();
                                                    zeroArmy.setArmyId(0);
                                                    zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                                                    regionToZeroReportedArmy.put(brigade.getRegionId(), zeroArmy);
                                                    if (!regionToReportedArmies.containsKey(brigade.getRegionId())) {
                                                        regionToReportedArmies.put(brigade.getRegionId(), new ArrayList<ArmyDTO>());
                                                    }
                                                    regionToReportedArmies.get(brigade.getRegionId()).add(zeroArmy);
                                                }
                                                regionToZeroReportedArmy.get(brigade.getRegionId()).getCorps().put(0, zeroCorp);
                                            }
                                            regionToZeroReportedCorp.get(brigade.getRegionId()).getBrigades().put(brigade.getBrigadeId(), brigade);
                                        }
                                    }
                                } else {
                                    if (corps.getCorpId() > 0) {
                                        if (!regionToZeroArmy.containsKey(corps.getRegionId())) {
                                            final ArmyDTO zeroArmy = new ArmyDTO();
                                            zeroArmy.setArmyId(0);
                                            zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                                            regionToZeroArmy.put(corps.getRegionId(), zeroArmy);
                                            if (!regionToArmies.containsKey(corps.getRegionId())) {
                                                regionToArmies.put(corps.getRegionId(), new ArrayList<ArmyDTO>());
                                            }
                                            regionToArmies.get(corps.getRegionId()).add(zeroArmy);
                                        }
                                        regionToZeroArmy.get(corps.getRegionId()).getCorps().put(corps.getCorpId(), corps);
                                    } else {
                                        if (!regionToZeroReportedArmy.containsKey(corps.getRegionId())) {
                                            final ArmyDTO zeroArmy = new ArmyDTO();
                                            zeroArmy.setArmyId(0);
                                            zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                                            regionToZeroReportedArmy.put(corps.getRegionId(), zeroArmy);
                                            if (!regionToReportedArmies.containsKey(corps.getRegionId())) {
                                                regionToReportedArmies.put(corps.getRegionId(), new ArrayList<ArmyDTO>());
                                            }
                                            regionToReportedArmies.get(corps.getRegionId()).add(zeroArmy);
                                        }
                                        regionToZeroReportedArmy.get(corps.getRegionId()).getCorps().put(corps.getCorpId(), corps);
                                    }

                                }
                            }
                        } else {
                            if (army.getArmyId() > 0) {
                                if (!regionToArmies.containsKey(army.getRegionId())) {
                                    regionToArmies.put(army.getRegionId(), new ArrayList<ArmyDTO>());
                                }
                                regionToArmies.get(army.getRegionId()).add(army);
                            } else if (army.getArmyId() < 0) {
                                if (!regionToReportedArmies.containsKey(army.getRegionId())) {
                                    regionToReportedArmies.put(army.getRegionId(), new ArrayList<ArmyDTO>());
                                }
                                regionToReportedArmies.get(army.getRegionId()).add(army);
                            }
                        }
                    }
                }

                final Map<Integer, List<FleetDTO>> regionToFleets = new HashMap<Integer, List<FleetDTO>>();
                final Map<Integer, FleetDTO> regionToZeroFleet = new HashMap<Integer, FleetDTO>();

                final Map<Integer, List<FleetDTO>> regionToReportedFleets = new HashMap<Integer, List<FleetDTO>>();
                final Map<Integer, FleetDTO> regionToZeroReportedFleet = new HashMap<Integer, FleetDTO>();
                for (List<FleetDTO> fleets : event.getForeignUnits().getFleets().values()) {
                    for (FleetDTO fleet : fleets) {
                        if (fleet.getFleetId() > 0) {
                            if (!regionToFleets.containsKey(fleet.getRegionId())) {
                                regionToFleets.put(fleet.getRegionId(), new ArrayList<FleetDTO>());
                            }
                            regionToFleets.get(fleet.getRegionId()).add(fleet);

                        } else if (fleet.getFleetId() < 0) {
                            if (!regionToReportedFleets.containsKey(fleet.getRegionId())) {
                                regionToReportedFleets.put(fleet.getRegionId(), new ArrayList<FleetDTO>());
                            }
                            regionToReportedFleets.get(fleet.getRegionId()).add(fleet);

                        } else {
                            for (ShipDTO ship : fleet.getShips().values()) {
                                if (ship.getId() >= 0) {
                                    if (!regionToZeroFleet.containsKey(ship.getRegionId())) {
                                        final FleetDTO zeroFleet = new FleetDTO();
                                        zeroFleet.setFleetId(0);
                                        zeroFleet.setShips(new HashMap<Integer, ShipDTO>());
                                        regionToZeroFleet.put(ship.getRegionId(), zeroFleet);
                                        if (!regionToFleets.containsKey(ship.getRegionId())) {
                                            regionToFleets.put(ship.getRegionId(), new ArrayList<FleetDTO>());
                                        }
                                        regionToFleets.get(ship.getRegionId()).add(zeroFleet);
                                    }
                                    regionToZeroFleet.get(ship.getRegionId()).getShips().put(ship.getId(), ship);

                                } else {
                                    if (!regionToZeroReportedFleet.containsKey(ship.getRegionId())) {
                                        final FleetDTO zeroFleet = new FleetDTO();
                                        zeroFleet.setFleetId(0);
                                        zeroFleet.setShips(new HashMap<Integer, ShipDTO>());
                                        regionToZeroReportedFleet.put(ship.getRegionId(), zeroFleet);
                                        if (!regionToReportedFleets.containsKey(ship.getRegionId())) {
                                            regionToReportedFleets.put(ship.getRegionId(), new ArrayList<FleetDTO>());
                                        }
                                        regionToReportedFleets.get(ship.getRegionId()).add(zeroFleet);
                                    }
                                    regionToZeroReportedFleet.get(ship.getRegionId()).getShips().put(ship.getId(), ship);
                                }
                            }
                        }
                    }
                }

                for (int regionId = 1; regionId <= 5; regionId++) {
                    final List<CommanderDTO> regionCommanders = new ArrayList<CommanderDTO>();
                    final List<SpyDTO> regionSpies = new ArrayList<SpyDTO>();
                    if (regionToCommanders.containsKey(regionId)) {
                        regionCommanders.addAll(regionToCommanders.get(regionId));
                    }
                    if (regionToSpies.containsKey(regionId)) {
                        regionSpies.addAll(regionToSpies.get(regionId));
                    }
                    if (!regionCommanders.isEmpty() || !regionSpies.isEmpty()) {
                        final MapSpyCommGroup mpcGroup = new MapSpyCommGroup(regionCommanders, regionSpies, regionId, false, true);
                        regionSpyComm.put(regionId, mpcGroup);
                        getByRegion(regionId).add(regionSpyComm.get(regionId));
                    }

                    if (regionToBaggageTrains.containsKey(regionId)
                            && regionToBaggageTrains.get(regionId).size() > 0) {
                        final MapBtrainGroup mbGroup = new MapBtrainGroup(regionToBaggageTrains.get(regionId), regionId, false, true);
                        regionBaggageTrains.put(regionId, mbGroup);
                        getByRegion(regionId).add(regionBaggageTrains.get(regionId));
                    }

                    if (regionToArmies.containsKey(regionId)
                            && regionToArmies.get(regionId).size() > 0) {
                        final MapArmyGroup maGroup = new MapArmyGroup(regionToArmies.get(regionId), regionId, false, true, false);
                        regionArmyImages.put(regionId, maGroup);
                        getByRegion(regionId).add(regionArmyImages.get(regionId));
                    }

                    //generate fleets group
                    try {
                        if (regionToFleets.containsKey(regionId) &&
                                regionToFleets.get(regionId).size() > 0) {
                            final MapFleetGroup mfGroup = new MapFleetGroup(regionToFleets.get(regionId), regionId, false, true, false);
                            regionFleetImages.put(regionId, mfGroup);
                            getByRegion(regionId).add(regionFleetImages.get(regionId));
                        }
                    } catch (Exception e) {
//                        Window.alert("fleets" + e.toString());
                    }

                    //at last create new group for virtual reported armies
                    if (regionToReportedArmies.containsKey(regionId)
                            && regionToReportedArmies.get(regionId).size() > 0) {
                        final MapArmyGroup maGroup = new MapArmyGroup(regionToReportedArmies.get(regionId), regionId, false, true, false);
                        regionReportedArmyImages.put(regionId, maGroup);
                        getReportedByRegion(regionId).add(regionReportedArmyImages.get(regionId));
                    }

                    if (regionToReportedFleets.containsKey(regionId)
                            && regionToReportedFleets.get(regionId).size() > 0) {
                        final MapFleetGroup mfGroup = new MapFleetGroup(regionToReportedFleets.get(regionId), regionId, false, true, false);
                        regionReportedFleetImages.put(regionId, mfGroup);
                        getReportedByRegion(regionId).add(regionReportedFleetImages.get(regionId));
                    }
                }
            }
        });
    }

    private Group getByRegion(final int regionId) {
        if (!regionUnitImages.containsKey(regionId)) {
            regionUnitImages.put(regionId, new Group());
        }
        return regionUnitImages.get(regionId);
    }

    private Group getReportedByRegion(final int regionId) {
        if (!regionReportedUnitImages.containsKey(regionId)) {
            regionReportedUnitImages.put(regionId, new Group());
        }
        return regionReportedUnitImages.get(regionId);
    }

    public Group getRegionForeignUnitImages(final int regionId) {
        return getByRegion(regionId);
    }

    public MapArmyGroup getRegionArmyImages(final int regionId) {
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

    public Group getRegionReportedForeignUnitImages(final int regionId) {
        return getReportedByRegion(regionId);
    }

    public MapArmyGroup getRegionReportedArmyImages(final int regionId) {
        return regionReportedArmyImages.get(regionId);
    }

    public MapFleetGroup getRegionReportedFleetImages(final int regionId) {
        return regionReportedFleetImages.get(regionId);
    }
}
