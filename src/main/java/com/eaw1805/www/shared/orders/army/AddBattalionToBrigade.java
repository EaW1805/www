package empire.webapp.shared.orders.army;

import empire.data.constants.NationConstants;
import empire.data.dto.web.army.ArmyTypeDTO;
import empire.data.dto.web.army.BattalionDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.webapp.shared.orders.Order;

import java.util.ArrayList;

public class AddBattalionToBrigade
        implements Order {

    private final BrigadeDTO brig;
    private final ArmyTypeDTO armyType;
    private final int slot;

    public AddBattalionToBrigade(final BrigadeDTO brigade, final ArmyTypeDTO armyType, final int slot) {
        brig = brigade;
        this.armyType = armyType;
        this.slot = slot;
    }

    public int execute(final int unitId) {
        final BattalionDTO batt = new BattalionDTO();
        batt.setEmpireArmyType(armyType);
        batt.setOrder(slot);
        batt.setTypeId(armyType.getIndPt());
        batt.setExperience(1);
        batt.setBrigadeId(unitId);
        batt.setFleeing(false);
        batt.setNotSupplied(false);
        batt.setParticipated(false);
        batt.setId(0);
        int headcount = 800;
        if (armyType.getNationId() == NationConstants.NATION_MOROCCO
                || armyType.getNationId() == NationConstants.NATION_OTTOMAN
                || armyType.getNationId() == NationConstants.NATION_EGYPT) {
            headcount = 1000;
        }
        batt.setHeadcount(headcount);

        if (brig.getBattalions() == null) {
            brig.setBattalions(new ArrayList<BattalionDTO>());
        }

        brig.getBattalions().add(batt);
        return 0;
    }

}
