package com.eaw1805.www.shared.stores.map.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.deploy.DeployEventManager;
import com.eaw1805.www.client.events.deploy.DisembarkUnitEvent;
import com.eaw1805.www.client.events.deploy.DisembarkUnitHandler;
import com.eaw1805.www.client.events.deploy.EmbarkUnitEvent;
import com.eaw1805.www.client.events.deploy.EmbarkUnitHandler;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.views.popups.actions.ArmyActionsPopup;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapArmyGroup
        extends Group
        implements MapConstants, ArmyConstants {

    private final Group removedArmies = new Group();

    public MapArmyGroup(final List<ArmyDTO> armies, final int regionId,
                        final boolean isAllied, final boolean isForeign, final boolean isNew) {
        super();

        DeployEventManager.addEmbarkUnitHandler(new EmbarkUnitHandler() {
            public void onEmbarkUnit(final EmbarkUnitEvent event) {
                if (!isAllied && !isForeign && !isNew
                        && event.getCargoType() == BRIGADE) {
                    MapArmyGroup.this.clear();
                    ArmyStore.getInstance().initSectorArmiesMap();
                    ArmyStore.getInstance().initStartingSectorArmiesMap();
                    initArmyImages(ArmyStore.getInstance().getArmiesByRegion(regionId), isAllied, isForeign, isNew);
                }
            }
        });

        DeployEventManager.addDisembarkUnitHandler(new DisembarkUnitHandler() {
            public void onDisembarkUnit(final DisembarkUnitEvent event) {
                if (!isAllied && !isForeign && !isNew
                        && event.getCargoType() == BRIGADE) {
                    MapArmyGroup.this.clear();
                    ArmyStore.getInstance().initSectorArmiesMap();
                    ArmyStore.getInstance().initStartingSectorArmiesMap();
                    initArmyImages(ArmyStore.getInstance().getArmiesByRegion(regionId), isAllied, isForeign, isNew);
                }
            }
        });

        MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
            public void onMovementStop(final MovementStopEvent event) {
                if (!isAllied && !isForeign && !isNew
                        && (event.getInfoType() == ARMY || event.getInfoType() == CORPS || event.getInfoType() == BRIGADE)) {
                    MapArmyGroup.this.clear();
                    ArmyStore.getInstance().initSectorArmiesMap();
                    ArmyStore.getInstance().initStartingSectorArmiesMap();
                    initArmyImages(ArmyStore.getInstance().getArmiesByRegion(regionId), isAllied, isForeign, isNew);
                }
            }
        });

        initArmyImages(armies, isAllied, isForeign, isNew);
    }

    public void initArmyImages(final List<ArmyDTO> armies, final boolean isAllied, final boolean isForeign, final boolean isNew) {
        int nationId;
        try {
            nationId = armies.get(0).getCorps().values().iterator().next().getBrigades().values().iterator().next().getNationId();

        } catch (Exception ex) {
            nationId = GameStore.getInstance().getNationId();
        }

        // For Every Army in the FederationCollection Add a new ArmyImage
        final Map<String, Integer> sectorToFlag = new HashMap<String, Integer>();
        final Map<String, Integer> sectorPowerMap = new HashMap<String, Integer>();
        for (ArmyDTO army : armies) {
            if (army.getArmyId() == 0) {
                if (!army.getCorps().isEmpty()) {
                    for (CorpDTO corp : army.getCorps().values()) {
                        if (corp.getCorpId() == 0) {
                            if (!corp.getBrigades().isEmpty()) {
                                for (BrigadeDTO brigade : corp.getBrigades()
                                        .values()) {
                                    if (!TutorialStore.getInstance().isTutorialMode()
                                            || !brigade.getFromInit()
                                            || TutorialStore.getInstance().getMonth() >= 8) {
                                        if (!brigade.getLoaded()) {
                                            if (!sectorPowerMap.containsKey(brigade
                                                    .getRegionId()
                                                    + "-"
                                                    + brigade.getX()
                                                    + ":"
                                                    + brigade.getY())) {
                                                sectorPowerMap.put(
                                                        brigade.getRegionId() + "-"
                                                                + brigade.getX() + ":"
                                                                + brigade.getY(), 0);
                                            }
                                            sectorToFlag.put(brigade.getRegionId() + "-"
                                                    + brigade.getX() + ":"
                                                    + brigade.getY(), brigade.getNationId());
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!sectorPowerMap.containsKey(corp.getRegionId()
                                    + "-" + corp.getX() + ":" + corp.getY())) {
                                sectorPowerMap.put(corp.getRegionId() + "-"
                                        + corp.getX() + ":" + corp.getY(), 0);
                            }
                            sectorToFlag.put(corp.getRegionId() + "-"
                                    + corp.getX() + ":" + corp.getY(), corp.getNationId());

                        }
                    }
                }
            } else {
                if (!sectorPowerMap.containsKey(army.getRegionId() + "-"
                        + army.getX() + ":" + army.getY())) {
                    sectorPowerMap.put(army.getRegionId() + "-" + army.getX()
                            + ":" + army.getY(), 0);
                }
                sectorToFlag.put(army.getRegionId() + "-" + army.getX()
                        + ":" + army.getY(), ArmyStore.getInstance().getArmyNation(army));
            }
        }

        for (final String sector : sectorPowerMap.keySet()) {
            final int regionId = Integer.parseInt(sector.split("-")[0]);
            final int xPos = Integer.parseInt(sector.split("-")[1].split(":")[0]);
            final int yPos = Integer.parseInt(sector.split("-")[1].split(":")[1]);
            final List<ArmyDTO> sArmies;
            // If we add our own units
            if (nationId == GameStore.getInstance().getNationId()) {
                sArmies = ArmyStore.getInstance().getArmiesBySector(
                        regionId, xPos, yPos, true);
            } else if (isAllied) {
                sArmies = AlliedUnitsStore.getInstance().getAlliedArmiesBySector(RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[xPos][yPos]);
            } else if (isNew) {
                sArmies = new ArrayList<ArmyDTO>();
            } else {
                sArmies = ForeignUnitsStore.getInstance().getArmiesBySector(RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[xPos][yPos]);
            }
            if (isNew) {
                addNewArmyImage(regionId, sectorToFlag.get(sector), xPos, yPos,
                        0, isNew);
            } else if (sArmies != null) {
                addNewArmyImage(regionId, sectorToFlag.get(sector), xPos, yPos,
                        PowerCalculators.calculateArmiesPower(sArmies, false), isNew);
            }
        }

    }

    final void addNewArmyImage(final int regionId, final int ntId, final int xPos, final int yPos, int power, final boolean isNew) {

        if (power == -1) {
            power = 0;
        }
        if (power >= 0) {
            final String nationId = String.valueOf(ntId);
            final String armyTile;
            if (isNew) {
                armyTile = "http://static.eaw1805.com/images/figures/" + nationId + "/UnitMapConstruction.png";

            } else if (power < 10) {
                armyTile = DIR_ARMIES + "/" + nationId + "/UnitMap0" + power
                        + ".png";
            } else {
                armyTile = DIR_ARMIES + "/" + nationId + "/UnitMap" + power
                        + ".png";
            }

            final double zoomLvl = MapStore.getInstance().getZoomLevel();

            final MapImage newImage = new MapImage(MapStore.getInstance().getPointX(xPos) + (int) (ARMYOFFSETS[1][0] * zoomLvl),
                    MapStore.getInstance().getPointY(yPos) + (int) (ARMYOFFSETS[1][1] * zoomLvl),
                    (int) (UNITSIZES[1] * zoomLvl), (int) (UNITSIZES[1] * zoomLvl), armyTile);
            newImage.setxPos(xPos);
            newImage.setyPos(yPos);
            addHoverImageFunctionality(newImage, ARMY);
            newImage.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent event) {
                    final SectorDTO selSector = RegionStore.getInstance()
                            .getRegionSectorsByRegionId(
                                    MapStore.getInstance().getActiveRegion())[newImage
                            .getxPos()][newImage.getyPos()];

                    final ArmyActionsPopup armyPopup = new ArmyActionsPopup(selSector);
                    armyPopup.show();
                    removeArmyImages(regionId, xPos, yPos);

                    event.stopPropagation();
                }
            });
            this.add(newImage);
        }


    }

    public void removeArmyImages(final int regionId, final int xPos, final int yPos) {
        final int armiesCounter = this.getVectorObjectCount();
        for (int i = armiesCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) this.getVectorObject(i);
            if (img.getxPos() == xPos && img.getyPos() == yPos) {
                this.remove(img);
                removedArmies.add(img);
            }
        }
    }

    public void recoverArmies(final int regionId) {
        final int armiesCounter = removedArmies.getVectorObjectCount();
        final double zoomLvl = MapStore.getInstance().getZoomLevel();
        for (int i = armiesCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) removedArmies.getVectorObject(i);
            removedArmies.remove(img);
            img.setSize(Integer.toString((int) (UNITSIZES[1] * zoomLvl)) + "px", Integer.toString((int) (UNITSIZES[1] * zoomLvl)) + "px");
            img.setX(MapStore.getInstance().getPointX(img.getxPos()) + (int) (ARMYOFFSETS[1][0] * zoomLvl));
            img.setY(MapStore.getInstance().getPointY(img.getyPos()) + (int) (ARMYOFFSETS[1][1] * zoomLvl));
            this.add(img);
        }
    }

    public void addHoverImageFunctionality(final Image img, final int armyType) {
        img.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                int xPos = 0, yPos = 0;
                xPos = img.getX();
                yPos = img.getY();
                img.setHeight((int) (img.getHeight() * 1.5));
                img.setWidth((int) (img.getWidth() * 1.5));
                img.setX(xPos);
                img.setY(yPos);
            }
        });
        img.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                final int xPos = img.getX();
                final int yPos = img.getY();
                img.setHeight(2 * img.getHeight() / 3);
                img.setWidth(2 * img.getWidth() / 3);
                img.setX(xPos);
                img.setY(yPos);
            }
        });
    }
}
