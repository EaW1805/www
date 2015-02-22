package empire.webapp.shared.stores.economy;

import empire.data.constants.GoodConstants;
import empire.data.constants.NaturalResourcesConstants;
import empire.data.constants.OrderConstants;
import empire.data.constants.ProductionSiteConstants;
import empire.data.constants.RelationConstants;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.common.ProductionSiteDTO;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.army.ArmyDTO;
import empire.webapp.client.events.economy.EcoEventManager;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.stores.DataStore;
import empire.webapp.shared.stores.map.MapStore;
import empire.webapp.shared.stores.support.Taxation;
import empire.webapp.shared.stores.units.ForeignUnitsStore;
import empire.webapp.shared.stores.util.calculators.CostCalculators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ProductionSiteStore
        implements GoodConstants, ProductionSiteConstants, NaturalResourcesConstants, OrderConstants, RelationConstants {

    /**
     * A hash map containing all the Sectors and their corresponding production sites.
     */
    private transient final Map<SectorDTO, Integer> sectorProdSites = new HashMap<SectorDTO, Integer>();

    /**
     * Our instance of the Manager.
     */
    private transient static ProductionSiteStore ourInstance = null;

    /**
     * The Map data used by the client.
     */
    private transient final MapStore mapStore = MapStore.getInstance();

    /**
     * The taxation level.
     */
    private Taxation tax = new Taxation();

    // Method returning the economy manager if already initialized
    // or the a new instance
    public static ProductionSiteStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new ProductionSiteStore();
        }
        return ourInstance;
    }

    /**
     * Method that builds a new production site.
     *
     * @param sector     tile where you want to place your site.
     * @param prodSiteId the id of the type of site.
     */
    public void buildProdSite(final SectorDTO sector, final Integer prodSiteId) {
        //check for conflicts
        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty() && !sector.getTradeCity()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot build production site.", false);
            return;
        }
        //done checking for conflicts

        final int[] ids = new int[2];
        ids[0] = sector.getId();
        ids[1] = prodSiteId;
        final ProductionSiteDTO prodSite = DataStore.getInstance().getProdSite(prodSiteId);
        if (OrderStore.getInstance().addNewOrder(ORDER_B_PRODS, CostCalculators.getProductionSiteCost(sector.getRegionId(), prodSite, false), sector.getRegionId(), prodSite.getName(), ids, 0, "") == 1) {
            getSectorProdSites().put(sector, prodSiteId);
            if (mapStore != null) {
                EcoEventManager.changePrSite(sector.getRegionId());
            }
        }
    }

    /**
     * Method that demolishes production site
     *
     * @param sector   tile where the existing site is
     * @param isClient set true if you are calling from the client side
     * @return true if site is demolished.
     */
    public boolean demolishProdSite(final SectorDTO sector, final boolean isClient) {

        if (isClient) {
            final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
            if (foreignArmies != null && !foreignArmies.isEmpty()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot demolish production site.", false);
                return false;
            }

            int[] ids = new int[2];
            ids[0] = sector.getId();
            ids[1] = 0;
            if (OrderStore.getInstance().addNewOrder(ORDER_D_PRODS, CostCalculators.getProductionSiteCost(sector.getRegionId(), null, true), sector.getRegionId(), "", ids, 0, "") == 1) {
                getSectorProdSites().put(sector, -1);
                if (mapStore != null) {
                    EcoEventManager.changePrSite(sector.getRegionId());
                }
                return true;
            } else {
                return false;
            }
        } else {
            getSectorProdSites().put(sector, -1);
            return true;
        }
    }

    /**
     * Cancels the build production site order in the client
     *
     * @param sector   where the production site is built
     * @param isClient boolean that shows if the order is run on the client side
     */
    public void cancelOrder(final SectorDTO sector, final boolean isClient) {
        // Check if it is a build or demolish order that we cancel
        boolean removed = false;
        int[] ids = new int[2];
        if (getSectorProdSites().get(sector) != -1) {
            ids[0] = sector.getId();
            ids[1] = getSectorProdSites().get(sector);
            removed = OrderStore.getInstance().removeOrder(ORDER_B_PRODS, ids);
        } else if (getSectorProdSites().get(sector) == -1) {
            ids[0] = sector.getId();
            ids[1] = 0;
            removed = OrderStore.getInstance().removeOrder(ORDER_D_PRODS, ids);
        }
        if (removed) {
            getSectorProdSites().remove(sector);
        }
        if (isClient && mapStore != null) {
            EcoEventManager.changePrSite(sector.getRegionId());
        }
    }

    public boolean isTileNeighborWithSea(final PositionDTO thisPos) {
        return isTileNeighborWithSeaById(thisPos.getX(), thisPos.getY(), thisPos.getRegionId());
    }

    // Get if the tile is neighboring an ocean tile
    public boolean isTileNeighborWithSeaById(final int xPos, final int yPos, final int regionId) {
        final SectorDTO[][] sectors = mapStore.getRegionSectorsByRegionId(regionId);
        int sizeX = sectors.length;
        int sizeY = sectors[0].length;
        if ((xPos != 0 && yPos + 1 != sizeY) && sectors[xPos - 1][yPos + 1].getTerrain().getId() == 12) {
            return true;

        } else if (yPos + 1 != sizeY && sectors[xPos][yPos + 1].getTerrain().getId() == 12) {
            return true;

        } else if (yPos != 0 && sectors[xPos][yPos - 1].getTerrain().getId() == 12) {
            return true;

        } else if (xPos + 1 != sizeX && sectors[xPos + 1][yPos].getTerrain().getId() == 12) {
            return true;

        } else if (xPos != 0 && sectors[xPos - 1][yPos].getTerrain().getId() == 12) {
            return true;

        } else if ((xPos + 1 != sizeX && yPos != 0) && sectors[xPos + 1][yPos - 1].getTerrain().getId() == 12) {
            return true;

        } else if ((xPos + 1 != sizeX && yPos + 1 != sizeY) && sectors[xPos + 1][yPos + 1].getTerrain().getId() == 12) {
            return true;

        } else if ((xPos != 0 && yPos != 0) && sectors[xPos - 1][yPos - 1].getTerrain().getId() == 12) {
            return true;
        }
        return false;

    }

    // Get if the tile is neighboring an ocean tile
    public SectorDTO getSectorByRegionXY(final int xPos, final int yPos, final int regionId) {
        final SectorDTO[][] sectors = mapStore.getRegionSectorsByRegionId(regionId);
        return sectors[xPos][yPos];
    }

    public Map<SectorDTO, Integer> getSectorProdSites() {
        return sectorProdSites;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(final Taxation tax) {
        this.tax = tax;
    }

    /**
     * @return the tax
     */
    public Taxation getTax() {
        return tax;
    }


}
