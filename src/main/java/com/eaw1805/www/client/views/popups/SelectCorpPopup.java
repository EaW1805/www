package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.military.ExchangeUnitsView;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.UnitImage;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.List;


public class SelectCorpPopup extends PopupPanelEAW implements ArmyConstants {
    private final VerticalPanel basePanel;
    private final AbsolutePanel armiesTitlePanel;
    private final VerticalPanel corpsVPanel;
    private final Label lblClickToExpand;
    private final List<ArmyDTO> armiesList;
    private final AbsolutePanel absolutePanel;
    private final Label lblClickToSelect;
    private final ExchangeUnitsView exchParent;

    public SelectCorpPopup(final List<ArmyDTO> armiesList, final ExchangeUnitsView exchParent, final int order, final int armyId) {
        this.getElement().getStyle().setZIndex(3);
        setAutoHideEnabled(true);
        setStyleName("none");
        this.exchParent = exchParent;
        this.armiesList = armiesList;
        this.basePanel = new VerticalPanel();
        setWidget(this.basePanel);
        this.basePanel.setSize("610px", "252px");

        this.armiesTitlePanel = new AbsolutePanel();
        this.armiesTitlePanel.setStyleName("LoginLabel");
        this.basePanel.add(this.armiesTitlePanel);
        this.armiesTitlePanel.setSize("610px", "23px");

        this.lblClickToExpand = new Label("Click on an army to select it!");
        this.lblClickToExpand.setStyleName("clearFont");
        this.armiesTitlePanel.add(this.lblClickToExpand, 0, 3);

        this.corpsVPanel = new VerticalPanel();
        this.corpsVPanel.setStyleName("tileCoords");
        this.basePanel.add(this.corpsVPanel);
        this.corpsVPanel.setSize("610px", "198px");

        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("embossedInfoPanel");
        this.basePanel.add(this.absolutePanel);
        this.absolutePanel.setSize("608px", "25px");

        this.lblClickToSelect = new Label("Click to select army:");
        this.lblClickToSelect.setStyleName("clearFont");
        this.absolutePanel.add(this.lblClickToSelect, 0, 3);
        this.lblClickToSelect.setSize("367px", "18px");

        initCorpsVPanel(this.armiesList, order, armyId);
    }

    private void initCorpsVPanel(final List<ArmyDTO> armiesList, final int order, final int armyId) {
        corpsVPanel.clear();
        final HorizontalPanel horPanel = new HorizontalPanel();

        for (ArmyDTO army : armiesList) {
            if (army.getArmyId() == armyId) {
                for (final CorpDTO corp : army.getCorps().values()) {
                    final PopupPanel pPanel = new PopupPanel();
                    final CorpsInfoPanel corpInfo = new CorpsInfoPanel(corp, false);
                    corpInfo.setWidth("240px");
                    pPanel.add(corpInfo);
                    final UnitImage corpImage = new UnitImage("http://static.eaw1805.com/images/figures/"
                            + GameStore.getInstance().getNationId() + "/UnitMap00.png",
                            pPanel);
                    corpImage.setId(army.getArmyId());
                    final CorpDTO thisCorp = corp;
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            selectCorp(thisCorp, corpImage, order);
                        }
                    }).addToElement(corpImage.getElement()).register();

                    horPanel.add(corpImage);
                    corpsVPanel.add(horPanel);
                }
                break;
            }
        }


    }

    private void selectCorp(final CorpDTO thisCorp, final UnitImage corpImg, final int order) {
        exchParent.setSelUnit(order, CORPS, thisCorp.getCorpId(), corpImg);
        this.hide();
    }
}
