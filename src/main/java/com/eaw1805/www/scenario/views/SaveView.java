package com.eaw1805.www.scenario.views;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.scenario.remote.EmpireScenarioRpcService;
import com.eaw1805.www.scenario.remote.EmpireScenarioRpcServiceAsync;
import com.eaw1805.www.scenario.stores.EditorStore;

public class SaveView extends VerticalPanel {
    private final static EmpireScenarioRpcServiceAsync service = GWT.create(EmpireScenarioRpcService.class);

    public SaveView() {
        getElement().getStyle().setBackgroundColor("grey");
        final Button save = new Button("Save");
        save.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                service.saveData(EditorStore.getInstance().getGameId(), EditorStore.getInstance().getRegions(),
                        EditorStore.getInstance().getRegionSectors(),
                        EditorStore.getInstance().getRegionToNationToWarehouse(),
                        EditorStore.getInstance().getBrigades(),
                        EditorStore.getInstance().getCorps(),
                        EditorStore.getInstance().getArmies(),
                        EditorStore.getInstance().getCommanders(),
                        EditorStore.getInstance().getSpies(),
                        EditorStore.getInstance().getBaggageTrains(),
                        EditorStore.getInstance().getShips(),
                        EditorStore.getInstance().getFleets(),
                        EditorStore.getInstance().getJumpOffPoints(),
                        new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("***Failed to save changes***");
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result == 2) {
                            Window.alert("!!Changes saved!! Client will reload.");
                            Window.Location.reload();
                        } else {
                            Window.alert("!!Changes saved!!");
                        }

                    }
                });
            }
        });
        add(save);
    }
}
