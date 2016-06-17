package com.eaw1805.www.scenario.views;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.views.overview.*;
import com.eaw1805.www.scenario.views.region.RegionSettingsPanel;
import com.eaw1805.www.scenario.widgets.DrawingAreaSE;

public class EditorPanel extends AbsolutePanel{
    private static EditorPanel instance;

    enum EditorView {
        MAP_VIEW,
        ARMY_VIEW,
        COMMANDER_VIEW,
        SPY_BTRAIN_VIEW,
        SHIP_VIEW,
        WAREHOUSE_VIEW
    };

    private MapsMenu mapsMenu;
    private RegionSettingsPanel regionPanel;
    private BrushView brushView;
    private MapOverView mapOverView;
    private ArmyBrushView armyBrush;
    private ArmyOverView armyOverView;
    private CommanderBrushView commBrush;
    private CommandersOverView commOverview;
    private SpyBaggageTrainBrush spyBTrainBrush;
    private SpyBTrainOverView spyBTrainOverView;
    private WarehouseSetupPanel warehouseSetupPanel;
    private SupplyLinesView supplyLinesView;

    private ShipBrush shipBrush;
    private ShipsOverView shipOverView;

    private final ScrollPanel drawingScroller = new ScrollPanel();
    private DrawingAreaSE drawingArea;
    Label debugLabel = new Label("Brush Mode - (hotkeys : Z = move map, X = Edit sectors on map)");
    private EditorView view = EditorView.MAP_VIEW;

    ViewMenu viewMenu;

    private EditorPanel() {
        setSize("100%", "100%");
    }

    public static EditorPanel getInstance() {
        if (instance == null) {
            instance = new EditorPanel();
        }
        return instance;
    }


    public void initPanel() {
        mapsMenu = new MapsMenu();
        armyBrush = new ArmyBrushView();
        armyOverView = new ArmyOverView();
        commBrush = new CommanderBrushView();
        mapOverView = new MapOverView();
        regionPanel = new RegionSettingsPanel();
        brushView = new BrushView();
        viewMenu = new ViewMenu();
        commOverview = new CommandersOverView();
        spyBTrainBrush = new SpyBaggageTrainBrush();
        spyBTrainOverView = new SpyBTrainOverView();
        shipBrush = new ShipBrush();
        shipOverView = new ShipsOverView();
        warehouseSetupPanel = new WarehouseSetupPanel();
        supplyLinesView = new SupplyLinesView();

        final VerticalPanel panelLeft = new VerticalPanel();
        final VerticalPanel panelRight = new VerticalPanel();
        drawingArea= new DrawingAreaSE(1000, 1000);
        drawingScroller.setWidget(drawingArea);
        add(drawingScroller);
        drawingScroller.setStyleName("noScrollBars");
        drawingScroller.setSize("100%", "100%");
        drawingScroller.setAlwaysShowScrollBars(false);
        drawingArea.setScroller(drawingScroller);

        add(debugLabel, 300, 0);
        debugLabel.getElement().getStyle().setColor("white");
        debugLabel.getElement().getStyle().setBackgroundColor("grey");

        add(panelLeft, 0, 0);
        SelectEAW<EditorView> viewSelect = new SelectEAW<EditorView>("Select View") {
            @Override
            public void onChange() {
                updateView(getValue());
            }
        };
        viewSelect.addOption(new OptionEAW(170, 15, "Map View"), EditorView.MAP_VIEW);
        viewSelect.addOption(new OptionEAW(170, 15, "Army View"), EditorView.ARMY_VIEW);
        viewSelect.addOption(new OptionEAW(170, 15, "Commander View"), EditorView.COMMANDER_VIEW);
        viewSelect.addOption(new OptionEAW(170, 15, "Spies/Baggage trains View"), EditorView.SPY_BTRAIN_VIEW);
        viewSelect.addOption(new OptionEAW(170, 15, "Ship View"), EditorView.SHIP_VIEW);
        viewSelect.addOption(new OptionEAW(170, 15, "Warehouse View"), EditorView.WAREHOUSE_VIEW);
        panelLeft.add(new HTML("<hr>"));
        panelLeft.add(viewSelect);


        panelLeft.add(new HTML("<hr>"));
        panelLeft.add(armyBrush);
        panelLeft.add(brushView);
        panelLeft.add(commBrush);
        panelLeft.add(spyBTrainBrush);
        panelLeft.add(shipBrush);
        panelLeft.add(warehouseSetupPanel);
        panelLeft.add(new HTML("<hr>"));
        panelLeft.add(mapOverView);
        panelLeft.add(armyOverView);
        panelLeft.add(commOverview);
        panelLeft.add(spyBTrainOverView);
        panelLeft.add(shipOverView);
        panelLeft.add(new HTML("<hr>"));




        add(panelRight, Window.getClientWidth() - 170, 0);
        panelRight.add(new HTML("<hr>"));
        panelRight.add(regionPanel);
        regionPanel.setVisible(false);//this should be false at the beginning
        panelRight.add(new HTML("<hr>"));
        panelRight.add(mapsMenu);
        panelRight.add(new HTML("<hr>"));
        panelRight.add(viewMenu);
        panelRight.add(new HTML("<hr>"));
        panelRight.add(supplyLinesView);
        panelRight.add(new HTML("<hr>"));
        panelRight.add(new SaveView());
        panelRight.add(new HTML("<hr>"));

        //update the view
        viewSelect.selectOption(0);
    }

    public void updateView(EditorView view) {
        this.view = view;
        mapsMenu.setVisible(true);//this should be always visible
        armyBrush.setVisible(view == EditorView.ARMY_VIEW);
        armyOverView.setVisible(view == EditorView.ARMY_VIEW);
        commBrush.setVisible(view == EditorView.COMMANDER_VIEW);
        commOverview.setVisible(view == EditorView.COMMANDER_VIEW);
        spyBTrainBrush.setVisible(view == EditorView.SPY_BTRAIN_VIEW);
        spyBTrainOverView.setVisible(view == EditorView.SPY_BTRAIN_VIEW);
        brushView.setVisible(view == EditorView.MAP_VIEW);
        mapOverView.setVisible(view == EditorView.MAP_VIEW);
        shipBrush.setVisible(view == EditorView.SHIP_VIEW);
        shipOverView.setVisible(view == EditorView.SHIP_VIEW);
        warehouseSetupPanel.setVisible(view == EditorView.WAREHOUSE_VIEW);

        viewMenu.setVisible(true);//this should be always visible
        supplyLinesView.setVisible(true);
    }

    public MapsMenu getMapsMenu() {
        return mapsMenu;
    }

    public RegionSettingsPanel getRegionPanel() {
        return regionPanel;
    }

    public BrushView getBrushView() {
        return brushView;
    }

    public DrawingAreaSE getDrawingArea() {
        return drawingArea;
    }

    public void setDebugMessage(final String msg) {
        debugLabel.setText(msg);
    }

    public ArmyBrushView getArmyBrush() {
        return armyBrush;
    }

    public CommanderBrushView getCommBrush() {
        return commBrush;
    }

    public CommandersOverView getCommOverview() {
        return commOverview;
    }

    public SpyBaggageTrainBrush getSpyBTrainBrush() {
        return spyBTrainBrush;
    }

    public ShipBrush getShipBrush() {
        return shipBrush;
    }

    public MapOverView getMapOverView() {
        return mapOverView;
    }

    public ArmyOverView getArmyOverView() {
        return armyOverView;
    }

    public SpyBTrainOverView getSpyBTrainOverView() {
        return spyBTrainOverView;
    }

    public ShipsOverView getShipOverView() {
        return shipOverView;
    }

    public WarehouseSetupPanel getWarehousePanel() {
        return warehouseSetupPanel;
    }

    public boolean isMapEdit() {
        return view == EditorView.MAP_VIEW;
    }

    public boolean isArmyEdit() {
        return view == EditorView.ARMY_VIEW;
    }

    public boolean isCommanderEdit() {
        return view == EditorView.COMMANDER_VIEW;
    }

    public boolean isSpyBTrainEdit() {
        return view == EditorView.SPY_BTRAIN_VIEW;
    }

    public boolean isShipEdit() {
        return view == EditorView.SHIP_VIEW;
    }
}
