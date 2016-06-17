package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.client.views.infopanels.units.mini.CorpsInfoMini;
import com.eaw1805.www.client.views.military.formArmy.FormArmy;
import com.eaw1805.www.shared.stores.units.ArmyStore;

public class FormArmiesDragController
        implements DragHandler {

    private AbsolutePanel apSource = null;
    private int sourceType = 0;
    private FormArmy parent = null;

    public void onDragEnd(final DragEndEvent event) {
        if (parent != null) {
            parent.rearrangeCorps();
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
                final CorpsInfoMini brigInfo = (CorpsInfoMini) dg.draggable;
                parent = (FormArmy) apDest.getParent().getParent().getParent();
                if (parent.getFreeArmy() == null) {
                    parent.getCorps().add(brigInfo.getCorp());
                } else {
                    ArmyStore.getInstance().changeCorpArmy(brigInfo.getCorp().getCorpId(), 0, parent.getFreeArmy().getArmyId());
                }

            } else if (dg.finalDropController.getDropTarget().getClass().equals(VerticalPanel.class)
                    && sourceType == 0) {
                final CorpsInfoMini corpInfo = (CorpsInfoMini) dg.draggable;
                if (corpInfo.getCorp().getCorpId() != 0) {
                    parent = (FormArmy) apSource.getParent().getParent().getParent();
                    if (corpInfo.getCorp().getArmyId() == 0) {
                        parent.removeCorpById(corpInfo.getCorp().getCorpId());
                    } else {
                        if (parent.getFreeArmy().getCorps().size() == 1) {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "By removing the last corps from the army, you are disbanding the army, continue?", true) {
                                public void onAccept() {
                                    ArmyStore.getInstance().changeCorpArmy(corpInfo.getCorp().getCorpId(), parent.getFreeArmy().getArmyId(), 0);
                                    parent.rearrangeCorps();
                                    parent.setLabels();

                                }

                                public void onReject() {
                                    parent.rearrangeCorps();
                                    parent.setLabels();
                                }
                            };
                        } else {
                            ArmyStore.getInstance().changeCorpArmy(corpInfo.getCorp().getCorpId(), parent.getFreeArmy().getArmyId(), 0);
                        }
                    }
                }
                parent = (FormArmy) apSource.getParent().getParent().getParent();

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
