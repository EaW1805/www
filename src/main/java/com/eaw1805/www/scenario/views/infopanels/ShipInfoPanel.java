package com.eaw1805.www.scenario.views.infopanels;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.data.dto.web.fleet.ShipTypeDTO;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.FleetUtils;
import com.eaw1805.www.scenario.stores.ShipUtils;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.scenario.widgets.MenuPopup;
import com.eaw1805.www.scenario.widgets.SelectEAW;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;

import java.util.Map;

public class ShipInfoPanel
        extends AbsolutePanel
        implements ArmyConstants,
        RelationConstants, StyleConstants {

    private final static String TONS = " tons";

    boolean update = false;

    public ShipInfoPanel(final ShipDTO ship) {
        setSize("366px", "90px");

        setStyleName("ShipInfoPanel");
        setStyleName("clickArmyPanel", true);
        setSize("363px", "87px");

        reInit(ship);
    }

    public void reInit(final ShipDTO ship) {
        clear();
        update = false;
        final Image shipTypeImg = new Image("http://static.eaw1805.com/images/ships/" + ship.getNationId() + "/" + ship.getType().getIntId() + ".png");
        shipTypeImg.setTitle(ship.getType().getName());
        add(shipTypeImg, 3, 3);
        shipTypeImg.setSize("", "82px");
        //main
        final TextBoxEditable lblName = new TextBoxEditable("Ships Name");
        lblName.setText(ship.getName());
        lblName.initHandler(new BasicHandler() {
            @Override
            public void run() {
                ship.setName(lblName.getText());
            }
        });
        lblName.setStyleName("clearFontMiniTitle");
        lblName.setSize("180px", "22px");
        add(lblName, 90, 3);

        //right end
        final Label lblXy = new Label(ship.positionToString());
        lblXy.setTitle("Ships position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        add(lblXy, 315, 3);
        lblXy.setSize("49px", "18px");

        final Label lblMp = new Label((int) (ship.getType().getMovementFactor() * ship.getCondition() / 100d) + " MPs");
        lblMp.setStyleName(CLASS_CLEARFONTSMALL);
        lblMp.setTitle("Movement points.");
        add(lblMp, 315, 20);
        lblMp.setSize("100px", "18px");
        //main
        Label lblCondition = new Label(ship.getCondition() + "%");
        NumberFormat numberFormat = NumberFormat.getDecimalFormat();
        Label lblMarines = new Label(numberFormat.format(ship.getMarines()) + " " + getMarinesExpStr(ship) + " marines");
        final SelectEAW<ShipTypeDTO> typeSelect = new SelectEAW<ShipTypeDTO>("Change ship type - you need to open popup again in order to update content") {
            @Override
            public void onChange() {
                if (update) {
                    ship.setType(getValue());
                    reInit(ship);
                }
            }
        };

        for (ShipTypeDTO type : EditorStore.getInstance().getShipTypes()) {
            typeSelect.addOption(new OptionEAW(130,15, type.getName()), type);
        }
        typeSelect.selectOptionByValue(ship.getType());

        if (ship.getType().getShipClass() == 0) {

            lblCondition.setTitle("Condition");
            lblCondition.setStyleName(CLASS_CLEARFONTSMALL);
            add(lblCondition, 90, 34);

            add(typeSelect, 90, 65);

        } else {

            add(typeSelect, 90, 20);

            lblMarines.setStyleName(CLASS_CLEARFONTSMALL);
            add(lblMarines, 90, 34);
            lblMarines.setSize("170px", "18px");

            lblCondition.setTitle("Condition");
            lblCondition.setStyleName(CLASS_CLEARFONTSMALL);
            add(lblCondition, 90, 48);

            final Label lblCapacity = new Label(numberFormat.format(ship.getType().getLoadCapacity()) + TONS);
            lblCapacity.setStyleName(CLASS_CLEARFONTSMALL);
            add(lblCapacity, 90, 62);
        }
        final Image nationImg = new Image("http://static.eaw1805.com/images/nations/nation-" + ship.getNationId() + "-120.png");
        nationImg.setHeight("30px");
        add(nationImg, 300, 40);


        final MenuPopup popup = createNationMenu(ship, nationImg);
        nationImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                popup.showMenu();
            }
        });

        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete spy");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                ShipUtils.deleteShip(ship);
                //then remove it from map
                EditorMapUtils.getInstance().drawShips(ship.getRegionId());
                //then remove it from parent
                removeFromParent();
                //finally update the army overview
                EditorPanel.getInstance().getShipOverView().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");
        add(deleteImg, 270, 60);
        update = true;

        final Image corpsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/fleet.png");
        corpsImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getMenu(ship.getNationId(), ship).showRelativeTo(corpsImg);
            }
        });

        add(corpsImg, 296, 60);

        if (ship.getFleet() != 0) {
            FleetDTO fleet = EditorStore.getInstance().getFleets().get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).get(ship.getFleet());
            Label corpLbl = new Label(":" + fleet.getName() + ":");
            add(corpLbl, 290, 45);
        }

    }


    PopupPanel getMenu(int nationId, final ShipDTO ship) {
        final PopupPanel menu = new PopupPanel();
        menu.setAutoHideEnabled(true);
        final VerticalPanel container = new VerticalPanel();

        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, FleetDTO>>>> corpsMap = EditorStore.getInstance().getFleets();


        if (corpsMap.get(ship.getRegionId()).containsKey(ship.getX())
                && corpsMap.get(ship.getRegionId()).get(ship.getX()).containsKey(ship.getY())) {
            for (final FleetDTO fleet : corpsMap.get(ship.getRegionId()).get(ship.getX()).get(ship.getY()).values()) {
                if (fleet.getNationId() == nationId) {
                    final Label corpsName = new Label(fleet.getName());
                    corpsName.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            FleetUtils.addShipToFleet(ship, fleet);
                            menu.hide();
                            reInit(ship);
                        }
                    });
                    container.add(corpsName);
                }
            }
        }
        final HorizontalPanel newCorps = new HorizontalPanel();
        final TextBox newCorpsName = new TextBox();
        newCorps.add(newCorpsName);
        final Button createCorps = new Button("Create And Add");
        newCorps.add(createCorps);
        createCorps.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (newCorpsName.getText().trim().isEmpty()) {
                    Window.alert("Name cannot be empty");
                    return;
                }
                FleetDTO fleet = FleetUtils.createFleet(newCorpsName.getText().trim(), ship.getRegionId(), ship.getX(), ship.getY(), ship.getNationId());
                FleetUtils.addShipToFleet(ship, fleet);
                menu.hide();
                reInit(ship);
            }
        });
        container.add(newCorps);
        if (ship.getFleet() != 0) {
            final Label free = new Label("Remove From Fleets");
            free.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    //remove brigade from corps
                    FleetUtils.removeShipFromFleet(ship);
                    menu.hide();
                    reInit(ship);
                }
            });
            container.add(free);
        }

        menu.setWidget(container);
        return menu;
    }

    public MenuPopup createNationMenu(final ShipDTO ship, final Image nationImg) {
        MenuPopup popup = new MenuPopup(nationImg) {
            @Override
            public void initChildren() {
                for (final NationDTO nation : EditorStore.getInstance().getNations()) {
                    if (nation.getNationId() > 0) {
                        addChild(nation.getName(), new BasicHandler() {
                            @Override
                            public void run() {
                                ship.setNationId(nation.getNationId());
                                reInit(ship);
                            }
                        });
                    }
                }
            }
        };
        popup.initChildren();

        return popup;
    }

    public final String getMarinesExpStr(ShipDTO ship) {

        switch (ship.getExp()) {
            case 1:
                return "Veteran";

            case 2:
                return "Elite";

            case 0:
            default:
                return "Regular";
        }
    }


}
