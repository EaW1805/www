package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.stores.BaseStore;


public class GameEndedBigPanel extends AbsolutePanel {

    public GameEndedBigPanel() {
        setSize("580px", "198px");
        setStyleName("disablePointerEvents", true);

        if (BaseStore.getInstance().isGameEnded()) {
            Label gameEndedLbl = new Label("Fieldbattle finished!");
            gameEndedLbl.setStyleName("clearFontExtreme whiteText");
            add(gameEndedLbl, 29, 5);
            Label winnerLbl = new Label();
            switch (BaseStore.getInstance().getWinner()) {
                case 0:
                    winnerLbl.setText("Side A Won the battle!");
                    break;
                case 1:
                    winnerLbl.setText("Side B Won the battle!");
                    break;
                default:
                    winnerLbl.setText("Battle was undecided!");
            }
            winnerLbl.setStyleName("clearFontExtreme whiteText");
            add(winnerLbl, 5, 96);
        }
    }
}
