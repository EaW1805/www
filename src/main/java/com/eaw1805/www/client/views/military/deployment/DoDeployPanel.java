package com.eaw1805.www.client.views.military.deployment;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.CargoUnitDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.deploy.DeployEventManager;
import com.eaw1805.www.client.events.deploy.EmbarkUnitEvent;
import com.eaw1805.www.client.events.deploy.EmbarkUnitHandler;
import com.eaw1805.www.client.gui.scroll.FlowPanelScrollChild;
import com.eaw1805.www.client.views.figures.BrigadeInfoFigPanel;
import com.eaw1805.www.client.views.figures.CommanderInfoFigPanel;
import com.eaw1805.www.client.views.figures.SpyInfoFigPanel;
import com.eaw1805.www.client.views.infopanels.units.BaggageTrainInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.FleetInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ShipInfoPanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ErrorPopup.Level;
import com.eaw1805.www.client.widgets.LoadDragController;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.SpyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import com.eaw1805.www.shared.stores.units.UnitService;

import java.util.ArrayList;
import java.util.List;

public class DoDeployPanel
        extends AbsolutePanel
        implements ArmyConstants {

    private AbsolutePanel[] unitPanels = new AbsolutePanel[15];
    private final AbsolutePanel selUnitPanel;
    private final FlowPanelScrollChild cargoUnitsPanel;
    private final FlowPanelScrollChild loadingUnitsPanel;
    private final FlowPanelScrollChild transportUnitsPanel;
    private int transportType, cargoType, x = 0, y = 0, regionId = 0;
    private int transportId = 0, tradePhase = 1;
    private PickupDragController dndController;

    private HandlerRegistration emdisHandle;
    private DeployTroopsView dpView;

    public DoDeployPanel(final int regionId, final int transportType, final int cargoType, final DeployTroopsView dpView) {
        this.dpView = dpView;
        this.dndController = new PickupDragController(this, false);
        this.dndController.addDragHandler(new LoadDragController(this));
        this.dndController.setBehaviorDragStartSensitivity(3);
        this.transportType = transportType;
        this.cargoType = cargoType;
        this.regionId = regionId;
        setStyleName("doDeployView");
        setSize("1098px", "560px");

        this.loadingUnitsPanel = new FlowPanelScrollChild();
        this.loadingUnitsPanel.getElement().setAttribute("style", "display:inline;");
        final ScrollVerticalBarEAW loadingUnitsScroller = new ScrollVerticalBarEAW(loadingUnitsPanel, false);
        loadingUnitsScroller.setSize(344, 532);
        add(loadingUnitsScroller, 13, 15);
        this.loadingUnitsPanel.setSize("320px", "532px");
        transportUnitsPanel = new FlowPanelScrollChild();
        transportUnitsPanel.getElement().setAttribute("style", "display:inline;");
        this.transportUnitsPanel.setSize("600px", "216px");

        int index = 0;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 4; j++) {
                this.unitPanels[index] = new AbsolutePanel();
                this.unitPanels[index].setStyleName("miniInfoPanel");
                this.unitPanels[index].setSize("120px", "72px");
                this.transportUnitsPanel.add(this.unitPanels[index]);
                index++;
            }
        }


        this.selUnitPanel = new AbsolutePanel();
        this.selUnitPanel.setStyleName("infoPanel");
        add(this.selUnitPanel, 430, 253);
        this.selUnitPanel.setSize("376px", "97px");

        final Label lblClickAUnit = new Label("Click a unit to load with units");
        lblClickAUnit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblClickAUnit.setStyleName("clearFontMedium");
        this.selUnitPanel.add(lblClickAUnit, 10, 42);
        lblClickAUnit.setSize("363px", "15px");

        this.cargoUnitsPanel = new FlowPanelScrollChild();
        final ScrollVerticalBarEAW cargoLoadedScroller = new ScrollVerticalBarEAW(cargoUnitsPanel, false);
        cargoLoadedScroller.setSize(514, 192);

        cargoLoadedScroller.addScrollPanelStyle("cargoUnitsPanel");
        add(cargoLoadedScroller, 430, 355);
//        scrollPanel.setWidget(this.cargoUnitsPanel);
        this.cargoUnitsPanel.setSize("489px", "188px");


        final ScrollVerticalBarEAW scVerticalBarEAW = new ScrollVerticalBarEAW(transportUnitsPanel, 75, false);
        add(scVerticalBarEAW, 430, 15);
        scVerticalBarEAW.setSize(616, 225);

        FlowPanelDropController fpDp = new FlowPanelDropController(loadingUnitsPanel);
        FlowPanelDropController fp2Dp = new FlowPanelDropController(cargoUnitsPanel);
        dndController.registerDropController(fpDp);
        dndController.registerDropController(fp2Dp);
        populateTransportUnitsPanel(regionId, transportType);
        //for updating the info panels correctly
        populateCargoUnitsPanel(regionId, cargoType);

        emdisHandle = DeployEventManager.addEmbarkUnitHandler(new EmbarkUnitHandler() {
            public void onEmbarkUnit(final EmbarkUnitEvent event) {
                if (transportId != 0) {
                    populateCargoUnitsPanel(regionId, cargoType);
                    populateCargoPanel((TransportUnitDTO) UnitService.getInstance().getUnitByTypeAndId(DoDeployPanel.this.transportType, transportId));
                }
            }
        });

        addAttachHandler(new AttachEvent.Handler() {
            public void onAttachOrDetach(final AttachEvent event) {
                if (!event.isAttached()) {
                    emdisHandle.removeHandler();
                }
            }
        });

    }

    public void populateCargoUnitsPanel(final int regionId, final int cargoType) {
        this.cargoType = cargoType;
        if (x != 0 && y != 0) {
            populateCargoUnitsPanelByPosition(regionId, cargoType, x, y);
        }

    }

    public void populateCargoUnitsPanelByPosition(final int regionId, final int cargoType, final int x, final int y) {
        List<CargoUnitDTO> disabledUnits = new ArrayList<CargoUnitDTO>();
        this.cargoType = cargoType;
        this.loadingUnitsPanel.clear();
        this.x = x;
        this.y = y;
        final List<CargoUnitDTO> cgUnits = new ArrayList<CargoUnitDTO>();
        final PositionDTO trPos = new PositionDTO();
        trPos.setXStart(x);
        trPos.setYStart(y);
        trPos.setX(x);
        trPos.setY(y);
        trPos.setRegionId(regionId);
        final SectorDTO trSector = RegionStore.getInstance().getSectorByPosition(trPos);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) == 1 && Math.abs(j) == 1) {//if it is diagonal position don't search for any cargo
                    continue;
                }
                cgUnits.addAll(TransportStore.getInstance().getCargoUnitsByRegionTypeAndPosition(this.transportType, this.cargoType, regionId, x + i, y + j, tradePhase));
            }
        }

        for (CargoUnitDTO pos : cgUnits) {
            if (!pos.getLoaded()) {
                if (pos.getStartLoaded()) {
                    disabledUnits.add(pos);
                } else {
                    addUnitToLoadingPanel(pos, this.cargoType);
                }

            }
        }
        for (CargoUnitDTO pos : disabledUnits) {
            switch (cargoType) {
                case BRIGADE:
                    final BrigadeInfoFigPanel bifp = new BrigadeInfoFigPanel((BrigadeDTO) pos, true, "Brigade was loaded this month. A unit cannot be boarded and un-boarded in the same turn");
                    loadingUnitsPanel.add(bifp);
                    break;
                case COMMANDER:
                    final CommanderInfoFigPanel cifp = new CommanderInfoFigPanel((CommanderDTO) pos, true, "Commander was unloaded this month. A unit cannot be boarded and un-boarded in the same turn");
                    loadingUnitsPanel.add(cifp);
                    break;
                case SPY:
                    final SpyInfoFigPanel sifp = new SpyInfoFigPanel((SpyDTO) pos, true, "Spy was unloaded this month. A unit cannot be boarded and un-boarded in the same turn");
                    loadingUnitsPanel.add(sifp);
                    break;
            }

        }
        loadingUnitsPanel.resizeScrollBar();
    }

    private void addUnitToLoadingPanel(final CargoUnitDTO pos, int cargoType) {
        switch (cargoType) {
            case BRIGADE:

                final BrigadeInfoFigPanel bifp = new BrigadeInfoFigPanel((BrigadeDTO) pos, false, "");

                bifp.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(final DoubleClickEvent event) {
                        loadUnitToTransport(bifp, ((BrigadeDTO) pos).getBrigadeId());
                    }
                }, DoubleClickEvent.getType());
                dndController.makeDraggable(bifp, bifp.getFigImg());
                this.loadingUnitsPanel.add(bifp);
                break;

            case COMMANDER:
                final CommanderInfoFigPanel cifp = new CommanderInfoFigPanel((CommanderDTO) pos, false, "");
                cifp.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(final DoubleClickEvent event) {
                        loadUnitToTransport(cifp, ((CommanderDTO) pos).getId());
                    }
                }, DoubleClickEvent.getType());
                dndController.makeDraggable(cifp, cifp.getFigImg());
                this.loadingUnitsPanel.add(cifp);
                break;

            case SPY:
                final SpyInfoFigPanel sifp = new SpyInfoFigPanel((SpyDTO) pos, false, "");
                sifp.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(final DoubleClickEvent event) {
                        loadUnitToTransport(sifp, ((SpyDTO) pos).getSpyId());
                    }
                }, DoubleClickEvent.getType());
                dndController.makeDraggable(sifp, sifp.getFigImg());
                this.loadingUnitsPanel.add(sifp);
                break;

            default:
                break;
        }

    }

    private void loadUnitToTransport(final Widget draggable, final int cargoId) {
        if (TransportStore.getInstance().canCarryLoad(transportType, transportId, cargoType, cargoId) &&
                TransportStore.getInstance().loadCargoToTransport(transportType, transportId, cargoType, cargoId, tradePhase)) {
            draggable.removeFromParent();
            draggable.unsinkEvents(Event.ONDBLCLICK);
            dndController.makeNotDraggable(draggable);
        } else {
            new ErrorPopup(Level.NORMAL, "The selected transport unit has insufficient space!", false);
        }
    }

    final List<TransportInfoPanel> currentPanels = new ArrayList<TransportInfoPanel>();

    public void populateTransportUnitsPanel(final int regionId, final int type) {
        currentPanels.clear();
        selUnitPanel.clear();
        cargoUnitsPanel.clear();
        loadingUnitsPanel.clear();
        transportUnitsPanel.clear();
        transportType = type;
        final List<TransportUnitDTO> trUnits = TransportStore.getInstance().getTransportUnitsByRegionAndType(regionId, transportType);
        int index = 0;
        for (AbsolutePanel unitPanel : this.unitPanels) {
            if (unitPanel != null)
                unitPanel.clear();
        }

        if (trUnits.size() > 15) {
            this.unitPanels = new AbsolutePanel[trUnits.size() + (15 - trUnits.size() % 15)];
        } else {
            this.unitPanels = new AbsolutePanel[15];
        }

        for (int i = 0; i < unitPanels.length; i++) {
            unitPanels[i] = new AbsolutePanel();
            unitPanels[i].setStyleName("miniInfoPanel");
            unitPanels[i].getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
            unitPanels[i].setSize("120px", "72px");
            this.transportUnitsPanel.add(unitPanels[i]);
        }

        for (final TransportUnitDTO trUnit : trUnits) {
            final TransportInfoPanel trUnitPanel = new TransportInfoPanel(trUnit);
            trUnitPanel.setPhase(tradePhase);
            currentPanels.add(trUnitPanel);
            trUnitPanel.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent event) {
                    selectTransportUnit(trUnit.getId());
                }
            });
            this.unitPanels[index].add(trUnitPanel, 3, 1);
            index++;
        }
        transportUnitsPanel.resizeScrollBar();
        loadingUnitsPanel.resizeScrollBar();
        cargoUnitsPanel.resizeScrollBar();
    }

    /**
     * Method that selects a transport unit as the one where we will load
     * the units
     *
     * @param trUnitId the id of the unit we will load
     */
    public void selectTransportUnit(int trUnitId) {

        for (AbsolutePanel unitPanel : unitPanels) {
            if (unitPanel.getWidgetCount() > 0) {
                unitPanel.getWidget(0).setStyleName("tradeUnit pointer");

                TransportInfoPanel trInfoPanel = (TransportInfoPanel) unitPanel.getWidget(0);
                if (trInfoPanel.getTrUnit().getId() == trUnitId) {
                    //@todo position after movement is not checked
                    //PositionDTO pos = MovementStore.getInstance().getUnitPosition(currUnit.getUnitType(), currUnit.getId());

                    populateCargoUnitsPanelByPosition(regionId, DoDeployPanel.this.cargoType, trInfoPanel.getTrUnit().getX(), trInfoPanel.getTrUnit().getY());
                    trInfoPanel.setStyleName("tradeUnitSel pointer");
                    setTransportUnit(trInfoPanel.getTrUnit());
                    transportId = trInfoPanel.getTrUnit().getId();
                    setTradePhase(tradePhase, trInfoPanel.getTrUnit());
                }
            }
        }

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
        if (currUnit != null) {
            for (final int type : currUnit.getLoadedUnitsMap().keySet()) {
                for (int index = 0; index < currUnit.getLoadedUnitsMap().get(type).size(); index++) {
                    final int id = currUnit.getLoadedUnitsMap().get(type).get(index);
                    switch (type) {
                        case BRIGADE:
                            BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(id);
                            if (brig == null) {
                                brig = AlliedUnitsStore.getInstance().getBrigadeById(id);
                            }
                            if (brig != null) {
                                final BrigadeInfoFigPanel bifp = new BrigadeInfoFigPanel(brig, false, "");
                                dndController.makeDraggable(bifp, bifp.getFigImg());
                                cargoUnitsPanel.add(bifp);
                            }
                            break;

                        case COMMANDER:
                            CommanderDTO comm = CommanderStore.getInstance().getCommanderById(id);
                            if (comm == null) {
                                comm = AlliedUnitsStore.getInstance().getCommanderById(id);
                            }
                            if (comm != null) {
                                final CommanderInfoFigPanel cifp = new CommanderInfoFigPanel(comm, false, "");
                                dndController.makeDraggable(cifp, cifp.getFigImg());
                                cargoUnitsPanel.add(cifp);
                            }
                            break;

                        case SPY:
                        default:
                            SpyDTO spy = SpyStore.getInstance().getSpyById(id);
                            if (spy == null) {
                                spy = AlliedUnitsStore.getInstance().getSpyById(id);
                            }
                            if (spy != null) {
                                final SpyInfoFigPanel sifp = new SpyInfoFigPanel(spy, false, "");
                                dndController.makeDraggable(sifp, sifp.getFigImg());
                                cargoUnitsPanel.add(sifp);
                            }
                            break;
                    }
                }
            }
        }
        cargoUnitsPanel.resizeScrollBar();
    }

    /**
     * Method that places the selected transport unit in the
     * selected unit panel and populates the cargo panel
     * with the loaded units
     *
     * @param currUnit the transport unit that was selected
     */
    private void setTransportUnit(final TransportUnitDTO currUnit) {
        try {
            selUnitPanel.clear();
        } catch (Exception e) {
            // eat it
        }
        switch (currUnit.getUnitType()) {
            case BAGGAGETRAIN:
                selUnitPanel.add(new BaggageTrainInfoPanel((BaggageTrainDTO) currUnit), 5, 5);
                break;

            case FLEET:
                selUnitPanel.add(new FleetInfoPanel((FleetDTO) currUnit, false), 5, 5);
                break;

            case SHIP:
                selUnitPanel.add(new ShipInfoPanel((ShipDTO) currUnit, false), 5, 5);
                break;

            default:
                break;

        }
        populateCargoPanel(currUnit);
    }


    /**
     * Set the tradePhase that works now
     *
     * @param tradePhase the phase value to be set
     */
    public void setTradePhase(final int tradePhase, TransportUnitDTO unit) {
        if (MovementStore.getInstance().hasMovedThisTurn(transportType, transportId)
                || AlliedUnitsStore.getInstance().hasUnitMoved(transportType, transportId, unit.getRegionId(), unit.getNationId())) {
            //we check also x and y with startX and startY.
            //easy check for allied units.
            this.tradePhase = 2;
            updateCheckBoxesFromTradePhase(false, tradePhase);
        } else {
            this.tradePhase = tradePhase;
            updateCheckBoxesFromTradePhase(true, tradePhase);
            populateCargoUnitsPanel(regionId, cargoType);
            for (TransportInfoPanel transportInfoPanel : currentPanels) {
                transportInfoPanel.setPhase(tradePhase);
            }
        }
    }

    public void updateCheckBoxesFromTradePhase(final boolean firstEnabled, final int tradePhase) {
        if (tradePhase == 1) {
            this.dpView.getPhaseBox1().setChecked(true, true);
            this.dpView.getPhaseBox2().setChecked(false, true);
        } else {
            this.dpView.getPhaseBox1().setChecked(false, true);
            this.dpView.getPhaseBox2().setChecked(true, true);
        }
        this.dpView.getPhaseBox1().setEnabled(firstEnabled);

    }


    /**
     * @return the transportType
     */
    public int getTransportType() {
        return transportType;
    }

    /**
     * @return the cargoType
     */
    public int getCargoType() {
        return cargoType;
    }

    /**
     * @return the cargoId
     */
    public int getTransportId() {
        return transportId;
    }

    /**
     * @return tradePhase
     */
    public int getTradePhase() {
        return tradePhase;
    }


    public FlowPanel getCargoPanel() {
        return cargoUnitsPanel;
    }
}
