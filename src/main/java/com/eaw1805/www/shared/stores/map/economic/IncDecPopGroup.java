package empire.webapp.shared.stores.map.economic;

import empire.data.constants.OrderConstants;
import empire.data.dto.common.SectorDTO;
import empire.data.dto.web.OrderPositionDTO;
import empire.webapp.client.events.economy.EcoEventManager;
import empire.webapp.client.events.economy.SectorChangeEvent;
import empire.webapp.client.events.economy.SectorChangeHandler;
import empire.webapp.client.widgets.MapImage;
import empire.webapp.shared.stores.RegionStore;
import empire.webapp.shared.stores.map.MapStore;
import empire.webapp.shared.stores.support.MapConstants;
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
