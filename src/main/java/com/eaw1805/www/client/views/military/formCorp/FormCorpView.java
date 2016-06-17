package com.eaw1805.www.client.views.military.formCorp;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.widgets.Disposable;
import com.eaw1805.www.client.widgets.FormCorpDragController;

import java.util.ArrayList;
import java.util.List;

public class FormCorpView
        extends AbsolutePanel implements Disposable {

    private PickupDragController dragController = new PickupDragController(this, false);
    private FormCorp formCorp;
    private CorpsSelector fcSel;
    private SectorDTO sector;
    FreeBrigadesSelector fbSel;

    public FormCorpView(List<ArmyDTO> armies, SectorDTO sector) {
        dragController.setBehaviorDragStartSensitivity(3);
        dragController.addDragHandler(new FormCorpDragController());
        this.sector = sector;
        setSize("1134px", "544px");
        List<CorpDTO> corps = new ArrayList<CorpDTO>();
        if (armies.size() > 0) {
            for (ArmyDTO army : armies) {
                if (army.getCorps() != null && army.getCorps().size() > 0) {
                    corps.addAll(new ArrayList<CorpDTO>(army.getCorps().values()));
                }
                if (army.getArmyId() == 0) {
                    if (army.getCorps().containsKey(0)) {
                        fbSel = new FreeBrigadesSelector(new ArrayList<BrigadeDTO>(army.getCorps().get(0).getBrigades().values()), dragController, sector, this);
                    } else {
                        fbSel = new FreeBrigadesSelector(null, dragController, sector, this);
                    }
                }
            }
        }
        if (corps.size() > 0) {
            if (fcSel != null) {
                fcSel.removeGWTHandler();
            }
            fcSel = new CorpsSelector(corps, sector, this);
        }
        if (fcSel == null) {
            fcSel = new CorpsSelector(null, sector, this);
        }
        if (fbSel == null) {
            fbSel = new FreeBrigadesSelector(null, dragController, sector, this);
        }

        this.add(fbSel, 0, 0);
        this.add(fcSel, 954, 0);

        formCorp = new FormCorp(dragController, null, this, sector);
        this.add(formCorp, 207, 0);

    }

    public void onDetach() {
        super.onDetach();
        if (formCorp != null) {
            formCorp.removeUnitDestroyedHandler();
        }
        if (fcSel != null) {
            fcSel.removeGWTHandler();
        }

    }

    public void createNewCorp() {
        formCorp.unregisterControllers();
        formCorp.removeUnitDestroyedHandler();
        this.remove(formCorp);
        formCorp = new FormCorp(dragController, null, this, sector);
        this.add(formCorp, 207, 0);
        fbSel.reInitBrigades();
    }

    public void changeOldCorp(CorpDTO corp) {
        formCorp.unregisterControllers();
        formCorp.removeUnitDestroyedHandler();
        this.remove(formCorp);
        formCorp = new FormCorp(dragController, corp, this, sector);
        this.add(formCorp, 207, 0);
        fbSel.reInitBrigades();
    }

    public FormCorp getFormCorpPanel() {
        return formCorp;
    }

    public void reInitBrigades() {
        fbSel.reInitBrigades();
    }

    @Override
    public void removeGWTHandlers() {
        try{
            if (formCorp != null) {
                formCorp.removeUnitDestroyedHandler();
            }
        } catch (Exception e) {/*eat it*/}
    }
}
