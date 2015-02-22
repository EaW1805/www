package empire.webapp.commands;

/**
 * Holds the arguments for map-related commands.
 */
public class MapCommand {

    /**
     * The game ID.
     */
    private String gameId;

    /**
     * The nation ID.
     */
    private String regionId;

    /**
     * The nation ID.
     */
    private String nationId;

    /**
     * The minimum X coordinate.
     */
    private String minX;

    /**
     * The minimum Y coordinate.
     */
    private String minY;

    /**
     * The maximum X coordinate.
     */
    private String maxX;

    /**
     * The maximum Y coordinate.
     */
    private String maxY;

    /**
     * The zoom ration.
     */
    private String zoom;

    public MapCommand() {
        gameId = null;
        regionId = null;
        nationId = null;
        zoom = null;
        minX = null;
        minY = null;
        maxX = null;
        maxY = null;
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
    public String getRegionId() {
        return regionId;
    }

    /**
     * Set the ID of the nation.
     *
     * @param thisId the ID of the nation.
     */
    public void setRegionId(final String thisId) {
        regionId = thisId;
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
     * Get the zoom ratio.
     *
     * @return the zoom ratio.
     */
    public String getZoom() {
        return zoom;
    }

    /**
     * Set the zoom ratio.
     *
     * @param value the zoom ratio.
     */
    public void setZoom(final String value) {
        this.zoom = value;
    }

    /**
     * Get the minimum X coordinate.
     *
     * @return the minimum X coordinate.
     */
    public String getMinX() {
        return minX;
    }

    /**
     * Set the minimum X coordinate.
     *
     * @param value the minimum X coordinate.
     */
    public void setMinX(final String value) {
        this.minX = value;
    }

    /**
     * Get the minimum Y coordinate.
     *
     * @return the minimum Y coordinate.
     */
    public String getMinY() {
        return minY;
    }

    /**
     * Set the minimum Y coordinate.
     *
     * @param value the minimum Y coordinate.
     */
    public void setMinY(final String value) {
        this.minY = value;
    }

    /**
     * Get the maximum X coordinate.
     *
     * @return the maximum X coordinate.
     */
    public String getMaxX() {
        return maxX;
    }

    /**
     * Set the maximum X coordinate.
     *
     * @param value the maximum X coordinate.
     */
    public void setMaxX(final String value) {
        this.maxX = value;
    }

    /**
     * Get the maximum Y coordinate.
     *
     * @return the maximum Y coordinate.
     */
    public String getMaxY() {
        return maxY;
    }

    /**
     * Set the maximum Y coordinate.
     *
     * @param value the maximum Y coordinate.
     */
    public void setMaxY(final String value) {
        this.maxY = value;
    }
}
