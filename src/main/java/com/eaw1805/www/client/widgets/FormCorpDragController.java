package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.client.views.infopanels.units.mini.BrigadeInfoMini;
import com.eaw1805.www.client.views.military.formCorp.FormCorp;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

public class FormCorpDragController
        implements DragHandler {

    private AbsolutePanel apSource = null;
    private int sourceType = 0;
    private FormCorp parent = null;

    public void onDragEnd(final DragEndEvent event) {
        if (parent != null) {
            parent.rearrangeBrigades();
            parent.setLabels();
        }
    }

    public void onDragStart(final DragStartEvent event) {
        // do nothing
    }

    public void onPreviewDragEnd(final DragEndEvent event) throws VetoDragException {
        final DragContext dg = event.getContext();
        if (dg.finalDropController != null) {
            final AbsolutePanel apDest;
            if (dg.finalDropController.getDropTarget().getClass().equals(AbsolutePanel.class) &&
                    sourceType == 0) {
                apDest = (AbsolutePanel) dg.finalDropController.getDropTarget();
                // check if target is empty
                // if not then take the target widget and
                // place it where on the source
                if (!apSource.equals(apDest)
                        && apDest.getWidgetCount() > 1) {
                    final Widget targetW = apDest.getWidget(0);
                    apDest.remove(0);
                    apSource.add(targetW);
                }

            } else if (dg.finalDropController.getDropTarget().getClass().equals(AbsolutePanel.class) &&
                    sourceType == 1) {
                apDest = (AbsolutePanel) dg.finalDropController.getDropTarget();
                final BrigadeInfoMini brigInfo = (BrigadeInfoMini) dg.draggable;
                parent = (FormCorp) apDest.getParent().getParent();
                if (parent.getFreeCorp() == null) {
                    parent.getBrigades().add(brigInfo.getBrigade());
                } else {
                    ArmyStore.getInstance().changeBrigadeCorp(brigInfo.getBrigade().getBrigadeId(), 0, parent.getFreeCorp().getCorpId(), true);

                }

            } else if (dg.finalDropController.getDropTarget().getClass().equals(VerticalPanel.class)) {
                if (sourceType == 0) {
                    final BrigadeInfoMini brigInfo = (BrigadeInfoMini) dg.draggable;
                    if (brigInfo.getBrigade().getCorpId() == 0) {
                        parent = (FormCorp) apSource.getParent().getParent();
                        parent.removeBrigadeById(brigInfo.getBrigade().getBrigadeId());
                    } else {
                        parent = (FormCorp) apSource.getParent().getParent();
                        if (parent.getFreeCorp().getBrigades().size() == 1) {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "By removing the last brigade of the corps, you are disbanding the corps, continue?", true) {
                                public void onAccept() {
                                    ArmyStore.getInstance().changeBrigadeCorp(brigInfo.getBrigade().getBrigadeId(), parent.getFreeCorp().getCorpId(), 0, true);
                                }

                                public void onReject() {
                                }
                            };
                        } else {
                            ArmyStore.getInstance().changeBrigadeCorp(brigInfo.getBrigade().getBrigadeId(), parent.getFreeCorp().getCorpId(), 0, true);
                        }


                    }
                }

            }
            if (TutorialStore.getInstance().isTutorialMode()
                    && TutorialStore.getInstance().getMonth() == 8
                    && TutorialStore.getInstance().getTutorialStep() == 3) {
                TutorialStore.getInstance().setBrigadeDraggedFlag(true);
                if (!TutorialStore.getInstance().isCommanderAssignedFlag()) {
                    TutorialStore.highLightButton(parent.getComImg());
                } else {
                    TutorialStore.highLightButton(parent.getSaveImg());
                }
            }
        }
    }


    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        if (event.getContext().draggable.getParent().getClass().equals(AbsolutePanel.class)) {
            apSource = (AbsolutePanel) event.getContext().draggable.getParent();
            sourceType = 0;
        } else {
            sourceType = 1;
        }

    }

}
