package com.eaw1805.www.client.asyncs.army;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.economy.BarrackStore;

import java.util.List;

public class BarracksAsyncCallback
        implements AsyncCallback<List<BarrackDTO>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Barracks").center();
    }

    public void onSuccess(final List<BarrackDTO> barrShipList) {
        BarrackStore.getInstance().initBarrShip(barrShipList);
    }
}
