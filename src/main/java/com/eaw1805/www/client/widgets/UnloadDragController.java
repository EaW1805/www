package com.eaw1805.www.client.widgets;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NavigationConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.RelationConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TransportUnitDTO;
import com.eaw1805.www.client.views.figures.BrigadeInfoFigPanel;
import com.eaw1805.www.client.views.figures.CommanderInfoFigPanel;
import com.eaw1805.www.client.views.figures.SpyInfoFigPanel;
import com.eaw1805.www.client.views.military.deployment.UnloadTroopsView;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

public class UnloadDragController
        implements DragHandler, ArmyConstants, NavigationConstants {

    private final UnloadTroopsView uldTroopsView;

    public UnloadDragController(final UnloadTroopsView uldTroopsView) {
        this.uldTroopsView = uldTroopsView;
    }

    public void onDragEnd(final DragEndEvent event) {
        if (event.getContext().finalDropController != null) {
            final ClickAbsolutePanel dropPanel = (ClickAbsolutePanel) event.getContext().finalDropController.getDropTarget();

            final int transportId = uldTroopsView.getTransportId();
            int type = 0;
            if (event.getContext().draggable.getClass().equals(BrigadeInfoFigPanel.class)) {
                type = BRIGADE;

            } else if (event.getContext().draggable.getClass().equals(CommanderInfoFigPanel.class)) {
                type = COMMANDER;

            } else if (event.getContext().draggable.getClass().equals(SpyInfoFigPanel.class)) {
                type = SPY;
            }

            final int cargoId = getCargoId(type, event.getContext().draggable);
            TransportStore.getInstance().unloadCargoFromTransport(uldTroopsView.getTransportType(), transportId, type, cargoId, dropPanel.getId(), uldTroopsView.getTradePhase());
            dropPanel.clear();
            this.uldTroopsView.populateDirectionalHolders();
        }
    }

    private int getCargoId(final int cargoType, final Widget draggable) {
        int cargoId = 0;
        switch (cargoType) {
            case BRIGADE:
                cargoId = ((BrigadeInfoFigPanel) draggable).getFigImg().getId();
                break;

            case COMMANDER:
                cargoId = ((CommanderInfoFigPanel) draggable).getFigImg().getId();
                break;

            case SPY:
                cargoId = ((SpyInfoFigPanel) draggable).getFigImg().getId();
                break;

            default:
                // do nothing
        }
        return cargoId;
    }


    public void onDragStart(final DragStartEvent event) {
        // do nothing here
    }

    //here, validate if the unit can be unloaded
    public void onPreviewDragEnd(final DragEndEvent event) throws VetoDragException {
        if (event.getContext().finalDropController != null) {

            final ClickAbsolutePanel dropPanel = (ClickAbsolutePanel) event.getContext().finalDropController.getDropTarget();

            final int transportId = uldTroopsView.getTransportId();
            final TransportUnitDTO tUnit = TransportStore.getInstance().getTransportUnitById(uldTroopsView.getTransportType(), transportId);
            int x = tUnit.getX();
            int y = tUnit.getY();
            if (event.getContext().draggable.getClass().equals(BrigadeInfoFigPanel.class)) {
                SectorDTO sector = null;
                switch (dropPanel.getId()) {
                    case 0:
                        sector = RegionStore.getInstance().getRegionSectorsByRegionId(tUnit.getRegionId())[x][y];
                        break;
                    case WEST:
                        sector = RegionStore.getInstance().getRegionSectorsByRegionId(tUnit.getRegionId())[x - 1][y];
                        break;
                    case SOUTH:
                        sector = RegionStore.getInstance().getRegionSectorsByRegionId(tUnit.getRegionId())[x][y + 1];
                        break;
                    case EAST:
                        sector = RegionStore.getInstance().getRegionSectorsByRegionId(tUnit.getRegionId())[x + 1][y];
                        break;
                    case NORTH:
                        sector = RegionStore.getInstance().getRegionSectorsByRegionId(tUnit.getRegionId())[x][y - 1];
                        break;
                    default:
                        //do nothing here
                }

                if (sector != null) {
                    if (sector.getNationId() > 0
                            && sector.getNationId() != GameStore.getInstance().getNationId()
                            && ((sector.getRegionId() == RegionConstants.EUROPE
                                && RelationsStore.getInstance().getRelationsMap().get(sector.getNationId()).getRelation() >= RelationConstants.REL_TRADE
                                && RelationsStore.getInstance().getRelationsMap().get(sector.getNationId()).getRelation() != RelationConstants.REL_WAR)
                            || (sector.getRegionId() != RegionConstants.EUROPE
                                && RelationsStore.getInstance().getRelationsMap().get(sector.getNationId()).getRelation() == RelationConstants.REL_TRADE))) {
                        throw new VetoDragException();
                    }

                }
            }
        }
    }

    public void onPreviewDragStart(final DragStartEvent event)
            throws VetoDragException {
        // do nothing here
    }

}
