package com.eaw1805.www.shared.stores.util.calculators;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Conquer calculators for armies/corps/brigades
 */
public final class ConquerCalculators implements RegionConstants {


    /**
     * CONQUER CALCULATORS FOR ARMIES
     **/

    /**
     * Get the number of sectors that it can conquer.
     *
     * @param theUnit the entity subject to this movement order.
     * @return the total number of sectors.
     */
    public static int getArmyMaxConquers(final ArmyDTO theUnit) {
        int totBrigades = 0;
        final List<CorpDTO> lstCorps = new ArrayList<CorpDTO>(theUnit.getCorps().values());
        for (final CorpDTO theCorp : lstCorps) {
            final List<BrigadeDTO> lstBrigades = new ArrayList<BrigadeDTO>(theCorp.getBrigades().values());
            totBrigades += lstBrigades.size();

            // Troops that lose a battle cannot move over/capture enemy territory for one turn
            for (BrigadeDTO brigade : lstBrigades) {
                for (BattalionDTO battalion : brigade.getBattalions()) {
                    if (battalion.getHasLost()) {
                        return 0;
                    }
                }
            }
        }

        int maxSectors = 0;

        if (GameStore.getInstance().getScenarioId() == HibernateUtil.DB_S3) {
            // You need a commander to conquer
            if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                if (totBrigades >= 1 && totBrigades <= 3) {
                    maxSectors = 1;

                } else if (totBrigades >= 4 && totBrigades <= 9) {
                    maxSectors = 2;

                } else if (totBrigades >= 10) {
                    maxSectors = 3;
                }
            }

        } else {
            // All other scenarios
            if (theUnit.getRegionId() == EUROPE) {
                // Europe
                // You need a commander to conquer in Europe
                if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                    if (totBrigades >= 5 && totBrigades <= 9) {
                        maxSectors = 1;

                    } else if (totBrigades >= 10 && totBrigades <= 14) {
                        maxSectors = 2;

                    } else if (totBrigades >= 15) {
                        maxSectors = 3;
                    }
                }

            } else {
                // Colonies
                // Either you need a commander
                if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                    if (totBrigades >= 4) {
                        maxSectors = 2;

                    } else if (totBrigades >= 1) {
                        maxSectors = 1;
                    }
                } else {
                    // You need KT troops
                    // Count KT troops
                    final int countKT = countArmyTotalKT(theUnit);

                    // Needs 2 KT battalions to conquer 1 sector
                    if (totBrigades >= 4 && countKT >= 5) {
                        return 2;

                    } else if (countKT >= 2) {
                        return 1;
                    }
                }
            }
        }

        return maxSectors;
    }


    /**
     * Count the number of KT battalions available in the Army.
     *
     * @param theUnit the Army.
     * @return the number of KT battalions.
     */
    public static int countArmyTotalKT(final ArmyDTO theUnit) {
        int countKT = 0;
        final List<CorpDTO> lstCorps = new ArrayList<CorpDTO>(theUnit.getCorps().values());
        for (final CorpDTO theCorp : lstCorps) {
            final List<BrigadeDTO> lstBrigades = new ArrayList<BrigadeDTO>(theCorp.getBrigades().values());
            for (final BrigadeDTO brigade : lstBrigades) {
                for (BattalionDTO movedItem : brigade.getBattalions()) {
                    if (movedItem.getEmpireArmyType().getType().equals("Kt")) {
                        countKT++;
                    }
                }
            }
        }
        return countKT;
    }


    /**
     * Get the neutral number of sectors that it can conquer.
     *
     * @param theUnit the entity subject to this movement order.
     * @return the total number of neutral sectors.
     */
    public static int getArmyMaxNeutralConquers(final ArmyDTO theUnit) {
        int totBrigades = 0;
        final List<CorpDTO> lstCorps = new ArrayList<CorpDTO>(theUnit.getCorps().values());
        for (final CorpDTO theCorp : lstCorps) {
            final List<BrigadeDTO> lstBrigades = new ArrayList<BrigadeDTO>(theCorp.getBrigades().values());
            totBrigades += lstBrigades.size();

            // Troops that lose a battle cannot move over/capture enemy territory for one turn
            for (BrigadeDTO brigade : lstBrigades) {
                for (BattalionDTO battalion : brigade.getBattalions()) {
                    if (battalion.getHasLost()) {
                        return 0;
                    }
                }
            }
        }

        int maxSectors = 0;
        if (theUnit.getRegionId() == EUROPE) {
            // You need a commander to conquer in Europe
            if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0
                    && totBrigades >= 1) {
                maxSectors = 3;
            }
        } else {
            // You need KT troops
            // Count KT troops
            final int countKT = countArmyTotalKT(theUnit);

            // Needs 5 KT battalions to conquer 2 neutral sector
            if (countKT >= 5) {
                maxSectors = 2;

            } else if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                // If you have a commander
                maxSectors = 2;

            } else if (countKT >= 2) {
                // Needs 2 KT battalions to conquer 1 neutral sector
                maxSectors = 1;
            }
        }

        return maxSectors;
    }


    /**
     * CONQUER CALCULATORS FOR CORPS
     **/


    /**
     * Get the number of sectors that it can conquer.
     *
     * @param theUnit the entity subject to this movement order.
     * @return the total number of sectors.
     */
    public static int getCorpsMaxConquers(final CorpDTO theUnit) {
        final List<BrigadeDTO> lstBrigades = new ArrayList<BrigadeDTO>(theUnit.getBrigades().values());
        final int totBrigades = lstBrigades.size();

        // Troops that lose a battle cannot move over/capture enemy territory for one turn
        for (BrigadeDTO brigade : lstBrigades) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getHasLost()) {
                    return 0;
                }
            }
        }

        int maxSectors = 0;
        if (theUnit.getRegionId() == EUROPE) {
            // You need a commander to conquer in Europe
            if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                if (totBrigades >= 5 && totBrigades <= 9) {
                    maxSectors = 1;

                } else if (totBrigades >= 10 && totBrigades <= 14) {
                    maxSectors = 2;

                } else if (totBrigades >= 15) {
                    maxSectors = 3;
                }
            }
        } else {
            // Either you need a commander
            if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                if (totBrigades >= 4) {
                    maxSectors = 2;

                } else if (totBrigades >= 1) {
                    maxSectors = 1;
                }
            } else {
                // You need KT troops
                // Count KT troops
                final int countKT = countCorpsTotalKT(theUnit);

                // Needs 2 KT battalions to conquer 1 sector
                if (totBrigades >= 4 && countKT >= 5) {
                    return 2;

                } else if (countKT >= 2) {
                    return 1;
                }
            }
        }

        return maxSectors;
    }

    /**
     * Get the neutral number of sectors that it can conquer.
     *
     * @param theUnit the entity subject to this movement order.
     * @return the total number of neutral sectors.
     */
    public static int getCorpsMaxNeutralConquers(final CorpDTO theUnit) {
        final List<BrigadeDTO> lstBrigades = new ArrayList<BrigadeDTO>(theUnit.getBrigades().values());
        final int totBrigades = lstBrigades.size();

        // Troops that lose a battle cannot move over/capture enemy territory for one turn
        for (BrigadeDTO brigade : lstBrigades) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getHasLost()) {
                    return 0;
                }
            }
        }

        int maxSectors = 0;
        if (theUnit.getRegionId() == EUROPE) {
            // You need a commander to conquer in Europe
            if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()
                    && totBrigades >= 1) {
                maxSectors = 3;
            }
        } else {
            // You need KT troops
            // Count KT troops
            final int countKT = countCorpsTotalKT(theUnit);

            // Needs 5 KT battalions to conquer 2 neutral sector
            if (countKT >= 5) {
                maxSectors = 2;

            } else if (theUnit.getCommander() != null && theUnit.getCommander().getId() != 0 && !theUnit.getCommander().getInTransit()) {
                // If you have a commander
                maxSectors = 2;

            } else if (countKT >= 2) {
                // Needs 2 KT battalions to conquer 1 neutral sector
                maxSectors = 1;
            }
        }

        return maxSectors;
    }


    /**
     * Count the number of KT battalions available in the Corp.
     *
     * @param theUnit the Corp.
     * @return the number of KT battalions.
     */
    public static int countCorpsTotalKT(final CorpDTO theUnit) {
        int countKT = 0;
        final List<BrigadeDTO> lstBrigades = new ArrayList<BrigadeDTO>(theUnit.getBrigades().values());
        for (final BrigadeDTO brigade : lstBrigades) {
            for (BattalionDTO movedItem : brigade.getBattalions()) {
                if (movedItem.getEmpireArmyType().getType().equals("Kt")) {
                    countKT++;
                }
            }
        }
        return countKT;
    }


    /**
     * CONQUER CALCULATORS FOR BRIGADES
     **/

    /**
     * Get the number of sectors that it can conquer.
     *
     * @param theUnit the entity subject to this movement order.
     * @return the total number of sectors.
     */
    public static int getBrigadeMaxConquers(final BrigadeDTO theUnit) {
        int maxSectors = 0;

        if (theUnit.getRegionId() != EUROPE) {
            // Troops that lose a battle cannot move over/capture enemy territory for one turn
            for (BattalionDTO battalion : theUnit.getBattalions()) {
                if (battalion.getHasLost()) {
                    return 0;
                }
            }

            // You need KT troops
            // Count KT troops
            final int countKT = countBrigadeTotalKT(theUnit);

            // Needs 2 KT battalions to conquer 1 neutral sector
            if (countKT >= 2) {
                maxSectors = 1;
            }
        }

        return maxSectors;
    }


    /**
     * Get the neutral number of sectors that it can conquer.
     *
     * @param theUnit the entity subject to this movement order.
     * @return the total number of neutral sectors.
     */
    public static int getBrigadeMaxNeutralConquers(final BrigadeDTO theUnit) {
        int maxSectors = 0;

        if (theUnit.getRegionId() != EUROPE) {
            // Troops that lose a battle cannot move over/capture enemy territory for one turn
            for (BattalionDTO battalion : theUnit.getBattalions()) {
                if (battalion.getHasLost()) {
                    return 0;
                }
            }

            // You need KT troops
            // Count KT troops
            final int countKT = countBrigadeTotalKT(theUnit);

            // Needs 5 KT battalions to conquer 2 neutral sector
            if (countKT >= 5) {
                maxSectors = 2;

            } else if (countKT >= 2) {
                // Needs 2 KT battalions to conquer 1 neutral sector
                maxSectors = 1;
            }
        }

        return maxSectors;
    }


    /**
     * Count the number of KT battalions available in the Corp.
     *
     * @param theUnit the Corp.
     * @return the number of KT battalions.
     */
    public static int countBrigadeTotalKT(final BrigadeDTO theUnit) {
        int countKT = 0;
        for (BattalionDTO movedItem : theUnit.getBattalions()) {
            if (movedItem.getEmpireArmyType().getType().equals("Kt")) {
                countKT++;
            }
        }

        return countKT;
    }


}
