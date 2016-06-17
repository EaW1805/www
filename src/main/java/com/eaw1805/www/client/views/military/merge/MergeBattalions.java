package com.eaw1805.www.client.views.military.merge;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.units.mini.BattalionInfoMini;
import com.eaw1805.www.client.widgets.Disposable;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.ArrayList;
import java.util.List;

public class MergeBattalions extends AbsolutePanel implements NationConstants, Disposable {
    private PickupDragController allDragController = new PickupDragController(this, false);
    private PickupDragController secDragController = new PickupDragController(this, false);
    private BattalionSelector btSel = new BattalionSelector(allDragController);
    private BattalionSelector secBatSel = new BattalionSelector(secDragController);
    private Label lblPossibleBattalionsTo;
    private AbsolutePanel batt1Panel;
    private AbsolutePanel batt2Panel;
    private Label lblBattalion;
    private Label lblBattalion_1;
    private List<ArmyDTO> armies;
    private AbsolutePanel resultPanel;
    private Label lblResultBattalion;

    private ImageButton confirmImg;


    public MergeBattalions(List<ArmyDTO> sectorArmies) {
        this.armies = sectorArmies;
        setStyleName("mergeBattalionsView");
        setSize("1134px", "544px");
        add(btSel, 88, 63);
        btSel.setSize("984px", "99px");
        add(this.secBatSel, 88, 412);
        secBatSel.setSize("984px", "99px");

        this.lblPossibleBattalionsTo = new Label("Battalions to merge");
        this.lblPossibleBattalionsTo.setStyleName("clearFont-large whiteText");
        add(this.lblPossibleBattalionsTo, 441, 33);

        this.batt1Panel = new AbsolutePanel();
//		this.batt1Panel.setStyleName("battalionMiniInfoPanel");
        add(this.batt1Panel, 280, 178);
        this.batt1Panel.setSize("245px", "75px");
        allDragController.registerDropController(new AbsolutePositionDropController(batt1Panel));

        this.batt2Panel = new AbsolutePanel();
//		this.batt2Panel.setStyleName("battalionMiniInfoPanel");
        add(this.batt2Panel, 280, 318);
        this.batt2Panel.setSize("245px", "75px");
        secDragController.registerDropController(new AbsolutePositionDropController(batt2Panel));

        this.lblResultBattalion = new Label("Result Battalion");
        this.lblResultBattalion.setStyleName("clearFontMiniTitle");
        this.lblResultBattalion.setStyleName("whiteText", true);
        add(this.lblResultBattalion, 811, 241);

        this.lblBattalion = new Label("Battalion 1");
        this.lblBattalion.setStyleName("clearFontMiniTitle");
        this.lblBattalion.setStyleName("whiteText", true);
        add(this.lblBattalion, 166, 173);

        this.lblBattalion_1 = new Label("Battalion 2");
        this.lblBattalion_1.setStyleName("clearFontMiniTitle");
        this.lblBattalion_1.setStyleName("whiteText", true);
        add(this.lblBattalion_1, 166, 311);
        this.lblBattalion_1.setSize("80px", "15px");

        this.resultPanel = new AbsolutePanel();
//		this.resultPanel.setStyleName("battalionMiniInfoPanel");
        add(this.resultPanel, 560, 248);
        this.resultPanel.setSize("245px", "75px");


        this.confirmImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
        add(this.confirmImg, 779, 295);
        this.confirmImg.setStyleName("pointer");
        this.confirmImg.setSize("26px", "26px");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                if (!confirmImg.getUrl().endsWith("NA.png")) {
                    BattalionDTO battalion1 = ((BattalionInfoMini) batt1Panel.getWidget(0)).getBattalion();
                    BattalionDTO battalion2 = ((BattalionInfoMini) batt2Panel.getWidget(0)).getBattalion();
                    ArmyStore.getInstance().mergeBattalions(battalion1.getId(), battalion2.getId());
                    BrigadeDTO parent = ArmyStore.getInstance().getBrigadeById(battalion1.getBrigadeId());
                    armies = ArmyStore.getInstance().getArmiesBySector(parent.getRegionId(), parent.getX(), parent.getY(), true);
                    confirmImg.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
                    reinitAll();
                }
                confirmImg.deselect();
            }
        }).addToElement(confirmImg.getElement()).register();


        initDragControllers();
        initAllBattalionsPanel();

    }

    private void initAllBattalionsPanel() {
        List<BattalionDTO> battalions = new ArrayList<BattalionDTO>();
        for (ArmyDTO army : armies) {
            for (CorpDTO corp : army.getCorps().values()) {
                for (BrigadeDTO brigade : corp.getBrigades().values()) {
                    if (!brigade.IsUpHeadcount() && !brigade.getLoaded()) {
                        for (BattalionDTO battalion : brigade.getBattalions()) {
                            if (battalion.getMergedWith() == 0 &&
                                    (battalion.getHeadcount() < 800 ||
                                            (battalion.getHeadcount() < 1000 &&
                                                    (brigade.getNationId() == NationConstants.NATION_MOROCCO
                                                            || brigade.getNationId() == NationConstants.NATION_OTTOMAN
                                                            || brigade.getNationId() == NationConstants.NATION_EGYPT
                                                    )
                                            )
                                    )
                                    ) {
                                battalions.add(battalion);
                            }
                        }
                    }
                }
            }
        }
        btSel.addBattalions(battalions);
    }

    protected void initSecBattalionSelector(BattalionDTO batSel) {
        List<BattalionDTO> battalions = new ArrayList<BattalionDTO>();
        batt2Panel.clear();
        if (batSel != null) {
            for (ArmyDTO army : armies) {
                for (CorpDTO corp : army.getCorps().values()) {
                    for (BrigadeDTO brigade : corp.getBrigades().values()) {
                        if (!brigade.IsUpHeadcount() && !brigade.getLoaded() && !brigade.getStartLoaded()
                                && !TransportStore.getInstance().hasUnitLoadOrder(ArmyConstants.BRIGADE, brigade.getBrigadeId())) {
                            for (BattalionDTO battalion : brigade.getBattalions()) {
                                if (battalion.getEmpireArmyType().getIntId() == batSel.getEmpireArmyType().getIntId() &&
                                        battalion.getId() != batSel.getId()) {
                                    if (battalion.getMergedWith() == 0 &&
                                            (battalion.getHeadcount() < 800 ||
                                                    (battalion.getHeadcount() < 1000 &&
                                                            (brigade.getNationId() == NationConstants.NATION_MOROCCO
                                                                    || brigade.getNationId() == NationConstants.NATION_OTTOMAN
                                                                    || brigade.getNationId() == NationConstants.NATION_EGYPT
                                                            )
                                                    )
                                            )
                                            ) {
                                        battalions.add(battalion);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        secBatSel.addBattalions(battalions);
    }

    private void makeResultingBattalion() {
        resultPanel.clear();
        BattalionDTO battalion1 = ((BattalionInfoMini) batt1Panel.getWidget(0)).getBattalion();
        BattalionDTO battalion2 = ((BattalionInfoMini) batt2Panel.getWidget(0)).getBattalion();
        BattalionDTO battalionRes = new BattalionDTO();
        battalionRes.setEmpireArmyType(battalion1.getEmpireArmyType());
        float exp = (float) (battalion1.getExperience() * battalion1.getHeadcount() + battalion2.getExperience() * battalion2.getHeadcount()) /
                (float) (battalion1.getHeadcount() + battalion2.getHeadcount());
        battalionRes.setExperience(Math.round(exp));
        if (GameStore.getInstance().getNationId() == NationConstants.NATION_MOROCCO ||
                GameStore.getInstance().getNationId() == NationConstants.NATION_EGYPT ||
                GameStore.getInstance().getNationId() == NationConstants.NATION_OTTOMAN) {
            int totalHd = battalion1.getHeadcount() + battalion2.getHeadcount();
            if (totalHd < 1000) {
                battalionRes.setHeadcount(totalHd);
            } else {
                battalionRes.setHeadcount(1000);
            }
        } else {
            int totalHd = battalion1.getHeadcount() + battalion2.getHeadcount();
            if (totalHd < 800) {
                battalionRes.setHeadcount(totalHd);
            } else {
                battalionRes.setHeadcount(800);
            }
        }
        BattalionInfoMini btNew = new BattalionInfoMini(battalionRes);
        resultPanel.add(btNew);
        confirmImg.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
    }

    private void initDragControllers() {
        allDragController.addDragHandler(new DragHandler() {
            HorizontalPanel source;

            public void onPreviewDragStart(DragStartEvent event)
                    throws VetoDragException {
                if (event.getContext().draggable.getParent().getClass().equals(HorizontalPanel.class)) {
                    source = (HorizontalPanel) event.getContext().draggable.getParent();
                }

            }

            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
            }

            public void onDragStart(DragStartEvent event) {
            }

            public void onDragEnd(DragEndEvent event) {
                if (event.getContext().finalDropController != null) {
                    if (event.getContext().finalDropController.getDropTarget().getClass().equals(AbsolutePanel.class)) {
                        AbsolutePanel finalDropPanel = (AbsolutePanel) event.getContext().finalDropController.getDropTarget();
                        if (finalDropPanel.getWidgetCount() > 1) {
                            BattalionInfoMini btInfo = (BattalionInfoMini) finalDropPanel.getWidget(0);
                            btInfo.removeFromParent();
                            source.add(btInfo);
                        }
                        BattalionInfoMini btInfo = (BattalionInfoMini) finalDropPanel.getWidget(0);
                        initSecBattalionSelector(btInfo.getBattalion());
                    } else {
                        initSecBattalionSelector(null);
                    }
                    resultPanel.clear();
                    confirmImg.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
                }
            }
        });

        secDragController.addDragHandler(new DragHandler() {
            HorizontalPanel source;

            public void onPreviewDragStart(DragStartEvent event)
                    throws VetoDragException {
                if (event.getContext().draggable.getParent().getClass().equals(HorizontalPanel.class)) {
                    source = (HorizontalPanel) event.getContext().draggable.getParent();
                }

            }

            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
            }

            public void onDragStart(DragStartEvent event) {
            }

            public void onDragEnd(DragEndEvent event) {
                if (event.getContext().finalDropController != null) {
                    if (event.getContext().finalDropController.getDropTarget().getClass().equals(AbsolutePanel.class)) {
                        AbsolutePanel finalDropPanel = (AbsolutePanel) event.getContext().finalDropController.getDropTarget();
                        if (finalDropPanel.getWidgetCount() > 1) {
                            BattalionInfoMini btInfo = (BattalionInfoMini) finalDropPanel.getWidget(0);
                            btInfo.removeFromParent();
                            source.add(btInfo);
                        }
                        makeResultingBattalion();
                    } else {
                        resultPanel.clear();
                        confirmImg.setUrl("http://static.eaw1805.com/images/buttons/ButAcceptNA.png");
                    }
                }
            }

        });
    }

    private void reinitAll() {
        batt1Panel.clear();
        batt2Panel.clear();
        initAllBattalionsPanel();
        initSecBattalionSelector(null);
        resultPanel.clear();
    }

    @Override
    public void removeGWTHandlers() {
        //todo: remove all gwt handlers since they eat a lot of resources
    }
}
