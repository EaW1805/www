package com.eaw1805.www.scenario.views.menu;

import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.infopanels.BrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.scenario.widgets.MenuPopup;


public class ArmyTypeBrigMenu extends MenuPopup {

    int nationId;
    final BattalionDTO battalion;
    final BrigadeInfoPanel infoPanel;
    public ArmyTypeBrigMenu(final Widget parent, final int nationId, final BattalionDTO battalion, final BrigadeInfoPanel infoPanel) {
        super(parent);
        this.nationId = nationId;
        this.battalion = battalion;
        this.infoPanel = infoPanel;
    }

    @Override
    public void initChildren() {
        for (final ArmyTypeDTO armyType : EditorStore.getInstance().getArmyTypesByNation(nationId)) {

            addChild(armyType.getName(), new BasicHandler() {
                @Override
                public void run() {
                    battalion.setExperience(armyType.getMaxExp());
                    battalion.setEmpireArmyType(armyType);
                    infoPanel.setUpImages();
                    hide();

                }
            });
        }
    }
}
