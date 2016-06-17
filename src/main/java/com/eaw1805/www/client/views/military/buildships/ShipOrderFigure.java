package com.eaw1805.www.client.views.military.buildships;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipInfoMini;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;

public class ShipOrderFigure extends ClickAbsolutePanel {

    private ShipDTO ship;
    private Image selShipImg;

    public ShipOrderFigure(final ShipDTO ship) {
        setSize("38px", "38px");
        setStyleName("ShipPicPanel-new");
        this.ship = ship;
        getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);

        final Image shipImg = new Image("http://static.eaw1805.com/images/ships/" + GameStore.getInstance().getNationId() + "/" + ship.getType().getIntId() + ".png");
        shipImg.setTitle("Ship type:" + ship.getType().getName() + " is being built \n with the name:" + ship.getName());
        shipImg.setSize("38px", "38px");
        add(shipImg, 0, 0);

        this.selShipImg = new Image("http://static.eaw1805.com/images/buttons/tint.png");
        this.selShipImg.setTitle("Ship type:" + ship.getType().getName() + " is being built \n with the name:" + ship.getName());
        this.selShipImg.setStyleName("pointer");
        this.selShipImg.setSize("38px", "38px");
        new ToolTipPanel(this) {
            @Override
            public void generateTip() {
                setTooltip(new ShipInfoMini(ship, true, false));
            }
        };
    }

    public ShipDTO select() {
        setStyleName("ShipPicPanel-selected");
        if (!selShipImg.isAttached()) {
            add(this.selShipImg, 0, 0);
        }
        return ship;
    }

    public void deselect() {
        setStyleName("ShipPicPanel-new");
        if (selShipImg.isAttached()) {
            remove(this.selShipImg);
        }
    }
}
