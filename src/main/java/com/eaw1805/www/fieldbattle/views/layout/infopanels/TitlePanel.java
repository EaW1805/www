package com.eaw1805.www.fieldbattle.views.layout.infopanels;


import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.stores.BaseStore;

public class TitlePanel extends AbsolutePanel {
    public static final int width = 300;
    public TitlePanel() {
        setSize(width + "px", "40px");
        setStyleName("titleFieldBattlePanel");
        final Label titleLabel = new Label(BaseStore.getInstance().getTitle());
        titleLabel.setStyleName("clearFontMiniTitle");
        add(titleLabel, 66, 2);

        final Label dateLabel = new Label(getMonthByTurn(0) + " " + getYearByTurn(0));
        dateLabel.setStyleName("clearFont");
        add(dateLabel, 98, 21);
    }

    public int getWidth() {
        return width;
    }

    public static String getMonthByTurn(final int value) {
        int turn = value;
        while ((turn - 12) > -1) {
            turn = turn - 12;
        }
        switch (turn) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";

            default:
                return "";
        }
    }

    public static String getYearByTurn(final int value) {
        int year = 1805;
        int turn = value;
        while ((turn - 12) > -1) {
            turn = turn - 12;
            year++;
        }
        return "" + year + "";
    }
}
