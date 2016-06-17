package com.eaw1805.www.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.www.client.ClientConstants;
import com.eaw1805.www.client.EmpireWebClient;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.events.GameReportInProcessEvent;
import com.eaw1805.www.client.events.GameReportInProcessHandler;
import com.eaw1805.www.client.events.loading.*;
import com.eaw1805.www.client.events.map.MapEventManager;
import com.eaw1805.www.client.events.map.TileImagesLoadedEvent;
import com.eaw1805.www.client.events.map.TileImagesLoadedHandler;
import com.eaw1805.www.shared.stores.GameStore;
import org.vaadin.gwtgraphics.client.DrawingArea;

import java.util.Date;
import java.util.Random;

public class LoadingView extends Composite {

    public final EmpireWebClient webClient;

    private final org.vaadin.gwtgraphics.client.Image loadingImages[] = new org.vaadin.gwtgraphics.client.Image[20];

    private final Label lblNowLoading;
    private final Label buildNumber;
    private final VerticalPanel pnlMain;
    private final DrawingArea loadingBar = new DrawingArea(200, 30);
    private boolean hasLoadedData = false;

    private int stepNo = 1;

    public LoadingView(final EmpireWebClient parent) {
        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(final ResizeEvent event) {
                LoadingView.this.pnlMain.setSize(event.getWidth() + "px", event.getHeight() + "px");
            }
        });
        ClientConstants myConstants = GWT.create(ClientConstants.class);
        webClient = parent;
        pnlMain = new VerticalPanel();
        pnlMain.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        pnlMain.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        pnlMain.getElement().getStyle().setBackgroundColor("black");
        pnlMain.setStyleName("loginPanel");
        initWidget(pnlMain);
        pnlMain.setSize(Window.getClientWidth() + "px", Window.getClientHeight() + "px");

        final AbsolutePanel loginPanel = new AbsolutePanel();
        loginPanel.setStyleName((String) null);
        pnlMain.add(loginPanel);
        loginPanel.setSize("570px", "550px");

        final Image imgLogo = new Image("http://static.eaw1805.com/images/loading/LoadingScreen.png");
        imgLogo.setSize("1440px", "900px");
        loginPanel.add(imgLogo, -467, -201);

        loginPanel.add(loadingBar, 68, 279);
        loadingBar.setSize("432px", "30px");

        lblNowLoading = new Label("Now loading...");
        lblNowLoading.setStyleName("clearFont");
        loginPanel.add(lblNowLoading, 68, 315);

        buildNumber = new Label("b" + myConstants.buildNumber());
        loginPanel.add(buildNumber, 460, 315);

        loadingImages[1] = new org.vaadin.gwtgraphics.client.Image(6, 6, 35, 20, "http://static.eaw1805.com/images/loading/loadingBar1.png");
        loadingImages[2] = new org.vaadin.gwtgraphics.client.Image(41, 6, 33, 20, "http://static.eaw1805.com/images/loading/loadingBar2.png");
        loadingImages[3] = new org.vaadin.gwtgraphics.client.Image(74, 6, 29, 20, "http://static.eaw1805.com/images/loading/loadingBar3.png");
        loadingImages[4] = new org.vaadin.gwtgraphics.client.Image(103, 6, 27, 20, "http://static.eaw1805.com/images/loading/loadingBar4.png");
        loadingImages[5] = new org.vaadin.gwtgraphics.client.Image(130, 6, 40, 20, "http://static.eaw1805.com/images/loading/loadingBar5.png");
        loadingImages[6] = new org.vaadin.gwtgraphics.client.Image(170, 6, 38, 20, "http://static.eaw1805.com/images/loading/loadingBar6.png");
        loadingImages[7] = new org.vaadin.gwtgraphics.client.Image(208, 6, 47, 20, "http://static.eaw1805.com/images/loading/loadingBar7.png");
        loadingImages[8] = new org.vaadin.gwtgraphics.client.Image(254, 6, 46, 20, "http://static.eaw1805.com/images/loading/loadingBar8.png");
        loadingImages[9] = new org.vaadin.gwtgraphics.client.Image(300, 6, 38, 20, "http://static.eaw1805.com/images/loading/loadingBar9.png");
        loadingImages[10] = new org.vaadin.gwtgraphics.client.Image(338, 6, 40, 20, "http://static.eaw1805.com/images/loading/loadingBar10.png");
        loadingImages[11] = new org.vaadin.gwtgraphics.client.Image(378, 6, 47, 20, "http://static.eaw1805.com/images/loading/loadingBar11.png");

        //add random quote
        final Random random = new Random();
        random.setSeed(new Date().getTime());
        int rand = random.nextInt(myConstants.quotesMap().size()) + 1;
        final Label quoteLabel = new Label(myConstants.quotesMap().get("q" + rand));
        quoteLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        quoteLabel.setWidth("415px");
        quoteLabel.setStyleName("quoteText");

        final Label personalityLabel = new Label(myConstants.personalitiesMap().get("p" + rand));
        personalityLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        personalityLabel.setWidth("415px");
        personalityLabel.setStyleName("quoteText");

        final AbsolutePanel quotePanel = new AbsolutePanel();
        quotePanel.setSize("464px", "155px");
        quotePanel.setStyleName("tutorialInfoPanel");
        quotePanel.add(quoteLabel, 23, 9);

        quotePanel.add(personalityLabel, 23, 129);
        loginPanel.add(quotePanel, 52, 364);


        LoadEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
            public void onArmiesLoaded(final ArmiesLoadedEvent event) {
                nextLoadingStep("Loading land forces ...");
            }
        });
        LoadEventManager.addBtrainLoadedHandler(new BtrainLoadedHandler() {
            public void onBtrainLoaded(final BtrainLoadedEvent event) {
                nextLoadingStep("Loading baggage trains ...");
            }
        });
        LoadEventManager.addCommLoadeddHandler(new CommLoadedHandler() {
            public void onCommLoaded(final CommLoadedEvent event) {
                nextLoadingStep("Loading commanders ...");
            }
        });
        LoadEventManager.addAlliedUnitsLoadedHandler(new AlliedUnitsLoadedHandler() {
            public void onAlliedUnitsLoaded(final AlliedUnitsLoadedEvent event) {
                nextLoadingStep("Loading allied units ...");
            }
        });
        LoadEventManager.addForeignUnitsLoadedHandler(new ForeignUnitsLoadedHandler() {
            public void onForeignUnitsLoaded(final ForeignUnitsLoadedEvent event) {
                nextLoadingStep("Loading foreign units ...");
            }
        });
        LoadEventManager.addOrdersLoadedHandler(new OrdersLoadedHandler() {
            public void onOrdersLoaded(final OrdersLoadedEvent event) {
                nextLoadingStep("Loading orders ...");
            }
        });
        LoadEventManager.addProSiteLoadedHandler(new ProSiteLoadedHandler() {
            public void onProSiteLoaded(final ProSiteLoadedEvent event) {
                nextLoadingStep("Loading production sites ...");
            }
        });
        LoadEventManager.addRegionLoadedHandler(new RegionLoadedHandler() {
            public void onRegionLoaded(final RegionLoadedEvent event) {
                nextLoadingStep("Loading regional information ...");
            }
        });
        LoadEventManager.addSectorsLoadedHandler(new SectorsLoadedHandler() {
            public void onSectorsLoaded(final SectorsLoadedEvent event) {
                nextLoadingStep("Loading sectors ...");
            }
        });
        LoadEventManager.addSpiesLoadedHandler(new SpiesLoadedHandler() {
            public void onSpiesLoaded(final SpiesLoadedEvent event) {
                nextLoadingStep("Loading spies ...");
            }
        });
        MapEventManager.addTileImagesLoadedHandler(new TileImagesLoadedHandler() {
            public void onTileImagesLoaded(final TileImagesLoadedEvent event) {
                nextLoadingStep("Loading complete!");

            }
        });
        loadingBar.clear();
        nextLoadingStep("Now loading ...");

        GameEventManager.addGameReportInProccesHandler(new GameReportInProcessHandler() {
            public void onGameInProcces(final GameReportInProcessEvent event) {
                if (!hasLoadedData) {
                    hasLoadedData = true;
                    if (event.getInProcces()) {
                        nextLoadingStep("Game is currently in process or connection to server is lost.");

                    } else {
                        try {
                            webClient.initViews();
                        } catch (Exception ex) {
                            if (GameStore.getInstance().getGameId() == 0) {
                                nextLoadingStep("Failed to get game data. Game number is not set.");

                            } else {
                                nextLoadingStep("Failed to get game data. Game number is set to:" + GameStore.getInstance().getGameId());
                            }
                        }
                    }
                }
            }
        });
    }

    public final void nextLoadingStep(final String nowLoadingText) {
        lblNowLoading.setText(nowLoadingText);
        if (stepNo < 12) {
            loadingBar.add(loadingImages[stepNo]);

            stepNo++;
        }
        if (stepNo == 12) {
            lblNowLoading.setText("Data loaded, initializing game ...");
            stepNo++;
        }
    }
}
