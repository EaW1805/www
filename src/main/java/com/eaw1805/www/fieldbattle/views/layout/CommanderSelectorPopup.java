package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.mini.CommanderInfoMini;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.SelectList;
import com.eaw1805.www.fieldbattle.widgets.StyledCheckBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CommanderSelectorPopup extends PopupPanel {

    AbsolutePanel container;
    final BrigadeDTO brigade;
    final SelectList<CommanderDTO> selectList;
    BasicHandler handler;

    public CommanderSelectorPopup(final BrigadeDTO brig) {
        setStyleName("none");
        addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                event.stopPropagation();
                event.preventDefault();
            }
        }, MouseDownEvent.getType());
        addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            }
        }, ClickEvent.getType());
        setAutoHideEnabled(true);
        container = new AbsolutePanel();
        setWidget(container);
        brigade = brig;
        container.setStyleName("fieldArmySelector");
        container.setSize("194px", "350px");

        final StyledCheckBox overallCheck = new StyledCheckBox("Overall Commander", false, false, Tips.OVERALL_COMMANDER);

        overallCheck.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                if (selectList.hasSelectedOption()) {
                    for (BrigadeDTO curBrig : ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId())) {
                        if (curBrig.getFieldBattleOverallCommanderId() == selectList.getSelectedValue().getId()) {
                            curBrig.setFieldBattleOverallCommanderId(0);
                            if (curBrig.isPlacedOnFieldMap()) {
                                MainPanel.getInstance().getMapUtils().removeBrigadeImage(curBrig, 0);
                                MainPanel.getInstance().getMapUtils().addArmyImage(curBrig, true, false);
                            }
                        }
                        if (curBrig.getFieldBattleCommanderId() == selectList.getSelectedValue().getId()) {
                            curBrig.setFieldBattleCommanderId(0);
                            if (curBrig.isPlacedOnFieldMap()) {
                                MainPanel.getInstance().getMapUtils().removeBrigadeImage(curBrig, 0);
                                MainPanel.getInstance().getMapUtils().addArmyImage(curBrig, true, false);
                            }
                        }
                        if (overallCheck.isChecked() && curBrig.getFieldBattleOverallCommanderId() != 0) {
                            curBrig.setFieldBattleCommanderId(curBrig.getFieldBattleOverallCommanderId());
                            curBrig.setFieldBattleOverallCommanderId(0);
                            if (curBrig.isPlacedOnFieldMap()) {
                                MainPanel.getInstance().getMapUtils().removeBrigadeImage(curBrig, 0);
                                MainPanel.getInstance().getMapUtils().addArmyImage(curBrig, true, false);
                            }
                        }
                    }
                    if (overallCheck.isChecked()) {
                        brigade.setFieldBattleOverallCommanderId(selectList.getSelectedValue().getId());
                        brigade.setFieldBattleCommanderId(0);

                    } else {
                        brigade.setFieldBattleCommanderId(selectList.getSelectedValue().getId());
                        brigade.setFieldBattleOverallCommanderId(0);
                    }

                    MainPanel.getInstance().removePanelFromScreen(CommanderSelectorPopup.this);
                    MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                    MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                    MainPanel.getInstance().updateNationCommanderPanels();
                    if (handler != null) {
                        handler.run();
                    }
                }
            }
        });
        overallCheck.setChecked(brig.getFieldBattleOverallCommanderId() > 0);
        container.add(overallCheck, 9, 53);

        selectList = new SelectList<CommanderDTO>() {
            @Override
            public void onChange(final Widget option, final CommanderDTO value) {
                for (BrigadeDTO curBrig : ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId())) {
                    if (curBrig.getFieldBattleOverallCommanderId() == selectList.getSelectedValue().getId()) {
                        curBrig.setFieldBattleOverallCommanderId(0);
                        if (curBrig.isPlacedOnFieldMap()) {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(curBrig, 0);
                            MainPanel.getInstance().getMapUtils().addArmyImage(curBrig, true, false);
                        }
                    }
                    if (curBrig.getFieldBattleCommanderId() == selectList.getSelectedValue().getId()) {
                        curBrig.setFieldBattleCommanderId(0);
                        if (curBrig.isPlacedOnFieldMap()) {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(curBrig, 0);
                            MainPanel.getInstance().getMapUtils().addArmyImage(curBrig, true, false);
                        }
                    }
                    if (overallCheck.isChecked() && curBrig.getFieldBattleOverallCommanderId() != 0) {
                        curBrig.setFieldBattleCommanderId(curBrig.getFieldBattleOverallCommanderId());
                        curBrig.setFieldBattleOverallCommanderId(0);
                        if (curBrig.isPlacedOnFieldMap()) {
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(curBrig, 0);
                            MainPanel.getInstance().getMapUtils().addArmyImage(curBrig, true, false);
                        }
                    }
                }
                if (overallCheck.isChecked()) {
                    brigade.setFieldBattleOverallCommanderId(value.getId());
                    brigade.setFieldBattleCommanderId(0);
                } else {
                    brigade.setFieldBattleCommanderId(value.getId());
                    brigade.setFieldBattleOverallCommanderId(0);
                }

                MainPanel.getInstance().removePanelFromScreen(CommanderSelectorPopup.this);
                MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                MainPanel.getInstance().updateNationCommanderPanels();
                if (handler != null) {
                    handler.run();
                }
            }
        };
        selectList.setSize(174, 250);


        container.add(selectList, 9, 85);
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png", "Close panel");
        imgX.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                hide();
            }
        });
        container.add(imgX, 147, 4);
        initCommanders();
    }

    public void setOnChangeHandler(BasicHandler handler) {
        this.handler = handler;
    }

    public void initCommanders() {
        final List<CommanderDTO> commanders = new ArrayList<CommanderDTO>();
        commanders.addAll(CommanderStore.getInstance().getCommanders().get(BaseStore.getInstance().getNationId()));
        selectList.clearOptions();

        for (final CommanderDTO comm : commanders) {
            selectList.addOption(new CommanderInfoMini(comm), comm);
        }
        final CommanderDTO selectedComm;
        if (brigade.getFieldBattleCommanderId() > 0) {
            selectedComm = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleCommanderId());
        } else if (brigade.getFieldBattleOverallCommanderId() > 0) {
            selectedComm = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleOverallCommanderId());
        } else {
            selectedComm = null;
        }
        if (selectedComm != null) {
            selectList.selectOptionByValue(selectedComm, new Comparator<CommanderDTO>() {
                @Override
                public int compare(CommanderDTO o1, CommanderDTO o2) {
                    return o1.getId() - o2.getId();
                }
            }, false);
        }

    }
}
