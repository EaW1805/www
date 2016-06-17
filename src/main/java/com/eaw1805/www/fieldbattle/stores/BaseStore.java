package com.eaw1805.www.fieldbattle.stores;


import com.google.gwt.core.client.GWT;
import com.eaw1805.data.dto.collections.FieldData;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Fieldbattle store containing the basic informations for the client.
 */
public class BaseStore implements ClearAble {

    /**
     * Our instance of the BaseStore
     */
    private static transient BaseStore ourInstance = null;



    public static enum STATE {
        NORMAL,
        PLACING_ARMY
    }

    public final static EmpireFieldBattleRpcServiceAsync Service = GWT.create(EmpireFieldBattleRpcService.class);


    STATE state = STATE.NORMAL;

    /**
     * Method returning the game store
     *
     * @return the GameStore
     */
    public static BaseStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new BaseStore();
        }
        return ourInstance;
    }

    private int gameId = 3;
    private int nationId;
    private int scenarioId;
    private int battleId;
    private int round;
    private String facebookId;

    private boolean showMoveLines = true;
    private boolean showFireLines = true;

    final Map<Integer, NationDTO> nations = new HashMap<Integer, NationDTO>();
    private List<Integer> enemyNations = new ArrayList<Integer>();
    private List<Integer> alliedNations = new ArrayList<Integer>();
    Set<Integer> editedOrders = new HashSet<Integer>();
    private int side;
    private boolean gameEnded;
    private int winner;
    private String title;
    boolean sideReady;
    boolean standAlone;

    public void initVariables(int scenarioId, int battleId, int nationId, int round, final int side, final String title, final boolean sideReady,
                              final boolean gameEnded, final int winner, final boolean standAlone, final String facebookId) {
        this.scenarioId = scenarioId;
        this.battleId = battleId;
        this.nationId = nationId;
        this.round = round;
        this.side = side;
        this.title = title;
        this.sideReady = sideReady;
        this.gameEnded = gameEnded;
        this.winner = winner;
        this.standAlone = standAlone;
        this.facebookId = facebookId;
    }

    private final String[] colors = {"blue", "brown", "red", "yellow", "orange", "grey", "black",
            "grey", "cyan", "black", "pink", "purple", "grey", "white",
            "GreenYellow", "MintCream", "DarkSlateGray"};

    public String getColorByNation(int nationId) {
        return colors[nationId - 1];
    }

    public void initNations(FieldData data) {

        for (NationDTO nation : data.getEnemyNations()) {

            enemyNations.add(nation.getNationId());
        }
        for (NationDTO nation : data.getAlliedNations()) {
            alliedNations.add(nation.getNationId());
        }
        for (NationDTO nation : data.getAllNations()) {
            nations.put(nation.getNationId(), nation);
        }
    }

    public void orderEdited(final int brigadeId) {
        editedOrders.add(brigadeId);
    }

    public boolean canEditMore() {
        return !isMiddleRound() || editedOrders.size() < CommanderStore.getInstance().getNumberOfOrdersAllowChange();
    }

    public Set<Integer> getEditedOrders() {
        return editedOrders;
    }

    public void orderUndoEdited(final int brigadeId) {
        editedOrders.remove(Integer.valueOf(brigadeId));
    }

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }


    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<Integer> getEnemyNations() {
        return enemyNations;
    }

    public List<Integer> getAlliedNations() {
        return alliedNations;
    }

    public boolean isNationAllied(final int nationId) {
        if (this.nationId == nationId) {
            return true;
        }
        for (Integer curNation : alliedNations) {
            if (nationId == curNation) {
                return true;
            }
        }
        return false;
    }

    public void setEnemyNations(List<Integer> enemyNations) {
        this.enemyNations = enemyNations;
    }

    public NationDTO getNationById(int nationId) {
        return nations.get(nationId);
    }

    public int getScenarioIdNormalized () {
        if (scenarioId == 1805) {
            return 2;
        } else if (scenarioId == 1802) {
            return 1;
        } else if (scenarioId == 1800) {
            return -3;
        }
        return -3;
    }

    public int getScenarioId() {
//        return scenarioId;
        return getScenarioIdNormalized();
    }

    public int getBattleId() {
        return battleId;
    }

    public int getRound() {
        return round;
    }

    public boolean isShowFireLines() {
        return showFireLines;
    }

    public void setShowFireLines(boolean showFireLines) {
        this.showFireLines = showFireLines;
    }

    public boolean isShowMoveLines() {
        return showMoveLines;
    }

    public void setShowMoveLines(boolean showMoveLines) {
        this.showMoveLines = showMoveLines;
    }

    public boolean isMiddleRound() {
        return round > 0;
    }

    public boolean isStartRound() {
        return round == -1;
    }

    public int getSide() {
        return side;
    }

    public String getTitle() {
        return title;
    }

    public boolean isLastHalfRound(final int halfRound) {
        return (round + 1) * 2 == halfRound;
    }

    public boolean isSideReady() {
        return sideReady;
    }

    public void setSideReady(boolean sideReady) {
        this.sideReady = sideReady;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public int getWinner() {
        return winner;
    }

    public boolean wonTheBattle() {
        return side == winner + 1;
    }

    public boolean lostTheBattle() {
        return side != winner + 1 && winner != -1;
    }

    public boolean drawTheBattle() {
        return winner == -1;
    }

    public boolean isStandAlone() {
        return standAlone;
    }

    public void setStandAlone(final boolean value) {
        standAlone = value;
    }

    public native boolean isMobileDevice() /*-{
        var isMob = navigator.userAgent.match(/(iPad)|(iPhone)|(iPod)|(android)|(webOS)/i)
        return isMob != null;
    }-*/;

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(final String value) {
        facebookId = value;
    }

    @Override
    public void clear() {
        nations.clear();
        enemyNations.clear();
        alliedNations.clear();
        editedOrders.clear();
    }
}
