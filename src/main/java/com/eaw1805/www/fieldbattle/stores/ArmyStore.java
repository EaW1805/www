package com.eaw1805.www.fieldbattle.stores;


import com.google.gwt.user.client.Window;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.asyncs.ArmyData;
import com.eaw1805.www.fieldbattle.events.loading.LoadingEventManager;
import com.eaw1805.www.fieldbattle.stores.utils.ArmyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmyStore implements ClearAble {

    private transient static ArmyStore instance = null;

    Map<Integer, Map<Integer, BrigadeDTO>> alliedBrigades = new HashMap<Integer, Map<Integer, BrigadeDTO>>();
    Map<Integer, Map<Integer, BrigadeDTO>> enemyBrigades = new HashMap<Integer, Map<Integer, BrigadeDTO>>();
    ArmyData armyData;
    boolean initialized = false;

    private ArmyStore() {
        //ha ha
    }

    public static ArmyStore getInstance() {
        if (instance == null) {
            instance = new ArmyStore();
        }
        return instance;
    }

    public void initBrigades(Map<Boolean, Map<Integer, Map<Integer, BrigadeDTO>>> dbBrigades) {
        alliedBrigades.putAll(dbBrigades.get(true));
        enemyBrigades.putAll(dbBrigades.get(false));
        initialized = true;
        //place brigades on map
        LoadingEventManager.ArmiesLoaded(new ArrayList<BrigadeDTO>(alliedBrigades.get(BaseStore.getInstance().getNationId()).values()));
    }

    public  void initArmiesData(final ArmyData in) {
        armyData = in;
    }

    public BrigadeDTO getBrigadeById(final int brigadeId) {
        for (Map<Integer, BrigadeDTO> alliedNationMap : alliedBrigades.values()) {
            if (alliedNationMap.containsKey(brigadeId)) {
                return alliedNationMap.get(brigadeId);
            }
        }
        for (Map<Integer, BrigadeDTO> enemyNationMap : enemyBrigades.values()) {
            if (enemyNationMap.containsKey(brigadeId)) {
                return enemyNationMap.get(brigadeId);
            }
        }
        return null;
    }

    public List<BrigadeDTO> getBrigadesByPosition(final int x, final int y) {
        List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (Map<Integer, BrigadeDTO> entry : alliedBrigades.values()) {
            for (BrigadeDTO brigade : entry.values()) {
                if (brigade.isPlacedOnFieldMap()) {
                    if (brigade.getFieldBattleX() == x && brigade.getFieldBattleY() == y) {
                        out.add(brigade);
                    }
                }
            }
        }
        return out;
    }

    public List<BrigadeDTO> getFollowers(final int leaderId) {
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (BrigadeDTO brigade : getBrigadesByNation(BaseStore.getInstance().getNationId())) {
            if (brigade.getBasicOrder() != null && "FOLLOW_DETACHMENT".equals(brigade.getBasicOrder().getOrderType())
                    && brigade.getBasicOrder().getLeaderId() == leaderId) {
                out.add(brigade);
            }
            if (brigade.getAdditionalOrder() != null && "FOLLOW_DETACHMENT".equals(brigade.getAdditionalOrder().getOrderType())
                    && brigade.getAdditionalOrder().getLeaderId() == leaderId) {
                out.add(brigade);
            }
        }
        return out;
    }

    public List<BrigadeDTO> getAlliedBrigades() {
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (Map<Integer, BrigadeDTO> entry : alliedBrigades.values()) {
            for (BrigadeDTO brig : entry.values()) {
                out.add(brig);
            }
        }
        return out;
    }

    public List<BrigadeDTO> getEnemyBrigades() {
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (Map<Integer, BrigadeDTO> entry : enemyBrigades.values()) {
            for (BrigadeDTO brig : entry.values()) {
                out.add(brig);
            }
        }
        return out;
    }

    public List<BrigadeDTO> getAlliedBrigadesByCorps(final int corps) {
        List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (Integer nationId : BaseStore.getInstance().getAlliedNations()) {
            for (BrigadeDTO brig : alliedBrigades.get(nationId).values()) {
                if (brig.getCorpId() == corps) {
                    out.add(brig);
                }
            }
        }
        return out;
    }

    public List<BrigadeDTO> getEnemyBrigadesByCorps(final int corps) {
        List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (Integer nationId : BaseStore.getInstance().getEnemyNations()) {
            for (BrigadeDTO brig : enemyBrigades.get(nationId).values()) {
                if (brig.getCorpId() == corps) {
                    out.add(brig);
                }
            }
        }
        return out;
    }

    public List<BrigadeDTO> getBrigadesByNation(final int nationId) {
        if (alliedBrigades.containsKey(nationId)) {
            return new ArrayList<BrigadeDTO>(alliedBrigades.get(nationId).values());
        } else if (enemyBrigades.containsKey(nationId)) {
            return new ArrayList<BrigadeDTO>(enemyBrigades.get(nationId).values());
        }
        return new ArrayList<BrigadeDTO>();
    }

    public List<BrigadeDTO> getPlacedBrigadesByNation(final int nationId) {
        List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (BrigadeDTO brigade : getBrigadesByNation(nationId)) {
            if (brigade.isPlacedOnFieldMap()) {
                out.add(brigade);
            }
        }
        return out;
    }

    public List<BrigadeDTO> getBrigadesByTypeAndNation(final int nationId, final int type) {
        final List<BrigadeDTO> nationBrigades = getBrigadesByNation(nationId);
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (BrigadeDTO brigade : nationBrigades) {
            final ArmyUnitInfoDTO unitInfo = MiscCalculators.getBrigadeInfo(brigade);
            if (unitInfo.getDominant() == type) {
                out.add(brigade);
            }
        }
        return out;
    }

    public List<BrigadeDTO> getBrigadesUnplacedByTypeAndNation(final int nationId, final int type) {
        final List<BrigadeDTO> nationBrigades = getBrigadesByNation(nationId);
        final List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (BrigadeDTO brigade : nationBrigades) {
            if (!brigade.isPlacedOnFieldMap()) {
                final ArmyUnitInfoDTO unitInfo = MiscCalculators.getBrigadeInfo(brigade);
                if (unitInfo.getDominant() == type) {
                    out.add(brigade);
                }
            }
        }
        return out;
    }

    public int getBrigadeHeadCountByNation(final int nationId) {
        int count = 0;
        for (BrigadeDTO brigade : getBrigadesByNation(nationId)) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                count += battalion.getHeadcount();
            }
        }
        return count;
    }

    public int getBrigadeCountByNation(final int nationId) {
        int count = 0;
        for (BrigadeDTO brigade : getBrigadesByNation(nationId)) {
            if (brigade.isPlacedOnFieldMap()) {
                count++;
            }
        }
        return count;
    }

    public double[] getBrigadeExperienceByNation(final int nationId) {
        int count = 0;
        double total = 0;
        double max = 0;
        for (BrigadeDTO brigade : getBrigadesByNation(nationId)) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                count++;
                total += battalion.getExperience();
                max += battalion.getEmpireArmyType().getMaxExp();
            }
        }
        if (count == 0) {
            return new double[]{0, 0};
        }
        total = total / (1.0 * count);
        max = max / (1.0 * count);
        return new double[]{total, max};
    }

    public ArmyData getArmyData() {
        return armyData;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void clear() {
        initialized = false;
        alliedBrigades.clear();
        enemyBrigades.clear();
    }
}
