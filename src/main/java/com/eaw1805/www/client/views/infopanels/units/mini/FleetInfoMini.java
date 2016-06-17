package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.trade.GetGoodEvent;
import com.eaw1805.www.client.events.trade.GetGoodHandler;
import com.eaw1805.www.client.events.trade.GiveGoodEvent;
import com.eaw1805.www.client.events.trade.GiveGoodHandler;
import com.eaw1805.www.client.events.trade.TradeEventManager;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CommandersViewerPopup;
import com.eaw1805.www.client.views.popups.SpiesViewerPopup;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import com.eaw1805.www.shared.stores.util.NavyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FleetInfoMini
        extends AbstractInfoPanel
        implements ArmyConstants {

    private FleetDTO fleet = new FleetDTO();
    private Label lblFleetName, lblBattShips, lblMerchShips;
    private Label lblMps;
    private Image selectedImg;
    private boolean isSelected = false;
    private Label lblMarines;
    private Label lblAvailable;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private AbsolutePanel goodsPanel;
    private final UnitChangedHandler unitChangedHandler;

    public FleetInfoMini(final FleetDTO value, final boolean enabled) {
        fleet = value;
        setStyleName("pointer");
        setSize("170px", "89px");

        final ClickAbsolutePanel fleetInfoPanel = new ClickAbsolutePanel();
        fleetInfoPanel.setStyleName("shipMiniInfoPanel pointer");
        fleetInfoPanel.setSize("170px", "89px");
        add(fleetInfoPanel, 0, 0);

        lblFleetName = new Label("Fleet Name");
        lblFleetName.setStyleName("clearFontSmall");
        fleetInfoPanel.add(lblFleetName, 4, 4);
        this.lblFleetName.setSize("156px", "25px");

        final Image btShipsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        btShipsImg.setTitle("Battle ships");
        btShipsImg.setSize("25px", "15px");
        fleetInfoPanel.add(btShipsImg, 4, 52);

        lblBattShips = new Label("x");
        lblBattShips.setStyleName("clearFontMini");
        fleetInfoPanel.add(lblBattShips, 32, 53);

        final Image merchShipsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        merchShipsImg.setTitle("Merchant ships");
        merchShipsImg.setSize("25px", "15px");
        fleetInfoPanel.add(merchShipsImg, 4, 69);

        lblMerchShips = new Label("y");
        lblMerchShips.setStyleName("clearFontMini");
        fleetInfoPanel.add(lblMerchShips, 32, 70);

        lblMps = new Label("");
        lblMps.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMps.setStyleName("clearFontMini");
        lblMps.setSize("50px", "15px");
        fleetInfoPanel.add(lblMps, 114, 70);

        int totalCapacity = 0;
        int totalMarines = 0;
        for (ShipDTO ship : fleet.getShips().values()) {
            totalCapacity += ship.getType().getLoadCapacity();
            totalMarines += ship.getMarines();
        }

        lblAvailable = new Label(numberFormat.format(totalCapacity) + " tons");
        lblAvailable.setStyleName("clearFontSmall");
        fleetInfoPanel.add(lblAvailable, 4, 33);
        this.lblAvailable.setSize("80px", "15px");

        lblMarines = new Label(numberFormat.format(totalMarines) + " marines");
        lblMarines.setStyleName("clearFontSmall");
        fleetInfoPanel.add(lblMarines, 4, 21);


        this.selectedImg = new Image("http://static.eaw1805.com/images/infopanels/selected.png");
        add(this.selectedImg, 0, 0);
        this.selectedImg.setStyleName("pointer");
        this.selectedImg.setSize("170px", "89px");

        deselect();

        UnitEventManager.addUnitChangedHandler(new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == FLEET) {
                    setUpLabels();
                }

            }
        });

        goodsPanel = new AbsolutePanel();
        goodsPanel.setSize("70px", "54px");
        add(goodsPanel, 112, 21);

        TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {
            public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                if (getGoodEvent.getUnitType() == SHIP
                        || (getGoodEvent.getUnitType() == FLEET
                        && (getGoodEvent.getUnitId() == fleet.getFleetId()
                        || getGoodEvent.getUnitId() == 0))) {
                    if (getGoodEvent.getRegionId() == MapStore.getInstance().getActiveRegion()) {
                        setupGoodsPanel();
                    }
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
                }
            }
        });
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (((event.getInfoType() == FLEET && event.getInfoId() == fleet.getFleetId()) ||
                        (fleet.getFleetId() == 0))) {
                    setupGoodsPanel();
                    setUpLabels();

                }
            }
        };

        setupGoodsPanel();

        setUpLabels();

        if (!enabled) {
            final Image disabledImage = new Image("http://static.eaw1805.com/images/infopanels/disabledMini.png");
            disabledImage.setTitle("Further orders are disabled due to orders already issued that may be in conflict");
            add(disabledImage);
        }
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    /**
     * Setup images with goods that are loaded in the fleet.
     */
    public void setupGoodsPanel() {
        goodsPanel.clear();
        //calculate goods in fleet.
        final Map<Integer, Integer> goodToQuantity = new HashMap<Integer, Integer>();
        final Map<Integer, String> goodToName = new HashMap<Integer, String>();

        for (int index = 1; index < 15; index++) {
            goodToQuantity.put(index, 0);
        }

        for (final ShipDTO shipDTO : fleet.getShips().values()) {
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

//                if (shipDTO.getLoadedUnitsMap() != null) {
//                    for (Map.Entry<Integer, List<Integer>> entry : shipDTO.getLoadedUnitsMap().entrySet()) {
//                        final int troopId = entry.getKey();
//                        final List<Integer> troops = entry.getValue();
//                        if (unitsLoaded.containsKey(troopId)) {
//                            unitsLoaded.get(troopId).addAll(troops);
//
//                        } else {
//                            unitsLoaded.put(troopId, troops);
//                        }
//                    }
//                }
            }
        }

        int posX = 0;
        int posY = 0;
        int countGoods = 0;
        int goodsPerColumn = 4;
        for (int index = 0; index < 14; index++) {
            final Image goodImage;
            if (goodToQuantity.get(index + 1) > 0) {
                goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + (index + 1) + ".png");

                goodsPanel.add(goodImage, posX, posY);

                goodImage.setSize("11px", "11px");

                if (goodToQuantity.get(index + 1) == null) {
                    goodImage.setTitle("null");
                } else {
                    goodImage.setTitle(goodToName.get(index + 1) + ":" + goodToQuantity.get(index + 1));
                }

                if ((countGoods + 1) % goodsPerColumn == 0 && countGoods != 0) {
                    posX += 12;
                    posY = 0;

                } else {
                    posY += 12;
                }

                countGoods++;
            }
        }

        if (fleet.getLoadedUnitsMap() != null) {
            if (fleet.getLoadedUnitsMap().containsKey(BRIGADE)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButArmiesOff.png");
                unitImage.setSize("11px", "11px");
                final List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();
                for (Integer brigId : fleet.getLoadedUnitsMap().get(BRIGADE)) {
                    brigades.add(ArmyStore.getInstance().getBrigadeById(brigId));
                }
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        new BrigadesViewerPopup(brigades, "Loaded brigades").open();
                    }
                }).addToElement(unitImage.getElement()).register();

                goodsPanel.add(unitImage, posX, posY);
                if ((countGoods + 1) % goodsPerColumn == 0 && countGoods != 0) {
                    posX += 12;
                    posY = 0;

                } else {
                    posY += 12;
                }
                countGoods++;
            }

            if (fleet.getLoadedUnitsMap().containsKey(COMMANDER)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButCommandersOff.png");
                unitImage.setSize("11px", "11px");
                final List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
                for (Integer commId : fleet.getLoadedUnitsMap().get(COMMANDER)) {
                    commanders.add(CommanderStore.getInstance().getCommanderById(commId));
                }

                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        new CommandersViewerPopup(commanders, "Loaded commanders").open();
                    }
                }).addToElement(unitImage.getElement()).register();

                goodsPanel.add(unitImage, posX, posY);
                if ((countGoods + 1) % goodsPerColumn == 0 && countGoods != 0) {
                    posX += 12;
                    posY = 0;
                } else {
                    posY += 12;
                }
            }

            if (fleet.getLoadedUnitsMap().containsKey(SPY)) {
                final Image unitImage = new Image("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSpiesOff.png");
                unitImage.setSize("11px", "11px");
                final List<SpyDTO> spies = new ArrayList<SpyDTO>();
                for (Integer spyId : fleet.getLoadedUnitsMap().get(SPY)) {
                    spies.add(SpyStore.getInstance().getSpyById(spyId));
                }

                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        new SpiesViewerPopup(spies, "Loaded spies").open();
                    }
                }).addToElement(unitImage.getElement()).register();
                goodsPanel.add(unitImage, posX, posY);
            }
        }
    }

    private void setUpLabels() {
        final NavyUnitInfoDTO fleetInfo = MiscCalculators.getFleetInfo(fleet);
        if (!fleet.getShips().isEmpty() && fleet.getShips().values().iterator().next().getFleet() == 0) {
            lblFleetName.setText("Ships in no fleet.");

        } else if (fleet != null && !fleet.getName().equals("")) {
            lblFleetName.setText(fleet.getName());

        } else {
            lblFleetName.setText("Click to set name");
        }

        lblBattShips.setText(fleetInfo.getWarShips() + " Battle Ships");
        lblMerchShips.setText(fleetInfo.getMerchantShips() + " Merchant Ships");
        lblMps.setText(fleetInfo.getMps() + " MPs");

        int totalCapacity = 0;
        int totalMarines = 0;
        for (ShipDTO ship : fleet.getShips().values()) {
            totalCapacity += ship.getType().getLoadCapacity();
            totalMarines += ship.getMarines();
        }

        lblAvailable.setText(numberFormat.format(totalCapacity) + " tons");
        lblMarines.setText(numberFormat.format(totalMarines) + " marines");
    }

    public void select() {
        isSelected = true;
        selectedImg.setVisible(true);
    }

    public void deselect() {
        isSelected = false;
        selectedImg.setVisible(false);
    }

    public void MouseOver() {
        if (!isSelected) {
            selectedImg.setVisible(true);
        }
    }

    public void MouseOut() {
        if (!isSelected) {
            selectedImg.setVisible(false);
        }
    }

    public FleetDTO getFleet() {
        return fleet;
    }

}
