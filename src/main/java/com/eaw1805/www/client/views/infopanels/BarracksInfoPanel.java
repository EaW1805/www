package com.eaw1805.www.client.views.infopanels;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.economy.SectorChangeEvent;
import com.eaw1805.www.client.events.economy.SectorChangeHandler;
import com.eaw1805.www.client.events.loading.ArmiesInitEvent;
import com.eaw1805.www.client.events.loading.ArmiesInitdHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.NavyLoadedEvent;
import com.eaw1805.www.client.events.loading.NavyLoadedHandler;
import com.eaw1805.www.client.events.loading.ProSiteLoadedEvent;
import com.eaw1805.www.client.events.loading.ProSiteLoadedHandler;
import com.eaw1805.www.client.events.loading.RegionLoadedEvent;
import com.eaw1805.www.client.events.loading.RegionLoadedHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.BarrackShipYardView;
import com.eaw1805.www.client.views.popups.ArmiesViewerPopup;
import com.eaw1805.www.client.views.popups.BattalionViewerPopup;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CorpsViewerPopup;
import com.eaw1805.www.client.views.popups.FleetsViewerPopup;
import com.eaw1805.www.client.views.popups.ShipsViewerPopup;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

import java.util.List;

public class BarracksInfoPanel
        extends AbstractInfoPanel
        implements ProductionSiteConstants, ArmyConstants, SelectableWidget<BarrackDTO> {

    private AbsolutePanel absolutePanel;
    private Label barrName;
    private ImageButton viewImg;
    private BarrackDTO barrShip;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    final Image fortressImg;

    //labels with values
    final Label armiesLabel;
    final Label corpsLabel;
    final Label brigadesLabel;
    final Label battalionsLabel;
    final Label infLabel;
    final Label cavLabel;
    final Label artLabel;
    final Label merchantLabel;
    final Label warLabel;
    final Label fleetsLabel;
    final Image navyImg;
    final Image navyImg2;
    final Image navyImg3;
    final Image armiesImg;
    final Image corpsImg;
    final Image brigadesImg;
    final Image infImage;
    final Image cavImage;
    final Image artImage;
    final Image battalionsImg;

    boolean productionsLoaded = false;
    boolean regionsLoaded = false;

    public BarracksInfoPanel(final BarrackDTO barrShip) {
        this.setBarrShip(barrShip);
        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("barrackPanel");
        add(this.absolutePanel);
        setSize("366px", "90px");
        this.absolutePanel.setSize("366px", "90px");

        final Image barrShipImg = new Image("");
        barrShipImg.setUrl("http://static.eaw1805.com/tiles/sites/tprod-12.png");
        barrShipImg.setSize("", "82px");
        absolutePanel.add(barrShipImg, 3, 3);

        String fortImgUrl = "";
        fortressImg = new Image(fortImgUrl);
        fortressImg.setSize("", "82px");
        this.absolutePanel.add(fortressImg, 3, 3);

        int placeY = 23;
        navyImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/fleet.png");
        navyImg.setSize("", "15px");
        this.absolutePanel.add(navyImg, 90, placeY);
        navyImg.setTitle("Fleets in shipyard");

        //fleets
        int placeX = 118;
        fleetsLabel = new Label("0");
        fleetsLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(fleetsLabel, placeX, placeY);
        placeY += 17;

        //merchantships
        navyImg2 = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        navyImg2.setSize("", "15px");
        this.absolutePanel.add(navyImg2, 90, placeY);
        navyImg2.setTitle("Merchant ships in shipyard");

        merchantLabel = new Label("0");
        merchantLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(merchantLabel, placeX, placeY);
        placeY += 17;

        //war ships
        navyImg3 = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        navyImg3.setSize("", "15px");
        this.absolutePanel.add(navyImg3, 90, placeY);
        navyImg3.setTitle("War ships in shipyard");

        warLabel = new Label("0");
        warLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(warLabel, placeX, placeY);


        placeY = 23;

        armiesImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/army.png");
        armiesImg.setTitle("Armies in barrack");
        armiesImg.setSize("", "15px");
        this.absolutePanel.add(armiesImg, 150, placeY);


        armiesLabel = new Label("0");


        armiesLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(armiesLabel, 178, placeY);
        placeY += 16;

        corpsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png");
        corpsImg.setTitle("Corps in barrack");
        corpsImg.setSize("", "15px");
        this.absolutePanel.add(corpsImg, 150, placeY);

        corpsLabel = new Label("0");
        corpsLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(corpsLabel, 178, placeY);
        placeY += 16;

        brigadesImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigadesImg.setTitle("Brigades in barrack");
        brigadesImg.setSize("", "15px");
        this.absolutePanel.add(brigadesImg, 150, placeY);

        brigadesLabel = new Label("0");
        brigadesLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(brigadesLabel, 178, placeY);
        placeY += 16;

        battalionsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        battalionsImg.setTitle("Battalions in barrack");
        battalionsImg.setSize("", "15px");
        this.absolutePanel.add(battalionsImg, 150, placeY);

        battalionsLabel = new Label("0");
        battalionsLabel.setStyleName("clearFontSmall");
        this.absolutePanel.add(battalionsLabel, 178, placeY);

        final Label xyLbl = new Label(getBarrShip().positionToString());
        xyLbl.setTitle("Barracks position.");
        xyLbl.setStyleName("clearFontSmall");
        absolutePanel.add(xyLbl, 315, 3);

        if (GameStore.getInstance().getNationId() == barrShip.getNationId()) {
            this.viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
            this.viewImg.setTitle("Go to barrack position");
            this.absolutePanel.add(this.viewImg, 338, 63);
            this.viewImg.setSize("20px", "20px");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    MapStore.getInstance().getMapsView().goToPosition(barrShip);
                    viewImg.deselect();
                }
            }).addToElement(viewImg.getElement()).register();


            final Image openBarrShipImg;
            if (!getBarrShip().getNotSupplied()) {
                openBarrShipImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButBarracksPanelOff.png");
                openBarrShipImg.setTitle("Click to open barrack.");

            } else {
                openBarrShipImg = new DualStateImage("http://static.eaw1805.com/images/buttons/icons/barShipNA.png");
                openBarrShipImg.setTitle("Barrack not supplied.");
            }

            absolutePanel.add(openBarrShipImg, 315, 63);
            openBarrShipImg.setSize("20px", "20px");
            openBarrShipImg.setStyleName("pointer", true);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    if (!getBarrShip().getNotSupplied()) {
                        SectorDTO sector = MapStore.getInstance().getRegionSectorByRegionIdXY(barrShip.getRegionId(), barrShip.getX(), barrShip.getY());
                        MapStore.getInstance().getMapsView().goToPosition(barrShip);
                        BarrackShipYardView barrackShipYardView = new BarrackShipYardView(barrShip, sector);
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(barrackShipYardView);
                        GameStore.getInstance().getLayoutView().positionTocCenter(barrackShipYardView);
                    } else {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "The current barrack is not supplied!", false);
                    }
                }
            }).addToElement(openBarrShipImg.getElement()).register();

        }

        if (getBarrShip().getNotSupplied()) {
            final Image notSupplyImg = new Image("http://static.eaw1805.com/images/buttons/OutOfSupply32.png");
            notSupplyImg.setSize("", "19px");
            this.absolutePanel.add(notSupplyImg, 286, 3);
            notSupplyImg.setTitle("Not supplied");
        }

        infImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        cavImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        artImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        absolutePanel.add(infImage, 225, 23);
        absolutePanel.add(cavImage, 225, 40);
        absolutePanel.add(artImage, 225, 57);
        infImage.setSize("", "15px");
        cavImage.setSize("", "15px");
        artImage.setSize("", "15px");
        infImage.setTitle("Infantry");
        cavImage.setTitle("Cavalry");
        artImage.setTitle("Artillery");

        infLabel = new Label("");
        cavLabel = new Label("");
        artLabel = new Label("");
        infLabel.setStyleName("clearFontSmall");
        cavLabel.setStyleName("clearFontSmall");
        artLabel.setStyleName("clearFontSmall");
        absolutePanel.add(infLabel, 253, 23);
        absolutePanel.add(cavLabel, 253, 40);
        absolutePanel.add(artLabel, 253, 57);

        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                productionsLoaded = true;
                if (regionsLoaded) {
                    setupName();
                    updateLabels();
                }
            }
        });

        LoadEventManager.addRegionLoadedHandler(new RegionLoadedHandler() {
            public void onRegionLoaded(RegionLoadedEvent event) {
                regionsLoaded = true;
                if (productionsLoaded && regionsLoaded) {
                    setupName();
                    updateLabels();
                }
            }
        });

        LoadEventManager.addArmiesInitHandler(
                new ArmiesInitdHandler() {
                    public void onArmiesInit(ArmiesInitEvent e) {
                        updateArmyLabels();
                    }
                });

        LoadEventManager.addNavyLoadedHandler(new NavyLoadedHandler() {
            public void onNavyLoaded(NavyLoadedEvent event) {
                updateNavyLabels();
            }
        });

        EcoEventManager.addSectorChangeHandler(new SectorChangeHandler() {
            public void onSectorChange(SectorChangeEvent event) {
                if (event.getRegion() == barrShip.getRegionId() || event.getRegion() == 0) {
                    updateArmyLabels();
                    updateLabels();
                    updateNavyLabels();
                }
            }
        });

        productionsLoaded = DataStore.getInstance().isInitialized();
        regionsLoaded = RegionStore.getInstance().isInitialized();

        if (NavyStore.getInstance().isInitialized()) {
            updateNavyLabels();
        }
        if (productionsLoaded && regionsLoaded) {
            setupName();
            updateLabels();
        }
        if (ArmyStore.getInstance().isArmiesInitialized()) {
            updateArmyLabels();
        }

    }

    public void setupName() {
        final TradeCityDTO tradeCity = TradeCityStore.getInstance().getTradeCityByPosition(barrShip);
        try {
            absolutePanel.remove(barrName);
        } catch (Exception ignore) {
        }
        if (tradeCity != null) {
            barrName = new Label(tradeCity.getName());

        } else if (barrShip.getName() == null) {
            barrName = new RenamingLabel("Barrack", BARRACK, barrShip.getId());

        } else {
            barrName = new RenamingLabel(barrShip.getName(), BARRACK, barrShip.getId());
        }
        barrName.setStyleName("clearFontMiniTitle");
        absolutePanel.add(barrName, 90, 3);
    }

    public void updateNavyLabels() {
        final List<FleetDTO> fleets = NavyStore.getInstance().getFleetsByRegionAndTile(barrShip, false, true);
        int fleetsCounter = 0;
        int warshipsCounter = 0;
        int merchantShipsCounter = 0;
        if (fleets != null && fleets.size() > 0) {
            for (FleetDTO fleet : fleets) {
                if (fleet.getFleetId() != 0) {
                    fleetsCounter++;
                }
                warshipsCounter += MiscCalculators.getFleetInfo(fleet).getWarShips();
                merchantShipsCounter += MiscCalculators.getFleetInfo(fleet).getMerchantShips();
            }
        }

        fleetsLabel.setText(numberFormat.format(fleetsCounter));
        merchantLabel.setText(numberFormat.format(merchantShipsCounter));
        warLabel.setText(numberFormat.format(warshipsCounter));
        GameStore.getInstance().unRegisterEvent(navyImg.getElement().getId());
        GameStore.getInstance().unRegisterEvent(navyImg2.getElement().getId());
        GameStore.getInstance().unRegisterEvent(navyImg3.getElement().getId());
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new FleetsViewerPopup(
                        fleets, "Ships in barrackBattalions", false).open();
            }
        }).addToElement(navyImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new ShipsViewerPopup(
                        NavyStore.getInstance().getShipsInFleets(fleets, true, false), "Merchant ships").open();
            }
        }).addToElement(navyImg2.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new ShipsViewerPopup(
                        NavyStore.getInstance().getShipsInFleets(fleets, false, true), "War ships").open();
            }
        }).addToElement(navyImg3.getElement()).register();

    }

    public void updateArmyLabels() {
        final SectorDTO sector = MapStore.getInstance().getRegionSectorByRegionIdXY(barrShip.getRegionId(), barrShip.getX(), barrShip.getY());
        final List<ArmyDTO> armies = ArmyStore.getInstance().getArmiesBySector(sector, true);

        int numOfInfs = 0;
        int numOfCavs = 0;
        int numOfArts = 0;

        int corpsCounter = 0;
        int brigadesCounter = 0;
        int battalionCounter = 0;
        int armiesCounter = 0;
        if (armies != null) {
            for (ArmyDTO army : armies) {
                if (army.getArmyId() != 0) {
                    armiesCounter++;
                } else {
                    if (army.getCorps().containsKey(0)) {
                        corpsCounter--;
                    }
                }

                numOfArts += MiscCalculators.getArmyInfo(army).getArtillery();
                numOfCavs += MiscCalculators.getArmyInfo(army).getCavalry();
                numOfInfs += MiscCalculators.getArmyInfo(army).getInfantry();

                battalionCounter += MiscCalculators.getArmyInfo(army).getBattalions();
                brigadesCounter += MiscCalculators.getArmyInfo(army).getBrigades();
                corpsCounter += MiscCalculators.getArmyInfo(army).getCorps();
            }
        }
        armiesLabel.setText(numberFormat.format(armiesCounter));
        corpsLabel.setText(numberFormat.format(corpsCounter));
        brigadesLabel.setText(numberFormat.format(brigadesCounter));
        battalionsLabel.setText(numberFormat.format(battalionCounter));
        infLabel.setText(numberFormat.format(numOfInfs));
        cavLabel.setText(numberFormat.format(numOfCavs));
        artLabel.setText(numberFormat.format(numOfArts));
        //be sure there are no forgotten handlers registered to those elements

        GameStore.getInstance().unRegisterEvent(armiesImg.getElement().getId());
        GameStore.getInstance().unRegisterEvent(corpsImg.getElement().getId());
        GameStore.getInstance().unRegisterEvent(brigadesImg.getElement().getId());
        GameStore.getInstance().unRegisterEvent(battalionsImg.getElement().getId());
        GameStore.getInstance().unRegisterEvent(infImage.getElement().getId());
        GameStore.getInstance().unRegisterEvent(cavImage.getElement().getId());
        GameStore.getInstance().unRegisterEvent(artImage.getElement().getId());


        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new ArmiesViewerPopup(
                        armies, "Armies in barrack", false).open();
            }
        }).addToElement(armiesImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new CorpsViewerPopup(
                        ArmyStore.getInstance().getCorpsInArmies(armies), "Corps in barrack", false).open();
            }
        }).addToElement(corpsImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BrigadesViewerPopup(
                        ArmyStore.getInstance().getBrigadesInArmies(armies), "Brigades in barrack").open();
            }
        }).addToElement(brigadesImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(
                        ArmyStore.getInstance().getBattalionsInArmies(armies, true, true, true), "Battalions in barrack").open();
            }
        }).addToElement(battalionsImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(
                        ArmyStore.getInstance().getBattalionsInArmies(armies, true, false, false), "Infantry in barrack").open();
            }
        }).addToElement(infImage.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(
                        ArmyStore.getInstance().getBattalionsInArmies(armies, false, true, false), "Cavalry in barrack").open();
            }
        }).addToElement(cavImage.getElement()).register();
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(
                        ArmyStore.getInstance().getBattalionsInArmies(armies, false, false, true), "Artillery in barrack").open();
            }
        }).addToElement(artImage.getElement()).register();

    }

    public void updateLabels() {
        try {
            final SectorDTO barrackSector = ProductionSiteStore.getInstance().getSectorByRegionXY(barrShip.getX(), barrShip.getY(), barrShip.getRegionId());
            if (barrackSector != null) {
                switch (barrackSector.getProductionSiteId()) {
                    case PS_BARRACKS_FS:
                        fortressImg.setVisible(true);
                        fortressImg.setUrl("http://static.eaw1805.com/images/buttons/icons/fort01.png");
                        fortressImg.setTitle("Small Fortress");
                        break;

                    case PS_BARRACKS_FM:
                        fortressImg.setVisible(true);
                        fortressImg.setUrl("http://static.eaw1805.com/images/buttons/icons/fort02.png");
                        fortressImg.setTitle("Medium Fortress");
                        break;

                    case PS_BARRACKS_FL:
                        fortressImg.setVisible(true);
                        fortressImg.setUrl("http://static.eaw1805.com/images/buttons/icons/fort03.png");
                        fortressImg.setTitle("Large Fortress");
                        break;

                    case PS_BARRACKS_FH:
                        fortressImg.setVisible(true);
                        fortressImg.setUrl("http://static.eaw1805.com/images/buttons/icons/fort04.png");
                        fortressImg.setTitle("Huge Fortress");
                        break;

                    default:
                        fortressImg.setVisible(false);
                        break;
                }

            } else {
//                Window.alert("sector is null");
            }

        } catch (Exception ignore) {
        }

        final TradeCityDTO tradeCity = TradeCityStore.getInstance().getTradeCityByPosition(barrShip);
        if (tradeCity != null) {
            barrName.setText(tradeCity.getName());

        } else if (barrShip.getName() == null) {
            barrName.setText("barrack");

        } else {
            barrName.setText(barrShip.getName());
        }

        barrName.setStyleName("clearFontMiniTitle");
    }

    public void setBarrShip(final BarrackDTO barrShip) {
        this.barrShip = barrShip;
    }

    public BarrackDTO getBarrShip() {
        return barrShip;
    }

    public BarrackDTO getValue() {
        return getBarrShip();
    }

    public int getIdentifier() {
        return BARRACK;
    }

    public void setSelected(final boolean selected) {
        if (selected) {
            setStyleName("infoPanelSelected", true);
        } else {
            try {
                removeStyleName("infoPanelSelected");
            } catch (Exception ignore) {
            }
        }
    }

    public void onEnter() {
        SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(getBarrShip().getRegionId())[getBarrShip().getX()][getBarrShip().getY()];

        MapStore.getInstance().getMapsView().goToPosition(getBarrShip());

        //SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
        BarrackShipYardView barShipView = new BarrackShipYardView(getBarrShip(), barrackSector);
        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(barShipView);
        GameStore.getInstance().getLayoutView().positionTocCenter(barShipView);

    }

    public Widget getWidget() {
        return this;
    }
}
