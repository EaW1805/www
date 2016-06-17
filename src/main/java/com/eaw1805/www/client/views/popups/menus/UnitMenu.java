package com.eaw1805.www.client.views.popups.menus;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.PopupPanelEAW;
import com.eaw1805.www.shared.stores.InfoPanelsStore;

import java.util.ArrayList;
import java.util.List;


public class UnitMenu extends AbsolutePanel implements StyleConstants {

    private int top = 0, left = 37;
    private int leftBut = 40;

    private final List<ImageButton> allImageButtons = new ArrayList<ImageButton>();
    private final List<Image> allBackgroundImages = new ArrayList<Image>();

    public UnitMenu() {
        super();
        this.setSize(SIZE_37PX, "137px");
    }

    public void setPopupParent(final PopupPanelEAW parent) {
        parent.setAutoHideEnabled(false);
        InfoPanelsStore.getInstance().setUnitPopup(parent);
    }

    protected void onAttach() {
        super.onAttach();
        InfoPanelsStore.getInstance().setInfoPanelLocked(true);
    }

    protected void onDetach() {
        super.onDetach();
        UnitEventManager.undoSelection();
        InfoPanelsStore.getInstance().setInfoPanelLocked(false);
    }

    protected boolean finalizeMenu(final int minWidth) {
        if (minWidth > left + 37) {
            this.setWidth(minWidth + "px");
        } else {
            this.setWidth((left + 37) + "px");
        }

        final Image leftBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/LeftEnd.png");
        leftBrdImg.setSize(SIZE_37PX, "73px");
        add(leftBrdImg, 0, 0);
        allBackgroundImages.add(leftBrdImg);
        final Image rightBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/RightEnd.png");
        rightBrdImg.setSize(SIZE_37PX, "73px");
        add(rightBrdImg, left, top);
        allBackgroundImages.add(rightBrdImg);
        return true;
    }

    protected void addImageButton(final ImageButton tgButton) {
        allImageButtons.add(tgButton);
        final int offset = 37;
        this.setWidth((leftBut + offset) + "px");

        final Image middleBrdImg = new Image("http://static.eaw1805.com/images/buttons/menu/Middle.png");
        allBackgroundImages.add(middleBrdImg);
        middleBrdImg.setSize(SIZE_37PX, "73px");
        add(middleBrdImg, left, top);
        final int topBut = 19;
        add(tgButton, leftBut, topBut);
        left += offset;
        leftBut += offset;
    }

    protected void clearAllButtons() {
        for (ImageButton button : allImageButtons) {
            remove(button);
        }
        for (Image bgImage : allBackgroundImages) {
            remove(bgImage);
        }
        allImageButtons.clear();
        allBackgroundImages.clear();
        left = 37;
        leftBut = 40;
        top = 0;
    }
}
