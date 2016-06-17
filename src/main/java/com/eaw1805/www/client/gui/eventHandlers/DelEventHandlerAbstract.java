package com.eaw1805.www.client.gui.eventHandlers;

import com.google.gwt.user.client.Element;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.util.ClientUtil;

public abstract class DelEventHandlerAbstract
        implements DelEventHandler {

    String id;

    public DelEventHandler addToElement(final Element element) {
        if (element.getId().isEmpty()) {
            element.setId(ClientUtil.getUniqueId());
        }
        id = element.getId();

        return this;
    }

    public void register() {
        GameStore.getInstance().registerEvent(this);
    }

    public void registerOnce() {
        unRegister();
        register();
    }

    public void unRegister() {
        GameStore.getInstance().unRegisterEvent(id);
    }

    public String getId() {
        return id;
    }

}

//(new DelEventHandlerAbstract() {
//public void execute(final MouseEvent event) {
//
//}
//        }).addToElement(rep.getElement(), 0, new Object[] {}).register();

