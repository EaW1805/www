package com.eaw1805.www.shared.stores.map;

import com.google.gwt.dom.client.Style;
import com.eaw1805.data.dto.common.SectorDTO;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * A simple group used to be on top of terrain images so the
 * map dragging events will occur on this group and not in the images.
 * It seems it speeds up the dragging animation.
 */
public class SpeedUpGroup extends Group {
    public void initSpeedUpGroup(final SectorDTO[][] sectors) {
        if (sectors.length == 0 || sectors[0].length == 0 ||
                sectors[0][0] == null) {
            return;
        }
        final int[][] dimensions = MapStore.getInstance().getDimensions();
        final Rectangle bigRect = new Rectangle(MapStore.getInstance().getPointX(0),
                MapStore.getInstance().getPointY(0),
                (int) (MapStore.getInstance().getZoomLevel() * dimensions[0][sectors[0][0].getRegionId()]),
                (int) (MapStore.getInstance().getZoomLevel() * dimensions[1][sectors[0][0].getRegionId()]));
        bigRect.setFillOpacity(0.0);
        bigRect.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        add(bigRect);
    }
}
