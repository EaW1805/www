package com.eaw1805.www.client.views;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.BasicHandler;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.MyPushButton;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RelationsStore;

import java.util.List;

public class RelationsView
        extends DraggablePanel
        implements RelationConstants, StyleConstants {

    private transient final GameStore gameStore = GameStore.getInstance();

    private transient final ImageButton imgAccept;
    private transient final VerticalPanel buttonHolder = new VerticalPanel();
    private final EmpireRpcServiceAsync eService = GWT.create(EmpireRpcService.class);

    public RelationsView() {
        setStyleName("relationsPanel");
        setStyleName("clearFont", true);
        setSize("810px", "656px");

        buttonHolder.setSize("560px", "561px");
        add(buttonHolder, 240, 55);

        final Label title = new Label("Foreign Relations");
        title.setStyleName("clearFont-large whiteText");
        this.add(title, 20, 10);

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        imgX.setStyleName(CLASS_POINTER);
        imgX.setTitle("Close without saving changes");
        final RelationsView self = this;

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                gameStore.getLayoutView().getOptionsMenu().getRelImage().deselect();
                gameStore.getLayoutView().removeWidgetFromPanelEAW(self);
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 3) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgX.getElement()).register();

        imgX.setSize(SIZE_36PX, SIZE_36PX);
        add(imgX, 761, 7);

        imgAccept = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOn.png");
        imgAccept.setSelected(true);
        imgAccept.setTitle("Save changes and exit");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                saveRelationChanges();
                gameStore.getLayoutView().removeWidgetFromPanelEAW(self);
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 3) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgAccept.getElement()).register();

        imgAccept.setStyleName(CLASS_POINTER);
        imgAccept.setSize(SIZE_36PX, SIZE_36PX);
        add(imgAccept, 719, 7);

        final RelationsStore relStore = RelationsStore.getInstance();
        final List<NationDTO> empireNationDTOs = DataStore.getInstance().getNations();
        for (final NationDTO empNationDTO : empireNationDTOs) {
            if (empNationDTO.getNationId() > 0) {
                final HorizontalPanel row = new HorizontalPanel();
                row.setSize("560px", "35px");
                buttonHolder.add(row);

                String tooltip = "";
                for (int relation = 0; relation < 5; relation++) {
                    final StringBuilder imgUrl = new StringBuilder();
                    imgUrl.append("Grey");

                    final AbsolutePanel relationPanel = new AbsolutePanel();
                    relationPanel.setSize("112px", "35px");

                    switch (relation) {
                        case 0:
                            imgUrl.append("Alliance");
                            tooltip = "All troops and baggage trains of " + empNationDTO.getName() + " may move over our territory. We share maps, spy reports and can view their ship positions.";
                            break;

                        case 1:
                            imgUrl.append("Passage");
                            tooltip = "Armies of " + empNationDTO.getName() + " may move over our territory and warships can enter and move through our shipyards. Our land can be used to support their supply chain.";
                            break;

                        case 2:
                            imgUrl.append("Trade");
                            tooltip = "Baggage trains of " + empNationDTO.getName() + " can move over our land, merchant ships can enter and move through our shipyards and they can trade with our cities. We may trade with their Trade cities.";
                            break;

                        case 3:
                            imgUrl.append("Neutral");
                            tooltip = "No troops or baggage trains of " + empNationDTO.getName() + " can move over your territory and their ships cannot load from or unload into your depots. You can conquer territory of " + empNationDTO.getName() + " in the Colonies and can attack their ships in Europe and the Colonies.";
                            break;

                        case 4:
                            imgUrl.append("War");
                            tooltip = "You can conquer enemy territory in Europe and the Colonies and have battles with enemy armies and fleets";
                            break;

                        default:
                            break;
                    }

                    final ImageButton relationButton = new ImageButton("http://static.eaw1805.com/images/panels/relations/ButRelations" + imgUrl.toString() + "Off.png");
                    relationButton.setId(relation);
                    relationButton.setSize("112px", "34px");
                    relationButton.setStyleName(CLASS_POINTER);
                    relationButton.setTitle(tooltip);
                    final int relId = relation + 1;
                    final int targetNation = empNationDTO.getNationId();
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            if (gameStore.isNationDead()) {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot change relations with other empires", false);

                            } else {
                                final RelationDTO origRelation = relStore.getRelationsMap().get(targetNation);

                                // Ignore fixed alliances
                                if (origRelation.getFixed()) {
                                    new ErrorPopup(ErrorPopup.Level.WARNING, "The alliance is fixed throughout the scenario.", false);
                                    return;
                                }

                                if ((origRelation.getPeaceCount() > 0 && origRelation.getPeaceCount() <= 3)) {
                                    new ErrorPopup(ErrorPopup.Level.WARNING, "We signed a peace treaty before less than 3 months. We cannot change relationships until end of truce period.", false);
                                    return;
                                }

                                if ((origRelation.getSurrenderCount() > 0 && origRelation.getSurrenderCount() <= 3)) {
                                    new ErrorPopup(ErrorPopup.Level.WARNING, "We surrendered before less than 3 months. We cannot change relationships until end of truce period.", false);
                                    return;
                                }

                                if (relId == REL_WAR && (
                                        (origRelation.getPeaceCount() > 1 && origRelation.getPeaceCount() <= 12)
                                                || (origRelation.getSurrenderCount() > 1 && origRelation.getSurrenderCount() <= 12))) {
                                    new ErrorPopup(ErrorPopup.Level.WARNING, "We signed a peace treaty before less than 12 months. We cannot declare war until end of truce period.", false);
                                    return;
                                }

                                // Special rules for Scenario 1802
                                if (GameStore.getInstance().getScenarioId() == HibernateUtil.DB_S1
                                        && gameStore.getTurn() < 3
                                        && relId == REL_WAR) {

                                    new ErrorPopup(ErrorPopup.Level.WARNING, "You can not declare war before July 1802", false);
                                    relationButton.deselect();

                                } else {
                                    // All other cases
                                    if (targetNation == gameStore.getNationId()) {
                                        relationButton.deselect();

                                    } else {
                                        if (Math.abs(origRelation.getRelation() - relId) < 3
                                                || (relStore.isAtWar(targetNation) && relId == REL_WAR)
                                                || (origRelation.getCalledToAllies() && relId == REL_WAR)) {
                                            chooseNewRelation(targetNation - 1, relId - 1, origRelation.getRelation() - 1);

                                        } else {
                                            new ErrorPopup(ErrorPopup.Level.WARNING, "You can change a relation for up to 2 points", false);
                                        }
                                    }
                                }
                            }

                        }
                    }).addToElement(relationButton.getElement()).register();

                    relationPanel.add(relationButton);
                    row.add(relationPanel);
                }
            }
        }

        final List<RelationDTO> empireRelationDTOs = relStore.getRelationsList();
        for (final RelationDTO empRelDTO : empireRelationDTOs) {
            final int targetNation = empRelDTO.getTargetNationId();
            final int originalRelationship = empRelDTO.getRelation();
            final int nextRelationship = empRelDTO.getNextRoundRelation();

            final ImageButton origRelButton = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNation - 1)).getWidget(originalRelationship - 1)).getWidget(0));
            origRelButton.setUrl(origRelButton.getUrl().replace("Grey", ""));
            origRelButton.setUrl(origRelButton.getUrl().replace(OFF, "On"));
            origRelButton.setSelected(true);

            if (originalRelationship != nextRelationship) {
                final ImageButton nextTurnRelButton = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNation - 1)).getWidget(nextRelationship - 1)).getWidget(0));
                nextTurnRelButton.setUrl(nextTurnRelButton.getUrl().replace("Grey", ""));
                nextTurnRelButton.setUrl(nextTurnRelButton.getUrl().replace(OFF, "NextOn"));
                nextTurnRelButton.setSelected(true);
            }

            if (originalRelationship == REL_ALLIANCE) {
                final AbsolutePanel peacePanel = ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNation - 1)).getWidget(0));
                final MyPushButton pushButton = new MyPushButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png", "Your moves to this ally are currently visible. Click to hide your moves to this ally.", "Your moves to this ally are currently hidden. Click to show your moves to this ally.");
                pushButton.setSize(SIZE_26PX, SIZE_26PX);
                pushButton.setPressed(RelationsStore.getInstance().getRelationsMap().get(targetNation).isVisible());
                pushButton.addPushHandler(new BasicHandler() {
                    @Override
                    public void run() {
                        RelationsStore.getInstance().getRelationsMap().get(targetNation).setVisible(pushButton.isPressed());
                        eService.updateAllianceView(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(), GameStore.getInstance().getNationId(), targetNation, pushButton.isPressed(),
                                new AsyncCallback<Integer>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to update relation", false);
                                    }

                                    @Override
                                    public void onSuccess(Integer result) {
                                        //eat it
                                    }
                                });

                    }
                });
                peacePanel.add(pushButton, 85, 0);

            } else if (originalRelationship == REL_WAR) {
                for (int relation = 0; relation < 5; relation++) {
                    final Image disabledImg = new Image("http://static.eaw1805.com/images/panels/relations/ButRelationsDisabledOff.png");
                    disabledImg.setSize("112px", "34px");
                    disabledImg.setTitle("Cannot change to this relation when at war with another nation");

                    final AbsolutePanel relPanel = ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNation - 1)).getWidget(relation));
                    relPanel.add(disabledImg, 0, 0);
                }

                final AbsolutePanel warPanel = ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNation - 1)).getWidget(4));
                final ImageButton acSurrImg = new ImageButton("http://static.eaw1805.com/images/panels/relations/ButAcceptSurrenderOff.png");
                acSurrImg.setStyleName(CLASS_POINTER);
                acSurrImg.setTitle("Accept surrender");
                acSurrImg.setSize(SIZE_26PX, SIZE_26PX);
                warPanel.add(acSurrImg, 15, 0);

                final ImageButton mkSurrImg = new ImageButton("http://static.eaw1805.com/images/panels/relations/ButOfferSurrenderOff.png");
                mkSurrImg.setStyleName(CLASS_POINTER);
                mkSurrImg.setTitle("Offer surrender");
                mkSurrImg.setSize(SIZE_26PX, SIZE_26PX);
                warPanel.add(mkSurrImg, 45, 0);

                final ImageButton mkPeaceImg = new ImageButton("http://static.eaw1805.com/images/panels/relations/ButOfferPeaceOff.png");
                mkPeaceImg.setStyleName(CLASS_POINTER);
                mkPeaceImg.setTitle("Offer peace");
                mkPeaceImg.setSize(SIZE_26PX, SIZE_26PX);
                warPanel.add(mkPeaceImg, 75, 0);

                addWarButtonFunctionality(acSurrImg, mkSurrImg, mkPeaceImg, empRelDTO);

                switch (empRelDTO.getWarAction()) {
                    case ACCEPT_SURR:
                        acSurrImg.setSelected(true);
                        acSurrImg.setId(1);
                        acSurrImg.setUrl(acSurrImg.getUrl().replace(OFF, ON));
                        break;

                    case OFFER_SURR:
                        mkSurrImg.setSelected(true);
                        mkSurrImg.setId(1);
                        mkSurrImg.setUrl(mkSurrImg.getUrl().replace(OFF, ON));
                        break;

                    case MAKE_PEACE:
                        mkPeaceImg.setSelected(true);
                        mkPeaceImg.setId(1);
                        mkPeaceImg.setUrl(mkPeaceImg.getUrl().replace(OFF_PIECE, ON_PIECE));
                        break;

                    default:
                        break;
                }

            }
        }
    }

    private void addWarButtonFunctionality(final ImageButton acSurrImg,
                                           final ImageButton mkSurrImg,
                                           final ImageButton mkPeaceImg,
                                           final RelationDTO relation) {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (acSurrImg.getId() == 1) {
                    acSurrImg.deselect();
                    acSurrImg.setId(0);

                } else {
                    acSurrImg.setSelected(true);
                    acSurrImg.setId(1);
                    mkSurrImg.deselect();
                    mkPeaceImg.deselect();
                }
            }
        }).addToElement(acSurrImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (mkSurrImg.getId() == 1) {
                    mkSurrImg.deselect();
                    mkSurrImg.setId(0);

                } else {
                    mkSurrImg.setSelected(true);
                    mkSurrImg.setId(1);
                    mkPeaceImg.deselect();
                    acSurrImg.deselect();
                }
            }
        }).addToElement(mkSurrImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (GameStore.getInstance().getScenarioId() == 2
                        && ((relation.getNationId() == NationConstants.NATION_FRANCE
                        && relation.getTargetNationId() == NationConstants.NATION_GREATBRITAIN)
                        || (relation.getNationId() == NationConstants.NATION_GREATBRITAIN
                        && relation.getTargetNationId() == NationConstants.NATION_FRANCE))) {
                    mkPeaceImg.deselect();
                    mkPeaceImg.setId(0);
                    new ErrorPopup(ErrorPopup.Level.WARNING, "France and Great Britain can end their war only with the surrender of one to the other", false);
                    return;
                }

                if (mkPeaceImg.getId() == 1) {
                    mkPeaceImg.deselect();
                    mkPeaceImg.setId(0);

                } else {
                    mkPeaceImg.setSelected(true);
                    mkPeaceImg.setId(1);
                    mkSurrImg.deselect();
                    acSurrImg.deselect();
                }
            }
        }).addToElement(mkPeaceImg.getElement()).register();

    }

    private void chooseNewRelation(final int targetNationRow, final int relIdCol, final int origRelId) {
        for (int relation = 0; relation < 5; relation++) {
            if (relation != origRelId) {
                final ImageButton button = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNationRow)).getWidget(relation)).getWidget(0));
                if (!button.getUrl().contains("Grey")) {
                    button.setUrl(button.getUrl().replace("ButRelations", "ButRelationsGrey"));
                    button.setUrl(button.getUrl().replace("Next", ""));
                }
                button.deselect();
            }
        }

        if (relIdCol != origRelId) {
            final ImageButton button = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(targetNationRow)).getWidget(relIdCol)).getWidget(0));
            button.setUrl(button.getUrl().replace("Grey", ""));
            button.setUrl(button.getUrl().replace("On", "NextOn"));
            button.setUrl(button.getUrl().replace(OFF, "NextOn"));
            button.setUrl(button.getUrl().replace("Hover", "NextOn"));
            button.setSelected(true);
        }

        imgAccept.deselect();
    }

    private void saveRelationChanges() {
        final int nationId = gameStore.getNationId();
        for (int foreignNation = 0; foreignNation < 17; foreignNation++) {
            if (foreignNation + 1 != nationId) {
                boolean changed = false;
                for (int relation = 0; relation < 5; relation++) {
                    final ImageButton button = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(foreignNation)).getWidget(relation)).getWidget(0));
                    if (button.getUrl().contains("Next")) {
                        RelationsStore.getInstance().changeNationRelationship(foreignNation + 1, relation + 1, NO_ACTION);
                        changed = true;

                    } else if (!changed && !button.getUrl().contains("Grey") && relation == REL_WAR - 1) {

                        final ImageButton accSurr = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(foreignNation)).getWidget(relation)).getWidget(2));
                        final ImageButton offSurr = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(foreignNation)).getWidget(relation)).getWidget(3));
                        final ImageButton mkPeace = ((ImageButton) ((AbsolutePanel) ((HorizontalPanel) buttonHolder.getWidget(foreignNation)).getWidget(relation)).getWidget(4));
                        if (accSurr.isSelected()) {
                            RelationsStore.getInstance().changeNationRelationship(foreignNation + 1, relation + 1, ACCEPT_SURR);

                        } else if (offSurr.isSelected()) {
                            RelationsStore.getInstance().changeNationRelationship(foreignNation + 1, relation + 1, OFFER_SURR);

                        } else if (mkPeace.isSelected()) {
                            RelationsStore.getInstance().changeNationRelationship(foreignNation + 1, relation + 1, MAKE_PEACE);

                        } else {
                            RelationsStore.getInstance().changeNationRelationship(foreignNation + 1, relation + 1, NO_ACTION);
                        }


                    } else if (!changed && !button.getUrl().contains("Grey")) {
                        RelationsStore.getInstance().changeNationRelationship(foreignNation + 1, relation + 1, NO_ACTION);
                    }
                }
            }
        }
    }
}
