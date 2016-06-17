package com.eaw1805.www.fieldbattle.widgets;

import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.Group;

public class FieldBattleMapGroup extends Group {

    protected int round;

    public FieldBattleMapGroup(int round) {
        this.round = round;
    }

    public void show() {
        if (BaseStore.getInstance().isStartRound()) {
            MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).add(this);
        } else {
            MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).add(this);
        }
    }

    public void hide() {
        if (isAttached()) {
            if (BaseStore.getInstance().isStartRound()) {
                MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).remove(this);
            } else {
                MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).remove(this);
            }
        }
    }

    public void showOnMinimap() {
        MainPanel.getInstance().getMiniMap().getMap().add(this);
    }

    public void hideFromMinimap() {
        if (isAttached()) {
            MainPanel.getInstance().getMiniMap().getMap().remove(this);
        }
    }

}
