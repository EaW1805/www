package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
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
import com.eaw1805.www.client.views.infopanels.units.mini.ShipRepairCostMini;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CommandersViewerPopup;
import com.eaw1805.www.client.views.popups.SpiesViewerPopup;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.List;

public class ShipInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, TradeInfoViewInterface,
        RelationConstants, SelectableWidget<ShipDTO>, StyleConstants {

    private final static String TONS = " tons";

    private ShipDTO ship;
    private Label lblAvailable;
    private Image moveImg;
    private final ImageButton viewImg;
    private final ImageButton handOverImg;
    private ImageButton tradeImage;
    private final Image unloadTroopsImg;
    private ImageButton repairImg;
    private final ImageButton scuttleShips;
    private final ImageButton scuttled;
    private final ImageButton loadImg;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private final AbsolutePanel goodsPanel;
    private final ClickAbsolutePanel shipPanel;
    private final Label lblCondition;
    private final Label lblMarines;
    private final UnitChangedHandler unitChangedHandler;
    private final RenamingLabel lblName;

    public ShipInfoPanel(final ShipDTO ship, final boolean showMove) {
        setSize("366px", "90px");
        this.setShip(ship);

        shipPanel = new ClickAbsolutePanel();
        this.add(shipPanel);
        shipPanel.setStyleName("ShipInfoPanel");
        shipPanel.setStyleName("clickArmyPanel", true);
        shipPanel.setSize("363px", "87px");

        final Image shipTypeImg = new Image("http://static.eaw1805.com/images/ships/" + ship.getNationId() + "/" + ship.getType().getIntId() + ".png");
        shipTypeImg.setTitle(ship.getType().getName());
        shipPanel.add(shipTypeImg, 3, 3);
        shipTypeImg.setSize("", "82px");

        //main
        lblName = new RenamingLabel(ship.getName(), SHIP, ship.getId());
        lblName.setStyleName("clearFontMiniTitle");
        shipPanel.add(lblName, 90, 3);

        //right end
        final Label lblXy = new Label(getShip().positionToString());
        lblXy.setTitle("Ships position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        shipPanel.add(lblXy, 315, 3);
        lblXy.setSize("49px", "18px");

        final Label lblMp = new Label((int) (ship.getType().getMovementFactor() * ship.getCondition() / 100d) + " MPs");
        lblMp.setStyleName(CLASS_CLEARFONTSMALL);
        lblMp.setTitle("Movement points.");
        shipPanel.add(lblMp, 315, 20);
        lblMp.setSize("100px", "18px");

        //main
        goodsPanel = new AbsolutePanel();
        goodsPanel.setSize("150px", "35px");
        add(goodsPanel, 170, 30);
        lblCondition = new Label(ship.getCondition() + "%");
        lblMarines = new Label(numberFormat.format(ship.getMarines()) + " " + getMarinesExpStr() + " marines");
        if (NavyStore.getInstance().isTradeShip(getShip())) {
            if (ship.getId() > 0) {
                lblAvailable = new Label(numberFormat.format((TradeStore.getInstance().getTradeUnitWeight(ship) - TradeStore.getInstance().getTradeUnitLoad(ship)
                        - TransportStore.getInstance().getUnitsLoadedWeight(SHIP, ship.getId(), true))) + TONS);
            } else {
                lblAvailable = new Label(numberFormat.format(TradeStore.getInstance().getTradeUnitWeight(ship)) + TONS);
            }
            lblAvailable.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblAvailable, 90, 20);


            lblCondition.setTitle("Condition");
            lblCondition.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblCondition, 90, 34);

            final Label lblType = new Label(ship.getType().getName());
            lblType.setTitle("Type");
            lblType.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblType, 90, 65);

            TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {
                public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                    if (getGoodEvent.getUnitType() == SHIP
                            && (getGoodEvent.getUnitId() == ship.getId()
                            || getGoodEvent.getUnitId() == 0)
                            && getGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()
                            && ship.getId() > 0) {
                        setupGoodsPanel();
                    }
                }
            });

            TradeEventManager.addGiveGoodHanlder(new GiveGoodHandler() {
                public void onGiveGoodIn(final GiveGoodEvent giveGoodEvent) {
                    if (giveGoodEvent.getUnitType() == SHIP
                            && (giveGoodEvent.getUnitId() == ship.getId()
                            || giveGoodEvent.getUnitId() == 0)
                            && giveGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()
                            && ship.getId() > 0) {
                        setupGoodsPanel();
                    }
                }
            });


        } else {
            final Label lblClass = new Label(ship.getType().getName());
            lblClass.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblClass, 90, 20);

            lblMarines.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblMarines, 90, 34);
            lblMarines.setSize("122px", "18px");

            lblCondition.setTitle("Condition");
            lblCondition.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblCondition, 90, 48);

            final Label lblCapacity = new Label(numberFormat.format(ship.getType().getLoadCapacity()) + TONS);
            lblCapacity.setStyleName(CLASS_CLEARFONTSMALL);
            shipPanel.add(lblCapacity, 90, 62);
        }

        if (ship.getId() > 0) {
            setupGoodsPanel();
        }

        //bottom
        viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
        viewImg.setTitle("Go to ship position");

        viewImg.setSize(SIZE_20PX, SIZE_20PX);

        handOverImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButHandOverShipOff.png");
        handOverImg.setTitle("Hand over ship");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                final HandOverShipPanel hoShip = new HandOverShipPanel(getShip());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(hoShip);
                GameStore.getInstance().getLayoutView()
                        .setWidgetPosition(hoShip,
                                (Document.get().getClientWidth() - hoShip.getOffsetWidth()) / 2,
                                (Document.get().getClientHeight() - hoShip.getOffsetHeight()) / 2, false, true);
                handOverImg.deselect();
            }
        }).addToElement(handOverImg.getElement()).register();

        handOverImg.setSize(SIZE_20PX, SIZE_20PX);

        if (ship.gethOverNationId() != 0) {
            final Image hOverNationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + ship.gethOverNationId() + "-120.png");
            this.setTitle("This ship will be handed over to:" + DataStore.getInstance().getNameByNationId(ship.gethOverNationId()));
            shipPanel.add(hOverNationImg, 0, 0);
            hOverNationImg.setSize("41px", "34px");
        }
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                MapStore.getInstance().getMapsView().goToPosition(ship);
                viewImg.deselect();
            }
        }).addToElement(viewImg.getElement()).register();

        if (showMove) {
            if (getShip().getFleet() == 0) {
                moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                moveImg.setTitle("Click here to move the ship");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        MapStore.getInstance().getMapsView().goToPosition(ship);
                        MapStore.getInstance().getMapsView().addFigureOnMap(SHIP, getShip().getId(),
                                getShip(), (int) (getShip().getType().getMovementFactor() * getShip().getCondition() / 100d), 1, 0, 0);
                    }
                }).addToElement(moveImg.getElement()).register();
            } else {
                moveImg = new DualStateImage("http://static.eaw1805.com/images/buttons/moveNA.png");
                moveImg.setTitle("Cannot move a ship that is in a higher level of organization");
            }

            moveImg.setSize(SIZE_20PX, SIZE_20PX);
        }

        if (NavyStore.getInstance().isTradeShip(getShip())) {
            tradeImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
            final ShipInfoPanel mySelf = this;
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new TradePanelView(getShip(), mySelf, SHIP));
                }
            }).addToElement(tradeImage.getElement()).register();

            tradeImage.setTitle("Open trade overview");
            tradeImage.setStyleName(CLASS_POINTER);
            tradeImage.setSize(SIZE_20PX, SIZE_20PX);
        }

        loadImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        loadImg.setTitle("Board troops");
        loadImg.setSize(SIZE_20PX, SIZE_20PX);
        loadImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                final DeployTroopsView dpView = new DeployTroopsView(ship.getRegionId(), SHIP, ship.getId());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
            }
        }).addToElement(loadImg.getElement()).register();

        final SectorDTO startingSector = RegionStore.getInstance().getRegionSectorsByRegionId(ship.getRegionId())[ship.getXStart()][ship.getYStart()];
        if (startingSector.hasShipyardOrBarracks() && startingSector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()
                && (ship.getNationId() == GameStore.getInstance().getNationId()
                || RelationsStore.getInstance().getOriginalRelationByNationId(ship.getNationId()) == REL_ALLIANCE)) {
            repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
            repairImg.setTitle("Repair");
            repairImg.setSize(SIZE_20PX, SIZE_20PX);
            repairImg.setStyleName(CLASS_POINTER);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    final SectorDTO sectorDTO = MapStore.getInstance().getRegionSectorByRegionIdXY(ship.getRegionId(), ship.getX(), ship.getY());
                    NavyStore.getInstance().repairShip(ship.getId(), sectorDTO.getId());
                }
            }).addToElement(repairImg.getElement()).register();


            //add tooltip for more information about the repair
            new ToolTipPanel(repairImg) {
                @Override
                public void generateTip() {
                    setTooltip(new ShipRepairCostMini(ship));
                }
            };
        }

        unloadTroopsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDisembarkOff.png");
        unloadTroopsImg.setTitle("Unload troops");
        unloadTroopsImg.setSize(SIZE_20PX, SIZE_20PX);
        unloadTroopsImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new UnloadTroopsView(SHIP, ship));
            }
        }).addToElement(unloadTroopsImg.getElement()).register();

        scuttleShips = new ImageButton("http://static.eaw1805.com/images/buttons/ButScuttleShipOff.png");
        scuttleShips.setTitle("Scuttle Ship");
        scuttleShips.setStyleName(CLASS_POINTER);
        scuttleShips.setSize(SIZE_20PX, SIZE_20PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                NavyStore.getInstance().addScuttleOrder(ship.getId(), ship.getRegionId());
                scuttleShips.deselect();
            }
        }).addToElement(scuttleShips.getElement()).register();

        scuttled = new ImageButton("http://static.eaw1805.com/images/buttons/ButScuttleShipOff.png");
        scuttled.setTitle("Ship has been scuttled, click here to undo.");
        scuttled.setStyleName(CLASS_POINTER);
        scuttled.setSize("30px", "30px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                NavyStore.getInstance().undoScuttleOrder(ship.getId());
                scuttleShips.deselect();
                scuttled.deselect();
            }
        }).addToElement(scuttled.getElement()).register();


        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                if (GameStore.getInstance().getNationId() == ship.getNationId()
                        && ship.getId() > 0) {
                    try {
                        positionActionImages(ship);
                    } catch (Exception e) {
//                        Window.alert("ship sites loaded : " + e.toString());
                    }
                }

            }
        });

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if ((event.getInfoType() == SHIP && event.getInfoId() == ship.getId())
                        || (event.getInfoType() == FLEET && event.getInfoId() == ship.getFleet())) {

                    setupGoodsPanel();
                    if (GameStore.getInstance().getNationId() == ship.getNationId()
                            && ship.getId() > 0) {
                        try {
                            positionActionImages(ship);
                        } catch (Exception e) {
                            // do nothing
                        }
                    }
                }
            }
        };


        if (GameStore.getInstance().getNationId() == ship.getNationId()) {
            if (ship.getId() > 0) {
                positionActionImages(ship);
            }
        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    shipPanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };

            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + ship.getNationId() + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(ship.getNationId()) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            shipPanel.add(relationPanel, 246, 63);
        }
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(SHIP, ship.getId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public final String getMarinesExpStr() {

        switch (ship.getExp()) {
            case 1:
                return "Veteran";

            case 2:
                return "Elite";

            case 0:
            default:
                return "Regular";
        }
    }

    /**
     * remove all action buttons from baggage train.
     */
    public void clearActionImages() {
        try {
            if (viewImg != null) {
                remove(viewImg);
            }

            if (moveImg != null) {
                remove(moveImg);
            }

            if (handOverImg != null) {
                remove(handOverImg);
            }

            if (tradeImage != null) {
                remove(tradeImage);
            }

            if (unloadTroopsImg != null) {
                remove(unloadTroopsImg);
            }

            if (repairImg != null) {
                remove(repairImg);
            }

            if (loadImg != null) {
                remove(loadImg);
            }

            if (scuttleShips != null) {
                remove(scuttleShips);
            }

            if (scuttled != null) {
                remove(scuttled);
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * place all action images.
     *
     * @param ship the ship object to use.
     */
    public final void positionActionImages(final ShipDTO ship) {

        if (!RegionStore.getInstance().isInitialized()
                || !BarrackStore.getInstance().isInitialized()) {
            return;
        }

        //clear all first...
        try {
            clearActionImages();

        } catch (Exception e) {
            // eat it
        }
        //then place them again..
        if (ship.isScuttle()) {
            if (scuttled != null) {
                add(scuttled, 333, 55);
            }

        } else {
            final SectorDTO[][] regionDTO = RegionStore.getInstance().getRegionSectorsByRegionId(ship.getRegionId());

            if (regionDTO.length < ship.getX() || regionDTO[ship.getX()].length < ship.getY()) {
                return;
            }

            final SectorDTO sector = regionDTO[ship.getX()][ship.getY()];

            if (sector == null) {
                return;
            }

            final int cond = ship.getCondition();
            boolean hasUnits = false;
            if (ship.getLoadedUnitsMap() != null) {
                for (List<Integer> units : ship.getLoadedUnitsMap().values()) {
                    if (units.size() > 0) {
                        hasUnits = true;
                        break;
                    }
                }
            }

            int posX = 338;
            final int posY = 63;

            if (viewImg != null) {
                add(viewImg, posX, posY);
            }

            if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                if (moveImg != null) {
                    posX -= 23;
                    add(moveImg, posX, posY);
                }

                if (handOverImg != null
                        && sector.hasShipyardOrBarracks()
                        && sector.getNationDTO().getNationId() != GameStore.getInstance().getNationId()
                        && RelationsStore.getInstance().getOriginalRelationByNationId(sector.getNationDTO().getNationId()) == REL_ALLIANCE) {
                    posX -= 23;
                    add(handOverImg, posX, posY);
                }

                if (tradeImage != null) {
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

                if (scuttleShips != null) {
                    posX -= 23;
                    add(scuttleShips, posX, posY);
                }
            }
        }
    }

    public final void setupGoodsPanel() {
        //update available tons for this
        goodsPanel.clear();
        lblName.setText(ship.getName());
        //goods
        int posX = 0;
        int posY = 0;
        int countGoods = 0;
        lblCondition.setText(ship.getCondition() + "%");
        lblMarines.setText(numberFormat.format(ship.getMarines()) + " " + getMarinesExpStr() + " marines");
        if (NavyStore.getInstance().isTradeShip(ship)) {
            lblAvailable.setText(numberFormat.format((TradeStore.getInstance().getTradeUnitWeight(ship) - TradeStore.getInstance().getTradeUnitLoad(ship))) + TONS);
            for (int index = 0; index < 14; index++) {
                final Image goodImage;
                if (ship.getGoodsDTO().get(index + 1).getQte() > 0) {
                    goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + (index + 1) + ".png");
                    goodImage.setSize(SIZE_15PX, SIZE_15PX);
                    goodsPanel.add(goodImage, posX, posY);

                    if (ship.getGoodsDTO().get(index + 1) == null) {
                        goodImage.setTitle("null");

                    } else {
                        goodImage.setTitle(ship.getGoodsDTO().get(index + 1).getGoodDTO().getName() + ":" + ship.getGoodsDTO().get(index + 1).getQte());
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
        }

        if (ship.getLoadedUnitsMap() != null) {
            if (ship.getLoadedUnitsMap().containsKey(BRIGADE)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png");
                final List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();
                for (int brigId : ship.getLoadedUnitsMap().get(BRIGADE)) {
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

            if (ship.getLoadedUnitsMap().containsKey(COMMANDER)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png");
                final List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
                for (int commId : ship.getLoadedUnitsMap().get(COMMANDER)) {
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
            }

            if (ship.getLoadedUnitsMap().containsKey(SPY)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png");
                final List<SpyDTO> spies = new ArrayList<SpyDTO>();
                for (int spyId : ship.getLoadedUnitsMap().get(SPY)) {
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

    public final void closeTradePanel() {
        tradeImage.deselect();
    }

    /**
     * @return the ship
     */
    public final ShipDTO getShip() {
        return ship;
    }

    /**
     * @param ship the ship to set
     */
    public final void setShip(final ShipDTO ship) {
        this.ship = ship;
    }

    public final ShipDTO getValue() {
        return getShip();
    }

    public final int getIdentifier() {
        return SHIP;
    }

    public final Widget getWidget() {
        return this;
    }

    public final void setSelected(final boolean selected) {
        if (selected) {
            shipPanel.setStyleName("infoPanelSelected");

        } else {
            shipPanel.setStyleName("ShipInfoPanel");
            shipPanel.setStyleName("clickArmyPanel", true);
        }
    }

    public final void onEnter() {
        // do nothing
    }
}
