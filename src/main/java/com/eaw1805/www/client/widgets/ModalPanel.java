package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.shared.stores.GameStore;

public class ModalPanel
        extends VerticalPanel {

    private final AbsolutePanel containerPanel;
    private final ImageButton imgX;

    public ModalPanel() {
        super();
        setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        setStyleName("blackPanel");
        setSize("100%", "100%");

        containerPanel = new AbsolutePanel();
        add(containerPanel);
        containerPanel.setSize("688px", "606px");

        imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");

        imgX.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                GameStore.getInstance().getLayoutView().removeLastWidgetFromPanel();
                imgX.deselect();
            }
        });
        imgX.setStyleName("pointer");
        imgX.setSize("28px", "30px");

    }

    public void setCoordPosition(final int xCoord, final int yCoord) {
        containerPanel.add(imgX, xCoord, yCoord);
    }


    /**
     * Method that returns the container panel
     * of our widgets
     *
     * @return the containerPanel
     */
    public AbsolutePanel getContainerPanel() {
        return containerPanel;
    }

    /**
     * @return the imgX
     */
    public ImageButton getImgX() {
        return imgX;
    }


}
