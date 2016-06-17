package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SpeedTest extends VerticalPanel {

    private boolean enabled = false;
    private final TextBoxEAW searchBox = new TextBoxEAW(true);

    public SpeedTest() {
        setWidth("111px");
        add(searchBox);
        final HorizontalPanel options = new HorizontalPanel();
        final Button clear = new Button("Clear", new ClickHandler() {

            public void onClick(final ClickEvent clickEvent) {
                for (int i = 2; i < SpeedTest.this.getWidgetCount(); i++) {
                    SpeedTest.this.remove(i);
                }
            }
        });
        options.add(clear);
        final CheckBox box = new CheckBox("enable");
        box.setValue(false);
        box.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent clickEvent) {
                enabled = box.getValue();
            }
        });
        options.add(box);
        add(options);
        searchBox.addKeyDownHandler(new KeyDownHandler() {

            public void onKeyDown(final KeyDownEvent keyDownEvent) {
                final String query = searchBox.getText();
                for (int i = 2; i < SpeedTest.this.getWidgetCount(); i++) {
                    final Label label = (Label) ((AbsolutePanel) SpeedTest.this.getWidget(i)).getWidget(0);
                    if (label.getText().isEmpty()
                            || label.getText().toLowerCase().contains(query)) {
                        SpeedTest.this.getWidget(i).setVisible(true);
                    } else {
                        SpeedTest.this.getWidget(i).setVisible(false);
                    }
                }
            }
        });
        setVisible(false);//start as hidden element
    }

    public void addMeasure(final String value) {
        if (!enabled) {
            return;
        }
        final AbsolutePanel container = new AbsolutePanel();
        container.setSize("111px", "32px");
        container.setStyleName("costInfoX111");
        final Label meas = new Label(value);
        container.add(meas, 9, 9);
        insert(container, 2);
//        add(container);
        container.addDomHandler(new ClickHandler() {

            public void onClick(final ClickEvent clickEvent) {
                SpeedTest.this.remove(container);
            }
        }, ClickEvent.getType());
    }
}
