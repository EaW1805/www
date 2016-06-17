package com.eaw1805.www.client.views.military.formFleet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.views.military.formFleet.FromFleet.Type;
import com.eaw1805.www.client.widgets.ImageButton;

public class ShipTypesList extends AbsolutePanel {
    private ImageButton changeSelImg;
    private String[] options = {"All", "Merchants", "Warships"};
    private Type selected = FromFleet.Type.ALL;
    private int option = 0;
    private Label lblSeltext;
    private Image holderImg;

    public ShipTypesList(final FromFleet formFleet) {
        setSize("142px", "33px");

        this.changeSelImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png");
        this.changeSelImg.setStyleName("pointer");

        this.changeSelImg.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                switch (selected) {
                    case WAR:
                        selected = FromFleet.Type.ALL;
                        option = 0;
                        break;
                    case ALL:
                        selected = FromFleet.Type.MERCHANT;
                        option = 1;
                        break;
                    case MERCHANT:
                        selected = FromFleet.Type.WAR;
                        option = 2;
                        break;
                }
                lblSeltext.setText(options[option]);
                changeSelImg.deselect();
                changeSelImg.setUrl(changeSelImg.getUrl().replace("Off", "Hover"));
                formFleet.changeShipType((selected));
            }
        });

        this.holderImg = new Image("http://static.eaw1805.com/images/panels/formFederations/listShipType.png");
        add(this.holderImg, 0, 0);
        this.holderImg.setSize("110px", "33px");
        add(this.changeSelImg, 111, 3);
        this.changeSelImg.setSize("26px", "26px");

        this.lblSeltext = new Label(options[option]);
        this.lblSeltext.setStyleName("clearFontMedSmall blackText");
        this.lblSeltext.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        add(this.lblSeltext, 10, 9);
        this.lblSeltext.setSize("90px", "15px");
    }

    public Type getSelected() {
        return selected;
    }
}
