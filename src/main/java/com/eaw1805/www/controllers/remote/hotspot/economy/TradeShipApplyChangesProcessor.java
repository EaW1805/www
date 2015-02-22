package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeShipApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants, GoodConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_EXCHF,
            ORDER_EXCHS
    };

    /**
     * A list containing all the available fleets as taken from the service
     */
    private transient List<FleetDTO> dbFleetlist = new ArrayList<FleetDTO>();

    /**
     * A hash map containing all the ship ids and their corresponding objects
     */
    private transient Map<Integer, ShipDTO> idShipMap = new HashMap<Integer, ShipDTO>();

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     */
    public TradeShipApplyChangesProcessor(final int thisScenario, final int gameId, final int nationId, final int turn) {
        super(thisScenario, gameId, nationId, turn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        // clear any previous additions to collections
        dbFleetlist = (List<FleetDTO>) dbData;
        orders = (List<OrderDTO>) chData;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        idShipMap = (Map<Integer, ShipDTO>) dbData;
    }

    public List<FleetDTO> processChanges() {
        for (final OrderDTO order : orders) {
            if (Integer.parseInt(order.getParameter1()) == SHIP) {
                final int trainId = Integer.parseInt(order.getParameter2());
                final int tpe = Integer.parseInt(order.getParameter5());
                final int secondPart = Integer.parseInt(order.getParameter3());
                final int quantity = Integer.parseInt(order.getParameter6());
                if (quantity > 0) {
                    unLoadGood(tpe, trainId, quantity, order.getNationId() != getNationId());
                    if (secondPart == TRADECITY) {
                        loadGood(GOOD_MONEY, trainId, Integer.parseInt(order.getTemp1()), order.getNationId() != getNationId());
                    }
                }
            }

            if (Integer.parseInt(order.getParameter3()) == SHIP) {
                final int trainId = Integer.parseInt(order.getParameter4());
                final int tpe = Integer.parseInt(order.getParameter5());
                final int secondPart = Integer.parseInt(order.getParameter1());
                final int quantity = Integer.parseInt(order.getParameter6());

                if (quantity > 0) {
                    loadGood(tpe, trainId, quantity, order.getNationId() != getNationId());
                    if (secondPart == TRADECITY) {
                        unLoadGood(GOOD_MONEY, trainId, Integer.parseInt(order.getTemp1()), order.getNationId() != getNationId());
                    }
                }
            }
        }

        return dbFleetlist;
    }

    /**
     * Method that returns a ship with a given shipId.
     *
     * @param shipId the shipId of the ship.
     * @return the ship or null when the ship is not
     *         found.
     */
    private ShipDTO getShipById(final int shipId) {
        if (idShipMap.containsKey(shipId)) {
            return idShipMap.get(shipId);
        }

        return null;
    }

    /**
     * Method that loads a good in a ship
     *
     * @param goodId the shipId of the good
     * @param shipId the shipId of the ship
     * @param qte    the quantity of the goods we want to load
     */
    private void loadGood(final int goodId, final int shipId, final int qte, final boolean allied) {
        final ShipDTO ship = getShipById(shipId);
        if (ship != null) {
            final Map<Integer, StoredGoodDTO> storedGoods = ship.getGoodsDTO();
            if (storedGoods.containsKey(goodId)) {
                storedGoods.get(goodId).setQte(storedGoods.get(goodId).getQte() + qte);
                if (allied) {
                    ship.getOriginalGoodsDTO().get(goodId).setQte(ship.getOriginalGoodsDTO().get(goodId).getQte() + qte);
                }
            }
        }
    }

    /**
     * Method that loads a good in a ship
     *
     * @param goodId the shipId of the good
     * @param shipId the shipId of the ship
     * @param qte    the quantity of the goods we want to load
     */
    private void unLoadGood(final Integer goodId, final int shipId, final int qte, final boolean allied) {
        final ShipDTO ship = getShipById(shipId);
        if (ship != null) {
            final Map<Integer, StoredGoodDTO> storedGoods = ship.getGoodsDTO();
            if (storedGoods.containsKey(goodId)) {
                storedGoods.get(goodId).setQte(storedGoods.get(goodId).getQte() - qte);
                if (allied) {
                    ship.getOriginalGoodsDTO().get(goodId).setQte(ship.getOriginalGoodsDTO().get(goodId).getQte() - qte);
                }
            }
        }
    }

}

