package empire.webapp.shared;

import empire.data.HibernateUtil;
import empire.data.constants.RegionConstants;
import empire.data.constants.TerrainConstants;
import empire.data.dto.common.BattleDTO;
import empire.data.dto.common.SectorDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("restriction")
public class TilesCollection
        implements com.google.gwt.user.client.rpc.IsSerializable, RegionConstants, TerrainConstants, Serializable {

    private Map<Integer, SectorDTO[][]> regionSectors = new HashMap<Integer, SectorDTO[][]>(4);

    private Map<SectorDTO, Integer> regionBuildDemPrSitesMap = new HashMap<SectorDTO, Integer>();

    private Map<Boolean, Map<Integer, BattleDTO>> battleToPositions = new HashMap<Boolean, Map<Integer, BattleDTO>>();

    public TilesCollection() {
        for (int regionId = REGION_FIRST; regionId <= REGION_LAST; regionId++) {
            regionSectors.put(regionId, new SectorDTO[0][0]);
        }
    }

    public void setUpRegion(final int scenarioId, final int regionId, final List<SectorDTO> sectors) {
        final int regionSizeX, regionSizeY;
        switch (scenarioId) {
            case HibernateUtil.DB_FREE:
                regionSizeX = RegionConstants.REGION_1804_SIZE_X[regionId - 1];
                regionSizeY = RegionConstants.REGION_1804_SIZE_Y[regionId - 1];
                break;

            case HibernateUtil.DB_S3:
                regionSizeX = RegionConstants.REGION_1808_SIZE_X[regionId - 1];
                regionSizeY = RegionConstants.REGION_1808_SIZE_Y[regionId - 1];
                break;

            case HibernateUtil.DB_S1:
            case HibernateUtil.DB_S2:
            default:
                regionSizeX = RegionConstants.REGION_1805_SIZE_X[regionId - 1];
                regionSizeY = RegionConstants.REGION_1805_SIZE_Y[regionId - 1];
                break;
        }

        final SectorDTO[][] sectorArray = new SectorDTO[regionSizeX][regionSizeY];
        for (SectorDTO SectorDTO : sectors) {
            try {
                sectorArray[SectorDTO.getX()][SectorDTO.getY()] = SectorDTO;

            } catch (Exception ex) {
                // eat it
            }
        }

        regionSectors.put(regionId, sectorArray);
    }

    public Map<Integer, SectorDTO[][]> getRegionSectorsArray() {
        return regionSectors;
    }

    public void setRegionBuildDemPrSitesMap(final Map<SectorDTO, Integer> regionBuildDemPrSitesMap) {
        this.regionBuildDemPrSitesMap = regionBuildDemPrSitesMap;
    }

    public Map<SectorDTO, Integer> getRegionBuildDemPrSitesMap() {
        return regionBuildDemPrSitesMap;
    }

    public Map<Boolean, Map<Integer, BattleDTO>> getBattleToPositions() {
        return battleToPositions;
    }

    public void setBattleToPositions(final Map<Boolean, Map<Integer, BattleDTO>> battleToPositions) {
        this.battleToPositions = battleToPositions;
    }

}
