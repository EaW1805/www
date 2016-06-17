package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.map.MapStore;

public class TextBoxEAW extends TextBox {

    public TextBoxEAW(final boolean loseFocusOnEnter) {

        addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                event.stopPropagation();
                SoundStore.getInstance().playClickWooden();
                TextBoxEAW.this.setSelectionRange(0, TextBoxEAW.this.getText().length());
                TextBoxEAW.this.setFocus(true);
                MapStore.getInstance().setFocusLocked(true);
            }
        });

        addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(final KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
                        && loseFocusOnEnter) {
                    TextBoxEAW.this.setFocus(false);
                    //free focus lock
                    MapStore.getInstance().setFocusLocked(false);
                }
            }
        });

        addFocusListener(new FocusListener() {
            public void onLostFocus(final Widget sender) {
                //release focus to be used by focus panel for keyboard sort cuts
                MapStore.getInstance().setFocusLocked(false);
            }

            public void onFocus(final Widget sender) {
                //grab focus so the focus panel won't take it from you
                MapStore.getInstance().setFocusLocked(true);
            }
        });

        addFocusHandler(new FocusHandler() {

            public void onFocus(final FocusEvent event) {
                event.stopPropagation();

            }
        });

    }

}
