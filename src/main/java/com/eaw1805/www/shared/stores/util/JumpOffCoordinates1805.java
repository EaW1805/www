package empire.webapp.shared.stores.util;

import empire.data.constants.RegionConstants;
import empire.data.dto.common.SectorDTO;

import java.util.ArrayList;
import java.util.List;

public final class JumpOffCoordinates1805
        implements RegionConstants {

    public final static List<List<SectorDTO>> jumpOffSectors = new ArrayList<List<SectorDTO>>(5);

    static {
        // Initialize 4 continents (+1 for 0 index)
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());
        jumpOffSectors.add(new ArrayList<SectorDTO>());

        // AFRICA JumpOff Points to EUROPE (1)
        for (int y = 0; y <= 2; y++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(AFRICA);
            thisSector.setX(0);
            thisSector.setY(y);
            thisSector.setTerrainId(EUROPE);
            jumpOffSectors.get(AFRICA).add(thisSector);
        }

        // AFRICA JumpOff Points to EUROPE (1b)
        {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(AFRICA);
            thisSector.setX(1);
            thisSector.setY(0);
            thisSector.setTerrainId(EUROPE);
            jumpOffSectors.get(AFRICA).add(thisSector);
        }

        // AFRICA JumpOff Points to EUROPE (2)
        for (int xPos = 38; xPos <= 39; xPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(AFRICA);
            thisSector.setX(xPos);
            thisSector.setY(0);
            thisSector.setTerrainId(EUROPE);
            jumpOffSectors.get(AFRICA).add(thisSector);
        }

        // AFRICA JumpOff Points to CARIBBEAN
        for (int yPos = 10; yPos <= 13; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(AFRICA);
            thisSector.setX(0);
            thisSector.setY(yPos);
            thisSector.setTerrainId(CARIBBEAN);
            jumpOffSectors.get(AFRICA).add(thisSector);
        }

        // AFRICA JumpOff Points to INDIES
        for (int yPos = 9; yPos <= 13; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(AFRICA);
            thisSector.setX(39);
            thisSector.setY(yPos);
            thisSector.setTerrainId(INDIES);
            jumpOffSectors.get(AFRICA).add(thisSector);
        }

        // INDIES JumpOff Points to EUROPE
        for (int yPos = 4; yPos <= 5; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(INDIES);
            thisSector.setX(0);
            thisSector.setY(yPos);
            thisSector.setTerrainId(EUROPE);
            jumpOffSectors.get(INDIES).add(thisSector);
        }

        // INDIES JumpOff Points to CARIBBEAN (1)
        for (int yPos = 4; yPos <= 6; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(INDIES);
            thisSector.setX(39);
            thisSector.setY(yPos);
            thisSector.setTerrainId(CARIBBEAN);
            jumpOffSectors.get(INDIES).add(thisSector);
        }

        // INDIES JumpOff Points to CARIBBEAN (2)
        for (int yPos = 21; yPos <= 23; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(INDIES);
            thisSector.setX(39);
            thisSector.setY(yPos);
            thisSector.setTerrainId(CARIBBEAN);
            jumpOffSectors.get(INDIES).add(thisSector);
        }

        // INDIES JumpOff Points to AFRICA
        for (int yPos = 9; yPos <= 13; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(INDIES);
            thisSector.setX(0);
            thisSector.setY(yPos);
            thisSector.setTerrainId(AFRICA);
            jumpOffSectors.get(INDIES).add(thisSector);
        }

        // CARIBBEAN JumpOff Points to EUROPE
        for (int yPos = 1; yPos <= 11; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(CARIBBEAN);
            thisSector.setX(39);
            thisSector.setY(yPos);
            thisSector.setTerrainId(EUROPE);
            jumpOffSectors.get(CARIBBEAN).add(thisSector);
        }

        // CARIBBEAN JumpOff Points to AFRICA
        for (int yPos = 19; yPos <= 22; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(CARIBBEAN);
            thisSector.setX(39);
            thisSector.setY(yPos);
            thisSector.setTerrainId(AFRICA);
            jumpOffSectors.get(CARIBBEAN).add(thisSector);
        }

        // CARIBBEAN JumpOff Points to INDIES (1)
        for (int xPos = 0; xPos <= 0; xPos++) {
            for (int yPos = 23; yPos <= 26; yPos++) {
                final SectorDTO thisSector = new SectorDTO();
                thisSector.setRegionId(CARIBBEAN);
                thisSector.setX(xPos);
                thisSector.setY(yPos);
                thisSector.setTerrainId(INDIES);
                jumpOffSectors.get(CARIBBEAN).add(thisSector);
            }
        }

        // CARIBBEAN JumpOff Points to INDIES (2)
        for (int xPos = 9; xPos <= 11; xPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(CARIBBEAN);
            thisSector.setX(xPos);
            thisSector.setY(29);
            thisSector.setTerrainId(INDIES);
            jumpOffSectors.get(CARIBBEAN).add(thisSector);

        }

        // EUROPE JumpOff Points to CARIBBEAN
        for (int yPos = 19; yPos <= 29; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(EUROPE);
            thisSector.setX(0);
            thisSector.setY(yPos);
            thisSector.setTerrainId(CARIBBEAN);
            jumpOffSectors.get(EUROPE).add(thisSector);
        }

        // EUROPE JumpOff Points to AFRICA (1)
        for (int yPos = 47; yPos <= 49; yPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(EUROPE);
            thisSector.setX(0);
            thisSector.setY(yPos);
            thisSector.setTerrainId(AFRICA);
            jumpOffSectors.get(EUROPE).add(thisSector);
        }

        {   // EUROPE JumpOff Points to AFRICA (1b)
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(EUROPE);
            thisSector.setX(0);
            thisSector.setY(51);
            thisSector.setTerrainId(AFRICA);
            jumpOffSectors.get(EUROPE).add(thisSector);
        }

        // EUROPE JumpOff Points to AFRICA (2)
        for (int xPos = 70; xPos <= 71; xPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(EUROPE);
            thisSector.setX(xPos);
            thisSector.setY(76);
            thisSector.setTerrainId(AFRICA);
            jumpOffSectors.get(EUROPE).add(thisSector);
        }

        // EUROPE JumpOff Points to INDIES
        for (int xPos = 72; xPos <= 73; xPos++) {
            final SectorDTO thisSector = new SectorDTO();
            thisSector.setRegionId(EUROPE);
            thisSector.setX(xPos);
            thisSector.setY(76);
            thisSector.setTerrainId(INDIES);
            jumpOffSectors.get(EUROPE).add(thisSector);
        }
    }

}
