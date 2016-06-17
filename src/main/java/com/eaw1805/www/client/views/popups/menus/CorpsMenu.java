package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
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
 * Created by IntelliJ IDEA.
 * User: karavias
 * Date: 1/18/12
 * Time: 4:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class CorpsMenu extends UnitMenu implements ArmyConstants, StyleConstants {

    private final CorpDTO thisCorp;

    public CorpsMenu(final CorpDTO corp, final PopupPanelEAW parent) {
        setPopupParent(parent);
        thisCorp = corp;
        setSize("197px", "137px");
        setStyleName("");

        final AbsolutePanel basePanel = new AbsolutePanel();
        add(basePanel, 66, 73);
        basePanel.setSize("64px", "64px");

        int power = PowerCalculators.calculateCorpsPower(corp, false);

        final String url = "http://static.eaw1805.com/images/figures/" + corp.getNationId()
                    + "/UnitMap00.png";

        final FigureItem corpFig = new FigureItem(url, 64, CORPS, corp.getCorpId(), ArmyStore.getInstance().getCorpNation(corp),
                RegionStore.getInstance().getSectorByPosition(corp).getId(), true, power);
        basePanel.add(corpFig, 0, 0);

        final MapStore mapStore = MapStore.getInstance();


        moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                mapStore.getMapsView().goToPosition(corp);
                mapStore.getMapsView().addFigureOnMap(CORPS, corp.getCorpId(),
                        corp, MiscCalculators.getCorpMps(corp, false), 0,
                        ConquerCalculators.getCorpsMaxConquers(corp), ConquerCalculators.getCorpsMaxNeutralConquers(corp));
                parent.hide();
            }
        }).addToElement(moveImg.getElement()).register();

        moveImg.setTitle("Issue move orders.");
        moveImg.setStyleName("pointer");
        add(moveImg, 40, 19);
        moveImg.setSize(SIZE_36PX, SIZE_36PX);


        boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        boardImg.setTitle("Board.");
        boardImg.setStyleName("pointer");
        add(boardImg, 81, 19);
        boardImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(CORPS, corp.getCorpId(), corp);
                final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                if (hasTransports) {
                    final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                    parent.hide();
                }
            }
        }).addToElement(boardImg.getElement()).register();


        if (MovementStore.getInstance().hasMovedThisTurn(CORPS, thisCorp.getCorpId())) {
            assignComImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAssignLeaderNA.png");
            assignComImg.setTitle("The unit has moved and cannot be assigned a commander");
            assignComImg.setStyleName("pointer");
        } else {
            assignComImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAssignLeaderOff.png");
            assignComImg.setTitle("Assign commander.");
            assignComImg.setStyleName("pointer");
            assignComImg.setSize(SIZE_36PX, SIZE_36PX);
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    final CommandersListPopup cmPopup = new CommandersListPopup(
                            CommanderStore.getInstance().getCommandersBySectorAndStartingPosition(
                                    RegionStore.getInstance().getSectorByPosition(thisCorp), true, true),
                            CorpsMenu.this, 0);
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
                    if (corp.getCommander() != null
                            && corp.getCommander().getId() > 0) {
                        CommanderStore.getInstance().commanderLeaveFederation(corp.getCommander().getId());
                    }
                }
            }.addToElement(dismissImg.getElement()).register();

        }
        add(assignComImg, 122, 19);
        setupImages();
        finalizeMenu(190);
    }


    private final ImageButton moveImg;
    private final ImageButton boardImg;
    private ImageButton assignComImg;
    private ImageButton dismissImg;


    public final void setupImages() {

        //if empire is dead then don't add the action images.
        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }

        addImageButton(moveImg);


        final SectorDTO sector = MapStore.getInstance()
                .getRegionSectorByRegionIdXY(thisCorp.getRegionId(),
                        thisCorp.getX(),
                        thisCorp.getY());

        final List<TransportUnitDTO> transports =
                TransportStore.getInstance().getAllTransportUnitsBySector(sector);

        if (!transports.isEmpty()) {
            addImageButton(boardImg);
        }
        addImageButton(assignComImg);
        if (thisCorp.getCommander() != null && thisCorp.getCommander().getId() > 0
                && dismissImg != null) {
            addImageButton(dismissImg);
        }
    }


    /**
     * Method that adds the selected commander to the corps
     *
     * @param selCommander
     */
    public void setCommander(final CommanderDTO selCommander) {
        CommanderStore.getInstance().addCommanderToCorp(thisCorp.getCorpId(), selCommander.getId(), false);
    }

}
