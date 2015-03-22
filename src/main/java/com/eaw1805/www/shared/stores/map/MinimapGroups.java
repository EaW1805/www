package com.eaw1805.www.shared.stores.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.asyncs.map.SupplyLinesAsyncCallBack;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.views.layout.MiniMapPanel;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MinimapGroups
        implements RegionConstants, TerrainConstants, StyleConstants {

    /**
     * Our instance of the MinimapGroup
     */
    private static transient MinimapGroups ourInstance = null;

    private final Map<Integer, Group> miniMapImages = new HashMap<Integer, Group>(4);
    private final Map<Integer, Group> miniMapPolitical = new HashMap<Integer, Group>(4);
    private final Map<Integer, Group> mapPolitical = new HashMap<Integer, Group>(4);
    private final Map<Integer, Group> mapSupplies = new HashMap<Integer, Group>(4);

    /**
     * Method returning the miniature map group manager
     *
     * @return UnitGroups
     */
    public static MinimapGroups getInstance() {
        if (ourInstance == null) {
            ourInstance = new MinimapGroups();
        }
        return ourInstance;
    }

    void loadMiniMapImages(final int regionId) {
        final Image imgMinmap;
        switch (regionId) {
            case empire.data.constants.RegionConstants.CARIBBEAN:
                imgMinmap = new Image(0, 0, 120, 90, "http://static.eaw1805.com/tiles/minimaps/s" + GameStore.getInstance().getScenarioId() + "/" + regionId
                        + PNG);
                break;

            case empire.data.constants.RegionConstants.INDIES:
                imgMinmap = new Image(0, 0, 120, 90, "http://static.eaw1805.com/tiles/minimaps/s" + GameStore.getInstance().getScenarioId() + "/" + regionId
                        + PNG);
                break;

            case empire.data.constants.RegionConstants.AFRICA:
                imgMinmap = new Image(0, 0, 120, 90, "http://static.eaw1805.com/tiles/minimaps/s" + GameStore.getInstance().getScenarioId() + "/" + regionId
                        + PNG);
                break;


            case empire.data.constants.RegionConstants.EUROPE:
            default:
                switch (GameStore.getInstance().getScenarioId()) {
                    case HibernateUtil.DB_FREE:
                        imgMinmap = new Image(0, 0, 78, 90, "http://static.eaw1805.com/tiles/minimaps/s" + GameStore.getInstance().getScenarioId() + "/" + regionId
                                + PNG);
                        break;

                    case HibernateUtil.DB_S3:
                        imgMinmap = new Image(0, 0, 78, 90, "http://static.eaw1805.com/tiles/minimaps/s" + GameStore.getInstance().getScenarioId() + "/" + regionId
                                + PNG);
                        break;

                    case HibernateUtil.DB_S1:
                    case HibernateUtil.DB_S2:
                    default:
                        imgMinmap = new Image(0, 0, 170, 160, "http://static.eaw1805.com/tiles/minimaps/s" + GameStore.getInstance().getScenarioId() + "/" + regionId
                                + PNG);
                        break;
                }
                break;

        }

        final Rectangle selector = new Rectangle(0, 0, MiniMapPanel.SELECTOR_WIDTH, MiniMapPanel.SELECTOR_HEIGHT);
        selector.setFillOpacity(0.2);
        selector.setFillColor("blue");
        selector.setStrokeColor("white");
        selector.setStrokeWidth(1);

        final Group imgGroup = miniMapImages.get(regionId);

        imgGroup.add(imgMinmap);
        imgGroup.add(miniMapPolitical.get(regionId));
        imgGroup.add(selector);
    }

    void drawPolitical(final SectorDTO[][] sectors, final int regionId) {
        final int miniSizeX, miniSizeY;
        if (sectors[0][0].getRegionId() == EUROPE
                && GameStore.getInstance().getScenarioId() != HibernateUtil.DB_FREE) {
            miniSizeX = 2;
            miniSizeY = 2;

        } else {
            miniSizeX = 3;
            miniSizeY = 3;
        }

        final NationDTO myNation = DataStore.getInstance().getNationById(GameStore.getInstance().getNationId());
        final char nationChar = String.valueOf(myNation.getCode()).toLowerCase().charAt(0);
        final String sphereOfInfluence = myNation.getSphereOfInfluence().toLowerCase();
        new DelayIterator(0, sectors.length, 1) {

            @Override
            public void executeStep() {
                final SectorDTO[] sector = sectors[ITERATE_INDEX];
                for (int y = 0; y < sectors[0].length; y++) {
                    if (sector[y] != null) {
                        final char thisSectorCode = String.valueOf(sector[y].getPoliticalSphere()).toLowerCase().charAt(0);
                        final String thisSectorSOI = String.valueOf(sector[y].getPoliticalSphere()).toLowerCase();
                        final String colorMini;

                        if (sector[y].getTerrainId() == TERRAIN_O) {
                            colorMini = "#000070";
                        } else {
                            colorMini = "#" + sector[y].getNationDTO().getColor();
                        }

                        final int sectorX = MapStore.getInstance().getPointX(sector[y].getX());
                        final int sectorY = MapStore.getInstance().getPointY(sector[y].getY());

                        if (sector[y].getTerrainId() != TERRAIN_O) {
                            final Rectangle rect = new Rectangle(sectorX, sectorY,
                                    (int) MapStore.getInstance().getTileSize(), (int) MapStore.getInstance().getTileSize());

                            if (nationChar == thisSectorCode) {
                                rect.setFillColor("#fafafa");
                                rect.setFillOpacity(0.6d);

                            } else if (sphereOfInfluence.contains(thisSectorSOI)) {
                                rect.setFillColor("#47934a");
                                rect.setFillOpacity(0.45d);

                            } else {
                                rect.setFillColor("#0d0101");
                                rect.setFillOpacity(0.3d);
                            }

                            rect.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                            mapPolitical.get(regionId).add(rect);
                        }

                        final int miniSectorX = sector[y].getX() * miniSizeX;
                        final int miniSectorY = sector[y].getY() * miniSizeY;

                        final Rectangle miniRect = new Rectangle(miniSectorX, miniSectorY, miniSizeX, miniSizeY);
                        miniRect.setFillColor(colorMini);
                        miniRect.setStrokeOpacity(0);
                        miniRect.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
                        miniMapPolitical.get(regionId).add(miniRect);
                    }
                }
            }

            @Override
            public void executeLast() {
                //nothing to do here
            }
        }.run();
//        for (final SectorDTO[] sector : sectors) {
//
//        }
    }

    public void retrieveSupplies(final int regionId) {
        final SupplyLinesAsyncCallBack async = new SupplyLinesAsyncCallBack(this, regionId);
        final EmpireRpcServiceAsync empireService = GWT.create(EmpireRpcService.class);
        empireService.getSupplyLines(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(),
                regionId, GameStore.getInstance().getNationId(), async);
    }

    public void loadSupplies(final int regionId) {
        if (!mapSupplies.containsKey(regionId)) {
            retrieveSupplies(regionId);
            MapStore.getInstance().getMapsView().getMapDrawArea().add(mapSupplies.get(regionId));
        } else {
            MapStore.getInstance().getMapsView().getMapDrawArea().add(mapSupplies.get(regionId));
        }
    }

    public void drawSupplies(final Set<PositionDTO> sectors, final int regionId) {
        final Group thisRegionSupplies = new Group();

        for (final PositionDTO sector : sectors) {
            final int sectorX = MapStore.getInstance().getPointX(sector.getX());
            final int sectorY = MapStore.getInstance().getPointY(sector.getY());

            final Rectangle rect = new Rectangle(sectorX, sectorY,
                    (int) MapStore.getInstance().getTileSize(), (int) MapStore.getInstance().getTileSize());

            if (sector.getYStart() == 1) {
                rect.setFillColor("#f12424");
                rect.setFillOpacity(0.8d);

            } else if (sector.getXStart() == 1) {
                rect.setFillColor("#fafafa");
                rect.setFillOpacity(0.5d);

            } else {
                rect.setFillColor("#0d0101");
                rect.setFillOpacity(0.5d);
            }

            rect.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
            thisRegionSupplies.add(rect);
        }

        mapSupplies.put(regionId, thisRegionSupplies);
    }

    /**
     * Method that initializes the maps we will use
     * to store the images by adding them a group
     *
     * @param regionId the corresponding regionId
     */
    public void initVectorHasMaps(final int regionId) {
        miniMapImages.put(regionId, new Group());
        miniMapPolitical.put(regionId, new Group());
        mapPolitical.put(regionId, new Group());
    }

    public Group getMinimapImageByRegionId(final int regionId) {
        return miniMapImages.get(regionId);
    }

    public Group getRegionInfluenceTilesById(final int regionId) {
        return mapPolitical.get(regionId);
    }

    public Group getRegionSuppliesTilesById(final int regionId) {
        return mapSupplies.get(regionId);
    }

}
