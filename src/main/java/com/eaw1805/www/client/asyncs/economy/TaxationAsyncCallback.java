package com.eaw1805.www.client.asyncs.economy;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.support.Taxation;

public class TaxationAsyncCallback
        implements AsyncCallback<Taxation> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Taxation").center();
    }

    public void onSuccess(final Taxation result) {

        if (result != null) {

            ProductionSiteStore.getInstance().setTax(result);
            GameStore.getInstance().setVps(result.getVps());
            GameStore.getInstance().setVpsMax(result.getVpsMax());
            GameStore.getInstance().setTotalPopulation(result.getPopulation());
            GameStore.getInstance().setDeficit(result.getDeficit());
            GameStore.getInstance().setSurplus(result.getSurplus());
            GameStore.getInstance().getLayoutView().getOptionsMenu().getTaxView().initTaxation();
        }

    }

}
