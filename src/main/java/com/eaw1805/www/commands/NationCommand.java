package empire.webapp.commands;

/**
 * POJO object for holding parameters for nation related commands.
 */
public class NationCommand {

    /**
     * The game ID.
     */
    private String gameId;

    /**
     * The nation ID.
     */
    private String nationId;

    /**
     * The turn.
     */
    private String turn;

    public NationCommand() {
        gameId = null;
        nationId = null;
        turn = null;
    }

    /**
     * Get the ID of the game.
     *
     * @return the Game ID.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Set the ID of the game.
     *
     * @param thisId the ID of the game.
     */
    public void setGameId(final String thisId) {
        gameId = thisId;
    }

    /**
     * Get the ID of the nation.
     *
     * @return the Nation ID.
     */
    public String getNationId() {
        return nationId;
    }

    /**
     * Set the ID of the nation.
     *
     * @param thisId the ID of the nation.
     */
    public void setNationId(final String thisId) {
        nationId = thisId;
    }

    /**
     * GEt the turn.
     *
     * @return the turn.
     */
    public String getTurn() {
        return turn;
    }

    /**
     * Set the turn.
     *
     * @param value the turn.
     */
    public void setTurn(final String value) {
        this.turn = value;
    }
}
