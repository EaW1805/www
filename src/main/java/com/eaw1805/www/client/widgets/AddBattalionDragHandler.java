package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.Window;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.www.client.views.extras.ArmyImage;
import com.eaw1805.www.client.views.military.addbattalion.AddToSelectedPanel;

public class AddBattalionDragHandler implements DragHandler {

    private ArmyImage dragImg;

    private final AddToSelectedPanel addToSelectedPanel;

    public AddBattalionDragHandler(final AddToSelectedPanel addToSelectedPanel) {
        this.addToSelectedPanel = addToSelectedPanel;
    }

    public void onDragEnd(final DragEndEvent event) {
        // do nothing here

    }

    public void onDragStart(final DragStartEvent event) {
        dragImg = (ArmyImage) event.getContext().draggable;
    }

    public void onPreviewDragEnd(final DragEndEvent event)
            throws VetoDragException {

        if (event.getContext().finalDropController != null) {
            if (!addToSelectedPanel.canAddBattalion(dragImg.getArmyTypeDTO())) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot mix infantry and cavalry battalions together in the same brigade", false);
                throw new VetoDragException();
            }
            final ClickAbsolutePanel dpPanel = (ClickAbsolutePanel) event.getContext().finalDropController.getDropTarget();
            final ArmyImage imgToInsert = new ArmyImage();
            imgToInsert.setArmyTypeDTO(dragImg.getArmyTypeDTO());
            imgToInsert.setUrl(dragImg.getUrl());
            imgToInsert.setWidth(dragImg.getWidth() + "px");
            imgToInsert.setHeight(dragImg.getHeight() + "px");
            addToSelectedPanel.setNewBattalionImage(imgToInsert, dpPanel);
            addToSelectedPanel.addNewBattRow(dragImg.getArmyTypeDTO(), dpPanel.getId());
        }
        throw new VetoDragException();
    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        if (!addToSelectedPanel.isSelectedBrig()) {
            throw new VetoDragException();
        }
    }

}
