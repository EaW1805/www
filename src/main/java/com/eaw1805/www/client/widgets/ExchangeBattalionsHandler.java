package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.infopanels.units.mini.BattInfoMini;
import com.eaw1805.www.client.views.military.formbrigades.FormBrigadesView;
import com.eaw1805.www.shared.stores.units.ArmyStore;

public class ExchangeBattalionsHandler
        implements DragHandler, ArmyConstants {

    private ClickAbsolutePanel sourcePanel;
    final FormBrigadesView formView;

    public ExchangeBattalionsHandler(final FormBrigadesView parent) {
        formView = parent;
    }

    public void onDragEnd(final DragEndEvent event) {
        // empty
        try {
            formView.getBFirstSelector().reSelectBrigade();
            formView.getBSecondSelector().reSelectBrigade();
        } catch (Exception e) {
            //eat it like a boss
        }
    }

    public void onDragStart(final DragStartEvent event) {
        // empty
    }

    public void onPreviewDragEnd(final DragEndEvent event)
            throws VetoDragException {
        ClickAbsolutePanel dropPanel = null;
        final BattInfoMini draggable = (BattInfoMini) event.getContext().draggable;
        if (event.getContext().finalDropController != null) {
            dropPanel = (ClickAbsolutePanel) event.getContext().finalDropController.getDropTarget();
        }

        if (dropPanel != null) {
            final BrigadeDTO fromBrigade = ArmyStore.getInstance().getBrigadeById(draggable.getBattalion().getBrigadeId());
            if (dropPanel.getId() != fromBrigade.getBrigadeId() && dropPanel.getId() != -1) {
                if (dropPanel.getWidgetCount() > 1) {
                    final int excBattId = ((BattInfoMini) dropPanel.getWidget(0)).getBattalion().getId();
                    changeBattalionBrigade(fromBrigade.getBrigadeId(), excBattId, sourcePanel.getIndex());
                }

                changeBattalionBrigade(dropPanel.getId(), draggable.getBattalion().getId(), dropPanel.getIndex());
                //change anyway the first brigade
                UnitEventManager.changeUnit(BRIGADE, fromBrigade.getBrigadeId());
                //just be sure the panels show the correct battalions

            } else {
                throw new VetoDragException();
            }

        }
    }

    private void changeBattalionBrigade(final int toBrigId, final int battId, final int slot) {
        ArmyStore.getInstance().changeBattalionBrigade(battId, toBrigId, slot, false);
    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        sourcePanel = (ClickAbsolutePanel) event.getContext().draggable.getParent();
    }

}
