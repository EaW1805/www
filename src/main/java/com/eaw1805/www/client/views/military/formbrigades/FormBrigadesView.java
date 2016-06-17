package com.eaw1805.www.client.views.military.formbrigades;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.widgets.Disposable;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.List;

public class FormBrigadesView extends AbsolutePanel implements Disposable {

    private transient final BrigadeSelector bFirstSel, bSecondSel;
    private transient final ExchangeBattalions exchBatt;

    public FormBrigadesView(final SectorDTO sector) {
        final List<BrigadeDTO> brigades = ArmyStore.getInstance().getBrigadesByPosition(sector.getRegionId(), sector.getX(), sector.getY(), false);
        bFirstSel = new BrigadeSelector(brigades, this, 1);
        bSecondSel = new BrigadeSelector(brigades, this, 2);
        exchBatt = new ExchangeBattalions(this);

        setSize("1134px", "544px");
        add(bFirstSel, 0, 0);
        add(bSecondSel, 954, 0);
        add(exchBatt, 207, 0);
    }

    public void selectBrigade(final int listNo, final BrigadeDTO brigade) {
        if (listNo == 1) {
            bSecondSel.removeBrigade(brigade.getBrigadeId());

        } else {
            bFirstSel.removeBrigade(brigade.getBrigadeId());
        }

        exchBatt.setSelectedBrigade(listNo, brigade);
    }

    public BrigadeSelector getBFirstSelector () {
        return bFirstSel;
    }

    public BrigadeSelector getBSecondSelector () {
        return bSecondSel;
    }

    @Override
    public void removeGWTHandlers() {
        exchBatt.removeGWTHandlers();
    }
}
