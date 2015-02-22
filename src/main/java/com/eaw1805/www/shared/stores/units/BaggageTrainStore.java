package empire.webapp.shared.stores.units;

import empire.data.constants.ArmyConstants;
import empire.data.constants.GoodConstants;
import empire.data.constants.OrderConstants;
import empire.data.dto.common.PositionDTO;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.ClientOrderDTO;
import empire.data.dto.web.OrderCostDTO;
import empire.data.dto.web.economy.BaggageTrainDTO;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.events.units.UnitEventManager;
import empire.webapp.client.views.infopanels.units.BaggageTrainInfoPanel;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.orders.economy.ChangeBaggageTrainNameOrder;
import empire.webapp.shared.orders.economy.ScuttleBaggageTrainOrder;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.InfoPanelsStore;
import empire.webapp.shared.stores.MovementStore;
import empire.webapp.shared.stores.economy.OrderStore;
import empire.webapp.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class BaggageTrainStore implements OrderConstants, ArmyConstants, GoodConstants {

    /**
     * A list containing all the available baggage trains and the corresponding map.
     */
    private final transient List<BaggageTrainDTO> baggageTList = new ArrayList<BaggageTrainDTO>();

    /**
     * Indexes baggage trains based on the identity.
     */
    private final transient Map<Integer, BaggageTrainDTO> baggageTMap = new HashMap<Integer, BaggageTrainDTO>();

    /**
     * A map containing all the new baggage trains
     */
    private final Map<Integer, List<BaggageTrainDTO>> newBaggageTMap = new HashMap<Integer, List<BaggageTrainDTO>>();

    /**
     * A mapping between positions and baggage trains.
     */
    private final transient Map<Integer, Map<String, Map<Integer, BaggageTrainDTO>>> positionToBTrains =
            new HashMap<Integer, Map<String, Map<Integer, BaggageTrainDTO>>>();

    /**
     * Our instance of the Manager.
     */
    private static BaggageTrainStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized.
     */
    private boolean isInitialized = false, isClient = false;

    // constructor
    private BaggageTrainStore() {
        super();
    }

    /**
     * Method returning the baggageStore manager if already initialized.
     *
     * @return the unique instance of the store.
     */
    public static BaggageTrainStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new BaggageTrainStore();
        }
        return ourInstance;
    }

    public Map<Integer, BaggageTrainDTO> getBaggageTMap() {
        return baggageTMap;
    }

    /**
     * Initialize the baggage trains as we took them from the DataBase.
     *
     * @param baggageTList the initial list of baggage trains.
     */
    public void initDbBaggageTrains(final List<BaggageTrainDTO> baggageTList) {
        try {
            this.baggageTList.addAll(baggageTList);

            for (BaggageTrainDTO baggageTrain : this.baggageTList) {
                baggageTMap.put(baggageTrain.getId(), baggageTrain);
            }
            try {
                initIndexes();
            } catch (Exception e) {

            }
            if (isClient()) {
                LoadEventManager.loadBtrains(getBaggageTList());
            }

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize baggage trains due to unexpected reason", false);
        }
    }

    public final void initIndexes() {
        positionToBTrains.clear();
        for (BaggageTrainDTO bTrain : baggageTList) {
            if (!positionToBTrains.containsKey(bTrain.getRegionId())) {
                positionToBTrains.put(bTrain.getRegionId(), new HashMap<String, Map<Integer, BaggageTrainDTO>>());
            }
            if (!positionToBTrains.get(bTrain.getRegionId()).containsKey(bTrain.positionToString())) {
                positionToBTrains.get(bTrain.getRegionId()).put(bTrain.positionToString(), new HashMap<Integer, BaggageTrainDTO>());
            }
            positionToBTrains.get(bTrain.getRegionId()).get(bTrain.positionToString()).put(bTrain.getId(), bTrain);
        }
    }

    public void updatePositionIndex(final int unitId, final String oldPos) {
        final BaggageTrainDTO train = getBaggageTrainById(unitId);
        positionToBTrains.get(train.getRegionId()).get(oldPos).remove(unitId);
        if (!positionToBTrains.get(train.getRegionId()).containsKey(train.positionToString())) {
            positionToBTrains.get(train.getRegionId()).put(train.positionToString(), new HashMap<Integer, BaggageTrainDTO>());
        }
        positionToBTrains.get(train.getRegionId()).get(train.positionToString()).put(unitId, train);
    }

    public List<BaggageTrainDTO> getBaggageTrainsByRegion(final int regionId, final boolean onlyNotScuttle) {
        final List<BaggageTrainDTO> regionBaggageTrains = new ArrayList<BaggageTrainDTO>();
        if (positionToBTrains.containsKey(regionId)) {
            for (final Map<Integer, BaggageTrainDTO> bTrains : positionToBTrains.get(regionId).values()) {
                for (final BaggageTrainDTO bTrain : bTrains.values()) {
                    if (!onlyNotScuttle || !bTrain.isScuttle()) {
                        regionBaggageTrains.add(bTrain);
                    }
                }
            }
        }
        return regionBaggageTrains;
    }

    public BaggageTrainDTO getBaggageTrainById(final int bTrainId) {
        return getBaggageTMap().get(bTrainId);
    }

    /**
     * Method that returns the baggage trains of a sector.
     *
     * @param sector the sector to look up.
     * @return all trains stationed at given sector.
     */
    public List<BaggageTrainDTO> getBaggageTrainsBySector(final SectorDTO sector, final boolean onlyNotScuttle) {

        final List<BaggageTrainDTO> tileBaggageTrains = new ArrayList<BaggageTrainDTO>();
        if (!positionToBTrains.containsKey(sector.getRegionId())
                || !positionToBTrains.get(sector.getRegionId()).containsKey(sector.positionToString())) {
            return tileBaggageTrains;
        }
        for (final BaggageTrainDTO baggageTrain : positionToBTrains.get(sector.getRegionId()).get(sector.positionToString()).values()) {
            if (!onlyNotScuttle || !baggageTrain.isScuttle()) {
                tileBaggageTrains.add(baggageTrain);
            }
        }

        return tileBaggageTrains;
    }

    public boolean hasSectorBtrains(final SectorDTO sector) {
        boolean out = false;
        try {
            out = (positionToBTrains.containsKey(sector.getRegionId()) && positionToBTrains.get(sector.getRegionId()).containsKey(sector.positionToString())
                    && !positionToBTrains.get(sector.getRegionId()).get(sector.positionToString()).isEmpty());
        } catch (Exception e) {

        }

        return out;
    }

    /**
     * Method that builds a new baggage train on a
     * selected barrack.
     *
     * @param sector the sector where the barrack is.
     * @param name   the name of the new baggage train
     *               placed.
     * @return The new map.
     */
    public boolean buildNewBaggageTrain(final SectorDTO sector, final String name) {
        final BaggageTrainDTO newBtrain = new BaggageTrainDTO();
        int[] ids = new int[2];
        ids[0] = sector.getId();
        ids[1] = getTotalSize();
        if (OrderStore.getInstance().addNewOrder(ORDER_B_BTRAIN, CostCalculators.getBaggageTrainCost(), sector.getRegionId(), name, ids, 0, "") == 1) {
            if (!newBaggageTMap.containsKey(sector.getId())) {
                newBaggageTMap.put(sector.getId(), new ArrayList<BaggageTrainDTO>());
            }
            newBtrain.setRegionId(sector.getRegionId());
            newBtrain.setId(getTotalSize());
            newBtrain.setName(name);
            newBaggageTMap.get(sector.getId()).add(newBtrain);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Method that removes a new baggage train from a selected barrack.
     *
     * @param sector   the sector where the barrack is.
     * @param btrainId the id of the new baggage train placed.
     * @return True if we found the command and deleted it successfully.
     */
    public boolean removeBuildOrder(final SectorDTO sector, final int btrainId) {
        for (final BaggageTrainDTO btrain : newBaggageTMap.get(sector.getId())) {
            if (btrain.getId() == btrainId) {
                int[] ids = new int[2];
                ids[0] = sector.getId();
                ids[1] = btrainId;
                if (OrderStore.getInstance().removeOrder(ORDER_B_BTRAIN, ids)) {
                    newBaggageTMap.get(sector.getId()).remove(btrain);
                    if (newBaggageTMap.get(sector.getId()).size() < 1) {
                        newBaggageTMap.remove(sector.getId());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public List<BaggageTrainDTO> getBaggageTList() {
        return baggageTList;
    }

    /**
     * @param value the isClient flag to set.
     */
    public void setClient(final boolean value) {
        this.isClient = value;
    }

    /**
     * @return the isClient
     */
    public boolean isClient() {
        return isClient;
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

    /**
     * @return the newBaggageTMap
     */
    public Map<Integer, List<BaggageTrainDTO>> getNewBaggageTMap() {
        return newBaggageTMap;
    }

    /**
     * Method that returns a helpfull number indicating
     * the number of the baggage trains we will have next round
     *
     * @return Full size of new and old baggage train
     *         data stores
     */
    public int getTotalSize() {
        int btrains = 0;
        for (final int id : newBaggageTMap.keySet()) {
            btrains += newBaggageTMap.get(id).size();
        }
        btrains += baggageTList.size();
        return btrains;
    }

    /**
     * Method that repairs the target baggage train
     *
     * @param btrainId the id of the train we want to repair
     * @param sectorId the id of the sector where the repairs will happen
     */
    public void addRepairOrder(final int btrainId, final int sectorId) {
        BaggageTrainDTO btrain = baggageTMap.get(btrainId);
        final int[] ids = new int[2];
        ids[0] = btrainId;
        ids[1] = sectorId;
        ids[2] = btrain.getRegionId();
        ids[3] = btrain.getNationId();
        if (OrderStore.getInstance().addNewOrder(ORDER_R_BTRAIN, CostCalculators.getBaggageTrainRepairCost(btrain.getCondition()), btrain.getRegionId(), "", ids, 0, "") == 1) {
            btrain.setCondition(100);
            UnitEventManager.changeUnit(BAGGAGETRAIN, btrainId);
        }
    }

    /**
     * Method that cancels repairs of a certain baggage train on
     * a specific sector
     *
     * @param btrainId the id of the train we want to repair
     * @param sectorId the id of the sector where the repairs will happen
     */
    public void cancelRepairOrder(final int btrainId, final int sectorId) {
        BaggageTrainDTO btrain = baggageTMap.get(btrainId);
        final List<ClientOrderDTO> orders = OrderStore.getInstance().getClientOrders().get(ORDER_R_BTRAIN);
        int conditionRepaired = 0;
        for (final ClientOrderDTO order : orders) {
            if (order.getIdentifier(0) == btrainId) {
                final OrderCostDTO cost = order.getCosts();
                conditionRepaired = cost.getNumericCost(GOOD_PEOPLE) / 10;
                break;
            }
        }

        final int[] ids = new int[2];
        ids[0] = btrainId;
        ids[1] = sectorId;
        if (OrderStore.getInstance().removeOrder(ORDER_R_BTRAIN, ids)) {
            btrain.setCondition(btrain.getOriginalCondition());

        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Unexpected Problem. Could not cancel command", false);
        }
    }

    /**
     * Method that loads a good to the b train
     *
     * @param goodId the id of the good
     * @param id     the id of the train
     * @param qte    the quantity to add
     */
    public void loadGood(final Integer goodId, final int id, final int qte) {
        baggageTMap.get(id).getGoodsDTO().get(goodId).setQte(baggageTMap.get(id).getGoodsDTO().get(goodId).getQte() + qte);
    }

    /**
     * Method that unloads a good to the b train
     *
     * @param goodId the id of the good
     * @param id     the id of the train
     * @param qte    the quantity to remove
     */
    public void unLoadGood(final Integer goodId, final int id, final int qte) {
        baggageTMap.get(id).getGoodsDTO().get(goodId).setQte(baggageTMap.get(id).getGoodsDTO().get(goodId).getQte() - qte);
    }

    /**
     * Method that checks if there are any
     * baggage trains available to embark
     *
     * @param sector the target sector
     * @return true if the trains in question exist
     */
    public boolean hasBtrainsToEmbark(final PositionDTO sector) {
        for (final BaggageTrainDTO btrain : baggageTMap.values()) {
            final PositionDTO pos = MovementStore.getInstance().getUnitPosition(BAGGAGETRAIN, btrain.getId(), btrain);
            if (pos.getRegionId() == btrain.getRegionId() && pos.getX() == sector.getX() && pos.getY() == sector.getY()) {
                return true;
            }
        }
        return false;
    }

    public void renameBaggageTrain(final int bTrainId, final String name) {
        if (bTrainId != 0) {
            int[] ids = new int[1];
            ids[0] = bTrainId;
            undoRenameBaggageTrain(bTrainId);
            BaggageTrainDTO train = getBaggageTrainById(bTrainId);
            if (!name.equals(train.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_BTRAIN, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeBaggageTrainName(bTrainId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_BTRAIN, ids);
                }
                UnitEventManager.changeUnit(BAGGAGETRAIN, bTrainId);
            }
        }
    }

    public void changeBaggageTrainName(final int bTrainId, final String name) {
        if (baggageTMap.containsKey(bTrainId)) {
            final ChangeBaggageTrainNameOrder ccnName = new ChangeBaggageTrainNameOrder(baggageTMap, name);
            ccnName.execute(bTrainId);
        }
    }

    /**
     * Remove baggage train rename order.
     *
     * @param bTrainId The Baggage train to undo the rename.
     */
    public void undoRenameBaggageTrain(final int bTrainId) {
        final int[] ids = new int[1];
        ids[0] = bTrainId;
        OrderStore.getInstance().removeOrder(ORDER_REN_BTRAIN, ids);
        BaggageTrainDTO train = getBaggageTrainById(bTrainId);
        if (train != null) {
            train.setName(train.getOriginalName());
        }
        UnitEventManager.changeUnit(BAGGAGETRAIN, bTrainId);
    }

    /**
     * Add a new scuttle baggage train order.
     *
     * @param bTrainId The baggage train to scuttle.
     * @param regionId The region id.
     */
    public void addScuttleOrder(final int bTrainId, final int regionId) {
        final int[] ids = new int[1];
        ids[0] = bTrainId;
        undoScuttleOrder(bTrainId);
        OrderStore.getInstance().addNewOrder(ORDER_SCUTTLE_BTRAIN, CostCalculators.getScuttleBaggageTrainCost(BaggageTrainStore.getInstance().getBaggageTMap().get(bTrainId).getCondition()), regionId, "", ids, 0, "");
        scuttleBaggageTrain(bTrainId, true, false);
    }

    /**
     * Execute scuttle baggage train order.
     *
     * @param bTrainId The baggage train to scuttle.
     * @param scuttle  True to scuttle.
     * @param isInit   True if it is called from server code.
     */
    public void scuttleBaggageTrain(final int bTrainId, final boolean scuttle, final boolean isInit) {
        //execute command
        final ScuttleBaggageTrainOrder sbtOrder = new ScuttleBaggageTrainOrder(baggageTMap, scuttle);
        sbtOrder.execute(bTrainId);

        final BaggageTrainDTO btrain = baggageTMap.get(bTrainId);
        if (!isInit) {
            //if scuttle is true remove from info panel
            boolean found = false;
            final Iterator<BaggageTrainInfoPanel> iter = InfoPanelsStore.getInstance().getBtrainInfoPanelsByRegion(btrain.getRegionId()).iterator();
            while (iter.hasNext()) {
                final BaggageTrainInfoPanel infoPanel = iter.next();
                if (infoPanel.getBaggageTrain().getId() == bTrainId) {
                    found = true;
                    //if scuttle remove from info panels.
                    if (scuttle) {
                        iter.remove();

                    }
                }
            }

            //if not scuttle and not displayed in info panels then add it.
            if (!scuttle && !found) {
                InfoPanelsStore.getInstance().getBtrainInfoPanelsByRegion(btrain.getRegionId()).add(new BaggageTrainInfoPanel(btrain));
            }
            UnitEventManager.changeUnit(BAGGAGETRAIN, bTrainId);
        }
    }

    /**
     * Undo scuttle baggage train order.
     *
     * @param bTrainId The baggage train to undo the order.
     */
    public void undoScuttleOrder(final int bTrainId) {
        final int[] ids = new int[1];
        ids[0] = bTrainId;
        OrderStore.getInstance().removeOrder(ORDER_SCUTTLE_BTRAIN, ids);
        //un-scuttle baggage train
        scuttleBaggageTrain(bTrainId, false, false);
    }
}
