package com.eaw1805.www.shared.stores.map;

import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.widgets.MapLine;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
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
