package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.utils.AnimationUtils;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.TitlePanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;

public class ResultPanel extends AbsolutePanel {

    public ResultPanel() {
        setSize("752px", "496px");
        if (BaseStore.getInstance().wonTheBattle()) {
            setStyleName("battleResultVictory");
        } else if (BaseStore.getInstance().lostTheBattle()) {
            setStyleName("battleResultDefeat");
        } else {
            setStyleName("battleResultDraw");
        }

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png", "Close panel");
        imgX.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                setStyleName("disablePointerEvents", true);
                AnimationUtils.hideElement(ResultPanel.this, new BasicHandler() {
                    @Override
                    public void run() {
                        MainPanel.getInstance().removePanelFromScreen(ResultPanel.this);
                    }
                });
            }
        });
        add(imgX, 685, 26);

        //add the players flag
        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + BaseStore.getInstance().getNationId() + "-120.png");
        flag.setSize("191px", "123px");
        final AbsolutePanel flagContainer = new AbsolutePanel();
        flagContainer.add(flag, 0, 0);

        if (BaseStore.getInstance().wonTheBattle()) {
            //if won then the flag takes all the space
            flagContainer.setSize("191px", "123px");
            add(flagContainer, 281, 190);

        } else if (BaseStore.getInstance().drawTheBattle()) {
            //if draw then the flag is taking only the half of the space
            flagContainer.setSize("96px", "123px");
            add(flagContainer, 281, 190);
        }

        //add the enemy flag
        final Image enemyFlag = new Image("http://static.eaw1805.com/images/nations/nation-" + BaseStore.getInstance().getEnemyNations().get(0) + "-120.png");
        enemyFlag.setSize("191px", "123px");
        final AbsolutePanel enemyFlagContainer = new AbsolutePanel();
        enemyFlagContainer.add(enemyFlag);
        if (BaseStore.getInstance().lostTheBattle()) {
            //if player lost then the enemy flag takes all the space
            enemyFlagContainer.setSize("191px", "123px");
            add(enemyFlagContainer, 281, 190);
        } else if (BaseStore.getInstance().drawTheBattle()) {
            //if draw then the enemy flag takes the second half of the space
            enemyFlagContainer.setSize("96px", "123px");
            enemyFlagContainer.setWidgetPosition(enemyFlag, -95, 0);
            add(enemyFlagContainer, 377, 190);
        }

        final CommanderDTO comm = CommanderStore.getInstance().getBaseCommander();

        final Image commander;
        if (comm != null) {
            commander = new Image("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + comm.getNationId() + "/" + comm.getIntId() + ".jpg");
        } else {
            commander = new Image("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
        }

        commander.setSize("122px", "122px");
        add(commander, 65, 181);

        final CommanderDTO enemyComm = CommanderStore.getInstance().getBaseEnemyCommander();
        final Image enemyCommander;
        if (enemyComm == null) {
            enemyCommander = new Image("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
        } else {
            enemyCommander = new Image("http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + enemyComm.getNationId() + "/" + enemyComm.getIntId() + ".jpg");
        }

        enemyCommander.setSize("122px", "122px");
        add(enemyCommander, 565, 181);

        final Label battleTitle = new Label(BaseStore.getInstance().getTitle());
        battleTitle.setStyleName("clearFont-large");
        final Label battleDate = new Label(TitlePanel.getMonthByTurn(0) + " " + TitlePanel.getYearByTurn(0));
        battleDate.setStyleName("clearFont-large");
        add(battleTitle, 269, 32);
        add(battleDate, 302, 58);


    }

}
