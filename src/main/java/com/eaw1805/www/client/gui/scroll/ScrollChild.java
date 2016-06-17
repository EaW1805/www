package com.eaw1805.www.client.gui.scroll;

import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;

public interface ScrollChild {
    public void setScroller(final ScrollVerticalBarEAW scroller, final boolean autoResize);
    public void resizeScrollBar();
}
