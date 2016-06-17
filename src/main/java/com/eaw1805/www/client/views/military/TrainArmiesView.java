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
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.gui.scroll.VerticalPanelScrollChild;
import com.eaw1805.www.client.views.infopanels.units.ArmyInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
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
import com.eaw1805.www.shared.stores.units.TransportStore;
import com.eaw1805.www.shared.stores.util.calculators.CostCalculators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TrainArmiesView
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
    private SectorDTO sectorDTO = new SectorDTO();
    private BrigadeDTO selBrigade = new BrigadeDTO();
    private final ImageButton typelftArmImg;
    private final ImageButton typerghArmImg;

    // Objects to keep our selections
    private ArmyDTO selArmy;
    private CorpDTO selCorp;

    private ImageButton typelftCorpImg;
    private ImageButton typerghCorpImg;
    private AbsolutePanel brigadePanel;
    private AbsolutePanel corpPanel;
    private AbsolutePanel armyPanel;
    private ImageButton typelftBrigImg;
    private ImageButton typerghBrigImg;


    //for brigades
    private AbsolutePanel brigadeCostPanel;
    private Label lblMoneyValueBrigade;
    private Label lblIndValueBrigade;
    private Image expUpBrigadeImg;


    //for corps
    private AbsolutePanel corpsCostPanel;
    private Label lblMoneyValueCorps;
    private Label lblIndValueCorps;
    private Image expUpCorpsImg;

    //for armies
    private AbsolutePanel armyCostPanel;
    private Label lblMoneyValueArmy;
    private Label lblIndValueArmy;
    private Image expUpArmyImg;

    private StyledCheckBox onlyUpgradable;

    private ImageButton leftImg;
    private ImageButton rightImg;

    private VerticalPanelScrollChild trainedBrigadesPanel;

    int offsetTop = -6;
    int offsetLeft = -6;

    public TrainArmiesView(final SectorDTO thisSector) {
        // Set selected sector
        sectorDTO = thisSector;

        // Initialize sector Armies
        sectorArmies = arStore.getArmiesBySector(sectorDTO, false);

        initArmiesInNeed();

        this.setStyleName("barracksPanel3");
        this.setSize("630px", "704px");

        this.leftImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButLeftOff.png");
        this.leftImg.setStyleName("pointer");
        final BarrackDTO barrShip = BarrackStore.getInstance().getBarrackByPosition(thisSector);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                int index = BarrackStore.getInstance().getBarracksList().indexOf(barrShip);
                int firstIndex = index;

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
                    boolean armyActionsEnabled = (armiesOnSector != null && armiesOnSector.size() > 0);
                    //if this barrack has armies.. break and load the barrack
                    if (armyActionsEnabled) {
                        break;
                    }
                }

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                //SectorDTO sector = regionStore.getSelectedSector(mapStore.getActiveRegion());
                TrainArmiesView trArmiesView = new TrainArmiesView(barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(trArmiesView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(trArmiesView, TrainArmiesView.this.getAbsoluteLeft(), TrainArmiesView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(TrainArmiesView.this);

                leftImg.deselect();
                leftImg.setUrl(leftImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(leftImg.getElement()).register();
        add(this.leftImg, 15, 9);
        this.leftImg.setSize("35px", "35px");

        this.rightImg = new ImageButton("http://static.eaw1805.com/images/panels/barracks/ButRightOff.png");
        this.rightImg.setStyleName("pointer");
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
                    boolean armyActionsEnabled = (armiesOnSector != null && armiesOnSector.size() > 0);
                    //if this barrack has armies.. break and load the barrack
                    if (armyActionsEnabled) {
                        break;
                    }

                }

                MapStore.getInstance().getMapsView().goToPosition(nextBarrack);

                TrainArmiesView trArmiesView = new TrainArmiesView(barrackSector);
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(trArmiesView);
                GameStore.getInstance().getLayoutView().setWidgetPosition(trArmiesView, TrainArmiesView.this.getAbsoluteLeft(), TrainArmiesView.this.getAbsoluteTop(), false, true);
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(TrainArmiesView.this);

                rightImg.deselect();
                rightImg.setUrl(rightImg.getUrl().replace("off", "Hover"));
            }
        }).addToElement(rightImg.getElement()).register();

        rightImg.setSize("35px", "35px");
        add(rightImg, 510, 9);

        final Label titleLbl = new Label(thisSector.positionToString() + " - Train your troops");
        titleLbl.setStyleName("clearFontMedLarge");
        titleLbl.setStyleName("whiteText", true);
        this.add(titleLbl, 60 + offsetLeft, 20 + offsetTop);

        final TrainArmiesView myself = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close panel");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        imgX.setSize("35px", "35px");
        this.add(imgX, 570 + offsetLeft, 15 + offsetTop);

        final Label trainedLbl = new Label("Trained brigades:");
        trainedLbl.setStyleName("clearFont whiteText");
        add(trainedLbl, 50 + offsetLeft, 410 + offsetTop);

        this.trainedBrigadesPanel = new VerticalPanelScrollChild();
        final ScrollVerticalBarEAW trainedBrigsScrollPanel = new ScrollVerticalBarEAW(trainedBrigadesPanel, 90, false);
        trainedBrigsScrollPanel.setBarAlwaysVisible(true);
        trainedBrigsScrollPanel.setSize(394, 187);
        trainedBrigadesPanel.setSize("100%", "0");
        trainedBrigadesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        add(trainedBrigsScrollPanel, 40 + offsetLeft, 435 + offsetTop);

        this.typelftArmImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        this.add(this.typelftArmImg, 25 + offsetLeft, 71 + offsetTop);
        this.typelftArmImg.setSize("16px", "91px");
        typelftArmImg.setStyleName("pointer", true);

        this.armyPanel = new AbsolutePanel();
        this.add(armyPanel, 45 + offsetLeft, 71 + offsetTop);
        this.armyPanel.setSize("366px", "90px");

        this.typerghArmImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.add(this.typerghArmImg, 415 + offsetLeft, 71 + offsetTop);
        this.typerghArmImg.setSize("16px", "91px");
        typerghArmImg.setStyleName("pointer", true);


        this.typelftCorpImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        this.typelftCorpImg.setStyleName("pointer", true);
        this.add(this.typelftCorpImg, 25 + offsetLeft, 166 + offsetTop);
        this.typelftCorpImg.setSize("16px", "91px");

        this.corpPanel = new AbsolutePanel();
        this.add(this.corpPanel, 45 + offsetLeft, 166 + offsetTop);
        this.corpPanel.setSize("366px", "90px");

        this.typerghCorpImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.typerghCorpImg.setStyleName("pointer", true);
        this.add(this.typerghCorpImg, 415 + offsetLeft, 166 + offsetTop);
        this.typerghCorpImg.setSize("16px", "91px");


        this.typelftBrigImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        this.typelftBrigImg.setStyleName("pointer", true);
        this.add(this.typelftBrigImg, 25 + offsetLeft, 265 + offsetTop);
        this.typelftBrigImg.setSize("16px", "91px");

        this.brigadePanel = new AbsolutePanel();
        this.add(this.brigadePanel, 45 + offsetLeft, 265 + offsetTop);
        this.brigadePanel.setSize("366px", "90px");

        this.typerghBrigImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.typerghBrigImg.setStyleName("pointer", true);
        this.add(this.typerghBrigImg, 415 + offsetLeft, 265 + offsetTop);
        this.typerghBrigImg.setSize("16px", "91px");

        armyCostPanel = new AbsolutePanel();
        lblMoneyValueArmy = new Label("0.0");
        lblIndValueArmy = new Label("0.0");
        expUpArmyImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButHorTextTrainArmyOff-Gray.png");
        try {

            setupStatusPanel(armyCostPanel,
                    lblMoneyValueArmy, lblIndValueArmy,
                    expUpArmyImg,
                    ARMY);
            this.add(armyCostPanel, 430 + offsetLeft, 68 + offsetTop);
        } catch (Exception e) {
            // do nothing
        }

        corpsCostPanel = new AbsolutePanel();
        lblMoneyValueCorps = new Label("0.0");
        lblIndValueCorps = new Label("0.0");
        expUpCorpsImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButHorTextTrainCorpsOff-Gray.png");
        try {
            setupStatusPanel(corpsCostPanel,
                    lblMoneyValueCorps, lblIndValueCorps,
                    expUpCorpsImg,
                    CORPS);
            this.add(corpsCostPanel, 430 + offsetLeft, 163 + offsetTop);
        } catch (Exception e) {
            // do nothing
        }

        brigadeCostPanel = new AbsolutePanel();
        lblMoneyValueBrigade = new Label("0.0");
        lblIndValueBrigade = new Label("0.0");
        expUpBrigadeImg = new Image("http://static.eaw1805.com/images/panels/barracks/ButTrainTroopsOff-Gray.png");
        try {
            setupStatusPanel(brigadeCostPanel,
                    lblMoneyValueBrigade, lblIndValueBrigade,
                    expUpBrigadeImg,
                    BRIGADE);
            this.add(brigadeCostPanel, 430 + offsetLeft, 260 + offsetTop);
        } catch (Exception e) {
            // do nothing
        }

        if (TutorialStore.getInstance().isTutorialMode()) {
            onlyUpgradable = new StyledCheckBox("Show only brigades that need train", true, false);
        } else {
            onlyUpgradable = new StyledCheckBox("Show only brigades that need train", false, false);
        }
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
        this.add(onlyUpgradable, 40 + offsetLeft, 366 + offsetTop);
        onlyUpgradable.setSize("350px", "21px");

        initArmySelectionBar();
        changeArmy(true);
        changeCorp(true, true);
        changeBrigade(true);
        armyCostPanel.setVisible(getSelArmy() != null && getSelArmy().getArmyId() != 0);
        corpsCostPanel.setVisible(getSelCorp() != null && getSelCorp().getCorpId() != 0);
        brigadeCostPanel.setVisible(getSelBrigade() != null && getSelBrigade().getBrigadeId() != 0);

        initTrainedBrigadesList();
        updateCostValues();
    }


    public void initArmiesInNeed() {
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
                    if (brigade.getBattalions() != null && !brigade.isUpgraded()
                            && !brigade.getLoaded()
                            && !TransportStore.getInstance().hasUnitLoadOrder(BRIGADE, brigade.getBrigadeId())) {
                        for (BattalionDTO battalion : brigade.getBattalions()) {
                            if (battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
                                addBrigade = true;
                                addCorps = true;
                                addArmy = true;
                            }
                        }
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


    public final void setupStatusPanel(final AbsolutePanel statusPanel,
                                       final Label lblMoneyValue,
                                       final Label lblIndValue,
                                       final Image expUpImg,
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

        expUpImg.setTitle("Train brigades battalions.");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                try {
                    if (type == BRIGADE && getSelBrigade() != null && getSelBrigade().getStartLoaded()) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Brigade just unloaded and cannot be trained or increase headcount", false);
                    } else if (!expUpImg.getUrl().endsWith("Gray.png")) {
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
                                break;
                        }
                    }
                } catch (Exception ex) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to train brigade", false);
                }
                initTrainedBrigadesList();
                updateCostValues();
            }
        }).addToElement(expUpImg.getElement()).register();

        expUpImg.setStyleName("pointer", true);
        statusPanel.add(expUpImg, 0, 69);
        expUpImg.setSize("150px", "");

        if (TutorialStore.getInstance().isTutorialMode()
                && TutorialStore.getInstance().getMonth() == 6) {
            TutorialStore.highLightButton(expUpImg);
        }
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
                    if (sectorArmies.get(i).getArmyId() == selArmy
                            .getArmyId()) {
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

            if (sectorArmies != null && sectorArmies.size() > 1) {
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
        if (getArmyCorps() != null && getArmyCorps().size() > 0) {
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
        if (getCorpBrigades() != null && getCorpBrigades().size() > 0) {
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
            noBrigFoundPanel.setSize("366px", "90px");

            final Label lblNoBrigadeFound = new Label("No Brigade found in this corps");
            lblNoBrigadeFound.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            lblNoBrigadeFound.setStyleName("clearFont-large");
            noBrigFoundPanel.add(lblNoBrigadeFound, 3, 15);
            brigadePanel.add(noBrigFoundPanel);
        }
        setBrigadeImages(getSelBrigade());
    }


    private void initTrainedBrigadesList() {
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
                if (order.getIdentifier(1) == 0) {
                    trainedUnits.add(new ArmyInfoPanel(ArmyStore.getInstance().getArmyById(order.getIdentifier(0))));
                }
            }
        }

        if (corpsTrainOrders != null) {
            for (ClientOrderDTO order : corpsTrainOrders) {
                if (order.getIdentifier(1) == 0) {
                    trainedUnits.add(new CorpsInfoPanel(ArmyStore.getInstance().getCorpByID(order.getIdentifier(0)), false));
                }
            }
        }

        if (brigadesTrainOrders != null) {
            for (ClientOrderDTO order : brigadesTrainOrders) {
                if (order.getIdentifier(1) == 0) {
                    trainedUnits.add(new BrigadeInfoPanel(ArmyStore.getInstance().getBrigadeById(order.getIdentifier(0)), false));
                }
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
                if (selectionList == null) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "No brigade unit", false);
                } else {
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
                }
                initTrainedBrigadesList();
                updateCostValues();
            }
        }).addToElement(btnCancelSelectedBattalion.getElement()).register();

        this.add(btnCancelSelectedBattalion, 76 + offsetLeft, 647 + offsetTop);
        btnCancelSelectedBattalion.setSize("272px", "26px");
        trainedBrigadesPanel.resizeScrollBar();
    }

    private void updateCostValues() {
        final OrderCostDTO brigadeCost = CostCalculators.getTrainBrigadeCost(getSelBrigade());
        final OrderCostDTO corpsCost = CostCalculators.getTrainCorpsCost(getSelCorp());
        final OrderCostDTO armyCost = CostCalculators.getTrainArmyCost(getSelArmy());

        lblMoneyValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_MONEY)));
        lblIndValueBrigade.setText(String.valueOf(brigadeCost.getNumericCost(GOOD_INPT)));

        lblMoneyValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_MONEY)));
        lblIndValueCorps.setText(String.valueOf(corpsCost.getNumericCost(GOOD_INPT)));

        lblMoneyValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_MONEY)));
        lblIndValueArmy.setText(String.valueOf(armyCost.getNumericCost(GOOD_INPT)));

    }

    private void upgradeArmy() {
        if (ArmyStore.getInstance().upgradeArmy(getSelArmy().getArmyId(), false, sectorDTO)) {
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
        ArmyStore.getInstance().cancelUpgradeArmy(army.getArmyId(), false, army.getRegionId(), false);
        for (CorpDTO corp : army.getCorps().values()) {
            setSelCorp(corp);
            for (BrigadeDTO brigade : corp.getBrigades().values()) {
                setSelBrigade(brigade);
                putSelectedBrigadeInNeed(brigade);
            }
        }
    }

    private void upgradeCorps() {
        if (ArmyStore.getInstance().upgradeCorps(getSelCorp().getCorpId(), false, sectorDTO)) {
            for (BrigadeDTO brigade : getSelCorp().getBrigades().values()) {
                setSelBrigade(brigade);
                removeSelectedBrigadeInNeed();
            }
        }
    }

    private void degradeCorps(final CorpDTO corp) {
        ArmyStore.getInstance().cancelUpgradeCorps(corp.getCorpId(), false, corp.getRegionId(), false);
        for (BrigadeDTO brigade : corp.getBrigades().values()) {
            setSelBrigade(brigade);
            putSelectedBrigadeInNeed(brigade);
        }
    }

    private void upgradeBrigade() {
        boolean brigadeInNeed = false;
        if (!getSelBrigade().isUpgraded()) {
            for (BattalionDTO battalion : getSelBrigade().getBattalions()) {
                if (battalion.getExperience() < battalion.getEmpireArmyType().getMaxExp()) {
                    brigadeInNeed = true;
                    break;
                }
            }
        }

        if (brigadeInNeed
                && ArmyStore.getInstance().upgradeBrigade(getSelBrigade().getBrigadeId(), false, sectorDTO)) {
            removeSelectedBrigadeInNeed();
        }

    }

    private void removeSelectedBrigadeInNeed() {
        if (getSelBrigade().isUpgraded()) {
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

    private void putSelectedBrigadeInNeed(final BrigadeDTO brigadeDTO) {
        if (ArmyStore.getInstance().canBrigadeUpgrade(brigadeDTO, false)) {
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

    private void degradeBrigade(final BrigadeDTO brigadeDTO) {
        if (brigadeDTO.isUpgraded()) {
            ArmyStore.getInstance().cancelUpgradeBrigade(brigadeDTO.getBrigadeId(), false, brigadeDTO.getRegionId(), false);
            //add it back to in need list...
            putSelectedBrigadeInNeed(brigadeDTO);
        }
    }


    private void setArmyImages(final ArmyDTO army) {
        if (ArmyStore.getInstance().canArmyUpgrade(army, false)) {
            expUpArmyImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextTrainArmyOff.png");

        } else {
            expUpArmyImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextTrainArmyOff-Gray.png");
        }


    }

    private void setCorpsImages(final CorpDTO corp) {
        if (ArmyStore.getInstance().canCorpsUpgrade(corp, false)) {
            expUpCorpsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextTrainCorpsOff.png");
        } else {
            expUpCorpsImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButHorTextTrainCorpsOff-Gray.png");
        }
    }


    private void setBrigadeImages(final BrigadeDTO brigade) {
        if (ArmyStore.getInstance().canBrigadeUpgrade(brigade, false)) {
            expUpBrigadeImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButTrainTroopsOff.png");

        } else {
            expUpBrigadeImg.setUrl("http://static.eaw1805.com/images/panels/barracks/ButTrainTroopsOff-Gray.png");
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
