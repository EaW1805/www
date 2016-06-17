package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.*;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.loading.AllUnitsLoadedEvent;
import com.eaw1805.www.client.events.loading.AllUnitsLoadedHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
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
import com.eaw1805.www.client.views.infopanels.units.mini.FleetRepairCostMini;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CommandersViewerPopup;
import com.eaw1805.www.client.views.popups.FleetsViewerPopup;
import com.eaw1805.www.client.views.popups.ShipsInFleetViewerPopup;
import com.eaw1805.www.client.views.popups.ShipsViewerPopup;
import com.eaw1805.www.client.views.popups.SpiesViewerPopup;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import com.eaw1805.www.shared.stores.util.NavyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FleetInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, RelationConstants, SelectableWidget<FleetDTO>, StyleConstants {

    private FleetDTO fleet;
    private final Label lblBattShips;
    private final Label lblMerchShips;
    private final Label lblFleets;
    private final Label lblFleetName;
    private ImageButton viewImg;
    private NavyUnitInfoDTO fleetInfo;
    private final Label lblMps;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private final AbsolutePanel goodsPanel;
    private final Label lblAvailable;
    private final ClickAbsolutePanel fleetInfoPanel;
    private Image unloadTroopsImg;
    private ImageButton moveImg;
    private ImageButton tradeImage;
    private Image repairImg;
    private final Label lblMarines;
    private final UnitChangedHandler unitChangedHandler;

    public FleetInfoPanel(final FleetDTO thisFleet, final boolean canMove) {
        fleet = thisFleet;
        int nationId = GameStore.getInstance().getNationId();
        if (fleet.getShips().size() > 0) {
            nationId = fleet.getShips().values().iterator().next().getNationId();
        }

        final int leftFactor = 87;
        setStyleName("");
        setSize("366px", "90px");

        fleetInfoPanel = new ClickAbsolutePanel();
        fleetInfoPanel.setStyleName("fleetInfoPanel");
        fleetInfoPanel.setSize("363px", "87px");
        add(fleetInfoPanel);

        if (fleet.getFleetId() == 0) {
            lblFleetName = new Label("Fleet Name");
        } else {
            lblFleetName = new RenamingLabel("Fleet Name", FLEET, fleet.getId());
        }
        lblFleetName.setStyleName("clearFontMiniTitle");
        lblFleetName.setSize("235px", "25px");
        if (fleet.getFleetId() != 0) {
            fleetInfoPanel.add(lblFleetName, 90 - leftFactor, 3);
        }

        int totalCapacity = 0;
        int totalMarines = 0;
        for (final ShipDTO ship : fleet.getShips().values()) {
            totalCapacity += ship.getType().getLoadCapacity();
            totalMarines += ship.getMarines();
        }

        lblAvailable = new Label(numberFormat.format(totalCapacity) + " tons");
        lblAvailable.setStyleName(CLASS_CLEARFONTSMALL);
        lblAvailable.setSize("235px", "25px");
        if (fleet.getFleetId() != 0) {
            fleetInfoPanel.add(lblAvailable, 90 - leftFactor, 20);
        } else {
            fleetInfoPanel.add(lblAvailable, 90 - leftFactor, 3);
        }

        lblMarines = new Label(numberFormat.format(totalMarines) + " marines");
        lblMarines.setStyleName(CLASS_CLEARFONTSMALL);
        if (fleet.getFleetId() != 0) {
            fleetInfoPanel.add(lblMarines, 90 - leftFactor, 34);
        } else {
            fleetInfoPanel.add(lblMarines, 90 - leftFactor, 17);
        }

        fleetInfo = MiscCalculators.getFleetInfo(fleet);

        lblFleets = new Label("");
        lblFleets.setStyleName(CLASS_CLEARFONTSMALL);


        final Image fleetImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/fleet.png");
        fleetImg.setSize("", SIZE_15PX);
        fleetImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                final List<FleetDTO> regionFleets = NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true);
                final Iterator<FleetDTO> iter = regionFleets.iterator();
                while (iter.hasNext()) {
                    if (iter.next().getFleetId() == 0) {
                        iter.remove();
                        break;
                    }
                }
                new FleetsViewerPopup(NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true), "Fleets", true).open();
            }
        });
        if (fleet.getFleetId() == 0) {
            fleetInfoPanel.add(fleetImg, 90 - leftFactor, 36);
            fleetInfoPanel.add(lblFleets, 118 - leftFactor, 36);
        }

        final Image navyImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        navyImg.setSize("", SIZE_15PX);
        fleetInfoPanel.add(navyImg, 90 - leftFactor, 52);
        navyImg.setTitle("War ships");
        if (GameStore.getInstance().getNationId() == NavyStore.getInstance().getNationIdFromFleet(fleet)) {
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    if (fleet.getFleetId() == 0) {
                        List<ShipDTO> allRegionShips = new ArrayList<ShipDTO>();
                        for (ShipDTO ship : fleet.getShips().values()) {
                            if (!NavyStore.getInstance().isTradeShip(ship)) {
                                allRegionShips.add(ship);
                            }
                        }
                        for (FleetDTO otherFleet : NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true)) {
                            if (otherFleet.getFleetId() != 0) {
                                for (ShipDTO ship : otherFleet.getShips().values()) {
                                    if (!NavyStore.getInstance().isTradeShip(ship)) {
                                        allRegionShips.add(ship);
                                    }
                                }
                            }
                        }

                        new ShipsViewerPopup(allRegionShips, "War ships in region").open();
                    } else {
                        new ShipsInFleetViewerPopup(fleet, false, true).open();
                    }

                }
            }).addToElement(navyImg.getElement()).register();
        }
        lblBattShips = new Label(numberFormat.format(fleetInfo.getWarShips()));
        lblBattShips.setStyleName(CLASS_CLEARFONTSMALL);
        fleetInfoPanel.add(lblBattShips, 118 - leftFactor, 52);

        final Image navyImg2 = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        if (GameStore.getInstance().getNationId() == NavyStore.getInstance().getNationIdFromFleet(fleet)) {
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    if (fleet.getFleetId() == 0) {
                        List<ShipDTO> allRegionShips = new ArrayList<ShipDTO>();
                        for (ShipDTO ship : fleet.getShips().values()) {
                            if (NavyStore.getInstance().isTradeShip(ship)) {
                                allRegionShips.add(ship);
                            }
                        }
                        for (FleetDTO otherFleet : NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true)) {
                            if (otherFleet.getFleetId() != 0) {
                                for (ShipDTO ship : otherFleet.getShips().values()) {
                                    if (NavyStore.getInstance().isTradeShip(ship)) {
                                        allRegionShips.add(ship);
                                    }
                                }
                            }
                        }

                        new ShipsViewerPopup(allRegionShips, "Merchant ships in region").open();
                    } else {
                        new ShipsInFleetViewerPopup(fleet, true, false).open();
                    }

                }
            }).addToElement(navyImg2.getElement()).register();
        }

        navyImg2.setSize("", SIZE_15PX);
        navyImg2.setTitle("Merchant ships");
        fleetInfoPanel.add(navyImg2, 90 - leftFactor, 68);

        lblMerchShips = new Label(numberFormat.format(fleetInfo.getMerchantShips()));
        lblMerchShips.setStyleName(CLASS_CLEARFONTSMALL);
        fleetInfoPanel.add(lblMerchShips, 118 - leftFactor, 68);

        final Label lblXy = new Label("");
        if (fleet.getFleetId() != 0) {
            lblXy.setText(fleet.positionToString());
        }
        lblXy.setTitle("Fleets position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        lblXy.setSize("38px", "18px");
        fleetInfoPanel.add(lblXy, 315, 3);

        lblMps = new Label("");
        if (fleet.getFleetId() != 0) {
            lblMps.setText(MiscCalculators.getFleetInfo(fleet).getMps() + " MPs");
        }
        lblMps.setTitle("Movement points.");
        lblMps.setStyleName(CLASS_CLEARFONTSMALL);
        lblMps.setSize("50px", SIZE_15PX);
        fleetInfoPanel.add(lblMps, 315, 20);

        if (GameStore.getInstance().getNationId() == nationId) {
            if (fleet.getFleetId() != 0) {
                viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");

                viewImg.setSize(SIZE_20PX, SIZE_20PX);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        MapStore.getInstance().getMapsView().goToPosition(fleet);
                        viewImg.deselect();
                    }
                }).addToElement(viewImg.getElement()).register();
            }

            if (canMove && !GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {

                if (fleet.getFleetId() == 0) {
                    moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/moveNA.png");
                    moveImg.setTitle("Cannot move the free ships as a fleet");
                } else {
                    moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");

                    moveImg.setTitle("Click here to move the fleet");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            event.stopPropagation();
                            MapStore.getInstance().getMapsView().goToPosition(fleet);
                            MapStore.getInstance().getMapsView().addFigureOnMap(FLEET, fleet.getFleetId(),
                                    fleet, MiscCalculators.getFleetMps(fleet), PowerCalculators.getPowerByWarShips(fleetInfo.getWarShips()),
                                    0, 0);
                            moveImg.deselect();
                        }
                    }).addToElement(moveImg.getElement()).register();
                }

                moveImg.setSize(SIZE_20PX, SIZE_20PX);

            }

            if (fleet.getFleetId() != 0 && !GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                tradeImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");

                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        ShipDTO firstTrade = null;
                        for (ShipDTO ship : fleet.getShips().values()) {
                            if (NavyStore.getInstance().isTradeShip(ship)) {
                                firstTrade = ship;
                                break;
                            }
                        }
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(
                                new TradePanelView(firstTrade, null, SHIP));
                    }
                }).addToElement(tradeImage.getElement()).register();

                tradeImage.setTitle("Open trade overview");
                tradeImage.setStyleName("pointer");

                tradeImage.setSize(SIZE_20PX, SIZE_20PX);


                repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
                repairImg.setTitle("Repair");
                repairImg.setSize(SIZE_20PX, SIZE_20PX);
                repairImg.setStyleName("pointer");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        final SectorDTO sector = MapStore.getInstance().getRegionSectorByRegionIdXY(fleet.getRegionId(), fleet.getX(), fleet.getY());
                        if (GameStore.getInstance().getNationId() == NavyStore.getInstance().getNationIdFromFleet(fleet)) {
                            NavyStore.getInstance().repairFleet(fleet.getFleetId(), sector.getId());

                        } else {
                            AlliedUnitsStore.getInstance().repairFleet(fleet.getFleetId(), sector.getId(), NavyStore.getInstance().getNationIdFromFleet(fleet));
                        }
                    }
                }).addToElement(repairImg.getElement()).register();


                //add tooltip with information about the fleet repair cost
                new ToolTipPanel(repairImg) {
                    @Override
                    public void generateTip() {
                        setTooltip(new FleetRepairCostMini(fleet));
                    }
                };


                unloadTroopsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDisembarkOff.png");
                unloadTroopsImg.setTitle("Unload troops");
                unloadTroopsImg.setSize(SIZE_20PX, SIZE_20PX);
                unloadTroopsImg.setStyleName("pointer");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new UnloadTroopsView(FLEET, fleet));
                    }
                }).addToElement(unloadTroopsImg.getElement()).register();
            }
        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    fleetInfoPanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };
            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + NavyStore.getInstance().getNationIdFromFleet(fleet) + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(NavyStore.getInstance().getNationIdFromFleet(fleet)) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            fleetInfoPanel.add(relationPanel, 246, 63);
        }

        setUpLabels();

        goodsPanel = new AbsolutePanel();
        goodsPanel.setSize("150px", "35px");
        add(goodsPanel, 194 - leftFactor, 30);

        TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {
            public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                if (getGoodEvent.getUnitType() == SHIP
                        || (getGoodEvent.getUnitType() == FLEET
                        && (getGoodEvent.getUnitId() == fleet.getFleetId()
                        || getGoodEvent.getUnitId() == 0))) {
                    if (getGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()) {
                        setupGoodsPanel();
                    }
                    positionActionImages();
                }
            }
        });

        TradeEventManager.addGiveGoodHanlder(new GiveGoodHandler() {
            public void onGiveGoodIn(final GiveGoodEvent giveGoodEvent) {
                if (giveGoodEvent.getUnitType() == SHIP
                        || (giveGoodEvent.getUnitType() == FLEET
                        && (giveGoodEvent.getUnitId() == fleet.getFleetId() ||
                        giveGoodEvent.getUnitId() == 0))) {
                    if (giveGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()) {
                        setupGoodsPanel();
                    }
                    positionActionImages();
                }
            }
        });

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if ((event.getInfoType() == FLEET && event.getInfoId() == fleet.getFleetId()) ||
                        (fleet.getFleetId() == 0)) {
                    setUpLabels();
                    setupGoodsPanel();
                    positionActionImages();
                }
            }
        };

        //when everything is loaded...
        LoadEventManager.addAllUnitsLoadedHandler(new AllUnitsLoadedHandler() {
            @Override
            public void onAllLoaded(final AllUnitsLoadedEvent event) {
                setupGoodsPanel();
                positionActionImages();
            }
        });
        setupGoodsPanel();
        positionActionImages();


    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(FLEET, fleet.getFleetId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void removeAllActionImages() {
        try {
            fleetInfoPanel.remove(moveImg);
        } catch (Exception ignore) {
        }
        try {
            fleetInfoPanel.remove(viewImg);
        } catch (Exception ignore) {
        }
        try {
            fleetInfoPanel.remove(tradeImage);
        } catch (Exception ignore) {
        }
        try {
            fleetInfoPanel.remove(repairImg);
        } catch (Exception ignore) {
        }
        try {
            fleetInfoPanel.remove(unloadTroopsImg);
        } catch (Exception ignore) {
        }
    }

    private void positionActionImages() {
        removeAllActionImages();

        boolean hasUnits = false;
        if (fleet.getLoadedUnitsMap() != null) {
            for (List<Integer> units : fleet.getLoadedUnitsMap().values()) {
                if (units.size() > 0) {
                    hasUnits = true;
                    break;
                }
            }
        }
        if (GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
            if (viewImg != null) {
                fleetInfoPanel.add(viewImg, 338, 63);
            }
        } else {

            int posX = 338;

            if (moveImg != null) {
                fleetInfoPanel.add(moveImg, posX, 63);
                posX -= 23;
            }

            if (viewImg != null) {
                fleetInfoPanel.add(viewImg, posX, 63);
                posX -= 23;
            }

            if (tradeImage != null && NavyStore.getInstance().hasMerchantShips(fleet)) {
                fleetInfoPanel.add(tradeImage, posX, 63);
                posX -= 23;
            }
            if (HibernateUtil.DB_S3 == GameStore.getInstance().getScenarioId()
                    && fleet.getRegionId() != RegionConstants.EUROPE) {
                return;
            }
            final SectorDTO startingSector = RegionStore.getInstance().getRegionSectorsByRegionId(fleet.getRegionId())[fleet.getXStart()][fleet.getYStart()];
            if (repairImg != null
                    && startingSector.hasShipyardOrBarracks() && startingSector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()
                    && (fleet.getNationId() == GameStore.getInstance().getNationId()
                    || RelationsStore.getInstance().getOriginalRelationByNationId(fleet.getNationId()) == REL_ALLIANCE)) {
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getCondition() < 100) {
                        fleetInfoPanel.add(repairImg, posX, 63);
                        posX -= 23;
                        break;
                    }
                }
            }


            if (hasUnits && unloadTroopsImg != null) {
                fleetInfoPanel.add(unloadTroopsImg, posX, 63);
                posX -= 23;
            }


        }


    }

    /**
     * Setup images with goods that are loaded in the fleet.
     */
    @SuppressWarnings("restriction")
    private void setupGoodsPanel() {
        goodsPanel.clear();

        //calculate goods in fleet.
        final Map<Integer, Integer> goodToQuantity = new HashMap<Integer, Integer>();
        final Map<Integer, String> goodToName = new HashMap<Integer, String>();

        for (int index = GoodConstants.GOOD_FIRST; index < GoodConstants.GOOD_LAST - 1; index++) {
            goodToQuantity.put(index, 0);
        }

        final List<FleetDTO> allFleets = new ArrayList<FleetDTO>();
        allFleets.add(fleet);
        if (fleet.getFleetId() == 0) {
            for (FleetDTO otherFleet : NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true)) {
                if (otherFleet.getFleetId() != 0) {
                    allFleets.add(otherFleet);
                }
            }
        }
        for (FleetDTO thisFleet : allFleets) {
            for (final ShipDTO shipDTO : thisFleet.getShips().values()) {
                if (NavyStore.getInstance().isTradeShip(shipDTO)) {
                    for (Map.Entry<Integer, StoredGoodDTO> entry : shipDTO.getGoodsDTO().entrySet()) {
                        final int goodId = entry.getKey();
                        final StoredGoodDTO good = entry.getValue();
                        if (goodToQuantity.containsKey(goodId)) {
                            goodToQuantity.put(goodId, goodToQuantity.get(goodId) + good.getQte());
                        } else {
                            goodToQuantity.put(goodId, good.getQte());
                        }

                        if (!goodToName.containsKey(goodId)) {
                            goodToName.put(goodId, good.getGoodDTO().getName());
                        }
                    }
                }
            }
        }

        int posX = 0;
        int posY = 0;
        int countGoods = 0;
        for (int index = GoodConstants.GOOD_FIRST; index < GoodConstants.GOOD_LAST - 1; index++) {
            final Image goodImage;
            if (goodToQuantity.get(index) > 0) {
                goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + Integer.toString(index) + ".png");
                goodImage.setSize(SIZE_15PX, SIZE_15PX);
                goodImage.setTitle(goodToName.get(index) + ": " + goodToQuantity.get(index));
                goodsPanel.add(goodImage, posX, posY);

                if ((countGoods + 1) % 2 == 0 && countGoods != 0) {
                    posX += 17;
                    posY = 0;

                } else {
                    posY += 17;
                }

                countGoods++;
            }
        }

        if (fleet.getLoadedUnitsMap() != null) {
            if (fleet.getLoadedUnitsMap().containsKey(BRIGADE)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png");
                unitImage.setSize(SIZE_15PX, SIZE_15PX);
                final List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();
                for (Integer brigId : fleet.getLoadedUnitsMap().get(BRIGADE)) {
                    BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(brigId);
                    if (brig != null) {//this will happen if you are the owner of this brigade...
                        brigades.add(brig);
                    }
                }
                if (!brigades.isEmpty()) {
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            new BrigadesViewerPopup(brigades, "Loaded brigades").open();
                        }
                    }).addToElement(unitImage.getElement()).register();
                }

                goodsPanel.add(unitImage, posX, posY);

                if ((countGoods + 1) % 2 == 0 && countGoods != 0) {
                    posX += 17;
                    posY = 0;

                } else {
                    posY += 17;
                }
                countGoods++;
            }

            if (fleet.getLoadedUnitsMap().containsKey(COMMANDER)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png");
                unitImage.setSize(SIZE_15PX, SIZE_15PX);
                final List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
                for (Integer commId : fleet.getLoadedUnitsMap().get(COMMANDER)) {
                    CommanderDTO comm = CommanderStore.getInstance().getCommanderById(commId);
                    if (comm != null) {
                        commanders.add(comm);
                    }
                }
                if (!commanders.isEmpty()) {
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            new CommandersViewerPopup(commanders, "Loaded commanders").open();
                        }
                    }).addToElement(unitImage.getElement()).register();
                }
                goodsPanel.add(unitImage, posX, posY);

                if ((countGoods + 1) % 2 == 0 && countGoods != 0) {
                    posX += 17;
                    posY = 0;

                } else {
                    posY += 17;
                }
            }

            if (fleet.getLoadedUnitsMap().containsKey(SPY)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png");
                unitImage.setSize(SIZE_15PX, SIZE_15PX);
                final List<SpyDTO> spies = new ArrayList<SpyDTO>();
                for (Integer spyId : fleet.getLoadedUnitsMap().get(SPY)) {
                    SpyDTO spy = SpyStore.getInstance().getSpyById(spyId);
                    if (spy != null) {
                        spies.add(spy);
                    }
                }
                if (!spies.isEmpty()) {
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            new SpiesViewerPopup(spies, "Loaded spies").open();
                        }
                    }).addToElement(unitImage.getElement()).register();
                }
                goodsPanel.add(unitImage, posX, posY);
            }
        }
    }

    private void setUpLabels() {
        int totalCapacity = 0;
        int totalMarines = 0;
        for (final ShipDTO ship : fleet.getShips().values()) {
            totalCapacity += ship.getType().getLoadCapacity();
            totalMarines += ship.getMarines();
        }
        if (fleet.getFleetId() == 0) {
            for (FleetDTO otherFleet : NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true)) {
                if (otherFleet.getFleetId() != 0) {
                    for (ShipDTO ship : otherFleet.getShips().values()) {
                        totalCapacity += ship.getType().getLoadCapacity();
                        totalMarines += ship.getMarines();
                    }
                }
            }
        }
        lblAvailable.setText(numberFormat.format(totalCapacity) + " tons");
        lblMarines.setText(numberFormat.format(totalMarines) + " marines");

        if (fleet.getFleetId() == 0) {
            if (NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true).size() > 0 &&
                    NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true).get(0).getFleetId() == 0) {
                fleet = NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true).get(0);

            } else {
                final int regionId = fleet.getRegionId();
                fleet = new FleetDTO();
                fleet.setFleetId(0);
                fleet.setShips(new HashMap<Integer, ShipDTO>());
                fleet.setRegionId(regionId);
            }
            final List<FleetDTO> regionFleets = NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true);
            if (regionFleets.size() - 1 >= 0) {
                lblFleets.setText(numberFormat.format(regionFleets.size() - 1) + " Fleets");
            } else {
                lblFleets.setText(numberFormat.format(0) + " Fleets");
            }
        }

        fleetInfo = MiscCalculators.getFleetInfo(fleet);
        if (fleet.getFleetId() == 0) {
            lblFleetName.setText("Ships in no fleet.");
        } else if (fleet.getName().equals("")) {
            lblFleetName.setText("Click to set name");
        } else {
            lblFleetName.setText(fleet.getName());
        }

        if (fleet.getFleetId() == 0) {
            for (FleetDTO otherFleet : NavyStore.getInstance().getFleetsByRegion(fleet.getRegionId(), true)) {
                if (otherFleet.getFleetId() != 0) {
                    NavyUnitInfoDTO info = MiscCalculators.getFleetInfo(otherFleet);
                    fleetInfo.setMerchantShips(fleetInfo.getMerchantShips() + info.getMerchantShips());
                    fleetInfo.setWarShips(fleetInfo.getWarShips() + info.getWarShips());
                }
            }
        }


        lblBattShips.setText(fleetInfo.getWarShips() + " War Ships");
        lblMerchShips.setText(fleetInfo.getMerchantShips() + " Merchant Ships");
        if (fleet.getFleetId() != 0) {
            lblMps.setText(fleetInfo.getMps() + " MPs");
        }
    }

    public FleetDTO getFleet() {
        return fleet;
    }

    public FleetDTO getValue() {
        return getFleet();
    }

    public int getIdentifier() {
        return FLEET;
    }

    public Widget getWidget() {
        return this;
    }

    public void setSelected(final boolean selected) {
        if (selected) {
            fleetInfoPanel.setStyleName("infoPanelSelected");
        } else {
            fleetInfoPanel.setStyleName("fleetInfoPanel");
        }
    }

    public void onEnter() {
        // do nothing
    }
}
