package com.eaw1805.www.controllers.remote.hotspot;

/**
 * Base functionality for all ChangesProcessor
 */
public abstract class AbstractChangesProcessor {

    /**
     * The game identity.
     */
    private transient final int gameId;

    /**
     * The turn.
     */
    private transient final int turn;

    /**
     * The owner.
     */
    private transient final int nationId;

    /**
     * The scenario.
     */
    private transient final int scenarioId;

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public AbstractChangesProcessor(final int thisScenario, final int thisGame, final int thisNation, final int thisTurn) {
        super();
        scenarioId = thisScenario;
        gameId = thisGame;
        turn = thisTurn;
        nationId = thisNation;
    }

    /**
     * Get the game of the order.
     *
     * @return the game of the order.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Get the turn of the order.
     *
     * @return the turn of the order.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Get the nation owner of the order.
     *
     * @return the nation owner of the order.
     */
    public int getNationId() {
        return nationId;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    /**
     * Process the orders.
     *
     * @return the result of the processing.
     */
    public abstract Object processChanges();

}
