package empire.webapp.shared.stores;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import empire.data.constants.OrderConstants;
import empire.data.constants.RelationConstants;
import empire.data.dto.web.OrderCostDTO;
import empire.data.dto.web.RelationDTO;
import empire.webapp.client.events.loading.LoadEventManager;
import empire.webapp.client.remote.EmpireRpcService;
import empire.webapp.client.remote.EmpireRpcServiceAsync;
import empire.webapp.client.views.SpyRelationsView;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.shared.stores.economy.OrderStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RelationsStore
        implements OrderConstants, RelationConstants {

    private final static EmpireRpcServiceAsync empireService = GWT.create(EmpireRpcService.class);

    //A list containing all the relations of our target nation
    private final transient List<RelationDTO> relationsList = new ArrayList<RelationDTO>();

    //A hash map containing all the Sectors and their corresponding production sites
    private final transient Map<Integer, RelationDTO> relationsMap = new HashMap<Integer, RelationDTO>();
    private final transient Map<Integer, List<RelationDTO>> nationToRelations = new HashMap<Integer, List<RelationDTO>>();

    private boolean isClient = false;

    // Our instance of the Manager
    private static transient RelationsStore ourInstance = null;

    // Initialize the relations as we took them from the DataBase
    public void initDbRelations(final List<RelationDTO> relationsList) {
        try {
            nationToRelations.put(GameStore.getInstance().getNationId(), relationsList);
            this.relationsList.clear();
            this.relationsMap.clear();
            this.relationsList.addAll(relationsList);

            for (final RelationDTO relation : this.relationsList) {
                relationsMap.put(relation.getTargetNationId(), relation);
            }
            if (isClient) {
                LoadEventManager.relationsLoaded();
            }
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to initialize relations due to unexpected reason", false);
        }
    }

    public List<RelationDTO> getNationRelations(final int nationId, final SpyRelationsView view) {
        //if we already have it... retrieve it.
        if (nationToRelations.containsKey(nationId)) {
            return nationToRelations.get(nationId);
        }

        //otherwise get it from database  and cache it
        empireService.getNationsRelations(GameStore.getInstance().getScenarioId(),
        GameStore.getInstance().getGameId(), nationId, GameStore.getInstance().getTurn(),
        new AsyncCallback<List<RelationDTO>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //ignore this case
            }

            @Override
            public void onSuccess(List<RelationDTO> result) {
                nationToRelations.put(nationId, result);
                view.initRelations(result);
            }
        });

        return null;

    }



    // Method returning the economy manager if already initialized
    // or the a new instance
    public static RelationsStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new RelationsStore();
        }
        return ourInstance;
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
            int[] ids = new int[3];
            ids[0] = targetNation;

            final RelationDTO tgRel = getRelationsMap().get(targetNation);
            if ((tgRel.getRelation() == newRelId && (tgRel.getRelation() == REL_WAR && action == NO_ACTION)) ||
                    (tgRel.getRelation() == newRelId && tgRel.getRelation() != REL_WAR)) {
                ids[1] = tgRel.getNextRoundRelation();
                ids[2] = tgRel.getWarAction();
                OrderStore.getInstance().removeOrder(ORDER_POLITICS, ids);

            } else if (tgRel.getNextRoundRelation() != newRelId ||
                    (tgRel.getRelation() == REL_WAR && action != NO_ACTION && action != tgRel.getWarAction())) {
                //remove any previous order
                ids[1] = tgRel.getNextRoundRelation();
                ids[2] = tgRel.getWarAction();
                OrderStore.getInstance().removeOrder(ORDER_POLITICS, ids);

                //add the new one
                ids[1] = newRelId;
                ids[2] = action;
                OrderStore.getInstance().addNewOrder(ORDER_POLITICS, new OrderCostDTO(), 1, "", ids, 0, "");
            }

            tgRel.setNextRoundRelation(newRelId);
            tgRel.setWarAction(action);
        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to change the relation due to unknown reasons.", false);
        }
    }

    // Returns our relation by the nation id
    public Integer getOriginalRelationByNationId(final int nationId) {
        if (nationId > 0 && nationId != GameStore.getInstance().getNationId()) {
            return getRelationsMap().get(nationId).getRelation();
        } else {
            return 0;
        }
    }

    public final boolean isMine(final int nationId) {
        return nationId == GameStore.getInstance().getNationId();
    }

    public final boolean isAlly(final int nationId) {
        return getOriginalRelationByNationId(nationId) == REL_ALLIANCE;
    }

    // Returns true if the target nation is at
    // war with us
    public boolean isAtWar(final int targetNation) {
        return getRelationsMap().get(targetNation).getTheirRelation() == REL_WAR;
    }

    public String getNameRelation(final int relation) {
        switch (relation) {
            case REL_ALLIANCE:
                return "Alliance";

            case REL_PASSAGE:
                return "Right of passage";

            case REL_TRADE:
                return "Trading";

            case REL_COLONIAL_WAR:
                return "Colonial War";

            case REL_WAR:
                return "At War";

            default:
                break;
        }
        return "unknown";
    }

    // Returns the name of the relationship
    // with the current target nation
    public String getRelName(final int nationId) {
        final int relation = getRelationsMap().get(nationId).getTheirRelation();
        switch (relation) {
            case REL_ALLIANCE:
                return "Alliance";

            case REL_PASSAGE:
                return "Right of passage";

            case REL_TRADE:
                return "Trading";

            case REL_COLONIAL_WAR:
                return "Colonial War";

            case REL_WAR:
                return "At War";

            default:
                break;
        }
        return null;
    }

    public Map<Integer, RelationDTO> getRelationsMap() {
        return relationsMap;
    }

    public List<RelationDTO> getRelationsList() {
        return relationsList;
    }

    public void setIsClient(boolean isClient) {
        this.isClient = isClient;

    }


}
