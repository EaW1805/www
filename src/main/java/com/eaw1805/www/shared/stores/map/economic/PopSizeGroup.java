package empire.webapp.shared.stores.map.economic;

import empire.data.constants.TerrainConstants;
import empire.data.dto.common.SectorDTO;
import empire.webapp.client.widgets.MapImage;
import empire.webapp.shared.stores.map.MapStore;
import empire.webapp.shared.stores.support.MapConstants;
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
