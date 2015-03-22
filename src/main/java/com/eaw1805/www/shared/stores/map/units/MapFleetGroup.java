package com.eaw1805.www.shared.stores.map.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.popups.actions.FleetActionsPopup;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFleetGroup
        extends Group
        implements MapConstants, ArmyConstants {

    private final Group removedFleets = new Group();

    public MapFleetGroup(final List<FleetDTO> fleets, final int regionId, final boolean isAllied, final boolean isForeign, final boolean isNew) {

        super();

        int nationId = GameStore.getInstance().getNationId();
        if (fleets != null && !fleets.isEmpty()) {
            nationId = fleets.get(0).getNationId();
            //since now we also could have foreign fleets... and a zero fleet could have ships from different nations
            //if nation id is zero check for the first fleets flag.
            if (nationId == 0
                    && fleets.get(0).getShips() != null) {
                for (ShipDTO ship : fleets.get(0).getShips().values()) {
                    nationId = ship.getNationId();
                    break;
                }

            }
        }

        if (nationId == 0) {
            nationId = GameStore.getInstance().getNationId();
        }

        final int fNationId = nationId;

        final MapFleetGroup myself = this;
        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (!isAllied && !isForeign && !isNew
                        && event.getInfoType() == SHIP) {
                    myself.clear();
                    final List<FleetDTO> fleets = NavyStore.getInstance().getFleetsByRegion(regionId, true);
                    setImageUrls(fleets, fNationId, isNew, isAllied, isForeign);
                }
            }
        });

        MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
            public void onMovementStop(final MovementStopEvent event) {
                if (!isAllied && !isForeign && !isNew
                        && (event.getInfoType() == SHIP || event.getInfoType() == FLEET)) {
                    myself.clear();
                    final List<FleetDTO> fleets = NavyStore.getInstance().getFleetsByRegion(regionId, true);
                    setImageUrls(fleets, fNationId, isNew, isAllied, isForeign);
                }

            }
        });

        setImageUrls(fleets, nationId, isNew, isAllied, isForeign);
    }


    public final void setImageUrls(final List<FleetDTO> fleets, final int nationId, final boolean isNew, final boolean isAllied, final boolean isForeign) {
        final Map<PositionDTO, Integer> sectorToFlag = new HashMap<PositionDTO, Integer>();
        final Map<PositionDTO, Integer> sectorPowerMap = new HashMap<PositionDTO, Integer>();
        if (fleets != null) {
            for (FleetDTO fleet : fleets) {
                if (fleet.getFleetId() == 0) {
                    for (final ShipDTO empireShipDTO : fleet.getShips().values()) {
                        if (!sectorPowerMap.containsKey(empireShipDTO)) {
                            sectorPowerMap.put(empireShipDTO, 0);
                        }
                        sectorToFlag.put(empireShipDTO, empireShipDTO.getNationId());
                    }

                } else {
                    if (!sectorPowerMap.containsKey(fleet)) {
                        sectorPowerMap.put(fleet, 0);
                    }
                    sectorToFlag.put(fleet, fleet.getNationId());
                }
            }

            for (PositionDTO position : sectorPowerMap.keySet()) {
                final List<FleetDTO> sFleets;

                if (nationId == GameStore.getInstance().getNationId()) {
                    sFleets = NavyStore.getInstance().getFleetsByRegionAndTile(position, false, true);
                } else if (isAllied) {

                    sFleets = AlliedUnitsStore.getInstance().getNavyBySector(RegionStore.getInstance().getSectorByPosition(position));
                } else {
                    sFleets = ForeignUnitsStore.getInstance().getFleetsBySector(RegionStore.getInstance().getSectorByPosition(position));
                }
                addNewFleetImage(sectorToFlag.get(position), position,
                        PowerCalculators.calculateFleetPower(sFleets), isNew);
            }
        }
    }

    private void addNewFleetImage(final int ntId, final PositionDTO position, final int power, final boolean onlyNew) {
        final String nationId = String.valueOf(ntId);
        final String fleetImg;
        if (onlyNew) {
            fleetImg = "http://static.eaw1805.com/images/figures/shipConstruction.png";

        } else {
            if (power < 10) {
                fleetImg = DIR_FLEET + "/" + nationId + "/FleetMap0" + power + ".png";

            } else {
                fleetImg = DIR_FLEET + "/" + nationId + "/FleetMap" + power + ".png";
            }
        }

        final double zoomLvl = MapStore.getInstance().getZoomLevel();

        final MapImage newImage = new MapImage(MapStore.getInstance().getPointX(position.getX()) + (int) (NAVYOFFSETS[1][0] * zoomLvl),
                MapStore.getInstance().getPointY(position.getY()) + (int) (NAVYOFFSETS[1][1] * zoomLvl),
                (int) (UNITSIZES[1] * zoomLvl), (int) (UNITSIZES[1] * zoomLvl), fleetImg);
        newImage.setxPos(position.getX());
        newImage.setyPos(position.getY());
        addHoverImageFunctionality(newImage);
        newImage.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final SectorDTO selSector = RegionStore.getInstance().getRegionSectorsByRegionId(MapStore.getInstance().getActiveRegion())[newImage.getxPos()][newImage.getyPos()];
                final FleetActionsPopup fleetPopup = new FleetActionsPopup(selSector);
                fleetPopup.show();
                removeFleetImages(selSector);
                event.stopPropagation();
            }
        });
        this.add(newImage);
    }

    public void removeFleetImages(final PositionDTO position) {
        final int fleetsCounter = this.getVectorObjectCount();
        for (int i = fleetsCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) this.getVectorObject(i);
            if (img.getxPos() == position.getX() && img.getyPos() == position.getY()) {
                this.remove(img);
                removedFleets.add(img);
            }
        }
    }

    public void recoverFleets(final int regionId) {
        final int fleetsCounter = removedFleets.getVectorObjectCount();
        final double zoomLvl = MapStore.getInstance().getZoomLevel();
        for (int i = fleetsCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) removedFleets.getVectorObject(i);
            removedFleets.remove(img);
            img.setSize((int) (UNITSIZES[1] * zoomLvl) + "px", (int) (UNITSIZES[1] * zoomLvl) + "px");
            img.setX(MapStore.getInstance().getPointX(img.getxPos()) + (int) (NAVYOFFSETS[1][0] * zoomLvl));
            img.setY(MapStore.getInstance().getPointY(img.getyPos()) + (int) (NAVYOFFSETS[1][1] * zoomLvl));
            this.add(img);

        }
    }

    public void addHoverImageFunctionality(final Image img) {
        img.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                int xPos, yPos;
                xPos = (int) (img.getX() - img.getWidth() * 0.5);
                yPos = img.getY();
                img.setHeight((int) (img.getHeight() * 1.5));
                img.setWidth((int) (img.getWidth() * 1.5));
                img.setX(xPos);
                img.setY(yPos);
            }
        });

        img.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                int xPos, yPos;
                xPos = (int) (img.getX() + img.getWidth() * 0.333334);
                yPos = img.getY();
                img.setHeight(2 * img.getHeight() / 3);
                img.setWidth(2 * img.getWidth() / 3);
                img.setX(xPos);
                img.setY(yPos);
            }
        });
    }
}
