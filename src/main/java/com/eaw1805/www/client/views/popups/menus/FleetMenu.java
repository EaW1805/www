package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.views.infopanels.units.mini.FleetRepairCostMini;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

import java.util.List;

public class FleetMenu extends UnitMenu implements ArmyConstants, TradeInfoViewInterface, RelationConstants, StyleConstants {

    ImageButton moveImg, repairImg, loadImg, unloadUnitsImg, loadGoodsImg;

    public FleetMenu(final FleetDTO fleet, final PopupPanelEAW caller) {
        setPopupParent(caller);
        setSize("279px", "137px");
        setStyleName("");

        final AbsolutePanel basePanel = new AbsolutePanel();
        basePanel.setSize("64px", "64px");
        int power = PowerCalculators.calculateFleetPower(fleet);
        final String url = "http://static.eaw1805.com/images/figures/" + fleet.getNationId()
                + "/FleetMap00.png";

        final FigureItem fleetImage = new FigureItem(url, 64, FLEET, fleet.getFleetId(), NavyStore.getInstance().getNationIdFromFleet(fleet),
                RegionStore.getInstance().getSectorByPosition(fleet).getId(), true, power);
        basePanel.add(fleetImage, 0, 0);
        this.add(basePanel, 87, 73);

        //if empire is dead then don't add the action images.
        if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
            final DualStateImage handOverImg = new DualStateImage("http://static.eaw1805.com/images/buttons/handOver.png");

//            handOverImg.addClickHandler(new ClickHandler() {
//                public void onClick(final ClickEvent event) {
//                    // do nothing here yet
//                }
//            });
            handOverImg.setTitle("Chose a ship to hand over");
            handOverImg.setStyleName(CLASS_POINTER);

            handOverImg.setSize("25px", "25px");
            final MapStore mapStore = MapStore.getInstance();

            if (GameStore.getInstance().getNationId() != NavyStore.getInstance().getNationIdFromFleet(fleet)) {
                this.setWidth("115px");
            }


            if (GameStore.getInstance().getNationId() == NavyStore.getInstance().getNationIdFromFleet(fleet)) {
                moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        mapStore.getMapsView().goToPosition(fleet);
                        mapStore.getMapsView().addFigureOnMap(FLEET, fleet.getFleetId(),
                                fleet, MiscCalculators.getFleetMps(fleet), PowerCalculators.getPowerByWarShips(fleet), 0, 0);
                        caller.hide();
                    }
                }).addToElement(moveImg.getElement()).register();
                moveImg.setTitle("Issue move orders.");
                moveImg.setStyleName(CLASS_POINTER);
                addImageButton(moveImg);
                moveImg.setSize(SIZE_36PX, SIZE_36PX);
            }

            final SectorDTO startingSector = RegionStore.getInstance().getRegionSectorsByRegionId(fleet.getRegionId())[fleet.getXStart()][fleet.getYStart()];
            if (startingSector.hasShipyardOrBarracks() && startingSector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()
                    && (RelationsStore.getInstance().isMine(fleet.getNationId())
                    || RelationsStore.getInstance().isAlly(fleet.getNationId()))) {
                repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
                repairImg.setTitle("Repair fleet.");
                repairImg.setStyleName(CLASS_POINTER);
                for (ShipDTO ship : fleet.getShips().values()) {
                    if (ship.getCondition() < 100) {
                        addImageButton(repairImg);
                        break;
                    }
                }

                repairImg.setSize(SIZE_36PX, SIZE_36PX);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        final SectorDTO sector = MapStore.getInstance().getRegionSectorByRegionIdXY(fleet.getRegionId(), fleet.getX(), fleet.getY());
                        if (GameStore.getInstance().getNationId() == NavyStore.getInstance().getNationIdFromFleet(fleet)) {
                            NavyStore.getInstance().repairFleet(fleet.getFleetId(), sector.getId());
                        } else {
                            AlliedUnitsStore.getInstance().repairFleet(fleet.getFleetId(), sector.getId(), NavyStore.getInstance().getNationIdFromFleet(fleet));
                        }
                    }
                }).addToElement(repairImg.getElement()).register();

                new ToolTipPanel(repairImg) {
                    @Override
                    public void generateTip() {
                        setTooltip(new FleetRepairCostMini(fleet));
                    }
                };
            }

            if ((RelationsStore.getInstance().isMine(fleet.getNationId())
                    || RelationsStore.getInstance().isAlly(fleet.getNationId()))) {
                if (MiscCalculators.getFleetInfo(fleet).getMerchantShips() > 0) {
                    loadGoodsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            ShipDTO firstTrade = null;
                            for (ShipDTO ship : fleet.getShips().values()) {
                                if (NavyStore.getInstance().isTradeShip(ship)) {
                                    firstTrade = ship;
                                    break;
                                }
                            }
                            GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(
                                    new TradePanelView(firstTrade, FleetMenu.this, SHIP));
                            caller.hide();
                        }
                    }).addToElement(loadGoodsImg.getElement()).register();

                    loadGoodsImg.setTitle("Show trade options.");
                    loadGoodsImg.setStyleName(CLASS_POINTER);
                    addImageButton(loadGoodsImg);
                    loadGoodsImg.setSize(SIZE_36PX, SIZE_36PX);
                } else {
                    setWidth("238px");
                }
            }


            unloadUnitsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDisembarkOff.png");
            unloadUnitsImg.setTitle("Unload troops.");
            unloadUnitsImg.setStyleName(CLASS_POINTER);

            if (fleet.getLoadedUnitsMap() != null) {
                for (List<Integer> units : fleet.getLoadedUnitsMap().values()) {
                    if (units.size() > 0) {
                        addImageButton(unloadUnitsImg);
                        break;
                    }
                }
            }
            unloadUnitsImg.setSize(SIZE_36PX, SIZE_36PX);

            unloadUnitsImg.addClickHandler(new ClickHandler() {
                public void onClick(final ClickEvent event) {
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new UnloadTroopsView(FLEET, fleet));
                    caller.hide();
                }
            });

            if ((RelationsStore.getInstance().isMine(fleet.getNationId())
                    || RelationsStore.getInstance().isAlly(fleet.getNationId()))) {
                loadImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
                loadImg.setTitle("Board troops");
                loadImg.setSize(SIZE_36PX, SIZE_36PX);
                loadImg.setStyleName(CLASS_POINTER);
                addImageButton(loadImg);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        final DeployTroopsView dpView = new DeployTroopsView(fleet.getRegionId(), FLEET, fleet.getFleetId());
                        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                        GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                        caller.hide();
                    }
                }).addToElement(loadImg.getElement()).register();
            }
            super.finalizeMenu(157);
        }
    }

    public void closeTradePanel() {
        //do nothing here
    }

}
