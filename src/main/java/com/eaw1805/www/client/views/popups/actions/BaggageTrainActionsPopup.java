package com.eaw1805.www.client.views.popups.actions;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.popups.menus.BaggageTrainMenu;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;

import java.util.ArrayList;
import java.util.List;

public class BaggageTrainActionsPopup extends PopupPanelEAW implements ArmyConstants {
    private SectorDTO sector;
    private MapStore mapStore = MapStore.getInstance();
    protected BaggageTrainActionsPopup mySelf = this;
    private BaggageTrainMenu btrainMenu;

    public BaggageTrainActionsPopup(SectorDTO selSector) {
        setAutoHideEnabled(true);
        sector = selSector;

        final List<BaggageTrainDTO> btrains = new ArrayList<BaggageTrainDTO>();
        if (BaggageTrainStore.getInstance().getBaggageTrainsBySector(sector, true).size() > 0) {
            btrains.addAll(BaggageTrainStore.getInstance().getBaggageTrainsBySector(sector, true));
        }
        if (AlliedUnitsStore.getInstance().getBaggageTrainsBySector(sector) != null) {
            btrains.addAll(AlliedUnitsStore.getInstance().getBaggageTrainsBySector(sector));
        }
        if (ForeignUnitsStore.getInstance().getBaggageTrainsBySector(sector) != null) {
            btrains.addAll(ForeignUnitsStore.getInstance().getBaggageTrainsBySector(sector));
        }
        final HorizontalPanel selectionPanel = new HorizontalPanel();
        final double tileSize = mapStore.getZoomedTileSize();

        //---Set the initial position---------//
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        double offset = 0;
        if (btrains.size() > 0) {
            offset = ((btrains.size()) * tileSize) / 2 - tileSize / 2;
        }
        this.setPopupPosition((int) (mapStore.getZoomedPointX(sector.getX()) + mapStore.getZoomOffsetX() - position.getX() - offset),
                (int) (mapStore.getZoomedPointY(sector.getY()) + mapStore.getZoomOffsetY() - position.getY()));
        //------------------------------------//
        final BarrackDTO bar = BarrackStore.getInstance().getBarrackByPosition(selSector);
        if (btrains.size() > 1
                || (bar == null && btrains.get(0).getNationId() != GameStore.getInstance().getNationId())) {
            for (int i = 1; i < selectionPanel.getWidgetCount() - 1; i++) {
                DualStateImage tmpTrainImg = (DualStateImage) selectionPanel.getWidget(i);
                tmpTrainImg.removeFromParent();
            }
            for (BaggageTrainDTO btrain : btrains) {
                if (!btrain.isScuttle()) {
                    final FigureItem btrainFig = new FigureItem("http://static.eaw1805.com/images/figures/baggage.png",
                            (int) tileSize, BAGGAGETRAIN, btrain.getId(), btrain.getNationId(), sector.getId(), false, 0);
                    final BaggageTrainDTO thisTrain = btrain;
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            final BarrackDTO bar = BarrackStore.getInstance().getBarrackByPosition(sector);
                            if (!ForeignUnitsStore.getInstance().isUnitForeign(sector.getId(), BAGGAGETRAIN, thisTrain.getId())) {
                                if (GameStore.getInstance().getNationId() == thisTrain.getNationId()
                                        || (bar != null && bar.getNationId() == GameStore.getInstance().getNationId())) {
                                    mapStore.getMapsView().goToPosition(thisTrain);
                                    selectionPanel.clear();
                                    if (thisTrain.getNationId() == GameStore.getInstance().getNationId()) {
                                        btrainMenu = new BaggageTrainMenu(thisTrain, mySelf);
                                        selectionPanel.add(btrainMenu);
                                        displacePopup();
                                    } else {
                                        MovementEventManager.startAllyMovement(BAGGAGETRAIN, thisTrain.getId(), thisTrain.getNationId(), RegionStore.getInstance().getSectorByPosition(thisTrain));
                                        UnitEventManager.undoSelection();
                                    }
                                }
                            }
                        }
                    }).addToElement(btrainFig.getFigImg().getElement()).register();

                    selectionPanel.add(btrainFig);
                }
            }
        } else {
            mapStore.getMapsView().goToPosition(btrains.get(0));
            selectionPanel.clear();
            btrainMenu = new BaggageTrainMenu(btrains.get(0), mySelf);
            selectionPanel.add(btrainMenu);
            displacePopup();

//            } else {
//                MovementEventManager.startAllyMovement(BAGGAGETRAIN, btrains.get(0).getId(), btrains.get(0).getNationId(), RegionStore.getInstance().getSectorByPosition(btrains.get(0)));
//                UnitEventManager.undoSelection();
//            }
        }

        this.add(selectionPanel);

        setStyleName("");

        this.addAttachHandler(new AttachEvent.Handler() {
            public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    if (mapStore.getUnitGroups().getBaggageTrainsByRegionId(mapStore.getActiveRegion()) != null) {
                        if (!BaggageTrainStore.getInstance().getBaggageTrainsBySector(sector, true).isEmpty()) {
                            mapStore.getUnitGroups().getBaggageTrainsByRegionId(mapStore.getActiveRegion()).recoverBaggageTrains();
                        }
                    }
                    if (mapStore.getAlliedUnitGroups().getBaggageTrainsByRegionId(mapStore.getActiveRegion()) != null) {
                        mapStore.getAlliedUnitGroups().getBaggageTrainsByRegionId(mapStore.getActiveRegion()).recoverBaggageTrains();
                    }
                    if (mapStore.getForeignUnitsGroup().getBaggageTrainsByRegionId(mapStore.getActiveRegion()) != null) {
                        mapStore.getForeignUnitsGroup().getBaggageTrainsByRegionId(mapStore.getActiveRegion()).recoverBaggageTrains();
                    }
                    UnitEventManager.undoSelection();
                }
            }
        });
    }

    private void displacePopup() {
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        this.setPopupPosition(mapStore.getZoomedPointX(sector.getX()) + (int)mapStore.getZoomOffsetX() - position.getX() - 113,
                mapStore.getZoomedPointY(sector.getY()) + (int)mapStore.getZoomOffsetY() - position.getY() - 73);
    }
}
