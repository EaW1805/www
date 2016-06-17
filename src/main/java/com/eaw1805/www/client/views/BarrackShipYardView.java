package com.eaw1805.www.client.views;

import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.shared.stores.units.NavyStore;

public class BarrackShipYardView extends DraggablePanel {
    private final BarracksView barracksView;
    private final BarrackDTO thisBarrack;

    public BarrackShipYardView(final BarrackDTO barrShip, final SectorDTO barrSectorDto) {
        thisBarrack = barrShip;
        this.setSize("522px", "437px");
        this.barracksView = new BarracksView(barrShip, barrSectorDto, this, NavyStore.getInstance().getShipTypesList());
        this.add(barracksView, 0, 0);
    }


    public BarrackDTO getThisBarrack() {
        return thisBarrack;
    }
}
