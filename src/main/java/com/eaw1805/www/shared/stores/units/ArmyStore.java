package com.eaw1805.www.shared.stores.units;

import com.google.gwt.user.client.Window;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.loading.AllUnitsLoadedEvent;
import com.eaw1805.www.client.events.loading.AllUnitsLoadedHandler;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.popups.OrdersViewerPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.orders.army.AddBattalionToBrigade;
import com.eaw1805.www.shared.orders.army.ChangeArmyNameOrder;
import com.eaw1805.www.shared.orders.army.ChangeBattalionBrigadeOrder;
import com.eaw1805.www.shared.orders.army.ChangeBrigadeCorpOrder;
import com.eaw1805.www.shared.orders.army.ChangeBrigadeNameOrder;
import com.eaw1805.www.shared.orders.army.ChangeCorpArmyOrder;
import com.eaw1805.www.shared.orders.army.ChangeCorpNameOrder;
import com.eaw1805.www.shared.orders.army.CreateArmyOrder;
import com.eaw1805.www.shared.orders.army.CreateCorpOrder;
import com.eaw1805.www.shared.orders.army.DeleteArmyOrder;
import com.eaw1805.www.shared.orders.army.DeleteCorpOrder;
import com.eaw1805.www.shared.orders.army.MergeBattalionsOrder;
import com.eaw1805.www.shared.orders.army.UndoMerge;
import com.eaw1805.www.shared.orders.army.UpHeadCountBrigadeOrder;
import com.eaw1805.www.shared.orders.army.UpgradeBrigadeOrder;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.TradeCityStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.ClientUtil;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ArmyStore
        implements OrderConstants, ArmyConstants, ProductionSiteConstants, RelationConstants {

    /**
     * The army as we have made it so far mapped to their IDs.
     */
    private final transient Map<Integer, ArmyDTO> armiesMap = new HashMap<Integer, ArmyDTO>();

    /**
     * The armies divided in regions and sectors
     */
    private final transient Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>> sectorArmiesMap = new HashMap<Integer, Map<Integer, Map<Integer, ArmyDTO>>>();

    private final transient Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>> startingSectorArmiesMap = new HashMap<Integer, Map<Integer, Map<Integer, ArmyDTO>>>();

    /**
     * The army types we have for this nation.
     */
    private List<ArmyTypeDTO> armyTypesList = new ArrayList<ArmyTypeDTO>();

    private Map<Integer, ArmyTypeDTO> armyTypesMap = new HashMap<Integer, ArmyTypeDTO>();

    /**
     * A list of the merged battalions that no longer exist for the client
     */
    private List<BattalionDTO> mergedBatts = new ArrayList<BattalionDTO>();
    /**
     * Singleton instance of the ArmyManager.
     */
    private static ArmyStore ourInstance = null;

    // Variable telling us if our data are initializes
    private transient boolean isInitialized = false;

    /**
     * A map of sector ids and new brigades.
     */
    private transient Map<Integer, List<BrigadeDTO>> barrBrigMap = new HashMap<Integer, List<BrigadeDTO>>();

    private final static String CONFLICT_ORDERS_WARNING = "Orders that conflict with your action";
    private final static String FAILED_REMOVE_ORDER = "Failed to remove order";

    private Map<Integer, ArmyDTO> idToDeletedArmy = new HashMap<Integer, ArmyDTO>();

    private Map<Integer, CorpDTO> idToDeletedCorp = new HashMap<Integer, CorpDTO>();

    /**
     * Empty constructor. Defines initialization event
     */
    private ArmyStore() {
        LoadEventManager.addAllUnitsLoadedHandler(new AllUnitsLoadedHandler() {
            public void onAllLoaded(final AllUnitsLoadedEvent event) {

                initSectorArmiesMap();
                initStartingSectorArmiesMap();
                LoadEventManager.armiesInitialised();

            }
        });
    }

    // Method returning the army manager
    public static ArmyStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new ArmyStore();
        }
        return ourInstance;
    }

    // Method Used By the async callback to initialize  the armies
    public void initDbArmies(final Collection<ArmyDTO> armiesList, final List<ArmyDTO> deletedArmies, final List<CorpDTO> deletedCorps) {
        try {
            for (ArmyDTO army : armiesList) {
                armiesMap.put(army.getArmyId(), army);
            }
            //init deleted armies map
            for (ArmyDTO army : deletedArmies) {
                idToDeletedArmy.put(army.getArmyId(), army);
            }
            //init deleted corps map
            for (CorpDTO corps : deletedCorps) {
                idToDeletedCorp.put(corps.getCorpId(), corps);
            }

            isInitialized = true;
            LoadEventManager.loadArmies(armiesList);

        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize armies due to unexpected reason", false);
        }
    }

    public void initSectorArmiesMap() {

        sectorArmiesMap.clear();
        for (final ArmyDTO army : armiesMap.values()) {
            if (army.getArmyId() > 0) {
                if (!sectorArmiesMap.containsKey(army.getRegionId())) {
                    sectorArmiesMap.put(army.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                }

                final SectorDTO armySector = RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getX()][army.getY()];
                if (!sectorArmiesMap.get(army.getRegionId()).containsKey(armySector.getId())) {
                    sectorArmiesMap.get(army.getRegionId()).put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                }

                sectorArmiesMap.get(army.getRegionId()).get(armySector.getId()).put(army.getArmyId(), army);

            } else {
                for (final CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() > 0) {
                        if (!sectorArmiesMap.containsKey(corp.getRegionId())) {
                            sectorArmiesMap.put(corp.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                        }

                        final SectorDTO armySector = RegionStore.getInstance().getRegionSectorsByRegionId(corp.getRegionId())[corp.getX()][corp.getY()];
                        if (!sectorArmiesMap.containsKey(armySector.getRegionId())) {
                            sectorArmiesMap.put(armySector.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                        }

                        if (!sectorArmiesMap.get(armySector.getRegionId()).containsKey(armySector.getId())) {
                            sectorArmiesMap.get(armySector.getRegionId()).put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                        }

                        if (!sectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).containsKey(0)) {
                            final ArmyDTO zeroArmy = new ArmyDTO();
                            zeroArmy.setArmyId(0);
                            zeroArmy.setRegionId(corp.getRegionId());
                            zeroArmy.setX(corp.getX());
                            zeroArmy.setY(corp.getY());
                            sectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).put(0, zeroArmy);
                        }

                        sectorArmiesMap.get(corp.getRegionId()).get(armySector.getId()).get(0).getCorps().put(corp.getCorpId(), corp);

                    } else {
                        for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                            if (!sectorArmiesMap.containsKey(brigade.getRegionId())) {
                                sectorArmiesMap.put(brigade.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                            }

                            final SectorDTO armySector = RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getX()][brigade.getY()];
                            if (!sectorArmiesMap.containsKey(armySector.getRegionId())) {
                                sectorArmiesMap.put(armySector.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                            }

                            if (!sectorArmiesMap.get(armySector.getRegionId()).containsKey(armySector.getId())) {
                                sectorArmiesMap.get(armySector.getRegionId()).put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                            }

                            if (!sectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).containsKey(0)) {
                                final ArmyDTO zeroArmy = new ArmyDTO();
                                zeroArmy.setArmyId(0);
                                zeroArmy.setRegionId(brigade.getRegionId());
                                zeroArmy.setX(brigade.getX());
                                zeroArmy.setY(brigade.getY());
                                sectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).put(0, zeroArmy);
                            }

                            if (!sectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).get(0).getCorps().containsKey(0)) {
                                final CorpDTO zeroCorp = new CorpDTO();
                                zeroCorp.setArmyId(0);
                                zeroCorp.setRegionId(brigade.getRegionId());
                                zeroCorp.setX(brigade.getX());
                                zeroCorp.setY(brigade.getY());
                                sectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).get(0).getCorps().put(0, zeroCorp);
                            }

                            sectorArmiesMap.get(brigade.getRegionId()).get(armySector.getId()).get(0).getCorps().get(0).getBrigades().put(brigade.getBrigadeId(), brigade);
                        }
                    }
                }
            }
        }

    }

    public void initStartingSectorArmiesMap() {
        startingSectorArmiesMap.clear();
        for (ArmyDTO army : armiesMap.values()) {
            if (army.getArmyId() > 0) {
                if (!startingSectorArmiesMap.containsKey(army.getRegionId())) {
                    startingSectorArmiesMap.put(army.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                }

                final SectorDTO armySector = RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getXStart()][army.getYStart()];
                if (!startingSectorArmiesMap.get(army.getRegionId()).containsKey(armySector.getId())) {
                    startingSectorArmiesMap.get(army.getRegionId()).put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                }

                startingSectorArmiesMap.get(army.getRegionId()).get(armySector.getId()).put(army.getArmyId(), army);

            } else {
                for (CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() > 0) {
                        if (!startingSectorArmiesMap.containsKey(corp.getRegionId())) {
                            startingSectorArmiesMap.put(corp.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                        }

                        final SectorDTO armySector = RegionStore.getInstance().getRegionSectorsByRegionId(corp.getRegionId())[corp.getXStart()][corp.getYStart()];
                        if (!startingSectorArmiesMap.containsKey(armySector.getRegionId())) {
                            startingSectorArmiesMap.put(armySector.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                        }

                        if (!startingSectorArmiesMap.get(armySector.getRegionId()).containsKey(armySector.getId())) {
                            startingSectorArmiesMap.get(armySector.getRegionId()).put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                        }

                        if (!startingSectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).containsKey(0)) {
                            final ArmyDTO zeroArmy = new ArmyDTO();
                            zeroArmy.setArmyId(0);
                            zeroArmy.setRegionId(corp.getRegionId());
                            zeroArmy.setX(corp.getXStart());
                            zeroArmy.setY(corp.getYStart());
                            startingSectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).put(0, zeroArmy);
                        }

                        startingSectorArmiesMap.get(corp.getRegionId()).get(armySector.getId()).get(0).getCorps().put(corp.getCorpId(), corp);

                    } else {
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            if (!startingSectorArmiesMap.containsKey(brigade.getRegionId())) {
                                startingSectorArmiesMap.put(brigade.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                            }

                            final SectorDTO armySector = RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getXStart()][brigade.getYStart()];
                            if (!startingSectorArmiesMap.containsKey(armySector.getRegionId())) {
                                startingSectorArmiesMap.put(armySector.getRegionId(), new HashMap<Integer, Map<Integer, ArmyDTO>>());
                            }

                            if (!startingSectorArmiesMap.get(armySector.getRegionId()).containsKey(armySector.getId())) {
                                startingSectorArmiesMap.get(armySector.getRegionId()).put(armySector.getId(), new HashMap<Integer, ArmyDTO>());
                            }

                            if (!startingSectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).containsKey(0)) {
                                final ArmyDTO zeroArmy = new ArmyDTO();
                                zeroArmy.setArmyId(0);
                                zeroArmy.setRegionId(brigade.getRegionId());
                                zeroArmy.setX(brigade.getXStart());
                                zeroArmy.setY(brigade.getYStart());
                                startingSectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).put(0, zeroArmy);
                            }

                            if (!startingSectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).get(0).getCorps().containsKey(0)) {
                                final CorpDTO zeroCorp = new CorpDTO();
                                zeroCorp.setArmyId(0);
                                zeroCorp.setRegionId(brigade.getRegionId());
                                zeroCorp.setX(brigade.getXStart());
                                zeroCorp.setY(brigade.getYStart());
                                startingSectorArmiesMap.get(armySector.getRegionId()).get(armySector.getId()).get(0).getCorps().put(0, zeroCorp);
                            }

                            startingSectorArmiesMap.get(brigade.getRegionId()).get(armySector.getId()).get(0).getCorps().get(0).getBrigades().put(brigade.getBrigadeId(), brigade);
                        }
                    }
                }
            }
        }
    }

    public ArmyTypeDTO getArmyTypeById(final int id) {
        for (final ArmyTypeDTO type : armyTypesList) {
            if (type.getIntId() == id) {
                return type;
            }
        }
        return null;
    }

    // Method Used By the async callback to initialize  the army types
    public void initDbArmyTypes(final List<ArmyTypeDTO> armyTypesList) {
        try {
            this.armyTypesList = armyTypesList;
            for (ArmyTypeDTO type : armyTypesList) {
                armyTypesMap.put(type.getIntId(), type);
            }

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize army types due to unexpected reason", false);
        }
    }

    // Method Used By the async callback to initialize  the army types
    public void initNewBrigades(final Map<Integer, List<BrigadeDTO>> newBrigadesMap) {
        try {
            barrBrigMap = newBrigadesMap;
            isInitialized = true;

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize new brigades due to unexpected reason", false);
        }
    }

    //Method used by the async callback to store the merged battalions
    public void initMergedBattalions(final List<BattalionDTO> mergedBattalions) {
        try {
            mergedBatts = mergedBattalions;

        } catch (Exception ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to retrieve unused merged battalions", false);
        }
    }

    public Map<Integer, List<BrigadeDTO>> getBarrBrigMap() {
        return barrBrigMap;
    }

    /**
     * Method that adds a new battalion to a brigade
     *
     * @param brigadeId the id of the brigade
     * @param armyType  the DTO describing the type
     * @param slot      the slot where we insert the brigade
     * @param regionId  the region where the brigade is located.
     */
    public void addBattalionToBrigade(final int brigadeId, final ArmyTypeDTO armyType, final int slot, final int regionId) {
        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsArmy()) ? 2 : 1;

        final BrigadeDTO thisBrigade = ArmyStore.getInstance().getBrigadeById(brigadeId);
        final SectorDTO thisSector = RegionStore.getInstance().getSectorByPosition(thisBrigade);
        final int multiplier = doubleCosts * CostCalculators.getSphere(thisSector, DataStore.getInstance().getNationById(thisBrigade.getNationId()));

        final AddBattalionToBrigade abtb = new AddBattalionToBrigade(thisBrigade, armyType, slot);
        int[] ids = new int[3];
        ids[0] = brigadeId;
        ids[1] = armyType.getIntId();
        ids[2] = slot;
        if (OrderStore.getInstance().addNewOrder(ORDER_ADD_BATT, CostCalculators.getArmyTypeCost(armyType, multiplier), regionId, "", ids, 0, "") == 1) {
            abtb.execute(brigadeId);
            UnitEventManager.changeUnit(BRIGADE, brigadeId);
        }
    }

    /**
     * @param brigadeId the brigade id.
     * @param armyType  the army type id.
     * @param slot      the slot to remove.
     */
    public void removeNewBattFromBrigade(final int brigadeId, final int armyType, final int slot) {
        int[] ids = new int[3];
        ids[0] = brigadeId;
        ids[1] = armyType;
        ids[2] = slot;
        OrderStore.getInstance().removeOrder(ORDER_ADD_BATT, ids);
        for (BattalionDTO batt : getBrigadeById(brigadeId).getBattalions()) {
            if (batt.getOrder() == slot) {
                getBrigadeById(brigadeId).getBattalions().remove(batt);
                break;
            }
        }
    }

    /**
     * Undo renaming army command.
     *
     * @param armyId The army to undo the renaming.
     */
    public void undoRenameArmy(final int armyId) {
        int[] ids = new int[1];
        ids[0] = armyId;
        OrderStore.getInstance().removeOrder(ORDER_REN_ARMY, ids);
        final ArmyDTO army = getArmyById(armyId);
        if (army != null) {
            army.setName(army.getOriginalName());
        }
        UnitEventManager.changeUnit(ARMY, armyId);
    }

    /**
     * Rename army.
     *
     * @param armyId The army id.
     * @param name   The name to give.
     */
    public void renameArmy(final int armyId, final String name) {
        if (armyId != 0) {
            int[] ids = new int[1];
            ids[0] = armyId;
            //remove previous rename order if exist.
            undoRenameArmy(armyId);
            ArmyDTO army = getArmyById(armyId);
            if (!name.equals(army.getOriginalName())) {
                //add new rename order.
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_ARMY, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeArmyName(armyId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_ARMY, ids);
                }
                UnitEventManager.changeUnit(ARMY, armyId);
            }

        }
    }

    /**
     * Change the name for client view.
     *
     * @param armyId The army id.
     * @param name   The new name.
     */
    public void changeArmyName(final int armyId, final String name) {
        final ChangeArmyNameOrder caName = new ChangeArmyNameOrder(armiesMap, name);
        caName.execute(armyId);
        applyChangesToPanels();
    }

    /**
     * Undo rename corps command.
     *
     * @param corpId The army to undo the renaming.
     */
    public void undoRenameCorps(final int corpId) {
        int[] ids = new int[1];
        ids[0] = corpId;
        OrderStore.getInstance().removeOrder(ORDER_REN_CORP, ids);
        final CorpDTO corp = getCorpByID(corpId);
        if (corp != null) {
            corp.setName(corp.getOriginalName());
        }
        UnitEventManager.changeUnit(CORPS, corpId);
    }

    /**
     * Add the rename command for corp.
     *
     * @param corpId The corp to rename.
     * @param name   The new name to set.
     */
    public void renameCorps(final int corpId, final String name) {
        if (corpId != 0) {
            int[] ids = new int[1];
            ids[0] = corpId;
            undoRenameCorps(corpId);
            final CorpDTO corps = getCorpByID(corpId);
            if (!name.equals(corps.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_CORP, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeCorpName(corpId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_CORP, ids);
                }
                UnitEventManager.changeUnit(CORPS, corpId);
            }
        }
    }

    /**
     * Change corps name for client view.
     *
     * @param corpId The corps id.
     * @param name   The name to set.
     */
    public void changeCorpName(final int corpId, final String name) {
        for (ArmyDTO army : armiesMap.values()) {
            if (army.getCorps().containsKey(corpId)) {
                final ChangeCorpNameOrder ccName = new ChangeCorpNameOrder(army.getCorps(), name);
                ccName.execute(corpId);
                break;
            }
        }
    }

    /**
     * Remove brigade rename order.
     *
     * @param brigadeId The brigade to undo the rename.
     */
    public void undoRenameBrig(final int brigadeId) {
        int[] ids = new int[1];
        ids[0] = brigadeId;
        OrderStore.getInstance().removeOrder(ORDER_REN_BRIG, ids);
        BrigadeDTO brig = getBrigadeById(brigadeId);
        if (brig == null) {
            brig = getNewBrigadeById(brigadeId);
        }
        if (brig != null) {
            brig.setName(brig.getOriginalName());
        }
        UnitEventManager.changeUnit(BRIGADE, brigadeId);
    }

    /**
     * Add the rename brigade order.
     *
     * @param brigadeId The brigade to rename.
     * @param name      The new name to set.
     */
    public void renameBrigade(final int brigadeId, final String name) {
        if (brigadeId != 0) {
            final int[] ids = new int[1];
            ids[0] = brigadeId;
            undoRenameBrig(brigadeId);
            BrigadeDTO brigade = getBrigadeById(brigadeId);
            if (!name.equals(brigade.getOriginalName())) {
                if (OrderStore.getInstance().addNewOrder(ORDER_REN_BRIG, CostCalculators.getShipRenameCost(), 1, name, ids, 0, "") == 1) {
                    changeBrigadeName(brigadeId, name);
                    GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_REN_BRIG, ids);
                }
                UnitEventManager.changeUnit(BRIGADE, brigadeId);
            }
        }

    }

    /**
     * Change the name for client purposes.
     *
     * @param brigadeId The brigade id to change the name.
     * @param name      the name to set.
     */
    public void changeBrigadeName(final int brigadeId, final String name) {
        boolean found = false;
        for (ArmyDTO army : armiesMap.values()) {
            for (CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(brigadeId)) {
                    final ChangeBrigadeNameOrder cbName = new ChangeBrigadeNameOrder(corp.getBrigades(), name);
                    if (cbName.execute(brigadeId) == 1) {
                        found = true;
                    }
                    break;
                }
            }
        }
        //applyChangesToPanels();
        if (!found) {
            //if failed check if the rename is for a new brigade.
            for (List<BrigadeDTO> entries : barrBrigMap.values()) {
                for (BrigadeDTO newBrig : entries) {
                    if (newBrig.getBrigadeId() == brigadeId) {
                        newBrig.setName(name);
                    }
                }
            }
        }
    }

    // Create Army
    public ArmyDTO createArmy(final int armyId, final String name,
                              final int xPos, final int yPos, final int regionId) {
        final CreateArmyOrder caOrder = new CreateArmyOrder(armiesMap, name, xPos, yPos, regionId, GameStore.getInstance().getNationId());
        caOrder.execute(armyId);
        applyChangesToPanels();
        UnitEventManager.createUnit(ARMY, armyId);
        UnitEventManager.changeUnit(ARMY, armyId);
        EcoEventManager.changeSector(regionId);
        return armiesMap.get(armyId);
    }

    // Create Corp
    public CorpDTO createCorp(final int corpId, final int armyId, final String name,
                              final int xPos, final int yPos, final int regionId,
                              final int nationId, final boolean notifyCorp) {
        final CreateCorpOrder ccOrder = new CreateCorpOrder(armiesMap.get(armyId).getCorps(), name, xPos, yPos, regionId, nationId, armyId);
        ccOrder.execute(corpId);
        applyChangesToPanels();
        if (notifyCorp) {
            UnitEventManager.changeUnit(CORPS, corpId);
        }
        UnitEventManager.createUnit(CORPS, corpId);
        EcoEventManager.changeSector(regionId);
        return armiesMap.get(0).getCorps().get(corpId);
    }

    // Create Brigade
    public Map<Integer, List<BrigadeDTO>> createBrigade(final int sectorId, final BrigadeDTO newBrig) {
        if (!barrBrigMap.containsKey(sectorId)) {
            barrBrigMap.put(sectorId, new ArrayList<BrigadeDTO>());
        }

        int newId = 1;
        if (OrderStore.getInstance().getClientOrders().containsKey(ORDER_B_BATT)) {
            newId = OrderStore.getInstance().getClientOrders().get(ORDER_B_BATT).size() + 1;
        }

        final int[] ids = new int[2];
        ids[0] = sectorId;
        ids[1] = newId;
        //add it to the map so when the the mini order panel tries to find - something that happens inside the addNewOrder
        //method - it will work properly.
        newBrig.setBrigadeId(newId);
        barrBrigMap.get(sectorId).add(newBrig);

        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsArmy()) ? 2 : 1;

        //if it is client calculate multiplier here... else use the given one.        
        final SectorDTO sector = RegionStore.getInstance().getSectorByPosition(newBrig);
        final int multiplier = doubleCosts * CostCalculators.getSphere(sector, DataStore.getInstance().getNationById(GameStore.getInstance().getNationId()));

        try {
            if (OrderStore.getInstance().addNewOrder(ORDER_B_BATT, CostCalculators.getBrigadeCost(newBrig, multiplier), newBrig.getRegionId(), newBrig.getName(), ids, 0, "") != 1) {
                //if something went wrong... remove it.
                barrBrigMap.get(sectorId).remove(barrBrigMap.get(sectorId).size() - 1);
            }

        } catch (Exception e) {
            //also if something breaks... also undo the insertion.
            barrBrigMap.get(sectorId).remove(barrBrigMap.get(sectorId).size() - 1);
        }

        MapStore.getInstance().getUnitGroups().getRegionNewArmiesByRegionId(newBrig.getRegionId()).clear();
        final List<ArmyDTO> newArmiesList = new ArrayList<ArmyDTO>();
        newArmiesList.add(getRegionNewBrigadesAsArmy(newBrig.getRegionId()));
        MapStore.getInstance().getUnitGroups().getRegionNewArmiesByRegionId(newBrig.getRegionId()).initArmyImages(newArmiesList, false, false, true);

        EcoEventManager.changeSector(newBrig.getRegionId());

        return barrBrigMap;
    }

    // Cancel Create Brigade
    public Map<Integer, List<BrigadeDTO>> cancelBrigade(final int sectorId, final BrigadeDTO newBrig) {
        final int regionId = newBrig.getRegionId();
        if (barrBrigMap.containsKey(sectorId)) {
            final List<BrigadeDTO> sectorBrigades = barrBrigMap.get(sectorId);
            final Iterator<BrigadeDTO> brigIter = sectorBrigades.iterator();
            while (brigIter.hasNext()) {
                //if it is the one to cancel.. remove it and exit iteration
                if (newBrig.getBrigadeId() == brigIter.next().getBrigadeId()) {
                    brigIter.remove();
                    break;
                }
            }

            //update the new brigade id values...
            for (int curSector : barrBrigMap.keySet()) {
                for (BrigadeDTO brig : barrBrigMap.get(curSector)) {
                    if (brig.getBrigadeId() > newBrig.getBrigadeId()) {
                        brig.setBrigadeId(brig.getBrigadeId() - 1);
                    }
                }
            }

            final int[] ids = new int[2];
            ids[0] = sectorId;
            ids[1] = newBrig.getBrigadeId();
            OrderStore.getInstance().removeOrder(ORDER_B_BATT, ids);

            //also be sure to update the order id values...
            if (OrderStore.getInstance().getClientOrders().containsKey(ORDER_B_BATT)) {
                for (ClientOrderDTO order : OrderStore.getInstance().getClientOrders().get(ORDER_B_BATT)) {
                    if (order.getIdentifier(1) > newBrig.getBrigadeId()) {
                        order.setIdentifier(1, order.getIdentifier(1) - 1);
                    }
                }
            }
        }

        MapStore.getInstance().getUnitGroups().getRegionNewArmiesByRegionId(newBrig.getRegionId()).clear();
        final List<ArmyDTO> newArmiesList = new ArrayList<ArmyDTO>();
        newArmiesList.add(getRegionNewBrigadesAsArmy(newBrig.getRegionId()));
        MapStore.getInstance().getUnitGroups().getRegionNewArmiesByRegionId(newBrig.getRegionId()).initArmyImages(newArmiesList, false, false, true);
        EcoEventManager.changeSector(regionId);
        return barrBrigMap;
    }

    // Change Battalion Brigade
    public void changeBattalionBrigade(final int battalionId, final int newBrigadeId, final int newSlot, final boolean isCancel) {
        //check for conflicts
        final BattalionDTO battalion = getBattalionById(battalionId);
        final BrigadeDTO oldBrigade = getBrigadeById(battalion.getBrigadeId());
        if (oldBrigade.isUpgraded() || oldBrigade.isUpgradedToElite() || oldBrigade.IsUpHeadcount()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "The Brigade the battalion belongs has upgrades, because of this the battalion cannot move to other brigade", false);
            return;
        }

        final BrigadeDTO newBrigade = getBrigadeById(newBrigadeId);

        if (newBrigade.isUpgraded() || newBrigade.isUpgradedToElite() || newBrigade.IsUpHeadcount()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "The brigade you try to add the battalion has upgrades, because of this the battalion cannot move to this brigade unless you cancel the upgrades first", false);
            return;
        }


        //check if destination brigade has empty slot at the selected position.
        if (isCancel) {
            for (final BattalionDTO curBat : newBrigade.getBattalions()) {
                if (curBat.getOrder() == newSlot) {
                    //it means we have problem.
                    //first search if a new battalion has been added
                    final List<ClientOrderDTO> addBats = OrderStore.getInstance().getClientOrders().get(ORDER_ADD_BATT);
                    final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
                    if (addBats != null) {
                        for (ClientOrderDTO addBat : addBats) {
                            if (addBat.getIdentifier(1) == newBrigadeId &&
                                    addBat.getIdentifier(2) == newSlot) {
                                conflictOrders.add(addBat);
                            }
                        }
                    }

                    //secondly search if a battalion has been moved to this position
                    final List<ClientOrderDTO> movedBats = OrderStore.getInstance().getClientOrders().get(ORDER_ADDTO_BRIGADE);
                    boolean forceUndo = false;
                    if (movedBats != null) {
                        for (final ClientOrderDTO movedBat : movedBats) {
                            if (movedBat.getIdentifier(1) == newBrigadeId
                                    && movedBat.getIdentifier(2) == newSlot) {
                                conflictOrders.add(movedBat);
                                final BattalionDTO conflictBat = getBattalionById(movedBat.getIdentifier(0));
                                if (conflictBat.getStartBrigadeId() == battalion.getBrigadeId()) {
                                    forceUndo = true;
                                }
                            }
                        }
                    }

                    if (!conflictOrders.isEmpty()) {
                        if (forceUndo) {
                            try {
                                final BattalionDTO conflictBat = getBattalionById(conflictOrders.get(0).getIdentifier(0));
                                changeBattalionBrigade(battalionId, newBrigadeId, newSlot, false);
                                changeBattalionBrigade(conflictOrders.get(0).getIdentifier(0), conflictBat.getStartBrigadeId(), conflictBat.getStartOrder(), false);

                            } catch (Exception e) {

                            }

                        } else {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "The original position of this battalion is not empty because you added or moved a brigade to it. Review conflict orders?", true) {
                                public void onAccept() {
                                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                                    viewer.show();
                                    viewer.center();
                                }
                            };
                        }
                        return;
                    }

                    break;
                }
            }
        }


        //done checking for conflicts
        final ChangeBattalionBrigadeOrder cbbOrder = new ChangeBattalionBrigadeOrder(armiesMap, newBrigadeId, newSlot);
        final int ids[] = new int[3];

        //first remove any previous orders for this battalion
        ids[0] = battalionId;
        ids[1] = battalion.getBrigadeId();
        ids[2] = battalion.getOrder();
        OrderStore.getInstance().removeOrder(ORDER_ADDTO_BRIGADE, ids);

        //then add the new order
        ids[1] = newBrigadeId;
        ids[2] = newSlot;
        if (newBrigadeId != battalion.getStartBrigadeId()) {
            OrderStore.getInstance().addNewOrder(ORDER_ADDTO_BRIGADE, new OrderCostDTO(), 1, "", ids, 0, "");
        }

        cbbOrder.execute(battalionId);
        UnitEventManager.changeUnit(BRIGADE, newBrigadeId);
        applyChangesToPanels();
    }

    // Change Brigade Corp
    public void changeBrigadeCorp(final int brigadeId, final int oldCorpId, final int newCorpId, final boolean notifyCorps) {

        //check for conflicts
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        final List<ClientOrderDTO> movementOrders = OrderStore.getInstance().getMovementOrders();
        if (oldCorpId != 0) {
            final CorpDTO oldCorp = getCorpByID(oldCorpId);
            if (oldCorp.isUpgraded() || oldCorp.isUpgradedToElite() || oldCorp.isUpHeadcount()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The corp the brigade belongs has upgrades, because of this the brigade cannot move to other corp or set free", false);
                return;
            }

            //check if corps has been moved!
            if (movementOrders != null) {
                for (ClientOrderDTO order : movementOrders) {
                    if (order.getIdentifier(0) == CORPS
                            && order.getIdentifier(1) == oldCorpId) {
                        conflictOrders.add(order);
                    }
                }
            }
            if (oldCorp.getArmyId() > 0) {
                //check if army has been moved...
                for (ClientOrderDTO order : movementOrders) {
                    if (order.getIdentifier(0) == ARMY
                            && order.getIdentifier(1) == oldCorp.getArmyId()) {
                        conflictOrders.add(order);
                    }
                }
            }

        }

        if (newCorpId != 0) {
            final CorpDTO newCorp = getCorpByID(newCorpId);
            //check if new corps is full
            if (newCorp != null && newCorp.getBrigades().size() >= MiscCalculators.getMaxBrigadesInCorps()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The corps is full, to perform this action remove a brigade from the corps and try again.", false);
                return;
            }

            if (newCorp != null) {
                if (newCorp.isUpgraded() || newCorp.isUpgradedToElite() || newCorp.isUpHeadcount()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The corp you try to add the brigade has upgrades, because of this the brigade cannot move to this corp unless you cancel the upgrades first", false);
                    return;
                }

                //check if corps has been moved!
                if (movementOrders != null) {
                    for (ClientOrderDTO order : movementOrders) {
                        if (order.getIdentifier(0) == CORPS
                                && order.getIdentifier(1) == newCorpId) {
                            conflictOrders.add(order);
                        }
                    }
                }

                if (newCorp.getArmyId() > 0) {
                    //check if army has been moved...
                    for (ClientOrderDTO order : movementOrders) {
                        if (order.getIdentifier(0) == ARMY
                                && order.getIdentifier(1) == newCorp.getArmyId()) {
                            conflictOrders.add(order);
                        }
                    }
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot add/remove a brigade to/from a corps if the corps or the army the corps belong has been moved. Review conflict orders?", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return;
        }


        //done  checking for conflicts

        final int[] ids = new int[2];
        ids[0] = brigadeId;
        ids[1] = oldCorpId;

        final BrigadeDTO brig = getBrigadeById(brigadeId);
        if (brig == null) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to add brigade to corp!", false);
        } else {
            try {

                OrderStore.getInstance().removeOrder(ORDER_ADDTO_CORP, ids);
                if (newCorpId != brig.getStartCorp()) {
                    ids[1] = newCorpId;
                    OrderStore.getInstance().addNewOrder(ORDER_ADDTO_CORP, new OrderCostDTO(), 1, "", ids, 0, "");
                }
                CorpDTO newCorp = getCorpByID(newCorpId);
                if (newCorp == null) { //if it is a deleted corp, recover it
                    newCorp = idToDeletedCorp.remove(newCorpId);
                    if (newCorp != null) {
                        armiesMap.get(newCorp.getArmyId()).getCorps().put(newCorpId, newCorp);
                    }
                }
                final ChangeBrigadeCorpOrder cbcOrder = new ChangeBrigadeCorpOrder(armiesMap, newCorpId);
                cbcOrder.execute(brigadeId);
                applyChangesToPanels();
                if (notifyCorps) {
                    UnitEventManager.changeUnit(CORPS, newCorpId);
                    UnitEventManager.changeUnit(CORPS, oldCorpId);
                }
                ClientUtil.startSpeedTest("uniteventmanagerchangebrigade");
                UnitEventManager.changeUnit(BRIGADE, brigadeId);
                ClientUtil.stopSpeedTest("uniteventmanagerchangebrigade", "cbc");
            } catch (Exception ex) {

            }
        }

        //if army has left with no corps... delete it
        if (oldCorpId != 0) {
            final CorpDTO oldCorp = getCorpByID(oldCorpId);
            if (oldCorp.getBrigades().isEmpty()) {
                if (oldCorp.getCommander() != null && oldCorp.getCommander().getId() > 0) {//first remove commander.
                    CommanderStore.getInstance().commanderLeaveFederation(oldCorp.getCommander().getId());
                }

                deleteCorp(oldCorpId, oldCorp.getArmyId());
            }
        }
    }

    // Change Corp Army
    public void changeCorpArmy(final int corpId, final int oldArmyId, final int armyId) {
        //check for conflicts
        //check if corps has been deleted, if yes it can't be re-added to any army
        if (isCorpDeleted(corpId)) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot add this corps to the army because the corps has been deleted. To add it you have to recreate it by undoing at least one \"Set brigade free...\" order for this corps", false);
            return;
        }
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        final List<ClientOrderDTO> movementOrders = OrderStore.getInstance().getMovementOrders();
        if (oldArmyId != 0) {

            final ArmyDTO oldArmy = armiesMap.get(oldArmyId);
            if (oldArmy.isUpgraded() || oldArmy.isUpgradedToElite() || oldArmy.isUpHeadcount()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The army the corps belongs has upgrades, because of this the corps cannot move to other army", false);
                return;
            }

            //check if corps has been moved!
            if (movementOrders != null) {
                for (ClientOrderDTO order : movementOrders) {
                    if (order.getIdentifier(0) == ARMY
                            && order.getIdentifier(1) == oldArmyId) {
                        conflictOrders.add(order);
                    }
                }
            }

        }
        if (armyId != 0) {
            final ArmyDTO newArmy = armiesMap.get(armyId);

            if (newArmy != null && newArmy.getCorps().size() >= MiscCalculators.getMaxCorpsInArmy()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The army is full, to perform this action remove a corps from the army and try again.", false);
                return;
            }

            if (newArmy != null) {
                if (newArmy.isUpgraded() || newArmy.isUpgradedToElite() || newArmy.isUpHeadcount()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The army you try to add the corps has upgrades, because of this the corps cannot move to this army unless you cancel the upgrades first", false);
                    return;
                }

                //check if corps has been moved!
                if (movementOrders != null) {
                    for (ClientOrderDTO order : movementOrders) {
                        if (order.getIdentifier(0) == ARMY
                                && order.getIdentifier(1) == armyId) {
                            conflictOrders.add(order);
                        }
                    }
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot add/remove a corps to/from an army that has been moved. Review conflict orders?", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return;
        }


        //done  checking for conflicts

        final int[] ids = new int[2];
        ids[0] = corpId;
        ids[1] = oldArmyId;

        final ArmyDTO newArmy = armiesMap.get(armyId);
        if (newArmy == null) {//if it is a deleted army, recover it
            armiesMap.put(armyId, idToDeletedArmy.remove(armyId));
        }
        OrderStore.getInstance().removeOrder(ORDER_ADDTO_ARMY, ids);
        if (armyId != getCorpByID(corpId).getStartArmy()) {
            ids[1] = armyId;
            OrderStore.getInstance().addNewOrder(ORDER_ADDTO_ARMY, new OrderCostDTO(), 1, "", ids, 0, "");
        }
        final ChangeCorpArmyOrder cbcOrder = new ChangeCorpArmyOrder(armiesMap, armyId);
        cbcOrder.execute(corpId);
        applyChangesToPanels();
        UnitEventManager.changeUnit(ARMY, oldArmyId);
        UnitEventManager.changeUnit(ARMY, armyId);
        UnitEventManager.changeUnit(CORPS, corpId);
        //if army has left with no corps... delete it
        if (oldArmyId != 0) {
            final ArmyDTO oldArmy = armiesMap.get(oldArmyId);
            if (oldArmy.getCorps().isEmpty()) {
                if (oldArmy.getCommander() != null && oldArmy.getCommander().getId() > 0) {//first remove commander.
                    CommanderStore.getInstance().commanderLeaveFederation(oldArmy.getCommander().getId());
                }
                deleteArmy(oldArmyId);//then delete army.
            }
        }
    }

    /**
     * Method that merges two battalions
     *
     * @param id  of the first battalion
     * @param id2 of the second battalion
     */
    public void mergeBattalions(final int id, final int id2) {
        int[] ids = new int[2];
        ids[0] = id;
        ids[1] = id2;
        if (OrderStore.getInstance().addNewOrder(ORDER_MRG_BATT, new OrderCostDTO(), MapStore.getInstance().getActiveRegion(), "", ids, 0, "") == 1) {
            final MergeBattalionsOrder mbOrder = new MergeBattalionsOrder(armiesMap, mergedBatts, id2);
            mbOrder.execute(id);

            final BrigadeDTO brigade = getBrigadeById(getBattalionById(id).getBrigadeId());
            final CorpDTO corp = getCorpByID(brigade.getCorpId());

            final BrigadeDTO brigade2 = getBrigadeById(getBattalionById(id2).getBrigadeId());
            final CorpDTO corp2 = getCorpByID(brigade2.getCorpId());

            UnitEventManager.changeUnit(BATTALION, id);
            UnitEventManager.changeUnit(BATTALION, id2);
            UnitEventManager.changeUnit(BRIGADE, brigade.getBrigadeId());
            UnitEventManager.changeUnit(CORPS, corp.getCorpId());
            UnitEventManager.changeUnit(ARMY, corp.getArmyId());
            UnitEventManager.changeUnit(BRIGADE, brigade2.getBrigadeId());
            UnitEventManager.changeUnit(CORPS, corp2.getCorpId());
            UnitEventManager.changeUnit(ARMY, corp2.getArmyId());
            applyChangesToPanels();
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Order not functioning.", false);
        }

    }

    public void undoMerge(final int id, final int id2) {
        final UndoMerge umOrder = new UndoMerge(mergedBatts, id2);
        if (umOrder.execute(id) == 1) {
            int[] ids = new int[2];
            ids[0] = id;
            ids[1] = id2;
            OrderStore.getInstance().removeOrder(ORDER_MRG_BATT, ids);
        }

        final BrigadeDTO brigade = getBrigadeById(getBattalionById(id).getBrigadeId());
        final CorpDTO corp = getCorpByID(brigade.getCorpId());

        final BrigadeDTO brigade2 = getBrigadeById(getBattalionById(id2).getBrigadeId());
        final CorpDTO corp2 = getCorpByID(brigade2.getCorpId());

        UnitEventManager.changeUnit(BATTALION, id);
        UnitEventManager.changeUnit(BATTALION, id2);
        UnitEventManager.changeUnit(BRIGADE, brigade.getBrigadeId());
        UnitEventManager.changeUnit(CORPS, corp.getCorpId());
        UnitEventManager.changeUnit(ARMY, corp.getArmyId());
        UnitEventManager.changeUnit(BRIGADE, brigade2.getBrigadeId());
        UnitEventManager.changeUnit(CORPS, corp2.getCorpId());
        UnitEventManager.changeUnit(ARMY, corp2.getArmyId());
        applyChangesToPanels();
    }

    // Delete an Army
    public void deleteArmy(final int armyId) {
        final ArmyDTO army = armiesMap.get(armyId);
        if (army == null) {
//            Window.alert("Unable to locate army.");

        } else if (!army.getCorps().values().isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "The army is not empty.", false);
        } else {
            int[] ids = new int[2];
            ids[0] = armyId;
            ids[1] = 0;
            OrderStore.getInstance().removeOrder(ORDER_B_ARMY, ids);
            final CommanderDTO comm = armiesMap.get(armyId).getCommander();
            final DeleteArmyOrder daOrder = new DeleteArmyOrder(armiesMap, armyId, idToDeletedArmy);
            daOrder.execute(armyId);
            applyChangesToPanels();
            UnitEventManager.destroyUnit(ARMY, armyId);
            UnitEventManager.changeUnit(COMMANDER, comm.getId());
            EcoEventManager.changeSector(army.getRegionId());
        }
    }

    // Delete a Corp
    public void deleteCorp(final int corpId, final int armyId) {
        final CorpDTO corp = getCorpByID(corpId);
        if (corp == null) {
            return;
//            Window.alert("Unable to locate corps.");
        } else if (!(corp.getBrigades().values().size() > 0)) {
            int[] ids = new int[2];
            ids[0] = corpId;
            ids[1] = 0;
            OrderStore.getInstance().removeOrder(ORDER_B_CORP, ids);
            final DeleteCorpOrder dcOrder = new DeleteCorpOrder(armiesMap.get(armyId).getCorps(), idToDeletedCorp);
            dcOrder.execute(corpId);
            applyChangesToPanels();
            UnitEventManager.destroyUnit(CORPS, corpId);
            EcoEventManager.changeSector(corp.getRegionId());
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "The corps is not empty", false);
        }

    }

    // Upgrade a brigade
    public boolean upgradeBrigade(final int brigadeId, final boolean toCrackElite, final SectorDTO sector) {
        final OrderCostDTO cost;
        final BrigadeDTO brigade = ArmyStore.getInstance().getBrigadeById(brigadeId);

        //check for conflicts
        if (brigade.getLoaded()) {
            return false;
        }
        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty()
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FH
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FL
                && TradeCityStore.getInstance().getTradeCityByPosition(sector) == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot train troops.", false);
            return false;
        }

        final List<ClientOrderDTO> corpsUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP_CORPS);
        final List<ClientOrderDTO> armyUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP_ARMY);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (corpsUpgrades != null) {
            for (ClientOrderDTO order : corpsUpgrades) {
                if (brigade.getCorpId() == order.getIdentifier(0) &&
                        ((order.getIdentifier(1) == 1 && toCrackElite) || (order.getIdentifier(1) == 0 && !toCrackElite))) {
                    conflictOrders.add(order);

                }
            }
        }

        if (brigade.getCorpId() > 0) {
            final CorpDTO corp = getCorpByID(brigade.getCorpId());
            if (armyUpgrades != null) {
                for (ClientOrderDTO order : armyUpgrades) {
                    if (corp.getArmyId() == order.getIdentifier(0) &&
                            ((order.getIdentifier(1) == 1 && toCrackElite) || (order.getIdentifier(1) == 0 && !toCrackElite))) {
                        conflictOrders.add(order);

                    }
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot upgrade brigade if the corps or the army it belongs has same upgrades. Review conflict orders?", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return false;
        }

        if (toCrackElite) {
            cost = CostCalculators.getCrackEliteBrigadeCost(brigade);

        } else {
            cost = CostCalculators.getTrainBrigadeCost(brigade);
        }

        final int[] ids = new int[3];
        ids[0] = brigadeId;
        if (toCrackElite) {
            ids[1] = 1;
        } else {
            ids[1] = 0;
        }
        ids[2] = sector.getRegionId();

        if (OrderStore.getInstance().addNewOrder(ORDER_INC_EXP, cost, brigade.getRegionId(), "", ids, 0, "") == 1) {
            upgradeBrigadeExecute(brigadeId, toCrackElite);
            UnitEventManager.changeUnit(BRIGADE, brigadeId);
            return true;
        }

        return false;
    }

    public boolean upgradeCorps(final int corpId, final boolean toCrackElite, final SectorDTO sector) {
        final OrderCostDTO cost;
        final CorpDTO corp = getCorpByID(corpId);
        //check for conflicts
        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty()
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FH
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FL
                && TradeCityStore.getInstance().getTradeCityByPosition(sector) == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot train troops.", false);
            return false;
        }

        final List<ClientOrderDTO> brigadeUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP);
        final List<ClientOrderDTO> armyUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP_ARMY);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();

        if (brigadeUpgrades != null) {
            for (ClientOrderDTO order : brigadeUpgrades) {
                if (corp.getBrigades().containsKey(order.getIdentifier(0)) &&
                        ((order.getIdentifier(1) == 1 && toCrackElite) || (order.getIdentifier(1) == 0 && !toCrackElite))) {
                    conflictOrders.add(order);

                }
            }
        }

        if (armyUpgrades != null) {
            for (ClientOrderDTO order : armyUpgrades) {
                if (corp.getArmyId() == order.getIdentifier(0) &&
                        ((order.getIdentifier(1) == 1 && toCrackElite) || (order.getIdentifier(1) == 0 && !toCrackElite))) {
                    conflictOrders.add(order);

                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot upgrade a corps that you already have upgrade the army it belongs or one of its brigades." +
                    " Review conflict orders?", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };

            return false;

        }

        if (toCrackElite) {
            cost = CostCalculators.getCrackEliteCorpsCost(corp);
        } else {
            cost = CostCalculators.getTrainCorpsCost(corp);
        }

        int[] ids = new int[3];
        ids[0] = corpId;
        if (toCrackElite) {
            ids[1] = 1;

        } else {
            ids[1] = 0;
        }

        ids[2] = sector.getRegionId();
        if (OrderStore.getInstance().addNewOrder(ORDER_INC_EXP_CORPS, cost, corp.getRegionId(), "", ids, 0, "") == 1) {
            //then for each brigade in the corps upgrade their parameters...
            //no need to make a copy function for corps and armies since it can be done like that
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                if (canBrigadeUpgrade(brigade, toCrackElite)) {
                    upgradeBrigadeExecute(brigade.getBrigadeId(), toCrackElite);
                }
            }

            if (toCrackElite) {
                corp.setUpgradedToElite(true);
            } else {
                corp.setUpgraded(true);
            }
            UnitEventManager.changeUnit(CORPS, corpId);
            return true;
        }

        return false;
    }

    public boolean upgradeArmy(final int armyId, final boolean toCrackElite, final SectorDTO sector) {
        final OrderCostDTO cost;
        final ArmyDTO army = armiesMap.get(armyId);

        //check for conflicts

        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty()
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FH
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FL
                && TradeCityStore.getInstance().getTradeCityByPosition(sector) == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot upgrade troops.", false);
            return false;
        }

        final List<ClientOrderDTO> brigadeUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP);
        final List<ClientOrderDTO> corpUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP_CORPS);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();

        if (brigadeUpgrades != null) {
            for (ClientOrderDTO order : brigadeUpgrades) {
                for (CorpDTO corps : army.getCorps().values()) {
                    if (corps.getBrigades().containsKey(order.getIdentifier(0)) &&
                            ((order.getIdentifier(1) == 1 && toCrackElite) || (order.getIdentifier(1) == 0 && !toCrackElite))) {
                        conflictOrders.add(order);
                    }
                }
            }
        }

        if (corpUpgrades != null) {
            for (ClientOrderDTO order : corpUpgrades) {
                if (army.getCorps().containsKey(order.getIdentifier(0)) &&
                        ((order.getIdentifier(1) == 1 && toCrackElite) || (order.getIdentifier(1) == 0 && !toCrackElite))) {
                    conflictOrders.add(order);
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot upgrade an army that you already have upgraded one of its corps or brigades. Review conflict orders?", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return false;
        }


        //done checking for conflicts

        if (toCrackElite) {
            cost = CostCalculators.getCrackEliteArmyCost(army);

        } else {
            cost = CostCalculators.getTrainArmyCost(army);
        }
//        Window.alert("cost : " + cost.convertToString());

        int[] ids = new int[3];
        ids[0] = armyId;
        if (toCrackElite) {
            ids[1] = 1;
        } else {
            ids[1] = 0;
        }
        ids[2] = sector.getRegionId();

        if (OrderStore.getInstance().addNewOrder(ORDER_INC_EXP_ARMY, cost, army.getRegionId(), "", ids, 0, "") == 1) {
            //then for each brigade in the corps upgrade their parameters...
            //no need to make a copy function for corps and armies since it can be done like that
            for (CorpDTO corp : army.getCorps().values()) {
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    if (canBrigadeUpgrade(brigade, toCrackElite)) {
                        upgradeBrigadeExecute(brigade.getBrigadeId(), toCrackElite);
                    }
                }

                if (toCrackElite) {
                    corp.setUpgradedToElite(true);

                } else {
                    corp.setUpgraded(true);
                }
            }

            if (toCrackElite) {
                army.setUpgradedToElite(true);
            } else {
                army.setUpgraded(true);
            }
            UnitEventManager.changeUnit(ARMY, armyId);
            return true;
        }
        return false;
    }

    public void upgradeBrigadeExecute(final int brigadeId, final boolean toCrackElite) {
        final UpgradeBrigadeOrder ubOrder = new UpgradeBrigadeOrder(armiesMap, toCrackElite, armyTypesMap);
        ubOrder.execute(brigadeId);
    }

    public void cancelUpgradeBrigade(final int brigadeId, final boolean toCrackElite, final int regionId, final boolean onlyOrder) {
        final int[] ids = new int[3];
        ids[0] = brigadeId;
        if (toCrackElite) {
            ids[1] = 1;
        } else {
            ids[1] = 0;
        }
        ids[2] = regionId;

        if (OrderStore.getInstance().removeOrder(ORDER_INC_EXP, ids)) {
            if (!onlyOrder) {
                final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(brigadeId);

                if (toCrackElite) {
                    brig.setIsUpgradedToElite(false);
                    for (final BattalionDTO battalion : brig.getBattalions()) {
                        battalion.setEmpireArmyType(battalion.getOriginalArmyType());
                    }
                } else {
                    brig.setIsUpgraded(false);
                    for (final BattalionDTO battalion : brig.getBattalions()) {
                        if (battalion.getId() != 0) {
                            battalion.setExperience(battalion.getOriginalExperience());
                        }
                    }
                }
            }
            UnitEventManager.changeUnit(BRIGADE, brigadeId);
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, FAILED_REMOVE_ORDER, false);

        }
    }

    public void cancelUpgradeCorps(final int corpId, final boolean toCrackElite, final int regionId, final boolean onlyOrder) {
        final int[] ids = new int[3];
        ids[0] = corpId;
        if (toCrackElite) {
            ids[1] = 1;
        } else {
            ids[1] = 0;
        }
        ids[2] = regionId;

        if (OrderStore.getInstance().removeOrder(ORDER_INC_EXP_CORPS, ids)) {
            final CorpDTO corp = ArmyStore.getInstance().getCorpByID(corpId);

            if (!onlyOrder) {
                for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                    if (toCrackElite) {
                        brigade.setIsUpgradedToElite(false);
                        for (final BattalionDTO battalion : brigade.getBattalions()) {
                            battalion.setEmpireArmyType(battalion.getOriginalArmyType());
                        }
                    } else {
                        brigade.setIsUpgraded(false);
                        for (final BattalionDTO battalion : brigade.getBattalions()) {
                            if (battalion.getId() != 0) {
                                battalion.setExperience(battalion.getOriginalExperience());
                            }
                        }
                    }
                }
            }
            if (toCrackElite) {
                corp.setUpgradedToElite(false);
            } else {
                corp.setUpgraded(false);
            }

            UnitEventManager.changeUnit(CORPS, corpId);
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, FAILED_REMOVE_ORDER, false);

        }
    }

    public void cancelUpgradeArmy(final int armyId, final boolean toCrackElite, final int regionId, final boolean onlyOrder) {
        final int[] ids = new int[3];
        ids[0] = armyId;
        if (toCrackElite) {
            ids[1] = 1;
        } else {
            ids[1] = 0;
        }
        ids[2] = regionId;

        if (OrderStore.getInstance().removeOrder(ORDER_INC_EXP_ARMY, ids)) {
            final ArmyDTO army = armiesMap.get(armyId);
            for (CorpDTO corps : army.getCorps().values()) {
                if (!onlyOrder) {
                    for (BrigadeDTO brigade : corps.getBrigades().values()) {
                        if (toCrackElite) {
                            brigade.setIsUpgradedToElite(false);
                            for (final BattalionDTO battalion : brigade.getBattalions()) {
                                battalion.setEmpireArmyType(battalion.getOriginalArmyType());
                            }
                        } else {
                            brigade.setIsUpgraded(false);
                            for (final BattalionDTO battalion : brigade.getBattalions()) {
                                if (battalion.getId() != 0) {
                                    battalion.setExperience(battalion.getOriginalExperience());
                                }
                            }
                        }
                    }
                }

                if (toCrackElite) {
                    corps.setUpgradedToElite(false);
                } else {
                    corps.setUpgraded(false);
                }
            }

            if (toCrackElite) {
                army.setUpgradedToElite(false);
            } else {
                army.setUpgraded(false);
            }
            UnitEventManager.changeUnit(ARMY, armyId);
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, FAILED_REMOVE_ORDER, false);

        }
    }

    // Up head-count of a brigade
    public boolean upHeadCountBrigade(final int brigadeId, final int count, final SectorDTO sector) {
        final BrigadeDTO brigade = ArmyStore.getInstance().getBrigadeById(brigadeId);
        final OrderCostDTO cost = CostCalculators.getIncreaseHeadcountCost(brigade, count);
        //check for conflicts
        if (brigade.getLoaded()) {
            return false;
        }

        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty()
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FH
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FL
                && TradeCityStore.getInstance().getTradeCityByPosition(sector) == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot increase troops headcount.", false);
            return false;
        }

        final List<ClientOrderDTO> corpsUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT_CORPS);
        final List<ClientOrderDTO> armyUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT_ARMY);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (corpsUpgrades != null) {
            for (ClientOrderDTO order : corpsUpgrades) {
                if (brigade.getCorpId() == order.getIdentifier(0)) {
                    conflictOrders.add(order);
                }
            }
        }

        if (brigade.getCorpId() > 0) {
            final CorpDTO corp = getCorpByID(brigade.getCorpId());
            if (armyUpgrades != null) {
                for (ClientOrderDTO order : armyUpgrades) {
                    if (corp.getArmyId() == order.getIdentifier(0)) {
                        conflictOrders.add(order);
                    }
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot increase headcount for brigade that you already have increase the corps or army it belongs", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return false;
        }
        //done checking for conflicts

        final int[] ids = new int[3];
        ids[0] = brigadeId;
        ids[1] = count;
        ids[2] = sector.getRegionId();
        if (OrderStore.getInstance().addNewOrder(ORDER_INC_HEADCNT, cost, brigade.getRegionId(), "", ids, 0, "") == 1) {
            upHeadCountExecute(brigadeId, count);
            UnitEventManager.changeUnit(BRIGADE, brigadeId);
            reAddUpgradeOrders(brigade);
            if (brigade.getCorpId() > 0) {
                final CorpDTO corps = getCorpByID(brigade.getCorpId());
                reAddUpgradeOrders(corps);
                if (corps.getArmyId() > 0) {
                    final ArmyDTO army = getArmyById(corps.getArmyId());
                    reAddUpgradeOrders(army);
                }
            }
            return true;
        }

        return false;
    }

    public void reAddUpgradeOrders(final BrigadeDTO brigade) {
        if (brigade.isUpgraded()) {
            final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_INC_EXP, brigade.getBrigadeId(), 0);
            if (order != null) {
                cancelUpgradeBrigade(brigade.getBrigadeId(), false, brigade.getRegionId(), false);
                if (!upgradeBrigade(brigade.getBrigadeId(), false, RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getXStart()][brigade.getYStart()])) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Training for the brigade " + brigade.getName() + "  has been canceled since after increasing headcount there are not enough goods in warehouse to train", false);
                }
            }
        }
        if (brigade.isUpgradedToElite()) {
            final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_INC_EXP, brigade.getBrigadeId(), 1);
            if (order != null) {
                cancelUpgradeBrigade(brigade.getBrigadeId(), true, brigade.getRegionId(), false);
                if (!upgradeBrigade(brigade.getBrigadeId(), true, RegionStore.getInstance().getRegionSectorsByRegionId(brigade.getRegionId())[brigade.getXStart()][brigade.getYStart()])) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Training for the brigade " + brigade.getName() + "  has been canceled since after increasing headcount there are not enough goods in warehouse to train", false);
                }
            }
        }
    }

    public void reAddUpgradeOrders(final CorpDTO corps) {
        if (corps.isUpgraded()) {
            final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_INC_EXP_CORPS, corps.getCorpId(), 0);
            if (order != null) {
                cancelUpgradeCorps(corps.getCorpId(), false, corps.getRegionId(), false);
                if (!upgradeCorps(corps.getCorpId(), false, RegionStore.getInstance().getRegionSectorsByRegionId(corps.getRegionId())[corps.getXStart()][corps.getYStart()])) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Training for the corps " + corps.getName() + "  has been canceled since after increasing headcount there are not enough goods in warehouse to train", false);
                }
            }
        }
        if (corps.isUpgradedToElite()) {
            final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_INC_EXP_CORPS, corps.getCorpId(), 1);
            if (order != null) {
                cancelUpgradeCorps(corps.getCorpId(), true, corps.getRegionId(), false);
                if (!upgradeCorps(corps.getCorpId(), true, RegionStore.getInstance().getRegionSectorsByRegionId(corps.getRegionId())[corps.getXStart()][corps.getYStart()])) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Training for the army " + corps.getName() + "  has been canceled since after increasing headcount there are not enough goods in warehouse to train", false);
                }
            }
        }
    }

    public void reAddUpgradeOrders(final ArmyDTO army) {
        if (army.isUpgraded()) {
            final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_INC_EXP_ARMY, army.getArmyId(), 0);
            if (order != null) {
                cancelUpgradeArmy(army.getArmyId(), false, army.getRegionId(), false);
                if (!upgradeArmy(army.getArmyId(), false, RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getXStart()][army.getYStart()])) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Training for the army " + army.getName() + "  has been canceled since after increasing headcount there are not enough goods in warehouse to train", false);
                }
            }
        }
        if (army.isUpgradedToElite()) {
            final ClientOrderDTO order = OrderStore.getInstance().getOrderByTypeIds(ORDER_INC_EXP_ARMY, army.getArmyId(), 1);
            if (order != null) {
                cancelUpgradeArmy(army.getArmyId(), true, army.getRegionId(), false);
                if (!upgradeArmy(army.getArmyId(), true, RegionStore.getInstance().getRegionSectorsByRegionId(army.getRegionId())[army.getXStart()][army.getYStart()])) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Training for the army " + army.getName() + "  has been canceled since after increasing headcount there are not enough goods in warehouse to train", false);
                }
            }

        }
    }

    public boolean upHeadCountCorps(final int corpsId, final int count, final SectorDTO sector) {
        final CorpDTO corps = ArmyStore.getInstance().getCorpByID(corpsId);

        //check for conflicts

        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty()
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FH
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FL
                && TradeCityStore.getInstance().getTradeCityByPosition(sector) == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot increase troops headcount.", false);
            return false;
        }

        final List<ClientOrderDTO> brigadeUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT);
        final List<ClientOrderDTO> armyUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT_ARMY);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();

        if (brigadeUpgrades != null) {
            for (ClientOrderDTO order : brigadeUpgrades) {
                if (corps.getBrigades().containsKey(order.getIdentifier(0))) {
                    conflictOrders.add(order);
                }
            }
        }

        if (armyUpgrades != null) {
            for (ClientOrderDTO order : armyUpgrades) {
                if (corps.getArmyId() == order.getIdentifier(0)) {
                    conflictOrders.add(order);
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot increase headcount for corps that you already increased the army it belongs or one of its battalions." +
                    " Review conflict orders?", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return false;
        }
        //done checking for orders

        final OrderCostDTO cost;

        cost = CostCalculators.getIncreaseHeadcountCorpsCost(corps, count);


        final int[] ids = new int[3];
        ids[0] = corpsId;
        ids[1] = count;
        ids[2] = sector.getRegionId();

        if (OrderStore.getInstance().addNewOrder(ORDER_INC_HEADCNT_CORPS, cost, corps.getRegionId(), "", ids, 0, "") == 1) {
            for (BrigadeDTO brigade : corps.getBrigades().values()) {
                upHeadCountExecute(brigade.getBrigadeId(), count);
            }
            corps.setUpHeadcount(true);
            UnitEventManager.changeUnit(CORPS, corpsId);
            reAddUpgradeOrders(corps);
            if (corps.getArmyId() > 0) {
                reAddUpgradeOrders(getArmyById(corps.getArmyId()));
            }
            return true;
        }

        return false;
    }

    public boolean upHeadCountArmy(final int armyId, final int count, final SectorDTO sector) {
        final ArmyDTO army = armiesMap.get(armyId);

        //check for conflicts

        final List<ArmyDTO> foreignArmies = ForeignUnitsStore.getInstance().getArmiesBySectorRelation(sector, REL_TRADE, REL_COLONIAL_WAR, REL_WAR);
        if (foreignArmies != null && !foreignArmies.isEmpty()
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FH
                && sector.getProductionSiteDTO().getId() != PS_BARRACKS_FL
                && TradeCityStore.getInstance().getTradeCityByPosition(sector) == null) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Foreign forces appear to be in this tile, cannot increase troops headcount.", false);
            return false;
        }

        final List<ClientOrderDTO> brigadeUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT);
        final List<ClientOrderDTO> corpUpgrades = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT_CORPS);
        final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
        if (brigadeUpgrades != null) {
            for (ClientOrderDTO order : brigadeUpgrades) {
                for (CorpDTO corps : army.getCorps().values()) {
                    if (corps.getBrigades().containsKey(order.getIdentifier(0))) {
                        conflictOrders.add(order);
                    }
                }
            }
        }

        if (corpUpgrades != null) {
            for (ClientOrderDTO order : corpUpgrades) {
                if (army.getCorps().containsKey(order.getIdentifier(0))) {
                    conflictOrders.add(order);
                }
            }
        }

        if (!conflictOrders.isEmpty()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot increase headcount for army that you already have increase one of its corps or brigades", true) {
                public void onAccept() {
                    final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, CONFLICT_ORDERS_WARNING);
                    viewer.show();
                    viewer.center();
                }
            };
            return false;
        }

        final OrderCostDTO cost;
        cost = CostCalculators.getIncreaseHeadcountArmyCost(army, count);


        final int[] ids = new int[3];
        ids[0] = armyId;
        ids[1] = count;
        ids[2] = sector.getRegionId();
        if (OrderStore.getInstance().addNewOrder(ORDER_INC_HEADCNT_ARMY, cost, army.getRegionId(), "", ids, 0, "") == 1) {
            for (CorpDTO corps : army.getCorps().values()) {
                for (BrigadeDTO brigade : corps.getBrigades().values()) {
                    upHeadCountExecute(brigade.getBrigadeId(), count);
                }
                corps.setUpHeadcount(true);
            }
            army.setUpHeadcount(true);
            UnitEventManager.changeUnit(ARMY, armyId);
            reAddUpgradeOrders(army);
            return true;
        }
        return false;
    }


    public void upHeadCountExecute(final int brigadeId, final int count) {
        final UpHeadCountBrigadeOrder ubOrder = new UpHeadCountBrigadeOrder(armiesMap, count);
        ubOrder.execute(brigadeId);
    }

    public void cancelUpHeadCountBrigade(final int brigadeId) {
        final int[] ids = new int[1];
        ids[0] = brigadeId;

        if (OrderStore.getInstance().removeOrder(ORDER_INC_HEADCNT, ids)) {
            final BrigadeDTO brig = ArmyStore.getInstance().getBrigadeById(brigadeId);
            brig.setIsUpHeadcount(false);
            brig.setUpHeadcount(0);
            for (BattalionDTO battalion : brig.getBattalions()) {
                battalion.setHeadcount(battalion.getOriginalHeadcount());
                battalion.setExperience(battalion.getOriginalExperience());
            }
            reAddUpgradeOrders(brig);
            if (brig.getCorpId() > 0) {
                final CorpDTO corps = getCorpByID(brig.getCorpId());
                reAddUpgradeOrders(corps);
                if (corps.getArmyId() > 0) {
                    final ArmyDTO army = getArmyById(corps.getArmyId());
                    reAddUpgradeOrders(army);
                }
            }
            UnitEventManager.changeUnit(BRIGADE, brigadeId);
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, FAILED_REMOVE_ORDER, false);

        }
    }

    public void cancelUpHeadCountCorps(final int corpsId) {
        final int[] ids = new int[1];
        ids[0] = corpsId;
        if (OrderStore.getInstance().removeOrder(ORDER_INC_HEADCNT_CORPS, ids)) {
            final CorpDTO corp = getCorpByID(corpsId);
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                brigade.setIsUpHeadcount(false);
                brigade.setUpHeadcount(0);
                for (BattalionDTO battalion : brigade.getBattalions()) {
                    battalion.setHeadcount(battalion.getOriginalHeadcount());
                    battalion.setExperience(battalion.getOriginalExperience());
                }
            }
            corp.setUpHeadcount(false);
            reAddUpgradeOrders(corp);
            if (corp.getArmyId() > 0) {
                reAddUpgradeOrders(getArmyById(corp.getArmyId()));
            }
            UnitEventManager.changeUnit(CORPS, corpsId);
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, FAILED_REMOVE_ORDER, false);

        }
    }

    public void cancelUpHeadCountArmy(final int armyId) {
        final int[] ids = new int[1];
        ids[0] = armyId;
        if (OrderStore.getInstance().removeOrder(ORDER_INC_HEADCNT_ARMY, ids)) {
            final ArmyDTO army = armiesMap.get(armyId);
            for (CorpDTO corps : army.getCorps().values()) {
                for (BrigadeDTO brigade : corps.getBrigades().values()) {
                    brigade.setIsUpHeadcount(false);
                    brigade.setUpHeadcount(0);
                    for (BattalionDTO battalion : brigade.getBattalions()) {
                        battalion.setHeadcount(battalion.getOriginalHeadcount());
                        battalion.setExperience(battalion.getOriginalExperience());
                    }
                }
                corps.setUpHeadcount(false);
            }
            army.setUpHeadcount(false);
            reAddUpgradeOrders(army);
            UnitEventManager.changeUnit(ARMY, armyId);
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, FAILED_REMOVE_ORDER, false);
        }
    }

    public boolean canArmyUpgrade(final ArmyDTO army, final boolean toCrackElite) {
        for (CorpDTO corp : army.getCorps().values()) {
            if (canCorpsUpgrade(corp, toCrackElite)) {
                return true;
            }
        }
        return false;
    }

    public boolean canCorpsUpgrade(final CorpDTO corp, final boolean toCrackElite) {
        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            if (canBrigadeUpgrade(brigade, toCrackElite)) {
                return true;
            }
        }
        return false;
    }

    public boolean canBrigadeUpgrade(final BrigadeDTO brigade, final boolean toCrackElite) {

        if (toCrackElite
                && !brigade.isUpgradedToElite()
                && !brigade.getLoaded()
                && !TransportStore.getInstance().hasUnitLoadOrder(BRIGADE, brigade.getBrigadeId())) {
            final int TOT_VPS = 100 * GameStore.getInstance().getVps() / GameStore.getInstance().getVpsMax();
            if (brigade.getBattalions() != null) {

                for (final BattalionDTO battalion : brigade.getBattalions()) {
                    if (battalion.getEmpireArmyType().isCrack() && !battalion.getEmpireArmyType().isElite()
                            && battalion.getEmpireArmyType().getUpgradeEliteTo() > 0
                            && (battalion.getExperience() > battalion.getEmpireArmyType().getMaxExp())
                            && battalion.getEmpireArmyType().getVps() <= TOT_VPS) {
                        return true;
                    }
                }
            }
        } else if (!toCrackElite) {
            if (!brigade.isUpgraded() && !brigade.getLoaded()
                    && !TransportStore.getInstance().hasUnitLoadOrder(BRIGADE, brigade.getBrigadeId())) {
                for (final BattalionDTO battalion : brigade.getBattalions()) {
                    if (battalion.getId() != 0 && battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canBrigadeUpHeadcount(final BrigadeDTO brigade) {
        if (brigade.getBattalions() != null && !brigade.IsUpHeadcount()
                && !brigade.getLoaded()
                && !TransportStore.getInstance().hasUnitLoadOrder(BRIGADE, brigade.getBrigadeId())) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                int headcount = 800;
                if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                        || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                        || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                    headcount = 1000;
                }

                if ((headcount - battalion.getHeadcount()) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canCorpsUpHeadcount(final CorpDTO corp) {
        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            if (canBrigadeUpHeadcount(brigade)) {
                return true;
            }
        }
        return false;
    }

    public boolean canArmyUpHeadCount(final ArmyDTO army) {
        for (CorpDTO corp : army.getCorps().values()) {
            if (canCorpsUpHeadcount(corp)) {
                return true;
            }
        }
        return false;
    }

    // Returns all armies on the target sector
    public List<ArmyDTO> getArmiesBySector(final SectorDTO sector, final boolean withMovement) {
        final Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>> tempMap;
        if (withMovement) {
            tempMap = sectorArmiesMap;

        } else {
            tempMap = startingSectorArmiesMap;
        }

        if (tempMap.containsKey(sector.getRegionId())) {
            if (tempMap.get(sector.getRegionId()).containsKey(sector.getId())) {
                final List<ArmyDTO> armies = new ArrayList<ArmyDTO>();
                armies.addAll(tempMap.get(sector.getRegionId()).get(sector.getId()).values());
                return armies;

            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Returns all armies on the target sector.
     *
     * @param regionId     the region.
     * @param xPos         the horizontal coordinate.
     * @param yPos         the vertical coodinate.
     * @param withMovement to take into account movement.
     * @return the armies locate in the sector.
     */
    public List<ArmyDTO> getArmiesBySector(final int regionId, final int xPos, final int yPos,
                                           final boolean withMovement) {
        if (RegionStore.getInstance().getRegionSectorsByRegionId(regionId) == null) {
            return null;
        } else {
            final SectorDTO tgSector = RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[xPos][yPos];
            return getArmiesBySector(tgSector, withMovement);
        }
    }

    // Return init status
    public boolean isArmiesInitialized() {
        return isInitialized;
    }

    public void setInitialized(final boolean value) {
        isInitialized = value;
    }

    public void applyChangesToPanels() {
        initSectorArmiesMap();
        initStartingSectorArmiesMap();
    }

    public boolean isArmyDeleted(final int armyId) {
        return idToDeletedArmy.containsKey(armyId);
    }

    public ArmyDTO getDeletedArmyById(final int armyId) {
        return idToDeletedArmy.get(armyId);
    }

    public boolean isCorpDeleted(final int corpId) {
        return idToDeletedCorp.containsKey(corpId);
    }

    public CorpDTO getDeletedCorpsById(final int corpId) {
        return idToDeletedCorp.get(corpId);
    }

    /**
     * Calculates and the total head count of an army, and the max head count the army could have.
     *
     * @param armyId The army to process.
     * @return A table of [total Heads, Max Heads].
     */
    public int[] getArmyHeadCountValues(int armyId) {
        int total = 0;
        int max = 0;
        final ArmyDTO army = getArmyById(armyId);
        for (CorpDTO corps : army.getCorps().values()) {
            for (BrigadeDTO brigade : corps.getBrigades().values()) {
                for (BattalionDTO batt : brigade.getBattalions()) {
                    total += batt.getHeadcount();
                    if (batt.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                            || batt.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                            || batt.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                        max += 1000;
                    } else {
                        max += 800;
                    }
                }
            }
        }
        return new int[]{total, max};
    }

    /**
     * Calculates and the total head count of a corps, and the max head count the corps could have.
     *
     * @param corpsId The corps to process.
     * @return A table of [total Heads, Max Heads].
     */
    public int[] getCorpsHeadCountValues(int corpsId) {
        int total = 0;
        int max = 0;
        final CorpDTO corps = getCorpByID(corpsId);
        for (BrigadeDTO brigade : corps.getBrigades().values()) {
            for (BattalionDTO batt : brigade.getBattalions()) {
                total += batt.getHeadcount();
                if (batt.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                        || batt.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                        || batt.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                    max += 1000;
                } else {
                    max += 800;
                }
            }
        }
        return new int[]{total, max};
    }

    /**
     * Calculates and the total head count of a brigade, and the max head count the brigade could have.
     *
     * @param brigadeId The brigade to process.
     * @return A table of [total Heads, Max Heads].
     */
    public int[] getBrigadeHeadCountValues(int brigadeId) {
        int total = 0;
        int max = 0;
        final BrigadeDTO brigade = getBrigadeById(brigadeId);
        for (BattalionDTO batt : brigade.getBattalions()) {
            total += batt.getHeadcount();
            if (batt.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                    || batt.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                    || batt.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                max += 1000;
            } else {
                max += 800;
            }
        }
        return new int[]{total, max};
    }

    /**
     * Calculates and the average experience of an army, and the max experience the army could have.
     *
     * @param armyId The army to process.
     * @return A table of [Average Exp, Max Exp].
     */
    public double[] getArmyExperienceValues(int armyId) {
        double count = 0;
        double total = 0;
        double max = 0;
        final ArmyDTO army = getArmyById(armyId);
        for (CorpDTO corps : army.getCorps().values()) {
            for (BrigadeDTO brigade : corps.getBrigades().values()) {
                for (BattalionDTO batt : brigade.getBattalions()) {
                    count += batt.getHeadcount();
                    total += batt.getExperience() * batt.getHeadcount();
                    max += batt.getEmpireArmyType().getMaxExp() * batt.getHeadcount();
                }
            }
        }
        if (count == 0) {
            return new double[]{1, 1};
        }
        return new double[]{total / count, max / count};
    }

    /**
     * Calculates and the average experience of a corps, and the max experience the corps could have.
     *
     * @param corpId The corps to process.
     * @return A table of [Average Exp, Max Exp].
     */
    public double[] getCorpsExperienceValues(int corpId) {
        double count = 0;
        double total = 0;
        double max = 0;
        final CorpDTO corps = getCorpByID(corpId);
        for (BrigadeDTO brigade : corps.getBrigades().values()) {
            for (BattalionDTO batt : brigade.getBattalions()) {
                count += batt.getHeadcount();
                total += batt.getExperience() * batt.getHeadcount();
                max += batt.getEmpireArmyType().getMaxExp() * batt.getHeadcount();
            }
        }
        if (count == 0) {
            return new double[]{1, 1};
        }
        return new double[]{total / count, max / count};
    }

    /**
     * Calculates and the average experience of a brigade, and the max experience the brigade could have.
     *
     * @param brigadeId The brigade to process.
     * @return A table of [Average Exp, Max Exp].
     */
    public double[] getBrigadeExperienceValues(int brigadeId) {
        double count = 0;
        double total = 0;
        double max = 0;
        final BrigadeDTO brigade = getBrigadeById(brigadeId);
        for (BattalionDTO batt : brigade.getBattalions()) {
            count += batt.getHeadcount();
            total += batt.getExperience() * batt.getHeadcount();
            max += batt.getEmpireArmyType().getMaxExp() * batt.getHeadcount();
        }
        if (count == 0) {
            return new double[]{1, 1};
        }
        return new double[]{total / count, max / count};
    }

    /**
     * Get all armies from the selected region.
     *
     * @param regionId the region to inspect.
     * @return a list of army objects.
     */
    public List<ArmyDTO> getArmiesByRegion(final int regionId) {
        final List<ArmyDTO> regionArmyList = new ArrayList<ArmyDTO>();
        for (ArmyDTO army : getcArmiesList()) {
            if (army.getArmyId() == 0) {
                final ArmyDTO freeArmy = new ArmyDTO();
                freeArmy.setName("Corps brigades in no army");
                freeArmy.setRegionId(regionId);
                freeArmy.setArmyId(0);
                for (CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() == 0) {
                        final CorpDTO freeCorp = new CorpDTO();
                        freeCorp.setName("Brigades in no corps or army");
                        freeCorp.setRegionId(regionId);
                        freeCorp.setArmyId(0);
                        freeCorp.setCorpId(0);
                        for (BrigadeDTO brigade : corp.getBrigades().values()) {
                            if (brigade.getRegionId() == regionId) {
                                freeCorp.getBrigades().put(brigade.getBrigadeId(), brigade);
                            }
                        }
                        freeArmy.getCorps().put(freeCorp.getCorpId(), freeCorp);
                    } else {
                        if (corp.getRegionId() == regionId) {
                            freeArmy.getCorps().put(corp.getCorpId(), corp);
                        }

                    }
                }
                regionArmyList.add(freeArmy);
            } else {
                if (army.getRegionId() == regionId) {
                    regionArmyList.add(army);
                }
            }
        }
        return regionArmyList;
    }

    public boolean hasSectorArmies(final SectorDTO selectedSector) {
        return getArmiesBySector(selectedSector, true) != null && getArmiesBySector(selectedSector, true).size() > 0;
    }

    public boolean hasSectorArmies2(final SectorDTO sectorDTO) {
        final List<ArmyDTO> armies = getArmiesBySector(sectorDTO, true);
        if (armies != null) {
            for (ArmyDTO army : armies) {
                if (army.getArmyId() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasSectorFreeCorps(final SectorDTO selectedSector) {
        final ArmyDTO dummyArmy = armiesMap.get(0);
        if (dummyArmy != null) {
            for (CorpDTO corp : dummyArmy.getCorps().values()) {
                if (corp.getRegionId() == selectedSector.getRegionId()
                        && corp.getX() == selectedSector.getX()
                        && corp.getY() == selectedSector.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Integer, ArmyDTO> getcArmies() {
        return armiesMap;
    }

    public ArmyDTO getArmyById(final int armyId) {
        return armiesMap.get(armyId);
    }

    public Collection<ArmyDTO> getcArmiesList() {
        return armiesMap.values();
    }


    public List<ArmyTypeDTO> getArmyTypesList() {
        return armyTypesList;
    }

    public Map<Integer, CorpDTO> getCorpsBySectorAndArmyId(final SectorDTO sector, final int firstId) {
        for (ArmyDTO army : getArmiesBySector(sector.getRegionId(), sector.getX(), sector.getY(), true)) {
            if (army.getArmyId() == firstId) {
                return army.getCorps();
            }
        }
        return null;
    }

    public Map<Integer, BrigadeDTO> getBrigadesBySectorAndCorpId(final SectorDTO sector, final int firstId) {
        for (ArmyDTO army : getArmiesBySector(sector.getRegionId(), sector.getX(), sector.getY(), true)) {
            if (army.getCorps().containsKey(firstId)) {
                return army.getCorps().get(firstId).getBrigades();
            }
        }
        return null;
    }

    public List<BattalionDTO> getBattalionsBySectorAndCorpId(final SectorDTO sector, final int firstId) {
        for (ArmyDTO army : getArmiesBySector(sector.getRegionId(), sector.getX(), sector.getY(), true)) {
            for (CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(firstId)) {
                    return corp.getBrigades().get(firstId).getBattalions();
                }
            }
        }

        return null;
    }

    public CorpDTO getCorpByID(final int corpId) {
        for (ArmyDTO army : getcArmiesList()) {
            if (army.getCorps().containsKey(corpId)) {
                return army.getCorps().get(corpId);
            }
        }
        return null;
    }

    public BrigadeDTO getBrigadeById(final int brigadeId) {
        for (final ArmyDTO army : getcArmiesList()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(brigadeId)) {
                    return corp.getBrigades().get(brigadeId);
                }
            }
        }
        return null;
    }

    public List<BrigadeDTO> getFreeBrigadesBySector(final SectorDTO sector) {
        final List<ArmyDTO> armies = getArmiesBySector(sector, true);
        if (armies != null && !armies.isEmpty()) {
            for (ArmyDTO army : armies) {
                if (army.getArmyId() == 0) {
                    if (army.getCorps().containsKey(0)) {
                        return new ArrayList<BrigadeDTO>(army.getCorps().get(0).getBrigades().values());
                    }
                    break;
                }
            }
        }
        return null;
    }

    public List<CorpDTO> getFreeCorpsBySector(final SectorDTO sector) {
        final List<ArmyDTO> armies = getArmiesBySector(sector, true);
        if (armies != null && !armies.isEmpty()) {
            for (ArmyDTO army : armies) {
                if (army.getArmyId() == 0) {
                    return new ArrayList<CorpDTO>(army.getCorps().values());
                }
            }
        }
        return null;
    }

    /**
     * Get a newly ordered brigade.
     *
     * @param brigadeId the id of the new brigade.
     * @return the brigade.
     */
    public BrigadeDTO getNewBrigadeById(final int brigadeId) {
        for (final Integer sectorId : barrBrigMap.keySet()) {
            for (final BrigadeDTO brig : barrBrigMap.get(sectorId)) {
                if (brig.getBrigadeId() == brigadeId) {
                    return brig;
                }
            }
        }
        return null;
    }

    /**
     * Method that returns a battalion DTO object by it's id.
     *
     * @param battalionId the id of the targeted battalion.
     * @return the BattalionDTO object corresponding to the
     * given id,
     */
    public BattalionDTO getBattalionById(final int battalionId) {
        for (final ArmyDTO army : getcArmiesList()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                for (final BrigadeDTO brig : corp.getBrigades().values()) {
                    for (final BattalionDTO battalion : brig.getBattalions()) {
                        if (battalion.getId() == battalionId) {
                            return battalion;
                        }
                    }
                }
            }
        }
        return null;
    }

    public BattalionDTO getMergedBattalionById(final int battalionId) {
        //if not found there... search merged battalions
        for (BattalionDTO battalion : mergedBatts) {
            if (battalion.getId() == battalionId) {
                return battalion;
            }
        }
        return null;
    }

    public int getCorpSize() {
        int corps = 0;
        for (final ArmyDTO army : armiesMap.values()) {
            corps = corps + army.getCorps().values().size();
        }
        return corps;
    }

    public int getBrigadesNum() {
        int brigades = 0;
        for (final ArmyDTO army : armiesMap.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                brigades = brigades + corp.getBrigades().values().size();
            }
        }

        for (final List<BrigadeDTO> brigs : barrBrigMap.values()) {
            brigades += brigs.size();
        }

        return brigades;
    }

    public List<BrigadeDTO> getNewBrigadesByPosition(final SectorDTO sector) {
        if (barrBrigMap.containsKey(sector.getId()) && barrBrigMap.get(sector.getId()) != null) {
            return barrBrigMap.get(sector.getId());
        }
        return new ArrayList<BrigadeDTO>();
    }

    public List<BrigadeDTO> getBrigadesByPosition(final int regionId, final int xPos, final int yPos,
                                                  final boolean afterMovement) {
        final List<BrigadeDTO> posBrigs = new ArrayList<BrigadeDTO>();
        for (final ArmyDTO army : getArmiesByRegion(regionId)) {
            if (army.getArmyId() == 0) {
                for (final CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() == 0) {
                        for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                            final PositionDTO pos = MovementStore.getInstance().getUnitPosition(BRIGADE, brigade.getBrigadeId(), brigade);
                            if (afterMovement) {
                                if (pos.getX() == xPos && pos.getY() == yPos) {
                                    posBrigs.add(brigade);
                                }
                            } else if (!MovementStore.getInstance().hasMovedThisTurn(BRIGADE, brigade.getBrigadeId())
                                    && brigade.getXStart() == xPos && brigade.getYStart() == yPos) {
                                posBrigs.add(brigade);
                            }
                        }
                    } else {
                        final PositionDTO pos = MovementStore.getInstance().getUnitPosition(CORPS, corp.getCorpId(), corp);
                        if (afterMovement) {
                            if (pos.getX() == xPos && pos.getY() == yPos) {
                                posBrigs.addAll(corp.getBrigades().values());
                            }
                        } else if (!MovementStore.getInstance().hasMovedThisTurn(CORPS, corp.getCorpId())
                                && corp.getXStart() == xPos && corp.getYStart() == yPos) {
                            posBrigs.addAll(corp.getBrigades().values());
                        }

                    }
                }
            } else {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(ARMY, army.getArmyId(), army);
                if (afterMovement) {
                    if (pos.getX() == xPos && pos.getY() == yPos) {
                        for (final CorpDTO corp : army.getCorps().values()) {
                            posBrigs.addAll(corp.getBrigades().values());
                        }
                    }

                } else if (!MovementStore.getInstance().hasMovedThisTurn(ARMY, army.getArmyId())
                        && army.getXStart() == xPos && army.getYStart() == yPos) {
                    for (final CorpDTO corp : army.getCorps().values()) {
                        posBrigs.addAll(corp.getBrigades().values());
                    }
                }

            }
        }
        return posBrigs;
    }

    public ArmyDTO getRegionNewBrigadesAsArmy(final int regionId) {
        final ArmyDTO newArmy = new ArmyDTO();
        newArmy.setArmyId(0);
        newArmy.setCorps(new HashMap<Integer, CorpDTO>());
        final CorpDTO zeroCorp = new CorpDTO();
        zeroCorp.setCorpId(0);
        zeroCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
        newArmy.getCorps().put(0, zeroCorp);
        for (Map.Entry<Integer, List<BrigadeDTO>> entry : ArmyStore.getInstance().getBarrBrigMap().entrySet()) {
            for (BrigadeDTO brigade : entry.getValue()) {
                if (brigade.getRegionId() == regionId) {
                    zeroCorp.getBrigades().put(brigade.getBrigadeId(), brigade);
                }
            }
        }
        return newArmy;
    }

    public List<SectorDTO> getRegionSectorsWithArmies(final int regionId) {
        final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
        if (sectorArmiesMap.containsKey(regionId)) {
            for (Integer sectorId : sectorArmiesMap.get(regionId).keySet()) {
                sectors.add(RegionStore.getInstance().getSectorById(sectorId));
            }
        }
        return sectors;
    }

    public int getArmyNation(final ArmyDTO army) {
        int nationId = GameStore.getInstance().getNationId();
        if (army.getCorps().size() > 0) {
            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().size() > 0) {
                    nationId = corp.getBrigades().values().iterator().next().getNationId();
                } else {
                    break;
                }
            }
        }
        return nationId;
    }

    public int getCorpNation(final CorpDTO corp) {
        int nationId = GameStore.getInstance().getNationId();
        if (corp.getBrigades().size() > 0) {
            nationId = corp.getBrigades().values().iterator().next().getNationId();
        }
        return nationId;
    }

    public String getArmyNameById(final int armyId) {
        if (armyId == 0) {
            return "none";

        } else {
            return armiesMap.get(armyId).getName();
        }
    }

    public List<BrigadeDTO> getArmyBrigadesList(final ArmyDTO army) {
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        try {
            for (CorpDTO corp : army.getCorps().values()) {
                out.addAll(corp.getBrigades().values());
            }

        } catch (Exception e) {
            // eat it
        }
        return out;
    }

    public List<BrigadeDTO> getBrigadesInArmies(final List<ArmyDTO> armies) {
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (final ArmyDTO army : armies) {
            try {
                for (CorpDTO corp : army.getCorps().values()) {
                    out.addAll(corp.getBrigades().values());
                }
            } catch (Exception e) {
                // eat it
            }
        }
        return out;
    }

    public List<BattalionDTO> getBrigadeBattalions(final BrigadeDTO brigade,
                                                   final boolean infantry,
                                                   final boolean cavalry,
                                                   final boolean artillery) {
        final List<BattalionDTO> out = new ArrayList<BattalionDTO>();
        try {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                final ArmyUnitInfoDTO info = MiscCalculators.getBattalionInfo(battalion);
                if (info.getArtillery() > 0 && artillery) {
                    out.add(battalion);
                }

                if (info.getCavalry() > 0 && cavalry) {
                    out.add(battalion);
                }

                if (info.getInfantry() > 0 && infantry) {
                    out.add(battalion);
                }
            }

        } catch (Exception ignore) {
            // eat it
        }
        return out;
    }

    public List<BattalionDTO> getCropsBattalions(final CorpDTO corp,
                                                 final boolean infantry,
                                                 final boolean cavalry,
                                                 final boolean artillery) {
        final List<BattalionDTO> out = new ArrayList<BattalionDTO>();
        try {
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                out.addAll(getBrigadeBattalions(brigade, infantry, cavalry, artillery));
            }

        } catch (Exception ignore) {
            // eat it
        }
        return out;
    }

    public List<BattalionDTO> getArmyBattalions(final ArmyDTO army,
                                                final boolean infantry,
                                                final boolean cavalry,
                                                final boolean artillery) {
        final List<BattalionDTO> out = new ArrayList<BattalionDTO>();

        try {
            for (CorpDTO corps : army.getCorps().values()) {
                out.addAll(getCropsBattalions(corps, infantry, cavalry, artillery));
            }

        } catch (Exception ignore) {
            // eat it
        }
        return out;
    }

    public List<BattalionDTO> getBattalionsInArmies(final List<ArmyDTO> armies,
                                                    final boolean infantry,
                                                    final boolean cavalry,
                                                    final boolean artillery) {
        final List<BattalionDTO> out = new ArrayList<BattalionDTO>();
        if (armies != null) {
            for (ArmyDTO army : armies) {
                out.addAll(getArmyBattalions(army, infantry, cavalry, artillery));
            }
        }
        return out;
    }

    public List<CorpDTO> getCorpsInArmies(final List<ArmyDTO> armies) {
        final List<CorpDTO> out = new ArrayList<CorpDTO>();
        for (ArmyDTO army : armies) {
            try {
                out.addAll(army.getCorps().values());

            } catch (Exception ignore) {
                // eat it
            }
        }
        return out;
    }

    public PositionDTO getUnitByTypeAndId(final int type, final int id) {
        switch (type) {
            case ARMY:
                return getArmyById(id);
            case CORPS:
                return getCorpByID(id);
            case BRIGADE:
                return getBrigadeById(id);
        }
        return null;
    }

    /**
     * Moves a unit to the specified position
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @param pos  the specified position
     */
    public void moveUnitByTypeToNewPosition(final int type,
                                            final int id,
                                            final PositionDTO pos) {
        PositionDTO oldPos = null;
        switch (type) {
            case ARMY:
                oldPos = armiesMap.get(id);
                if (oldPos != null) {
                    for (final CorpDTO corp : ((ArmyDTO) oldPos).getCorps().values()) {
                        moveUnitByTypeToNewPosition(CORPS, corp.getCorpId(), pos);
                    }
                }
                break;

            case CORPS:
                oldPos = getCorpByID(id);
                if (oldPos != null) {
                    for (final BrigadeDTO brigade : ((CorpDTO) oldPos).getBrigades().values()) {
                        moveUnitByTypeToNewPosition(BRIGADE, brigade.getBrigadeId(), pos);
                    }
                }
                break;

            case BRIGADE:
                oldPos = getBrigadeById(id);
                break;
            default:
                break;
        }

        if (oldPos != null) {
            oldPos.setX(pos.getX());
            oldPos.setY(pos.getY());
        }
    }
}
