package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.MovementStore;

import java.util.Map;

public class MovemenentOrdersAsyncCallback
        implements AsyncCallback<Map<Integer, Map<Integer, MovementDTO>>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Movement orders").center();
    }

    public void onSuccess(final Map<Integer, Map<Integer, MovementDTO>> movementMap) {
        MovementStore.getInstance().initDbMovements(movementMap);
    }

}
