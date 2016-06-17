package com.eaw1805.www.scenario.widgets;


public abstract class SelectEAW<J>
        extends com.eaw1805.www.fieldbattle.widgets.SelectEAW<J> {

    public SelectEAW(String title) {
        super(title);
        getElement().getStyle().setBackgroundColor("grey");
    }
}
