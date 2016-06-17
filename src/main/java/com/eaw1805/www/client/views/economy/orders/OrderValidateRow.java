package com.eaw1805.www.client.views.economy.orders;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;

import java.util.Map;


public class OrderValidateRow extends Composite
        implements GoodConstants {

    private ClientOrderDTO clientOrder = new ClientOrderDTO();
    private Label lblOrdertype;

    public OrderValidateRow(final ClientOrderDTO clientOrder, final OrderValidateView ordersView,
                            Map<Integer, Integer> goodsSoFar, final boolean isTheConflictOne,
                            final boolean isWarehouse, final int regionId) {
        this.setClientOrder(clientOrder);
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();

        final AbsolutePanel rowPanel = new AbsolutePanel();
        initWidget(rowPanel);
        rowPanel.setSize("1166px", "26px");
        if (isTheConflictOne) {
            rowPanel.setStyleName("orderCheck");
        } else if (isWarehouse) {
            rowPanel.setStyleName("orderCheckWarehouse");
        }
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        rowPanel.add(absolutePanel, 0, 0);
        absolutePanel.setSize("21px", "26px");

        final AbsolutePanel orderType = new AbsolutePanel();
        rowPanel.add(orderType, 21, 0);
        orderType.setSize("190px", "26px");

        this.lblOrdertype = new Label("OrderType");
        this.lblOrdertype.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.lblOrdertype.setStyleName("clearFontMini");
        orderType.add(this.lblOrdertype, 0, 3);
        this.lblOrdertype.setSize("190px", "26px");

        final AbsolutePanel name = new AbsolutePanel();
        name.setSize("86px", "26px");

        for (int i = GOOD_FIRST; i <= GOOD_LAST; i++) {
            final AbsolutePanel[] goodPanels = new AbsolutePanel[16];
            goodPanels[i - 1] = new AbsolutePanel();
            if (i == GOOD_MONEY) {
                rowPanel.add(goodPanels[i - 1], 205, 3);
            } else if (i == 15) {
                rowPanel.add(goodPanels[i - 1], 972, 3);
            } else if (i == 16) {
                rowPanel.add(goodPanels[i - 1], 1007, 3);
            } else {
                rowPanel.add(goodPanels[i - 1], (242 + (i - 1) * 53), 3);
            }
            if (i == GOOD_MONEY) {
                goodPanels[i - 1].setSize("95px", "26px");
            } else {
                goodPanels[i - 1].setSize("56px", "26px");
            }


            int quantity = goodsSoFar.get(i);
            initRow(isTheConflictOne, isWarehouse, regionId);
            int orderNeed = 0;
            if (clientOrder != null
                    && (regionId == clientOrder.getRegionId()
                    || (i == GOOD_AP || i == GOOD_AP || i == GOOD_CP))) {
                orderNeed = clientOrder.getCosts().getNumericCost(i);
            }


            final Label[] lblGoods = new Label[16];
            lblGoods[i - 1] = new Label(numberFormat.format(quantity));
            lblGoods[i - 1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            lblGoods[i - 1].setStyleName("clearFontSmall");
            if (quantity < 0) {
                lblGoods[i - 1].setStyleName("redText", true);
                if (!isTheConflictOne
                        && orderNeed > 0) {
                    rowPanel.setStyleName("orderConflicted");
                }
            }
            goodPanels[i - 1].add(lblGoods[i - 1], 0, 0);
            if (i == GOOD_MONEY) {
                lblGoods[i - 1].setSize("87px", "22px");
            } else {
                lblGoods[i - 1].setSize("52px", "22px");
            }
        }

        final AbsolutePanel commands = new AbsolutePanel();
        commands.setSize("80px", "26px");
        rowPanel.add(commands, 1072, 0);
        if (!isTheConflictOne
                && !isWarehouse) {
//            final ImageButton upImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomInOff.png");
//            (new DelEventHandlerAbstract() {
//                @Override
//                public void execute(final MouseEvent event) {
//                    OrderStore.getInstance().changeOrdersOrder(clientOrder, true);
//                    ordersView.refreshRows();
//                    upImg.deselect();
//                }
//            }).addToElement(upImg.getElement()).register();

//            upImg.setStyleName("pointer");
//            upImg.setSize("25px", "25px");
//            upImg.setTitle("Increase the priority of the order. It will be executed earlier during next month");
//            commands.add(upImg, 1, 0);

//            final ImageButton downImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png");
//            (new DelEventHandlerAbstract() {
//                @Override
//                public void execute(final MouseEvent event) {
//                    OrderStore.getInstance().changeOrdersOrder(clientOrder, false);
//                    ordersView.refreshRows();
//                    downImg.deselect();
//                }
//            }).addToElement(downImg.getElement()).register();

//            downImg.setStyleName("pointer");
//            downImg.setSize("25px", "25px");
//            downImg.setTitle("Decrease the priority of the order. It will be executed later during next turn");
//            commands.add(downImg, 27, 0);

            final ImageButton cancelImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
            cancelImg.setStyleName("pointer");
            commands.add(cancelImg, 53, 0);
            cancelImg.setSize("25px", "25px");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    OrderStore.getInstance().cancelOrder(clientOrder);
                    ordersView.refreshRows();
                }
            }).addToElement(cancelImg.getElement()).register();
            cancelImg.setTitle("Cancel order");
        }

    }

    private void initRow(final boolean isTheConflictOne, final boolean isWarehouse, final int regionId) {
        if (isTheConflictOne) {
            lblOrdertype.setText("Your new order");
        } else if (isWarehouse) {
            lblOrdertype.setText("Initial state of " + RegionStore.getInstance().getRegionNameById(regionId) + " warehouse");
        } else {
            lblOrdertype.setText(OrderStore.getInstance().getOrderDescription(clientOrder));
        }


    }

    /**
     * @param clientOrder the clientOrder to set
     */
    public void setClientOrder(final ClientOrderDTO clientOrder) {
        this.clientOrder = clientOrder;
    }


}
