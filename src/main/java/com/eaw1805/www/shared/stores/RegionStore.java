package com.eaw1805.www.shared.stores;


import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.BattleDTO;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.views.popups.OrdersViewerPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class is final. We only need one region manager for our application and only once the data.
 */
public final class RegionStore
        implements OrderConstants, RegionConstants {

    private static final String CANCEL_PRODUCT_WARNING = "Cancel product site building before issuing command";
    /**
     * A hash map of the sector increase pop , decrease pop and hand over
     * First Integer is the sector id and second is the Order Id
     */
    private transient Map<Integer, OrderPositionDTO> sectorOrderMap = new HashMap<Integer, OrderPositionDTO>();

    /**
     * A hash map of our sectors by Id inside a map of regionIds
     */
    private final transient Map<Integer, Map<Integer, SectorDTO>> regionSectorsMap = new HashMap<Integer, Map<Integer, SectorDTO>>();

    /**
     * A hash map of our sectors by x and y inside a map of regionIds
     */
    private final transient Map<Integer, SectorDTO[][]> regionSectorsArrayMap = new HashMap<Integer, SectorDTO[][]>();

    /**
     * Maps sector ids to region ids.
     */
    private final transient Map<Integer, Integer> sectorIdToRegionId = new HashMap<Integer, Integer>();

    /**
     * Our instance of the RegionManager
     */
    private static transient RegionStore ourInstance = null;

    /**
     * A hash map of four sector ids of the selected sector id
     */
    private final transient Map<Integer, Integer> regionSelSectorMap = new HashMap<Integer, Integer>();

    /**
     * Variable telling us if our data are initialized.
     */
    private boolean isInitialized = false;

    private Map<PositionDTO, BattleDTO> mapTacticalPos = new HashMap<PositionDTO, BattleDTO>();
    private Map<PositionDTO, BattleDTO> mapNavalPos = new HashMap<PositionDTO, BattleDTO>();

    // constructor
    private RegionStore() {
        // empty
    }

    // Method returning the army manager
    public static RegionStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new RegionStore();
        }
        return ourInstance;
    }

    // Method Used By the client to initialize the sectors  the Map
    public void initDbSectors(final SectorDTO[][] sectorDTOs, final int regionId) {
        try {
            // clear any previous additions to collections
            if (regionSectorsMap.get(regionId) != null) {
                regionSectorsMap.get(regionId).clear();
            }

            if (regionSectorsArrayMap.get(regionId) != null) {
                regionSectorsArrayMap.clear();
            }

            regionSectorsArrayMap.put(regionId, sectorDTOs);
            for (SectorDTO[] sectorsCols : sectorDTOs) {
                for (SectorDTO sectorDTO : sectorsCols) {
                    if (sectorDTO != null) {
                        sectorIdToRegionId.put(sectorDTO.getId(), regionId);
                    }
                }
            }
            regionSectorsMap.put(regionId, new HashMap<Integer, SectorDTO>());
            for (final SectorDTO[] sectorDTO : sectorDTOs) {
                for (final SectorDTO aSectorDTO : sectorDTO) {
                    if (aSectorDTO != null) {
                        regionSectorsMap.get(regionId).put(aSectorDTO.getId(), aSectorDTO);
                    }
                }
            }

            if (regionSectorsMap.containsKey(EUROPE)
                    && regionSectorsMap.containsKey(CARIBBEAN)
                    && regionSectorsMap.containsKey(INDIES)
                    && regionSectorsMap.containsKey(AFRICA)) {
                isInitialized = true;
            }
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize sectors due to unexcpected reason", false);
        }
    }

    // Method Used By the async callback to initialize  the sector commands
    public void initDbSectorOrders(final Map<Integer, OrderPositionDTO> sectorOrderMap) {
        try {
            this.sectorOrderMap.clear();
            this.setSectorOrderMap(sectorOrderMap);
            LoadEventManager.loadSectors();

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize sector orders due to unexcpected reason", false);
        }
    }

    public void initDbBattles(final Map<Boolean, Map<Integer, BattleDTO>> typeToPositions) {
        for (final BattleDTO battleDTO : typeToPositions.get(true).values()) {
            mapTacticalPos.put(battleDTO, battleDTO);
        }

        for (final BattleDTO battleDTO : typeToPositions.get(false).values()) {
            mapNavalPos.put(battleDTO, battleDTO);
        }
    }

    /**
     * Method that increases the population of a given sector identification
     *
     * @param sector the target sector we want to increase
     */
    public void increasePopulation(final SectorDTO sector) {
        //check for conflicts
        final List<ClientOrderDTO> buildPrSitesOrders = OrderStore.getInstance().getClientOrders().get(ORDER_B_PRODS);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (buildPrSitesOrders != null) {
            for (ClientOrderDTO order : buildPrSitesOrders) {
                if (order.getIdentifier(0) == sector.getId()) {
                    conflictOrders.add(order);
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING,
                    "Cannot increase/decrease population density after you built a production site in this tile. Review conflict orders?", true) {
                public void onAccept() {
                    super.onAccept();
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                    viewer.show();
                    viewer.center();
                }
            };

            return;
        }
        //done checking for conflicts

        boolean canIncrease = true;
        if (ProductionSiteStore.getInstance().getSectorProdSites().containsKey(sector)) {
            final int prodSiteId = ProductionSiteStore.getInstance().getSectorProdSites().get(sector);
            final ProductionSiteDTO productionSite = DataStore.getInstance().getProdSite(prodSiteId);
            if ((sector.getPopulation() + 1) > productionSite.getMaxPopDensity()) {
                canIncrease = false;
            }
        }

        if (sectorOrderMap.containsKey(sector.getId())) {
            if (sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_DEC_POP) {
                if (canIncrease) {
                    final int[] ids = new int[2];
                    ids[0] = sector.getId();
                    ids[1] = sector.getRegionId();
                    if (cancelOrder(sector)) {
                        if (OrderStore.getInstance().addNewOrder(ORDER_INC_POP, CostCalculators.getIncDecPopCost(sector, true), sector.getRegionId(), "", ids, 0, "") == 1) {
                            final OrderPositionDTO orPos = new OrderPositionDTO();
                            orPos.setOrderType(ORDER_INC_POP);
                            orPos.setPosition(-1);
                            orPos.setParameter1(String.valueOf(sector.getId()));
                            orPos.setParameter2(String.valueOf(sector.getPopulation()));
                            orPos.setParameter3(String.valueOf(sector.getRegionId()));
                            orPos.setParameter4(String.valueOf(sector.getNationId()));
                            sectorOrderMap.put(sector.getId(), orPos);
                        }
                    }
                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, CANCEL_PRODUCT_WARNING, false);
                }
            } else if (sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_HOVER_SEC) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "You have already added a handover command", false);
            }
        } else {
            if (canIncrease) {
                // If the funds are sufficient
                final int[] ids = new int[2];
                ids[0] = sector.getId();
                ids[1] = sector.getRegionId();
                if (OrderStore.getInstance().addNewOrder(ORDER_INC_POP, CostCalculators.getIncDecPopCost(sector, true), sector.getRegionId(), "", ids, 0, "") == 1) {
                    final OrderPositionDTO orPos = new OrderPositionDTO();
                    orPos.setOrderType(ORDER_INC_POP);
                    orPos.setPosition(-1);
                    orPos.setParameter1(String.valueOf(sector.getId()));
                    orPos.setParameter2(String.valueOf(sector.getPopulation()));
                    orPos.setParameter3(String.valueOf(sector.getRegionId()));
                    orPos.setParameter4(String.valueOf(sector.getNationId()));
                    sectorOrderMap.put(sector.getId(), orPos);
                }
            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, CANCEL_PRODUCT_WARNING, false);
            }

        }

        EcoEventManager.changeSector(sector.getRegionId());
    }

    /**
     * Method that decreases the population of a given sector identification
     *
     * @param sector the target sector we want to decrease
     */
    public void decreasePopulation(final SectorDTO sector) {
        //check for conflicts        
        if (sector.getConqueredCounter() > 0) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot decrease population density in this sector, when you conquer a sector you can decrease population after 5 rounds", false);
            return;
        }

        final List<ClientOrderDTO> buildPrSitesOrders = OrderStore.getInstance().getClientOrders().get(ORDER_B_PRODS);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (buildPrSitesOrders != null) {
            for (ClientOrderDTO order : buildPrSitesOrders) {
                if (order.getIdentifier(0) == sector.getId()) {
                    conflictOrders.add(order);
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot increase or decrease population density after you built a production site in the same tile", false);
                    return;
                }
            }
        }
        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot increase or decrease population density after you built a production site in the same tile. Review conflict orders?", true) {
                public void onAccept() {
                    super.onAccept();
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                    viewer.show();
                    viewer.center();
                }
            };
            return;
        }
        //done checking for conflicts

        boolean canDecrease = true;
        if (ProductionSiteStore.getInstance().getSectorProdSites().containsKey(sector)) {
            final int prodSiteId = ProductionSiteStore.getInstance().getSectorProdSites().get(sector);
            final ProductionSiteDTO productionSite = DataStore.getInstance().getProdSite(prodSiteId);
            if ((sector.getPopulation() - 1) < productionSite.getMinPopDensity()) {
                canDecrease = false;
            }
        }
        if (sectorOrderMap.containsKey(sector.getId())) {
            if (sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_INC_POP) {
                if (canDecrease) {
                    final int[] ids = new int[2];
                    ids[0] = sector.getId();
                    ids[1] = sector.getRegionId();
                    if (OrderStore.getInstance().addNewOrder(ORDER_DEC_POP, CostCalculators.getIncDecPopCost(sector, false), sector.getRegionId(), "", ids, 0, "") == 1) {
                        sectorOrderMap.get(sector.getId()).setOrderType(ORDER_DEC_POP);
                        OrderStore.getInstance().removeOrder(ORDER_INC_POP, ids);
                    }
                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, CANCEL_PRODUCT_WARNING, false);
                }
            } else if (sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_HOVER_SEC) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "You have already added a handover command", false);
            }
        } else {

            if (canDecrease) {
                final int[] ids = new int[2];
                ids[0] = sector.getId();
                ids[1] = sector.getRegionId();
                if (OrderStore.getInstance().addNewOrder(ORDER_DEC_POP, CostCalculators.getIncDecPopCost(sector, false), sector.getRegionId(), "", ids, 0, "") == 1) {
                    final OrderPositionDTO orPos = new OrderPositionDTO();
                    orPos.setOrderType(ORDER_DEC_POP);
                    orPos.setPosition(-1);
                    orPos.setParameter1(String.valueOf(sector.getId()));
                    orPos.setParameter2(String.valueOf(sector.getPopulation()));
                    orPos.setParameter3(String.valueOf(sector.getRegionId()));
                    orPos.setParameter4(String.valueOf(sector.getNationId()));
                    sectorOrderMap.put(sector.getId(), orPos);
                }
            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, CANCEL_PRODUCT_WARNING, false);
            }
        }

        EcoEventManager.changeSector(sector.getRegionId());
    }

    /**
     * Method that hands over a sector to a selected allied empire
     *
     * @param sector   the target sector to hand over
     * @param nationId the allied empire's nation identification number
     */
    public void handOverSector(final SectorDTO sector, final int nationId) {
        // check for conflicts
        final List<ClientOrderDTO> changeRelationOrders = OrderStore.getInstance().getClientOrders().get(ORDER_POLITICS);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (changeRelationOrders != null) {
            for (ClientOrderDTO order : changeRelationOrders) {
                if (order.getIdentifier(0) == nationId) {
                    conflictOrders.add(order);
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot handover tile to a nation that you changed the relations first. Review conflict orders?", true) {
                public void onAccept() {
                    super.onAccept();
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                    viewer.show();
                    viewer.center();
                }
            };
            return;
        }

        //done checking for conflicts
        if (!sectorOrderMap.containsKey(sector.getId()) || sectorOrderMap.get(sector.getId()).getOrderType() != ORDER_HOVER_SEC) {
            //get home nation of this sector
            final NationDTO nationReceiver = DataStore.getInstance().getNationById(nationId);
            final NationDTO nationHome = DataStore.getInstance().getNationByCode(sector.getPoliticalSphere());
            if (nationHome != null && (getSphere(sector, nationReceiver) <= 2)
                    || (sector.getPopulation() <= 1)
                    || (sector.getPopulation() <= 2
                    && sectorOrderMap.containsKey(sector.getId())
                    && sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_DEC_POP)) {
                final int[] ids = new int[2];
                ids[0] = sector.getId();
                ids[1] = 0;
                if (OrderStore.getInstance().addNewOrder(ORDER_HOVER_SEC, CostCalculators.getHandOverCost(ORDER_HOVER_SEC, sector.getPopulation()), sector.getRegionId(), "", ids, 0, "") == 1) {
                    OrderPositionDTO orPos = new OrderPositionDTO();
                    if (sectorOrderMap.containsKey(sector.getId())) {
                        orPos = sectorOrderMap.get(sector.getId());
                        orPos.setParameter4(String.valueOf(nationId));

                    } else {
                        orPos.setOrderType(ORDER_HOVER_SEC);
                        orPos.setPosition(-1);
                        orPos.setParameter1(String.valueOf(sector.getId()));
                        orPos.setParameter2(String.valueOf(sector.getPopulation()));
                        orPos.setParameter3(String.valueOf(sector.getRegionId()));
                        orPos.setParameter4(String.valueOf(nationId));
                        sectorOrderMap.put(sector.getId(), orPos);
                    }
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_HOVER_SEC, ids);
                }
            } else if (sector.getNationId() != nationId) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot handover sector outiside of sphere of influence of the receiver with population greater to 1", false);
            }
        } else if (sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_HOVER_SEC) {
            sectorOrderMap.remove(sector.getId());
            final int[] ids = new int[2];
            ids[0] = sector.getId();
            ids[1] = 0;
            OrderStore.getInstance().removeOrder(ORDER_HOVER_SEC, ids);

        }
        EcoEventManager.changeSector(sector.getRegionId());
    }

    /**
     * Undo hand over sector order.
     *
     * @param sectorId The sector id to undo the order.
     */
    public void undoHandOverSector(final int sectorId) {
        final SectorDTO sector = getSectorById(sectorId);
        if (sectorOrderMap.containsKey(sectorId)
                && Integer.parseInt(sectorOrderMap.get(sectorId).getParameter4()) != sector.getNationId()) {
            int[] ids = new int[2];
            ids[0] = sectorId;
            ids[1] = 0;
            OrderStore.getInstance().removeOrder(ORDER_HOVER_SEC, ids);
            //if it is a simple handover order... remove it
            if (sectorOrderMap.get(sectorId).getOrderType() == ORDER_HOVER_SEC) {
                sectorOrderMap.remove(sectorId);
            } else {
                sectorOrderMap.get(sectorId).setParameter4(String.valueOf(sector.getNationId()));
            }
        }
        EcoEventManager.changeSector(sector.getRegionId());

    }

    // Method that return true if a sector is increased population in this turn
    public boolean isIncreased(final int sectorId) {
        return sectorOrderMap.containsKey(sectorId) && sectorOrderMap.get(sectorId).getOrderType() == ORDER_INC_POP;
    }

    // Method that return true if a sector is decreased in population in this turn
    public boolean isDecreased(final int sectorId) {
        return sectorOrderMap.containsKey(sectorId) && sectorOrderMap.get(sectorId).getOrderType() == ORDER_DEC_POP;
    }

    /**
     * Method that cancels any given command
     * for this sector and returns success
     *
     * @param selTileSector: Target sector
     * @return true if the command was canceled successfully
     */
    public boolean cancelOrder(final SectorDTO selTileSector) {
        boolean canUndoCommand = true;
        if (sectorOrderMap.containsKey(selTileSector.getId())) {
            if (sectorOrderMap.get(selTileSector.getId()).getOrderType() == ORDER_INC_POP
                    && ProductionSiteStore.getInstance().getSectorProdSites().containsKey(selTileSector)) {
                final int prodSiteId = ProductionSiteStore.getInstance().getSectorProdSites().get(selTileSector);
                final ProductionSiteDTO productionSite = DataStore.getInstance().getProdSite(prodSiteId);
                if (selTileSector.getPopulation() < productionSite.getMinPopDensity()) {
                    canUndoCommand = false;
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Cancel production site building before undoing command", false);
                }
            } else if (sectorOrderMap.get(selTileSector.getId()).getOrderType() == ORDER_DEC_POP) {

                //you are going to decrease the population in the warehouse... so be sure you have enough population.

                final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_DEC_POP, selTileSector.getId(), selTileSector.getRegionId());
                if (WarehouseStore.getInstance().canRefundOrder(order) < 1) {
                    canUndoCommand = false;
                    new ErrorPopup(ErrorPopup.Level.WARNING, "There are not enough people in warehouse to return sector to its original population density.", false);
                }
            }

            if (canUndoCommand) {
                final int[] ids = new int[2];
                ids[0] = selTileSector.getId();
                ids[1] = selTileSector.getRegionId();
                OrderStore.getInstance().removeOrder(sectorOrderMap.get(selTileSector.getId()).getOrderType(), ids);
                if (sectorOrderMap.get(selTileSector.getId()).getOrderType() == ORDER_HOVER_SEC
                        || Integer.parseInt(sectorOrderMap.get(selTileSector.getId()).getParameter4()) == selTileSector.getNationId()) {
                    sectorOrderMap.remove(selTileSector.getId());
                } else {
                    sectorOrderMap.get(selTileSector.getId()).setOrderType(ORDER_HOVER_SEC);
                }
                EcoEventManager.changeSector(selTileSector.getRegionId());
            }
            return canUndoCommand;

        } else {
            return false;
        }
    }

    /**
     * Method that returns the population of a tile based
     * on the increase population orders given this turn.
     *
     * @param sector: The target sector whose population we require.
     * @return the sector population.
     */
    public int getSectorNextTurnPopulation(final SectorDTO sector) {
        if (sectorOrderMap.containsKey(sector.getId()) &&
                sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_INC_POP) {
            return (sector.getPopulation() + 1);

        } else if (sectorOrderMap.containsKey(sector.getId()) &&
                sectorOrderMap.get(sector.getId()).getOrderType() == ORDER_DEC_POP) {
            return (sector.getPopulation() - 1);

        } else {
            return sector.getPopulation();
        }
    }

    /**
     * @param regionId the id of the target region
     * @return the name of the region whose id we provided
     */
    public String getRegionNameById(final int regionId) {
        switch (regionId) {
            case RegionConstants.EUROPE:
                return "Europe";

            case RegionConstants.CARIBBEAN:
                return "Caribbean";

            case RegionConstants.INDIES:
                return "India";

            case RegionConstants.AFRICA:
                return "Africa";

            default:
                return "";
        }
    }

    public SectorDTO[][] getRegionSectorsByRegionId(final int regionId) {
        if (regionSectorsArrayMap.containsKey(regionId)) {
            return regionSectorsArrayMap.get(regionId);
        } else {
            return null;
        }
    }

    public void setSectorOrderMap(final Map<Integer, OrderPositionDTO> sectorOrderMap) {
        this.sectorOrderMap = sectorOrderMap;
    }

    public Map<Integer, OrderPositionDTO> getSectorOrderMap() {
        return sectorOrderMap;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the regionSectorsMap
     */
    public Map<Integer, Map<Integer, SectorDTO>> getRegionSectorsMap() {
        return regionSectorsMap;
    }

    public int getCurrentOwner(final SectorDTO selSector) {
        if (sectorOrderMap.containsKey(selSector.getId())) {

            return Integer.parseInt(sectorOrderMap.get(selSector.getId()).getParameter4());
        } else {
            return selSector.getNationId();
        }
    }

    /**
     * Sets the selected sector in the client UI
     *
     * @param sector the Sector.
     */
    public void setSelectedSector(final SectorDTO sector) {
        regionSelSectorMap.put(sector.getRegionId(), sector.getId());
        GameStore.getInstance().getSectorMenu().setSelectedSectorInfo(sector);
    }

    /**
     * Gets the selected sector in the client UI
     *
     * @param regionId the Region
     * @return sector the Sector
     */
    public SectorDTO getSelectedSector(final int regionId) {
        if (regionSelSectorMap.get(regionId) == null) {
            return null;

        } else {
            final int sectorId = regionSelSectorMap.get(regionId);
            return regionSectorsMap.get(regionId).get(sectorId);
        }
    }

    /**
     * Method that returns a sector by it's id
     *
     * @param sectorId the id of the sector
     * @return the sector object
     */
    public SectorDTO getSectorById(final int sectorId) {
        for (int regionId = 1; regionId <= 4; regionId++) {
            if (regionSectorsMap.get(regionId).containsKey(sectorId)) {
                return regionSectorsMap.get(regionId).get(sectorId);
            }
        }
        return null;
    }

    /**
     * Method that returns sector by it's information.
     *
     * @param position the position to retrieve.
     * @return the sector corresponding to the position.
     */
    public SectorDTO getSectorByPosition(final PositionDTO position) {
        return getRegionSectorsByRegionId(position.getRegionId())[position.getX()][position.getY()];
    }

    public SectorDTO getSectorByStartingPosition(final PositionDTO position) {
        return getRegionSectorsByRegionId(position.getRegionId())[position.getXStart()][position.getYStart()];
    }

    public BattleDTO hasSectorNavalBattle(final PositionDTO position) {
        if (mapNavalPos.containsKey(position)) {
            return mapNavalPos.get(position);
        }
        return null;
    }

    public BattleDTO hasSectorTacticalBattle(final PositionDTO position) {
        if (mapTacticalPos.containsKey(position)) {
            return mapTacticalPos.get(position);
        }
        return null;
    }

    /**
     * Identify if sector is a home region, inside sphere of influence, or outside of the receiving nation.
     *
     * @param sector   the sector to examine.
     * @param receiver the receiving nation.
     * @return 1 if home region, 2 if in sphere of influence, 3 if outside.
     */
    protected final int getSphere(final SectorDTO sector, final NationDTO receiver) {
        final char thisNationCodeLower = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
        final char thisSectorCodeLower = String.valueOf(sector.getPoliticalSphere()).toLowerCase().charAt(0);
        int sphere = 1;

        // Check if this is not home region
        if (thisNationCodeLower != thisSectorCodeLower) {
            sphere = 2;

            // Check if this is outside sphere of influence
            if (receiver.getSphereOfInfluence().toLowerCase().indexOf(thisSectorCodeLower) < 0) {
                sphere = 3;
            }
        }

        return sphere;
    }

    public int getRegionBySectorId(int sectorId) {
        return sectorIdToRegionId.get(sectorId);
    }

    /**
     * Get all sites with tactical battles.
     *
     * @return a collection of BattleDTO objects.
     */
    public Collection<BattleDTO> listTacticalBattles() {
        return mapTacticalPos.values();
    }

    /**
     * Get all sites with naval battles.
     *
     * @return a collection of BattleDTO objects.
     */
    public Collection<BattleDTO> listNavalBattles() {
        return mapNavalPos.values();
    }

}
