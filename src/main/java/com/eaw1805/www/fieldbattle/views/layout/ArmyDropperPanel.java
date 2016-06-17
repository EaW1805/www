package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.eaw1805.www.fieldbattle.stores.utils.MapConstants;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;

public class ArmyDropperPanel extends AbsolutePanel implements MapConstants {

    public ArmyDropperPanel() {

    }

    public void initPanel() {
        final MapUtils mapUtils = MainPanel.getInstance().getMapUtils();
        setSize((mapUtils.getNumTilesX()*TILE_WIDTH)+"px", (mapUtils.getNumTilesY()*TILE_HEIGHT)+"px");

        final Grid grid = new Grid(mapUtils.getNumTilesY(), mapUtils.getNumTilesY());
        add(grid);
    }


}
