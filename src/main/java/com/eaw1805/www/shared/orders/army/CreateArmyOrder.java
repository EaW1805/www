package empire.webapp.shared.orders.army;

import empire.data.dto.web.army.ArmyDTO;
import empire.data.dto.web.army.CommanderDTO;
import empire.data.dto.web.army.CorpDTO;
import empire.webapp.shared.orders.Order;

import java.util.HashMap;
import java.util.Map;


public class CreateArmyOrder
        implements Order {

    private Map<Integer, ArmyDTO> armiesMap;
    private String name;
    private int xPos, yPos, regionId, nationId;


    public CreateArmyOrder(final Map<Integer, ArmyDTO> armiesMap,
                           final String name,
                           final int xPos, final int yPos, final int regionId,
                           final int nationId) {
        this.armiesMap = armiesMap;
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.regionId = regionId;
        this.nationId = nationId;
    }

    public int execute(final int unitId) {
        final ArmyDTO newArmy = new ArmyDTO();
        newArmy.setArmyId(unitId);
        newArmy.setCommander(new CommanderDTO());
        newArmy.setCorps(new HashMap<Integer, CorpDTO>());
        newArmy.setName(getName());
        newArmy.setOriginalName(getName());
        newArmy.setX(getX());
        newArmy.setY(getY());
        newArmy.setXStart(getX());
        newArmy.setYStart(getY());
        newArmy.setRegionId(getRegionId());
        newArmy.setNationId(nationId);
        armiesMap.put(unitId, newArmy);
        return 1;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

