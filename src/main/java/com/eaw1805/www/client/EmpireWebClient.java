package com.eaw1805.www.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.www.client.asyncs.CallForAlliesAsyncCallback;
import com.eaw1805.www.client.asyncs.ChatMessagesAsyncCallback;
import com.eaw1805.www.client.asyncs.GameProcessAsyncCallback;
import com.eaw1805.www.client.asyncs.GameSettingsAsyncCallback;
import com.eaw1805.www.client.asyncs.GameStatusAsyncCallback;
import com.eaw1805.www.client.asyncs.NationsStatusAsyncCallback;
import com.eaw1805.www.client.asyncs.RelationAsyncCallback;
import com.eaw1805.www.client.asyncs.army.ArmyTypeAsyncCallback;
import com.eaw1805.www.client.asyncs.army.BarracksAsyncCallback;
import com.eaw1805.www.client.asyncs.economy.DataCollectionAsyncCallback;
import com.eaw1805.www.client.asyncs.economy.WarehouseAsyncCallback;
import com.eaw1805.www.client.asyncs.map.RegionDataAsyncCallBack;
import com.eaw1805.www.client.asyncs.navy.ShipTypeAsyncCallback;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.events.loading.AppInitEvent;
import com.eaw1805.www.client.events.loading.AppInitHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandler;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.views.LoadingView;
import com.eaw1805.www.client.views.frames.BattleFrame;
import com.eaw1805.www.client.views.layout.LayoutView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.map.MinimapGroups;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.AnimationUtils;
import com.eaw1805.www.shared.stores.util.BasicHandler;

import java.util.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EmpireWebClient
        implements EntryPoint, RegionConstants {

    private static final String sessResolution = "staticRes";

    /**
     * This is the declaration of the widgets that we will use.
     */
    private final VerticalPanel mainPanel = new VerticalPanel();
    private LoadingView loadingView;
    private LayoutView layoutView;
    private RootPanel rootPanel;
    /**
     * This is the declaration of the Asynchronous Callbacks we will use.
     */
    private final static EmpireRpcServiceAsync empireService = GWT.create(EmpireRpcService.class);
    private RegionDataAsyncCallBack regionAsync2;

    private final RelationAsyncCallback relationAsync = new RelationAsyncCallback();
    private final WarehouseAsyncCallback warehouseAsync = new WarehouseAsyncCallback(this);
    private final ArmyTypeAsyncCallback armyTypeAsync = new ArmyTypeAsyncCallback();
    private final ShipTypeAsyncCallback shipTypeAsync = new ShipTypeAsyncCallback();
    private final BarracksAsyncCallback barrackAsync = new BarracksAsyncCallback();
    private final DataCollectionAsyncCallback resosAsync = new DataCollectionAsyncCallback();
    private final GameSettingsAsyncCallback settingsAsync = new GameSettingsAsyncCallback();
    private final GameStatusAsyncCallback statusAsync = new GameStatusAsyncCallback();
    private final GameProcessAsyncCallback procAsync = new GameProcessAsyncCallback();
    private final ChatMessagesAsyncCallback chMsgAsync = new ChatMessagesAsyncCallback();
    private final NationsStatusAsyncCallback nationsStatusAsync = new NationsStatusAsyncCallback();
    private final CallForAlliesAsyncCallback callAlliesAsync = new CallForAlliesAsyncCallback();
    /**
     * This is an overall application controller
     */
    public static ApplicationManager appManager = new ApplicationManager(empireService);

    /**
     * This is the entry point method.
     */
    private boolean isFirstLoad = true;

    public void onModuleLoad() {
        final int scenarioId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("scenarioId")).getValue());
        final int gameId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("gameId")).getValue());
        final int nationId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("nationId")).getValue());
        final int turn = Integer.parseInt(((InputElement) (Element) DOM.getElementById("turn")).getValue());
        final String nationName = ((InputElement) (Element) DOM.getElementById("nationName")).getValue();
        final boolean firstLoad = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("firstLoad")).getValue());
        String currentMonth = com.google.gwt.user.client.Window.Location.getParameter("month");
        final String userName = ((InputElement) (Element) DOM.getElementById("usernameStr")).getValue();
        final String isTutorial = ((InputElement) (Element) DOM.getElementById("isTutorial")).getValue();
        final boolean fogOfWar = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("fogOfWar")).getValue());
        final boolean fullscreen = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("fullscreen")).getValue());
        final boolean colonyMps = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("colonyMps")).getValue());
        final boolean fastAssignment = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("fastAssignment")).getValue());
        final boolean doubleCostsArmy = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("doubleCostsArmy")).getValue());
        final boolean doubleCostsNavy = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("doubleCostsNavy")).getValue());
        final boolean fastShipConstruction = "true".equalsIgnoreCase(((InputElement) (Element) DOM.getElementById("fastShipConstruction")).getValue());

        GameStore.getInstance().initGameVariables(scenarioId, gameId, nationId, turn, nationName,
                firstLoad, userName, fogOfWar, fullscreen, colonyMps, fastAssignment, fastShipConstruction,
                doubleCostsArmy, doubleCostsNavy);

        loadingView = new LoadingView(this);
        layoutView = new LayoutView();
        regionAsync2 = new RegionDataAsyncCallBack(layoutView.getMap());

        rootPanel = RootPanel.get("MainPanel");
        rootPanel.setSize("100%", "100%");
        rootPanel.add(mainPanel);

        //manage keyboard input
        RootPanel.get().addDomHandler(new KeyDownHandler() {

            public void onKeyDown(final KeyDownEvent event) {
                if (!MapStore.getInstance().isFocusLocked()) {
                    GameStore.getInstance().getLayoutView().getMap().getMapDrawArea().scrollKeyPressed(event.getNativeKeyCode());
                }

                if (event.getNativeKeyCode() == 9) {//if tab try to stop tabs bad effect
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, KeyDownEvent.getType());

        //stop map moving if on
        RootPanel.get().addDomHandler(new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                //only do actions if focus is not locked by another element
                GameStore.getInstance().getLayoutView().getMap().getMapDrawArea().scrollKeyUnPressed(event.getNativeKeyCode());
            }
        }, KeyUpEvent.getType());

        //manage most of page clicks.
        RootPanel.get().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent clickEvent) {
//                ClientUtil.startSpeedTest("globalClickHandler");
                final Element var = clickEvent.getNativeEvent().getEventTarget().cast();
                if (!var.getId().isEmpty()) {
                    final List<DelEventHandler> handlers = GameStore.getInstance().getEventHandler(var.getId());
                    if (handlers != null) {
                        for (DelEventHandler handler : handlers) {
                            handler.execute(clickEvent);
                        }
                    }
                }

            }
        }, ClickEvent.getType());

        mainPanel.setStyleName("MainPanelDivOnLogin");

        GameStore.getInstance().setLayoutView(layoutView);
        mainPanel.add(layoutView);
        loadingView.getElement().getStyle().setZIndex(9999999);
        layoutView.addWidgetToPanel(loadingView, 0, 0);
        layoutView.getElement().getStyle().setZIndex(1000);
        if (currentMonth == null) {
            currentMonth = "1";
        }
        TutorialStore.getInstance().initTutorialVars((isTutorial != null && "true".equals(isTutorial)), userName,
                turn + 1);

        RegionStore.getInstance().setInitialized(false);
        ArmyStore.getInstance().setInitialized(false);
        GameEventManager.GameStarted();
        //Initialize the session resolution available to all browsers
        Cookies.setCookie(sessResolution, "no");

        setSize(Window.getClientWidth(), Window.getClientHeight(), true);

        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(final ResizeEvent event) {
                setSize(event.getWidth(), event.getHeight(), true);
            }
        });

        LoadEventManager.addAppInitHandler(new AppInitHandler() {
            public void onApplicationInit(final AppInitEvent e) {
                constructPage();
                if (GameStore.getInstance().isFirstLoad()) {
                    requestNewsletter();

                } else {
                    GameStore.getInstance().showCallForAllies(0);
                }

                if (!GameStore.getInstance().isGameEnded()) {
                    empireService.getProcessDate(GameStore.getInstance().getScenarioId(),
                            GameStore.getInstance().getGameId(),
                            new Date().getTimezoneOffset(),
                            procAsync);
                }
            }
        });

        empireService.getGameStatus(scenarioId, gameId, statusAsync);

    }

    public void requestNewsletter() {
        final BattleFrame frame = new BattleFrame(0, false, false, true, null, true);
        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(frame);
        GameStore.getInstance().getLayoutView().positionTocCenter(frame);
    }

    public void constructPage() {
        loadingView.setStyleName("disablePointerEvents", true);
        if (GameStore.getInstance().getZoomLevel() > 0) {
            MapStore.getInstance().doZoom(GameStore.getInstance().getZoomLevel());
        }
//        layoutView.removeWidgetFromPanel(loadingView);
        AnimationUtils.hideElement(loadingView, 100, new BasicHandler() {
            @Override
            public void run() {
                //in the end of the animation... just remove it
                layoutView.removeWidgetFromPanel(loadingView);
                layoutView.initGraphicsPanel();
            }
        });

        mainPanel.setSize("100%", "100%");
        RootPanel.get().setStyleName("body");
        rootPanel.setSize("100%", "100%");

        if (isFirstLoad) {
            isFirstLoad = false;
            try {
                layoutView.getMap().gotoStartingCoord(EUROPE);
                if (TutorialStore.getInstance().isTutorialMode()) {
                    TutorialStore.getInstance().nextStep(false);
                }

            } catch (Exception e) {
                new ErrorPopup(ErrorPopup.Level.ERROR, "Unable to construct layout.", false);
            }
        }
    }

    public void initViews() {
        try {
            final int scenarioId = GameStore.getInstance().getScenarioId();
            final int nationId = GameStore.getInstance().getNationId();
            final int gameId = GameStore.getInstance().getGameId();
            final int turn = GameStore.getInstance().getTurn();
            final int regionId = EUROPE;
            MapStore.getInstance().initVectorHashMaps();
            MapStore.getInstance().setActiveRegion(EUROPE);
            loadingView.nextLoadingStep("Now loading... Requesting data from server");

            // Load all global data from the database
            empireService.getGameSettings(scenarioId, nationId, gameId, turn, settingsAsync);
            empireService.getNaturalResAndProdSites(scenarioId, resosAsync);
            empireService.getNationsStatus(scenarioId, gameId, turn, nationsStatusAsync);
            empireService.getCallForAllies(scenarioId, nationId, gameId, turn, callAlliesAsync);
            empireService.getWarehouses(scenarioId, gameId, nationId, warehouseAsync);
            empireService.getNationsRelations(scenarioId, gameId, nationId, turn, relationAsync);
            empireService.getRegionByGameAndNation(scenarioId, regionId, gameId, turn, nationId, regionAsync2);
            empireService.getArmyTypes(scenarioId, nationId, armyTypeAsync);
            empireService.getShipTypes(scenarioId, nationId, shipTypeAsync);
            empireService.getBarracksByNation(scenarioId, nationId, gameId, turn, barrackAsync);
            empireService.getChatMessageHistory(scenarioId, nationId, gameId, chMsgAsync);
            for (int curReg = EUROPE; curReg <= AFRICA; curReg++) {
                MinimapGroups.getInstance().retrieveSupplies(curReg);
            }
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "We had a problem loading the game. Please try again.", false);
        }


    }

    //trying to fix scaling issues.
    private final Timer timer = new Timer() {
        public void run() {
            setSize(Window.getClientWidth(), Window.getClientHeight(), false);
        }
    };

    private void setSize(final int width, final int height, final boolean forceResize) {

        if (forceResize) {
            //after 0.1 second resize again...
            timer.schedule(100);
        }
        rootPanel.setSize(width + "px", height + "px");
        mainPanel.setSize(width + "px", height + "px");
        layoutView.reSize();
    }

    /**
     * @return the layoutView
     */
    public LayoutView getLayoutView() {
        return layoutView;
    }

    public void fireEvent(final GwtEvent<?> event) {
        // do nothing
    }

}
