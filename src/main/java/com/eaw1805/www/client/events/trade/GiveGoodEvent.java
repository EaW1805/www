package com.eaw1805.www.client.events.trade;

import com.google.gwt.event.shared.GwtEvent;

public class GiveGoodEvent
        extends GwtEvent<GiveGoodHandler> {

    private static final Type<GiveGoodHandler> TYPE = new Type<GiveGoodHandler>();

    private final int unitId, goodId, unitType, qte, regionId;

    public GiveGoodEvent(final int unitId, final int goodId, final int unitType, final int qte, final int regionId) {
        this.unitId = unitId;
        this.goodId = goodId;
        this.unitType = unitType;
        this.qte = qte;
        this.regionId = regionId;
    }

    public static Type<GiveGoodHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final GiveGoodHandler handler) {
        handler.onGiveGoodIn(this);
    }

    public Type<GiveGoodHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the goodId
     */
    public int getGoodId() {
        return goodId;
    }

    /**
     * @return the unitType
     */
    public int getUnitType() {
        return unitType;
    }

    /**
     * @return the qte
     */
    public int getQte() {
        return qte;
    }

    /**
     * @return the regionId
     */
    public int getRegionId() {
        return regionId;
    }

    /**
     * @return the unitId
     */
    public int getUnitId() {
        return unitId;
    }


}
