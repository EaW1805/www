package com.eaw1805.www.client.events.trade;

import com.google.gwt.event.shared.GwtEvent;

public class GetGoodEvent
        extends GwtEvent<GetGoodHandler> {

    private static final Type<GetGoodHandler> TYPE = new Type<GetGoodHandler>();

    private final int unitId, goodId, unitType, qte, regionId;

    public GetGoodEvent(final int unitId, final int goodId, final int unitType, final int qte, final int regionId) {
        this.unitId = unitId;
        this.goodId = goodId;
        this.unitType = unitType;
        this.qte = qte;
        this.regionId = regionId;
    }

    public static Type<GetGoodHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final GetGoodHandler handler) {
        handler.onGetGoodIn(this);
    }

    public Type<GetGoodHandler> getAssociatedType() {
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
