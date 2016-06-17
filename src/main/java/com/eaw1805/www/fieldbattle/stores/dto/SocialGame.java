package com.eaw1805.www.fieldbattle.stores.dto;

import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.SectorDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a social fieldbattle scenario game.
 */
public class SocialGame implements com.google.gwt.user.client.rpc.IsSerializable {

    /**
     * The unique identifier of this game.
     */
    private int id;

    /**
     * A human recognizable name.
     */
    private String name;



    private List<SocialPosition> side1 = new ArrayList<SocialPosition>();

    private List<SocialPosition> side2 = new ArrayList<SocialPosition>();

    /**
     * info about the round.
     */
    private int round = 0;

    /**
     * Info about the winner.
     */
    private int winner = 0;

    /**
     * The X size of the map.
     */
    private int mapSizeX;

    /**
     * The Y size of the map.
     */
    private int mapSizeY;

    /**
     * The sector where the battle takes place.
     */
    private SectorDTO sector;

    private boolean scenarioGame;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        final StringBuilder outBuilder = new StringBuilder();
        for (final SocialPosition pos : side1) {
            outBuilder.append("<img src='http://static.eaw1805.com/images/nations/nation-").append(pos.getNation().getNationId()).append("-120.png' style='width: 18px;'/>");
        }
        outBuilder.append(" vs ");
        for (final SocialPosition pos : side2) {
            outBuilder.append("<img src='http://static.eaw1805.com/images/nations/nation-").append(pos.getNation().getNationId()).append("-120.png' style='width: 18px;'/>");
        }
        if (!isScenarioGame()) {
            outBuilder.append(" - ").append(sector.getTerrain().getName()).append("<br>");

        } else {
            outBuilder.append("<br>");
            outBuilder.append(sector.getTerrain().getName());
        }
        return outBuilder.toString();
    }

    public List<SocialPosition> getSide1() {
        return side1;
    }

    public void setSide1(List<SocialPosition> side1) {
        this.side1 = side1;
    }

    public List<SocialPosition> getSide2() {
        return side2;
    }

    public void setSide2(List<SocialPosition> side2) {
        this.side2 = side2;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public boolean isUserPlaying(final String facebookId) {
        for (SocialPosition position : getSide1()) {
            if (facebookId.equals(position.getFacebookId())) {
                return true;
            }
        }
        for (SocialPosition position : getSide2()) {
            if (facebookId.equals(position.getFacebookId())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAvailablePosition() {
        for (SocialPosition position : getSide1()) {
            if (position.getFacebookId() == null || position.getFacebookId().isEmpty()) {
                return true;
            }
        }
        for (SocialPosition position : getSide2()) {
            if (position.getFacebookId() == null || position.getFacebookId().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameEnded() {
        return getWinner() != -1 || getRound() == 23;
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public void setMapSizeX(int mapSizeX) {
        this.mapSizeX = mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public void setMapSizeY(int mapSizeY) {
        this.mapSizeY = mapSizeY;
    }

    public SectorDTO getSector() {
        return sector;
    }

    public void setSector(SectorDTO sector) {
        this.sector = sector;
    }

    public boolean isScenarioGame() {
        return scenarioGame;
    }

    public void setScenarioGame(boolean scenarioGame) {
        this.scenarioGame = scenarioGame;
    }
}
