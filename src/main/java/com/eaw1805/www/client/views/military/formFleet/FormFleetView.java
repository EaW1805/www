package com.eaw1805.www.client.views.military.formFleet;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.client.events.units.UnitCreatedEvent;
import com.eaw1805.www.client.events.units.UnitCreatedHandler;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.FormFleetDragHandler;


public class FormFleetView extends AbsolutePanel {
    private PickupDragController pickupBrigDragController;
    private FreeShipSelector freeShipsList;
    private FleetSelector fleetSelector;
    private FromFleet formFleet;
    private SectorDTO sector;

    public FormFleetView(final SectorDTO sector) {
        this.sector = sector;
        setSize("1134px", "544px");

        setStyleName("navyPanel");
        pickupBrigDragController = new PickupDragController(this, false);
        pickupBrigDragController.addDragHandler(new FormFleetDragHandler(this));
        pickupBrigDragController.setBehaviorDragStartSensitivity(3);
        freeShipsList = new FreeShipSelector(this, pickupBrigDragController);
        fleetSelector = new FleetSelector(this);
        selectFleet(null);
        populateVPanels();
        add(freeShipsList, 0, 0);
        add(fleetSelector, 954, 0);

        UnitEventManager.addUnitCreatedHandler(new UnitCreatedHandler() {
            public void onUnitCreated(UnitCreatedEvent event) {
                selectFleet(null);

            }
        });
        UnitEventManager.addUnitDestroyedHandler(new UnitDestroyedHandler() {
            public void onUnitDestroyed(UnitDestroyedEvent event) {
                selectFleet(null);

            }
        });
    }


    public void populateVPanels() {
        freeShipsList.initShips();
        fleetSelector.initFleets();
    }


    public void reInitShips() {
        populateVPanels();
    }


    public void selectFleet(FleetDTO fleet) {
        if (formFleet != null && formFleet.isAttached()) {
            formFleet.removeFromParent();
        }
        pickupBrigDragController.unregisterDropControllers();
        DropController dp1 = new VerticalPanelDropController(freeShipsList.getFreeShipContainer());
        pickupBrigDragController.registerDropController(dp1);
        formFleet = new FromFleet(pickupBrigDragController, this, fleet);
        this.add(formFleet, 207, 0);
        reInitShips();

    }

    public PickupDragController getPickupDragController() {
        return pickupBrigDragController;
    }

    public void setPickupDragController(final PickupDragController pickupDragController) {
        this.pickupBrigDragController = pickupDragController;
    }

    public FromFleet getFormFleet() {
        return formFleet;
    }

    /**
     * @return the sector
     */
    public SectorDTO getSector() {
        return sector;
    }


    /**
     * @return the freeShipsList
     */
    public FreeShipSelector getFreeShipsList() {
        return freeShipsList;
    }


}
