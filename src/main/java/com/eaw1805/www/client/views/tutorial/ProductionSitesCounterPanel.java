package com.eaw1805.www.client.views.tutorial;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.economy.OrderAddedEvent;
import com.eaw1805.www.client.events.economy.OrderAddedHandler;
import com.eaw1805.www.client.events.economy.OrderRemovedEvent;
import com.eaw1805.www.client.events.economy.OrderRemovedHandler;

public class ProductionSitesCounterPanel extends AbsolutePanel implements ProductionSiteConstants, OrderConstants {
    int numOfEstates;
    int numOfLumberCamps;
    int numOfSheepFarms;
    final int MAX_ESTATES = 1;
    final int MAX_LUMBERCAMPS = 2;
    final int MAX_SHEEP_FARMS = 4;

    public ProductionSitesCounterPanel() {
        setSize("300px", "50px");
        setStyleName("psCounterPanel");
        final Image estate = new Image("http://static.eaw1805.com/tiles/sites/tprod-" + PS_ESTATE + ".png");
        estate.setSize("35px", "");
        estate.setTitle("Estates");
        final Image lumberCamp = new Image("http://static.eaw1805.com/tiles/sites/tprod-" + PS_LUMBERCAMP + ".png");
        lumberCamp.setSize("35px", "");
        lumberCamp.setTitle("Lumbercamps");
        final Image sheepFarm = new Image("http://static.eaw1805.com/tiles/sites/tprod-" + PS_FARM_SHEEP + ".png");
        sheepFarm.setSize("35px", "");
        sheepFarm.setTitle("Sheep farms");
        add(estate, 3, 10);
        add(lumberCamp, 100, 10);
        add(sheepFarm, 197, 10);
        numOfEstates = 1;
        numOfLumberCamps = 1;
        numOfSheepFarms = 1;
        final Label estatesLabel = new Label(numOfEstates + " / " + MAX_ESTATES);
        final Label lumbercampsLabel = new Label(numOfLumberCamps + " / " + MAX_LUMBERCAMPS);
        final Label sheepFarmLabel = new Label(numOfSheepFarms + " / " + MAX_SHEEP_FARMS);
        estatesLabel.setStyleName("clearFontMedium whiteText");
        lumbercampsLabel.setStyleName("clearFontMedium whiteText");
        sheepFarmLabel.setStyleName("clearFontMedium whiteText");
        add(estatesLabel, 45, 17);
        add(lumbercampsLabel, 142, 17);
        add(sheepFarmLabel, 239, 17);
        EcoEventManager.addOrderRemovedHandler(new OrderRemovedHandler() {
            @Override
            public void onOrderRemoved(OrderRemovedEvent event) {
                final ClientOrderDTO order = event.getClientOrder();
                if (order.getOrderTypeId() == ORDER_B_PRODS) {
                    switch (order.getIdentifier(1)) {
                        case PS_ESTATE:
                            numOfEstates--;
                            estatesLabel.setText(numOfEstates + " / " + MAX_ESTATES);
                            break;
                        case PS_LUMBERCAMP:
                            numOfLumberCamps--;
                            lumbercampsLabel.setText(numOfLumberCamps + " / " + MAX_LUMBERCAMPS);
                            break;
                        case PS_FARM_SHEEP:
                            numOfSheepFarms--;
                            sheepFarmLabel.setText(numOfSheepFarms + " / " + MAX_SHEEP_FARMS);
                            break;
                    }
                }
            }
        });

        EcoEventManager.addOrderAddedHandler(new OrderAddedHandler() {
            @Override
            public void onOrderAdded(OrderAddedEvent event) {
                final ClientOrderDTO order = event.getClientOrder();
                if (order.getOrderTypeId() == ORDER_B_PRODS) {
                    switch (order.getIdentifier(1)) {
                        case PS_ESTATE:
                            numOfEstates++;
                            estatesLabel.setText(numOfEstates + " / " + MAX_ESTATES);
                            break;
                        case PS_LUMBERCAMP:
                            numOfLumberCamps++;
                            lumbercampsLabel.setText(numOfLumberCamps + " / " + MAX_LUMBERCAMPS);
                            break;
                        case PS_FARM_SHEEP:
                            numOfSheepFarms++;
                            sheepFarmLabel.setText(numOfSheepFarms + " / " + MAX_SHEEP_FARMS);
                            break;
                    }
                }
            }
        });
    }

    public boolean tutorialFinished() {
        return (numOfEstates >= MAX_ESTATES
                && numOfLumberCamps >= MAX_LUMBERCAMPS
                && numOfSheepFarms >= MAX_SHEEP_FARMS);
    }
}
