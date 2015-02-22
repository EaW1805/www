package empire.webapp.shared.stores.map;

import empire.data.HibernateUtil;
import empire.data.constants.RegionConstants;
import empire.data.constants.StyleConstants;
import empire.webapp.client.widgets.MapLine;
import empire.webapp.shared.stores.GameStore;
import empire.webapp.shared.stores.support.MapConstants;
import org.vaadin.gwtgraphics.client.Group;

public class MapGridGroup extends Group implements MapConstants,
        RegionConstants, StyleConstants {

    private final int regionId;

    private final int scenarioId;

    private final int[] regionSizeX, regionSizeY;

    public MapGridGroup(final int regionId) {
        this.regionId = regionId;
        scenarioId = GameStore.getInstance().getScenarioId();
        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                regionSizeX = RegionConstants.REGION_1804_SIZE_X;
                regionSizeY = RegionConstants.REGION_1804_SIZE_Y;
                break;

            case HibernateUtil.DB_S3:
                regionSizeX = RegionConstants.REGION_1808_SIZE_X;
                regionSizeY = RegionConstants.REGION_1808_SIZE_Y;
                break;

            case HibernateUtil.DB_S1:
            case HibernateUtil.DB_S2:
            default:
                regionSizeX = RegionConstants.REGION_1805_SIZE_X;
                regionSizeY = RegionConstants.REGION_1805_SIZE_Y;
                break;
        }

        this.clear();
        for (int i = 0; i < regionSizeX[regionId - 1]; i++) {
            final MapLine gridLine = new MapLine(MapStore.getInstance().getPointY(i) - 1,
                    0,
                    MapStore.getInstance().getPointY(i) + 1,
                    MapStore.getInstance().getPointY(regionSizeY[regionId - 1]));
            gridLine.setStrokeColor(COLOR_BLACK);
            this.add(gridLine);
        }
        for (int i = 0; i < regionSizeY[regionId - 1]; i++) {
            final MapLine gridLine = new MapLine(0,
                    MapStore.getInstance().getPointY(i) - 1,
                    MapStore.getInstance().getPointY(regionSizeX[regionId - 1]),
                    MapStore.getInstance().getPointY(i) + 1);
            gridLine.setStrokeColor(COLOR_BLACK);
            this.add(gridLine);
        }
    }
}
