package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;
import com.eaw1805.data.dto.web.field.FieldBattlePositionDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.tooltips.TipPanel;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.FieldOrdersConstants;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.BrigadeFullInfoPanel;

import java.util.*;

public class OrderMiniWidget extends VerticalPanel implements FieldOrdersConstants {

    final BrigadeDTO brigade;
    final SelectEAW<String> selector;
    final HorizontalPanel movePanel;
    final ImageButton moveButton;
    final ImageButton spButton;
    final HorizontalPanel spPanel;
    final TextBox distanceBox;
    final SelectEAW<String> formationSelector;
    final SelectEAW<String> targetTypeSelector;
    final SelectEAW<String> targetFormationSelector;
    final StyledCheckBox headCount;
    final StyledCheckBox closestRange;
    final StyledCheckBox enemyCapturedOwnStrPointCheckbox;
    final StyledCheckBox captureEnemyStrPointCheckbox;
    final StyledCheckBox lastDestCheckbox;
    final TextBoxEAW headCountBox;
    final TextBoxEAW actRoundBox;
    Map<Integer, StyledCheckBox> nationIdToElement = new HashMap<Integer, StyledCheckBox>();

    AbsolutePanel baseInfoContainer = new AbsolutePanel();
    AbsolutePanel enemySelectionContainer = new AbsolutePanel();
    AbsolutePanel triggersContainer = new AbsolutePanel();
    final FieldBattleOrderDTO order;
    final Label spLabel = new Label();
    final Label distanceLabel = new Label();
    final Label detachmentLabel = new Label();
    final Label moveLabel = new Label();
    boolean initilized = false;
    final SelectEAW<String> detachmentPosition;
    final SelectEAW<Integer> leaderSelector;
    boolean isBasic;
    AbsolutePanel locker1;
    AbsolutePanel locker2;
    AbsolutePanel locker3;
    boolean updateOthers = false;
    /**
     *
     * @param brig The brigade to edit an order.
     * @param isBasic Boolean to indicate if we edit basic or additional order.
     * @param selectedBrigades Other brigades changes should apply to as well.
     */
    public OrderMiniWidget(final BrigadeDTO brig, final boolean isBasic, final Set<BrigadeDTO> selectedBrigades) {
        this.isBasic = isBasic;
        brigade = brig;
        baseInfoContainer.setSize("366px", "110px");
        baseInfoContainer.setStyleName("battleOrderPanelBig");
        enemySelectionContainer.setSize("366px", "90px");
        enemySelectionContainer.setStyleName("battleOrderPanel");
        triggersContainer.setSize("366px", "90px");
        triggersContainer.setStyleName("battleOrderPanel");
        add(baseInfoContainer);
//        add(enemySelectionContainer);


        if (isBasic) {
            baseInfoContainer.add(createLabel("Basic Order"), 151, 5);
        } else {
            baseInfoContainer.add(createLabel("Additional Order"), 131, 5);
        }

        final ImageButton openEnemiesCard = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersOff.png", Tips.BRIGADE_TARGET_PANEL);
        openEnemiesCard.getElement().getStyle().setZIndex(1);
        openEnemiesCard.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (enemySelectionContainer.isAttached()) {
                    remove(enemySelectionContainer);
                } else {
                    add(enemySelectionContainer);
                }
            }
        });
        openEnemiesCard.setWidth("20px");
        baseInfoContainer.add(openEnemiesCard, 335, 87);

        final ImageButton openTriggersCard = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersOff.png", Tips.BRIGADE_ACTIVATE_PANEL);
        openTriggersCard.getElement().getStyle().setZIndex(1);
        openTriggersCard.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (triggersContainer.isAttached()) {
                    remove(triggersContainer);
                } else {
                    add(triggersContainer);
                }
            }
        });

        openTriggersCard.setWidth("20px");
        if (!isBasic) {
            baseInfoContainer.add(openTriggersCard, 310, 87);
        }
        selector = new SelectEAW<String>(Tips.ORDER_COMMAND) {
            @Override
            public void onChange() {
                order.setOrderType(getValue());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setOrderType(getValue());
                        } else {
                            otherBrig.getAdditionalOrder().setOrderType(getValue());
                        }
                    }
                }
                updateVisibleInputs();
                updateOtherPanels(selectedBrigades);
            }
        };
        selector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        selector.setDropDownStyleName("dropDown320x420");
        int count = 0;
        for (final FieldOrdersConstants.OrdersEnum entry : FieldOrdersConstants.OrdersEnum.values()) {
            count++;
            if (count <= 13 || brigade.hasEngineers()) {
                final OptionEAW option = new OptionEAW(175, 15, entry.name().replaceAll("_", " "));
                Tips.generateTip(option, Tips.OrderTip.get(entry.name()));
                selector.addOption(option, entry.name());
            }
        }
        baseInfoContainer.add(selector, 5, 25);

        formationSelector = new SelectEAW<String>(Tips.ORDER_FORMATION) {
            @Override
            public void onChange() {
                order.setFormation(getValue());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setFormation(getValue());
                        } else {
                            otherBrig.getAdditionalOrder().setFormation(getValue());

                        }
                    }
                }
                updateOtherPanels(selectedBrigades);

            }
        };
        formationSelector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        formationSelector.setDropDownStyleName("dropDown320x420");

        for (FieldOrdersConstants.FormationEnum entry : getAvailableFormations()) {
            formationSelector.addOption(new OptionEAW(75, 15, entry.name()), entry.name());
        }

        baseInfoContainer.add(createLabel("Formation : "), 5, 43);
        baseInfoContainer.add(formationSelector, 80, 43);


        baseInfoContainer.add(createLabel("Distance: ", distanceLabel), 189, 43);
        distanceBox = new TextBox();
        distanceBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                String valueStr = distanceBox.getText();
                if (valueStr != null && !valueStr.isEmpty()) {
                    try {
                        int value = Integer.parseInt(valueStr);
                        order.setMaintainDistance(value);
                        if (updateOthers) {
                            for (BrigadeDTO otherBrig : selectedBrigades) {
                                if (isBasic) {
                                    otherBrig.getBasicOrder().setMaintainDistance(value);
                                } else {
                                    otherBrig.getAdditionalOrder().setMaintainDistance(value);
                                }
                            }
                        }
                        updateOtherPanels(selectedBrigades);
                    } catch (Exception e) {
                        //eat it
                    }
                }

            }
        });
        distanceBox.setSize("35px", "25px");
        Tips.generateTip(distanceBox, Tips.ORDER_DISTANCE);
        baseInfoContainer.add(distanceBox, 249, 39);


        baseInfoContainer.add(createLabel("Move: ", moveLabel), 5, 86);
        movePanel = new HorizontalPanel();
        Tips.generateTip(movePanel, Tips.ORDER_MOVE);
        moveButton = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png", "Move brigade");
        moveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                MainPanel.getInstance().getDrawingArea().selectPosition(brigade, false, isBasic);
            }
        });
        moveButton.setHeight("20px");
        movePanel.add(moveButton);
        baseInfoContainer.add(movePanel, 50, 86);

        baseInfoContainer.add(createLabel("Move to Strategic Points: ", spLabel), 5, 64);
        spPanel = new HorizontalPanel();
        Tips.generateTip(spPanel, Tips.ORDER_SP);
        spButton = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png", "Move brigade to strategic points");
        spButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                MainPanel.getInstance().getDrawingArea().selectPosition(brigade, true, isBasic);
            }
        });
        spButton.setHeight("20px");
        spPanel.add(spButton);
        baseInfoContainer.add(spPanel, 160, 64);

        leaderSelector = new SelectEAW<Integer>(Tips.ORDER_LEADER) {
            @Override
            public void onChange() {
                order.setLeaderId(getValue());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setLeaderId(getValue());
                        } else {
                            otherBrig.getAdditionalOrder().setLeaderId(getValue());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        };
        leaderSelector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        leaderSelector.setDropDownStyleName("dropDown320x420");
        leaderSelector.addOption(new OptionEAW(150, 15, "No one selected"), 0);
        for (BrigadeDTO curBrig : ArmyStore.getInstance().getPlacedBrigadesByNation(BaseStore.getInstance().getNationId())) {
            if (curBrig.getBrigadeId() != brigade.getBrigadeId()) {
                leaderSelector.addOption(new OptionEAW(150, 15, curBrig.positionFieldBattleToString() + " - " + curBrig.getName()), curBrig.getBrigadeId());
            }
        }
        baseInfoContainer.add(createLabel("Leader : ", detachmentLabel), 5, 69);
        baseInfoContainer.add(leaderSelector, 55, 71);


        detachmentPosition = new SelectEAW<String>(Tips.ORDER_DETACHMENT) {
            @Override
            public void onChange() {
                order.setDetachmentPosition(getValue());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setDetachmentPosition(getValue());
                        } else {
                            otherBrig.getAdditionalOrder().setDetachmentPosition(getValue());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        };

        detachmentPosition.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        detachmentPosition.setDropDownStyleName("dropDown320x420");

        for (DetachmentPosition entry : DetachmentPosition.values()) {
            detachmentPosition.addOption(new OptionEAW(100, 15, entry.name()), entry.name());
        }
        baseInfoContainer.add(detachmentPosition, 228, 71);


        enemySelectionContainer.add(createLabel("Prefer target type : "), 5, 5);
        targetTypeSelector = new SelectEAW<String>(Tips.ORDER_ENEMY_TYPE) {
            @Override
            public void onChange() {
                order.setTargetArm(getValue());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setTargetArm(getValue());
                        } else {
                            otherBrig.getAdditionalOrder().setTargetArm(getValue());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        };
        targetTypeSelector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        targetTypeSelector.setDropDownStyleName("dropDown320x420");
        for (FieldOrdersConstants.ArmEnum entry : FieldOrdersConstants.ArmEnum.values()) {
            targetTypeSelector.addOption(new OptionEAW(75, 15, entry.name()), entry.name());
        }
        enemySelectionContainer.add(targetTypeSelector, 126, 5);


        enemySelectionContainer.add(createLabel("Prefer target formation : "), 5, 23);
        targetFormationSelector = new SelectEAW<String>(Tips.ORDER_ENEMY_FORMATION) {

            @Override
            public void onChange() {
                order.setTargetFormation(getValue());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setTargetFormation(getValue());
                        } else {
                            otherBrig.getAdditionalOrder().setTargetFormation(getValue());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        };

        targetFormationSelector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");
        targetFormationSelector.setDropDownStyleName("dropDown320x420");
        for (FieldOrdersConstants.FormationEnum entry : FieldOrdersConstants.FormationEnum.values()) {
            targetFormationSelector.addOption(new OptionEAW(75, 15, entry.name()), entry.name());
        }
        enemySelectionContainer.add(targetFormationSelector, 150, 23);

        enemySelectionContainer.add(createLabel("Prefer enemies with : "), 5, 44);

        headCount = new StyledCheckBox("Highest headcount", false, false, Tips.ORDER_HEADCOUNT);

        closestRange = new StyledCheckBox("Closest range", false, false, Tips.ORDER_RANGE);

        headCount.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                closestRange.setChecked(!headCount.isChecked());
                order.setTargetClosestInRange(closestRange.isChecked());
                order.setTargetHighestHeadcount(headCount.isChecked());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setTargetClosestInRange(closestRange.isChecked());
                            otherBrig.getBasicOrder().setTargetHighestHeadcount(headCount.isChecked());
                        } else {
                            otherBrig.getAdditionalOrder().setTargetClosestInRange(closestRange.isChecked());
                            otherBrig.getAdditionalOrder().setTargetHighestHeadcount(headCount.isChecked());
                        }
                    }
                }

                updateOtherPanels(selectedBrigades);
            }
        });
        closestRange.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                headCount.setChecked(!closestRange.isChecked());
                order.setTargetClosestInRange(closestRange.isChecked());
                order.setTargetHighestHeadcount(headCount.isChecked());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setTargetClosestInRange(closestRange.isChecked());
                        } else {
                            otherBrig.getAdditionalOrder().setTargetHighestHeadcount(headCount.isChecked());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        });
        headCount.setTextStyle("clearFontMedMini");
        closestRange.setTextStyle("clearFontMedMini");
        enemySelectionContainer.add(headCount, 145, 44);
        enemySelectionContainer.add(closestRange, 265, 44);
        Label preferEnemiesLbl = new Label("");
        enemySelectionContainer.add(createLabel("Prefer enemies from :", preferEnemiesLbl), 5, 64);
        preferEnemiesLbl.setVisible(BaseStore.getInstance().getEnemyNations().size() > 1);

        HorizontalPanel nationsContainer = new HorizontalPanel();
        for (final Integer nationId : BaseStore.getInstance().getEnemyNations()) {
            final StyledCheckBox nationCheckbox = new StyledCheckBox(BaseStore.getInstance().getNationById(nationId).getName(), true, false, "Prefer enemies from " + BaseStore.getInstance().getNationById(nationId).getName());
            nationCheckbox.addOnChangeHandler(new BasicHandler() {
                @Override
                public void run() {
                    if (nationCheckbox.isChecked()) {
                        order.getTargetNations().add(nationId);
                        if (updateOthers) {
                            for (BrigadeDTO otherBrig : selectedBrigades) {
                                if (isBasic) {
                                    otherBrig.getBasicOrder().getTargetNations().add(nationId);
                                } else {
                                    otherBrig.getAdditionalOrder().getTargetNations().add(nationId);
                                }
                            }
                        }
                    } else {
                        //just be sure it will try to remove the object and not the index.....
                        if (order.getTargetNations().contains(nationId)) {
                            order.getTargetNations().remove(nationId);
                        }
                        if (updateOthers) {
                            for (BrigadeDTO otherBrig : selectedBrigades) {
                                if (isBasic) {
                                    if (otherBrig.getBasicOrder().getTargetNations().contains(nationId)) {
                                        otherBrig.getBasicOrder().getTargetNations().remove(nationId);
                                    }
                                } else {
                                    if (otherBrig.getAdditionalOrder().getTargetNations().contains(nationId)) {
                                        otherBrig.getAdditionalOrder().getTargetNations().remove(nationId);
                                    }
                                }
                            }
                        }
                    }
                    updateOtherPanels(selectedBrigades);
                }
            });
            nationCheckbox.setTextStyle("clearFontMedMini");
            nationCheckbox.setVisible(BaseStore.getInstance().getEnemyNations().size() > 1);
            nationIdToElement.put(nationId, nationCheckbox);
            nationsContainer.add(nationCheckbox);
        }
        enemySelectionContainer.add(nationsContainer, 136, 65);


        triggersContainer.add(createLabel("Activate at round: "), 5, 5);
        actRoundBox = new TextBoxEAW();
        Tips.generateTip(actRoundBox, Tips.ORDER_TRIGGER_ROUND);

        actRoundBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                final String valueStr = actRoundBox.getText();
                if (valueStr != null && !valueStr.isEmpty()) {
                    try {
                        order.setActivationRound(Integer.parseInt(valueStr));
                        if (updateOthers) {
                            for (BrigadeDTO otherBrig : selectedBrigades) {
                                if (isBasic) {
                                    otherBrig.getBasicOrder().setActivationRound(Integer.parseInt(valueStr));
                                } else {
                                    otherBrig.getAdditionalOrder().setActivationRound(Integer.parseInt(valueStr));
                                }
                            }
                        }
                        updateOtherPanels(selectedBrigades);
                    } catch (Exception e) {
                        //eat it
                    }
                }
            }
        });
        actRoundBox.setText("0");
        actRoundBox.setSize("35px", "25px");

        triggersContainer.add(actRoundBox, 112, 2);

        triggersContainer.add(createLabel("When headcount falls bellow : "), 5, 23);
        headCountBox = new TextBoxEAW();
        Tips.generateTip(headCountBox, Tips.ORDER_TRIGGER_HEADCOUNT);

        headCountBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                final String valueStr = headCountBox.getText();
                if (valueStr != null && !valueStr.isEmpty()) {
                    try {
                        order.setHeadCountThreshold(Integer.parseInt(valueStr));
                        if (updateOthers) {
                            for (BrigadeDTO otherBrig : selectedBrigades) {
                                if (isBasic) {
                                    otherBrig.getBasicOrder().setHeadCountThreshold(Integer.parseInt(valueStr));
                                } else {
                                    otherBrig.getAdditionalOrder().setHeadCountThreshold(Integer.parseInt(valueStr));
                                }
                            }
                        }
                        updateOtherPanels(selectedBrigades);
                    } catch (Exception e) {
                        //eat it
                    }
                }
            }
        });
        headCountBox.setText("0");
        headCountBox.setSize("35px", "20px");
        triggersContainer.add(headCountBox, 181, 19);

        lastDestCheckbox = new StyledCheckBox("Destination reached", false, false, Tips.ORDER_TRIGGER_LASTDEST);

        lastDestCheckbox.setTextStyle("clearFontMedMini");
        lastDestCheckbox.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                order.setLastDestinationReached(lastDestCheckbox.isChecked());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setLastDestinationReached(lastDestCheckbox.isChecked());
                        } else {
                            otherBrig.getAdditionalOrder().setLastDestinationReached(lastDestCheckbox.isChecked());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        });
        triggersContainer.add(lastDestCheckbox, 5, 44);

        captureEnemyStrPointCheckbox = new StyledCheckBox("Capture strategic point", false, false, Tips.ORDER_TRIGGER_CAPTURE_ENEMY);

        captureEnemyStrPointCheckbox.setTextStyle("clearFontMedMini");
        captureEnemyStrPointCheckbox.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                order.setOwnSideCapturedEnemyStrategicPoint(captureEnemyStrPointCheckbox.isChecked());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setOwnSideCapturedEnemyStrategicPoint(captureEnemyStrPointCheckbox.isChecked());
                        } else {
                            otherBrig.getAdditionalOrder().setOwnSideCapturedEnemyStrategicPoint(captureEnemyStrPointCheckbox.isChecked());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        });
        triggersContainer.add(captureEnemyStrPointCheckbox, 153, 44);


        enemyCapturedOwnStrPointCheckbox = new StyledCheckBox("Lose strategic point", false, false, Tips.ORDER_TRIGGER_CAPTURE_OWN);
        enemyCapturedOwnStrPointCheckbox.setTextStyle("clearFontMedMini");
        enemyCapturedOwnStrPointCheckbox.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                order.setEnemySideCapturedOwnStrategicPoint(enemyCapturedOwnStrPointCheckbox.isChecked());
                if (updateOthers) {
                    for (BrigadeDTO otherBrig : selectedBrigades) {
                        if (isBasic) {
                            otherBrig.getBasicOrder().setEnemySideCapturedOwnStrategicPoint(enemyCapturedOwnStrPointCheckbox.isChecked());
                        } else {
                            otherBrig.getAdditionalOrder().setEnemySideCapturedOwnStrategicPoint(enemyCapturedOwnStrPointCheckbox.isChecked());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
            }
        });
        triggersContainer.add(enemyCapturedOwnStrPointCheckbox, 5, 67);

        fixBrigadeOrders();
        if (isBasic) {
            order = brigade.getBasicOrder();
        } else {
            order = brigade.getAdditionalOrder();
        }
//        if (brigade.getBrigadeId()> 0) {
            updateInputsFromOrder();
//        }
        updateVisibleInputs();

        locker1 = new AbsolutePanel();
        locker1.getElement().getStyle().setBackgroundColor("rgba(0, 200, 200, 0.5)");
        locker1.setSize("366px", "90px");
        baseInfoContainer.add(locker1, 0, 20);

        final Image clearImg = new Image("http://static.eaw1805.com/images/field/extra/clear.png");
        Tips.generateTip(clearImg, Tips.BRIGADE_CLEAR_ORDER);
        clearImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //just copy a dummys order values back to order.
                copyOrder(order, createNewOrder());
                if (updateOthers) {
                    for (final BrigadeDTO brigade : selectedBrigades) {
                        if (isBasic) {
                            copyOrder(brigade.getBasicOrder(), createNewOrder());
                        } else {
                            copyOrder(brigade.getAdditionalOrder(), createNewOrder());
                        }
                    }
                }
                updateOtherPanels(selectedBrigades);
                if (BaseStore.getInstance().isStartRound()) {
                    MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                    MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                } else {
                    MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                    MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                }
                if (brigade.getBrigadeId() > 0) {
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }

                if (updateOthers) {
                    for (final BrigadeDTO brigade : selectedBrigades) {
                        if (BaseStore.getInstance().isStartRound()) {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                            MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                        } else {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                            MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                        }
//                        MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                    }
                    if (selectedBrigades.size() > 0) {
                        final List<BrigadeDTO> selectedList = new ArrayList<BrigadeDTO>(selectedBrigades);
                        MainPanel.getInstance().getDrawingArea().getSelectedGroup().hidePanel();
                        MainPanel.getInstance().getDrawingArea().getSelectedGroup().selectBrigades(selectedList);
                        MainPanel.getInstance().getDrawingArea().getSelectedGroup().showPanel();
                    }
                }
            }
        });

        clearImg.setVisible(BaseStore.getInstance().isStartRound());

        clearImg.setSize("20px", "20px");
        baseInfoContainer.add(clearImg, 335, 64);
        lockImg.setSize("18px", "18px");
        Tips.generateTip(lockImg, Tips.BRIGADE_LOCK_ORDER);
        lockImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                try {
                    final boolean lock = brigade.isFieldBattleUpdateMiddleRound();
                    if (lock || BaseStore.getInstance().canEditMore()) {
                        //update lock value
                        MainPanel.getInstance().getMapUtils().setBrigadeOrderLock(brigade.getBrigadeId(), lock);
                        //re-add brigade on map
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);

                        MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, true, MainPanel.getInstance().getPlayback().getRound() - 1);
//                        parent.getBasicOrder().setLock(lock);
//                        parent.getAdditionalOrder().setLock(lock);
                    }
                } catch (Exception e) {
                    Window.alert("ftls? " + e.toString());
                }
            }
        });
        baseInfoContainer.add(lockImg, 340, 2);
        locker2 = new AbsolutePanel();
        locker2.getElement().getStyle().setBackgroundColor("rgba(0, 200, 200, 0.5)");
        locker2.setSize("366px", "90px");
        enemySelectionContainer.add(locker2, 0, 0);

        locker3 = new AbsolutePanel();
        locker3.getElement().getStyle().setBackgroundColor("rgba(0, 200, 200, 0.5)");
        locker3.setSize("366px", "90px");
        triggersContainer.add(locker3, 0, 0);
        if ((BaseStore.getInstance().isStartRound() || brigade.isFieldBattleUpdateMiddleRound())
                && !BaseStore.getInstance().isGameEnded()) {
            unlockBrigade();
            lockImg.setUrl("http://static.eaw1805.com/images/field/extra/unlock.png");
        }
        if (BaseStore.getInstance().isStartRound()
                || BaseStore.getInstance().isGameEnded()) {
            lockImg.setVisible(false);
        }
        initilized = true;
    }



    final Image lockImg = new Image("http://static.eaw1805.com/images/field/extra/lock.png");

    public void setLock(boolean value) {
        if (value) {
            lockBrigade();
            recoverOriginalOrders();
            updateInputsFromOrder();
            lockImg.setUrl("http://static.eaw1805.com/images/field/extra/lock.png");
        } else {
            unlockBrigade();
            lockImg.setUrl("http://static.eaw1805.com/images/field/extra/unlock.png");
        }

    }

    public void updateOtherPanels(Set<BrigadeDTO> selectedBrigades) {
        if (initilized) {
            if (BaseStore.getInstance().isStartRound()) {
                MainPanel.getInstance().getMapUtils().updateBrigadeOrderedImages(brigade, 0);
                for (BrigadeDTO otherBrig : selectedBrigades) {
                    MainPanel.getInstance().getMapUtils().updateBrigadeOrderedImages(otherBrig, 0);
                }
            } else {
                MainPanel.getInstance().getMapUtils().updateBrigadeOrderedImages(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                for (BrigadeDTO otherBrig : selectedBrigades) {
                    MainPanel.getInstance().getMapUtils().updateBrigadeOrderedImages(otherBrig, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            }

        }
    }

    public void updateLeaderSelectionOptions() {
        final List<BrigadeDTO> placedBrigades = ArmyStore.getInstance().getPlacedBrigadesByNation(BaseStore.getInstance().getNationId());
        leaderSelector.clearOptions();
        leaderSelector.addOption(new OptionEAW(200, 15, "No one selected"), 0);
        for (BrigadeDTO brig : placedBrigades) {
            if (brig.getBrigadeId() != brigade.getBrigadeId()) {
                leaderSelector.addOption(new OptionEAW(200, 15, brig.positionFieldBattleToString() + " - " + brig.getName()), brig.getBrigadeId());
            }
        }
        leaderSelector.selectOptionByValue(order.getLeaderId());
    }


    public void updateVisibleInputs() {
        OrdersEnum type = OrdersEnum.valueOf(selector.getValue());
        distanceBox.setVisible(type == OrdersEnum.MAINTAIN_DISTANCE);
        distanceLabel.setVisible(type == OrdersEnum.MAINTAIN_DISTANCE);
        spButton.setVisible(type == OrdersEnum.ATTACK_ENEMY_STRATEGIC_POINTS || type == OrdersEnum.RECOVER_OWN_STRATEGIC_POINTS);
        spPanel.setVisible(type == OrdersEnum.ATTACK_ENEMY_STRATEGIC_POINTS || type == OrdersEnum.RECOVER_OWN_STRATEGIC_POINTS);
        spLabel.setVisible(type == OrdersEnum.ATTACK_ENEMY_STRATEGIC_POINTS || type == OrdersEnum.RECOVER_OWN_STRATEGIC_POINTS);
        detachmentLabel.setVisible(type == OrdersEnum.FOLLOW_DETACHMENT);
        leaderSelector.setVisible(type == OrdersEnum.FOLLOW_DETACHMENT);
        detachmentPosition.setVisible(type == OrdersEnum.FOLLOW_DETACHMENT);
        movePanel.setVisible(type != OrdersEnum.FOLLOW_DETACHMENT);
        moveLabel.setVisible(type != OrdersEnum.FOLLOW_DETACHMENT);
    }

    public void updateInputsFromOrder() {
        updateOthers = false;
        initilized = false;
        selector.selectOptionByValue(order.getOrderType());
        formationSelector.selectOptionByValue(order.getFormation());
        distanceBox.setText(String.valueOf(order.getMaintainDistance()));
        updateMoveCoords();
        updateSPCoords();
        leaderSelector.selectOptionByValue(order.getLeaderId());
        detachmentPosition.selectOptionByValue(order.getDetachmentPosition());
        targetTypeSelector.selectOptionByValue(order.getTargetArm());
        targetFormationSelector.selectOptionByValue(order.getTargetFormation());
        headCount.setChecked(order.isTargetHighestHeadcount());
        closestRange.setChecked(order.isTargetClosestInRange());
        for (final Integer nationId : BaseStore.getInstance().getEnemyNations()) {
            nationIdToElement.get(nationId).setChecked(order.getTargetNations().contains(nationId));
        }
        enemyCapturedOwnStrPointCheckbox.setChecked(order.isEnemySideCapturedOwnStrategicPoint());
        captureEnemyStrPointCheckbox.setChecked(order.isOwnSideCapturedEnemyStrategicPoint());
        lastDestCheckbox.setChecked(order.isLastDestinationReached());
        headCountBox.setText(String.valueOf(order.getHeadCountThreshold()));
        actRoundBox.setText(String.valueOf(order.getActivationRound()));
        initilized = true;
        updateOthers = true;
    }

    public void setMoveCoords(final int x, final int y) {
        FieldBattlePositionDTO pos = new FieldBattlePositionDTO();
        pos.setX(x);
        pos.setY(y);
        if (!order.hasCheckPoint1()) {
            order.setCheckPoint1(pos);
        } else if (!order.hasCheckPoint2()) {
            order.setCheckPoint2(pos);
        } else if (!order.hasCheckPoint3()) {
            order.setCheckPoint3(pos);
        }
        updateMoveCoords();
    }

    public void updateMoveCoords() {
        movePanel.clear();
        moveButton.setVisible(true);
        if (order.hasCheckPoint1()) {
            movePanel.add(createLabel(order.getCheckPoint1().toString() + ","));
            final ImageButton rmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png", "Remove position");
            rmImg.setSize("20px", "20px");
            rmImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    //shift checkpoints
                    order.setCheckPoint1(order.getCheckPoint2());
                    order.setCheckPoint2(order.getCheckPoint3());
                    order.setCheckPoint3(new FieldBattlePositionDTO());
                    order.getCheckPoint3().reset();

                    if (BaseStore.getInstance().isStartRound()) {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    } else {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                    }
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            });
            movePanel.add(rmImg);
        }
        if (order.hasCheckPoint2()) {
            movePanel.add(createLabel(order.getCheckPoint2().toString() + ","));
            final ImageButton rmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png", "Remove position");
            rmImg.setSize("20px", "20px");
            rmImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    order.setCheckPoint2(order.getCheckPoint3());
                    order.setCheckPoint3(new FieldBattlePositionDTO());
                    order.getCheckPoint3().reset();

                    if (BaseStore.getInstance().isStartRound()) {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    } else {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                    }
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            });
            movePanel.add(rmImg);
        }
        if (order.hasCheckPoint3()) {
            movePanel.add(createLabel(order.getCheckPoint3().toString() + ", "));
            final ImageButton rmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png", "Remove position");
            rmImg.setSize("20px", "20px");
            rmImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    order.getCheckPoint3().reset();

                    if (BaseStore.getInstance().isStartRound()) {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    } else {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                    }
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            });
            movePanel.add(rmImg);
            moveButton.setVisible(false);
        }
        movePanel.add(moveButton);
    }

    public void setStrategicPointCoords(final int x, final int y) {
        FieldBattlePositionDTO pos = new FieldBattlePositionDTO();
        pos.setX(x);
        pos.setY(y);
        if (!order.hasStrategicPoint1()) {
            order.setStrategicPoint1(pos);
        } else if (!order.hasStrategicPoint2()) {
            order.setStrategicPoint2(pos);
        } else if (!order.hasStrategicPoint3()) {
            order.setStrategicPoint3(pos);
        }
        updateSPCoords();
    }

    public void updateSPCoords() {
        spPanel.clear();
        spButton.setVisible(true);
        if (order.hasStrategicPoint1()) {
            spPanel.add(createLabel(order.getStrategicPoint1().toString() + ", "));
            final ImageButton rmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png", "Remove position");
            rmImg.setSize("20px", "20px");
            rmImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    order.setStrategicPoint1(order.getStrategicPoint2());
                    order.setStrategicPoint2(order.getStrategicPoint3());
                    order.setStrategicPoint3(new FieldBattlePositionDTO());
                    order.getStrategicPoint3().reset();
                    if (BaseStore.getInstance().isStartRound()) {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    } else {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                    }
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            });
            spPanel.add(rmImg);
        }
        if (order.hasStrategicPoint2()) {
            spPanel.add(createLabel(order.getStrategicPoint2().toString() + ","));
            final ImageButton rmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png", "Remove position");
            rmImg.setSize("20px", "20px");
            rmImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    order.setStrategicPoint2(order.getStrategicPoint3());
                    order.setStrategicPoint3(new FieldBattlePositionDTO());
                    order.getStrategicPoint3().reset();
                    if (BaseStore.getInstance().isStartRound()) {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    } else {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                    }
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            });
            spPanel.add(rmImg);
        }
        if (order.hasStrategicPoint3()) {
            spPanel.add(createLabel(order.getStrategicPoint3().toString() + ","));
            final ImageButton rmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png", "Remove position");
            rmImg.setSize("20px", "20px");
            rmImg.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    order.getStrategicPoint3().reset();
                    if (BaseStore.getInstance().isStartRound()) {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    } else {
                        MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound() - 1);
                        MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                    }
                    MainPanel.getInstance().getMapUtils().openBrigadeMenu(brigade, false, MainPanel.getInstance().getPlayback().getRound() - 1);
                }
            });
            spPanel.add(rmImg);
            spButton.setVisible(false);
        }
        spPanel.add(spButton);
    }

    private Label createLabel(final String text) {
        final Label out = new Label(text);
        out.setStyleName("whiteText");
        return out;
    }

    private Label createLabel(final String text, final Label label) {
        label.setText(text);
        label.setStyleName("whiteText");
        return label;
    }

    public void fixBrigadeOrders() {
        if (brigade.getBasicOrder() == null) {
            brigade.setBasicOrder(createNewOrder());
        }
        if (brigade.getBasicOrder().getOriginalOrder() == null) {
            brigade.getBasicOrder().setOriginalOrder(createNewOrder());
        }
        if (brigade.getAdditionalOrder() == null) {
            brigade.setAdditionalOrder(createNewOrder());
        }
        if (brigade.getAdditionalOrder().getOriginalOrder() == null) {
            brigade.getAdditionalOrder().setOriginalOrder(createNewOrder());
        }
    }

    public static FieldBattleOrderDTO createNewOrder() {
        final FieldBattleOrderDTO out = new FieldBattleOrderDTO();
        out.setOrderType("SELECT_AN_ORDER");
        out.setCheckPoint1(new FieldBattlePositionDTO());
        out.setCheckPoint2(new FieldBattlePositionDTO());
        out.setCheckPoint3(new FieldBattlePositionDTO());
        out.setStrategicPoint1(new FieldBattlePositionDTO());
        out.setStrategicPoint2(new FieldBattlePositionDTO());
        out.setStrategicPoint3(new FieldBattlePositionDTO());
        out.setCustomStrategicPoint1(new FieldBattlePositionDTO());
        out.setCustomStrategicPoint2(new FieldBattlePositionDTO());
        out.setCustomStrategicPoint3(new FieldBattlePositionDTO());
        out.setMaintainDistance(5);
        out.setFormation("COLUMN");

        //default values
        out.getCheckPoint1().setY(-1);
        out.getCheckPoint1().setX(-1);
        out.getCheckPoint2().setY(-1);
        out.getCheckPoint2().setX(-1);
        out.getCheckPoint3().setY(-1);
        out.getCheckPoint3().setX(-1);

        out.getStrategicPoint1().setY(-1);
        out.getStrategicPoint1().setX(-1);
        out.getStrategicPoint2().setY(-1);
        out.getStrategicPoint2().setX(-1);
        out.getStrategicPoint3().setY(-1);
        out.getStrategicPoint3().setX(-1);

        out.getCustomStrategicPoint1().setY(-1);
        out.getCustomStrategicPoint1().setX(-1);
        out.getCustomStrategicPoint2().setY(-1);
        out.getCustomStrategicPoint2().setX(-1);
        out.getCustomStrategicPoint3().setY(-1);
        out.getCustomStrategicPoint3().setX(-1);

        out.setTargetNations(new TreeSet<Integer>());
        for (Integer nationId : BaseStore.getInstance().getEnemyNations()) {
            out.getTargetNations().add(nationId);
        }
        return out;
    }

    public List<FormationEnum> getAvailableFormations() {
        final List<FormationEnum> out = new ArrayList<FormationEnum>();
        for (FieldOrdersConstants.FormationEnum entry : FieldOrdersConstants.FormationEnum.values()) {
            if (canFormFormation(brigade, entry)) {
                out.add(entry);
            }
        }
        return out;
    }

    public boolean canFormFormation(BrigadeDTO brigade, FormationEnum formation) {
        int formationCapableBattalions = 0;
        for (BattalionDTO battalion : brigade.getBattalions()) {
            switch (formation) {
                case SKIRMISH:
                    formationCapableBattalions += battalion.getEmpireArmyType().getFormationSk() ? 1 : 0;
                    break;
                case LINE:
                    formationCapableBattalions += battalion.getEmpireArmyType().getFormationLi() ? 1 : 0;
                    break;
                case SQUARE:
                    formationCapableBattalions += battalion.getEmpireArmyType().getFormationSq() ? 1 : 0;
                    break;
                case COLUMN:
                    return true;
                default:
                    return false;
            }
        }
        return formationCapableBattalions * 2 > brigade.getBattalions().size();
    }

    public void copyOrder(final FieldBattleOrderDTO order1, final FieldBattleOrderDTO order2) {
        order1.setOrderType(order2.getOrderType());
        order1.setActivationRound(order2.getActivationRound());
        order1.getCheckPoint1().setX(order2.getCheckPoint1().getX());
        order1.getCheckPoint1().setY(order2.getCheckPoint1().getY());
        order1.getCheckPoint2().setX(order2.getCheckPoint2().getX());
        order1.getCheckPoint2().setY(order2.getCheckPoint2().getY());
        order1.getCheckPoint3().setX(order2.getCheckPoint3().getX());
        order1.getCheckPoint3().setY(order2.getCheckPoint3().getY());
        order1.setConstructionCounter(order2.getConstructionCounter());
        order1.getCustomStrategicPoint1().setX(order2.getCustomStrategicPoint1().getX());
        order1.getCustomStrategicPoint1().setY(order2.getCustomStrategicPoint1().getY());
        order1.getCustomStrategicPoint2().setX(order2.getCustomStrategicPoint2().getX());
        order1.getCustomStrategicPoint2().setY(order2.getCustomStrategicPoint2().getY());
        order1.getCustomStrategicPoint3().setX(order2.getCustomStrategicPoint3().getX());
        order1.getCustomStrategicPoint3().setY(order2.getCustomStrategicPoint3().getY());
        order1.setEnemySideCapturedOwnStrategicPoint(order2.isEnemySideCapturedOwnStrategicPoint());
        order1.setFormation(order2.getFormation());
        order1.setHeadCountThreshold(order2.getHeadCountThreshold());
        order1.setId(order2.getId());
        order1.setLastDestinationReached(order2.isLastDestinationReached());
        order1.setMaintainDistance(order2.getMaintainDistance());
        order1.setReachedCheckpoint1(order2.isReachedCheckpoint1());
        order1.setReachedCheckpoint2(order2.isReachedCheckpoint2());
        order1.setReachedCheckpoint3(order2.isReachedCheckpoint3());
        order1.setOwnSideCapturedEnemyStrategicPoint(order2.isOwnSideCapturedEnemyStrategicPoint());
        order1.getStrategicPoint1().setX(order2.getStrategicPoint1().getX());
        order1.getStrategicPoint1().setY(order2.getStrategicPoint1().getY());
        order1.getStrategicPoint2().setX(order2.getStrategicPoint2().getX());
        order1.getStrategicPoint2().setY(order2.getStrategicPoint2().getY());
        order1.getStrategicPoint3().setX(order2.getStrategicPoint3().getX());
        order1.getStrategicPoint3().setY(order2.getStrategicPoint3().getY());
        order1.setTargetArm(order2.getTargetArm());
        order1.setTargetClosestInRange(order2.isTargetClosestInRange());
        order1.setTargetFormation(order2.getTargetFormation());
        order1.setTargetHighestHeadcount(order2.isTargetHighestHeadcount());
        order1.setTargetNations(order2.getTargetNations());
        order1.setLeaderId(order2.getLeaderId());
        order1.setDetachmentPosition(order2.getDetachmentPosition());
    }

    public void recoverOriginalOrders() {
        copyOrder(order, order.getOriginalOrder());
    }

    public void lockBrigade() {
        if (isBasic && !BaseStore.getInstance().isStartRound()
                && !BaseStore.getInstance().isGameEnded()) {
            BaseStore.getInstance().orderUndoEdited(brigade.getBrigadeId());
            brigade.setFieldBattleUpdateMiddleRound(false);
            MainPanel.getInstance().getMapUtils().removeOrderChangedGraphic(brigade);
        }
        locker1.setVisible(true);
        locker2.setVisible(true);
        locker3.setVisible(true);
        MainPanel.getInstance().updateNationCommanderPanels();

    }

    public void unlockBrigade() {
        if (isBasic && !BaseStore.getInstance().isStartRound()
                && !BaseStore.getInstance().isGameEnded()) {
            BaseStore.getInstance().orderEdited(brigade.getBrigadeId());
            brigade.setFieldBattleUpdateMiddleRound(true);
            MainPanel.getInstance().getMapUtils().addOrderChangedGraphic(brigade);
        }

        locker1.setVisible(false);
        locker2.setVisible(false);
        locker3.setVisible(false);
        MainPanel.getInstance().updateNationCommanderPanels();
    }

    
}
