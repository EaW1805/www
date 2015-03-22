package com.eaw1805.www.commands;

import com.eaw1805.data.model.Nation;

import java.io.Serializable;
import java.util.List;

/**
 * Stores Report data in easy presentable format.
 */
public class BattleData
    implements Serializable {

    /**
     * The identity of the battle.
     */
    private int battleId;

    /**
     * Battle's relative coordinates.
     */
    private String position;

    /**
     * The turn of the battle.
     */
    private String turn;

    /**
     * The enemy nations.
     */
    private List<Nation> sideEnemies;

    /**
     * The allied nations.
     */
    private List<Nation> sideAllies;

    /**
     * The winner of the battle (0: none, 1: side 1, 2: side 2).
     */
    private String winner;

    /**
     * Get the identity of the battle.
     *
     * @return the identity of the battle.
     */
    public int getBattleId() {
        return battleId;
    }

    /**
     * Set the identity of the battle.
     *
     * @param value the identity of the battle.
     */
    public void setBattleId(final int value) {
        this.battleId = value;
    }

    /**
     * Get the Battle's relative coordinates.
     *
     * @return the Battle's relative coordinates.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Set the Battle's relative coordinates.
     *
     * @param value the Battle's relative coordinates.
     */
    public void setPosition(final String value) {
        this.position = value;
    }

    /**
     * Get the turn of the battle.
     *
     * @return the turn of the battle.
     */
    public String getTurn() {
        return turn;
    }

    /**
     * Set the turn of the battle.
     *
     * @param value the turn of the battle.
     */
    public void setTurn(final String value) {
        this.turn = value;
    }

    /**
     * Get the enemy nations.
     *
     * @return the enemy nations.
     */
    public List<Nation> getSideEnemies() {
        return sideEnemies;
    }

    /**
     * Set the enemy nations.
     *
     * @param value the enemy nations.
     */
    public void setSideEnemies(final List<Nation> value) {
        this.sideEnemies = value;
    }

    /**
     * Get the allied nations.
     *
     * @return the allied nations.
     */
    public List<Nation> getSideAllies() {
        return sideAllies;
    }

    /**
     * Set the allied nations.
     *
     * @param value the allied nations.
     */
    public void setSideAllies(final List<Nation> value) {
        this.sideAllies = value;
    }

    /**
     * Get the winner of the battle.
     *
     * @return the winner of the battle.
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Set the winner of the battle.
     *
     * @param value the winner of the battle.
     */
    public void setWinner(final String value) {
        this.winner = value;
    }
}
