package com.eaw1805.www.client.views.military.deployment;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

public class DeployTroopsView
        extends DraggablePanel
        implements ArmyConstants, RegionConstants {

    private int regionId = EUROPE;
    private ImageButton europeImg;
    private ImageButton africaImg;
    private ImageButton caribImg;
    private ImageButton indiaImg;
    private DoDeployPanel deployView;
    private StyledCheckBox firstPhaseBox, secondPhaseBox;
    private ImageButton fleetSelectorImg, shipSelectorImg, btSelectorImg;
    private ImageButton brigSelectorImg, commSelectorImg, spySelectorImg;

    public DeployTroopsView(final int regionId, final int callerType, final int callerId) {
        this.setSize("1218px", "680px");
        this.setStyleName("deployTroopsPanel");
        // Initialize the passed on variables
        this.regionId = regionId;
        final Label lblPanelTitle = new Label("Deploy troops panel");
        lblPanelTitle.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblPanelTitle.setStyleName("clearFont-large whiteText");
        this.add(lblPanelTitle, 0, 22);
        lblPanelTitle.setSize("100%", "30px");

        deployView = new DoDeployPanel(DeployTroopsView.this.regionId, BAGGAGETRAIN, BRIGADE, this);
        this.add(deployView, 63, 110);

        this.europeImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOff.png");
        this.europeImg.setId(EUROPE);
        this.europeImg.setTitle("Select european theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectView(EUROPE);
            }
        }).addToElement(europeImg.getElement()).register();

        this.europeImg.setStyleName("pointer");
        this.add(this.europeImg, 512, 70);
        this.europeImg.setSize("31px", "31px");

        this.africaImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png");
        this.africaImg.setId(AFRICA);
        this.africaImg.setTitle("Select african theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectView(AFRICA);
            }
        }).addToElement(africaImg.getElement()).register();

        this.africaImg.setStyleName("pointer");
        this.add(this.africaImg, 570, 70);
        this.africaImg.setSize("31px", "31px");

        this.caribImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png");
        this.caribImg.setId(CARIBBEAN);
        this.caribImg.setTitle("Select carribean theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectView(CARIBBEAN);
            }
        }).addToElement(caribImg.getElement()).register();

        this.caribImg.setStyleName("pointer");
        this.add(this.caribImg, 628, 70);
        this.caribImg.setSize("31px", "31px");

        this.indiaImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png");
        this.indiaImg.setId(INDIES);
        this.indiaImg.setTitle("Select india theater");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectView(INDIES);
            }
        }).addToElement(indiaImg.getElement()).register();

        this.indiaImg.setStyleName("pointer");
        this.add(this.indiaImg, 685, 70);
        this.indiaImg.setSize("31px", "31px");

        this.btSelectorImg = new ImageButton("http://static.eaw1805.com/images/panels/boarding/buttons/ButBaggageTrainsOn.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                deployView.populateTransportUnitsPanel(DeployTroopsView.this.regionId, BAGGAGETRAIN);
            }
        }).addToElement(btSelectorImg.getElement()).register();

        this.btSelectorImg.setStyleName("pointer");
        this.add(this.btSelectorImg, 1147, 100);
        this.btSelectorImg.setSize("54px", "171px");

        this.fleetSelectorImg = new ImageButton("http://static.eaw1805.com/images/panels/boarding/buttons/ButFleetsOn.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                deployView.populateTransportUnitsPanel(DeployTroopsView.this.regionId, FLEET);
            }
        }).addToElement(fleetSelectorImg.getElement()).register();

        this.fleetSelectorImg.setStyleName("pointer");
        this.add(this.fleetSelectorImg, 1147, 293);
        this.fleetSelectorImg.setSize("54px", "171px");

        this.shipSelectorImg = new ImageButton("http://static.eaw1805.com/images/panels/boarding/buttons/ButShipsOn.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                deployView.populateTransportUnitsPanel(DeployTroopsView.this.regionId, SHIP);
            }
        }).addToElement(shipSelectorImg.getElement()).register();

        this.shipSelectorImg.setStyleName("pointer");
        this.add(this.shipSelectorImg, 1147, 486);
        this.shipSelectorImg.setSize("54px", "171px");
        final TransportUnitDTO transport = TransportStore.getInstance().getTransportUnitById(callerType, callerId);
        firstPhaseBox = new StyledCheckBox("First load phase", !MovementStore.getInstance().hasMovedThisTurn(callerType, callerId)
                                && !AlliedUnitsStore.getInstance().hasUnitMoved(callerType, callerId, transport.getRegionId(), transport.getNationId()), false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!firstPhaseBox.isEnabled()) {
                    firstPhaseBox.setChecked(false);
                    secondPhaseBox.setChecked(true);
                    deployView.setTradePhase(2, transport);
                } else {
                    if (firstPhaseBox.isChecked()) {
                        secondPhaseBox.setChecked(false);
                        deployView.setTradePhase(1, transport);
                    } else {
                        secondPhaseBox.setChecked(true);
                        deployView.setTradePhase(2, transport);
                    }
                }

            }
        }).addToElement(firstPhaseBox.getCheckBox().getElement()).register();

        this.firstPhaseBox.setText("First loading phase");
        this.add(this.firstPhaseBox, 120, 75);

        secondPhaseBox = new StyledCheckBox("Second load phase", MovementStore.getInstance().hasMovedThisTurn(callerType, callerId)
                            || AlliedUnitsStore.getInstance().hasUnitMoved(callerType, callerId, transport.getRegionId(), transport.getNationId()), false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!firstPhaseBox.isEnabled()) {
                    firstPhaseBox.setChecked(false);
                    secondPhaseBox.setChecked(true);
                    deployView.setTradePhase(2, transport);
                } else {
                    if (secondPhaseBox.isChecked()) {
                        firstPhaseBox.setChecked(false);
                        deployView.setTradePhase(2, transport);
                    } else {
                        firstPhaseBox.setChecked(true);
                        deployView.setTradePhase(1, transport);
                    }
                }
            }
        }).addToElement(secondPhaseBox.getCheckBox().getElement()).register();

        this.secondPhaseBox.setText("Second loading phase");
        this.add(this.secondPhaseBox, 921, 75);
        this.secondPhaseBox.setSize("203px", "21px");
        this.brigSelectorImg = new ImageButton("http://static.eaw1805.com/images/panels/boarding/buttons/ButBrigadesOn.png");
        this.brigSelectorImg.setSelected(true);
        this.add(this.brigSelectorImg, 20, 100);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectCargoType(BRIGADE);
            }
        }).addToElement(brigSelectorImg.getElement()).register();

        this.brigSelectorImg.setStyleName("pointer");
        this.brigSelectorImg.setSize("30px", "171px");

        this.commSelectorImg = new ImageButton("http://static.eaw1805.com/images/panels/boarding/buttons/ButCommandersOff.png");
        this.add(this.commSelectorImg, 20, 293);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectCargoType(COMMANDER);
            }
        }).addToElement(commSelectorImg.getElement()).register();

        this.commSelectorImg.setStyleName("pointer");
        this.commSelectorImg.setSize("30px", "171px");

        this.spySelectorImg = new ImageButton("http://static.eaw1805.com/images/panels/boarding/buttons/ButSpiesOff.png");
        this.add(this.spySelectorImg, 20, 486);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                selectCargoType(SPY);
            }
        }).addToElement(spySelectorImg.getElement()).register();

        this.spySelectorImg.setStyleName("pointer");
        this.spySelectorImg.setSize("30px", "171px");

        // Add the close and accept window button
        final DeployTroopsView myself = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close panel");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 1166, 17);
        imgX.setSize("36px", "36px");


        selectView(DeployTroopsView.this.regionId);
        if (callerType != 0) {
            selectTransportType(callerType);
            deployView.selectTransportUnit(callerId);
        }
    }

    private void selectView(final int regionId) {
        this.regionId = regionId;
        final ImageButton img;
        switch (DeployTroopsView.this.regionId) {
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
        selectTransportType(BAGGAGETRAIN);
        selectCargoType(BRIGADE);
    }

    private void selectRegionViewImg(final ImageButton img) {
        this.europeImg.deselect();
        this.europeImg.getUrl().replace("On", "Off");
        this.caribImg.deselect();
        this.caribImg.getUrl().replace("On", "Off");
        this.indiaImg.deselect();
        this.indiaImg.getUrl().replace("On", "Off");
        this.africaImg.deselect();
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }

    private void selectCargoType(final int typeId) {
        final ImageButton img;
        switch (typeId) {
            case BRIGADE:
                img = brigSelectorImg;
                break;

            case COMMANDER:
                img = commSelectorImg;
                break;

            case SPY:
            default:
                img = spySelectorImg;
                break;
        }
        deployView.populateCargoUnitsPanel(regionId, typeId);
        selectCargoTypeViewImg(img);
    }

    private void selectCargoTypeViewImg(final ImageButton img) {
        this.brigSelectorImg.deselect();
        this.brigSelectorImg.getUrl().replace("On", "Off");
        this.commSelectorImg.deselect();
        this.commSelectorImg.getUrl().replace("On", "Off");
        this.spySelectorImg.deselect();
        this.spySelectorImg.getUrl().replace("On", "Off");
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }

    private void selectTransportType(final int typeId) {
        final ImageButton img;
        switch (typeId) {
            case BAGGAGETRAIN:
                img = btSelectorImg;
                break;

            case SHIP:
                img = shipSelectorImg;
                break;

            case FLEET:
            default:
                img = fleetSelectorImg;
                break;
        }

        deployView.populateTransportUnitsPanel(regionId, typeId);
        selectTransportTypeViewImg(img);
    }

    private void selectTransportTypeViewImg(final ImageButton img) {
        this.btSelectorImg.deselect();
        this.btSelectorImg.getUrl().replace("On", "Off");
        this.shipSelectorImg.deselect();
        this.shipSelectorImg.getUrl().replace("On", "Off");
        this.fleetSelectorImg.deselect();
        this.fleetSelectorImg.getUrl().replace("On", "Off");
        img.setUrl(img.getUrl().replace("Off.png", "On.png"));
        img.setSelected(true);
    }

    public StyledCheckBox getPhaseBox1() {
        return firstPhaseBox;
    }

    public StyledCheckBox getPhaseBox2() {
        return secondPhaseBox;
    }
}
