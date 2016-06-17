package com.eaw1805.www.client.events.movement;

import com.google.gwt.event.shared.GwtEvent;
import com.eaw1805.data.dto.common.SectorDTO;

public class StartAllyMoveEvent
        extends GwtEvent<StartAllyMoveHandler> {

    private static final Type<StartAllyMoveHandler> TYPE = new Type<StartAllyMoveHandler>();

    private final int infoType;

    private final int infoId;

    private final int nationId;

    private final SectorDTO startSector;

    public StartAllyMoveEvent(final int infoType, final int infoId, final int nationId, final SectorDTO startSector) {
        this.infoType = infoType;
        this.infoId = infoId;
        this.nationId = nationId;
        this.startSector = startSector;
    }

    public static Type<StartAllyMoveHandler> getType() {
        return TYPE;
    }


    protected void dispatch(final StartAllyMoveHandler handler) {
        handler.onStartAllyMove(this);
    }

    public Type<StartAllyMoveHandler> getAssociatedType() {
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

    /**
     * @return the nationId
     */
    public int getNationId() {
        return nationId;
    }

    public SectorDTO getStartSector() {
        return startSector;
    }
}
