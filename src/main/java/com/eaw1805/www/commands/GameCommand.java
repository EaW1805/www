package empire.webapp.commands;

/**
 * POJO object for holding parameters for game related commands.
 */
public class GameCommand {

    /**
     * The game ID.
     */
    private String gameId;

    private String action;

    /**
     * Default constructor.
     */
    public GameCommand() {
        this.gameId = null;
        this.action = null;
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

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }
}
