package com.eaw1805.www.scenario.stores;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;

import java.util.HashMap;
import java.util.Map;

public class ArmyUtils {

    private static int armyId = -1;


    public static ArmyDTO createArmy(String name, int regionId, int x, int y, final int nationId) {
        final ArmyDTO army = new ArmyDTO();
        army.setArmyId(getArmyId());
        army.setName(name);
        army.setNationId(nationId);
        army.setRegionId(regionId);
        army.setX(x);
        army.setY(y);
        final Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>> armiesMap = EditorStore.getInstance().getArmies().get(regionId);
        if (!armiesMap.containsKey(x)) {
            armiesMap.put(x, new HashMap<Integer, Map<Integer, ArmyDTO>>());
        }
        if (!armiesMap.get(x).containsKey(y)) {
            armiesMap.get(x).put(y, new HashMap<Integer, ArmyDTO>());
        }
        armiesMap.get(x).get(y).put(army.getArmyId(), army);
        return army;
//        EditorMapUtils.getInstance().drawBrigade(brigade);
    }

    public static void addCorpsToArmy(CorpDTO corps, ArmyDTO army) {
        //just be sure if the brigade belongs in another corps to update that corps.
        removeCorpsFromArmy(corps);
        //then just add the brigade to corps
        army.getCorps().put(corps.getCorpId(), corps);
        corps.setArmyId(army.getArmyId());
    }

    public static void removeCorpsFromArmy(CorpDTO corps) {
        if (corps.getArmyId() == 0) {
            return;
        }
        final int x = corps.getX();
        final int y = corps.getY();
        final Map<Integer, Map<Integer, Map<Integer, ArmyDTO>>> armiesMap = EditorStore.getInstance().getArmies().get(corps.getRegionId());
        if (!armiesMap.containsKey(x)) {
            armiesMap.put(x, new HashMap<Integer, Map<Integer, ArmyDTO>>());
        }
        if (!armiesMap.get(x).containsKey(y)) {
            armiesMap.get(x).put(y, new HashMap<Integer, ArmyDTO>());
        }
        final ArmyDTO army = armiesMap.get(x).get(y).get(corps.getArmyId());
        army.getCorps().remove(corps.getCorpId());
        corps.setArmyId(0);
    }

    public static void deleteArmy(ArmyDTO army) {
        //update all brigades
        for (CorpDTO corps : army.getCorps().values()) {
            corps.setArmyId(0);
        }
        //update corps map
        EditorStore.getInstance().getArmies().get(army.getRegionId()).get(army.getX()).get(army.getY()).remove(army.getArmyId());
    }

    private static int getArmyId() {
        armyId--;
        return armyId;
    }

}
