package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.ProSiteLoadedEvent;
import com.eaw1805.www.client.events.loading.ProSiteLoadedHandler;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.views.infopanels.units.HandOverShipPanel;
import com.eaw1805.www.client.views.infopanels.units.mini.ShipRepairCostMini;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class ShipMenu extends UnitMenu implements ArmyConstants, RelationConstants,
        TradeInfoViewInterface, StyleConstants {

    private final MapStore mapStore = MapStore.getInstance();
    private final ImageButton moveImg;
    private final ImageButton tradeImg;
    private ImageButton repairImg;
    private final ImageButton unloadUnitsImg;
    private final ImageButton loadImg;
    private final ImageButton scuttleShips;
    private final ImageButton handOverImg;

    private final ShipDTO thisShip;
    private final UnitChangedHandler unitChangedHandler;

    public ShipMenu(final ShipDTO ship, final PopupPanelEAW caller) {
        setPopupParent(caller);
        thisShip = ship;
        final AbsolutePanel basePanel = new AbsolutePanel();
        basePanel.setSize("64px", "64px");

        if (NavyStore.getInstance().isTradeShip(ship)) {
            setSize("381px", "137px");
            add(basePanel, 147, 73);
        } else {
            setSize("320px", "137px");
            add(basePanel, 127, 73);
        }
        setStyleName("");


        final FigureItem shipImage = new FigureItem("http://static.eaw1805.com/images/figures/"
                + GameStore.getInstance().getNationId() + "/FleetMap00.png", 64, SHIP, ship.getId(), ship.getNationId(),
                RegionStore.getInstance().getSectorByPosition(ship).getId(), true, 0);
        basePanel.add(shipImage, 0, 0);

        final ShipMenu mySelf = this;

        moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                mapStore.getMapsView().goToPosition(ship);
                mapStore.getMapsView().addFigureOnMap(SHIP, ship.getId(),
                        ship, (int) (ship.getType().getMovementFactor() * ship.getCondition() / 100d), 0, 0, 0);
                caller.hide();
            }
        }).addToElement(moveImg.getElement()).register();

        moveImg.setTitle("Issue move orders.");
        moveImg.setStyleName(CLASS_POINTER);
        moveImg.setSize(SIZE_36PX, SIZE_36PX);

        if (NavyStore.getInstance().isTradeShip(ship)) {
            tradeImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButLoadOff.png");
            tradeImg.setTitle("Trade");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(
                            new TradePanelView(ship, mySelf, SHIP));
                    caller.hide();
                }
            }).addToElement(tradeImg.getElement()).register();

            tradeImg.setStyleName(CLASS_POINTER);
            tradeImg.setSize(SIZE_36PX, SIZE_36PX);
        } else {
            tradeImg = new ImageButton("");
        }
        final SectorDTO startingSector = RegionStore.getInstance().getRegionSectorsByRegionId(ship.getRegionId())[ship.getXStart()][ship.getYStart()];

        if (startingSector.hasShipyardOrBarracks() && startingSector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()
                && (RelationsStore.getInstance().isMine(ship.getNationId())
                || RelationsStore.getInstance().isAlly(ship.getNationId()))) {
            repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
            repairImg.setTitle("Repair ship.");
            repairImg.setStyleName(CLASS_POINTER);
            repairImg.setSize(SIZE_36PX, SIZE_36PX);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final SectorDTO sector = MapStore.getInstance().getRegionSectorByRegionIdXY(ship.getRegionId(), ship.getXStart(), ship.getYStart());
                    if (GameStore.getInstance().getNationId() == thisShip.getNationId()) {
                        NavyStore.getInstance().repairShip(ship.getId(), sector.getId());
                    } else {
                        AlliedUnitsStore.getInstance().repairShip(ship.getId(), sector.getId(), thisShip.getNationId());
                    }
                }
            }).addToElement(repairImg.getElement()).register();

            //add tooltip with information about the repair cost.
            new ToolTipPanel(repairImg) {
                @Override
                public void generateTip() {
                    setTooltip(new ShipRepairCostMini(ship));
                }
            };
        }

        unloadUnitsImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDisembarkOff.png");
        unloadUnitsImg.setTitle("Unload units.");
        unloadUnitsImg.setStyleName(CLASS_POINTER);

        unloadUnitsImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new UnloadTroopsView(SHIP, ship));
                caller.hide();
            }
        }).addToElement(unloadUnitsImg.getElement()).register();

        loadImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        loadImg.setTitle("Board troops");
        loadImg.setSize(SIZE_36PX, SIZE_36PX);
        loadImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final DeployTroopsView dpView = new DeployTroopsView(ship.getRegionId(), SHIP, ship.getId());
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                caller.hide();
            }
        }).addToElement(loadImg.getElement()).register();

        scuttleShips = new ImageButton("http://static.eaw1805.com/images/buttons/ButScuttleShipOff.png");
        scuttleShips.setTitle("Scuttle Ship");
        scuttleShips.setStyleName(CLASS_POINTER);
        scuttleShips.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                NavyStore.getInstance().addScuttleOrder(ship.getId(), ship.getRegionId());
                caller.hide();
                scuttleShips.deselect();
            }
        }).addToElement(scuttleShips.getElement()).register();

        handOverImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButHandOverShipOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final HandOverShipPanel hoShip = new HandOverShipPanel(ship);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(hoShip);
                GameStore.getInstance().getLayoutView().positionTocCenter(hoShip);

                caller.hide();
            }
        }).addToElement(handOverImg.getElement()).register();

        handOverImg.setTitle("Hand Over Ship.");
        handOverImg.setStyleName(CLASS_POINTER);
        handOverImg.setSize(SIZE_36PX, SIZE_36PX);

        setupImages();

        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                setupImages();
            }
        });
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == SHIP
                        && (event.getInfoId() == thisShip.getId() || event.getInfoId() == 0)) {
                    setupImages();
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

    public final void setupImages() {
        super.clearAllButtons();

        final SectorDTO[][] regionDTO = RegionStore.getInstance().getRegionSectorsByRegionId(thisShip.getRegionId());

        if (regionDTO.length < thisShip.getX() || regionDTO[thisShip.getX()].length < thisShip.getY()) {
            return;
        }

        final SectorDTO sector = regionDTO[thisShip.getX()][thisShip.getY()];

        if (sector == null) {
            return;
        }

        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }

        final BarrackDTO barrack = BarrackStore.getInstance().
                getBarrackByPosition(sector);


        final int cond = thisShip.getCondition();


        boolean canTrade = false;
        if ((barrack != null
                || NavyStore.getInstance().canTradeWithOtherShip(sector, thisShip.getId(), true))
                && NavyStore.getInstance().isTradeShip(thisShip)) {
            canTrade = true;
        }
        boolean hasUnits = false;
        if (thisShip.getLoadedUnitsMap() != null) {
            for (List<Integer> units : thisShip.getLoadedUnitsMap().values()) {
                if (units.size() > 0) {
                    hasUnits = true;
                    break;
                }
            }
        }

        if (GameStore.getInstance().getNationId() == thisShip.getNationId()) {
            addImageButton(moveImg);
        }
        if ((RelationsStore.getInstance().isMine(thisShip.getNationId()) ||
                RelationsStore.getInstance().isAlly(thisShip.getNationId()))
                && canTrade) {
            addImageButton(tradeImg);
        }

        if ((cond < 100 || thisShip.getNationId() != GameStore.getInstance().getNationId()) && repairImg != null) {

            addImageButton(repairImg);

        }

        if (hasUnits) {
            addImageButton(unloadUnitsImg);
        }

        if (RelationsStore.getInstance().isMine(thisShip.getNationId())) {

            addImageButton(loadImg);

            addImageButton(scuttleShips);

            if (handOverImg != null
                    && sector.hasShipyardOrBarracks()
                    && sector.getNationDTO().getNationId() != GameStore.getInstance().getNationId()
                    && RelationsStore.getInstance().getOriginalRelationByNationId(sector.getNationDTO().getNationId()) == REL_ALLIANCE) {

                addImageButton(handOverImg);
            }
        }

        super.finalizeMenu(190);
    }

    public void closeTradePanel() {

    }

}
