package com.eaw1805.www.fieldbattle.widgets;


import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.fieldbattle.stores.AdStore;

public class AdWidget extends VerticalPanel {

    /**
     * Create an Ad in the given size and adsense code.
     *
     * @param width The width of the ad.
     * @param height The height of the ad.
     * @param code The code provided by adsense console.
     */
    public AdWidget(final int width, final int height, final String code) {
        setStyleName("tipPanel");
        add(new Label("Sponsored links"));
        add(new HTML(code));
    }

    public void onAttach() {
        super.onAttach();
        //when code is displayed.. execute push to retrieve an ad.
        AdStore.showAds();
    }

}
