package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;

import java.util.List;


public class ArmiesViewerPopup extends DraggablePanel {


    public ArmiesViewerPopup(final List<ArmyDTO> armies, final String title, final boolean includeZero) {

        setStyleName("bigDoubleSelector");
        setSize("800px", "478px");

        final Label titleLbl = new Label();
        titleLbl.setText(title);

        titleLbl.setStyleName("clearFontMiniTitle whiteText");
        add(titleLbl, 32, 19);

        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setWidth("752px");
        final VerticalPanelScrollChild side1 = new VerticalPanelScrollChild();
        final VerticalPanelScrollChild side2 = new VerticalPanelScrollChild();

        hPanel.add(side1);
        hPanel.add(side2);

        final ScrollVerticalBarEAW scrollPanel = new ScrollVerticalBarEAW(hPanel, 89, false);
        side1.setScroller(scrollPanel, false);
        side2.setScroller(scrollPanel, false);
        scrollPanel.setSize(766, 396);
        add(scrollPanel, 19, 64);


        int column = 0;
        for (ArmyDTO army : armies) {
            if (army.getArmyId() != 0 || includeZero) {
                if (column == 0) {
                    side1.add(new ArmyInfoPanel(army));
                    column = 1;
                } else {
                    side2.add(new ArmyInfoPanel(army));
                    column = 0;
                }
            }
        }

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close popup");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                ArmiesViewerPopup.this.close();
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        add(imgX, 750, 8);
        imgX.setSize("36px", "36px");
    }
}
