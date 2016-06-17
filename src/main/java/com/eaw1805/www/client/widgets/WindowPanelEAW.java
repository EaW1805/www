package com.eaw1805.www.client.widgets;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class WindowPanelEAW
        extends AbsolutePanel {

    /**
     * An identifier to recognize the window when we search it.
     */
    private int windowId = 0;

    /**
     * Get the window identifier.
     *
     * @return The identifier.
     */
    public int getWindowId() {
        return windowId;
    }

    /**
     * Set the window identifier.
     *
     * @param id The identifier to set.
     */
    public void setWindowId(final int id) {
        this.windowId = id;
    }

    /**
     * @param position the position of the window.
     */
    public void setWindowPosition(final int position) {
        this.getElement().getStyle().setZIndex(position);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final WindowPanelEAW that = (WindowPanelEAW) o;
        return windowId == that.windowId;
    }

    @Override
    public int hashCode() {
        return windowId;
    }
}
