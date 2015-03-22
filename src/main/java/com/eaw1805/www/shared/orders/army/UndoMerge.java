package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.orders.Order;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.List;

public class UndoMerge implements Order {

    private final List<BattalionDTO> unused;
    private final int secBatId;

    public UndoMerge(final List<BattalionDTO> unused, int secBatId) {
        this.unused = unused;
        this.secBatId = secBatId;
    }

    public int execute(final int battId) {
        try {
            BattalionDTO batt1 = null, batt2 = null, baseBatt = ArmyStore.getInstance().getBattalionById(battId);
            BrigadeDTO baseBrig = ArmyStore.getInstance().getBrigadeById(baseBatt.getBrigadeId());
            for (BattalionDTO battalion : unused) {
                if (battalion.getId() == battId) {
                    batt1 = battalion;
                    batt1.setMergedWith(0);
                } else if (battalion.getId() == secBatId) {
                    batt2 = battalion;
                    batt2.setMergedWith(0);
                }
            }
            BrigadeDTO brig1 = ArmyStore.getInstance().getBrigadeById(batt1.getBrigadeId());
            BrigadeDTO brig2 = ArmyStore.getInstance().getBrigadeById(batt2.getBrigadeId());

            if (brig1 != null && brig2 != null && baseBrig != null) {
                if (brig2.getBattalions().size() > 5 || (brig1.getBattalions().size() > 5 && brig1 != baseBrig)) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "There is no longer space in the brigade to recover the battalion", false);
                } else {
                    baseBrig.getBattalions().remove(baseBatt);
                    brig1.getBattalions().add(batt1);
                    unused.remove(batt1);
                    brig2.getBattalions().add(batt2);
                    unused.remove(batt2);
                }

            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "A target brigade no longer exists. Unable to undo merge.", false);
            }
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

}
