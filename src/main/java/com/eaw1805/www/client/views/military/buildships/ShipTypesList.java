package com.eaw1805.www.client.views.military.buildships;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ImageButton;

public class ShipTypesList extends AbsolutePanel {
    private ImageButton changeSelImg;
    private String[] options = {"Merchants", "Warships"};
    private int selected = 0;
    private Label lblSeltext;

    public ShipTypesList(final BuildShipView buildShipView) {
        setSize("132px", "26px");

        this.changeSelImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png");
        this.changeSelImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                switch (selected) {
                    case 0:
                        selected = 1;
                        break;

                    case 1:
                        selected = 0;
                        break;
                }

                lblSeltext.setText(options[selected]);
                changeSelImg.deselect();
                changeSelImg.setUrl(changeSelImg.getUrl().replace("Off", "Hover"));
                buildShipView.populateShipsPanelByType(selected);
            }
        }).addToElement(changeSelImg.getElement()).register();

        add(this.changeSelImg, 106, 0);
        this.changeSelImg.setSize("26px", "26px");

        this.lblSeltext = new Label(options[selected]);
        this.lblSeltext.setStyleName("clearFontMedSmall blackText");
        this.lblSeltext.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.lblSeltext.setSize("90px", "15px");
        add(this.lblSeltext, 5, 5);
    }

}
