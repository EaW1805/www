package com.eaw1805.www.controllers.remote.hotspot.economy;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeCitiesApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, ArmyConstants {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_EXCHF,
            ORDER_EXCHS
    };

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(TradeCitiesApplyChangesProcessor.class);

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    private transient List<TradeCityDTO> tcList = new ArrayList<TradeCityDTO>();

    private final Map<Integer, TradeCityDTO> idCitiesMap = new HashMap<Integer, TradeCityDTO>();

    public TradeCitiesApplyChangesProcessor(final int scenarioId,
                                            final int thisGame,
                                            final int thisNation,
                                            final int thisTurn) {
        super(scenarioId, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        tcList = (List<TradeCityDTO>) dbData;
        for (final TradeCityDTO tCity : tcList) {
            idCitiesMap.put(tCity.getId(), tCity);
        }

        orders = (List<OrderDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<TradeCityDTO> processChanges() {
        for (OrderDTO order : orders) {
            if (Integer.parseInt(order.getParameter1()) == TRADECITY) {
                final int tradeCityId = Integer.parseInt(order.getParameter2());
                final int tpe = Integer.parseInt(order.getParameter5());
                final int quantity = Integer.parseInt(order.getParameter6());
                if (quantity > 0) {
                    sellGood(tradeCityId, tpe, quantity);
                }

            } else if (Integer.parseInt(order.getParameter3()) == TRADECITY) {
                try {
                    final int tradeCityId = Integer.parseInt(order.getParameter4());
                    final int tpe = Integer.parseInt(order.getParameter5());
                    final int quantity = Integer.parseInt(order.getParameter6());
                    if (quantity > 0) {
                        buyGood(tradeCityId, tpe, quantity);
                    }

                } catch (Exception ex) {
                    LOGGER.error("Failed to process order " + order.getOrderId());
                }
            }
        }
        return tcList;
    }

    /**
     * Method that buys a certain good from a trade city
     * and calculates the city funds
     *
     * @param tradeCityId the Id of the trade city.
     * @param goodType    the type of the good.
     * @param quantity    the quantity of the good.
     * @return true for a successful transaction.
     */
    private boolean buyGood(final int tradeCityId, final int goodType, final int quantity) {
        final TradeCityDTO tcity = idCitiesMap.get(tradeCityId);
        tcity.getSoldGoods().put(goodType, tcity.getSoldGoods().get(goodType) + quantity);
        return true;
    }


    /**
     * Method that sells a certain good from a trade city
     * and calculates the city funds
     *
     * @param tradeCityId the Id of the trade city.
     * @param goodType    the type of the good.
     * @param quantity    the quantity of the good.
     * @return true for a successful transaction.
     */
    private boolean sellGood(final int tradeCityId, final int goodType, final int quantity) {
        final TradeCityDTO tcity = idCitiesMap.get(tradeCityId);
        tcity.getBoughtGoods().put(goodType, tcity.getBoughtGoods().get(goodType) + quantity);
        return true;
    }

}
