package com.eaw1805.www.client.asyncs.economy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;

import java.util.List;

public class BaggageAsyncCallback
        implements AsyncCallback<List<BaggageTrainDTO>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Baggage  trains").center();
    }

    public void onSuccess(final List<BaggageTrainDTO> result) {
        BaggageTrainStore.getInstance().setClient(true);
        BaggageTrainStore.getInstance().initDbBaggageTrains(result);
        BaggageTrainStore.getInstance().setInitialized(true);
    }

}
