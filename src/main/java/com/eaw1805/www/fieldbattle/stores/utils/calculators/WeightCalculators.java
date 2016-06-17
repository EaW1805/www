package com.eaw1805.www.fieldbattle.stores.utils.calculators;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;

public final class WeightCalculators
        implements ArmyConstants {

    /**
     * Method that returns the weight of a battalion
     *
     * @param battalion the battalion whose weight we want
     * @return the weight in tons of the battalion
     */
    public static int getBattalionWeight(final BattalionDTO battalion) {
        double unitWeight;
        if (battalion.getEmpireArmyType().isInfantry() || battalion.getEmpireArmyType().isEngineer()) {
            unitWeight = 200d;

        } else if (battalion.getEmpireArmyType().isCavalry()) {
            unitWeight = 400d;

        } else {
            unitWeight = 600d;
        }

        return (int) ((battalion.getHeadcount() * unitWeight) / 1000d);
    }

    /**
     * Method that returns the weight of a brigade
     *
     * @param brigade the brigade whose weight we want
     * @return the weight in tons of the brigade
     */
    public static int getBrigadeWeight(final BrigadeDTO brigade) {
        if (brigade == null) {//todo:check this.. it happens for allied units
            return 0;
        }
        int weight = 0;
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            weight += getBattalionWeight(battalion);
        }
        return weight;
    }

}
