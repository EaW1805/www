package com.eaw1805.www.shared.stores.util;

import com.google.gwt.user.client.ui.Widget;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;


public final class AnimationUtils {

    public static void showElement(final Widget widget, final BasicHandler handler) {
        widget.setVisible(true);
        final Fade f;
        f = new Fade(widget.getElement());
        f.setDuration(3);
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
        hideElement(widget, 100, new BasicHandler() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    public static void hideElement(final Widget widget, final int startOpacity) {
        hideElement(widget, 100, new BasicHandler() {
            @Override
            public void run() {

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

    public static void hideElement(final Widget widget, final int startOpacity, final BasicHandler handler) {
        final Fade f;
        f = new Fade(widget.getElement());
        f.setDuration(3);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                widget.setVisible(false);
                handler.run();
            }
        });

        f.setStartOpacity(startOpacity);
        f.setEndOpacity(0);
        f.play();
    }



}
