package com.eaw1805.www.scenario.views.infopanels;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.web.army.ArmyTypeDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;

import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.ArmyTypeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.ArmyImage;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;
import com.eaw1805.www.scenario.stores.BrigadeUtils;
import com.eaw1805.www.scenario.stores.CorpsUtils;
import com.eaw1805.www.scenario.stores.EditorMapUtils;
import com.eaw1805.www.scenario.stores.EditorStore;
import com.eaw1805.www.scenario.views.EditorPanel;
import com.eaw1805.www.scenario.views.menu.ArmyTypeBrigMenu;
import com.eaw1805.www.fieldbattle.widgets.shared.BasicHandler;
import com.eaw1805.www.fieldbattle.widgets.shared.TextBoxEditable;

import java.util.Map;


public class BrigadeInfoPanel
        extends AbsolutePanel
        implements ArmyConstants, StyleConstants {


    private BrigadeDTO brigade;


    private final TextBoxEditable lblBrigade;
    private final int nationId;


    private final Label explbls[] = new Label[7];
    AbsolutePanel container = new AbsolutePanel();
    public BrigadeInfoPanel(final BrigadeDTO brigade) {

        setSize("366px", "90px");
        this.setBrigade(brigade);
        nationId = brigade.getNationId();


        setStylePrimaryName("brigadeInfoPanel");
        setStyleName("clickArmyPanel", true);
        setSize("363px", "87px");

        add(container, 0, 0);
        lblBrigade = new TextBoxEditable("Brigs Name");
        lblBrigade.setText(brigade.getName());
        lblBrigade.initHandler(new BasicHandler() {
            @Override
            public void run() {
                brigade.setName(lblBrigade.getText());
            }
        });
        lblBrigade.setStyleName("clearFontMiniTitle");
        add(lblBrigade, 8, 3);

        final Label lblXy = new Label(brigade.positionToString());
        lblXy.setTitle("Brigades position");
        lblXy.setStyleName(CLASS_CLEARFONTSMALL);
        add(lblXy, 270, 3);
        lblXy.setSize("60px", SIZE_15PX);


        for (int index = 0; index < 7; index++) {
            explbls[index] = new Label();
        }

        final ImageButton deleteImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Delete brigade");
        deleteImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //first delete this brigade
                BrigadeUtils.deleteBrigade(brigade);
                //then remove it from map
                EditorMapUtils.getInstance().drawArmies(brigade.getRegionId());
                //then remove it from parent
                removeFromParent();
                //finally update the army overview
                EditorPanel.getInstance().getArmyOverView().updateOverview();
            }
        });
        deleteImg.setSize("20px", "20px");
        add(deleteImg, 270, 60);

        container.setSize("366px", "90px");

        final Image corpsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/corps.png");
        corpsImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getMenu(brigade.getNationId()).showRelativeTo(corpsImg);
            }
        });

        add(corpsImg, 296, 60);
        setUpImages();
    }

    public void onAttach() {
        super.onAttach();
    }

    public void setUpImages() {
        container.clear();

        int indexer = 0;
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            if (battalion.getOrder() == 0) {
                indexer = 1;
                break;
            }
        }
        final ArmyImage[] battImages = new ArmyImage[7];
        int curIndex = 0;
        for (final BattalionDTO battalion : brigade.getBattalions()) {
            curIndex++;
            final int curIndexFinal = curIndex;
            final int index = battalion.getOrder() - 1 + indexer;
            battImages[index] = new ArmyImage();
            battImages[index].setArmyTypeDTO(battalion.getEmpireArmyType());
            battImages[index].setEmpireBattalionDTO(battalion);


            battImages[index].setUrl("http://static.eaw1805.com/images/armies/" + nationId + "/" + battalion.getEmpireArmyType().getIntId() + ".jpg");
            addOverViewPanelToImage(battImages[index]);
            battImages[index].setTitle(battalion.getEmpireArmyType().getName());
            explbls[index].setText(battalion.getExperience() + "-" + battalion.getHeadcount());

            battImages[index].setSize("49px", "49px");


            explbls[index].setStylePrimaryName(CLASS_CLEARFONTSMALL);
            battImages[index].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    final ArmyTypeBrigMenu menu = new ArmyTypeBrigMenu(battImages[index], brigade.getNationId(), battalion, BrigadeInfoPanel.this);
                    menu.initChildren();
                    menu.showMenu();
                }
            });
            container.add(battImages[index], 8 + (49 * index), 23);
            container.add(explbls[index], 8 + (49 * index), 72);

            final ImageButton removeBatt = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Remove Battalion");
            container.add(removeBatt, 8 + (49 * index) + 35, 23);
            removeBatt.setSize("10px", "10px");
            removeBatt.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    //first remove battalion
                    brigade.getBattalions().remove(battalion);
                    //then fix others order
                    int previousOrder = 0;
                    for (BattalionDTO battalion : brigade.getBattalions()) {
                        if (battalion.getOrder() != previousOrder + 1) {
                            battalion.setOrder(previousOrder + 1);
                        }
                        previousOrder = battalion.getOrder();
                    }
                    setUpImages();
                }
            });


        }

        for (int index = brigade.getBattalions().size(); index < 6; index++) {
            final ImageButton newBatt = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png", "close panel");
            container.add(newBatt, 8 + (49 * index), 23);
            newBatt.setSize("49px", "49px");
            newBatt.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    getNewBattMenu(brigade.getNationId()).showRelativeTo(newBatt);

                }
            });

        }

        if (brigade.getName().isEmpty()) {
            lblBrigade.setText("Brigade Name");

        } else {
            lblBrigade.setText(brigade.getName());
        }
        if (brigade.getCorpId() != 0) {
            CorpDTO corps = EditorStore.getInstance().getCorps().get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).get(brigade.getCorpId());
            Label corpLbl = new Label(":" + corps.getName() + ":");
            container.add(corpLbl, 290, 45);
        }
    }

    PopupPanel getNewBattMenu(int nationId) {
        final PopupPanel menu = new PopupPanel();
        menu.setAutoHideEnabled(true);
        VerticalPanel container = new VerticalPanel();

        for (final ArmyTypeDTO armyType : EditorStore.getInstance().getArmyTypesByNation(nationId)) {
            Label label = new Label(armyType.getName());
            label.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (brigade.getBattalions().size() >= 6) {
                        return;
                    }
                    final BattalionDTO batt = new BattalionDTO();
                    if (getValue().getNationId() == NationConstants.NATION_MOROCCO
                            || getValue().getNationId() == NationConstants.NATION_EGYPT
                            || getValue().getNationId() == NationConstants.NATION_OTTOMAN) {
                        batt.setHeadcount(1000);

                    } else {
                        batt.setHeadcount(800);
                    }
                    batt.setOrder(brigade.getBattalions().size() + 1);
                    batt.setExperience(armyType.getMaxExp());
                    batt.setEmpireArmyType(armyType);
                    brigade.getBattalions().add(batt);
                    menu.hide();
                    setUpImages();
                }
            });
            container.add(label);


        }
        menu.setWidget(container);
        return menu;
    }

    PopupPanel getMenu(int nationId) {
        final PopupPanel menu = new PopupPanel();
        menu.setAutoHideEnabled(true);
        final VerticalPanel container = new VerticalPanel();

        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, CorpDTO>>>> corpsMap = EditorStore.getInstance().getCorps();


        if (corpsMap.get(brigade.getRegionId()).containsKey(brigade.getX())
                && corpsMap.get(brigade.getRegionId()).get(brigade.getX()).containsKey(brigade.getY())) {
            for (final CorpDTO corps : corpsMap.get(brigade.getRegionId()).get(brigade.getX()).get(brigade.getY()).values()) {
                if (corps.getNationId() == nationId) {
                    final Label corpsName = new Label(corps.getName());
                    corpsName.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            CorpsUtils.addBrigadeToCorps(brigade, corps);
                            menu.hide();
                            setUpImages();
                        }
                    });
                    container.add(corpsName);
                }
            }
        }
        final HorizontalPanel newCorps = new HorizontalPanel();
        final TextBox newCorpsName = new TextBox();
        newCorps.add(newCorpsName);
        final Button createCorps = new Button("Create And Add");
        newCorps.add(createCorps);
        createCorps.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (newCorpsName.getText().trim().isEmpty()) {
                    Window.alert("Name cannot be empty");
                    return;
                }
                CorpDTO corp = CorpsUtils.createCorps(newCorpsName.getText().trim(), brigade.getRegionId(), brigade.getX(), brigade.getY(), brigade.getNationId());
                CorpsUtils.addBrigadeToCorps(brigade, corp);
                menu.hide();
                setUpImages();
            }
        });
        container.add(newCorps);
        if (brigade.getCorpId() != 0) {
            final Label free = new Label("Remove From Corps");
            free.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    //remove brigade from corps
                    CorpsUtils.removeBrigadeFromCorps(brigade);
                    menu.hide();
                    setUpImages();
                }
            });
            container.add(free);
        }

        menu.setWidget(container);
        return menu;
    }

    /**
     * Add tooltip popup panel that
     * shows information about a battalion.
     *
     * @param armyTypeImg The image to add the hover event
     */
    private void addOverViewPanelToImage(final ArmyImage armyTypeImg) {
        armyTypeImg.setStyleName("pointer", true);


        new ToolTipPanel(armyTypeImg, true) {
            @Override
            public void generateTip() {
                try {
                    setTooltip(new ArmyTypeInfoPanel(armyTypeImg.getEmpireBattalionDTO().getEmpireArmyType(),
                            armyTypeImg.getEmpireBattalionDTO()));
                } catch (Exception e) {

                }
            }
        };
    }


    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public final void setBrigade(final BrigadeDTO value) {
        this.brigade = value;
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


    public void onEnter() {
        // do nothing
    }
}
