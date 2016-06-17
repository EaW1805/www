package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;

public class GameProcessAsyncCallback
        implements AsyncCallback<String> {

    public void onFailure(final Throwable caught) {
        new ErrorPopup(ErrorPopup.Level.ERROR, "The time cannot be estimated", false);
        GameEventManager.reportProcessTime("unknown");
    }

    public void onSuccess(final String result) {
        GameEventManager.reportProcessTime(result);
    }

}
