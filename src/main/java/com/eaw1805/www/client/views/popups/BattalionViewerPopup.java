package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.mini.BattalionInfoMini;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;

import java.util.List;

public class BattalionViewerPopup extends DraggablePanel {
    private int column = 0;

    public BattalionViewerPopup(final List<BattalionDTO> battalions, final String title) {
        setStyleName("smallDoubleSelector");
        setSize("560px", "480px");

        final Label titleLbl = new Label();
        titleLbl.setText(title);

        titleLbl.setStyleName("clearFontMiniTitle whiteText");
        add(titleLbl, 24, 19);

        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setWidth("513px");
        final VerticalPanelScrollChild side1 = new VerticalPanelScrollChild();
        final VerticalPanelScrollChild side2 = new VerticalPanelScrollChild();
        hPanel.add(side1);
        hPanel.add(side2);

        final ScrollVerticalBarEAW scrollPanel = new ScrollVerticalBarEAW(hPanel, 89, true);
        side1.setScroller(scrollPanel, true);
        side2.setScroller(scrollPanel, true);
        scrollPanel.setSize(527, 396);
        add(scrollPanel, 18, 67);



        new DelayIterator(0, battalions.size(), 1) {
            @Override
            public void executeStep() {
                final BattalionDTO battalion = battalions.get(ITERATE_INDEX);
                if (column == 0) {
                    side1.add(new BattalionInfoMini(battalion));
                    column = 1;
                } else {
                    side2.add(new BattalionInfoMini(battalion));
                    column = 0;
                }
            }

            @Override
            public void executeLast() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }.run();

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close popup");

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                BattalionViewerPopup.this.close();
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        add(imgX, 510, 8);
        imgX.setSize("36px", "36px");
    }
}
