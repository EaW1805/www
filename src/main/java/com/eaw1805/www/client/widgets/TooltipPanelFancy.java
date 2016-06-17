package com.eaw1805.www.client.widgets;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;

public class TooltipPanelFancy extends PopupPanel {

    public void hideFancy() {
        final TooltipPanelFancy myself = this;
        myself.getElement().getStyle().setOpacity(1.0);

        new Timer() {
            @Override
            public void run() {
                myself.setPopupPosition(myself.getAbsoluteTop() - 5, myself.getAbsoluteLeft());
                myself.getElement().getStyle().setOpacity(Double.valueOf(myself.getElement().getStyle().getOpacity()) - 0.1);
                if (Double.valueOf(myself.getElement().getStyle().getOpacity()) > 0) {
                    schedule(20);
                } else {
                    myself.hide();
                    myself.getElement().getStyle().setOpacity(1.0);
                }
            }
        }.run();


//        final Fade f;
//        f = new Fade(this.getElement());
//        f.setDuration(1);
//        f.addEffectCompletedHandler(new EffectCompletedHandler() {
//            @Override
//            public void onEffectCompleted(EffectCompletedEvent effectCompletedEvent) {
//                TooltipPanelFancy.this.hide();
//            }
//        });
//        f.setStartOpacity(100);
//        f.setEndOpacity(0);
//        f.play();
    }
}
