package com.eaw1805.www.client.views.layout;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

public class SectorMenu
        extends AbsolutePanel
        implements TerrainConstants, StyleConstants, ArmyConstants {

    private final Image resourceImg, citizensImg, nationImg, psImg;
    private final Image armyImg, corpImg, warShipImg, merchShipImg;
    private final Image payedImg, buildProgressImg, conquerImg;
    private final Image homeImg;
    private final Label lblTileTitle, lblPopulation, lblCoordinates, lblTerrainType, lblFortType;
    private final ImageButton togglePanelImg;
    private boolean maximized = true;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();

    private transient final ProductionSiteStore eStore = ProductionSiteStore.getInstance();

    private transient final DataStore dStore = DataStore.getInstance();
    private final PopupPanel tooltip;
    private boolean tooltipLocked = false;

    public SectorMenu() {
        setStyleName("sectorPanel");
        setSize("179px", "194px");

        tooltip = new PopupPanel();
        tooltip.setStyleName("none");
        final Image sectorImg = new Image("http://static.eaw1805.com/images/infopanels/InfoPanel1.png");
        sectorImg.setSize("167px", "141px");
        sectorImg.setStyleName("sectorPanelBG");
        add(sectorImg, 9, 45);

        final AbsolutePanel sectorMenu = new AbsolutePanel();
        sectorMenu.setSize("170px", "140px");
        add(sectorMenu, 9, 46);

        lblTileTitle = new Label("");
        lblTileTitle.setStyleName("clearFontSmall");
        lblTileTitle.setTitle("");
        lblTileTitle.setSize("150px", "25px");
        sectorMenu.add(lblTileTitle, 3, 1);

        lblCoordinates = new Label("");
        lblCoordinates.setStyleName("clearFont");
        lblCoordinates.setTitle("Sector coordinates");
        sectorMenu.add(this.lblCoordinates, 105, 12);

        corpImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png");
        corpImg.setSize(SIZE_33PX, "");
        corpImg.setTitle("Corps");
        sectorMenu.add(corpImg, 3, 30);

        armyImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/army.png");
        armyImg.setSize(SIZE_33PX, "");
        armyImg.setTitle("Armies");
        armyImg.setVisible(false);
        sectorMenu.add(armyImg, 43, 30);

        warShipImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        warShipImg.setSize(SIZE_33PX, "");
        warShipImg.setTitle("War ships");
        warShipImg.setVisible(false);
        sectorMenu.add(warShipImg, 83, 30);

        merchShipImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        merchShipImg.setSize(SIZE_33PX, "");
        merchShipImg.setTitle("Merchant ships");
        merchShipImg.setVisible(false);
        sectorMenu.add(merchShipImg, 123, 30);

        payedImg = new Image("http://static.eaw1805.com/tiles/resources/gray-resource-1.png");
        payedImg.setSize("15px", "");
        payedImg.setVisible(false);
        sectorMenu.add(payedImg, 3, 56);

        buildProgressImg = new Image("");
        buildProgressImg.setSize("15px", "");
        buildProgressImg.setVisible(false);
        sectorMenu.add(buildProgressImg, 96, 68);

        conquerImg = new Image("");
        conquerImg.setSize("15px", "");
        conquerImg.setVisible(false);
        sectorMenu.add(conquerImg, 38, 68);

        nationImg = new Image("");
        nationImg.setSize(SIZE_47PX, "29px");
        nationImg.setVisible(false);
        sectorMenu.add(nationImg, 3, 107);

        homeImg = new Image("");
        homeImg.setSize(SIZE_47PX, "29px");
        homeImg.setVisible(false);
        sectorMenu.add(homeImg, 112, 107);

        psImg = new Image("");
        this.psImg.setSize(SIZE_47PX, SIZE_47PX);
        psImg.setStyleName("pointer");
        sectorMenu.add(psImg, 112, 61);

        citizensImg = new Image("http://static.eaw1805.com/images/goods/good-2.png");
        citizensImg.setTitle("Population.");
        citizensImg.setSize("25px", "25px");
        citizensImg.setVisible(false);
        sectorMenu.add(citizensImg, 3, 77);

        lblTerrainType = new Label("");
        lblTerrainType.setStyleName("clearFontSmall");
        lblTerrainType.setSize("120px", "13px");
        sectorMenu.add(lblTerrainType, 3, 51);

        lblFortType = new Label("");
        lblFortType.setStyleName("clearFontSmall");
        lblFortType.setSize("70px", "13px");
        sectorMenu.add(lblFortType, 111, 51);

        lblPopulation = new Label("0");
        lblPopulation.setStyleName("clearFont");
        lblPopulation.setVisible(false);
        lblPopulation.setSize("63px", "13px");
        sectorMenu.add(lblPopulation, 38, 84);

        resourceImg = new Image("http://static.eaw1805.com/tiles/resources/resource-0.png");
        resourceImg.setTitle("Special Resource");
        resourceImg.setVisible(false);
        resourceImg.setSize("24px", "24px");
        sectorMenu.add(resourceImg, 68, 108);

        togglePanelImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
        togglePanelImg.setTitle("Minimize panel");
        togglePanelImg.setStyleName("pointer");
        add(this.togglePanelImg, 9, 15);

//        UnitEventManager.addUnitSelectedHandler(new UnitSelectedHandler() {
//            @Override
//            public void onUnitSelected(UnitSelectedEvent event) {
//                switch (event.getInfoType()) {
//                    case ARMY:
//                        showTip(new ArmyBrowserMini(ArmyStore.getInstance().getArmyById(event.getInfoId())), true, true);
//                        break;
//                    case CORPS:
//                        showTip(new CorpsBrowserMini(ArmyStore.getInstance().getCorpByID(event.getInfoId())), true, true);
//                        break;
//                    case BRIGADE:
//                        showTip(new BrigadeBrowserMini(ArmyStore.getInstance().getBrigadeById(event.getInfoId())), true, true);
//                        break;
//                    default:
//                        removeTip(true);
//                        break;
//                }
//            }
//        });
//
//        UnitEventManager.addUnitUndoSelectedHandler(new UnitUndoSelectedHandler() {
//            @Override
//            public void onUnitUndoSelected(UnitUndoSelectedEvent event) {
//                removeTip(true);
//            }
//        });
        //if it is mobile device and not tutorial mode.. flag panel as minimized.
        if (GameStore.getInstance().isMobileDevice() && !TutorialStore.getInstance().isTutorialMode()) {
            maximized = false;
        }

    }

    public void initToggleHandler(final AbsolutePanel unitsMenuContainer,
                                  final OptionsMenu optionsMenu) {
        final SectorMenu myself = this;
        final Timer opener;
        final Timer closer;
        opener = new Timer() {
            @Override
            public void run() {
                final int height = Window.getClientHeight();
                final LayoutView view = GameStore.getInstance().getLayoutView();

                //update sectors menu new position
                view.setWidgetPosition(myself, view.getWidgetX(myself), view.getWidgetY(myself) - 9, false, true);
                int status = 0;
                if (view.getWidgetY(myself) <= height - 194) {
                    view.setWidgetPosition(myself, view.getWidgetX(myself), height - 194, false, true);
                    status++;
                }

                //update units menu new position
                view.setWidgetPosition(unitsMenuContainer, view.getWidgetX(unitsMenuContainer), view.getWidgetY(unitsMenuContainer) - 9, false, true);
                if (view.getWidgetY(unitsMenuContainer) <= height - 165) {
                    view.setWidgetPosition(unitsMenuContainer, view.getWidgetX(unitsMenuContainer), height - 165, false, true);
                    status++;
                }

                //update options menu position
                view.setWidgetPosition(optionsMenu, view.getWidgetX(optionsMenu), view.getWidgetY(optionsMenu) - 9, false, true);
                if (view.getWidgetY(optionsMenu) <= height - 93) {
                    view.setWidgetPosition(optionsMenu, view.getWidgetX(optionsMenu), height - 93, false, true);
                    status++;
                }
                if (status == 3) {
                    view.setWidgetPosition(view.getEconomyView().getAnimatedPopups()[view.getEconomyView().getAnimatedPopups().length - 2], 399, height - 180, false, false);
                    view.setWidgetPosition(view.getEconomyView().getAnimatedPopups()[view.getEconomyView().getAnimatedPopups().length - 1], 454, height - 180, false, false);
                    view.setWidgetPosition(view.getEconomyView().getWarningPopups()[view.getEconomyView().getWarningPopups().length - 2], 394, height - 133, false, false);
                    view.setWidgetPosition(view.getEconomyView().getWarningPopups()[view.getEconomyView().getWarningPopups().length - 1], 448, height - 133, false, false);
                    this.cancel();
                }
            }
        };

        closer = new Timer() {
            @Override
            public void run() {
                final int height = Window.getClientHeight();
                final LayoutView view = GameStore.getInstance().getLayoutView();
                int status = 0;

                //update sectors menu position
                view.setWidgetPosition(myself, view.getWidgetX(myself), view.getWidgetY(myself) + 9, false, true);
                if (view.getWidgetY(myself) >= (height - 36)) {
                    view.setWidgetPosition(myself, view.getWidgetX(myself), height - 36, false, true);
                    status++;
                }

                //update units menu position
                view.setWidgetPosition(unitsMenuContainer, view.getWidgetX(unitsMenuContainer), view.getWidgetY(unitsMenuContainer) + 9, false, true);
                if (view.getWidgetY(unitsMenuContainer) >= (height - 7)) {
                    view.setWidgetPosition(unitsMenuContainer, view.getWidgetX(unitsMenuContainer), height - 7, false, true);
                    status++;
                }

                //update options menu position
                view.setWidgetPosition(optionsMenu, view.getWidgetX(optionsMenu), view.getWidgetY(optionsMenu) + 9, false, true);
                if (view.getWidgetY(optionsMenu) >= (height + 65)) {
                    view.setWidgetPosition(optionsMenu, view.getWidgetX(optionsMenu), height + 65, false, true);
                    status++;
                }

                if (status == 3) {
                    view.setWidgetPosition(view.getEconomyView().getAnimatedPopups()[view.getEconomyView().getAnimatedPopups().length - 2], 399, height - 40, false, false);
                    view.setWidgetPosition(view.getEconomyView().getAnimatedPopups()[view.getEconomyView().getAnimatedPopups().length - 1], 454, height - 40, false, false);
                    view.setWidgetPosition(view.getEconomyView().getWarningPopups()[view.getEconomyView().getWarningPopups().length - 2], 394, height, false, false);
                    view.setWidgetPosition(view.getEconomyView().getWarningPopups()[view.getEconomyView().getWarningPopups().length - 1], 448, height, false, false);
                    this.cancel();
                }
            }
        };

        //init toggle panel image click handler
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (maximized) {
                    opener.cancel();
                    closer.scheduleRepeating(9);
                    maximized = false;
                } else {
                    closer.cancel();
                    opener.scheduleRepeating(9);
                    maximized = true;

                }
            }
        }).addToElement(togglePanelImg.getElement()).register();
    }

    public void removeTip(final boolean hard) {
        if (hard || !tooltipLocked) {
            tooltip.clear();
            tooltip.hide();
            tooltipLocked = false;
        }
    }

    public void showTip(final Widget widget, final boolean setLocked, final boolean hard) {
        if (hard || !tooltipLocked) {
            tooltip.setWidget(widget);
            tooltip.showRelativeTo(this);
            tooltipLocked = setLocked;
        }
    }

    public void setSelectedSectorInfo(final SectorDTO sector) {
        removeTip(false);
        if (sector != null) {
            if (sector.getTradeCity()) {
                final TradeCityDTO tradeCity = TradeCityStore.getInstance().getTradeCityByPosition(sector);
                if (!GameStore.getInstance().isShowTradeCities()) {
                    showTip(TradeCityStore.getInstance().getTradeCityGoodsPopupById(tradeCity.getId()), false, false);
                }
                lblTileTitle.setText(tradeCity.getName());

                // Report fortifications
                switch (sector.getProductionSiteId()) {
                    case ProductionSiteConstants.PS_BARRACKS_FS:
                        lblFortType.setText("Small");
                        break;

                    case ProductionSiteConstants.PS_BARRACKS_FM:
                        lblFortType.setText("Medium");
                        break;

                    case ProductionSiteConstants.PS_BARRACKS_FL:
                        lblFortType.setText("Large");
                        break;

                    case ProductionSiteConstants.PS_BARRACKS_FH:
                        lblFortType.setText("Huge");
                        break;

                    default:
                        lblFortType.setText("");
                }

            } else if (sector.getVisible() &&
                    (sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS
                            || sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS_FH
                            || sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS_FL
                            || sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS_FM
                            || sector.getProductionSiteId() == ProductionSiteConstants.PS_BARRACKS_FS)) {

                // Retrieve barrack
                final BarrackDTO barrack = BarrackStore.getInstance().getBarrackByPosition(sector);
                if (barrack == null) {
                    lblTileTitle.setText(sector.getName());

                } else {
                    lblTileTitle.setText(barrack.getName());
                }

                // Report fortifications
                switch (sector.getProductionSiteId()) {
                    case ProductionSiteConstants.PS_BARRACKS_FS:
                        lblFortType.setText("Small");
                        break;

                    case ProductionSiteConstants.PS_BARRACKS_FM:
                        lblFortType.setText("Medium");
                        break;

                    case ProductionSiteConstants.PS_BARRACKS_FL:
                        lblFortType.setText("Large");
                        break;

                    case ProductionSiteConstants.PS_BARRACKS_FH:
                        lblFortType.setText("Huge");
                        break;

                    default:
                        lblFortType.setText("");
                }

            } else {
                lblTileTitle.setText(sector.getName());
                lblFortType.setText("");
            }

            lblCoordinates.setText(sector.positionToString());
            lblTerrainType.setText(sector.getTerrain().getName());
            lblPopulation.setText(numberFormat.format(sector.populationCount()));
            if (sector.getTerrain().getId() != TERRAIN_O) {
                if (sector.getNationId() > 0) {
                    lblPopulation.setVisible(sector.getVisible());
                    citizensImg.setVisible(sector.getVisible());

                    nationImg.setVisible(true);
                    nationImg.setUrl("http://static.eaw1805.com/images/nations/nation-" + sector.getNationId() + "-120.png");
                    nationImg.setTitle("This tile belongs to " + sector.getNationDTO().getName() + ".");

                } else {
                    lblPopulation.setVisible(false);
                    citizensImg.setVisible(false);
                    nationImg.setVisible(false);
                }

                final NationDTO nation = DataStore.getInstance().getNationByCode(sector.getPoliticalSphere());
                if (nation != null && nation.getNationId() > 0) {
                    homeImg.setVisible(true);
                    homeImg.setUrl("http://static.eaw1805.com/images/nations/nation-" + nation.getNationId() + "-120.png");
                    homeImg.setTitle("Home nation");

                } else {
                    homeImg.setVisible(false);
                }
            } else {
                lblPopulation.setVisible(false);
                citizensImg.setVisible(false);
                nationImg.setVisible(false);
                homeImg.setVisible(false);
            }

            if (sector.getVisible()) {
                if (sector.getNatResId() > 0) {
                    resourceImg.setVisible(true);
                    resourceImg.setUrl("http://static.eaw1805.com/tiles/resources/resource-" + sector.getNatResId() + ".png");

                } else {
                    resourceImg.setVisible(false);
                }

                if (sector.getBuildProgress() > 0) {
                    switch (sector.getBuildProgress()) {
                        case 1:
                            buildProgressImg.setUrl("http://static.eaw1805.com/tiles/progress/prog4of5.png");
                            break;

                        case 2:
                            buildProgressImg.setUrl("http://static.eaw1805.com/tiles/progress/prog3of5.png");
                            break;

                        default:
                            buildProgressImg.setUrl("http://static.eaw1805.com/tiles/progress/prog2of5.png");
                    }
                    buildProgressImg.setVisible(true);

                } else {
                    buildProgressImg.setVisible(false);
                }

                if (sector.getConqueredCounter() > 0) {
                    conquerImg.setUrl("http://static.eaw1805.com/tiles/progress/prog" + (6 - sector.getConqueredCounter()) + "of5.png");
                    conquerImg.setVisible(true);

                } else {
                    conquerImg.setVisible(false);
                }

                armyImg.setVisible(ArmyStore.getInstance().hasSectorArmies2(sector));
                corpImg.setVisible(ArmyStore.getInstance().hasSectorFreeCorps(sector));
                warShipImg.setVisible(NavyStore.getInstance().hasWarShips(sector, true));
                merchShipImg.setVisible(NavyStore.getInstance().hasMerchantShips(sector, true));

                payedImg.setVisible(false);
                setProductionSiteInfo(sector);

            } else {
                conquerImg.setVisible(false);
                buildProgressImg.setVisible(false);
                payedImg.setVisible(false);
                lblPopulation.setVisible(false);
                citizensImg.setVisible(false);
                resourceImg.setVisible(false);
                armyImg.setVisible(false);
                corpImg.setVisible(false);
                warShipImg.setVisible(false);
                merchShipImg.setVisible(false);
            }
        }
    }


    private void setProductionSiteInfo(final SectorDTO sector) {

        if (sector.getVisible()) {

            if (sector.getProductionSiteId() > 0) {
                psImg.setVisible(true);
                final String name = sector.getProductionSiteDTO().getName();
                if (sector.getBuildProgress() > 0) {
                    psImg.setUrl("http://static.eaw1805.com/tiles/sites/tprod-16-half.png");

                } else {
                    psImg.setUrl("http://static.eaw1805.com/tiles/sites/tprod-" + sector.getProductionSiteId() + ".png");
                }
                psImg.setTitle(name);
                payedImg.setVisible(!sector.getPayed());

            } else if (eStore.getSectorProdSites().containsKey(sector)) {
                final int prodSiteId = eStore.getSectorProdSites().get(sector);
                psImg.setVisible(true);
                final String name = dStore.getProdSite(prodSiteId).getName();
                if (sector.getBuildProgress() > 0) {
                    psImg.setUrl("http://static.eaw1805.com/tiles/sites/tprod-16-half.png");

                } else {
                    psImg.setUrl("http://static.eaw1805.com/tiles/sites/tprod-" + prodSiteId + ".png");
                }
                psImg.setTitle(name);
                payedImg.setVisible(!sector.getPayed());

            } else {
                psImg.setVisible(false);
                psImg.setUrl("");
                psImg.setTitle("No production site available. Build a new one!");
                payedImg.setVisible(false);
            }

        } else {
            psImg.setVisible(false);
            psImg.setUrl("");
            psImg.setTitle("No production site available! Cannot build on enemy territory!");
        }
    }

    public boolean isMaximized() {
        return maximized;
    }

}
