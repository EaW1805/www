package com.eaw1805.www.client.widgets;

import com.google.gwt.user.client.ui.AbsolutePanel;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

/**
 * Panel used to animate costs with not enough capacity for buying.
 */
public class ResourceLowWarning extends AbsolutePanel {

    private final Fade f;
    private static final int lowOpacity = 0;
    private static final int highOpacity = 50;
    private int countUpDown = 0;
    private boolean forever;
    public ResourceLowWarning(final int width, final int height, final boolean rounded) {
        setSize(width + "px", height + "px");
        setStyleName("costLowWarning");
        if (rounded) {
            setStyleName("costLowRounded", true);
        }
        getElement().getStyle().setZIndex(10000);
        getElement().getStyle().setOpacity(0.0);

        f = new Fade(getElement());
        f.setDuration(1);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                countUpDown++;
                if (countUpDown <= 6 || forever) {
                    if (countUpDown % 2 == 1) {
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
    }

    public void showWarning(boolean forever) {
        this.forever = forever;
        countUpDown = 1;
        f.setStartOpacity(lowOpacity);
        f.setEndOpacity(highOpacity);
        f.play();
    }

    public void hideWarning() {
        forever = false;//be sure it won't be set to forever
        countUpDown = 7;//so it will not repeat itself.. it only repeats 6 times
        f.setStartOpacity(highOpacity);
        f.setEndOpacity(lowOpacity);
        f.play();
    }


}
