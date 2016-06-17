package com.eaw1805.www.client.gui;

import com.eaw1805.www.shared.stores.GameStore;

public abstract class GuiComponentAbstract<E> {

    protected E widget;
    protected int id;
    protected int position;
    protected int unitId;
    protected int unitType;

    protected final GameStore gameStore = GameStore.getInstance();

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(final int unitId) {
        this.unitId = unitId;
    }

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(final int unitType) {
        this.unitType = unitType;
    }

    public E getWidget() {
        return widget;
    }

    public void setWidget(final E widget) {
        this.widget = widget;
    }

    public void registerComponent() {
        gameStore.registerComponent((GuiComponent) this, false);
    }

    public void unRegisterComponent() {
        gameStore.unRegisterComponent((GuiComponent) this, false);
    }

    public void handleEnter() {
        //do nothing here... should be overridden..
    }
}
