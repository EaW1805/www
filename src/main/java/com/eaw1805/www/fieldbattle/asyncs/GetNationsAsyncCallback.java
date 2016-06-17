package com.eaw1805.www.fieldbattle.asyncs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.collections.FieldData;
import com.eaw1805.www.fieldbattle.EmpireFieldBattleClient;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;


public class GetNationsAsyncCallback implements AsyncCallback<FieldData> {



    public GetNationsAsyncCallback() {


    }

    public void onFailure(final Throwable caught) {
        // do nothing
        Window.alert("Nations failed");
    }

    public void onSuccess(final FieldData result) {
        BaseStore.getInstance().initNations(result);

        try {
//        MainPanel.getInstance().getRoundInfo().initNations();
            MainPanel.getInstance().initNationsInfoPanels();
        } catch (Exception e) {
            Window.alert("failed " + e.toString());
        }
        MainPanel.getInstance().getLoading().addLoadingStep("Loading nations");
        LoadUtil.getInstance().secondLoadingStep();
    }

}

