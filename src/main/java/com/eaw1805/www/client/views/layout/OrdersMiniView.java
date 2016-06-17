package com.eaw1805.www.client.views.layout;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.economy.OrderAddedEvent;
import com.eaw1805.www.client.events.economy.OrderAddedHandler;
import com.eaw1805.www.client.events.economy.OrderRemovedEvent;
import com.eaw1805.www.client.events.economy.OrderRemovedHandler;
import com.eaw1805.www.client.events.loading.*;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.OrderInfoPanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OrdersMiniView extends AbsolutePanel {

    private final VerticalPanelScrollChild ordersPanel = new VerticalPanelScrollChild();
    private final List<ClientOrderDTO> ordersToRemember = new ArrayList<ClientOrderDTO>();
    private boolean opened;
    private final Timer opener;
    private final Timer closer;
    private boolean baggageTrainsLoaded = false;
    private boolean armiesLoaded = false;
    private boolean fleetsLoaded = false;
    private boolean commandersLoaded = false;
    private boolean spiesLoaded = false;
    private boolean sectorsLoaded = false;
    private boolean alliedUnitsLoaded = false;
    private boolean ordersLoaded = false;
    private boolean productionSitesLoaded = false;
    private boolean regionLoaded = false;
    private boolean isInitialized = false;

    public OrdersMiniView() {
        opened = false;
        setStyleName("orderSidePanel");
        setSize("252px", "446px");

        final Label title = new Label("Orders History");
        title.setStyleName("clearFontMiniTitle whiteText");
        add(title, 60, 18);

        final ImageButton toggler = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (opened) {
                    opener.cancel();
                    closer.scheduleRepeating(9);
                    opened = false;
                } else {
                    closer.cancel();
                    opener.scheduleRepeating(9);
                    opened = true;

                }
            }
        }).addToElement(toggler.getElement()).register();


        this.add(toggler, 15, 410);

        final OrdersMiniView myself = this;
        opener = new Timer() {
            @Override
            public void run() {
                final int width = Window.getClientWidth();
                final LayoutView view = GameStore.getInstance().getLayoutView();
                view.setWidgetPosition(myself, view.getWidgetX(myself) - 9, view.getWidgetY(myself), false, true);
                if (view.getWidgetX(myself) < (width - (myself.getOffsetWidth() - 10))) {
                    view.setWidgetPosition(myself, width - (myself.getOffsetWidth() - 10), view.getWidgetY(myself), false, true);
                    this.cancel();
                }
            }
        };

        closer = new Timer() {
            @Override
            public void run() {
                final int width = Window.getClientWidth();
                final LayoutView view = GameStore.getInstance().getLayoutView();
                view.setWidgetPosition(myself, view.getWidgetX(myself) + 9, view.getWidgetY(myself), false, true);
                if (view.getWidgetX(myself) > (width - 40)) {
                    view.setWidgetPosition(myself, width - 40, view.getWidgetY(myself), false, true);
                    this.cancel();
                }
            }
        };

        final ScrollVerticalBarEAW scrollPanel = new ScrollVerticalBarEAW(ordersPanel, 45, true);
        scrollPanel.setSize(193, 371);
        add(scrollPanel, 50, 67);

        EcoEventManager.addOrderAddedHandler(new OrderAddedHandler() {
            public void onOrderAdded(final OrderAddedEvent event) {
                insertIntoList(event.getClientOrder());
            }
        });

        EcoEventManager.addOrderRemovedHandler(new OrderRemovedHandler() {
            public void onOrderRemoved(final OrderRemovedEvent event) {
                removeFromList(event.getClientOrder());
            }
        });

        LoadEventManager.addOrdersLoadedHandler(new OrdersLoadedHandler() {
            public void onOrdersLoaded(final OrdersLoadedEvent event) {
                ordersLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addAlliedUnitsLoadedHandler(new AlliedUnitsLoadedHandler() {
            public void onAlliedUnitsLoaded(final AlliedUnitsLoadedEvent event) {
                alliedUnitsLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
            public void onArmiesLoaded(final ArmiesLoadedEvent event) {
                armiesLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addBtrainLoadedHandler(new BtrainLoadedHandler() {
            public void onBtrainLoaded(final BtrainLoadedEvent event) {
                baggageTrainsLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addCommLoadeddHandler(new CommLoadedHandler() {
            public void onCommLoaded(final CommLoadedEvent event) {
                commandersLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addNavyLoadedHandler(new NavyLoadedHandler() {
            public void onNavyLoaded(final NavyLoadedEvent event) {
                fleetsLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addSpiesLoadedHandler(new SpiesLoadedHandler() {
            public void onSpiesLoaded(final SpiesLoadedEvent event) {
                spiesLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addSectorsLoadedHandler(new SectorsLoadedHandler() {
            public void onSectorsLoaded(final SectorsLoadedEvent event) {
                sectorsLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                productionSitesLoaded = true;
                doInit();
            }
        });

        LoadEventManager.addRegionLoadedHandler(new RegionLoadedHandler() {
            public void onRegionLoaded(final RegionLoadedEvent event) {
                regionLoaded = true;
                doInit();
            }
        });
//        initOrdersList();
    }

    public void doInit() {
        if (baggageTrainsLoaded &&
                armiesLoaded &&
                fleetsLoaded &&
                commandersLoaded &&
                spiesLoaded &&
                sectorsLoaded &&
                alliedUnitsLoaded &&
                ordersLoaded &&
                productionSitesLoaded &&
                regionLoaded &&
                !isInitialized) {
            initOrdersList();
            isInitialized = true;
        }
    }

    //init orders list.
    public void initOrdersList() {
        for (List<ClientOrderDTO> typeOrders : OrderStore.getInstance().getClientOrders().values()) {
            for (ClientOrderDTO order : typeOrders) {
                ordersPanel.insert(new OrderInfoPanel(order), 0);
                ordersToRemember.add(0, order);
            }
        }
    }

    public void insertIntoList(final ClientOrderDTO order) {
        //insert it at the top...
        ordersPanel.insert(new OrderInfoPanel(order), 0);
        ordersToRemember.add(0, order);
    }

    public void removeFromList(final ClientOrderDTO order) {
        final Iterator<ClientOrderDTO> orderIter = ordersToRemember.iterator();
        int index = 0;
        while (orderIter.hasNext()) {
            final ClientOrderDTO clientOrder = orderIter.next();
            if (clientOrder.getOrderTypeId() == order.getOrderTypeId()) {
                boolean matchids = true;
                for (int i = 0; i < 9; i++) {
                    if (clientOrder.getIdentifier(i) != order.getIdentifier(i)) {
                        matchids = false;
                        break;
                    }
                }
                if (matchids) {
                    orderIter.remove();
                    ordersPanel.remove(index);
                    break;
                }
            }
            index++;
        }
    }

    public void updateAllRelative(final int type, final int[] ids) {
        final Iterator<ClientOrderDTO> orderIter = ordersToRemember.iterator();
        int index = 0;
        while (orderIter.hasNext()) {
            final ClientOrderDTO clientOrder = orderIter.next();
            if (clientOrder.getOrderTypeId() == type) {
                boolean matchids = true;
                int count = 0;
                for (int id : ids) {
                    if (clientOrder.getIdentifier(count) != id) {
                        matchids = false;
                        break;
                    }
                    count++;
                }

                if (matchids) {
                    ((OrderInfoPanel) ordersPanel.getWidget(index)).updateDescription();
                }
            }
            index++;
        }
    }

    public boolean isOpened() {
        return opened;
    }
}
