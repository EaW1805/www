package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.List;

public class CommanderMenu extends UnitMenu implements ArmyConstants, StyleConstants {

    private final FigureItem commanderImage;
    private final ImageButton moveImg;
    private final ImageButton boardImg;
    private final ImageButton dismissImg;
    private final CommanderDTO thisCommander;
    private final Image skull;

    public CommanderMenu(final CommanderDTO commander, final PopupPanelEAW caller) {
        setPopupParent(caller);
        thisCommander = commander;
        final MapStore mapStore = MapStore.getInstance();

        setSize("197px", "137px");
        setStyleName("");

        final AbsolutePanel basePanel = new AbsolutePanel();
        basePanel.setSize("64px", "64px");
        add(basePanel, 65, 73);


        setupBackground();


        skull = new Image("http://static.eaw1805.com/img/commanders/skull.png");
        skull.setTitle("Commander is dead");
        skull.setSize(SIZE_36PX, SIZE_36PX);
        commanderImage = new FigureItem("http://static.eaw1805.com/images/figures/" + commander.getNationId() + "/commander.png",
                64, COMMANDER, commander.getId(), commander.getNationId(),
                RegionStore.getInstance().getSectorByPosition(commander).getId(), true, 0);
        basePanel.add(commanderImage, 0, 0);

        if (TradeStore.getInstance().hasInitSecondPhase(COMMANDER, commander.getId())) {
            moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/moveNA.png");
            moveImg.setTitle("You have initiated the second phase. Cannot move.");
        } else {
            moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    mapStore.getMapsView().goToPosition(commander);
                    mapStore.getMapsView().addFigureOnMap(COMMANDER, commander.getId(),
                            commander, commander.getMps(), 0, 0, 0);
                    caller.hide();
                }
            }).addToElement(moveImg.getElement()).register();
            moveImg.setTitle("Issue move orders.");
        }
        moveImg.setStyleName("pointer");
        moveImg.setSize(SIZE_36PX, SIZE_36PX);
//        add(moveImg, 40, 19);

        boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        boardImg.setTitle("Board.");
        boardImg.setStyleName("pointer");
        boardImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(COMMANDER, commander.getId(), commander);
                final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                if (hasTransports) {
                    final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                    caller.hide();
                }
            }
        }).addToElement(boardImg.getElement()).register();

        dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
        dismissImg.setTitle("Dismiss commander");
        dismissImg.setStyleName("pointer");
        dismissImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                CommanderStore.getInstance().dismissCommander(commander.getId());
                caller.hide();
            }
        }).addToElement(dismissImg.getElement()).register();

        setupImages();
    }


    private Image leftBrdImg;
    private Image rightBrdImg;
    private Image butt1Img;
    private Image butt2Img;
    private Image butt3Img;


    public final void setupImages() {
        //if empire is dead then don't add the action images.
        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }


        if (!thisCommander.getLoaded() && (thisCommander.getArmy() == 0) && (thisCommander.getCorp() == 0)) {
            if (thisCommander.getDead()) {
                add(leftBrdImg, 0, 0);
                int posX = 37;
                add(butt1Img, posX, 0);
                add(skull, posX + 3, 19);
                posX += 41;
                add(rightBrdImg, posX, 0);
            } else {

                addImageButton(moveImg);

                final SectorDTO sector = MapStore.getInstance()
                        .getRegionSectorByRegionIdXY(thisCommander.getRegionId(),
                                thisCommander.getX(),
                                thisCommander.getY());

                final List<TransportUnitDTO> transports =
                        TransportStore.getInstance().getAllTransportUnitsBySector(sector);

                if (!transports.isEmpty()) {
                    addImageButton(boardImg);
                }

                addImageButton(dismissImg);
                finalizeMenu(190);
            }

        }

    }


    public final void setupBackground() {
        //setup background
        leftBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/LeftEnd.png");
        leftBrdImg.setSize("37px", SIZE_73PX);

        rightBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/RightEnd.png");
        rightBrdImg.setSize("37px", SIZE_73PX);

        butt1Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt1Img.setSize("41px", SIZE_73PX);

        butt2Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt2Img.setSize("41px", SIZE_73PX);

        butt3Img = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        butt3Img.setSize("41px", SIZE_73PX);

        //end-setup background
    }
}
