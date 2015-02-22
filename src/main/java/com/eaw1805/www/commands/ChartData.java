package empire.webapp.commands;

import java.io.Serializable;

/**
 * Stores information used for charts.
 */
public class ChartData implements Serializable {

    /**
     * Required by Serializable interface.
     */
    static final long serialVersionUID = 42L; //NOPMD

    private String caption;

    private int year;

    private int month;

    private int day;

    private int value;

    private double doubleValue;

    public String getCaption() {
        return caption;
    }

    public void setCaption(final String value) {
        caption = value;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(final int value) {
        month = value;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(final int value) {
        year = value;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
