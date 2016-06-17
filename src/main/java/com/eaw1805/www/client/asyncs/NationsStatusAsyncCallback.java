package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.DataStore;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 7/07/12
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NationsStatusAsyncCallback implements AsyncCallback<Map<Integer, Boolean>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Nation status").center();
    }

    public void onSuccess(final Map<Integer, Boolean> nationsToDead) {
        // type 1 refers to the actual authentication whereas 2 refers to is authenticated
        DataStore.getInstance().initNationsToDead(nationsToDead);
    }

}
