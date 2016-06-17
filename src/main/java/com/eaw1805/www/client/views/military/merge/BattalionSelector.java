package com.eaw1805.www.client.views.military.merge;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.BattalionInfoMini;
import com.eaw1805.www.client.widgets.ImageButton;

import java.util.List;

public class BattalionSelector extends AbsolutePanel {
    private ScrollPanel battScrollPanel;
    private HorizontalPanel freeBattContainer;
    private ImageButton freeBattleft;
    private ImageButton freeBattRight;
    private PickupDragController dragController;
    private AbsolutePanel selectorHolder;

    public BattalionSelector(PickupDragController dragController) {
        super();
        setSize("984px", "99px");
        this.dragController = dragController;

        this.selectorHolder = new AbsolutePanel();
//		this.selectorHolder.setStyleName("horizontalListPanel");
        add(this.selectorHolder, 22, 0);
        this.selectorHolder.setSize("940px", "99px");
        this.battScrollPanel = new ScrollPanel();
        this.selectorHolder.add(this.battScrollPanel, 5, 5);
        battScrollPanel.setStyleName("noScrollBars");
        this.battScrollPanel.setSize("930px", "89px");

        this.freeBattContainer = new HorizontalPanel();
        this.freeBattContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        battScrollPanel.setWidget(this.freeBattContainer);
        this.freeBattContainer.setSize("262px", "89px");

        DropController dp = new HorizontalPanelDropController(freeBattContainer);
        this.dragController.registerDropController(dp);

        this.freeBattleft = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowLeftOff.png");
        this.freeBattleft.setStyleName("pointer");
        this.add(this.freeBattleft, 0, 0);
        this.freeBattleft.setSize("22px", "99px");

        this.freeBattRight = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowRightOff.png");
        this.freeBattRight.setStyleName("pointer");
        this.add(this.freeBattRight, 962, 0);
        this.freeBattRight.setSize("22px", "99px");

        addScrollingFunctionality(freeBattleft, freeBattRight, battScrollPanel);

    }

    public void addBattalions(List<BattalionDTO> battalions) {
        freeBattContainer.clear();
        for (BattalionDTO battalion : battalions) {
            BattalionInfoMini btPanel = new BattalionInfoMini(battalion);
            dragController.makeDraggable(btPanel, btPanel.getBattalionPanel());
            freeBattContainer.add(btPanel);
        }

    }

    private void addScrollingFunctionality(final ImageButton fleetleft,
                                           final ImageButton fleetright, final ScrollPanel fleetScroll) {
        final Timer timer = new Timer() {
            public void run() {
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() + 3);

            }
        };
        final Timer timer2 = new Timer() {
            public void run() {
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() - 3);

            }
        };
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() + 3);
                fleetright.deselect();
                fleetright.setUrl(fleetright.getUrl().replace("Off.png", "Hover.png"));
            }
        }).addToElement(fleetright.getElement()).register();

        fleetright.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                timer.scheduleRepeating(1);

            }
        });
        fleetright.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                timer.cancel();

            }
        });
        fleetright.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                timer.cancel();
            }
        });

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                fleetScroll.setHorizontalScrollPosition(fleetScroll
                        .getHorizontalScrollPosition() - 3);
                fleetleft.deselect();
                fleetleft.setUrl(fleetleft.getUrl().replace("Off.png", "Hover.png"));
            }
        }).addToElement(fleetleft.getElement()).register();

        fleetleft.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                timer2.scheduleRepeating(1);

            }
        });
        fleetleft.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                timer2.cancel();

            }
        });
        fleetleft.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                timer2.cancel();
            }
        });
    }


}
