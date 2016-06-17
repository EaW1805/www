package com.eaw1805.www.client.views.military.formArmy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.CommanderInfoMini;
import com.eaw1805.www.client.views.infopanels.units.mini.CorpsInfoMini;
import com.eaw1805.www.client.views.popups.CommandersListPopup;
import com.eaw1805.www.client.views.popups.DisbandFederationPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

import java.util.ArrayList;
import java.util.List;

public class FormArmy extends AbsolutePanel implements ArmyConstants,
        OrderConstants {
    private AbsolutePanel armyHolder = new AbsolutePanel();
    private AbsolutePanel[] corpHlds = new AbsolutePanel[5], corps = new AbsolutePanel[5];
    private DropController[] dps = new AbsolutePositionDropController[5];
    private Image infImg, cavImg, artImg;
    private Label lblInf, lblCav, lblArt;
    private RenamingLabel lblName;
    private Image comImg;
    private ImageButton saveImg, xImg;
    boolean isNew = true;
    private ArmyDTO freeArmy;
    private CommanderDTO commander;
    private FormArmyView formArmyController;
    private PickupDragController dragController;
    private ImageButton dissmissImg;
    private List<CorpDTO> currCorps = new ArrayList<CorpDTO>();
    private ToolTipPanel commanderTooltip;
    private final UnitDestroyedHandler unitDestroyedHandler;

    public FormArmy(PickupDragController dragController, ArmyDTO army,
                    final FormArmyView formArmyController, final SectorDTO sector) {
        if (army != null) {
            isNew = true;
            freeArmy = army;
        }
        this.dragController = dragController;
        this.formArmyController = formArmyController;
        setSize("720px", "547px");
        armyHolder.setStyleName((String) null);
        armyHolder.setSize("720px", "433px");
        add(armyHolder, 0, 47);

        this.lblName = new RenamingLabel("Name:", 10000, 0);
        this.lblName.setStyleName("clearFontMedLarge whiteText");
        this.lblName
                .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add(this.lblName, 133, 17);
        this.lblName.setSize("437px", "24px");

        this.saveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        this.saveImg.setStyleName("pointer");
        add(this.saveImg, 635, 0);
        this.saveImg.setSize("36px", "36px");

        if (!isNew) {
            this.saveImg.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
        }
        if (army != null) {
            lblName.setText(army.getName());
            isNew = false;
            this.remove(saveImg);
        } else {
            lblName.setText("Army "
                    + ArmyStore.getInstance().getcArmies().values().size());
            isNew = true;
        }

        if (freeArmy == null || freeArmy.getCommander() == null
                || freeArmy.getCommander().getId() == 0) {
            comImg = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
            comImg.setTitle("assign commander");
            commanderTooltip = new ToolTipPanel(comImg) {
                public void generateTip() {
                    setTooltip(null);
                }
            };
        } else {
            comImg = new Image("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + freeArmy.getCommander().getNationId() + "/"
                    + freeArmy.getCommander().getIntId() + ".jpg");
            comImg.setTitle("");
            commanderTooltip = new ToolTipPanel(comImg) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(freeArmy.getCommander()));
                }
            };
        }
        final FormArmy naPanel = this;
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                CommandersListPopup commanderSelectionList = new CommandersListPopup(
                        CommanderStore.getInstance().getCommandersBySectorAndStartingPosition(
                                sector, true, true), (AbsolutePanel) naPanel, 1);
                commanderSelectionList.setPopupPosition(event.getNativeEvent()
                        .getClientX(), event.getNativeEvent().getClientY());
                commanderSelectionList.show();
            }
        }).addToElement(comImg.getElement()).register();

        this.comImg.setSize("44px", "44px");
        this.add(this.comImg, 0, 0);

        this.dissmissImg = new ImageButton(
                "http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
        if (freeArmy != null && freeArmy.getCommander() != null
                && freeArmy.getCommander().getId() != 0) {
            dissmissImg.setVisible(true);
        } else {
            dissmissImg.setVisible(false);
        }

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!dissmissImg.getUrl().endsWith("NA.png")) {
                    dissmissImg.deselect();
                    if (freeArmy != null && freeArmy.getCommander() != null) {
                        CommanderStore.getInstance().commanderLeaveFederation(
                                freeArmy.getCommander().getId());
                        comImg.setUrl("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
                    } else if (commander != null) {
                        commander = null;
                        comImg.setUrl("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
                    }
                    commanderTooltip.setTooltip(null);
                    dissmissImg.setVisible(false);
                    comImg.setTitle("assign commander");
                }
            }
        }).addToElement(dissmissImg.getElement()).register();

        dissmissImg.setTitle("Remove commander");
        this.dissmissImg.setStyleName("pointer");
        this.dissmissImg.setPixelSize(24, 24);
        this.add(this.dissmissImg, 50, 21);


        this.xImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        this.xImg.setStyleName("pointer");
        add(this.xImg, 684, 0);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (freeArmy != null) {
                    if (freeArmy.getCorps().values().size() == 0) {
                        ArmyStore.getInstance()
                                .deleteArmy(freeArmy.getArmyId());
                        formArmyController.createNewArmy();

                    } else {
                        DisbandFederationPopup disArmy = new DisbandFederationPopup(ARMY, freeArmy.getArmyId());
                        disArmy.center();
                    }
                }
                xImg.deselect();
            }
        }).addToElement(xImg.getElement()).register();

        this.xImg.setSize("36px", "36px");


        this.lblInf = new Label("Inf");
        this.lblInf.setStyleName("whiteText");
        add(this.lblInf, 287, 495);
        this.lblInf.setSize("36px", "15px");

        this.infImg = new Image("http://static.eaw1805.com/images/armies/dominant/infantry.png");
        add(this.infImg, 246, 486);
        this.infImg.setSize("36px", "24px");

        this.cavImg = new Image("http://static.eaw1805.com/images/armies/dominant/cavalry.png");
        add(this.cavImg, 328, 486);
        this.cavImg.setSize("36px", "24px");

        this.lblCav = new Label("Cav");
        this.lblCav.setStyleName("whiteText");
        add(this.lblCav, 369, 495);
        this.lblCav.setSize("36px", "15px");

        this.artImg = new Image("http://static.eaw1805.com/images/armies/dominant/artillery.png");
        add(this.artImg, 410, 486);
        this.artImg.setSize("36px", "24px");

        this.lblArt = new Label("Art");
        this.lblArt.setStyleName("whiteText");
        add(this.lblArt, 451, 495);
        this.lblArt.setSize("36px", "15px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (isNew) {
                    saveArmy();
                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "This corp is not new. Adding and removing brigades "
                            + "automatically changes their status", false);
                }
                saveImg.deselect();
            }
        }).addToElement(saveImg.getElement()).register();

        comImg.setStyleName("pointer");
        comImg.setStyleName("borderCommander", true);

        for (int i = 0; i < 5; i++) {
            this.corps[i] = new AbsolutePanel();
            this.corpHlds[i] = new AbsolutePanel();
            this.corpHlds[i].setStyleName("infoPanelHolder");
            this.corpHlds[i].setSize("180px", "99px");
            this.corps[i].setSize("170px", "89px");
            if (i == 0) {
                this.armyHolder.add(this.corpHlds[i], 37, 10);
            } else if (i == 1) {
                this.armyHolder.add(this.corpHlds[i], 505, 10);
            } else if (i == 2) {
                this.armyHolder.add(this.corpHlds[i], 270, 167);
            } else if (i == 3) {
                this.armyHolder.add(this.corpHlds[i], 37, 324);
            } else if (i == 4) {
                this.armyHolder.add(this.corpHlds[i], 505, 324);
            }
            corpHlds[i].add(corps[i], 5, 5);
            dps[i] = new AbsolutePositionDropController(corps[i]);
            dragController.registerDropController(dps[i]);

        }
        if (army != null) {
            int i = 0;
            for (CorpDTO corp : army.getCorps().values()) {
                final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, true);
                corpInfo.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(DoubleClickEvent event) {
                        if (freeArmy != null) {
                            if (freeArmy.getCorps().size() == 1) {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "By removing the last corps from the army, you are disbanding the army, continue?", true) {
                                    public void onAccept() {
                                        ArmyStore.getInstance().changeCorpArmy(corpInfo.getCorp().getCorpId(), freeArmy.getArmyId(), 0);
                                        formArmyController.reInitCorps();
                                        corpInfo.removeFromParent();
                                        setLabels();
                                    }

                                    public void onReject() {
                                        formArmyController.reInitCorps();
                                        corpInfo.removeFromParent();
                                        setLabels();
                                    }
                                };
                            } else {
                                ArmyStore.getInstance().changeCorpArmy(corpInfo.getCorp().getCorpId(), freeArmy.getArmyId(), 0);
                            }
                        } else {
                            removeCorpById(corpInfo.getCorp().getCorpId());
                        }
                        formArmyController.reInitCorps();
                        corpInfo.removeFromParent();
                        setLabels();
                    }
                }, DoubleClickEvent.getType());
                corps[i].add(corpInfo);
                dragController.makeDraggable(corpInfo,
                        corpInfo.getCorpInfoPanel());
                i++;
            }
        }
        unitDestroyedHandler = new UnitDestroyedHandler() {
            public void onUnitDestroyed(UnitDestroyedEvent event) {
                formArmyController.createNewArmy();

            }
        };
        UnitEventManager.addUnitDestroyedHandler(unitDestroyedHandler);
        setLabels();
    }

    public void removeGWTHandlers() {
        UnitEventManager.removeUnitDestroyedHandler(unitDestroyedHandler);
    }

    public void setLabels() {
        ArmyUnitInfoDTO armyInfo = new ArmyUnitInfoDTO();
        for (int i = 0; i < 5; i++) {
            AbsolutePanel container = (AbsolutePanel) ((AbsolutePanel) armyHolder.getWidget(i)).getWidget(0);
            if (container.getWidgetCount() > 0) {
                if (!container.getWidget(0).getClass()
                        .equals(SimplePanel.class)) {
                    armyInfo.addToInfo(MiscCalculators
                            .getCorpInfo(((CorpsInfoMini) container
                                    .getWidget(0)).getCorp()));
                }
            }
        }

        lblInf.setText(String.valueOf(armyInfo.getInfantry()));
        lblCav.setText(String.valueOf(armyInfo.getCavalry()));
        lblArt.setText(String.valueOf(armyInfo.getArtillery()));
    }

    private void saveArmy() {
        List<CorpDTO> armyCorps = new ArrayList<CorpDTO>();
        final StringBuffer armyStrBuf = new StringBuffer();
        if (!lblName.getText().trim().equals("")
                && !lblName.getText().isEmpty()) {
            for (int i = 0; i < 5; i++) {
                if (corps[i].getWidgetCount() > 0) {
                    armyCorps.add(((CorpsInfoMini) corps[i].getWidget(0))
                            .getCorp());
                    armyStrBuf.append(((CorpsInfoMini) corps[i].getWidget(0))
                            .getCorp().getCorpId());
                }

            }
            if (!armyCorps.isEmpty()) {
                int armyId = 0;
                if (armyStrBuf.length() > 8) {
                    armyId = Integer.parseInt(armyStrBuf.substring(0, 8));

                } else {
                    armyId = Integer.parseInt(armyStrBuf.toString());
                }
                if (isNew) {
                    String name = lblName.getText();
                    int[] ids = new int[2];
                    ids[0] = armyId;
                    ids[1] = 0;
                    if (OrderStore
                            .getInstance()
                            .addNewOrder(
                                    ORDER_B_ARMY,
                                    CostCalculators
                                            .getFormFederationCost(ORDER_B_ARMY),
                                    armyCorps.get(0).getRegionId(), name, ids,
                                    0, "") == 1) {
                        freeArmy = ArmyStore.getInstance().createArmy(armyId,
                                name, armyCorps.get(0).getX(),
                                armyCorps.get(0).getY(),
                                armyCorps.get(0).getRegionId());
                        isNew = false;
                        saveImg.removeFromParent();
                        //now update the description of the new order...
                        GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_B_ARMY, ids);
                    }
                }
                if (!isNew) {
                    for (CorpDTO corp : armyCorps) {
                        if (freeArmy != null) {
                            ArmyStore.getInstance().changeCorpArmy(
                                    corp.getCorpId(), 0, freeArmy.getArmyId());
                        }
                    }
                    if (getCommander() != null) {
                        CommanderStore.getInstance().addCommanderToArmy(
                                freeArmy.getArmyId(), getCommander().getId(), false);
                    }
                    formArmyController.createNewArmy();
                }
            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "This army has no corps. Add at least one before saving", false);
            }
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot save army. Enter a name first", false);
        }

    }

    // Function that set the selected commander for this corp
    public void setSelCommander(final CommanderDTO selCommander) {
        setCommander(selCommander);
        final int imageId = selCommander.getIntId();
        comImg.setUrl("http://static.eaw1805.com/img/commanders/s"
                + GameStore.getInstance().getScenarioId() + "/"
                + selCommander.getNationId() + "/"
                + imageId + ".jpg");
        if (commanderTooltip == null) {
            commanderTooltip = new ToolTipPanel(comImg) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(selCommander));
                }
            };

        } else {
            commanderTooltip.setTooltip(new CommanderInfoMini(selCommander));
        }
        if (!isNew) {
            CommanderStore.getInstance().addCommanderToArmy(
                    freeArmy.getArmyId(), selCommander.getId(), false);
        }
        dissmissImg.setVisible(true);
        comImg.setTitle("");
    }

    public void unregisterControllers() {
        for (int i = 0; i < 5; i++) {
            if (dps[i] != null) {
                dragController.unregisterDropController(dps[i]);
            }
        }
    }

    public boolean hasSpace() {
        for (int i = 0; i < 5; i++) {
            if (corps[i].getWidgetCount() == 0) {
                return true;
            }
        }
        return false;
    }

    public void addCorp(CorpDTO corp) {
        if (freeArmy != null) {
            ArmyStore.getInstance().changeCorpArmy(
                    corp.getCorpId(), 0, freeArmy.getArmyId());
        } else {
            currCorps.add(corp);
        }
        for (int i = 0; i < 5; i++) {
            if (corps[i].getWidgetCount() == 0) {
                final CorpsInfoMini corpInfoMini = new CorpsInfoMini(corp, true);
                corpInfoMini.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(DoubleClickEvent event) {
                        if (freeArmy != null) {
                            ArmyStore.getInstance().changeCorpArmy(corpInfoMini.getCorp().getCorpId(), freeArmy.getArmyId(), 0);
                        } else {
                            removeCorpById(corpInfoMini.getCorp().getCorpId());
                        }
                        formArmyController.reInitCorps();
                        corpInfoMini.removeFromParent();
                        setLabels();

                    }
                }, DoubleClickEvent.getType());
                corps[i].add(corpInfoMini);
                dragController.makeDraggable(corpInfoMini, corpInfoMini.getCorpInfoPanel());
                break;
            }
        }
        setLabels();

    }

    /**
     * Method that removes a corp from a
     * non initialized army
     *
     * @param corpId the id of the corp
     * @return true if the corp was found and removed
     */
    public boolean removeCorpById(int corpId) {
        for (int i = 0; i < currCorps.size(); i++) {
            if (currCorps.get(i).getCorpId() == corpId) {
                currCorps.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the unformed army already has this corp
     *
     * @param corp the corp
     * @return true if the corp is present
     */
    public boolean hasCorp(CorpDTO corp) {
        for (CorpDTO currCorp : currCorps) {
            if (currCorp.getCorpId() == corp.getCorpId()) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param commander the commander to set
     */
    public void setCommander(CommanderDTO commander) {
        this.commander = commander;
    }

    /**
     * @return the commander
     */
    public CommanderDTO getCommander() {
        return commander;
    }

    /**
     * @return the freeCorp
     */
    public ArmyDTO getFreeArmy() {
        return freeArmy;
    }

    /**
     * @param freeArmy the freeCorp to set
     */
    public void setFreeArmy(ArmyDTO freeArmy) {
        this.freeArmy = freeArmy;
    }

    public List<CorpDTO> getCorps() {
        return currCorps;
    }

    /**
     * Method that checks the brigades and rearranges them
     * if there are two on a single slot
     */
    public void rearrangeCorps() {
        for (int i = 0; i < 5; i++) {
            if (corps[i].getWidgetCount() == 2) {
                CorpsInfoMini brigInfo = (CorpsInfoMini) corps[i].getWidget(1);
                brigInfo.removeFromParent();
                boolean found = false;
                for (int j = 0; j < 5; j++) {
                    if (corps[j].getWidgetCount() == 0) {
                        corps[j].add(brigInfo);
                        found = true;
                        break;
                    }
                }
                // if the loop is not broken no
                // space is available
                if (!found) {
                    if (freeArmy == null) {
                        removeCorpById(brigInfo.getCorp().getCorpId());
                    } else {
                        ArmyStore.getInstance().changeCorpArmy(brigInfo.getCorp().getCorpId(), freeArmy.getArmyId(), 0);
                    }
                }

            }
        }
        formArmyController.reInitCorps();

    }
}
