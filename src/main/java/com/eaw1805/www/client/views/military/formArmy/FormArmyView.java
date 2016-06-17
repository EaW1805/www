package com.eaw1805.www.client.views.military.formArmy;


import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.widgets.Disposable;
import com.eaw1805.www.client.widgets.FormArmiesDragController;

import java.util.ArrayList;
import java.util.List;

public class FormArmyView extends AbsolutePanel implements Disposable {
    private PickupDragController dragController = new PickupDragController(this, false);
    private FormArmy formArmy;
    private ArmiesSelector faSel;
    private FreeCorpsSelector fcSel;
    private SectorDTO sector;

    public FormArmyView(List<ArmyDTO> armies, SectorDTO sector) {
        dragController.addDragHandler(new FormArmiesDragController());
        dragController.setBehaviorDragStartSensitivity(3);
        setSize("1134px", "544px");
        this.sector = sector;
        faSel = new ArmiesSelector(armies, sector, this);
        if (armies.size() > 0) {
            for (ArmyDTO army : armies) {
                if (army.getArmyId() == 0) {
                    fcSel = new FreeCorpsSelector(new ArrayList<CorpDTO>(army.getCorps().values()), dragController, sector, this);
                    break;
                }
            }
        }

        if (fcSel == null) {
            fcSel = new FreeCorpsSelector(null, dragController, sector, this);
        }
        this.add(faSel, 954, 0);
        this.add(fcSel, 0, 0);

        formArmy = new FormArmy(dragController, null, this, sector);
        this.add(formArmy, 207, 0);
    }

    public void createNewArmy() {
        formArmy.unregisterControllers();
        formArmy.removeGWTHandlers();
        this.remove(formArmy);
        formArmy = new FormArmy(dragController, null, this, sector);
        this.add(formArmy, 207, 0);
        fcSel.reInitCorps();
    }

    public void changeOldArmy(ArmyDTO army) {
        formArmy.unregisterControllers();
        formArmy.removeGWTHandlers();
        this.remove(formArmy);
        formArmy = new FormArmy(dragController, army, this, sector);
        this.add(formArmy, 207, 0);
        fcSel.reInitCorps();
    }

    public FormArmy getFormArmyPanel() {
        return formArmy;
    }

    public void reInitCorps() {
        fcSel.reInitCorps();
    }

    @Override
    public void removeGWTHandlers() {
        faSel.removeGWTHandlers();
        formArmy.removeGWTHandlers();
    }
}
