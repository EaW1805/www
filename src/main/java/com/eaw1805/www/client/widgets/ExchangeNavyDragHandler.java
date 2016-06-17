package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipInfoMini;
import com.eaw1805.www.client.views.military.exchShips.ExchangeShipsView;
import com.eaw1805.www.shared.stores.units.NavyStore;


public class ExchangeNavyDragHandler
        implements DragHandler {

    private final ExchangeShipsView exchPanel;
    private ShipInfoMini dragShipPanel;
    private int targetFleetId;

    public ExchangeNavyDragHandler(final com.eaw1805.www.client.views.military.exchShips.ExchangeShipsView exchPanel) {
        this.exchPanel = exchPanel;
    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        if (exchPanel.getFleet1() == null || exchPanel.getFleet2() == null) {
            throw new VetoDragException();
        }

    }

    public void onPreviewDragEnd(final DragEndEvent event) throws VetoDragException {
        if (event.getContext().finalDropController != null) {
            dragShipPanel = (ShipInfoMini) event.getContext().draggable;
            targetFleetId = ((ClickAbsolutePanel) event.getContext().finalDropController.getDropTarget().getParent()).getId();
            if (dragShipPanel.getShip().getFleet() == targetFleetId ||
                    NavyStore.getInstance().getIdFleetMap().get(dragShipPanel.getShip().getFleet()).hasLoadedItemsOrUnits()) {
                throw new VetoDragException();
            }
        }
    }

    public void onDragStart(final DragStartEvent event) {
        // do nothing
    }

    public void onDragEnd(final DragEndEvent event) {
        // If you made it this far without a VetoException change
        // the fleet from the old to the new one
        NavyStore.getInstance().changeShipFleet(dragShipPanel.getShip().getId(), dragShipPanel.getShip().getFleet(), targetFleetId, true, false);
    }


}
