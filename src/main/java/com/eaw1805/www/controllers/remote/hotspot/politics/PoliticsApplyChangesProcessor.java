package com.eaw1805.www.controllers.remote.hotspot.politics;

import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoliticsApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor, RelationConstants {

    /**
     * a log4j logger to print messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(PoliticsApplyChangesProcessor.class);

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_POLITICS
    };

    private transient List<OrderDTO> orders = new ArrayList<OrderDTO>();

    /**
     * A list containing all the relations of our target nation.
     */
    private transient List<RelationDTO> proccRelations = new ArrayList<RelationDTO>();

    /**
     * A hash map containing all the Sectors and their corresponding production sites.
     */
    private transient final Map<Integer, RelationDTO> relationsMap = new HashMap<Integer, RelationDTO>();

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public PoliticsApplyChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super(thisScenario, thisGame, thisNation, thisTurn);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        proccRelations = (List<RelationDTO>) dbData;
        for (RelationDTO relation : proccRelations) {
            relationsMap.put(relation.getTargetNationId(), relation);
        }

        orders = (List<OrderDTO>) chData;
    }

    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        // do nothing
    }

    public List<RelationDTO> processChanges() {
        for (OrderDTO order : orders) {
            changeNationRelationship(Integer.parseInt(order.getParameter1()),
                    Integer.parseInt(order.getParameter2()),
                    Integer.parseInt(order.getParameter3()));

        }
        return proccRelations;
    }

    /**
     * Method to change the nation relationship to a new one.
     *
     * @param targetNation the nation affected.
     * @param newRelId     the new relation level.
     * @param action       the action to perform.
     */
    public void changeNationRelationship(final int targetNation, final int newRelId, final int action) {
        try {
            final RelationDTO tgRel = relationsMap.get(targetNation);
            tgRel.setNextRoundRelation(newRelId);
            tgRel.setWarAction(action);

        } catch (Exception Ex) {
            LOGGER.error("Failed to change the relation due to unknown reasons.");
        }
    }

}
