package com.eaw1805.www.shared.stores.map.economic;

import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import org.vaadin.gwtgraphics.client.Group;

public class PopSizeGroup
        extends Group
        implements MapConstants, TerrainConstants {

    public PopSizeGroup(final SectorDTO[][] sectors, final int regionId) {
        final MapStore mapStore = MapStore.getInstance();
        final double zoomLvl = mapStore.getZoomLevel();
        for (int xPos = 0; xPos < sectors.length; xPos++) {
            for (int yPos = 0; yPos < sectors[0].length; yPos++) {
                if (sectors[xPos][yPos] != null
                        && sectors[xPos][yPos].getRegionId() == regionId
                        && sectors[xPos][yPos].getVisible()
                        && sectors[xPos][yPos].getPopulation() > 0) {

                    // Add a population size text when there is pop in the list
                    // (pops available only for sectors of your nation)
                    final int textX = mapStore.getPointX(xPos) + (int) (POPSIZEOFFSETS[1][0] * zoomLvl);
                    final int textY = mapStore.getPointY(yPos) + (int) (POPSIZEOFFSETS[1][1] * zoomLvl);

                    String color = "";
                    if (sectors[xPos][yPos].getTerrainId() == TerrainConstants.TERRAIN_W
                            || sectors[xPos][yPos].getTerrainId() == TerrainConstants.TERRAIN_J) {
                        color = "w";
                    }

                    final MapImage sizeImage = new MapImage(textX, textY,
                            (int) (POPSIZESIZES[1] * zoomLvl), (int) (POPSIZESIZES[1] * zoomLvl),
                            "http://static.eaw1805.com/tiles/popsizes/pop_" + sectors[xPos][yPos].getPopulation() + color + ".png");

                    sizeImage.setxPos(xPos);
                    sizeImage.setyPos(yPos);
                    this.add(sizeImage);
                }
            }
        }
    }
}
