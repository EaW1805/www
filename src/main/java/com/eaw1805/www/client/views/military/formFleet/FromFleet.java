package com.eaw1805.www.client.views.military.formFleet;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipInfoMini;
import com.eaw1805.www.client.views.popups.DisbandFederationPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;
import java.util.List;

public class FromFleet extends AbsolutePanel implements OrderConstants, ArmyConstants {
    public enum Type {
        WAR, MERCHANT, ALL
    }

    private AbsolutePanel shipsPanel, pagesPanel;
    private HorizontalPanel pagNumPanel;
    private Label lblWarShips, lblMarines;
    private final AbsolutePanel shipPanel[] = new AbsolutePanel[12];
    private final DropController dropControllers[] = new DropController[12];
    private int page = 1;
    private PickupDragController dndCtrl;
    private ImageButton lblX, lblSave;
    private RenamingLabel lblCName;
    private FleetDTO nfleet;
    private boolean isNew;
    private Image imgMarines;
    private Label lblMerchShips;
    private FormFleetView formFleetView;
    private List<ShipDTO> ships = new ArrayList<ShipDTO>();
    private Type type = Type.ALL;
    private final ShipTypesList typeList = new ShipTypesList(this);
    private UnitChangedHandler unitChangedHandler;

    public FromFleet(final PickupDragController dndCtrl,
                     final FormFleetView formFleetView,
                     final FleetDTO selFleet
    ) {
        // Initialisation of needed vars
        this.nfleet = selFleet;
        if (nfleet != null)
            ships.addAll(nfleet.getShips().values());
        this.dndCtrl = dndCtrl;
        this.formFleetView = formFleetView;
        //*****************************//
        shipsPanel = new AbsolutePanel();
        shipsPanel.setStyleName("infoPanelHolder");
        shipsPanel.setSize("540px", "396px");
        this.add(shipsPanel, 90, 99);

        this.pagesPanel = new AbsolutePanel();
        this.pagesPanel.setStyleName("pageBar");
        this.add(this.pagesPanel, 90, 501);
        this.pagesPanel.setSize("540px", "36px");

        this.pagNumPanel = new HorizontalPanel();
        this.pagesPanel.add(this.pagNumPanel, 5, 5);
        this.pagNumPanel.setHeight("25px");

        Image imgWarShips = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        imgWarShips.setTitle("War Ships");
        add(imgWarShips, 342, 0);
        imgWarShips.setSize("36px", "24px");

        Image imgMerchShips = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        imgMerchShips.setTitle("Merchant Ships");
        add(imgMerchShips, 411, 0);
        imgMerchShips.setSize("36px", "24px");

        lblWarShips = new Label("0");
        this.lblWarShips.setStyleName("clearFontMedium whiteText");
        add(lblWarShips, 342, 30);
        this.lblWarShips.setSize("36px", "15px");

        lblMarines = new Label("0");
        this.lblMarines.setStyleName("clearFontMedium whiteText");
        add(lblMarines, 267, 30);
        this.lblMarines.setSize("36px", "15px");

        this.imgMarines = new Image("http://static.eaw1805.com/images/armies/dominant/infantry.png");
        add(this.imgMarines, 267, 0);
        this.imgMarines.setSize("36px", "24px");

        this.lblMerchShips = new Label("0");
        this.lblMerchShips.setStyleName("clearFontMedium whiteText");
        add(this.lblMerchShips, 411, 30);
        this.lblMerchShips.setSize("36px", "15px");

        this.lblCName = new RenamingLabel("Fleet name", 100000, 0);
        this.lblCName.setStyleName("clearFontMedLarge whiteText");
        this.lblCName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.add(this.lblCName, 90, 70);
        this.lblCName.setSize("540px", "23px");

        this.lblSave = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        this.lblSave.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (isNew) {
                    saveFleet();
                    lblSave.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "This fleet is not new.Adding and removing ships" +
                            "automatically changes their status", false);
                }
                lblSave.deselect();
            }
        }).addToElement(lblSave.getElement()).register();


        this.add(this.lblSave, 664, 0);
        this.lblSave.setSize("24px", "24px");

        this.lblX = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                boolean isEmpty = true;
                for (int i = 0; i < 10; i++) {
                    if (shipPanel[0].getWidgetCount() > 0) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    if (nfleet != null) {
                        dndCtrl.unregisterDropControllers();
                        NavyStore.getInstance().deleteFleet(nfleet.getFleetId());
                    }
                } else {
                    if (nfleet != null) {
                        DisbandFederationPopup disFleet = new DisbandFederationPopup(FLEET, nfleet.getFleetId());
                        disFleet.center();
                    }
                }
                lblX.deselect();
            }
        }).addToElement(lblX.getElement()).register();

        this.add(this.lblX, 692, 0);
        this.lblX.setSize("24px", "24px");
        this.lblX.setStyleName("pointer");
        if (nfleet != null) {
            lblCName.setText(nfleet.getName());
            isNew = false;
            lblSave.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
        } else {
            lblCName.setText("Fleet " + NavyStore.getInstance().getIdFleetMap().values().size());

            isNew = true;
            lblSave.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        }
        addDropShipPanel();
        setSize("720px", "547px");


        this.add(typeList, 90, 0);

    }

    public void changeShipType(Type selected) {
        this.type = selected;
        changeShipsType();
        formFleetView.getFreeShipsList().initShips();
    }

    private void addDropShipPanel() {
        for (int i = 0; i < 12; i++) {
            this.shipPanel[i] = new AbsolutePanel();
            this.shipPanel[i].setSize("170px", "89px");
            if (i < 1) {
                this.shipsPanel.add(this.shipPanel[i], 5, 7);
            } else if (i < 3) {
                this.shipsPanel.add(this.shipPanel[i], 5 + i * 180, 7);
            } else if (i < 6) {
                this.shipsPanel.add(this.shipPanel[i], 5 + (i - 3) * 180, 106);
            } else if (i < 9) {
                this.shipsPanel.add(this.shipPanel[i], 5 + (i - 6) * 180, 205);
            } else if (i < 12) {
                this.shipsPanel.add(this.shipPanel[i], 5 + (i - 9) * 180, 304);
            }
            dropControllers[i] = new AbsolutePositionDropController(shipPanel[i]);
            dndCtrl.registerDropController(dropControllers[i]);
        }

        selectFleet();
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == SHIP) {
                    initPages();
                }

            }
        };


    }

    public void onAttach() {
        super.onAttach();
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public boolean addShipToFleet(ShipDTO ship) {
        if (countWarShips(ships) < 40) {
            ships.add(ship);
            if (nfleet != null) {
                NavyStore.getInstance().changeShipFleet(ship.getId(), 0, nfleet.getFleetId(), true, false);
            }
            initPages();
            addShipsToPage(page);
            reCalculateLabelValues();
            return true;
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Can not add more than 40 warships to a Fleet", false);
            return false;
        }

    }

    private void changeShipsType() {
        initPages();
        addShipsToPage(1);
    }

    public void selectFleet() {

        if (nfleet != null) {
            initPages();
            addShipsToPage(1);
        }
        reCalculateLabelValues();
    }

    private void addShipsToPage(int page) {
        for (int j = 0; j < 12; j++) {
            shipPanel[j].clear();
        }
        int from = (page - 1) * 12;
        int to = from + 11;
        int j = 0;
        List<ShipDTO> properShips = new ArrayList<ShipDTO>();
        properShips.addAll(getShipsByType(type));
        for (int i = from; i <= to; i++) {
            if (properShips.size() > i) {
                ShipDTO ship = properShips.get(i);
                final ShipInfoMini shipInfo = new ShipInfoMini(ship, true, false);
                shipInfo.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(DoubleClickEvent event) {
                        boolean success = false;
                        if (nfleet != null) {
                            if (nfleet.hasLoadedUnits()) {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot remove ship from loaded fleet", false);
                                return;
                            }
                            success = NavyStore.getInstance().changeShipFleet(shipInfo.getShip().getId(), nfleet.getFleetId(), 0, true, false);
                        }
                        if (success) {
                            removeShipById(shipInfo.getShip().getId());
                            formFleetView.reInitShips();
                            initPages();
                            setPage(1);
                            shipInfo.removeFromParent();
                        }
                    }
                }, DoubleClickEvent.getType());
                shipPanel[j].add(shipInfo);
                dndCtrl.makeDraggable(shipInfo,
                        shipInfo.getShipPanel());
                j++;
            }
        }
        int cnt = shipsPanel.getWidgetCount();
        for (int l = cnt - 1; l > 0; l--) {
            if (!shipsPanel.getWidget(l).getClass().equals(AbsolutePanel.class)) {
                shipsPanel.getWidget(l).removeFromParent();
            }
        }
        if (type == Type.WAR && page == 2) {
            for (int i = 8; i < 12; i++) {
                Label lbl = new Label("More than 20 warships");
                lbl.setStyleName("clearFontSmall");
                lbl.setWidth("180px");
                if (i < 9) {
                    this.shipsPanel.add(lbl, 5 + (i - 6) * 180, 205 + 40);
                } else if (i < 12) {
                    this.shipsPanel.add(lbl, 5 + (i - 9) * 180, 304 + 40);
                }
            }
        } else if (type == Type.WAR && page == 3) {
            for (int i = 0; i < 12; i++) {
                Label lbl = new Label("More than 20 warships");
                lbl.setStyleName("clearFontSmall");
                lbl.setWidth("180px");
                if (i < 1) {
                    this.shipsPanel.add(lbl, 5, 7 + 40);
                } else if (i < 3) {
                    this.shipsPanel.add(lbl, 5 + i * 180, 7 + 40);
                } else if (i < 6) {
                    this.shipsPanel.add(lbl, 5 + (i - 3) * 180, 106 + 40);
                } else if (i < 9) {
                    this.shipsPanel.add(lbl, 5 + (i - 6) * 180, 205 + 40);
                } else if (i < 12) {
                    this.shipsPanel.add(lbl, 5 + (i - 9) * 180, 304 + 40);
                }
            }
        } else if (type == Type.WAR && page == 4) {
            for (int i = 0; i < 12; i++) {
                Image img = new Image("http://static.eaw1805.com/images/buttons/tint.png");
                img.setSize("170px", "89px");
                if (i < 1) {
                    this.shipsPanel.add(img, 5, 7);
                } else if (i < 3) {
                    this.shipsPanel.add(img, 5 + i * 180, 7);
                } else if (i < 6) {
                    this.shipsPanel.add(img, 5 + (i - 3) * 180, 106);
                } else if (i < 9) {
                    this.shipsPanel.add(img, 5 + (i - 6) * 180, 205);
                } else if (i < 12) {
                    this.shipsPanel.add(img, 5 + (i - 9) * 180, 304);
                }
                dropControllers[i] = new AbsolutePositionDropController(shipPanel[i]);
                dndCtrl.registerDropController(dropControllers[i]);
            }
        }
    }


    private List<ShipDTO> getShipsByType(Type type) {
        List<ShipDTO> retShips = new ArrayList<ShipDTO>();
        for (ShipDTO ship : this.ships) {
            if (!NavyStore.getInstance().isTradeShip(ship) && (type == Type.WAR || type == Type.ALL)) {
                retShips.add(ship);
            } else if (NavyStore.getInstance().isTradeShip(ship) && (type == Type.MERCHANT || type == Type.ALL)) {
                retShips.add(ship);
            }
        }
        return retShips;
    }

    public int initPages() {
        pagNumPanel.clear();

        int size = 0;
        if (type == Type.WAR || type == Type.ALL) {
            size += countWarShips(ships);
        }
        if (type == Type.MERCHANT || type == Type.ALL) {
            size += countMerchShips(ships);
        }
        int pages = 1;
        while ((size - 12) >= 0) {
            pages++;
            size -= 12;
        }
        for (int i = 0; i < pages; i++) {
            final int pageNo = i + 1;
            final DrawingArea canvas = new DrawingArea(30, 30);
            Text pageNum = new Text(8, 22, String.valueOf(i + 1));
            pageNum.setStyleName("pointer");
            final Circle circle = new Circle(15, 15, 14);
            pageNum.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    setPage(pageNo);
                    for (int j = 0; j < canvas.getVectorObjectCount(); j++) {
                        if (canvas.getVectorObject(j).getClass().equals(Circle.class)) {
                            ((Circle) canvas.getVectorObject(j)).setFillOpacity(0.0);
                        }
                    }
                    circle.setFillOpacity(0.2);
                }

            });
            circle.setFillColor("black");

            if (page == i) {
                circle.setFillOpacity(0.2);
            } else {
                circle.setFillOpacity(0.0);
            }
            canvas.add(circle);
            canvas.add(pageNum);
            pagNumPanel.add(canvas);
        }
        setPage(this.page);
        return pages;

    }

    private int countWarShips(List<ShipDTO> ships) {
        int warShips = 0;
        for (ShipDTO ship : ships) {
            if (ship.getTypeId() != 31 && ship.getType().getShipClass() != 0) {
                warShips++;
            }
        }
        return warShips;
    }


    private int countMerchShips(List<ShipDTO> ships) {
        int merchShips = 0;
        for (ShipDTO ship : ships) {
            if (ship.getTypeId() == 31 || ship.getType().getShipClass() == 0) {
                merchShips++;
            }
        }
        return merchShips;
    }


    public void setPage(int i) {
        page = i;
        addShipsToPage(page);

    }

    public void reCalculateLabelValues() {
        int warShips = 0, merchShips = 0, marines = 0;
        for (ShipDTO ship : ships) {
            marines = marines + ship.getMarines();
            if (ship.getType().getIntId() < 13
                    || ship.getType().getIntId() == 19) {
                warShips++;
            } else {
                merchShips++;
            }
        }
        lblMarines.setText(String.valueOf(marines));
        lblWarShips.setText(String.valueOf(warShips));
        lblMerchShips.setText(String.valueOf(merchShips));
    }


    private void saveFleet() {
        if (!lblCName.getText().trim().equals("") && !lblCName.getText().isEmpty()) {
            if (!ships.isEmpty() && ships.size() > 0) {
                int fleetId = 0;
                for (ShipDTO ship : ships) {
                    fleetId += ship.getId();
                }
                if (isNew) {
                    String name = lblCName.getText();
                    int[] ids = new int[2];
                    ids[0] = fleetId;
                    ids[1] = 0;
                    if (OrderStore.getInstance().addNewOrder(ORDER_B_FLT, CostCalculators.getFormFederationCost(ORDER_B_FLT), ships.get(0).getRegionId(), name, ids, 0, "") == 1) {
                        nfleet = NavyStore.getInstance().createFleet(fleetId, 0, name, ships.get(0).getX(), ships.get(0).getY(), ships.get(0).getRegionId(), ships.get(0).getNationId());
                        GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_B_FLT, ids);
                        isNew = false;
                    }
                }
                if (!isNew) {
                    for (ShipDTO brig : ships) {
                        NavyStore.getInstance().changeShipFleet(brig.getId(), 0, nfleet.getFleetId(), false, false);
                    }
                    UnitEventManager.createUnit(FLEET, fleetId);
                }

            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "This fleet has no ships.Add at least one before saving", false);
            }
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot save fleet. Enter a name first", false);
        }

    }

    public int getFleetId() {
        if (nfleet == null) {
            return -1;
        } else {
            return nfleet.getFleetId();
        }
    }

    public List<ShipDTO> getShips() {
        return ships;
    }

    public boolean hasShip(ShipDTO tgShip) {
        for (ShipDTO ship : ships) {
            if (ship.getId() == tgShip.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean removeShipById(int id) {
        for (int i = 0; i < ships.size(); i++) {
            ShipDTO ship = ships.get(i);
            if (ship.getId() == id) {
                ships.remove(i);
                reCalculateLabelValues();
                return true;
            }
        }
        return false;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }
}
