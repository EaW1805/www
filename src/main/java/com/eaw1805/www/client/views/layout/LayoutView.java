package com.eaw1805.www.client.views.layout;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.www.client.views.GlobalIteratorView;
import com.eaw1805.www.client.views.LoadingGraphicsPanel;
import com.eaw1805.www.client.views.extras.AdmAndCommPtsPanel;
import com.eaw1805.www.client.views.popups.NotificationPopup;
import com.eaw1805.www.client.views.tutorial.ProductionSitesCounterPanel;
import com.eaw1805.www.client.views.tutorial.TutorialInfoPanel;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.SpeedTest;
import com.eaw1805.www.client.widgets.WindowPanelEAW;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.InfoPanelsStore;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

public class LayoutView extends Composite implements RegionConstants {

    /**
     * The area where the map will be shown
     */
    private final AbsolutePanel base = new AbsolutePanel();

    /**
     * The map area of our client
     */
    private final MapsView map;


    /**
     * The economic values panel and extras
     */
    private final EconomyView economyView;
    private final DateView datePanel;
    //    private RegionSelectionView regionSelectionPanel = new RegionSelectionView();
    private final SaveView unitsMenu = new SaveView();
    private final SectorMenu sectorMenu = new SectorMenu();
    private final OptionsMenu optionsMenu = new OptionsMenu();
    private final OrdersMiniView ordersMiniView = new OrdersMiniView();
    private final ChatView chatView = new ChatView();
    private final FocusPanel fpanel;
    private final AbsolutePanel unitsMenuContainer;
    private final NotificationPopup notificationPopup = new NotificationPopup();
    private final GlobalIteratorView gIterView = new GlobalIteratorView();
    private final SpeedTest speedTest = new SpeedTest();
    private final TutorialInfoPanel tutorial = new TutorialInfoPanel();
    private final AbsolutePanel freezeLayoutUp = new AbsolutePanel();
    private final AbsolutePanel freezeLayoutDown = new AbsolutePanel();
    private final AbsolutePanel freezeLayoutRight = new AbsolutePanel();
    private final AbsolutePanel freezeLayoutLeft = new AbsolutePanel();
    private final AbsolutePanel highlightAnim = new AbsolutePanel();
    private LoadingGraphicsPanel loadingGraphicsPanel;
    final Fade f;
    int countUpDown = 0;
    private final static int lowOpacity = 0;
    private final static int highOpacity = 50;

    public LayoutView() {
        fpanel = new FocusPanel();
        initWidget(fpanel);
        fpanel.add(base);
        map = new MapsView();

        fpanel.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(final MouseDownEvent event) {
                //if there is any persistent popup... close it
                if (InfoPanelsStore.getInstance().getUnitPopup() != null) {
                    InfoPanelsStore.getInstance().getUnitPopup().hide();
                    InfoPanelsStore.getInstance().setInfoPanelLocked(false);
                    InfoPanelsStore.getInstance().setUnitPopup(null);
                }
                event.preventDefault();
                event.stopPropagation();
            }
        });
        fpanel.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                //once a click has been fired on the focus panel
                //release focus to be available for focus panel
                fpanel.setFocus(true);
                MapStore.getInstance().setFocusLocked(false);
            }
        });

        freezeLayoutUp.setSize("100%", "100%");
        freezeLayoutUp.setStyleName("freezeLayout");
        freezeLayoutUp.getElement().getStyle().setOpacity(0.3);
        freezeLayoutUp.getElement().getStyle().setZIndex(10000);
        freezeLayoutUp.setVisible(false);
        freezeLayoutDown.setSize("100%", "100%");
        freezeLayoutDown.setStyleName("freezeLayout");
        freezeLayoutDown.getElement().getStyle().setOpacity(0.3);
        freezeLayoutDown.getElement().getStyle().setZIndex(10000);
        freezeLayoutDown.setVisible(false);
        freezeLayoutLeft.setSize("100%", "100%");
        freezeLayoutLeft.setStyleName("freezeLayout");
        freezeLayoutLeft.getElement().getStyle().setOpacity(0.3);
        freezeLayoutLeft.getElement().getStyle().setZIndex(10000);
        freezeLayoutLeft.setVisible(false);
        freezeLayoutRight.setSize("100%", "100%");
        freezeLayoutRight.setStyleName("freezeLayout");
        freezeLayoutRight.getElement().getStyle().setOpacity(0.3);
        freezeLayoutRight.getElement().getStyle().setZIndex(10000);
        freezeLayoutRight.setVisible(false);
        highlightAnim.setStyleName("highLightAnim");
        highlightAnim.getElement().getStyle().setOpacity(0.5);
        highlightAnim.getElement().getStyle().setZIndex(10000);
        highlightAnim.setVisible(false);

        f = new Fade(highlightAnim.getElement());
        f.setDuration(1);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                countUpDown++;
                if (countUpDown <= 6) {
                    if (countUpDown % 2 == 1) {
                        f.setStartOpacity(lowOpacity);
                        f.setEndOpacity(highOpacity);
                    } else {
                        f.setStartOpacity(highOpacity);
                        f.setEndOpacity(lowOpacity);
                    }
                    f.play();
                } else {
                    highlightAnim.setVisible(false);
                }
            }
        });

        base.add(freezeLayoutUp, 0, 0);
        base.add(freezeLayoutDown, 0, 0);
        base.add(freezeLayoutLeft, 0, 0);
        base.add(freezeLayoutRight, 0, 0);
        base.add(highlightAnim, 0, 0);

        GameStore.getInstance().setSectorMenu(sectorMenu);
        this.base.setSize("700px", "700px");
        this.base.add(map, 0, 0);
//        this.base.add(regionSelectionPanel, 10, 202);
//        regionSelectionPanel.setSize("116px", "29px");

        // Administration Points and Command Points panels
        final AdmAndCommPtsPanel admPtsPanel = new AdmAndCommPtsPanel();
        final AdmAndCommPtsPanel commPtsPanel = new AdmAndCommPtsPanel();
        economyView = new EconomyView(admPtsPanel, commPtsPanel, base);

        unitsMenuContainer = new AbsolutePanel();
        unitsMenuContainer.setSize("412px", "165px");


//        final AbsolutePanel menusAbsPanel = new AbsolutePanel();
        unitsMenuContainer.add(unitsMenu, 0, 0);
        unitsMenuContainer.add(admPtsPanel, 220, 38);
        unitsMenuContainer.add(commPtsPanel, 275, 38);
        base.add(unitsMenuContainer);
        base.add(sectorMenu, 0, 1);
        base.add(optionsMenu, 589, 102);
        this.base.add(economyView, 0, 0);
        this.base.add(ordersMiniView);
        this.base.add(chatView);
        TutorialStore.getInstance().setInfoPanel(tutorial);
        if (TutorialStore.getInstance().isTutorialMode()) {
            base.add(tutorial);
        }

        datePanel = new DateView();
        this.base.add(datePanel);
        base.add(speedTest, 1207, 0);
        reSize();
        sectorMenu.initToggleHandler(unitsMenuContainer, optionsMenu);

    }

    public void addWidgetToPanel(final Widget w, final int x, final int y) {
        base.add(w, x, y);
    }

    public void removeWidgetFromPanel(final Widget w) {
        base.remove(w);
    }

    public void addPSCounterForTutorial(final ProductionSitesCounterPanel panel) {
        final int width = Window.getClientWidth();
        this.base.add(panel, (width / 2) - 150, 83);
    }

    public SpeedTest getSpeedTest() {
        return speedTest;
    }

    public void resetTutorialPosition() {
        final int width = Window.getClientWidth();
        final int height = Window.getClientHeight();
        if (TutorialStore.getInstance().isTutorialMode()) {
            if (!tutorial.isAttached()) {
                base.add(tutorial, width - 600, (height / 2) - 250);
            }
            if (tutorial.isWindowMaximized()) {
                if (tutorial.getPosition() == TutorialInfoPanel.ScreenPosition.RIGHT) {
                    base.setWidgetPosition(tutorial, width - 600, (height / 2) - 250);
                } else if (tutorial.getPosition() == TutorialInfoPanel.ScreenPosition.CENTER) {
                    positionTocCenter(tutorial);
                } else {
                    setWidgetPosition(tutorial, tutorial.getCustomX(), tutorial.getCustomY(), true, false);
                }
            } else {
                base.setWidgetPosition(tutorial, width - 40, (height / 2) - 250);
            }
        }

    }

    public final void reSize() {
        final int width = Window.getClientWidth();
        final int height = Window.getClientHeight();
        this.base.setSize(width + "px", height + "px");
        this.base.setWidgetPosition(economyView, 0, 0);
        this.base.setWidgetPosition(datePanel, width / 2 - (datePanel.getOffsetWidth() / 2), 42);
        resetTutorialPosition();
        if (ordersMiniView.isOpened()) {
            this.base.setWidgetPosition(ordersMiniView, width - (264 - 10), height - (441));
        } else {
            this.base.setWidgetPosition(ordersMiniView, width - 40, height - (441));
        }

        if (chatView.isOpened()) {
            this.base.setWidgetPosition(chatView, width - (427 - 10), -8);
        } else {
            this.base.setWidgetPosition(chatView, width - 40, -8);
        }

        if (sectorMenu.isMaximized()) {
            this.base.setWidgetPosition(sectorMenu, 0, height - 194);
            this.base.setWidgetPosition(unitsMenuContainer, 178, height - 165);
            this.base.setWidgetPosition(optionsMenu, 589, height - 93);
            this.base.setWidgetPosition(economyView.getAnimatedPopups()[economyView.getAnimatedPopups().length - 2], 399, height - 180);
            this.base.setWidgetPosition(economyView.getAnimatedPopups()[economyView.getAnimatedPopups().length - 1], 454, height - 180);
            this.base.setWidgetPosition(economyView.getWarningPopups()[economyView.getWarningPopups().length - 2], 394, height - 133);
            this.base.setWidgetPosition(economyView.getWarningPopups()[economyView.getWarningPopups().length - 1], 448, height - 133);

        } else {
            this.base.setWidgetPosition(sectorMenu, 0, height - 36);
            this.base.setWidgetPosition(unitsMenuContainer, 178, height - 7);
            this.base.setWidgetPosition(optionsMenu, 589, height + 65);
            this.base.setWidgetPosition(economyView.getAnimatedPopups()[economyView.getAnimatedPopups().length - 2], 399, height - 40);
            this.base.setWidgetPosition(economyView.getAnimatedPopups()[economyView.getAnimatedPopups().length - 1], 454, height - 40);
            this.base.setWidgetPosition(economyView.getWarningPopups()[economyView.getWarningPopups().length - 2], 394, height);
            this.base.setWidgetPosition(economyView.getWarningPopups()[economyView.getWarningPopups().length - 1], 448, height);
        }

        getMap().setMapSize();
    }

    /**
     * @return the map
     */
    public MapsView getMap() {
        return map;
    }

    /**
     * @return the economyView
     */
    public EconomyView getEconomyView() {
        return economyView;
    }

    public void removeLastWidgetFromPanel() {
        base.remove(base.getWidget(base.getWidgetCount() - 1));
    }

    public void showNotification() {
        base.add(notificationPopup, Window.getClientWidth() - 366 - 150, 20);
    }

    public void hideNotification() {
        base.remove(notificationPopup);
    }

    public void showGlobalIterator() {
        base.add(gIterView, (Document.get().getClientWidth() - gIterView.getOffsetWidth()) / 2,
                (Document.get().getClientHeight() - gIterView.getOffsetHeight()) / 2);
    }

    public void hideGlobalIterator() {
        base.remove(gIterView);
    }

    public void addWidgetToLayoutPanelEAW(final WindowPanelEAW panel) {
        base.add(panel, 0, 0);
        //play sound
        SoundStore.getInstance().playWindowOpenSound(panel);
        //register panel
        GameStore.getInstance().registerPanel(panel);
    }


    public void setWidgetPosition(final Widget widget, final int x, int y, final boolean allowAboveTop, final boolean updateDraggableVariables) {
        //be sure you will not the top of the panel on a small screen..
        //because only from there can be dragged and only from there can be closed..
        if (y < 0 && !allowAboveTop) {
            y = 0;
        }
        base.setWidgetPosition(widget, x, y);
        if (updateDraggableVariables
                && widget instanceof DraggablePanel) {

            final DraggablePanel panel = (DraggablePanel) widget;
            panel.setNewPosX(x);
            panel.setNewPosY(y);
        }
    }

    public void positionTocCenter(final Widget widget) {
        setWidgetPosition(widget,
                (Document.get().getClientWidth() - widget.getOffsetWidth()) / 2,
                (Document.get().getClientHeight() - widget.getOffsetHeight()) / 2, false, true);
    }

//    public void removeWidgetFromPanel(final Widget widget) {
//        base.remove(widget);
//    }

    public void removeWidgetFromPanelEAW(final WindowPanelEAW panel) {
        //first remove from panel
        try {
            base.remove(panel);
        } catch (Exception e) {
            // TODO: handle exception
        }

        //then un-register it
        GameStore.getInstance().unRegisterPanel(panel);
    }


    public void bringToTop(final WindowPanelEAW panel) {
        GameStore.getInstance().bringPanelToTop(panel);
    }

//    public void defaultZIndexToAll() {
//        //be sure all widgets have z-index 0 (the default)
//        for (int index = 0; index < base.getWidgetCount(); index++) {
//            base.getWidget(index).getElement().getStyle().setZIndex(0);
//        }
//    }

    public int getWidgetX(final Widget widget) {
        return base.getWidgetLeft(widget);
    }

    public int getWidgetY(final Widget widget) {
        return base.getWidgetTop(widget);
    }

    /**
     * @return the optionsMenu
     */
    public OptionsMenu getOptionsMenu() {
        return optionsMenu;
    }

    /**
     * @return the unitsMenu
     */
    public SaveView getUnitsMenu() {
        return unitsMenu;
    }

    public NotificationPopup getNotificationPopup() {
        return notificationPopup;
    }

    public GlobalIteratorView getgIterView() {
        return gIterView;
    }

    public OrdersMiniView getOrdersMiniView() {
        return ordersMiniView;
    }

    public void freezeAllExcept(final Widget widget) {
        final int x = getWidgetX(widget);
        final int y = getWidgetY(widget);
        final int width = widget.getOffsetWidth();
        final int height = widget.getOffsetHeight();
        freezeAllExcept(x, x + width, y, y + height);
    }

    public void freezeAllExcept(final int xStart, final int xEnd, final int yStart, final int yEnd) {
        freezeLayoutUp.setVisible(true);
        freezeLayoutDown.setVisible(true);
        freezeLayoutLeft.setVisible(true);
        freezeLayoutRight.setVisible(true);
        final int height = Window.getClientHeight();
        final int width = Window.getClientWidth();
        setWidgetPosition(freezeLayoutUp, 0, -height + yStart, true, false);
        setWidgetPosition(freezeLayoutDown, 0, yEnd, true, false);
        setWidgetPosition(freezeLayoutLeft, -width + xStart, 0, true, false);
        setWidgetPosition(freezeLayoutRight, xEnd, 0, true, false);
    }

    public void freezeLayout() {
        freezeLayoutUp.setVisible(true);//only one needed.
        freezeLayoutDown.setVisible(false);
        freezeLayoutRight.setVisible(false);
        freezeLayoutLeft.setVisible(false);
        setWidgetPosition(freezeLayoutUp, 0, 0, true, false);
    }

    public void unFreezeLayout() {
        freezeLayoutUp.setVisible(false);
        freezeLayoutDown.setVisible(false);
        freezeLayoutRight.setVisible(false);
        freezeLayoutLeft.setVisible(false);

    }

    public void highLightWidget(final Widget widget) {
        final int x = getWidgetX(widget);
        final int y = getWidgetY(widget);
        final int width = widget.getOffsetWidth();
        final int height = widget.getOffsetHeight();
        highLightWidget(x, y, width, height);

    }

    public void highLightWidget(final int x, final int y, final int width, final int height) {
        highlightAnim.setVisible(true);
        highlightAnim.setSize(width + "px", height + "px");
        setWidgetPosition(highlightAnim, x, y, true, false);
        countUpDown = 1;
        f.setStartOpacity(lowOpacity);
        f.setEndOpacity(highOpacity);
        f.play();
    }


    public void initGraphicsPanel() {
        loadingGraphicsPanel = new LoadingGraphicsPanel();
        addWidgetToPanel(loadingGraphicsPanel, Window.getClientWidth() - 300, 50);
    }

    public LoadingGraphicsPanel getLoadingGraphicsPanel() {
        return loadingGraphicsPanel;
    }

}
