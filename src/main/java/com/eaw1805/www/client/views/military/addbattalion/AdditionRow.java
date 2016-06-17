package com.eaw1805.www.client.views.military.addbattalion;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;

public class AdditionRow
        extends AbsolutePanel {

    private final ArmyTypeDTO armyType;
    private final int slot;

    public AdditionRow(final ArmyTypeDTO armyType, final int slot, final AddToSelectedPanel addToSelectedPanel) {
        setSize("251px", "30px");
        this.armyType = armyType;
        this.slot = slot;
        final Image battImg = new Image("http://static.eaw1805.com/images/armies/" + GameStore.getInstance().getNationId() + "/"
                + armyType.getIntId() + ".jpg");
        add(battImg, 0, 0);
        battImg.setSize("30px", "30px");

        final Label lblAddedOnSlot = new Label("Added on slot:" + slot);
        lblAddedOnSlot.setStyleName("clearFontMedSmall");
        add(lblAddedOnSlot, 36, 8);
        lblAddedOnSlot.setSize("114px", "15px");

        final ImageButton removeImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                AdditionRow.this.removeFromParent();
                addToSelectedPanel.removeBattFromBrig(slot);
            }
        }).addToElement(removeImg.getElement()).register();

        add(removeImg, 227, 3);
        removeImg.setSize("24px", "24px");
    }

    /**
     * @return the armyType
     */
    public ArmyTypeDTO getArmyType() {
        return armyType;
    }

    /**
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }
}
