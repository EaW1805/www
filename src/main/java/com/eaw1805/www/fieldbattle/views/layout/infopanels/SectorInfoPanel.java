package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;

public class SectorInfoPanel extends AbsolutePanel {

    private final AbsolutePanel sectorInfoContainer;

    public SectorInfoPanel() {
        setStyleName("sectorPanel");
        setSize("179px", "194px");

        final Image sectorImg = new Image("http://static.eaw1805.com/images/infopanels/InfoPanel1.png");
        sectorImg.setSize("167px", "141px");
        sectorImg.setStyleName("sectorPanelBG");
        add(sectorImg, 9, 45);

        sectorInfoContainer = new AbsolutePanel();
        add(sectorInfoContainer, 9, 45);
    }

    public void updateSectorInfo(final FieldBattleSectorDTO sector) {
        sectorInfoContainer.clear();
        sectorInfoContainer.add(new TerrainInfoPanel(sector));
    }



}
