package com.eaw1805.www.client.views.popups;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;

public class OrderCostInfoPopup extends AbsolutePanel
        implements GoodConstants {
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();

    public OrderCostInfoPopup(final ClientOrderDTO order) {
        this.setSize("199px", "166px");
        this.setStyleName("costInfoMini");
        int posX = 3;
        int posY = 3;
        for (int index = 0; index < 14; index++) {
            final Image goodImage = new Image("http://static.eaw1805.com/images/goods/good-" + (index + 1) + ".png");
            goodImage.setSize("20px", "20px");
            final Label goodAmount = new Label(numberFormat.format(order.getCosts().getNumericCost(index + 1)));
            goodAmount.setWidth("50px");
            goodAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            goodAmount.setStyleName("clearFontMini");
            this.add(new HorizontalPanelLabelValue(0, goodImage, 5, goodAmount), posX, posY);
            posY += 23;
            if (index == 6) {
                posX += 83;
                posY = 3;
            }
        }
    }


}
