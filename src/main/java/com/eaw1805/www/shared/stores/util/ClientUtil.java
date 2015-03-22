package com.eaw1805.www.shared.stores.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClientUtil implements NationConstants {

    static int uniqueId = 0;

    static final Map<String, Long> keyToMeasure = new HashMap<String, Long>();

    private ClientUtil() {
        // avoid instantiating this class
    }

    public static String getUniqueId() {
        uniqueId++;
        return "widget-" + uniqueId;
    }

    public static boolean isMerchantile(final int nationId) {
        return (nationId == NATION_EGYPT
                || nationId == NATION_HOLLAND);
    }

    public static void printMessage(String msg) {
        if (GWT.isClient()) {
            Window.alert(msg);
        } else {
            System.out.println(msg);
        }
    }

    public static void startSpeedTest(final String key) {
        keyToMeasure.put(key, new Date().getTime());
    }

    public static void setDebugMessage(String msg) {
        GameStore.getInstance().getLayoutView().getSpeedTest().addMeasure(msg);
    }

    public static void stopSpeedTest(final String key, final String com) {
        if (GameStore.getInstance().getLayoutView() != null) {
            try {
                GameStore.getInstance().getLayoutView().getSpeedTest().addMeasure(com +
                        " (" + (new Date().getTime() - keyToMeasure.get(key)) + ")");
                GWT.log(com + " (" + (new Date().getTime() - keyToMeasure.get(key)) + ")");
            } catch (Exception e) {
                GameStore.getInstance().getLayoutView().getSpeedTest().addMeasure(com + " (failed)");
            }
        }
    }


}
