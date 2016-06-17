package com.eaw1805.www.client.views.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.asyncs.SaveChangesAsyncCallback;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.events.map.MapEventManager;
import com.eaw1805.www.client.events.map.RegionChangedEvent;
import com.eaw1805.www.client.events.map.RegionChangedHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitCreatedEvent;
import com.eaw1805.www.client.events.units.UnitCreatedHandler;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.events.units.UnitSelectedEvent;
import com.eaw1805.www.client.events.units.UnitSelectedHandler;
import com.eaw1805.www.client.events.units.UnitUndoSelectedEvent;
import com.eaw1805.www.client.events.units.UnitUndoSelectedHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.views.economy.orders.OrdersView;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignBaggageTrainInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignBrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignCommanderInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignCorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignFleetInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignShipInfoPanel;
import com.eaw1805.www.client.views.infopanels.foreignUnits.ForeignSpyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BaggageTrainInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CommanderInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.FleetInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ShipInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.SpyInfoPanel;
import com.eaw1805.www.client.views.popups.menus.ArmyMenu;
import com.eaw1805.www.client.views.popups.menus.BaggageTrainMenu;
import com.eaw1805.www.client.views.popups.menus.BrigadeMenu;
import com.eaw1805.www.client.views.popups.menus.CommanderMenu;
import com.eaw1805.www.client.views.popups.menus.CorpsMenu;
import com.eaw1805.www.client.views.popups.menus.FleetMenu;
import com.eaw1805.www.client.views.popups.menus.ShipMenu;
import com.eaw1805.www.client.views.popups.menus.SpyMenu;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.InfoPanelsStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.ArrayList;

public class SaveView
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {

    private final ImageButton armiesImg, fleetsImg, commandersImg, spiesImg,
            baggageImg;
    private final AbsolutePanel unitsBasePanel;
    private final ImageButton leftArrowImg, rightArrowImg;
    private int armyUnitIndex = 0, navyUnitIndex = 0, spyUnitIndex = 0,
            commUnitIndex = 0, btrainUnitIndex = 0, barrackIndex = 0;
    private int typeSel = ARMY;
    private final InfoPanelsStore panelStore = InfoPanelsStore.getInstance();
    private final ImageButton barrackImg;
    private static final int BARRACK = 50;
    private ImageButton ordersImg;

    private int gameId = -1, nationId = -1, turn = -1, scenarioId = -1;

    private final EmpireRpcServiceAsync eService = GWT.create(EmpireRpcService.class);



    private final OrderStore orStore = OrderStore.getInstance();

    private final PopupPanelEAW popup = new PopupPanelEAW();
    private final HorizontalPanel container = new HorizontalPanel();


    /**
     * Custom widget's called from this
     */
    private final OrdersView ordersView;
    final ImageButton saveImg;
    int saveMaxCounter = 13;
    int saveCounter;
    boolean saving = false;


    public SaveView() {
        popup.setAutoHideEnabled(true);
        popup.setWidget(container);
        popup.setStyleName("");
        ordersView = new OrdersView(ordersImg);

        container.setVerticalAlignment(HasAlignment.ALIGN_TOP);

        setStyleName("unitsPanel");
        setSize("412px", "165px");

        final AbsolutePanel unitSelectorsPanel = new AbsolutePanel();
        this.add(unitSelectorsPanel, 0, 32);
        unitSelectorsPanel.setSize("401px", "33px");
        this.fleetsImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButNaviesOff.png");
        this.fleetsImg.setTitle("Select Fleet or Ship");
        unitSelectorsPanel.add(this.fleetsImg, 53, 0);
        this.fleetsImg.setSize(SIZE_27PX, SIZE_27PX);
        this.fleetsImg.setStyleName(CLASS_POINTER);
        addUnitImgFunctionality(fleetsImg, FLEET);

        this.commandersImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png");
        this.commandersImg.setTitle("Select Commander");
        unitSelectorsPanel.add(this.commandersImg, 86, 0);
        this.commandersImg.setSize(SIZE_27PX, SIZE_27PX);
        addUnitImgFunctionality(commandersImg, COMMANDER);
        this.commandersImg.setStyleName(CLASS_POINTER);
        this.spiesImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png");
        this.spiesImg.setTitle("Select Spy");
        unitSelectorsPanel.add(this.spiesImg, 119, 0);
        this.spiesImg.setSize(SIZE_27PX, SIZE_27PX);
        addUnitImgFunctionality(spiesImg, SPY);
        this.spiesImg.setStyleName(CLASS_POINTER);
        this.baggageImg = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSuppliesOff.png");
        this.baggageImg.setTitle("Select Baggage Train");
        unitSelectorsPanel.add(this.baggageImg, 152, 0);
        this.baggageImg.setSize(SIZE_27PX, SIZE_27PX);
        addUnitImgFunctionality(baggageImg, BAGGAGETRAIN);
        this.baggageImg.setStyleName(CLASS_POINTER);

        this.barrackImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButBarracksOff.png");
        this.barrackImg.setTitle("Select Barrack");
        unitSelectorsPanel.add(this.barrackImg, 185, 0);
        this.barrackImg.setStyleName(CLASS_POINTER);
        addUnitImgFunctionality(barrackImg, BARRACK);
        this.barrackImg.setSize(SIZE_27PX, SIZE_27PX);
        this.armiesImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOn.png");
        this.armiesImg.setTitle("Select Army, Corps or Brigade");
        unitSelectorsPanel.add(this.armiesImg, 20, 0);
        this.armiesImg.setSize(SIZE_27PX, SIZE_27PX);
        this.armiesImg.setSelected(true);
        addUnitImgFunctionality(armiesImg, ARMY);
        this.armiesImg.setStyleName(CLASS_POINTER);

        this.ordersImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersOff.png");
        this.ordersImg.setTitle("Review Orders");
        unitSelectorsPanel.add(this.ordersImg, 325, 0);
        this.ordersImg.setSize(SIZE_29PX, SIZE_29PX);
        this.ordersImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you have no orders to review.", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you have no orders to review.", false);

                } else {
                    ordersView.refreshRows();
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(ordersView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(ordersView);
                }
            }
        }).addToElement(ordersImg.getElement()).register();

        saveImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSaveOrdersOff.png");
        unitSelectorsPanel.add(saveImg, 360, 0);
        saveImg.setSize(SIZE_29PX, SIZE_29PX);
        saveImg.setStyleName(CLASS_POINTER);
        if (TutorialStore.getInstance().isTutorialMode()) {
            saveImg.setTitle("End turn and proceed to next month");
        } else {
            saveImg.setTitle("Save Orders");
        }

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                saveGame(false);
            }
        }).addToElement(saveImg.getElement()).register();


        this.unitsBasePanel = new AbsolutePanel();
        add(this.unitsBasePanel, 20, 67);
        this.unitsBasePanel.setStyleName("noScrollBarsBlackEmbossed");
        this.unitsBasePanel.setSize("365px", "90px");

        this.leftArrowImg = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        add(this.leftArrowImg, 4, 67);
        this.leftArrowImg.setStyleName(CLASS_POINTER);
        this.leftArrowImg.setSize("14px", "89px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                leftArrowImg.deselect();
                navigate(typeSel, -1);
                leftArrowImg.setUrl(leftArrowImg.getUrl().replace("Off",
                        "Hover"));
                highLight();
            }
        }).addToElement(leftArrowImg.getElement()).register();


        this.rightArrowImg = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        add(this.rightArrowImg, 387, 67);
        this.rightArrowImg.setStyleName(CLASS_POINTER);
        this.rightArrowImg.setSize("14px", "90px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                rightArrowImg.deselect();
                navigate(typeSel, 1);
                rightArrowImg.setUrl(rightArrowImg.getUrl().replace("Off",
                        "Hover"));
                highLight();
            }
        }).addToElement(rightArrowImg.getElement()).register();


        UnitEventManager.addUnitSelectedHandler(new UnitSelectedHandler() {
            public void onUnitSelected(final UnitSelectedEvent event) {
                showSelectedTypeInfo(event.getInfoType(), event.getInfoId(), event.getNationId(), event.getSectorId());
                if (!ForeignUnitsStore.getInstance().isUnitForeign(event.getSectorId(), event.getInfoType(), event.getInfoId())) {
                    MovementStore.getInstance().highLightPath(MapStore.getInstance().getActiveRegion(), event.getInfoType(), event.getInfoId());
                    MapStore.getInstance().getAlliedUnitGroups().highLightPath(MapStore.getInstance().getActiveRegion(), event.getNationId(), event.getInfoType(), event.getInfoId());
                }
            }
        });

        UnitEventManager
                .addUnitUndoSelectedHandler(new UnitUndoSelectedHandler() {

                    public void onUnitUndoSelected(final UnitUndoSelectedEvent event) {
                        populatePanel(typeSel);
                        MovementStore.getInstance().highLightAllPaths();
                        MapStore.getInstance().getAlliedUnitGroups().highLightAllPaths();
                    }
                });
        UnitEventManager.addUnitCreatedHandler(new UnitCreatedHandler() {
            public void onUnitCreated(final UnitCreatedEvent event) {
                populatePanel(typeSel);

            }
        });

        UnitEventManager.addUnitDestroyedHandler(new UnitDestroyedHandler() {
            public void onUnitDestroyed(final UnitDestroyedEvent event) {
                populatePanel(typeSel);
            }
        });

        MapEventManager.addRegionChangedHandler(new RegionChangedHandler() {
            public void onRegionChanged(final RegionChangedEvent event) {
                populatePanel(typeSel);
            }
        });

        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if ((typeSel == ARMY && (event.getInfoType() == ARMY || event.getInfoType() == CORPS || event.getInfoType() == BRIGADE)) ||
                        (typeSel == FLEET && (event.getInfoType() == FLEET || event.getInfoType() == SHIP)) ||
                        (typeSel != ARMY && typeSel != FLEET && typeSel == event.getInfoType())) {
                    populatePanel(typeSel);
                }

            }
        });

    }

    public ImageButton getOrdersImg() {
        return ordersImg;
    }

    public ImageButton getSaveImg() {
        return saveImg;
    }

    public void saveGame(final boolean forceSave) {
        if (saving) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Please wait until the previous save is complete and try again.", false);
            return;
        }
        saving = true;
        saveCounter = 0;
        if (!forceSave && !TutorialStore.getInstance().checkCanSave()) {//if tutorial doesn't allow you to save then don't save
            saving = false;
            return;
        }

        initSessionVar();
        if (GameStore.getInstance().isNationDead()) {
            saving = false;
            new ErrorPopup(ErrorPopup.Level.ERROR, "Your empire is dead, no changes can be saved.", false);

        } else if (GameStore.getInstance().isGameEnded()) {
            saving = false;
            new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, no changes can be saved.", false);

        } else {
            eService.getGameStatus(scenarioId, gameId, new AsyncCallback<Boolean>() {
                public void onFailure(final Throwable caught) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your orders were NOT saved! it appears that you are offline. Click here for the login screen to open, login again, and then try to save your orders.", false) {
                        public void onAccept() {
                            Window.open("/login", "_blank", "");
                        }
                    };
                    saving = false;
                }

                /*!!WHEN ADDING SAVEASYNC CALLS TO SERVER DON'T FORGET TO UPDATE THE VALUE OF THE saveMaxCounter TO THE NUMBER OF THE ASYNC REQUESTS!!*/
                public void onSuccess(final Boolean result) {
                    if (result) {
                        new ErrorPopup(ErrorPopup.Level.ERROR, "Game in process, cannot save now", false);
                        GameEventManager.reportInProcess(true);
                        saving = false;

                    } else {
                        SaveProgressPanel.getInstance().reset();
                        SaveProgressPanel.getInstance().center();
                        try {
                            eService.saveUnitChanges(scenarioId, new ArrayList<ArmyDTO>(ArmyStore.getInstance().getcArmiesList()),
                                    ArmyStore.getInstance().getBarrBrigMap(),
                                    orStore.getClientOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 0));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(0);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving production army changes.", false);
                        }

                        try {
                            eService.saveCommanderChanges(scenarioId, CommanderStore.getInstance().getCommandersList(),
                                    orStore.getClientOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 1));
                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(1);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving commander changes.", false);
                        }

                        try {
                            eService.saveMovementChanges(scenarioId, MovementStore.getInstance().getMvMap(), orStore.getClientOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 2));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(2);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving movement changes.", false);
                        }

                        try {
                            eService.saveTransportChanges(scenarioId, OrderStore.getInstance().getCargoRelatedOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 3));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(3);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving transportation changes.", false);
                        }

                        try {
                            eService.saveNavyChanges(scenarioId, NavyStore.getInstance().getIdFleetMap(), NavyStore.getInstance().getBarrShipMap(),
                                    orStore.getClientOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 4));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(4);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving navy changes.", false);
                        }

                        try {
                            eService.saveEconomyChanges(scenarioId, ProductionSiteStore.getInstance().getSectorProdSites(),
                                    orStore.getClientOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 5));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(5);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving production sites changes.", false);
                        }

                        try {
                            eService.saveRelationsChanges(scenarioId, RelationsStore.getInstance().getRelationsList(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 6));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(6);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving relations changes.", false);
                        }

                        try {
                            eService.saveRegionChanges(scenarioId, RegionStore.getInstance().getSectorOrderMap(),
                                    orStore.getClientOrders(),
                                    nationId, gameId, turn, new SaveChangesAsyncCallback(true, 7));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(7);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving pops inc/dec and handover changes.", false);
                        }

                        try {
                            eService.saveTaxationChanges(scenarioId, ProductionSiteStore.getInstance().getTax(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 8));
                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(8);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving pops inc/dec and handover changes.", false);
                        }

                        try {
                            eService.saveTradeChanges(scenarioId, OrderStore.getInstance().getCargoRelatedOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 9));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(9);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving trade changes.", false);
                        }

                        try {
                            eService.saveBaggageTrainChanges(scenarioId, BaggageTrainStore.getInstance().getBaggageTList(), orStore.getClientOrders(), BaggageTrainStore.getInstance().getNewBaggageTMap(),
                                    nationId, gameId, turn, new SaveChangesAsyncCallback(true, 10));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(10);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving trade changes.", false);
                        }

                        try {
                            eService.saveBarrackChanges(scenarioId, BarrackStore.getInstance().getBarracksMap(), orStore.getClientOrders(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 11));

                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(11);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving barrack changes.", false);
                        }

                        try {
                            // store current zoom level
                            if (GameStore.getInstance().getSettings().getZoom() > 0) {
                                GameStore.getInstance().getSettings().setZoom(MapStore.getInstance().getZoomLevelSettings());
                            }
                            eService.saveGameSettingsChanges(scenarioId, GameStore.getInstance().getSettings(), nationId, gameId, turn, new SaveChangesAsyncCallback(true, 12));
                        } catch (Exception e) {
                            increaseSaveCounter();
                            SaveProgressPanel.getInstance().setNoOk(12);
                            new ErrorPopup(ErrorPopup.Level.WARNING, "There was an error saving settings", false);
                        }
//                        new ErrorPopup(ErrorPopup.Level.NORMAL, "Save concluded successfully.", false) {
//                            public void onAccept() {
//                                if (TutorialStore.getInstance().isTutorialMode()
//                                        && TutorialStore.getInstance().getMonth() == 10
//                                        && TutorialStore.getInstance().getTutorialStep() == 18) {
//                                    TutorialStore.getInstance().setSaveOrdersDone(true);
//
//                                }
//                                if (TutorialStore.getInstance().isTutorialMode()) {
//                                    Window.Location.replace("/processing/scenario/" + GameStore.getInstance().getScenarioStr() + "/game/" + GameStore.getInstance().getGameId() + "/turn/" + GameStore.getInstance().getTurn());
//                                }
//
//                            }
//                        };

                    }
                }
            });
        }
    }

    public void increaseSaveCounter() {
        saveCounter++;
        if (saveCounter == saveMaxCounter) {
            saving = false;
        }
    }

    private void showSelectedTypeInfo(final int infoType, final int infoId, final int nationId, final int sectorId) {
        try {
            if (unitsBasePanel.getWidgetCount() > 0) {
                unitsBasePanel.clear();
            }
        } catch (Exception e) {
            //new ErrorPopup(ErrorPopup.Level.NORMAL,"Unknown error");
        }
        boolean isForeign = false;
        switch (infoType) {
            case ARMY:
                ArmyDTO army;
                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    army = ForeignUnitsStore.getInstance().getArmyBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    army = ArmyStore.getInstance().getcArmies().get(infoId);
                } else {
                    army = AlliedUnitsStore.getInstance().getAlliedArmyById(infoId);
                }
                if (army != null) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignArmyInfoPanel(army), -1, 0);
                    } else {
                        unitsBasePanel.add(new ArmyInfoPanel(army), -1, 0);
                    }
                }
                break;

            case CORPS:
                CorpDTO corp;

                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    corp = ForeignUnitsStore.getInstance().getCorpBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    corp = ArmyStore.getInstance().getCorpByID(infoId);
                } else {
                    corp = AlliedUnitsStore.getInstance().getCorpByID(infoId);
                }
                if (corp != null) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignCorpsInfoPanel(corp), -1, 0);
                    } else {
                        unitsBasePanel.add(new CorpsInfoPanel(corp, false), -1, 0);
                    }
                }
                break;

            case BRIGADE:
                BrigadeDTO brigade;
                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    brigade = ForeignUnitsStore.getInstance().getBrigadeBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (ArmyStore.getInstance().getNewBrigadeById(infoId) != null) {
                    brigade = ArmyStore.getInstance().getNewBrigadeById(infoId);
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    brigade = ArmyStore.getInstance().getBrigadeById(infoId);
                } else {
                    brigade = AlliedUnitsStore.getInstance().getBrigadeById(infoId);
                }
                if (brigade != null) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignBrigadeInfoPanel(brigade), -1, 0);
                    } else {
                        unitsBasePanel.add(new BrigadeInfoPanel(brigade, false), -1, 0);
                    }
                }
                break;

            case FLEET:
                FleetDTO fleet;
                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    fleet = ForeignUnitsStore.getInstance().getFleetBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    fleet = NavyStore.getInstance().getIdFleetMap()
                            .get(infoId);
                } else {
                    fleet = AlliedUnitsStore.getInstance().getFleetById(infoId);
                }
                if (fleet != null) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignFleetInfoPanel(fleet), -1, 0);
                    } else {
                        unitsBasePanel.add(new FleetInfoPanel(fleet, false), -1, 0);
                    }
                }
                break;

            case SHIP:
                try {
                    final ShipDTO ship;
                    if (nationId == GameStore.getInstance().getNationId()) {
                        if (infoId < 0) {//that means it is a new ship
                            ship = NavyStore.getInstance().getNewShip(infoId, sectorId);
                        } else {//that means it is an old ship
                            ship = NavyStore.getInstance().getShipById(infoId);
                        }
                    } else if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                        ship = ForeignUnitsStore.getInstance().getShipBySectorAndID(sectorId, infoId);
                        isForeign = true;
                    } else {
                        ship = AlliedUnitsStore.getInstance().getShipById(infoId);
                    }
                    if (ship != null) {
                        if (isForeign) {
                            unitsBasePanel.add(new ForeignShipInfoPanel(ship), -1, 0);
                        } else {
                            unitsBasePanel.add(new ShipInfoPanel(ship, false), -1, 0);
                        }
                    }
                } catch (Exception e) {
//                    Window.alert("could not : " + e.toString());
                }
                break;

            case BAGGAGETRAIN:
                BaggageTrainDTO btrain;
                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    btrain = ForeignUnitsStore.getInstance().getBaggageTrainBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    btrain = BaggageTrainStore.getInstance()
                            .getBaggageTMap().get(infoId);
                } else {
                    btrain = AlliedUnitsStore.getInstance().getBaggageTrainById(infoId);
                }
                if (btrain != null && !btrain.isScuttle()) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignBaggageTrainInfoPanel(btrain), -1, 0);
                    } else {
                        unitsBasePanel.add(new BaggageTrainInfoPanel(btrain), -1, 0);
                    }
                }
                break;

            case SPY:
                SpyDTO spy;
                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    spy = ForeignUnitsStore.getInstance().getSpyBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    spy = SpyStore.getInstance().getSpyById(infoId);
                } else {
                    spy = AlliedUnitsStore.getInstance().getSpyById(infoId);
                }
                if (spy != null) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignSpyInfoPanel(spy), -1, 0);
                    } else {
                        unitsBasePanel.add(new SpyInfoPanel(spy), -1, 0);
                    }
                }
                break;

            case COMMANDER:
                CommanderDTO commander;
                if (ForeignUnitsStore.getInstance().isUnitForeign(sectorId, infoType, infoId)) {
                    commander = ForeignUnitsStore.getInstance().getCommanderBySectorAndId(sectorId, infoId);
                    isForeign = true;
                } else if (nationId == GameStore.getInstance().getNationId()) {
                    commander = CommanderStore.getInstance().getCommandersMap().get(infoId);
                } else {
                    commander = AlliedUnitsStore.getInstance().getCommanderById(infoId);
                }
                if (commander != null) {
                    if (isForeign) {
                        unitsBasePanel.add(new ForeignCommanderInfoPanel(commander), -1, 0);
                    } else {
                        unitsBasePanel.add(new CommanderInfoPanel(commander), -1, 0);
                    }
                }
                break;

            default:
                break;
        }
    }

    public final void populatePanel(final int type) {
        final int regionId = MapStore.getInstance().getActiveRegion();
        try {
            if (unitsBasePanel != null && unitsBasePanel.getWidgetCount() > 0) {
                unitsBasePanel.getWidget(0).removeFromParent();
            }
        } catch (Exception ignore) {
        }

        switch (type) {
            case ARMY:
                if (!panelStore.getMilitaryUnitPanelsByRegion(regionId).isEmpty()) {
                    while (panelStore.getMilitaryUnitPanelsByRegion(regionId).size() < armyUnitIndex + 1) {
                        armyUnitIndex--;
                    }
                    unitsBasePanel.add(panelStore
                            .getMilitaryUnitPanelsByRegion(regionId).get(armyUnitIndex), -1, 0);
                }
                break;
            case FLEET:
                if (!panelStore.getNavalUnitPanelsByRegionId(regionId).isEmpty()) {
                    while (panelStore.getNavalUnitPanelsByRegionId(regionId).size() < navyUnitIndex + 1) {
                        navyUnitIndex--;
                    }
                    unitsBasePanel.add(panelStore
                            .getNavalUnitPanelsByRegionId(regionId).get(navyUnitIndex), -1, 0);
                }
                break;
            case BAGGAGETRAIN:
                if (!panelStore.getBtrainInfoPanelsByRegion(regionId).isEmpty()) {
                    while (panelStore.getBtrainInfoPanelsByRegion(regionId).size() < btrainUnitIndex + 1) {
                        btrainUnitIndex--;
                    }
                    unitsBasePanel.add(panelStore
                            .getBtrainInfoPanelsByRegion(regionId).get(btrainUnitIndex), -1, 0);
                }
                break;
            case SPY:
                if (!panelStore.getSpyInfoPanelsByRegion(regionId).isEmpty()) {
                    while (panelStore.getSpyInfoPanelsByRegion(regionId).size() < spyUnitIndex + 1) {
                        spyUnitIndex--;
                    }
                    unitsBasePanel.add(panelStore
                            .getSpyInfoPanelsByRegion(regionId).get(spyUnitIndex), -1, 0);
                }
                break;
            case COMMANDER:
                if (!panelStore.getAllCommanderInfoPanelsByRegion(regionId).isEmpty()) {
                    while (panelStore.getAllCommanderInfoPanelsByRegion(regionId).size() < commUnitIndex + 1) {
                        commUnitIndex--;
                    }
                    unitsBasePanel.add(panelStore
                            .getAllCommanderInfoPanelsByRegion(regionId).get(commUnitIndex), -1, 0);
                }
                break;
            case BARRACK:
                if (!panelStore.getBarrackInfoPanelsByRegion(regionId).isEmpty()) {
                    while (panelStore.getBarrackInfoPanelsByRegion(regionId).size() < barrackIndex + 1) {
                        barrackIndex--;
                    }
                    unitsBasePanel.add(panelStore
                            .getBarrackInfoPanelsByRegion(regionId).get(barrackIndex), -1, 0);
                }
                break;
            default:
                break;
        }

    }

    private void navigate(final int typeSel, final int i) {
        final int regionId = MapStore.getInstance().getActiveRegion();
        switch (typeSel) {
            case ARMY:
                if ((armyUnitIndex > 0 && i == -1) || (armyUnitIndex < panelStore.getMilitaryUnitPanelsByRegion(regionId).size() - 1 && i == 1)) {
                    armyUnitIndex += i;
                }
                break;
            case FLEET:
                if ((navyUnitIndex > 0 && i == -1) || (navyUnitIndex < panelStore.getNavalUnitPanelsByRegionId(regionId).size() - 1 && i == 1)) {
                    navyUnitIndex += i;
                }
                break;
            case BAGGAGETRAIN:
                if ((btrainUnitIndex > 0 && i == -1) || (btrainUnitIndex < panelStore.getBtrainInfoPanelsByRegion(regionId).size() - 1 && i == 1)) {
                    btrainUnitIndex += i;
                }
                break;
            case SPY:
                if ((spyUnitIndex > 0 && i == -1) || (spyUnitIndex < panelStore.getSpyInfoPanelsByRegion(regionId).size() - 1 && i == 1)) {
                    spyUnitIndex += i;
                }
                break;
            case COMMANDER:
                if ((commUnitIndex > 0 && i == -1) || (commUnitIndex < panelStore.getAllCommanderInfoPanelsByRegion(regionId).size() - 1 && i == 1)) {
                    commUnitIndex += i;
                }
                break;
            case BARRACK:
                if ((barrackIndex > 0 && i == -1) || (barrackIndex < panelStore.getBarrackInfoPanelsByRegion(regionId).size() - 1 && i == 1)) {
                    barrackIndex += i;
                }
                break;
            default:
                break;
        }
        populatePanel(typeSel);
    }

    private void addUnitImgFunctionality(final ImageButton tgImg, final int type) {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectUnitTypes(type);
                typeSel = type;
                populatePanel(type);
                event.stopPropagation();
            }
        }).addToElement(tgImg.getElement()).register();
    }

    private void selectUnitTypes(final int type) {
        armiesImg.deselect();
        fleetsImg.deselect();
        spiesImg.deselect();
        commandersImg.deselect();
        baggageImg.deselect();
        barrackImg.deselect();
        switch (type) {
            case ARMY:
                armiesImg.setSelected(true);
                break;

            case FLEET:
                fleetsImg.setSelected(true);
                break;

            case BAGGAGETRAIN:
                baggageImg.setSelected(true);
                break;

            case SPY:
                spiesImg.setSelected(true);
                break;

            case COMMANDER:
                commandersImg.setSelected(true);
                break;

            case BARRACK:
                barrackImg.setSelected(true);
                break;

            default:
                break;
        }

    }


    private void initSessionVar() {
        if (gameId == -1) {
            gameId = GameStore.getInstance().getGameId();
            nationId = GameStore.getInstance().getNationId();
            turn = GameStore.getInstance().getTurn();
            scenarioId = GameStore.getInstance().getScenarioId();
        }
    }

    /**
     * This functions navigates the user in the position of the selected unit
     * and opens the menu for this unit.
     */
    private void highLight() {

        final SelectableWidget selWidget = (SelectableWidget) unitsBasePanel.getWidget(0);
        container.clear();
        switch (selWidget.getIdentifier()) {

            case ARMY:
                if (((ArmyDTO) selWidget.getValue()).getArmyId() != 0) {
                    MapStore.getInstance().getMapsView().goToPosition((ArmyDTO) selWidget.getValue());
                    final ArmyMenu armyMenu = new ArmyMenu((ArmyDTO) selWidget.getValue(), popup);
                    container.add(armyMenu);
                    displacePopup((ArmyDTO) selWidget.getValue(), 73, 80);
                }
                break;
            case CORPS:
                if (((CorpDTO) selWidget.getValue()).getCorpId() != 0) {
                    MapStore.getInstance().getMapsView().goToPosition((CorpDTO) selWidget.getValue());
                    final CorpsMenu corpsMenu = new CorpsMenu((CorpDTO) selWidget.getValue(), popup);
                    container.add(corpsMenu);
                    displacePopup((CorpDTO) selWidget.getValue(), 73, 80);
                }
                break;
            case BRIGADE:
                MapStore.getInstance().getMapsView().goToPosition((BrigadeDTO) selWidget.getValue());
                final BrigadeMenu brigadeMenu = new BrigadeMenu((BrigadeDTO) selWidget.getValue(), popup);
                container.add(brigadeMenu);
                displacePopup((BrigadeDTO) selWidget.getValue(), 73, 80);
                break;
            case BAGGAGETRAIN:
                MapStore.getInstance().getMapsView().goToPosition((BaggageTrainDTO) selWidget.getValue());
                final BaggageTrainMenu baggageTrainMenu = new BaggageTrainMenu((BaggageTrainDTO) selWidget.getValue(), popup);
                container.add(baggageTrainMenu);
                displacePopup((BaggageTrainDTO) selWidget.getValue(), 113, 73);
                break;
            case BARRACK:
                MapStore.getInstance().getMapsView().goToPosition((BarrackDTO) selWidget.getValue());
//                        BaggageTrainMenu baggageTrainMenu = new BaggageTrainMenu((BarrackDTO) selWidget.getValue(), popup);
//                        container.add(baggageTrainMenu);
//                        displacePopup((BaggageTrainDTO) selWidget.getValue(), 113, 73);
                break;
            case COMMANDER:
                final CommanderDTO comm = (CommanderDTO) selWidget.getValue();
                if (!comm.getDead() && !comm.isCaptured() && !comm.getInPool() && !comm.getInTransit()) {
                    MapStore.getInstance().getMapsView().goToPosition((CommanderDTO) selWidget.getValue());
                    final CommanderMenu commanderMenu = new CommanderMenu((CommanderDTO) selWidget.getValue(), popup);
                    container.add(commanderMenu);
                    displacePopup((CommanderDTO) selWidget.getValue(), 73, 80);
                }
                break;
            case SPY:
                MapStore.getInstance().getMapsView().goToPosition((SpyDTO) selWidget.getValue());
                final SpyMenu spyMenu = new SpyMenu((SpyDTO) selWidget.getValue(), popup);
                container.add(spyMenu);
                displacePopup((SpyDTO) selWidget.getValue(), 89, 73);
                break;
            case FLEET:
                if (((FleetDTO) selWidget.getValue()).getFleetId() != 0) {
                    MapStore.getInstance().getMapsView().goToPosition((FleetDTO) selWidget.getValue());
                    final FleetMenu fleetMenu = new FleetMenu((FleetDTO) selWidget.getValue(), popup);
                    container.add(fleetMenu);
                    displacePopup((FleetDTO) selWidget.getValue(), 87, 73);
                }
                break;
            case SHIP:
                MapStore.getInstance().getMapsView().goToPosition((ShipDTO) selWidget.getValue());
                final ShipMenu shipMenu = new ShipMenu((ShipDTO) selWidget.getValue(), popup);
                container.add(shipMenu);
                if (NavyStore.getInstance().isTradeShip((ShipDTO) selWidget.getValue())) {
                    displacePopup((ShipDTO) selWidget.getValue(), 67, 73);
                } else {
                    displacePopup((ShipDTO) selWidget.getValue(), 87, 73);
                }
                break;

        }
        popup.show();

    }


    private void displacePopup(final PositionDTO pos, final int offsetX, final int offsetY) {
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        popup.setPopupPosition(MapStore.getInstance().getZoomedPointX(pos.getX()) + (int) MapStore.getInstance().getZoomOffsetX() - position.getX() - offsetX,
                MapStore.getInstance().getZoomedPointY(pos.getY()) + (int) MapStore.getInstance().getZoomOffsetY() - position.getY() - offsetY);
    }


}
