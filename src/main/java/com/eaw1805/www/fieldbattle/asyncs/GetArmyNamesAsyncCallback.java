package com.eaw1805.www.fieldbattle.asyncs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

import java.util.List;
import java.util.Map;


public class GetArmyNamesAsyncCallback implements AsyncCallback<ArmyData> {

    public GetArmyNamesAsyncCallback() {

    }

    public void onFailure(final Throwable caught) {
        // do nothing
        Window.alert("Map failed");
    }

    public void onSuccess(final ArmyData result) {
        ArmyStore.getInstance().initArmiesData(result);
        CommanderStore.getInstance().initOverallCommander();
        MainPanel.getInstance().getLoading().addLoadingStep("Army names loaded");
    }
}
