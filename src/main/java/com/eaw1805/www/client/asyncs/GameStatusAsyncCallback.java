package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;

public class GameStatusAsyncCallback
        implements AsyncCallback<Boolean> {

    public void onFailure(final Throwable caught) {
        new ErrorPopup(ErrorPopup.Level.ERROR, "Connection to server lost...", false);
        GameEventManager.reportInProcess(true);
    }

    public void onSuccess(final Boolean result) {
        GameEventManager.reportInProcess(result);
    }

}
