package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.List;

public class BrigadesViewerPopup extends DraggablePanel {

    int column = 0;

    public BrigadesViewerPopup(final List<BrigadeDTO> brigades, final String title) {

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

        final ScrollVerticalBarEAW scrollPanel = new ScrollVerticalBarEAW(hPanel, 89, true);
        side1.setScroller(scrollPanel, true);
        side2.setScroller(scrollPanel, true);
        scrollPanel.setSize(766, 396);
        add(scrollPanel, 19, 64);


        new DelayIterator(0, brigades.size(), 1) {

            @Override
            public void executeStep() {
                final BrigadeDTO brigade = brigades.get(ITERATE_INDEX);
                if (column == 0) {
                    side1.add(new BrigadeInfoPanel(brigade, true));
                    column = 1;
                } else {
                    side2.add(new BrigadeInfoPanel(brigade, true));
                    column = 0;
                }
            }

            @Override
            public void executeLast() {
                //do nothing here
            }
        }.run();


        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close popup");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(BrigadesViewerPopup.this);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        add(imgX, 750, 8);
        imgX.setSize("36px", "36px");
    }
}
