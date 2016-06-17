package com.eaw1805.www.client.views.military.buildships;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.GameStore;

public class ShipTypePanel extends Composite {
    private ShipTypeDTO shipTypeDTO;
    private BuildShipView empireBuildShipsWidget;
    private ClickAbsolutePanel containerPanel;

    public ShipTypePanel(final ShipTypeDTO typeDTO, final BuildShipView empireBuildShipsWidget) {
        shipTypeDTO = typeDTO;
        setEmpireBuildShipsWidget(empireBuildShipsWidget);
        containerPanel = new ClickAbsolutePanel();
        containerPanel.setStyleName("ShipPicPanel-new");
        initWidget(containerPanel);
        this.containerPanel.setSize("175px", "51px");
        containerPanel.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                empireBuildShipsWidget.populateShipInformation(shipTypeDTO);
                empireBuildShipsWidget.unSelectPreviousSelection();
                containerPanel.setStyleName("ShipPicPanel-selected");
            }
        });

        final Image image = new Image("http://static.eaw1805.com/images/ships/" + GameStore.getInstance().getNationId() + "/" + shipTypeDTO.getIntId() + ".png");
        containerPanel.add(image, 0, 0);
        image.setSize("51px", "51px");

        final Label lblName = new Label(shipTypeDTO.getName());
        lblName.setStyleName("clearFontMedSmall");
        lblName.setSize("118px", "15px");
        containerPanel.add(lblName, 57, 0);

        final Label lblClass = new Label("Class:" + shipTypeDTO.getShipClass());
        lblClass.setStyleName("clearFontMedSmall");
        containerPanel.add(lblClass, 57, 34);
    }

    public ClickAbsolutePanel getContainerPanel() {
        return containerPanel;
    }

    public void setEmpireBuildShipsWidget(final BuildShipView empireBuildShipsWidget) {
        this.empireBuildShipsWidget = empireBuildShipsWidget;
    }

}
