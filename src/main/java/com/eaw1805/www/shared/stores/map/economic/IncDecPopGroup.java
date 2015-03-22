package com.eaw1805.www.shared.stores.map.economic;

import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.OrderPositionDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.economy.SectorChangeEvent;
import com.eaw1805.www.client.events.economy.SectorChangeHandler;
import com.eaw1805.www.client.widgets.MapImage;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.support.MapConstants;
import org.vaadin.gwtgraphics.client.Group;

import java.util.Map;

public class IncDecPopGroup
        extends Group
        implements OrderConstants, MapConstants {

    private int regionId;

    public IncDecPopGroup(final Map<Integer, OrderPositionDTO> sectorOrderMap, final int regionId) {
        super();
        this.regionId = regionId;

        EcoEventManager.addSectorChangeHandler(new SectorChangeHandler() {
            public void onSectorChange(final SectorChangeEvent event) {
                if (event.getRegion() == regionId) {
                    addIncDecPopuplationImages(RegionStore.getInstance().getSectorOrderMap());
                }

            }
        });
        addIncDecPopuplationImages(sectorOrderMap);
    }


    public void addIncDecPopuplationImages(final Map<Integer, OrderPositionDTO> sectorOrderMap) {
        this.clear();
        final double zoomLvl = MapStore.getInstance().getZoomLevel();
        for (OrderPositionDTO pos : sectorOrderMap.values()) {
            final SectorDTO posSector = RegionStore.getInstance().getRegionSectorsMap().get(regionId).get(Integer.parseInt(pos.getParameter1()));
            if (posSector != null) {
                int xPos = posSector.getX();
                int yPos = posSector.getY();
                MapImage psUCImage;
                if (pos.getOrderType() == ORDER_INC_POP) {
                    psUCImage = new MapImage(MapStore.getInstance().getPointX(xPos),
                            MapStore.getInstance().getPointY(yPos),
                            (int) (POPINCDECSIZES[1] * zoomLvl), (int) (POPINCDECSIZES[1] * zoomLvl),
                            "http://static.eaw1805.com/images/tiles/MUIPopUp.png");
                    psUCImage.setxPos(xPos);
                    psUCImage.setyPos(yPos);
                    this.add(psUCImage);
                } else if (pos.getOrderType() == ORDER_DEC_POP) {
                    psUCImage = new MapImage(MapStore.getInstance().getPointX(xPos),
                            MapStore.getInstance().getPointY(yPos),
                            (int) (POPINCDECSIZES[1] * zoomLvl), (int) (POPINCDECSIZES[1] * zoomLvl),
                            "http://static.eaw1805.com/images/tiles/MUIPopDown.png");
                    psUCImage.setxPos(xPos);
                    psUCImage.setyPos(yPos);
                    this.add(psUCImage);
                    if (Integer.parseInt(pos.getParameter4()) != posSector.getNationId()) {//then it is also handover
                        psUCImage = new MapImage(MapStore.getInstance().getPointX(xPos),
                                MapStore.getInstance().getPointY(yPos),
                                (int) (POPINCDECSIZES[1] * zoomLvl), (int) (POPINCDECSIZES[1] * zoomLvl),
                                "http://static.eaw1805.com/images/tiles/MUIHandOver.png");
                        psUCImage.setxPos(xPos);
                        psUCImage.setyPos(yPos);
                        this.add(psUCImage);
                    }
                } else {
                    psUCImage = new MapImage(MapStore.getInstance().getPointX(xPos),
                            MapStore.getInstance().getPointY(yPos),
                            (int) (POPINCDECSIZES[1] * zoomLvl), (int) (POPINCDECSIZES[1] * zoomLvl),
                            "http://static.eaw1805.com/images/tiles/MUIHandOver.png");
                    psUCImage.setxPos(xPos);
                    psUCImage.setyPos(yPos);
                    this.add(psUCImage);
                }

            }
        }
    }
}
