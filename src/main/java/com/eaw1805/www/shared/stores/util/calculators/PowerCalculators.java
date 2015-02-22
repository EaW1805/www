package empire.webapp.shared.stores.util.calculators;

import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.BrigadeDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;

import java.util.List;

public final class PowerCalculators {

    /**
     * Method that return the power of a list of armies
     *
     * @param armies whose power we want to measure
     * @return 0-10 according to the power we count
     */
    public static int calculateArmiesPower(final List<ArmyDTO> armies, final boolean withLoadedUnits) {
        int brigades = 0, battalions = 0;
        for (ArmyDTO army : armies) {
            for (CorpDTO corp : army.getCorps().values()) {
                brigades += corp.getBrigades().values().size();
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    if (withLoadedUnits || (!withLoadedUnits && !brigade.getLoaded())) {
                        battalions += brigade.getBattalions().size();
                    } else {
                        brigades--;
                    }
                }
            }
        }

        return getPowerByBrigsAndBatts(brigades, battalions);
    }

    public static int calculateCorpsPower(final CorpDTO corp, final boolean withLoadedUnits) {
        int brigades = 0, battalions = 0;

        brigades = brigades + corp.getBrigades().values().size();
        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            if (withLoadedUnits || (!withLoadedUnits && !brigade.getLoaded())) {
                battalions += brigade.getBattalions().size();
            } else {
                brigades--;
            }
        }

        return getPowerByBrigsAndBatts(brigades, battalions);
    }

    /**
     * Method that returns the power of an army
     *
     * @param army whose power we want to measure
     * @return 0-10 according to the power we count
     */
    public static int calculateArmyPower(final ArmyDTO army, final boolean withLoadedUnits) {
        int brigades = 0, battalions = 0;
        for (CorpDTO corp : army.getCorps().values()) {
            brigades = brigades + corp.getBrigades().values().size();
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                if (withLoadedUnits || (!withLoadedUnits && !brigade.getLoaded())) {
                    battalions += brigade.getBattalions().size();
                } else {
                    brigades--;
                }
            }
        }
        return getPowerByBrigsAndBatts(brigades, battalions);
    }

    /**
     * Method that returns the power in numeric form according to the input
     *
     * @param brigades   power in brigades
     * @param battalions power in battalions
     * @return the numeric power from 0-10
     */
    public static int getPowerByBrigsAndBatts(final int brigades, final int battalions) {
        int power = 0;
        if (battalions >= 480) {
            power = 10;

        } else if (battalions >= 360) {
            power = 9;

        } else if (battalions >= 240) {
            power = 8;

        } else if (battalions >= 120) {
            power = 7;

        } else if (battalions >= 91) {
            power = 6;

        } else if (brigades >= 15) {
            power = 5;

        } else if (brigades >= 11) {
            power = 4;

        } else if (brigades >= 6) {
            power = 3;

        } else if (brigades == 5) {
            power = 2;

        } else if (brigades == 4) {
            power = 1;

        } else if (brigades <= 3 && brigades > 0) {
            power = 0;
        } else if (brigades == 0) {
            power = -1;
        }
        return power;
    }

    public static int calculateFleetPower(final List<FleetDTO> fleets) {
        int warShips = 0;
        for (FleetDTO fleet : fleets) {
            for (ShipDTO ship : fleet.getShips().values()) {
                if (ship.getType().getShipClass() > 0 || ship.getType().getIntId() == 18) {
                    warShips++;
                }
            }
        }

        return getPowerByWarShips(warShips);
    }

    public static int calculateFleetPower(final FleetDTO fleet) {
        int warShips = 0;
        for (ShipDTO ship : fleet.getShips().values()) {
            if (ship.getType().getShipClass() > 0 || ship.getType().getIntId() == 18) {
                warShips++;
            }
        }
        return getPowerByWarShips(warShips);
    }

    public static int getPowerByWarShips(final int warShips) {
        int power = 0;
        if (warShips >= 31) {
            power = 10;
        } else if (warShips >= 21) {
            power = 9;
        } else if (warShips >= 18) {
            power = 8;
        } else if (warShips >= 14) {
            power = 7;
        } else if (warShips >= 11) {
            power = 6;
        } else if (warShips >= 8) {
            power = 5;
        } else if (warShips >= 6) {
            power = 4;
        } else if (warShips >= 4) {
            power = 3;
        } else if (warShips >= 2) {
            power = 2;
        } else if (warShips == 1) {
            power = 1;
        }
        return power;
    }

    public static int getPowerByWarShips(final FleetDTO fleet) {
        int totWarShips = 0;
        for (ShipDTO ship : fleet.getShips().values()) {
            if (ship.getTypeId() != 31 && ship.getType().getShipClass() > 0) {
                totWarShips++;
            }
        }
        return getPowerByWarShips(totWarShips);
    }


}
