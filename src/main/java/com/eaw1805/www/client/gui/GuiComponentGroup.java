package com.eaw1805.www.client.gui;

import org.vaadin.gwtgraphics.client.Group;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 4/5/12
 * Time: 2:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class GuiComponentGroup
        extends GuiComponentAbstract<Group>
        implements GuiComponent {

    private Group parent;

    public GuiComponentGroup(final Group widget, final Group parent) {
        this.widget = widget;
        this.parent = parent;
    }

    public void handleEscape() {
        parent.remove(widget);
        gameStore.unRegisterComponent(this, false);
    }

    @Override
    public void handleWave() {
        //do nothing here
    }


}
