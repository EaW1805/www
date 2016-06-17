package com.eaw1805.www.fieldbattle.views.layout;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedEvent;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedHandler;
import com.eaw1805.www.fieldbattle.events.loading.LoadingEventManager;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.calculators.MapStore;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.mini.BrigadeInfoMini;
import com.eaw1805.www.fieldbattle.widgets.DrawingAreaFB;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.PushButton;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.fieldbattle.widgets.StyledCheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ArmyBarPanel extends AbsolutePanel {

    private final HorizontalPanel brigadeContainer;
    PickupDragController dragController = new PickupDragController(MainPanel.getInstance(), true);
    final List<Integer> brigades = new ArrayList<Integer>();
    final Map<Integer, BrigadeInfoMini> brigadeToInfoPanel = new HashMap<Integer, BrigadeInfoMini>();
    final List<Integer> headcountBrigades = new ArrayList<Integer>();
    final List<Integer> efficiencyBrigades = new ArrayList<Integer>();
    final List<Integer> powerBrigades = new ArrayList<Integer>();
    final ScrollPanel scrollPanel;

    final PushButton infantryCheck;
    final PushButton cavalryCheck;
    final PushButton artilleryCheck;
    final SelectEAW<Integer> orderSelect;
    final SelectEAW<Integer> placedSelect;
    final SelectEAW<Integer> shortSelect;
    boolean placing = false;
    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);
    final DrawingAreaFB drawingArea;
    BrigadeDTO placingBrigade;

    public ArmyBarPanel() {
        setSize("849px", "155px");
        setStyleName("brigadeBarPanel");

        drawingArea = MainPanel.getInstance().getDrawingArea();

        scrollPanel = new ScrollPanel();
        scrollPanel.setSize("784px", "90px");
        add(scrollPanel, 36, 55);

        brigadeContainer = new HorizontalPanel();
        scrollPanel.setWidget(brigadeContainer);
        scrollPanel.setStyleName("noScrollBars");
        addScrollFunctionality();
        if (ArmyStore.getInstance().isInitialized()) {
            initBrigades();
        } else {
            LoadUtil.getInstance().registerHandlerForClean(LoadingEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
                @Override
                public void onUnitChanged(ArmiesLoadedEvent event) {
                    initBrigades();
                }
            }));
        }

        final ImageButton rightButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png", "Scroll through brigades");
        rightButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() + 170);
            }
        });
        rightButton.setSize("14px", "90px");
        add(rightButton, 824, 55);
        final ImageButton leftButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png", "Scroll through brigades");
        leftButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() - 170);
            }
        });
        leftButton.setSize("14px", "90px");
        add(leftButton, 18, 55);

        final StyledCheckBox readyBox = new StyledCheckBox("Ready?", BaseStore.getInstance().isSideReady(), false, "Click this when you are done with all orders so the game can process");
        readyBox.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                BaseStore.getInstance().setSideReady(readyBox.isChecked());

            }
        });
        add(readyBox, 672, 25);

        final ImageButton saveLbl = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSaveOrdersOff.png", Tips.ELEMENT_SAVE);
        saveLbl.setSize("30px", "30px");
        add(saveLbl, 761, 21);
        saveLbl.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                service.saveBrigadesPositions(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), BaseStore.getInstance().getNationId(), ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId()), BaseStore.getInstance().isSideReady(), new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Window.alert("Failed to save brigades positions");
                    }

                    @Override
                    public void onSuccess(final Integer integer) {
                        if (integer == -1) {
                            Window.alert("Your position is ready, you can't save again!");
                            return;
                        }

                        //validate orders to notify user.
                        boolean allFine = true;
                        for (BrigadeDTO brigade : ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId())) {
                            if (brigade.isPlacedOnFieldMap()) {
                                if (errorsInOrders(brigade)) {
                                    allFine = false;
                                    break;
                                }
                            }
                        }
                        if (allFine) {
                            Window.alert("Orders saved");

                        } else {
                            Window.alert("Positions saved - Follow Detachment orders with no selected leader have been found. Notice the brigades on the red rectangles. If you don't set a leader, those orders will be replaced with Defend Position orders.");
                        }
                        if (BaseStore.getInstance().isSideReady()) {
                            LoadUtil.getInstance().loadSocialPanel(true);
                        }
                    }
                });
            }
        });

        artilleryCheck = new PushButton("http://static.eaw1805.com/images/buttons/icons/formations/artilleryOff.png", Tips.FILTER_ARTILLERY);
        artilleryCheck.setPressed(true);
        artilleryCheck.addPushHandler(new BasicHandler() {
            @Override
            public void run() {
                updateContainer();
            }
        });
        cavalryCheck = new PushButton("http://static.eaw1805.com/images/buttons/icons/formations/cavalryOff.png", Tips.FILTER_CAVALRY);
        cavalryCheck.setPressed(true);
        cavalryCheck.addPushHandler(new BasicHandler() {
            @Override
            public void run() {
                updateContainer();
            }
        });
        infantryCheck = new PushButton("http://static.eaw1805.com/images/buttons/icons/formations/infantryOff.png", Tips.FILTER_INFANTRY);
        infantryCheck.setPressed(true);
        infantryCheck.addPushHandler(new BasicHandler() {
            @Override
            public void run() {
                updateContainer();
            }
        });

//        cavalryCheck.setTextStyle("clearFont");
        add(cavalryCheck, 102, 31);
//        infantryCheck.setTextStyle("clearFont");
        add(infantryCheck, 69, 31);
//        artilleryCheck.setTextStyle("clearFont");
        add(artilleryCheck, 135, 31);

        orderSelect = new SelectEAW<Integer>(Tips.FILTER_ORDERS) {
            @Override
            public void onChange() {
                updateContainer();
            }
        };
        orderSelect.hideSideBar();
        orderSelect.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        orderSelect.setDropDownStyleName("dropDown320x420");
        orderSelect.addOption(new OptionEAW(80, 15, "All"), 1);
        orderSelect.addOption(new OptionEAW(80, 15, "With Orders"), 2);
        orderSelect.addOption(new OptionEAW(80, 15, "Without Orders"), 3);

        add(orderSelect, 182, 31);

        placedSelect = new SelectEAW<Integer>(Tips.FILTER_PLACED) {
            @Override
            public void onChange() {
                updateContainer();
            }
        };
        placedSelect.hideSideBar();
        placedSelect.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        placedSelect.setDropDownStyleName("dropDown320x420");
        placedSelect.addOption(new OptionEAW(60, 15, "All"), 1);
        placedSelect.addOption(new OptionEAW(60, 15, "Placed"), 2);
        placedSelect.addOption(new OptionEAW(60, 15, "Not Placed"), 3);

        add(placedSelect, 292, 31);

        shortSelect = new SelectEAW<Integer>(Tips.FILTER_SHORT) {
            @Override
            public void onChange() {
                updateContainer();
            }
        };
        shortSelect.hideSideBar();
        shortSelect.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        shortSelect.setDropDownStyleName("dropDown320x420");
        shortSelect.addOption(new OptionEAW(60, 15, "Short By"), 1);
        shortSelect.addOption(new OptionEAW(60, 15, "Power"), 2);
        shortSelect.addOption(new OptionEAW(60, 15, "Headcount"), 3);
        shortSelect.addOption(new OptionEAW(60, 15, "Efficiency"), 4);

        add(shortSelect, 378, 31);

        shortSelect.selectOption(1);
        orderSelect.selectOption(0);
        placedSelect.selectOption(2);


        final Timer closeT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteTop() < Window.getClientHeight() - 40) {
                    MainPanel.getInstance().setWidgetPosition(ArmyBarPanel.this, getAbsoluteLeft(), getAbsoluteTop() + 5);
                } else {
                    MainPanel.getInstance().setWidgetPosition(ArmyBarPanel.this, getAbsoluteLeft(), Window.getClientHeight() - 40);
                    cancel();
                }

            }
        };

        final Timer openT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteTop() > Window.getClientHeight() - 155) {
                    MainPanel.getInstance().setWidgetPosition(ArmyBarPanel.this, getAbsoluteLeft(), getAbsoluteTop() - 5);
                } else {
                    MainPanel.getInstance().setWidgetPosition(ArmyBarPanel.this, getAbsoluteLeft(), Window.getClientHeight() - 155);
                    cancel();
                }
            }
        };

        final ImageButton toggleButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png", Tips.ELEMENT_SHOW_PANELS);
        toggleButton.setSize("24px", "24px");
        toggleButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (MainPanel.getInstance().getArmyInfo().isOpened()) {
                    openT.cancel();//be sure to stop the opener if running...
                    closeT.scheduleRepeating(10);
                    MainPanel.getInstance().getArmyInfo().close();
                } else {
                    closeT.cancel();//be sure to stop the closer if running...
                    openT.scheduleRepeating(10);
                    MainPanel.getInstance().getArmyInfo().open();
                }
                MainPanel.getInstance().getArmyInfo().setOpened(!MainPanel.getInstance().getArmyInfo().isOpened());
            }
        });

        add(toggleButton, 809, 25);


    }

    private boolean errorsInOrders(BrigadeDTO brigade) {
        if (brigade.hasBasicOrder()) {
            if (!validateOrder(brigade.getBasicOrder())) {
                return true;
            }
        }
        if (brigade.hasAdditionalOrder()) {
            if (!validateOrder(brigade.getAdditionalOrder())) {
                return true;
            }
        }
        return false;

    }

    private boolean validateOrder(FieldBattleOrderDTO order) {
        if ("FOLLOW_DETACHMENT".equals(order.getOrderType())
                && order.getLeaderId() == 0) {
            return false;
        }
        if ("FOLLOW_DETACHMENT".equals(order.getOrderType())
                && ("Select position".equals(order.getDetachmentPosition())
                || "".equals(order.getDetachmentPosition()))) {
            return false;
        }
        return true;
    }

    public void refreshBrigadesContainer() {
        int rememberPanelPos = scrollPanel.getHorizontalScrollPosition();
        updateContainer();
        scrollPanel.setHorizontalScrollPosition(rememberPanelPos);
    }

    public void addScrollFunctionality() {
        scrollPanel.addDomHandler(new MouseWheelHandler() {
            @Override
            public void onMouseWheel(MouseWheelEvent event) {
                if (event.getDeltaY() < 0) {
                    scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() - 170);
                } else {
                    scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() + 170);
                }
            }
        }, MouseWheelEvent.getType());
    }

    public void initBrigades() {
        List<BrigadeDTO> allBrigades = ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId());
        for (BrigadeDTO brig : allBrigades) {
            brigades.add(brig.getBrigadeId());
        }

        SimpleDropController sdp = new SimpleDropController(MainPanel.getInstance()) {
            public void onMove(DragContext context) {
                try {
                    int x = MainPanel.getInstance().getMapUtils().translateToMapX(context.mouseX);
                    int y = MainPanel.getInstance().getMapUtils().translateToMapY(context.mouseY);
                    drawingArea.updateRectangle(MainPanel.getInstance().getMapUtils().getPointX(x), MainPanel.getInstance().getMapUtils().getPointY(y));
                    final FieldBattleSectorDTO sector = MapStore.getInstance().getSectorByXY(x, y);
                    if (MainPanel.getInstance().getDrawingArea().canBrigadeMoveToSector(sector, MainPanel.getInstance().getArmyBar().getPlacingBrigade())) {
                        MainPanel.getInstance().getDrawingArea().getSelectionRectangle().setFillColor("white");
                    } else {
                        MainPanel.getInstance().getDrawingArea().getSelectionRectangle().setFillColor("black");
                    }
                    MainPanel.getInstance().getMapUtils().updateLevelLabel(sector, -1);
                } catch (Exception e) {
                    //eat it
                }
            }
        };
        dragController.registerDropController(sdp);


        dragController.addDragHandler(new DragHandler() {
            @Override
            public void onDragEnd(DragEndEvent dragEndEvent) {

                int x = MainPanel.getInstance().getMapUtils().translateToMapX(dragEndEvent.getContext().mouseX);
                int y = MainPanel.getInstance().getMapUtils().translateToMapY(dragEndEvent.getContext().mouseY);
                BrigadeInfoMini brigPanel = ((BrigadeInfoMini) dragEndEvent.getSource());
                MainPanel.getInstance().getDrawingArea().getSelectionRectangle().setFillColor("red");
                if (x < 0 || y < 0) {
                    brigPanel.removeFromParent();
                    brigPanel.getElement().getStyle().setPosition(Style.Position.RELATIVE);
                    int rememberPanelPos = scrollPanel.getHorizontalScrollPosition();
                    updateContainer();
                    scrollPanel.setHorizontalScrollPosition(rememberPanelPos);
//                    select.selectOptionByValue(brigPanel.getBrigade(), new Comparator<BrigadeDTO>() {
//                        @Override
//                        public int compare(BrigadeDTO o1, BrigadeDTO o2) {
//                            if (o1 == null) {
//                                return 1;
//                            }
//                            if (o2 == null) {
//                                return -1;
//                            }
//                            return o1.getBrigadeId() - o2.getBrigadeId();  //To change body of implemented methods use File | Settings | File Templates.
//                        }
//                    });
//                    select.onChange(select.getSelectedOption(), select.getSelectedValue());
//                    select.ensureVisible(brigPanel);

                    return;
                }

                final FieldBattleSectorDTO sector = MapStore.getInstance().getSectorByXY(x, y);
                brigPanel.removeFromParent();

                if (MainPanel.getInstance().getDrawingArea().canBrigadeMoveToSector(sector, brigPanel.getBrigade())) {
                    brigPanel.getBrigade().setFieldBattleX(sector.getX());
                    brigPanel.getBrigade().setFieldBattleY(sector.getY());
                    brigPanel.getBrigade().setPlacedOnFieldMap(true);
                    try {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigPanel.getBrigade(), 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigPanel.getBrigade(), true, false);
                        if (BaseStore.getInstance().isStartRound()) {
                            MainPanel.getInstance().updateNationInfoPanels();
                        }
                    } catch (Exception e) {
                        Window.alert("f?" + e.toString());
                    }
                }
                brigPanel.getElement().getStyle().setPosition(Style.Position.RELATIVE);
                int rememberPanelPos = scrollPanel.getHorizontalScrollPosition();
                updateContainer();
                scrollPanel.setHorizontalScrollPosition(rememberPanelPos);
//                select.selectOptionByValue(brigPanel.getBrigade(), new Comparator<BrigadeDTO>() {
//                    @Override
//                    public int compare(BrigadeDTO o1, BrigadeDTO o2) {
//                        if (o1 == null) {
//                            return 1;
//                        }
//                        if (o2 == null) {
//                            return -1;
//                        }
//                        return o1.getBrigadeId() - o2.getBrigadeId();  //To change body of implemented methods use File | Settings | File Templates.
//                    }
//                });
//                select.onChange(select.getSelectedOption(), select.getSelectedValue());
//                select.ensureVisible(brigPanel);
                MainPanel.getInstance().getMapUtils().hidePositionGroup();
                BaseStore.getInstance().setState(BaseStore.STATE.NORMAL);
                MainPanel.getInstance().getMapUtils().updateLeaderSelectionOptions();
                placing = false;
            }

            @Override
            public void onDragStart(DragStartEvent dragStartEvent) {
                placing = true;
                BrigadeInfoMini brigPanel = ((BrigadeInfoMini) dragStartEvent.getSource());
                placingBrigade = brigPanel.getBrigade();

                BaseStore.getInstance().setState(BaseStore.STATE.PLACING_ARMY);
                MainPanel.getInstance().getMapUtils().showPositionGroup();
            }

            @Override
            public void onPreviewDragEnd(DragEndEvent dragEndEvent) throws VetoDragException {
                //do nothing here...
            }

            @Override
            public void onPreviewDragStart(DragStartEvent dragStartEvent) throws VetoDragException {
                //do nothing here...
            }
        });

        for (final BrigadeDTO brigade : allBrigades) {
            brigadeToInfoPanel.put(brigade.getBrigadeId(), new BrigadeInfoMini(brigade, true));
            brigadeToInfoPanel.get(brigade.getBrigadeId()).addDomHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent event) {
                    MainPanel.getInstance().getArmyInfo().updateBrigadeInfoPanel(brigade);
                }
            }, MouseOverEvent.getType());
            dragController.makeDraggable(brigadeToInfoPanel.get(brigade.getBrigadeId()), brigadeToInfoPanel.get(brigade.getBrigadeId()).getBrigadePanel());
        }
        //init indexes
        final TreeMap<Double, BrigadeDTO> headCountSorter = new TreeMap<Double, BrigadeDTO>();
        final TreeMap<Double, BrigadeDTO> powerSorter = new TreeMap<Double, BrigadeDTO>();
        final TreeMap<Double, BrigadeDTO> efficiencySorter = new TreeMap<Double, BrigadeDTO>();
        for (BrigadeDTO brigade : allBrigades) {
            double totalHeadCount = brigade.calculateTotalHeadCount();
            while (headCountSorter.containsKey(totalHeadCount)) {
                totalHeadCount += 0.001;
            }
            headCountSorter.put(totalHeadCount, brigade);

            double totalEfficiency = brigade.calculateTotalEfficiency();
            while (efficiencySorter.containsKey(totalEfficiency)) {
                totalEfficiency += 0.001;
            }
            efficiencySorter.put(totalEfficiency, brigade);

            double totalPower = brigade.calculateTotalPower();
            while (powerSorter.containsKey(totalPower)) {
                totalPower += 0.001;
            }
            powerSorter.put(totalPower, brigade);
        }
        for (Map.Entry<Double, BrigadeDTO> entry : headCountSorter.entrySet()) {
            headcountBrigades.add(entry.getValue().getBrigadeId());
        }

        for (Map.Entry<Double, BrigadeDTO> entry : efficiencySorter.entrySet()) {
            efficiencyBrigades.add(entry.getValue().getBrigadeId());
        }
        for (Map.Entry<Double, BrigadeDTO> entry : powerSorter.entrySet()) {
            powerBrigades.add(entry.getValue().getBrigadeId());
        }

        Collections.reverse(headcountBrigades);
        Collections.reverse(efficiencyBrigades);
        Collections.reverse(powerBrigades);

        updateContainer();
    }

    public void updateContainer() {
        brigadeContainer.clear();
        final List<Integer> sorter;

        switch (shortSelect.getValue()) {
            case 2:
                sorter = powerBrigades;
                break;
            case 3:
                sorter = headcountBrigades;
                break;
            case 4:
                sorter = efficiencyBrigades;
                break;
            default:
                sorter = brigades;
        }

        for (Integer index : sorter) {
            final BrigadeInfoMini bInfo = brigadeToInfoPanel.get(index);
            final BrigadeDTO brig = bInfo.getBrigade();
            if (!((placedSelect.getValue() == 1 || (placedSelect.getValue() == 2 && brig.isPlacedOnFieldMap()) || (placedSelect.getValue() == 3 && !brig.isPlacedOnFieldMap())) && (orderSelect.getValue() == 1 || (orderSelect.getValue() == 2 && brig.getBasicOrder() != null && !"SELECT_AN_ORDER".equals(brig.getBasicOrder().getOrderType()))
                    || (orderSelect.getValue() == 3 && (brig.getBasicOrder() == null || "SELECT_AN_ORDER".equals(brig.getBasicOrder().getOrderType())))))) {
                continue;
            }

            final ArmyUnitInfoDTO unitInfo = MiscCalculators.getBrigadeInfo(brig);
            if (unitInfo.getDominant() == 1 && infantryCheck.isPressed()) {
                brigadeContainer.add(bInfo);
                //infantry
            } else if (unitInfo.getDominant() == 2 && cavalryCheck.isPressed()) {
                brigadeContainer.add(bInfo);
                //cavalry
            } else if (unitInfo.getDominant() == 3 && artilleryCheck.isPressed()) {
                brigadeContainer.add(bInfo);
                //artillery
            }
//            if (!bInfo.getBrigade().isPlacedOnFieldMap()) {
//                brigadeContainer.add(bInfo);
//            }
        }
    }

    public BrigadeDTO getPlacingBrigade() {
        return placingBrigade;
    }
}
