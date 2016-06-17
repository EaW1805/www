package com.eaw1805.www.client.views.extras;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

public class RelationRow
        extends AbsolutePanel {

    private int nationId;

    public RelationRow(final int sourceNationId, final int nationId, final int relation) {
        this.setNationId(nationId);
        setSize("551px", "33px");
        int divisor = nationId;
        if (nationId > sourceNationId) {
            divisor = nationId + 1;
        }
//		if(divisor%2==1){
//			this.setStyleName("evenRow");
//		}else{
//			this.setStyleName("oddRow");
//		}
        if (sourceNationId != nationId) {
            Image allianceImg = new Image("http://static.eaw1805.com/images/panels/relations/ButRelationsGreyAllianceOff.png");
            Image rightOfPassImg = new Image("http://static.eaw1805.com/images/panels/relations/ButRelationsGreyPassageOff.png");
            Image tradeImg = new Image("http://static.eaw1805.com/images/panels/relations/ButRelationsGreyTradeOff.png");
            Image neutralImg = new Image("http://static.eaw1805.com/images/panels/relations/ButRelationsGreyNeutralOff.png");
            Image warImg = new Image("http://static.eaw1805.com/images/panels/relations/ButRelationsGreyWarOff.png");
            add(allianceImg, 1, 0);
            add(rightOfPassImg, 111, 0);
            add(tradeImg, 221, 0);
            add(neutralImg, 331, 0);
            add(warImg, 441, 0);


            switch (relation) {
                case 1:
                    allianceImg.setUrl("http://static.eaw1805.com/images/panels/relations/ButRelationsAllianceOn.png");
                    break;
                case 2:
                    rightOfPassImg.setUrl("http://static.eaw1805.com/images/panels/relations/ButRelationsPassageOn.png");
                    break;
                case 3:
                    tradeImg.setUrl("http://static.eaw1805.com/images/panels/relations/ButRelationsTradeOn.png");
                    break;
                case 4:
                    neutralImg.setUrl("http://static.eaw1805.com/images/panels/relations/ButRelationsNeutralOn.png");
                    break;
                case 5:
                    warImg.setUrl("http://static.eaw1805.com/images/panels/relations/ButRelationsWarOn.png");
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * @param nationId the nationId to set
     */
    public void setNationId(final int nationId) {
        this.nationId = nationId;
    }

    /**
     * @return the nationId
     */
    public int getNationId() {
        return nationId;
    }
}
