package com.eaw1805.www.client.views.figures;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.WeightCalculators;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;

public class BrigadeInfoFigPanel extends FocusPanel {
    private AbsolutePanel absolutePanel;
    private Label lblBrigName;
    private Label lbHd;
    private Label lblWeight;
    private DualStateImage figImg;

    public BrigadeInfoFigPanel(final BrigadeDTO brig, final boolean disabled, final String title) {

        setSize("64px", "97px");
        this.setStyleName("");
        this.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.absolutePanel = new AbsolutePanel();
        add(this.absolutePanel);
        this.absolutePanel.setSize("64px", "97px");

        this.lblBrigName = new Label(brig.getName());
        this.lblBrigName.setStyleName("clearFontSmall");
        this.lblBrigName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.absolutePanel.add(this.lblBrigName, 0, 0);
        this.lblBrigName.setWidth("64px");
        ArmyUnitInfoDTO bgInfo = MiscCalculators.getBrigadeInfo(brig);
        int hd = bgInfo.getInfantry() + bgInfo.getArtillery() + bgInfo.getCavalry();
        if (brig.getNationId() == GameStore.getInstance().getNationId()) {
            this.lbHd = new Label(String.valueOf(hd));
        } else {
            this.lbHd = new Label("????");
        }
        this.lbHd.setStyleName("clearFontSmall");
        this.lbHd.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.absolutePanel.add(this.lbHd, 0, 82);
        this.lbHd.setSize("64px", "15px");

        this.figImg = new DualStateImage("http://static.eaw1805.com/images/figures/" + brig.getNationId() + "/UnitMap00.png");
        this.figImg.setId(brig.getBrigadeId());
        this.absolutePanel.add(this.figImg, 0, 17);
        this.figImg.setSize("64px", "64px");
        addOverViewPanelToImage(figImg, brig);


        lblWeight = new Label("Weight:" + WeightCalculators.getBrigadeWeight(brig));
        this.lblWeight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.lblWeight.setStyleName("clearFontMini whiteText");
        this.absolutePanel.add(lblWeight, 0, 69);
        this.lblWeight.setSize("64px", "12px");

        this.setTitle(title);
        if (disabled) {
            this.getElement().getStyle().setOpacity(0.7);
        }
    }

    public void scaleToMini() {
        this.absolutePanel.remove(lblBrigName);
        this.absolutePanel.remove(lbHd);
        this.absolutePanel.remove(lblWeight);
        this.setSize("32", "32");
        this.absolutePanel.setSize("32", "32");
        this.figImg.setSize("32px", "32px");
        this.absolutePanel.setWidgetPosition(figImg, 0, 0);

    }

    /**
     * Add tooltip information panel to brigade image.
     *
     * @param armyTypeImg The image to add the tooltip to.
     * @param brig        The brigade to get the tooltip for.
     */
    private void addOverViewPanelToImage(final DualStateImage armyTypeImg, final BrigadeDTO brig) {
        armyTypeImg.setStyleName("pointer", true);
        if (brig.getNationId() == GameStore.getInstance().getNationId()) {
            new ToolTipPanel(armyTypeImg) {
                @Override
                public void generateTip() {
                    setTooltip(new BrigadeInfoPanel(brig, false));
                }
            };
        }
    }

    /**
     * @return the figImg
     */
    public DualStateImage getFigImg() {
        return figImg;
    }
}
