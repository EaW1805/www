package com.eaw1805.www.shared.stores.map.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.popups.actions.BaggageTrainActionsPopup;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.List;

public class MapBtrainGroup
        extends Group
        implements MapConstants, ArmyConstants {

    private final Group removedBtrains = new Group();

    public MapBtrainGroup(final List<BaggageTrainDTO> baggageTrains, final int regionId, final boolean isAllied, final boolean isForeign) {
        super();

        final MapBtrainGroup myself = this;
        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (!isAllied && !isForeign
                        && event.getInfoType() == BAGGAGETRAIN) {
                    myself.clear();
                    final List<BaggageTrainDTO> bTrains = BaggageTrainStore.getInstance().getBaggageTrainsByRegion(regionId, true);
                    setImageUrls(bTrains);
                }
            }
        });

        MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
            public void onMovementStop(final MovementStopEvent event) {
                if (!isAllied && !isForeign
                        && event.getInfoType() == BAGGAGETRAIN) {
                    myself.clear();
                    final List<BaggageTrainDTO> bTrains = BaggageTrainStore.getInstance().getBaggageTrainsByRegion(regionId, true);
                    setImageUrls(bTrains);
                }
            }
        });

        setImageUrls(baggageTrains);
    }

    /**
     * Set the images on map for the baggage trains.
     *
     * @param baggageTrains The list of baggage trains.
     */
    public final void setImageUrls(final List<BaggageTrainDTO> baggageTrains) {
        if (baggageTrains != null) {
            for (BaggageTrainDTO baggageTrain : baggageTrains) {
                //if is scuttled don't display it
                if (!baggageTrain.isScuttle()) {
                    addNewBaggageTrainImage(baggageTrain);
                }
            }
        }
    }

    private void addNewBaggageTrainImage(final BaggageTrainDTO baggageTrain) {
        final double zoomLvl = MapStore.getInstance().getZoomLevel();

        final String bTrainUrl = DIR_ARMIES + "/baggage.png";
        final MapImage btrainImg = new MapImage(MapStore.getInstance().getPointX(baggageTrain.getX()) + (int) (BTRAINOFFSETS[1][0] * zoomLvl),
                MapStore.getInstance().getPointY(baggageTrain.getY()) + (int) (BTRAINOFFSETS[1][1] * zoomLvl),
                (int) (UNITSIZES[1] * zoomLvl), (int) (UNITSIZES[1] * zoomLvl), bTrainUrl);
        btrainImg.setxPos(baggageTrain.getX());
        btrainImg.setyPos(baggageTrain.getY());
        btrainImg.setId(baggageTrain.getId());
        btrainImg.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                final SectorDTO selSector = RegionStore.getInstance().getRegionSectorsByRegionId(MapStore.getInstance().getActiveRegion())[btrainImg.getxPos()][btrainImg.getyPos()];
                final BaggageTrainActionsPopup btrainPopup = new BaggageTrainActionsPopup(selSector);
                btrainPopup.show();
                removeTileBaggageTrainImages(baggageTrain.getX(), baggageTrain.getY());
                event.stopPropagation();
            }
        });

        addHoverImageFunctionality(btrainImg);
        this.add(btrainImg);

    }

    public void removeTileBaggageTrainImages(final int xPos, final int yPos) {
        final int btrainCounter = this.getVectorObjectCount();

        for (int i = btrainCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) this.getVectorObject(i);
            if (img.getxPos() == xPos && img.getyPos() == yPos) {
                this.remove(img);
                removedBtrains.add(img);
            }
        }
    }

    public void recoverBaggageTrains() {
        final int btrainCounter = removedBtrains.getVectorObjectCount();
        final double zoomLvl = MapStore.getInstance().getZoomLevel();
        for (int i = btrainCounter - 1; i > -1; i--) {
            final MapImage img = (MapImage) removedBtrains.getVectorObject(i);
            removedBtrains.remove(img);
            img.setSize((int) (UNITSIZES[1] * zoomLvl) + "px", (int) (UNITSIZES[1] * zoomLvl) + "px");
            img.setX(MapStore.getInstance().getPointX(img.getxPos()) + (int) (BTRAINOFFSETS[1][0] * zoomLvl));
            img.setY(MapStore.getInstance().getPointY(img.getyPos()) + (int) (BTRAINOFFSETS[1][1] * zoomLvl));
            this.add(img);
        }
    }

    public void addHoverImageFunctionality(final Image img) {
        img.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(final MouseOverEvent event) {
                int xPos, yPos;
                xPos = (int) (img.getX() - img.getWidth() * 0.5);
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
                xPos = (int) (img.getX() + img.getWidth() * 0.333334);
                yPos = (int) (img.getY() + img.getHeight() * 0.333334);
                img.setHeight(2 * img.getHeight() / 3);
                img.setWidth(2 * img.getWidth() / 3);
                img.setX(xPos);
                img.setY(yPos);
            }
        });
    }
}
