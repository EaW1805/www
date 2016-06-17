package com.eaw1805.www.client.views.infopanels.foreignUnits;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.HorizontalPanelLabelValue;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;

/**
 * Info panel that shows information about a baggage train.
 */
public class ForeignBaggageTrainInfoPanel
        extends AbstractInfoPanel {

    public ForeignBaggageTrainInfoPanel(final BaggageTrainDTO baggageTrain) {
        this.setStyleName("baggageInfoPanel");
        this.setSize("366px", "90px");

        final Image image = new Image("http://static.eaw1805.com/images/figures/baggage.png");
        this.add(image, 3, 3);
        image.setSize("", "82px");

        final AbsolutePanel goodsPanel = new AbsolutePanel();
        goodsPanel.setSize("150px", "35px");
        add(goodsPanel, 170, 30);

        final Label lblBaggageTrainName = new Label("Baggage Train");
        lblBaggageTrainName.setStyleName("clearFontMiniTitle");
        lblBaggageTrainName.setSize("249px", "21px");

        final NumberFormat numberFormat = NumberFormat.getDecimalFormat();
        final Label lblAvailable = new Label(numberFormat.format((TradeStore.getInstance().getTradeUnitWeight(baggageTrain) - TradeStore.getInstance().getTradeUnitLoad(baggageTrain))) + " tons");
        lblAvailable.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        lblAvailable.setStyleName("clearFontSmall");
        add(lblAvailable, 90, 20);

        final Label lblLocation = new Label(baggageTrain.positionToString());
        lblLocation.setStyleName("clearFontSmall");
        lblLocation.setTitle("Baggage trains position.");
        lblLocation.setSize("47px", "15px");
        this.add(lblLocation, 315, 3);

        final HorizontalPanel relationPanel = new HorizontalPanel();
        relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        relationPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);

        final VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + baggageTrain.getNationId() + "-120.png");
        flag.setSize("", "82px");

        final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(baggageTrain.getNationId()));
        relationStatus.setStyleName("clearFont");
        infoPanel.add(new HorizontalPanelLabelValue(3, lblBaggageTrainName));
        infoPanel.add(new HorizontalPanelLabelValue(3, relationStatus));
        relationPanel.add(flag);
        relationPanel.add(infoPanel);
        add(relationPanel, 90, 3);
    }

}
