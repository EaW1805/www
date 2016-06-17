package com.eaw1805.www.fieldbattle;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.web.field.FieldBattleHalfRoundStatisticsDTO;
import com.eaw1805.www.fieldbattle.asyncs.*;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedEvent;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedHandler;
import com.eaw1805.www.fieldbattle.events.loading.LoadingEventManager;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.stores.dto.SocialSettings;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.views.layout.social.SocialLoadingPanel;
import com.eaw1805.www.fieldbattle.views.layout.social.StandAloneSocialPanel;

import java.util.ArrayList;
import java.util.List;

public class LoadUtil {

    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);
    private final GetNationsAsyncCallback nationsCallback = new GetNationsAsyncCallback();

    private final GetMapAsyncCallback mapCallback = new GetMapAsyncCallback();
    private final GetArmiesAsyncCallback armiesCallback = new GetArmiesAsyncCallback();
    private final GetArmyNamesAsyncCallback armyDataCallback = new GetArmyNamesAsyncCallback();
    private final GetCommandersAsyncCallback commandersCallback = new GetCommandersAsyncCallback();
    private static LoadUtil instance;
    private final List<HandlerRegistration> handlersToClean = new ArrayList<HandlerRegistration>();
    final AbsolutePanel bgPanel = new AbsolutePanel();
    private LoadUtil() {
        bgPanel.setStyleName("loginPanel");
        bgPanel.setSize(Window.getClientWidth() +"px", Window.getClientHeight() +"px");
    }

    public static LoadUtil getInstance() {
        if (instance == null) {
            instance = new LoadUtil();
        }
        return instance;
    }

    /**
     * Exits a game, must clear everything the game left behind.
     */
    public void loadSocialPanel(boolean clearStores) {
        //first clear stores.
        try {
            if (clearStores) {
                clearStores();
            } else {
                StandAloneSocialPanel.getInstance().setVisible(false);
                SocialLoadingPanel.getInstance().center();
                SocialLoadingPanel.getInstance().load();
            }

        } catch (Exception e) {
            Window.alert("loadsocialpanel? " + e.toString());
        }

        //then

        //clear previous data
        MainPanel.getInstance().clear();
        MainPanel.getInstance().add(bgPanel, 0, 0);
        //clear previous handlers
        for (HandlerRegistration handler : handlersToClean) {
            handler.removeHandler();
        }
        //show new data
        StandAloneSocialPanel.getInstance().setEnabled(true);
        if (!StandAloneSocialPanel.getInstance().isAttached()) {
            MainPanel.getInstance().addWidgetToScreen(StandAloneSocialPanel.getInstance());
            MainPanel.getInstance().positionToCenter(StandAloneSocialPanel.getInstance());
        }
        //clear any previous datafrom main panel
    }

    /**
     * Load a game from the social settings.
     *
     * @param settings Contains all the info needed to load a game.
     */
    public void load(final SocialSettings settings) {
        MainPanel.getInstance().remove(bgPanel);
        //first disable social panel
        StandAloneSocialPanel.getInstance().setEnabled(false);
        //then hide social panel
        if (StandAloneSocialPanel.getInstance().isAttached()) {
            MainPanel.getInstance().removePanelFromScreen(StandAloneSocialPanel.getInstance());
        }
        try {
        //1, initialize game variables
        BaseStore.getInstance().initVariables(settings.getScenarioId(), settings.getBattleId(),
                settings.getNationId(), settings.getRound(), settings.getSide(),
                settings.getTitle(), settings.isSideReady(), settings.isGameEnded(),
                settings.getWinner(), settings.isStandAlone(), settings.getFacebookId());
        //2. initialize gui
        //clear main panel
        MainPanel.getInstance().clear();
        //clear stores
//        clearStores();
        //show loading panel
        MainPanel.getInstance().showLoadingPanel();
            MainPanel.getInstance().getLoading().resetLoading();
        //initialize gui
        MainPanel.getInstance().initPanel();

        //3. retrieve data from server
        firstLoadingStep();
        } catch (Exception e) {
            Window.alert("Fff? " + e.toString());
        }

    }



    public void clearStores() {
        MainPanel.getInstance().getMapUtils().clear();
        MainPanel.getInstance().getMiniMapUtils().clear();
        ArmyStore.getInstance().clear();
        BaseStore.getInstance().clear();
        CommanderStore.getInstance().clear();
        PlaybackStore.getInstance().clear();

    }

    public void firstLoadingStep() {
        service.getNations(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), BaseStore.getInstance().getNationId(), nationsCallback);
    }

    public void secondLoadingStep() {
        service.getMap(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), mapCallback);
        service.getArmyData(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), BaseStore.getInstance().getNationId(), armyDataCallback);
        service.getArmies(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), BaseStore.getInstance().getNationId(), armiesCallback);
        service.getCommandersByNation(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), BaseStore.getInstance().getNationId(), commandersCallback);
    }


    public void thirdLoadingStep() {
        if (!BaseStore.getInstance().isStartRound()) {

            service.getFieldBattleReports(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), new AsyncCallback<List<FieldBattleHalfRoundStatisticsDTO>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Window.alert("Failed to get reports");
                }

                @Override
                public void onSuccess(final List<FieldBattleHalfRoundStatisticsDTO> result) {

                    try {
                        PlaybackStore.getInstance().initStatistics(result);
                        MainPanel.getInstance().getPlayback().updateSliderValues(0, result.size() - 1, 1);
                        MainPanel.getInstance().getMapUtils().initPlaybackAdditions();
                        if (ArmyStore.getInstance().isInitialized()) {
                            MainPanel.getInstance().getMapUtils().initArmyGroups();
                            MainPanel.getInstance().getMapUtils().placeDummyEnememies();
                        } else {
                            handlersToClean.add(LoadingEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
                                @Override
                                public void onUnitChanged(ArmiesLoadedEvent event) {
                                    MainPanel.getInstance().getMapUtils().initArmyGroups();
                                    MainPanel.getInstance().getMapUtils().placeDummyEnememies();
                                }
                            }));

                        }
                        MainPanel.getInstance().updateNationCommanderPanels();
                        MainPanel.getInstance().getLoading().addLoadingStep("Reports Loaded");
                        MainPanel.getInstance().getOverviewPanel().updateTurn(0);

                    } catch (Exception e) {
                        Window.alert("fff ? " + e.toString());
                    }
                }
            });
        } else {
            if (ArmyStore.getInstance().isInitialized()) {
                MainPanel.getInstance().getMapUtils().initArmyGroups();
                MainPanel.getInstance().getMapUtils().placeDummyEnememies();
                MainPanel.getInstance().getOverviewPanel().updateTurn(0);

            } else {

                handlersToClean.add(LoadingEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
                    @Override
                    public void onUnitChanged(ArmiesLoadedEvent event) {
                        MainPanel.getInstance().getMapUtils().initArmyGroups();
                        MainPanel.getInstance().getMapUtils().placeDummyEnememies();
                        MainPanel.getInstance().getOverviewPanel().updateTurn(0);

                    }
                }));

            }

            MainPanel.getInstance().updateNationCommanderPanels();
            MainPanel.getInstance().getLoading().addLoadingStep("Reports Loaded");


        }
        int[] coords = MainPanel.getInstance().getMapUtils().getCenterSetupAreaCoords();
        MainPanel.getInstance().getMiniMap().getMap().moveMap(MainPanel.getInstance().getMiniMapUtils().getPointX(coords[0]), MainPanel.getInstance().getMiniMapUtils().getPointX(coords[1]), false);
    }

    public void registerHandlerForClean(HandlerRegistration handler) {
        handlersToClean.add(handler);
    }
}
