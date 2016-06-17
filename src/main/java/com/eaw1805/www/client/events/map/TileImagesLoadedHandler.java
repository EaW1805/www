package com.eaw1805.www.client.events.map;

import com.google.gwt.event.shared.EventHandler;

public interface TileImagesLoadedHandler
        extends EventHandler {

    void onTileImagesLoaded(final TileImagesLoadedEvent event);

}
