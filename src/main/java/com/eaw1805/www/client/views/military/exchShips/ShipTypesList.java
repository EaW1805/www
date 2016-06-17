package com.eaw1805.www.client.views.military.exchShips;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ImageButton;

public class ShipTypesList extends AbsolutePanel {
    private ImageButton changeSelImg;
    private String[] options = {"Merchants", "Warships"};
    private int selected = 1;
    private Label lblSeltext;
    private Image holderImg;

    public ShipTypesList(final ExchangeShipsView exchShips) {
        setSize("142px", "33px");

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
                exchShips.changeShipType((selected == 1));
            }
        }).addToElement(changeSelImg.getElement()).register();

        this.holderImg = new Image("http://static.eaw1805.com/images/panels/formFederations/listShipType.png");
        add(this.holderImg, 0, 0);
        this.holderImg.setSize("110px", "33px");
        add(this.changeSelImg, 111, 3);
        this.changeSelImg.setSize("26px", "26px");

        this.lblSeltext = new Label(options[selected]);
        this.lblSeltext.setStyleName("clearFontMedSmall blackText");
        this.lblSeltext.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        add(this.lblSeltext, 10, 9);
        this.lblSeltext.setSize("90px", "15px");
    }

    public int getSelected() {
        return selected;
    }
}
