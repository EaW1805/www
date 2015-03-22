package com.eaw1805.www.shared.stores.units;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.orders.army.ChangeSpyNameOrder;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds Spy records and their status as per player orders.
 */
public class SpyStore implements ArmyConstants, OrderConstants {

    /**
     * A list containing all the spies of our target nation.
     */
    private final transient List<SpyDTO> spyList = new ArrayList<SpyDTO>();

    /**
     * A hash map containing all the spy Ids and their corresponding spies.
     */
    private final transient Map<Integer, SpyDTO> spyMap = new HashMap<Integer, SpyDTO>();

    // Our instance of the Manager
    private static transient SpyStore ourInstance = null;

    private boolean isClient;
    private boolean isInitialized = false;

    // constructor
    private SpyStore() {
        super();
    }

    // Method returning the economy manager if already initialized
    // or the a new instance
    public static SpyStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new SpyStore();
        }
        return ourInstance;
    }

    // Initialize the spies as we took them from the DataBase
    public void initDbSpy(final List<SpyDTO> spyList) {
        try {
            this.spyList.addAll(spyList);

            for (SpyDTO spy : this.spyList) {
                spyMap.put(spy.getSpyId(), spy);
            }
            isInitialized = true;
            if (isClient) {
                LoadEventManager.loadSpies(spyList);
            }

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize spies due to unexpected reason", false);
        }
    }

    public List<SpyDTO> getSpiesByRegion(final int regionId) {
        final List<SpyDTO> regionSpies = new ArrayList<SpyDTO>();
        for (SpyDTO spy : spyList) {
            if (spy.getRegionId() == regionId) {
                regionSpies.add(spy);
            }
        }
        return regionSpies;
    }

    public List<SpyDTO> getSpiesBySector(final SectorDTO selSector) {
        final List<SpyDTO> tileSpies = new ArrayList<SpyDTO>();
        for (SpyDTO spy : spyList) {
            if (spy.getX() == selSector.getX() && spy.getY() == selSector.getY() && spy.getRegionId() == selSector.getRegionId()) {
                tileSpies.add(spy);
            }
        }
        return tileSpies;
    }

    public List<SpyDTO> getSpiesByPositionWithMovement(final int regionId,
                                                       final int xPos,
                                                       final int yPos,
                                                       final boolean afterMovement) {
        final List<SpyDTO> tileSpies = new ArrayList<SpyDTO>();
        for (SpyDTO spy : spyList) {
            PositionDTO pos = MovementStore.getInstance().getUnitPosition(SPY, spy.getSpyId(), spy);
            if (!afterMovement) {
                pos = new PositionDTO();
                pos.setRegionId(spy.getRegionId());
                pos.setX(spy.getX());
                pos.setY(spy.getY());
            }

            if (pos.getX() == xPos && pos.getY() == yPos && pos.getRegionId() == regionId) {
                tileSpies.add(spy);
            }
        }
        return tileSpies;
    }

    public boolean hasSectorSpies(final SectorDTO selSector) {
        for (SpyDTO spy : spyList) {
            if (spy.getX() == selSector.getX()
                    && spy.getY() == selSector.getY()
                    && spy.getRegionId() == selSector.getRegionId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve spy object based on identity.
     *
     * @param id the identity of the spy object.
     * @return the spy object, or null if not found.
     */
    public SpyDTO getSpyById(final int id) {
        if (spyMap.containsKey(id)) {
            return spyMap.get(id);
        }
        return null;
    }

    public void undoRenameSpy(final int spyId) {
        final int[] ids = new int[1];
        ids[0] = spyId;
        OrderStore.getInstance().removeOrder(ORDER_REN_SPY, ids);
        final SpyDTO spy = getSpyById(spyId);
        if (spy != null) {
            spy.setName(spy.getOriginalName());
        }
        UnitEventManager.changeUnit(SPY, spyId);
    }

    public void renameSpy(final int spyId, final String name) {
        if (spyId != 0) {
            int[] ids = new int[1];
            ids[0] = spyId;
            undoRenameSpy(spyId);
            SpyDTO spy = getSpyById(spyId);
            if (!name.equals(spy.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_SPY, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeSpyName(spyId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_SPY, ids);
                }
                UnitEventManager.changeUnit(SPY, spyId);
            }
        }
    }

    public void changeSpyName(final int spyId, final String name) {
        if (spyMap.containsKey(spyId)) {
            final ChangeSpyNameOrder ccnName = new ChangeSpyNameOrder(spyMap, name);
            ccnName.execute(spyId);
        }
    }

    public List<SpyDTO> getSpyList() {
        return spyList;
    }

    /**
     * @param isClient the isClient to set
     */
    public void setClient(final boolean isClient) {
        this.isClient = isClient;
    }

    /**
     * @return the isClient
     */
    public boolean isClient() {
        return isClient;
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }


}
