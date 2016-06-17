package com.eaw1805.www.fieldbattle.asyncs;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.field.FieldBattleMapDTO;
import com.eaw1805.www.fieldbattle.EmpireFieldBattleClient;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.stores.calculators.MapStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

public class GetMapAsyncCallback implements AsyncCallback<FieldBattleMapDTO> {


    public void onFailure(final Throwable caught) {
        // do nothing
        Window.alert("Map failed");
    }

    public void onSuccess(final FieldBattleMapDTO result) {
        MapStore.getInstance().init(result);
        MainPanel.getInstance().getMapUtils().initMap(result);
        MainPanel.getInstance().getMiniMapUtils().initMap(result);
        MainPanel.getInstance().getMiniMap().getMap().updateRectangle();
        MainPanel.getInstance().getLoading().addLoadingStep("Map Loaded");
        LoadUtil.getInstance().thirdLoadingStep();
    }

}

