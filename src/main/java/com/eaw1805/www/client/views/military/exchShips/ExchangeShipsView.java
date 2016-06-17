package com.eaw1805.www.client.views.military.exchShips;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.popups.FleetsListPopup;
import com.eaw1805.www.client.widgets.ExchangeNavyDragHandler;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class ExchangeShipsView extends AbsolutePanel {

    private SectorDTO sector;
    private FleetPanel fleetPanel1, fleetPanel2;
    private boolean warShips = true;
    private PickupDragController dndCtrl;
    private ImageButton fleet1SearchImg, fleet2SearchImg;
    private final ShipTypesList typeList = new ShipTypesList(this);
    private FleetDTO fleet1 = null, fleet2 = null;

    public ExchangeShipsView(final SectorDTO sector) {
        this.sector = sector;
        dndCtrl = new PickupDragController(this, false);
        dndCtrl.addDragHandler(new ExchangeNavyDragHandler(this));
        dndCtrl.setBehaviorDragStartSensitivity(3);
        setSize("1134px", "544px");
        fleetPanel1 = new FleetPanel(dndCtrl, this, null);
        fleetPanel2 = new FleetPanel(dndCtrl, this, null);
        this.add(fleetPanel1, 0, 0);
        this.add(fleetPanel2, 581, 0);


        this.fleet1SearchImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
        this.fleet1SearchImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                PopupPanel fleetSelectionList = new PopupPanel();
                fleetSelectionList.setStylePrimaryName("none");
                fleetSelectionList.add(new FleetsListPopup(sector, ExchangeShipsView.this, 1, fleet2));
                fleetSelectionList.setAutoHideEnabled(true);
                fleetSelectionList.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
                fleetSelectionList.getElement().getStyle().setZIndex(100000);
                fleetSelectionList.show();

                fleet1SearchImg.deselect();
            }
        }).addToElement(fleet1SearchImg.getElement()).register();

        add(this.fleet1SearchImg, 418, 0);

        this.fleet2SearchImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
        this.fleet2SearchImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                PopupPanel fleetSelectionList = new PopupPanel();
                fleetSelectionList.setStylePrimaryName("none");
                fleetSelectionList.add(new FleetsListPopup(sector, ExchangeShipsView.this, 2, fleet1));
                fleetSelectionList.setAutoHideEnabled(true);
                fleetSelectionList.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
                fleetSelectionList.getElement().getStyle().setZIndex(100000);
                fleetSelectionList.show();

                fleet2SearchImg.deselect();
            }
        }).addToElement(fleet2SearchImg.getElement()).register();

        add(this.fleet2SearchImg, 997, 0);
        add(this.typeList, 0, 0);
    }

    public void changeShipType(boolean isWarShips) {
        warShips = isWarShips;
        fleetPanel1.setType(warShips);
        fleetPanel2.setType(warShips);

    }


    public void setFleetSelected(int selFleet, int index) {
        if (index == 1) {
            fleet1 = getFleetById(selFleet);
            fleetPanel1.selectFleet(fleet1);
        } else {
            fleet2 = getFleetById(selFleet);
            fleetPanel2.selectFleet(fleet2);
        }

    }

    private FleetDTO getFleetById(int selFleet) {
        List<FleetDTO> fleets = NavyStore.getInstance().getFleetsByRegionAndTile(sector, false, true);
        for (FleetDTO fleet : fleets) {
            if (fleet.getFleetId() == selFleet) {
                return fleet;
            }
        }
        return null;
    }

    public int getNewFleetIdByFleetId(int fleetId) {
        if (fleet1 != null && fleet2 != null) {
            if (fleet1.getFleetId() == fleetId) {
                return fleet2.getFleetId();
            } else if (fleet2.getFleetId() == fleetId) {
                return fleet1.getFleetId();
            } else {
                return 0;
            }
        }
        return -1;
    }

    /**
     * @return the fleet1
     */
    public FleetDTO getFleet1() {
        return fleet1;
    }

    /**
     * @return the fleet2
     */
    public FleetDTO getFleet2() {
        return fleet2;
    }


}
