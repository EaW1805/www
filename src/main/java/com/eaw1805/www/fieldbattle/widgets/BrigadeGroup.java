package com.eaw1805.www.fieldbattle.widgets;


import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

import java.util.ArrayList;
import java.util.List;

public class BrigadeGroup extends Group {

    final List<Image> battImages = new ArrayList<Image>();
    final BrigadeDTO brigade;

    public BrigadeGroup(BrigadeDTO brigade) {
        this.brigade = brigade;
        final MapUtils mapUtils = MainPanel.getInstance().getMapUtils();
        int brigX = mapUtils.getPointX(brigade.getFieldBattleX());
        int brigY = mapUtils.getPointY(brigade.getFieldBattleY());

        int count = 0;
        final int offsetX;
        if (brigade.getBattalions().size() >= 3) {
            offsetX = 1;
        } else {
            offsetX = 10 * (3 - brigade.getBattalions().size());
        }


        String direction = "Down";
        if ((!BaseStore.getInstance().isNationAllied(brigade.getNationId())
                && BaseStore.getInstance().getSide() == 1)
                || (BaseStore.getInstance().isNationAllied(brigade.getNationId())
                && BaseStore.getInstance().getSide() == 2)) {
            direction = "Up";
        }

        for (BattalionDTO battalion : brigade.getBattalions()) {
            final StringBuilder url = new StringBuilder();

            url.append("http://static.eaw1805.com").append("/images/field/map/troops/").append(brigade.getNationId()).append("/");
            if (battalion.getEmpireArmyType().isArtillery()) {
                url.append("ArtHeavy").append(direction).append("_").append(mapUtils.getLetterByNationId(brigade.getNationId())).append(".png");
            } else if (battalion.getEmpireArmyType().isCavalry()) {
                url.append("CavHeavy").append(direction).append("_").append(mapUtils.getLetterByNationId(brigade.getNationId())).append(".png");
            } else {
                url.append("InfReg").append(direction).append("_").append(mapUtils.getLetterByNationId(brigade.getNationId())).append(".png");
            }

            final Image batImg;
            if (count < 3) {
                batImg = new Image(brigX + offsetX + (20 * count), brigY - 4, 21, 58, url.toString());
            } else {
                batImg = new Image(brigX + offsetX + (20 * (count - 3)), brigY + 10, 21, 58, url.toString());
            }
            battImages.add(batImg);
            add(batImg);
            count++;
        }
    }

    public void updateGroupPosition(final int x, final int y) {
        final int offsetX;
        if (battImages.size() >= 3) {
            offsetX = 1;
        } else {
            offsetX = 10 * (3 - battImages.size());
        }
        int count = 0;
        for (Image batImg : battImages) {

            if (count < 3) {
                batImg.setX(x + offsetX + (20 * count));
                batImg.setY(y - 4);

            } else {
                batImg.setX(x + offsetX + (20 * (count - 3)));
                batImg.setY(y + 10);
            }
            count++;
        }
//        MainPanel.getInstance().getArmyInfo().debugText.setText("Repositioning? " + x +", "+ y);
    }

    public BrigadeDTO getBrigade() {
        return brigade;
    }

}
