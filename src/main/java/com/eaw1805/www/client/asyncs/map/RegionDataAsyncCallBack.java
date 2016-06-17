package com.eaw1805.www.client.asyncs.map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.views.layout.MapsView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.TilesCollection;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.map.MapStore;

public class RegionDataAsyncCallBack
        implements AsyncCallback<TilesCollection>, RegionConstants {

    private transient final MapsView mapsView;

    public RegionDataAsyncCallBack(final MapsView _mapsView) {
        this.mapsView = _mapsView;
    }

    public void onFailure(final Throwable caught) {
        // do nothing
    }

    public void onSuccess(final TilesCollection regionData) {

        final MapStore mapStore = MapStore.getInstance();
        mapStore.setMapsView(this.mapsView);
        RegionStore.getInstance().initDbBattles(regionData.getBattleToPositions());
        for (int regionId = REGION_FIRST; regionId <= REGION_LAST; regionId++) {
            RegionStore.getInstance().initDbSectors(regionData.getRegionSectorsArray().get(regionId), regionId);
        }

        // the production sites
        ProductionSiteStore.getInstance().getSectorProdSites().clear();
        ProductionSiteStore.getInstance().getSectorProdSites().putAll(regionData.getRegionBuildDemPrSitesMap());
        LoadEventManager.loadPrSites();

        // The Regions
        mapStore.setUpRegion(EUROPE);

        // Setup Tutorial Scenario points of navigation
        if (HibernateUtil.DB_FREE == GameStore.getInstance().getScenarioId()) {
            TutorialStore.getInstance().getGroup().initTutorialGroup(regionData.getRegionSectorsArray().get(EUROPE));
        }

        MapStore.getInstance().initSpeedUpGroups(regionData.getRegionSectorsArray());
        LoadEventManager.loadRegion();
    }

    public MapsView getMapsView() {
        return mapsView;
    }

}
