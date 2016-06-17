package com.eaw1805.www.client.views.tutorial;

import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class TutorialGroup extends Group {

    Rectangle[][] rectangles;
    final Group highLights = new Group();
    int[][] countUpDowns;
    private static final double lowOpacity = 0;
    private static final double highOpacity = 1;
    private static final List<SectorDTO> highLightedSectors = new ArrayList<SectorDTO>();

    public TutorialGroup() {

    }

    public void initTutorialGroup(final SectorDTO[][] sectors) {
        rectangles = new Rectangle[sectors.length][sectors[0].length];
        countUpDowns = new int[sectors.length][sectors[0].length];
        final MapStore mapStore = MapStore.getInstance();
        final double tileSize = mapStore.getTileSize();
        for (SectorDTO[] sectorRow : sectors) {
            for (SectorDTO sector : sectorRow) {
                if (sector != null) {
                    if (sector.getVisible()) {
                        final Rectangle rect = new Rectangle(MapStore.getInstance().getPointX(sector.getX()),
                                MapStore.getInstance().getPointY(sector.getY()),
                                (int) tileSize, (int) tileSize);
                        rect.setFillColor("grey");
                        rect.setFillOpacity(0.3);
                        rectangles[sector.getX()][sector.getY()] = rect;
                        add(rect);
                    }
                }
            }
        }
        add(highLights);
    }

    public void highLightSectors(final List<SectorDTO> sectors) {
        removeHighLight();
        highLightedSectors.addAll(sectors);
        for (final SectorDTO sector : sectors) {
            countUpDowns[sector.getX()][sector.getY()] = 1;
            final FadeGroup f;
            f = new FadeGroup();
            f.setDuration(1);
            f.addEffectCompletedHandler(new EffectCompletedHandler() {
                @Override
                public void onEffectCompleted(final EffectCompletedEvent event) {
                    countUpDowns[sector.getX()][sector.getY()] = countUpDowns[sector.getX()][sector.getY()] + 1;
                    if (countUpDowns[sector.getX()][sector.getY()] <= 6) {
                        if (countUpDowns[sector.getX()][sector.getY()] % 2 == 1) {
                            f.setStartOpacity(lowOpacity);
                            f.setEndOpacity(highOpacity);
                        } else {
                            f.setStartOpacity(highOpacity);
                            f.setEndOpacity(lowOpacity);
                        }
                        f.play();
                    }
                }
            });
            remove(rectangles[sector.getX()][sector.getY()]);
            final Rectangle rect = new Rectangle(MapStore.getInstance().getPointX(sector.getX()),
                    MapStore.getInstance().getPointY(sector.getY()),
                    (int) MapStore.getInstance().getTileSize(), (int) MapStore.getInstance().getTileSize());
            rect.setFillColor("green");
            rect.setFillOpacity(0.5);
            highLights.add(rect);
            f.addEffectElement(highLights.getElement());
            f.setStartOpacity(lowOpacity);
            f.setEndOpacity(highOpacity);
            f.play();
        }
    }

    public void removeHighLight() {
        highLights.clear();
        for (final SectorDTO sector : highLightedSectors) {
            add(rectangles[sector.getX()][sector.getY()]);
        }
        highLightedSectors.clear();
    }
}
