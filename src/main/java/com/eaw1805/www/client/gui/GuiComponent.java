package com.eaw1805.www.client.gui;

public interface GuiComponent {

    void setId(final int id);

    int getId();

    int getUnitType();

    int getUnitId();

    void handleEscape();

    void handleWave();

    void handleEnter();

    void registerComponent();

    void unRegisterComponent();
}
