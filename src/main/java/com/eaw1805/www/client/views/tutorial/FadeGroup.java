package com.eaw1805.www.client.views.tutorial;

import com.google.gwt.dom.client.Element;
import org.adamtacy.client.ui.effects.core.NMorphScalar;

public class FadeGroup extends NMorphScalar {
    public double getEndOpacity() {
        return super.getEndValue();
    }

    public double getStartOpacity() {
        return super.getStartValue();
    }

    /**
     * Set the ending opacity.
     *
     * @param percentage Ending opacity percentage.
     */
    public void setEndOpacity(double percentage) {
        super.setEndValue("" + percentage + "");

    }

    /**
     * Set the starting opacity.
     *
     * @param percentage Starting opacity in percentage.
     */
    public void setStartOpacity(double percentage) {
        super.setStartValue("" + percentage + "");
    }

    public FadeGroup() {
        super("fill-opacity");
        setEndValue("0");
        setStartValue("100");
    }

    public FadeGroup(Element el){
        this();
        addEffectElement(el);
    }
}
