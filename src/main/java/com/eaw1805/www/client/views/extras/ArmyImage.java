package com.eaw1805.www.client.views.extras;

import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;

public class ArmyImage extends Image {

    private ArmyTypeDTO armyTypeDTO;
    private BattalionDTO empireBattalionDTO;

    public ArmyImage() {
        super();
    }

    public ArmyImage(final String url) {
        super(url);
    }

    public void setArmyTypeDTO(final ArmyTypeDTO empireArmyTypeDTO) {
        this.armyTypeDTO = empireArmyTypeDTO;
    }

    public ArmyTypeDTO getArmyTypeDTO() {
        return armyTypeDTO;
    }

    public void setEmpireBattalionDTO(final BattalionDTO empireBattalionDTO) {
        this.empireBattalionDTO = empireBattalionDTO;
    }

    public BattalionDTO getEmpireBattalionDTO() {
        return empireBattalionDTO;
    }

}
