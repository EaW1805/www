package com.eaw1805.www.controllers.remote.hotspot.army;

import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupNewBrigadeOrders
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_B_BATT
    };

    /**
     * A map of sector ids and new brigades.
     */
    private transient final Map<Integer, List<BrigadeDTO>> newBrigades = new HashMap<Integer, List<BrigadeDTO>>();

    private transient final Map<Integer, ArmyTypeDTO> mapArmyTypes = new HashMap<Integer, ArmyTypeDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    private transient final EmpireRpcServiceImpl service;

    private transient int regionId;

    /**
     * Default constructor.
     *
     * @param thisGame             the game of the order.
     * @param thisNation           the owner of the order.
     * @param thisTurn             the turn of the order.
     * @param empireRpcServiceImpl the calling function.
     */
    public SetupNewBrigadeOrders(final int thisScenario, final int thisGame,
                                 final int thisNation,
                                 final int thisTurn,
                                 final EmpireRpcServiceImpl empireRpcServiceImpl) {
        super(thisScenario, thisGame, thisNation, thisTurn);
        this.service = empireRpcServiceImpl;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chOrders) {
        final List<ArmyTypeDTO> armyTypes = (List<ArmyTypeDTO>) dbData;
        for (final ArmyTypeDTO armyType : armyTypes) {
            mapArmyTypes.put(armyType.getIntId(), armyType);
        }
        orders = (List<OrderDTO>) chOrders;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        newBrigades.putAll((Map<Integer, List<BrigadeDTO>>) dbData);
    }

    public List<Map<Integer, List<BrigadeDTO>>> processChanges() {
        final List<Map<Integer, List<BrigadeDTO>>> dummyList = new ArrayList<Map<Integer, List<BrigadeDTO>>>();

        for (OrderDTO order : orders) {
            final BrigadeDTO newBrigade = new BrigadeDTO();
            newBrigade.setNationId(getNationId());
            newBrigade.setName(order.getParameter9());
            newBrigade.setOriginalName(order.getParameter9());

            try {
                newBrigade.setBrigadeId(Integer.parseInt(order.getTemp1()));

            } catch (Exception ex) {
                newBrigade.setBrigadeId(0);
            }

            newBrigade.setBattalions(new ArrayList<BattalionDTO>());

            final BattalionDTO battalionDto1 = getBattalionByArmyTypeId(order.getParameter2(), 1);
            final BattalionDTO battalionDto2 = getBattalionByArmyTypeId(order.getParameter3(), 2);
            final BattalionDTO battalionDto3 = getBattalionByArmyTypeId(order.getParameter4(), 3);
            final BattalionDTO battalionDto4 = getBattalionByArmyTypeId(order.getParameter5(), 4);
            final BattalionDTO battalionDto5 = getBattalionByArmyTypeId(order.getParameter6(), 5);
            final BattalionDTO battalionDto6 = getBattalionByArmyTypeId(order.getParameter7(), 6);
            final BattalionDTO battalionDto7 = getBattalionByArmyTypeId(order.getParameter8(), 7);
            if (battalionDto1 != null) {
                newBrigade.getBattalions().add(battalionDto1);
            }
            if (battalionDto2 != null) {
                newBrigade.getBattalions().add(battalionDto2);
            }
            if (battalionDto3 != null) {
                newBrigade.getBattalions().add(battalionDto3);
            }
            if (battalionDto4 != null) {
                newBrigade.getBattalions().add(battalionDto4);
            }
            if (battalionDto5 != null) {
                newBrigade.getBattalions().add(battalionDto5);
            }
            if (battalionDto6 != null) {
                newBrigade.getBattalions().add(battalionDto6);
            }
            if (battalionDto7 != null) {
                newBrigade.getBattalions().add(battalionDto7);
            }

            final Sector sector = service.getSectorManager().getByID(Integer.parseInt(order.getParameter1()));
            newBrigade.setRegionId(sector.getPosition().getRegion().getId());
            newBrigade.setX(sector.getPosition().getX());
            newBrigade.setY(sector.getPosition().getY());

            createBrigade(Integer.parseInt(order.getParameter1()), newBrigade);
            try {
                newBrigade.setBrigadeId(Integer.parseInt(order.getTemp1()));

            } catch (Exception ex) {
                newBrigade.setBrigadeId(0);
            }
        }

        dummyList.add(newBrigades);
        return dummyList;
    }

    // Create Brigade
    private Map<Integer, List<BrigadeDTO>> createBrigade(final int sectorId, final BrigadeDTO newBrig) {
        if (!newBrigades.containsKey(sectorId)) {
            newBrigades.put(sectorId, new ArrayList<BrigadeDTO>());
        }

        final int newId = orders.size() + 1;

        //add it to the map so when the the mini order panel tries to find - something that happens inside the addNewOrder
        //method - it will work properly.
        newBrig.setBrigadeId(newId);
        newBrigades.get(sectorId).add(newBrig);
        return newBrigades;
    }

    private BattalionDTO getBattalionByArmyTypeId(final String armyTypeID, final int order) {
        ArmyTypeDTO armyTypeDto = mapArmyTypes.get(Integer.parseInt(armyTypeID));
        if (armyTypeDto == null) {
            return null;
        }
        final BattalionDTO newBattalion = new BattalionDTO();
        newBattalion.setEmpireArmyType(armyTypeDto);

        int headcount = 800;
        if (armyTypeDto.getNationId() == NationConstants.NATION_MOROCCO
                || armyTypeDto.getNationId() == NationConstants.NATION_OTTOMAN
                || armyTypeDto.getNationId() == NationConstants.NATION_EGYPT) {
            headcount = 1000;
        }

        newBattalion.setHeadcount(headcount);
        newBattalion.setExperience(1);
        newBattalion.setId(-1);
        newBattalion.setOrder(order);
        return newBattalion;
    }

    public void setRegionId(final int regionId) {
        this.regionId = regionId;
    }

    public int getRegionId() {
        return regionId;
    }

}
