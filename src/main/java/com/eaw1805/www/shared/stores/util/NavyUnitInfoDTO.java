package empire.webapp.shared.stores.util;

public class NavyUnitInfoDTO {

    private int warShips = 0;

    private int merchantShips = 0;

    private int mps = Integer.MAX_VALUE;

    /**
     * @return the warShips
     */
    public int getWarShips() {
        return warShips;
    }

    /**
     * @return the merchantShips
     */
    public int getMerchantShips() {
        return merchantShips;
    }

    /**
     * @return the mps
     */
    public int getMps() {
        return mps;
    }

    /**
     * @param value the warShips to set
     */
    public void setWarShips(final int value) {
        this.warShips = value;
    }

    /**
     * @param value the merchantShips to set
     */
    public void setMerchantShips(final int value) {
        this.merchantShips = value;
    }

    /**
     * @param value the mps to set
     */
    public void setMps(final int value) {
        this.mps = value;
    }

}
