package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;


public class ChangeBrigadeCorpOrder
        implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private int corpId;

    public ChangeBrigadeCorpOrder(final Map<Integer, ArmyDTO> armiesMap,
                                  final int newCorpId) {
        this.armiesMap = armiesMap;
        this.setCorpId(newCorpId);
    }
    CorpDTO source = null;
    public int execute(final int unitId) {
        CorpDTO target = null;
        for (final ArmyDTO army : armiesMap.values()) {
            if (army.getCorps().containsKey(getCorpId())) {
                target = army.getCorps().get(getCorpId());
            }

            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(unitId)) {
                    source = corp;
                }
            }
        }

        if (source != null && target != null && (target.getCorpId() != source.getCorpId())) {
            source.getBrigades().get(unitId).setCorpId(getCorpId());
            target.getBrigades().put(unitId, source.getBrigades().get(unitId));
            source.getBrigades().remove(unitId);
            return 1;

        } else {
            return 0;
        }
    }

    public CorpDTO getSource() {
        return source;
    }

    public void setCorpId(final int corpId) {
        this.corpId = corpId;
    }

    public int getCorpId() {
        return corpId;
    }

}

