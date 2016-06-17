package com.eaw1805.www.scenario.asyncs;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.www.scenario.stores.EditorStore;

import java.util.List;
import java.util.Map;

public class GetRegionDataCallback
        implements AsyncCallback<Map<RegionDTO, List<SectorDTO>>> {

    public void onFailure(final Throwable caught) {
        // do nothing
        Window.alert("Hi failed");
    }

    public void onSuccess(final Map<RegionDTO, List<SectorDTO>> val) {
        EditorStore.getInstance().setRegionsMap(val);
    }

}

