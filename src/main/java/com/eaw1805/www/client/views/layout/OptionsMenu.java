package com.eaw1805.www.client.views.layout;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.NewsletterView;
import com.eaw1805.www.client.views.RelationsView;
import com.eaw1805.www.client.views.SpyReportsView;
import com.eaw1805.www.client.views.TaxationView;
import com.eaw1805.www.client.views.TradePanelView;
import com.eaw1805.www.client.views.frames.BattleFrame;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.StaticWidgets;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.map.MapStore;

public class OptionsMenu
        extends Composite
        implements OrderConstants, ArmyConstants, TradeInfoViewInterface, StyleConstants {

    private final TaxationView taxView;
    private final ImageButton relImage;

    private final ImageButton taxationImg;

    private final ImageButton newsImg;
    private final NewsletterView newsView;
    private final ImageButton tradeImg;
    private BattleFrame ecoFrame;
    private BattleFrame newsFrame;

    public OptionsMenu() {

        newsView = new NewsletterView();
        taxView = new TaxationView();

        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("optionsPanel");
        initWidget(absolutePanel);
        absolutePanel.setSize("282px", "93px");

        final ImageButton spyReportsImg = StaticWidgets.SPYREPORT_IMAGE_OPTIONS;
        spyReportsImg.setTitle("Review spy reports");
        spyReportsImg.setStyleName(CLASS_POINTER);
        spyReportsImg.setSize(SIZE_57PX, SIZE_23PX);
        absolutePanel.add(spyReportsImg, 0, 39);

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                final SpyReportsView spyReportsView = new SpyReportsView(DataStore.getInstance().getNations().get(0));
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(spyReportsView);
                GameStore.getInstance().getLayoutView()
                        .positionTocCenter(spyReportsView);
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 5) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(spyReportsImg.getElement()).register();

        final ImageButton embarkDis = StaticWidgets.EMBARK_IMAGE_OPTIONS;
        embarkDis.setStyleName(CLASS_POINTER);
        embarkDis.setTitle("Embark-disembark troops");
        embarkDis.setSize(SIZE_57PX, SIZE_23PX);
        absolutePanel.add(embarkDis, 0, 64);

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Your empire is dead, you cannot deploy troops in transport units.", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, you cannot deploy troops in transport units.", false);

                } else {
                    final DeployTroopsView dpView = new DeployTroopsView(MapStore.getInstance().getActiveRegion(), 0, 0);
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(dpView);

                }
            }
        }).addToElement(embarkDis.getElement()).register();

        relImage = StaticWidgets.RELATION_IMAGE_OPTIONS;
        relImage.setTitle("Review & Adjust Political Relations");
        relImage.setStyleName(CLASS_POINTER);
        relImage.setSize(SIZE_57PX, SIZE_23PX);

        if (HibernateUtil.DB_S3 == GameStore.getInstance().getScenarioId()) {
            // Scenario 1808 has fixed political relations
            relImage.setDisabled(true);

        } else {
            absolutePanel.add(this.relImage, 60, 39);

            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    final RelationsView relView = new RelationsView();
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(relView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(relView);
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getMonth() == 10
                            && TutorialStore.getInstance().getTutorialStep() == 2) {
                        TutorialStore.nextStep(false);
                    }
                }
            }).addToElement(relImage.getElement()).register();
        }

        this.taxationImg = StaticWidgets.TAXATION_IMAGE_OPTIONS;
        this.taxationImg.setTitle("Adjust Taxation");
        this.taxationImg.setStyleName(CLASS_POINTER);
        this.taxationImg.setSize(SIZE_57PX, SIZE_23PX);
        absolutePanel.add(this.taxationImg, 180, 64);

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(taxView);
                GameStore.getInstance().getLayoutView().positionTocCenter(taxView);
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 4
                        && TutorialStore.getInstance().getTutorialStep() == 1) {
                    TutorialStore.nextStep(false);
                }
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 16) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(taxationImg.getElement()).register();


        final ImageButton economyImg = StaticWidgets.ECONOMIC_IMAGE_OPTIONS;
        economyImg.setTitle("Review your Empire's Economic Report");
        economyImg.setStyleName(CLASS_POINTER);
        economyImg.setSize(SIZE_57PX, SIZE_23PX);
        absolutePanel.add(economyImg, 60, 64);

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                Window.open("http://www.eaw1805.com/report/scenario/" + GameStore.getInstance().getScenarioStr() + "/game/" + GameStore.getInstance().getGameId() + "/nation/" + GameStore.getInstance().getNationId() + "/overview", "_blank", "");
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 4
                        && TutorialStore.getInstance().getTutorialStep() == 3) {
                    TutorialStore.nextStep(false);
                }
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 4
                        && TutorialStore.getInstance().getTutorialStep() == 4) {
                    TutorialStore.nextStep(false);
                }
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 7) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(economyImg.getElement()).register();


        this.newsImg = StaticWidgets.NEWS_IMAGE_OPTIONS;
        this.newsImg.setTitle("Read This month's Newsletter");
        this.newsImg.setStyleName(CLASS_POINTER);
        absolutePanel.add(this.newsImg, 180, 39);
        this.newsImg.setSize(SIZE_57PX, SIZE_23PX);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (newsFrame == null) {
                    newsFrame = new BattleFrame(0, false, false, true, null, false);
                }

                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(newsFrame);
                GameStore.getInstance().getLayoutView().positionTocCenter(newsFrame);
            }
        }).addToElement(newsImg.getElement()).register();

        tradeImg = StaticWidgets.TRADE_IMAGE_OPTIONS;
        tradeImg.setTitle("Perform Trade");
        tradeImg.setStyleName(CLASS_POINTER);
        tradeImg.setSize(SIZE_57PX, SIZE_23PX);
        absolutePanel.add(tradeImg, 120, 64);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(new TradePanelView(null, OptionsMenu.this, TRADECITY));
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 8) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(tradeImg.getElement()).register();

        final ImageButton messageImg = StaticWidgets.MESSAGE_IMAGE_OPTIONS;
        messageImg.setTitle("Send Newsletters and Private Messages");
        messageImg.setStyleName(CLASS_POINTER);
        messageImg.setSize(SIZE_57PX, SIZE_23PX);
        absolutePanel.add(messageImg, 120, 39);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (GameStore.getInstance().isNationDead()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "A dead leader cannot write a newsletter.", false);

                } else if (GameStore.getInstance().isGameEnded()) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "The game has ended, cannot write a newsletter.", false);

                } else {
                    GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(newsView);
                    GameStore.getInstance().getLayoutView().positionTocCenter(newsView);

                }
            }
        }).addToElement(messageImg.getElement()).register();

    }

    /**
     * @return the relImage
     */
    public ImageButton getRelImage() {
        return relImage;
    }

    /**
     * @return the taxationImg
     */
    public ImageButton getTaxationImg() {
        return taxationImg;
    }

    /**
     * @return the taxView
     */
    public TaxationView getTaxView() {
        return taxView;
    }

    /**
     * @return the newsImg
     */
    public ImageButton getNewsImg() {
        return newsImg;
    }


    public void closeTradePanel() {
        this.tradeImg.deselect();
        this.tradeImg.setUrl(this.tradeImg.getUrl().replace("On", "Off"));
    }

    public NewsletterView getNewsView() {
        return newsView;
    }
}
