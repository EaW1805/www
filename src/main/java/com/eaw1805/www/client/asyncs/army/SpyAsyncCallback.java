package com.eaw1805.www.client.asyncs.army;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.List;

public class SpyAsyncCallback
        implements AsyncCallback<List<SpyDTO>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Spies").center();
    }

    public void onSuccess(final List<SpyDTO> result) {
        SpyStore.getInstance().setClient(true);
        SpyStore.getInstance().initDbSpy(result);

    }
}
