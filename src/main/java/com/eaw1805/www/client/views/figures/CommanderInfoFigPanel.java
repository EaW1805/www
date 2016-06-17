package com.eaw1805.www.client.views.figures;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.www.client.views.infopanels.units.CommanderInfoPanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;

public class CommanderInfoFigPanel extends FocusPanel {
    private AbsolutePanel absolutePanel;
    private Label lblBrigName;
    private DualStateImage figImg;

    public CommanderInfoFigPanel(final CommanderDTO commander, final boolean disabled, final String title) {
        setSize("64px", "97px");
        this.setStyleName("");
        this.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        this.absolutePanel = new AbsolutePanel();
        add(this.absolutePanel);
        this.absolutePanel.setSize("64px", "97px");

        this.lblBrigName = new Label(commander.getName());
        this.lblBrigName.setStyleName("clearFontSmall");
        this.lblBrigName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.absolutePanel.add(this.lblBrigName, 0, 1);
        this.lblBrigName.setWidth("64px");

        this.figImg = new DualStateImage("http://static.eaw1805.com/images/figures/" + GameStore.getInstance().getNationId() + "/commander.png");
        this.figImg.setId(commander.getId());
        this.absolutePanel.add(this.figImg, 0, 17);
        this.figImg.setSize("64px", "64px");
        addOverViewPanelToImage(figImg, commander);
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
     * Add tooltip information panel to commander image.
     *
     * @param armyTypeImg The image to add the tooltip to.
     * @param commander   The commander to get the tooltip for.
     */
    private void addOverViewPanelToImage(final DualStateImage armyTypeImg, final CommanderDTO commander) {
        armyTypeImg.setStyleName("pointer", true);
        new ToolTipPanel(armyTypeImg) {
            @Override
            public void generateTip() {
                setTooltip(new CommanderInfoPanel(commander));
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
