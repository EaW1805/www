package com.eaw1805.www.client.views.infopanels;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.movement.MovementStopEvent;
import com.eaw1805.www.client.events.movement.MovementStopHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.popups.OrderCostInfoPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.adamtacy.client.ui.effects.impl.Fade;

/**
 * Provides gui graphics for info panel
 */
public class OrderInfoPanel
        extends AbsolutePanel
        implements OrderConstants {

    private final Label name;
    private final ClientOrderDTO clientOrder;
    private final AbsolutePanel bgPanel;
    Timer t;

    public OrderInfoPanel(final ClientOrderDTO order) {
        clientOrder = order;
        setSize("170px", "45px");
        setStyleName("orderInfoPanel");
        bgPanel = new AbsolutePanel();

        bgPanel.setSize("170px", "45px");
        bgPanel.setStyleName("costLowWarning");
        bgPanel.getElement().getStyle().setOpacity(0.5);
        add(bgPanel, 0, 0);


        name = new Label(OrderStore.getInstance().getOrderDescription(order));
        name.setStyleName("clearFontMini");
        add(name, 3, 3);

        final ImageButton viewImg;
        if (OrderStore.getInstance().getPositionRelatedToOrder(order) == null) {
            viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff-Gray.png");
            viewImg.setTitle("This order doesn't specify a position to go to.");

        } else {
            viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
            viewImg.setTitle("Go to position related to this order");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    MapStore.getInstance().getMapsView()
                            .goToPosition(OrderStore.getInstance().getPositionRelatedToOrder(order));
                    viewImg.deselect();
                }
            }).addToElement(viewImg.getElement()).register();
        }
        viewImg.setSize("20px", "20px");
        viewImg.setStyleName("pointer");
        add(viewImg, 147, 22);

        final ImageButton cancelImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        cancelImg.setStyleName("pointer");
        cancelImg.setSize("20px", "20px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                OrderStore.getInstance().cancelOrder(order);
            }
        }).addToElement(cancelImg.getElement()).register();

        cancelImg.setTitle("Cancel order");
        add(cancelImg, 124, 22);

        //at last.... assign a cost info tooltip only if there is a non zero cost value
        for (int index = 0; index < 14; index++) {
            if (order.getCosts().getNumericCost(index + 1) != 0) {
                new ToolTipPanel(this){
                    @Override
                    public void generateTip() {
                        setTooltip(new OrderCostInfoPopup(order));
                    }
                };
                break;
            }
        }

        // For Movement orders monitor the movement
        if (order.getOrderTypeId() == ORDER_M_UNIT) {
            final int unitType = order.getIdentifier(0);
            final int unitId = order.getIdentifier(1);

            MovementEventManager.addMovementStopHandler(new MovementStopHandler() {
                public void onMovementStop(final MovementStopEvent event) {
                    if (event.getInfoType() == unitType && event.getInfoId() == unitId) {
                        // Movement was updated, change description
                        name.setText(OrderStore.getInstance().getOrderDescription(order));
                    }
                }
            });
        }

        t = new Timer() {
            @Override
            public void run() {
                animate();
            }
        };
        t.schedule(6000);
    }

    public void animate() {
        Fade f = new Fade(bgPanel.getElement());
        f.setDuration(1);
        f.setStartOpacity(50);
        f.setEndOpacity(0);
        f.play();
    }

    public void updateDescription() {
        name.setText(OrderStore.getInstance().getOrderDescription(clientOrder));
        bgPanel.getElement().getStyle().setOpacity(0.5);
        t.schedule(6000);
    }
}
