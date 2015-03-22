package com.eaw1805.www.shared.stores.units;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;

/**
 * A class that provides basic access to some important
 * unit functions
 *
 * @author tsakygr
 */
public final class UnitService implements ArmyConstants {
    /**
     * Singleton instance of the UnitService.
     */
    private static UnitService ourInstance = null;

    /**
     * Method returning the on instance of
     * the service
     *
     * @return the instance
     */
    public static UnitService getInstance() {
        if (ourInstance == null) {
            ourInstance = new UnitService();
        }
        return ourInstance;
    }

    /**
     * Method that returns the position object of
     * the unit by type and id
     *
     * @param unitType the type of the unit
     * @param unitId   the id of the unit
     * @return the position object
     */
    public PositionDTO getUnitByTypeAndId(final int unitType, final int unitId) {
        PositionDTO pos = null;
        switch (unitType) {
            case ARMY:
                pos = ArmyStore.getInstance().getcArmies().get(unitId);
                break;
            case CORPS:
                pos = ArmyStore.getInstance().getCorpByID(unitId);
                break;
            case BRIGADE:
                pos = ArmyStore.getInstance().getBrigadeById(unitId);
                break;
            case FLEET:
                pos = NavyStore.getInstance().getIdFleetMap().get(unitId);
                if (pos == null) {
                    pos = AlliedUnitsStore.getInstance().getFleetById(unitId);
                }
                break;
            case SHIP:
                pos = NavyStore.getInstance().getShipById(unitId);
                break;
            case SPY:
                pos = SpyStore.getInstance().getSpyById(unitId);
                break;
            case COMMANDER:
                pos = CommanderStore.getInstance().getCommanderById(unitId);
                break;
            case BAGGAGETRAIN:
                pos = BaggageTrainStore.getInstance().getBaggageTrainById(unitId);
                break;
            default:
                break;
        }
        return pos;
    }

    public PositionDTO getCopyPosition(final int unitType, final int unitId) {
        final PositionDTO original = getUnitByTypeAndId(unitType, unitId);
        if (original == null) {
            return null;
        }
        final PositionDTO copy = new PositionDTO();
        copy.setRegionId(original.getRegionId());
        copy.setX(original.getX());
        copy.setY(original.getY());
        copy.setXStart(original.getXStart());
        copy.setYStart(original.getYStart());
        return copy;
    }

    public void updateIndexes(final int unitType, final int unitId, final String previous) {
        if (unitType == BAGGAGETRAIN) {
            BaggageTrainStore.getInstance().updatePositionIndex(unitId, previous);
        }
    }

    /**
     * Method that removes the target unit from it's parenting
     * federation
     *
     * @param type the type of the unit
     * @param id   the id of the unit
     * @return true if it successful
     */
    public boolean removeFromFederation(final int type, final int id) {
        switch (type) {
            case BRIGADE:
                if (ArmyStore.getInstance().getBrigadeById(id).getCorpId() == 0) {
                    return false;
                } else {
                    ArmyStore.getInstance().changeBrigadeCorp(id, ArmyStore.getInstance().getBrigadeById(id).getCorpId(), 0, true);
                }
                break;
            case COMMANDER:
                final CommanderDTO commander = CommanderStore.getInstance().getCommanderById(id);
                if (commander.getCorp() != 0 || commander.getArmy() != 0) {
                    CommanderStore.getInstance().commanderLeaveFederation(id);
                } else {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

}
