package com.eaw1805.www.shared.stores.support;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.OrderCostDTO;

import java.io.Serializable;

public final class Taxation
        implements OrderConstants, GoodConstants, IsSerializable, Serializable {

    /**
     * The total number of VPs earned by the nation.
     */
    private int vps = 0;

    /**
     * The maximum number of VPs required by the nation.
     */
    private int vpsMax = 0;

    /**
     * The total population of the nation in EUROPE.
     */
    private int population = 0;

    /**
     * Captures if the Random Event Trade Surplus is in effect.
     */
    private int surplus = 0;

    /**
     * Captures if the Random Event Trade Deficit is in effect.
     */
    private int deficit = 0;

    /**
     * Tax level of the empire.
     */
    private int taxLevel = 0;

    private boolean useColGoods = false;

    private boolean useGems = false;

    private boolean useIndPoints = false;

    private boolean useMoney = false;

    private boolean useColonials = false;

    private OrderCostDTO cost = new OrderCostDTO();

    /**
     * @param taxLevel the taxLevel to set
     */
    public void setTaxLevel(final int taxLevel) {
        this.taxLevel = taxLevel;
    }

    /**
     * @return the taxLevel
     */
    public int getTaxLevel() {
        return taxLevel;
    }


    /**
     * @return the cost
     */
    public OrderCostDTO getCost() {
        return cost;
    }

    /**
     * Get the total population of the nation in EUROPE.
     *
     * @return the total population of the nation in EUROPE.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set the total population of the nation in EUROPE.
     *
     * @param value the total population of the nation in EUROPE.
     */
    public void setPopulation(final int value) {
        this.population = value;
    }

    /**
     * Get the total number of VPs earned by the nation.
     *
     * @return the total number of VPs earned by the nation.
     */
    public int getVps() {
        return vps;
    }

    /**
     * Set the total number of VPs earned by the nation.
     *
     * @param value the total number of VPs earned by the nation.
     */
    public void setVps(final int value) {
        this.vps = value;
    }

    /**
     * Get the maximum number of VPs required by the nation.
     *
     * @return the maximum number of VPs required by the nation.
     */
    public int getVpsMax() {
        return vpsMax;
    }

    /**
     * Set the maximum number of VPs required by the nation.
     *
     * @param value the maximum number of VPs required by the nation.
     */
    public void setVpsMax(final int value) {
        this.vpsMax = value;
    }


    /**
     * Get if the Random Event Trade Deficit is in effect.
     *
     * @return if the Random Event Trade Deficit is in effect.
     */
    public int getDeficit() {
        return deficit;
    }

    /**
     * Set if the Random Event Trade Deficit is in effect.
     *
     * @param value if the Random Event Trade Deficit is in effect.
     */
    public void setDeficit(final int value) {
        this.deficit = value;
    }

    /**
     * Get if the Random Event Trade Surplus is in effect.
     *
     * @return if the Random Event Trade Surplus is in effect.
     */
    public int getSurplus() {
        return surplus;
    }

    /**
     * Set if the Random Event Trade Surplus is in effect.
     *
     * @param value if the Random Event Trade Surplus is in effect.
     */
    public void setSurplus(final int value) {
        this.surplus = value;
    }

    public boolean isUseColGoods() {
        return useColGoods;
    }

    public void setUseColGoods(boolean useColGoods) {
        this.useColGoods = useColGoods;
    }

    public boolean isUseGems() {
        return useGems;
    }

    public void setUseGems(boolean useGems) {
        this.useGems = useGems;
    }

    public boolean isUseIndPoints() {
        return useIndPoints;
    }

    public void setUseIndPoints(boolean useIndPoints) {
        this.useIndPoints = useIndPoints;
    }

    public boolean isUseMoney() {
        return useMoney;
    }

    public void setUseMoney(boolean useMoney) {
        this.useMoney = useMoney;
    }

    public boolean isUseColonials() {
        return useColonials;
    }

    public void setUseColonials(boolean useColonials) {
        this.useColonials = useColonials;
    }
}
