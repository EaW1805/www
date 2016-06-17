package com.eaw1805.www.client.views.military.formArmy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.CorpsInfoMini;
import com.eaw1805.www.client.widgets.DelayIterator;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.ArrayList;
import java.util.List;

public class FreeCorpsSelector extends AbsolutePanel implements ArmyConstants {
    private ScrollPanel corpScrollPanel;
    private VerticalPanel freeCorpContainer;
    private ImageButton freeCorpUp;
    private ImageButton freeCorpDown;
    private SectorDTO sector;
    private PickupDragController dragController;
    private AbsolutePanel absolutePanel;
    private final FormArmyView formArmyView;
    private MovementStore mvStore = MovementStore.getInstance();

    public FreeCorpsSelector(List<CorpDTO> corps,
                             PickupDragController dragController, SectorDTO sector, final FormArmyView formArmyView) {
        setSize("180px", "544px");
        this.sector = sector;
        this.dragController = dragController;
        this.formArmyView = formArmyView;
        this.absolutePanel = new AbsolutePanel();
        this.absolutePanel.setStyleName("verticalListPanel");
        add(this.absolutePanel, 0, 22);
        this.absolutePanel.setSize("180px", "500px");
        this.corpScrollPanel = new ScrollPanel();
        this.absolutePanel.add(this.corpScrollPanel, 5, 5);
        corpScrollPanel.setStyleName("noScrollBars");
        this.corpScrollPanel.setSize("170px", "490px");

        this.freeCorpContainer = new VerticalPanel();
        corpScrollPanel.setWidget(this.freeCorpContainer);
        this.freeCorpContainer.setSize("170px", "89px");
        DropController dp = new VerticalPanelDropController(freeCorpContainer);
        this.freeCorpDown = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowDownOff.png");
        this.freeCorpDown.setStyleName("pointer");
        this.add(this.freeCorpDown, 0, 522);
        this.freeCorpDown.setSize("180px", "22px");

        this.freeCorpUp = new ImageButton(
                "http://static.eaw1805.com/images/layout/buttons/ButInfoArrowUpOff.png");
        this.freeCorpUp.setStyleName("pointer");
        this.add(this.freeCorpUp, 0, 0);
        this.freeCorpUp.setSize("180px", "22px");

        addScrollingFunctionality(freeCorpDown, freeCorpUp, corpScrollPanel);

        if (corps != null) {
            List<CorpDTO> moveCorps = new ArrayList<CorpDTO>();
            for (CorpDTO corp : corps) {
                if (corp.getCorpId() != 0) {
                    if (mvStore.hasMovedThisTurn(CORPS, corp.getCorpId())) {
                        moveCorps.add(corp);
                    } else {
                        final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, true);
                        corpInfo.addDomHandler(new DoubleClickHandler() {
                            public void onDoubleClick(DoubleClickEvent event) {
                                if (formArmyView.getFormArmyPanel().hasSpace()) {
                                    corpInfo.removeFromParent();
                                    formArmyView.getFormArmyPanel().addCorp(corpInfo.getCorp());
                                }
                            }
                        }, DoubleClickEvent.getType());
                        dragController.makeDraggable(corpInfo,
                                corpInfo.getCorpInfoPanel());
                        freeCorpContainer.add(corpInfo);
                    }
                }
            }
            for (CorpDTO corp : moveCorps) {
                final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, false);
                freeCorpContainer.add(corpInfo);
            }
        }
        dragController.registerDropController(dp);
    }

    public void reInitCorps() {
        freeCorpContainer.clear();
        final List<CorpDTO> corps = ArmyStore.getInstance().getFreeCorpsBySector(
                sector);
        if (corps != null) {
            final List<CorpDTO> moveCorps = new ArrayList<CorpDTO>();
            new DelayIterator(0, corps.size(), 1) {

                @Override
                public void executeStep() {
                    final CorpDTO corp = corps.get(ITERATE_INDEX);
                    if (corp.getCorpId() != 0 && !formArmyView.getFormArmyPanel().hasCorp(corp)) {
                        if (mvStore.hasMovedThisTurn(CORPS, corp.getCorpId())) {
                            moveCorps.add(corp);
                        } else {
                            final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, true);
                            corpInfo.addDomHandler(new DoubleClickHandler() {
                                public void onDoubleClick(DoubleClickEvent event) {
                                    if (formArmyView.getFormArmyPanel().hasSpace()) {
                                        corpInfo.removeFromParent();
                                        formArmyView.getFormArmyPanel().addCorp(corpInfo.getCorp());
                                    }
                                }
                            }, DoubleClickEvent.getType());
                            dragController.makeDraggable(corpInfo,
                                    corpInfo.getCorpInfoPanel());
                            freeCorpContainer.add(corpInfo);
                        }
                    }
                }

                @Override
                public void executeLast() {
                    for (CorpDTO corp : moveCorps) {
                        final CorpsInfoMini corpInfo = new CorpsInfoMini(corp, false);
                        freeCorpContainer.add(corpInfo);
                    }
                }
            }.run();
        }
    }

    private void addScrollingFunctionality(final ImageButton corpDown,
                                           final ImageButton corpUp, final ScrollPanel corpsScroll) {
        final Timer timer = new Timer() {
            public void run() {
                corpsScroll.setVerticalScrollPosition(corpsScroll
                        .getVerticalScrollPosition() + 9);

            }
        };
        final Timer timer2 = new Timer() {
            public void run() {
                corpsScroll.setVerticalScrollPosition(corpsScroll
                        .getVerticalScrollPosition() - 9);

            }
        };
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                corpsScroll.setVerticalScrollPosition(corpsScroll
                        .getVerticalScrollPosition() - 9);
                corpUp.deselect();
            }
        }).addToElement(corpUp.getElement()).register();

        corpUp.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                timer2.scheduleRepeating(1);

            }
        });
        corpUp.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                timer2.cancel();

            }
        });
        corpUp.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                timer2.cancel();
            }
        });

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                corpsScroll.setVerticalScrollPosition(corpsScroll
                        .getVerticalScrollPosition() + 9);
                corpDown.deselect();
            }
        }).addToElement(corpDown.getElement()).register();

        corpDown.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                timer.scheduleRepeating(1);

            }
        });
        corpDown.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                timer.cancel();

            }
        });
        corpDown.addMouseUpHandler(new MouseUpHandler() {
            public void onMouseUp(MouseUpEvent event) {
                timer.cancel();
            }
        });

        corpsScroll.addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(final MouseWheelEvent event) {
                if (event.getDeltaY() < 0) {
                    corpsScroll.setVerticalScrollPosition(corpsScroll.getVerticalScrollPosition() - 90);
                } else if (event.getDeltaY() > 0) {
                    corpsScroll.setVerticalScrollPosition(corpsScroll.getVerticalScrollPosition() + 90);
                }
            }
        }, MouseWheelEvent.getType());
    }

}
