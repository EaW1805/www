package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.popups.CommandersListPopup;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import com.eaw1805.www.shared.stores.util.calculators.ConquerCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

import java.util.List;

/**
 * Menu popup when selecting an army figure.
 */
public class ArmyMenu extends UnitMenu implements ArmyConstants {

    final ArmyDTO thisArmy;

    public ArmyMenu(final ArmyDTO army, final PopupPanelEAW parent) {
        setPopupParent(parent);
        thisArmy = army;
        setSize("197px", "137px");
        setStyleName("");

        final AbsolutePanel basePanel = new AbsolutePanel();
        this.add(basePanel, 66, 73);
        basePanel.setSize("64px", "64px");

        int power = PowerCalculators.calculateArmyPower(army, false);

        final String url = "http://static.eaw1805.com/images/figures/" + army.getNationId()
                    + "/UnitMap00.png";


        final FigureItem armyFig = new FigureItem(url, 64, ARMY, army.getArmyId(), ArmyStore.getInstance().getArmyNation(army),
                RegionStore.getInstance().getSectorByPosition(army).getId(), true, power);
        basePanel.add(armyFig, 0, 0);

        final MapStore mapStore = MapStore.getInstance();


        moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                mapStore.getMapsView().goToPosition(army);
                mapStore.getMapsView().addFigureOnMap(ARMY, army.getArmyId(),
                        army, MiscCalculators.getArmyMps(army), 0, ConquerCalculators.getArmyMaxConquers(army), ConquerCalculators.getArmyMaxNeutralConquers(army));
                parent.hide();
            }
        }).addToElement(moveImg.getElement()).register();

        moveImg.setTitle("Issue move orders.");
        moveImg.setStyleName(CLASS_POINTER);
        moveImg.setSize(SIZE_36PX, SIZE_36PX);


        boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        boardImg.setTitle("Board.");
        boardImg.setStyleName(CLASS_POINTER);
        add(boardImg, 81, 19);
        boardImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(ARMY, army.getArmyId(), army);
                final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                if (hasTransports) {
                    final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                    parent.hide();
                }
            }
        }).addToElement(boardImg.getElement()).register();


        if (MovementStore.getInstance().hasMovedThisTurn(ARMY, thisArmy.getArmyId())) {
            assignComImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAssignLeaderNA.png");
            assignComImg.setTitle("The unit has moved and cannot be assigned a commander");
            assignComImg.setStyleName(CLASS_POINTER);

        } else {
            assignComImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAssignLeaderOff.png");
            assignComImg.setTitle("Assign commander.");
            assignComImg.setStyleName(CLASS_POINTER);

            assignComImg.setSize(SIZE_36PX, SIZE_36PX);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final CommandersListPopup cmPopup = new CommandersListPopup(
                            CommanderStore.getInstance().getCommandersBySectorAndStartingPosition(
                                    RegionStore.getInstance().getSectorByPosition(thisArmy), true, true),
                            ArmyMenu.this, 0);
                    cmPopup.setPopupPosition(event.getNativeEvent()
                            .getClientX(), event.getNativeEvent().getClientY() - 250);
                    cmPopup.show();
                }
            }).addToElement(assignComImg.getElement()).register();
            dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
            dismissImg.setTitle("Dismiss commander");
            dismissImg.setStyleName(CLASS_POINTER);
            dismissImg.setSize(SIZE_36PX, SIZE_36PX);
            new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    if (army.getCommander() != null
                            && army.getCommander().getId() > 0) {
                        CommanderStore.getInstance().commanderLeaveFederation(army.getCommander().getId());
                    }
                }
            }.addToElement(dismissImg.getElement()).register();

        }
        setupImages();
        finalizeMenu(190);
    }


    private final ImageButton moveImg;
    private final ImageButton boardImg;
    private ImageButton assignComImg;
    private ImageButton dismissImg;


    public void clearImages() {


        remove(moveImg);
        remove(boardImg);
        remove(assignComImg);
        remove(dismissImg);
    }


    public final void setupImages() {
        try {
            clearImages();
        } catch (Exception e) {
            //do nothing here for now
        }
        //if empire is dead then don't add any images
        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }


        addImageButton(moveImg);


        final SectorDTO sector = MapStore.getInstance()
                .getRegionSectorByRegionIdXY(thisArmy.getRegionId(),
                        thisArmy.getX(),
                        thisArmy.getY());

        final List<TransportUnitDTO> transports =
                TransportStore.getInstance().getAllTransportUnitsBySector(sector);

        if (!transports.isEmpty()) {
            addImageButton(boardImg);
        }
        addImageButton(assignComImg);
        if (thisArmy.getCommander() != null
                && thisArmy.getCommander().getId() > 0
                && dismissImg != null) {
            addImageButton(dismissImg);
        }
    }


    /**
     * Method that adds the selected commander to the army
     *
     * @param selCommander
     */
    public void setCommander(final CommanderDTO selCommander) {
        CommanderStore.getInstance().addCommanderToArmy(thisArmy.getArmyId(), selCommander.getId(), false);
    }

}
