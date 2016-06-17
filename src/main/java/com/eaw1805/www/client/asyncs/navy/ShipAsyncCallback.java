package com.eaw1805.www.client.asyncs.navy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;
import java.util.Map;

public class ShipAsyncCallback
        implements AsyncCallback<Map<Boolean, List<FleetDTO>>>, RegionConstants {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Ships").center();
    }

    public void onSuccess(final Map<Boolean, List<FleetDTO>> mapFleet) {
        NavyStore.getInstance().setClient(true);
        NavyStore.getInstance().initDbFleets(mapFleet.get(true), mapFleet.get(false));
    }

}
