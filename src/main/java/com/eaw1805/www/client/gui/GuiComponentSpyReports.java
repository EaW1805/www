package com.eaw1805.www.client.gui;

import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.vaadin.gwtgraphics.client.Group;

/**
 * Created with IntelliJ IDEA.
 * User: karavias
 * Date: 4/5/12
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class GuiComponentSpyReports
        extends GuiComponentAbstract<Group>
        implements GuiComponent {

    private SpyDTO spy;

    public GuiComponentSpyReports(final Group group, final SpyDTO spy) {
        widget = group;
        this.spy = spy;
    }

    public void handleEscape() {
        MapStore.getInstance().getUnitGroups().toggleInspectionTiles(widget, spy.getSpyId(), this);
        //just be sure it will un-register...
        gameStore.unRegisterComponent(this, false);
    }

    public void handleWave() {
        //do nothing here
    }
}
