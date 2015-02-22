package empire.webapp.shared.stores.map.economic;

import empire.data.constants.ProductionSiteConstants;
import empire.data.dto.common.SectorDTO;
import empire.webapp.client.events.economy.EcoEventManager;
import empire.webapp.client.events.economy.PrSiteChangeEvent;
import empire.webapp.client.events.economy.PrSiteChangeHandler;
import empire.webapp.client.widgets.MapImage;
import empire.webapp.shared.stores.economy.ProductionSiteStore;
import empire.webapp.shared.stores.map.MapStore;
import empire.webapp.shared.stores.support.MapConstants;
import org.vaadin.gwtgraphics.client.Group;

import java.util.Map;

public class PrSitesGroup
        extends Group
        implements MapConstants, ProductionSiteConstants {

    int regionId;

    public PrSitesGroup(final Map<SectorDTO, Integer> sectorProdSites, final int regionId) {
        super();
        this.regionId = regionId;

        EcoEventManager.addPrSiteChangeHandler(new PrSiteChangeHandler() {
            public void onPrSiteChange(final PrSiteChangeEvent event) {
                if (event.getRegion() == regionId) {
                    addBuildDemolishToProdSiteImages(ProductionSiteStore.getInstance().getSectorProdSites());
                }
            }
        });

        addBuildDemolishToProdSiteImages(sectorProdSites);
    }

    public void addBuildDemolishToProdSiteImages(final Map<SectorDTO, Integer> sectorProdSites) {
        final int objCount = this.getVectorObjectCount();
        for (int index = objCount - 1; index > -1; index--) {
            final MapImage currImg = (MapImage) this.getVectorObject(index);
            final String href = currImg.getHref();
            if (href.contains("const")) {
                this.remove(currImg);
            }
        }

        final MapStore mapStore = MapStore.getInstance();

        for (SectorDTO sector : sectorProdSites.keySet()) {
            if (sector != null && sector.getRegionId() == regionId) {
                final int xPos = sector.getX();
                final int yPos = sector.getY();
                final int proSiteId = sectorProdSites.get(sector);
                String url = "http://static.eaw1805.com/tiles/sites/tconst-" + String.valueOf(proSiteId) + ".png";
                if (proSiteId == -1) {
                    url = "http://static.eaw1805.com/tiles/sites/tprodconstruction.png";

                } else if (proSiteId == PS_BARRACKS_FH || proSiteId == PS_BARRACKS_FL
                        || proSiteId == PS_BARRACKS_FM || proSiteId == PS_BARRACKS_FS) {
                    url = "http://static.eaw1805.com/tiles/sites/tconst-" + PS_BARRACKS + ".png";
                }

                final MapImage psUCImage = new MapImage(mapStore.getPointX(xPos),
                        mapStore.getPointY(yPos),
                        (int) mapStore.getTileSize(),
                        (int) mapStore.getTileSize(),
                        url);
                psUCImage.setxPos(xPos);
                psUCImage.setyPos(yPos);
                this.add(psUCImage);
            }
        }
    }
}
