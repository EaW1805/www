package com.eaw1805.www.scenario.views.overview;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.NaturalResourcesConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.stores.RegionSettings;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.scenario.widgets.MenuElementWidget;

import java.util.HashMap;
import java.util.Map;

public class MapOverView extends VerticalPanel {

    Grid grid;
    public MapOverView() {
        grid = new Grid(17, 19);

        getElement().getStyle().setBackgroundColor("grey");
        for (NationDTO nation : EditorStore.getInstance().getNations()) {
            if (nation.getNationId() > 0) {
                grid.setWidget(nation.getNationId() - 1, 0, new Label(nation.getName().substring(0, 4)));
                grid.setWidget(nation.getNationId() - 1, 1, new Label("S"));
                grid.setWidget(nation.getNationId() - 1, 2, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 3, new Label("PS"));
                grid.setWidget(nation.getNationId() - 1, 4, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 5, new Label("R"));
                grid.setWidget(nation.getNationId() - 1, 6, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 7, new Label("M"));
                grid.setWidget(nation.getNationId() - 1, 8, new Label("0"));

                grid.setWidget(nation.getNationId() - 1, 9, new Label("PC"));
                grid.setWidget(nation.getNationId() - 1, 10, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 11, new Label("TC"));
                grid.setWidget(nation.getNationId() - 1, 12, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 13, new Label("CH"));
                grid.setWidget(nation.getNationId() - 1, 14, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 15, new Label("CS"));
                grid.setWidget(nation.getNationId() - 1, 16, new Label("0"));
                grid.setWidget(nation.getNationId() - 1, 17, new Label("CO"));
                grid.setWidget(nation.getNationId() - 1, 18, new Label("0"));
            }
        }
        add(grid);
    }

    public void updateOverView() {
        try {
        Map<Integer, Integer[]> nationToStats = new HashMap<Integer, Integer[]>();
        for (int index = 1; index <= 17;index++) {
            nationToStats.put(index, new Integer[9]);
            nationToStats.get(index)[0] = 0;
            nationToStats.get(index)[1] = 0;
            nationToStats.get(index)[2] = 0;
            nationToStats.get(index)[3] = 0;
            nationToStats.get(index)[4] = 0;
            nationToStats.get(index)[5] = 0;
            nationToStats.get(index)[6] = 0;
            nationToStats.get(index)[7] = 0;
            nationToStats.get(index)[8] = 0;
        }
        for (SectorDTO sector : RegionSettings.sectors) {
            if (sector.getNationDTO() != null && sector.getNationDTO().getNationId() > 0) {
                nationToStats.get(sector.getNationDTO().getNationId())[0]++;
                if (sector.getProductionSiteDTO() != null) {
                    nationToStats.get(sector.getNationDTO().getNationId())[1]++;
                }
                if (sector.getNatResDTO() != null) {
                    nationToStats.get(sector.getNationDTO().getNationId())[2]++;
                    if (sector.getNatResDTO().getId() == NaturalResourcesConstants.NATRES_ORE
                            || sector.getNatResDTO().getId() == NaturalResourcesConstants.NATRES_GEMS
                            || sector.getNatResDTO().getId() == NaturalResourcesConstants.NATRES_METALS) {
                        nationToStats.get(sector.getNationDTO().getNationId())[3]++;
                    }
                }
                nationToStats.get(sector.getNationDTO().getNationId())[4] += sector.populationCount();
                nationToStats.get(sector.getNationDTO().getNationId())[5] += sector.populationCount() * calculateTaxRate(sector);
                if (getSphere(sector, sector.getNationDTO()) == 1) {
                    nationToStats.get(sector.getNationDTO().getNationId())[6] += sector.populationCount();
                } else if (getSphere(sector, sector.getNationDTO()) == 2) {
                    nationToStats.get(sector.getNationDTO().getNationId())[7] += sector.populationCount();
                } else {
                    nationToStats.get(sector.getNationDTO().getNationId())[8] += sector.populationCount();
                }

            }

        }
            for (int index = 1; index <= 17; index++) {
                grid.setWidget(index - 1, 2, new Label(String.valueOf(nationToStats.get(index)[0])));
                grid.setWidget(index - 1, 4, new Label(String.valueOf(nationToStats.get(index)[1])));
                grid.setWidget(index - 1, 6, new Label(String.valueOf(nationToStats.get(index)[2])));
                grid.setWidget(index - 1, 8, new Label(String.valueOf(nationToStats.get(index)[3])));
                grid.setWidget(index - 1, 10, new Label(String.valueOf(nationToStats.get(index)[4])));
                grid.setWidget(index - 1, 12, new Label(String.valueOf(nationToStats.get(index)[5])));
                grid.setWidget(index - 1, 14, new Label(String.valueOf(nationToStats.get(index)[6])));
                grid.setWidget(index - 1, 16, new Label(String.valueOf(nationToStats.get(index)[7])));
                grid.setWidget(index - 1, 18, new Label(String.valueOf(nationToStats.get(index)[8])));
            }
        } catch (Exception e) {
            Window.alert("Fff? " + e.toString());
        }
    }

    /**
     * Identify taxation rate.
     *
     * @param sector the sector to examine.
     * @return the taxation rate.
     */
    private int calculateTaxRate(final SectorDTO sector) {
        final int baseTax;
        final int sphere = getSphere(sector, sector.getNationDTO());

        // Check if this is a colonial sector

        // check sphere
        switch (sphere) {
            case 1:
                baseTax = sector.getNationDTO().getTaxRate();
                break;

            case 2:
                baseTax = 4;
                break;

            case 3:
            default:
                baseTax = 3;
                break;
        }


        return baseTax;
    }

    /**
     * Identify if sector is a home region, inside sphere of influence, or outside of the receiving nation.
     *
     * @param sector   the sector to examine.
     * @param receiver the receiving nation.
     * @return 1 if home region, 2 if in sphere of influence, 3 if outside.
     */
    protected final int getSphere(final SectorDTO sector, final NationDTO receiver) {
        final char thisNationCodeLower = String.valueOf(receiver.getCode()).toLowerCase().charAt(0);
        final char thisSectorCodeLower = String.valueOf(sector.getPoliticalSphere()).toLowerCase().charAt(0);
        int sphere = 1;

        // Check if this is not home region
        if (thisNationCodeLower != thisSectorCodeLower) {
            sphere = 2;

            // Check if this is outside sphere of influence
            if (receiver.getSphereOfInfluence().toLowerCase().indexOf(thisSectorCodeLower) < 0) {
                sphere = 3;
            }
        }

        return sphere;
    }

}
