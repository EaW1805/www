package com.eaw1805.www.fieldbattle.widgets.shared;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxEditable extends TextBox {

    public TextBoxEditable(final String placeholder) {
        getElement().setAttribute("placeholder", placeholder);
        getElement().getStyle().setFontSize(8d, Style.Unit.PT);

    }

    public void initHandler(final BasicHandler handler) {
        addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                handler.run();
            }
        });
    }

}
