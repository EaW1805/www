package com.eaw1805.www.fieldbattle.stores.dto;


public class SocialFriend {
    /**
     * Facebook id of the friend.
     */
    private String id;

    /**
     * Facebook name of the friend.
     */
    private String name;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return "http://graph.facebook.com/" + id + "/picture?type=square";
    }
}
