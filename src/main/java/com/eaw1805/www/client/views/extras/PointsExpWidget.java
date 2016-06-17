package com.eaw1805.www.client.views.extras;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

public class PointsExpWidget extends Composite {


    private final static String[] ALL_TITLES = {
            "Build Production Site",
            "Increase Population",
            "Decrease Population",
            "Demolish Site",
            "Cede Territory",
            "Build Brigade (Europe/Home country)",
            "Build Brigade (Europe/Sphere of influence)",
            "Build Brigade (Europe/Outside)",
            "Build Brigade (Colonies)",
            "Move Brigade",
            "Move Corps",
            "Move Army",
            "Move Baggage Train",
            "Move Spy",
            "Move Ship",
            "Move Fleet",
            "Build Ship",
            "Cede Ship (Fleets not allowed)",
            "Form Corps/Army/Fleet",
            "Increase Headcount (Brigade/Corps)",
            "Increase Headcount (Army)",
            "Build Baggage Train",
            "Remove Commander from Army/Corps",
            "Dismiss Commander",
            "Trade (First Phase)",
            "Trade (Second Phase)",
            "Load/Unload (not in barrack/shipyard)"
    };

    //0 for administrative points and 1 for command points
    private final static String[][] ALL_COSTS = {
            {
                    "1 AP",
                    "population /3 (min 1)",
                    "population /2 (min 1)",
                    "2 AP",
                    "population (min 1)",
                    "1 AP",
                    "3 AP",
                    "5 AP",
                    "3 AP",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "1 AP",
                    "1 AP",
                    "1 AP",
                    "1 AP",
                    "2 AP",
                    "1 AP",
                    "",
                    "1 AP",
                    "1 AP",
                    "1 AP",
                    ""
            },
            {
                    "",
                    "",
                    "",
                    "",
                    "",
                    "1 CP",
                    "3 CP",
                    "5 CP",
                    "3 CP",
                    "1 CP",
                    "2 CP",
                    "3 CP",
                    "1 CP",
                    "1 CP",
                    "1 CP",
                    "3 CP",
                    "1 CP",
                    "1 CP",
                    "1 CP",
                    "",
                    "",
                    "",
                    "1 CP",
                    "1 CP",
                    "",
                    "",
                    "1 CP"
            }

    };

    /**
     * type 0 administrative points and 1 command points.
     */
    public PointsExpWidget() {
        final AbsolutePanel basePanel = new AbsolutePanel();
        basePanel.setSize("450px", "585px");
        basePanel.setStyleName("pointsInfoPanel");
        initWidget(basePanel);

        final Label lblAdministrationPointsXp = new Label("Administration & Command Points Expenditure");
        lblAdministrationPointsXp.setStyleName("clearFontMiniTitle");

        basePanel.add(lblAdministrationPointsXp, 3, 3);


        final Grid grid = new Grid(ALL_TITLES.length + 1, 3);
        grid.setWidget(0, 0, new Label(""));
        grid.setWidget(0, 1, getLabel("AP", "clearFont", 140));
        grid.setWidget(0, 2, getLabel("CP", "clearFont", 32));
        for (int index = 0; index < ALL_TITLES.length; index++) {
            grid.setWidget(index + 1, 0, getLabel(ALL_TITLES[index], "clearFontSmall", 255));
            grid.setWidget(index + 1, 1, getLabel(ALL_COSTS[0][index], "clearFontSmall", 140));
            grid.setWidget(index + 1, 2, getLabel(ALL_COSTS[1][index], "clearFontSmall", 32));
        }

        grid.setSize("420px", "500px");
        basePanel.add(grid, 3, 23);
    }

    public Label getLabel(final String text, final String styleName, final int size) {
        final Label out = new Label(text);
        out.setStyleName(styleName);
        out.setSize(size + "px", "");
        return out;
    }
}
