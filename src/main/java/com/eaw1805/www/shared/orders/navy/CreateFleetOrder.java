package empire.webapp.shared.orders.navy;

import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.fleet.FleetDTO;
import empire.data.dto.web.fleet.ShipDTO;
import empire.webapp.shared.orders.Order;

import java.util.HashMap;
import java.util.Map;

public class CreateFleetOrder implements Order {

    private Map<Integer, FleetDTO> fleetsMap;
    private String name;
    private CommanderDTO commander;
    private int xPos, yPos, regionId, nationId;

    public CreateFleetOrder(final Map<Integer, FleetDTO> armiesMap,
                            final String name,
                            final CommanderDTO commander,
                            final int xPos,
                            final int yPos,
                            final int regionId,
                            final int nationId) {
        this.fleetsMap = armiesMap;
        this.name = name;
        this.commander = commander;
        this.xPos = xPos;
        this.yPos = yPos;
        this.regionId = regionId;
        this.nationId = nationId;
    }

    public int execute(final int unitId) {

        try {
            final FleetDTO newFleet = new FleetDTO();
            newFleet.setFleetId(unitId);
            //newFleet.setCommander(getCommander());
            newFleet.setShips(new HashMap<Integer, ShipDTO>());
            newFleet.setName(getName());
            newFleet.setX(getX());
            newFleet.setY(getY());
            newFleet.setXStart(getX());
            newFleet.setYStart(getY());
            newFleet.setRegionId(getRegionId());
            newFleet.setNationId(nationId);
            getFleetsMap().put(unitId, newFleet);
            return 1;

        } catch (Exception ex) {
            return 0;
        }
    }

    public Map<Integer, FleetDTO> getFleetsMap() {
        return fleetsMap;
    }

    public String getName() {
        return name;
    }

    public CommanderDTO getCommander() {
        return commander;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setCommander(final CommanderDTO commander) {
        this.commander = commander;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setX(final int xPos) {
        this.xPos = xPos;
    }

    public void setY(final int yPos) {
        this.yPos = yPos;
    }

    public void setRegionId(final int regionId) {
        this.regionId = regionId;
    }

}

