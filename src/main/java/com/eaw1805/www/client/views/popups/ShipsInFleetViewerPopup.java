package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.ShipInfoPanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.units.NavyStore;


public class ShipsInFleetViewerPopup extends DraggablePanel {

    public ShipsInFleetViewerPopup(final FleetDTO fleet, final boolean showMerchant, final boolean showWar) {

        setStyleName("bigDoubleSelector");
        setSize("800px", "478px");

        final Label title = new Label();
        if (!showMerchant && showWar) {
            title.setText("War Ships");
        } else if (!showWar && showMerchant) {
            title.setText("Merchant Ships");
        } else if (showWar && showWar) {
            title.setText("Fleet Ships");
        } else {
            title.setText("No Ships");
        }
        title.setStyleName("clearFontMiniTitle whiteText");
        add(title, 32, 19);


        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setWidth("752px");
        final VerticalPanelScrollChild side1 = new VerticalPanelScrollChild();
        final VerticalPanelScrollChild side2 = new VerticalPanelScrollChild();
        hPanel.add(side1);
        hPanel.add(side2);

        final ScrollVerticalBarEAW scrollPanel = new ScrollVerticalBarEAW(hPanel, 89, false);
        side1.setScroller(scrollPanel, false);
        side2.setScroller(scrollPanel, false);
        scrollPanel.setSize(766, 396);
        add(scrollPanel, 19, 64);


        int column = 0;
        if (showMerchant) {
            for (ShipDTO ship : fleet.getShips().values()) {
                if (NavyStore.getInstance().isTradeShip(ship)) {
                    if (column == 0) {
                        side1.add(new ShipInfoPanel(ship, false));
                        column = 1;
                    } else {
                        side2.add(new ShipInfoPanel(ship, false));
                        column = 0;
                    }
                }
            }
        }

        if (showWar) {
            for (ShipDTO ship : fleet.getShips().values()) {
                if (!NavyStore.getInstance().isTradeShip(ship)) {
                    if (column == 0) {
                        side1.add(new ShipInfoPanel(ship, false));
                        column = 1;
                    } else {
                        side2.add(new ShipInfoPanel(ship, false));
                        column = 0;
                    }
                }
            }
        }

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close popup");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                ShipsInFleetViewerPopup.this.close();
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        add(imgX, 750, 8);
        imgX.setSize("36px", "36px");

    }
}
