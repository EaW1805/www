package com.eaw1805.www.client.views.economy.orders;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderValidateView extends DraggablePanel implements GoodConstants, RegionConstants, ArmyConstants {

    private static final String[] NAMES =
            {"Money", "Citizens",
                    "Industrial Points", "Food", "Stone", "Wood", "Ore", "Gems",
                    "Horse", "Fabric", "Wool", "Precious Metals", "Wine", "Colonial Goods"};

    private AbsolutePanel orderPanel;
    private VerticalPanelScrollChild ordersGrid = new VerticalPanelScrollChild();

    private OrderStore orStore = OrderStore.getInstance();


    /**
     * The instance of the GameStore where the layout panel
     * and global game variables reside
     */

    private Label lblOrder;
    private ClientOrderDTO orderToCheck;

    public OrderValidateView(final ClientOrderDTO orderToCheck) {
        this.setSize("1217px", "576px");
        this.orderToCheck = orderToCheck;

        this.orderPanel = new AbsolutePanel();
        this.add(this.orderPanel);


        this.orderPanel.setStyleName("ordersPanel");
        this.orderPanel.setSize("1217px", "576px");


        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        this.add(imgX, 1129, 9);
        imgX.setSize("36px", "36px");
        final OrderValidateView self = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        final Label title = new Label("Orders effects in " + RegionStore.getInstance().getRegionNameById(orderToCheck.getRegionId())
                + "'s warehouse");
        title.setStyleName("clearFont-large whiteText");
        this.add(title, 382, 8);

        final ScrollVerticalBarEAW scroller = new ScrollVerticalBarEAW(ordersGrid, 26, false);
        scroller.enableAndSetStep(26);
        scroller.setSize(1177, 418);
        this.orderPanel.add(scroller, 0, 88);
        this.ordersGrid.setSize("792px", "");


        this.lblOrder = new Label("Order");
        this.lblOrder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.lblOrder.setStyleName("clearFont-large");
        this.lblOrder.setStyleName("whiteText", true);
        this.orderPanel.add(this.lblOrder, 90, 61);
        this.lblOrder.setSize("89px", "28px");
        setupHiddenLabels();
        refreshRows();
    }

    /**
     * Hidden labels that will work for some images in the background that cannot be tooltiped
     */
    public void setupHiddenLabels() {
        int posX;
        int posY = 63;

        posX = 329;
        for (String goodName : NAMES) {
            final Label hiddenLabel = new Label("");
            hiddenLabel.setSize("31px", "29px");

            hiddenLabel.setTitle(goodName);

            if ("money".equalsIgnoreCase(goodName)) {
                this.orderPanel.add(hiddenLabel, 258, posY);
            } else {
                this.orderPanel.add(hiddenLabel, posX, posY);
                posX += 52;
            }


        }
    }


    public void refreshRows() {
        ordersGrid.clear();
        Map<Integer, Integer> regionExpensesSoFar = new HashMap<Integer, Integer>();

        //init map with original values.
        for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
            int curRegion = EUROPE;
            if (goodId != 1 && goodId != GOOD_AP && goodId != GOOD_CP) {
                curRegion = orderToCheck.getRegionId();
            }
            if (curRegion == 0) {//if region doesn't matter.. user europe to avoid crashes
                curRegion = EUROPE;
            }
            regionExpensesSoFar.put(goodId, WarehouseStore.getInstance().getWareHouseMap().get(curRegion).getOriginalGoodsDTO().get(goodId).getQte());
        }
        ordersGrid.add(new OrderValidateRow(null, this, regionExpensesSoFar, false, true, orderToCheck.getRegionId()));


        List<ClientOrderDTO> orders = new ArrayList<ClientOrderDTO>();
        for (List<ClientOrderDTO> orderDTOs : orStore.getClientOrders().values()) {
            orders.addAll(orderDTOs);
        }

        for (ClientOrderDTO order : orders) {
            if (order.getOrderTypeId() <= orderToCheck.getOrderTypeId()) {
                //calculate the difference from this order
                boolean shouldAdd = false;
                for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
                    int sign = getSign(order, goodId);
                    if (sign == -2) {
                        continue;
                    }
                    if (((goodId == 1 || goodId == GOOD_AP || goodId == GOOD_CP) && order.getCosts().getNumericCost(goodId) != 0)
                            || order.getRegionId() == orderToCheck.getRegionId()) {
                        shouldAdd = true;
                        regionExpensesSoFar.put(goodId, regionExpensesSoFar.get(goodId) + sign * order.getCosts().getNumericCost(goodId));
                    }
                }
                if (shouldAdd) {
                    ordersGrid.add(new OrderValidateRow(order, this, regionExpensesSoFar, false, false, orderToCheck.getRegionId()));
                }
            }
        }
        //calculate the difference from this order
        for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
            int sign = getSign(orderToCheck, goodId);
            if (sign == -2) {
                continue;
            }
            regionExpensesSoFar.put(goodId, regionExpensesSoFar.get(goodId) + sign * orderToCheck.getCosts().getNumericCost(goodId));
        }

        ordersGrid.add(new OrderValidateRow(orderToCheck, this, regionExpensesSoFar, true, false, orderToCheck.getRegionId()));
        for (ClientOrderDTO order : orders) {
            if (order.getOrderTypeId() > orderToCheck.getOrderTypeId()) {
                //calculate the difference from this order
                boolean shouldAdd = false;
                for (int goodId = 1; goodId <= GOOD_LAST; goodId++) {
                    int sign = getSign(order, goodId);
                    if (sign == -2) {
                        continue;
                    }
                    if (((goodId == GOOD_MONEY || goodId == GOOD_AP || goodId == GOOD_CP) && order.getCosts().getNumericCost(goodId) != 0)
                            || order.getRegionId() == orderToCheck.getRegionId()) {
                        shouldAdd = true;
                        regionExpensesSoFar.put(goodId, regionExpensesSoFar.get(goodId) + sign * order.getCosts().getNumericCost(goodId));
                    }
                }
                if (shouldAdd) {
                    ordersGrid.add(new OrderValidateRow(order, this, regionExpensesSoFar, false, false, orderToCheck.getRegionId()));
                }
            }
        }
        ordersGrid.resizeScrollBar();
    }

    public int getSign(final ClientOrderDTO order, final int goodId) {
        int sign = -1;
        if ((order.getOrderTypeId() == OrderConstants.ORDER_EXCHF
                || order.getOrderTypeId() == OrderConstants.ORDER_EXCHS)
                && order.getIdentifier(2) != WAREHOUSE
                && order.getIdentifier(0) != WAREHOUSE) {
            if (goodId == GOOD_AP || goodId == GOOD_CP) {
                return -1;//this means remove it from warehouse
            }
            return -2;   //this means it doesn't count.
        } else if (order.getOrderTypeId() == OrderConstants.ORDER_EXCHF
                || order.getOrderTypeId() == OrderConstants.ORDER_EXCHS) {
            if (goodId == GOOD_AP || goodId == GOOD_CP) {
                return -1;//this means remove it from warehouse
            }
            if (order.getIdentifier(0) == WAREHOUSE) {//this means the warehouse gives.
                if (order.getIdentifier(2) == TRADECITY && goodId == GOOD_MONEY) {
                    sign = 1; //this means add it to the warehouse
                } else {
                    sign = -1; // this means remove it from warehouse
                }

            } else if (order.getIdentifier(2) == WAREHOUSE) {
                if (order.getIdentifier(0) == TRADECITY) {
                    if (goodId == GOOD_MONEY) {
                        sign = -1;//this means remove it from warehouse
                    } else {
                        sign = 1;//this means add it to warehouse.
                    }
                } else {
                    sign = 1;//this means add it to warehouse
                }

            }

        }
        return sign;
    }


}
