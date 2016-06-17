package com.eaw1805.www.fieldbattle.asyncs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 2/17/13
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetCommandersAsyncCallback implements AsyncCallback<Map<Integer, List<CommanderDTO>>> {
    public GetCommandersAsyncCallback() {

    }

    public void onFailure(final Throwable caught) {
        // do nothing
        Window.alert("commanders failed");
    }

    public void onSuccess(final Map<Integer, List<CommanderDTO>> result) {
        CommanderStore.getInstance().initCommanders(result);
        MainPanel.getInstance().getLoading().addLoadingStep("Commanders loaded");
    }

}
