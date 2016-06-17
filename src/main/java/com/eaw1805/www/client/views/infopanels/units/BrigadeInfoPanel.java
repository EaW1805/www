package com.eaw1805.www.client.views.infopanels.units;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.client.events.units.UnitChangedEvent;
import com.eaw1805.www.client.events.units.UnitChangedHandler;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.extras.ArmyImage;
import com.eaw1805.www.client.views.infopanels.AbstractInfoPanel;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RenamingLabel;
import com.eaw1805.www.client.widgets.SelectableWidget;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RelationsStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.util.ArmyUnitInfoDTO;
import com.eaw1805.www.shared.stores.util.calculators.ConquerCalculators;
import com.eaw1805.www.shared.stores.util.calculators.MiscCalculators;
import com.eaw1805.www.shared.stores.util.calculators.PowerCalculators;

public class BrigadeInfoPanel
        extends AbstractInfoPanel
        implements ArmyConstants, SelectableWidget<BrigadeDTO>, StyleConstants {

    private final ClickAbsolutePanel brigadePanel;
    private BrigadeDTO brigade;


    private Label lblMp;
    private final Label lblBrigade;
    private final int nationId;
    private ImageButton viewImg;
    private ArmyUnitInfoDTO brigInfo;
    private final Image selectedImg;
    private boolean isSelected = false;
    private final Image loadedImg;
    private final Label explbls[] = new Label[7];
    private final UnitChangedHandler unitChangedHandler;

    public BrigadeInfoPanel(final BrigadeDTO brigade, final boolean canMove) {
        setSize("366px", "90px");
        this.setBrigade(brigade);
        nationId = brigade.getNationId();
        brigadePanel = new ClickAbsolutePanel();
        this.add(brigadePanel);
        brigadePanel.setStylePrimaryName("brigadeInfoPanel");
        brigadePanel.setStyleName("clickArmyPanel", true);
        brigadePanel.setSize("363px", "87px");

        selectedImg = new Image("http://static.eaw1805.com/images/infopanels/selected.png");
        deselect();
        brigadePanel.add(selectedImg, 0, 0);
        selectedImg.setSize("363px", "87px");

        lblBrigade = new RenamingLabel("Brigade: " + brigade.getName(), BRIGADE, brigade.getBrigadeId());
        lblBrigade.setStyleName("clearFontMiniTitle");
        brigadePanel.add(lblBrigade, 8, 3);

        if (GameStore.getInstance().getNationId() == brigade.getNationId()) {
            lblMp = new Label("");
            lblMp.setStyleName(CLASS_CLEARFONTSMALL);
            lblMp.setTitle("Movement points");
            brigadePanel.add(lblMp, 315, 20);
        }

        final Label lblXy = new Label(brigade.positionToString());
        lblXy.setTitle("Brigades position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        brigadePanel.add(lblXy, 315, 3);
        lblXy.setSize("47px", SIZE_15PX);

        loadedImg = new Image("http://static.eaw1805.com/images/buttons/icons/transport.png");
        loadedImg.setTitle("This item is loaded on a transport vehicle");
        brigadePanel.add(loadedImg, 339, 34);
        loadedImg.setSize(SIZE_20PX, SIZE_20PX);

        if (GameStore.getInstance().getNationId() == brigade.getNationId()) {
            if (canMove) {
                if (brigade.getCorpId() == 0 && !GameStore.getInstance().isNationDead() && !GameStore.getInstance().isGameEnded()) {
                    final ImageButton image;


                    if (brigade.getCorpId() == 0) {
                        image = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                        image.setTitle("Click here to move the brigade");
                        (new DelEventHandlerAbstract() {
                            @Override
                            public void execute(MouseEvent event) {
                                MapStore.getInstance().getMapsView().goToPosition(brigade);
                                MapStore.getInstance().getMapsView().addFigureOnMap(BRIGADE, brigade.getBrigadeId(),
                                        brigade, brigInfo.getMps(), PowerCalculators.getPowerByBrigsAndBatts(1, getBrigade().getBattalions().size()),
                                        ConquerCalculators.getBrigadeMaxConquers(brigade), ConquerCalculators.getBrigadeMaxNeutralConquers(brigade));
                                image.deselect();
                            }
                        }).addToElement(image.getElement()).register();
                    } else {
                        image = new ImageButton("http://static.eaw1805.com/images/buttons/ButMoveOff.png");
                        image.setTitle("Cannot move brigade that is in a higher level of organization");
                    }
                    image.setSize(SIZE_20PX, SIZE_20PX);
                    brigadePanel.add(image, 315, 63);
                }


                viewImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButMagGlassOff.png");
                viewImg.setStyleName("pointer");
                viewImg.setTitle("Go to brigade position");
                brigadePanel.add(viewImg, 338, 63);
                viewImg.setSize(SIZE_20PX, SIZE_20PX);
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(MouseEvent event) {
                        MapStore.getInstance().getMapsView().goToPosition(brigade);
                        viewImg.deselect();
                    }
                }).addToElement(viewImg.getElement()).register();

            }
            boolean notSupplied = false;
            for (BattalionDTO battalion : brigade.getBattalions()) {
                if (battalion.getNotSupplied()) {
                    notSupplied = true;
                    break;
                }
            }
            if (notSupplied) {
                final Image notSupplyImg = new Image("http://static.eaw1805.com/images/buttons/OutOfSupply32.png");
                notSupplyImg.setSize("", "19px");
                brigadePanel.add(notSupplyImg, 286, 3);
                notSupplyImg.setTitle("Not supplied");
            }
        } else {
            final HorizontalPanel relationPanel = new HorizontalPanel() {
                protected void onAttach() {
                    //the easiest way to reposition this...
                    brigadePanel.setWidgetPosition(this, 366 - 3 - this.getOffsetWidth(), 90 - 3 - this.getOffsetHeight());
                }
            };
            relationPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + brigade.getNationId() + "-36.png");
            final Label relationStatus = new Label(RelationsStore.getInstance().getRelName(brigade.getNationId()) + " - ");
            relationStatus.setStyleName("clearFont");
            relationPanel.add(relationStatus);
            relationPanel.add(flag);
            brigadePanel.add(relationPanel, 246, 63);
        }
        for (int index = 0; index < 7; index++) {
            explbls[index] = new Label();
        }
        setUpImages();

        unitChangedHandler = new UnitChangedHandler() {

            public void onUnitChanged(final UnitChangedEvent event) {
                if (event.getInfoType() == BRIGADE && event.getInfoId() == brigade.getBrigadeId()) {
                    setUpImages();
                }
            }
        };

    }

    private void setUpImages() {
        brigInfo = MiscCalculators.getBrigadeInfo(brigade);
        int indexer = 0;
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            if (battalion.getOrder() == 0) {
                indexer = 1;
                break;
            }
        }
        final ArmyImage[] battImages = new ArmyImage[7];

        for (final BattalionDTO battalion : brigade.getBattalions()) {

            final int index = battalion.getOrder() - 1 + indexer;
            battImages[index] = new ArmyImage();
            battImages[index].setArmyTypeDTO(battalion.getEmpireArmyType());
            battImages[index].setEmpireBattalionDTO(battalion);


            if (brigade.getNationId() == GameStore.getInstance().getNationId()) {
                battImages[index].setUrl("http://static.eaw1805.com/images/armies/" + nationId + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
                addOverViewPanelToImage(battImages[index]);
                battImages[index].setTitle(battalion.getEmpireArmyType().getName());
                explbls[index].setText(battalion.getExperience() + "-" + battalion.getHeadcount());

            } else {
                battImages[index].setUrl("http://static.eaw1805.com/img/commanders/Generic_Naval_Commander.png");
                addOverViewPanelToImage(battImages[index]);
                battImages[index].setTitle("Unknown type");
                explbls[index].setText("? - ???");
            }
            battImages[index].setSize("49px", "49px");


            explbls[index].setStylePrimaryName(CLASS_CLEARFONTSMALL);

            brigadePanel.add(battImages[index], 8 + (49 * index), 23);
            brigadePanel.add(explbls[index], 8 + (49 * index), 72);

            this.loadedImg.setVisible(brigade.getLoaded());

        }


        if (brigade.getNationId() != GameStore.getInstance().getNationId()) {
            final Image infImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
            final Image cavImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
            final Image artImage = new Image("http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
            brigadePanel.add(infImage, 287, 26);
            brigadePanel.add(cavImage, 287, 44);
            brigadePanel.add(artImage, 287, 62);
            infImage.setSize("25px", SIZE_15PX);
            cavImage.setSize("25px", SIZE_15PX);
            artImage.setSize("25px", SIZE_15PX);
            infImage.setTitle("Infantry");
            cavImage.setTitle("Cavalry");
            artImage.setTitle("Artillery");
            final Label lblInfantryNo = new Label(String.valueOf(brigInfo.getInfantry()));
            lblInfantryNo.setStyleName(CLASS_CLEARFONTSMALL);
            brigadePanel.add(lblInfantryNo, 315, 26);

            final Label lblCavalryNo = new Label(String.valueOf(brigInfo.getCavalry()));
            lblCavalryNo.setStyleName(CLASS_CLEARFONTSMALL);
            brigadePanel.add(lblCavalryNo, 315, 44);

            final Label lblArtilleryNo = new Label(String.valueOf(brigInfo.getArtillery()));
            lblArtilleryNo.setStyleName(CLASS_CLEARFONTSMALL);
            brigadePanel.add(lblArtilleryNo, 315, 62);
        }

        if (brigade.getName().isEmpty()) {
            lblBrigade.setText("Brigade Name");

        } else {
            lblBrigade.setText(brigade.getName());
        }

        if (brigade.getNationId() == GameStore.getInstance().getNationId()) {
            lblMp.setText(brigInfo.getMps() + " MPs");
        }
    }

    public void onDetach() {
        super.onDetach();
        UnitEventManager.removeUnitChangedHandler(unitChangedHandler);
    }

    public void onAttach() {
        super.onAttach();
        unitChangedHandler.onUnitChanged(new UnitChangedEvent(BRIGADE, brigade.getBrigadeId()));
        UnitEventManager.addUnitChangedHandler(unitChangedHandler);
    }

    /**
     * Add tooltip popup panel that
     * shows information about a battalion.
     *
     * @param armyTypeImg The image to add the hover event
     */
    private void addOverViewPanelToImage(final ArmyImage armyTypeImg) {
        armyTypeImg.setStyleName("pointer", true);


        new ToolTipPanel(armyTypeImg) {
            @Override
            public void generateTip() {
                try {
                    setTooltip(new ArmyTypeInfoPanel(armyTypeImg.getEmpireBattalionDTO().getEmpireArmyType(),
                            armyTypeImg.getEmpireBattalionDTO()));
                } catch (Exception e) {
                    new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to generate tooltip", false);
                }
            }
        };
    }

    public void select() {
        isSelected = true;
        this.selectedImg.setVisible(true);

    }

    public final void deselect() {
        isSelected = false;
        this.selectedImg.setVisible(false);

    }

    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public final void setBrigade(final BrigadeDTO value) {
        this.brigade = value;
    }

    public ClickAbsolutePanel getBrigadePanel() {
        return brigadePanel;
    }

    public void mouseOver() {
        if (!isSelected) {
            selectedImg.setVisible(true);
        }

    }

    public void mouseOut() {
        if (!isSelected) {
            selectedImg.setVisible(false);
        }

    }

    public BrigadeDTO getValue() {
        return getBrigade();
    }

    public int getIdentifier() {
        return BRIGADE;
    }

    public Widget getWidget() {
        return this;
    }

    public void setSelected(final boolean selected) {
        if (selected) {
            brigadePanel.setStyleName("infoPanelSelected");
        } else {
            try {
                brigadePanel.setStylePrimaryName("brigadeInfoPanel");
                brigadePanel.setStyleName("clickArmyPanel", true);

            } catch (Exception ignore) {
            }
        }
    }

    public void onEnter() {
        // do nothing
    }
}
