package empire.webapp.shared.stores.map;

import empire.data.constants.ArmyConstants;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.webapp.client.events.loading.ArmiesInitEvent;
import empire.webapp.client.events.loading.ArmiesInitdHandler;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.gui.GuiComponentSpyReports;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.map.units.MapArmyGroup;
import empire.webapp.shared.stores.map.units.MapBtrainGroup;
import empire.webapp.shared.stores.map.units.MapFleetGroup;
import empire.webapp.shared.stores.map.units.MapSpyCommGroup;
import empire.webapp.shared.stores.units.ArmyStore;
import empire.webapp.shared.stores.units.BaggageTrainStore;
import empire.webapp.shared.stores.units.CommanderStore;
import empire.webapp.shared.stores.units.NavyStore;
import empire.webapp.shared.stores.units.SpyStore;
import org.vaadin.gwtgraphics.client.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnitGroups implements ArmyConstants {

    private Map<Integer, Group> regionUnitImages = new HashMap<Integer, Group>(4);
    private final Map<Integer, MapArmyGroup> regionArmyImages = new HashMap<Integer, MapArmyGroup>(4);
    private final Map<Integer, MapArmyGroup> regionNewArmyImages = new HashMap<Integer, MapArmyGroup>(4);
    private final Map<Integer, MapFleetGroup> regionFleetImages = new HashMap<Integer, MapFleetGroup>(4);
    private final Map<Integer, MapFleetGroup> regionNewShipImages = new HashMap<Integer, MapFleetGroup>(4);
    private final Map<Integer, MapBtrainGroup> regionBaggageTrains = new HashMap<Integer, MapBtrainGroup>(4);
    private final Map<Integer, MapSpyCommGroup> regionSpyComm = new HashMap<Integer, MapSpyCommGroup>(4);

    /**
     * InspectionTiles for spy on map reports.
     */
    private Group inspectionTiles;

    private GuiComponentSpyReports tilesComponent;

    /**
     * The id of the spy that was last selected to show reports on map.
     */
    private int lastSpyId;

    private final Map<Integer, Group> regionSpyReports = new HashMap<Integer, Group>(4);

    UnitGroups() {
        super();
        LoadEventManager.addArmiesInitHandler(new ArmiesInitdHandler() {
            public void onArmiesInit(final ArmiesInitEvent e) {
                for (int i = 1; i < 5; i++) {
                    final MapArmyGroup maGroup = new MapArmyGroup(ArmyStore.getInstance().getArmiesByRegion(i), i, false, false, false);
                    regionArmyImages.put(i, maGroup);
                    getByRegion(i).add(regionArmyImages.get(i));


                    final List<ArmyDTO> newArmies = new ArrayList<ArmyDTO>();
                    newArmies.add(ArmyStore.getInstance().getRegionNewBrigadesAsArmy(i));
                    final MapArmyGroup newArmyGroup = new MapArmyGroup(newArmies, i, false, false, true);
                    regionNewArmyImages.put(i, newArmyGroup);
                    getByRegion(i).add(regionNewArmyImages.get(i));

                    final MapFleetGroup mfGroup = new MapFleetGroup(NavyStore.getInstance().getFleetsByRegion(i, true), i, false, false, false);
                    regionFleetImages.put(i, mfGroup);
                    getByRegion(i).add(regionFleetImages.get(i));


                    final List<FleetDTO> newShipsFleet = new ArrayList<FleetDTO>();
                    newShipsFleet.add(NavyStore.getInstance().getNewShipsByRegionAsFleet(i));
                    final MapFleetGroup newShipsGroup = new MapFleetGroup(newShipsFleet, i, false, false, true);
                    regionNewShipImages.put(i, newShipsGroup);
                    getByRegion(i).add(regionNewShipImages.get(i));


                    final MapBtrainGroup mbGroup = new MapBtrainGroup(BaggageTrainStore.getInstance().getBaggageTrainsByRegion(i, true), i, false, false);
                    regionBaggageTrains.put(i, mbGroup);
                    getByRegion(i).add(regionBaggageTrains.get(i));


                    final MapSpyCommGroup mpcGroup = new MapSpyCommGroup(CommanderStore.getInstance().getCommandersByRegion(i, true),
                            SpyStore.getInstance().getSpiesByRegion(i), i, false, false);
                    regionSpyComm.put(i, mpcGroup);
                    regionSpyReports.put(i, new Group());
                    getByRegion(i).add(regionSpyComm.get(i));
                }

                LoadEventManager.mapUnitsLoaded();
            }
        });

    }

    public Group getByRegion(final int regionId) {
        if (!regionUnitImages.containsKey(regionId)) {
            regionUnitImages.put(regionId, new Group());
        }
        return regionUnitImages.get(regionId);
    }

    public Group getSpyReportsByRegion(final int regionId) {
        if (regionSpyReports.get(regionId) == null) {
            regionSpyReports.put(regionId, new Group());
        }
        return regionSpyReports.get(regionId);

    }

    public void addSpyReportsOnMap(final Group inspectionTiles, final int regionId) {
        getSpyReportsByRegion(regionId).add(inspectionTiles);
    }

    public void addReportsOnMap(final Group inspectionTiles) {
        addSpyReportsOnMap(inspectionTiles, MapStore.getInstance().getActiveRegion());

    }


    public void removeSpyReportsFromMap(final Group inspectionTiles,
                                        final int regionId) {
        getSpyReportsByRegion(regionId).remove(inspectionTiles);
    }


    public void removeReportsFromMap(final Group inspectionTiles) {
        removeSpyReportsFromMap(inspectionTiles, MapStore.getInstance().getActiveRegion());

    }

    /**
     * Method that returns the vector containing the commander
     * images of a region
     *
     * @param regionId the id of the region
     * @return the Group
     */
    public MapArmyGroup getRegionArmyImagesById(final int regionId) {
        return regionArmyImages.get(regionId);
    }


    public MapArmyGroup getRegionNewArmiesByRegionId(final int regionId) {
        return regionNewArmyImages.get(regionId);
    }

    /**
     * Method that returns the vector containing the commander
     * images of a region
     *
     * @param regionId the id of the region
     * @return the Group
     */
    public MapFleetGroup getFleetsByRegionId(final int regionId) {
        return regionFleetImages.get(regionId);
    }

    public MapFleetGroup getNewShipsByRegionId(final int regionId) {
        return regionNewShipImages.get(regionId);
    }

    /**
     * Method that returns the vector containing the commander
     * images of a region
     *
     * @param regionId the id of the region
     * @return the Group
     */
    public MapSpyCommGroup getSpiesCommandersByRegionId(final int regionId) {

        return regionSpyComm.get(regionId);
    }

    /**
     * Method that returns the vector containing the baggage train
     * images of a region
     *
     * @param regionId the id of the region
     * @return the Group
     */
    public MapBtrainGroup getBaggageTrainsByRegionId(final int regionId) {
        return regionBaggageTrains.get(regionId);
    }

    /**
     * @return the regionUnitImages
     */
    public Group getRegionUnitImages(final int regionId) {
        if (GameStore.getInstance().isShowArmies()) {
            if (regionUnitImages.get(regionId) != regionArmyImages.get(regionId).getParent()) {
                regionUnitImages.get(regionId).add(regionArmyImages.get(regionId));
            }
        } else {
            regionUnitImages.get(regionId).remove(regionArmyImages.get(regionId));
        }
        if (GameStore.getInstance().isShowNavy()) {
            if (regionUnitImages.get(regionId) != regionFleetImages.get(regionId).getParent()) {
                regionUnitImages.get(regionId).add(regionFleetImages.get(regionId));
//                regionUnitImages.get(regionId).add(regionNewShipImages.get(regionId));
            }
        } else {
            regionUnitImages.get(regionId).remove(regionFleetImages.get(regionId));
//			regionUnitImages.get(regionId).remove(regionNewShipImages.get(regionId));
        }
        return regionUnitImages.get(regionId);
    }

    /**
     * @param regionUnitImages the regionUnitImages to set
     */
    public void setRegionUnitImages(final Map<Integer, Group> regionUnitImages) {
        this.regionUnitImages = regionUnitImages;
    }

    /**
     * Toggles spy reports on map.
     *
     * @param inInspectTiles Inspection tiles to show or hide.
     * @param spyId          The spy id.
     */
    public void toggleInspectionTiles(final Group inInspectTiles, final int spyId, final GuiComponentSpyReports comp) {
        if (inspectionTiles == null) {
            inspectionTiles = inInspectTiles;
            tilesComponent = comp;
            lastSpyId = spyId;
            addReportsOnMap(inspectionTiles);
            comp.registerComponent();
        } else {
            if (spyId == lastSpyId) {
                removeReportsFromMap(inspectionTiles);
                comp.unRegisterComponent();
                tilesComponent = null;
                inspectionTiles = null;
                lastSpyId = -1;
            } else {
                removeReportsFromMap(inspectionTiles);
                tilesComponent.unRegisterComponent();
                inspectionTiles = inInspectTiles;
                lastSpyId = spyId;
                addReportsOnMap(inspectionTiles);
                comp.registerComponent();
                tilesComponent = comp;

            }
        }
    }

}
