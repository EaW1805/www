package com.eaw1805.www.client.views.popups.actions;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.popups.menus.FleetMenu;
import com.eaw1805.www.client.views.popups.menus.ShipMenu;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FleetActionsPopup extends PopupPanelEAW implements ArmyConstants {
    private SectorDTO sector;
    private MapStore mapStore = MapStore.getInstance();
    private FleetMenu fleetMenu;
    private ShipMenu shipMenu;

    public FleetActionsPopup(final SectorDTO selSector) {
        setAutoHideEnabled(true);
        sector = selSector;
        final BarrackDTO bar = BarrackStore.getInstance().getBarrackByPosition(selSector);
        boolean haveMyShips = false;
        final List<FleetDTO> fleets = new ArrayList<FleetDTO>();
        if (NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true).size() > 0) {
            fleets.addAll(NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true));
            haveMyShips = true;
        }
        if (AlliedUnitsStore.getInstance().getAlliedFleetsBySector(sector) != null) {
            fleets.addAll(AlliedUnitsStore.getInstance().getAlliedFleetsBySector(sector));
        }
        if (ForeignUnitsStore.getInstance().getFleetsBySector(sector) != null) {
            fleets.addAll(ForeignUnitsStore.getInstance().getFleetsBySector(sector));
        }
        final FleetDTO zeroNew = new FleetDTO();
        zeroNew.setFleetId(0);
        zeroNew.setShips(new HashMap<Integer, ShipDTO>());
        if (NavyStore.getInstance().getNewShipBySector(sector.getId()) != null) {

            for (ShipDTO ship : NavyStore.getInstance().getNewShipBySector(sector.getId())) {
                zeroNew.getShips().put(ship.getId(), ship);
            }
            fleets.add(zeroNew);
        }
        final VerticalPanel selectionPanel = new VerticalPanel();
        final HorizontalPanel container = new HorizontalPanel();
        final double tileSize = MapStore.getInstance().getZoomedTileSize();

        FleetDTO zeroFleet = new FleetDTO();
        int totalMovingUnits = MiscCalculators.getFleetsMovingUnits(fleets);
        int rows = MiscCalculators.getMinPowerUnderTarget(totalMovingUnits);
        HorizontalPanel[] hPanels = new HorizontalPanel[rows];
        int counter = 0, row = 0;
        hPanels[0] = new HorizontalPanel();

        for (FleetDTO fleet : fleets) {
            if (fleet.getFleetId() == 0) {
                //since we could have 2 zero fleets..
                //one for the current nation and one for the allied
                zeroFleet.getShips().putAll(fleet.getShips());

            }
        }
        final FleetActionsPopup mySelf = this;

        if (haveMyShips) {
            final ImageButton orgfleetImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButFormFleetOff.png");
            orgfleetImg.deselect();
            orgfleetImg.setTitle("Organize Fleets");
            orgfleetImg.setSize((tileSize / 2) + "px", (tileSize / 2) + "px");
            orgfleetImg.setStyleName("pointer", true);

            // If there are armies on the tile.
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    mapStore.getMapsView().getNavyOrgPanel().initBySector(sector.getRegionId(), sector.getX(), sector.getY(), FLEET);
                    mySelf.hide();
                }
            }).addToElement(orgfleetImg.getElement()).register();

            container.add(orgfleetImg);
        }


        for (final FleetDTO fleet : fleets) {
            if (fleet.getFleetId() != 0) {
                final int nationId = NavyStore.getInstance().getNationIdFromFleet(fleet);
                int power = PowerCalculators.calculateFleetPower(fleet);

                final String url = "http://static.eaw1805.com/images/figures/" + fleet.getNationId()
                            + "/FleetMap00.png";

                final FigureItem fleetFig = new FigureItem(url,
                        (int) tileSize, FLEET, fleet.getFleetId(), nationId, sector.getId(), false, power);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        if (!ForeignUnitsStore.getInstance().isUnitForeign(sector.getId(), FLEET, fleet.getId())) {
                            if (GameStore.getInstance().getNationId() == nationId
                                    || (bar != null && bar.getNationId() == GameStore.getInstance().getNationId())
                                    || fleet.hasLoadedItemsOrUnits()
                                    || TransportStore.getInstance().hasUnitsToBoard(fleet)) {
                                mapStore.getMapsView().goToPosition(fleet);
                                container.clear();
                                //                            if (nationId == GameStore.getInstance().getNationId()) {
                                container.add(selectionPanel);
                                selectionPanel.clear();
                                fleetMenu = new FleetMenu(fleet, mySelf);
                                selectionPanel.add(fleetMenu);
                                displacePopup1();
                                //                            } else {
                                //                                MovementEventManager.startAllyMovement(FLEET, fleetImage.getId(), nationId,
                                //                                        RegionStore.getInstance().getSectorByPosition(fleet));
                                //                                UnitEventManager.undoSelection();
                                //                            }
                            }
                        }
                    }
                }).addToElement(fleetFig.getFigImg().getElement()).register();

                hPanels[row].add(fleetFig);
                counter++;
                if (counter == rows) {
                    row++;
                    if (row < rows) {
                        hPanels[row] = new HorizontalPanel();
                        hPanels[row].add(new AbsolutePanel());
                    }
                    counter = 0;
                }
            }
        }

        if (zeroFleet != null) {
            for (final ShipDTO ship : zeroFleet.getShips().values()) {
                final int nationId = ship.getNationId();
                final String url;
                //check if it is a new ship - then add the construction image
                final boolean isNew;
                isNew = zeroNew.getShips().containsKey(ship.getId());
                if (isNew) {
                    url = "http://static.eaw1805.com/images/figures/shipConstruction.png";
                } else {
                    url = "http://static.eaw1805.com/images/figures/" + nationId + "/FleetMap00.png";
                }
                final FigureItem shipFig = new FigureItem(url,
                        (int) tileSize, SHIP, ship.getId(), nationId, sector.getId(), false, 0);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        if (!isNew && !ForeignUnitsStore.getInstance().isUnitForeign(sector.getId(), SHIP, ship.getId())) {
                            if (GameStore.getInstance().getNationId() == nationId ||
                                    (bar != null && bar.getNationId() == GameStore.getInstance().getNationId()) ||
                                    ship.hasLoadedItemsOrUnits()) {
                                mapStore.getMapsView().goToPosition(ship);
                                container.clear();
                                //                            if (nationId == GameStore.getInstance().getNationId()) {
                                container.add(selectionPanel);
                                selectionPanel.clear();
                                shipMenu = new ShipMenu(ship, mySelf);
                                selectionPanel.add(shipMenu);
                                if (NavyStore.getInstance().isTradeShip(ship)) {
                                    displacePopup(-40);
                                } else {
                                    displacePopup(-20);
                                }
                            }
                        }
                    }
                }).addToElement(shipFig.getFigImg().getElement()).register();

                hPanels[row].add(shipFig);
                counter++;
                if (counter == rows) {
                    row++;
                    if (row < rows) {
                        hPanels[row] = new HorizontalPanel();
                    }
                    counter = 0;
                }
            }
        }

        for (int i = 0; i <= row; i++) {
            try {
                selectionPanel.add(hPanels[i]);
            } catch (Exception exr) {
                // do nothing
            }
        }

        PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        double offset = 0;
        double offset2 = 0;
        if (totalMovingUnits > 1) {
            offset = ((rows - 1) * tileSize) / 2 - tileSize / 2;
            offset2 = ((row - 1) * tileSize) / 2 - tileSize / 2;
        }
        this.setPopupPosition((int) (mapStore.getZoomedPointX(sector.getX()) + mapStore.getZoomOffsetX() - position.getX() - offset),
                (int) (mapStore.getZoomedPointY(sector.getY()) + mapStore.getZoomOffsetY() - position.getY() - offset2));


        container.add(selectionPanel);
        this.add(container);

        setStyleName("");

        this.addAttachHandler(new AttachEvent.Handler() {
            public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    UnitEventManager.undoSelection();
                    if (mapStore.getUnitGroups().getFleetsByRegionId(mapStore.getActiveRegion()) != null
                            && !NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true).isEmpty()) {
                        mapStore.getUnitGroups().getFleetsByRegionId(mapStore.getActiveRegion())
                                .recoverFleets(mapStore.getActiveRegion());
                    }
                }
                if (mapStore.getAlliedUnitGroups().getFleetsByRegionId(mapStore.getActiveRegion()) != null) {
                    mapStore.getAlliedUnitGroups().getFleetsByRegionId(mapStore.getActiveRegion())
                            .recoverFleets(mapStore.getActiveRegion());
                }
                if (mapStore.getForeignUnitsGroup().getFleetsByRegionId(mapStore.getActiveRegion()) != null) {
                    mapStore.getForeignUnitsGroup().getFleetsByRegionId(mapStore.getActiveRegion())
                            .recoverFleets(mapStore.getActiveRegion());
                }
                if (mapStore.getForeignUnitsGroup().getRegionReportedFleetImages(mapStore.getActiveRegion()) != null) {
                    mapStore.getForeignUnitsGroup().getRegionReportedFleetImages(mapStore.getActiveRegion())
                            .recoverFleets(mapStore.getActiveRegion());
                }
                if (mapStore.getUnitGroups().getNewShipsByRegionId(mapStore.getActiveRegion()) != null
                        && !NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true).isEmpty()) {
                    mapStore.getUnitGroups().getNewShipsByRegionId(mapStore.getActiveRegion())
                            .recoverFleets(mapStore.getActiveRegion());
                }
            }
        });

    }

    private void displacePopup1() {
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        this.setPopupPosition(mapStore.getZoomedPointX(sector.getX()) + (int)mapStore.getZoomOffsetX() - position.getX() - 87,
                mapStore.getZoomedPointY(sector.getY()) + (int)mapStore.getZoomOffsetY() - position.getY() - 73);
    }

    private void displacePopup(int left) {
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        this.setPopupPosition(mapStore.getZoomedPointX(sector.getX()) + (int)mapStore.getZoomOffsetX() - position.getX() - 107 + left,
                mapStore.getZoomedPointY(sector.getY()) + (int)mapStore.getZoomOffsetY() - position.getY() - 73);
    }
}
