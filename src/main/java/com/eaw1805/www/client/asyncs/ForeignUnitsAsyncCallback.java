package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.ForeignUnits;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;


public class ForeignUnitsAsyncCallback
        implements AsyncCallback<ForeignUnits> {

    public void onFailure(final Throwable caught) {
        //do nothing here
        ForceReloadPanel.getInstance().addFail("Foreign units").center();
    }

    public void onSuccess(final ForeignUnits foreignUnits) {
        ForeignUnitsStore.getInstance().initForeignUnits(foreignUnits);
    }
}
