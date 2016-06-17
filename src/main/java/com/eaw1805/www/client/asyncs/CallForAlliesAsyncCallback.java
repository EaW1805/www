package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.List;

public class CallForAlliesAsyncCallback implements AsyncCallback<List<String>> {

    public void onFailure(final Throwable caught) {
        // do nothing
        ForceReloadPanel.getInstance().addFail("Call for allies").center();
    }

    public void onSuccess(final List<String> result) {
        GameStore.getInstance().initCallForAllies(result);
    }
}
