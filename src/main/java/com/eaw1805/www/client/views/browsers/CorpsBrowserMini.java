package com.eaw1805.www.client.views.browsers;

import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.views.popups.CorpsExtraPopup;
import com.eaw1805.www.client.widgets.BrowserWidget;


public class CorpsBrowserMini extends BrowserWidget {

    public CorpsBrowserMini(final CorpDTO corps) {
        setStyleName("costInfoMini");
        setSize(199, 166);
        addWidgetAndGo(new CorpsExtraPopup(corps, this));
    }
}
