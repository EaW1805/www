package com.eaw1805.www.client;

import com.google.gwt.i18n.client.Constants;

import java.util.Map;

public interface ClientConstants extends Constants {
    /**
     * Retrieve the build number.
     *
     * @return The build number
     */
    String buildNumber();

    Map<String, String> quotesMap();

    Map<String, String> personalitiesMap();


}
