package com.eaw1805.www.client.views.military;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderCostDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BarrackDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
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
import com.eaw1805.www.client.widgets.TextBoxEAW;
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

public class UpHdCountView extends DraggablePanel
        implements ArmyConstants, GoodConstants, OrderConstants, StyleConstants {
    // save all your troops of the coordinate in this list
    private List<ArmyDTO> sectorArmies = new ArrayList<ArmyDTO>();
    private final List<CorpDTO> armyCorps = new ArrayList<CorpDTO>();
    private final List<BrigadeDTO> corpBrigades = new ArrayList<BrigadeDTO>();

    private final List<ArmyDTO> armiesInNeed = new ArrayList<ArmyDTO>();
    private final Map<Integer, ArmyDTO> armiesInNeedMap = new HashMap<Integer, ArmyDTO>();
    private final List<CorpDTO> corpsInNeed = new ArrayList<CorpDTO>();
    private final Map<Integer, CorpDTO> corpsInNeedMap = new HashMap<Integer, CorpDTO>();
    private final List<BrigadeDTO> brigadesInNeed = new ArrayList<BrigadeDTO>();
    private final Map<Integer, BrigadeDTO> brigadesInNeedMap = new HashMap<Integer, BrigadeDTO>();

    private SectorDTO sectorDTO = new SectorDTO();
    private BrigadeDTO selBrigade = new BrigadeDTO();
    private final ImageButton typelftArmImg;

    private final ImageButton typerghArmImg;

    // Objects to keep our selections
    private ArmyDTO selArmy;
    private CorpDTO selCorp;
    private final ImageButton typelftCorpImg;
    private final ImageButton typerghCorpImg;
    private final AbsolutePanel brigadePanel;
    private final AbsolutePanel corpsPanel;
    private final AbsolutePanel armyPanel;
    private final ImageButton typelftBrigImg;
    private final ImageButton typerghBrigImg;

    //for brigade status panel
    private final AbsolutePanel brigadeCostPanel;
    private final Label lblMoneyValueBrigade;
    private final Label lblIndValueBrigade;
    private final Image hdCntUpBrigadeImg;
    private final Label lblHorsesValueBrigade;
    private final TextBoxEAW upValueBoxBrigade;

    //for corps status panel
    private final AbsolutePanel corpsCostPanel;
    private final Label lblMoneyValueCorps;
    private final Label lblIndValueCorps;
    private final Image hdCntUpCorpsImg;
    private final Label lblHorsesValueCorps;
    private final TextBoxEAW upValueBoxCorps;

    //for armies status panel
    private final AbsolutePanel armyCostPanel;
    private final Label lblMoneyValueArmy;
    private final Label lblIndValueArmy;
    private final Image hdCntUpArmyImg;
    private final Label lblHorsesValueArmy;
    private final TextBoxEAW upValueBoxArmy;
    private final StyledCheckBox onlyUpgradable;
    final ArmyStore arStore = ArmyStore.getInstance();


    private final ImageButton leftImg;
    private final ImageButton rightImg;
    private final VerticalPanelScrollChild trainedBrigadesPanel;
    private final PopupPanel totPeoplePopup;
    private final Label totPeopleLabel;

    public UpHdCountView(final SectorDTO _SectorDTO) {
        // Set selected sector
        setSectorDTO(_SectorDTO);
        // Initialize sector Armies

        setSectorArmies(arStore.getArmiesBySector(getSectorDTO(), false));

        initArmiesInNeed();
        totPeopleLabel = new Label("");
        totPeoplePopup = new PopupPanel(true);
        totPeoplePopup.setWidget(totPeopleLabel);
        totPeoplePopup.getElement().getStyle().setZIndex(1000000);
        this.setStyleName("barracksPanel4");
        this.setSize("610px", "680px");

        this.leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        this.leftImg.setStyleName(CLASS_POINTER);
        final BarrackDTO barrShip = BarrackStore.getInstance().getBarrackByPosition(_SectorDTO);
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
                final UpHdCountView upHdCountView = new UpHdCountView(barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(upHdCountView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(upHdCountView, UpHdCountView.this.getAbsoluteLeft(), UpHdCountView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(UpHdCountView.this);

                leftImg.deselect();
                leftImg.setUrl(leftImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(leftImg.getElement()).register();
        this.leftImg.setSize(SIZE_35PX, SIZE_35PX);
        add(this.leftImg, 15, 10);

        this.rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        this.rightImg.setStyleName(CLASS_POINTER);
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

                final UpHdCountView upHdCountView = new UpHdCountView(barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(upHdCountView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(upHdCountView, UpHdCountView.this.getAbsoluteLeft(), UpHdCountView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(UpHdCountView.this);

                rightImg.deselect();
                rightImg.setUrl(rightImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(rightImg.getElement()).register();
        this.rightImg.setSize(SIZE_35PX, SIZE_35PX);
        add(this.rightImg, 515, 10);

        final Label titleLbl = new Label(_SectorDTO.positionToString() + " - Increase Headcount");
        titleLbl.setStyleName("clearFontMedLarge whiteText");
        add(titleLbl, 65, 18);

        final UpHdCountView myself = this;
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
        imgX.setSize("36px", "36px");
        this.add(imgX, 560, 10);

        final Label trainedLbl = new Label("Brigades with increased headcount:");
        trainedLbl.setStyleName("clearFont whiteText");
        add(trainedLbl, 50, 400);

        trainedBrigadesPanel = new VerticalPanelScrollChild();
        trainedBrigadesPanel.setSize("100%", "0");
        trainedBrigadesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        final ScrollVerticalBarEAW trainedBrigsScrollPanel = new ScrollVerticalBarEAW(this.trainedBrigadesPanel, 90, false);
        trainedBrigsScrollPanel.setBarAlwaysVisible(true);
        trainedBrigsScrollPanel.setSize(394, 187);
        add(trainedBrigsScrollPanel, 36, 432);

        typelftArmImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        typelftArmImg.setSize(SIZE_16PX, SIZE_91PX);
        typelftArmImg.setStyleName(CLASS_POINTER, true);
        add(typelftArmImg, 22, 64);

        this.armyPanel = new AbsolutePanel();
        this.armyPanel.setSize(SIZE_366PX, SIZE_90PX);
        this.add(armyPanel, 40, 64);

        this.typerghArmImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.typerghArmImg.setSize(SIZE_16PX, SIZE_91PX);
        typerghArmImg.setStyleName(CLASS_POINTER, true);
        this.add(this.typerghArmImg, 405, 64);


        this.typelftCorpImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        this.typelftCorpImg.setStyleName(CLASS_POINTER, true);
        this.typelftCorpImg.setSize(SIZE_16PX, SIZE_91PX);
        this.add(this.typelftCorpImg, 22, 162);

        this.corpsPanel = new AbsolutePanel();
        this.corpsPanel.setSize(SIZE_366PX, SIZE_90PX);
        this.add(this.corpsPanel, 40, 162);

        this.typerghCorpImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.typerghCorpImg.setStyleName(CLASS_POINTER, true);
        this.typerghCorpImg.setSize(SIZE_16PX, SIZE_91PX);
        this.add(this.typerghCorpImg, 405, 162);


        this.typelftBrigImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        this.typelftBrigImg.setStyleName(CLASS_POINTER, true);
        this.typelftBrigImg.setSize(SIZE_16PX, SIZE_90PX);
        this.add(this.typelftBrigImg, 22, 258);

        this.brigadePanel = new AbsolutePanel();
        this.brigadePanel.setSize(SIZE_366PX, SIZE_90PX);
        this.add(this.brigadePanel, 40, 258);

        this.typerghBrigImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.typerghBrigImg.setStyleName(CLASS_POINTER, true);
        this.typerghBrigImg.setSize(SIZE_16PX, SIZE_90PX);
        this.add(this.typerghBrigImg, 405, 258);

        //for armies status panel
        armyCostPanel = new AbsolutePanel();
        lblMoneyValueArmy = new Label(VALUE_ZERO_FLOAT);
        this.lblIndValueArmy = new Label(VALUE_ZERO_FLOAT);
        this.hdCntUpArmyImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff-Gray.png");
        this.lblHorsesValueArmy = new Label(VALUE_ZERO_FLOAT);
        this.upValueBoxArmy = new TextBoxEAW(true);
        setupStatusPanel(armyCostPanel, lblMoneyValueArmy, lblIndValueArmy,
                hdCntUpArmyImg,
                lblHorsesValueArmy, upValueBoxArmy, ARMY);
        this.add(armyCostPanel, 426, 63);

        //for corps status panel
        corpsCostPanel = new AbsolutePanel();
        lblMoneyValueCorps = new Label(VALUE_ZERO_FLOAT);
        this.lblIndValueCorps = new Label(VALUE_ZERO_FLOAT);
        this.hdCntUpCorpsImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff-Gray.png");
        this.lblHorsesValueCorps = new Label(VALUE_ZERO_FLOAT);
        this.upValueBoxCorps = new TextBoxEAW(true);
        setupStatusPanel(corpsCostPanel, lblMoneyValueCorps, lblIndValueCorps,
                hdCntUpCorpsImg,
                lblHorsesValueCorps, upValueBoxCorps, CORPS);
        this.add(corpsCostPanel, 426, 159);

        //for brigade status panel
        brigadeCostPanel = new AbsolutePanel();
        lblMoneyValueBrigade = new Label(VALUE_ZERO_FLOAT);
        this.lblIndValueBrigade = new Label(VALUE_ZERO_FLOAT);
        this.hdCntUpBrigadeImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff-Gray.png");
        this.lblHorsesValueBrigade = new Label(VALUE_ZERO_FLOAT);
        this.upValueBoxBrigade = new TextBoxEAW(true);
        setupStatusPanel(brigadeCostPanel, lblMoneyValueBrigade, lblIndValueBrigade,
                hdCntUpBrigadeImg,
                lblHorsesValueBrigade, upValueBoxBrigade, BRIGADE);
        this.add(brigadeCostPanel, 426, 255);


        onlyUpgradable = new StyledCheckBox("Show only brigades that need increase", false, false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (onlyUpgradable.isChecked()) {
                    getSectorArmies().clear();
                    getSectorArmies().addAll(armiesInNeed);
                } else {
                    getSectorArmies().clear();
                    getSectorArmies().addAll(arStore.getArmiesBySector(getSectorDTO(), false));
                }
                changeArmy(true);
                changeCorp(true, true);
                changeBrigade(true);
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
                updateCostValues(0);
            }
        }).addToElement(onlyUpgradable.getCheckBox().getElement()).register();
        onlyUpgradable.setSize("350px", "21px");
        this.add(onlyUpgradable, 40, 360);

        initArmySelectionBar();
        changeArmy(true);
        changeCorp(true, true);
        changeBrigade(true);
        armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
        corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
        brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);

        initUpHeadCountBrigadesList();
        updateCostValues(0);
    }

    private void initArmiesInNeed() {
        ArmyDTO zeroArmy = null;
        CorpDTO zeroCorp = null;
        for (ArmyDTO army : arStore.getArmiesBySector(getSectorDTO(), false)) {
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
                    if (ArmyStore.getInstance().canBrigadeUpHeadcount(brigade)) {
                        addBrigade = true;
                        addCorps = true;
                        addArmy = true;
                    }
                    if (addBrigade) {
                        brigadesInNeed.add(brigade);
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
                armiesInNeedMap.put(army.getArmyId(), army);
            }
        }
        if (armiesInNeed.isEmpty() && zeroArmy != null) {
            armiesInNeed.add(zeroArmy);
            armiesInNeedMap.put(zeroArmy.getArmyId(), zeroArmy);
        }
        if (corpsInNeed.isEmpty() && zeroCorp != null) {
            corpsInNeed.add(zeroCorp);
            corpsInNeedMap.put(zeroCorp.getCorpId(), zeroCorp);
        }

        //at last be sure there is at least one corp
        // and at least one army so you have something to display
        if (armiesInNeed.isEmpty()) {
            final ArmyDTO lstArmy = arStore.getArmiesBySector(getSectorDTO(), false).get(0);
            armiesInNeed.add(lstArmy);
            armiesInNeedMap.put(lstArmy.getArmyId(), lstArmy);
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


    private void initUpHeadCountBrigadesList() {
        trainedBrigadesPanel.clear();
        //lets do this better by orders....
        //first retrieve the army train orders
        final List<ClientOrderDTO> armyTrainOrders = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT_ARMY);
        //then the corps train orders
        final List<ClientOrderDTO> corpsTrainOrders = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT_CORPS);
        //and at last the brigade train orders
        final List<ClientOrderDTO> brigadesTrainOrders = OrderStore.getInstance().getClientOrders().get(ORDER_INC_HEADCNT);

        final List<SelectableWidget> trainedUnits = new ArrayList<SelectableWidget>();
        if (armyTrainOrders != null) {
            for (ClientOrderDTO order : armyTrainOrders) {
                trainedUnits.add(new ArmyInfoPanel(ArmyStore.getInstance().getArmyById(order.getIdentifier(0))));
            }
        }
        if (corpsTrainOrders != null) {
            for (ClientOrderDTO order : corpsTrainOrders) {
                trainedUnits.add(new CorpsInfoPanel(ArmyStore.getInstance().getCorpByID(order.getIdentifier(0)), false));
            }
        }
        if (brigadesTrainOrders != null) {
            for (ClientOrderDTO order : brigadesTrainOrders) {
                trainedUnits.add(new BrigadeInfoPanel(ArmyStore.getInstance().getBrigadeById(order.getIdentifier(0)), false));
            }
        }
        final SelectionListPanel selectionList;
        if (trainedUnits.isEmpty()) {
            selectionList = null;
        } else {
            selectionList = new SelectionListPanel(trainedUnits);
            trainedBrigadesPanel.add(selectionList);
        }


        final ImageButton btnCancelSelectedBattalion = new ImageButton("http://static.eaw1805.com/images/panels/buildShips/ButDeleteOrderOff.png");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                SelectableWidget selected = null;
                if (selectionList == null) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "No brigade unit", false);
                } else {
                    selected = selectionList.getSelectedWidget();
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
                }
                initUpHeadCountBrigadesList();
                if (selected == null) {
                    updateCostValues(0);
                } else {
                    updateCostValues(selected.getIdentifier());
                }
            }
        }).addToElement(btnCancelSelectedBattalion.getElement()).register();


        this.add(btnCancelSelectedBattalion, 76, 630);
        btnCancelSelectedBattalion.setSize("272px", "26px");
        trainedBrigadesPanel.resizeScrollBar();
    }

    private void setupStatusPanel(final AbsolutePanel statusPanel,
                                  final Label lblMoneyValue,
                                  final Label lblIndValue,
                                  final Image hdCntUpImg,
                                  final Label lblHorsesValue,
                                  final TextBoxEAW upValueBox,
                                  final int type) {

        statusPanel.setStyleName("standardPanel");
        statusPanel.setSize("173px", SIZE_90PX);


        final Image moneyImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_MONEY + ".png");
        moneyImg.setSize(SIZE_20PX, SIZE_20PX);
        statusPanel.add(moneyImg, 4, 9);

        lblMoneyValue.setStyleName("clearFontSmall");
        statusPanel.add(lblMoneyValue, 26, 11);
        lblMoneyValue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMoneyValue.setSize("57px", "18px");

        final Image indPntsImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_INPT + ".png");
        indPntsImg.setSize(SIZE_20PX, SIZE_20PX);
        statusPanel.add(indPntsImg, 4, 38);

        lblIndValue.setStyleName("clearFontSmall");
        statusPanel.add(lblIndValue, 26, 41);
        lblIndValue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblIndValue.setSize("57px", "18px");

        final Image horseImg = new Image("http://static.eaw1805.com/images/goods/good-" + GOOD_HORSE + ".png");
        horseImg.setSize(SIZE_20PX, SIZE_20PX);
        statusPanel.add(horseImg, 85, 9);

        lblHorsesValue.setStyleName("clearFontSmall");
        statusPanel.add(lblHorsesValue, 108, 11);
        lblHorsesValue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblHorsesValue.setSize("57px", "18px");

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                upValueBox.setFocus(true);
            }
        }).addToElement(upValueBox.getElement()).register();


        upValueBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(final KeyUpEvent event) {
                updateCostValues(type);
            }
        });
        upValueBox.addChangeHandler(new ChangeHandler() {
            public void onChange(final ChangeEvent changeEvent) {
                updateCostValues(type);
            }
        });

        upValueBox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(final BlurEvent blurEvent) {
                totPeopleLabel.setText("");
                totPeoplePopup.hide();
            }
        });

        upValueBox.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(final FocusEvent focusEvent) {
                updateCostValues(type);
            }
        });

        hdCntUpImg.setTitle("Increase brigades headcount.");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                try {
                    if (type == BRIGADE && getSelBrigade() != null && getSelBrigade().getStartLoaded()) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Brigade just unloaded and cannot be trained or increase headcount", false);
                    } else if (hdCntUpImg.getUrl().endsWith("Gray.png")) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Brigade already at maximum headcount", false);
                    } else {
                        switch (type) {
                            case BRIGADE:
                                upgradeBrigade(Integer.parseInt(upValueBox.getText()));
                                break;
                            case CORPS:
                                upgradeCorps(Integer.parseInt(upValueBox.getText()));
                                break;
                            case ARMY:
                                upgradeArmy(Integer.parseInt(upValueBox.getText()));
                                break;
                            default:
                                break;
                        }


                    }

                } catch (Exception ex) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "You need to select a brigade first", false);
                }
                initUpHeadCountBrigadesList();
                updateCostValues(type);
            }
        }).addToElement(hdCntUpImg.getElement()).register();

        hdCntUpImg.setStyleName(CLASS_POINTER, true);
        statusPanel.add(hdCntUpImg, 2, 64);
        hdCntUpImg.setSize("150px", "");

        statusPanel.add(upValueBox, 102, 35);
        upValueBox.setSize("58px", "25px");
    }

    private void initArmySelectionBar() {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                typelftArmImg.deselect();
                changeArmy(false);
                resetArmyCostValues();
                resetCorpsCostValues();
                resetBrigadeCostValues();
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
                resetArmyCostValues();
                resetCorpsCostValues();
                resetBrigadeCostValues();
                armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
                corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
                brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);
            }
        }).addToElement(typerghArmImg.getElement()).register();

        initCorpSelectionBar();
    }

    private void changeArmy(final boolean forward) {
        int index = 0;
        if (getSectorArmies() != null && !getSectorArmies().isEmpty()) {
            if (selArmy != null) {
                for (int i = 0; i < getSectorArmies().size(); i++) {
                    if (getSectorArmies().get(i).getArmyId() == selArmy
                            .getArmyId()) {
                        if (forward) {
                            if (i == (getSectorArmies().size() - 1)) {
                                index = 0;
                                break;
                            } else {
                                index = i + 1;
                                break;
                            }
                        } else {
                            if (i == 0) {
                                index = getSectorArmies().size() - 1;
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
            armyPanel.add(new ArmyInfoPanel(getSectorArmies().get(index)));

            setSelArmy(getSectorArmies().get(index));
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
            if (getSectorArmies() != null && getSectorArmies().size() > 1) {
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
                resetCorpsCostValues();
                resetBrigadeCostValues();
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
                resetCorpsCostValues();
                resetBrigadeCostValues();
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
                    if (getArmyCorps().get(i).getCorpId() == getSelCorp()
                            .getCorpId()) {
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
            this.corpsPanel.clear();
            this.corpsPanel.add(new CorpsInfoPanel(getArmyCorps().get(index), false));
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
                resetBrigadeCostValues();
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
                resetBrigadeCostValues();
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


    private void updateCostValues(final int type) {
        int brigHeads = 0;
        int corpsHeads = 0;
        int armyHeads = 0;
        if (!upValueBoxBrigade.getText().isEmpty()) {
            try {
                brigHeads = Integer.parseInt(upValueBoxBrigade.getText());
            } catch (Exception ignore) {
                //do nothing here
            }
        }
        if (!upValueBoxCorps.getText().isEmpty()) {
            try {
                corpsHeads = Integer.parseInt(upValueBoxCorps.getText());
            } catch (Exception ignore) {
                //do nothing here
            }
        }
        if (!upValueBoxArmy.getText().isEmpty()) {
            try {
                armyHeads = Integer.parseInt(upValueBoxArmy.getText());
            } catch (Exception ignore) {
                //do nothing here
            }
        }

        OrderCostDTO brigadeCost;
        if (getSelBrigade() != null) {
            try {
                brigadeCost = CostCalculators.getIncreaseHeadcountCost(getSelBrigade(), brigHeads);
            } catch (Exception e) {
                brigadeCost = new OrderCostDTO();
            }
        } else {
            brigadeCost = new OrderCostDTO();
        }

        OrderCostDTO corpsCost;
        if (getSelCorp() != null) {
            try {
                corpsCost = CostCalculators.getIncreaseHeadcountCorpsCost(getSelCorp(), corpsHeads);
            } catch (Exception e) {
                corpsCost = new OrderCostDTO();
            }
        } else {
            corpsCost = new OrderCostDTO();
        }

        OrderCostDTO armyCost;
        if (getSelArmy() != null) {
            try {
                armyCost = CostCalculators.getIncreaseHeadcountArmyCost(getSelArmy(), armyHeads);
            } catch (Exception e) {
                armyCost = new OrderCostDTO();
            }
        } else {
            armyCost = new OrderCostDTO();
        }


        lblMoneyValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_MONEY)));
        lblIndValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_INPT)));
        lblHorsesValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_HORSE)));

        lblMoneyValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_MONEY)));
        lblIndValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_INPT)));
        lblHorsesValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_HORSE)));

        lblMoneyValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_MONEY)));
        lblIndValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_INPT)));
        lblHorsesValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_HORSE)));
        switch (type) {
            case BRIGADE:
                totPeopleLabel.setText("Total " + brigadeCost.getNumericCost(GOOD_PEOPLE) + " people");
                totPeoplePopup.showRelativeTo(upValueBoxBrigade);
                break;
            case CORPS:
                totPeopleLabel.setText("Total " + corpsCost.getNumericCost(GOOD_PEOPLE) + " people");
                totPeoplePopup.showRelativeTo(upValueBoxCorps);
                break;
            case ARMY:
                totPeopleLabel.setText("Total " + armyCost.getNumericCost(GOOD_PEOPLE) + " people");
                totPeoplePopup.showRelativeTo(upValueBoxArmy);
                break;
            default:
                totPeopleLabel.setText("");
                totPeoplePopup.hide();
        }
    }

    private void resetBrigadeCostValues() {
        upValueBoxBrigade.setText("0");
        lblMoneyValueBrigade.setText(VALUE_ZERO_FLOAT);
        lblIndValueBrigade.setText(VALUE_ZERO_FLOAT);
        lblHorsesValueBrigade.setText(VALUE_ZERO_FLOAT);
    }

    private void resetCorpsCostValues() {
        upValueBoxCorps.setText("0");
        lblMoneyValueCorps.setText(VALUE_ZERO_FLOAT);
        lblIndValueCorps.setText(VALUE_ZERO_FLOAT);
        lblHorsesValueCorps.setText(VALUE_ZERO_FLOAT);
    }

    private void resetArmyCostValues() {
        upValueBoxArmy.setText("0");
        lblMoneyValueArmy.setText(VALUE_ZERO_FLOAT);
        lblIndValueArmy.setText(VALUE_ZERO_FLOAT);
        lblHorsesValueArmy.setText(VALUE_ZERO_FLOAT);
    }


    private void upgradeArmy(final int value) {
        if (ArmyStore.getInstance().upHeadCountArmy(getSelArmy().getArmyId(), value, getSectorDTO())) {
            for (CorpDTO corp : getSelArmy().getCorps().values()) {
                setSelCorp(corp);
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    setSelBrigade(brigade);
                    removeSelectedBrigadeInNeed();
                }
            }
        }
    }

    private void upgradeCorps(final int value) {
        if (ArmyStore.getInstance().upHeadCountCorps(getSelCorp().getCorpId(), value, getSectorDTO())) {
            for (BrigadeDTO brigade : getSelCorp().getBrigades().values()) {
                setSelBrigade(brigade);
                removeSelectedBrigadeInNeed();
            }
        }

    }

    private void upgradeBrigade(final int value) {
        boolean inNeed = false;
        if (!getSelBrigade().IsUpHeadcount()) {
            if (getSelBrigade().getBattalions() != null) {
                for (BattalionDTO battalion : getSelBrigade().getBattalions()) {
                    int headcount = 800;
                    if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                            || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                            || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                        headcount = 1000;
                    }
                    if ((headcount - battalion.getHeadcount()) > 0) {
                        inNeed = true;
                        break;
                    }
                }
            }


            if (inNeed && ArmyStore.getInstance().upHeadCountBrigade(getSelBrigade().getBrigadeId(), value, getSectorDTO())) {
                removeSelectedBrigadeInNeed();
            }

        }

    }

    private void removeSelectedBrigadeInNeed() {
        if (getSelBrigade().IsUpHeadcount()) {
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

    private void putSelectedBrigadeInNeed(final BrigadeDTO brigade) {
        if (ArmyStore.getInstance().canBrigadeUpHeadcount(brigade)) {
            brigadesInNeedMap.put(brigade.getBrigadeId(), brigade);
            if (getSelCorp().getCorpId() == brigade.getCorpId()) {
                //check if it is already in because maybe we didn't remove it
                boolean found = false;
                for (final BrigadeDTO brigade2 : getCorpBrigades()) {
                    if (brigade.getBrigadeId() == brigade2.getBrigadeId()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    getCorpBrigades().add(brigade);
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


    private void degradeArmy(final ArmyDTO army) {
        ArmyStore.getInstance().cancelUpHeadCountArmy(army.getArmyId());
        for (final CorpDTO corp : army.getCorps().values()) {
            setSelCorp(corp);
            for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                setSelBrigade(brigade);
                putSelectedBrigadeInNeed(brigade);
            }
        }
    }

    private void degradeCorps(final CorpDTO corps) {
        ArmyStore.getInstance().cancelUpHeadCountCorps(corps.getCorpId());
        for (final BrigadeDTO brigade : getSelCorp().getBrigades().values()) {
            setSelBrigade(brigade);
            putSelectedBrigadeInNeed(brigade);
        }
    }

    private void degradeBrigade(final BrigadeDTO brigade) {
        if (brigade.IsUpHeadcount()) {
            ArmyStore.getInstance().cancelUpHeadCountBrigade(brigade.getBrigadeId());
            putSelectedBrigadeInNeed(brigade);
        }
    }

    private void setCorpsImages(final CorpDTO corp) {
        boolean upEnabled = false;
        boolean downEnabled = false;
        for (final BrigadeDTO brigade : corp.getBrigades().values()) {
            if (brigade.IsUpHeadcount() || brigade.getLoaded()) {
                downEnabled = true;
            } else {
                if (brigade.getBattalions() != null && !brigade.getBattalions().isEmpty()) {
                    for (final BattalionDTO battalion : brigade.getBattalions()) {
                        int headCount = 800;
                        if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                                || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                                || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                            headCount = 1000;
                        }
                        if ((headCount - battalion.getHeadcount()) > 0) {
                            upEnabled = true;
                        }
                    }
                }
            }
            if (upEnabled && downEnabled) {
                break;
            }
        }
        if (upEnabled) {
            hdCntUpCorpsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff.png");
        } else {
            hdCntUpCorpsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff-Gray.png");
        }
    }

    private void setArmyImages(final ArmyDTO army) {
        boolean upEnabled = false;
        boolean downEnabled = false;
        for (final CorpDTO corp : army.getCorps().values()) {
            for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                if (brigade.IsUpHeadcount() || brigade.getLoaded()) {
                    downEnabled = true;
                } else {
                    if (brigade.getBattalions() != null && !brigade.getBattalions().isEmpty()) {
                        for (final BattalionDTO battalion : brigade.getBattalions()) {
                            int headCount = 800;
                            if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                                headCount = 1000;
                            }
                            if ((headCount - battalion.getHeadcount()) > 0) {
                                upEnabled = true;
                            }
                        }
                    }
                }
                if (upEnabled && downEnabled) {
                    break;
                }
            }
        }

        if (upEnabled) {
            hdCntUpArmyImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff.png");
        } else {
            hdCntUpArmyImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff-Gray.png");
        }
    }

    private void setBrigadeImages(final BrigadeDTO brigade) {
        if (ArmyStore.getInstance().canBrigadeUpHeadcount(brigade)) {
            hdCntUpBrigadeImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff.png");
        } else {
            hdCntUpBrigadeImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButIncreaseHeadcountOff-Gray.png");
        }
    }

    public final void setSectorDTO(final SectorDTO sectorDTO) {
        this.sectorDTO = sectorDTO;
    }

    public final SectorDTO getSectorDTO() {
        return sectorDTO;
    }

    public final void setSectorArmies(final List<ArmyDTO> sectorArmies) {
        this.sectorArmies = sectorArmies;
    }

    public final List<ArmyDTO> getSectorArmies() {
        return sectorArmies;
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
