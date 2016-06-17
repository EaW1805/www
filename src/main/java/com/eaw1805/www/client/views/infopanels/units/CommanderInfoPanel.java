package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.views.military.deployment.DeployTroopsView;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.TransportStore;

import java.util.Map;

public class CommanderInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, SelectableWidget<CommanderDTO>, StyleConstants {

    private final ClickAbsolutePanel mainPanel = new ClickAbsolutePanel();
    private final CommanderDTO commander;
    private final MapStore mapStore = MapStore.getInstance();
    private ImageButton moveImage, dismissImg, viewImg, boardImg;
    private final Image loadedImg;
    private final Label lblRank;
    private final Label lblPosition;
    private final Label lblXy;
    private final Label lbmps;
    private final Image sickImage;
    private final Image supremeImage;
    private final UnitChangedHandler unitChangedHandler;
    private final RenamingLabel lblName;

    public CommanderInfoPanel(final CommanderDTO thisDTO) {
        commander = thisDTO;
        setSize("366px", "90px");

        mainPanel.setStyleName("commanderInfoPanel");
        mainPanel.setSize("363px", "87px");
        add(mainPanel);

        final int imageId;
        if (commander.getIntId() > 10) {
            imageId = 0;

        } else {
            imageId = commander.getIntId();
        }

        final Image commanderImg = new Image("http://static.eaw1805.com/img/commanders/s"
                + GameStore.getInstance().getScenarioId() + "/"
                + commander.getNationId() + "/" + imageId + ".jpg");
        commanderImg.setSize("", "82px");
        mainPanel.add(commanderImg, 3, 3);

        lblName = new RenamingLabel(commander.getName(), COMMANDER, commander.getId());
        lblName.setStyleName("clearFontMiniTitle");
        lblName.setSize("190px", SIZE_20PX);
        mainPanel.add(lblName, 90, 3);

        lblRank = new Label("");
        lblRank.setStyleName(CLASS_CLEARFONTSMALL);
        lblRank.setSize("190px", SIZE_20PX);
        mainPanel.add(lblRank, 90, 25);

        lblPosition = new Label("");
        lblPosition.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblPosition, 90, 39);

        loadedImg = new Image("http://static.eaw1805.com/images/buttons/icons/transport.png");
        loadedImg.setTitle("This item is loaded on a transport vehicle");
        loadedImg.setSize(SIZE_20PX, SIZE_20PX);
        mainPanel.add(loadedImg, 339, 34);

        sickImage = new Image("http://static.eaw1805.com/img/commanders/redcross.png");
        sickImage.setTitle("Commander is sick");
        sickImage.setSize(SIZE_20PX, SIZE_20PX);
        mainPanel.add(sickImage, 109, 63);

        supremeImage = new Image("http://static.eaw1805.com/img/commanders/supreme.png");
        supremeImage.setTitle("Supreme Commander");
        supremeImage.setSize(SIZE_20PX, SIZE_20PX);
        mainPanel.add(supremeImage, 90, 63);

        lblXy = new Label(commander.positionToString());
        if (commander.getInPool()) {
            // If commander is in the pool, just display the region
            lblXy.setText(commander.positionToString().substring(0, 1));
        }
        lblXy.setTitle("Commanders position.");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lblXy, 315, 3);
        lblXy.setSize("47px", "15px");

        lbmps = new Label(commander.getMps() + " MPs");
        lbmps.setTitle("Movement points");
        lbmps.setStyleName(CLASS_CLEARFONTSMALL);
        mainPanel.add(lbmps, 315, 20);
        lbmps.setSize("47px", "15px");

        unitChangedHandler = new UnitChangedHandler() {
            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == COMMANDER && event.getInfoId() == commander.getId()) {
                    setupImages();
                    setupLabels();
                }
            }
        };
        if (commander.isCaptured()) {
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + commander.getCaptured() + "-120.png");
            flag.setWidth("44px");
            mainPanel.add(flag, 315, 56);
        }

        setupImages();
        setupLabels();
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(COMMANDER, commander.getId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    private void setupLabels() {
        lblName.setText(commander.getName());
        lblRank.setText(commander.getRank().getName() +
                " (" + commander.getStrc() + "-" + commander.getComc() + ")");

        if (commander.getDead()) {
            lblPosition.setText("Commander is dead");

        } else if (commander.getNationId() != commander.getCaptured()) {
            lblPosition.setText("Commander has been captured by " + DataStore.getInstance().getNationNameByNationId(commander.getCaptured()));

        } else {
            if (commander.getArmy() > 0) {
                final ArmyDTO army = ArmyStore.getInstance().getcArmies().get(commander.getArmy());
                if (commander.getInTransit()) {
                    switch (commander.getTransit()) {
                        case 1:
                            lblPosition.setText("In transit to lead " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army. Expected to reach army at the end of this month.");
                            break;

                        case 2:
                            lblPosition.setText("In transit to lead " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army. Expected to reach army at the end of next month.");
                            break;

                        default:
                            lblPosition.setText("In transit to lead " + ArmyStore.getInstance().getcArmies().get(commander.getArmy()).getName() + " army. Expected to reach army in " + commander.getTransit() + " months.");
                            break;
                    }

                    lblXy.setText(commander.positionToString().substring(0, 1) + " In Tr.");

                } else {
                    lblPosition.setText("Leads " + army.getName() + " army");
                    lblXy.setText(army.positionToString());
                }

            } else if (commander.getCorp() > 0) {
                //find corp
                String name = "No name";
                final Map<Integer, ArmyDTO> armies = ArmyStore.getInstance().getcArmies();
                CorpDTO corp = null;
                for (ArmyDTO army : armies.values()) {
                    if (army.getCorps().containsKey(commander.getCorp())) {
                        name = army.getCorps().get(commander.getCorp()).getName();
                        corp = army.getCorps().get(commander.getCorp());
                    }
                }

                if (corp != null) {
                    if (commander.getInTransit()) {
                        switch (commander.getTransit()) {
                            case 1:
                                lblPosition.setText("In transit to lead " + name + " corps. Expected to reach corps at the end of this month.");
                                break;

                            case 2:
                                lblPosition.setText("In transit to lead " + name + " corps. Expected to reach corps at the end of next month.");
                                break;

                            default:
                                lblPosition.setText("In transit to lead " + name + " corps. Expected to reach corps in " + commander.getTransit() + " months.");
                                break;
                        }

                        lblXy.setText(corp.positionToString().substring(0, 1) + " In Tr.");

                    } else {
                        lblPosition.setText("Leads " + name + " corps");
                        lblXy.setText(corp.positionToString());
                    }
                }

            } else if (commander.getInPool()) {
                if (commander.getInTransit()) {
                    lblPosition.setText("Commander is in transit to the pool");
                    lblXy.setText(commander.positionToString().substring(0, 1) + " In Tr.");
                } else {
                    lblPosition.setText("Commander is in the pool");
                    lblXy.setText(commander.positionToString().substring(0, 1) + " Pool");
                }
            } else {
                lblPosition.setText("Commander is unassigned");
                lblXy.setText(commander.positionToString());
            }
        }

        if (GameStore.getInstance().getNationId() == commander.getNationId()) {
            lbmps.setText(commander.getMps() + " MPs");
        } else {
            lbmps.removeFromParent();
        }
        if (commander.getDead()) {
            lbmps.setVisible(false);
            lblXy.setVisible(false);
        }
    }

    private void setupImages() {
        if (getCommander().getDead()) {
            final Image scull = new Image("http://static.eaw1805.com/img/commanders/skull.png");
            mainPanel.add(scull, 325, 53);

            if (dismissImg != null && dismissImg.isAttached()) {
                dismissImg.removeFromParent();
            }

            if (moveImage != null && moveImage.isAttached()) {
                moveImage.removeFromParent();
            }

            if (viewImg != null && viewImg.isAttached()) {
                viewImg.removeFromParent();
            }

            if (boardImg != null && boardImg.isAttached()) {
                boardImg.removeFromParent();
            }

            loadedImg.setVisible(false);
            sickImage.setVisible(false);
            supremeImage.setVisible(false);


        } else if (getCommander().isCaptured()) {
            if (dismissImg != null && dismissImg.isAttached()) {
                dismissImg.removeFromParent();
            }

            if (moveImage != null && moveImage.isAttached()) {
                moveImage.removeFromParent();
            }

            if (viewImg != null && viewImg.isAttached()) {
                viewImg.removeFromParent();
            }

            if (boardImg != null && boardImg.isAttached()) {
                boardImg.removeFromParent();
            }


            loadedImg.setVisible(false);
            sickImage.setVisible(false);
            supremeImage.setVisible(false);
            if (!getCommander().getInPool()) {
                dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
                dismissImg.setTitle("Dismiss captured commander");
                dismissImg.setStyleName("pointer");
                dismissImg.setSize(SIZE_20PX, SIZE_20PX);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        CommanderStore.getInstance().dismissCommander(commander.getId());
                    }
                }).addToElement(dismissImg.getElement()).register();
                mainPanel.add(dismissImg, 290, 63);
            }

        } else {
            try {
                if (dismissImg != null && dismissImg.isAttached()) {
                    dismissImg.removeFromParent();
                }

                if (moveImage != null && moveImage.isAttached()) {
                    moveImage.removeFromParent();
                }

                if (viewImg != null && viewImg.isAttached()) {
                    viewImg.removeFromParent();
                }

                if (boardImg != null && boardImg.isAttached()) {
                    boardImg.removeFromParent();
                }

                if (commander.getInPool()) {
                    viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff-Gray.png");
                    viewImg.setTitle("Commander in pool");

                    boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkNA.png");
                    boardImg.setTitle("Commander in pool");
                } else {
                    viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
                    viewImg.setStyleName("pointer", true);

                    viewImg.setTitle("Go to commander position");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            MapStore.getInstance().getMapsView().goToPosition(commander);
                            viewImg.deselect();
                        }
                    }).addToElement(viewImg.getElement());

                    boardImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButEmbarkOff.png");
                    boardImg.setTitle("Board");
                    boardImg.setStyleName("pointer");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            final PositionDTO pos = MovementStore.getInstance().getUnitPosition(COMMANDER, commander.getId(), commander);
                            final boolean hasTransports = TransportStore.getInstance().hasTransportUnits(pos);
                            if (hasTransports) {
                                final DeployTroopsView dpView = new DeployTroopsView(pos.getRegionId(), 0, 0);
                                GameStore.getInstance().getLayoutView().addWidgetToLayoutPanelEAW(dpView);
                                GameStore.getInstance().getLayoutView().positionTocCenter(dpView);
                            }
                        }
                    }).addToElement(boardImg.getElement()).register();
                }

                if (commander.getArmy() == 0 && commander.getCorp() == 0 && !commander.getInPool()) {
                    dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderOff.png");
                    dismissImg.setTitle("Dismiss commander");
                    dismissImg.setStyleName("pointer");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            CommanderStore.getInstance().dismissCommander(commander.getId());
                        }
                    }).addToElement(dismissImg.getElement()).register();

                    moveImage = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                    moveImage.setTitle("Click here to move the commander");
                    (new DelEventHandlerAbstract() {
                        @Override
                        public void execute(MouseEvent event) {
                            if (commander.getArmy() == 0 && commander.getCorp() == 0) {
                                mapStore.getMapsView().goToPosition(commander);
                                mapStore.getMapsView().addFigureOnMap(COMMANDER, commander.getId(),
                                        commander, commander.getMps(), 0, 0, 0);
                            }
                        }
                    }).addToElement(moveImage.getElement()).register();

                } else {
                    dismissImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButDismissCommanderNA.png");
                    dismissImg.setTitle("The commander cannot be dismissed (in federation/pool)");

                    moveImage = new ImageButton("http://static.eaw1805.com/images/buttons/moveNA.png");
                    moveImage.setTitle("This commander cannot be moved (in federation/pool)");
                }

                if (GameStore.getInstance().getNationId() == commander.getNationId()) {
                    if (!GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                        mainPanel.add(moveImage, 315, 63);
                        mainPanel.add(dismissImg, 269, 63);
                        mainPanel.add(boardImg, 292, 63);
                    }
                    mainPanel.add(viewImg, 338, 63);

                } else {
                    final HorizontalPanel relationPanel = new HorizontalPanel() {
                        protected void onAttach() {
                            //the easiest way to reposition this...
                            mainPanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                        }
                    };
                    relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
                    final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + commander.getNationId() + "-36.png");
                    final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(commander.getNationId()) + " - ");
                    relationStatus.setStyleName("clearFont");
                    relationPanel.add(relationStatus);
                    relationPanel.add(flag);
                    mainPanel.add(relationPanel, 246, 63);
                }

                moveImage.setSize(SIZE_20PX, SIZE_20PX);
                dismissImg.setSize(SIZE_20PX, SIZE_20PX);
                boardImg.setSize(SIZE_20PX, SIZE_20PX);
                viewImg.setSize(SIZE_20PX, SIZE_20PX);
                supremeImage.setVisible(commander.getSupreme());
                loadedImg.setVisible(commander.getLoaded());
                sickImage.setVisible(commander.getSick() > 0);

            } catch (Exception ex) {
                GWT.log(ex.getMessage());
            }
        }
    }

    public CommanderDTO getCommander() {
        return commander;
    }

    public CommanderDTO getValue() {
        return getCommander();
    }

    public int getIdentifier() {
        return COMMANDER;
    }

    public Widget getWidget() {
        return this;
    }

    public void setSelected(final boolean selected) {
        if (selected) {
            mainPanel.setStyleName("infoPanelSelected");
        } else {
            mainPanel.setStyleName("commanderInfoPanel");
        }
    }

    public void onEnter() {
        // do nothing
    }

}
