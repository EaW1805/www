package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.eaw1805.www.shared.stores.map.MapStore;

public class ParseIntBox
        extends TextBox {

    private int max = Integer.MAX_VALUE;

    public ParseIntBox(final int maxed) {
        super();
        this.setMax(maxed);
        final ParseIntBox mySelf = this;

        addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                event.stopPropagation();
                ParseIntBox.this.setSelectionRange(0, ParseIntBox.this.getText().length());
                ParseIntBox.this.setFocus(true);
                MapStore.getInstance().setFocusLocked(true);
            }
        });


        addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(final MouseDownEvent event) {
                MapStore.getInstance().setFocusLocked(true);
                getFocusImpl().focus(ParseIntBox.this.getElement());
                ParseIntBox.this.setSelectionRange(0, ParseIntBox.this.getText().length());

            }
        });

        addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(final KeyPressEvent event) {
                if (!(event.getCharCode() < '9' && event.getCharCode() > '0')) {
                    event.stopPropagation();
                }

            }
        });
        addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(final KeyDownEvent event) {
                if (!(event.getNativeEvent().getCharCode() < '9' && event.getNativeEvent().getCharCode() > '0')) {
                    event.stopPropagation();
                }
            }
        });
        addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(final KeyUpEvent event) {
                if (!(event.getNativeEvent().getCharCode() < '9' && event.getNativeEvent().getCharCode() > '0')) {
                    final String txt = mySelf.getText().replaceAll("\\D", "");
                    mySelf.setText(mySelf.getText().replaceAll("\\D", ""));
                    try {
                        if ("".equals(txt)) {
                            mySelf.setText("");

                        } else {
                            if (Integer.parseInt(txt) > max) {
                                mySelf.setText(String.valueOf(max));
                            }
                        }

                    } catch (Exception ex) {
                        mySelf.setText(String.valueOf(0));
                    }
                    event.stopPropagation();
                }
            }
        });
    }

    /**
     * @param max the max to set
     */
    public final void setMax(final int max) {
        this.max = max;
    }

}
