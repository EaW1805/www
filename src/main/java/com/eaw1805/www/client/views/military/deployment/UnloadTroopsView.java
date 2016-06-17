package com.eaw1805.www.client.views.military.deployment;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NavigationConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.CargoUnitDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.FlowPanelScrollChild;
import com.eaw1805.www.client.views.figures.BrigadeInfoFigPanel;
import com.eaw1805.www.client.views.figures.CommanderInfoFigPanel;
import com.eaw1805.www.client.views.figures.SpyInfoFigPanel;
import com.eaw1805.www.client.views.infopanels.units.BaggageTrainInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.FleetInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ShipInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.client.widgets.UnloadDragController;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.List;

public class UnloadTroopsView
        extends DraggablePanel
        implements ArmyConstants, RegionConstants, TerrainConstants, NavigationConstants {

    private StyledCheckBox firstPhaseBox, secondPhaseBox;
    private int tradePhase = 1;
    private AbsolutePanel selUnitPanel;
    private FlowPanelScrollChild cargoUnitsPanel;
    private ClickAbsolutePanel northPanel;
    private ClickAbsolutePanel southPanel;
    private ClickAbsolutePanel westPanel;
    private ClickAbsolutePanel eastPanel;
    private ClickAbsolutePanel tilePanel;
    private PickupDragController dndController;
    private AbsolutePanel[] navCargoPanels = new AbsolutePanel[5];
    private int transportType, transportId;
    private TransportUnitDTO thisTrUnit;
    private final AbsolutePositionDropController tileController;
    private final AbsolutePositionDropController northController;
    private final AbsolutePositionDropController southController;
    private final AbsolutePositionDropController westController;
    private final AbsolutePositionDropController eastController;

    public UnloadTroopsView(int type, TransportUnitDTO trUnit) {
        thisTrUnit = trUnit;
        this.setSize("1218px", "680px");
        this.setStyleName("disembarkView");
        dndController = new PickupDragController(this, false);
        this.dndController.addDragHandler(new UnloadDragController(this));
        this.transportType = type;
        this.transportId = trUnit.getId();
        final Label lblPanelTitle = new Label("Disembark troops panel");
        lblPanelTitle.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblPanelTitle.setStyleName("clearFont-large whiteText");
        this.add(lblPanelTitle, 0, 22);
        lblPanelTitle.setSize("100%", "30px");

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close panel");
        this.add(imgX, 1141, 15);
        imgX.setSize("36px", "36px");
        final UnloadTroopsView self = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();


        final boolean hasMoved;
        if (trUnit.getNationId() == GameStore.getInstance().getNationId()) {//if it is our unit
            hasMoved = MovementStore.getInstance().hasMovedThisTurn(transportType, transportId);
        } else {//if it is allied unit
            hasMoved = AlliedUnitsStore.getInstance().hasUnitMoved(transportType, transportId, trUnit.getRegionId(), trUnit.getNationId());
        }
        firstPhaseBox = new StyledCheckBox("First load phase", !hasMoved, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (firstPhaseBox.isChecked() || !hasMoved) {
                    firstPhaseBox.setChecked(true);
                    secondPhaseBox.setChecked(false);
                    setTradePhase(1);
                } else {
                    secondPhaseBox.setChecked(true);
                    setTradePhase(2);
                }
            }
        }).addToElement(firstPhaseBox.getCheckBox().getElement()).register();

        this.firstPhaseBox.setText("First loading phase");
        this.add(this.firstPhaseBox, 120, 75);
        secondPhaseBox = new StyledCheckBox("Second load phase", hasMoved, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (secondPhaseBox.isChecked() && hasMoved) {
                    firstPhaseBox.setChecked(false);
                    setTradePhase(2);
                } else {
                    secondPhaseBox.setChecked(false);
                    firstPhaseBox.setChecked(true);
                    setTradePhase(1);
                }
            }
        }).addToElement(secondPhaseBox.getCheckBox().getElement()).register();

        this.secondPhaseBox.setText("Second loading phase");
        this.add(this.secondPhaseBox, 921, 75);
        this.secondPhaseBox.setSize("203px", "21px");

        this.selUnitPanel = new AbsolutePanel();
        this.add(this.selUnitPanel, 109, 154);
        this.selUnitPanel.setSize("383px", "97px");

        final Label label = new Label("Click a unit to load with units");
        label.setStyleName("blackText");
        label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.selUnitPanel.add(label, -431, -254);
        label.setSize("363px", "15px");


        this.cargoUnitsPanel = new FlowPanelScrollChild();
        final ScrollVerticalBarEAW scrollPanel = new ScrollVerticalBarEAW(cargoUnitsPanel, false);
        scrollPanel.setSize(510, 192);
        this.add(scrollPanel, 119, 412);


        this.cargoUnitsPanel.setSize("489px", "188px");

        final Label lblSelectedUnit = new Label("Selected Unit:");
        lblSelectedUnit.setStyleName("clearFontMedLarge whiteText");
        this.add(lblSelectedUnit, 120, 125);

        final Label lblCargoHold = new Label("Cargo Hold:");
        lblCargoHold.setStyleName("clearFontMedLarge whiteText");
        this.add(lblCargoHold, 120, 376);

        this.northPanel = new ClickAbsolutePanel();
        this.northPanel.setId(NORTH);
        this.add(this.northPanel, 805, 191);
        this.northPanel.setSize("78px", "78px");

        this.southPanel = new ClickAbsolutePanel();
        this.southPanel.setId(SOUTH);
        this.southPanel.setStyleName("southPanel");
        this.add(this.southPanel, 805, 445);
        this.southPanel.setSize("78px", "78px");

        this.westPanel = new ClickAbsolutePanel();
        this.westPanel.setId(WEST);
        this.westPanel.setStyleName("westPanel");
        this.add(this.westPanel, 670, 317);
        this.westPanel.setSize("78px", "78px");

        this.eastPanel = new ClickAbsolutePanel();
        this.eastPanel.setId(EAST);
        this.eastPanel.setStyleName("eastPanel");
        this.add(this.eastPanel, 940, 317);
        this.eastPanel.setSize("78px", "78px");

        this.tilePanel = new ClickAbsolutePanel();
        this.tilePanel.setId(0);
        this.tilePanel.setStyleName("tilePanel");
        this.add(this.tilePanel, 804, 318);
        this.tilePanel.setSize("78px", "78px");

        tileController = new AbsolutePositionDropController(tilePanel);
        northController = new AbsolutePositionDropController(northPanel);
        southController = new AbsolutePositionDropController(southPanel);
        westController = new AbsolutePositionDropController(westPanel);
        eastController = new AbsolutePositionDropController(eastPanel);

        if (hasMoved) {
            setTradePhase(2);
        } else {
            setTradePhase(1);
        }

        navCargoPanels[NORTH] = new AbsolutePanel();
        this.add(navCargoPanels[NORTH], 716, 152);
        navCargoPanels[NORTH].setSize("256px", "32px");

        navCargoPanels[SOUTH] = new AbsolutePanel();
        this.add(navCargoPanels[SOUTH], 716, 527);
        navCargoPanels[SOUTH].setSize("256px", "32px");

        navCargoPanels[WEST] = new AbsolutePanel();
        this.add(navCargoPanels[WEST], 506, 280);
        navCargoPanels[WEST].setSize("256px", "32px");

        navCargoPanels[EAST] = new AbsolutePanel();
        this.add(navCargoPanels[EAST], 937, 280);
        navCargoPanels[EAST].setSize("256px", "32px");

        navCargoPanels[0] = new AbsolutePanel();
        this.add(navCargoPanels[0], 716, 405);
        navCargoPanels[0].setSize("256px", "32px");

        final Label lblDragAndDrop = new Label("Drag and Drop unit on the you want it to disembark");
        lblDragAndDrop.setStyleName("clearFontMedium whiteText");
        this.add(lblDragAndDrop, 671, 125);

        setTransportUnit(trUnit);
        initDropPanels(trUnit);
        populateDirectionalHolders();
    }


    /**
     * Method that registers drop controllers on
     * the drop target available for the units to
     * disembark on.
     *
     * @param trUnit the transport unit
     */
    private void initDropPanels(final TransportUnitDTO trUnit) {
        final SectorDTO[][] sectors = RegionStore.getInstance()
                .getRegionSectorsByRegionId(trUnit.getRegionId());
        final int x;
        final int y;
        if (tradePhase == 1) {
            x = trUnit.getXStart();
            y = trUnit.getYStart();
        } else {
            x = trUnit.getX();
            y = trUnit.getY();
        }
        final int sizeX = sectors.length;
        final int sizeY = sectors[0].length;
        if (sectors[x][y].getTerrainId() != TERRAIN_O) {
            this.tilePanel.setStyleName("tilePanel");
            dndController.registerDropController(tileController);

        } else {
            this.tilePanel.setStyleName("tilePanelDisabled");
//            dndController.unregisterDropController(tileController);
        }

        if (y - 1 >= 0 && sectors[x][y - 1].getTerrainId() != TERRAIN_O) {
            this.northPanel.setStyleName("northPanel");
            dndController.registerDropController(northController);


        } else {
            this.northPanel.setStyleName("northPanelDisabled");
//            dndController.unregisterDropController(northController);
        }

        if (y + 1 < sizeY && sectors[x][y + 1].getTerrainId() != TERRAIN_O) {
            this.southPanel.setStyleName("southPanel");
            dndController.registerDropController(southController);

        } else {
            this.southPanel.setStyleName("southPanelDisabled");
//            dndController.unregisterDropController(southController);
        }

        if (x - 1 >= 0 && sectors[x - 1][y].getTerrainId() != TERRAIN_O) {
            this.westPanel.setStyleName("westPanel");
            dndController.registerDropController(westController);

        } else {
            this.westPanel.setStyleName("westPanelDisabled");
//            dndController.unregisterDropController(westController);
        }

        if (x + 1 < sizeX && sectors[x + 1][y].getTerrainId() != TERRAIN_O) {
            this.eastPanel.setStyleName("eastPanel");
            dndController.registerDropController(eastController);

        } else {
            this.eastPanel.setStyleName("eastPanelDisabled");
//            dndController.unregisterDropController(eastController);
        }
    }

    /**
     * Method that places the selected transport unit in the
     * selected unit panel and populates the cargo panel
     * with the loaded units
     *
     * @param currUnit the transport unit that was selected
     */
    private void setTransportUnit(final TransportUnitDTO currUnit) {
        selUnitPanel.clear();
        switch (currUnit.getUnitType()) {
            case BAGGAGETRAIN:
                selUnitPanel.add(new BaggageTrainInfoPanel((BaggageTrainDTO) currUnit), 13, 6);
                break;

            case FLEET:
                selUnitPanel.add(new FleetInfoPanel((FleetDTO) currUnit, false), 13, 6);
                break;

            case SHIP:
                selUnitPanel.add(new ShipInfoPanel((ShipDTO) currUnit, false), 13, 6);
                break;

            default:
                break;

        }
        populateCargoPanel(currUnit);
    }

    /**
     * Method that populates the cargo panel with
     * the units loaded on the current unit
     *
     * @param currUnit the selected unit whose cargo
     *                 will be added to the cargo panel
     */
    private void populateCargoPanel(final TransportUnitDTO currUnit) {
        cargoUnitsPanel.clear();
        final List<CargoUnitDTO> disabledUnits = new ArrayList<CargoUnitDTO>();
        for (final int type : currUnit.getLoadedUnitsMap().keySet()) {
            for (int index = 0; index < currUnit.getLoadedUnitsMap().get(type).size(); index++) {
                int id = currUnit.getLoadedUnitsMap().get(type).get(index);
                switch (type) {
                    case BRIGADE:
                        final BrigadeDTO brig = (BrigadeDTO) TransportStore.getInstance().getCargoUnitById(BRIGADE, id);
                        if (brig.getNationId() != GameStore.getInstance().getNationId() || TransportStore.getInstance().hasUnitLoadOrder(ArmyConstants.BRIGADE, id)) {
                            disabledUnits.add(brig);
                        } else {
                            final BrigadeInfoFigPanel bifp = new BrigadeInfoFigPanel(brig, false, "");
                            dndController.makeDraggable(bifp, bifp.getFigImg());
                            cargoUnitsPanel.add(bifp);
                        }
                        break;
                    case COMMANDER:
                        final CommanderDTO comm = (CommanderDTO) TransportStore.getInstance().getCargoUnitById(COMMANDER, id);
                        if (comm.getNationId() != GameStore.getInstance().getNationId() || TransportStore.getInstance().hasUnitLoadOrder(ArmyConstants.COMMANDER, id)) {
                            disabledUnits.add(comm);
                        } else {
                            final CommanderInfoFigPanel cifp = new CommanderInfoFigPanel(comm, false, "");
                            dndController.makeDraggable(cifp, cifp.getFigImg());
                            cargoUnitsPanel.add(cifp);
                        }
                        break;

                    case SPY:
                        final SpyDTO spy = (SpyDTO) TransportStore.getInstance().getCargoUnitById(SPY, id);
                        if (spy.getNationId() != GameStore.getInstance().getNationId() || TransportStore.getInstance().hasUnitLoadOrder(SPY, id)) {
                            disabledUnits.add(spy);
                        } else {
                            final SpyInfoFigPanel sifp = new SpyInfoFigPanel(spy, false, "");
                            dndController.makeDraggable(sifp, sifp.getFigImg());
                            cargoUnitsPanel.add(sifp);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        for (CargoUnitDTO cargo : disabledUnits) {
            if (cargo instanceof BrigadeDTO) {
                if (((BrigadeDTO) cargo).getNationId() != GameStore.getInstance().getNationId()) {
                    cargoUnitsPanel.add(new BrigadeInfoFigPanel((BrigadeDTO) cargo, true, "The brigade belongs to an ally. Allied units can only be unloaded by their owner"));
                } else {
                    cargoUnitsPanel.add(new BrigadeInfoFigPanel((BrigadeDTO) cargo, true, "The brigade was loaded this month. A unit cannot be boarded and un-boarded in the same turn"));
                }
            } else if (cargo instanceof CommanderDTO) {
                if (((CommanderDTO) cargo).getNationId() != GameStore.getInstance().getNationId()) {
                    cargoUnitsPanel.add(new CommanderInfoFigPanel((CommanderDTO) cargo, true, "The commander belongs to an ally. Allied units can only be unloaded by their owner"));
                } else {
                    cargoUnitsPanel.add(new CommanderInfoFigPanel((CommanderDTO) cargo, true, "The commander was loaded this month. A unit cannot be boarded and un-boarded in the same turn"));
                }

            } else {
                if (((SpyDTO) cargo).getNationId() != GameStore.getInstance().getNationId()) {
                    cargoUnitsPanel.add(new SpyInfoFigPanel((SpyDTO) cargo, true, "The spy belongs to an ally. Allied units can only be unloaded by their owner"));
                } else {
                    cargoUnitsPanel.add(new SpyInfoFigPanel((SpyDTO) cargo, true, "The spy was loaded this month. A unit cannot be boarded and un-boarded in the same turn"));
                }

            }
        }
        cargoUnitsPanel.resizeScrollBar();
    }

    public void populateDirectionalHolders() {
        for (int i = 0; i <= 4; i++) {
            navCargoPanels[i].clear();
        }

        final List<ClientOrderDTO> orders = OrderStore.getInstance().getUnloadOrders();
        for (ClientOrderDTO order : orders) {
            if (order.getIdentifier(0) == transportType && order.getIdentifier(1) == transportId) {
                final int cargoId = order.getIdentifier(3);
                final int direction = order.getIdentifier(4);
                switch (order.getIdentifier(2)) {
                    case BRIGADE:
                        final BrigadeInfoFigPanel bifp = new BrigadeInfoFigPanel(ArmyStore.getInstance().getBrigadeById(cargoId), false, "");
                        bifp.scaleToMini();
                        navCargoPanels[direction].add(bifp, navCargoPanels[direction].getWidgetCount() * 32, 0);
                        break;
                    case COMMANDER:
                        final CommanderInfoFigPanel cifp = new CommanderInfoFigPanel(CommanderStore.getInstance().getCommanderById(cargoId), false, "");
                        cifp.scaleToMini();
                        navCargoPanels[direction].add(cifp, navCargoPanels[direction].getWidgetCount() * 32, 0);
                        break;
                    case SPY:
                        final SpyInfoFigPanel sifp = new SpyInfoFigPanel(SpyStore.getInstance().getSpyById(cargoId), false, "");
                        sifp.scaleToMini();
                        navCargoPanels[direction].add(sifp, navCargoPanels[direction].getWidgetCount() * 32, 0);
                        break;
                    default:
                }
            }
        }

    }

    /**
     * @param tradePhase the tradePhase to set
     */
    public void setTradePhase(final int tradePhase) {
        this.tradePhase = tradePhase;
        initDropPanels(thisTrUnit);
    }

    /**
     * @return the tradePhase
     */
    public int getTradePhase() {
        return tradePhase;
    }

    public int getTransportId() {
        return transportId;
    }

    public int getTransportType() {
        return transportType;
    }
}
