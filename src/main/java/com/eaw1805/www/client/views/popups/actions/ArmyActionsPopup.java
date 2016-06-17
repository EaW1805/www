package com.eaw1805.www.client.views.popups.actions;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.popups.menus.ArmyMenu;
import com.eaw1805.www.client.views.popups.menus.BrigadeMenu;
import com.eaw1805.www.client.views.popups.menus.CorpsMenu;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.ForeignUnitsStore;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArmyActionsPopup
        extends PopupPanelEAW
        implements ArmyConstants {

    private SectorDTO sector;
    private MapStore mapStore = MapStore.getInstance();

    public ArmyActionsPopup(final SectorDTO selSector) {
        setAutoHideEnabled(true);
        final ArmyActionsPopup mySelf = this;
        sector = selSector;

        final List<ArmyDTO> armies = new ArrayList<ArmyDTO>();
        if (ArmyStore.getInstance().getArmiesBySector(sector, true) != null) {
            armies.addAll(ArmyStore.getInstance().getArmiesBySector(sector, true));
        }

        if (AlliedUnitsStore.getInstance().getAlliedArmiesBySector(sector) != null) {
            armies.addAll(AlliedUnitsStore.getInstance().getAlliedArmiesBySector(sector));
        }

        if (ForeignUnitsStore.getInstance().getArmiesBySector(sector) != null) {
            armies.addAll(ForeignUnitsStore.getInstance().getArmiesBySector(sector));
        }

        final VerticalPanel selectionPanel = new VerticalPanel();
        final HorizontalPanel container = new HorizontalPanel();
        container.setVerticalAlignment(HasAlignment.ALIGN_TOP);
        final double tileSize = mapStore.getZoomedTileSize();

        final ArmyDTO zeroArmy = new ArmyDTO();
        final CorpDTO zeroCorp = new CorpDTO();
        zeroCorp.setBrigades(new HashMap<Integer, BrigadeDTO>());
        zeroArmy.setCorps(new HashMap<Integer, CorpDTO>());

        //find all zero armies for allied foreign and user nations
        for (ArmyDTO army : armies) {
            if (army.getArmyId() == 0) {
                if (army.getCorps().containsKey(0)) {
                    zeroCorp.getBrigades().putAll(army.getCorps().get(0).getBrigades());
                }
                zeroArmy.getCorps().putAll(army.getCorps());
            }
        }
        for (BrigadeDTO brig : ArmyStore.getInstance().getNewBrigadesByPosition(sector)) {
            zeroCorp.getBrigades().put(brig.getBrigadeId(), brig);
        }
        if (zeroCorp.getBrigades().size() > 0) {
            zeroArmy.getCorps().put(0, zeroCorp);
        }
        List<ArmyDTO> allArmies = new ArrayList<ArmyDTO>();
        allArmies.addAll(armies);
        allArmies.add(zeroArmy);

        int totalMovingUnits = MiscCalculators.getArmyMovingUnits(allArmies);
        int rows = MiscCalculators.getMinPowerUnderTarget(totalMovingUnits);
        HorizontalPanel[] hPanels = new HorizontalPanel[rows];
        int counter = 0, row = 0;
        hPanels[0] = new HorizontalPanel();


        //If you have no armies of yours on the tile
        //there is no need for the organization button
        final ImageButton orgArmyImg;
        if (ArmyStore.getInstance().getArmiesBySector(sector, true) != null) {
            orgArmyImg = new ImageButton(
                    "http://static.eaw1805.com/images/buttons/ButFormFedOff.png");
            orgArmyImg.deselect();
            orgArmyImg.setTitle("Organize Armies");

            orgArmyImg.setSize((tileSize / 2) + "px", (tileSize / 2) + "px");
            orgArmyImg.setStyleName("pointer", true);
            // If there are armies on the tile.
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                        mapStore.getMapsView()
                                .getArmyOrgPanel()
                                .initBySector(sector.getRegionId(),
                                        sector.getX(), sector.getY(), CORPS);
                    } else {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot organize your armies", false);
                    }
                    mySelf.hide();
                }
            }).addToElement(orgArmyImg.getElement()).register();

            container.add(orgArmyImg);
        } else {
            orgArmyImg = null;
        }


        final PositionDTO position = MapStore.getInstance()
                .getMapScrollPanelPosition();
        double offset = 0;
        if (armies.size() > 1) {
            offset = ((armies.size() - 1) * tileSize) / 2 - tileSize / 2;
        }
        this.setPopupPosition((int) (mapStore.getZoomedPointX(sector.getX()) + mapStore.getZoomOffsetX() - position.getX() - offset),
                (int) (mapStore.getZoomedPointY(sector.getY()) + mapStore.getZoomOffsetY() - position.getY()));
        for (final ArmyDTO army : armies) {
            if (army.getArmyId() != 0) {
                final int nationId = ArmyStore.getInstance().getArmyNation(army);
                int power = PowerCalculators.calculateArmyPower(army, false);

                final String url = "http://static.eaw1805.com/images/figures/" + nationId
                            + "/UnitMap00.png";
                final FigureItem armyFigure = new FigureItem(url, (int) tileSize, ARMY, army.getArmyId(), nationId,
                        sector.getId(), false, power);
                armyFigure.setId(army.getArmyId());

//                final ArmyInfoPanel armyPanel = new ArmyInfoPanel(army, false);
//                armyPanel.setWidth("260px");
//                final PopupPanel armyPopup = new PopupPanel();
//                armyPopup.setStylePrimaryName("none");
//                armyPopup.add(armyPanel);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        if (GameStore.getInstance().getNationId() == nationId) {
                            mapStore.getMapsView().goToPosition(army);

                            container.clear();
                            if (nationId == GameStore.getInstance().getNationId()) {
                                ArmyMenu armyMenu = new ArmyMenu(army, mySelf);
                                container.add(armyMenu);
                                displacePopup(73, 80);
                            } else {

                                MovementEventManager.startAllyMovement(ARMY, army.getArmyId(),
                                        nationId, RegionStore.getInstance().getSectorByPosition(army));
                                UnitEventManager.undoSelection();
                            }
                        }
                    }
                }).addToElement(armyFigure.getFigImg().getElement()).register();

                hPanels[row].add(armyFigure);
                counter++;
                if (counter == rows) {
                    row++;
                    if (row < rows) {
                        hPanels[row] = new HorizontalPanel();
                    }
                    counter = 0;
                }
            }
        }

        if (zeroArmy != null) {
            for (final CorpDTO corp : zeroArmy.getCorps().values()) {
                if (corp.getCorpId() != 0) {
                    final int nationId;
                    if (corp.getBrigades().size() > 0) {
                        nationId = corp.getBrigades().values().iterator().next().getNationId();
                    } else {
                        nationId = corp.getNationId();
                    }
                    int power = PowerCalculators.calculateCorpsPower(corp, false);
                    final String url = "http://static.eaw1805.com/images/figures/" + nationId
                                + "/UnitMap00.png";

                    final FigureItem corpFig = new FigureItem(
                            url, (int) tileSize, CORPS, corp.getCorpId(), nationId, sector.getId(), false, power);
                    corpFig.setId(corp.getCorpId());
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            if (GameStore.getInstance().getNationId() == nationId) {
                                mapStore.getMapsView().goToPosition(corp);

                                container.clear();
                                if (nationId == GameStore.getInstance().getNationId()) {
                                    CorpsMenu brigMenu = new CorpsMenu(corp, mySelf);
                                    container.add(brigMenu);
                                    displacePopup(73, 80);
                                } else {
                                    MovementEventManager.startAllyMovement(CORPS, corp.getCorpId(), nationId, RegionStore.getInstance().getSectorByPosition(corp));
                                    UnitEventManager.undoSelection();
                                }
                            }
                        }
                    }).addToElement(corpFig.getFigImg().getElement()).register();


                    hPanels[row].add(corpFig);
                    counter++;
                    if (counter == rows) {
                        row++;
                        if (row < rows) {
                            hPanels[row] = new HorizontalPanel();
                        }
                        counter = 0;
                    }
                }
            }
        }
        if (zeroCorp != null) {
            for (final BrigadeDTO brig : zeroCorp.getBrigades().values()) {
                if (!brig.getLoaded()) {
                    String url;
                    final boolean isNew = ArmyStore.getInstance().getNewBrigadeById(brig.getBrigadeId()) != null;
                    if (isNew) {
                        url = "http://static.eaw1805.com/images/figures/" + brig.getNationId() + "/UnitMapConstruction.png";
                    } else {
                        url = "http://static.eaw1805.com/images/figures/" + brig.getNationId() + "/UnitMap00.png";
                    }
                    final FigureItem brigFig = new FigureItem(url,
                            (int) tileSize, BRIGADE, brig.getBrigadeId(), brig.getNationId(), sector.getId(), false, 0);
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            if (!isNew && GameStore.getInstance().getNationId() == brig.getNationId()) {
                                mapStore.getMapsView().goToPosition(brig);
                                container.clear();
                                if (brig.getNationId() == GameStore.getInstance().getNationId()) {
                                    BrigadeMenu brigMenu = new BrigadeMenu(brig, mySelf);
                                    container.add(brigMenu);
                                    displacePopup(53, 80);
                                } else {
                                    MovementEventManager.startAllyMovement(BRIGADE, brig.getBrigadeId(), brig.getNationId(), RegionStore.getInstance().getSectorByPosition(brig));
                                    UnitEventManager.undoSelection();
                                }
                            }
                        }
                    }).addToElement(brigFig.getFigImg().getElement()).register();
                    hPanels[row].add(brigFig);
                    counter++;
                    if (counter == rows) {
                        row++;
                        if (row < rows) {
                            hPanels[row] = new HorizontalPanel();
                        }
                        counter = 0;
                    }
                }
            }
        }

        for (int i = 0; i <= row; i++) {
            try {
                selectionPanel.add(hPanels[i]);
            } catch (Exception ex) {
                // do nothing
            }
        }

        container.add(selectionPanel);
        this.add(container);
        setStyleName("");


        this.addAttachHandler(new AttachEvent.Handler() {
            public void onAttachOrDetach(AttachEvent event) {
                if (!event.isAttached()) {
                    UnitEventManager.undoSelection();
                    if (mapStore.getUnitGroups().getRegionArmyImagesById(mapStore.getActiveRegion()) != null) {
                        mapStore.getUnitGroups().getRegionArmyImagesById(mapStore.getActiveRegion()).recoverArmies(
                                mapStore.getActiveRegion());
                    }
                    if (mapStore.getAlliedUnitGroups().getRegionAlliedArmyImages(mapStore.getActiveRegion()) != null) {
                        mapStore.getAlliedUnitGroups().getRegionAlliedArmyImages(mapStore.getActiveRegion()).recoverArmies(
                                mapStore.getActiveRegion());
                    }
                    if (mapStore.getForeignUnitsGroup().getRegionArmyImages(mapStore.getActiveRegion()) != null) {
                        mapStore.getForeignUnitsGroup().getRegionArmyImages(mapStore.getActiveRegion()).recoverArmies(
                                mapStore.getActiveRegion());
                    }
                    if (mapStore.getForeignUnitsGroup().getRegionReportedArmyImages(mapStore.getActiveRegion()) != null) {
                        mapStore.getForeignUnitsGroup().getRegionReportedArmyImages(mapStore.getActiveRegion()).recoverArmies(
                                mapStore.getActiveRegion());
                    }
                    if (mapStore.getUnitGroups().getRegionNewArmiesByRegionId(mapStore.getActiveRegion()) != null) {
                        mapStore.getUnitGroups().getRegionNewArmiesByRegionId(mapStore.getActiveRegion()).recoverArmies(
                                mapStore.getActiveRegion());
                    }
                }
            }
        });

        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 7
                && TutorialStore.getInstance().getTutorialStep() == 1) {//then go to next step
            TutorialStore.nextStep(false);
        }

        if (orgArmyImg != null
                && TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 8
                && TutorialStore.getInstance().getTutorialStep() == 2) {//then go to next step
            tempReg = orgArmyImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    tempReg.removeHandler();
                    TutorialStore.nextStep(false);
                }
            });
            TutorialStore.getInstance().setActionWidget(orgArmyImg);
            TutorialStore.highLightButton(orgArmyImg);
        }

    }

    private HandlerRegistration tempReg;

    private void displacePopup(int offsetX, int offsetY) {
        final PositionDTO position = MapStore.getInstance().getMapScrollPanelPosition();
        this.setPopupPosition(mapStore.getZoomedPointX(sector.getX()) + (int) mapStore.getZoomOffsetX() - position.getX() - offsetX,
                mapStore.getZoomedPointY(sector.getY()) + (int) mapStore.getZoomOffsetY() - position.getY() - offsetY);
    }

}
