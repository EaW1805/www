package com.eaw1805.www.client.asyncs.economy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;

import java.util.List;

public class TradeCitiesAsyncCallback
        implements AsyncCallback<List<TradeCityDTO>> {


    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Trade cities").center();
    }

    public void onSuccess(final List<TradeCityDTO> tradeCities) {
        TradeCityStore.getInstance().initTradeCities(tradeCities);
    }

}
