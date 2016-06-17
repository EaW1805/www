package com.eaw1805.www.scenario.views.infopanels;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.RankDTO;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.OptionEAW;
import com.eaw1805.www.fieldbattle.widgets.SelectEAW;
import com.eaw1805.www.scenario.stores.CommanderUtils;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.EditorPanel;


public class CommanderInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {



    final AbsolutePanel mainPanel;
    final TextBox commName;
    final TextBox commComC;
    final TextBox commStrC;
    final SelectEAW<RankDTO> rankSelect;
    final CommanderDTO comm;
    public CommanderInfoPanel(final CommanderDTO commander) {
        comm = commander;
        setSize("366px", "90px");

        setStyleName("commanderInfoPanel");
        setSize("363px", "87px");
        mainPanel = new AbsolutePanel();
        add(mainPanel, 0, 0);

        commName = new TextBox();


        rankSelect = new SelectEAW<RankDTO>("Rank, click to change") {
            @Override
            public void onChange() {
                //do nothing here
            }
        };

        for (RankDTO rank : EditorStore.getInstance().getRanks()) {
            rankSelect.addOption(new OptionEAW(130, 15, rank.getName()), rank);
        }

        commComC = new TextBox();
        commStrC = new TextBox();

        final ImageButton updateImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png", "Update commander");
        updateImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                try {
                    commander.setComc(Integer.parseInt(commComC.getText()));
                } catch (Exception e) {}
                try{
                    commander.setStrc(Integer.parseInt(commStrC.getText()));
                } catch (Exception e) {}
                if (!commName.getText().isEmpty()) {
                    commander.setName(commName.getText());
                }
                commander.setRank(rankSelect.getValue());
                setupPanel();
            }
        });


        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete commander");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                EditorStore.getInstance().getCommanders().get(commander.getRegionId()).get(commander.getX()).get(commander.getY()).remove(commander.getId());
                //then remove it from map
                EditorMapUtils.getInstance().drawCommanders(commander.getRegionId());
                //then remove it from parent
                removeFromParent();
                //finally update commanders overview
                EditorPanel.getInstance().getCommOverview().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");

        add(commName, 3, 3);
        add(rankSelect, 3, 40);
        add(commComC, 180, 3);
        add(commStrC, 180, 40);
        add(deleteImg, 320, 60);
        add(updateImg, 290, 60);
        setupPanel();
    }

    public void setupPanel() {
        commName.setText(comm.getName());
        //now select the selected rank
        rankSelect.selectOptionByValue(comm.getRank());
        commComC.setText(String.valueOf(comm.getComc()));
        commStrC.setText(String.valueOf(comm.getStrc()));
    }


}
