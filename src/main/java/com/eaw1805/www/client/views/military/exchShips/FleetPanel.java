package com.eaw1805.www.client.views.military.exchShips;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipInfoMini;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.shared.stores.units.NavyStore;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;
import java.util.List;

public class FleetPanel extends AbsolutePanel implements ArmyConstants {
    private ClickAbsolutePanel shipsPanel;
    private AbsolutePanel pagesPanel;
    private HorizontalPanel pagNumPanel;
    private Label lblWarShips, lblMarines;
    private final AbsolutePanel shipPanel[] = new AbsolutePanel[12];
    private final DropController dropControllers[] = new DropController[12];
    private int page = 1;
    private PickupDragController dndCtrl;
    private Label lblCName;
    private FleetDTO nfleet;
    private Image imgMarines;
    private Label lblMerchShips;
    private ExchangeShipsView exchShipsView;
    private List<ShipDTO> ships = new ArrayList<ShipDTO>();
    private boolean warShips = true;
    private UnitChangedHandler unitChangedHandler;

    public FleetPanel(final PickupDragController dndCtrl,
                      final ExchangeShipsView exchShipsView,
                      final FleetDTO selFleet) {
        // Initialisation of needed vars
        this.nfleet = selFleet;
        if (nfleet != null)
            ships.addAll(nfleet.getShips().values());
        this.dndCtrl = dndCtrl;
        this.exchShipsView = exchShipsView;
        //*****************************//
        shipsPanel = new ClickAbsolutePanel();
        shipsPanel.setStyleName("infoPanelHolder");
        shipsPanel.setSize("540px", "396px");
        this.add(shipsPanel, 0, 99);

        this.pagesPanel = new AbsolutePanel();
        this.pagesPanel.setStyleName("pageBar");
        this.add(this.pagesPanel, 0, 501);
        this.pagesPanel.setSize("540px", "36px");

        this.pagNumPanel = new HorizontalPanel();
        this.pagesPanel.add(this.pagNumPanel, 5, 5);
        this.pagNumPanel.setHeight("25px");

        Image imgWarShips = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
        add(imgWarShips, 252, 0);
        imgWarShips.setSize("36px", "24px");

        Image imgMerchShips = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
        add(imgMerchShips, 321, 0);
        imgMerchShips.setSize("36px", "24px");

        lblWarShips = new Label("0");
        this.lblWarShips.setStyleName("clearFontMedium whiteText");
        add(lblWarShips, 252, 30);
        this.lblWarShips.setSize("36px", "15px");

        lblMarines = new Label("0");
        this.lblMarines.setStyleName("clearFontMedium whiteText");
        add(lblMarines, 177, 30);
        this.lblMarines.setSize("36px", "15px");

        this.imgMarines = new Image("http://static.eaw1805.com/images/armies/dominant/infantry.png");
        add(this.imgMarines, 177, 0);
        this.imgMarines.setSize("36px", "24px");

        this.lblMerchShips = new Label("0");
        this.lblMerchShips.setStyleName("clearFontMedium whiteText");
        add(this.lblMerchShips, 321, 30);
        this.lblMerchShips.setSize("36px", "15px");

        this.lblCName = new Label("Fleet name");
        this.lblCName.setStyleName("clearFontMedLarge whiteText");
        this.lblCName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.add(this.lblCName, 0, 70);
        this.lblCName.setSize("540px", "23px");

        if (nfleet != null) {
            lblCName.setText(nfleet.getName());
        } else {
            lblCName.setText("No fleet selected");
        }
        addDropShipPanel();
        setSize("541px", "547px");


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

        selectFleet(null);

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(UnitChangedEvent event) {
                if (event.getInfoType() == SHIP) {
                    boolean found = false;
                    for (ShipDTO ship : ships) {
                        if (ship.getId() == event.getInfoId()) {
                            removeShipById(ship.getId());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        ships.add((ShipDTO) NavyStore.getInstance().getShipById(event.getInfoId()));
                    }
                    initPages();
                    reCalculateLabelValues();
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

    public int initPages() {
        pagNumPanel.clear();

        int size = 0;
        if (warShips) {
            size = countWarShips(ships);
        } else {
            size = countMerchShips(ships);
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

    public void selectFleet(FleetDTO fleet) {
        nfleet = fleet;
        if (nfleet != null) {
            lblCName.setText(fleet.getName());
            shipsPanel.setId(fleet.getFleetId());
            ships.clear();
            ships.addAll(nfleet.getShips().values());
            initPages();
            addShipsToPage(1);
        }
        reCalculateLabelValues();
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

    private void addShipsToPage(int page) {
        for (int j = 0; j < 12; j++) {
            shipPanel[j].clear();
        }
        int from = (page - 1) * 12;
        int to = from + 11;
        int j = 0;
        List<ShipDTO> properShips = new ArrayList<ShipDTO>();
        properShips.addAll(getShipsByType(warShips));
        for (int i = from; i <= to; i++) {
            if (properShips.size() > i) {
                ShipDTO ship = properShips.get(i);
                final ShipInfoMini shipInfo = new ShipInfoMini(ship, true, false);
                shipInfo.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(DoubleClickEvent event) {
                        boolean success = false;
                        if (nfleet != null) {
                            int newFleetId = exchShipsView.getNewFleetIdByFleetId(nfleet.getFleetId());
                            success = NavyStore.getInstance().changeShipFleet(shipInfo.getShip().getId(), nfleet.getFleetId(), newFleetId, true, false);
                        }
                        if (success) {
                            removeShipById(shipInfo.getShip().getId());
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
        if (warShips && page == 2) {
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
        } else if (warShips && page == 3) {
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
        } else if (warShips && page == 4) {
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

    private List<ShipDTO> getShipsByType(boolean isWarShips) {
        List<ShipDTO> retShips = new ArrayList<ShipDTO>();
        for (ShipDTO ship : this.ships) {
            if (NavyStore.getInstance().isTradeShip(ship) && !isWarShips) {
                retShips.add(ship);
            } else if (!NavyStore.getInstance().isTradeShip(ship) && isWarShips) {
                retShips.add(ship);
            }
        }
        return retShips;
    }

    public boolean removeShipById(int id) {
        for (int i = 0; i < ships.size(); i++) {
            ShipDTO ship = ships.get(i);
            if (ship.getId() == id) {
                ships.remove(i);
                return true;
            }
        }
        return false;
    }

    public void setType(boolean isWarShips) {
        warShips = isWarShips;
        initPages();
        addShipsToPage(1);
    }
}
