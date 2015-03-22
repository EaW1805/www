package com.eaw1805.www.commands;

import com.eaw1805.data.model.Nation;
import com.eaw1805.data.model.army.Rank;
import com.eaw1805.data.model.map.Region;
import com.eaw1805.data.model.map.Terrain;

/**
 * POJO object for holding parameters for a tactical or naval battle.
 */
public class BattleCommand {

    /**
     * The 1st nation ID.
     */
    private String nationId1;

    /**
     * The 1st nation.
     */
    private Nation nation1;

    /**
     * The rank of the commander of the 1st nation.
     */
    private Rank rank1;

    /**
     * The 2nd nation ID.
     */
    private String nationId2;

    /**
     * The 2nd nation.
     */
    private Nation nation2;

    /**
     * The rank of the commander of the 2nd nation.
     */
    private Rank rank2;

    /**
     * The terrain type. For tactical battles.
     */
    private Terrain terrainType;

    /**
     * The region where the battle will take place.
     */
    private Region region;

    /**
     * The fortress type.
     */
    private String fortress;

    /**
     * The weather. For naval battles only.
     */
    private String weather;

    /**
     * Default constructor.
     */
    public BattleCommand() {
        nationId1 = null;
        nation1 = null;
        rank1 = null;
        nationId2 = null;
        nation2 = null;
        rank2 = null;
        terrainType = null;
        region = null;
        weather = null;
        fortress = null;
    }

    /**
     * Get the ID of the 1st nation.
     *
     * @return the 1st Nation ID.
     */
    public String getNationId1() {
        return nationId1;
    }

    /**
     * Set the ID of the 1st nation.
     *
     * @param thisId the ID of the 1st nation.
     */
    public void setNationId1(final String thisId) {
        nationId1 = thisId;
    }

    /**
     * Get the 1st nation.
     *
     * @return the 1st Nation.
     */
    public Nation getNation1() {
        return nation1;
    }

    /**
     * Set the 1st nation.
     *
     * @param thisId the 1st nation.
     */
    public void setNation1(final Nation thisId) {
        nation1 = thisId;
    }

    /**
     * Get the rank of the commander of the 1st side.
     *
     * @return the rank of the commander of the 1st side.
     */
    public Rank getRank1() {
        return rank1;
    }

    /**
     * Set the rank of the commander of the 1st side.
     *
     * @param value the rank of the commander of the 1st side.
     */
    public void setRank1(final Rank value) {
        this.rank1 = value;
    }

    /**
     * Get the ID of the 2nd nation.
     *
     * @return the 2nd Nation ID.
     */
    public String getNationId2() {
        return nationId2;
    }

    /**
     * Set the ID of the 2nd nation.
     *
     * @param thisId the ID of the 2nd nation.
     */
    public void setNationId2(final String thisId) {
        nationId2 = thisId;
    }

    /**
     * Get the 2nd nation.
     *
     * @return the 2nd Nation.
     */
    public Nation getNation2() {
        return nation2;
    }

    /**
     * Set the 2nd nation.
     *
     * @param thisId the 2nd nation.
     */
    public void setNation2(final Nation thisId) {
        nation2 = thisId;
    }

    /**
     * Get the rank of the commander of the 2nd side.
     *
     * @return the rank of the commander of the 2nd side.
     */
    public Rank getRank2() {
        return rank2;
    }

    /**
     * Set the rank of the commander of the 2nd side.
     *
     * @param value the rank of the commander of the 2nd side.
     */
    public void setRank2(final Rank value) {
        this.rank2 = value;
    }

    /**
     * Get the terrain type.
     *
     * @return the terrain type.
     */
    public Terrain getTerrainType() {
        return terrainType;
    }

    /**
     * Set the terrain type.
     *
     * @param value the terrain type.
     */
    public void setTerrainType(final Terrain value) {
        this.terrainType = value;
    }

    /**
     * Get the region.
     *
     * @return the region object.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Set the region.
     *
     * @param value the region object.
     */
    public void setRegion(final Region value) {
        this.region = value;
    }

    /**
     * Get the fortress level.
     *
     * @return the fortress level.
     */
    public String getFortress() {
        return fortress;
    }

    /**
     * Set the fortress level.
     *
     * @param value the fortress level.
     */
    public void setFortress(final String value) {
        this.fortress = value;
    }

    /**
     * Get the weather type.
     *
     * @return the weather type.
     */
    public String getWeather() {
        return weather;
    }

    /**
     * Set the weather type.
     *
     * @param value the weather type.
     */
    public void setWeather(final String value) {
        this.weather = value;
    }
}
