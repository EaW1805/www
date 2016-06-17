package com.eaw1805.www.client.views.economy.trade;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.infopanels.units.TradeUnitInfoPanel;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.TradeStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.ArrayList;
import java.util.List;

public class StartTradeView
        extends AbsolutePanel
        implements ArmyConstants {

    private final VerticalPanelScrollChild leftTradeContainer;
    private final VerticalPanelScrollChild rightTradeContainer;
    private final Label lblSelectAUnit;
    private final AbsolutePanel firstSelPanel;
    private final AbsolutePanel secondSelPanel;
    private final Label lblFirstTradeUnit;
    private final Label lblSecondTradeUnit;
    private final ImageButton startTradeImg;
    private final int tradePhase, regionId;
    private int type;
    private final TradePanelView parent;
    public StartTradeView(final TradePanelView tdView, final int type, final int typeId, final int regionId, final int tradePhase) {
        parent = tdView;
        this.tradePhase = tradePhase;
        this.type = type;
        this.regionId = regionId;
        setStyleName("startTradeTab");
        setSize("1148px", "560px");

        leftTradeContainer = new VerticalPanelScrollChild();
        final ScrollVerticalBarEAW leftTradeScroll = new ScrollVerticalBarEAW(leftTradeContainer, 114, false);
        leftTradeScroll.setSize(323, 529);
        leftTradeContainer.setSize("100%", "0");
        add(leftTradeScroll, 14, 16);

        rightTradeContainer = new VerticalPanelScrollChild();
        rightTradeContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        rightTradeContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        final ScrollVerticalBarEAW rightTradeScroll = new ScrollVerticalBarEAW(rightTradeContainer, 114, false);
        rightTradeScroll.setSize(323, 529);
        rightTradeContainer.setSize("100%", "0");
        add(rightTradeScroll, 814, 16);

        lblSelectAUnit = new Label("Select a unit from the right panel");
        lblSelectAUnit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblSelectAUnit.setStyleName("clearFont-large");
        lblSelectAUnit.setWidth("153px");
        rightTradeContainer.add(lblSelectAUnit);

        firstSelPanel = new AbsolutePanel();
        firstSelPanel.setStyleName("tradeUnitSelected");
        firstSelPanel.setSize("322px", "126px");
        add(firstSelPanel, 429, 76);

        lblFirstTradeUnit = new Label("First trade unit selection");
        lblFirstTradeUnit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblFirstTradeUnit.setStyleName("clearFontMiniTitle");
        lblFirstTradeUnit.setSize("308px", "25px");
        firstSelPanel.add(this.lblFirstTradeUnit, 7, 50);

        secondSelPanel = new AbsolutePanel();
        secondSelPanel.setStyleName("tradeUnitSelected");
        secondSelPanel.setSize("322px", "126px");
        add(secondSelPanel, 429, 399);

        lblSecondTradeUnit = new Label("Second trade unit selection");
        lblSecondTradeUnit.setStyleName("clearFontMiniTitle");
        lblSecondTradeUnit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblSecondTradeUnit.setSize("308px", "25px");
        secondSelPanel.add(this.lblSecondTradeUnit, 7, 50);

        startTradeImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButTradingCitiesOff.png");
        startTradeImg.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                TradeUnitAbstractDTO tdUnit1 = null, tdUnit2 = null;
                if (firstSelPanel.getWidgetCount() > 0 && firstSelPanel.getWidget(0).getClass() == TradeUnitInfoPanel.class) {
                    tdUnit1 = ((TradeUnitInfoPanel) firstSelPanel.getWidget(0)).getTdUnit();
                }

                if (secondSelPanel.getWidgetCount() > 0 && secondSelPanel.getWidget(0).getClass() == TradeUnitInfoPanel.class) {
                    tdUnit2 = ((TradeUnitInfoPanel) secondSelPanel.getWidget(0)).getTdUnit();
                }

                if (tdUnit1 != null && tdUnit2 != null) {
                    tdView.startTrading(tdUnit1, tdUnit2, StartTradeView.this.tradePhase);
                }

                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 13) {
                    TutorialStore.nextStep(false);
                }
            }
        });
        startTradeImg.setStyleName("pointer");
        startTradeImg.setSize("160px", "45px");
        add(startTradeImg, 510, 278);

        setupContainerPanel(type, typeId, regionId, tradePhase, 1, 0, 0, true);
    }

    public void selectType(final int type, final boolean allNations) {
        this.type = type;
        leftTradeContainer.clear();
        setupContainerPanel(type, 0, regionId, tradePhase, 1, 0, 0, allNations);

        firstSelPanel.clear();
        firstSelPanel.add(lblFirstTradeUnit, 7, 50);

        secondSelPanel.clear();
        secondSelPanel.add(lblSecondTradeUnit, 7, 50);

        rightTradeContainer.clear();
        rightTradeContainer.add(lblSelectAUnit);

    }

    private void setupContainerPanel(final int type, final int typeId, final int region,
                                     final int tradePhase, final int container, final int x, final int y, final boolean allNations) {
        final VerticalPanelScrollChild cont;
        final List<TradeUnitAbstractDTO> tradeUnits = new ArrayList<TradeUnitAbstractDTO>();
        if (container == 1) {
            cont = leftTradeContainer;
            tradeUnits.addAll(TradeStore.getInstance().getTradeUnitsByRegionTypeAndPhase(type, region, true));
            tradeUnits.addAll(AlliedUnitsStore.getInstance().getTradeUnitsByRegionTypeAndPhase(type, region));

        } else {
            cont = rightTradeContainer;
            tradeUnits.addAll(TradeStore.getInstance().getTradeUnitsByRegionTypePhasePos(type, typeId, region, tradePhase, x, y, true));
            if (allNations) {
                tradeUnits.addAll(AlliedUnitsStore.getInstance().getTradeUnitsByRegionTypePhasePos(type, region, x, y, tradePhase));
            }
        }
        cont.clear();

        for (final TradeUnitAbstractDTO tdUnitNfo : tradeUnits) {
            if (tdUnitNfo.getUnitType() != SHIP || NavyStore.getInstance().isTradeShip((ShipDTO) tdUnitNfo)) {
                final TradeUnitInfoPanel tdPanel = new TradeUnitInfoPanel(tdUnitNfo, tradePhase);
                if (tdUnitNfo.getId() == typeId) {
                    tdPanel.select();
                    selectTdUnit(container, tdUnitNfo);
                }

                tdPanel.getBasePanel().addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        for (int i = 0; i < cont.getWidgetCount(); i++) {
                            ((TradeUnitInfoPanel) cont.getWidget(i)).deSelect();
                        }
                        tdPanel.select();
                        selectTdUnit(container, tdUnitNfo);

                        if (tdUnitNfo.getUnitType() == TRADECITY
                                && "Paris".equalsIgnoreCase(tdUnitNfo.getName())) {
                            if (TutorialStore.getInstance().isTutorialMode()
                                    && TutorialStore.getInstance().getMonth() == 10
                                    && TutorialStore.getInstance().getTutorialStep() == 12) {
                                TutorialStore.nextStep(false);
                            }
                        }
                        if (tdUnitNfo.getUnitType() == WAREHOUSE
                                && "European Warehouse".equalsIgnoreCase(tdUnitNfo.getName())) {
                            if (TutorialStore.getInstance().isTutorialMode()
                                    && TutorialStore.getInstance().getMonth() == 10
                                    && TutorialStore.getInstance().getTutorialStep() == 13) {
                                TutorialStore.highLightButton(startTradeImg);
                            }
                        }

                    }
                });
                cont.add(tdPanel);
                if (tdUnitNfo.getUnitType() == TRADECITY
                        && "Paris".equalsIgnoreCase(tdUnitNfo.getName())) {
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 10
                            && TutorialStore.getInstance().getTutorialStep() == 12) {
                        TutorialStore.highLightButton(tdPanel);
                    }
                }
                if (tdUnitNfo.getUnitType() == WAREHOUSE
                        && "European Warehouse".equalsIgnoreCase(tdUnitNfo.getName())) {
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 10
                            && TutorialStore.getInstance().getTutorialStep() == 13) {
                        TutorialStore.highLightButton(tdPanel);
                    }
                }
            }
        }
        cont.resizeScrollBar();
    }

    private void selectTdUnit(final int container, final TradeUnitAbstractDTO tdUnitNfo) {
        final AbsolutePanel cont;
        if (container == 1) {
            cont = firstSelPanel;
            secondSelPanel.clear();
            secondSelPanel.add(lblSecondTradeUnit, 7, 50);

        } else {
            cont = secondSelPanel;
        }
        cont.clear();

        final TradeUnitInfoPanel tdPanel = new TradeUnitInfoPanel(tdUnitNfo, tradePhase);
        tdPanel.select();
        cont.add(tdPanel, 8, 7);

        if (container == 1) {
            if (tradePhase == 1) {
                setupContainerPanel(type, tdUnitNfo.getId(), tdUnitNfo.getRegionId(), tradePhase, 2, tdUnitNfo.getXStart(), tdUnitNfo.getYStart(), tdUnitNfo.getNationId() == GameStore.getInstance().getNationId());

            } else {
                setupContainerPanel(type, tdUnitNfo.getId(), tdUnitNfo.getRegionId(), tradePhase, 2, tdUnitNfo.getX(), tdUnitNfo.getY(), tdUnitNfo.getNationId() == GameStore.getInstance().getNationId());
            }
            parent.rememberSelectedUnit(tdUnitNfo);
        }
    }
}
