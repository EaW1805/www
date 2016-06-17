package com.eaw1805.www.client.asyncs.economy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.collections.DataCollection;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.DataStore;

public class DataCollectionAsyncCallback
        implements AsyncCallback<DataCollection> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Data collection").center();
    }

    public void onSuccess(final DataCollection globalData) {
        DataStore.getInstance().initDataCollection(globalData);
        LoadEventManager.loadNations(globalData.getNations());
    }
}
