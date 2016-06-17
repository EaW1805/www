package com.eaw1805.www.client.views.tutorial;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.events.economy.EcoEventManager;
import com.eaw1805.www.client.events.economy.OrderAddedEvent;
import com.eaw1805.www.client.events.economy.OrderAddedHandler;
import com.eaw1805.www.client.events.tutorial.PositionClickedEvent;
import com.eaw1805.www.client.events.tutorial.PositionClickedHandler;
import com.eaw1805.www.client.events.tutorial.TutorialEventManager;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.StaticWidgets;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorialStore implements OrderConstants, GoodConstants, RegionConstants, ProductionSiteConstants {
    public static final int BUILD_BRIGADES = 1;
    public static final int TRAIN_TROOPS = 2;
    public static final int BUILD_BTRAIN = 3;
    public static final int BUILD_SHIPS = 4;


    private final static TutorialGroup group = new TutorialGroup();
    private static int animateProdSite = 0;
    private static int animatedBarracksButton = 0;
    public static boolean canChangeRegion;
    private static int month = 1;
    private static ProductionSitesCounterPanel psCounter;
    static Widget actionWidget = null;
    static final Map<Integer, Integer> countUpDowns = new HashMap<Integer, Integer>();
    private static Integer countUpDownKey = 1;
    /**
     * flags for form corps steps
     */
    private boolean brigadeDraggedFlag = false;
    private boolean commanderAssignedFlag = false;
    private static int maxStepSoFar = 0;

    private final static String[] tutorialParts = {
        "Tutorial part 1",
        "Tutorial part 1",
        "Tutorial part 1",
        "Tutorial part 1",
        "Tutorial part 2",
        "Tutorial part 2",
        "Tutorial part 2",
        "Tutorial part 2",
        "Tutorial part 2",
        "Tutorial part 3",
    };

    private final static String[] tutorialTitles = {
            "Introduction",
            "Production sites",
            "Production sites & Population Density",
            "Economy",
            "Building troops",
            "Training troops",
            "Movement",
            "Organize troops",
            "Building Baggage trains & Ships",
            "Diplomacy, Taxation & Trade",
    };

    private final static int[] tutorialSteps1 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/4,
            /*step 5*/5,
            /*step 6*/6,
            /*step 7*/6,
            /*step 8*/7,
            /*step 9*/8,
            /*step 10*/8,
            /*step 11*/9,
            /*step 12*/9,
            /*step 13*/10,
            /*step 14*/11,
            /*step 15*/12,
            /*step 16*/13,
            /*step 17*/13
    };

    private final static String[] tutorialText1 = {
            /*step 1*/"Welcome \"player name\" to the world of Empires at War. The date is January 1805, and you take the role of the Emperor of France. In this tutorial you will go through the basics of managing your country.",
            /*step 2*/"First, lets familiarize with the interface. On the top-left corner of the screen you can see a mini-map of Europe. The small rectangular frame inside the mini-map indicates the area of the map that we are now looking at, France and central Europe. The 4 little circles beneath the mini-map direct to the 4 world areas (Europe, Africa, Carribean and India). We will concentrate in Europe first.",
            /*step 3*/"On the top of the screen there is a long, horizontal bar which displays the amount of resources available in the active area (Europe in this case). Note that each area has a different warehouse, therefore materials produced in Europe are stored in the European warehouse. Materials produced in Africa go to the African warehouse and so on. The only exception is money (national treasury) which is common for all areas.",
            /*step 4*/"At the bottom left of the screen there is the information panel. A lot of information regarding terrain, units etc will be displayed in this panel.",
            /*step 5*/"Finally, these 8 buttons at the bottom of the screen open up various  menus such as diplomacy, trade etc that we will examine a little later.",
            /*step 6*/"Now, lets examine the map more closely. France's capital is Paris, which is located at coordinate 11/13. Drag the map by clicking the left mouse button so that you locate Paris and click once on the tile.",
            /*step 7*/"",
            /*step 8*/"Congratulations. You can notice that Paris is located on a tile that is \"arable land\", as you can read at the info-panel at the low-left corner. Furthermore, the population density of that area is 7 (bottom right number on the tile), which is very high since the maximum is 9, and a barrack already exists here. By looking at the other areas around Paris you will notice that no other buildings currently exist. We will build some in order to produce raw materials.",
            /*step 9*/"Lets start by building a lumbercamp that will produce timber. Lumbercamps can only be built at forest locations with population density of 1 to 3 (bottom right number on the tile). Therefore, you will have to find a forest tile with the appropriate population density.",
            /*step 10*/"",
            /*step 11*/"Very well \"player name\", you succesfully issued orders for the construction of a lumbercamp. The building is under construction and will be completed by the end of the month. It will produce the appropriate resource (timber) which will be available for use in February. Now, lets built a different production site, a sheep farm. For this building we need to locate a hills area, with population density of 1 to 3.",
            /*step 12*/"",
            /*step 13*/"Very well \"player name\", you succesfully issued orders for the construction of a sheep farm. The building is under construction and will be completed by the end of the month. It will produce the appropriate resource (wool) which will be available for use in February.",
            /*step 14*/"Notice that every time that you build  a production site, citizens and money are reduced from the warehouse of your country and the national treasury. Furthermore, Administration Points are used.",
            /*step 15*/"Admininstration Points and Command Points represent your government's ability to issue orders to the civil authorities and military leaders. Most actions in the game require the expenditure of either Administration or Command Points or a mixture of both.",
            /*step 16*/"So far you have issued orders for a lumbercamp and a sheep farm to be constructed. It is time to finish this turn (remember: each turn represents one month), and proceed to next month so that we can see the buildings produce some resources. Click on the \"save orders\" button in order to proceed to February and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 17*/""
    };


    public void initTutorialVars(final boolean tutorialEnabled, final String username, final int currentMonth) {
        tutorialMode = tutorialEnabled;
        canChangeRegion = false;
        month = currentMonth;
        tutorialText1[0] = "Welcome " + username + " to the world of Empires at War. The date is January 1805, and you take the role of the Emperor of France. In this tutorial you will go through the basics of managing your country.";
        tutorialText1[10] = "Very well " + username + ", you succesfully issued orders for the construction of a lumbercamp. The building is under construction and will be completed by the end of the month. It will produce the appropriate resource (timber) which will be available for use in February. Now, lets built a different production site, a sheep farm. For this building we need to locate a hills area, with population density of 1 to 3.";
        tutorialText1[12] = "Very well " + username + ", you succesfully issued orders for the construction of a sheep farm. The building is under construction and will be completed by the end of the month. It will produce the appropriate resource (wool) which will be available for use in February.";
        tutorialText4[4] = "Congratulations " + username + ". You have been instructed to the basics of economic management. You can now proceed to learn about raising, training and using armies and fleets to defend your empire, rule the seas and conquer our enemies! Press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.";
        tutorialText10[18] = "Congratulations " + username + ". You have succesfully completed all steps of the walkthought tutorial. You are now familiar with the basics of the game. It took me 11 years of military career (1793-1804) to become Emperor of France. How long will it take you to master the game, overcome your opponents and become the most Glorious Emperor of Europe?";
        infoPanel.setTotalSteps(getTotalStepsByMonth(month));
        if (month <= 10) {
            infoPanel.setPartAndTitle(tutorialParts[month - 1], tutorialTitles[month - 1]);
        }
    }

    private static boolean forceForward = false;
    private static boolean forceBackward = false;
    private static boolean handleForceStepChange() {
        if (forceForward) {
            if (tutorialStep < getTotalStepsByMonth(month)) {
                nextStep(true);
                return false;
            }
        }
        if (forceBackward) {
            if (tutorialStep > 1) {
                previousStep(true);
                return false;
            }
        }
        return true;
    }

    final static TutorialAction[] tutorialActions1 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.CENTER);
                    GameStore.getInstance().getLayoutView().freezeLayout();
                    GameStore.getInstance().getMiniMapPanel().getRegionSelectionPanel().setDisabledRegionButtons(INDIES, AFRICA, CARIBBEAN);
                }
            },
            new TutorialAction() {
                @Override
                public void execute() {/*step 2*/

                    GameStore.getInstance().getLayoutView().freezeAllExcept(
                            0, GameStore.getInstance().getMiniMapPanel().getOffsetWidth(),
                            0, GameStore.getInstance().getMiniMapPanel().getOffsetHeight()
                    );
                    GameStore.getInstance().getLayoutView().highLightWidget(
                            0, 0, GameStore.getInstance().getMiniMapPanel().getOffsetWidth(),
                            GameStore.getInstance().getMiniMapPanel().getOffsetHeight()
                    );
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().freezeAllExcept(
                            GameStore.getInstance().getLayoutView().getEconomyView()
                    );
                    GameStore.getInstance().getLayoutView().highLightWidget(
                            GameStore.getInstance().getLayoutView().getEconomyView());
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().freezeAllExcept(0, 589,
                            Window.getClientHeight() - 194, Window.getClientHeight());

                    GameStore.getInstance().getLayoutView().highLightWidget(0,
                            Window.getClientHeight() - 194,
                            589, 194);
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().freezeAllExcept(
                            GameStore.getInstance().getLayoutView().getOptionsMenu());

                    GameStore.getInstance().getLayoutView().highLightWidget(
                            GameStore.getInstance().getLayoutView().getOptionsMenu());
                }
            },
            new TutorialAction() {/*step 6*/
                @Override
                public void execute() {
                    //do nothing at this step.. just view the text
                }
            },
            new TutorialAction() {/*step 7*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    infoPanel.setVisible(false);
                    final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[10][12]);
                    group.highLightSectors(sectors);
                    GameStore.getInstance().getLayoutView().unFreezeLayout();
                    TutorialEventManager.addPositionClickedHandler(new PositionClickedHandler() {
                        @Override
                        public void onUnitChanged(PositionClickedEvent event) {

                            if (MapStore.getInstance().getPositionX(event.getX()) + 1 == 11
                                    && MapStore.getInstance().getPositionY(event.getY()) + 1 == 13) {
                                nextStep(false);
                                TutorialEventManager.removePositionClickedHandler(this);
                            }
                        }
                    });
                }
            },
            new TutorialAction() {/*step 8*/
                @Override
                public void execute() {
                    group.removeHighLight();
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 9*/
                @Override
                public void execute() {
                    //do nothing at this step... just view the text
                }
            },
            new TutorialAction() {/*step 10*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    buildPRSiteFlow(true, PS_LUMBERCAMP, 7);
                }
            },
            new TutorialAction() {/*step 11*/
                @Override
                public void execute() {
                    animateProdSite = 0;
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 12*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    buildPRSiteFlow(true, PS_FARM_SHEEP, 4);
                }
            },
            new TutorialAction() {/*step 13*/
                @Override
                public void execute() {
                    animateProdSite = 0;
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 14*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(GOOD_PEOPLE, GOOD_MONEY, GOOD_AP);
                }
            },
            new TutorialAction() {/*step 15*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().getEconomyView().stopHighLightGoods(GOOD_PEOPLE, GOOD_MONEY, GOOD_AP);
                    GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(GOOD_AP, GOOD_CP);
                }
            },
            new TutorialAction() {/*step 16*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().getEconomyView().stopHighLightGoods(GOOD_AP, GOOD_CP);
                    infoPanel.setVisible(true, true);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };


    private final static int[] tutorialSteps2 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/3,
            /*step 5*/4,
            /*step 6*/4,
            /*step 7*/5,
            /*step 8*/5,
            /*step 9*/6,
            /*step 10*/6,
            /*step 11*/6,
    };
    private final static String[] tutorialText2 = {
            /*step 1*/"The game advanced to February 1805. You can notice on the map that the 2 production sites are now constructed and running, and that they produced the appropriate resources: timber and wool.",
            /*step 2*/"Furthermore, notice that the amount of money available has increased since the tax collectors were busy collecting money throughout  the country's lands. The number of available citizens has also increased, while the food supplies have dropped since people and soldiers consume food every month.",
            /*step 3*/"The next step is to build a few more production sites and set up a viable economy. By following the same method as before, we will now build an estate. Estates produce food which is necessary for feeding the people and soldiers of the empire. Find a suitable area (arable land with population of 1 to 3) and build an estate. Tiles with a resource icon (a grain mark) produce extra food per month.",
            /*step 4*/"",
            /*step 5*/"One of the most important (and rare) production sites are the mines. There are mines that produce ore and mines that produce precious metals. Though precious metals are very valuable and create a lot of profit, it is the ore resource that is necessary for the smooth operation of the country's economy. Mines can only be built at locations that have the appropriate special resource: ore. Find the ore location in France and build us a mine!",
            /*step 6*/"",
            /*step 7*/"Some raw materials can be used to produce more advanced resources. Wool is used by Weaving Mills in order to produce fabric, a material necessary for building Ships and for the operation of factories. Mills can be built on either arable lands or hills, with a population density of 3 or more.",
            /*step 8*/"",
            /*step 9*/"By the end of February you should try to have established at least an estate, 2 lumbercamps, 1 ore mine, 4 sheep farms and a weaving mill. Built all these production sites and click the \"save orders\" button in order to proceed to the next turn, and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 10*/"",
            /*step 11*/"",
    };

    final static TutorialAction[] tutorialActions2 = {
            new TutorialAction() {/*step 1*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.CENTER);
                    GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(GOOD_WOOL, GOOD_WOOD);
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().getEconomyView().stopHighLightGoods(GOOD_WOOL, GOOD_WOOD);
                    GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(GOOD_MONEY, GOOD_PEOPLE, GOOD_FOOD);

                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().getEconomyView().stopHighLightGoods(GOOD_MONEY, GOOD_PEOPLE, GOOD_FOOD);
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    buildPRSiteFlow(true, PS_ESTATE, 1);
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 6*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    buildPRSiteFlow(false, PS_MINE, 2);
                }
            },
            new TutorialAction() {/*step 7*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 8*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    buildPRSiteFlow(true, PS_MILL, 1, 4);
                }
            },
            new TutorialAction() {/*step 9*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 10*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    infoPanel.setVisible(false);
                    psCounter = new ProductionSitesCounterPanel();
                    GameStore.getInstance().getLayoutView().addPSCounterForTutorial(psCounter);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };

    private final static int[] tutorialSteps3 = {
            /*step 1*/1,
            /*step 2*/1,
            /*step 3*/2,
            /*step 4*/3,
            /*step 5*/3,
            /*step 6*/4,
            /*step 7*/4,
            /*step 8*/5,
            /*step 9*/5,
            /*step 10*/5,
    };

    private final static String[] tutorialText3 = {
            /*step 1*/"It is now March, and our economy is building up. As you can see at the resource bar at the top of the screen the materials are being collected and stockpiled in the warehouse. It is now time to build a factory, one of the most valuable buildings. Factories use lumber, fabric and ore in order to produce an important commodity: Industrial Points which are used for a variety of purposes. Find a suitable area (arable land or hill with population density of 4 or more) and set up a factory.",
            /*step 2*/"",
            /*step 3*/"Good. With the construction of a factory and the necessary production sites, the country will have a steady flow of industrial points. Lets examine now another important aspect of the map, the population density. As you have noticed, each land area (tile) has a population density number (bottom right number on the tile) which represent the number of people who inhabit this area. A larger population means more citizens (recruits) arriving at the national warehouse every month and more taxes entering the state coffers! ",
            /*step 4*/"It is sometimes necessary to increase or decrease the population of an area. For example, if an area has a population density of \"zero\", you may want to set some inhabitants there and increase it to \"one\" in order to build a production site. Let's try to do that: Find an area with no population marker (population density = 0) and left-click on it. Afterwards, click on the green arrow on the menu in order to perform a population density raise.",
            /*step 5*/"",
            /*step 6*/"You just ordered the population density at \"X/Y\" to increase by 1. Notice that the number of citizens at the warehouse (bar at the top) decreased, since people were sent to inhabit this area. You cannot alter the population levels by more than one level per month. Let's try now to decrease the population density. This action may be useful in a number of situations: perhaps you want to reduce the population in an area in order to build a production site, or perhaps this area is under threat of being conquered. Furthermore, when decreasing population a proportion of the citizens removed from the area arrive at the national warehouse immediately, as recruits. But remember, that by doing so you will collect less taxes next month. Find an area with any population marker (population density > 0) and left-click on it. Afterwards, click on the red arrow on the menu in order to perform a population density reduction.",
            /*step 7*/"",
            /*step 8*/"Very well, you've placed orders for raising and reducing population densities. Spent some time now to build some more production sites (try quarries at mountains, and horse farms at suitable locations). Notice that you may alter the population density of an area and build on it a production site at the same turn. When you are finished, press the \"Save orders\" button as before to process the turn and proceed to next month, and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 9*/"",
            /*step 10*/"",
    };

    final static TutorialAction[] tutorialActions3 = {
            new TutorialAction() {/*step 1*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.CENTER);
                    GameStore.getInstance().getLayoutView().getEconomyView().highLightGoods(GOOD_WOOL, GOOD_WOOD);
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    GameStore.getInstance().getLayoutView().getEconomyView().stopHighLightGoods(GOOD_WOOL, GOOD_WOOD);
                    if (!handleForceStepChange()) {
                        return;
                    }
                    buildPRSiteFlow(true, PS_FACTORY, 1, 4);
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    popupDownFlow(true, 5);
                }
            },
            new TutorialAction() {/*step 6*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 7*/
                @Override
                public void execute() {
                    if (!handleForceStepChange()) {
                        return;
                    }
                    popupDownFlow(false, 7);
                }
            },
            new TutorialAction() {/*step 8*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true);
                }
            },
            new TutorialAction() {/*step 9*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };

    public static void popupDownFlow(final boolean up, final int step) {
        infoPanel.setVisible(false);
        final List<SectorDTO> sectorsToHighLight = new ArrayList<SectorDTO>();
        for (SectorDTO[] sectorRow : RegionStore.getInstance().getRegionSectorsByRegionId(1)) {
            for (SectorDTO sector : sectorRow) {
                if (sector != null
                        && sector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()
                        && ((up && sector.getPopulation() == 0)
                        || (!up && sector.getPopulation() > 0))) {
                    sectorsToHighLight.add(sector);
                }
            }
        }
        try {
            group.highLightSectors(sectorsToHighLight);
        } catch (Exception e) {

        }
        final PositionClickedHandler handler = new PositionClickedHandler() {
            @Override
            public void onUnitChanged(PositionClickedEvent event) {
                final SectorDTO sectorDTO = RegionStore.getInstance()
                        .getRegionSectorsByRegionId(1)[MapStore.getInstance().getPositionX(event.getX())]
                        [MapStore.getInstance().getPositionY(event.getY())];
                if (sectorDTO != null) {
                    if (up && sectorDTO.getPopulation() == 0) {
                        MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                                .setDisabledButtons(StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP);
                        highLightButton(StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP);
                    } else if (!up && sectorDTO.getPopulation() > 0) {
                        MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                                .setDisabledButtons(StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP);
                        highLightButton(StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP);
                    }
                }
            }
        };
        final OrderAddedHandler orderHandler = new OrderAddedHandler() {
            @Override
            public void onOrderAdded(OrderAddedEvent event) {

                if ((up && event.getClientOrder().getOrderTypeId() == ORDER_INC_POP)
                        || (!up && event.getClientOrder().getOrderTypeId() == ORDER_DEC_POP)) {
                    final SectorDTO sector = RegionStore.getInstance().getSectorById(event.getClientOrder().getIdentifier(0));
                    if (up && sector.getPopulation() != 0
                            || !up && sector.getPopulation() == 0) {
                        return;
                    }
                    TutorialEventManager.removePositionClickedHandler(handler);
                    EcoEventManager.removeOrderAddedHandler(this);
                    group.removeHighLight();
                    MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                            .setDisabledButtons();
                    if (month == 3 && step == 5) {
                        tutorialText3[5] = "You just ordered the population density at \"" + sector.positionToString() + "\" to increase by 1. Notice that the number of citizens at the warehouse (bar at the top) decreased, since people were sent to inhabit this area. You cannot alter the population levels by more than one level per month. Let's try now to decrease the population density. This action may be useful in a number of situations: perhaps you want to reduce the population in an area in order to build a production site, or perhaps this area is under threat of being conquered. Furthermore, when decreasing population a proportion of the citizens removed from the area arrive at the national warehouse immediately, as recruits. But remember, that by doing so you will collect less taxes next month. Find an area with any population marker (population density > 0) and left-click on it. Afterwards, click on the red arrow on the menu in order to perform a population density reduction.";
                    }
                    nextStep(false);
                }
            }
        };
        TutorialEventManager.addPositionClickedHandler(handler);
        EcoEventManager.addOrderAddedHandler(orderHandler);
    }

    public static void buildPRSiteFlow(final boolean byTerrainId, final int siteId, final int... ids) {
        List<Integer> lookIds = new ArrayList<Integer>();
        for (int id : ids) {
            lookIds.add(id);
        }
        buildPRSiteFlow(byTerrainId, lookIds, siteId);
    }

    public static void buildPRSiteFlow(final boolean byTerrainId, final List<Integer> lookIds, final int siteId) {
        infoPanel.setVisible(false);
        final List<SectorDTO> sectorsToHighLight = new ArrayList<SectorDTO>();
        for (SectorDTO[] sectorRow : RegionStore.getInstance().getRegionSectorsByRegionId(1)) {
            for (SectorDTO sector : sectorRow) {
                if (sector != null
                        && sector.getNationDTO().getNationId() == GameStore.getInstance().getNationId()
                        && ((byTerrainId && lookIds.contains(sector.getTerrain().getId()))
                        || (!byTerrainId && lookIds.contains(sector.getNatResId())))) {
                    sectorsToHighLight.add(sector);
                }
            }
        }
        try {
            group.highLightSectors(sectorsToHighLight);
        } catch (Exception e) {

        }
        animateProdSite = siteId;
        final PositionClickedHandler handler = new PositionClickedHandler() {
            @Override
            public void onUnitChanged(PositionClickedEvent event) {
                final SectorDTO sectorDTO = RegionStore.getInstance()
                        .getRegionSectorsByRegionId(1)[MapStore.getInstance().getPositionX(event.getX())]
                        [MapStore.getInstance().getPositionY(event.getY())];
                if (sectorDTO != null
                        && ((byTerrainId && lookIds.contains(sectorDTO.getTerrain().getId()))
                        || (!byTerrainId && lookIds.contains(sectorDTO.getNatResId())))) {
                    if (sectorDTO.getPopulation() == 0) {
                        new ErrorPopup(ErrorPopup.Level.ERROR, "this location does not have enough people! Choose a forest with population density of 1 to 3.", false);
                    } else {
                        MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                                .setDisabledButtons(StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP,
                                        StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP);
                        highLightButton(StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP);
                    }
                }
            }
        };
        final OrderAddedHandler orderHandler = new OrderAddedHandler() {
            @Override
            public void onOrderAdded(OrderAddedEvent event) {

                if (event.getClientOrder().getOrderTypeId() == ORDER_B_PRODS
                        && event.getClientOrder().getIdentifier(1) == siteId) {
                    final SectorDTO sector = RegionStore.getInstance().getSectorById(event.getClientOrder().getIdentifier(0));
                    if (!((byTerrainId && lookIds.contains(sector.getTerrain().getId()))
                            || (!byTerrainId && lookIds.contains(sector.getNatResId())))
                            || sector.getPopulation() == 0) {
                        return;
                    }
                    TutorialEventManager.removePositionClickedHandler(handler);
                    EcoEventManager.removeOrderAddedHandler(this);
                    group.removeHighLight();
                    animateProdSite = -1;
                    MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                            .setDisabledButtons();
                    nextStep(false);
                }
            }
        };
        TutorialEventManager.addPositionClickedHandler(handler);
        EcoEventManager.addOrderAddedHandler(orderHandler);
    }

    private final static int[] tutorialSteps4 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/4,
            /*step 5*/5,
            /*step 6*/5,
    };

    private final static String[] tutorialText4 = {
            /*step 1*/"As April enters, you have now acquired a good basic understanding of setting up the  production of the country. We will now look at a more advanced economical option. Click on the \"Adjust Taxation\" button at the bottom of the screen.",
            /*step 2*/"You can notice that there are three different options for taxation of the European population of our Empire. It can be set to either \"low\", \"normal\" or \"harsh\". Each choice has pros and cons, and you can adjust this setting every month, according to the country's needs. It is recommended that you keep the setting to \"normal\" in the first months of the game and until you get more experienced in the state matters. Press the green button at the top right corner of the panel in order to proceed.",
            /*step 3*/"Now take at look at the \"Review your Empire's economic review\" button which is located closely to the previous one.",
            /*step 4*/"In this menu, you can access all the information necessary to have a complete overall picture of the Empire's economics. Spent some time browsing the reports and click the green button at the top right corner when you are ready.",
            /*step 5*/"Congratulations \"Player Name\". You have been instructed to the basics of economic management. You can now proceed to learn about raising, training and using armies and fleets to defend your empire, rule the seas and conquer our enemies! Press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 6*/"",
    };

    final static TutorialAction[] tutorialActions4 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.CENTER);
                    highLightButton(StaticWidgets.TAXATION_IMAGE_OPTIONS);
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(StaticWidgets.CLOSE_IMAGE_TAXATION_VIEW);
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.CENTER);
                    highLightButton(StaticWidgets.ECONOMIC_IMAGE_OPTIONS);
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(StaticWidgets.CLOSE_IMAGE_ECONOMY_REPORTS);
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.CENTER);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }

    };

    private final static int[] tutorialSteps5 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/4,
            /*step 5*/4
    };

    private final static String[] tutorialText5 = {
            /*step 1*/"Armed forces are raised and trained in barracks. All countries begin with a barrack at the Capital, in our case Paris. Click at the center of the tile, to open the actions ribbon and then click on the highlighted barrack button.",
            /*step 2*/"We have now accessed the barrack menu. This is the place to build new troops. Let's try to build a brigade: Click on the \"build new troops\" icon.",
            /*step 3*/"On the right side of the panel, there is a list of all available battalion types of our Empire, separated into three categories: infantry, artillery and cavalry. By clicking on a battalion type, you order one battalion to be built. Notice that the required materials will be deducted from your national warehouse. Furthermore, remember that a brigade must have a minimum of 4 battalions and a maximum of 6. Try to build a new brigade by selecting 4-6 battalions.",
            /*step 4*/"Congratulations, you've just ordered a new brigade to be built. It will report for service at the beginning of next month. Proceed now to build as many new brigades as you want, or as many as our available materials allow. After you're finished, exit the \"barrack's menu\" and press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 5*/""
    };
    private static PositionClickedHandler handler;

    public void clearPositionClickedHandler() {
        if (handler != null) {
            TutorialEventManager.removePositionClickedHandler(handler);
        }
    }

    final static TutorialAction[] tutorialActions5 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                    final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[10][12]);
                    group.highLightSectors(sectors);
                    animatedBarracksButton = BUILD_BRIGADES;
                    handler = new PositionClickedHandler() {
                        @Override
                        public void onUnitChanged(PositionClickedEvent event) {

                            if (MapStore.getInstance().getPositionX(event.getX()) + 1 == 11
                                    && MapStore.getInstance().getPositionY(event.getY()) + 1 == 13) {
                                TutorialEventManager.removePositionClickedHandler(this);
                                MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                                        .setDisabledButtons(StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP,
                                                StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP,
                                                StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP,
                                                StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP);
                                highLightButton(StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP);
                            }
                        }
                    };

                    TutorialEventManager.addPositionClickedHandler(handler);
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }

    };

    private final static int[] tutorialSteps6 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/2
    };

    private final static String[] tutorialText6 = {
            /*step 1*/"Very well, the new troops have been raised. You can see the soldier's figure at Paris. The new units are ready for orders, but they are still untrained, thus will perform poorly at battle. It is a good practice to always try to train troops before sending them to war. To do so, access the barrack's menu again, and choose \"train troops\".",
            /*step 2*/"The troops that are stationed in this barrack, appear at the centre of the menu. Click the \"train\" button, to give the order for the unit (all battalions) to be trained. After you've issued the training orders, you can raise more troops if you like (they will be ready next month). Once you're finished with that, exit the menu, press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 3*/""
    };

    final static TutorialAction[] tutorialActions6 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                    final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[10][12]);
                    group.highLightSectors(sectors);
                    animatedBarracksButton = TRAIN_TROOPS;
                    handler = new PositionClickedHandler() {
                        @Override
                        public void onUnitChanged(PositionClickedEvent event) {

                            if (MapStore.getInstance().getPositionX(event.getX()) + 1 == 11
                                    && MapStore.getInstance().getPositionY(event.getY()) + 1 == 13) {
                                TutorialEventManager.removePositionClickedHandler(this);
                                MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                                        .setDisabledButtons(StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP,
                                                StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP,
                                                StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP,
                                                StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP);
                                highLightButton(StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP);
                            }
                        }
                    };


                    TutorialEventManager.addPositionClickedHandler(handler);

                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };

    private final static int[] tutorialSteps7 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/3
    };

    private final static String[] tutorialText7 = {
            /*step 1*/"Now that we've raised some troops, it’s time we move them around. Click on the soldier's figure at Paris.",
            /*step 2*/"You can now see all the brigades that we've built in the previous turns. By selecting each brigade, you can see more information about it, such as it's strength and composition. Furthermore, you can see a new ribbon appearing over the brigade with some new options. Select a brigade and then click on the \"move\" icon.",
            /*step 3*/"The highlighed tiles are the ones that the brigade can reach by movement this turn. Click on any highlighted destination tile, and notice how the arrows will indicate the ordered move. You can increase the range of movement by ordering a \"forced march\" movement. This allow for a 50% increase in movement points, but can be used only over owned territory. Furthermore, it results in higher attrition rates. Go on now and give some movement orders to your brigades. When you're done, press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 4*/""
    };


    final static TutorialAction[] tutorialActions7 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    group.clear();//now we can touch things on map.
                    final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[10][12]);
                    group.highLightSectors(sectors);
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };

    private final static int[] tutorialSteps8 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/4,
            /*step 5*/4
    };

    private final static String[] tutorialText8 = {
            /*step 1*/"Our troops perfomed their movements, as ordered. Moving individual brigades though is not an efficient way to command your forces. Lets see how to form Corps and Armies.",
            /*step 2*/"We have created some new brigades at Paris, to demonstrate how to organise forces.Click on the soldier's figure, and then click on the little round button on the top left, named \"organise forces\".",
            /*step 3*/"In this menu, you can form new Corps by dragging and dropping brigades in the vacant boxes. After you've created a Corp, you can form a new Army. You can add a commander to the Corp/Army by clicking on the commander's box on the top.",
            /*step 4*/"You can use the same process (drag & drop) to add or remove brigades from Corps and add or remove Corps from Armies. Try to form a Corps or two now, and then form an Army. You can then proceed and issue movement orders to these Corps and Army. Remember that individual brigades cannot conquer enemy territories. Only Corps and Armies with commanders can do that. When you're done, press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 5*/""
    };

    final static TutorialAction[] tutorialActions8 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    group.clear();//now we can touch things on map.
                    final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[10][12]);
                    group.highLightSectors(sectors);
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };


    private final static int[] tutorialSteps9 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/4,
            /*step 5*/5,
            /*step 6*/6,
            /*step 7*/6
    };

    private final static String[] tutorialText9 = {
            /*step 1*/"The barrack menu has more options, such as \"increasing headcount\" or \"merging battalions\", that will prove useful later one. For the time being, let's see how to build baggage trains. Access the barrack menu of Paris.",
            /*step 2*/"Baggage trains are necessary for overland trade with other trade cities, if the relations of the two countries are at  \"trade\" status or better. Go on and build a baggage train.",
            /*step 3*/"You can build troops and baggage trains at all controlled barracks. But barracks that are on tiles adjacent to water, also function as shipyards and in addition to the above, they allow the construction of ships - both warships and merchant ships.",
            /*step 4*/"Naval power is critical for Empires with oversea possecions in the colonies. Ships allow transportation of troops and goods, sea trading, fihing, piracy and of-course naval warfare. There are two main categories of ships: Warships & Merchant Ships. Try to build one of each.",
            /*step 5*/"The shipyard has began construction of the ships and they will be ready for service in the following month(s). Most ships require only one month of construction, while larger ships require more time.",
            /*step 6*/"You have learned how to raise new troops & ships and move them. You can place a few more orders now if you want, to better familiarise yourself with the process, and when you're ready, we can proceed to the third and final part of the tutorial. Press the \"save orders\" button and close the game window. The engine will process your turn within the next 5 minutes. As soon as your turn is processed, you will receive an e-mail to continue your game.",
            /*step 7*/""
    };

    final static TutorialAction[] tutorialActions9 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    animatedBarracksButton = BUILD_BTRAIN;
                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    final List<SectorDTO> sectors = new ArrayList<SectorDTO>();
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[2][11]);
                    sectors.add(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[14][21]);

                    group.highLightSectors(sectors);

                    MapStore.getInstance().getMapsView().getMapDrawArea().getTileActions()
                            .setDisabledButtons(StaticWidgets.BUILD_IMAGE_TILE_ACTIONS_POPUP,
                                    StaticWidgets.POPDOWN_IMAGE_TILE_ACTIONS_POPUP,
                                    StaticWidgets.HAND_OVER_IMAGE_TILE_ACTIONS_POPUP,
                                    StaticWidgets.POPUP_IMAGE_TILE_ACTIONS_POPUP);
                    highLightButton(StaticWidgets.OPEN_BARRACK_IMAGE_TILE_ACTIONS_POPUP);
                    animatedBarracksButton = BUILD_SHIPS;
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };


    private final static int[] tutorialSteps10 = {
            /*step 1*/1,
            /*step 2*/2,
            /*step 3*/3,
            /*step 4*/4,
            /*step 5*/5,
            /*step 6*/6,
            /*step 7*/7,
            /*step 8*/8,
            /*step 9*/9,
            /*step 10*/10,
            /*step 11*/11,
            /*step 12*/12,
            /*step 13*/13,
            /*step 14*/14,
            /*step 15*/15,
            /*step 16*/16,
            /*step 17*/17,
            /*step 18*/18,
            /*step 19*/19,
            /*step 20*/19
    };

    private final static String[] tutorialText10 = {
            /*step 1*/"Welcome to the 3rd part of this walkthrough. We will now learn the basics of various administrative functions, such as diplomacy, taxation and trade.",
            /*step 2*/"To begin with, take notice of the panel at the bottom of the screen. These 8 buttons access various important departments of your Government. Let's start with Diplomacy. Click on the button \"Review and adjust Political Relations\"",
            /*step 3*/"In this menu, you can review and change the political relations of your Empire towards the other countries. There are 5 possible political relations, ranging from \"war\" to \"alliance\", and each level of relations allow for different interactions between the two countries.  Keep in mind that any changes of political relations will take effect at the beginning of next month. Therefore, if you order your diplomats to declare war to another country, the declaration will take place at the end of this turn and your troops will be able to invade the enemy territories from next month onwards. Once you've made any changes you want, you can click on the green tick button on the top-right corner of the panel to exit the menu or you can click the red cross button to exit without saving.",
            /*step 4*/"You can review the political relations of other countries if you have a spy in a barrack owned by another nation. For this tutorial, we have moved one of our spies to London, the British capital.",
            /*step 5*/"Click on the \"Review Spy Reports\" button.",
            /*step 6*/"In this panel, you can review the political relations of other countries that we spy, i.e. that we have a spy in a barrack they own. In order to see the political relations of Britain, click on the British flag at the top of the panel. When finished, exit the panel by clicking on the green button at the top.",
            /*step 7*/"As you become more familiar with the various economical mechanics, you may want to have a better insight understanding of the financial situation and production of our country. A good way to do that is by examining the Empire's \"Economic Report\". ",
            /*step 8*/"The citizens are taxed and provide a steady source of income each month, as the production sites produce materials. Trade however, may boost income and resource's availability and can be the difference between a mediocre and a wealthy empire. Trading is done between trade cities, baggage trains, merchant ships and the national warehouse. It is useful for all countries, but absolutely essential to some. Click on the \"Perform Trade\" button.",
            /*step 9*/"Trading mechanisms are a little more complex, since they recreate a rich and dynamic environment of merchantile operations. Each Trace City has \"wealth\", which represents the overall financial power of the city's merchants. This figure is not static, but fluctuates each month according to various factors, such as the volume of trading and others. Furthermore, each country has a different quantity of materials available, depending on its wealth, volume of trading, country's traits and others.",
            /*step 10*/"If a Trace city has many icons of a certain resource, then this resource is in \"surplus\". On the contrary, if there are few or no icons, then this resource is in \"demand\". Resources in demand are more expensive, and traders can get a higher price by selling the specific material to a trade city in \"demand\", while resources \"in surplus\" tend to be cheaper. Clever merchants try to buy products in surplus, move their baggage trains and merchant ships, and sell the products in cities with demand.",
            /*step 11*/"Click on the \"Trade Cities\" button on the right of the panel.",
            /*step 12*/"Paris is the only Trade city we control in Europe. Click on it.",
            /*step 13*/"Good. The column on the right indicates that you can perform trade actions between Paris on the one hand and you national warehouse or baggage trains on the other. Click on \"national warehouse\" and then on the central button.",
            /*step 14*/"This is the actual trading panel. From here you can select a type of resource on the left, and either buy it from the Trade City or sell it. If you buy a resource, the amount bought will be added to your national warehouse's stockpile (on the top of the screen), but you will pay money for it. Obviously, if you sell some resources, you will have them removed from the stockpile but you will gain money. As a rule of thumb, it is usually better to buy materials in surplus and sell materials in demand, but specific conditions and requirements of the Empire may force you to do otherwise. For example, if our industries are running out of ore, it may be necessary to buy it at whatever price!",
            /*step 15*/"In order to perform a sell or purchase, choose a resource, indicate the quantity by either dragging the bar or typing a nymber, and then press the \"perform transaction\" button. Perform as many transcations as you like and then exit the \"Trade\" menu. Remember that Trade transactions cost administration points, therefore you can only perform a limited number each month.",
            /*step 16*/"Trading can be a very useful tool for increasing income. But if more money is needed there is always the option of taxing your people more! Click on the \"adjust taxation\" button.",
            /*step 17*/"There are 3 levels of taxation: Low, normal and harsh. The default level is \"normal\", but you can change that to low or harsh to receive the respective bonuses. Low taxation will provide more recruits but less money next month, while harsh taxation will provide more money and less recruits. You cannot use harsh taxation for more than two months in a row - the people will revolt! Note that this taxation options apply only to the European population, colonies operate independetly.",
            /*step 18*/"Every time you finish issuing some orders, it is important to remember saving them. Only saved orders are processed. If you exit the game and re-enter it at a later time (between turns), all saved orders will still be in memory. You can review all orders given so far at any time by accessing the \"review orders\" menu, that organize orders given in different sections, according to the sequence of play.",
            /*step 19*/"Congratulations \"ruler name\". You have succesfully completed all steps of the walkthought tutorial. You are now familiar with the basics of the game. It took me 11 years of military career (1793-1804) to become Emperor of France. How long will it take you to master the game, overcome your opponents and become the most Glorious Emperor of Europe?",
            /*step 20*/""
    };

    final static TutorialAction[] tutorialActions10 = {
            new TutorialAction() {
                @Override
                public void execute() {/*step 1*/
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    group.clear();

                }
            },
            new TutorialAction() {/*step 2*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(StaticWidgets.TAXATION_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.ECONOMIC_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.TRADE_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.NEWS_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.MESSAGE_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.RELATION_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.EMBARK_IMAGE_OPTIONS);
                    highLightButton(StaticWidgets.SPYREPORT_IMAGE_OPTIONS);

                }
            },
            new TutorialAction() {/*step 3*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 4*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 5*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    MapStore.getInstance().getMapsView().goToPosition(RegionStore.getInstance().getRegionSectorsByRegionId(EUROPE)[10][5]);
                    highLightButton(StaticWidgets.SPYREPORT_IMAGE_OPTIONS);
                }
            },
            new TutorialAction() {/*step 6*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 7*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(StaticWidgets.ECONOMIC_IMAGE_OPTIONS);
                }
            },
            new TutorialAction() {/*step 8*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(StaticWidgets.TRADE_IMAGE_OPTIONS);
                }
            },
            new TutorialAction() {/*step 9*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 10*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 11*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 12*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 13*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 14*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 15*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);

                }
            },
            new TutorialAction() {/*step 16*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(StaticWidgets.TAXATION_IMAGE_OPTIONS);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, false);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 18*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                    highLightButton(GameStore.getInstance().getLayoutView().getUnitsMenu().getOrdersImg());
                    highLightButton(GameStore.getInstance().getLayoutView().getUnitsMenu().getSaveImg());
                }
            },
            new TutorialAction() {/*step 19*/
                @Override
                public void execute() {
                    infoPanel.setVisible(true, true);
                    infoPanel.setPosition(TutorialInfoPanel.ScreenPosition.RIGHT);
                }
            },
            new TutorialAction() {/*step 17*/
                @Override
                public void execute() {
                    infoPanel.setVisible(false, false);
                }
            }
    };
    boolean ordersViewDone = false;
    boolean saveOrdersDone = false;

    public void setSaveOrdersDone(boolean saveOrdersDone) {
        if (tutorialMode
                && month == 10
                && tutorialStep == 18) {
            this.saveOrdersDone = saveOrdersDone;
            if (ordersViewDone) {
                nextStep(false);
            }
        }
    }

    public void setOrdersViewDone(boolean ordersViewDone) {
        if (tutorialMode
                && month == 10
                && tutorialStep == 18) {
            this.ordersViewDone = ordersViewDone;
            if (saveOrdersDone) {
                nextStep(false);
            }
        }


    }

    static int lowOpacity = 50;
    static int highOpacity = 100;

    public static int getHighLightKey() {
        return ++countUpDownKey;
    }


    public static void highLightButton(Widget w) {
        final int key = getHighLightKey();
        countUpDowns.put(key, 1);
        final Fade f;
        f = new Fade(w.getElement());
        f.setDuration(1);
        f.addEffectCompletedHandler(new EffectCompletedHandler() {
            @Override
            public void onEffectCompleted(final EffectCompletedEvent event) {
                countUpDowns.put(key, countUpDowns.get(key) + 1);
                if (countUpDowns.get(key) <= 7) {
                    if (countUpDowns.get(key) % 2 == 1) {
                        f.setStartOpacity(lowOpacity);
                        f.setEndOpacity(highOpacity);
                    } else {
                        f.setStartOpacity(highOpacity);
                        f.setEndOpacity(lowOpacity);
                    }
                    f.play();
                }
            }
        });

        f.setStartOpacity(lowOpacity);
        f.setEndOpacity(highOpacity);
        f.play();
    }


    static int tutorialStep;
    private static TutorialInfoPanel infoPanel;
    private boolean tutorialMode = false;

    /**
     * Our instance of the GameStore
     */
    private static transient TutorialStore ourInstance = null;

    /**
     * Method returning the game store
     *
     * @return the GameStore
     */
    public static TutorialStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new TutorialStore();
        }
        return ourInstance;
    }

    public static void nextStep(final boolean forced) {
        forceBackward = false;
        forceForward = forced;
        final String[] texts;
        final TutorialAction[] actions;
        final int[] steps;
        switch (month) {
            case 1:
                texts = tutorialText1;
                actions = tutorialActions1;
                steps = tutorialSteps1;
                break;
            case 2:
                texts = tutorialText2;
                actions = tutorialActions2;
                steps = tutorialSteps2;
                break;
            case 3:
                texts = tutorialText3;
                actions = tutorialActions3;
                steps = tutorialSteps3;
                break;
            case 4:
                texts = tutorialText4;
                actions = tutorialActions4;
                steps = tutorialSteps4;
                break;
            case 5:
                texts = tutorialText5;
                actions = tutorialActions5;
                steps = tutorialSteps5;
                break;
            case 6:
                texts = tutorialText6;
                actions = tutorialActions6;
                steps = tutorialSteps6;
                break;
            case 7:
                texts = tutorialText7;
                actions = tutorialActions7;
                steps = tutorialSteps7;
                break;
            case 8:
                texts = tutorialText8;
                actions = tutorialActions8;
                steps = tutorialSteps8;
                break;
            case 9:
                texts = tutorialText9;
                actions = tutorialActions9;
                steps = tutorialSteps9;
                break;
            case 10:
                texts = tutorialText10;
                actions = tutorialActions10;
                steps = tutorialSteps10;
                break;
            default:
                group.clear();
                //if it is a turn after the last step of tutorial then do nothing
                return;
        }
        tutorialStep++;

        if (tutorialStep > maxStepSoFar) {
            maxStepSoFar = tutorialStep;
        }
        infoPanel.updateInfo(texts[tutorialStep-1], steps[tutorialStep - 1]);
        actions[tutorialStep - 1].execute();
    }

    public static void previousStep(final boolean forced) {
        forceForward = false;
        forceBackward = forced;
        final String[] texts;
        final int[] steps;
        final TutorialAction[] actions;
        switch (month) {
            case 1:
                texts = tutorialText1;
                actions = tutorialActions1;
                steps = tutorialSteps1;
                break;
            case 2:
                texts = tutorialText2;
                actions = tutorialActions2;
                steps = tutorialSteps2;
                break;
            case 3:
                texts = tutorialText3;
                actions = tutorialActions3;
                steps = tutorialSteps3;
                break;
            case 4:
                texts = tutorialText4;
                actions = tutorialActions4;
                steps = tutorialSteps4;
                break;
            case 5:
                texts = tutorialText5;
                actions = tutorialActions5;
                steps = tutorialSteps5;
                break;
            case 6:
                texts = tutorialText6;
                actions = tutorialActions6;
                steps = tutorialSteps6;
                break;
            case 7:
                texts = tutorialText7;
                actions = tutorialActions7;
                steps = tutorialSteps7;
                break;
            case 8:
                texts = tutorialText8;
                actions = tutorialActions8;
                steps = tutorialSteps8;
                break;
            case 9:
                texts = tutorialText9;
                actions = tutorialActions9;
                steps = tutorialSteps9;
                break;
            case 10:
                texts = tutorialText10;
                actions = tutorialActions10;
                steps = tutorialSteps10;
                break;
            default:
                //if not 1,2,3 or 4 then you have nothing to show.
                return;
        }
        tutorialStep--;

        if (tutorialStep > maxStepSoFar) {
            maxStepSoFar = tutorialStep;
        }
        infoPanel.updateInfo(texts[tutorialStep - 1], steps[tutorialStep - 1]);
        actions[tutorialStep - 1].execute();
    }

    public int getMaxStepSoFar() {
        return maxStepSoFar;
    }

    public int getTutorialStep() {
        return tutorialStep;
    }

    public void setTutorialStep(final int value) {
        this.tutorialStep = value;
    }

    public void setInfoPanel(final TutorialInfoPanel value) {
        this.infoPanel = value;
    }

    /**
     * If we are in a tutorial mode this will return true.
     *
     * @return True if tutorial mode enabled.
     */
    public boolean isTutorialMode() {
        return tutorialMode;
    }

    public boolean isTutorialTurn() {
        return (month <= 10);
    }

    public TutorialInfoPanel getInfoPanel() {
        return infoPanel;
    }

    public TutorialGroup getGroup() {
        return group;
    }

    public int getAnimateProdSite() {
        return animateProdSite;
    }

    public static int getAnimatedBarracksButton() {
        return animatedBarracksButton;
    }

    public static int getTotalStepsByMonth(int month) {
        switch (month) {
            case 1:
                return tutorialText1.length - 1;

            case 2:
                return tutorialText2.length - 1;

            case 3:
                return tutorialText3.length - 1;

            case 4:
                return tutorialText4.length - 1;

            case 5:
                return tutorialText5.length - 1;

            case 6:
                return tutorialText6.length - 1;

            case 7:
                return tutorialText7.length - 1;

            case 8:
                return tutorialText8.length - 1;

            case 9:
                return tutorialText9.length - 1;

            case 10:
                return tutorialText10.length - 1;

            default:
                return 0;
        }
    }

    public boolean checkCanSave() {
        if (tutorialMode) {
            int totalSteps = getTotalStepsByMonth(month);
            //first for 2d if counter panel is on check if user built all requested production sites.
            if (month == 2
                    && psCounter != null
                    && !psCounter.tutorialFinished()) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The tutorial is not finished yet, if you click save, the game will be scheduled to be processed and you will be moved to the next month. Are you sure you want to save the game?", true) {
                    public void onAccept() {
                        GameStore.getInstance().getLayoutView().getUnitsMenu().saveGame(true);
                    }
                };
                return false;
            }
            //for every other case include 2d round check if the user has done all tutorial steps.
            if ((month != 10 && tutorialStep < totalSteps)
                    || (month == 10 && (tutorialStep < totalSteps - 1))) {
                new ErrorPopup(ErrorPopup.Level.WARNING, "The tutorial is not finished yet, if you click save, the game will be scheduled to be processed and you will be moved to the next month. Are you sure you want to save the game?", true) {
                    public void onAccept() {
                        GameStore.getInstance().getLayoutView().getUnitsMenu().saveGame(true);
                    }
                };
                return false;
            }
        }

        return true;
    }

    public void setActionWidget(Widget actionWidget) {
        this.actionWidget = actionWidget;
    }

    public int getMonth() {
        return month;
    }

    public boolean isBrigadeDraggedFlag() {
        return brigadeDraggedFlag;
    }

    public void setBrigadeDraggedFlag(boolean brigadeDraggedFlag) {
        this.brigadeDraggedFlag = brigadeDraggedFlag;
    }

    public boolean isCommanderAssignedFlag() {
        return commanderAssignedFlag;
    }

    public void setCommanderAssignedFlag(boolean commanderAssignedFlag) {
        this.commanderAssignedFlag = commanderAssignedFlag;
    }
}
