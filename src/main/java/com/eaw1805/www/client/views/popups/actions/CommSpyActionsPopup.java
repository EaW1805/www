package com.eaw1805.www.client.views.popups.actions;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.popups.menus.CommanderMenu;
import com.eaw1805.www.client.views.popups.menus.SpyMenu;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.ArrayList;
import java.util.List;

public class CommSpyActionsPopup extends PopupPanelEAW implements ArmyConstants {
    private SectorDTO sector;
    private MapStore mapStore = MapStore.getInstance();
    protected CommSpyActionsPopup mySelf = this;
    private CommanderMenu commanderMenu;
    private SpyMenu spyMenu;

    public CommSpyActionsPopup(SectorDTO selSector) {
        setAutoHideEnabled(true);
        sector = selSector;
        final List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
        if (CommanderStore.getInstance().getCommandersBySector(sector, false, false) != null) {
            commanders.addAll(CommanderStore.getInstance().getCommandersBySector(sector, false, false));
        }
        if (AlliedUnitsStore.getInstance().getCommanderBySector(sector) != null) {
            commanders.addAll(AlliedUnitsStore.getInstance().getCommanderBySector(sector));
        }
        if (ForeignUnitsStore.getInstance().getCommandersBySector(sector) != null) {
            commanders.addAll(ForeignUnitsStore.getInstance().getCommandersBySector(sector));
        }
        final List<SpyDTO> spies = new ArrayList<SpyDTO>();
        if (SpyStore.getInstance().getSpiesBySector(sector) != null) {
            spies.addAll(SpyStore.getInstance().getSpiesBySector(sector));
        }
        if (AlliedUnitsStore.getInstance().getSpiesBySector(sector) != null) {
            spies.addAll(AlliedUnitsStore.getInstance().getSpiesBySector(sector));
        }
        if (ForeignUnitsStore.getInstance().getSpiesBySector(sector) != null) {
            spies.addAll(ForeignUnitsStore.getInstance().getSpiesBySector(sector));
        }
        final HorizontalPanel selectionPanel = new HorizontalPanel();
        final double tileSize = mapStore.getZoomedTileSize();

        // ---Set the initial position---------//
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        double offset = 0;
        int commSize = 0;
        int spySize = 0;
        for (CommanderDTO commander : commanders) {
            if (commander.getArmy() == 0 && commander.getCorp() == 0 && !commander.getLoaded()) {
                commSize++;
            }
        }

        for (SpyDTO spy : spies) {
            if (!spy.getLoaded()) {
                spySize++;
            }
        }

        if (commanders.size() + spySize > 0) {
            offset = ((commSize + spySize) * tileSize) / 2 - tileSize / 2;
        }
        // ------------------------------------//

        this.setPopupPosition((int) (mapStore.getZoomedPointX(sector.getX()) + mapStore.getZoomOffsetX() - position.getX() - offset),
                (int) (mapStore.getZoomedPointY(sector.getY()) + mapStore.getZoomOffsetY() - position.getY()));
        boolean hasAliveCommanders = false;
        if (commanders.size() == 1 && spySize == 0 && commanders.get(0).getNationId() == GameStore.getInstance().getNationId()) {
            mapStore.getMapsView().goToPosition(commanders.get(0));
            selectionPanel.clear();
            commanderMenu = new CommanderMenu(commanders.get(0), mySelf);
            selectionPanel.add(commanderMenu);
            displacePopup(73, 80);
        } else if (commanders.size() > 0) {

            for (int i = 1; i < selectionPanel.getWidgetCount() - 1; i++) {
                DualStateImage tmpCommImg = (DualStateImage) selectionPanel
                        .getWidget(i);
                tmpCommImg.removeFromParent();
            }

            for (CommanderDTO commander : commanders) {

                if (commander.getArmy() == 0 && commander.getCorp() == 0 && !commander.getInPool()
                        && !commander.getDead() && !commander.isCaptured() && !commander.getLoaded()) {
                    hasAliveCommanders = true;
                    final FigureItem commanderImage = new FigureItem(
                            "http://static.eaw1805.com/images/figures/" + commander.getNationId()
                                    + "/commander.png", (int) tileSize, COMMANDER, commander.getId(), commander.getNationId(),
                            sector.getId(), false, 0);
                    final CommanderDTO comm = commander;
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            if (GameStore.getInstance().getNationId() == comm.getNationId()) {
                                mapStore.getMapsView().goToPosition(comm);
                                selectionPanel.clear();
                                if (comm.getNationId() == GameStore.getInstance().getNationId()) {
                                    commanderMenu = new CommanderMenu(comm, mySelf);
                                    selectionPanel.add(commanderMenu);
                                    displacePopup(73, 80);

                                } else {
                                    MovementEventManager.startAllyMovement(COMMANDER, comm.getId(), comm.getNationId(), RegionStore.getInstance().getSectorByPosition(comm));
                                    UnitEventManager.undoSelection();
                                }
                            }
                        }
                    }).addToElement(commanderImage.getFigImg().getElement()).register();

                    selectionPanel.add(commanderImage);
                }
            }
        }

        if (!hasAliveCommanders && spySize == 1 && !ForeignUnitsStore.getInstance().isUnitForeign(sector.getId(), SPY, spies.get(0).getSpyId())) {
            mapStore.getMapsView().goToPosition(spies.get(0));
            selectionPanel.clear();
            spyMenu = new SpyMenu(spies.get(0), mySelf);
            selectionPanel.add(spyMenu);
            displacePopup(89, 73);
        } else if (spySize > 0) {
            for (SpyDTO spy : spies) {
                if (!spy.getLoaded()) {
                    final FigureItem spyFig = new FigureItem(
                            "http://static.eaw1805.com/images/figures/" + spy.getNationId()
                                    + "/spy.png", (int) tileSize, SPY, spy.getSpyId(), spy.getNationId(), sector.getId(), false, 0);
                    final SpyDTO selSpy = spy;
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            if (!ForeignUnitsStore.getInstance().isUnitForeign(sector.getId(), SPY, selSpy.getSpyId())) {
                                mapStore.getMapsView().goToPosition(selSpy);
                                selectionPanel.clear();
                                spyMenu = new SpyMenu(selSpy, mySelf);
                                selectionPanel.add(spyMenu);
                                displacePopup(89, 73);
                            }
                        }
                    }).addToElement(spyFig.getFigImg().getElement()).register();
                    selectionPanel.add(spyFig);
                }
            }
        }

        this.add(selectionPanel);

        setStyleName("");

        this.addAttachHandler(new AttachEvent.Handler() {
            public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    if (mapStore.getUnitGroups().getSpiesCommandersByRegionId(mapStore.getActiveRegion()) != null) {
                        mapStore.getUnitGroups().getSpiesCommandersByRegionId(mapStore.getActiveRegion()).recoverUnits();
                    }
                    if (mapStore.getAlliedUnitGroups().getSpiesCommandersByRegionId(mapStore.getActiveRegion()) != null) {
                        mapStore.getAlliedUnitGroups().getSpiesCommandersByRegionId(mapStore.getActiveRegion()).recoverUnits();
                    }
                    if (mapStore.getForeignUnitsGroup().getSpiesCommandersByRegionId(mapStore.getActiveRegion()) != null) {
                        mapStore.getForeignUnitsGroup().getSpiesCommandersByRegionId(mapStore.getActiveRegion()).recoverUnits();
                    }
                    UnitEventManager.undoSelection();
                }
            }
        });
    }


    private void displacePopup(int offsetX, int offsetY) {
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        this.setPopupPosition(mapStore.getZoomedPointX(sector.getX()) + (int)mapStore.getZoomOffsetX() - position.getX() - offsetX,
                mapStore.getZoomedPointY(sector.getY()) + (int)mapStore.getZoomOffsetY() - position.getY() - offsetY);
    }
}
