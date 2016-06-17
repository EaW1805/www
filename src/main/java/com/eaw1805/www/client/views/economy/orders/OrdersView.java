package com.eaw1805.www.client.views.economy.orders;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersView extends DraggablePanel implements GoodConstants {


    private static final String[] NAMES =
            {"Money", "Citizens",
                    "Industrial Points", "Food", "Stone", "Wood", "Ore", "Gems",
                    "Horse", "Fabric", "Wool", "Precious Metals", "Wine", "Colonial Goods"};

    private AbsolutePanel orderPanel;
    private VerticalPanelScrollChild ordersGrid = new VerticalPanelScrollChild();
    ;
    private HorizontalPanel[][] orderRows = new HorizontalPanel[2][20];
    private OrderStore orStore = OrderStore.getInstance();
    private HorizontalPanel pagesPanel;
    private int page = 0;
    private HorizontalPanel pagNumPanel;

    /**
     * The instance of the GameStore where the layout panel
     * and global game variables reside
     */
    private GameStore gameStore = GameStore.getInstance();
    private Label lblOrder;
    List<Integer> selectedOrders = new ArrayList<Integer>();
    final Label titleLbl;

    public OrdersView(final ImageButton caller) {
        this.setSize("1217px", "576px");

        this.orderPanel = new AbsolutePanel();
        this.add(this.orderPanel);


        this.orderPanel.setStyleName("ordersPanel");
        this.orderPanel.setSize("1217px", "576px");


        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        this.add(imgX, 1124, 8);
        imgX.setSize("36px", "36px");
        final OrdersView self = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
                if (caller != null) {
                    caller.deselect();
                }
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 18) {
                    TutorialStore.getInstance().setOrdersViewDone(true);
                }
            }
        }).addToElement(imgX.getElement()).register();

        titleLbl = new Label("Review orders");
        titleLbl.setStyleName("clearFont-large whiteText");
        this.add(titleLbl, 27, 10);


        final ScrollVerticalBarEAW scroller = new ScrollVerticalBarEAW(ordersGrid, 26, false);
        scroller.enableAndSetStep(26);
        scroller.setSize(1162, 418);
        this.orderPanel.add(scroller, 0, 88);
        this.ordersGrid.setSize("792px", "");

        this.pagesPanel = new HorizontalPanel();
        this.pagesPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        this.pagesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.orderPanel.add(this.pagesPanel, 25, 510);
        this.pagesPanel.setSize("788px", "39px");

        this.pagNumPanel = new HorizontalPanel();
        this.pagesPanel.add(this.pagNumPanel);
        this.pagNumPanel.setHeight("29px");

        this.lblOrder = new Label("Order");
        this.lblOrder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.lblOrder.setStyleName("clearFont-large");
        this.lblOrder.setStyleName("whiteText", true);
        this.orderPanel.add(this.lblOrder, 76, 55);
        this.lblOrder.setSize("89px", "28px");
        setupHiddenLabels();
        addPages();
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

    public void addPages() {
        int num = 0;

        pagNumPanel.clear();
        Label pagesLabel = new Label("Phases: ");
        pagesLabel.setStyleName("clearFont-large");
        pagNumPanel.add(pagesLabel);
        final List<Circle> circles = new ArrayList<Circle>();
        final Map<Integer, List<Integer>> phaseToOrders = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, Integer> entry : OrderStore.ORDER_PHASE.entrySet()) {
            if (!phaseToOrders.containsKey(entry.getValue())) {
                phaseToOrders.put(entry.getValue(), new ArrayList<Integer>());
            }
            phaseToOrders.get(entry.getValue()).add(entry.getKey());
        }

        if (phaseToOrders.containsKey(10)) {
            selectedOrders.clear();
            selectedOrders.addAll(phaseToOrders.get(10));//init with "Organize Land & Naval Forces" orders
        }


        for (final Map.Entry<Integer, String> entry : OrderStore.PHASE_NAMES.entrySet()) {
            num++;
            final int pageNo = num;
            DrawingArea canvas = new DrawingArea(30, 30);
            int x = 8;
            if (num > 9) {
                x = 4;
            }
            Text pageNum = new Text(x, 22, String.valueOf(num));
            final Circle circle = new Circle(15, 15, 14);
            circles.add(circle);
            final List<Integer> orderIds = new ArrayList<Integer>();
            if (phaseToOrders.containsKey(entry.getKey())) {
                orderIds.addAll(phaseToOrders.get(entry.getKey()));
            }
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    setPage(pageNo);
                    selectedOrders.clear();
                    selectedOrders.addAll(orderIds);
                    for (int index = 0; index < circles.size(); index++) {
                        if (index == pageNo - 1) {
                            circles.get(index).setFillOpacity(0.2);
                        } else {
                            circles.get(index).setFillOpacity(0.0);
                        }
                    }
                    refreshRows(selectedOrders);
                    titleLbl.setText("Review orders - " + entry.getValue());
                }
            }).addToElement(pageNum.getElement()).register();

            circle.setFillColor("black");
            final StringBuilder title = new StringBuilder();
            title.append(entry.getValue()).append("\n");
            if (phaseToOrders.containsKey(entry.getKey())) {
                for (Integer orderId : phaseToOrders.get(entry.getKey())) {
                    title.append("\n").append(OrderStore.ORDER_NAMES.get(orderId));
                }
            }
            circle.setTitle(title.toString());
            pageNum.setTitle(title.toString());
            if (num == 1) {
                titleLbl.setText("Review orders - " + entry.getValue());
                circle.setFillOpacity(0.2);
            } else {
                circle.setFillOpacity(0.0);
            }
            canvas.add(circle);
            canvas.add(pageNum);
            pagNumPanel.add(canvas);
        }
    }


    public void refreshRows() {
        refreshRows(selectedOrders);
    }


    public void refreshRows(final List<Integer> orderIds) {
        ordersGrid.clear();
        List<ClientOrderDTO> ordersToShow = new ArrayList<ClientOrderDTO>();
        for (Integer id : orderIds) {
            if (OrderStore.getInstance().getClientOrders().containsKey(id)) {
                ordersToShow.addAll(OrderStore.getInstance().getClientOrders().get(id));
            }
        }
        int count = 0;
        for (ClientOrderDTO order : ordersToShow) {
            boolean canGoUp = true, canGoDown = true;
            if (count == 0) {
                canGoUp = false;
            } else if (ordersToShow.get(count - 1).getOrderTypeId() != order.getOrderTypeId()) {
                canGoUp = false;
            }
            if (order.getOrderTypeId() != OrderConstants.ORDER_M_UNIT) {
                canGoUp = false;
                canGoDown = false;
            }

            if (count == ordersToShow.size() - 1) {
                canGoDown = false;
            } else if (ordersToShow.get(count + 1).getOrderTypeId() != order.getOrderTypeId()) {
                canGoDown = false;
            }
            ordersGrid.add(new OrderRow(order, this, canGoUp, canGoDown));
            count++;
        }
    }

    /**
     * @param page the page to set
     */
    public void setPage(final int page) {
        this.page = page;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }
}
