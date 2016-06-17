package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxEAW extends TextBox {

    public TextBoxEAW() {

        addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                event.stopPropagation();

                TextBoxEAW.this.setSelectionRange(0, TextBoxEAW.this.getText().length());
                TextBoxEAW.this.setFocus(true);

            }
        });

        addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(final KeyPressEvent event) {

            }
        });

        addFocusHandler(new FocusHandler() {

            public void onFocus(final FocusEvent event) {
                event.stopPropagation();

            }
        });

    }

}
