package empire.webapp.shared.orders.army;

import empire.data.constants.NationConstants;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BattalionDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.webapp.shared.orders.Order;

import java.util.Map;

public class UpHeadCountBrigadeOrder implements Order {

    private Map<Integer, ArmyDTO> armies;
    private int count;

    public UpHeadCountBrigadeOrder(final Map<Integer, ArmyDTO> getcArmies, final int count) {
        armies = getcArmies;
        this.count = count;
    }

    public int execute(final int unitId) {
        boolean found = false;
        for (final ArmyDTO army : armies.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(unitId)) {
                    final BrigadeDTO brigade = corp.getBrigades().get(unitId);
                    //because those 2 orders will be canceled
                    brigade.setIsUpgraded(false);
                    brigade.setIsUpgradedToElite(false);
                    if (brigade.getBrigadeId() == unitId) {
                        brigade.setIsUpHeadcount(true);
                        brigade.setUpHeadcount(count);

                        for (final BattalionDTO battalion : brigade.getBattalions()) {
                            int maxHeadcount = 800;
                            if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                                maxHeadcount = 1000;
                            }

                            final int newHeads;
                            if (battalion.getHeadcount() + count > maxHeadcount) {
                                newHeads = maxHeadcount;

                            } else {
                                newHeads = battalion.getHeadcount() + count;
                            }

                            //these affect the original state of the battalion.
                            battalion.setExperience(battalion.getOriginalExperience());
                            battalion.setEmpireArmyType(battalion.getEmpireArmyType());

                            // Check if experience level will be reduced
                            if (newHeads - battalion.getHeadcount() > battalion.getHeadcount()) {
                                // drop by 2 since more than 100% increase
                                battalion.setExperience(battalion.getExperience() - 2);

                            } else if (newHeads - battalion.getHeadcount() > 0.5d * battalion.getHeadcount()) {
                                // drop by 1 since more than 50% increase
                                battalion.setExperience(battalion.getExperience() - 1);
                            }
                            if (battalion.getExperience() < 1) {
                                battalion.setExperience(1);
                            }

                            battalion.setHeadcount(newHeads);

                        }

                        found = true;
                        break;
                    }

                    if (found) {
                        break;
                    }
                }

                if (found) {
                    break;
                }
            }

            if (found) {
                break;
            }
        }

        if (found) {
            return 1;
        } else {
            return 0;
        }
    }

}
