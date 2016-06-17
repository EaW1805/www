package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.ProSiteLoadedEvent;
import com.eaw1805.www.client.events.loading.ProSiteLoadedHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.views.infopanels.units.mini.BaggageTrainRepairCostMini;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.client.widgets.*;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class BaggageTrainMenu
        extends UnitMenu
        implements ArmyConstants, RelationConstants, TradeInfoViewInterface, StyleConstants {

    private final ImageButton tradeImg;
    private final MapStore mapStore = MapStore.getInstance();
    private final ImageButton moveImg;
    private ImageButton repairImg;
    private final ImageButton unloadUnitsImg;
    private final ImageButton scuttleImg;
    private final ImageButton loadImg;
    private final UnitChangedHandler unitChangedHandler;
    public BaggageTrainMenu(final BaggageTrainDTO btrain, final PopupPanelEAW caller) {
        setPopupParent(caller);
        setSize("320px", "137px");
        setStyleName("none");

        final AbsolutePanel basePanel = new AbsolutePanel();
        this.add(basePanel, 110, 73);
        basePanel.setSize("64px", "64px");

        final FigureItem btrainImage = new FigureItem("http://static.eaw1805.com/images/figures/baggage.png", 64, BAGGAGETRAIN, btrain.getId(), btrain.getNationId(),
                RegionStore.getInstance().getSectorByPosition(btrain).getId(), true, 0);
        basePanel.add(btrainImage, 0, 0);

        moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                mapStore.getMapsView().goToPosition(btrain);
                mapStore.getMapsView().addFigureOnMap(BAGGAGETRAIN, btrain.getId(),
                        btrain, (int) (80d * btrain.getCondition() / 100d), 0, 0, 0);
                caller.hide();
            }
        }).addToElement(moveImg.getElement()).register();

        moveImg.setTitle("Issue move orders.");
        moveImg.setStyleName(CLASS_POINTER);
        //////
        moveImg.setSize(SIZE_36PX, SIZE_36PX);


        tradeImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
        final TradeInfoViewInterface mySelf = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new TradePanelView(btrain, mySelf, BAGGAGETRAIN));
                caller.hide();
            }
        }).addToElement(tradeImg.getElement()).register();

        tradeImg.setTitle("Show trade options.");
        tradeImg.setStyleName(CLASS_POINTER);
        tradeImg.setSize(SIZE_36PX, SIZE_36PX);

        if (RegionStore.getInstance().getRegionSectorsByRegionId(btrain.getRegionId())[btrain.getXStart()][btrain.getYStart()].hasShipyardOrBarracks() &&
                (RegionStore.getInstance().getRegionSectorsByRegionId(btrain.getRegionId())[btrain.getXStart()][btrain.getYStart()].getNationId() == GameStore.getInstance().getNationId()
                        || RelationsStore.getInstance().isAlly(btrain.getNationId()))) {
            repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
            repairImg.setTitle("Repair baggage train.");
            repairImg.setStyleName(CLASS_POINTER);
            repairImg.setSize(SIZE_36PX, SIZE_36PX);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final SectorDTO sector = MapStore.getInstance().getRegionSectorByRegionIdXY(btrain.getRegionId(), btrain.getXStart(), btrain.getYStart());
                    if (GameStore.getInstance().getNationId() == btrain.getNationId()) {
                        BaggageTrainStore.getInstance().addRepairOrder(btrain.getId(), sector.getId());

                    } else {
                        AlliedUnitsStore.getInstance().repairTrain(btrain.getId(), sector.getId(), btrain.getNationId());
                    }
                    caller.hide();
                }
            }).addToElement(repairImg.getElement()).register();

            new ToolTipPanel(repairImg) {
                @Override
                public void generateTip() {
                    setTooltip(new BaggageTrainRepairCostMini(btrain));
                }
            };
        }

        unloadUnitsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDisembarkOff.png");
        unloadUnitsImg.setTitle("Unload units.");
        unloadUnitsImg.setStyleName(CLASS_POINTER);
        unloadUnitsImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new UnloadTroopsView(BAGGAGETRAIN, btrain));
            }
        }).addToElement(unloadUnitsImg.getElement()).register();

        loadImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        loadImg.setTitle("Board troops");
        loadImg.setSize(SIZE_36PX, SIZE_36PX);
        loadImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final DeployTroopsView dpView = new DeployTroopsView(btrain.getRegionId(), BAGGAGETRAIN, btrain.getId());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                caller.hide();
            }
        }).addToElement(loadImg.getElement()).register();

        scuttleImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButScuttleBagTrainOff.png");
        scuttleImg.setTitle("Scuttle baggage train.");
        scuttleImg.setStyleName(CLASS_POINTER);
        scuttleImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                BaggageTrainStore.getInstance().addScuttleOrder(btrain.getId(), btrain.getRegionId());
                scuttleImg.deselect();
                caller.hide();
            }
        }).addToElement(scuttleImg.getElement()).register();

        positionImagesAndBackground(btrain);

        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                positionImagesAndBackground(btrain);
            }
        });

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == BAGGAGETRAIN
                        && (event.getInfoId() == btrain.getId() || event.getInfoId() == 0)) {
                    positionImagesAndBackground(btrain);
                }
            }
        };

    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public final void positionImagesAndBackground(final BaggageTrainDTO bTrain) {
        super.clearAllButtons();

        //then place them again..
        final SectorDTO[][] regionDTO = RegionStore.getInstance().getRegionSectorsByRegionId(bTrain.getRegionId());

        if (regionDTO.length < bTrain.getX() || regionDTO[bTrain.getX()].length < bTrain.getY()) {
            return;
        }

        final SectorDTO sector = regionDTO[bTrain.getX()][bTrain.getY()];

        if (sector == null) {
            return;
        }

        //if empire is dead then don't add the action images.
        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }

        final BarrackDTO barrack = BarrackStore.getInstance().getBarrackByPosition(sector);

        final int cond = bTrain.getCondition();
        final List<BaggageTrainDTO> bTrainsList = BaggageTrainStore.getInstance().getBaggageTrainsBySector(sector, true);

        boolean canTrade = false;
        if (barrack != null || !bTrainsList.isEmpty()) {
            canTrade = true;
        }

        boolean hasUnits = false;
        if (bTrain.getLoadedUnitsMap() != null) {
            for (List<Integer> units : bTrain.getLoadedUnitsMap().values()) {
                if (units.size() > 0) {
                    hasUnits = true;
                    break;
                }
            }
        }

        //start placing images
        if (GameStore.getInstance().getNationId() == bTrain.getNationId()) {
            addImageButton(moveImg);

        }

        if (canTrade) {
            addImageButton(tradeImg);
        }

        if (cond < 100 && repairImg != null) {
            addImageButton(repairImg);
        }

        if (hasUnits) {
            addImageButton(unloadUnitsImg);
        }

        addImageButton(loadImg);

        if (GameStore.getInstance().getNationId() == bTrain.getNationId()) {
            addImageButton(scuttleImg);
        }
        super.finalizeMenu(180);
    }

    public void closeTradePanel() {
        this.tradeImg.deselect();
    }

}
