package com.eaw1805.www.client.asyncs.army;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.ArmiesAndCommanders;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;

public class ArmyAsyncCallback
        implements AsyncCallback<ArmiesAndCommanders>, RegionConstants {

    public void onFailure(final Throwable caught) {
        ForceReloadPanel.getInstance().addFail("Armies").center();
    }

    public void onSuccess(final ArmiesAndCommanders lstArmies) {
        if (lstArmies.getArmies() != null) {
            ArmyStore.getInstance().initDbArmies(lstArmies.getArmies(), lstArmies.getDeletedArmies(), lstArmies.getDeletedCorps());
            ArmyStore.getInstance().initMergedBattalions(lstArmies.getMergedBatts());
        }
        CommanderStore.getInstance().setClient(true);
        CommanderStore.getInstance().initCommanders(lstArmies.getCommanders(), lstArmies.getCapturedCommanders());
    }
}
