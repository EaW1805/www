package com.eaw1805.www.client.views.extras;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.client.widgets.ToolTipPanel;

public class AdmAndCommPtsPanel
        extends Composite {

    private Label lblQuantity;

    public AdmAndCommPtsPanel() {
        final VerticalPanel containerPanel = new VerticalPanel();
        containerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        containerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initWidget(containerPanel);
        containerPanel.setSize("36px", "16px");
        lblQuantity = new Label("");
        lblQuantity.setStyleName("pointer", true);
        lblQuantity.setStyleName("redText", true);
        lblQuantity.setStyleName("clearFont", true);
        containerPanel.add(lblQuantity);
        new ToolTipPanel(lblQuantity) {
            @Override
            public void generateTip() {
                setTooltip(new PointsExpWidget());
            }
        };
    }

    /**
     * @return the lblQuantity
     */
    public Label getLblQuantity() {
        return lblQuantity;
    }

}
