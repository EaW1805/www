package com.eaw1805.www.fieldbattle.stores.dto;


import com.eaw1805.data.dto.common.NationDTO;

public class SocialPosition implements com.google.gwt.user.client.rpc.IsSerializable {
    /**
     * The players social id, aka, facebook id.
     * If empty means the nation doesn't has a player yet.
     */
    private int playerId;

    private String facebookId;

    /**
     * The nation this player has.
     */
    private NationDTO nation;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getFacebookId() {
        return facebookId;
    }


    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public NationDTO getNation() {
        return nation;
    }

    public void setNation(NationDTO nation) {
        this.nation = nation;
    }

}
