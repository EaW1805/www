package com.eaw1805.www.client.asyncs.economy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.www.client.EmpireWebClient;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.map.MapStore;

import java.util.List;

public class WarehouseAsyncCallback
        implements AsyncCallback<List<WarehouseDTO>> {

    private final EmpireWebClient webClient;

    public WarehouseAsyncCallback(final EmpireWebClient webClient) {
        this.webClient = webClient;
    }

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Warehouses").center();
    }

    public void onSuccess(final List<WarehouseDTO> warehouses) {
        if (warehouses != null) {
            WarehouseStore.getInstance().setClient(true);
            WarehouseStore.getInstance().setEconomyWidget(webClient.getLayoutView().getEconomyView());
            WarehouseStore.getInstance().initDbWareHouses(warehouses);

            webClient.getLayoutView().getEconomyView().populateGoodsLabels(
                    WarehouseStore.getInstance().getWareHouseByRegion(MapStore.getInstance().getActiveRegion()), false);
        }
    }

}
