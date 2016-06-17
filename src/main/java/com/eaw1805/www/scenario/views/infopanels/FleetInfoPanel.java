package com.eaw1805.www.scenario.views.infopanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;

import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.fieldbattle.stores.calculators.NavyUnitInfoDTO;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.FleetUtils;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;
import com.eaw1805.www.scenario.widgets.popups.UnitViewerPopup;

import java.util.ArrayList;
import java.util.List;

public class FleetInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, RelationConstants, StyleConstants {

    private FleetDTO fleet;
    private final Label lblBattShips;
    private final Label lblMerchShips;
    private final Label lblFleets;
    private NavyUnitInfoDTO fleetInfo;
    private final Label lblMps;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();

    private final Label lblAvailable;
    private final AbsolutePanel fleetInfoPanel;
    private final Label lblMarines;

    public FleetInfoPanel(final FleetDTO thisFleet) {
        fleet = thisFleet;

        final int leftFactor = 87;
        setStyleName("");
        setSize("366px", "90px");

        fleetInfoPanel = new AbsolutePanel();
        fleetInfoPanel.setStyleName("fleetInfoPanel");
        fleetInfoPanel.setSize("363px", "87px");
        add(fleetInfoPanel);

        final TextBoxEditable lblFleetName = new TextBoxEditable("Fleet Name");
        lblFleetName.setText(thisFleet.getName());
        lblFleetName.initHandler(new BasicHandler() {
            @Override
            public void run() {
                thisFleet.setName(lblFleetName.getText());
            }
        });


        fleetInfoPanel.add(lblFleetName, 90 - leftFactor, 3);

        int totalCapacity = 0;
        int totalMarines = 0;
        for (final ShipDTO ship : fleet.getShips().values()) {
            totalCapacity += ship.getType().getLoadCapacity();
            totalMarines += ship.getMarines();
        }

        lblAvailable = new Label(numberFormat.format(totalCapacity) + " tons");
        lblAvailable.setStyleName(CLASS_CLEARFONTSMALL);
        lblAvailable.setSize("235px", "25px");
        fleetInfoPanel.add(lblAvailable, 90 - leftFactor, 20);


        lblMarines = new Label(numberFormat.format(totalMarines) + " marines");
        lblMarines.setStyleName(CLASS_CLEARFONTSMALL);
        if (fleet.getFleetId() != 0) {
            fleetInfoPanel.add(lblMarines, 90 - leftFactor, 34);
        } else {
            fleetInfoPanel.add(lblMarines, 90 - leftFactor, 17);
        }

        fleetInfo = MiscCalculators.getFleetInfo(fleet);

        lblFleets = new Label("");
        lblFleets.setStyleName(CLASS_CLEARFONTSMALL);




        final Image navyImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        navyImg.setSize("", SIZE_15PX);
        fleetInfoPanel.add(navyImg, 90 - leftFactor, 52);
        navyImg.setTitle("War ships");
        navyImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                List<ShipDTO> warships = new ArrayList<ShipDTO>();
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getType().getShipClass() != 0) {
                        warships.add(ship);
                    }
                }
                new UnitViewerPopup<ShipDTO>(warships) {

                    @Override
                    public Widget getUnitWidget(ShipDTO unit) {
                        return new ShipInfoPanel(unit);
                    }
                }.showRelativeTo(navyImg);
            }
        });


        lblBattShips = new Label(numberFormat.format(fleetInfo.getWarShips()));
        lblBattShips.setStyleName(CLASS_CLEARFONTSMALL);
        fleetInfoPanel.add(lblBattShips, 118 - leftFactor, 52);

        final Image navyImg2 = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        navyImg2.setSize("", SIZE_15PX);
        navyImg2.setTitle("Merchant ships");
        fleetInfoPanel.add(navyImg2, 90 - leftFactor, 68);
        navyImg2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                List<ShipDTO> warships = new ArrayList<ShipDTO>();
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getType().getShipClass() == 0) {
                        warships.add(ship);
                    }
                }
                new UnitViewerPopup<ShipDTO>(warships) {

                    @Override
                    public Widget getUnitWidget(ShipDTO unit) {
                        return new ShipInfoPanel(unit);
                    }
                }.showRelativeTo(navyImg2);
            }
        });

        lblMerchShips = new Label(numberFormat.format(fleetInfo.getMerchantShips()));
        lblMerchShips.setStyleName(CLASS_CLEARFONTSMALL);
        fleetInfoPanel.add(lblMerchShips, 118 - leftFactor, 68);

        final Label lblXy = new Label("");
        if (fleet.getFleetId() != 0) {
            lblXy.setText(fleet.positionToString());
        }
        lblXy.setTitle("Fleets position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        lblXy.setSize("38px", "18px");
        fleetInfoPanel.add(lblXy, 315, 3);

        lblMps = new Label("");
        if (fleet.getFleetId() != 0) {
            lblMps.setText(MiscCalculators.getFleetInfo(fleet).getMps() + " MPs");
        }
        lblMps.setTitle("Movement points.");
        lblMps.setStyleName(CLASS_CLEARFONTSMALL);
        lblMps.setSize("50px", SIZE_15PX);
        fleetInfoPanel.add(lblMps, 315, 20);

        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete brigade");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                FleetUtils.deleteFleet(thisFleet);
                //then remove it from map
                EditorMapUtils.getInstance().drawShips(thisFleet.getRegionId());
                //then remove it from parent
                removeFromParent();
                //finally update the army overview
                EditorPanel.getInstance().getShipOverView().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");
        add(deleteImg, 270, 60);

        final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + thisFleet.getNationId() + "-120.png");
        nationImg.setHeight("30px");
        add(nationImg, 300, 40);

        setUpLabels();


    }


    private void setUpLabels() {
        int totalCapacity = 0;
        int totalMarines = 0;
        for (final ShipDTO ship : fleet.getShips().values()) {
            totalCapacity += ship.getType().getLoadCapacity();
            totalMarines += ship.getMarines();
        }

        lblAvailable.setText(numberFormat.format(totalCapacity) + " tons");
        lblMarines.setText(numberFormat.format(totalMarines) + " marines");


        fleetInfo = MiscCalculators.getFleetInfo(fleet);


        lblBattShips.setText(fleetInfo.getWarShips() + " War Ships");
        lblMerchShips.setText(fleetInfo.getMerchantShips() + " Merchant Ships");
        if (fleet.getFleetId() != 0) {
            lblMps.setText(fleetInfo.getMps() + " MPs");
        }
    }


}
