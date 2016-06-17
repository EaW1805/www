package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.scenario.views.ArmyBrushView;
import com.eaw1805.www.scenario.views.EditorPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrigadeUtils {
    private static int brigadeId = -1;

    public static void createBrigade(final int x, final int y) {
        final ArmyBrushView brush = EditorPanel.getInstance().getArmyBrush();
        final BrigadeDTO brigade = new BrigadeDTO();
        brigade.setBrigadeId(getBrigadeId());
        brigade.setX(x);
        brigade.setY(y);
        brigade.setRegionId(RegionSettings.region.getRegionId());
        brigade.setName("brigade " + Math.abs(brigade.getBrigadeId()));
        brigade.setNationId(brush.getNation().getNationId());
        List<BattalionDTO> batts = new ArrayList<BattalionDTO>();
        if (brush.getBatt1() != null) {
            batts.add(cloneBattalion(brush.getBatt1()));
        }
        if (brush.getBatt2() != null) {
            batts.add(cloneBattalion(brush.getBatt2()));
        }
        if (brush.getBatt3() != null) {
            batts.add(cloneBattalion(brush.getBatt3()));
        }
        if (brush.getBatt4() != null) {
            batts.add(cloneBattalion(brush.getBatt4()));
        }
        if (brush.getBatt5() != null) {
            batts.add(cloneBattalion(brush.getBatt5()));
        }
        if (brush.getBatt6() != null) {
            batts.add(cloneBattalion(brush.getBatt6()));
        }
        brigade.setBattalions(batts);
        final Map<Integer, Map<Integer, Map<Integer, BrigadeDTO>>> brigMap = EditorStore.getInstance().getBrigades().get(RegionSettings.region.getRegionId());
        if (!brigMap.containsKey(x)) {
            brigMap.put(x, new HashMap<Integer, Map<Integer, BrigadeDTO>>());
        }
        if (!brigMap.get(x).containsKey(y)) {
            brigMap.get(x).put(y, new HashMap<Integer, BrigadeDTO>());
        }
        brigMap.get(x).get(y).put(brigade.getBrigadeId(), brigade);
        EditorMapUtils.getInstance().drawBrigade(brigade);
    }

    public static void deleteBrigade(final BrigadeDTO brigade) {
        //first if the brigade belongs to a corps.. update the corps.
        if (brigade.getCorpId() != 0) {
            EditorStore.getInstance().getCorps().get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).get(brigade.getCorpId()).getBrigades().remove(brigade.getBrigadeId());
        }
        //second remove the brigade from the store.
        EditorStore.getInstance().getBrigades().get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).remove(brigade.getBrigadeId());
    }

    private static BattalionDTO cloneBattalion(final BattalionDTO in) {
        BattalionDTO out = new BattalionDTO();
        out.setOrder(in.getOrder());
        out.setExperience(in.getExperience());
        out.setHeadcount(in.getHeadcount());
        out.setEmpireArmyType(in.getEmpireArmyType());
        return out;
    }




    private static int getBrigadeId() {
        brigadeId--;
        return brigadeId;
    }
}
