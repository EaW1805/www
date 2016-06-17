package com.eaw1805.www.client.asyncs.economy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;

import java.util.List;
import java.util.Map;

public class NewBaggageAsyncCallback
        implements AsyncCallback<Map<Integer, List<BaggageTrainDTO>>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("New baggage trains").center();
    }

    public void onSuccess(final Map<Integer, List<BaggageTrainDTO>> result) {
        BaggageTrainStore.getInstance().getNewBaggageTMap().clear();
        BaggageTrainStore.getInstance().getNewBaggageTMap().putAll(result);
    }

}
