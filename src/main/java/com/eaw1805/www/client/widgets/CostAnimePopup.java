package com.eaw1805.www.client.widgets;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

public class CostAnimePopup extends AbsolutePanel {
    Label valueLbl;
    Fade f;
    boolean visible = false;
    Timer t;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
    int curValue = 0;

    public CostAnimePopup(final int width) {
        valueLbl = new Label("0");
        setSize(width + "px", "42px");
        setStyleName("costInfoX" + width);
        this.getElement().getStyle().setZIndex(100000);
        setHeight("32px");

        add(valueLbl, 13, 8);
        setVisible(false);
        f = new Fade(getElement());
        f.setDuration(1);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                if (!visible) {
                    CostAnimePopup.this.setVisible(false);
                }
            }
        });
        t = new Timer() {
            @Override
            public void run() {
                try {
                    visible = false;
                    f.setStartOpacity(100);
                    f.setEndOpacity(0);
                    f.play();
                } catch (Exception e) {

                }
            }
        };
    }

    public void setValueAndShow(int val) {
        try {
            setVisible(true);
            if (visible) {
                val = val + curValue;
                curValue = val;
                String sign = "+";
                if (val < 0) {
                    valueLbl.setStyleName("redCostText");
                    sign = "";
                } else {
                    valueLbl.setStyleName("greenCostText");
                }
                valueLbl.setText(sign + "" + val);
                t.schedule(5000);
                visible = true;
            } else {
                String sign = "+";
                curValue = val;
                if (val < 0) {
                    valueLbl.setStyleName("redCostText");
                    sign = "";
                } else {
                    valueLbl.setStyleName("greenCostText");
                }
                valueLbl.setText(sign + "" + numberFormat.format(val));
                f.setStartOpacity(0);
                f.setEndOpacity(100);
                f.play();
                t.schedule(5000);
                visible = true;
            }
        } catch (Exception e) {

        }
    }

    public void showWarning() {
        setVisible(true);
        valueLbl.setStyleName("redCostText");
        valueLbl.setText("Not enough!");
        f.setStartOpacity(0);
        f.setEndOpacity(100);
        f.play();
        t.schedule(5000);
        visible = true;
    }
}
