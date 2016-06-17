package com.eaw1805.www.client.asyncs.navy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class ShipTypeAsyncCallback
        implements AsyncCallback<List<ShipTypeDTO>> {

    public void onFailure(final Throwable caught) {
        // do nothing
        ForceReloadPanel.getInstance().addFail("Ship types").center();
    }

    public void onSuccess(final List<ShipTypeDTO> shipTypes) {
        NavyStore.getInstance().setClient(true);
        NavyStore.getInstance().initDbShipTypes(shipTypes);
    }

}
