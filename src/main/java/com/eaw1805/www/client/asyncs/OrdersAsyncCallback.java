package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.economy.OrderStore;

import java.util.List;
import java.util.Map;

public class OrdersAsyncCallback
        implements AsyncCallback<Map<Integer, List<ClientOrderDTO>>> {

    private final OrderStore orManager = OrderStore.getInstance();

    public void onFailure(final Throwable caught) {
        ForceReloadPanel.getInstance().addFail("Orders").center();

    }

    public void onSuccess(final Map<Integer, List<ClientOrderDTO>> orders) {
        // Initialize client side order data structures
        orManager.initDbOrders(orders);
        LoadEventManager.loadOrders();
    }

}
