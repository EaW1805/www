package com.eaw1805.www.client.views.military;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.military.formArmy.FormArmyView;
import com.eaw1805.www.client.views.military.formCorp.FormCorpView;
import com.eaw1805.www.client.views.military.formbrigades.FormBrigadesView;
import com.eaw1805.www.client.views.military.merge.MergeBattalions;
import com.eaw1805.www.client.widgets.Disposable;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.List;

public class FormFederationsView
        extends DraggablePanel
        implements RegionConstants, ArmyConstants, StyleConstants {

    private final static String NO_ARMIES = "No armies here";
    private final ImageButton formCorpsImg, formArmiesImg, formBrigadesImg, mergeBattalionsImg;
    private final ImageButton europeImg, africaImg, caribImg, indiaImg;
    private int regionId = EUROPE, x = 0, y = 0, type = CORPS;
    private final AbsolutePanel controlsPanel;
    private final ImageButton leftImg;
    private final ImageButton rightImg;
    private final Label lblPosition;
    private List<SectorDTO> posWithArmies;

    public FormFederationsView() {
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
        this.europeImg.setSize(SIZE_31PX, SIZE_31PX);
        this.add(this.europeImg, 505, 60);

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
        this.africaImg.setSize(SIZE_31PX, SIZE_31PX);
        this.add(this.africaImg, 555, 60);

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
        this.caribImg.setSize(SIZE_31PX, SIZE_31PX);
        this.add(this.caribImg, 605, 60);

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
        this.indiaImg.setSize(SIZE_31PX, SIZE_31PX);
        this.add(this.indiaImg, 655, 60);

        this.formCorpsImg = new ImageButton("http://static.eaw1805.com/images/panels/formFederations/buttons/ButCorpsOn.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectOrgType(formCorpsImg.getId());
            }
        }).addToElement(formCorpsImg.getElement()).register();

        this.formCorpsImg.setStyleName(CLASS_POINTER);
        this.formCorpsImg.setId(CORPS);
        this.formCorpsImg.setSize(SIZE_31PX, SIZE_140PX);
        this.add(this.formCorpsImg, 8, 60);

        this.formArmiesImg = new ImageButton("http://static.eaw1805.com/images/panels/formFederations/buttons/ButArmiesOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectOrgType(formArmiesImg.getId());
            }
        }).addToElement(formArmiesImg.getElement()).register();

        this.formArmiesImg.setStyleName(CLASS_POINTER);
        this.formArmiesImg.setId(ARMY);
        this.formArmiesImg.setSize(SIZE_30PX, SIZE_140PX);
        this.add(this.formArmiesImg, 8, 205);

        this.formBrigadesImg = new ImageButton("http://static.eaw1805.com/images/panels/formFederations/buttons/ButBrigadesOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectOrgType(formBrigadesImg.getId());
            }
        }).addToElement(formBrigadesImg.getElement()).register();

        this.formBrigadesImg.setStyleName(CLASS_POINTER);
        this.formBrigadesImg.setId(BRIGADE);
        this.formBrigadesImg.setSize(SIZE_30PX, SIZE_140PX);
        this.add(this.formBrigadesImg, 8, 350);

        this.mergeBattalionsImg = new ImageButton("http://static.eaw1805.com/images/panels/formFederations/buttons/ButMergeOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                selectOrgType(mergeBattalionsImg.getId());
            }
        }).addToElement(mergeBattalionsImg.getElement()).register();

        this.mergeBattalionsImg.setStyleName(CLASS_POINTER);
        this.mergeBattalionsImg.setId(BATTALION);
        this.mergeBattalionsImg.setSize(SIZE_30PX, SIZE_140PX);
        this.add(this.mergeBattalionsImg, 8, 500);

        this.controlsPanel = new AbsolutePanel();
        this.controlsPanel.setSize("1134px", "544px");
        this.add(this.controlsPanel, 50, 106);

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
        this.leftImg.setSize(SIZE_31PX, SIZE_31PX);
        this.add(this.leftImg, 10, 9);

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
        this.rightImg.setSize(SIZE_31PX, SIZE_31PX);
        this.add(this.rightImg, 1105, 7);

        this.lblPosition = new Label("");
        this.lblPosition.setStyleName("clearFontMedLarge whiteText");
        this.add(this.lblPosition, 50, 10);

        // Add the close and accept window button
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName(CLASS_POINTER);
        imgX.setTitle("Close");
        final FormFederationsView myself = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().getOptionsMenu().getRelImage().deselect();
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();
        imgX.setSize("36px", "36px");
        this.add(imgX, 1150, 7);
    }

    private void selectView(final int regionId) {
        if (this.regionId == regionId) {
            final ImageButton img;
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
                default:
                    img = caribImg;
                    break;
            }
            selectRegionViewImg(img);
        } else {
            this.regionId = regionId;
            final ImageButton img;
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
                default:
                    img = caribImg;
                    break;

            }
            posWithArmies = ArmyStore.getInstance().getRegionSectorsWithArmies(regionId);
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
        this.lblPosition.setText(RegionStore.getInstance().getRegionSectorsByRegionId(regionId)[x][y].positionToString()
                + " - Form Federations");

        posWithArmies = ArmyStore.getInstance().getRegionSectorsWithArmies(
                regionId);
        if (!ArmyStore.getInstance().getRegionSectorsWithArmies(EUROPE).isEmpty()) {
            this.europeImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png");
            this.europeImg.setTitle("Select european theater");
        } else {
            this.europeImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeNA.png");
            this.europeImg.setTitle(NO_ARMIES);
        }
        if (!ArmyStore.getInstance().getRegionSectorsWithArmies(CARIBBEAN).isEmpty()) {
            this.caribImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png");
            this.caribImg.setTitle("Select carribean theater");
        } else {
            this.caribImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaNA.png");
            this.caribImg.setTitle(NO_ARMIES);
        }
        if (!ArmyStore.getInstance().getRegionSectorsWithArmies(INDIES).isEmpty()) {
            this.indiaImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png");
            this.indiaImg.setTitle("Select indies theater");
        } else {
            this.indiaImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaNA.png");
            this.indiaImg.setTitle(NO_ARMIES);
        }
        if (!ArmyStore.getInstance().getRegionSectorsWithArmies(AFRICA).isEmpty()) {
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

    private void selectOrgType(int typeId) {
        ImageButton img = null;
        this.type = typeId;
        final SectorDTO sector = RegionStore.getInstance().getRegionSectorsByRegionId(this.regionId)[this.x][this.y];
        if (controlsPanel.getWidgetCount() == 1) {
            if (controlsPanel.getWidget(0) instanceof Disposable) {
                ((Disposable) controlsPanel.getWidget(0)).removeGWTHandlers();
            }
        }
        controlsPanel.clear();
        switch (typeId) {
            case BRIGADE:
                img = formBrigadesImg;
                controlsPanel.add(new FormBrigadesView(sector));
                break;
            case CORPS:
                img = formCorpsImg;
                controlsPanel.add(new FormCorpView(ArmyStore.getInstance().getArmiesBySector(this.regionId, this.x, this.y, true), sector));
                break;
            case ARMY:
                img = formArmiesImg;
                controlsPanel.add(new FormArmyView(ArmyStore.getInstance().getArmiesBySector(this.regionId, this.x, this.y, true), sector));
                break;
            case BATTALION:
                img = mergeBattalionsImg;
                controlsPanel.add(new MergeBattalions(ArmyStore.getInstance().getArmiesBySector(this.regionId, this.x, this.y, true)));
                break;
            default:
                break;
        }
        selectCargoTypeViewImg(img);
    }

    private void selectCargoTypeViewImg(final ImageButton img) {
        this.formBrigadesImg.deselect();
        this.formBrigadesImg.getUrl().replace("On", OFF);
        this.formCorpsImg.deselect();
        this.formCorpsImg.getUrl().replace("On", OFF);
        this.formArmiesImg.deselect();
        this.formArmiesImg.getUrl().replace("On", OFF);
        this.mergeBattalionsImg.deselect();
        this.mergeBattalionsImg.getUrl().replace("On", OFF);
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }
}
