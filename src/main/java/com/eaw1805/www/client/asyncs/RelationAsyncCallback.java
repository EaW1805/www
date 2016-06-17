package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.client.views.layout.ForceReloadPanel;
import com.eaw1805.www.shared.stores.RelationsStore;

import java.util.List;

public class RelationAsyncCallback
        implements AsyncCallback<List<RelationDTO>> {

    public void onFailure(final Throwable caught) {
        // Do nothing
        ForceReloadPanel.getInstance().addFail("Relations").center();
    }

    public void onSuccess(final List<RelationDTO> relations) {
        try {
            // Provide the manager with the required data
            RelationsStore.getInstance().setIsClient(true);
            RelationsStore.getInstance().initDbRelations(relations);

        } catch (Exception ex) {
            // eat it
        }
    }

}
