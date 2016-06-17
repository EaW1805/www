package com.eaw1805.www.client.views.popups;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.military.ExchangeUnitsView;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.client.widgets.UnitImage;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.List;


public class SelectBrigadePopup extends PopupPanelEAW implements ArmyConstants {
    private final VerticalPanel basePanel;
    private final AbsolutePanel armiesTitlePanel;
    private final VerticalPanel brigsVPanel;
    private final Label lblClickToExpand;
    private final List<ArmyDTO> armiesList;
    private final AbsolutePanel absolutePanel;
    private final Label lblClickToSelect;
    private final ExchangeUnitsView exchParent;

    public SelectBrigadePopup(final List<ArmyDTO> armiesList, final ExchangeUnitsView exchParent, final int order, final int corpId) {
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

        this.brigsVPanel = new VerticalPanel();
        this.brigsVPanel.setStyleName("tileCoords");
        this.basePanel.add(this.brigsVPanel);
        this.brigsVPanel.setSize("610px", "198px");

        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("embossedInfoPanel");
        this.basePanel.add(this.absolutePanel);
        this.absolutePanel.setSize("608px", "25px");

        this.lblClickToSelect = new Label("Click to select army:");
        this.lblClickToSelect.setStyleName("clearFont");
        this.absolutePanel.add(this.lblClickToSelect, 0, 3);
        this.lblClickToSelect.setSize("367px", "18px");

        initBrigsVPanel(this.armiesList, order, corpId);
    }

    private void initBrigsVPanel(final List<ArmyDTO> armiesList, final int order, final int corpId) {
        brigsVPanel.clear();
        HorizontalPanel horPanel = new HorizontalPanel();

        for (ArmyDTO army : armiesList) {
            if (army.getCorps().containsKey(corpId)) {
                for (final BrigadeDTO brig : army.getCorps().get(corpId).getBrigades().values()) {
                    final PopupPanel pPanel = new PopupPanel();
                    final BrigadeInfoPanel brigInfo = new BrigadeInfoPanel(brig, false);
                    brigInfo.setWidth("240px");
                    pPanel.add(brigInfo);
                    final UnitImage brigImage = new UnitImage("http://static.eaw1805.com/images/figures/"
                            + GameStore.getInstance().getNationId() + "/UnitMap00.png",
                            pPanel);
                    brigImage.setId(brig.getBrigadeId());
                    final BrigadeDTO thisBrig = brig;
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(final MouseEvent event) {
                            selectBrig(thisBrig, brigImage, order);
                        }
                    }).addToElement(brigImage.getElement()).register();

                    horPanel.add(brigImage);
                    brigsVPanel.add(horPanel);
                }
            }
        }


    }

    private void selectBrig(final BrigadeDTO thisBrig, final UnitImage brigImg, final int order) {
        exchParent.setSelUnit(order, BRIGADE, thisBrig.getBrigadeId(), brigImg);
        this.hide();
    }
}
