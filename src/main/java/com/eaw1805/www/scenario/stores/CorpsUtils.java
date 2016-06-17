package com.eaw1805.www.scenario.stores;

import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 12/21/13
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class CorpsUtils {

    private static int corpsId = -1;


    public static CorpDTO createCorps(String name, int regionId, int x, int y, final int nationId) {
        final CorpDTO corp = new CorpDTO();
        corp.setCorpId(getCorpsId());
        corp.setName(name);
        corp.setNationId(nationId);
        corp.setRegionId(regionId);
        corp.setX(x);
        corp.setY(y);
        final Map<Integer, Map<Integer, Map<Integer, CorpDTO>>> corpsMap = EditorStore.getInstance().getCorps().get(regionId);
        if (!corpsMap.containsKey(x)) {
            corpsMap.put(x, new HashMap<Integer, Map<Integer, CorpDTO>>());
        }
        if (!corpsMap.get(x).containsKey(y)) {
            corpsMap.get(x).put(y, new HashMap<Integer, CorpDTO>());
        }
        corpsMap.get(x).get(y).put(corp.getCorpId(), corp);
        return corp;
//        EditorMapUtils.getInstance().drawBrigade(brigade);
    }

    public static void addBrigadeToCorps(BrigadeDTO brigade, CorpDTO corps) {
        //just be sure if the brigade belongs in another corps to update that corps.
        removeBrigadeFromCorps(brigade);
        //then just add the brigade to corps
        corps.getBrigades().put(brigade.getBrigadeId(), brigade);
        brigade.setCorpId(corps.getCorpId());
    }

    public static void removeBrigadeFromCorps(BrigadeDTO brigade) {
        if (brigade.getCorpId() == 0) {
            return;
        }
        final int x = brigade.getX();
        final int y = brigade.getY();
        final Map<Integer, Map<Integer, Map<Integer, CorpDTO>>> corpsMap = EditorStore.getInstance().getCorps().get(brigade.getRegionId());
        if (!corpsMap.containsKey(x)) {
            corpsMap.put(x, new HashMap<Integer, Map<Integer, CorpDTO>>());
        }
        if (!corpsMap.get(x).containsKey(y)) {
            corpsMap.get(x).put(y, new HashMap<Integer, CorpDTO>());
        }
        final CorpDTO corps = corpsMap.get(x).get(y).get(brigade.getCorpId());
        corps.getBrigades().remove(brigade.getBrigadeId());
        brigade.setCorpId(0);
    }

    public static void deleteCorps(CorpDTO corps) {
        //update all brigades
        for (BrigadeDTO brig : corps.getBrigades().values()) {
            brig.setCorpId(0);
        }
        //update corps map
        EditorStore.getInstance().getCorps().get(corps.getRegionId()).get(corps.getX()).get(corps.getY()).remove(corps.getCorpId());
    }





    private static int getCorpsId() {
        corpsId--;
        return corpsId;
    }
}
