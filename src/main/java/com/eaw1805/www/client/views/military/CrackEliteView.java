package com.eaw1805.www.client.views.military;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ScrollVerticalBarEAW;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.client.widgets.SelectionListPanel;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CrackEliteView
        extends DraggablePanel
        implements ArmyConstants, GoodConstants, OrderConstants, StyleConstants {

    // save all your troops of the coordinate in this list
    private final List<ArmyDTO> sectorArmies;
    private final List<CorpDTO> armyCorps = new ArrayList<CorpDTO>();
    private final List<BrigadeDTO> corpBrigades = new ArrayList<BrigadeDTO>();

    private final List<ArmyDTO> armiesInNeed = new ArrayList<ArmyDTO>();
    private final List<CorpDTO> corpsInNeed = new ArrayList<CorpDTO>();
    private final Map<Integer, CorpDTO> corpsInNeedMap = new HashMap<Integer, CorpDTO>();
    private final Map<Integer, BrigadeDTO> brigadesInNeedMap = new HashMap<Integer, BrigadeDTO>();

    // init an instance of army manager to use in the widget
    private final ArmyStore arStore = ArmyStore.getInstance();
    private final SectorDTO sectorDTO;
    private BrigadeDTO selBrigade = new BrigadeDTO();
    private final ImageButton typelftArmImg;
    private final ImageButton typerghArmImg;

    // Objects to keep our selections
    private ArmyDTO selArmy;
    private CorpDTO selCorp;

    private final ImageButton typelftCorpImg;
    private final ImageButton typerghCorpImg;
    private final AbsolutePanel brigadePanel;
    private final AbsolutePanel corpPanel;
    private final AbsolutePanel armyPanel;
    private final ImageButton typelftBrigImg;
    private final ImageButton typerghBrigImg;

    //for brigades
    private final AbsolutePanel brigadeCostPanel;
    private final Label lblMoneyValueBrigade;
    private final Label lblIndValueBrigade;
    private final Image expUpElBrigadeImg;


    //for corps
    private final AbsolutePanel corpsCostPanel;
    private final Label lblMoneyValueCorps;
    private final Label lblIndValueCorps;
    private final Image expUpElCorpsImg;

    //for armies
    private final AbsolutePanel armyCostPanel;
    private final Label lblMoneyValueArmy;
    private final Label lblIndValueArmy;
    private final Image expUpElArmyImg;

    private final StyledCheckBox onlyUpgradable;
    private ImageButton leftImg;
    private ImageButton rightImg;
    private final VerticalPanelScrollChild trainedBrigadesPanel;
    final static int offsetLeft = -6;
    final static int offsetTop = -6;
    private final int TOT_VPS = 100 * GameStore.getInstance().getVps() / GameStore.getInstance().getVpsMax();

    public CrackEliteView(final SectorDTO thisSector, final BarrackDTO barrShip) {
        // Set selected sector
        sectorDTO = thisSector;

        // Initialize sector Armies
        sectorArmies = arStore.getArmiesBySector(sectorDTO, false);

        initArmiesInNeed();

        setStyleName("barracksPanel3");
        setSize("630px", "704px");

        initLoopImages(barrShip);

        final Label trainedLbl = new Label("Upgraded brigades:");
        trainedLbl.setStyleName("clearFont whiteText");
        add(trainedLbl, 50 + offsetLeft, 410 + offsetTop);

        this.trainedBrigadesPanel = new VerticalPanelScrollChild();

        final Label titleLbl = new Label(barrShip.positionToString() + " - Upgrade your troops");
        titleLbl.setStyleName("clearFontMedLarge");
        titleLbl.setStyleName("whiteText", true);
        add(titleLbl, 60, 20 + offsetTop);

        final CrackEliteView myself = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName(CLASS_POINTER);
        imgX.setTitle("Close panel");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();
        imgX.setSize(SIZE_35PX, SIZE_35PX);
        add(imgX, 570 + offsetLeft, 15 + offsetTop);

        final ScrollVerticalBarEAW trainedBrigsScrollPanel = new ScrollVerticalBarEAW(this.trainedBrigadesPanel, 90, false);
        trainedBrigsScrollPanel.setBarAlwaysVisible(true);
        trainedBrigsScrollPanel.setSize(394, 187);
        trainedBrigadesPanel.setSize("100%", "0");
        trainedBrigadesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add(trainedBrigsScrollPanel, 40 + offsetLeft, 435 + offsetTop);


        typelftArmImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        typelftArmImg.setSize(SIZE_16PX, SIZE_91PX);
        typelftArmImg.setStyleName(CLASS_POINTER, true);
        add(typelftArmImg, 25 + offsetLeft, 71 + offsetTop);

        armyPanel = new AbsolutePanel();
        armyPanel.setSize(SIZE_366PX, SIZE_90PX);
        add(armyPanel, 45 + offsetLeft, 71 + offsetTop);

        typerghArmImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        typerghArmImg.setSize(SIZE_16PX, SIZE_91PX);
        typerghArmImg.setStyleName(CLASS_POINTER, true);
        add(this.typerghArmImg, 415 + offsetLeft, 71 + offsetTop);


        typelftCorpImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        typelftCorpImg.setStyleName(CLASS_POINTER, true);
        typelftCorpImg.setSize(SIZE_16PX, SIZE_91PX);
        add(this.typelftCorpImg, 25 + offsetLeft, 166 + offsetTop);

        corpPanel = new AbsolutePanel();
        corpPanel.setSize(SIZE_366PX, SIZE_90PX);
        add(this.corpPanel, 45 + offsetLeft, 166 + offsetTop);

        typerghCorpImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        typerghCorpImg.setStyleName(CLASS_POINTER, true);
        typerghCorpImg.setSize(SIZE_16PX, SIZE_91PX);
        add(this.typerghCorpImg, 415 + offsetLeft, 166 + offsetTop);


        typelftBrigImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        typelftBrigImg.setStyleName(CLASS_POINTER, true);
        typelftBrigImg.setSize(SIZE_16PX, SIZE_91PX);
        add(this.typelftBrigImg, 25 + offsetLeft, 265 + offsetTop);

        brigadePanel = new AbsolutePanel();
        brigadePanel.setSize(SIZE_366PX, SIZE_90PX);
        add(this.brigadePanel, 45 + offsetLeft, 265 + offsetTop);

        this.typerghBrigImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.typerghBrigImg.setStyleName(CLASS_POINTER, true);
        this.add(this.typerghBrigImg, 415 + offsetLeft, 265 + offsetTop);
        this.typerghBrigImg.setSize(SIZE_16PX, SIZE_91PX);

        armyCostPanel = new AbsolutePanel();
        lblMoneyValueArmy = new Label(VALUE_ZERO_FLOAT);
        lblIndValueArmy = new Label(VALUE_ZERO_FLOAT);
        expUpElArmyImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButHorTextUpgradeArmyOff-Gray.png");
        try {

            setupStatusPanel(armyCostPanel,
                    lblMoneyValueArmy, lblIndValueArmy,
                    expUpElArmyImg, ARMY);

            add(armyCostPanel, 430 + offsetLeft, 68 + offsetTop);
        } catch (Exception e) {
//            Window.alert("1 \n" + e.toString());
        }

        corpsCostPanel = new AbsolutePanel();
        lblMoneyValueCorps = new Label(VALUE_ZERO_FLOAT);
        lblIndValueCorps = new Label(VALUE_ZERO_FLOAT);
        expUpElCorpsImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButHorTextUpgradeCorpsOff-Gray.png");
        try {
            setupStatusPanel(corpsCostPanel,
                    lblMoneyValueCorps, lblIndValueCorps,
                    expUpElCorpsImg, CORPS);
            add(corpsCostPanel, 430 + offsetLeft, 163 + offsetTop);
        } catch (Exception e) {
//            Window.alert("2 \n" + e.toString());
        }

        brigadeCostPanel = new AbsolutePanel();
        lblMoneyValueBrigade = new Label(VALUE_ZERO_FLOAT);
        lblIndValueBrigade = new Label(VALUE_ZERO_FLOAT);
        expUpElBrigadeImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButUpgradeTroopsOff-Gray.png");
        try {
            setupStatusPanel(brigadeCostPanel,
                    lblMoneyValueBrigade, lblIndValueBrigade,
                    expUpElBrigadeImg, BRIGADE);
            add(brigadeCostPanel, 430 + offsetLeft, 260 + offsetTop);
        } catch (Exception e) {
//            Window.alert("3 \n" + e.toString());
        }


        onlyUpgradable = new StyledCheckBox("Show only brigades that need upgrade", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (onlyUpgradable.isChecked()) {
                    sectorArmies.clear();
                    sectorArmies.addAll(armiesInNeed);
                } else {
                    sectorArmies.clear();
                    sectorArmies.addAll(arStore.getArmiesBySector(sectorDTO, false));
                }
                changeArmy(true);
                changeCorp(true, true);
                changeBrigade(true);
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
                updateCostValues();
            }
        }).addToElement(onlyUpgradable.getCheckBox().getElement()).register();

        onlyUpgradable.setSize("350px", "21px");
        add(onlyUpgradable, 40 + offsetLeft, 366 + offsetTop);

        initArmySelectionBar();
        changeArmy(true);
        changeCorp(true, true);
        changeBrigade(true);
        armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
        corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
        brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);

        initEliteBrigadesList();
        updateCostValues();
    }

    public final void initLoopImages(final BarrackDTO barrShip) {
        this.leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        this.leftImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrShip);
                final int firstIndex = index;

                BarrackDTO nextBarrack;
                SectorDTO barrackSector;
                while (true) {
                    if (index > 0) {
                        index--;
                    } else {
                        index = BarrackStore.getInstance().getBarracksList().size() - 1;
                    }

                    //if you came back in the same index again...
                    //then there is no other barrack that has armies.
                    if (index == firstIndex) {
                        return;
                    }

                    nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                    barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];

                    //get armies on this sector...
                    final List<ArmyDTO> armiesOnSector = ArmyStore.getInstance().getArmiesBySector(barrackSector, false);

                    //if this barrack has armies.. break and load the barrack
                    if ((armiesOnSector != null && !armiesOnSector.isEmpty())) {
                        break;
                    }
                }

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                //SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                final CrackEliteView crackEliteView = new CrackEliteView(barrackSector, nextBarrack);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(crackEliteView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(crackEliteView, CrackEliteView.this.getAbsoluteLeft(), CrackEliteView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(CrackEliteView.this);

                leftImg.deselect();
                leftImg.setUrl(leftImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(leftImg.getElement()).register();
        add(this.leftImg, 15, 9);

        leftImg.setSize(SIZE_35PX, SIZE_35PX);
        rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        rightImg.setStyleName(CLASS_POINTER);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrShip);
                final int firstIndex = index;

                BarrackDTO nextBarrack;
                SectorDTO barrackSector;
                while (true) {
                    if (index < BarrackStore.getInstance().getBarracksList().size() - 1) {
                        index++;
                    } else {
                        index = 0;
                    }

                    //if you came back in the same index again...
                    //then there is no other barrack that has armies.
                    if (index == firstIndex) {
                        return;
                    }

                    nextBarrack = BarrackStore.getInstance().getBarracksList().get(index);
                    barrackSector = RegionStore.getInstance().getRegionSectorsByRegionId(nextBarrack.getRegionId())[nextBarrack.getX()][nextBarrack.getY()];

                    //get armies on this sector...
                    final List<ArmyDTO> armiesOnSector = ArmyStore.getInstance().getArmiesBySector(barrackSector, false);

                    //if this barrack has armies.. break and load the barrack
                    if ((armiesOnSector != null && !armiesOnSector.isEmpty())) {
                        break;
                    }
                }

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                final CrackEliteView crackEliteView = new CrackEliteView(barrackSector, nextBarrack);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(crackEliteView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(crackEliteView, CrackEliteView.this.getAbsoluteLeft(), CrackEliteView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(CrackEliteView.this);

                rightImg.deselect();
                rightImg.setUrl(rightImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(rightImg.getElement()).register();
        rightImg.setSize(SIZE_35PX, SIZE_35PX);
        add(this.rightImg, 510, 9);
    }


    public final void initArmiesInNeed() {
        ArmyDTO zeroArmy = null;
        CorpDTO zeroCorp = null;
        for (ArmyDTO army : arStore.getArmiesBySector(sectorDTO, false)) {
            if (army.getArmyId() == 0) {
                zeroArmy = army;
            }
            boolean addArmy = false;
            for (CorpDTO corp : army.getCorps().values()) {
                if (corp.getCorpId() == 0) {
                    zeroCorp = corp;
                }
                boolean addCorps = false;
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    boolean addBrigade = false;
                    if (!brigade.getLoaded()
                            && ArmyStore.getInstance().canBrigadeUpgrade(brigade, true)) {
                        addBrigade = true;
                        addCorps = true;
                        addArmy = true;
                    }
                    if (addBrigade) {
                        brigadesInNeedMap.put(brigade.getBrigadeId(), brigade);
                    }
                }

                if (addCorps) {
                    corpsInNeed.add(corp);
                    corpsInNeedMap.put(corp.getCorpId(), corp);
                }
            }

            if (addArmy) {
                armiesInNeed.add(army);
            }
        }

        if (armiesInNeed.isEmpty() && zeroArmy != null) {
            armiesInNeed.add(zeroArmy);
        }

        if (corpsInNeed.isEmpty() && zeroCorp != null) {
            corpsInNeed.add(zeroCorp);
            corpsInNeedMap.put(zeroCorp.getCorpId(), zeroCorp);
        }

        //at last be sure there is at least one corp
        // and at least one army so you have something to display
        if (armiesInNeed.isEmpty()) {
            final ArmyDTO lstArmy = arStore.getArmiesBySector(sectorDTO, false).get(0);
            armiesInNeed.add(lstArmy);
            if (corpsInNeed.isEmpty()) {
                CorpDTO lstCorps = null;
                for (CorpDTO corp : lstArmy.getCorps().values()) {
                    lstCorps = corp;
                    break;
                }
                corpsInNeed.add(lstCorps);
                corpsInNeedMap.put(lstCorps.getCorpId(), lstCorps);
            }
        } else if (corpsInNeed.isEmpty()) {
            CorpDTO lstCorps = null;
            for (CorpDTO corp : armiesInNeed.get(0).getCorps().values()) {
                lstCorps = corp;
                break;
            }
            corpsInNeed.add(lstCorps);
            corpsInNeedMap.put(lstCorps.getCorpId(), lstCorps);
        }
    }


    private void initEliteBrigadesList() {
        trainedBrigadesPanel.clear();
        //lets do this better by orders....
        //first retrieve the army train orders
        final List<ClientOrderDTO> armyTrainOrders = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP_ARMY);

        //then the corps train orders
        final List<ClientOrderDTO> corpsTrainOrders = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP_CORPS);

        //and at last the brigade train orders
        final List<ClientOrderDTO> brigadesTrainOrders = OrderStore.getInstance().getClientOrders().get(ORDER_INC_EXP);

        final List<SelectableWidget> trainedUnits = new ArrayList<SelectableWidget>();
        if (armyTrainOrders != null) {
            for (ClientOrderDTO order : armyTrainOrders) {
                if (order.getIdentifier(1) == 1) {
                    trainedUnits.add(new ArmyInfoPanel(ArmyStore.getInstance().getArmyById(order.getIdentifier(0))));
                }
            }
        }

        if (corpsTrainOrders != null) {
            for (ClientOrderDTO order : corpsTrainOrders) {
                if (order.getIdentifier(1) == 1) {
                    trainedUnits.add(new CorpsInfoPanel(ArmyStore.getInstance().getCorpByID(order.getIdentifier(0)), false));
                }
            }
        }

        if (brigadesTrainOrders != null) {
            for (ClientOrderDTO order : brigadesTrainOrders) {
                if (order.getIdentifier(1) == 1) {
                    trainedUnits.add(new BrigadeInfoPanel(ArmyStore.getInstance().getBrigadeById(order.getIdentifier(0)), false));
                }
            }
        }

        final SelectionListPanel selectionList;
        if (!trainedUnits.isEmpty()) {
            selectionList = new SelectionListPanel(trainedUnits);
            trainedBrigadesPanel.add(selectionList);

        } else {
            selectionList = null;
        }

        final ImageButton btnCancelSelectedBattalion = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButDeleteOrderOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (selectionList != null) {
                    final SelectableWidget selected = selectionList.getSelectedWidget();
                    if (selected != null) {
                        switch (selected.getIdentifier()) {
                            case ARMY:
                                degradeArmy((ArmyDTO) selected.getValue());
                                break;

                            case CORPS:
                                degradeCorps((CorpDTO) selected.getValue());
                                break;

                            case BRIGADE:
                                degradeBrigade((BrigadeDTO) selected.getValue());
                                break;
                            default:
                                //nothing goes here really
                        }
                    }
                } else {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "No brigade unit", false);
                }
                initEliteBrigadesList();
                updateCostValues();
            }
        }).addToElement(btnCancelSelectedBattalion.getElement()).register();

        add(btnCancelSelectedBattalion, 76 + offsetLeft, 637 + offsetTop);
        btnCancelSelectedBattalion.setSize("272px", "26px");
        trainedBrigadesPanel.resizeScrollBar();
    }

    public final void setupStatusPanel(final AbsolutePanel statusPanel,
                                       final Label lblMoneyValue,
                                       final Label lblIndValue,
                                       final Image expUpElImg,
                                       final int type) {
        statusPanel.setSize("173px", "95px");

        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + ".png");
        moneyImg.setSize(SIZE_20PX, SIZE_20PX);
        statusPanel.add(moneyImg, 38, 9);

        final Image indPntsImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + ".png");
        indPntsImg.setSize(SIZE_20PX, SIZE_20PX);
        statusPanel.add(indPntsImg, 38, 39);

        lblMoneyValue.setStyleName("clearFontSmall");
        lblMoneyValue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        statusPanel.add(lblMoneyValue, 66, 11);
        lblMoneyValue.setSize("79px", "18px");

        lblIndValue.setStyleName("clearFontSmall");
        statusPanel.add(lblIndValue, 66, 42);
        lblIndValue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblIndValue.setSize("79px", "18px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                try {
                    if (expUpElImg.getUrl().endsWith("Gray.png")) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "No battalion eligible to become crack elite", false);
                    } else {
                        switch (type) {
                            case BRIGADE:
                                upgradeBrigade();
                                break;

                            case CORPS:
                                upgradeCorps();
                                break;

                            case ARMY:
                                upgradeArmy();
                                break;
                            default:
                                //do nothing here.
                        }

                    }

                } catch (Exception ex) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "You need to select a brigade first", false);
                }
                initEliteBrigadesList();
                updateCostValues();
            }
        }).addToElement(expUpElImg.getElement()).register();

        expUpElImg.setTitle("Upgrade brigades battalions.");
        statusPanel.add(expUpElImg, 0, 69);
        expUpElImg.setSize("150px", "");
    }

    private void initArmySelectionBar() {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typelftArmImg.deselect();
                changeArmy(false);
                updateCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typelftArmImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typerghArmImg.deselect();
                changeArmy(true);
                updateCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typerghArmImg.getElement()).register();
        initCorpSelectionBar();
    }

    private void changeArmy(final boolean forward) {
        int index = 0;
        if (sectorArmies != null && !sectorArmies.isEmpty()) {
            if (selArmy != null) {
                for (int i = 0; i < sectorArmies.size(); i++) {
                    if (sectorArmies.get(i).getArmyId() == selArmy.getArmyId()) {
                        if (forward) {
                            if (i == (sectorArmies.size() - 1)) {
                                index = 0;
                                break;
                            } else {
                                index = i + 1;
                                break;
                            }

                        } else {
                            if (i == 0) {
                                index = sectorArmies.size() - 1;
                                break;
                            } else {
                                index = i - 1;
                                break;
                            }
                        }
                    }
                }
            }

            // Select the proper army
            armyPanel.clear();
            armyPanel.add(new ArmyInfoPanel(sectorArmies.get(index)));
            setSelArmy(sectorArmies.get(index));

            getArmyCorps().clear();
            if (onlyUpgradable.isChecked()) {
                for (CorpDTO corp : getSelArmy().getCorps().values()) {
                    if (corpsInNeedMap.containsKey(corp.getCorpId())) {
                        getArmyCorps().add(corp);
                    }
                }

            } else {
                getArmyCorps().addAll(getSelArmy().getCorps().values());
            }

            if (sectorArmies.size() > 1) {
                changeCorp(true, true);
            }
        }

        setArmyImages(getSelArmy());
    }

    private void initCorpSelectionBar() {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typelftCorpImg.deselect();
                changeCorp(false, false);
                updateCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typelftCorpImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typerghCorpImg.deselect();
                changeCorp(true, false);
                updateCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typerghCorpImg.getElement()).register();


        initBrigadeSelectionBar();
    }

    private void changeCorp(final boolean forward, final boolean armyChanged) {
        int index = 0;
        if (getArmyCorps() != null && !getArmyCorps().isEmpty()) {
            if (selCorp != null) {
                for (int i = 0; i < getArmyCorps().size(); i++) {
                    if (getArmyCorps().get(i).getCorpId() == getSelCorp().getCorpId()) {
                        if (forward) {
                            if (i == (getArmyCorps().size() - 1)) {
                                index = 0;
                                break;
                            } else {
                                index = i + 1;
                                break;
                            }
                        } else {
                            if (i == 0) {
                                index = getArmyCorps().size() - 1;
                                break;
                            } else {
                                index = i - 1;
                                break;
                            }
                        }
                    }
                }
            }

            // Select the proper army
            this.corpPanel.clear();
            this.corpPanel.add(new CorpsInfoPanel(getArmyCorps().get(index), false));

            setSelCorp(getArmyCorps().get(index));
            getCorpBrigades().clear();
            if (onlyUpgradable.isChecked()) {
                for (BrigadeDTO brigade : getSelCorp().getBrigades().values()) {
                    if (brigadesInNeedMap.containsKey(brigade.getBrigadeId())) {
                        getCorpBrigades().add(brigade);
                    }
                }

            } else {
                getCorpBrigades().addAll(getSelCorp().getBrigades().values());
            }

            if (getArmyCorps() != null && (getArmyCorps().size() > 1 || armyChanged)) {
                changeBrigade(true);
            }
        }

        setCorpsImages(getSelCorp());
    }

    private void initBrigadeSelectionBar() {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typelftBrigImg.deselect();
                changeBrigade(false);
                updateCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typelftBrigImg.getElement()).register();

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typerghBrigImg.deselect();
                changeBrigade(true);
                updateCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typerghBrigImg.getElement()).register();

    }

    private void changeBrigade(final boolean forward) {
        int index = 0;
        if (getCorpBrigades() != null && !getCorpBrigades().isEmpty()) {
            if (selBrigade != null) {
                for (int i = 0; i < getCorpBrigades().size(); i++) {

                    if (getCorpBrigades().get(i).getBrigadeId() == getSelBrigade().getBrigadeId()) {
                        if (forward) {
                            if (i == (getCorpBrigades().size() - 1)) {
                                index = 0;
                                break;
                            } else {
                                index = i + 1;
                                break;
                            }

                        } else {
                            if (i == 0) {
                                index = getCorpBrigades().size() - 1;
                                break;
                            } else {
                                index = i - 1;
                                break;
                            }
                        }
                    }
                }
            }

            // Select the proper army
            brigadePanel.clear();
            setSelBrigade(getCorpBrigades().get(index));
            brigadePanel.add(new BrigadeInfoPanel(getSelBrigade(), false));

        } else {
            brigadePanel.clear();
            final AbsolutePanel noBrigFoundPanel = new AbsolutePanel();
            noBrigFoundPanel.setStyleName("freeCorpListNT");
            this.brigadePanel.add(noBrigFoundPanel, 0, 0);
            noBrigFoundPanel.setSize(SIZE_366PX, SIZE_90PX);

            final Label lblNoBrigadeFond = new Label("No Brigade found in this corps");
            lblNoBrigadeFond.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            lblNoBrigadeFond.setStyleName("clearFont-large");
            noBrigFoundPanel.add(lblNoBrigadeFond, 3, 15);
            brigadePanel.add(noBrigFoundPanel);

        }
        setBrigadeImages(getSelBrigade());

    }

    private void updateCostValues() {
        final OrderCostDTO brigadeCost = CostCalculators.getCrackEliteBrigadeCost(getSelBrigade());
        final OrderCostDTO corpsCost = CostCalculators.getCrackEliteCorpsCost(getSelCorp());
        final OrderCostDTO armyCost = CostCalculators.getCrackEliteArmyCost(getSelArmy());

        lblMoneyValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_MONEY)));
        lblIndValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_INPT)));

        lblMoneyValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_MONEY)));
        lblIndValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_INPT)));

        lblMoneyValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_MONEY)));
        lblIndValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_INPT)));
    }

    private void upgradeArmy() {
        if (ArmyStore.getInstance().upgradeArmy(getSelArmy().getArmyId(), true, sectorDTO)) {
            for (CorpDTO corp : getSelArmy().getCorps().values()) {
                setSelCorp(corp);
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    setSelBrigade(brigade);
                    removeSelectedBrigadeInNeed();
                }
            }
        }
    }

    private void degradeArmy(final ArmyDTO army) {
        ArmyStore.getInstance().cancelUpgradeArmy(army.getArmyId(), true, army.getRegionId(), false);
        for (CorpDTO corp : army.getCorps().values()) {
            setSelCorp(corp);
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                setSelBrigade(brigade);
                putSelectedBrigadeInNeed(brigade);
            }
        }
    }

    private void upgradeCorps() {
        if (ArmyStore.getInstance().upgradeCorps(getSelCorp().getCorpId(), true, sectorDTO)) {
            for (BrigadeDTO brigade : getSelCorp().getBrigades().values()) {
                setSelBrigade(brigade);
                removeSelectedBrigadeInNeed();
            }
        }
    }

    private void degradeCorps(final CorpDTO corp) {
        ArmyStore.getInstance().cancelUpgradeCorps(corp.getCorpId(), true, corp.getRegionId(), false);
        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            setSelBrigade(brigade);
            putSelectedBrigadeInNeed(brigade);
        }
    }

    private void upgradeBrigade() {
        if (ArmyStore.getInstance().canBrigadeUpgrade(getSelBrigade(), true)) {
            if (ArmyStore.getInstance().upgradeBrigade(getSelBrigade().getBrigadeId(), true, sectorDTO)) {
                removeSelectedBrigadeInNeed();
            }
        }
    }

    private void putSelectedBrigadeInNeed(final BrigadeDTO brigadeDTO) {
        if (ArmyStore.getInstance().canBrigadeUpgrade(brigadeDTO, true)) {
            brigadesInNeedMap.put(brigadeDTO.getBrigadeId(), brigadeDTO);
            if (getSelCorp().getCorpId() == brigadeDTO.getCorpId()) {

                //check if it is already in because maybe we didn't remove it
                boolean found = false;
                for (BrigadeDTO brigade : getCorpBrigades()) {
                    if (brigadeDTO.getBrigadeId() == brigade.getBrigadeId()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    getCorpBrigades().add(brigadeDTO);
                }
                if (getCorpBrigades().size() == 1) {
                    changeBrigade(true);
                }
            }
            setBrigadeImages(getSelBrigade());
            setCorpsImages(getSelCorp());
            setArmyImages(getSelArmy());
        }
    }

    private void removeSelectedBrigadeInNeed() {
        if (getSelBrigade().isUpgradedToElite()) {
            //remove from brigades in need...
            brigadesInNeedMap.remove(getSelBrigade().getBrigadeId());

            if (onlyUpgradable.isChecked()) {
                final Iterator<BrigadeDTO> brigIter2 = getCorpBrigades().iterator();
                while (brigIter2.hasNext()) {
                    final BrigadeDTO curBrigade = brigIter2.next();
                    if (curBrigade.getBrigadeId() == getSelBrigade().getBrigadeId()) {
                        brigIter2.remove();
                        break;
                    }
                }
                changeBrigade(true);
            }
        }
        setBrigadeImages(getSelBrigade());
        setCorpsImages(getSelCorp());
        setArmyImages(getSelArmy());
    }

    private void degradeBrigade(final BrigadeDTO brigadeDTO) {
        if (brigadeDTO.isUpgradedToElite()) {
            ArmyStore.getInstance().cancelUpgradeBrigade(brigadeDTO.getBrigadeId(), true, brigadeDTO.getRegionId(), false);
            putSelectedBrigadeInNeed(brigadeDTO);
        }
    }

    private void setArmyImages(final ArmyDTO army) {
        if (ArmyStore.getInstance().canArmyUpgrade(army, true)) {
            expUpElArmyImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextUpgradeArmyOff.png");
        } else {
            expUpElArmyImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextUpgradeArmyOff-Gray.png");
        }
    }

    private void setCorpsImages(final CorpDTO corp) {
        if (ArmyStore.getInstance().canCorpsUpgrade(corp, true)) {
            expUpElCorpsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextUpgradeCorpsOff.png");
        } else {
            expUpElCorpsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextUpgradeCorpsOff-Gray.png");
        }
    }


    private void setBrigadeImages(final BrigadeDTO brigade) {

        if (ArmyStore.getInstance().canBrigadeUpgrade(brigade, true)) {
            expUpElBrigadeImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButUpgradeTroopsOff.png");

        } else {
            expUpElBrigadeImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButUpgradeTroopsOff-Gray.png");
        }
    }

    /**
     * @return the selArmy
     */
    public final ArmyDTO getSelArmy() {
        return selArmy;
    }

    /**
     * @return the selCorp
     */
    public final CorpDTO getSelCorp() {
        return selCorp;
    }

    /**
     * @param selArmy the selArmy to set
     */
    public final void setSelArmy(final ArmyDTO selArmy) {
        this.selArmy = selArmy;
    }

    /**
     * @param selCorp the selCorp to set
     */
    public final void setSelCorp(final CorpDTO selCorp) {
        this.selCorp = selCorp;
    }

    public final List<CorpDTO> getArmyCorps() {
        return armyCorps;
    }

    public final List<BrigadeDTO> getCorpBrigades() {
        return corpBrigades;
    }

    public final void setSelBrigade(final BrigadeDTO selBrigade) {
        this.selBrigade = selBrigade;
    }

    public final BrigadeDTO getSelBrigade() {
        return selBrigade;
    }
}
