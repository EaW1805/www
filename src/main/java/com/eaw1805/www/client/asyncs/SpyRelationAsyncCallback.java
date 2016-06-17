package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.client.views.SpyRelationsView;

import java.util.List;

public class SpyRelationAsyncCallback
        implements AsyncCallback<List<RelationDTO>> {

    final SpyRelationsView relView;

    public SpyRelationAsyncCallback(SpyRelationsView relView) {
        this.relView = relView;
    }

    public void onFailure(final Throwable caught) {
        // Do nothing
    }

    public void onSuccess(final List<RelationDTO> relations) {
        try {
            // Provide the manager with the required data
            relView.initRelations(relations);
        } catch (Exception ex) {
            // eat it
        }
    }

}
