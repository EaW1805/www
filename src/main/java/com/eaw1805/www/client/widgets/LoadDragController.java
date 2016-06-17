package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.www.client.views.figures.BrigadeInfoFigPanel;
import com.eaw1805.www.client.views.figures.CommanderInfoFigPanel;
import com.eaw1805.www.client.views.figures.SpyInfoFigPanel;
import com.eaw1805.www.client.views.military.deployment.DoDeployPanel;
import com.eaw1805.www.client.widgets.ErrorPopup.Level;
import com.eaw1805.www.shared.stores.units.TransportStore;

public class LoadDragController
        implements DragHandler, ArmyConstants {

    private final DoDeployPanel dpPanel;

    public LoadDragController(final DoDeployPanel dpPanel) {
        this.dpPanel = dpPanel;
    }

    public void onDragEnd(final DragEndEvent event) {
        if (event.getContext().finalDropController != null &&
                event.getContext().finalDropController.getDropTarget().equals(dpPanel.getCargoPanel())) {
            final int transportId = dpPanel.getTransportId();
            final int cargoId = getCargoId(event.getContext().draggable);
            if (TransportStore.getInstance().loadCargoToTransport(dpPanel.getTransportType(), transportId, dpPanel.getCargoType(), cargoId, dpPanel.getTradePhase())) {
                event.getContext().draggable.unsinkEvents(Event.ONDBLCLICK);
            }
        }
    }

    public void onDragStart(final DragStartEvent event) {
        //do nothing here

    }

    public void onPreviewDragEnd(final DragEndEvent event) throws VetoDragException {
        // do nothing here

    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        final int transportId = dpPanel.getTransportId();
        final int cargoId = getCargoId(event.getContext().draggable);
        if (dpPanel.getTransportId() != 0 && !event.getContext().draggable.getParent().equals(dpPanel.getCargoPanel())) {

            if (!TransportStore.getInstance().canCarryLoad(dpPanel.getTransportType(), transportId, dpPanel.getCargoType(), cargoId)) {
                new ErrorPopup(Level.NORMAL, "The selected transport unit has insufficient space!", false);
                throw new VetoDragException();
            }
        } else {
            throw new VetoDragException();
        }
    }

    private int getCargoId(final Widget draggable) {
        int cargoId = 0;
        switch (dpPanel.getCargoType()) {
            case BRIGADE:
                cargoId = ((BrigadeInfoFigPanel) draggable).getFigImg().getId();
                break;

            case COMMANDER:
                cargoId = ((CommanderInfoFigPanel) draggable).getFigImg().getId();
                break;

            case SPY:
                cargoId = ((SpyInfoFigPanel) draggable).getFigImg().getId();
                break;

            default:
                // do nothing
        }
        return cargoId;
    }

}
