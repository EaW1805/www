package com.eaw1805.www.client.views.military.addbattalion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.List;


public class AddBattalionView
        extends DraggablePanel
        implements ArmyConstants, OrderConstants, RegionConstants, GoodConstants {

    private transient final VerticalPanelScrollChild brigadesPanel;
    private transient final AddToSelectedPanel addToSelPanel;
    private transient final SectorDTO sector;
    private transient final ImageButton leftImg;
    private transient final ImageButton rightImg;

    public AddBattalionView(final SectorDTO barrSectorDto) {
        this.sector = barrSectorDto;
        this.setStyleName("addBattalionsView");
        this.setSize("998px", "679px");

        final BarrackDTO barrack = BarrackStore.getInstance().getBarrackByPosition(barrSectorDto);

        this.leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        this.leftImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrack);
                if (index > 0) {
                    index--;
                } else {
                    index = BarrackStore.getInstance().getBarracksList().size() - 1;
                }

                final BarrackDTO nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                final SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                //SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                final AddBattalionView batView = new AddBattalionView(barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(batView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(batView, AddBattalionView.this.getAbsoluteLeft(), AddBattalionView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(AddBattalionView.this);

                leftImg.deselect();
                leftImg.setUrl(leftImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(leftImg.getElement()).register();
        add(this.leftImg, 15, 9);

        this.leftImg.setSize("35px", "35px");
        this.rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        this.rightImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrack);
                if (index < BarrackStore.getInstance().getBarracksList().size() - 1) {
                    index++;
                } else {
                    index = 0;
                }

                final BarrackDTO nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                final SectorDTO barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];
                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                final AddBattalionView batView = new AddBattalionView(barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(batView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(batView, AddBattalionView.this.getAbsoluteLeft(), AddBattalionView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(AddBattalionView.this);

                rightImg.deselect();
                rightImg.setUrl(rightImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(rightImg.getElement()).register();

        add(this.rightImg, 895, 9);
        this.rightImg.setSize("35px", "35px");

        final Label lblAddBattalionsTo = new Label(sector.positionToString() + " - Add battalions to Brigades");
        lblAddBattalionsTo.setStyleName("clearFontMedLarge whiteText");
        add(lblAddBattalionsTo, 60, 14);

        final Label lblBrigades = new Label("Brigades:");
        lblBrigades.setStyleName("clearFontMedLarge whiteText");
        add(lblBrigades, 25, 81);

        brigadesPanel = new VerticalPanelScrollChild();
        brigadesPanel.setSize("366px", "46px");
        final ScrollVerticalBarEAW scroller = new ScrollVerticalBarEAW(brigadesPanel, 90, false);
        scroller.setSize(389, 527);
        add(scroller, 17, 114);

        addToSelPanel = new AddToSelectedPanel(barrSectorDto, this);
        add(addToSelPanel, 417, 78);

        populateBrigadesPanel();

        final AddBattalionView myself = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().getOptionsMenu().getRelImage().deselect();
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 945, 9);
        imgX.setSize("36px", "36px");
    }

    public void populateBrigadesPanel() {
        brigadesPanel.clear();
        final List<BrigadeDTO> sectorBrigades = ArmyStore.getInstance().getBrigadesByPosition(sector.getRegionId(), sector.getX(), sector.getY(), false);
        for (final BrigadeDTO brig : sectorBrigades) {
            if (brig.getBattalions().size() < 6
                    && !brig.getLoaded()
                    && !brig.getStartLoaded()) {
                final BrigadeInfoPanel brigPanel = new BrigadeInfoPanel(brig, false);
                brigPanel.getBrigadePanel().setStyleName("pointer", true);
                brigPanel.getBrigadePanel().addDomHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        for (int index = 0; index < brigadesPanel.getWidgetCount(); index++) {
                            ((BrigadeInfoPanel) brigadesPanel.getWidget(index)).deselect();
                        }
                        brigPanel.select();
                        addToSelPanel.selectBrigade(brigPanel.getBrigade());
                    }
                }, ClickEvent.getType());

                brigPanel.getBrigadePanel().addDomHandler(new MouseOverHandler() {
                    public void onMouseOver(final MouseOverEvent event) {
                        brigPanel.mouseOver();
                    }
                }, MouseOverEvent.getType());

                brigPanel.addDomHandler(new MouseOutHandler() {
                    public void onMouseOut(final MouseOutEvent event) {
                        brigPanel.mouseOut();
                    }
                }, MouseOutEvent.getType());

                brigadesPanel.add(brigPanel);
            }
        }
        brigadesPanel.resizeScrollBar();
    }
}
