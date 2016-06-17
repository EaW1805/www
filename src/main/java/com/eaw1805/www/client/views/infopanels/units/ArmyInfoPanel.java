package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
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
import com.eaw1805.www.client.views.popups.ArmiesViewerPopup;
import com.eaw1805.www.client.views.popups.BattalionViewerPopup;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CommandersListPopup;
import com.eaw1805.www.client.views.popups.CorpsViewerPopup;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
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
import java.util.HashMap;
import java.util.List;


public class ArmyInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, SelectableWidget<ArmyDTO>, StyleConstants {

    private final static String ARMY_BATS = "Army Battalions";

    private final ClickAbsolutePanel mainPanel = new ClickAbsolutePanel();
    private ArmyDTO army = new ArmyDTO();
    private final Label lblXCorps, lblYBrigades, lblZBattalions, lblInfantryno, lblCavalryno, lblArtilleryno, lblArmyName, lblXArmies;
    private ImageButton viewImg;
    private ArmyUnitInfoDTO armyInf;
    private final Label lblMps;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    private ImageButton dismissImg;
    private final UnitChangedHandler unitChangedHandler;

    public ArmyInfoPanel(final ArmyDTO army) {
        setArmy(army);
        int leftFactor = 0;
        if (army.getArmyId() == 0) {
            leftFactor = 80;
        }

        this.setStyleName("armyInfoPanel");
        this.setSize("366px", "90px");
        mainPanel.setStyleName("clickArmyPanel");
        this.add(mainPanel);
        mainPanel.setSize("363px", "87px");

        if (getArmy().getArmyId() == 0) {
            lblArmyName = new Label();
        } else {
            lblArmyName = new RenamingLabel("Army Name", ARMY, getArmy().getArmyId());
        }
        lblArmyName.setStyleName("clearFontMiniTitle");
        lblArmyName.setStyleName("editName", true);
        mainPanel.add(lblArmyName, 90 - leftFactor, 3);
        lblArmyName.setSize("182px", "24px");


        lblXArmies = new Label("x Armies");
        lblXArmies.setStyleName(CLASS_CLEARFONTSMALL);

        if (army.getArmyId() == 0) {
            final Image armyImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/army.png");
            armyImg.setTitle("Armies");
            mainPanel.add(armyImg, 90 - leftFactor, 11);
            armyImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    new ArmiesViewerPopup(ArmyStore.getInstance().getArmiesByRegion(army.getRegionId()), "Armies", false).open();
                }
            });
            mainPanel.add(lblXArmies, 118 - leftFactor, 11);
        }


        final Image corpImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png");
        corpImg.setTitle("Corps");
        mainPanel.add(corpImg, 90 - leftFactor, 28);
        corpImg.setSize("", SIZE_15PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (army.getArmyId() == 0) {
                    final List<CorpDTO> allCorps = new ArrayList<CorpDTO>();
                    allCorps.addAll(army.getCorps().values());
                    for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                        if (otherArmy.getArmyId() != 0) {
                            allCorps.addAll(otherArmy.getCorps().values());
                        }
                    }
                    new CorpsViewerPopup(allCorps, "Region Corps", false).open();
                } else {
                    new CorpsViewerPopup(new ArrayList<CorpDTO>(army.getCorps().values()), "Corps", false).open();
                }
            }
        }).addToElement(corpImg.getElement()).register();

        lblXCorps = new Label("x Corps");
        lblXCorps.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblXCorps, 118 - leftFactor, 28);


        final Image brigImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
        brigImg.setTitle("Brigades");
        mainPanel.add(brigImg, 90 - leftFactor, 45);
        brigImg.setSize("", SIZE_15PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (army.getArmyId() == 0) {
                    List<BrigadeDTO> allBrigades = new ArrayList<BrigadeDTO>();
                    allBrigades.addAll(ArmyStore.getInstance().getArmyBrigadesList(army));

                    for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                        if (otherArmy.getArmyId() != 0) {
                            allBrigades.addAll(ArmyStore.getInstance().getArmyBrigadesList(otherArmy));
                        }
                    }
                    new BrigadesViewerPopup(allBrigades, "Region Brigades").open();
                } else {
                    new BrigadesViewerPopup(ArmyStore.getInstance().getArmyBrigadesList(army), "Brigades").open();
                }

            }
        }).addToElement(brigImg.getElement()).register();

        lblYBrigades = new Label("y Brigades");
        lblYBrigades.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblYBrigades, 118 - leftFactor, 45);


        final Image batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
        batImg.setTitle("Batttalions");
        mainPanel.add(batImg, 90 - leftFactor, 62);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (army.getArmyId() == 0) {
                    List<BattalionDTO> allBattalions = new ArrayList<BattalionDTO>();
                    allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, true, true, true));

                    for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                        if (otherArmy.getArmyId() != 0) {
                            allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, true, true, true));
                        }
                    }
                    new BattalionViewerPopup(allBattalions, ARMY_BATS).open();
                } else {
                    new BattalionViewerPopup(
                            ArmyStore.getInstance().getArmyBattalions(army, true, true, true), ARMY_BATS).open();
                }

            }
        }).addToElement(batImg.getElement()).register();

        lblZBattalions = new Label("z Battalions");
        lblZBattalions.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblZBattalions, 118 - leftFactor, 62);
        lblZBattalions.setSize("84px", SIZE_15PX);


        //battalion types counters
        final Image infantryImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
        infantryImg.setTitle("Infantry");
        mainPanel.add(infantryImg, 160 - leftFactor, 28);
        infantryImg.setSize("25px", SIZE_15PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (army.getArmyId() == 0) {
                    List<BattalionDTO> allBattalions = new ArrayList<BattalionDTO>();
                    allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, true, false, false));

                    for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                        if (otherArmy.getArmyId() != 0) {
                            allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, true, false, false));
                        }
                    }
                    new BattalionViewerPopup(allBattalions, ARMY_BATS).open();
                } else {
                    new BattalionViewerPopup(
                            ArmyStore.getInstance().getArmyBattalions(army, true, false, false), ARMY_BATS).open();
                }

            }
        }).addToElement(infantryImg.getElement()).register();

        lblInfantryno = new Label("InfantryNo");
        lblInfantryno.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblInfantryno, 188 - leftFactor, 28);

        final Image cavalryImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
        cavalryImg.setTitle("Cavalry");
        mainPanel.add(cavalryImg, 160 - leftFactor, 46);
        cavalryImg.setSize("25px", SIZE_15PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (army.getArmyId() == 0) {
                    List<BattalionDTO> allBattalions = new ArrayList<BattalionDTO>();
                    allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, false, true, false));

                    for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                        if (otherArmy.getArmyId() != 0) {
                            allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, false, true, false));
                        }
                    }
                    new BattalionViewerPopup(allBattalions, ARMY_BATS).open();
                } else {
                    new BattalionViewerPopup(
                            ArmyStore.getInstance().getArmyBattalions(army, false, true, false), ARMY_BATS).open();
                }

            }
        }).addToElement(cavalryImg.getElement()).register();

        lblCavalryno = new Label("CavalryNo");
        lblCavalryno.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblCavalryno, 188 - leftFactor, 46);

        final Image artilleryImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
        artilleryImg.setTitle("Artillery");
        mainPanel.add(artilleryImg, 160 - leftFactor, 64);
        artilleryImg.setSize("25px", SIZE_15PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (army.getArmyId() == 0) {
                    List<BattalionDTO> allBattalions = new ArrayList<BattalionDTO>();
                    allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, false, false, true));

                    for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                        if (otherArmy.getArmyId() != 0) {
                            allBattalions.addAll(ArmyStore.getInstance().getArmyBattalions(army, false, false, true));
                        }
                    }
                    new BattalionViewerPopup(allBattalions, ARMY_BATS).open();
                } else {
                    new BattalionViewerPopup(
                            ArmyStore.getInstance().getArmyBattalions(army, false, false, true), ARMY_BATS).open();
                }

            }
        }).addToElement(artilleryImg.getElement()).register();

        lblArtilleryno = new Label("ArtilleryNo");
        lblArtilleryno.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblArtilleryno, 188 - leftFactor, 64);

        final ImageButton moveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
        moveImg.setSize(SIZE_20PX, SIZE_20PX);
        moveImg.setTitle("Click here to move the army");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                MapStore.getInstance().getMapsView().goToPosition(army);
                MapStore.getInstance().getMapsView().addFigureOnMap(ARMY, army.getArmyId(),
                        army, MiscCalculators.getArmyMps(army), PowerCalculators.getPowerByBrigsAndBatts(armyInf.getBrigades(), armyInf.getBattalions()),
                        ConquerCalculators.getArmyMaxConquers(army), ConquerCalculators.getArmyMaxNeutralConquers(army));
            }
        }).addToElement(moveImg.getElement()).register();

        lblMps = new Label(MiscCalculators.getArmyMps(army) + " MPs");
        if (GameStore.getInstance().getNationId() == ArmyStore.getInstance().getArmyNation(army)) {
            if (army.getArmyId() != 0) {
                if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                    mainPanel.add(moveImg, 315, 63);
                }

                final Label label = new Label(army.positionToString());
                label.setTitle("Armys position.");
                label.setStyleName(CLASS_CLEARFONTSMALL);
                mainPanel.add(label, 315, 3);


                lblMps.setTitle("Movement points");
                lblMps.setStyleName(CLASS_CLEARFONTSMALL);
                mainPanel.add(lblMps, 315, 20);


                viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
                viewImg.setTitle("Go to army position");
                if (GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                    mainPanel.add(viewImg, 315, 63);
                } else {
                    mainPanel.add(viewImg, 338, 63);
                }
                viewImg.setSize(SIZE_20PX, SIZE_20PX);
                viewImg.setStyleName("pointer");
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        MapStore.getInstance().getMapsView().goToPosition(army);
                        viewImg.deselect();
                    }
                }).addToElement(viewImg.getElement()).register();

                if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                    final ImageButton assignLeaderImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAssignLeaderOff.png");
                    assignLeaderImg.setSize(SIZE_20PX, SIZE_20PX);
                    assignLeaderImg.setTitle("Assign leader");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            final CommandersListPopup cmPopup = new CommandersListPopup(
                                    CommanderStore.getInstance().getCommandersBySectorAndStartingPosition(
                                            RegionStore.getInstance().getSectorByPosition(getArmy()), true, true),
                                    ArmyInfoPanel.this, 0);
                            cmPopup.setPopupPosition(event.getNativeEvent()
                                    .getClientX(), event.getNativeEvent().getClientY() - 500);
                            cmPopup.show();
                        }
                    }).addToElement(assignLeaderImg.getElement()).register();
                    mainPanel.add(assignLeaderImg, 292, 63);

                    dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
                    dismissImg.setSize(SIZE_20PX, SIZE_20PX);
                    dismissImg.setTitle("Dismiss leader");
                    (new DelEventHandlerAbstract() {

                        @Override
                        public void execute(final MouseEvent event) {
                            if (army.getCommander() != null
                                    && army.getCommander().getId() > 0) {
                                CommanderStore.getInstance().commanderLeaveFederation(army.getCommander().getId());
                            }
                        }
                    }).addToElement(dismissImg.getElement()).register();
                }
            }
        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    mainPanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };
            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" +
                    ArmyStore.getInstance().getArmyNation(army) + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(ArmyStore.getInstance().getArmyNation(army)) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            mainPanel.add(relationPanel, 246, 63);
        }
        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if ((event.getInfoType() == ARMY && event.getInfoId() == army.getArmyId()) ||
                        (event.getInfoType() == CORPS && army.getCorps().containsKey(event.getInfoId())) ||
                        (army.getArmyId() == 0)) {
                    setUpLabels();
                } else if (event.getInfoType() == COMMANDER && army.getCommander() != null) {
                    setUpLabels();
                }
            }
        };


        boolean notSupplied = false;
        for (CorpDTO corp : army.getCorps().values()) {
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
                break;
            }
        }
        if (notSupplied) {
            final Image notSupplyImg = new Image("http://static.eaw1805.com/images/buttons/OutOfSupply32.png");
            notSupplyImg.setSize("", "19px");
            mainPanel.add(notSupplyImg, 286, 3);
            notSupplyImg.setTitle("Not supplied");
        }

        setUpLabels();
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(ARMY, army.getArmyId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    private void setUpLabels() {
        if (army.getArmyId() == 0) {
            if (ArmyStore.getInstance().getArmiesByRegion(army.getRegionId()).size() > 0
                    && ArmyStore.getInstance().getArmiesByRegion(army.getRegionId()).get(0).getArmyId() == 0) {
                army = ArmyStore.getInstance().getArmiesByRegion(army.getRegionId()).get(0);
            } else {
                final int regionId = army.getRegionId();
                army = new ArmyDTO();
                army.setRegionId(regionId);
                army.setCorps(new HashMap<Integer, CorpDTO>());
                army.setArmyId(0);
            }
        }

        armyInf = MiscCalculators.getArmyInfo(army);
        if (army.getArmyId() == 0) {
            for (ArmyDTO otherArmy : ArmyStore.getInstance().getArmiesByRegion(army.getRegionId())) {
                if (otherArmy.getArmyId() != 0) {
                    ArmyUnitInfoDTO info = MiscCalculators.getArmyInfo(otherArmy);
                    armyInf.setArtillery(armyInf.getArtillery() + info.getArtillery());
                    armyInf.setBattalions(armyInf.getBattalions() + info.getBattalions());
                    armyInf.setBrigades(armyInf.getBrigades() + info.getBrigades());
                    armyInf.setCavalry(armyInf.getCavalry() + info.getCavalry());
                    armyInf.setCorps(armyInf.getCorps() + info.getCorps());
                    armyInf.setInfantry(armyInf.getInfantry() + info.getInfantry());
                }
            }
        }

        if (!getArmy().getCorps().isEmpty() && getArmy().getCorps().values().iterator().next().getArmyId() == 0) {
            getLblArmyName().setText("");

        } else if (getArmy() != null && getArmy().getName() != null && !getArmy().getName().equals("")) {
            getLblArmyName().setText(getArmy().getName());

        } else {
            getLblArmyName().setText("Click to set name");
        }

        if (army.getArmyId() == 0) {
            final List<ArmyDTO> armies = ArmyStore.getInstance().getArmiesByRegion(army.getRegionId());
            if (armies.size() - 1 >= 0) {
                lblXArmies.setText(numberFormat.format(armies.size() - 1));
            } else {
                lblXArmies.setText(numberFormat.format(0));
            }

            if (armyInf.getCorps() - 1 >= 0) {
                getLblXCorps().setText(numberFormat.format(armyInf.getCorps() - 1));

            } else {
                getLblXCorps().setText(numberFormat.format(0));
            }

        } else {
            getLblXCorps().setText(numberFormat.format(armyInf.getCorps()));
        }

        getLblYBrigades().setText(numberFormat.format(armyInf.getBrigades()));
        getLblZBattalions().setText(numberFormat.format(armyInf.getBattalions()));
        getLblInfantryno().setText(numberFormat.format(armyInf.getInfantry()));
        getLblCavalryno().setText(numberFormat.format(armyInf.getCavalry()));
        getLblArtilleryno().setText(numberFormat.format(armyInf.getArtillery()));

        final Image commanderImg;
        if (army.getCommander() != null && army.getCommander().getId() != 0) {
            final int imageId = army.getCommander().getIntId();
            commanderImg = new Image("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + army.getCommander().getNationId() + "/" + imageId + ".jpg");
            new ToolTipPanel(commanderImg) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(army.getCommander()));
                }
            };
        } else {
            commanderImg = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
        }
        if (army.getArmyId() != 0) {
            mainPanel.add(commanderImg, 3, 3);
        }
        commanderImg.setSize("", "82px");
        if (GameStore.getInstance().getNationId() == ArmyStore.getInstance().getArmyNation(army)) {
            lblMps.setText(MiscCalculators.getArmyMps(army) + " MPs");
        }
        if (army.getCommander() != null
                && army.getCommander().getId() > 0
                && dismissImg != null) {
            mainPanel.add(dismissImg, 269, 63);
        } else if (dismissImg != null
                && dismissImg.isAttached()) {
            mainPanel.remove(dismissImg);
        }

    }

    /**
     * Method that adds the selected commander to the corp
     *
     * @param selCommander
     */
    public void setCommander(final CommanderDTO selCommander) {
        CommanderStore.getInstance().addCommanderToArmy(getArmy().getArmyId(), selCommander.getId(), false);
    }

    public final void setArmy(final ArmyDTO army) {
        this.army = army;
    }

    public final ArmyDTO getArmy() {
        return army;
    }

    public final Label getLblXCorps() {
        return lblXCorps;
    }

    public final Label getLblYBrigades() {
        return lblYBrigades;
    }

    public final Label getLblZBattalions() {
        return lblZBattalions;
    }

    public final Label getLblInfantryno() {
        return lblInfantryno;
    }

    public final Label getLblCavalryno() {
        return lblCavalryno;
    }

    public final Label getLblArtilleryno() {
        return lblArtilleryno;
    }

    public final Label getLblArmyName() {
        return lblArmyName;
    }

    /**
     * This will be used in lists where armies, corps and brigades
     * can be selected and we need to determine who is selected.
     *
     * @return The army.
     */
    public ArmyDTO getValue() {
        return getArmy();
    }

    /**
     * This will be used in lists where armies, corps and brigades
     * can be selected and we need to determine who is selected.
     *
     * @return The ARMY type identifier.
     */
    public int getIdentifier() {
        return ARMY;
    }

    public Widget getWidget() {
        return this;
    }

    public void setSelected(final boolean selected) {
        if (selected) {
            setStyleName("infoPanelSelected");
        } else {
            try {
                setStyleName("armyInfoPanel");
            } catch (Exception ignore) {
            }
        }
    }

    public void onEnter() {
        // do nothing
    }
}
