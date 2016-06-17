package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.FigureItem;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.TransportStore;
import com.eaw1805.www.shared.stores.util.calculators.ConquerCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

import java.util.List;

public class BrigadeMenu extends UnitMenu implements ArmyConstants, StyleConstants {

    final BrigadeDTO thisBrigade;

    public BrigadeMenu(final BrigadeDTO brigade, final PopupPanelEAW parent) {
        setPopupParent(parent);
        thisBrigade = brigade;
        setSize("156px", "137px");
        setStyleName("");

        final AbsolutePanel basePanel = new AbsolutePanel();
        this.add(basePanel, 46, 73);
        basePanel.setSize("64px", "64px");

        final FigureItem brigadeImage = new FigureItem("http://static.eaw1805.com/images/figures/"
                + GameStore.getInstance().getNationId() + "/UnitMap00.png", 64, BRIGADE, brigade.getBrigadeId(), brigade.getNationId(),
                RegionStore.getInstance().getSectorByPosition(brigade).getId(), true, 0);
        basePanel.add(brigadeImage, 0, 0);

        final MapStore mapStore = MapStore.getInstance();


        if (TradeStore.getInstance().hasInitSecondPhase(BRIGADE, brigade.getBrigadeId())) {
            moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/moveNA.png");
            moveImg.setTitle("You have initiated the second phase. Cannot move.");
        } else {
            moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    mapStore.getMapsView().goToPosition(brigade);
                    mapStore.getMapsView().addFigureOnMap(BRIGADE, brigade.getBrigadeId(),
                            brigade, MiscCalculators.getBrigadeMps(brigade, false), 0,
                            ConquerCalculators.getBrigadeMaxConquers(brigade), ConquerCalculators.getBrigadeMaxNeutralConquers(brigade));
                    parent.hide();
                }
            }).addToElement(moveImg.getElement()).register();

            moveImg.setTitle("Issue move orders.");
        }

        moveImg.setStyleName("pointer");
        moveImg.setSize(SIZE_36PX, SIZE_36PX);


        boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
        boardImg.setTitle("Board.");
        boardImg.setStyleName("pointer");
        boardImg.setSize(SIZE_36PX, SIZE_36PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final PositionDTO pos = MovementStore.getInstance().getUnitPosition(BRIGADE, brigade.getBrigadeId(), brigade);
                final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                if (hasTransports) {
                    final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                    parent.hide();
                }
            }
        }).addToElement(boardImg.getElement()).register();

        if (!brigade.getLoaded()) {
            setupImages();
        }
        this.finalizeMenu(115);


        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 7
                && TutorialStore.getInstance().getTutorialStep() == 2) {

            tempReg = moveImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    tempReg.removeHandler();
                    TutorialStore.nextStep(false);
                }
            });
            TutorialStore.highLightButton(moveImg);
        }
    }

    private HandlerRegistration tempReg;

    public void clearImages() {
        remove(moveImg);
        remove(boardImg);
    }


    private final ImageButton moveImg;
    private final ImageButton boardImg;


    public final void setupImages() {
        try {
            clearImages();
        } catch (Exception e) {
            //do nothing here
        }

        //if empire is dead then don't add the action images.
        if (GameStore.getInstance().isNationDead() || GameStore.getInstance().isGameEnded()) {
            return;
        }
        addImageButton(moveImg);


        final SectorDTO sector = MapStore.getInstance()
                .getRegionSectorByRegionIdXY(thisBrigade.getRegionId(),
                        thisBrigade.getX(),
                        thisBrigade.getY());

        final List<TransportUnitDTO> transports =
                TransportStore.getInstance().getAllTransportUnitsBySector(sector);

        if (!transports.isEmpty()) {
            addImageButton(boardImg);
        }

    }


}
