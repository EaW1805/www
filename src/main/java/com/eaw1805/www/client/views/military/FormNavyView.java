package com.eaw1805.www.client.views.military;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.military.formFleet.FormFleetView;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.List;

public class FormNavyView extends DraggablePanel implements RegionConstants, ArmyConstants, StyleConstants {

    private static final String NO_ARMIES = "No armies here";

    private final ImageButton formFleetsImg, exchShipsImg;
    private final ImageButton europeImg, africaImg, caribImg, indiaImg;
    private int regionId = EUROPE, x = 0, y = 0, type = FLEET;
    private final AbsolutePanel controlsPanel;
    private final ImageButton leftImg;
    private final ImageButton rightImg;
    private final Label lblPosition;
    private List<SectorDTO> posWithArmies;

    public FormNavyView() {
        this.setStyleName("formFederationsView");
        this.setSize("1218px", "680px");

        this.europeImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png");
        this.europeImg.setId(EUROPE);
        this.europeImg.setTitle("Select european theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!europeImg.getUrl().contains("NA")) {
                    selectView(EUROPE);
                }
            }
        }).addToElement(europeImg.getElement()).register();

        this.europeImg.setStyleName(CLASS_POINTER);
        this.add(this.europeImg, 512, 62);
        this.europeImg.setSize(SIZE_31PX, SIZE_31PX);

        this.africaImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png");
        this.africaImg.setId(AFRICA);
        this.africaImg.setTitle("Select african theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!africaImg.getUrl().contains("NA")) {
                    selectView(AFRICA);
                }
            }
        }).addToElement(africaImg.getElement()).register();

        this.africaImg.setStyleName(CLASS_POINTER);
        this.add(this.africaImg, 570, 62);
        this.africaImg.setSize(SIZE_31PX, SIZE_31PX);

        this.caribImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png");
        this.caribImg.setId(CARIBBEAN);
        this.caribImg.setTitle("Select carribean theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!caribImg.getUrl().contains("NA")) {
                    selectView(CARIBBEAN);
                }
            }
        }).addToElement(caribImg.getElement()).register();

        this.caribImg.setStyleName(CLASS_POINTER);
        this.add(this.caribImg, 628, 62);
        this.caribImg.setSize(SIZE_31PX, SIZE_31PX);

        this.indiaImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png");
        this.indiaImg.setId(INDIES);
        this.indiaImg.setTitle("Select india theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!indiaImg.getUrl().contains("NA")) {
                    selectView(INDIES);
                }
            }
        }).addToElement(indiaImg.getElement()).register();

        this.indiaImg.setStyleName(CLASS_POINTER);
        this.add(this.indiaImg, 685, 62);
        this.indiaImg.setSize(SIZE_31PX, SIZE_31PX);


        this.formFleetsImg = new ImageButton("http://static.eaw1805.com/images/panels/formFederations/buttons/ButFleetsOn.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectOrgType(formFleetsImg.getId());
            }
        }).addToElement(formFleetsImg.getElement()).register();

        this.formFleetsImg.setStyleName(CLASS_POINTER);
        this.formFleetsImg.setId(FLEET);
        this.add(this.formFleetsImg, 13, 74);
        this.formFleetsImg.setSize("30px", "140px");

        this.exchShipsImg = new ImageButton("http://static.eaw1805.com/images/panels/formFederations/buttons/ButShipsOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectOrgType(exchShipsImg.getId());
            }
        }).addToElement(exchShipsImg.getElement()).register();

        this.exchShipsImg.setStyleName(CLASS_POINTER);
        this.exchShipsImg.setId(SHIP);
        this.add(this.exchShipsImg, 13, 220);
        this.exchShipsImg.setSize("30px", "140px");

        this.controlsPanel = new AbsolutePanel();
        this.add(this.controlsPanel, 54, 106);
        this.controlsPanel.setSize("1134px", "544px");

        this.leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                leftImg.deselect();
                if (!posWithArmies.isEmpty()) {
                    SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[x][y];
                    int index = posWithArmies.indexOf(sector) - 1;
                    if (index < 0) {
                        index = posWithArmies.size() - 1;
                    }
                    sector = posWithArmies.get(index);
                    initBySector(sector.getRegionId(), sector.getX(), sector.getY(), type);
                }
            }
        }).addToElement(leftImg.getElement()).register();

        this.leftImg.setStyleName(CLASS_POINTER);
        this.add(this.leftImg, 19, 11);
        this.leftImg.setSize(SIZE_31PX, SIZE_31PX);

        this.rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                rightImg.deselect();
                if (!posWithArmies.isEmpty()) {
                    SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[x][y];
                    int index = posWithArmies.indexOf(sector) + 1;
                    if (index > (posWithArmies.size() - 1)) {
                        index = 0;
                    }
                    sector = posWithArmies.get(index);
                    initBySector(sector.getRegionId(), sector.getX(), sector.getY(), type);
                }
            }
        }).addToElement(rightImg.getElement()).register();

        this.rightImg.setStyleName(CLASS_POINTER);
        this.add(this.rightImg, 1114, 11);
        this.rightImg.setSize(SIZE_31PX, SIZE_31PX);

        this.lblPosition = new Label("");
        this.lblPosition.setStyleName("clearFontMedLarge whiteText");
        this.add(this.lblPosition, 65, 14);


        // Add the close and accept window button
        final FormNavyView myself = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName(CLASS_POINTER);
        imgX.setTitle("Close panel");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 1152, 10);
        imgX.setSize("36px", "36px");

    }

    private void selectView(final int regionId) {
        if (this.regionId == regionId) {
            ImageButton img = null;
            switch (this.regionId) {
                case EUROPE:
                    img = europeImg;
                    break;
                case AFRICA:
                    img = africaImg;
                    break;
                case INDIES:
                    img = indiaImg;
                    break;
                case CARIBBEAN:
                    img = caribImg;
                    break;
                default:
                    break;

            }
            selectRegionViewImg(img);
        } else {
            this.regionId = regionId;
            ImageButton img = null;
            switch (regionId) {
                case EUROPE:
                    img = europeImg;
                    break;
                case AFRICA:
                    img = africaImg;
                    break;
                case INDIES:
                    img = indiaImg;
                    break;
                case CARIBBEAN:
                    img = caribImg;
                    break;
                default:
                    break;
            }
            posWithArmies = NavyStore.getInstance().getRegionSectorsWithFleets(regionId, true);
            if (posWithArmies != null && !posWithArmies.isEmpty()) {
                initBySector(posWithArmies.get(0).getRegionId(), posWithArmies.get(0).getX(), posWithArmies.get(0).getY(), this.type);
            } else {
                lblPosition.setText(NO_ARMIES);
            }
            selectRegionViewImg(img);
        }
    }

    private void selectRegionViewImg(final ImageButton img) {
        this.europeImg.deselect();
        this.europeImg.getUrl().replace("On", OFF);
        this.caribImg.deselect();
        this.caribImg.getUrl().replace("On", OFF);
        this.indiaImg.deselect();
        this.indiaImg.getUrl().replace("On", OFF);
        this.africaImg.deselect();
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }

    public void initBySector(final int regionId, final int x, final int y, final int typeId) {
        // Add the widget to the layout panel
        GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(this);
        GameStore.getInstance().getLayoutView().positionTocCenter(this);
        selectView(MapStore.getInstance().getActiveRegion());

        // Set the sector information
        this.regionId = regionId;
        this.x = x;
        this.y = y;
        this.lblPosition.setText(RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[x][y].positionToString() + " - Form Navy");

        posWithArmies = NavyStore.getInstance().getRegionSectorsWithFleets(
                regionId, true);
        if (!NavyStore.getInstance().getRegionSectorsWithFleets(EUROPE, true).isEmpty()) {
            this.europeImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png");
            this.europeImg.setTitle("Select european theater");
        } else {
            this.europeImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeNA.png");
            this.europeImg.setTitle(NO_ARMIES);
        }
        if (!NavyStore.getInstance().getRegionSectorsWithFleets(CARIBBEAN, true).isEmpty()) {
            this.caribImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png");
            this.caribImg.setTitle("Select carribean theater");
        } else {
            this.caribImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaNA.png");
            this.caribImg.setTitle(NO_ARMIES);
        }
        if (!NavyStore.getInstance().getRegionSectorsWithFleets(INDIES, true).isEmpty()) {
            this.indiaImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png");
            this.indiaImg.setTitle("Select indies theater");
        } else {
            this.indiaImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaNA.png");
            this.indiaImg.setTitle(NO_ARMIES);
        }
        if (!NavyStore.getInstance().getRegionSectorsWithFleets(AFRICA, true).isEmpty()) {
            this.africaImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png");
            this.africaImg.setTitle("Select african theater");
        } else {
            this.africaImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaNA.png");
            this.africaImg.setTitle(NO_ARMIES);
        }
        // select the appropriate control
        // to add to the controls panel
        selectOrgType(typeId);
    }

    private void selectOrgType(final int typeId) {
        ImageButton img = null;
        this.type = typeId;
        final SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(this.regionId)[this.x][this.y];
        controlsPanel.clear();
        switch (typeId) {
            case FLEET:
                img = formFleetsImg;
                controlsPanel.add(new FormFleetView(sector));
                break;
            case SHIP:
                img = exchShipsImg;
                controlsPanel.add(new com.eaw1805.www.client.views.military.exchShips.ExchangeShipsView(sector));
                break;
            default:
                break;
        }
        selectCargoTypeViewImg(img);
    }

    private void selectCargoTypeViewImg(final ImageButton img) {
        this.formFleetsImg.deselect();
        this.formFleetsImg.getUrl().replace("On", OFF);
        this.exchShipsImg.deselect();
        this.exchShipsImg.getUrl().replace("On", OFF);
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }
}

