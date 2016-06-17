package com.eaw1805.www.scenario.stores;


import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.RankDTO;
import com.eaw1805.www.scenario.views.CommanderBrushView;
import com.eaw1805.www.scenario.views.EditorPanel;

import java.util.HashMap;
import java.util.Map;

public class CommanderUtils {

    private static int commanderId = -1;
    public static void createCommander(final int x, final int y) {
        final CommanderDTO commander = new CommanderDTO();
        commander.setId(getCommanderId());
        CommanderBrushView brush = EditorPanel.getInstance().getCommBrush();
        commander.setName(brush.getName());
        if (brush.getNation() != null) {
            commander.setNationId(brush.getNation().getNationId());
        }
        commander.setRank(brush.getRank());
        commander.setComc(brush.getComc());
        commander.setStrc(brush.getStr());
        commander.setRegionId(RegionSettings.region.getRegionId());
        commander.setX(x);
        commander.setY(y);

        final Map<Integer, Map<Integer, Map<Integer, CommanderDTO>>> commMap = EditorStore.getInstance().getCommanders().get(RegionSettings.region.getRegionId());
        if (!commMap.containsKey(x)) {
            commMap.put(x, new HashMap<Integer, Map<Integer, CommanderDTO>>());
        }
        if (!commMap.get(x).containsKey(y)) {
            commMap.get(x).put(y, new HashMap<Integer, CommanderDTO>());
        }
        commMap.get(x).get(y).put(commander.getId(), commander);
        EditorMapUtils.getInstance().drawCommander(commander);
    }




    private static int getCommanderId() {
        commanderId--;
        return commanderId;
    }
}
