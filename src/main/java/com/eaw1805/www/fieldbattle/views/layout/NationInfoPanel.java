package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;

/**
 * This is the panel that appears in the top of the screen containing abstract info for the two sides.
 */
public class NationInfoPanel extends HorizontalPanel {

    public NationInfoPanel(final int nationId, final boolean leftToRight) {
        setSize("200px", "85px");
        Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + nationId + "-120.png");
        final VerticalPanel dataPanel = new VerticalPanel();
        final Label headCount = new Label("Men: " + ArmyStore.getInstance().getBrigadeHeadCountByNation(nationId));
        dataPanel.add(headCount);
        if (leftToRight) {
            add(nationImg);
            dataPanel.setHorizontalAlignment(ALIGN_LEFT);

            add(dataPanel);
            setCellHorizontalAlignment(dataPanel, ALIGN_LEFT);
        } else {
            dataPanel.setHorizontalAlignment(ALIGN_RIGHT);
            add(dataPanel);
            add(nationImg);
            setCellHorizontalAlignment(dataPanel, ALIGN_RIGHT);
        }
        setCellWidth(nationImg, "120px");

    }

}
