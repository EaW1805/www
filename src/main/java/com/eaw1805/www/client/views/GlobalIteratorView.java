package com.eaw1805.www.client.views;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import com.eaw1805.www.client.gui.GuiComponentBase;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.infopanels.BarracksInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BaggageTrainInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CommanderInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.FleetInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.ShipInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.SpyInfoPanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.ArrayList;
import java.util.List;


public class GlobalIteratorView
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {

    private boolean opened = false;

    private final int[] verticalPositions = new int[6];
    private final int[] maxPositions = new int[6];
    private int horizontalPosition;
    private final int stepHorizontal = 366;
    private final int stepVertical = 89;
    private final ScrollPanel verticalScroll;
    private final ScrollPanel[] unitPanels = new ScrollPanel[6];
    private final HorizontalPanel armies;
    private final HorizontalPanel navy;
    private final HorizontalPanel commanders;
    private final HorizontalPanel spies;
    private final HorizontalPanel bTrains;
    private final HorizontalPanel barracks;
    private final GuiComponentBase component;
    Timer closer;

    public GlobalIteratorView() {
        setSize("1000px", "189px");
        horizontalPosition = 0;
        for (int i = 0; i < 6; i++) {
            verticalPositions[i] = 0;
        }
        setStyleName("allSelector");
        final VerticalPanel verticalContainer = new VerticalPanel();

        verticalScroll = new ScrollPanel(verticalContainer);
        verticalScroll.setSize(SIZE_900PX, SIZE_89PX);
        verticalScroll.setStyleName(CLASS_NOSCROLLBARS);
        //one for the land forces
        armies = new HorizontalPanel();
        unitPanels[0] = new ScrollPanel(armies);
        unitPanels[0].setSize(SIZE_900PX, SIZE_89PX);
        unitPanels[0].setStyleName(CLASS_NOSCROLLBARS);
        verticalContainer.add(unitPanels[0]);
        //one for the navy
        navy = new HorizontalPanel();
        unitPanels[1] = new ScrollPanel(navy);
        unitPanels[1].setSize(SIZE_900PX, SIZE_89PX);
        unitPanels[1].setStyleName(CLASS_NOSCROLLBARS);
        verticalContainer.add(unitPanels[1]);
        //one for the commanders
        commanders = new HorizontalPanel();
        unitPanels[2] = new ScrollPanel(commanders);
        unitPanels[2].setSize(SIZE_900PX, SIZE_89PX);
        unitPanels[2].setStyleName(CLASS_NOSCROLLBARS);
        verticalContainer.add(unitPanels[2]);
        //one for the spies
        spies = new HorizontalPanel();
        unitPanels[3] = new ScrollPanel(spies);
        unitPanels[3].setSize(SIZE_900PX, SIZE_89PX);
        unitPanels[3].setStyleName(CLASS_NOSCROLLBARS);
        verticalContainer.add(unitPanels[3]);
        //one for the baggage trains
        bTrains = new HorizontalPanel();
        unitPanels[4] = new ScrollPanel(bTrains);
        unitPanels[4].setSize(SIZE_900PX, SIZE_89PX);
        unitPanels[4].setStyleName(CLASS_NOSCROLLBARS);
        verticalContainer.add(unitPanels[4]);
        //one for the barracks
        barracks = new HorizontalPanel();
        unitPanels[5] = new ScrollPanel(barracks);
        unitPanels[5].setSize(SIZE_900PX, SIZE_89PX);
        unitPanels[5].setStyleName(CLASS_NOSCROLLBARS);
        verticalContainer.add(unitPanels[5]);

        add(verticalScroll, 50, 50);


        final ImageButton left = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                moveLeft();
                left.deselect();
            }
        }).addToElement(left.getElement()).register();

        left.setSize("14px", "90px");
        left.setStyleName(CLASS_POINTER);
        add(left, 12, 48);

        final ImageButton right = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                moveRight();
                right.deselect();
            }
        }).addToElement(right.getElement()).register();

        right.setSize("14px", "90px");
        right.setStyleName(CLASS_POINTER);
        add(right, 971, 48);


        final ImageButton up = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                moveUp();
                up.deselect();
            }
        }).addToElement(up.getElement()).register();

        up.setSize("172px", "24px");
        up.setStyleName(CLASS_POINTER);
        add(up, 414, 20);

        final ImageButton down = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                moveDown();
                down.deselect();
            }
        }).addToElement(down.getElement()).register();

        down.setSize("172px", "24px");
        down.setStyleName(CLASS_POINTER);
        add(down, 414, 149);

        component = new GuiComponentBase(this) {
            public void handleEnter() {
                //for now just do the same as handling escape..
                final SelectableWidget sWidget = (SelectableWidget) infoPanels.get(horizontalPosition).get(verticalPositions[horizontalPosition]);
                sWidget.onEnter();
                handleEscape();
            }
        };

        closer = new Timer() {
            @Override
            public void run() {
                hide();
            }
        };
        //just make it be on top...
        getElement().getStyle().setZIndex(Integer.MAX_VALUE);
    }


    public void onAttach() {
        component.registerComponent();
        opened = true;
    }

    public void onDetach() {
        component.unRegisterComponent();
        opened = false;
    }

    List<List<AbstractInfoPanel>> infoPanels = new ArrayList<List<AbstractInfoPanel>>();

    public void initPanels() {
        armies.clear();
        navy.clear();
        commanders.clear();
        spies.clear();
        bTrains.clear();
        barracks.clear();

        infoPanels.clear();

        //prepare 6 lists
        infoPanels.add(new ArrayList<AbstractInfoPanel>());
        infoPanels.add(new ArrayList<AbstractInfoPanel>());
        infoPanels.add(new ArrayList<AbstractInfoPanel>());
        infoPanels.add(new ArrayList<AbstractInfoPanel>());
        infoPanels.add(new ArrayList<AbstractInfoPanel>());
        infoPanels.add(new ArrayList<AbstractInfoPanel>());

        for (ArmyDTO army : ArmyStore.getInstance().getcArmiesList()) {
            if (army.getArmyId() > 0) {
                infoPanels.get(0).add(new ArmyInfoPanel(army));
            } else {
                for (CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() > 0) {
                        infoPanels.get(0).add(new CorpsInfoPanel(corp, true));
                    } else {
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            infoPanels.get(0).add(new BrigadeInfoPanel(brigade, true));
                        }
                    }
                }
            }
        }

        for (FleetDTO fleet : NavyStore.getInstance().getDbFleetlist()) {
            if (fleet.getFleetId() > 0) {
                infoPanels.get(1).add(new FleetInfoPanel(fleet, true));
            } else {
                for (ShipDTO ship : fleet.getShips().values()) {
                    infoPanels.get(1).add(new ShipInfoPanel(ship, true));
                }
            }
        }

        for (CommanderDTO commander : CommanderStore.getInstance().getCommandersList()) {
            infoPanels.get(2).add(new CommanderInfoPanel(commander));
        }
        for (SpyDTO spy : SpyStore.getInstance().getSpyList()) {
            infoPanels.get(3).add(new SpyInfoPanel(spy));
        }
        for (BaggageTrainDTO bTrain : BaggageTrainStore.getInstance().getBaggageTList()) {
            infoPanels.get(4).add(new BaggageTrainInfoPanel(bTrain));
        }
        for (BarrackDTO barrack : BarrackStore.getInstance().getBarracksList()) {
            infoPanels.get(5).add(new BarracksInfoPanel(barrack));
        }

        for (AbstractInfoPanel infoPanel : infoPanels.get(0)) {
            armies.add(infoPanel);
        }

        maxPositions[0] = infoPanels.get(0).size() - 1;

        for (AbstractInfoPanel infoPanel : infoPanels.get(1)) {
            navy.add(infoPanel);
        }
        maxPositions[1] = infoPanels.get(1).size() - 1;

        for (AbstractInfoPanel infoPanel : infoPanels.get(2)) {
            commanders.add(infoPanel);
        }

        maxPositions[2] = infoPanels.get(2).size() - 1;
        for (AbstractInfoPanel infoPanel : infoPanels.get(3)) {
            spies.add(infoPanel);
        }
        maxPositions[3] = infoPanels.get(3).size() - 1;

        for (AbstractInfoPanel infoPanel : infoPanels.get(4)) {
            bTrains.add(infoPanel);
        }
        maxPositions[4] = infoPanels.get(4).size() - 1;

        for (AbstractInfoPanel infoPanel : infoPanels.get(5)) {
            barracks.add(infoPanel);
        }
        maxPositions[5] = infoPanels.get(5).size() - 1;
        updateSelection();
    }


    public void moveUp() {
        horizontalPosition--;
        if (horizontalPosition < 0) {
            horizontalPosition = 5;
        }
        updateSelection();
    }

    public void moveDown() {
        horizontalPosition++;
        if (horizontalPosition > 5) {
            horizontalPosition = 0;
        }

        updateSelection();

    }

    public void deSelectAll() {
        for (int i = 0; i < 6; i++) {
            for (AbstractInfoPanel infoPanel : infoPanels.get(i)) {
                final SelectableWidget sWidget = (SelectableWidget) infoPanel;
                sWidget.setSelected(false);
            }
        }
    }

    public void moveLeft() {
        verticalPositions[horizontalPosition]--;
        if (verticalPositions[horizontalPosition] < 0) {
            verticalPositions[horizontalPosition] = maxPositions[horizontalPosition];
        }

        updateSelection();
    }

    public void moveRight() {
        verticalPositions[horizontalPosition]++;
        if (verticalPositions[horizontalPosition] > maxPositions[horizontalPosition]) {
            verticalPositions[horizontalPosition] = 0;
        }
        updateSelection();
    }

    public void show() {
        initPanels();
        GameStore.getInstance().getLayoutView().showGlobalIterator();
        GameStore.getInstance().getLayoutView().positionTocCenter(this);
        GameStore.getInstance().getLayoutView().setWidgetPosition(this, GameStore.getInstance().getLayoutView().getWidgetX(this), 100, false, false);
        opened = true;
        updateSelection();
        scheduleClose();
    }

    public void hide() {
        GameStore.getInstance().getLayoutView().hideGlobalIterator();
        opened = false;
    }

    public boolean isOpened() {
        return opened;
    }

    public void updateSelection() {
        scheduleClose();
        verticalScroll.setScrollPosition(horizontalPosition * stepVertical);
        unitPanels[horizontalPosition].setHorizontalScrollPosition(verticalPositions[horizontalPosition] * stepHorizontal);
        deSelectAll();
        final SelectableWidget sWidget = (SelectableWidget) infoPanels.get(horizontalPosition).get(verticalPositions[horizontalPosition]);
        sWidget.setSelected(true);
        MapStore.getInstance().getMapsView().goToPosition((PositionDTO) sWidget.getValue());
        scheduleClose();
    }

    public void scheduleClose() {
        closer.cancel();
        closer.schedule(3000);
    }
}
