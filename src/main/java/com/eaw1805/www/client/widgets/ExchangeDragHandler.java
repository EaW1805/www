package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.www.client.views.extras.ExchangeUnitsWidget;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.mini.BattalionInfoMini;
import com.eaw1805.www.shared.stores.units.ArmyStore;

public class ExchangeDragHandler
        implements DragHandler, ArmyConstants {

    private final ExchangeUnitsWidget exchPanel;
    private ClickAbsolutePanel fdSource;
    private final ArmyStore arStore = ArmyStore.getInstance();

    public ExchangeDragHandler(final ExchangeUnitsWidget exchangeUnitsWidget) {
        this.exchPanel = exchangeUnitsWidget;
    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        fdSource = (ClickAbsolutePanel) event.getContext().draggable
                .getParent();
        if (!exchPanel.isEnabled()) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "You need to choose 2 different-same type units to start drag and drop", false);
            throw new VetoDragException();
        }

    }

    public void onPreviewDragEnd(final DragEndEvent event) throws VetoDragException {
        //do nothing here
    }

    public void onDragStart(final DragStartEvent event) {
        //do nothing here
    }

    public void onDragEnd(final DragEndEvent event) {
        final ClickAbsolutePanel fdTarget, fdSource = this.fdSource;
        final DragContext dg = event.getContext();
        if (dg.finalDropController != null) {
            fdTarget = (ClickAbsolutePanel) dg.finalDropController.getDropTarget();
            if (!fdSource.getParent().getParent().equals(fdTarget.getParent().getParent())) {
                // check if target is empty
                // if not then take the target widget and
                // place it where on the source
                if (fdTarget.getWidgetCount() > 1) {
                    final Widget targetW = fdTarget.getWidget(0);
                    fdTarget.remove(0);
                    fdSource.add(targetW);
                    doUnitTransaction((UnitImage) fdTarget.getWidget(0), exchPanel.getType(), fdTarget.getId());
                    doUnitTransaction((UnitImage) fdSource.getWidget(0), exchPanel.getType(), fdSource.getId());
                } else {
                    // If the target was empty just do
                    // the dnd transaction
                    doUnitTransaction((UnitImage) fdTarget.getWidget(0), exchPanel.getType(), fdTarget.getId());
                }
            }
        }
    }

    private void doUnitTransaction(final UnitImage img, final int type, final int slot) {
        final int firstId = exchPanel.getFirst();
        final int secondId = exchPanel.getSecond();
        switch (type) {
            case ARMY:
                int armyId = ((CorpsInfoPanel) (img.getPopupInfo().getWidget())).getCorp().getArmyId();
                int oldArmyId = 0;
                int corpId = ((CorpsInfoPanel) (img.getPopupInfo().getWidget())).getCorp().getCorpId();
                if (armyId == firstId) {
                    armyId = secondId;
                    oldArmyId = firstId;
                } else {
                    armyId = firstId;
                    oldArmyId = secondId;
                }
                arStore.changeCorpArmy(corpId, oldArmyId, armyId);
                break;

            case CORPS:
                corpId = ((BrigadeInfoPanel) (img.getPopupInfo().getWidget())).getBrigade().getCorpId();
                int oldCorpId = 0;
                int brigadeId = ((BrigadeInfoPanel) (img.getPopupInfo().getWidget())).getBrigade().getBrigadeId();
                if (corpId == firstId) {
                    oldCorpId = firstId;
                    corpId = secondId;
                } else {
                    oldCorpId = secondId;
                    corpId = firstId;
                }
                arStore.changeBrigadeCorp(brigadeId, oldCorpId, corpId, true);
                break;

            case BRIGADE:
                brigadeId = ((BattalionInfoMini) (img.getPopupInfo().getWidget())).getBattalion().getBrigadeId();
                final int battalionId = ((BattalionInfoMini) (img.getPopupInfo().getWidget())).getBattalion().getId();
                if (brigadeId == firstId) {
                    brigadeId = secondId;
                } else {
                    brigadeId = firstId;
                }
                arStore.changeBattalionBrigade(battalionId, brigadeId, slot, false);
                break;

            default:
                break;
        }

    }

}
