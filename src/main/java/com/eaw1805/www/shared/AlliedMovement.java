package empire.webapp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import empire.data.dto.web.movement.MovementDTO;
import empire.data.dto.web.movement.PathDTO;

import java.io.Serializable;
import java.util.List;

public class AlliedMovement
        implements IsSerializable, Serializable {

    private int unitType;
    private int id;
    private boolean forcedMarch;
    private boolean patrol;
    private List<PathDTO> sectorPaths;

    public void setUpSectorPaths(final MovementDTO move) {
        sectorPaths = move.getPaths();
    }

    /**
     * @param unitType the unitType to set
     */
    public void setUnitType(final int unitType) {
        this.unitType = unitType;
    }

    /**
     * @return the unitType
     */
    public int getUnitType() {
        return unitType;
    }

    /**
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the sectorPaths
     */
    public List<PathDTO> getSectorPaths() {
        return sectorPaths;
    }

    public boolean isForcedMarch() {
        return forcedMarch;
    }

    public void setForcedMarch(boolean forcedMarch) {
        this.forcedMarch = forcedMarch;
    }

    public boolean isPatrol() {
        return patrol;
    }

    public void setPatrol(boolean patrol) {
        this.patrol = patrol;
    }
}
