package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.AlliedUnits;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;

import java.util.Map;

public class AlliedUnitsAsyncCallback
        implements AsyncCallback<Map<Integer, Map<Integer, AlliedUnits>>> {

    public void onFailure(final Throwable caught) {
        // do nothing
        ForceReloadPanel.getInstance().addFail("Allied units").center();
    }

    public void onSuccess(final Map<Integer, Map<Integer, AlliedUnits>> result) {
        AlliedUnitsStore.getInstance().initAlliedUnits(result);
    }

}
