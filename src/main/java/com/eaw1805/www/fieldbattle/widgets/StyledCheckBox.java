package com.eaw1805.www.fieldbattle.widgets;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.tooltips.Tips;

import java.util.ArrayList;
import java.util.List;

public class StyledCheckBox extends HorizontalPanel implements StyleConstants {
    private boolean checked = false;
    private String text = "";
    private final Image checkImg;
    private final Label lblText;
    private boolean enabled = true;
    boolean noText;

    final List<BasicHandler> handlers = new ArrayList<BasicHandler>();

    public StyledCheckBox(final String text, final boolean chcked, final boolean noText, final String title) {
        if (title != null) {
            Tips.generateTip(this, title);
        }
        this.noText = noText;
        this.text = text;
        this.checked = chcked;
        this.checkImg = new Image();
        this.checkImg.setStyleName("pointer");
        checkImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (enabled) {
                    checked ^= true;
                    setChecked(checked);
                    executeHandlers();
                }
            }
        });


        this.setChecked(this.checked);
        add(this.checkImg);
        this.checkImg.setSize(SIZE_21PX, SIZE_21PX);

        this.lblText = new Label(this.text);
        this.lblText.setWordWrap(false);
        this.lblText.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.lblText.setStyleName("clearFont");
        this.lblText.setStyleName("whiteText", true);
        add(this.lblText);

        if (noText) {
            remove(lblText);
            setSize(SIZE_21PX, SIZE_21PX);
        }
    }

    public void addOnChangeHandler(final BasicHandler handler) {
        handlers.add(handler);
    }


    public void executeHandlers() {
        for (BasicHandler handler : handlers) {
            handler.run();
        }
    }


    public Image getCheckBox() {
        return checkImg;
    }

    public void invertDirection(final boolean setInvert) {
        clear();
        if (setInvert) {
            add(lblText);
            add(checkImg);
        } else {
            add(checkImg);
            add(lblText);
        }
    }

    public boolean isChecked() {
        return this.checked;
    }

    public final void setChecked(final boolean checked) {
        if (enabled) {
            this.checked = checked;
        }
        updateImage();
    }

    public void setText(final String text) {
        this.text = text;
        this.lblText.setText(this.text);
    }

    public void setTextStyle(final String style) {
        this.lblText.setStyleName(style);
    }

    public void setEnabled(final boolean value) {
        this.enabled = value;
        updateImage();
    }

    public void updateImage() {
        if (this.enabled) {
            if (this.checked) {
                this.checkImg.setUrl("http://static.eaw1805.com/images/buttons/CheckBoxOn.png");
            } else {
                this.checkImg.setUrl("http://static.eaw1805.com/images/buttons/CheckBoxOff.png");
            }

        } else {
            if (this.checked) {
                this.checkImg.setUrl("http://static.eaw1805.com/images/buttons/CheckBoxDisabledOn.png");
            } else {
                this.checkImg.setUrl("http://static.eaw1805.com/images/buttons/CheckBoxDisabledOff.png");
            }

        }
    }

}
