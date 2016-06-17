package com.eaw1805.www.client.gui;

import com.eaw1805.www.client.events.movement.MovementEventManager;
import com.eaw1805.www.client.movement.FiguresGroup;
import com.eaw1805.www.client.movement.MovementGroup;

public class GuiComponentMovement
        extends GuiComponentAbstract<Integer>
        implements GuiComponent {

    private MovementGroup myMovementGroup;

    public GuiComponentMovement(final int unitId, final int type, final MovementGroup mvGroup) {
        this.unitId = unitId;
        this.unitType = type;
        myMovementGroup = mvGroup;
    }

    public void registerComponent() {
        gameStore.registerComponent(this, true);
    }

    public void unRegisterComponent() {
        gameStore.unRegisterComponent(this, true);
    }

    public void handleEscape() {
        MovementEventManager.stopMovement(unitType, unitId);
        gameStore.unRegisterComponent(this, true);
    }

    @Override
    public void handleWave() {
        if (myMovementGroup.getFigures().getVectorObjectCount() > 1) {
            //remove last movement.
            myMovementGroup.removeMyMovement(((FiguresGroup) myMovementGroup.getFigures().getVectorObject(myMovementGroup.getFigures().getVectorObjectCount() - 1)));
        }

    }
}
