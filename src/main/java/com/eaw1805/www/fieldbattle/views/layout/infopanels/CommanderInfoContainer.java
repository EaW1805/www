package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;

public class CommanderInfoContainer extends AbsolutePanel {

    final int nationId;
    final Image commImg;
    final Label lblName;
    final Label lblRank;
    final Label remainingOrders;
    public CommanderInfoContainer(final int nationId) {
        this.nationId = nationId;
        setStyleName("commanderFieldBattlePanel");
        setSize("160px", "60px");
        commImg = new Image("http://static.eaw1805.com/images/site/Generic_Naval_Commander.png");
        commImg.setSize("56px", "56px");
        add(commImg, 3, 3);
        lblName = new Label("None assigned");
        add(lblName, 60, 3);
        lblRank = new Label("");
        add(lblRank, 60, 15);
        lblRank.setStyleName("clearFontMini");
        remainingOrders = new Label("");
        add(remainingOrders, 60, 40);
    }

    public void updatePanel() {
        CommanderDTO comm = null;
        for (BrigadeDTO brigade : ArmyStore.getInstance().getPlacedBrigadesByNation(nationId)) {
            if (brigade.getFieldBattleOverallCommanderId() > 0) {
                comm = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleOverallCommanderId());
                break;
            }
            if (brigade.getFieldBattleCommanderId() > 0) {
                comm = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleCommanderId());
            }
        }
        if (comm != null) {
            commImg.setUrl("http://static.eaw1805.com/img/commanders/" + comm.getNationId() + "/" + comm.getIntId() + ".jpg");
            lblName.setText(comm.getName());
            lblRank.setText(comm.getRank().getName() +
                    " (" + comm.getStrc() + "-" + comm.getComc() + ")");
            if (BaseStore.getInstance().isStartRound()) {
                remainingOrders.setText(String.valueOf(CommanderStore.getInstance().getNumberOfOrdersAllowChange()));
            } else {
                remainingOrders.setText((CommanderStore.getInstance().getNumberOfOrdersAllowChange() - BaseStore.getInstance().getEditedOrders().size()) + " / " + CommanderStore.getInstance().getNumberOfOrdersAllowChange());
            }

        } else {
            commImg.setUrl("http://static.eaw1805.com/images/site/Generic_Naval_Commander.png");
            if (BaseStore.getInstance().isNationAllied(nationId)) {
                lblName.setText("None assigned");
                if (BaseStore.getInstance().isStartRound()) {
                    remainingOrders.setText(String.valueOf(CommanderStore.getInstance().getNumberOfOrdersAllowChange()));
                } else {
                    remainingOrders.setText((CommanderStore.getInstance().getNumberOfOrdersAllowChange() - BaseStore.getInstance().getEditedOrders().size()) + " / " + CommanderStore.getInstance().getNumberOfOrdersAllowChange());
                }
            } else {
                lblName.setText("Unknown");
            }

        }

    }




}
