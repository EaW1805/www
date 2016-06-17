package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.economy.OrderRemovedEvent;
import com.eaw1805.www.client.events.economy.OrderRemovedHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.OrderInfoPanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;

import java.util.Iterator;
import java.util.List;

/**
 * This panel is a popup that displays a list of orders given as parameter in the constructor.
 */
public class OrdersViewerPopup extends PopupPanelEAW {
    private final AbsolutePanel basePanel = new AbsolutePanel();
    List<ClientOrderDTO> conflictOrders;

    public OrdersViewerPopup(final List<ClientOrderDTO> orders, final String title) {
        conflictOrders = orders;

        this.setAutoHideEnabled(true);
        this.setStylePrimaryName("none");
        this.getElement().getStyle().setZIndex(1000);
        this.add(basePanel);
        basePanel.setStyleName("ordersDoubleSelector");
        basePanel.setSize("390px", "480px");

        final Label titleLbl = new Label();
        titleLbl.setText(title);

        titleLbl.setStyleName("clearFontMiniTitle whiteText");
        basePanel.add(titleLbl, 24, 19);

        setupPanel();
        EcoEventManager.addOrderRemovedHandler(new OrderRemovedHandler() {
            @Override
            public void onOrderRemoved(final OrderRemovedEvent event) {
                final Iterator<ClientOrderDTO> orderIter = conflictOrders.iterator();

                while (orderIter.hasNext()) {
                    final ClientOrderDTO clientOrder = orderIter.next();
                    if (clientOrder.getOrderTypeId() == event.getClientOrder().getOrderTypeId()) {
                        boolean matchids = true;
                        for (int i = 0; i < 9; i++) {
                            if (clientOrder.getIdentifier(i) != event.getClientOrder().getIdentifier(i)) {
                                matchids = false;
                                break;
                            }
                        }
                        if (matchids) {
                            orderIter.remove();
                            break;
                        }
                    }

                }
                setupPanel();
            }
        });
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close popup");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                OrdersViewerPopup.this.hide();
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        basePanel.add(imgX, 340, 8);
        imgX.setSize("36px", "36px");
    }

    ScrollVerticalBarEAW scrollPanel;

    public final void setupPanel() {
        if (scrollPanel != null) {
            try {
                basePanel.remove(scrollPanel);
            } catch (Exception ignore) {
            }
        }
        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setWidth("350px");
        final VerticalPanelScrollChild side1 = new VerticalPanelScrollChild();
        final VerticalPanelScrollChild side2 = new VerticalPanelScrollChild();
        hPanel.add(side1);
        hPanel.add(side2);

        scrollPanel = new ScrollVerticalBarEAW(hPanel, 89, false);
        side1.setScroller(scrollPanel, false);
        side2.setScroller(scrollPanel, false);
        scrollPanel.setSize(368, 396);
        basePanel.add(scrollPanel, 13, 67);


        int column = 0;
        for (ClientOrderDTO order : conflictOrders) {
            if (column == 0) {
                side1.add(new OrderInfoPanel(order));
                column = 1;
            } else {
                side2.add(new OrderInfoPanel(order));
                column = 0;
            }
        }
    }

}
