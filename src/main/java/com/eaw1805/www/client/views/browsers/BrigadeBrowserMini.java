package com.eaw1805.www.client.views.browsers;

import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.views.popups.BrigadeExtraPopup;
import com.eaw1805.www.client.widgets.BrowserWidget;

public class BrigadeBrowserMini extends BrowserWidget {
    public BrigadeBrowserMini(final BrigadeDTO brigade) {
        setStyleName("costInfoMini");
        setSize(199, 166);
        addWidgetAndGo(new BrigadeExtraPopup(brigade));
    }
}
