package com.eaw1805.www.fieldbattle.stores;


public class AdStore {
    public static String FIRST_ADD = "<!-- First Ad alt -->\n" +
            "<ins class=\"adsbygoogle\"\n" +
            "     style=\"display:inline-block;width:728px;height:90px\"\n" +
            "     data-ad-client=\"ca-pub-5437756843446105\"\n" +
            "     data-ad-slot=\"7252270472\"></ins>";

    public static String FIRST_COLUMN = "<ins class=\"adsbygoogle\"\n" +
            "     style=\"display:inline-block;width:120px;height:240px\"\n" +
            "     data-ad-client=\"ca-pub-5437756843446105\"\n" +
            "     data-ad-slot=\"6554266475\"></ins>";

    public static String VERTICAL_LARGE = "<ins class=\"adsbygoogle\"\n" +
            "     style=\"display:inline-block;width:120px;height:600px\"\n" +
            "     data-ad-client=\"ca-pub-5437756843446105\"\n" +
            "     data-ad-slot=\"9507732879\"></ins>";

    public static native void showAds() /*-{
        try {
            (adsbygoogle = window.adsbygoogle || []).push({});
        } catch (e) {
            alert("Failed?  " + e);
        }
    }-*/;
}
