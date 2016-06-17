package com.eaw1805.www.fieldbattle.stores.utils;

public class ArmyUnitInfoDTO {

    private int infantry = 0;

    private int cavalry = 0;

    private int artillery = 0;

    private int corps = 0;

    private int brigades = 0;

    private int battalions = 0;

    private int mps = Integer.MAX_VALUE;

    public ArmyUnitInfoDTO() {
        super();
    }

    /**
     * @return the infantry
     */
    public int getInfantry() {
        return infantry;
    }

    /**
     * @return the cavalry
     */
    public int getCavalry() {
        return cavalry;
    }

    /**
     * @return the artilletry
     */
    public int getArtillery() {
        return artillery;
    }

    /**
     * @param infantry the infantry to set
     */
    public void setInfantry(final int infantry) {
        this.infantry = infantry;
    }

    /**
     * @param cavalry the cavalry to set
     */
    public void setCavalry(final int cavalry) {
        this.cavalry = cavalry;
    }

    /**
     * @param artillery the artilletry to set
     */
    public void setArtillery(final int artillery) {
        this.artillery = artillery;
    }

    /**
     * @return the corps
     */
    public int getCorps() {
        return corps;
    }

    /**
     * @return the brigades
     */
    public int getBrigades() {
        return brigades;
    }

    /**
     * @return the battalions
     */
    public int getBattalions() {
        return battalions;
    }

    /**
     * @param corps the corps to set
     */
    public void setCorps(final int corps) {
        this.corps = corps;
    }

    /**
     * @param brigades the brigades to set
     */
    public void setBrigades(final int brigades) {
        this.brigades = brigades;
    }

    /**
     * @param battalions the battalions to set
     */
    public void setBattalions(final int battalions) {
        this.battalions = battalions;
    }

    /**
     * @param mps the mps to set
     */
    public void setMps(final int mps) {
        this.mps = mps;
    }

    /**
     * @return the mps
     */
    public int getMps() {
        return mps;
    }

    public void addToInfo(ArmyUnitInfoDTO unitInfo) {
        this.setArtillery(this.getArtillery() + unitInfo.getArtillery());
        this.setCavalry(this.getCavalry() + unitInfo.getCavalry());
        this.setInfantry(this.getInfantry() + unitInfo.getInfantry());
        this.setCorps(this.getCorps() + unitInfo.getCorps());
        this.setBrigades(this.getBrigades() + unitInfo.getBrigades());
        this.setBattalions(this.getBattalions() + unitInfo.getBattalions());
        if (unitInfo.getMps() < this.getMps()) {
            this.setMps(unitInfo.getMps());
        }
    }

    public int getDominant() {
        if (infantry > cavalry && infantry > artillery)
            return 1;
        else if (cavalry > infantry && cavalry > artillery)
            return 2;
        else
            return 3;
    }


}
