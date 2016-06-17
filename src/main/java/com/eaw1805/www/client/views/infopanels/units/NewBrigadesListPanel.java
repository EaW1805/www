package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.army.BrigadeDTO;

import java.util.List;

public class NewBrigadesListPanel
        extends Composite
        implements RegionConstants {

    private BrigadeInfoPanel selectedBrigade;

    public NewBrigadesListPanel(final List<BrigadeDTO> thisNewBrigs, final boolean isCurrBarrack) {
        final VerticalPanel verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        verticalPanel.setSize("252px", "0");
        for (final BrigadeDTO brig : thisNewBrigs) {
            final BrigadeInfoPanel brigInfoPanel = new BrigadeInfoPanel(brig, false);
            verticalPanel.add(brigInfoPanel);
            if (isCurrBarrack) {
                brigInfoPanel.getBrigadePanel().addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {

                        for (int i = 0; i < verticalPanel.getWidgetCount(); i++) {
                            try {
                                final BrigadeInfoPanel brigPanel = (BrigadeInfoPanel) verticalPanel.getWidget(i);
                                if (getSelectedBrigade() != null
                                        && getSelectedBrigade().getBrigade().getBrigadeId() == brigPanel.getBrigade().getBrigadeId()) {
                                    brigPanel.removeStyleName("selectArmyPanel");
                                }

                                brigPanel.getBrigadePanel().setStyleName("brigadeInfoPanel");
                                brigPanel.getBrigadePanel().setStyleName("clickArmyPanel", true);
                                brigPanel.setSize("368px", "90px");
                            } catch (Exception ex) {
//                                Window.alert("ex : " + ex.toString());
                            }
                        }
                        if (getSelectedBrigade() == null ||
                                getSelectedBrigade().getBrigade().getBrigadeId() != brigInfoPanel.getBrigade().getBrigadeId()) {
                            brigInfoPanel.setStyleName("selectArmyPanel");
                            setSelectedBrigade(brigInfoPanel);
                        } else {
                            setSelectedBrigade(null);
                        }
                    }
                });
            }
        }
    }

    public BrigadeInfoPanel getSelectedBrigade() {
        return this.selectedBrigade;
    }

    /**
     * @param selectedBrigade the selectedBrigade to set
     */
    public void setSelectedBrigade(final BrigadeInfoPanel selectedBrigade) {
        this.selectedBrigade = selectedBrigade;
    }

}
