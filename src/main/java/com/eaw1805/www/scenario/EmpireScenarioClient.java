package com.eaw1805.www.scenario;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.www.scenario.asyncs.GetRegionDataCallback;
import com.eaw1805.www.scenario.remote.EmpireScenarioRpcService;
import com.eaw1805.www.scenario.remote.EmpireScenarioRpcServiceAsync;
import com.eaw1805.www.scenario.stores.ArmyData;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.StaticEditorData;
import com.eaw1805.www.scenario.views.EditorPanel;

import java.util.List;
import java.util.Map;

public class EmpireScenarioClient  implements EntryPoint, RegionConstants {

    private final static EmpireScenarioRpcServiceAsync service = GWT.create(EmpireScenarioRpcService.class);
    final GetRegionDataCallback regionCallback = new GetRegionDataCallback();
    @Override
    public void onModuleLoad() {
        final RootPanel rootPanel = RootPanel.get("MainPanel");
        rootPanel.setSize("100%", "100%");

        final int gameId = Integer.parseInt(((InputElement) (Element) DOM.getElementById("gameId")).getValue());
        //init editor.
        EditorStore.getInstance().setGameId(gameId);
        rootPanel.add(EditorPanel.getInstance());
        RootPanel.get().addDomHandler(new KeyDownHandler() {

            public void onKeyDown(final KeyDownEvent event) {
                EditorPanel.getInstance().getDrawingArea().
                        handleKeyDownEvent(event.getNativeKeyCode());

                if (event.getNativeKeyCode() == 9) {//if tab try to stop tabs bad effect
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, KeyDownEvent.getType());

        RootPanel.get().addDomHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(final KeyUpEvent event) {
                EditorPanel.getInstance().getDrawingArea()
                        .handleKeyUpEvent(event.getNativeKeyCode());

                if (event.getNativeKeyCode() == 9) {//if tab try to stop tabs bad effect
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, KeyUpEvent.getType());


        loadStaticData();
    }

    public void loadStaticData() {
        service.getEditorStaticData(new AsyncCallback<StaticEditorData>() {
            @Override
            public void onFailure(final Throwable throwable) {
                Window.alert("Failed to retrieve static data");
            }

            @Override
            public void onSuccess(final StaticEditorData staticEditorData) {
                try {
                    EditorStore.getInstance().setStaticData(staticEditorData.getNations(), staticEditorData.getGoods(), staticEditorData.getResources(), staticEditorData.getTerrains(), staticEditorData.getProductionSites(), staticEditorData.getArmyTypes(), staticEditorData.getRanks(), staticEditorData.getShipTypes());

                } catch (Exception e) {
                    Window.alert(e.toString());
                }
                loadRegionData();
            }
        });
    }

    public void loadRegionData() {
        service.getRegionData(EditorStore.getInstance().getGameId(), new AsyncCallback<Map<RegionDTO, List<SectorDTO>>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failed to load region data");
            }

            @Override
            public void onSuccess(Map<RegionDTO, List<SectorDTO>> result) {
                EditorStore.getInstance().setRegionsMap(result);
                loadUnitsData();
            }
        });

    }

    public void loadUnitsData() {

        service.getWarehouses(EditorStore.getInstance().getGameId(), new AsyncCallback<Map<Integer, Map<Integer, WarehouseDTO>>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failed to retrieve warehouses... please try reloading the page");
            }

            @Override
            public void onSuccess(Map<Integer, Map<Integer, WarehouseDTO>> result) {
                try {
                    EditorStore.getInstance().setWarehouseData(result);
                } catch (Exception e) {
                    Window.alert("Failed initializing warehouses : " + e);
                }
            }
        });

        service.getArmies(EditorStore.getInstance().getGameId(), new AsyncCallback<ArmyData>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Failed to get armies");
            }

            @Override
            public void onSuccess(ArmyData data) {
                EditorStore.getInstance().setArmyData(data.getBrigades(),
                        data.getCorps(),
                        data.getArmies(),
                        data.getSpies(),
                        data.getBaggageTrains(),
                        data.getCommanders(),
                        data.getShips(),
                        data.getFleets(),
                        data.getJumpOffPoints());
            }
        });

        service.getCommanderNamesByNation(EditorStore.getInstance().getGameId(), new AsyncCallback<Map<Integer, List<String>>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert("Failed to get commander names");
            }

            @Override
            public void onSuccess(Map<Integer, List<String>> integerListMap) {
                EditorStore.getInstance().setCommanderNamesData(integerListMap);
            }
        });
        EditorPanel.getInstance().initPanel();
    }
}
