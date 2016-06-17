package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;

import java.util.List;

public class SelectionListPanel
        extends Composite
        implements RegionConstants, ArmyConstants {

    private SelectableWidget selectedWidget;

    public SelectionListPanel(final List<SelectableWidget> widgets) {
        final VerticalPanel verticalPanel = new VerticalPanel();
        initWidget(verticalPanel);
        verticalPanel.setSize("250px", "0");
        for (final SelectableWidget widget : widgets) {
            verticalPanel.add(widget.getWidget());
            widget.getWidget().addDomHandler(new ClickHandler() {
                public void onClick(final ClickEvent clickEvent) {
                    for (SelectableWidget widget1 : widgets) {
                        try {
                            if (getSelectedWidget() != null) {
                                widget1.getWidget().removeStyleName("selectArmyPanel");
                            }

                        } catch (Exception ex) {

                        }
                    }
                    //at last apply selected style and set as selected this widget
                    widget.getWidget().setStyleName("selectArmyPanel", true);
                    setSelectedWidget(widget);
                }
            }, ClickEvent.getType());
        }
    }

    public SelectableWidget getSelectedWidget() {
        return selectedWidget;
    }

    public void setSelectedWidget(final SelectableWidget selectedWidget) {
        this.selectedWidget = selectedWidget;
    }
}
