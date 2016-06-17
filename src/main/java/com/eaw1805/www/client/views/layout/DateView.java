package com.eaw1805.www.client.views.layout;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.events.GameStartedEvent;
import com.eaw1805.www.client.events.GameStartedHandler;
import com.eaw1805.www.shared.stores.GameStore;

public class DateView extends AbsolutePanel {

    public DateView() {
        setSize("137px", "37px");

        final Image dateImg = new Image("http://static.eaw1805.com/images/layout/DateParchment.png");
        dateImg.setSize("137px", "37px");
        add(dateImg, 0, 0);

        final Label lblDate = new Label();
        lblDate.setStyleName("datePanel");
        lblDate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblDate.setWidth("100%");
        add(lblDate, 0, 12);

        GameEventManager.addGameStartedHandler(new GameStartedHandler() {
            public void onGameStarted(final GameStartedEvent event) {
                final String month = getMonthByTurn(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(), GameStore.getInstance().getTurn());
                final String year = getYearByTurn(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(), GameStore.getInstance().getTurn());
                lblDate.setText(month + " " + year);
            }
        });
    }

    public String getMonthByTurn(final int scenarioId, final int gameId, final int value) {
        int month;
        switch (scenarioId) {
            case HibernateUtil.DB_S1:
                if (gameId < 8) {
                    month = value; // Old games start from January

                } else {
                    month = value + 3; // April is the 1st month
                }
                break;

            case HibernateUtil.DB_S3:
                month = value + 8; // September is the 1st month
                break;

            case HibernateUtil.DB_S2:
            case HibernateUtil.DB_FREE:
            default:
                month = value;
                break;
        }

        while ((month - 12) > -1) {
            month -= 12;
        }

        switch (month) {
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

    public String getYearByTurn(final int scenarioId, final int gameId, final int value) {
        int year, month;
        switch (scenarioId) {
            case HibernateUtil.DB_S1:
                if (gameId < 8) {
                    year = 1805; // Old games start on 1805
                    month = value; // Old games start from January

                } else {
                    year = 1802;
                    month = value + 3; // April is the 1st month
                }
                break;

            case HibernateUtil.DB_S2:
                year = 1805;
                month = value; // January is the 1st month
                break;

            case HibernateUtil.DB_S3:
                year = 1808;
                month = value + 8; // September is the 1st month
                break;

            default:
            case HibernateUtil.DB_FREE:
                year = 1804;
                month = value; // January is the 1st month
                break;
        }

        while ((month - 12) > -1) {
            month -= 12;
            year++;
        }
        return "" + year + "";

    }
}
