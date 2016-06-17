package com.eaw1805.www.client.views.layout;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.economy.StoredGoodDTO;
import com.eaw1805.data.dto.web.economy.WarehouseDTO;
import com.eaw1805.www.client.events.trade.GetGoodEvent;
import com.eaw1805.www.client.events.trade.GetGoodHandler;
import com.eaw1805.www.client.events.trade.GiveGoodEvent;
import com.eaw1805.www.client.events.trade.GiveGoodHandler;
import com.eaw1805.www.client.events.trade.TradeEventManager;
import com.eaw1805.www.client.views.extras.AdmAndCommPtsPanel;
import com.eaw1805.www.client.views.extras.PointsExpWidget;
import com.eaw1805.www.client.widgets.CostAnimePopup;
import com.eaw1805.www.client.widgets.ResourceLowWarning;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.economy.WarehouseStore;
import com.eaw1805.www.shared.stores.map.MapStore;

import java.util.Map;
import java.util.Set;

/**
 * Populates the list of available materials (warehouse) positioned at the very top of the screen.
 * Also populates the available Administrative & Command points located at the bottom of the screen.
 */
public class EconomyView
        extends VerticalPanel implements GoodConstants, ArmyConstants {

    /**
     * The names of the goods.
     */
    private static final String[] NAMES =
            {"Money", "Citizens",
                    "Industrial Points", "Food", "Stone", "Wood", "Ore", "Gems",
                    "Horse", "Fabric", "Wool", "Precious Metals", "Wine", "Colonial Goods"};

    /**
     * The positions of the goods on the top bar.
     */
    public static final int[] POSITION = {
            0, 1,
            2, 3, 6, 7, 9, 11,
            8, 4, 5, 10, 12, 13, 14, 15
    };

    public static final int[] WIDTHS = {
            111, 80, 80, 80, 80, 80, 80, 80, 80, 60, 60, 60, 60, 60
    };

    private final Label[] lblGoods = new Label[NAMES.length];
    private final CostAnimePopup[] animatedPopups = new CostAnimePopup[NAMES.length + 2];
    private final ResourceLowWarning[] warningPopups = new ResourceLowWarning[NAMES.length + 2];
    private AdmAndCommPtsPanel admPtsPanel, commPtsPanel;

    /**
     * Map panel that shows the current region's warehouse goods.
     *
     * @param admPtsPanel  the panel that displays the administrative points.
     * @param commPtsPanel the panel that displays the command points.
     */
    public EconomyView(final AdmAndCommPtsPanel admPtsPanel, final AdmAndCommPtsPanel commPtsPanel, final AbsolutePanel container) {
        try {
            setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

            final AbsolutePanel economyPanel = new AbsolutePanel();
            setSize("1199px", "42px");
            this.add(economyPanel);
            this.admPtsPanel = admPtsPanel;
            this.commPtsPanel = commPtsPanel;

            economyPanel.setStyleName("economyPanel");
            economyPanel.setSize("1199px", "42px");

            // Initialize records
            for (int i = 0; i < NAMES.length; i++) {
                lblGoods[i] = new Label("0");

                lblGoods[i].setHorizontalAlignment(ALIGN_LEFT);
                if (i == 0) {
                    economyPanel.add(lblGoods[i], 34, 7);
                    if (container != null) {
                        animatedPopups[i] = new CostAnimePopup(WIDTHS[i]);
                        container.add(animatedPopups[i], 23, 35);

                        warningPopups[i] = new ResourceLowWarning(WIDTHS[i] - 16, 18, false);
                        container.add(warningPopups[i], 29, 7);
                    }
                } else if (i < 10) {
                    economyPanel.add(lblGoods[i], 154 + 91 * (i - 1), 7);
                    if (container != null) {
                        animatedPopups[i] = new CostAnimePopup(WIDTHS[i]);
                        container.add(animatedPopups[i], 143 + 91 * (i - 1), 35);

                        warningPopups[i] = new ResourceLowWarning(WIDTHS[i] - 16, 18, false);
                        container.add(warningPopups[i], 149 + 91 * (i - 1), 7);
                    }
                } else {
                    economyPanel.add(lblGoods[i], 881 + 68 * (i - 9), 7);
                    if (container != null) {
                        animatedPopups[i] = new CostAnimePopup(WIDTHS[i]);
                        container.add(animatedPopups[i], 870 + 68 * (i - 9), 35);

                        warningPopups[i] = new ResourceLowWarning(WIDTHS[i] - 16, 18, false);
                        container.add(warningPopups[i], 876 + 68 * (i - 9), 7);
                    }
                }
            }
            if (container != null) {
                animatedPopups[animatedPopups.length - 2] = new CostAnimePopup(40);
                container.add(animatedPopups[animatedPopups.length - 2], 399, Window.getClientHeight() - 180);

                animatedPopups[animatedPopups.length - 1] = new CostAnimePopup(40);
                container.add(animatedPopups[animatedPopups.length - 1], 454, Window.getClientHeight() - 180);

                warningPopups[warningPopups.length - 2] = new ResourceLowWarning(49, 29, true);
                container.add(warningPopups[warningPopups.length - 2], 394, Window.getClientHeight() - 133);
                new ToolTipPanel(warningPopups[warningPopups.length - 2]) {
                    @Override
                    public void generateTip() {
                        setTooltip(new PointsExpWidget());
                    }
                };

                warningPopups[warningPopups.length - 1] = new ResourceLowWarning(49, 29, true);
                container.add(warningPopups[warningPopups.length - 1], 448, Window.getClientHeight() - 133);
                new ToolTipPanel(warningPopups[warningPopups.length - 1]) {
                    @Override
                    public void generateTip() {
                        setTooltip(new PointsExpWidget());
                    }
                };
            }
            TradeEventManager.addGetGoodHanlder(new GetGoodHandler() {
                public void onGetGoodIn(final GetGoodEvent getGoodEvent) {
                    if (getGoodEvent.getUnitType() == WAREHOUSE) {
                        populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(MapStore.getInstance().getActiveRegion()), false);
                    }
                }
            });

            TradeEventManager.addGiveGoodHanlder(new GiveGoodHandler() {
                public void onGiveGoodIn(final GiveGoodEvent giveGoodEvent) {
                    if (giveGoodEvent.getUnitType() == WAREHOUSE) {
                        populateGoodsLabels(WarehouseStore.getInstance().getWareHouseByRegion(MapStore.getInstance().getActiveRegion()), false);
                    }
                }
            });
        } catch (Exception e) {
//            Window.alert("e w : " + e.toString());
        }
    }

    public void populateGoodsLabels(final WarehouseDTO warehouse, boolean forTrade) {
        //lblDateLabel.setText(ClientUtil.getMonthByTurn(GameStore.getInstance().getTurn()) + " " + ClientUtil.getYearByTurn(GameStore.getInstance().getTurn()));
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        if (warehouse.getRegionId() == MapStore.getInstance().getActiveRegion() || forTrade) {
            for (final StoredGoodDTO thisGood : warehouse.getGoodsDTO().values()) {
                if (thisGood.getTpe() - 1 < 14) {
                    lblGoods[POSITION[thisGood.getTpe() - 1]].setText(numberFormat.format(thisGood.getQte()));
                    if (thisGood.getTpe() == 1) {
                        lblGoods[POSITION[thisGood.getTpe() - 1]].setTitle("Current Treasury level");
                    } else {
                        lblGoods[POSITION[thisGood.getTpe() - 1]].setTitle("Current Regional Warehouse stock of " + thisGood.getGoodDTO().getName());
                    }
                }
            }
        }
        final WarehouseDTO baseWarehouse = WarehouseStore.getInstance().getWareHouseByRegion(RegionConstants.EUROPE);
        final Map<Integer, StoredGoodDTO> baseWhGoods = baseWarehouse.getGoodsDTO();

        lblGoods[0].setText(numberFormat.format(baseWhGoods.get(GOOD_MONEY).getQte()));

        if (admPtsPanel != null && commPtsPanel != null) {
            admPtsPanel.getLblQuantity().setText(numberFormat.format(baseWhGoods.get(GOOD_AP).getQte()));
            commPtsPanel.getLblQuantity().setText(numberFormat.format(baseWhGoods.get(GOOD_CP).getQte()));
        }
    }

    public CostAnimePopup[] getAnimatedPopups() {
        return animatedPopups;
    }

    public ResourceLowWarning[] getWarningPopups() {
        return warningPopups;
    }

    public void highLightGoods(int... goodIds) {
        for (int goodId : goodIds) {
            warningPopups[POSITION[goodId - 1]].showWarning(true);
        }
    }

    public void highLightGoods(final Set<Integer> goodIds) {
        for (int goodId : goodIds) {
            warningPopups[POSITION[goodId - 1]].showWarning(false);
        }
    }

    public void highLightGoods(boolean forever, int... goodIds) {
        for (int goodId : goodIds) {
            warningPopups[POSITION[goodId - 1]].showWarning(forever);
        }
    }

    public void stopHighLightGoods(int... goodIds) {
        for (int goodId : goodIds) {
            warningPopups[POSITION[goodId - 1]].hideWarning();
        }
    }
}
