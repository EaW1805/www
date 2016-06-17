package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;

public class TerrainInfoPanel extends AbsolutePanel {
    final String host = "http://static.eaw1805.com";

    public TerrainInfoPanel(final FieldBattleSectorDTO sector) {
        setSize("167px", "141px");

        add(createLabel((sector.getX() + 1) + "/" + (sector.getY() + 1)), 135, 3);

        Image image = new Image(host + "/images/field/level" + sector.getAltitude() + ".png");
        image.setSize("30px", "30px");
        add(image, 3, 3);
        add(createLabel("Level : " + sector.getAltitude()), 35, 5);
        String type = "";
        if (sector.isForest()) {
            image = new Image(host + "/images/field/tree.png");
            type = "Forest";
        } else if (sector.isLake()) {
            image = new Image(host + "/images/field/small-lake.png");
            type = "Lake";
        } else if (sector.isMinorRiver() || sector.isMajorRiver()) {
            image = new Image(host + "/images/field/map/rivers/r_B1.png");
            type = "Minor river";
        } else if (sector.isRoad()) {
            image = new Image(host + "/images/field/map/roads/r_B1.png");
            type = "Road";
        }
        if (!"".equals(type)) {
            image.setSize("30px", "30px");
            add(image, 4, 38);
            add(createLabel(type), 35, 37);
        }

        if (sector.hasTown()) {
            image = new Image(host + "/images/field/city1.png");
            image.setSize("30px", "30px");
            add(image, 4, 38);

        }

        if (sector.hasVillage()) {
            image = new Image(host + "/images/field/city1.png");
            image.setSize("30px", "30px");
            add(image, 4, 38);

        }

        if (sector.hasWall()) {
            image = new Image(host + "/images/field/map/walls/f_BL.png");
            image.setSize("30px", "30px");
            add(image, 4, 38);
        }

        if (sector.hasBridge()) {
            image = new Image(host + "/images/field/bridgeUD.png");
            image.setSize("30px", "30px");
            add(image, 4, 38);

        }

        if (sector.hasChateau()) {
            image = new Image(host + "/images/field/map/buildings/Chateau.png");
            image.setSize("30px", "30px");
            add(image, 4, 38);
        }

        if (sector.isBush()) {
            image = new Image(host + "/images/field/map/vegetation/Rough1.png");
            image.setSize("30px", "30px");
            add(image, 4, 38);

        }

        if (sector.isStrategicPoint()) {
            add(createLabel("Strategic Point"), 72, 119);
        }

//        final HorizontalPanel terrainImgs = new HorizontalPanel();
//        add(terrainImgs);
//
//        if (sector.hasTown() || sector.hasVillage()) {
//            image = new Image(host + "/images/field/city1.png");
//            terrainImgs.add(image);
//        }
//
//        if (sector.hasWall()) {
//            image = new Image(host + "/images/field/WALL1.png");
//            terrainImgs.add(image);
//        }
//
//        if (sector.hasBridge()) {
//            image = new Image(host + "/images/field/bridgeUD.png");
//            terrainImgs.add(image);
//        }
    }

    public Label createLabel(String text) {
        Label out = new Label(text);
        out.setStyleName("whiteText");
        return out;
    }

}
