package com.eaw1805.www.client.views.figures;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.views.infopanels.units.SpyInfoPanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;

public class SpyInfoFigPanel extends FocusPanel {

    private AbsolutePanel absolutePanel;
    private Label lblBrigName;
    private DualStateImage figImg;

    public SpyInfoFigPanel(final SpyDTO spy, final boolean disabled, final String title) {
        setSize("64px", "97px");
        this.setStyleName("");
        this.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setSize("64px", "97px");
        add(this.absolutePanel);

        this.lblBrigName = new Label(spy.getName());
        this.lblBrigName.setStyleName("clearFontSmall");
        this.lblBrigName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.lblBrigName.setWidth("64px");
        this.absolutePanel.add(this.lblBrigName, 0, 1);

        this.figImg = new DualStateImage("http://static.eaw1805.com/images/figures/" + GameStore.getInstance().getNationId() + "/spy.png");
        this.figImg.setId(spy.getSpyId());
        this.figImg.setSize("64px", "64px");
        this.absolutePanel.add(this.figImg, 0, 17);

        addOverViewPanelToImage(figImg, spy);
        this.setTitle(title);
        if (disabled) {
            this.getElement().getStyle().setOpacity(0.7);
        }
    }

    public void scaleToMini() {
        this.absolutePanel.remove(lblBrigName);
        this.setSize("32", "32");
        this.absolutePanel.setSize("32", "32");
        this.figImg.setSize("32px", "32px");
        this.absolutePanel.setWidgetPosition(figImg, 0, 0);
    }

    /**
     * Add tooltip information panel to spy image.
     *
     * @param armyTypeImg The image to add the tooltip to.
     * @param spy         The spy to get the tooltip for.
     */
    private void addOverViewPanelToImage(final DualStateImage armyTypeImg, final SpyDTO spy) {
        armyTypeImg.setStyleName("pointer", true);
        new ToolTipPanel(armyTypeImg) {
            @Override
            public void generateTip() {
                setTooltip(new SpyInfoPanel(spy));
            }
        };
    }

    /**
     * @return the figImg
     */
    public DualStateImage getFigImg() {
        return figImg;
    }
}
