package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.DraggablePanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.StyledCheckBox;

public class SettingsPanel extends DraggablePanel {

    public final StyledCheckBox gridLines = new StyledCheckBox("Show Grid", false, false, "Show/Hide Grid");
    public final StyledCheckBox corpses = new StyledCheckBox("Show Dead Units", false, false, "Show/Hide Dead Units");
    public final StyledCheckBox units = new StyledCheckBox("Show Brigades", true, false, "Show/Hide Brigades");
    public final StyledCheckBox alliedUnits = new StyledCheckBox("Show Allied Units", true, false, "Show/Hide Allied Units");
    public final StyledCheckBox moveLines = new StyledCheckBox("Move Lines", true, false, "Show/Hide movement lines");

    public SettingsPanel() {
        setStyleName("settingsPanel");
        setStyleName("clearFont", true);
        setSize("490px", "341px");

        final Label title = new Label("Display options");
        title.setStyleName("clearFont-large whiteText");
        add(title, 25, 9);

        final SettingsPanel self = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png", "Close settings panel");
        imgX.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                self.removeFromParent();
            }
        });
        imgX.setStyleName("pointer");
        imgX.setSize("36px", "36px");
        add(imgX, 425, 9);


        gridLines.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                MainPanel.getInstance().getMapUtils().setShowGrid(gridLines.isChecked());
            }
        });
        add(gridLines, 8, 90);

        corpses.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                MainPanel.getInstance().getMapUtils().setShowCorpses(corpses.isChecked());
            }
        });
        add(corpses, 8, 120);

        units.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                MainPanel.getInstance().getMapUtils().setShowBrigades(units.isChecked());
            }
        });
        add(units, 8, 150);

        alliedUnits.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                MainPanel.getInstance().getMapUtils().setShowAlliedBrigades(alliedUnits.isChecked());
            }
        });
        add(alliedUnits, 8, 180);

        moveLines.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                if (moveLines.isChecked()) {
                    MainPanel.getInstance().getMapUtils().showMoveLines();
                } else {
                    MainPanel.getInstance().getMapUtils().hideMoveLines();
                }
                BaseStore.getInstance().setShowMoveLines(moveLines.isChecked());
            }
        });
        add(moveLines, 8, 210);

        final StyledCheckBox fireLines = new StyledCheckBox("Fire Lines", true, false, "Show/Hide fire lines");
        fireLines.addOnChangeHandler(new BasicHandler() {
            @Override
            public void run() {
                if (fireLines.isChecked()) {
                    MainPanel.getInstance().getMapUtils().showFireLines();
                } else {
                    MainPanel.getInstance().getMapUtils().hideFireLines();
                }
                BaseStore.getInstance().setShowFireLines(fireLines.isChecked());
            }
        });
        add(fireLines, 8, 240);
    }
}
