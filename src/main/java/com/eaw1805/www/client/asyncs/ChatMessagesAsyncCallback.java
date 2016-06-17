package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.shared.stores.DataStore;

import java.util.List;

public class ChatMessagesAsyncCallback
        implements AsyncCallback<List<String>> {

    public void onFailure(Throwable caught) {
        // do nothing
    }

    public void onSuccess(List<String> result) {
        DataStore.getInstance().initChatMessages(result);
    }
}
