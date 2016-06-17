package com.eaw1805.www.fieldbattle.stores.dto;

/**
 * Created by karavias on 4/18/14.
 */
public class SocialSettings implements com.google.gwt.user.client.rpc.IsSerializable {

    private int scenarioId;
    private int battleId;
    private int nationId;
    private int round;
    private int side;
    private String title;
    private boolean sideReady;
    private boolean gameEnded;
    private int winner;
    private boolean standAlone;
    private String facebookId;

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int getBattleId() {
        return battleId;
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public boolean isStandAlone() {
        return standAlone;
    }

    public void setStandAlone(boolean standAlone) {
        this.standAlone = standAlone;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public String toString() {
        return "SocialSettings{" +
                "scenarioId=" + scenarioId +
                ", battleId=" + battleId +
                ", nationId=" + nationId +
                ", round=" + round +
                ", side=" + side +
                ", title='" + title + '\'' +
                ", sideReady=" + sideReady +
                ", gameEnded=" + gameEnded +
                ", winner=" + winner +
                ", standAlone=" + standAlone +
                ", facebookId='" + facebookId + '\'' +
                '}';
    }
}
