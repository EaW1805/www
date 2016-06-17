package com.eaw1805.www.fieldbattle.stores.utils;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.widgets.DrawingAreaFB;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class AnimationUtils {

    public static void showElement(final Widget widget, final BasicHandler handler) {
        widget.setVisible(true);
        final Fade f;
        f = new Fade(widget.getElement());
        f.setDuration(2);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                handler.run();
            }
        });

        f.setStartOpacity(0);
        f.setEndOpacity(100);
        f.play();
    }

    public static void hideElement(final Widget widget) {
        hideElement(widget, new BasicHandler() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public static void showElement(final Widget widget) {
        showElement(widget, new BasicHandler() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public static void hideElement(final Widget widget, final BasicHandler handler) {
        final Fade f;
        f = new Fade(widget.getElement());
        f.setDuration(2);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                widget.setVisible(false);
                handler.run();
            }
        });

        f.setStartOpacity(100);
        f.setEndOpacity(0);
        f.play();
    }

    final static int animationPeriod = 10000;
    final static int framePeriod = 100;
    static Date start;
    final static List<BrigadeDTO> brigades = new ArrayList<BrigadeDTO>();

    public static void stopArmyAnimation() {
        try {
            t.cancel();
        } catch (Exception e) {
            //eat it
        }
    }

    static Timer t = new Timer() {
        @Override
        public void run() {

            final Date now = new Date();

            if (now.getTime() - start.getTime() < 10000) {
                for (BrigadeDTO brigade : brigades) {
//                    MainPanel.getInstance().getMapUtils().addArmyImageForPlayback(brigade, now.getTime() - start.getTime(), animationPeriod);
                }
                schedule(framePeriod);
            } else {
                MainPanel.getInstance().getPlayback().nextStep();
            }
        }
    };

    public static void animateArmyMovement() {
        stopArmyAnimation();
        brigades.clear();
        for (BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(MainPanel.getInstance().getPlayback().getRound()).getAllBrigades()) {
            if (brigade.isPlacedOnFieldMap()) {
                brigades.add(brigade);
            }
        }
        start = new Date();
        t.run();

    }

    private static Timer mapTimer;
    private static int mapStep = 50;

    public static void cancelMapAnimation() {
        if (mapTimer != null) {
            try {
                mapTimer.cancel();
            } catch (Exception e) {
                //eat it
            }
        }
    }

    public static void animateMapScroll(final int x, final int y, final BasicHandler stepHandler, final BasicHandler endHandler) {
        final DrawingAreaFB area = MainPanel.getInstance().getDrawingArea();
        //if you give a new animation order
        //cancel any previous one.
        cancelMapAnimation();
        mapTimer = new Timer() {
            @Override
            public void run() {
                if (Math.abs(area.getScroller().getScrollPosition() - y) < mapStep + 1) {
                    area.getScroller().setScrollPosition(y);
                } else if (area.getScroller().getScrollPosition() < y) {
                    area.getScroller().setScrollPosition(area.getScroller().getScrollPosition() + mapStep);
                } else if (area.getScroller().getScrollPosition() > y) {
                    area.getScroller().setScrollPosition(area.getScroller().getScrollPosition() - mapStep);
                }
                if (Math.abs(area.getScroller().getHorizontalScrollPosition() - x) < mapStep + 1) {
                    area.getScroller().setHorizontalScrollPosition(x);
                } else if (area.getScroller().getHorizontalScrollPosition() < x) {
                    area.getScroller().setHorizontalScrollPosition(area.getScroller().getHorizontalScrollPosition() + mapStep);
                } else if (area.getScroller().getHorizontalScrollPosition() > x) {
                    area.getScroller().setHorizontalScrollPosition(area.getScroller().getHorizontalScrollPosition() - mapStep);
                }
                if (stepHandler != null) {
                    stepHandler.run();
                }
                if (area.getScroller().getHorizontalScrollPosition() != x
                        || area.getScroller().getScrollPosition() != y) {
                    schedule(10);
                } else if (endHandler != null) {
                    endHandler.run();
                }
            }
        };
        mapTimer.run();


    }

}
