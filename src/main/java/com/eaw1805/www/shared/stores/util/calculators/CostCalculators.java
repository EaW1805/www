package com.eaw1805.www.shared.stores.util.calculators;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.AdminCommandPoints;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;

/**
 * @author tsakygr
 *         A class that provides various functions that return the cost
 *         of the target object,order or command
 */
public final class CostCalculators
        implements GoodConstants, OrderConstants, NationConstants, RegionConstants, ProductionSiteConstants {

    /**
     * Our instance of the calculator.
     * used when static reference is not enough
     */
    private static CostCalculators ourInstance = null;

    /**
     * Method returning the class object.
     *
     * @return an instance of the CostCalculator singleton.
     */
    public static CostCalculators getInstance() {
        if (ourInstance == null) {
            ourInstance = new CostCalculators();
        }
        return ourInstance;
    }

    /**
     * Identify if sector is a home region, inside sphere of influence, or outside of the receiving nation.
     *
     * @param sector   the sector to examine.
     * @param receiver the receiving nation.
     * @return 1 if home region, 2 if in sphere of influence, 3 if outside.
     */
    public static int getSphere(final SectorDTO sector, final NationDTO receiver) {
        final char thisNationCodeLower = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
        final char thisSectorCodeLower = String.valueOf(sector.getPoliticalSphere()).toLowerCase().charAt(0);
        int sphere = 1;

        // In colonies the costs are not multiplied
        if (sector.getRegionId() != EUROPE) {
            return sphere;
        }

        // Check if this is not home region
        if (thisNationCodeLower != thisSectorCodeLower) {
            sphere = 2;

            // Check if this is outside sphere of influence
            if (receiver.getSphereOfInfluence().toLowerCase().indexOf(thisSectorCodeLower) < 0) {
                sphere = 3;
            }
        }

        return sphere;
    }

    /**
     * Method that calculates the cost of our new brigade
     *
     * @param brigade    whose cost we want to define
     * @param multiplier the cost is doubled or trippled based on the soi.
     * @return the cost object that describes the brigade
     */
    public static OrderCostDTO getBrigadeCost(final BrigadeDTO brigade, final int multiplier) {
        final OrderCostDTO cost = new OrderCostDTO();

        if (brigade.getRegionId() == EUROPE) {
            if (multiplier == 1) {
                cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_B_BATT));
                cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_B_BATT));

            } else if (multiplier == 2) {
                cost.setNumericCost(GOOD_AP, 3);
                cost.setNumericCost(GOOD_CP, 3);

            } else {
                cost.setNumericCost(GOOD_AP, 5);
                cost.setNumericCost(GOOD_CP, 5);
            }

        } else {
            cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_B_BATT_COL));
            cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_B_BATT_COL));
        }

        for (BattalionDTO battalion : brigade.getBattalions()) {
            if (battalion != null && battalion.getEmpireArmyType().getType() != null) {

                int headcount = 800;
                if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                        || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                        || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                    headcount = 1000;
                }

                cost.setNumericCost(GOOD_MONEY, cost.getNumericCost(GOOD_MONEY) + multiplier * battalion.getEmpireArmyType().getCost());
                cost.setNumericCost(GOOD_INPT, cost.getNumericCost(GOOD_INPT) + multiplier * battalion.getEmpireArmyType().getIndPt());
                cost.setNumericCost(GOOD_PEOPLE, cost.getNumericCost(GOOD_PEOPLE) + headcount);
                if (battalion.getEmpireArmyType().needsHorse()) {
                    cost.setNumericCost(GOOD_HORSE, cost.getNumericCost(GOOD_HORSE) + headcount);
                }
            }
        }

        return cost;
    }

    public static OrderCostDTO getIncreaseHeadcountCost(final BrigadeDTO brigade, final int newSoldiers) {
        final OrderCostDTO cost = new OrderCostDTO();
        if (brigade == null) {
            return cost;
        }

        // Determine the maximum headcount
        int totalMoney = 0;
        int totalInpt = 0;
        int totalHorse = 0;
        int totalPeople = 0;

        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsArmy()) ? 2 : 1;

        final SectorDTO thisSector = RegionStore.getInstance().getSectorByStartingPosition(brigade);
        final int multiplier = doubleCosts * getSphere(thisSector, DataStore.getInstance().getNationById(brigade.getNationId()));

        for (BattalionDTO battalion : brigade.getBattalions()) {

            int headcount = 800;
            if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                headcount = 1000;
            }

            if (battalion.getHeadcount() < headcount) {
                int trainSoldiers = newSoldiers;
                if (battalion.getHeadcount() + trainSoldiers > headcount) {
                    trainSoldiers = headcount - battalion.getHeadcount();
                }

                if (WarehouseStore.getInstance().getWareHouseByRegion(brigade.getRegionId()).getGoodsDTO().get(GOOD_PEOPLE).getQte() < trainSoldiers) {
                    trainSoldiers = WarehouseStore.getInstance().getWareHouseByRegion(brigade.getRegionId()).getGoodsDTO().get(GOOD_PEOPLE).getQte();
                }

                if (trainSoldiers > 0) {
                    totalMoney += multiplier * trainSoldiers * battalion.getEmpireArmyType().getCost() / headcount;
                    totalInpt += multiplier * trainSoldiers * battalion.getEmpireArmyType().getIndPt() / headcount;
                    if (battalion.getEmpireArmyType().needsHorse()) {
                        totalHorse += trainSoldiers;
                    }
                    totalPeople += trainSoldiers;
                }
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_HEADCNT));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_HEADCNT));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        cost.setNumericCost(GOOD_HORSE, totalHorse);
        cost.setNumericCost(GOOD_PEOPLE, totalPeople);
        return cost;
    }

    public static OrderCostDTO getIncreaseHeadcountCorpsCost(final CorpDTO corps, final int newSoldiers) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;
        int totalHorse = 0;
        int totalPeople = 0;

        if (corps != null && corps.getBrigades() != null) {
            for (BrigadeDTO brigade : corps.getBrigades().values()) {
                final OrderCostDTO temp = getIncreaseHeadcountCost(brigade, newSoldiers);
                totalMoney += temp.getNumericCost(GOOD_MONEY);
                totalInpt += temp.getNumericCost(GOOD_INPT);
                totalHorse += temp.getNumericCost(GOOD_HORSE);
                totalPeople += temp.getNumericCost(GOOD_PEOPLE);
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_HEADCNT));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_HEADCNT));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        cost.setNumericCost(GOOD_HORSE, totalHorse);
        cost.setNumericCost(GOOD_PEOPLE, totalPeople);
        return cost;
    }

    public static OrderCostDTO getIncreaseHeadcountArmyCost(final ArmyDTO army, final int newSoldiers) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;
        int totalHorse = 0;
        int totalPeople = 0;

        if (army != null && army.getCorps() != null) {
            for (CorpDTO corp : army.getCorps().values()) {
                final OrderCostDTO temp = getIncreaseHeadcountCorpsCost(corp, newSoldiers);
                totalMoney += temp.getNumericCost(GOOD_MONEY);
                totalInpt += temp.getNumericCost(GOOD_INPT);
                totalHorse += temp.getNumericCost(GOOD_HORSE);
                totalPeople += temp.getNumericCost(GOOD_PEOPLE);
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_HEADCNT_ARMY));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_HEADCNT_ARMY));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        cost.setNumericCost(GOOD_HORSE, totalHorse);
        cost.setNumericCost(GOOD_PEOPLE, totalPeople);
        return cost;
    }

    public static OrderCostDTO getTrainBrigadeCost(final BrigadeDTO brigade) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;
        if (!brigade.isUpgraded()
                && brigade.getBattalions() != null) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getId() == 0) {
                    continue;
                }
                if (battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
                    // Determine the maximum headcount
                    int headcount = 800;
                    if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                            || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                            || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                        headcount = 1000;
                    }

                    // Calculate required amount of money
                    totalMoney += (int) (battalion.getHeadcount() * battalion.getEmpireArmyType().getCost() / (10d * headcount));
                    totalInpt += (int) (battalion.getHeadcount() * battalion.getEmpireArmyType().getIndPt() / (10d * headcount));
                }
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        return cost;
    }

    public static OrderCostDTO getTrainCorpsCost(final CorpDTO corp) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;

        if (corp != null && corp.getBrigades() != null) {
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                final OrderCostDTO temp = getTrainBrigadeCost(brigade);
                totalMoney += temp.getNumericCost(GOOD_MONEY);
                totalInpt += temp.getNumericCost(GOOD_INPT);
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        return cost;
    }

    public static OrderCostDTO getTrainArmyCost(final ArmyDTO army) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;

        if (army != null && army.getCorps() != null) {
            for (CorpDTO corp : army.getCorps().values()) {
                final OrderCostDTO temp = getTrainCorpsCost(corp);
                totalMoney += temp.getNumericCost(GOOD_MONEY);
                totalInpt += temp.getNumericCost(GOOD_INPT);
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        return cost;
    }

    public static OrderCostDTO getCrackEliteBrigadeCost(final BrigadeDTO brigade) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;
        if (!brigade.isUpgradedToElite()
                && brigade.getBattalions() != null) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getEmpireArmyType().isCrack()
                        && battalion.getEmpireArmyType().getUpgradeEliteTo() > 0
                        && battalion.getExperience() > battalion.getEmpireArmyType().getMaxExp()) {
                    totalMoney += battalion.getEmpireArmyType().getCost();
                    totalInpt += battalion.getEmpireArmyType().getIndPt();
                }
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        return cost;
    }

    public static OrderCostDTO getCrackEliteCorpsCost(final CorpDTO corp) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;

        if (corp != null && corp.getBrigades() != null) {
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                final OrderCostDTO temp = getCrackEliteBrigadeCost(brigade);
                totalMoney += temp.getNumericCost(GOOD_MONEY);
                totalInpt += temp.getNumericCost(GOOD_INPT);
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        return cost;
    }

    public static OrderCostDTO getCrackEliteArmyCost(final ArmyDTO army) {
        final OrderCostDTO cost = new OrderCostDTO();
        int totalMoney = 0;
        int totalInpt = 0;

        if (army != null && army.getCorps() != null) {
            for (CorpDTO corp : army.getCorps().values()) {
                final OrderCostDTO temp = getCrackEliteCorpsCost(corp);
                totalMoney += temp.getNumericCost(GOOD_MONEY);
                totalInpt += temp.getNumericCost(GOOD_INPT);
            }
        }

        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_INC_EXP));
        cost.setNumericCost(GOOD_MONEY, totalMoney);
        cost.setNumericCost(GOOD_INPT, totalInpt);
        return cost;
    }

    /**
     * Method that returns cost of a specific starting army type
     *
     * @param armyType the type of the army
     * @param modifier the cost is doubled or tripled based on the soi.
     * @return the cost object that describes the army type
     */
    public static OrderCostDTO getArmyTypeCost(final ArmyTypeDTO armyType, final int modifier) {
        final OrderCostDTO cost = new OrderCostDTO();
        int headcount = 800;
        if (armyType.getNationId() == NationConstants.NATION_MOROCCO
                || armyType.getNationId() == NationConstants.NATION_OTTOMAN
                || armyType.getNationId() == NationConstants.NATION_EGYPT) {
            headcount = 1000;
        }

        cost.setNumericCost(GOOD_MONEY, armyType.getCost() * modifier);
        cost.setNumericCost(GOOD_INPT, armyType.getIndPt() * modifier);
        cost.setNumericCost(GOOD_PEOPLE, headcount);
        if (armyType.needsHorse()) {
            cost.setNumericCost(GOOD_HORSE, headcount);
        }
        return cost;
    }

    /**
     * Method that calculates the cost of forming a federation of units
     *
     * @param type of the order of the formed group
     * @return the cost object that describes the form action
     */
    public static OrderCostDTO getFormFederationCost(final int type) {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(type));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(type));
        return cost;
    }

    /**
     * Method that calculates the cost of moving a unit or a group
     *
     * @param type the order of group moving
     * @return the cost object that describes the movement action
     */
    public static OrderCostDTO getMovementCost(final int type) {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(type));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(type));
        return cost;
    }

    /**
     * Method that calculates the cost of building a production site
     *
     * @param prodSite the production site type.
     * @param demolish -- if the production site needs to be demolished.
     * @return the cost object that describes the building of the production site
     */
    public static OrderCostDTO getProductionSiteCost(final int regionId, final ProductionSiteDTO prodSite, final boolean demolish) {
        final OrderCostDTO cost = new OrderCostDTO();
        if (demolish) {
            cost.setNumericCost(GOOD_MONEY, 5000);
            cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_D_PRODS));
            cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_D_PRODS));
        } else {
            cost.setNumericCost(GOOD_MONEY, prodSite.getCost());
            cost.setNumericCost(GOOD_PEOPLE, 2000);
            cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_B_PRODS));
            cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_B_PRODS));

            switch (prodSite.getId()) {
                case PS_BARRACKS_FS:
                    cost.setNumericCost(GOOD_INPT, 500);

                    if (regionId == EUROPE) {
                        cost.setNumericCost(GOOD_STONE, 1500);

                    } else {
                        cost.setNumericCost(GOOD_WOOD, 1500);
                    }
                    break;

                case PS_BARRACKS_FM:
                    cost.setNumericCost(GOOD_INPT, 1000);
                    cost.setNumericCost(GOOD_STONE, 4000);
                    break;

                case PS_BARRACKS_FL:
                    cost.setNumericCost(GOOD_INPT, 5000);
                    cost.setNumericCost(GOOD_STONE, 16000);

                    break;

                case PS_BARRACKS_FH:
                    cost.setNumericCost(GOOD_INPT, 15000);
                    cost.setNumericCost(GOOD_STONE, 45000);
                    break;

                case PS_MINT:
                    cost.setNumericCost(GOOD_PEOPLE, 2000);
                    break;

                case PS_FACTORY:
                case PS_MINE:
                    cost.setNumericCost(GOOD_PEOPLE, 4000);
                    break;

                default:
                    //do nothing here
            }

        }
        return cost;
    }

    /**
     * Method that calculates the cost of building a ship
     *
     * @param ship the ship to check for calculating the cost.
     * @return the cost object that describes the building of the production site
     */
    public static OrderCostDTO getShipCost(final ShipDTO ship) {
        // Double costs custom game option
        final int doubleCosts = (GameStore.getInstance().isDoubleCostsNavy()) ? 2 : 1;

        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_MONEY, doubleCosts * ship.getType().getCost());
        cost.setNumericCost(GOOD_PEOPLE, ship.getType().getCitizens());
        cost.setNumericCost(GOOD_WOOD, doubleCosts * ship.getType().getWood());
        cost.setNumericCost(GOOD_FABRIC, doubleCosts * ship.getType().getFabrics());
        cost.setNumericCost(GOOD_INPT, doubleCosts * ship.getType().getIndPt());
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_B_SHIP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_B_SHIP));
        return cost;
    }

    /**
     * Method that calculates the cost of increasing or decreasing a sector's population value.
     *
     * @param sector   the current sector.
     * @param increase true if we are asking for increase.
     * @return the cost object of the increase/decrease command.
     */
    public static OrderCostDTO getIncDecPopCost(final SectorDTO sector, final boolean increase) {
        final OrderCostDTO cost = new OrderCostDTO();

        if (increase) {
            final boolean needsWood = ((sector.getRegionId() != EUROPE)
                    || (sector.getNationId() == NATION_RUSSIA)) && (sector.getPopulation() < 3);

            // Reduce goods
            cost.setNumericCost(GOOD_PEOPLE, Sector.REQ_CITIZENS[sector.getPopulation()]);

            if (needsWood) {
                cost.setNumericCost(GOOD_WOOD, Sector.REQ_STONE[sector.getPopulation()]);

            } else {
                cost.setNumericCost(GOOD_STONE, Sector.REQ_STONE[sector.getPopulation()]);
            }

            // Determine AP cost
            int apCost = Math.round((float) sector.getPopulation() / (float) 3);
            if (apCost == 0) {
                apCost = 1;
            }
            cost.setNumericCost(GOOD_AP, apCost);

        } else {
            double people = 0;
            if (sector.getPopulation() > 0) {
                if (sector.getTerrainId() == TerrainConstants.TERRAIN_D) {
                    people = 500;

                } else {
                    people = Sector.POP_LEVELS[sector.getPopulation()] - Sector.POP_LEVELS[sector.getPopulation() - 1];
                }
            }

            final int sphere = getSphere(sector, sector.getNationDTO());
            if (sector.getRegionId() == EUROPE) {
                switch (sphere) {
                    case 1:
                        people *= HOME;
                        break;

                    case 2:
                        people *= SPHERE;
                        break;

                    case 3:
                    default:
                        people *= FOREIGN;
                        break;
                }

            } else {
                people *= COLONIAL;
            }

            // Determine AP cost
            int apCost = Math.round((float) sector.getPopulation() / (float) 2);
            if (apCost == 0) {
                apCost = 1;
            }

            cost.setNumericCost(GOOD_PEOPLE, (int) -people);
            cost.setNumericCost(GOOD_AP, apCost);
        }

        return cost;
    }

    /**
     * Method that calculates handing over costs.
     *
     * @param type       is the order type to differentiate between ship and territory.
     * @param population the population of the sector.
     * @return the cost object of the handover command.
     */
    public static OrderCostDTO getHandOverCost(final int type, final int population) {
        final OrderCostDTO cost = new OrderCostDTO();
        if (type == ORDER_HOVER_SHIP) {
            cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_HOVER_SHIP));
            cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_HOVER_SHIP));

        } else {
            if (population == 0) {// If the population is 0 then the administration points equal 1
                cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_HOVER_SHIP));
                cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_HOVER_SHIP));
            } else {// Administration points is the population equivalent
                cost.setNumericCost(GOOD_AP, population);
                cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_HOVER_SHIP));

            }
        }
        return cost;
    }

    /**
     * Method that returns the monetary cost of a baggage train
     *
     * @return the cost object of the build baggage train order
     */
    public static OrderCostDTO getBaggageTrainCost() {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_MONEY, 300000);
        cost.setNumericCost(GOOD_PEOPLE, 1000);
        cost.setNumericCost(GOOD_WOOD, 1500);
        cost.setNumericCost(GOOD_HORSE, 2000);
        cost.setNumericCost(GOOD_INPT, 500);
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_B_BTRAIN));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_B_BTRAIN));
        return cost;
    }

    public static OrderCostDTO getScuttleBaggageTrainCost(final int condition) {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_WOOD, (int) (-1500 * condition / 200d));
        cost.setNumericCost(GOOD_HORSE, (int) (-2000 * condition / 200d));
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_SCUTTLE_BTRAIN));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_SCUTTLE_BTRAIN));
        return cost;
    }

    /**
     * Method that returns the monetary cost of repairing a baggage train
     *
     * @param condition the % condition of the baggage train
     * @return the cost object of the repair baggage train order
     */
    public static OrderCostDTO getBaggageTrainRepairCost(final int condition) {
        final OrderCostDTO cost = new OrderCostDTO();
        final double percentage = (100f - condition) / 100f;
        cost.setNumericCost(GOOD_MONEY, (int) (percentage * 300000));
        cost.setNumericCost(GOOD_PEOPLE, (int) (percentage * 1000));
        cost.setNumericCost(GOOD_WOOD, (int) (percentage * 1500));
        cost.setNumericCost(GOOD_HORSE, (int) (percentage * 2000));
        cost.setNumericCost(GOOD_INPT, (int) (percentage * 500));
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_R_BTRAIN));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_R_BTRAIN));
        return cost;
    }

    /**
     * Method that returns the monetary cost of repairing a ship.
     *
     * @param condition the condition of the ship we want to repair.
     * @param ship      the type of the ship tat we want to repair that also contains the costs.
     * @return the cost object of the repair ship order/
     */
    public static OrderCostDTO getShipRepairCost(final int condition, final ShipDTO ship) {
        final OrderCostDTO cost = new OrderCostDTO();
        ShipTypeDTO shipType = ship.getType();
        final double percentage = (100d - condition) / 100d;
        cost.setNumericCost(GOOD_MONEY, (int) (percentage * shipType.getCost()));
        cost.setNumericCost(GOOD_PEOPLE, shipType.getCitizens() - ship.getMarines());
        cost.setNumericCost(GOOD_WOOD, (int) (percentage * shipType.getWood()));
        cost.setNumericCost(GOOD_FABRIC, (int) (percentage * shipType.getFabrics()));
        cost.setNumericCost(GOOD_INPT, (int) (percentage * shipType.getIndPt()));
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_R_SHP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_R_SHP));
        return cost;
    }

    public static OrderCostDTO getFleetRepairCost(final FleetDTO fleet) {
        final OrderCostDTO cost = new OrderCostDTO();
        int moneyCost = 0;
        int peopleCost = 0;
        int woodCost = 0;
        int fabricCost = 0;
        int inPtCost = 0;

        for (ShipDTO ship : fleet.getShips().values()) {
            final OrderCostDTO temp = getShipRepairCost(ship.getCondition(), ship);
            moneyCost += temp.getNumericCost(GOOD_MONEY);
            peopleCost += temp.getNumericCost(GOOD_PEOPLE);
            woodCost += temp.getNumericCost(GOOD_WOOD);
            fabricCost += temp.getNumericCost(GOOD_FABRIC);
            inPtCost += temp.getNumericCost(GOOD_INPT);
        }

        cost.setNumericCost(GOOD_MONEY, moneyCost);
        cost.setNumericCost(GOOD_PEOPLE, peopleCost);
        cost.setNumericCost(GOOD_WOOD, woodCost);
        cost.setNumericCost(GOOD_FABRIC, fabricCost);
        cost.setNumericCost(GOOD_INPT, inPtCost);
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_R_FLT));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_R_FLT));
        return cost;
    }

    public static OrderCostDTO getShipRenameCost() {
        return new OrderCostDTO();
    }

    public static OrderCostDTO getScuttleShipCost(final ShipDTO ship) {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_SCUTTLE_SHIP));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_SCUTTLE_SHIP));
        // Calculate amount of Industrial Points that will be recovered from the destruction
        final int inPt = (int) (ship.getType().getIndPt() * ship.getCondition() / 200d);
        // Calculate amount of Fabrics that will be recovered from the destruction
        final int fabrics = (int) (ship.getType().getFabrics() * ship.getCondition() / 300d);

        cost.setNumericCost(GOOD_INPT, inPt * -1);
        cost.setNumericCost(GOOD_FABRIC, fabrics * -1);

        return cost;
    }

    /**
     * Method that returns the cost of dismissing a commander
     *
     * @param commander the target commander
     * @return the cost object describing the transaction
     */

    public static OrderCostDTO getDissCommCost(final CommanderDTO commander) {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_DISS_COM));
        cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_DISS_COM));
        return cost;
    }

    /**
     * Method that returns the cost of hiring a commander
     *
     * @param commander the target commander
     * @return the cost object describing the transaction
     */
    public static OrderCostDTO getHireCommCost(final CommanderDTO commander, final int regionId) {
        final OrderCostDTO cost = new OrderCostDTO();
        if (regionId == EUROPE) {
            cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_HIRE_COM));
            cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_HIRE_COM));
        } else {
            cost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(ORDER_HIRE_COM_COL));
            cost.setNumericCost(GOOD_CP, AdminCommandPoints.P_COM.get(ORDER_HIRE_COM_COL));
        }
        return cost;
    }


    public static OrderCostDTO getTaxationCost(final int indPt, final int money, final int col, final int gems) {
        final OrderCostDTO cost = new OrderCostDTO();
        cost.setNumericCost(GOOD_INPT, indPt);
        cost.setNumericCost(GOOD_MONEY, money);
        cost.setNumericCost(GOOD_COLONIAL, col);
        cost.setNumericCost(GOOD_GEMS, gems);
        return cost;
    }

}
