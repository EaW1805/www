package empire.webapp.shared.stores.util;

import empire.data.constants.RegionConstants;
import empire.data.dto.common.SectorDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Jump-off coordinates for 1804 scenario.
 */
public class JumpOffCoordinates1804
        implements RegionConstants {

    public final static List<List<SectorDTO>> jumpOffSectors = new ArrayList<List<SectorDTO>>(5);

    static {
        // Initialize 4 continents (+1 for 0 index)
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());

        // CARIBBEAN JumpOff Points to EUROPE
        for (int yPos = 7; yPos <= 10; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(CARIBBEAN);
            thisSector.setX(39);
            thisSector.setY(yPos);
            thisSector.setTerrainId(EUROPE);
            jumpOffSectors.get(CARIBBEAN).add(thisSector);
        }

        // EUROPE JumpOff Points to CARIBBEAN
        for (int yPos = 7; yPos <= 10; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(EUROPE);
            thisSector.setX(0);
            thisSector.setY(yPos);
            thisSector.setTerrainId(CARIBBEAN);
            jumpOffSectors.get(EUROPE).add(thisSector);
        }
    }

}
