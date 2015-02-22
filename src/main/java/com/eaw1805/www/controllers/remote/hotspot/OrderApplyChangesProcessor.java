package com.eaw1805.www.controllers.remote.hotspot;

import com.eaw1805.data.constants.AdminCommandPoints;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.collections.DataCollection;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.converters.SectorConverter;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.army.Brigade;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("restriction")
public class OrderApplyChangesProcessor
        extends AbstractChangesProcessor
        implements OrderConstants, GoodConstants {

    /**
     * The orders and the corresponding costs.
     */
    private transient final Map<Integer, List<ClientOrderDTO>> clientOrders;

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();
    private transient final Map<Integer, ArmyTypeDTO> armyTypes = new HashMap<Integer, ArmyTypeDTO>();

    private transient final Map<Integer, ShipTypeDTO> shipTypes = new HashMap<Integer, ShipTypeDTO>();

    private transient final EmpireRpcServiceImpl service;

    private transient final DataCollection prodAndNatSites;


    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public OrderApplyChangesProcessor(final int scenarioId,
                                      final int thisGame,
                                      final int thisNation,
                                      final int thisTurn,
                                      final EmpireRpcServiceImpl empireRpcServiceImpl) {
        super(scenarioId, thisGame, thisNation, thisTurn);
        service = empireRpcServiceImpl;

        for (ArmyTypeDTO armyTypeDTO : service.getArmyTypes(getScenarioId(), getNationId())) {
            armyTypes.put(armyTypeDTO.getIntId(), armyTypeDTO);
        }

        for (ShipTypeDTO shipTypeDTO : service.getShipTypes(getScenarioId(), getNationId())) {
            shipTypes.put(shipTypeDTO.getIntId(), shipTypeDTO);
        }

        prodAndNatSites = service.getNaturalResAndProdSites(getScenarioId());
        clientOrders = new TreeMap<Integer, List<ClientOrderDTO>>();
    }

    @SuppressWarnings({"unchecked"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        orders = (List<OrderDTO>) chOrders;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public Map<Integer, List<ClientOrderDTO>> processChanges() {
        for (final OrderDTO order : orders) {
            OrderCostDTO orderCost = new OrderCostDTO();
            int regionId = 1;
            String name = "";
            String comment = "";
            final int[] ids = new int[9];
            for (int i = 0; i < 9; i++) {
                ids[i] = 0;
            }

            switch (order.getType()) {
                case ORDER_B_BATT: {
                    final Sector sector = service.getSectorManager().getByID(Integer.parseInt(order.getParameter1()));
                    final int multiplier = getSphere(sector, service.nationManager.getByID(getNationId()));

                    final BrigadeDTO brig = createBrigadeFromOrder(order);
                    orderCost = CostCalculators.getBrigadeCost(brig, multiplier);
                    name = order.getParameter9();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = brig.getBrigadeId();
                    regionId = brig.getRegionId();
                    break;
                }

                case ORDER_ADDTO_BRIGADE:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    break;

                case ORDER_ADDTO_ARMY:
                case ORDER_ADDTO_CORP:
                case ORDER_ADDTO_FLT:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    break;

                case ORDER_HIRE_COM:
                    orderCost.setNumericCost(GOOD_AP, Integer.parseInt(order.getTemp1()));
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    break;

                case ORDER_ARMY_COM:
                    name = order.getTemp1() + "-" + order.getParameter2();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    break;

                case ORDER_CORP_COM:
                    name = order.getTemp1() + "-" + order.getParameter2();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    break;

                case ORDER_LEAVE_COM:
                    ids[0] = Integer.parseInt(order.getParameter2());
                    ids[1] = 0;
                    break;

                case ORDER_DISS_COM:
                    orderCost.setNumericCost(GOOD_AP, Integer.parseInt(order.getTemp1()));
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    break;

                case ORDER_B_ARMY:
                case ORDER_B_FLT:
                    orderCost = CostCalculators.getFormFederationCost(order.getType());
                    name = order.getParameter2();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    break;

                case ORDER_B_CORP:
                    orderCost = CostCalculators.getFormFederationCost(order.getType());
                    name = order.getParameter3();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    break;

                case ORDER_ADD_BATT: {
                    final Brigade brigade = service.getBrigadeManager().getByID(Integer.parseInt(order.getParameter1()));
                    final Sector sector = service.getSectorManager().getByPosition(brigade.getPosition());
                    final int multiplier = getSphere(sector, service.getNationManager().getByID(getNationId()));
                    orderCost = CostCalculators.getArmyTypeCost(getArmyTypeById(Integer.parseInt(order.getParameter2())), multiplier);
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    regionId = Integer.parseInt(order.getTemp1());
                    break;
                }

                case ORDER_B_BTRAIN:
                    orderCost = CostCalculators.getBaggageTrainCost();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    regionId = Integer.parseInt(order.getTemp1());
                    break;

                case ORDER_R_BTRAIN:
                    orderCost = CostCalculators.getBaggageTrainRepairCost(100 - Integer.parseInt(order.getParameter3()));
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    regionId = Integer.parseInt(order.getTemp1());
                    break;

                case ORDER_B_PRODS:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());

                    SectorDTO sectorDTO = SectorConverter.convert(service.getSectorManager().getByID(ids[0]));
                    regionId = sectorDTO.getRegionId();

                    orderCost = CostCalculators.getProductionSiteCost(regionId, prodAndNatSites.getProdSite(Integer.parseInt(order.getParameter2())), false);
                    break;

                case ORDER_D_PRODS:
                    ids[0] = Integer.parseInt(order.getParameter1());

                    sectorDTO = SectorConverter.convert(service.getSectorManager().getByID(ids[0]));
                    regionId = sectorDTO.getRegionId();

                    orderCost = CostCalculators.getProductionSiteCost(regionId, null, true);
                    break;

                case ORDER_B_SHIP:
                    ShipDTO ship = createShipFromOrder(order);
                    orderCost = CostCalculators.getShipCost(ship);
                    name = order.getParameter3();
                    ids[0] = Integer.parseInt(order.getTemp1());
                    ids[1] = Integer.parseInt(order.getParameter1());
                    ids[2] = Integer.parseInt(order.getParameter2());
                    regionId = ship.getRegionId();
                    break;

                case ORDER_R_FLT:
                    final String[] costValues = order.getTemp2().split("!");
                    for (String costValue : costValues) {
                        final String[] keyVal = costValue.split(":");
                        orderCost.setNumericCost(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
                    }
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    ids[3] = Integer.parseInt(order.getParameter4());
                    comment = order.getTemp2();

                    regionId = ids[2];
                    break;

                case ORDER_R_SHP:
                    ship = service.getShipById(getScenarioId(), Integer.parseInt(order.getParameter1()));
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    ids[3] = Integer.parseInt(order.getParameter4());
                    orderCost = CostCalculators.getShipRepairCost(ship.getCondition(), ship);
                    regionId = Integer.parseInt(order.getTemp1());
                    break;

                case ORDER_D_ARMY:
                case ORDER_D_BATT:
                case ORDER_D_BRIG:
                case ORDER_D_CORP:
                case ORDER_D_FLT:
                    name = order.getTemp1();
                    ids[0] = Integer.parseInt(order.getParameter1());
                    break;

                case ORDER_FM_ARMY:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    break;

                case ORDER_FM_BRIG:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    break;

                case ORDER_FM_CORP:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    break;

                case ORDER_HOVER_SEC:
                case ORDER_HOVER_SHIP:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    orderCost = CostCalculators.getHandOverCost(order.getType(), Integer.parseInt(order.getTemp1()));
                    break;

                case ORDER_INC_EXP:
                case ORDER_INC_EXP_CORPS:
                case ORDER_INC_EXP_ARMY:
                    if (order.getTemp2() != null && !order.getTemp2().isEmpty()) {
                        final String[] expCostValues = order.getTemp2().split("!");
                        for (String costValue : expCostValues) {
                            final String[] keyVal = costValue.split(":");
                            orderCost.setNumericCost(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
                        }
                    }
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    regionId = ids[2];
                    break;

                case ORDER_INC_HEADCNT:
                case ORDER_INC_HEADCNT_CORPS:
                case ORDER_INC_HEADCNT_ARMY:
                    if (order.getTemp2() != null && !order.getTemp2().isEmpty()) {
                        final String[] upHeadCostValues = order.getTemp2().split("!");
                        for (String costValue : upHeadCostValues) {
                            final String[] keyVal = costValue.split(":");
                            orderCost.setNumericCost(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
                        }
                    }
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    regionId = ids[2];
                    break;

                case ORDER_INC_POP: {
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    regionId = ids[1];

                    sectorDTO = SectorConverter.convert(service.getSectorManager().getByID(ids[0]));
                    orderCost = CostCalculators.getIncDecPopCost(sectorDTO, true);
                    break;
                }

                case ORDER_DEC_POP: {
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    regionId = ids[1];

                    final SectorDTO sector = SectorConverter.convert(service.getSectorManager().getByID(ids[0]));
                    orderCost = CostCalculators.getIncDecPopCost(sector, false);
                    break;
                }

                case ORDER_M_UNIT:
                    orderCost = CostCalculators.getMovementCost(Integer.parseInt(order.getParameter5()));
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter5());
                    break;

                case ORDER_MRG_BATT:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    break;

                case ORDER_TAXATION:
                    orderCost = CostCalculators.getTaxationCost(Integer.parseInt(order.getTemp3()),
                            Integer.parseInt(order.getTemp4()),
                            Integer.parseInt(order.getTemp1()),
                            Integer.parseInt(order.getTemp2()));
                    break;

                case ORDER_EXCHF:
                case ORDER_EXCHS:
                    orderCost.setNumericCost(Integer.parseInt(order.getParameter5()), Integer.parseInt(order.getParameter6()));
                    orderCost.setNumericCost(1, Integer.parseInt(order.getTemp1()));
                    if (Integer.parseInt(order.getParameter1()) == ArmyConstants.TRADECITY
                            || Integer.parseInt(order.getParameter3()) == ArmyConstants.TRADECITY) {
                        orderCost.setNumericCost(GOOD_AP, AdminCommandPoints.P_ADM.get(order.getType()));
                    }

                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    ids[3] = Integer.parseInt(order.getParameter4());
                    ids[4] = Integer.parseInt(order.getParameter5());
                    ids[5] = Integer.parseInt(order.getParameter6());
                    regionId = Integer.parseInt(order.getParameter9());
                    break;

                case ORDER_LOAD_TROOPSF:
                case ORDER_LOAD_TROOPSS:
                    if (order.getTemp3() != null && !order.getTemp3().isEmpty()) {//recover cp cost
                        orderCost.setNumericCost(GOOD_CP, Integer.parseInt(order.getTemp3()));
                    }
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    ids[3] = Integer.parseInt(order.getParameter4());
                    ids[4] = Integer.parseInt(order.getParameter5());
                    ids[5] = Integer.parseInt(order.getParameter6());
                    break;

                case ORDER_UNLOAD_TROOPSF:
                case ORDER_UNLOAD_TROOPSS:
                    if (order.getTemp3() != null && !order.getTemp3().isEmpty()) {//recover cp cost
                        orderCost.setNumericCost(GOOD_CP, Integer.parseInt(order.getTemp3()));
                    }
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    ids[3] = Integer.parseInt(order.getParameter4());
                    ids[4] = Integer.parseInt(order.getParameter5());
                    ids[5] = Integer.parseInt(order.getTemp1());
                    ids[6] = Integer.parseInt(order.getTemp2());
                    break;

                case ORDER_REN_SHIP:
                case ORDER_REN_BRIG:
                case ORDER_REN_COMM:
                case ORDER_REN_ARMY:
                case ORDER_REN_CORP:
                case ORDER_REN_FLT:
                case ORDER_REN_BARRACK:
                case ORDER_REN_BTRAIN:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    name = order.getParameter2();
                    break;

                case ORDER_SCUTTLE_BTRAIN:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    final BaggageTrainDTO scuttledTrain = service.getBaggageTrainById(getScenarioId(), ids[0]);
                    if (scuttledTrain != null) {
                        orderCost = CostCalculators.getScuttleBaggageTrainCost(scuttledTrain.getCondition());
                    }
                    break;
                case ORDER_SCUTTLE_SHIP:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    final ShipDTO scuttledShip = service.getShipById(getScenarioId(), ids[0]);
                    if (scuttledShip != null) {
                        orderCost = CostCalculators.getScuttleShipCost(scuttledShip);
                    }
                    break;
                case ORDER_POLITICS:
                    ids[0] = Integer.parseInt(order.getParameter1());
                    ids[1] = Integer.parseInt(order.getParameter2());
                    ids[2] = Integer.parseInt(order.getParameter3());
                    break;
                default:
                    // do nothing
            }

            addNewOrder(order.getType(), orderCost, regionId, name, ids, order.getPosition(), comment);

        }
        return clientOrders;
    }


    /**
     * Identify if sector is a home region, inside sphere of influence, or outside of the receiving nation.
     *
     * @param sector   the sector to examine.
     * @param receiver the receiving nation.
     * @return 1 if home region, 2 if in sphere of influence, 3 if outside.
     */
    protected final int getSphere(final Sector sector, final Nation receiver) {
        final char thisNationCodeLower = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
        final char thisSectorCodeLower = String.valueOf(sector.getPoliticalSphere()).toLowerCase().charAt(0);
        int sphere = 1;

        // Τα χ2 και x3 ισχύουν μόνο για τα Ευρωπαικά αν χτίζονται στο SoI ή εκτός SoI
        if (sector.getPosition().getRegion().getId() != RegionConstants.EUROPE) {
            return 1;
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

//    /**
//     * Identify if sector is a home region, inside sphere of influence, or outside of the receiving nation.
//     *
//     * @param sector   the sector to examine.
//     * @param receiver the receiving nation.
//     * @return 1 if home region, 2 if in sphere of influence, 3 if outside.
//     */
//    protected int getSphere(final Sector sector, final Nation receiver) {
//        final char thisNationCodeLower = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
//        final char thisSectorCodeLower = String.valueOf(sector.getPoliticalSphere()).toLowerCase().charAt(0);
//        int sphere = 1;
//
//        if (sector.getPosition().getRegion().getId() != RegionConstants.EUROPE) {
//            return sphere;
//        }
//
//        // Check if this is not home region
//        if (thisNationCodeLower != thisSectorCodeLower) {
//            sphere = 2;
//
//            final char thisNationCode = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
//
//            // Check if this is outside sphere of influence
//            if (sector.getNation().getSphereOfInfluence().indexOf(thisNationCode) < 0) {
//                sphere = 3;
//            }
//        }
//
//        return sphere;
//    }

    /**
     * Method that adds a new order to our order Map
     *
     * @param typeId      The type of the order we want to add.
     * @param orderCost   The calculated cost of the order.
     * @param regionId    the region of the warehouse.
     * @param name        the name of the order.
     * @param identifiers the id of the order.
     * @param position    The position of the order.
     * @param comment     the comment of the order.
     * @return 0 if there are no available funds 1 if there are
     */
    private int addNewOrder(final int typeId, final OrderCostDTO orderCost,
                            final int regionId,
                            final String name,
                            final int[] identifiers,
                            final int position,
                            final String comment) {
        int priority = position;
        if (clientOrders.get(typeId) == null) {
            clientOrders.put(typeId, new ArrayList<ClientOrderDTO>());

        } else {
            priority = clientOrders.get(typeId).size();
        }

        if (position == 0) {
            priority = clientOrders.get(typeId).size();
        }

        final ClientOrderDTO order = new ClientOrderDTO();
        order.setOrderTypeId(typeId);
        order.setPriority(priority);
        order.setCosts(orderCost);
        order.setName(name);
        order.setComment(comment);

        for (int index = 0; index < identifiers.length; index++) {
            order.setIdentifier(index, identifiers[index]);
        }

        order.setRegionId(regionId);

        clientOrders.get(typeId).add(order);
        return 1;
    }

    /**
     * Creates new brigade from the order.
     *
     * @param order the order to use.
     * @return the new brigade object.
     */
    private BrigadeDTO createBrigadeFromOrder(final OrderDTO order) {
        final BrigadeDTO newBrigade = new BrigadeDTO();

        newBrigade.setNationId(getNationId());
        newBrigade.setName(order.getParameter9());
        try {
            newBrigade.setBrigadeId(Integer.parseInt(order.getTemp1()));

        } catch (Exception ex) {
            if (clientOrders.get(ORDER_ADD_BATT) == null) {
                newBrigade.setBrigadeId(0);

            } else {
                newBrigade.setBrigadeId(clientOrders.get(ORDER_ADD_BATT).size());
            }
        }

        newBrigade.setBattalions(new ArrayList<BattalionDTO>());

        final BattalionDTO battalionDto1 = getBattalionByArmyTypeId(order.getParameter2(), 1);
        final BattalionDTO battalionDto2 = getBattalionByArmyTypeId(order.getParameter3(), 2);
        final BattalionDTO battalionDto3 = getBattalionByArmyTypeId(order.getParameter4(), 3);
        final BattalionDTO battalionDto4 = getBattalionByArmyTypeId(order.getParameter5(), 4);
        final BattalionDTO battalionDto5 = getBattalionByArmyTypeId(order.getParameter6(), 5);
        final BattalionDTO battalionDto6 = getBattalionByArmyTypeId(order.getParameter7(), 6);
        final BattalionDTO battalionDto7 = getBattalionByArmyTypeId(order.getParameter8(), 7);
        newBrigade.getBattalions().add(battalionDto1);
        newBrigade.getBattalions().add(battalionDto2);
        newBrigade.getBattalions().add(battalionDto3);
        newBrigade.getBattalions().add(battalionDto4);
        newBrigade.getBattalions().add(battalionDto5);
        newBrigade.getBattalions().add(battalionDto6);
        newBrigade.getBattalions().add(battalionDto7);

        final Sector sector = service.getSectorManager().getByID(Integer.parseInt(order.getParameter1()));
        newBrigade.setRegionId(sector.getPosition().getRegion().getId());
        newBrigade.setX(sector.getPosition().getX());
        newBrigade.setY(sector.getPosition().getY());

        return newBrigade;
    }

    /**
     * Create new battalion.
     *
     * @param armyTypeID the battalion type.
     * @param order      the order ID.
     * @return the new battalio dto.
     */
    private BattalionDTO getBattalionByArmyTypeId(final String armyTypeID, final int order) {
        final ArmyTypeDTO armyTypeDto = getArmyTypeById(Integer.parseInt(armyTypeID));
        final BattalionDTO newBattalion = new BattalionDTO();
        newBattalion.setEmpireArmyType(armyTypeDto);

        int headcount = 800;
        if (newBattalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                || newBattalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                || newBattalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
            headcount = 1000;
        }

        newBattalion.setExperience(1);
        newBattalion.setHeadcount(headcount);
        newBattalion.setId(-1);
        newBattalion.setOrder(order);
        return newBattalion;
    }

    /**
     * Creates new ship from the order.
     *
     * @param order the order describing the new ship.
     * @return the new ship dto.
     */
    public ShipDTO createShipFromOrder(final OrderDTO order) {
        final ShipDTO newShip = new ShipDTO();
        newShip.setNationId(getNationId());
        newShip.setName(order.getParameter3());
        newShip.setId(Integer.parseInt(order.getTemp1()));

        final ShipTypeDTO shipTypeDTO = getShipTypeId(Integer.parseInt(order.getParameter2()));
        newShip.setTypeId(shipTypeDTO.getIndPt());
        newShip.setType(shipTypeDTO);

        final Sector sector = service.getSectorManager().getByID(Integer.parseInt(order.getParameter1()));
        newShip.setRegionId(sector.getPosition().getRegion().getId());
        newShip.setX(sector.getPosition().getX());
        newShip.setY(sector.getPosition().getY());
        return newShip;
    }

    /**
     * @param shipTypeID the type of the ship.
     * @return the new ship type dto.
     */
    private ShipTypeDTO getShipTypeId(final int shipTypeID) {
        final ShipTypeDTO shipTypeDto = new ShipTypeDTO();
        if (shipTypes.containsKey(shipTypeID)) {
            return shipTypes.get(shipTypeID);
        }
        return shipTypeDto;
    }

    public ArmyTypeDTO getArmyTypeById(final int armyTypeId) {
        final ArmyTypeDTO armyTypeDto = new ArmyTypeDTO();
        if (armyTypes.containsKey(armyTypeId)) {
            return armyTypes.get(armyTypeId);
        }
        return armyTypeDto;
    }

}
