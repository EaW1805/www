package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.mini.CommanderInfoMini;
import com.eaw1805.www.client.views.popups.BattalionViewerPopup;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CommandersListPopup;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.ConquerCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

import java.util.ArrayList;


public class CorpsInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, SelectableWidget<CorpDTO>, StyleConstants {

    private final ClickAbsolutePanel corpInfoPanel;
    private CorpDTO corp;
    private final Label lblXBrigades, lblYBattalions,
            lblInfantryNo, lblCavalryNo, lblArtilleryNo,
            lblCorpName, lblMps;
    private final Image corpImage;
    private final Label lblXy;
    private ArmyUnitInfoDTO corpInfo;
    private final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private final Image brigImg;
    private final Image batImg;
    private final Image infImage;
    private final Image cavImage;
    private final Image artImage;
    private ImageButton dismissImg;
    private final UnitChangedHandler unitChangedHandler;

    public CorpsInfoPanel(final CorpDTO corp, final boolean canMove) {
        setCorp(corp);
        int leftFactor = 0;
        if (corp.getCorpId() == 0) {
            leftFactor = 80;
        }


        corpInfo = MiscCalculators.getCorpInfo(corp);
        this.setStyleName("corpInfoPanel");

        this.setSize("366px", "90px");

        corpInfoPanel = new ClickAbsolutePanel();
        corpInfoPanel.setStyleName("clickArmyPanel");
        this.add(corpInfoPanel);
        corpInfoPanel.setSize("363px", "87px");

        corpImage = new Image();
        if (corp.getCorpId() != 0) {
            corpInfoPanel.add(corpImage, 3, 3);
        }
        corpImage.setSize("82px", "82px");

        if (getCorp().getCorpId() == 0) {
            lblCorpName = new Label("Corps Name");
        } else {
            lblCorpName = new RenamingLabel("Corps Name", CORPS, getCorp().getCorpId());
        }

        lblCorpName.setStyleName("clearFontMiniTitle");
        corpInfoPanel.add(lblCorpName, 90 - leftFactor, 3);


        brigImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigImg.setTitle("Brigades");
        corpInfoPanel.add(brigImg, 90 - leftFactor, 28);
        brigImg.setSize("", SIZE_15PX);

        lblXBrigades = new Label("X");
        lblXBrigades.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblXBrigades, 118 - leftFactor, 28);

        batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        batImg.setTitle("Batttalions");
        corpInfoPanel.add(batImg, 90 - leftFactor, 46);
        batImg.setSize("", SIZE_15PX);
        lblYBattalions = new Label("Y");
        lblYBattalions.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblYBattalions, 118 - leftFactor, 46);


        infImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        cavImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        artImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        corpInfoPanel.add(infImage, 160 - leftFactor, 28);
        corpInfoPanel.add(cavImage, 160 - leftFactor, 46);
        corpInfoPanel.add(artImage, 160 - leftFactor, 64);

        infImage.setSize("25px", SIZE_15PX);
        cavImage.setSize("25px", SIZE_15PX);
        artImage.setSize("25px", SIZE_15PX);
        infImage.setTitle("Infantry");
        cavImage.setTitle("Cavalry");
        artImage.setTitle("Artillery");

        lblInfantryNo = new Label("");
        lblInfantryNo.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblInfantryNo, 188 - leftFactor, 28);

        lblCavalryNo = new Label("");
        lblCavalryNo.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblCavalryNo, 188 - leftFactor, 46);

        lblArtilleryNo = new Label("");
        lblArtilleryNo.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblArtilleryNo, 188 - leftFactor, 64);
        lblMps = new Label(corpInfo.getMps() + " MPs");
        if (GameStore.getInstance().getNationId() == ArmyStore.getInstance().getCorpNation(corp)) {
            if (canMove) {
                final Image moveImg;
                if (getCorp().getArmyId() == 0) {
                    moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                    moveImg.setTitle("Click here to move the corps");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            event.stopPropagation();
                            MapStore.getInstance().getMapsView().goToPosition(getCorp());
                            MapStore.getInstance().getMapsView().addFigureOnMap(CORPS, getCorp().getCorpId(),
                                    getCorp(), corpInfo.getMps(), PowerCalculators.getPowerByBrigsAndBatts(getCorp().getBrigades().size(), corpInfo.getBattalions()),
                                    ConquerCalculators.getCorpsMaxConquers(corp), ConquerCalculators.getCorpsMaxNeutralConquers(corp));
                        }
                    }).addToElement(moveImg.getElement()).register();


                } else {
                    moveImg = new DualStateImage("http://static.eaw1805.com/images/buttons/moveNA.png");
                    moveImg.setTitle("Cannot move corp that is in a higher level of organization");
                }

                if (getCorp().getCorpId() != 0 && getCorp().getArmyId() == 0
                        && !GameStore.getInstance().isNationDead()
                        && !GameStore.getInstance().isGameEnded()) {
                    corpInfoPanel.add(moveImg, 315, 63);
                    moveImg.setSize(SIZE_20PX, SIZE_20PX);

                    final ImageButton assignLeaderImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAssignLeaderOff.png");
                    assignLeaderImg.setSize(SIZE_20PX, SIZE_20PX);
                    assignLeaderImg.setTitle("Assign leader");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            final CommandersListPopup cmPopup = new CommandersListPopup(
                                    CommanderStore.getInstance().getCommandersBySectorAndStartingPosition(
                                            RegionStore.getInstance().getSectorByPosition(getCorp()), true, true),
                                    CorpsInfoPanel.this, 0);
                            cmPopup.setPopupPosition(event.getNativeEvent()
                                    .getClientX(), event.getNativeEvent().getClientY() - 500);
                            cmPopup.show();
                        }
                    }).addToElement(assignLeaderImg.getElement()).register();
                    corpInfoPanel.add(assignLeaderImg, 292, 63);
                }
                if (getCorp().getCorpId() != 0) {
                    final ImageButton viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
                    viewImg.setStyleName("pointer");
                    viewImg.setTitle("Go to corps position");
                    corpInfoPanel.add(viewImg, 338, 63);
                    viewImg.setSize(SIZE_20PX, SIZE_20PX);
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            MapStore.getInstance().getMapsView().goToPosition(getCorp());
                        }
                    }).addToElement(viewImg.getElement()).register();
                }
                if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                    dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
                    dismissImg.setSize(SIZE_20PX, SIZE_20PX);
                    dismissImg.setTitle("Dismiss leader");
                    (new DelEventHandlerAbstract() {

                        @Override
                        public void execute(final MouseEvent event) {
                            if (corp.getCommander() != null
                                    && corp.getCommander().getId() > 0) {
                                CommanderStore.getInstance().commanderLeaveFederation(corp.getCommander().getId());
                            }
                        }
                    }).addToElement(dismissImg.getElement()).register();
                }
            }

            lblMps.setTitle("Movement points.");
            lblMps.setStyleName(CLASS_CLEARFONTSMALL);
            corpInfoPanel.add(lblMps, 315, 20);
            lblMps.setSize("150px", "18px");
        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    corpInfoPanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };
            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + ArmyStore.getInstance().getCorpNation(corp) + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(ArmyStore.getInstance().getCorpNation(corp)) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            corpInfoPanel.add(relationPanel, 246, 63);
        }
        lblXy = new Label(getCorp().positionToString());
        lblXy.setTitle("Corps position.");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        corpInfoPanel.add(lblXy, 315, 3);


        boolean notSupplied = false;

        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getNotSupplied()) {
                    notSupplied = true;
                }
            }
            if (notSupplied) {
                break;
            }
        }

        if (notSupplied) {
            final Image notSupplyImg = new Image("http://static.eaw1805.com/images/buttons/OutOfSupply32.png");
            notSupplyImg.setSize("", "19px");
            corpInfoPanel.add(notSupplyImg, 286, 3);
            notSupplyImg.setTitle("Not supplied");
        }

        setUpLabels();


        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == CORPS && event.getInfoId() == corp.getCorpId() || event.getInfoId() == 0) {
                    setUpLabels();

                }
            }
        };
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(CORPS, corp.getCorpId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    private void setUpLabels() {
        corpInfo = MiscCalculators.getCorpInfo(corp);
        if (!getCorp().getBrigades().isEmpty() && getCorp().getBrigades().values().iterator().next().getCorpId() == 0) {
            getLblCorpName().setText("Brigades without corps.");
        } else if (getCorp() != null && getCorp().getName() != null && !getCorp().getName().equals("")) {
            getLblCorpName().setText(getCorp().getName());
        } else {
            getLblCorpName().setText("Click to set name");
        }
        lblXy.setText(getCorp().positionToString());
//        lblMps = new Label(corpInfo.getMps() + " MPs");
        getLblXBrigades().setText(numberFormat.format(corpInfo.getBrigades()));
        getLblYBattalions().setText(numberFormat.format(corpInfo.getBattalions()));
        getLblInfantryNo().setText(numberFormat.format(corpInfo.getInfantry()));
        getLblCavalryNo().setText(numberFormat.format(corpInfo.getCavalry()));
        getLblArtilleryNo().setText(numberFormat.format(corpInfo.getArtillery()));


        if (corp.getCommander() == null || corp.getCommander().getNationId() == 0) {
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
        } else {
            final int imageId = corp.getCommander().getIntId();
            corpImage.setUrl("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + corp.getCommander().getNationId() + "/" + imageId + ".jpg");
            new ToolTipPanel(corpImage) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(corp.getCommander()));
                }
            };
        }
        if (GameStore.getInstance().getNationId() == ArmyStore.getInstance().getCorpNation(corp)) {
            lblMps.setText(corpInfo.getMps() + " MPs");
        }
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BrigadesViewerPopup(new ArrayList<BrigadeDTO>(corp.getBrigades().values()), "Brigades").open();
            }
        }).addToElement(brigImg.getElement()).registerOnce();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(ArmyStore.getInstance().getCropsBattalions(corp, true, true, true), "Battalions").open();
            }
        }).addToElement(batImg.getElement()).registerOnce();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(ArmyStore.getInstance().getCropsBattalions(corp, true, false, false), "Battalions").open();
            }
        }).addToElement(infImage.getElement()).registerOnce();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(ArmyStore.getInstance().getCropsBattalions(corp, false, true, false), "Battalions").open();
            }
        }).addToElement(cavImage.getElement()).registerOnce();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                new BattalionViewerPopup(ArmyStore.getInstance().getCropsBattalions(corp, false, false, true), "Battalions").open();
            }
        }).addToElement(artImage.getElement()).registerOnce();

        if (corp.getCommander() != null
                && corp.getCommander().getId() > 0
                && dismissImg != null) {
            corpInfoPanel.add(dismissImg, 269, 63);
        } else if (dismissImg != null
                && dismissImg.isAttached()) {
            corpInfoPanel.remove(dismissImg);
        }
    }

    /**
     * Method that adds the selected commander to the corp
     *
     * @param selCommander the commander to select.
     */
    public void setCommander(final CommanderDTO selCommander) {
        CommanderStore.getInstance().addCommanderToCorp(getCorp().getCorpId(), selCommander.getId(), false);
    }

    public final CorpDTO getCorp() {
        return corp;
    }

    public final void setCorp(final CorpDTO corp) {
        this.corp = corp;
    }

    public final Label getLblXBrigades() {
        return lblXBrigades;
    }

    public final Label getLblYBattalions() {
        return lblYBattalions;
    }

    public final Label getLblInfantryNo() {
        return lblInfantryNo;
    }

    public final Label getLblCavalryNo() {
        return lblCavalryNo;
    }

    public final Label getLblArtilleryNo() {
        return lblArtilleryNo;
    }

    public final Label getLblCorpName() {
        return lblCorpName;
    }

    public final CorpDTO getValue() {
        return getCorp();
    }

    public final int getIdentifier() {
        return CORPS;
    }

    public final Widget getWidget() {
        return this;
    }

    public final void setSelected(final boolean selected) {
        if (selected) {
            setStyleName("infoPanelSelected", true);
        } else {
            this.setStyleName("corpInfoPanel");
        }
    }

    public final void onEnter() {
        // do nothing
    }
}
