package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.stores.AdStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;
import com.eaw1805.www.fieldbattle.stores.utils.MiniMapUtils;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.CommanderInfoContainer;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.NationInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.TitlePanel;
import com.eaw1805.www.fieldbattle.widgets.AdWidget;
import com.eaw1805.www.fieldbattle.widgets.DrawingAreaFB;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;

import java.util.HashMap;
import java.util.Map;

public class MainPanel extends AbsolutePanel {

    private static MainPanel instance;

    public static MainPanel getInstance() {
        if (instance == null) {
            instance = new MainPanel();
        }
        return instance;
    }


    private ScrollPanel drawingScroller = new ScrollPanel();
    DrawingAreaFB drawingArea;
    Label debugLabel = new Label("This is a debugging message");
    MapUtils mapUtils;
    MiniMapUtils miniMapUtils;
    MiniMapPanel miniMap;


    PlaybackPanel playback;
    PlaybackInfoPanel playbackInfo;
    BattleLogPanel logPanel;
    ArmiesOverviewPanel overviewPanel;

    TitlePanel titlePanel;

    Map<Integer, NationInfoPanel> nationIdToInfoPanel;

    ArmyBarPanel armyBar;
    BrigadeInfoContainerPanel armyInfo;
    LoadingPanel loading;
    CommanderInfoContainer commContainer;
//    AdWidget firstAd;
    int offset = 0;

    ImageButton imgX;


    private MainPanel() {
        //be sure main panel takes all the available space on the screen.
        setSize("100%", "100%");
        //do nothing else

        //never call a class/method that uses MainPanel inside him in here!!
        // otherwise infinite loops will occur.
    }


    public void showLoadingPanel() {
        //add first...
        if (loading == null) {
            loading = new LoadingPanel();
        }
        //but put it on top of everything else...
        loading.getElement().getStyle().setZIndex(1000);
        add(loading, 0, 0);
    }

    public void hideLoadingPanel() {
        if (loading != null && loading.isAttached()) {
            remove(loading);
        }
    }

    public LoadingPanel getLoading() {
        return loading;
    }

    //this should be called at the very beginning
    public void initPanel() {
        nationIdToInfoPanel = new HashMap<Integer, NationInfoPanel>();

        drawingArea = new DrawingAreaFB(1000, 1000, this);
        drawingScroller.setWidget(drawingArea);

        add(drawingScroller);
        add(debugLabel, 800, 0);
        debugLabel.setVisible(true);

        drawingScroller.setStyleName("noScrollBars");
        drawingScroller.setAlwaysShowScrollBars(false);

        drawingArea.setScroller(drawingScroller);
        debugLabel.getElement().getStyle().setBackgroundColor("green");


        miniMap = new MiniMapPanel();
        add(miniMap, 0, 0);


        mapUtils = new MapUtils();
        miniMapUtils = new MiniMapUtils();

        playback = new PlaybackPanel();
        add(playback, (Window.getClientWidth() - 600) / 2, 5);
        playback.setVisible(!BaseStore.getInstance().isStartRound());
        playbackInfo = new PlaybackInfoPanel();
        add(playbackInfo, 0, Window.getClientHeight() - 140 - 285);
        playbackInfo.setVisible(!BaseStore.getInstance().isStartRound());

        logPanel = new BattleLogPanel();
        add(logPanel, Window.getClientWidth() - 275, Window.getClientHeight() - 415);
        logPanel.setVisible(!BaseStore.getInstance().isStartRound());

        overviewPanel = new ArmiesOverviewPanel();
        add(overviewPanel, (Window.getClientWidth() - 900) / 2 , (Window.getClientHeight() - 600) / 2);
        overviewPanel.setVisible(false);
        offset = 0;
        if (BaseStore.getInstance().isStartRound()) {
            offset = 55;
        }
        titlePanel = new TitlePanel();
        add(titlePanel, (Window.getClientWidth() - titlePanel.getWidth()) / 2, 65 - offset);


        if (!BaseStore.getInstance().isMiddleRound()) {
            MainPanel.getInstance().addArmySelectionPanel();
        }

        armyInfo = new BrigadeInfoContainerPanel();
        add(armyInfo, 0, Window.getClientHeight() - 155);

        armyBar = new ArmyBarPanel();
        if (BaseStore.getInstance().isStartRound()) {
            add(armyBar, 371, Window.getClientHeight() - 155);
        }
        //that means it is middle round
        commContainer = new CommanderInfoContainer(BaseStore.getInstance().getNationId());
        if (!BaseStore.getInstance().isStartRound() && !BaseStore.getInstance().isGameEnded()) {
            add(commContainer, 219, 3);
        }
        imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Back to Main Menu");
        imgX.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                LoadUtil.getInstance().loadSocialPanel(true);
            }
        });
        imgX.setSize("30px", "30px");
        add(imgX, Window.getClientWidth() - 35, 5);
        //this should be only visible when it is a stand alone game.
        imgX.setVisible(BaseStore.getInstance().isStandAlone());
        setMapSize();
    }

    public void initNationsInfoPanels() {
        int size = BaseStore.getInstance().getAlliedNations().size();
        int count = 0;
        for (Integer nation : BaseStore.getInstance().getAlliedNations()) {

            nationIdToInfoPanel.put(nation, new NationInfoPanel(nation));
            add(nationIdToInfoPanel.get(nation), (Window.getClientWidth() - titlePanel.getWidth()) / 2 - 160 - count*163, 65 - offset);
            count++;
        }
        count = 0;
        for (Integer nation : BaseStore.getInstance().getEnemyNations()) {

            nationIdToInfoPanel.put(nation, new NationInfoPanel(nation));
            add(nationIdToInfoPanel.get(nation), (Window.getClientWidth() - titlePanel.getWidth()) / 2 + titlePanel.getWidth() + count*163, 65 - offset);
            count++;
        }
    }

    public void updateNationInfoPanels(final int round) {
        for (Integer nation : BaseStore.getInstance().getAlliedNations()) {
            nationIdToInfoPanel.get(nation).setLabelValues(round);
        }
        for (Integer nation : BaseStore.getInstance().getEnemyNations()) {
            nationIdToInfoPanel.get(nation).setLabelValues(round);
        }
    }

    public void updateNationInfoPanels() {
        for (Integer nation : BaseStore.getInstance().getAlliedNations()) {
            nationIdToInfoPanel.get(nation).setLabelValues();
        }
        for (Integer nation : BaseStore.getInstance().getEnemyNations()) {
            nationIdToInfoPanel.get(nation).setLabelValues();
        }
    }

    public void updateNationCommanderPanels() {
        commContainer.updatePanel();
    }

    public NationInfoPanel getNationInfoPanel(final int nationId) {
        return nationIdToInfoPanel.get(nationId);
    }


    public void addArmySelectionPanel() {

    }

    public int getWidgetX(final Widget widget) {
        return getWidgetLeft(widget);
    }

    public int getWidgetY(final Widget widget) {
        return getWidgetTop(widget);
    }

    public void setWidgetPosition(final Widget widget, final int x, int y) {
        if (y < 0) {
            y = 0;
        }
        super.setWidgetPosition(widget, x, y);
    }

    public MapUtils getMapUtils() {
        return mapUtils;
    }

    public MiniMapUtils getMiniMapUtils() {
        return miniMapUtils;
    }

    public void setMapSize() {
        final int width = Window.getClientWidth();
        final int height = Window.getClientHeight();
        drawingScroller.setSize(width + "px", height + "px");
    }

    public void handleKeyDownEvent(int keyCode) {
        drawingArea.handleKeyDownEvent(keyCode);
    }

    public void handleKeyUpEvent(int keyCode) {
        drawingArea.handleKeyUpEvent(keyCode);
    }

    public void setDebugMessage(final String msg) {
        debugLabel.setText(msg);
    }

    public DrawingAreaFB getDrawingArea() {
        return drawingArea;
    }

    public void addWidgetToScreen(final Widget panel) {
        add(panel, 0, 0);
    }

    public void positionToCenter(final Widget widget) {
        setWidgetPosition(widget,
                (Window.getClientWidth() - widget.getOffsetWidth()) / 2,
                (Window.getClientHeight() - widget.getOffsetHeight()) / 2);
    }

    public void addToCenter(final Widget widget) {
        add(widget, -20000, -20000);
        positionToCenter(widget);
    }

    public void onResize() {
        if (loading != null) {
            loading.onResize();
        }
        if (logPanel.isOpened()) {
            setWidgetPosition(logPanel, Window.getClientWidth() - 275, Window.getClientHeight() - 415);
        } else {
            setWidgetPosition(logPanel, Window.getClientWidth() - 40, Window.getClientHeight() - 415);
        }

        setWidgetPosition(playback, (Window.getClientWidth() - 600) / 2, 5);
        setWidgetPosition(titlePanel, (Window.getClientWidth() - titlePanel.getWidth()) / 2, 65 - offset);
        setWidgetPosition(imgX, Window.getClientWidth() - 35, 5);

        if (armyInfo.isOpened()) {
            setWidgetPosition(armyInfo, 0, Window.getClientHeight() - 155);
            setWidgetPosition(playbackInfo, 0, Window.getClientHeight() - 140 - 285);
            if (BaseStore.getInstance().isStartRound()) {
                setWidgetPosition(armyBar, 371, Window.getClientHeight() - 155);
            }
        } else {
            setWidgetPosition(armyInfo, 0, Window.getClientHeight() - 25);
            setWidgetPosition(playbackInfo, -275, Window.getClientHeight() - 155 - 285);
            if (BaseStore.getInstance().isStartRound()) {
                setWidgetPosition(armyBar, 371, Window.getClientHeight() - 40);
            }
        }
//        setWidgetPosition(firstAd, Window.getClientWidth() - 200, 50);

        int count = 0;
        for (Integer nation : BaseStore.getInstance().getAlliedNations()) {
            if (nationIdToInfoPanel.containsKey(nation)) {
                setWidgetPosition(nationIdToInfoPanel.get(nation), (Window.getClientWidth() - titlePanel.getWidth()) / 2 - 160 - count*163, 65 - offset);
            }
            count++;
        }
        count = 0;
        for (Integer nation : BaseStore.getInstance().getEnemyNations()) {
            if (nationIdToInfoPanel.containsKey(nation)) {
                setWidgetPosition(nationIdToInfoPanel.get(nation), (Window.getClientWidth() - titlePanel.getWidth()) / 2 + titlePanel.getWidth() + count*163, 65 - offset);
            }
            count++;
        }
    }


    public void removePanelFromScreen(final Widget panel) {
        //first remove from panel
        try {
            remove(panel);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public MiniMapPanel getMiniMap() {
        return miniMap;
    }

    public PlaybackPanel getPlayback() {
        return playback;
    }

    public PlaybackInfoPanel getPlaybackInfo() {
        return playbackInfo;
    }

    public BattleLogPanel getLogPanel() {
        return logPanel;
    }


    public void fitMapToScreen() {
        //do zoom so the map will fit on the screen
        double screenHeight = (double) Window.getClientHeight();
        double mapHeight = (double) mapUtils.getMapHeight();
        double zoomLevel = screenHeight * 1.0 / mapHeight;
        drawingArea.rememberCurrentZoom();
        drawingArea.zoom(zoomLevel);
        //center map
        int x = miniMapUtils.getMiniMapWidth() / 2;
        int y = miniMapUtils.getMiniMapHeight() / 2;
        miniMap.getMap().moveMap(x, y, true);
    }

    private void firePlaybackEvent(final BasicHandler handler) {
        handler.run();
    }

//    public UnitsInfoPanel getUnitsPanel() {
//        return unitsPanel;
//    }


    public ArmiesOverviewPanel getOverviewPanel() {
        return overviewPanel;
    }

    public ArmyBarPanel getArmyBar() {
        return armyBar;
    }

    public BrigadeInfoContainerPanel getArmyInfo() {
        return armyInfo;
    }
}
