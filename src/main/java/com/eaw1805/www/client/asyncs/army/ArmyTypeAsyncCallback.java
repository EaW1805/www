package com.eaw1805.www.client.asyncs.army;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.List;

public class ArmyTypeAsyncCallback
        implements AsyncCallback<List<ArmyTypeDTO>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Army types").center();
    }

    public void onSuccess(final List<ArmyTypeDTO> armyTypeLst) {
        ArmyStore.getInstance().initDbArmyTypes(armyTypeLst);
    }

}
