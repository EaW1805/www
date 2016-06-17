package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.views.layout.SaveProgressPanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;

public class SaveChangesAsyncCallback
        implements AsyncCallback<Integer> {

    final boolean updateSaveCounter;
    final int state;
    public SaveChangesAsyncCallback(final boolean updateSaveCounter, final int state) {
        this.updateSaveCounter = updateSaveCounter;
        this.state = state;
    }

    public void onFailure(final Throwable caught) {
        if (updateSaveCounter) {
            GameStore.getInstance().getLayoutView().getUnitsMenu().increaseSaveCounter();

        }
        SaveProgressPanel.getInstance().setNoOk(state);
    }

    public void onSuccess(final Integer result) {
        if (updateSaveCounter) {
            GameStore.getInstance().getLayoutView().getUnitsMenu().increaseSaveCounter();
        }
        SaveProgressPanel.getInstance().setOk(state, result);
    }

}
