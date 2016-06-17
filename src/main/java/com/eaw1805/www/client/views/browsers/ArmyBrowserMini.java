package com.eaw1805.www.client.views.browsers;


import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.views.popups.ArmyExtraPopup;
import com.eaw1805.www.client.widgets.BrowserWidget;

public class ArmyBrowserMini extends BrowserWidget {

    public ArmyBrowserMini(final ArmyDTO army) {
        setStyleName("costInfoMini");
        setSize(199, 166);
        addWidgetAndGo(new ArmyExtraPopup(army, this));
    }
}
