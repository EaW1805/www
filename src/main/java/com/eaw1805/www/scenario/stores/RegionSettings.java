package com.eaw1805.www.scenario.stores;

import com.google.gwt.user.client.Window;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.RegionDTO;

import java.util.ArrayList;
import java.util.List;

public class RegionSettings {
    public static int sizeX;
    public static int sizeY;
    public static String name = "";
    public static RegionDTO region;
    public static List<SectorDTO> sectors = new ArrayList<SectorDTO>();
    public static void generateSettings(final RegionDTO inReg, final List<SectorDTO> regionSectors) {
        region = inReg;
        sizeX = 0;
        sizeY = 0;
        name = region.getName();
        for (SectorDTO sector : regionSectors) {
            if (sector.getX() + 1 > sizeX) {
                sizeX = sector.getX() + 1;
            }
            if (sector.getY() + 1 > sizeY) {
                sizeY = sector.getY() + 1;
            }
        }
        sectors = regionSectors;
    }

}
