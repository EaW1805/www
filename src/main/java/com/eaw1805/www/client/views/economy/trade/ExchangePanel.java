package com.eaw1805.www.client.views.economy.trade;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;

public class ExchangePanel extends AbsolutePanel {

    public ExchangePanel(final int goodId,
                         final TradeUnitAbstractDTO tdUnit1,
                         final TradeUnitAbstractDTO tdUnit2,
                         final int tradePhase) {
        setSize("445px", "448px");
        final TradeBarWidget tbw1 = new TradeBarWidget(goodId, tdUnit1, tdUnit2, tradePhase);
        final TradeBarWidget tbw2 = new TradeBarWidget(goodId, tdUnit2, tdUnit1, tradePhase);
        add(tbw1, 10, 34);
        add(tbw2, 10, 244);
    }
}
