package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Iterator;
import java.util.Map;

public class ChangeBattalionBrigadeOrder implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private int brigadeId, newSlot;

    public ChangeBattalionBrigadeOrder(final Map<Integer, ArmyDTO> armiesMap,
                                       final int newBrigadeId, final int newSlot) {
        this.armiesMap = armiesMap;
        brigadeId = newBrigadeId;
        this.newSlot = newSlot;
    }

    public int execute(final int unitId) {
        BrigadeDTO source = null, target = null;
        BattalionDTO battalion = null;
        for (final ArmyDTO army : armiesMap.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                for (final BrigadeDTO brig : corp.getBrigades().values()) {
                    if (brig.getBrigadeId() == brigadeId) {
                        target = brig;
                    }

                    for (final BattalionDTO batt : brig.getBattalions()) {
                        if (batt.getId() == unitId) {
                            source = brig;
                            battalion = batt;
                        }
                    }
                }
            }
        }

        if (source != null && target != null && (target.getBrigadeId() != source.getBrigadeId())) {
            battalion.setBrigadeId(target.getBrigadeId());
            battalion.setOrder(newSlot);
//            source.getBattalions().remove(battalion);
            //remove battalion using iterator.. just to be sure..
            final Iterator<BattalionDTO> iter = source.getBattalions().iterator();
            while (iter.hasNext()) {
                if (iter.next().getId() == battalion.getId()) {
                    iter.remove();
                    break;
                }
            }
            target.getBattalions().add(battalion);
            return 1;

        } else {
            return 0;
        }
    }

}
