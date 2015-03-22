package com.eaw1805.www.shared.stores.economy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.orders.economy.ChangeBarrackNameOrder;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.InfoPanelsStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BarrackStore implements OrderConstants {

    /**
     * A list containing all the available barracks and the corresponding map.
     */
    private transient List<BarrackDTO> barracksList = new ArrayList<BarrackDTO>();

    /**
     * Indexes barracks objects based on their position.
     */
    private final transient Map<String, BarrackDTO> barracksMapSector = new HashMap<String, BarrackDTO>();

    /**
     * Indexes barracks objects based on their id.
     */
    private final transient Map<Integer, BarrackDTO> barracksMap = new HashMap<Integer, BarrackDTO>();

    /**
     * Variable telling us if our data are initialized.
     */
    private boolean isInitialized = false;

    /**
     * Our instance of the Manager.
     */
    private static BarrackStore ourInstance = null;

    /**
     * default constructor.
     */
    private BarrackStore() {
        // do nothing
    }

    /**
     * Method returning the economy manager if already initialized
     * or the a new instance.
     *
     * @return the unique instance of the store.
     */
    public static BarrackStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new BarrackStore();
        }
        return ourInstance;
    }


    // Method Used By the service to initialize Barrack data
    public void initBarrShip(final List<BarrackDTO> barrShipList) {
        try {
            // clear any previous additions to collections
            this.barracksList = barrShipList;
            for (BarrackDTO barrack : barracksList) {
                barracksMap.put(barrack.getId(), barrack);
                barracksMapSector.put(barrack.positionToString(), barrack);
            }

            InfoPanelsStore.getInstance().initBarrackInfoPanels(barracksList);
            setInitialized(true);
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize barracks due to unexcpected reason", false);
        }
    }

    public List<BarrackDTO> getBarracksList() {
        return barracksList;
    }

    public Map<Integer, BarrackDTO> getBarracksMap() {
        return barracksMap;
    }

    public BarrackDTO getBarrackById(final int barId) {
        return barracksMap.get(barId);
    }

    @SuppressWarnings({"unused", "restriction"})
    private List<BarrackDTO> getBarracksByRegion(final int regionId) {
        final List<BarrackDTO> regionBarracks = new ArrayList<BarrackDTO>();
        for (BarrackDTO spy : barracksList) {
            if (spy.getRegionId() == regionId) {
                regionBarracks.add(spy);
            }
        }
        return regionBarracks;
    }

    /**
     * Method that returns the barrack of a target sector
     *
     * @param sector in which we search for barracks
     * @return the barrack of the target sector
     */
    public BarrackDTO getBarrackByPosition(final PositionDTO sector) {
        if (barracksMapSector.containsKey(sector.positionToString())) {
            return barracksMapSector.get(sector.positionToString());
        }

        return null;
    }

    /**
     * Method that returns the next barrack in the list of
     * barracks
     *
     * @param barrack the current barrack whose next we are
     *                searching for in the list
     * @return the next barrack
     */
    public BarrackDTO getNextBarrack(final BarrackDTO barrack) {
        int index = barracksList.indexOf(barrack);
        if (index < barracksList.size() - 1) {
            return barracksList.get(++index);

        } else if (index == barracksList.size() - 1) {
            return barracksList.get(0);

        } else {
            return null;
        }

    }

    public String getBarracksName(final BarrackDTO barrShip) {
        String name;
        final TradeCityDTO tradeCity = TradeCityStore.getInstance().getTradeCityByPosition(barrShip);
        if (tradeCity != null) {
            name = tradeCity.getName();

        } else if (barrShip.getName() == null) {
            name = "Barrack";

        } else {
            name = barrShip.getName();
        }
        return name;
    }

    /**
     * Remove barrack rename order.
     *
     * @param barrId The barrack to undo the rename.
     */
    public void undoRenameBarrack(final int barrId) {
        final int[] ids = new int[1];
        ids[0] = barrId;
        OrderStore.getInstance().removeOrder(ORDER_REN_BARRACK, ids);
        BarrackDTO barrack = getBarrackById(barrId);
        if (barrack != null) {
            barrack.setName(barrack.getOriginalName());
        }
        UnitEventManager.changeUnit(ArmyConstants.BARRACK, barrId);
    }

    /**
     * Add rename barrack command.
     *
     * @param barId The barrack to rename.
     * @param name  The new name to set.
     */
    public void renameBarrack(final int barId, final String name) {
        if (barId != 0) {
            final int[] ids = new int[1];
            ids[0] = barId;
            undoRenameBarrack(barId);
            BarrackDTO barrack = getBarrackById(barId);
            if (!name.equals(barrack.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_BARRACK, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeBarrackName(barId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_BARRACK, ids);
                }
                UnitEventManager.changeUnit(ArmyConstants.BARRACK, barId);
            }
        }
    }

    /**
     * Apply barrack rename in clients view.
     *
     * @param barId The barrack to rename.
     * @param name  The new name to set.
     */
    public void changeBarrackName(final int barId, final String name) {
        try {
            if (barracksMap.containsKey(barId)) {
                final ChangeBarrackNameOrder ccnName = new ChangeBarrackNameOrder(barracksMap, name);
                ccnName.execute(barId);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Method that returns the previous barrack in the list of
     * barracks
     *
     * @param barrack the current barrack whose previous we are
     *                searching for in the list
     * @return the previous barrack
     */
    public BarrackDTO getPreviousBarrack(final BarrackDTO barrack) {
        int index = barracksList.indexOf(barrack);
        if (index > 0) {
            return barracksList.get(--index);

        } else if (index == 0) {
            return barracksList.get(barracksList.size() - 1);

        } else {
            return null;
        }
    }

    /**
     * @param value the isInitialized to set
     */
    public void setInitialized(final boolean value) {
        this.isInitialized = value;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

}
