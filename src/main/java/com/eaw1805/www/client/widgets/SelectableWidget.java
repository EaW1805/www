package com.eaw1805.www.client.widgets;

import com.google.gwt.user.client.ui.Widget;

/**
 * An interface that should be implemented by widgets that will be used in select lists.
 *
 * @param <H> The class of the value
 */
public interface SelectableWidget<H> {
    /**
     * Get the value of the selectable widget
     *
     * @return The value
     */
    H getValue();

    /**
     * An identifier that describes the type of the value.
     *
     * @return The type.
     */
    int getIdentifier();

    /**
     * The actual widget as it should be displayed.
     *
     * @return The widget
     */
    Widget getWidget();


    /**
     * Implement this to tell what will happen to the  panel if it is selected - some styling maybe..
     *
     * @param selected True if the panel get selected - false otherwise.
     */
    void setSelected(final boolean selected);


    void onEnter();
}
