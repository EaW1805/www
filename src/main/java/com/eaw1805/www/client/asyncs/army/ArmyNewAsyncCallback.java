package com.eaw1805.www.client.asyncs.army;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.List;
import java.util.Map;

public class ArmyNewAsyncCallback
        implements AsyncCallback<Map<Integer, List<BrigadeDTO>>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("New armies").center();
    }

    public void onSuccess(final Map<Integer, List<BrigadeDTO>> result) {
        ArmyStore.getInstance().initNewBrigades(result);
    }

}
