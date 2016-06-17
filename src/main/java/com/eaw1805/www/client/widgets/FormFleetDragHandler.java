package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipInfoMini;
import com.eaw1805.www.client.views.military.formFleet.FormFleetView;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class FormFleetDragHandler
        implements DragHandler, ArmyConstants {

    private final FormFleetView formFleetView;
    private boolean toFleet;

    public FormFleetDragHandler(final FormFleetView formFleetView) {
        this.formFleetView = formFleetView;
    }

    public void onDragEnd(final DragEndEvent event) {
        if (event.getContext().finalDropController != null) {
            final int fleetId = formFleetView.getFormFleet().getFleetId();
            if (fleetId != -1 && toFleet) {
                final ShipInfoMini shipInfo = (ShipInfoMini) event.getContext().draggable;
                NavyStore.getInstance().changeShipFleet(shipInfo.getShip().getId(), 0, fleetId, true, false);
                formFleetView.getFormFleet().getShips().add(shipInfo.getShip());

            } else if (fleetId != -1 && !toFleet) {
                final ShipInfoMini shipInfo = (ShipInfoMini) event.getContext().draggable;
                NavyStore.getInstance().changeShipFleet(shipInfo.getShip().getId(), fleetId, 0, true, false);
                formFleetView.getFormFleet().removeShipById(shipInfo.getShip().getId());

            } else if (toFleet) {
                final ShipInfoMini shipInfo = (ShipInfoMini) event.getContext().draggable;
                formFleetView.getFormFleet().getShips().add(shipInfo.getShip());

            } else {
                final ShipInfoMini shipInfo = (ShipInfoMini) event.getContext().draggable;
                formFleetView.getFormFleet().removeShipById(shipInfo.getShip().getId());
            }

            // If the drop target has already a ship on it
            // remove it and keep the new one
            if (toFleet) {
                final AbsolutePanel dropPanel = ((AbsolutePanel) event.getContext().finalDropController.getDropTarget());
                if (dropPanel.getWidgetCount() == 2) {
                    final ShipInfoMini shipPanel = (ShipInfoMini) dropPanel.getWidget(0);
                    boolean success = false;
                    if (fleetId != -1) {
                        success = NavyStore.getInstance().changeShipFleet(shipPanel.getShip().getId(), fleetId, 0, true, false);
                    }

                    if (success) {

                        if (formFleetView.getFormFleet().removeShipById(shipPanel.getShip().getId())) {
                            shipPanel.removeFromParent();
                            formFleetView.getFreeShipsList().getFreeShipContainer().insert(shipPanel, 0);
                        }
                    }
                }
            }
            formFleetView.getFormFleet().initPages();
            formFleetView.getFormFleet().reCalculateLabelValues();
        }
    }


    public void onDragStart(final DragStartEvent event) {
        //do nothing here
    }

    public void onPreviewDragEnd(final DragEndEvent event) throws VetoDragException {
        if (event.getContext().finalDropController != null) {
            if (event.getContext().finalDropController.getDropTarget().getClass().equals(AbsolutePanel.class)) {
                if (!toFleet) {
                    throw new VetoDragException();
                }
            } else {
                if (toFleet) {
                    throw new VetoDragException();
                }
            }
        }
    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        final int fleetId = formFleetView.getFormFleet().getFleetId();
        final FleetDTO tgFleet = NavyStore.getInstance().getIdFleetMap().get(fleetId);
        final int warShips = MiscCalculators.getFleetInfo(tgFleet).getWarShips();
        if (warShips >= 40) {
            throw new VetoDragException();
        }

        toFleet = !event.getContext().draggable.getParent().getClass().equals(AbsolutePanel.class);

        if (tgFleet != null && !toFleet && tgFleet.hasLoadedUnits()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot remove ship from loaded fleet", false);
            throw new VetoDragException();
        }
    }

}
