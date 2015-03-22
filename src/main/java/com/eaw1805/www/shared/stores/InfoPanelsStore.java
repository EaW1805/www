package com.eaw1805.www.shared.stores;

import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.loading.AppInitEvent;
import com.eaw1805.www.client.events.loading.AppInitHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitCreatedEvent;
import com.eaw1805.www.client.events.units.UnitCreatedHandler;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.infopanels.BarracksInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignCommanderInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BaggageTrainInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CommanderInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.FleetInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ShipInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.SpyInfoPanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class where we save the info panels
 * so that we will not need to make them
 * more than once
 *
 * @author tsakygr
 */
public final class InfoPanelsStore
        implements ArmyConstants, RegionConstants {

    private final Map<Integer, List<ArmyInfoPanel>> armyInfoPanels = new HashMap<Integer, List<ArmyInfoPanel>>();
    private final Map<Integer, List<CorpsInfoPanel>> corpInfoPanels = new HashMap<Integer, List<CorpsInfoPanel>>();
    private final Map<Integer, List<BrigadeInfoPanel>> brigInfoPanels = new HashMap<Integer, List<BrigadeInfoPanel>>();
    private final Map<Integer, List<SpyInfoPanel>> spyInfoPanels = new HashMap<Integer, List<SpyInfoPanel>>();
    private final Map<Integer, List<BaggageTrainInfoPanel>> btrainInfoPanels = new HashMap<Integer, List<BaggageTrainInfoPanel>>();
    private final Map<Integer, List<CommanderInfoPanel>> commInfoPanels = new HashMap<Integer, List<CommanderInfoPanel>>();
    private final Map<Integer, List<ForeignCommanderInfoPanel>> capturedCommInfoPanels = new HashMap<Integer, List<ForeignCommanderInfoPanel>>();
    private final Map<Integer, List<FleetInfoPanel>> fleetInfoPanels = new HashMap<Integer, List<FleetInfoPanel>>();
    private final Map<Integer, List<ShipInfoPanel>> shipInfoPanels = new HashMap<Integer, List<ShipInfoPanel>>();
    private final Map<Integer, List<BarracksInfoPanel>> barrackInfoPanels = new HashMap<Integer, List<BarracksInfoPanel>>();

    private PopupPanelEAW unitPopup;
    private boolean infoPanelLocked = false;

    /**
     * The one and only instance of this manager
     */
    private static InfoPanelsStore ourInstance;

    /**
     * Method returning the info panels manager
     *
     * @return the one and only instance of this class
     */
    public static InfoPanelsStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new InfoPanelsStore();
        }
        return ourInstance;
    }

    /**
     * Private constructor invoked only once.
     */
    private InfoPanelsStore() {
        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
            armyInfoPanels.put(thisRegion, new ArrayList<ArmyInfoPanel>());
            corpInfoPanels.put(thisRegion, new ArrayList<CorpsInfoPanel>());
            brigInfoPanels.put(thisRegion, new ArrayList<BrigadeInfoPanel>());
            spyInfoPanels.put(thisRegion, new ArrayList<SpyInfoPanel>());
            btrainInfoPanels.put(thisRegion, new ArrayList<BaggageTrainInfoPanel>());
            commInfoPanels.put(thisRegion, new ArrayList<CommanderInfoPanel>());
            capturedCommInfoPanels.put(thisRegion, new ArrayList<ForeignCommanderInfoPanel>());
            fleetInfoPanels.put(thisRegion, new ArrayList<FleetInfoPanel>());
            shipInfoPanels.put(thisRegion, new ArrayList<ShipInfoPanel>());
            barrackInfoPanels.put(thisRegion, new ArrayList<BarracksInfoPanel>());
        }

        UnitEventManager.addUnitCreatedHandler(new UnitCreatedHandler() {
            public void onUnitCreated(final UnitCreatedEvent event) {
                switch (event.getInfoType()) {
                    case CORPS:
                        final CorpDTO corp = ArmyStore.getInstance().getCorpByID(event.getInfoId());
                        corpInfoPanels.get(corp.getRegionId()).add(new CorpsInfoPanel(corp, true));
                        break;

                    case ARMY:
                        final ArmyDTO army = ArmyStore.getInstance().getcArmies().get(event.getInfoId());
                        armyInfoPanels.get(army.getRegionId()).add(new ArmyInfoPanel(army));
                        break;

                    case FLEET:
                        final FleetDTO fleet = NavyStore.getInstance().getIdFleetMap().get(event.getInfoId());
                        fleetInfoPanels.get(fleet.getRegionId()).add(new FleetInfoPanel(fleet, true));
                        break;

                    default:
                        // do nothing
                }
            }
        });

        UnitEventManager.addUnitDestroyedHandler(new UnitDestroyedHandler() {
            public void onUnitDestroyed(final UnitDestroyedEvent event) {
                switch (event.getInfoType()) {
                    case CORPS:
                        for (int i = REGION_FIRST; i <= REGION_LAST; i++) {
                            for (CorpsInfoPanel cPanel : corpInfoPanels.get(i)) {
                                if (cPanel.getCorp() == null || cPanel.getCorp().getCorpId() == event.getInfoId()) {
                                    corpInfoPanels.get(i).remove(cPanel);
                                    break;
                                }
                            }
                        }
                        break;

                    case ARMY:
                        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                            for (ArmyInfoPanel cPanel : armyInfoPanels.get(thisRegion)) {
                                if (cPanel.getArmy() == null || cPanel.getArmy().getArmyId() == event.getInfoId()) {
                                    armyInfoPanels.get(thisRegion).remove(cPanel);
                                    break;
                                }
                            }
                        }
                        break;

                    case FLEET:
                        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                            for (final FleetInfoPanel fPanel : fleetInfoPanels.get(thisRegion)) {
                                if (fPanel.getFleet() == null || fPanel.getFleet().getFleetId() == event.getInfoId()) {
                                    armyInfoPanels.get(thisRegion).remove(fPanel);
                                    break;
                                }
                            }
                        }
                        break;

                    default:
                        // do nothing
                }
            }
        });

        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {

            public void onUnitChanged(final UnitChangedEvent event) {
                switch (event.getInfoType()) {
                    case CORPS:
                        boolean found = false;
                        final CorpDTO corp = ArmyStore.getInstance().getCorpByID(event.getInfoId());
                        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                            for (CorpsInfoPanel cpPanel : corpInfoPanels.get(thisRegion)) {
                                if (cpPanel.getCorp() != null && cpPanel.getCorp().getCorpId() == event.getInfoId()) {
                                    if (corp.getArmyId() != 0) {
                                        corpInfoPanels.get(thisRegion).remove(cpPanel);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                break;
                            }
                        }

                        if (!found && corp.getCorpId() != 0 && corp.getArmyId() == 0) {
                            corpInfoPanels.get(corp.getRegionId()).add(new CorpsInfoPanel(corp, true));
                        }
                        break;

                    case BRIGADE:
                        found = false;
                        final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(event.getInfoId());
                        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                            for (BrigadeInfoPanel bPanel : brigInfoPanels.get(thisRegion)) {
                                if (bPanel.getBrigade() != null && bPanel.getBrigade().getBrigadeId() == event.getInfoId()) {
                                    if (brig.getCorpId() != 0) {
                                        brigInfoPanels.get(thisRegion).remove(bPanel);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                break;
                            }
                        }
                        if (!found && brig.getCorpId() == 0) {
                            brigInfoPanels.get(brig.getRegionId()).add(new BrigadeInfoPanel(brig, true));
                        }
                        break;

                    case SHIP:
                        found = false;
                        final ShipDTO ship = NavyStore.getInstance().getShipById(event.getInfoId());
                        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                            for (ShipInfoPanel sPanel : shipInfoPanels.get(thisRegion)) {
                                if (sPanel.getShip() != null && sPanel.getShip().getId() == event.getInfoId()) {
                                    if (ship.getFleet() != 0 || ship.isScuttle()) {
                                        shipInfoPanels.get(thisRegion).remove(sPanel);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                break;
                            }
                        }

                        if (!found && ship.getFleet() == 0) {
                            shipInfoPanels.get(ship.getRegionId()).add(new ShipInfoPanel(ship, true));
                        }

                        break;

                    default:
                        // do nothing
                }

            }
        });

        // When the application manager informs us that all units have
        // been loaded from the database initialize all info panels
        LoadEventManager.addAppInitHandler(new AppInitHandler() {
            public void onApplicationInit(final AppInitEvent e) {
                initAllUnitInfoPanels();
            }
        });
    }

    private void initAllUnitInfoPanels() {
        initArmyInfoPanels(ArmyStore.getInstance().getcArmiesList());
        initNavyInfoPanels(NavyStore.getInstance().getDbFleetlist());
        initSpyInfoPanels(SpyStore.getInstance().getSpyList());
        initCommanderInfoPanels(CommanderStore.getInstance().getCommandersList());
        initCapturedCommanderInfoPanels(CommanderStore.getInstance().getCapturedCommanders());
        initBtrainInfoPanels(BaggageTrainStore.getInstance().getBaggageTList());
    }

    public boolean initArmyInfoPanels(final Collection<ArmyDTO> armies) {
        for (final ArmyDTO army : armies) {
            if (army.getArmyId() == 0) {
                for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                    ArmyDTO zeroArmy = new ArmyDTO();
                    zeroArmy.setRegionId(thisRegion);
                    zeroArmy.setArmyId(0);
                    zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());
                    for (final ArmyDTO tmpArmy : ArmyStore.getInstance().getArmiesByRegion(thisRegion)) {
                        if (tmpArmy.getArmyId() == 0) {
                            zeroArmy = tmpArmy;
                            break;
                        }
                    }

                    for (CorpDTO corp : zeroArmy.getCorps().values()) {
                        boolean canMoveCorp = true;
                        if (corp.getCorpId() == 0) {
                            canMoveCorp = false;
                            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                                brigInfoPanels.get(brigade.getRegionId()).add(new BrigadeInfoPanel(brigade, true));
                            }
                        }
                        if (corp.getArmyId() == 0
                                && corp.getCorpId() > 0) {
                            corpInfoPanels.get(corp.getRegionId()).add(new CorpsInfoPanel(corp, canMoveCorp));
                        }
                    }
                    armyInfoPanels.get(thisRegion).add(new ArmyInfoPanel(zeroArmy));
                }

            } else {
                armyInfoPanels.get(army.getRegionId()).add(new ArmyInfoPanel(army));
            }
        }

        GameStore.getInstance().getLayoutView().getUnitsMenu().populatePanel(ARMY);
        return true;
    }

    public boolean initNavyInfoPanels(final List<FleetDTO> fleets) {
        try {
            if (fleets != null) {


                for (final FleetDTO fleet : fleets) {
                    final boolean canMove = true;

                    if (fleet.getFleetId() == 0) {
                        for (int thisRegion = REGION_FIRST; thisRegion <= REGION_LAST; thisRegion++) {
                            FleetDTO zeroFleet = new FleetDTO();
                            zeroFleet.setRegionId(thisRegion);
                            zeroFleet.setFleetId(0);
                            zeroFleet.setShips(new HashMap<Integer, ShipDTO>());
                            zeroFleet.setGoodsDTO(new HashMap<Integer, StoredGoodDTO>());
                            for (final FleetDTO tmpFleet : NavyStore.getInstance().getFleetsByRegion(thisRegion, false)) {
                                // todo: check this break
                                if (tmpFleet.getFleetId() == 0) {
                                    zeroFleet = tmpFleet;
                                }
                                break;
                            }

                            for (final ShipDTO ship : zeroFleet.getShips().values()) {
                                if (!ship.isScuttle()) {
                                    shipInfoPanels.get(ship.getRegionId()).add(new ShipInfoPanel(ship, true));
                                }
                            }

                            fleetInfoPanels.get(thisRegion).add(new FleetInfoPanel(zeroFleet, false));
                        }

                    } else {
                        fleetInfoPanels.get(fleet.getRegionId()).add(new FleetInfoPanel(fleet, canMove));
                    }
                }
            }
            for (Map.Entry<Integer, List<FleetInfoPanel>> entry : fleetInfoPanels.entrySet()) {
                Collections.reverse(entry.getValue());
            }
            return true;
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error initializing navy info panels\n", false);
            return false;
        }
    }

    public boolean initSpyInfoPanels(final List<SpyDTO> spies) {
        try {
            for (SpyDTO spy : spies) {
                spyInfoPanels.get(spy.getRegionId()).add(new SpyInfoPanel(spy));
            }
            return true;
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error initializing spy info panels\n", false);
            return false;
        }
    }

    public boolean initCommanderInfoPanels(final List<CommanderDTO> commanders) {
        try {
            for (CommanderDTO commander : commanders) {
                if (commander.getStartInPool()) {
                    for (int i = REGION_FIRST; i <= REGION_LAST; i++) {
                        commInfoPanels.get(i).add(new CommanderInfoPanel(commander));
                    }
                } else {
                    commInfoPanels.get(commander.getRegionId()).add(new CommanderInfoPanel(commander));
                }
            }
            return true;
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error initializing commander info panels", false);
            return false;
        }
    }

    public boolean initCapturedCommanderInfoPanels(final List<CommanderDTO> commanders) {
        try {
            for (CommanderDTO commander : commanders) {
                capturedCommInfoPanels.get(commander.getRegionId()).add(new ForeignCommanderInfoPanel(commander));
            }
            return true;
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error initializing commander info panels", false);
            return false;
        }
    }

    public boolean initBtrainInfoPanels(final List<BaggageTrainDTO> btrains) {
        try {
            if (btrains != null) {
                for (BaggageTrainDTO btrain : btrains) {
                    if (!btrain.isScuttle()) {
                        btrainInfoPanels.get(btrain.getRegionId()).add(new BaggageTrainInfoPanel(btrain));
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error initializing baggage train info panels", false);
            return false;
        }
    }

    public boolean initBarrackInfoPanels(final List<BarrackDTO> barracks) {
        try {
            for (BarrackDTO barrack : barracks) {
                barrackInfoPanels.get(barrack.getRegionId()).add(new BarracksInfoPanel(barrack));
            }
            return true;
        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Error initializing barrack info panels", false);
            return false;
        }
    }

    public List<AbstractInfoPanel> getMilitaryUnitPanelsByRegion(final int regionId) {
        final List<AbstractInfoPanel> list = new ArrayList<AbstractInfoPanel>();
        list.addAll(armyInfoPanels.get(regionId));
        list.addAll(corpInfoPanels.get(regionId));
        list.addAll(brigInfoPanels.get(regionId));
        return list;
    }

    public List<AbstractInfoPanel> getNavalUnitPanelsByRegionId(final int regionId) {
        final List<AbstractInfoPanel> list = new ArrayList<AbstractInfoPanel>();
        list.addAll(fleetInfoPanels.get(regionId));
        list.addAll(shipInfoPanels.get(regionId));
        return list;
    }

    /**
     * @param regionId the region to inspect.
     * @return the spyInfoPanels.
     */
    public List<SpyInfoPanel> getSpyInfoPanelsByRegion(final int regionId) {
        return spyInfoPanels.get(regionId);
    }

    /**
     * @param regionId the region to inspect.
     * @return the btrainInfoPanels
     */
    public List<BaggageTrainInfoPanel> getBtrainInfoPanelsByRegion(final int regionId) {
        return btrainInfoPanels.get(regionId);
    }

    /**
     * @param regionId the region to inspect.
     * @return the commInfoPanels
     */
    public List<CommanderInfoPanel> getCommInfoPanelsByRegion(final int regionId) {
        return commInfoPanels.get(regionId);
    }

    public List<Widget> getAllCommanderInfoPanelsByRegion(final int regionId) {
        final List<Widget> out = new ArrayList<Widget>();
        out.addAll(commInfoPanels.get(regionId));
        out.addAll(capturedCommInfoPanels.get(regionId));
        return out;
    }

    /**
     * @param regionId the region to inspect.
     * @return the commInfoPanels
     */
    public List<ForeignCommanderInfoPanel> getCapturedCommInfoPanelsByRegion(final int regionId) {
        return capturedCommInfoPanels.get(regionId);
    }


    /**
     * @param regionId the region to inspect.
     * @return the barrackInfoPanels
     */
    public List<BarracksInfoPanel> getBarrackInfoPanelsByRegion(final int regionId) {
        return barrackInfoPanels.get(regionId);
    }

    public boolean isInfoPanelLocked() {
        return infoPanelLocked;
    }

    public void setInfoPanelLocked(final boolean value) {
        this.infoPanelLocked = value;
    }

    public PopupPanelEAW getUnitPopup() {
        return unitPopup;
    }

    public void setUnitPopup(final PopupPanelEAW value) {
        this.unitPopup = value;
    }


}
