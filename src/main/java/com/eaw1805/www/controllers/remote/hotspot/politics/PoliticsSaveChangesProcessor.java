package com.eaw1805.www.controllers.remote.hotspot.politics;

import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PoliticsSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, RelationConstants {

    private List<RelationDTO> relations;

    /**
     * Default constructor.
     *
     * @param gameId   the game of the order.
     * @param nationId the owner of the order.
     * @param turn     the turn of the order.
     */
    public PoliticsSaveChangesProcessor(final int thisScenario, final int gameId, final int nationId, final int turn) {
        super(thisScenario, gameId, nationId, turn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        relations = (List<RelationDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<OrderDTO> processChanges() {
        final List<OrderDTO> newPoliticsOrders = new ArrayList<OrderDTO>();
        for (final RelationDTO relation : relations) {
            if ((relation.getRelation() != relation.getNextRoundRelation())
                    || relation.getWarAction() != NO_ACTION) {
                newPoliticsOrders.add(createOrderPolitics(relation));
            }
        }
        return newPoliticsOrders;
    }

    private OrderDTO createOrderPolitics(final RelationDTO relation) {
        return new OrderDTO(getGameId(), getNationId(), getTurn(), ORDER_POLITICS, 0, 0,
                String.valueOf(relation.getTargetNationId()),
                String.valueOf(relation.getNextRoundRelation()),
                String.valueOf(relation.getWarAction()), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

}
