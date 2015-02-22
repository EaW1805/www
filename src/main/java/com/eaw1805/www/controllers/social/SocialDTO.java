package com.eaw1805.www.controllers.social;

/**
 * Handles User Data for Facebook / Twitter.
 */
public class SocialDTO {

    public final static int NO_CONNECTION = -1;

    public final static int NO_LIKE = 0;

    public final static int NO_FOLLOW = 0;

    public final static int FOLLOW = 1;

    public final static int LIKED = 1;

    public final static int NO_SHARED = 2;

    public final static int SHARED= 3;

    public final static int COMPLETE= 4;

    private final int facebookConnectionStatus;

    private final int twitterConnectionStatus;

    /**
     * Consructo.
     *
     * @param facebookStatus the facebook Status.
     * @param twitterStatus  the twitter Status.
     */
    public SocialDTO(final int facebookStatus, final int twitterStatus) {
        this.facebookConnectionStatus = facebookStatus;
        this.twitterConnectionStatus = twitterStatus;
    }

    public int getFacebookConnectionStatus() {
        return facebookConnectionStatus;
    }

    public int getTwitterConnectionStatus() {
        return twitterConnectionStatus;
    }


    //
    // For use in JSP.
    //

    public int getNoConnection() {
        return NO_CONNECTION;
    }

    public int getNoLike() {
        return NO_LIKE;
    }

    public int getNoFollow() {
        return NO_FOLLOW;
    }

    public int getFollow() {
        return FOLLOW;
    }

    public int getLiked() {
        return LIKED;
    }

    public int getNoShared() {
        return NO_SHARED;
    }

    public int getShared() {
        return SHARED;
    }

    public int getComplete() {
        return COMPLETE;
    }


}
