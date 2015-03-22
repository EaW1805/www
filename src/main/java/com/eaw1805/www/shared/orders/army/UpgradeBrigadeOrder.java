package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.Map;

public class UpgradeBrigadeOrder implements Order {

    private Map<Integer, ArmyDTO> armies;
    private boolean toCrackElite = false;
    private Map<Integer, ArmyTypeDTO> armyTypes;

    public UpgradeBrigadeOrder(final Map<Integer, ArmyDTO> getcArmies, final boolean toCrackElite, final Map<Integer, ArmyTypeDTO> armyTypes) {
        armies = getcArmies;
        this.toCrackElite = toCrackElite;
        this.armyTypes = armyTypes;
    }

    public int execute(final int unitId) {
        boolean found = false;
        for (final ArmyDTO army : armies.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(unitId)) {
                    final BrigadeDTO brigade = corp.getBrigades().get(unitId);
                    if (brigade.getBrigadeId() == unitId) {
                        if (!toCrackElite) {
                            brigade.setIsUpgraded(true);
                            for (final BattalionDTO battalion : brigade.getBattalions()) {
                                if (battalion.getId() != 0 && battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
                                    battalion.setExperience(battalion.getExperience() + 1);
                                }
                            }
                        } else {
                            brigade.setIsUpgradedToElite(true);
                            for (final BattalionDTO battalion : brigade.getBattalions()) {
                                if (battalion.getExperience() > battalion.getEmpireArmyType().getMaxExp()
                                        && battalion.getEmpireArmyType().getUpgradeEliteTo() > 0
                                        && battalion.getEmpireArmyType().isCrack()) {
                                    battalion.setEmpireArmyType(armyTypes.get(battalion.getEmpireArmyType().getUpgradeEliteTo()));

                                }
                            }
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
