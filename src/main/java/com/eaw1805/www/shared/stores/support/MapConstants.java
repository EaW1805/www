package com.eaw1805.www.shared.stores.support;

/**
 * @author tsakygr
 *         A class that defines constants
 *         for the map. Here we can find the
 *         offset placing of the vectors for each
 *         and every zoom level
 */
public interface MapConstants {
    /**
     * Image folders
     */
    String DIR_ARMIES = "http://static.eaw1805.com/images/figures";

    String DIR_FLEET = "http://static.eaw1805.com/images/figures";

    /**
     * Int that defines the total zoom levels
     */
    int ZOOMLEVELS = 3;

    /**
     * The zoom level that the client starts.
     */
    int STARTLEVEL = 1;

    /**
     * The unit vector size for different zoom levels
     */
    int[] UNITSIZES = {24, 38, 58};

    /**
     * The unit vector size for different zoom levels
     */
    int[] POPINCDECSIZES = {21, 32, 48};

    /**
     * The unit vector size for different zoom levels
     */
    int[] POPSIZESIZES = {8, 12, 18};

    /**
     * Spy offsets for different zoom levels
     */
    int[][] SPYOFFSETS = {{-8, +22}, {-12, +36}, {-18, +62}};

    /**
     * Commander offsets for different zoom levels
     */
    int[][] COMMOFFSETS = {{-8, +22}, {-12, +36}, {-18, +62}};

    /**
     * Army offsets for different zoom levels
     */
    int[][] ARMYOFFSETS = {{-8, -8}, {-12, -12}, {-18, -18}};

    /**
     * Navy offsets for different zoom levels
     */
    int[][] NAVYOFFSETS = {{+22, -6}, {+36, -12}, {+62, -14}};

    /**
     * Btrain offsets for different zoom levels
     */
    int[][] BTRAINOFFSETS = {{+22, +22}, {+36, +36}, {+62, +62}};
    ;

    /**
     * Pr.Sites offsets for different zoom levels
     */
    int[][] PRSITESOFFSETS = {{0, 0}, {0, 0}, {0, 0}};

    /**
     * Pop.Size offsets for different zoom levels
     */
    int[][] POPSIZEOFFSETS = {{28, 32}, {51, 51}, {66, 86}};

}
