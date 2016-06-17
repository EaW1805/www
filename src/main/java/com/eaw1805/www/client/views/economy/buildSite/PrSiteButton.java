package com.eaw1805.www.client.views.economy.buildSite;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.www.client.views.popups.OrdersViewerPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.economy.ProductionSiteStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

import java.util.ArrayList;
import java.util.List;

public class PrSiteButton extends AbsolutePanel implements
        ProductionSiteConstants, OrderConstants {

    private String name = "";
    private final Fade f;
    private int countUpDown = 0;
    private static final int lowOpacity = 50;
    private static final int highOpacity = 100;

    public PrSiteButton(final ProductionSiteDTO prodSite,
                        final SectorDTO selSector, final boolean avail) {
        super();
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        setSize("117px", "174px");
        final ImageButton img = new ImageButton();
        this.add(img);
        img.setStyleName("pointer");
        img.setSize("117px", "174px");

        f = new Fade(getElement());
        f.setDuration(1);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                countUpDown++;
                if (countUpDown <= 13) {
                    if (countUpDown % 2 == 1) {
                        f.setStartOpacity(lowOpacity);
                        f.setEndOpacity(highOpacity);
                    } else {
                        f.setStartOpacity(highOpacity);
                        f.setEndOpacity(lowOpacity);
                    }
                    f.play();
                } else {

                }
            }
        });


        if (prodSite.getId() == PS_BARRACKS_FS
                || prodSite.getId() == PS_BARRACKS_FM
                || prodSite.getId() == PS_BARRACKS_FL
                || prodSite.getId() == PS_BARRACKS_FH) {
            switch (selSector.getProductionSiteId()) {

                case PS_BARRACKS:
                    if (prodSite.getId() == PS_BARRACKS_FS) {
                        name = "BarracksFortress";
                    }
                    break;

                case PS_BARRACKS_FS:
                    if (prodSite.getId() == PS_BARRACKS_FM) {
                        name = "BarracksFortress";
                    }
                    break;

                case PS_BARRACKS_FM:
                    if (prodSite.getId() == PS_BARRACKS_FL) {
                        name = "BarracksFortress";
                    }
                    break;

                case PS_BARRACKS_FL:
                    if (prodSite.getId() == PS_BARRACKS_FH) {
                        name = "BarracksFortress";
                    }
                    break;

                default:
                    name = "";
                    break;
            }
        } else {
            name = prodSite.getName().split(" ")[0];
        }

        if (avail) {
            img.setUrl("http://static.eaw1805.com/images/panels/proSites/ButProd" + name + "Off.png");

        } else {
            img.setUrl("http://static.eaw1805.com/images/panels/proSites/ButProd" + name + "Gray.png");
        }

        img.setTitle(prodSite.getDescription());

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-1.png");
        moneyImg.setStyleName("pointer");
        moneyImg.setTitle("Monetary Costs");
        add(moneyImg, 24, 126);
        moneyImg.setSize("16px", "16px");

        final Label moneyLbl = new Label(numberFormat.format(prodSite.getCost()));
        moneyLbl.setStyleName("clearFontMini  pointer");
        add(moneyLbl, 48, 126);
        moneyLbl.setSize("22px", "18px");

        if (!prodSite.getName().equals("Demolition")) {
            final Image peopleImg = new Image("http://static.eaw1805.com/images/goods/good-2.png");
            peopleImg.setStyleName("pointer");
            peopleImg.setTitle("Citizens cost");
            add(peopleImg, 24, 144);
            peopleImg.setSize("16px", "16px");

            final Label peopleLbl;
            switch (prodSite.getId()) {
                case PS_MINE:
                case PS_FACTORY:
                    peopleLbl = new Label(numberFormat.format(4000));
                    break;
                default:
                    peopleLbl = new Label(numberFormat.format(2000));
            }

            peopleLbl.setStyleName("clearFontMini  pointer");
            add(peopleLbl, 48, 144);
            peopleLbl.setSize("22px", "18px");
        }

        final Label lblName = new Label(prodSite.getName());
        lblName.setStyleName("clearFontSmall pointer");
        lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add(lblName, 10, 95);
        lblName.setSize("97px", "15px");
        addDomHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                if (!prodSite.getName().equals("Demolition")) {
                    if (avail) {
                        final int nextTurnPop = RegionStore.getInstance().getSectorNextTurnPopulation(selSector);
                        if (nextTurnPop > (prodSite.getMinPopDensity() - 1)
                                && nextTurnPop < (prodSite.getMaxPopDensity() + 1)) {
                            ProductionSiteStore.getInstance().buildProdSite(selSector, prodSite.getId());
                            GameStore.getInstance().getLayoutView().removeLastWidgetFromPanel();
                            SoundStore.getInstance().playProductionSite();
                        } else {
                            new ErrorPopup(ErrorPopup.Level.WARNING, "You do not meet the population density requirements", false);
                        }
                    }
                } else {

                    final int nationId = GameStore.getInstance().getNationId();
                    if (nationId == selSector.getNationId()) {
                        if (selSector.getProductionSiteId() > 0
                                && !ProductionSiteStore.getInstance().getSectorProdSites().containsKey(selSector)) {
                            if (!selSector.getTradeCity()) {

                                final List<ClientOrderDTO> conflictOrders = new ArrayList<ClientOrderDTO>();
                                if (selSector.hasShipyardOrBarracks()) {
                                    List<ClientOrderDTO> orders = OrderStore.getInstance().getOrdersByTypes2(ORDER_INC_EXP, ORDER_INC_EXP_ARMY, ORDER_INC_EXP_CORPS,
                                            ORDER_INC_HEADCNT, ORDER_INC_HEADCNT_CORPS, ORDER_INC_HEADCNT_ARMY, ORDER_ADD_BATT, ORDER_B_BATT, ORDER_B_SHIP, ORDER_B_BTRAIN);
                                    for (ClientOrderDTO order : orders) {
                                        PositionDTO pos = null;
                                        switch (order.getOrderTypeId()) {
                                            case ORDER_INC_EXP:
                                            case ORDER_INC_HEADCNT:
                                            case ORDER_ADD_BATT:
                                                pos = ArmyStore.getInstance().getBrigadeById(order.getIdentifier(0));
                                                break;
                                            case ORDER_INC_EXP_CORPS:
                                            case ORDER_INC_HEADCNT_CORPS:
                                                pos = ArmyStore.getInstance().getCorpByID(order.getIdentifier(0));
                                                break;
                                            case ORDER_INC_EXP_ARMY:
                                            case ORDER_INC_HEADCNT_ARMY:
                                                pos = ArmyStore.getInstance().getArmyById(order.getIdentifier(0));
                                                break;
                                            case ORDER_B_SHIP:
                                                if (order.getIdentifier(1) == selSector.getId()) {
                                                    conflictOrders.add(order);
                                                }
                                                break;
                                            case ORDER_B_BATT:
                                            case ORDER_B_BTRAIN:
                                                if (order.getIdentifier(0) == selSector.getId()) {
                                                    conflictOrders.add(order);
                                                }
                                                break;
                                            default:
                                                pos = null;
                                                break;
                                        }
                                        if (pos != null) {
                                            if (pos.equalsStart(selSector)) {
                                                conflictOrders.add(order);
                                            }
                                        }
                                    }
                                }
                                if (!conflictOrders.isEmpty()) {
                                    new ErrorPopup(ErrorPopup.Level.WARNING, "This barrack cannot be demolished because there are orders that conflict with this action. Review conflict orders?", true) {
                                        public void onAccept() {
                                            final OrdersViewerPopup viewer = new OrdersViewerPopup(conflictOrders, "Orders that conflict with your action");
                                            viewer.show();
                                            viewer.center();
                                        }
                                    };
                                } else {
                                    ProductionSiteStore.getInstance().demolishProdSite(selSector, true);
                                    GameStore.getInstance().getLayoutView().removeLastWidgetFromPanel();
                                    SoundStore.getInstance().playProductionSite();
                                }
                            } else {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "You cannot demolish a trade city", false);
                            }
                        }
                    }
                }

            }
        }, ClickEvent.getType());
    }

    public void animate() {
        f.play();
    }


    public boolean isEligible() {
        return !(name.equals(""));
    }
}
