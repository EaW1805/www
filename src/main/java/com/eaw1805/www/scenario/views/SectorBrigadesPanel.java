package com.eaw1805.www.scenario.views;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.RegionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.infopanels.BrigadeInfoPanel;

import java.util.ArrayList;
import java.util.List;

public class SectorBrigadesPanel extends PopupPanel {


    public SectorBrigadesPanel(final int regionId, final int x, final int y) {
        setAutoHideEnabled(true);
        getElement().getStyle().setBackgroundColor("grey");
        final VerticalPanel container = new VerticalPanel();
        final List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>(EditorStore.getInstance().getBrigades().get(regionId).get(x).get(y).values());
        for (BrigadeDTO brigade : brigades) {
            container.add(new BrigadeInfoPanel(brigade));
        }
        setWidget(container);
    }
}
