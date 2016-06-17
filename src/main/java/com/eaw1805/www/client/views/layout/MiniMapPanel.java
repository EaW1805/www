package com.eaw1805.www.client.views.layout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.events.GameProcessDateReportEvent;
import com.eaw1805.www.client.events.GameProcessDateReportHandler;
import com.eaw1805.www.client.events.GameStartedEvent;
import com.eaw1805.www.client.events.GameStartedHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.SettingsView;
import com.eaw1805.www.client.widgets.CountdownLabel;
import com.eaw1805.www.client.widgets.DrawingAreaWC;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * The minimap panel that appears on the top-left of the window.
 */
public class MiniMapPanel
        extends HorizontalPanel implements StyleConstants {

    public final static int SELECTOR_WIDTH = 20;

    public final static int SELECTOR_HEIGHT = 16;

    private transient final DrawingArea drawArea = new DrawingArea(8, 40);

    private Rectangle rect;

    private AbsolutePanel mapActionsPanel;

    private final ImageButton settingsImg = new ImageButton();

    private final SettingsView settingsPanel;
    private ImageButton imageButton;
    private final ImageButton geograImg, politicImg, zoomInImg, zoomOutImg;
    private boolean mouseDown = false;

    private final RegionSelectionView regionSelectionPanel = new RegionSelectionView();

    /**
     * Default constructor.
     *
     * @param parent the panel that uses the MiniMap.
     */
    public MiniMapPanel(final DrawingAreaWC parent) {
        setSize("261px", SIZE_291PX);
        setStyleName("minimapPanel");
        initDrawArea(parent);
        initTogglePanel();
        settingsPanel = new SettingsView(settingsImg);

        final Image mimapLeftImg = new Image("http://static.eaw1805.com/images/layout/minimapLeft.png");
        this.mapActionsPanel.add(mimapLeftImg, 8, 52);
        mimapLeftImg.setSize("16px", "134px");

        final Image minimapRightImg = new Image("http://static.eaw1805.com/images/layout/minimapRight.png");
        this.mapActionsPanel.add(minimapRightImg, 166, 52);
        minimapRightImg.setSize("16px", "134px");

        final Image mimimapDownImg = new Image("http://static.eaw1805.com/images/layout/minimapDown.png");
        this.mapActionsPanel.add(mimimapDownImg, 8, 186);
        mimimapDownImg.setSize("174px", "17px");

        final Image minimapUpImg = new Image("http://static.eaw1805.com/images/layout/minimapUp.png");
        minimapUpImg.setSize("174px", "17px");
        mapActionsPanel.add(minimapUpImg, 8, 35);

        geograImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButGeographicalMapOff.png");
        geograImg.setStyleName("pointer");
        geograImg.setTitle("Display the Geographical Map");
        geograImg.setSize("20px", "53px");
        geograImg.setSelected(!GameStore.getInstance().isShowPolitical());
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!GameStore.getInstance().isShowPolitical()) {
                    return;
                }

                GameStore.getInstance().setShowPolitical(false);

                politicImg.deselect();
                geograImg.setSelected(true);

                final MapStore mapStore = MapStore.getInstance();
                final int regionId = mapStore.getActiveRegion();
                mapStore.getMapsView().constructMap(regionId, false);
            }
        }).addToElement(geograImg.getElement()).register();

        mapActionsPanel.add(geograImg, 182, 36);

        politicImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButPoliticalMapOn.png");
        politicImg.setStyleName("pointer");
        politicImg.setSize("20px", "53px");
        politicImg.setTitle("Display the Political Map");
        politicImg.setSelected(GameStore.getInstance().isShowPolitical());
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (GameStore.getInstance().isShowPolitical()) {
                    return;
                }

                GameStore.getInstance().setShowPolitical(true);

                politicImg.setSelected(true);
                geograImg.deselect();

                final MapStore mapStore = MapStore.getInstance();
                final int regionId = mapStore.getActiveRegion();
                mapStore.getMapsView().constructMap(regionId, false);
            }
        }).addToElement(politicImg.getElement()).register();
        mapActionsPanel.add(politicImg, 182, 88);

        zoomInImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomInOff.png");
        zoomInImg.setStyleName("pointer");
        zoomInImg.setTitle("Increase zoom for the Political Map");
        zoomInImg.setSize(SIZE_26PX, SIZE_23PX);
        zoomInImg.setSelected(false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                zoomInImg.deselect();

                if (!GameStore.getInstance().isShowPolitical()) {
                    return;
                }

                MapStore.getInstance().zoom(true);
            }
        }).addToElement(zoomInImg.getElement()).register();
        mapActionsPanel.add(zoomInImg, 134, 205);

        zoomOutImg = new ImageButton("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButZoomOutOff.png");
        zoomOutImg.setStyleName("pointer");
        zoomOutImg.setTitle("Reduce zoom for the Political Map");
        zoomOutImg.setSize(SIZE_26PX, SIZE_23PX);
        zoomOutImg.setSelected(false);
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                zoomOutImg.deselect();

                if (!GameStore.getInstance().isShowPolitical()) {
                    return;
                }

                MapStore.getInstance().zoom(false);
            }
        }).addToElement(zoomOutImg.getElement()).register();
        mapActionsPanel.add(zoomOutImg, 155, 205);
    }


    private void initDrawArea(final DrawingAreaWC parent) {
        mapActionsPanel = new AbsolutePanel();
        mapActionsPanel.setStyleName("minimapPanel");
        mapActionsPanel.setSize("204px", SIZE_291PX);
        add(mapActionsPanel);

        final VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setSize("169px", "157px");
        mapActionsPanel.add(verticalPanel, 10, 40);

        drawArea.setSize("165px", "154px");
        verticalPanel.add(drawArea);

        drawArea.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(final MouseMoveEvent event) {
                if (mouseDown) {
                    repositionMap(event.getX(), event.getY(), parent);
                }
            }
        });

        drawArea.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                repositionMap(event.getX(), event.getY(), parent);
                mouseDown = true;
            }
        });

        drawArea.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(final MouseUpEvent event) {
                mouseDown = false;
            }
        });

        drawArea.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(final MouseOutEvent event) {
                mouseDown = false;
            }
        });

        mapActionsPanel.add(regionSelectionPanel, 10, 202);
        regionSelectionPanel.setSize("116px", "29px");

        if (HibernateUtil.DB_S3 == GameStore.getInstance().getScenarioId()) {
            // Scenario 1808 has only 1 region
            regionSelectionPanel.setDisabledRegionButtons(RegionConstants.AFRICA, RegionConstants.CARIBBEAN, RegionConstants.INDIES);
        }

        this.settingsImg.setUrl("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButPreferencesOff.png");
        this.mapActionsPanel.add(this.settingsImg, 153, 238);
        this.settingsImg.setTitle("Review Display Options");
        this.settingsImg.setSize("21px", "36px");

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                GameStore.getInstance().getLayoutView()
                        .addWidgetToLayoutPanelEAW(settingsPanel);
                GameStore.getInstance().getLayoutView().positionTocCenter(settingsPanel);
            }
        }).addToElement(settingsImg.getElement()).register();

        final AbsolutePanel absolutePanel = new AbsolutePanel();
        add(absolutePanel);
        absolutePanel.setSize("57px", SIZE_291PX);

        this.imageButton = new ImageButton("http://static.eaw1805.com/images/layout/buttons/ButPanelSwitchOff.png");
        this.imageButton.setStyleName("pointer");
        this.imageButton.setTitle("Minimize mini map");
        absolutePanel.add(this.imageButton, 3, 35);
        this.imageButton.setSize("24px", "24px");


        this.imageButton.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                toggleMiniMap();
                imageButton.deselect();
            }
        });

        GameEventManager.addGameStartedHandler(new GameStartedHandler() {
            public void onGameStarted(final GameStartedEvent event) {
                final com.google.gwt.user.client.ui.Image flagImg = new com.google.gwt.user.client.ui.Image("http://static.eaw1805.com/images/nations/nation-" + GameStore.getInstance().getNationId() + ".png");
                flagImg.setSize("44px", "30px");
                mapActionsPanel.add(flagImg, 11, 240);

                final Label lblGame = new Label("Game: " + GameStore.getInstance().getGameId());
                lblGame.setSize("77px", "10px");
                mapActionsPanel.add(lblGame, 66, 239);

            }
        });

        GameEventManager.addGameProcessDateReportHandler(new GameProcessDateReportHandler() {
            public void onGameProccesDateReport(final GameProcessDateReportEvent event) {
                final CountdownLabel lblTimer = new CountdownLabel(event.getProcDate());
                lblTimer.setSize("89px", "13px");
                mapActionsPanel.add(lblTimer, 66, 258);
            }
        });

    }

    public final void toggleMiniMap() {
        final MiniMapPanel self = this;
        final LayoutPanel animatedParent = (LayoutPanel) self.getParent();
        if (GameStore.getInstance().isShowMinimap()) {
            animatedParent.setWidgetRightWidth(self, 261 - 57, Style.Unit.PX, 261, Style.Unit.PX);
            animatedParent.animate(1000, new Layout.AnimationCallback() {

                public void onAnimationComplete() {
                    animatedParent.setSize("57px", SIZE_291PX);
                    animatedParent.setWidgetRightWidth(self, 0, Style.Unit.PX, 261, Style.Unit.PX);
                    animatedParent.forceLayout();
                }

                public void onLayout(final Layout.Layer layer, final double v) {
                    //nothing to do here really.
                }
            });
            GameStore.getInstance().setShowMinimap(false);
            imageButton.setTitle("Maximize mini map");

        } else {

            animatedParent.setWidgetRightWidth(self, 261 - 57, Style.Unit.PX, 261, Style.Unit.PX);
            animatedParent.forceLayout();
            animatedParent.setSize("261px", SIZE_291PX);

            animatedParent.setWidgetRightWidth(self, 0, Style.Unit.PX, 261, Style.Unit.PX);
            animatedParent.animate(1000);
            GameStore.getInstance().setShowMinimap(true);
            imageButton.setTitle("Minimize mini map");
        }

    }

    private void repositionMap(final int thisX, final int thisY, final DrawingAreaWC parent) {
        if ((thisX - (rect.getWidth() / 2) > 0)
                && (thisX - (rect.getWidth() / 2) + rect.getWidth()) < drawArea.getWidth()) {
            rect.setX(thisX - (rect.getWidth() / 2));

        } else if ((thisX - (rect.getWidth() / 2) < 0)) {
            rect.setX(0);

        } else {
            rect.setX(drawArea.getWidth() - (rect.getWidth()));
        }

        if ((thisY - (rect.getHeight() / 2) > 0)
                && (thisY - (rect.getHeight() / 2) + rect.getHeight()) < drawArea.getHeight()) {
            rect.setY(thisY - (rect.getHeight() / 2));

        } else if ((thisY - (rect.getHeight() / 2) < 0)) {
            rect.setY(0);

        } else {
            rect.setY(drawArea.getHeight() - (rect.getHeight()));
        }

        final double mapToMiniMapX = (parent.getWidth() * (MapStore.getInstance().getZoomLevelSettings())) / (double) drawArea.getWidth();
        final double mapToMiniMapY = (parent.getHeight() * (MapStore.getInstance().getZoomLevelSettings())) / (double) drawArea.getHeight();

        parent.getMapScrollPanel().setHorizontalScrollPosition((int) (rect.getX() * mapToMiniMapX) + (int) MapStore.getInstance().getZoomOffsetX());
        parent.getMapScrollPanel().setVerticalScrollPosition((int) (rect.getY() * mapToMiniMapY) + (int) MapStore.getInstance().getZoomOffsetY());
        MapStore.getInstance()
                .getMapsView()
                .setLastKnownPosition(
                        MapStore.getInstance().getActiveRegion(),
                        parent.getMapScrollPanel().getHorizontalScrollPosition(),
                        parent.getMapScrollPanel().getVerticalScrollPosition(),
                        rect.getX(), rect.getY());
    }

    private void initTogglePanel() {
        //do nothing here for now
    }

    public void clearDrawArea() {
        drawArea.clear();
    }

    public void setSize(final int width, final int height) {
        drawArea.setHeight(height);
        drawArea.setWidth(width);
    }

    public int getWidth() {
        return drawArea.getWidth();
    }

    public int getHeight() {
        return drawArea.getHeight();
    }

    public void setImage(final Group thisGroup) {
        drawArea.add(thisGroup);
        if (((Group) drawArea.getVectorObject(0)).getVectorObjectCount() >= 2) {
            // Update rectangle
            rect = (Rectangle) ((Group) drawArea.getVectorObject(0)).getVectorObject(2);
            if (politicImg.getUrl().endsWith("On.png")) {
                ((Group) drawArea.getVectorObject(0)).getVectorObject(1).setVisible(true);
            } else {
                ((Group) drawArea.getVectorObject(0)).getVectorObject(1).setVisible(false);

            }
        }
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public SettingsView getSettingsPanel() {
        return settingsPanel;
    }

    public RegionSelectionView getRegionSelectionPanel() {
        return regionSelectionPanel;
    }
}

