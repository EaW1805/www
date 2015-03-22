package com.eaw1805.www.shared.stores.util;

public class ArmyUnitInfoDTO {

    private int infantry = 0;

    private int cavalry = 0;

    private int artillery = 0;

    private int corps = 0;

    private int brigades = 0;

    private int battalions = 0;

    private int mps = Integer.MAX_VALUE;

    private int headCount = 0;
    private int maxHeadCount = 0;
    private double avgExperience = 0;
    private double maxExperience = 0;
    private int numOfBattalionsNeedTrain = 0;
    private int numOfBattalionsNeedCrackElite = 0;

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
        if (battalions + unitInfo.getBattalions() != 0) {
            setAvgExperience((getAvgExperience()*(double)battalions + unitInfo.getAvgExperience()*(double)unitInfo.getBattalions())*1.0 / (double)(battalions + unitInfo.getBattalions()));
            setMaxExperience((getMaxExperience()*(double)battalions + unitInfo.getMaxExperience()*(double)unitInfo.getBattalions())*1.0 / (double)(battalions + unitInfo.getBattalions()));
        }
        setHeadCount(getHeadCount() + unitInfo.getHeadCount());
        setMaxHeadCount(getMaxHeadCount() + unitInfo.getMaxHeadCount());
        setNumOfBattalionsNeedCrackElite(getNumOfBattalionsNeedCrackElite() + unitInfo.getNumOfBattalionsNeedCrackElite());
        setNumOfBattalionsNeedTrain(getNumOfBattalionsNeedTrain() + unitInfo.getNumOfBattalionsNeedTrain());

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

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public int getMaxHeadCount() {
        return maxHeadCount;
    }

    public void setMaxHeadCount(int maxHeadCount) {
        this.maxHeadCount = maxHeadCount;
    }

    public double getAvgExperience() {
        return avgExperience;
    }

    public void setAvgExperience(double avgExperience) {
        this.avgExperience = avgExperience;
    }

    public double getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(double maxExperience) {
        this.maxExperience = maxExperience;
    }

    public int getNumOfBattalionsNeedTrain() {
        return numOfBattalionsNeedTrain;
    }

    public void setNumOfBattalionsNeedTrain(int numOfBattalionsNeedTrain) {
        this.numOfBattalionsNeedTrain = numOfBattalionsNeedTrain;
    }

    public int getNumOfBattalionsNeedCrackElite() {
        return numOfBattalionsNeedCrackElite;
    }

    public void setNumOfBattalionsNeedCrackElite(int numOfBattalionsNeedCrackElite) {
        this.numOfBattalionsNeedCrackElite = numOfBattalionsNeedCrackElite;
    }
}
