package com.eaw1805.www.client.views;


import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.www.client.events.trade.GetGoodEvent;
import com.eaw1805.www.client.events.trade.GetGoodHandler;
import com.eaw1805.www.client.events.trade.GiveGoodEvent;
import com.eaw1805.www.client.events.trade.GiveGoodHandler;
import com.eaw1805.www.client.events.trade.TradeEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.economy.trade.DoTradeView;
import com.eaw1805.www.client.views.economy.trade.StartTradeView;
import com.eaw1805.www.client.views.economy.trade.TradeCitiesView;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.views.layout.EconomyView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.map.MapStore;

public class TradePanelView extends DraggablePanel implements ArmyConstants, RegionConstants, StyleConstants {

    private final AbsolutePanel viewsPanel;
    private final ImageButton tradeCitiesImg, bTrainsImg;
    private final ImageButton mShipsImg;
    private final StyledCheckBox firstPhaseCheck, secondPhaseCheck;
    private final ImageButton europeImg;
    private final ImageButton africaImg;
    private final ImageButton caribImg;
    private final ImageButton indiaImg;

    private StartTradeView startTradePanel;
    private DoTradeView doTradePanel;
    private TradeCitiesView tradeCitiesPanel;
    private EconomyView ecoView;
    private int tradePhase = 1, regionId = 0, unitType = 0;
    private final ImageButton tradeCitiesOverImg;
    private TradeUnitAbstractDTO tdUnitNfo;

    public TradePanelView(final TradeUnitAbstractDTO tradeUnit,
                          final TradeInfoViewInterface parent,
                          final int type) {
        this.setStyleName("tradePanel");
        this.setSize("1218px", "653px");

        // Initialize the passed on variables
        this.setStyleName("tradePanel", true);
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        final TradePanelView self = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
                parent.closeTradePanel();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 15) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 1162, 47);

        imgX.setSize("35px", "35px");

        firstPhaseCheck = new StyledCheckBox("Trade & loading 1", true, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (firstPhaseCheck.isChecked()) {
                    secondPhaseCheck.setChecked(false);
                    setTradePhase(1);
                } else {
                    secondPhaseCheck.setChecked(true);
                    setTradePhase(2);
                }
                if (tdUnitNfo != null) {
                    selectRegion(tdUnitNfo.getRegionId());

                    startTradePanel = new StartTradeView(TradePanelView.this, tdUnitNfo.getUnitType(), tdUnitNfo.getId(), tdUnitNfo.getRegionId(), tradePhase);
                    viewsPanel.add(startTradePanel, 10, 0);

                    setViewInfo(tdUnitNfo.getRegionId());
                    setStartType(tdUnitNfo.getUnitType());
                }

            }
        }).addToElement(firstPhaseCheck.getCheckBox().getElement()).register();

        firstPhaseCheck.invertDirection(true);
        secondPhaseCheck = new StyledCheckBox("Trade & loading 2", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (secondPhaseCheck.isChecked()) {
                    firstPhaseCheck.setChecked(false);
                    setTradePhase(2);
                } else {
                    firstPhaseCheck.setChecked(true);
                    setTradePhase(1);
                }
                if (tdUnitNfo != null) {
                    selectRegion(tdUnitNfo.getRegionId());

                    startTradePanel = new StartTradeView(TradePanelView.this, tdUnitNfo.getUnitType(), tdUnitNfo.getId(), tdUnitNfo.getRegionId(), tradePhase);
                    viewsPanel.add(startTradePanel, 10, 0);

                    setViewInfo(tdUnitNfo.getRegionId());
                    setStartType(tdUnitNfo.getUnitType());
                }
            }
        }).addToElement(secondPhaseCheck.getCheckBox().getElement()).register();

        secondPhaseCheck.invertDirection(true);
        this.add(firstPhaseCheck, 94, 53);
        this.add(secondPhaseCheck, 916, 53);

        this.viewsPanel = new AbsolutePanel();
        this.add(this.viewsPanel, 0, 83);
        this.viewsPanel.setSize("1158px", "603px");

        this.tradeCitiesImg = new ImageButton("http://static.eaw1805.com/images/panels/trade/buttons/ButTradeCitiesOn.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (startTradePanel == null) {
                    startTradePanel = new StartTradeView(TradePanelView.this, TRADECITY, 0, MapStore.getInstance().getActiveRegion(), tradePhase);
                    viewsPanel.add(startTradePanel, 10, 0);
                }
                unitType = TRADECITY;
                startTradePanel.selectType(TRADECITY, true);
                selectTypeImg(tradeCitiesImg);
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 11) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(tradeCitiesImg.getElement()).register();

        this.tradeCitiesImg.setStyleName(CLASS_POINTER);
        this.add(this.tradeCitiesImg, 1164, 86);
        this.tradeCitiesImg.setSize("30px", "171px");

        this.bTrainsImg = new ImageButton("http://static.eaw1805.com/images/panels/trade/buttons/ButBaggageTrainsOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (startTradePanel == null) {
                    startTradePanel = new StartTradeView(TradePanelView.this, BAGGAGETRAIN, 0, MapStore.getInstance().getActiveRegion(), tradePhase);
                    viewsPanel.add(startTradePanel, 10, 0);
                }

                unitType = BAGGAGETRAIN;
                startTradePanel.selectType(BAGGAGETRAIN, true);
                selectTypeImg(bTrainsImg);
            }
        }).addToElement(bTrainsImg.getElement()).register();

        this.bTrainsImg.setStyleName(CLASS_POINTER);
        this.add(this.bTrainsImg, 1164, 275);
        this.bTrainsImg.setSize("30px", "171px");

        this.mShipsImg = new ImageButton("http://static.eaw1805.com/images/panels/trade/buttons/ButMerchantShipsOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (startTradePanel == null) {
                    startTradePanel = new StartTradeView(TradePanelView.this, SHIP, 0, MapStore.getInstance().getActiveRegion(), tradePhase);
                    viewsPanel.add(startTradePanel, 10, 0);
                }
                unitType = SHIP;
                startTradePanel.selectType(SHIP, true);
                selectTypeImg(mShipsImg);
            }
        }).addToElement(mShipsImg.getElement()).register();

        this.mShipsImg.setStyleName(CLASS_POINTER);
        this.add(this.mShipsImg, 1164, 469);
        this.mShipsImg.setSize("30px", "171px");

        this.europeImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png");
        this.europeImg.setId(EUROPE);
        this.europeImg.setTitle("Select european theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot perform trade.", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot perform trade.", false);

                } else {
                    selectRegion(EUROPE);
                }
            }
        }).addToElement(europeImg.getElement()).register();

        this.europeImg.setStyleName(CLASS_POINTER);
        this.add(this.europeImg, 482, 44);
        this.europeImg.setSize(SIZE_31PX, SIZE_31PX);

        this.africaImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png");
        this.africaImg.setId(AFRICA);
        this.africaImg.setTitle("Select African theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot perform trade", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot perform trade.", false);

                } else {
                    selectRegion(AFRICA);
                }
            }
        }).addToElement(africaImg.getElement()).register();

        this.africaImg.setStyleName(CLASS_POINTER);
        this.add(this.africaImg, 540, 44);
        this.africaImg.setSize(SIZE_31PX, SIZE_31PX);

        this.caribImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png");
        this.caribImg.setId(CARIBBEAN);
        this.caribImg.setTitle("Select Caribbean theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot perform trade", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot perform trade.", false);

                } else {
                    selectRegion(CARIBBEAN);
                }
            }
        }).addToElement(caribImg.getElement()).register();

        this.caribImg.setStyleName(CLASS_POINTER);
        this.add(this.caribImg, 598, 44);
        this.caribImg.setSize(SIZE_31PX, SIZE_31PX);

        this.indiaImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png");
        this.indiaImg.setId(INDIES);
        this.indiaImg.setTitle("Select Indies theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot perform trade", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot perform trade.", false);

                } else {
                    selectRegion(INDIES);
                }
            }
        }).addToElement(indiaImg.getElement()).register();

        this.indiaImg.setStyleName(CLASS_POINTER);
        this.add(this.indiaImg, 655, 44);
        this.indiaImg.setSize(SIZE_31PX, SIZE_31PX);

        this.tradeCitiesOverImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButTradeCityOff.png");
        this.tradeCitiesOverImg.setTitle("Browse trade cities");
        this.tradeCitiesOverImg.setStyleName(CLASS_POINTER);
        this.tradeCitiesOverImg.setId(TRADECITY);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectRegion(TRADECITY);
            }
        }).addToElement(tradeCitiesOverImg.getElement()).register();

        this.add(this.tradeCitiesOverImg, 713, 44);
        this.tradeCitiesOverImg.setSize(SIZE_31PX, SIZE_31PX);

        if (firstPhaseCheck.isChecked()) {
            tradePhase = 1;
        } else {
            tradePhase = 2;
        }

        if (tradeUnit == null) {
            // Go to the tradecities view
            selectRegion(TRADECITY);

        } else {
            selectRegion(tradeUnit.getRegionId());
            if (MovementStore.getInstance().hasMovedThisTurn(tradeUnit.getUnitType(), tradeUnit.getId())
                    || TradeStore.getInstance().hasInitSecondPhase(tradeUnit.getId())) {
                tradePhase = 2;
                secondPhaseCheck.setChecked(true);
                firstPhaseCheck.setChecked(false);
            }

            startTradePanel = new StartTradeView(this, type, tradeUnit.getId(), tradeUnit.getRegionId(), tradePhase);
            viewsPanel.add(startTradePanel, 10, 0);

            setViewInfo(tradeUnit.getRegionId());
            setStartType(type);
        }

        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 10
                && TutorialStore.getInstance().getTutorialStep() == 11) {
            TutorialStore.highLightButton(tradeCitiesImg);
        }

    }

    public void rememberSelectedUnit(final TradeUnitAbstractDTO tdUnitNfo) {
        this.tdUnitNfo = tdUnitNfo;
    }


    private void setTradePhase(final int index) {
        tradePhase = index;
        selectRegion(regionId);

    }

    private void selectRegion(final int viewId) {
        ImageButton img = null;
        switch (viewId) {
            case EUROPE:
                img = europeImg;
                break;

            case AFRICA:
                img = africaImg;
                break;

            case INDIES:
                img = indiaImg;
                break;

            case CARIBBEAN:
                img = caribImg;
                break;

            case TRADECITY:
                img = tradeCitiesOverImg;
                break;
            default:
                break;
        }

        selectRegionImg(img);
        if (startTradePanel != null && startTradePanel.isAttached()) {
            startTradePanel.removeFromParent();
        }

        if (tradeCitiesPanel != null && tradeCitiesPanel.isAttached()) {
            tradeCitiesPanel.removeFromParent();
        }

        if (doTradePanel != null && doTradePanel.isAttached()) {
            doTradePanel.removeFromParent();
        }

        if (viewId == TRADECITY) {
            tradeCitiesPanel = new TradeCitiesView();
            viewsPanel.add(tradeCitiesPanel, 10, 0);
            if (img != null) {
                setViewInfo(img.getId());
            }
        } else {
            startTradePanel = new StartTradeView(this, unitType, 0, viewId, tradePhase);
            viewsPanel.add(startTradePanel, 10, 0);
            if (img != null) {
                setViewInfo(img.getId());
            }
            setStartType(unitType);

        }
    }

    private void setStartType(final int type) {
        switch (type) {
            case BAGGAGETRAIN:
                bTrainsImg.setUrl(bTrainsImg.getUrl().replace(OFF, "On"));
                bTrainsImg.setSelected(true);
                break;

            case SHIP:
                mShipsImg.setUrl(mShipsImg.getUrl().replace(OFF, "On"));
                mShipsImg.setSelected(true);
                break;

            default:
                tradeCitiesImg.setUrl(tradeCitiesImg.getUrl().replace(OFF, "On"));
                tradeCitiesImg.setSelected(true);
                break;
        }

    }

    private void setViewInfo(final int regionId) {
        switch (regionId) {
            case 1:
                europeImg.setUrl(europeImg.getUrl().replace(OFF, "On"));
                europeImg.setSelected(true);
                break;

            case 2:
                caribImg.setUrl(caribImg.getUrl().replace(OFF, "On"));
                caribImg.setSelected(true);
                break;

            case 3:
                indiaImg.setUrl(indiaImg.getUrl().replace(OFF, "On"));
                indiaImg.setSelected(true);
                break;

            case 4:
                africaImg.setUrl(africaImg.getUrl().replace(OFF, "On"));
                africaImg.setSelected(true);
                break;

            default:
                tradeCitiesOverImg.setUrl(tradeCitiesOverImg.getUrl().replace(OFF, "On"));
                break;
        }

        if (regionId != TRADECITY) {
            this.regionId = regionId;
            setEcoView(regionId);
        }
    }

    private void setEcoView(final int regionId) {

        ecoView = new EconomyView(null, null, null);
        this.add(ecoView, 10, 7);
        ecoView.populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(regionId), true);
        TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {
            public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                if (getGoodEvent.getRegionId() == regionId) {
                    ecoView.populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(regionId), false);
                }
            }
        });

        TradeEventManager.addGiveGoodHanlder(new GiveGoodHandler() {
            public void onGiveGoodIn(final GiveGoodEvent giveGoodEvent) {
                if (giveGoodEvent.getRegionId() == regionId) {
                    ecoView.populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(regionId), false);
                }
            }
        });

    }

    private void selectRegionImg(final ImageButton img) {
        europeImg.deselect();
        europeImg.setUrl(europeImg.getUrl().replace("On", OFF));

        caribImg.deselect();
        caribImg.setUrl(caribImg.getUrl().replace("On", OFF));

        indiaImg.deselect();
        indiaImg.setUrl(indiaImg.getUrl().replace("On", OFF));

        africaImg.deselect();
        africaImg.setUrl(africaImg.getUrl().replace("On", OFF));

        tradeCitiesOverImg.deselect();
        tradeCitiesOverImg.setUrl(tradeCitiesOverImg.getUrl().replace("On", OFF));

        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
        setViewInfo(img.getId());
    }

    private void selectTypeImg(final ImageButton img) {
        if (!(europeImg.isSelected() || caribImg.isSelected() || africaImg.isSelected() || indiaImg.isSelected())) {
            selectRegion(MapStore.getInstance().getActiveRegion());
        }

        if (!startTradePanel.isAttached()) {
            viewsPanel.clear();
            viewsPanel.add(startTradePanel, 10, 0);
        }

        bTrainsImg.deselect();
        bTrainsImg.setUrl(bTrainsImg.getUrl().replace("On", OFF));
        mShipsImg.deselect();
        mShipsImg.setUrl(mShipsImg.getUrl().replace("On", OFF));
        tradeCitiesImg.deselect();
        tradeCitiesImg.setUrl(tradeCitiesImg.getUrl().replace("Slc", ""));
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }

    public void startTrading(final TradeUnitAbstractDTO tdUnit1,
                             final TradeUnitAbstractDTO tdUnit2,
                             final int tradePhase) {
        doTradePanel = new DoTradeView(tdUnit1, tdUnit2, tradePhase);
        viewsPanel.clear();
        viewsPanel.add(doTradePanel, 10, 0);
    }

}
