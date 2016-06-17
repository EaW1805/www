package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.widgets.OrderMiniWidget;

import java.util.HashSet;

public class BrigadeFullInfoPanel {


    final BrigadeInfoPanel info;
    final OrderMiniWidget basicOrder;
    final OrderMiniWidget additionalOrder;

    public BrigadeFullInfoPanel(BrigadeDTO brigade) {

        info = new BrigadeInfoPanel(brigade, false, false, false);

        final AbsolutePanel spacer = new AbsolutePanel();
        spacer.setSize("5px", "74px");

        basicOrder = new OrderMiniWidget(brigade, true, new HashSet<BrigadeDTO>());
        additionalOrder = new OrderMiniWidget(brigade, false, new HashSet<BrigadeDTO>());

    }

    public BrigadeInfoPanel getInfo() {
        return info;
    }

    public OrderMiniWidget getBasicOrder() {
        return basicOrder;
    }

    public OrderMiniWidget getAdditionalOrder() {
        return additionalOrder;
    }


}
