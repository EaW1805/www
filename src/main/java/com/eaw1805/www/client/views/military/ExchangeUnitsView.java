package com.eaw1805.www.client.views.military;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.extras.ExchangeUnitsWidget;
import com.eaw1805.www.client.views.popups.SelectArmyPopup;
import com.eaw1805.www.client.views.popups.SelectBrigadePopup;
import com.eaw1805.www.client.views.popups.SelectCorpPopup;
import com.eaw1805.www.client.widgets.UnitImage;
import com.eaw1805.www.shared.stores.RegionStore;

import java.util.List;


public class ExchangeUnitsView extends Composite implements ArmyConstants, StyleConstants {

    private final Label lblSelectTypeOf;
    private int selArmyFirst = -1, selArmySecond = -1;
    private int selCorpFirst = -1, selCorpSecond = -1;
    private int selBrigFirst = -1, selBrigSecond = -1;
    private final ExchangeUnitsWidget exchView;




    private final UnitImage army1Img;
    private final UnitImage corp1Img;
    private final UnitImage brig1Img;

    private final UnitImage army2Img;

    private final UnitImage corp2Img;

    private final UnitImage brig2Img;

    public ExchangeUnitsView(final List<ArmyDTO> armiesList) {
        exchView = new ExchangeUnitsWidget(RegionStore.getInstance()
                .getRegionSectorsByRegionId(armiesList.get(0).getRegionId())
                [armiesList.get(0).getX()][armiesList.get(0).getY()].getId());
        final AbsolutePanel exchangeBasePanel = new AbsolutePanel();
        exchangeBasePanel.setStyleName("militaryPanel-new");
        initWidget(exchangeBasePanel);
        exchangeBasePanel.setSize("679px", "580px");

        this.lblSelectTypeOf = new Label("Select type of organization:");
        this.lblSelectTypeOf.setStyleName("clearFontMiniTitle");
        exchangeBasePanel.add(this.lblSelectTypeOf, 21, 24);

        final ExchangeUnitsView mySelf = this;

        exchangeBasePanel.add(this.exchView, 21, 124);

        final Label label = new Label("Army:");
        label.setStyleName("whiteText");
        exchView.add(label, 404, 171);
        label.setSize("35px", "15px");

        this.army2Img = new UnitImage("http://static.eaw1805.com/images/buttons/goto.png", null);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                final SelectArmyPopup selArmy = new SelectArmyPopup(armiesList, mySelf, 2);
                selArmy.showRelativeTo(exchView);
                selArmy.setPopupPosition(selArmy.getAbsoluteLeft(), selArmy.getAbsoluteTop() + 252 + 194);
            }
        }).addToElement(army2Img.getElement()).register();

        this.army2Img.setStyleName(CLASS_POINTER);
        exchView.add(this.army2Img, 445, 171);
        this.army2Img.setSize(SIZE_22PX, SIZE_20PX);

        final Label label_1 = new Label("Corps:");
        label_1.setStyleName("whiteText");
        exchView.add(label_1, 468, 171);
        label_1.setSize("31px", "15px");

        this.corp2Img = new UnitImage("http://static.eaw1805.com/images/buttons/gotoNA.png", null);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!corp2Img.getUrl().endsWith("NA.png")) {
                    final SelectCorpPopup selCorp = new SelectCorpPopup(armiesList, mySelf, 2, selArmySecond);
                    selCorp.showRelativeTo(exchView);
                    selCorp.setPopupPosition(selCorp.getAbsoluteLeft(), selCorp.getAbsoluteTop() + 252 + 194);

                }
            }
        }).addToElement(corp2Img.getElement()).register();

        this.corp2Img.setStyleName(CLASS_POINTER);
        exchView.add(this.corp2Img, 505, 171);
        this.corp2Img.setSize(SIZE_22PX, SIZE_20PX);

        final Label label_2 = new Label("Brigade:");
        label_2.setStyleName("whiteText");
        exchView.add(label_2, 527, 171);
        label_2.setSize("48px", "15px");

        this.brig2Img = new UnitImage("http://static.eaw1805.com/images/buttons/gotoNA.png", null);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!brig2Img.getUrl().endsWith("NA.png")) {
                    final SelectBrigadePopup selBrig = new SelectBrigadePopup(armiesList, mySelf, 2, selCorpSecond);
                    selBrig.showRelativeTo(exchView);
                    selBrig.setPopupPosition(selBrig.getAbsoluteLeft(), selBrig.getAbsoluteTop() + 252 + 194);

                }
            }
        }).addToElement(brig2Img.getElement()).register();

        this.brig2Img.setStyleName(CLASS_POINTER);
        exchView.add(this.brig2Img, 581, 171);
        this.brig2Img.setSize(SIZE_22PX, SIZE_20PX);

        final Label lblArmy = new Label("Army:");
        exchangeBasePanel.add(lblArmy, 423, 102);

        final Label lblCorp = new Label("Corps:");
        exchangeBasePanel.add(lblCorp, 487, 102);

        final Label lblBrigade = new Label("Brigade:");
        exchangeBasePanel.add(lblBrigade, 546, 102);

        this.army1Img = new UnitImage("http://static.eaw1805.com/images/buttons/goto.png", null);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!army1Img.getUrl().endsWith("NA.png")) {
                    final SelectArmyPopup selArmy = new SelectArmyPopup(armiesList, mySelf, 1);
                    selArmy.showRelativeTo(exchView);
                    selArmy.setPopupPosition(selArmy.getAbsoluteLeft(), selArmy.getAbsoluteTop() + 252);

                }
            }
        }).addToElement(army1Img.getElement()).register();

        this.army1Img.setStyleName(CLASS_POINTER);
        exchangeBasePanel.add(this.army1Img, 464, 102);
        this.army1Img.setSize(SIZE_22PX, SIZE_20PX);

        this.corp1Img = new UnitImage("http://static.eaw1805.com/images/buttons/gotoNA.png", null);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!corp1Img.getUrl().endsWith("NA.png")) {
                    final SelectCorpPopup selCorp = new SelectCorpPopup(armiesList, mySelf, 1, selArmyFirst);
                    selCorp.showRelativeTo(exchView);
                    selCorp.setPopupPosition(selCorp.getAbsoluteLeft(), selCorp.getAbsoluteTop() + 252);

                }
            }
        }).addToElement(corp1Img.getElement()).register();

        this.corp1Img.setStyleName(CLASS_POINTER);
        exchangeBasePanel.add(this.corp1Img, 524, 102);
        this.corp1Img.setSize(SIZE_22PX, SIZE_20PX);

        this.brig1Img = new UnitImage("http://static.eaw1805.com/images/buttons/gotoNA.png", null);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!brig1Img.getUrl().endsWith("NA.png")) {
                    final SelectBrigadePopup selBrig = new SelectBrigadePopup(armiesList, mySelf, 1, selCorpFirst);
                    selBrig.showRelativeTo(exchView);
                    selBrig.setPopupPosition(selBrig.getAbsoluteLeft(), selBrig.getAbsoluteTop() + 252);

                }
            }
        }).addToElement(brig1Img.getElement()).register();

        this.brig1Img.setStyleName(CLASS_POINTER);
        exchangeBasePanel.add(this.brig1Img, 600, 102);
        this.brig1Img.setSize(SIZE_22PX, SIZE_20PX);
    }


    public boolean setSelUnit(final int order, final int type, final int unitSel, final UnitImage unitImg) {
        switch (type) {
            case ARMY:
                if (order == 1) {
                    deselect1Img();
                    army1Img.setUrl(unitImg.getUrl());
                    army1Img.addPopupInfoToImage(unitImg.getPopupInfo());
                    selArmyFirst = unitSel;
                    exchView.populatePanel(1, selArmyFirst, type);
                } else if (order == 2) {
                    deselect2Img();
                    army2Img.setUrl(unitImg.getUrl());
                    army2Img.addPopupInfoToImage(unitImg.getPopupInfo());
                    selArmySecond = unitSel;
                    exchView.populatePanel(2, selArmySecond, type);
                }
                break;
            case CORPS:
                if (order == 1) {
                    deselect1Brig();
                    corp1Img.setUrl(unitImg.getUrl());
                    corp1Img.addPopupInfoToImage(unitImg.getPopupInfo());
                    selCorpFirst = unitSel;
                    exchView.populatePanel(1, selCorpFirst, type);
                } else if (order == 2) {
                    deselect2Brig();
                    corp2Img.setUrl(unitImg.getUrl());
                    corp2Img.addPopupInfoToImage(unitImg.getPopupInfo());
                    selCorpSecond = unitSel;
                    exchView.populatePanel(2, selCorpSecond, type);
                }
                break;
            case BRIGADE:
                if (order == 1) {
                    brig1Img.setUrl(unitImg.getUrl());
                    brig1Img.addPopupInfoToImage(unitImg.getPopupInfo());
                    selBrigFirst = unitSel;
                    exchView.populatePanel(1, selBrigFirst, type);
                } else if (order == 2) {
                    brig2Img.setUrl(unitImg.getUrl());
                    brig2Img.addPopupInfoToImage(unitImg.getPopupInfo());
                    selBrigSecond = unitSel;
                    exchView.populatePanel(2, selBrigSecond, type);
                }
                break;
            default:
                return false;
        }
        checkEnableSecondScrollPanel();
        return true;
    }

    private void checkEnableSecondScrollPanel() {
        if (selBrigFirst != selBrigSecond && selBrigFirst != -1 && selBrigSecond != -1) {
            exchView.setEnabled(true, selBrigFirst, selBrigSecond, BRIGADE);
        } else if (selCorpFirst != selCorpSecond && selBrigFirst == -1 && selBrigSecond == -1 && selCorpFirst != -1 && selCorpSecond != -1) {
            exchView.setEnabled(true, selCorpFirst, selCorpSecond, CORPS);
        } else if (selArmyFirst != selArmySecond && selCorpFirst == -1 && selCorpSecond == -1 && selArmyFirst != -1 && selArmySecond != -1) {
            exchView.setEnabled(true, selArmyFirst, selArmySecond, ARMY);
        } else {
            exchView.setEnabled(false, 0, 0, -1);
        }
    }


    private void deselect1Img() {
        corp1Img.setUrl("http://static.eaw1805.com/images/buttons/goto.png");
        brig1Img.setUrl("http://static.eaw1805.com/images/buttons/gotoNA.png");
        selCorpFirst = -1;
        selBrigFirst = -1;
    }

    private void deselect2Img() {
        corp2Img.setUrl("http://static.eaw1805.com/images/buttons/goto.png");
        brig2Img.setUrl("http://static.eaw1805.com/images/buttons/gotoNA.png");
        selCorpSecond = -1;
        selBrigSecond = -1;
    }

    private void deselect1Brig() {
        brig1Img.setUrl("http://static.eaw1805.com/images/buttons/goto.png");
        selBrigFirst = -1;

    }

    private void deselect2Brig() {
        brig2Img.setUrl("http://static.eaw1805.com/images/buttons/goto.png");
        selBrigSecond = -1;

    }
}

