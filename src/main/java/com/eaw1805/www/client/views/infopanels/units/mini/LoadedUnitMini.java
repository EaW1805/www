package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.www.client.views.figures.BrigadeInfoFigPanel;
import com.eaw1805.www.client.views.figures.CommanderInfoFigPanel;
import com.eaw1805.www.client.views.figures.SpyInfoFigPanel;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

/**
 * This class creates the pop-up that shows the loaded units.
 */
public class LoadedUnitMini
        extends VerticalPanel
        implements ArmyConstants {

    public LoadedUnitMini(final TransportUnitDTO thisUnit, final int unitType) {
        final HorizontalPanel mainContainer = new HorizontalPanel();
        mainContainer.setStyleName("battalionInfoPanel");
        mainContainer.setStylePrimaryName("battalionInfoPanel");
        mainContainer.setStyleName("clickArmyPanel", true);
        add(mainContainer);

        if (thisUnit.getLoadedUnitsMap() != null) {
            for (final int unitId : thisUnit.getLoadedUnitsMap().get(unitType)) {
                switch (unitType) {
                    case BRIGADE:
                        final BrigadeInfoFigPanel bifp = new BrigadeInfoFigPanel(ArmyStore.getInstance().getBrigadeById(unitId), false, "");
                        mainContainer.add(bifp);
                        break;

                    case COMMANDER:
                        final CommanderInfoFigPanel cifp = new CommanderInfoFigPanel(CommanderStore.getInstance().getCommanderById(unitId), false, "");
                        mainContainer.add(cifp);
                        break;

                    case SPY:
                        final SpyInfoFigPanel sifp = new SpyInfoFigPanel(SpyStore.getInstance().getSpyById(unitId), false, "");
                        mainContainer.add(sifp);
                        break;

                    default:
                        //do nothing here
                }
            }
        }
    }

}
