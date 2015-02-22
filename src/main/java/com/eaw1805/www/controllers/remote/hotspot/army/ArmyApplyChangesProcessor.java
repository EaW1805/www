package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
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
import com.eaw1805.www.shared.orders.army.UpHeadCountBrigadeOrder;
import com.eaw1805.www.shared.orders.army.UpgradeBrigadeOrder;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmyApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_ADD_BATT,
            ORDER_B_ARMY,
            ORDER_B_CORP,
            ORDER_REN_ARMY,
            ORDER_REN_CORP,
            ORDER_REN_BRIG,
            ORDER_ADDTO_ARMY,
            ORDER_ADDTO_CORP,
            ORDER_ADDTO_BRIGADE,
            ORDER_D_ARMY,
            ORDER_D_CORP,
            ORDER_D_BRIG,
            ORDER_INC_EXP,
            ORDER_INC_HEADCNT,
            ORDER_INC_HEADCNT_CORPS,
            ORDER_INC_HEADCNT_ARMY,
            ORDER_MRG_BATT,
            ORDER_LOAD_TROOPSF,
            ORDER_LOAD_TROOPSS,
            ORDER_UNLOAD_TROOPSF,
            ORDER_UNLOAD_TROOPSS,
            ORDER_M_ARMY,
            ORDER_M_CORP,
            ORDER_M_BRIG,
            ORDER_FM_ARMY,
            ORDER_FM_CORP,
            ORDER_FM_BRIG,
            ORDER_M_UNIT,
            ORDER_INC_EXP_ARMY,
            ORDER_INC_EXP_CORPS,
            ORDER_CORP_COM,
            ORDER_ARMY_COM,
            ORDER_LEAVE_COM,
            ORDER_DISS_COM,
            ORDER_HIRE_COM
    };

    /**
     * The region the armies came from.
     */
    private int regionId;

    /**
     * A boolean if the units are allied or not.
     */
    private boolean areAllied;

    /**
     * A list with army types.
     */
    private transient final List<ArmyTypeDTO> armyTypes;

    private transient final Map<Integer, ArmyTypeDTO> armyTypesMap = new HashMap<Integer, ArmyTypeDTO>();

    /**
     * The armyList as we have made it so far mapped to their IDs.
     */
    private transient final Map<Integer, ArmyDTO> armiesMap = new HashMap<Integer, ArmyDTO>();

    /**
     * A list of the merged battalions that no longer exist for the client
     */
    private transient final List<BattalionDTO> mergedBatts = new ArrayList<BattalionDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * The deleted armies map.
     */
    private transient final Map<Integer, ArmyDTO> deletedArmies = new HashMap<Integer, ArmyDTO>();

    /**
     * The deleted corps map.
     */
    private transient final Map<Integer, CorpDTO> deletedCorps = new HashMap<Integer, CorpDTO>();


    /**
     * Default constructor.
     *
     * @param thisGame    the game of the order.
     * @param thisNation  the owner of the order.
     * @param thisTurn    the turn of the order.
     * @param rpcEndpoint the RPC service instance.
     */
    public ArmyApplyChangesProcessor(final int thisScenario,
                                     final int thisGame,
                                     final int thisNation,
                                     final int thisTurn,
                                     final EmpireRpcServiceImpl rpcEndpoint,
                                     final int regionId,
                                     final boolean isAllied) {

        super(thisScenario, thisGame, thisNation, thisTurn);
        this.regionId = regionId;
        this.areAllied = isAllied;
        armyTypes = rpcEndpoint.getArmyTypes(thisScenario, thisNation);
        for (ArmyTypeDTO type : armyTypes) {
            armyTypesMap.put(type.getIntId(), type);
        }
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        for (ArmyDTO army : (List<ArmyDTO>) dbData) {
            armiesMap.put(army.getArmyId(), army);
        }

        orders = (List<OrderDTO>) chOrders;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<ArmyDTO> processChanges() {
        //first create all new armies just to be sure...
        for (final OrderDTO order : orders) {
            if (order.getType() == ORDER_B_ARMY) {
                if (!areAllied || (Integer.parseInt(order.getParameter6()) == regionId)) {
                    execOrderBuildA(order);
                }
            }
        }

        for (final OrderDTO order : orders) {
            switch (order.getType()) {

                case ORDER_ADD_BATT: {
                    execOrderAddB(order);

                    break;
                }

                case ORDER_B_ARMY:
                    //do nothing at this point
                    break;

                case ORDER_B_CORP: {
                    execOrderBuildC(order);
                    break;
                }

                case ORDER_REN_ARMY: {
                    execOrderRenA(order);
                    break;
                }

                case ORDER_REN_CORP: {
                    execOrderRenC(order);
                    break;
                }

                case ORDER_REN_BRIG: {
                    execOrderRenB(order);
                    break;
                }

                case ORDER_ADDTO_ARMY: {
                    execOrderAddA(order);
                    break;
                }

                case ORDER_ADDTO_CORP: {
                    execOrderAddC(order);
                    break;
                }

                case ORDER_ADDTO_BRIGADE: {
                    execOrderExchB(order);
                    break;
                }

                case ORDER_D_ARMY: {
                    execOrderDelA(order);
                    break;
                }

                case ORDER_D_CORP: {
                    execOrderDelC(order);
                    break;
                }

                case ORDER_D_BRIG:
                    // do nothing
                    break;

                case ORDER_INC_EXP: {
                    execOrderIncExp(order);
                    break;
                }

                case ORDER_INC_HEADCNT: {
                    execOrderIncHB(order);
                    break;
                }

                case ORDER_INC_HEADCNT_CORPS: {
                    execOrderIncHC(order);
                    break;
                }

                case ORDER_INC_HEADCNT_ARMY: {
                    execOrderIncHA(order);
                    break;
                }

                case ORDER_MRG_BATT: {
                    execOrderMerge(order);
                    break;
                }

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS: {
                    final int type1 = Integer.parseInt(order.getParameter3());
                    if (type1 == BRIGADE) {
                        final BrigadeDTO brig = getBrigadeById(Integer.parseInt(order.getParameter4()));
                        if (brig != null) {
                            brig.setLoaded(true);

                            if (brig.getCorpId() > 0) {
                                final ChangeBrigadeCorpOrder cbcOrder = new ChangeBrigadeCorpOrder(armiesMap, 0);
                                cbcOrder.execute(brig.getBrigadeId());
                            }

                        }
                    }
                    break;
                }

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS: {
                    final int type1 = Integer.parseInt(order.getParameter3());
                    if (type1 == BRIGADE) {
                        final BrigadeDTO brig = getBrigadeById(Integer.parseInt(order.getParameter4()));
                        if (brig == null) {
                            break;
                        }
                        brig.setLoaded(false);
                        final int[] xy = TransportStore.getCoordsByDirection(Integer.parseInt(order.getParameter5()),
                                Integer.parseInt(order.getTemp1()),
                                Integer.parseInt(order.getTemp2()));
                        brig.setX(xy[0]);
                        brig.setY(xy[1]);
                        brig.setXStart(xy[0]);
                        brig.setYStart(xy[1]);
                    }
                    break;
                }

                case ORDER_M_ARMY:
                case ORDER_M_CORP:
                case ORDER_M_BRIG:
                case ORDER_FM_ARMY:
                case ORDER_FM_CORP:
                case ORDER_FM_BRIG:
                case ORDER_M_UNIT: {
                    final int unitType = Integer.parseInt(order.getParameter1());
                    if (unitType == ARMY || unitType == CORPS || unitType == BRIGADE) {
                        final PositionDTO pos = getLastPositionByString(order.getParameter3());
                        if (pos != null) {
                            moveUnitByTypeToNewPosition(Integer.parseInt(order.getParameter1()), Integer.parseInt(order.getParameter2()), pos);
                        }
                    }
                    break;
                }

                case ORDER_INC_EXP_ARMY: {
                    final ArmyDTO army = armiesMap.get(Integer.parseInt(order.getParameter1()));
                    if (army == null) {//when doing this for allied armies it could be null.
                        break;
                    }
                    final boolean toCrackElite = Integer.parseInt(order.getParameter2()) == 1;
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
                    break;
                }

                case ORDER_INC_EXP_CORPS: {
                    final CorpDTO corp = getCorpByID(Integer.parseInt(order.getParameter1()));
                    if (corp == null) {//when doing this for allied armies it could be null
                        break;
                    }
                    final boolean toCrackElite = Integer.parseInt(order.getParameter2()) == 1;
                    if (corp != null) {
                        if (corp.getBrigades() != null && corp.getBrigades().size() > 0) {
                            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                                if (canBrigadeUpgrade(brigade, toCrackElite)) {
                                    upgradeBrigadeExecute(brigade.getBrigadeId(), toCrackElite);
                                }
                            }
                        }

                        if (toCrackElite) {
                            corp.setUpgradedToElite(true);
                        } else {
                            corp.setUpgraded(true);
                        }
                    }
                    break;
                }

                default:
                    // do nothing
            }
        }

        return new ArrayList<ArmyDTO>(armiesMap.values());
    }

    private void execOrderBuildA(final OrderDTO order) {
        final CreateArmyOrder caOrder = new CreateArmyOrder(armiesMap, order.getParameter2(),
                Integer.parseInt(order.getParameter4()),
                Integer.parseInt(order.getParameter5()),
                Integer.parseInt(order.getParameter6()), getNationId());
        caOrder.execute(Integer.parseInt(order.getParameter1()));
    }

    private void execOrderAddB(final OrderDTO order) {
        //done checking for conflicts
        final int brigadeId = Integer.parseInt(order.getParameter1());
        final ArmyTypeDTO armyType = getArmyTypeByID(Integer.parseInt(order.getParameter2()));
        final int slot = Integer.parseInt(order.getParameter3());
        BrigadeDTO brig = getBrigadeById(brigadeId);
        if (brig == null) {
            return;
        }
        final AddBattalionToBrigade abtb = new AddBattalionToBrigade(brig,
                armyType,
                slot);
        abtb.execute(brigadeId);
    }

    private void execOrderBuildC(final OrderDTO order) {
        final int armyId = Integer.parseInt(order.getParameter2());
        final int corpId = Integer.parseInt(order.getParameter1());
        final ArmyDTO army = armiesMap.get(armyId);
        if (army == null || (areAllied && Integer.parseInt(order.getParameter6()) != regionId)) {
            return;
        }
        final CreateCorpOrder ccOrder = new CreateCorpOrder(army.getCorps(),
                order.getParameter3(),
                Integer.parseInt(order.getParameter4()),
                Integer.parseInt(order.getParameter5()),
                Integer.parseInt(order.getParameter6()),
                order.getNationId(), armyId);
        ccOrder.execute(corpId);
    }

    private void execOrderRenA(final OrderDTO order) {
        final int armyId = Integer.parseInt(order.getParameter1());
        final ChangeArmyNameOrder caName = new ChangeArmyNameOrder(armiesMap, order.getParameter2());
        caName.execute(armyId);
    }

    private void execOrderRenC(final OrderDTO order) {
        final int corpId = Integer.parseInt(order.getParameter1());
        for (ArmyDTO army : armiesMap.values()) {
            if (army.getCorps().containsKey(corpId)) {
                final ChangeCorpNameOrder ccName = new ChangeCorpNameOrder(army.getCorps(), order.getParameter2());
                ccName.execute(corpId);
                break;
            }
        }
    }

    private void execOrderRenB(final OrderDTO order) {
        final int brigadeId = Integer.parseInt(order.getParameter1());
        for (final ArmyDTO army : armiesMap.values()) {
            for (CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(brigadeId)) {
                    final ChangeBrigadeNameOrder cbName = new ChangeBrigadeNameOrder(corp.getBrigades(), order.getParameter2());
                    cbName.execute(brigadeId);
                    break;
                }
            }
        }
    }

    private void execOrderAddA(final OrderDTO order) {
        //check if corps is deleted first.
        if (deletedCorps.containsKey(Integer.parseInt(order.getParameter1()))) {
            final CorpDTO corp = deletedCorps.get(Integer.parseInt(order.getParameter1()));
            corp.setArmyId(0);//then the army id is 0.
        } else {
            final ChangeCorpArmyOrder cbcOrder = new ChangeCorpArmyOrder(armiesMap, Integer.parseInt(order.getParameter2()));
            cbcOrder.execute(Integer.parseInt(order.getParameter1()));
            final ArmyDTO sourceArmy = cbcOrder.getSource();
            if (sourceArmy != null) {
                if (sourceArmy.getCorps().isEmpty()) {
                    deletedArmies.put(sourceArmy.getArmyId(), armiesMap.remove(sourceArmy.getArmyId()));
                }
            }
        }
    }

    private void execOrderAddC(final OrderDTO order) {
        final ChangeBrigadeCorpOrder cbcOrder = new ChangeBrigadeCorpOrder(armiesMap, Integer.parseInt(order.getParameter2()));
        cbcOrder.execute(Integer.parseInt(order.getParameter1()));
        final CorpDTO sourceCorp = cbcOrder.getSource();
        if (sourceCorp != null) {
            if (sourceCorp.getBrigades().isEmpty()) {
                deletedCorps.put(sourceCorp.getCorpId(), armiesMap.get(sourceCorp.getArmyId()).getCorps().remove(sourceCorp.getCorpId()));
            }
        }
    }

    private void execOrderExchB(final OrderDTO order) {
        final ChangeBattalionBrigadeOrder cbbOrder = new ChangeBattalionBrigadeOrder(armiesMap,
                Integer.parseInt(order.getParameter2()),
                Integer.parseInt(order.getParameter3()));
        cbbOrder.execute(Integer.parseInt(order.getParameter1()));
    }

    private void execOrderDelA(final OrderDTO order) {
        final int armyId = Integer.parseInt(order.getParameter1());
        final DeleteArmyOrder daOrder = new DeleteArmyOrder(armiesMap, armyId, deletedArmies);
        daOrder.execute(armyId);
    }

    private void execOrderDelC(final OrderDTO order) {
        final int armyId = Integer.parseInt(order.getParameter2());
        final ArmyDTO army = armiesMap.get(armyId);
        if (army == null) {
            return;
        }
        final DeleteCorpOrder dcOrder = new DeleteCorpOrder(army.getCorps(), deletedCorps);
        dcOrder.execute(Integer.parseInt(order.getParameter1()));
    }

    private void execOrderIncExp(final OrderDTO order) {
        boolean toCrackElite = false;
        try {
            if (Integer.parseInt(order.getParameter2()) == 1) {
                toCrackElite = true;
            }

        } catch (Exception ex) {
            toCrackElite = false;
        }

        final UpgradeBrigadeOrder ubOrder = new UpgradeBrigadeOrder(armiesMap, toCrackElite, armyTypesMap);
        ubOrder.execute(Integer.parseInt(order.getParameter1()));
    }

    private void execOrderIncHB(final OrderDTO order) {
        final UpHeadCountBrigadeOrder ubOrder = new UpHeadCountBrigadeOrder(armiesMap,
                Integer.parseInt(order.getParameter2()));
        ubOrder.execute(Integer.parseInt(order.getParameter1()));
    }

    private void execOrderIncHC(final OrderDTO order) {
        final int corpsId = Integer.parseInt(order.getParameter1());
        final int count = Integer.parseInt(order.getParameter2());
        final CorpDTO corps = getCorpByID(corpsId);
        if (corps == null) {
            return;
        }
        for (BrigadeDTO brigade : corps.getBrigades().values()) {
            upHeadCountExecute(brigade.getBrigadeId(), count);
        }
        corps.setUpHeadcount(true);
    }

    private void execOrderIncHA(final OrderDTO order) {
        final int armyId = Integer.parseInt(order.getParameter1());
        final int count = Integer.parseInt(order.getParameter2());
        final ArmyDTO army = armiesMap.get(armyId);
        if (army == null) {
            return;
        }
        for (CorpDTO corps : army.getCorps().values()) {
            for (BrigadeDTO brigade : corps.getBrigades().values()) {
                upHeadCountExecute(brigade.getBrigadeId(), count);
            }
            corps.setUpHeadcount(true);
        }
        army.setUpHeadcount(true);
    }

    private void execOrderMerge(final OrderDTO order) {
        final MergeBattalionsOrder mbOrder = new MergeBattalionsOrder(armiesMap, mergedBatts, Integer.parseInt(order.getParameter2()));
        mbOrder.execute(Integer.parseInt(order.getParameter1()));
    }

    /**
     * Get map of armies.
     *
     * @return army objects mapped by id.
     */
    public Map<Integer, ArmyDTO> getArmiesMap() {
        return armiesMap;
    }

    /**
     * Get list of merged battalions.
     *
     * @return a list of battalionDTO objects.
     */
    public List<BattalionDTO> getMergedBatts() {
        return mergedBatts;
    }

    private PositionDTO getLastPositionByString(final String movementString) {
        final PositionDTO lastPosition = new PositionDTO();
        final String[] moves = movementString.split("!");
        final String lastMove = moves[moves.length - 1];
        final String[] sectors = lastMove.split("-");
        final String lastSector = sectors[sectors.length - 1];
        final String[] coords = lastSector.split(":");
        try {
            lastPosition.setX(Integer.parseInt(coords[0]));
            lastPosition.setY(Integer.parseInt(coords[1]));
        } catch (Exception ex) {
            return null;
        }

        return lastPosition;
    }

    private ArmyTypeDTO getArmyTypeByID(final int type) {
        ArmyTypeDTO armyTypeDto = new ArmyTypeDTO();
        for (ArmyTypeDTO armyType : armyTypes) {
            if (armyType.getIntId() == type) {
                armyTypeDto = armyType;
                break;
            }
        }
        return armyTypeDto;
    }

    private BrigadeDTO getBrigadeById(final int brigadeId) {
        for (final ArmyDTO army : armiesMap.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(brigadeId)) {
                    return corp.getBrigades().get(brigadeId);
                }
            }
        }
        return null;
    }

    private CorpDTO getCorpByID(final int corpId) {
        for (ArmyDTO army : armiesMap.values()) {
            if (army.getCorps().containsKey(corpId)) {
                return army.getCorps().get(corpId);
            }
        }
        return null;
    }

    private void upHeadCountExecute(final int brigadeId, final int count) {
        final UpHeadCountBrigadeOrder ubOrder = new UpHeadCountBrigadeOrder(armiesMap, count);
        ubOrder.execute(brigadeId);
    }

    /**
     * Moves a unit to the specified position
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @param pos  the specified position
     */
    private void moveUnitByTypeToNewPosition(final int type,
                                             final int id,
                                             final PositionDTO pos) {
        final PositionDTO oldPos;
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
            default:
                oldPos = getBrigadeById(id);
                break;
        }

        if (oldPos != null) {
            oldPos.setX(pos.getX());
            oldPos.setY(pos.getY());
        }
    }

    private boolean canBrigadeUpgrade(final BrigadeDTO brigade, final boolean toCrackElite) {
        if (toCrackElite) {
            if (!brigade.isUpgradedToElite() && brigade.getBattalions() != null) {
                for (final BattalionDTO battalion : brigade.getBattalions()) {
                    if (battalion.getEmpireArmyType().isCrack() && !battalion.getEmpireArmyType().isElite() &&
                            (battalion.getExperience() > battalion.getEmpireArmyType().getMaxExp())) {
                        return true;
                    }
                }
            }

        } else {
            if (!brigade.isUpgraded()) {
                for (final BattalionDTO battalion : brigade.getBattalions()) {
                    if (battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void upgradeBrigadeExecute(final int brigadeId, final boolean toCrackElite) {
        final UpgradeBrigadeOrder ubOrder = new UpgradeBrigadeOrder(armiesMap, toCrackElite, armyTypesMap);
        ubOrder.execute(brigadeId);
    }

    public Map<Integer, ArmyDTO> getDeletedArmies() {
        return deletedArmies;
    }

    public Map<Integer, CorpDTO> getDeletedCorps() {
        return deletedCorps;
    }
}
