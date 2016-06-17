package com.eaw1805.www.fieldbattle.widgets;

import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

import java.util.HashMap;
import java.util.Map;

public class GroupMap<J> extends Group {

    final Map<J, VectorObject> idToGroup = new HashMap<J, VectorObject>();
    public void add(final J id, final VectorObject group) {
        if (idToGroup.containsKey(id)) {
            remove(id);
        }
        idToGroup.put(id, group);
        add(group);

    }
    public void remove(final J id) {
        remove(idToGroup.remove(id));
    }
}
