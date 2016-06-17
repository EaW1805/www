package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.shared.stores.SoundStore;

public class StyledCheckBox extends ClickAbsolutePanel implements StyleConstants {
    private boolean checked = false;
    private String text = "";
    private final Image checkImg;
    private final Label lblText;
    private boolean enabled = true;

    public StyledCheckBox(final String text, final boolean chcked, final boolean noText) {
        setSize("210px", SIZE_21PX);
        this.text = text;
        this.checked = chcked;
        this.checkImg = new Image();
        this.checkImg.setStyleName("pointer");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                SoundStore.getInstance().playClickWooden();
                if (enabled) {
                    checked ^= true;
                    setChecked(checked);
                }
            }
        }).addToElement(checkImg.getElement()).register();

        this.setChecked(this.checked);
        add(this.checkImg, 0, 0);
        this.checkImg.setSize(SIZE_21PX, SIZE_21PX);

        this.lblText = new Label(this.text);
        this.lblText.setWordWrap(false);
        this.lblText.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.lblText.setStyleName("clearFont");
        this.lblText.setStyleName("whiteText", true);
        add(this.lblText, 26, 0);
        this.lblText.setSize("150px", SIZE_21PX);
        if (noText) {
            remove(lblText);
            setSize(SIZE_21PX, SIZE_21PX);
        }
    }

    public Image getCheckBox() {
        return checkImg;
    }

    public void invertDirection(final boolean setInvert) {
        if (setInvert) {
            setWidgetPositionImpl(lblText, 0, 0);
            setWidgetPositionImpl(checkImg, 155, 0);

        } else {
            setWidgetPositionImpl(lblText, 26, 0);
            setWidgetPositionImpl(checkImg, 0, 0);
        }
    }

    public boolean isChecked() {
        return this.checked;
    }

    public final void setChecked(final boolean checked, final boolean force) {
        if (enabled || force) {
            this.checked = checked;
        }
        updateImage();
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

    public boolean isEnabled() {
        return enabled;
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
