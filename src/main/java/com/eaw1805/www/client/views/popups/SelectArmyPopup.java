package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.military.ExchangeUnitsView;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.UnitImage;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.List;

public class SelectArmyPopup extends PopupPanelEAW implements ArmyConstants {
    private final VerticalPanel basePanel;
    private final AbsolutePanel armiesTitlePanel;
    private final VerticalPanel armiesVPanel;
    private final Label lblClickToExpand;
    private final List<ArmyDTO> armiesList;
    private final AbsolutePanel absolutePanel;
    private final Label lblClickToSelect;
    private final ExchangeUnitsView exchParent;

    public SelectArmyPopup(final List<ArmyDTO> armiesList, final ExchangeUnitsView exchParent, final int order) {
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

        this.armiesVPanel = new VerticalPanel();
        this.armiesVPanel.setStyleName("tileCoords");
        this.basePanel.add(this.armiesVPanel);
        this.armiesVPanel.setSize("610px", "198px");

        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("embossedInfoPanel");
        this.basePanel.add(this.absolutePanel);
        this.absolutePanel.setSize("608px", "25px");

        this.lblClickToSelect = new Label("Click to select army:");
        this.lblClickToSelect.setStyleName("clearFont");
        this.absolutePanel.add(this.lblClickToSelect, 0, 3);
        this.lblClickToSelect.setSize("367px", "18px");

        initArmiesVPanel(this.armiesList, order);
    }

    private void initArmiesVPanel(final List<ArmyDTO> armiesList, final int order) {
        armiesVPanel.clear();
        final HorizontalPanel horPanel = new HorizontalPanel();
        for (final ArmyDTO army : armiesList) {
            final PopupPanel pPanel = new PopupPanel();
            final ArmyInfoPanel armyInfo = new ArmyInfoPanel(army);
            armyInfo.setWidth("240px");
            pPanel.add(armyInfo);
            final UnitImage armyImage = new UnitImage("http://static.eaw1805.com/images/figures/"
                    + GameStore.getInstance().getNationId() + "/UnitMap00.png", pPanel);
            armyImage.setId(army.getArmyId());
            final ArmyDTO thisArmy = army;
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(final MouseEvent event) {
                    selectArmy(thisArmy, armyImage, order);
                }
            }).addToElement(armyImage.getElement()).register();

            horPanel.add(armyImage);
        }
        armiesVPanel.add(horPanel);

    }

    private void selectArmy(final ArmyDTO thisArmy, final UnitImage armyImg, final int order) {
        exchParent.setSelUnit(order, ARMY, thisArmy.getArmyId(), armyImg);
        this.hide();
    }
}
