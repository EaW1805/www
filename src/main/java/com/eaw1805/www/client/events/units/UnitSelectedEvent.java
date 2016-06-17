package com.eaw1805.www.client.events.units;

import com.google.gwt.event.shared.GwtEvent;

public class UnitSelectedEvent
        extends GwtEvent<UnitSelectedHandler> {

    private static final Type<UnitSelectedHandler> TYPE = new Type<UnitSelectedHandler>();

    private final int infoType;

    private final int infoId;

    private final int nationId;

    private final int sectorId;

    public UnitSelectedEvent(final int infoType, final int infoId, final int nationId, final int sectorId) {
        this.infoType = infoType;
        this.infoId = infoId;
        this.nationId = nationId;
        this.sectorId = sectorId;
    }

    public static Type<UnitSelectedHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final UnitSelectedHandler handler) {
        handler.onUnitSelected(this);
    }

    public com.google.gwt.event.shared.GwtEvent.Type<UnitSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the infoType
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @return the infoId
     */
    public int getInfoId() {
        return infoId;
    }

    public int getNationId() {
        return nationId;
    }

    public int getSectorId() {
        return sectorId;
    }

}
