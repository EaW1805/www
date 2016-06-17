package com.eaw1805.www.client.views.military.buildships;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class OrderPanel extends AbsolutePanel {

    private FlowPanel buildOrderPanel;
    private ImageButton cancelBuildImg;
    private SectorDTO sector;
    private int selectedSector = 0;
    private ShipDTO selectedShip = null;

    public OrderPanel(SectorDTO sector) {
        setSize("385px", "195px");
        this.sector = sector;

        final ScrollPanel scrollPanel = new ScrollPanel();
        add(scrollPanel, 0, 30);
        scrollPanel.setSize("385px", "126px");

        this.buildOrderPanel = new FlowPanel();
        this.buildOrderPanel.setSize("385px", "100%");
        scrollPanel.setWidget(this.buildOrderPanel);

        this.cancelBuildImg = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButDeleteOrderOff.png");
        this.cancelBuildImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                for (ShipDTO ship : NavyStore.getInstance().getBarrShipMap().get(selectedSector)) {
                    if (ship.getId() == selectedShip.getId() && ship.getType().getName().equals(selectedShip.getType().getName())) {
                        NavyStore.getInstance().cancelBuildShip(OrderPanel.this.selectedSector, ship);
                        break;
                    }
                }
                cancelBuildImg.deselect();
                cancelBuildImg.setUrl(cancelBuildImg.getUrl().replace("Off", "Hover"));
                repopulateOrdersList();
            }
        }).addToElement(cancelBuildImg.getElement()).register();

        this.cancelBuildImg.setSize("273px", "30px");
        add(this.cancelBuildImg, 56, 165);

        final Label lblOrdersPlaced = new Label("Orders placed:");
        lblOrdersPlaced.setStyleName("clearFontMedLarge");
        add(lblOrdersPlaced, 0, 0);
        repopulateOrdersList();
    }

    void repopulateOrdersList() {
        buildOrderPanel.clear();
        final List<ShipDTO> thisList = NavyStore.getInstance().getBarrShipMapNew().get(sector.getId());
        addListEntry(sector.getId(), thisList);
        for (int sectorId : NavyStore.getInstance().getBarrShipMap().keySet()) {
            if (sectorId != sector.getId()) {
                final List<ShipDTO> otherList = NavyStore.getInstance().getBarrShipMap().get(sectorId);
                addListEntry(sectorId, otherList);
            }
        }
    }

    private void addListEntry(final int id, final List<ShipDTO> thisList) {
        if (thisList != null) {
            for (ShipDTO ship : thisList) {
                final ShipOrderFigure shipOrFigure = new ShipOrderFigure(ship);
                shipOrFigure.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        for (int index = 0; index < buildOrderPanel.getWidgetCount(); index++) {
                            ((ShipOrderFigure) buildOrderPanel.getWidget(index)).deselect();
                        }
                        selectedSector = id;
                        selectedShip = shipOrFigure.select();
                    }
                });
                buildOrderPanel.add(shipOrFigure);
            }
        }

    }
}
