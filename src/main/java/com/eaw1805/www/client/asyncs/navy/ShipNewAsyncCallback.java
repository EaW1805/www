package com.eaw1805.www.client.asyncs.navy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;
import java.util.Map;

public class ShipNewAsyncCallback
        implements AsyncCallback<Map<Integer, List<ShipDTO>>> {

    public void onFailure(Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("New ships").center();
    }

    public void onSuccess(Map<Integer, List<ShipDTO>> result) {
        NavyStore.getInstance().initNewShips(result);

    }

}
