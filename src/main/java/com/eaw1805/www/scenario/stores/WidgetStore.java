package com.eaw1805.www.scenario.stores;


import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.TerrainConstants;

public class WidgetStore implements TerrainConstants {

    public static Widget getNationImg(int nationId, int width) {
        Image out = new Image("http://static.eaw1805.com/images/nations/nation-" + nationId + "-36.png");
        out.setWidth(width + "px");
        return out;
    }

    public static Widget getLabel(String text) {
        return new Label(text);
    }

    public static Widget getResourceImg(final int resourceId, final int width) {
        Image out = new Image("http://static.eaw1805.com/tiles/resources/resource-" + resourceId + ".png");
        out.setWidth(width + "px");
        return out;
    }

    public static Widget getProductionSiteImg(final int prSiteId, final int width) {
        Image out = new Image("http://static.eaw1805.com/tiles/sites/tprod-" + prSiteId + ".png");
        out.setWidth(width + "px");
        return out;
    }


    public static Widget getTerrainImg(int terrainId, final int width) {
        Image out = new Image(getTerrainImageHref(terrainId));
        out.setWidth(width + "px");
        return out;
    }


    public static String getTerrainImageHref(final int terrainId) {
        String baseUrl = "http://static.eaw1805.com/tiles/";
        switch (terrainId) {
            case TERRAIN_B:
                return baseUrl + "base/arable.png";
            case TERRAIN_D:
                return baseUrl + "base/desert1.png";
            case TERRAIN_G:
                return baseUrl + "elevation/tm01.png";
            case TERRAIN_H:
                return baseUrl + "elevation/th01.png";
            case TERRAIN_K:
                return baseUrl + "base/steppe1.png";
            case TERRAIN_Q:
                return baseUrl + "base/arable.png";
            case TERRAIN_W:
                return baseUrl + "base/forest1.png";
            case TERRAIN_S:
                return baseUrl + "elevation/tww01.png";
            case TERRAIN_T:
                return baseUrl + "base/forestw.png";
            case TERRAIN_J:
                return baseUrl + "base/jungle1.png";
            case TERRAIN_R:
                return baseUrl + "base/ocean.png";
            case TERRAIN_O:
                return baseUrl + "base/ocean.png";
            case TERRAIN_I:
                return baseUrl + "base/impassable2.png";
            default:
                return "";

        }

    }

    public static Widget getClimateImg(char climate, final int width) {
        Image out = new Image("http://static.eaw1805.com/tiles/climates/climate_" + String.valueOf(climate) + ".png");
        out.setWidth(width + "px");
        return out;
    }

    public static Widget getBattalionImg(int nationId, int intId, int width) {
        Image out = new Image("http://static.eaw1805.com/images/armies/" + nationId + "/" + intId + ".jpg");
        out.setWidth(width + "px");
        return out;

    }
}
