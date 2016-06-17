package com.eaw1805.www.fieldbattle.asyncs;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

import java.util.Map;

public class GetArmiesAsyncCallback implements AsyncCallback<Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>>> {

    public GetArmiesAsyncCallback() {

    }

    public void onFailure(final Throwable caught) {
        // do nothing
        Window.alert("Map failed");
    }

    public void onSuccess(final Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>> result) {
        ArmyStore.getInstance().initBrigades(result);
        CommanderStore.getInstance().initOverallCommander();
        MainPanel.getInstance().getLoading().addLoadingStep("Armies Loaded");
    }

}

