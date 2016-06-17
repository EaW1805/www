package com.eaw1805.www.client.views.economy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;

import java.util.List;

public class HandOverLandView
        extends DraggablePanel {

    private final Grid countriesGrid;
    private final Label lblHandOverTo;

    private int selectedNationId = -1;
    private SectorDTO selSector;

    public HandOverLandView(final SectorDTO selTileSector) {
        setSelectedNationId(selTileSector.getNationId());
        setSelSector(selTileSector);

        this.setStyleName("handOverPanel whiteText");
        this.setSize("402px", "437px");

        final Label lblHandOverTile = new Label("Hand over tile");
        lblHandOverTile.setStyleName("clearFont-large whiteText");
        lblHandOverTile.setDirectionEstimator(true);
        lblHandOverTile.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblHandOverTile.setSize("402px", "34px");
        add(lblHandOverTile, 15, 12);

        final Label lblSector = new Label("Sector " + selTileSector.positionToString());
        lblSector.setStyleName("clearFontMiniTitle");
        this.add(lblSector, 32, 91);

        final Label lblSectorName = new Label(selTileSector.getName());
        lblSectorName.setStyleName("clearFontMiniTitle");
        this.add(lblSectorName, 45, 115);

        final Label lblSelectCountry = new Label("Select Country:");
        lblSelectCountry.setStyleName("clearFontMiniTitle");
        this.add(lblSelectCountry, 32, 150);

        this.countriesGrid = new Grid(4, 4);
        this.countriesGrid.setBorderWidth(0);
        this.countriesGrid.setCellPadding(0);
        this.countriesGrid.setCellSpacing(0);
        this.add(this.countriesGrid, 61, 175);
        this.countriesGrid.setSize("270px", "172px");

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        imgX.setStyleName("pointer");
        imgX.setSize("36px", "36px");
        final HandOverLandView self = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();
        add(imgX, 335, 9);

        final ImageButton imgTick = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgTick.setStyleName("pointer");
        imgTick.setSize("36px", "36px");
        imgTick.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                RegionStore.getInstance().handOverSector(selTileSector, getSelectedNationId());
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgTick.deselect();
            }
        });
        add(imgTick, 300, 9);

        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("whiteText");
        this.add(absolutePanel, 0, 367);
        absolutePanel.setSize("408px", "18px");

        this.lblHandOverTo = new Label("Hand over to ");
        this.lblHandOverTo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        absolutePanel.add(this.lblHandOverTo, 39, 350);
        this.lblHandOverTo.setSize("268px", "");

        setUpCountriesGrid();
    }

    private void setUpCountriesGrid() {
        final List<RelationDTO> relations = RelationsStore.getInstance().getRelationsList();
        int xPos = 0, yPos = 0;
        final int myNationId = GameStore.getInstance().getNationId();
        for (RelationDTO relation : relations) {
            if (relation.getTargetNationId() != myNationId) {
                final DualStateImage countryImage = new DualStateImage();
                addFunctionalityToImage(countryImage);
                countryImage.setStyleName("pointer", true);
                countryImage.setId(relation.getTargetNationId());
                countryImage.setSize("64px", "41px");
                countryImage.setTitle(DataStore.getInstance().getNameByNationId(relation.getTargetNationId()));
                countriesGrid.setWidget(yPos, xPos, countryImage);
                countryImage.setUrl("http://static.eaw1805.com/images/nations/nation-" + relation.getTargetNationId() + ".png");
                if (relation.getRelation() != 1) {
                    countryImage.setUrl("http://static.eaw1805.com/images/nations/nation-" + relation.getTargetNationId() + "NA.png");
                    countryImage.deselect();
                }
            }

            if (xPos == 3) {
                xPos = 0;
                yPos++;

            } else {
                xPos++;
            }
        }

        final int currOwner = RegionStore.getInstance().getCurrentOwner(getSelSector());
        setCurrentHandOverNation(currOwner);
    }

    private void setCurrentHandOverNation(int nationId) {
        if (nationId != getSelSector().getNationId()) {
            setSelectedNationId(nationId);
            if (getSelSector().getNationId() < nationId) {
                nationId--;
            }

            final DualStateImage imgToSelect = (DualStateImage) countriesGrid.getWidget(Math.round(nationId / 4), (nationId % 4) - 1);
            imgToSelect.setSelected(true);
            if (!imgToSelect.getUrl().endsWith("Slc.png")) {
                imgToSelect.setUrl(imgToSelect.getUrl().replace(".png", "Slc.png"));
            }
        }
    }

    private void addFunctionalityToImage(final DualStateImage countryImage) {
        countryImage.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                if (!countryImage.getUrl().endsWith("NA.png")) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            final DualStateImage ctImage = (DualStateImage) countriesGrid.getWidget(i, j);
                            if (!ctImage.equals(countryImage)) {

                                ctImage.deselect();
                            }
                        }
                    }

                    setSelectedNationId(countryImage.getId());
                    setCurrentHandOverNation(countryImage.getId());
                }
            }
        });

        countryImage.addMouseOverHandler(new MouseOverHandler() {

            public void onMouseOver(final MouseOverEvent event) {
                lblHandOverTo.setText("Hand over to " + countryImage.getTitle());
            }
        });

        countryImage.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(final MouseOutEvent event) {
                lblHandOverTo.setText("Hand over to (Select Nation)");

            }
        });
    }

    public final void setSelectedNationId(final int selectedNationId) {
        this.selectedNationId = selectedNationId;
    }

    public final int getSelectedNationId() {
        return selectedNationId;
    }

    public final void setSelSector(final SectorDTO selSector) {
        this.selSector = selSector;
    }

    public final SectorDTO getSelSector() {
        return selSector;
    }
}
