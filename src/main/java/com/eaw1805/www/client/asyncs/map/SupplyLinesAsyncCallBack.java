package com.eaw1805.www.client.asyncs.map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.www.shared.stores.map.MinimapGroups;

import java.util.Set;

/**
 * Invoked when supply lines calculation is completed.
 */
public class SupplyLinesAsyncCallBack
        implements AsyncCallback<Set<PositionDTO>>, RegionConstants {

    private transient final MinimapGroups miniMapsGroups;

    private transient int regionId;

    public SupplyLinesAsyncCallBack(final MinimapGroups miniMaps, final int theRegion) {
        miniMapsGroups = miniMaps;
        regionId = theRegion;
    }

    public void onFailure(final Throwable caught) {
        // do nothing
    }

    public void onSuccess(final Set<PositionDTO> regionData) {
        miniMapsGroups.drawSupplies(regionData, regionId);
    }

}
