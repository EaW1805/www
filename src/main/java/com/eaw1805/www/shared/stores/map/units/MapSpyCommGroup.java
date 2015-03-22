package com.eaw1805.www.shared.stores.map.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.events.deploy.DeployEventManager;
import com.eaw1805.www.client.events.deploy.DisembarkUnitEvent;
import com.eaw1805.www.client.events.deploy.DisembarkUnitHandler;
import com.eaw1805.www.client.events.deploy.EmbarkUnitEvent;
import com.eaw1805.www.client.events.deploy.EmbarkUnitHandler;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.popups.actions.CommSpyActionsPopup;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSpyCommGroup
        extends Group
        implements ArmyConstants, MapConstants {

    private final Map<String, String> imageUrls = new HashMap<String, String>();

    private final Group removedUnits = new Group();

    private final int regionId;

    public MapSpyCommGroup(final List<CommanderDTO> commanders, final List<SpyDTO> spies, final int regionId, final boolean isAllied, final boolean isForeign) {
        super();

        this.regionId = regionId;

        setImageUrls(commanders, spies);

        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (!isAllied && !isForeign
                        && event.getInfoType() == COMMANDER) {
                    final List<CommanderDTO> commanders = CommanderStore.getInstance().getCommandersByRegion(regionId, true);
                    final List<SpyDTO> spies = SpyStore.getInstance().getSpiesByRegion(regionId);
                    setImageUrls(commanders, spies);
                }
            }
        });

        DeployEventManager.addEmbarkUnitHandler(new EmbarkUnitHandler() {
            public void onEmbarkUnit(final EmbarkUnitEvent event) {
                if (!isAllied && !isForeign
                        && (event.getCargoType() == SPY || event.getCargoType() == COMMANDER)) {
                    final List<CommanderDTO> commanders = CommanderStore.getInstance().getCommandersByRegion(regionId, true);
                    final List<SpyDTO> spies = SpyStore.getInstance().getSpiesByRegion(regionId);
                    setImageUrls(commanders, spies);
                }
            }
        });

        DeployEventManager.addDisembarkUnitHandler(new DisembarkUnitHandler() {
            public void onDisembarkUnit(final DisembarkUnitEvent event) {
                if (!isAllied && !isForeign
                        && (event.getCargoType() == SPY || event.getCargoType() == COMMANDER)) {
                    final List<CommanderDTO> commanders = CommanderStore.getInstance().getCommandersByRegion(regionId, true);
                    final List<SpyDTO> spies = SpyStore.getInstance().getSpiesByRegion(regionId);
                    setImageUrls(commanders, spies);
                }
            }
        });

        MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
            public void onMovementStop(final MovementStopEvent event) {
                if (!isAllied && !isForeign
                        && (event.getInfoType() == SPY || event.getInfoType() == COMMANDER)) {
                    final List<CommanderDTO> commanders = CommanderStore.getInstance().getCommandersByRegion(regionId, true);
                    final List<SpyDTO> spies = SpyStore.getInstance().getSpiesByRegion(regionId);
                    setImageUrls(commanders, spies);
                }
            }
        });
    }

    public final void setImageUrls(final List<CommanderDTO> commanders, final List<SpyDTO> spies) {
        this.clear();
        imageUrls.clear();

        if (commanders != null) {
            for (CommanderDTO commander : commanders) {
                if (!commander.getDead() && !commander.isCaptured()
                        && commander.getArmy() == 0 && commander.getCorp() == 0
                        && commander.getRegionId() == regionId && !commander.getInPool()
                        && !commander.getLoaded()) {
                    final String pos = commander.getX() + "-" + commander.getY();
                    final String thisCommStr = DIR_ARMIES + "/" + commander.getNationId() + "/commander.png";
                    if (!imageUrls.containsKey(pos)) {
                        imageUrls.put(pos, thisCommStr);
                    }
                }
            }
        }
        if (spies != null) {
            for (SpyDTO spy : spies) {
                if (spy.getRegionId() == regionId && !spy.getLoaded()) {
                    final String pos = spy.getX() + "-" + spy.getY();
                    final String thisSpyStr = DIR_ARMIES + "/" + spy.getNationId() + "/spy.png";
                    final String dualStr;
                    if (spy.getNationId() == NationConstants.NATION_MOROCCO
                            || spy.getNationId() == NationConstants.NATION_OTTOMAN
                            || spy.getNationId() == NationConstants.NATION_EGYPT) {
                        dualStr = DIR_ARMIES + "/dualMuslim.png";

                    } else {
                        dualStr = DIR_ARMIES + "/dual.png";
                    }
                    if (imageUrls.containsKey(pos)
                            && !imageUrls.get(pos).endsWith("/spy.png")) {
                        //if we have an image already, but the image is not a spies image
                        if (imageUrls.get(pos).equals(thisSpyStr)) {//if is same image, keep same image
                            imageUrls.put(pos, thisSpyStr);

                        } else {//if is other image means it is commanders, then add both
                            imageUrls.put(pos, dualStr);
                        }

                    } else {
                        //that means it is spy or it is nothing, both cases keep spy image
                        imageUrls.put(pos, thisSpyStr);
                    }
                }
            }
        }

        removedUnits.clear();
        addSpyCommandersOnMap();
    }


    private void addSpyCommandersOnMap() {
        final MapStore mapStore = MapStore.getInstance();
        final RegionStore regStore = RegionStore.getInstance();
        final double zoomLvl = mapStore.getZoomLevel();
        for (String pos : imageUrls.keySet()) {
            final String url = imageUrls.get(pos);
            final int xPos = Integer.parseInt(pos.split("-")[0]);
            final int yPos = Integer.parseInt(pos.split("-")[1]);

            final MapImage newImage = new MapImage(mapStore.getPointX(xPos) + (int) (SPYOFFSETS[1][0] * zoomLvl),
                    MapStore.getInstance().getPointY(yPos) + (int) (SPYOFFSETS[1][1] * zoomLvl),
                    (int) (UNITSIZES[1] * zoomLvl), (int) (UNITSIZES[1] * zoomLvl), url);
            newImage.setxPos(xPos);
            newImage.setyPos(yPos);
            addHoverImageFunctionality(newImage);
            newImage.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent event) {
                    final SectorDTO selSector = regStore.getRegionSectorsByRegionId(MapStore.getInstance().getActiveRegion())[newImage.getxPos()][newImage.getyPos()];
                    final CommSpyActionsPopup comPopup = new CommSpyActionsPopup(selSector);
                    comPopup.show();
                    removeTileUnitImages(xPos, yPos);
                    event.stopPropagation();
                }
            });
            this.add(newImage);
        }
    }


    public void removeTileUnitImages(final int xPos, final int yPos) {
        final int spiesCounter = this.getVectorObjectCount();
        for (int i = spiesCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) this.getVectorObject(i);
            if (img.getxPos() == xPos && img.getyPos() == yPos) {
                this.remove(img);
                removedUnits.add(img);
            }
        }
    }

    public void recoverUnits() {
        final int spiesCounter = removedUnits.getVectorObjectCount();
        final double zoomLvl = MapStore.getInstance().getZoomLevel();
        for (int i = spiesCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) removedUnits.getVectorObject(i);
            removedUnits.remove(img);
            img.setSize((int) (UNITSIZES[1] * zoomLvl) + "px", (int) (UNITSIZES[1] * zoomLvl) + "px");
            img.setX(MapStore.getInstance().getPointX(img.getxPos()) + (int) (SPYOFFSETS[1][0] * zoomLvl));
            img.setY(MapStore.getInstance().getPointY(img.getyPos()) + (int) (SPYOFFSETS[1][1] * zoomLvl));
            this.add(img);

        }
    }

    public void addHoverImageFunctionality(final Image img) {
        img.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                final int xPos, yPos;
                xPos = img.getX();
                yPos = (int) (img.getY() - img.getHeight() * 0.5);
                img.setHeight((int) (img.getHeight() * 1.5));
                img.setWidth((int) (img.getWidth() * 1.5));
                img.setX(xPos);
                img.setY(yPos);
            }
        });

        img.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                int xPos, yPos;
                xPos = img.getX();
                yPos = (int) (img.getY() + img.getHeight() * 0.333334);

                img.setHeight(2 * img.getHeight() / 3);
                img.setWidth(2 * img.getWidth() / 3);
                img.setX(xPos);
                img.setY(yPos);
            }
        });
    }
}
