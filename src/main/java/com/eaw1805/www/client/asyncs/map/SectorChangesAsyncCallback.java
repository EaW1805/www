package com.eaw1805.www.client.asyncs.map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.RegionStore;

import java.util.Map;

public class SectorChangesAsyncCallback
        implements AsyncCallback<Map<Integer, OrderPositionDTO>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Sectors").center();
    }

    public void onSuccess(final Map<Integer, OrderPositionDTO> sectorOrderMap) {
        RegionStore.getInstance().initDbSectorOrders(sectorOrderMap);
    }

}
