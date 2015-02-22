package empire.webapp.shared.stores.util.calculators;

import empire.data.constants.NationConstants;
import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BattalionDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.util.ArmyUnitInfoDTO;
import empire.webapp.shared.stores.util.NavyUnitInfoDTO;

import java.util.List;


public final class MiscCalculators
        implements NationConstants {

    public static int getFleetsMovingUnits(final List<FleetDTO> fleets) {
        int movingUnits = 0;
        for (final FleetDTO fleet : fleets) {
            if (fleet.getFleetId() == 0) {
                movingUnits += fleet.getShips().size();
            } else {
                movingUnits++;
            }
        }
        return movingUnits;
    }

    public static int getMinPowerUnderTarget(final int target) {
        int begin = (int) Math.sqrt(target);
        while (Math.pow(begin, 2d) < target) {
            begin++;
        }
        return begin;
    }

    public static int getArmyMovingUnits(final List<ArmyDTO> armies) {
        int movingUnits = 0;
        for (final ArmyDTO army : armies) {
            if (army.getArmyId() == 0) {
                for (final CorpDTO corp : army.getCorps().values()) {
                    if (corp.getCorpId() == 0) {
                        for (BrigadeDTO brig : corp.getBrigades().values()) {
                            if (!brig.getLoaded()) {
                                movingUnits += 1;
                            }
                        }
                    } else {
                        movingUnits++;
                    }
                }
            } else {
                movingUnits++;
            }
        }
        return movingUnits;
    }

    public static int getArmyMps(final ArmyDTO army) {
        int mps = Integer.MAX_VALUE;
        int commC = 0;
        if (army.getCommander() != null && army.getCommander().getId() != 0
                && !army.getCommander().getInTransit()) {
            commC = army.getCommander().getStrc();
        }

        for (final CorpDTO corp : army.getCorps().values()) {
            mps = Math.min(mps, getCorpMps(corp, true));
            commC--;
        }

        if (commC > 0) {
            commC = 0;
        }

        mps += (commC * 2);

        if (mps < 0) {
            mps = 0;
        }

        if (mps == Integer.MAX_VALUE) {
            mps = 0;
        }

        return mps;
    }

    public static int getCorpMps(final CorpDTO corp, final boolean checkEmpty) {
        int mps = Integer.MAX_VALUE;

        if (checkEmpty && corp.getBrigades().isEmpty()) {
            return mps;
        }

        for (final BrigadeDTO brigade : corp.getBrigades().values()) {
            mps = Math.min(mps, getBrigadeMps(brigade, true));
        }

        if (mps < 0) {
            mps = 0;
        }

        if (mps == Integer.MAX_VALUE) {
            mps = 0;
        }

        return mps;
    }

    public static int getBrigadeMps(final BrigadeDTO brigade, final boolean checkEmpty) {
        int mps = Integer.MAX_VALUE;
        if (checkEmpty && brigade.getBattalions().isEmpty()) {
            return mps;
        }

        for (final BattalionDTO battalion : brigade.getBattalions()) {
            mps = Math.min(mps, battalion.getEmpireArmyType().getMps());
        }

        if (mps < 0) {
            mps = 0;
        }

        if (mps == Integer.MAX_VALUE) {
            mps = 0;
        }

        return mps;
    }

    public static int getFleetMps(final FleetDTO fleet) {
        int mps = Integer.MAX_VALUE;
        int totWarShips = 0;
        for (final ShipDTO ship : fleet.getShips().values()) {
            if (ship.getTypeId() != 31 && ship.getType().getShipClass() > 0) {
                totWarShips++;
            }

            mps = Math.min(mps, (int) (ship.getType().getMovementFactor() * ship.getCondition() / 100d));
        }

        // Check fleet size
        if (totWarShips > 20) {
            // Calculate penalty
            // It is possible for a fleet to contain more than 20 warships, however for every 2 ships above 20,
            // the movement allowance of the fleet is reduced by 1 movement point due to coordination limitations.
            final double divisor;

            // certain nations have superior seamanship
            // other nations have inferior seamanship
            switch (fleet.getNationId()) {
                case NATION_GREATBRITAIN:
                case NATION_HOLLAND:
                case NATION_DENMARK:
                case NATION_PORTUGAL:
                    // superior seamanship
                    divisor = 3d;
                    break;

                case NATION_AUSTRIA:
                case NATION_PRUSSIA:
                case NATION_WARSAW:
                    // inferior seamanship
                    divisor = 1d;
                    break;

                default:
                    divisor = 2d;
            }

            final int penalty = (int) Math.ceil((totWarShips - 20d) / divisor);
            mps -= penalty;
        }

        if (mps == Integer.MAX_VALUE) {
            mps = 0;
        }

        if (mps < 0) {
            mps = 0;
        }

        return mps;
    }

    public static ArmyUnitInfoDTO getArmyInfo(final ArmyDTO army) {
        final ArmyUnitInfoDTO armyInfo = new ArmyUnitInfoDTO();
        armyInfo.setCorps(army.getCorps().size());
        for (final CorpDTO corp : army.getCorps().values()) {
            armyInfo.addToInfo(getCorpInfo(corp));
        }
        return armyInfo;
    }

    public static ArmyUnitInfoDTO getCorpInfo(final CorpDTO corp) {
        final ArmyUnitInfoDTO corpInfo = new ArmyUnitInfoDTO();
        corpInfo.setBrigades(corp.getBrigades().size());
        for (final BrigadeDTO brigade : corp.getBrigades().values()) {
            corpInfo.addToInfo(getBrigadeInfo(brigade));
        }
        return corpInfo;
    }

    public static ArmyUnitInfoDTO getBrigadeInfo(final BrigadeDTO brigade) {
        final ArmyUnitInfoDTO brigadeInfo = new ArmyUnitInfoDTO();
        brigadeInfo.setBattalions(brigade.getBattalions().size());
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            brigadeInfo.addToInfo(getBattalionInfo(battalion));
        }
        return brigadeInfo;
    }

    public static ArmyUnitInfoDTO getBattalionInfo(final BattalionDTO battalion) {
        final ArmyUnitInfoDTO battalionInfo = new ArmyUnitInfoDTO();
        if (battalion.getEmpireArmyType().isInfantry() || battalion.getEmpireArmyType().isEngineer()) {
            battalionInfo.setInfantry(battalion.getHeadcount());

        } else if (battalion.getEmpireArmyType().isCavalry()) {
            battalionInfo.setCavalry(battalion.getHeadcount());

        } else {
            battalionInfo.setArtillery(battalion.getHeadcount());
        }

        battalionInfo.setMps(battalion.getEmpireArmyType().getMps());
        battalionInfo.setHeadCount(battalion.getHeadcount());
        int maxHeadcount = 800;
        if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
            maxHeadcount = 1000;
        }
        battalionInfo.setMaxHeadCount(maxHeadcount);
        battalionInfo.setAvgExperience(battalion.getExperience());
        battalionInfo.setMaxExperience(battalion.getEmpireArmyType().getMaxExp());
        final int TOT_VPS = 100 * GameStore.getInstance().getVps() / GameStore.getInstance().getVpsMax();
        if (battalion.getEmpireArmyType().isCrack() && !battalion.getEmpireArmyType().isElite()
                && battalion.getEmpireArmyType().getUpgradeEliteTo() > 0
                && (battalion.getExperience() > battalion.getEmpireArmyType().getMaxExp())
                && battalion.getEmpireArmyType().getVps() <= TOT_VPS) {
            battalionInfo.setNumOfBattalionsNeedCrackElite(1);
        }

        if (battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
            battalionInfo.setNumOfBattalionsNeedTrain(1);
        }

        return battalionInfo;
    }

    public static NavyUnitInfoDTO getFleetInfo(final FleetDTO fleet) {
        final NavyUnitInfoDTO fleetInfo = new NavyUnitInfoDTO();
        int totMerchants = 0;
        int totWarships = 0;
        if (fleet != null && fleet.getShips() != null) {
            for (ShipDTO ship : fleet.getShips().values()) {
                if (ship.getTypeId() == 31 || ship.getType().getShipClass() == 0) {
                    totMerchants++;

                } else {
                    totWarships++;
                }
            }
        }

        fleetInfo.setMerchantShips(totMerchants);
        fleetInfo.setWarShips(totWarships);
        fleetInfo.setMps(getFleetMps(fleet));
        return fleetInfo;
    }

    public static int getMaxBrigadesInCorps() {
        if (GameStore.getInstance().getNationId() == NationConstants.NATION_MOROCCO
                || GameStore.getInstance().getNationId() == NationConstants.NATION_OTTOMAN
                || GameStore.getInstance().getNationId() == NationConstants.NATION_EGYPT) {
            return 20;
        } else {
            return 16;
        }
    }

    public static int getMaxCorpsInArmy() {
        return 5;
    }


}
