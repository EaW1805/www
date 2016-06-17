package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.ProSiteLoadedEvent;
import com.eaw1805.www.client.events.loading.ProSiteLoadedHandler;
import com.eaw1805.www.client.events.trade.GetGoodEvent;
import com.eaw1805.www.client.events.trade.GetGoodHandler;
import com.eaw1805.www.client.events.trade.GiveGoodEvent;
import com.eaw1805.www.client.events.trade.GiveGoodHandler;
import com.eaw1805.www.client.events.trade.TradeEventManager;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.views.infopanels.units.mini.BaggageTrainRepairCostMini;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CommandersViewerPopup;
import com.eaw1805.www.client.views.popups.SpiesViewerPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Info panel that shows information about a baggage train.
 */
public class BaggageTrainInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, GoodConstants, TradeInfoViewInterface,
        RelationConstants, SelectableWidget<BaggageTrainDTO>, StyleConstants {

    private final RenamingLabel lblBaggageTrainName;

    private final BaggageTrainDTO baggageTrain;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private final Label lblAvailable;
    private final AbsolutePanel goodsPanel;

    private final ImageButton scuttleBTrain;
    private ImageButton repairImg;
    private final Image unloadTroopsImg;
    private final ImageButton tradeImage;
    private final ImageButton moveImage;
    private final ImageButton viewImage;
    private final ImageButton loadImg;
    private final Label lbMovementPoints;
    private final UnitChangedHandler unitChangedHandler;

    public BaggageTrainInfoPanel(final BaggageTrainDTO train) {
        baggageTrain = train;
        this.setStyleName("baggageInfoPanel");
        this.setSize("366px", "90px");

        final Image image = new Image("http://static.eaw1805.com/images/figures/baggage.png");
        this.add(image, 3, 3);
        image.setSize("", "82px");

        goodsPanel = new AbsolutePanel();
        goodsPanel.setSize("150px", "35px");
        add(goodsPanel, 170, 30);

        lblBaggageTrainName = new RenamingLabel("Baggage Train Name", BAGGAGETRAIN, baggageTrain.getId());
        lblBaggageTrainName.setStyleName("clearFontMiniTitle");
        this.add(lblBaggageTrainName, 90, 3);
        lblBaggageTrainName.setSize("249px", "21px");

        lblAvailable = new Label(numberFormat.format((TradeStore.getInstance().getTradeUnitWeight(baggageTrain) - TradeStore.getInstance().getTradeUnitLoad(baggageTrain))) + " tons");
        lblAvailable.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblAvailable.setStyleName(CLASS_CLEARFONTSMALL);
        add(lblAvailable, 90, 20);

        final Label lblCondition = new Label(train.getCondition() + "%");
        lblCondition.setTitle("Condition");
        lblCondition.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblCondition.setStyleName(CLASS_CLEARFONTSMALL);
        this.add(lblCondition, 90, 34);

        //x/y,mps
        final Label lblLocation = new Label(baggageTrain.positionToString());
        lblLocation.setStyleName(CLASS_CLEARFONTSMALL);
        lblLocation.setTitle("Baggage trains position.");
        this.add(lblLocation, 315, 3);
        lblLocation.setSize("47px", SIZE_15PX);
        lbMovementPoints = new Label(train.getMps() + " MPs");
        if (GameStore.getInstance().getNationId() == baggageTrain.getNationId()) {

            lbMovementPoints.setStyleName(CLASS_CLEARFONTSMALL);
            lbMovementPoints.setTitle("Movement Points");
            this.add(lbMovementPoints, 315, 20);
            lbMovementPoints.setSize("47px", SIZE_15PX);
        }

        moveImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                MapStore.getInstance().getMapsView().goToPosition(baggageTrain);
                MapStore.getInstance().getMapsView().addFigureOnMap(BAGGAGETRAIN, baggageTrain.getId(),
                        baggageTrain, 75, 0, 0, 0);
                moveImage.deselect();
            }
        }).addToElement(moveImage.getElement()).register();

        moveImage.setTitle("Click here to move the baggage train");
        moveImage.setSize(SIZE_20PX, SIZE_20PX);

        viewImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
        viewImage.setTitle("Go to target sector");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                MapStore.getInstance().getMapsView().goToPosition(baggageTrain);
                viewImage.deselect();
            }
        }).addToElement(viewImage.getElement()).register();

        viewImage.setStyleName(CLASS_POINTER);
        viewImage.setSize(SIZE_20PX, SIZE_20PX);

        tradeImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
        final BaggageTrainInfoPanel mySelf = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new TradePanelView(train, mySelf, BAGGAGETRAIN));
            }
        }).addToElement(tradeImage.getElement()).register();
        tradeImage.setTitle("Open trade overview");
        tradeImage.setStyleName(CLASS_POINTER);
        tradeImage.setSize(SIZE_20PX, SIZE_20PX);

        unloadTroopsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDisembarkOff.png");
        unloadTroopsImg.setTitle("Unload troops");
        unloadTroopsImg.setSize(SIZE_20PX, SIZE_20PX);
        unloadTroopsImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new UnloadTroopsView(BAGGAGETRAIN, baggageTrain));
            }
        }).addToElement(unloadTroopsImg.getElement()).register();


        loadImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        loadImg.setTitle("Board troops");
        loadImg.setSize(SIZE_20PX, SIZE_20PX);
        loadImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final DeployTroopsView dpView = new DeployTroopsView(baggageTrain.getRegionId(), BAGGAGETRAIN, baggageTrain.getId());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
            }
        }).addToElement(loadImg.getElement()).register();

        if (RegionStore.getInstance().getRegionSectorsByRegionId(baggageTrain.getRegionId())[baggageTrain.getXStart()][baggageTrain.getYStart()].hasShipyardOrBarracks() &&
                (RegionStore.getInstance().getRegionSectorsByRegionId(baggageTrain.getRegionId())[baggageTrain.getXStart()][baggageTrain.getYStart()].getNationId() == GameStore.getInstance().getNationId()
                        || RelationsStore.getInstance().getOriginalRelationByNationId(baggageTrain.getNationId()) == REL_ALLIANCE)) {
            repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
            repairImg.setTitle("Repair");
            repairImg.setSize(SIZE_20PX, SIZE_20PX);
            repairImg.setStyleName(CLASS_POINTER);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    final SectorDTO sectorDTO = MapStore.getInstance().getRegionSectorByRegionIdXY(baggageTrain.getRegionId(), baggageTrain.getX(), baggageTrain.getY());
                    BaggageTrainStore.getInstance().addRepairOrder(baggageTrain.getId(), sectorDTO.getId());
                    repairImg.deselect();
                }
            }).addToElement(repairImg.getElement()).register();

            //add tooltip to repair image with more information about this action.
            new ToolTipPanel(repairImg) {
                @Override
                public void generateTip() {
                    setTooltip(new BaggageTrainRepairCostMini(baggageTrain));
                }
            };
        }


        scuttleBTrain = new ImageButton("http://static.eaw1805.com/images/buttons/ButScuttleBagTrainOff.png");
        scuttleBTrain.setTitle("Scuttle Baggage Train");
        scuttleBTrain.setStyleName(CLASS_POINTER);
        scuttleBTrain.setSize(SIZE_20PX, SIZE_20PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                BaggageTrainStore.getInstance().addScuttleOrder(baggageTrain.getId(), baggageTrain.getRegionId());
                scuttleBTrain.deselect();
            }
        }).addToElement(scuttleBTrain.getElement()).register();


        TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {
            public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                if (getGoodEvent.getUnitType() == BAGGAGETRAIN
                        && (getGoodEvent.getUnitId() == baggageTrain.getId() || getGoodEvent.getUnitId() == 0)
                        && getGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()) {
                    setupGoodsPanel();
                }
            }
        });

        TradeEventManager.addGiveGoodHanlder(new GiveGoodHandler() {
            public void onGiveGoodIn(final GiveGoodEvent giveGoodEvent) {
                if (giveGoodEvent.getUnitType() == BAGGAGETRAIN
                        && (giveGoodEvent.getUnitId() == baggageTrain.getId() || giveGoodEvent.getUnitId() == 0)
                        && giveGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()) {
                    setupGoodsPanel();
                }
            }
        });

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == BAGGAGETRAIN && event.getInfoId() == baggageTrain.getId()) {
                    setupGoodsPanel();
                    if (GameStore.getInstance().getNationId() == train.getNationId()) {
                        try {
                            positionActionImages(train);
                        } catch (Exception e) {
//                            Window.alert("btrain unit changed : " + e.toString());
                        }
                    }
                }
            }
        };

        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                if (GameStore.getInstance().getNationId() == train.getNationId()) {
                    try {
                        positionActionImages(train);
                    } catch (Exception e) {
//                        Window.alert("btrain unit pro sites loaded : " + e.toString());
                    }
                }

            }
        });

        if (GameStore.getInstance().getNationId() == train.getNationId()) {
            positionActionImages(train);

        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    BaggageTrainInfoPanel.this.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };
            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + baggageTrain.getNationId() + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(baggageTrain.getNationId()) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            add(relationPanel, 246, 63);
        }

        setupGoodsPanel();
        initValues();
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(BAGGAGETRAIN, baggageTrain.getId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    /**
     * remove all action buttons from baggage train.
     */
    public void clearActionImages() {
        remove(viewImage);
        remove(moveImage);
        remove(tradeImage);
        remove(unloadTroopsImg);
        remove(loadImg);
        remove(repairImg);
        remove(scuttleBTrain);
    }

    /**
     * place all action images.
     *
     * @param bTrain the baggage train to display.
     */
    private void positionActionImages(final BaggageTrainDTO bTrain) {

        if (!RegionStore.getInstance().isInitialized()
                || !BarrackStore.getInstance().isInitialized()) {
            return;
        }


        //clear all first...
        try {
            clearActionImages();
        } catch (Exception ignore) {
            // do nothing
        }
        //then place them again..
        final SectorDTO[][] regionDTO = RegionStore.getInstance().getRegionSectorsByRegionId(bTrain.getRegionId());

        if (regionDTO.length < bTrain.getX() || regionDTO[bTrain.getX()].length < bTrain.getY()) {
            return;
        }

        final SectorDTO sector = regionDTO[bTrain.getX()][bTrain.getY()];

        if (sector == null) {
            return;
        }

        final BarrackDTO barrack = BarrackStore.getInstance().
                getBarrackByPosition(sector);

        final int cond = bTrain.getCondition();
        final List<BaggageTrainDTO> bTrainsList = BaggageTrainStore.getInstance().getBaggageTrainsBySector(sector, true);

        final boolean canTrade = (barrack != null || !bTrainsList.isEmpty());

        boolean hasUnits = false;
        if (bTrain.getLoadedUnitsMap() != null) {
            for (List<Integer> units : bTrain.getLoadedUnitsMap().values()) {
                if (!units.isEmpty()) {
                    hasUnits = true;
                    break;
                }
            }
        }

        int posX = 338;
        final int posY = 63;
        add(viewImage, posX, posY);
        if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
            posX -= 23;
            add(moveImage, posX, posY);

            if (canTrade && tradeImage != null) {
                posX -= 23;
                add(tradeImage, posX, posY);
            }

            if (hasUnits && unloadTroopsImg != null) {
                posX -= 23;
                add(unloadTroopsImg, posX, posY);
            }

            posX -= 23;
            add(loadImg, posX, posY);

            if (cond < 100 && repairImg != null) {
                posX -= 23;
                add(repairImg, posX, posY);
            }

            if (scuttleBTrain != null) {
                posX -= 23;
                add(scuttleBTrain, posX, posY);
            }
        }

    }

    /**
     * Setup images for the goods loaded on this baggage train.
     */
    private void setupGoodsPanel() {
        lblBaggageTrainName.setText(baggageTrain.getName());
        //update available capacity
        goodsPanel.clear();
        if (GameStore.getInstance().getNationId() == baggageTrain.getNationId()) {
            lbMovementPoints.setText(baggageTrain.getMps() + " MPs");
        }
        lblAvailable.setText(numberFormat.format((TradeStore.getInstance().getTradeUnitWeight(baggageTrain) - TradeStore.getInstance().getTradeUnitLoad(baggageTrain))) + " tons");
        //goods
        int posX = 0;
        int posY = 0;
        int countGoods = 0;
        for (int index = 0; index < 14; index++) {
            final Image goodImage;
            if (baggageTrain.getGoodsDTO().get(index + 1).getQte() > 0) {

                goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + (index + 1) + ".png");
                goodsPanel.add(goodImage, posX, posY);
                goodImage.setSize(SIZE_15PX, SIZE_15PX);

                if (baggageTrain.getGoodsDTO().get(index + 1) == null) {
                    goodImage.setTitle("null");
                } else {
                    goodImage.setTitle(baggageTrain.getGoodsDTO().get(index + 1).getGoodDTO().getName() + ":" + baggageTrain.getGoodsDTO().get(index + 1).getQte());
                }
                if ((countGoods + 1) % 2 == 0 && countGoods != 0) {
                    posX += 17;
                    posY = 0;
                } else {
                    posY += 17;
                }
                countGoods++;
            }
        }

        if (baggageTrain.getLoadedUnitsMap() != null) {

            if (baggageTrain.getLoadedUnitsMap().containsKey(BRIGADE)
                    && !baggageTrain.getLoadedUnitsMap().get(BRIGADE).isEmpty()) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png");
                final List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();
                for (Integer brigId : baggageTrain.getLoadedUnitsMap().get(BRIGADE)) {
                    brigades.add(ArmyStore.getInstance().getBrigadeById(brigId));
                }
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        new BrigadesViewerPopup(brigades, "Loaded brigades").open();
                    }
                }).addToElement(unitImage.getElement()).register();

                unitImage.setSize(SIZE_15PX, SIZE_15PX);
                goodsPanel.add(unitImage, posX, posY);
                if ((countGoods + 1) % 2 == 0 && countGoods != 0) {
                    posX += 17;
                    posY = 0;
                } else {
                    posY += 17;
                }
                countGoods++;
            }

            if (baggageTrain.getLoadedUnitsMap().containsKey(COMMANDER)
                    && !baggageTrain.getLoadedUnitsMap().get(COMMANDER).isEmpty()) {

                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png");
                final List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
                for (Integer commId : baggageTrain.getLoadedUnitsMap().get(COMMANDER)) {
                    commanders.add(CommanderStore.getInstance().getCommanderById(commId));
                }
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        new CommandersViewerPopup(commanders, "Loaded commanders").open();
                    }
                }).addToElement(unitImage.getElement()).register();

                unitImage.setSize(SIZE_15PX, SIZE_15PX);
                goodsPanel.add(unitImage, posX, posY);
                if ((countGoods + 1) % 2 == 0 && countGoods != 0) {
                    posX += 17;
                    posY = 0;
                } else {
                    posY += 17;
                }
                countGoods++;
            }

            if (baggageTrain.getLoadedUnitsMap().containsKey(SPY)
                    && !baggageTrain.getLoadedUnitsMap().get(SPY).isEmpty()) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png");

                final List<SpyDTO> spies = new ArrayList<SpyDTO>();
                for (Integer spyId : baggageTrain.getLoadedUnitsMap().get(SPY)) {
                    spies.add(SpyStore.getInstance().getSpyById(spyId));
                }

                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        new SpiesViewerPopup(spies, "Loaded spies").open();
                    }
                }).addToElement(unitImage.getElement()).register();


                unitImage.setSize(SIZE_15PX, SIZE_15PX);
                goodsPanel.add(unitImage, posX, posY);
            }

        }
    }


    private void initValues() {
        lblBaggageTrainName.setText(baggageTrain.getName());
//        lblMoneyvalue.setText(String.valueOf(baggageTrain.getGoodsDTO().get(GOOD_MONEY).getQte()));
//        final int good1QTE = baggageTrain.getGoodsDTO().get(GOOD_MONEY).getQte();
//        if (getBaggageTrain().getGood1().getQte() > 0) {
//            good1Img.setUrl("http://static.eaw1805.com/images/goods/" + getBaggageTrain().getGood1().getGoodDTO().getGoodId() + ".png");
//            good1Img.setTitle(getBaggageTrain().getGood1().getGoodDTO().getName());
//
//        }
//        lblGood1qte.setText(String.valueOf(getBaggageTrain().getGood1().getQte()));
//        if (getBaggageTrain().getGood2().getQte() > 0) {
//            good2Img.setUrl("http://static.eaw1805.com/images/goods/" + getBaggageTrain().getGood2().getGoodDTO().getGoodId() + ".png");
//            good2Img.setTitle(getBaggageTrain().getGood2().getGoodDTO().getName());
//
//        }
//        lblGood2qte_1.setText(String.valueOf(getBaggageTrain().getGood2().getQte()));
    }

    public void closeTradePanel() {
        tradeImage.deselect();
    }

    public BaggageTrainDTO getBaggageTrain() {
        return baggageTrain;
    }

    public BaggageTrainDTO getValue() {
        return getBaggageTrain();
    }

    public int getIdentifier() {
        return BAGGAGETRAIN;
    }

    public Widget getWidget() {
        return this;
    }

    public void setSelected(final boolean selected) {
        if (selected) {
            setStyleName("infoPanelSelected");
        } else {
            try {
                this.setStyleName("baggageInfoPanel");
            } catch (Exception ignore) {
            }
        }
    }

    public void onEnter() {
        // do nothing
    }
}
