package com.eaw1805.www.client.views.military.formCorp;

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
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitDestroyedEvent;
import com.eaw1805.www.client.events.units.UnitDestroyedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.BrigadeInfoMini;
import com.eaw1805.www.client.views.infopanels.units.mini.CommanderInfoMini;
import com.eaw1805.www.client.views.popups.CommandersListPopup;
import com.eaw1805.www.client.views.popups.DisbandFederationPopup;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DelayIterator;
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

public class FormCorp
        extends AbsolutePanel
        implements ArmyConstants, OrderConstants {

    private AbsolutePanel corpHolder = new AbsolutePanel();
    private AbsolutePanel[] brigPanels = new AbsolutePanel[20];
    private DropController[] dps = new AbsolutePositionDropController[20];
    private Label lblInf, lblCav, lblArt;
    private RenamingLabel lblName;
    private Image comImg;
    private ImageButton saveImg;
    boolean isNew = true;
    private CorpDTO freeCorp;
    private CommanderDTO commander;
    private FormCorpView formCorpController;
    private PickupDragController dragController;
    private ImageButton dissmissImg;
    private List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();

    private ToolTipPanel commanderTooltip;
    private UnitDestroyedHandler unitDestroyedHandler;

    public void setLabels() {
        ArmyUnitInfoDTO corpInfo = new ArmyUnitInfoDTO();
        for (int index = 0; index < BRIGLIMIT[GameStore.getInstance().getNationId() - 1]; index++) {
            final AbsolutePanel container = (AbsolutePanel) corpHolder.getWidget(index);
            if (container.getWidgetCount() > 0) {
                if (!container.getWidget(0).getClass().equals(SimplePanel.class)) {
                    corpInfo.addToInfo(MiscCalculators.getBrigadeInfo(((BrigadeInfoMini) container.getWidget(0)).getBrigade()));
                }
            }
        }
        lblInf.setText(String.valueOf(corpInfo.getInfantry()));
        lblCav.setText(String.valueOf(corpInfo.getCavalry()));
        lblArt.setText(String.valueOf(corpInfo.getArtillery()));
    }

    public FormCorp(final PickupDragController dragController,
                    final CorpDTO corp,
                    final FormCorpView formCorpController,
                    final SectorDTO sector) {
        if (corp != null) {
            isNew = true;
            freeCorp = corp;
        }
        this.dragController = dragController;
        this.formCorpController = formCorpController;
        setSize("720px", "547px");
        corpHolder.setStyleName("infoPanelHolder");
        corpHolder.setSize("720px", "497px");
        add(corpHolder, 0, 49);

        this.saveImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        this.saveImg.setStyleName("pointer");
        this.saveImg.setSize("24px", "24px");
        this.saveImg.setTitle("Save new corp");
        add(this.saveImg, 664, 0);

        this.lblName = new RenamingLabel("Name:", CORPS, 0) {
            public void doChangeName(final int orgArmyType) {
                //first check if corps exist in store
                if (freeCorp != null) {
                    final CorpDTO thisCorp = ArmyStore.getInstance().getCorpByID(freeCorp.getCorpId());
                    if (thisCorp != null && (thisCorp.getName() == null || !thisCorp.getName().equals(this.getText()))) {
                        ArmyStore.getInstance().renameCorps(thisCorp.getCorpId(), this.getText());
                    }
                }
            }
        };
        this.lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.lblName.setStyleName("clearFontMedLarge whiteText");
        this.lblName.setSize("277px", "22px");
        add(this.lblName, 221, 0);

        if (!isNew) {
            this.saveImg.setUrl("http://static.eaw1805.com/images/buttons/ButAccepNA.png");
        }

        if (corp != null) {
            lblName.setText(corp.getName());
            isNew = false;
            this.remove(saveImg);

        } else {
            lblName.setText("Corps " + ArmyStore.getInstance().getCorpSize());
            isNew = true;
        }

        if (freeCorp == null
                || freeCorp.getCommander() == null
                || freeCorp.getCommander().getId() == 0) {
            comImg = new Image("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
            comImg.setTitle("assign commander");
            commanderTooltip = new ToolTipPanel(comImg) {
                @Override
                public void generateTip() {
                    setTooltip(null);
                }
            };

        } else {
            comImg = new Image("http://static.eaw1805.com/img/commanders/s"
                    + GameStore.getInstance().getScenarioId() + "/"
                    + freeCorp.getCommander().getNationId() + "/"
                    + freeCorp.getCommander().getIntId() + ".jpg");
            comImg.setTitle("");
            new ToolTipPanel(comImg) {
                @Override
                public void generateTip() {
                    setTooltip(new CommanderInfoMini(freeCorp.getCommander()));
                }
            };
        }
        final FormCorp ncPanel = this;
        comImg.setStyleName("pointer");
        comImg.setStyleName("borderCommander", true);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                final CommandersListPopup commanderSelectionList = new CommandersListPopup(
                        CommanderStore.getInstance().getCommandersBySectorAndStartingPosition(sector, true, true), ncPanel, 1);
                commanderSelectionList.setAutoHideEnabled(true);
                commanderSelectionList.setPopupPosition(event.getNativeEvent()
                        .getClientX(), event.getNativeEvent().getClientY());
                commanderSelectionList.show();
            }
        }).addToElement(comImg.getElement()).register();

        this.comImg.setSize("40px", "40px");
        dissmissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
        if (freeCorp != null
                && freeCorp.getCommander() != null
                && freeCorp.getCommander().getId() != 0) {
            dissmissImg.setVisible(true);

        } else {
            dissmissImg.setVisible(false);
        }

        dissmissImg.setTitle("Remove commander");
        dissmissImg.setStyleName("pointer");
        dissmissImg.setSize("26px", "26px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!dissmissImg.getUrl().endsWith("NA.png")) {
                    dissmissImg.deselect();
                    if (freeCorp != null && freeCorp.getCommander() != null
                            && freeCorp.getCommander().getId() != 0) {
                        CommanderStore.getInstance().commanderLeaveFederation(freeCorp.getCommander().getId());
                        comImg.setUrl("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
                        commanderTooltip.setTooltip(null);
                    } else if (commander != null) {
                        commander = null;
                        comImg.setUrl("http://static.eaw1805.com/images/armies/dominant/comUnknown.png");
                        commanderTooltip.setTooltip(null);
                    }

                    dissmissImg.setVisible(false);
                    comImg.setTitle("assign commander");
                }
            }
        }).addToElement(dissmissImg.getElement()).register();

        this.add(this.comImg, 0, 0);
        this.add(this.dissmissImg, 50, 14);

        final ImageButton xImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        add(xImg, 692, 0);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (freeCorp != null) {
                    if (freeCorp.getBrigades().values().size() == 0) {
                        ArmyStore.getInstance().deleteCorp(freeCorp.getCorpId(), 0);
                        formCorpController.createNewCorp();

                    } else {
                        final DisbandFederationPopup disCorp = new DisbandFederationPopup(CORPS, freeCorp.getCorpId());
                        disCorp.center();
                    }
                }
            }
        }).addToElement(xImg.getElement()).register();

        xImg.setSize("24px", "24px");

        final Image infImg = new Image("http://static.eaw1805.com/images/armies/dominant/infantry.png");
        infImg.setSize("36px", "24px");
        add(infImg, 259, 23);

        this.lblInf = new Label("Inf");
        add(this.lblInf, 301, 29);
        this.lblInf.setStyleName("whiteText");
        this.lblInf.setSize("36px", "15px");

        final Image cavImg = new Image("http://static.eaw1805.com/images/armies/dominant/cavalry.png");
        add(cavImg, 339, 23);
        cavImg.setSize("36px", "24px");

        this.lblCav = new Label("Cav");
        add(this.lblCav, 378, 29);
        this.lblCav.setStyleName("whiteText");
        this.lblCav.setSize("36px", "15px");

        final Image artImg = new Image("http://static.eaw1805.com/images/armies/dominant/artillery.png");
        add(artImg, 420, 23);
        artImg.setSize("36px", "24px");

        this.lblArt = new Label("Art");
        add(this.lblArt, 462, 29);
        this.lblArt.setStyleName("whiteText");
        this.lblArt.setSize("36px", "15px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (isNew) {
                    saveCorp();
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 8
                            && TutorialStore.getInstance().getTutorialStep() == 3) {
                        TutorialStore.nextStep(false);
                    }
                } else {

                    new ErrorPopup(ErrorPopup.Level.WARNING, "This corp is not new. Adding and removing brigades "
                            + "automatically changes their status", false);
                }
                saveImg.deselect();
            }
        }).addToElement(saveImg.getElement()).register();


        final int nationId = GameStore.getInstance().getNationId();
        for (int i = 0; i < 20; i++) {
            this.brigPanels[i] = new AbsolutePanel();
            if (i == 0) {
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 8
                        && TutorialStore.getInstance().getTutorialStep() == 3
                        && !TutorialStore.getInstance().isBrigadeDraggedFlag()) {
                    brigPanels[i].getElement().getStyle().setBackgroundColor("green");
                    TutorialStore.highLightButton(brigPanels[i]);
                }
            }
            this.brigPanels[i].setSize("170px", "89px");
            if (i < 1) {
                this.corpHolder.add(this.brigPanels[i], 5, 7);

            } else if (i < 4) {
                this.corpHolder.add(this.brigPanels[i], 5 + i * 180, 7);

            } else if (i < 8) {
                this.corpHolder.add(this.brigPanels[i], 5 + (i - 4) * 180, 106);

            } else if (i < 12) {
                this.corpHolder.add(this.brigPanels[i], 5 + (i - 8) * 180, 205);

            } else if (i < 16) {
                this.corpHolder.add(this.brigPanels[i], 5 + (i - 12) * 180, 304);

            } else {
                this.corpHolder.add(this.brigPanels[i], 5 + (i - 16) * 180, 403);
                if (BRIGLIMIT[nationId - 1] < 20) {
                    final Image notAvailImg = new Image("http://static.eaw1805.com/images/panels/formFederations/DarkParchment.png");
                    notAvailImg.setSize("170px", "89px");
                    this.brigPanels[i].add(notAvailImg);
                }
            }
            if (i < BRIGLIMIT[nationId - 1]) {
                dps[i] = new AbsolutePositionDropController(brigPanels[i]);
                dragController.registerDropController(dps[i]);
            }
        }
        if (corp != null) {
            final List<BrigadeDTO> corpsBrigs = new ArrayList<BrigadeDTO>(corp.getBrigades().values());
            new DelayIterator(0, corpsBrigs.size(), 1) {

                @Override
                public void executeStep() {
                    final BrigadeDTO brig = corpsBrigs.get(ITERATE_INDEX);
                    final BrigadeInfoMini brigInfo = new BrigadeInfoMini(brig, true);
                    brigInfo.addDomHandler(new DoubleClickHandler() {
                        public void onDoubleClick(DoubleClickEvent event) {
                            if (freeCorp != null) {
                                if (freeCorp.getBrigades().size() == 1) {
                                    new ErrorPopup(ErrorPopup.Level.WARNING, "By removing the last brigade of the corps, you are disbanding the corps, continue?", true) {
                                        public void onAccept() {
                                            ArmyStore.getInstance().changeBrigadeCorp(brigInfo.getBrigade().getBrigadeId(), freeCorp.getCorpId(), 0, true);
                                            formCorpController.reInitBrigades();
                                            setLabels();
                                        }

                                        public void onReject() {
                                            formCorpController.reInitBrigades();
                                            setLabels();
                                        }
                                    };
                                } else {
                                    ArmyStore.getInstance().changeBrigadeCorp(brigInfo.getBrigade().getBrigadeId(), freeCorp.getCorpId(), 0, true);
                                }
                            } else {
                                removeBrigadeById(brigInfo.getBrigade().getBrigadeId());
                            }
                            formCorpController.reInitBrigades();
                            brigInfo.removeFromParent();
                            setLabels();
                        }
                    }, DoubleClickEvent.getType());
                    brigPanels[ITERATE_INDEX].add(brigInfo);
                    dragController.makeDraggable(brigInfo,
                            brigInfo.getBrigadePanel());
                }

                @Override
                public void executeLast() {
                    setLabels();
                }
            }.run();
        }

        unitDestroyedHandler = new UnitDestroyedHandler() {
            public void onUnitDestroyed(UnitDestroyedEvent event) {
                formCorpController.createNewCorp();
            }
        };

        UnitEventManager.addUnitDestroyedHandler(unitDestroyedHandler);

    }

    public void removeUnitDestroyedHandler() {
        UnitEventManager.removeUnitDestroyedHandler(unitDestroyedHandler);
    }

    private void saveCorp() {
        final List<BrigadeDTO> corpBrigades = new ArrayList<BrigadeDTO>();
        final StringBuilder corpStrBuf = new StringBuilder();
        if (!lblName.getText().trim().equals("")
                && !lblName.getText().isEmpty()) {
            for (int i = 0; i < BRIGLIMIT[GameStore.getInstance().getNationId() - 1]; i++) {
                AbsolutePanel container = (AbsolutePanel) corpHolder
                        .getWidget(i);
                if (container.getWidgetCount() > 0) {
                    corpBrigades.add(((BrigadeInfoMini) container.getWidget(0))
                            .getBrigade());
                    corpStrBuf
                            .append(((BrigadeInfoMini) container.getWidget(0))
                                    .getBrigade().getBrigadeId());
                }

            }
            if (!corpBrigades.isEmpty()) {
                int corpId;
                if (corpStrBuf.length() > 8) {
                    corpId = Integer.parseInt(corpStrBuf.substring(0, 8));

                } else {
                    corpId = Integer.parseInt(corpStrBuf.toString());
                }
                if (isNew) {
                    final String name = lblName.getText();
                    final int[] ids = new int[2];
                    ids[0] = corpId;
                    ids[1] = 0;
                    if (OrderStore.getInstance().addNewOrder(ORDER_B_CORP,
                            CostCalculators.getFormFederationCost(ORDER_B_CORP),
                            corpBrigades.get(0).getRegionId(), name,
                            ids, 0, "") == 1) {
                        freeCorp = ArmyStore.getInstance().createCorp(corpId,
                                0, name, corpBrigades.get(0).getX(),
                                corpBrigades.get(0).getY(),
                                corpBrigades.get(0).getRegionId(),
                                corpBrigades.get(0).getNationId(), false);
                        isNew = false;
                        saveImg.removeFromParent();

                        //now search the new order
                        GameStore.getInstance().getLayoutView().getOrdersMiniView().updateAllRelative(ORDER_B_CORP, ids);
                    }
                }
                if (!isNew) {
                    if (freeCorp != null) {
                        for (BrigadeDTO brig : corpBrigades) {
                            ArmyStore.getInstance().changeBrigadeCorp(
                                    brig.getBrigadeId(), 0,
                                    freeCorp.getCorpId(), false);
                        }
                        UnitEventManager.changeUnit(CORPS, 0);
                        UnitEventManager.changeUnit(CORPS, freeCorp.getCorpId());
                    }

                    if (getCommander() != null) {
                        CommanderStore.getInstance().addCommanderToCorp(
                                freeCorp.getCorpId(), getCommander().getId(), false);
                    }
                    formCorpController.createNewCorp();
                }

            } else {
                new ErrorPopup(ErrorPopup.Level.WARNING, "This corp has no brigades. Add at least one before saving", false);
            }
        } else {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Cannot save corp. Enter a name first", false);
        }

    }

    // Function that set the selected commander for this corp
    public void setSelCommander(final CommanderDTO selCommander) {
        setCommander(selCommander);
        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 8
                && TutorialStore.getInstance().getTutorialStep() == 3) {
            if (selCommander != null) {
                TutorialStore.getInstance().setCommanderAssignedFlag(true);
                if (TutorialStore.getInstance().isBrigadeDraggedFlag()) {
                    TutorialStore.highLightButton(saveImg);
                }
            }
        }


        final int imageId = selCommander.getIntId();
        comImg.setUrl("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + selCommander.getNationId() + "/" + imageId + ".jpg");
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
            CommanderStore.getInstance().addCommanderToCorp(
                    freeCorp.getCorpId(), getCommander().getId(), false);
        }
        dissmissImg.setVisible(true);
        comImg.setTitle("");
    }

    public void unregisterControllers() {
        for (int index = 0; index < 20; index++) {
            if (dps[index] != null) {
                dragController.unregisterDropController(dps[index]);
            }
        }
    }

    public boolean hasSpace() {
        for (int index = 0; index < BRIGLIMIT[GameStore.getInstance().getNationId() - 1]; index++) {
            if (brigPanels[index].getWidgetCount() == 0) {
                return true;
            }
        }
        return false;
    }

    public void addBrigade(BrigadeDTO brigade) {
        if (freeCorp != null) {
            ArmyStore.getInstance().changeBrigadeCorp(
                    brigade.getBrigadeId(), 0,
                    freeCorp.getCorpId(), true);
        } else {
            brigades.add(brigade);
        }

        for (int index = 0; index < BRIGLIMIT[GameStore.getInstance().getNationId() - 1]; index++) {
            if (brigPanels[index].getWidgetCount() == 0) {
                final BrigadeInfoMini brigInfoMini = new BrigadeInfoMini(brigade, true);
                brigInfoMini.addDomHandler(new DoubleClickHandler() {
                    public void onDoubleClick(DoubleClickEvent event) {
                        if (freeCorp != null) {
                            ArmyStore.getInstance().changeBrigadeCorp(brigInfoMini.getBrigade().getBrigadeId(), freeCorp.getCorpId(), 0, true);

                        } else {
                            removeBrigadeById(brigInfoMini.getBrigade().getBrigadeId());
                        }
                        formCorpController.reInitBrigades();
                        brigInfoMini.removeFromParent();
                        setLabels();
                    }
                }, DoubleClickEvent.getType());
                brigPanels[index].add(brigInfoMini);
                dragController.makeDraggable(brigInfoMini, brigInfoMini.getBrigadePanel());
                break;
            }
        }
        setLabels();

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
    public CorpDTO getFreeCorp() {
        return freeCorp;
    }

    /**
     * @return the brigades
     */
    public List<BrigadeDTO> getBrigades() {
        return brigades;
    }

    /**
     * Method that removes a brigade from a
     * non initializes army
     *
     * @param brigadeId the id of the brigade
     * @return true if the brigade was found and removed
     */
    public boolean removeBrigadeById(int brigadeId) {
        for (int index = 0; index < brigades.size(); index++) {
            if (brigades.get(index).getBrigadeId() == brigadeId) {
                brigades.remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the unformed corp already has this brigade
     *
     * @param brigade the brigade
     * @return true if the brigade is present
     */
    public boolean hasBrigade(BrigadeDTO brigade) {
        for (BrigadeDTO brig : brigades) {
            if (brig.getBrigadeId() == brigade.getBrigadeId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that checks the brigades and rearranges them
     * if there are two on a single slot
     */
    public void rearrangeBrigades() {
        for (int index = 0; index < BRIGLIMIT[GameStore.getInstance().getNationId() - 1]; index++) {
            if (brigPanels[index].getWidgetCount() == 2) {
                BrigadeInfoMini brigInfo = (BrigadeInfoMini) brigPanels[index].getWidget(1);
                brigInfo.removeFromParent();
                boolean found = false;
                for (int count = 0; count < BRIGLIMIT[GameStore.getInstance().getNationId() - 1]; count++) {
                    if (brigPanels[count].getWidgetCount() == 0) {
                        brigPanels[count].add(brigInfo);
                        found = true;
                        break;
                    }
                }
                // if the loop is not broken no
                // space is available
                if (!found) {
                    if (freeCorp == null) {
                        removeBrigadeById(brigInfo.getBrigade().getBrigadeId());
                    } else {
                        ArmyStore.getInstance().changeBrigadeCorp(brigInfo.getBrigade().getBrigadeId(), freeCorp.getCorpId(), 0, true);
                    }

                }
            }
        }
        formCorpController.reInitBrigades();
    }

    public Image getComImg() {
        return comImg;
    }

    public ImageButton getSaveImg() {
        return saveImg;
    }
}
