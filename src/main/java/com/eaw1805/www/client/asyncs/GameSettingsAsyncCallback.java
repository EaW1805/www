package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.GameSettingsDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.SoundStore;


public class GameSettingsAsyncCallback
        implements AsyncCallback<GameSettingsDTO> {

    public void onFailure(final Throwable caught) {
        // do nothing
        ForceReloadPanel.getInstance().addFail("Game settings").center();
    }

    public void onSuccess(final GameSettingsDTO result) {
        GameStore.getInstance().setSettings(result);
        SoundStore.getInstance().initIntro();
    }
}
