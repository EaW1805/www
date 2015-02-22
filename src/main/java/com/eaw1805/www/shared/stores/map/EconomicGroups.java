package empire.webapp.shared.stores.map;

import empire.data.constants.RegionConstants;
import empire.data.dto.common.SectorDTO;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.events.loading.ProSiteLoadedEvent;
import empire.webapp.client.events.loading.ProSiteLoadedHandler;
import empire.webapp.client.events.loading.RegionLoadedEvent;
import empire.webapp.client.events.loading.RegionLoadedHandler;
import empire.webapp.client.events.loading.SectorsLoadedEvent;
import empire.webapp.client.events.loading.SectorsLoadedHandler;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.RegionStore;
import empire.webapp.shared.stores.economy.ProductionSiteStore;
import empire.webapp.shared.stores.map.economic.IncDecPopGroup;
import empire.webapp.shared.stores.map.economic.PopSizeGroup;
import empire.webapp.shared.stores.map.economic.PrSitesGroup;
import org.vaadin.gwtgraphics.client.Group;

import java.util.HashMap;
import java.util.Map;

public class EconomicGroups {
    private final Map<Integer, Group> regionEconomicImages = new HashMap<Integer, Group>(4);
    private final Map<Integer, PrSitesGroup> regionProdSitesImages = new HashMap<Integer, PrSitesGroup>(4);
    private final Map<Integer, IncDecPopGroup> regionIncDecPopImages = new HashMap<Integer, IncDecPopGroup>(4);
    private final Map<Integer, PopSizeGroup> regionPopSizeImages = new HashMap<Integer, PopSizeGroup>(4);

    private boolean sectorsInit = false, regionInit = false;

    EconomicGroups() {

        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                for (int regionId = RegionConstants.REGION_FIRST; regionId <= RegionConstants.REGION_LAST; regionId++) {
                    final SectorDTO[][] regionDTO = RegionStore.getInstance().getRegionSectorsByRegionId(regionId);
                    if (regionDTO.length > 2) {
                        final PrSitesGroup psGroup = new PrSitesGroup(ProductionSiteStore.getInstance().getSectorProdSites(), regionId);
                        regionProdSitesImages.put(regionId, psGroup);
                        getByRegion(regionId).add(regionProdSitesImages.get(regionId));
                        getByRegion(regionId).bringToFront(regionProdSitesImages.get(regionId));
                    }
                }
            }
        });

        LoadEventManager.addSectorsLoadedHandler(new SectorsLoadedHandler() {
            public void onSectorsLoaded(final SectorsLoadedEvent event) {
                sectorsInit = true;
                if (regionInit) {
                    for (int i = RegionConstants.REGION_FIRST; i <= RegionConstants.REGION_LAST; i++) {
                        final IncDecPopGroup idpGroup = new IncDecPopGroup(RegionStore.getInstance().getSectorOrderMap(), i);
                        regionIncDecPopImages.put(i, idpGroup);
                        getByRegion(i).add(regionIncDecPopImages.get(i));
                    }
                }
            }
        });

        LoadEventManager.addRegionLoadedHandler(new RegionLoadedHandler() {
            public void onRegionLoaded(final RegionLoadedEvent event) {
                regionInit = true;
                for (int i = RegionConstants.REGION_FIRST; i <= RegionConstants.REGION_LAST; i++) {
                    final SectorDTO[][] sectors = RegionStore.getInstance().getRegionSectorsByRegionId(i);
                    if (sectors != null) {
                        final PopSizeGroup psGroup = new PopSizeGroup(sectors, i);
                        regionPopSizeImages.put(i, psGroup);
                        getByRegion(i).add(regionPopSizeImages.get(i));

                        if (sectorsInit) {
                            final IncDecPopGroup idpGroup = new IncDecPopGroup(RegionStore.getInstance().getSectorOrderMap(), i);
                            regionIncDecPopImages.put(i, idpGroup);
                            getByRegion(i).add(regionIncDecPopImages.get(i));
                        }
                    }
                }
            }
        });

    }

    public Group getByRegion(final int regionId) {
        if (!regionEconomicImages.containsKey(regionId)) {
            regionEconomicImages.put(regionId, new Group());
        }
        return regionEconomicImages.get(regionId);
    }

    /**
     * @return the regionEconomicImages
     */
    public Group getRegionEconomicImages(final int regionId) {
        if (GameStore.getInstance().isShowPopulation()) {
            regionPopSizeImages.get(regionId).setVisible(true);

        } else {
            regionPopSizeImages.get(regionId).setVisible(false);
        }
        return regionEconomicImages.get(regionId);
    }

    public PopSizeGroup getRegionPopSizes(final int regionId) {
        return regionPopSizeImages.get(regionId);
    }

}
