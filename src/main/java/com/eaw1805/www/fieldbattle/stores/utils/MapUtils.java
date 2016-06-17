package com.eaw1805.www.fieldbattle.stores.utils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.touch.client.Point;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.field.FieldBattleMapDTO;
import com.eaw1805.data.dto.web.field.FieldBattlePositionDTO;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;
import com.eaw1805.data.dto.web.field.LongRangeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.MeleeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.RallyLogEntryDTO;
import com.eaw1805.data.dto.web.field.StructureStatusDTO;
import com.eaw1805.www.client.events.loading.ArmiesLoadedHandler;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedEvent;
import com.eaw1805.www.fieldbattle.events.loading.LoadingEventManager;
import com.eaw1805.www.fieldbattle.stores.*;
import com.eaw1805.www.fieldbattle.stores.calculators.MapStore;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.BrigadeFullInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.BrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.OrderInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.PlaybackBrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.*;
import com.eaw1805.www.shared.stores.GameStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils implements MapConstants, ClearAble {

    final String host = "http://static.eaw1805.com";
//    final String host = "http://46.103.184.99";
//    final String host = "http://localhost";

    final MainPanel main = MainPanel.getInstance();

    int mapWidth = 0;
    int mapHeight = 0;
    int numTilesX = 0;
    int numTilesY = 0;
    Group positionGroup = new Group();
    Group coordinatesGroup = new Group();
    final Map<Integer, Map<Integer, ArmyOptionsGroup>> brigadeToGroup = new HashMap<Integer, Map<Integer, ArmyOptionsGroup>>();
    final Map<Integer, Map<Integer, FieldBattleMapGroup>> brigadeToOrdersGroup = new HashMap<Integer, Map<Integer, FieldBattleMapGroup>>();
    final Map<Integer, List<PopupPanel>> brigadeToMenu = new HashMap<Integer, List<PopupPanel>>();

    Image pointerImg1;
    Image pointerImg2;
    Line arrow;
    final Map<Integer, Group> roundToPlaybackAdditionsGroup = new HashMap<Integer, Group>();
    final Map<Integer, Group> roundToMapTilesGroup = new HashMap<Integer, Group>();
    final Map<Integer, Group> roundToMoveLines = new HashMap<Integer, Group>();
    final Map<Integer, Group> roundToFireLines = new HashMap<Integer, Group>();
    final Map<Integer, Group> roundToStructuresGroup = new HashMap<Integer, Group>();
    final Map<Integer, Group> roundToCorpsesGroup = new HashMap<Integer, Group>();
    Group structuresGroup = new Group();
    int[] centerSetupAreaCoords = new int[2];
    Text levelLabel = new Text(0, 0, "");
    Text spLabel = new Text(0, 0, "");
    Text buildingLabel = new Text(0, 0, "");
    final List<PopupPanel> lastOpenedMenu = new ArrayList<PopupPanel>();
    final Circle selectionArea = new Circle(0, 0, (TILE_WIDTH / 2) + 14);

    private final Group gridGroup = new Group();


    GroupMap<Integer> staticGroup = new GroupMap<Integer>();
    final Map<Integer, Group> roundToArmiesGroup = new HashMap<Integer, Group>();
    final int[] enemyTopLeft = {Integer.MAX_VALUE, Integer.MAX_VALUE};//this is the top left corner for the setup area
    final int[] enemyBottomRight = {0, 0};//this is the bottom right corner for the setup area

    //    final String host = "http://localhost";
    public void initMap(final FieldBattleMapDTO map) {

        numTilesX = map.getSectors().length;
        numTilesY = map.getSectors()[0].length;
        main.getDrawingArea().setSize((map.getSectors().length + 2 * SIDE_OFFSET) * TILE_WIDTH + "px", (map.getSectors()[0].length + 2 * SIDE_OFFSET) * TILE_HEIGHT + "px");
        mapWidth = (map.getSectors().length + 2 * SIDE_OFFSET) * TILE_WIDTH;
        mapHeight = (map.getSectors()[0].length + 2 * SIDE_OFFSET) * TILE_HEIGHT;
        updateMapTiles(map, false);


        pointerImg1 = new Image(0, 0, 32, 32, host + "/images/field/pointerDown.png");
        pointerImg1.setStyleName("disablePointerEvents");
        pointerImg2 = new Image(0, 0, 32, 32, host + "/images/field/pointerDown.png");
        pointerImg2.setStyleName("disablePointerEvents");
        arrow = new Line(0, 0, 0, 0);
        arrow.setStrokeColor("red");


        selectionArea.setFillOpacity(0.3);
        selectionArea.setFillColor("#FDD017");

        RootPanel.get().addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                hideLastBrigadeMenu();
            }
        }, MouseDownEvent.getType());
    }

    public void initArmyGroups() {
        if (BaseStore.getInstance().isStartRound()) {
            roundToArmiesGroup.put(0, new Group());
            brigadeToGroup.put(0, new HashMap<Integer, ArmyOptionsGroup>());
            brigadeToOrdersGroup.put(0, new HashMap<Integer, FieldBattleMapGroup>());
            //finally add the brigades...
            for (BrigadeDTO brigade : ArmyStore.getInstance().getAlliedBrigades()) {
                if (brigade.isPlacedOnFieldMap()) {
                    addArmyImage(brigade, false, false, 0, false);
                }
            }
            MainPanel.getInstance().getDrawingArea().add(roundToArmiesGroup.get(0));
        } else {
            new DelayIterator(-1, PlaybackStore.getInstance().getRounds(), 1) {

                @Override
                public void executeStep() {
                    roundToArmiesGroup.put(ITERATE_INDEX, new Group());
                    roundToArmiesGroup.get(ITERATE_INDEX).setVisible(false);
                    brigadeToGroup.put(ITERATE_INDEX, new HashMap<Integer, ArmyOptionsGroup>());
                    brigadeToOrdersGroup.put(ITERATE_INDEX, new HashMap<Integer, FieldBattleMapGroup>());


                    if (BaseStore.getInstance().isLastHalfRound(ITERATE_INDEX + 1)) {//if it is the last round of playback... add the stored brigade objects on map.
                        for (BrigadeDTO brigade : ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId())) {
                            if (!brigade.isRouted()) {
                                addArmyImage(brigade, false, true, ITERATE_INDEX, false);
                            }
                        }
                    }

                    for (BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(ITERATE_INDEX).getAllBrigades()) {
                        //if it is not the last round... or the brigade doesn't belong to the player.. add the reported brigade
                        if (!BaseStore.getInstance().isLastHalfRound(ITERATE_INDEX + 1) || brigade.getNationId() != BaseStore.getInstance().getNationId()) {
                            if (!brigade.isRouted()) {
                                addArmyImage(brigade, false, true, ITERATE_INDEX, false);
                            }
                        }
                    }

                    MainPanel.getInstance().getDrawingArea().add(roundToArmiesGroup.get(ITERATE_INDEX));
                    MainPanel.getInstance().getLoading().addLoadingStep("Half round " + ITERATE_INDEX + " initialized");
                }

                @Override
                public void executeLast() {
                    MainPanel.getInstance().getLogPanel().initPanel();
                    MainPanel.getInstance().getPlayback().goToRound((BaseStore.getInstance().getRound() + 1) * 2);
                }
            }.run();


        }
    }


    public Line getBrigadeMovementLine(final BrigadeDTO brigade, final int round) {
        BrigadeDTO brig2;
        if (round < PlaybackStore.getInstance().getRounds() - 1) {
            brig2 = PlaybackStore.getInstance().getBrigadeByIdRound(brigade.getBrigadeId(), round + 1);
            if (brig2 == null) {
                brig2 = brigade;
            }
        } else {
            brig2 = brigade;
        }
        final Line line = new UntouchableLine(getPointX(brigade.getFieldBattleX()) + TILE_WIDTH / 2, getPointY(brigade.getFieldBattleY()) + TILE_HEIGHT / 2, getPointX(brig2.getFieldBattleX()) + TILE_WIDTH / 2, getPointY(brig2.getFieldBattleY()) + TILE_HEIGHT / 2);
        line.setStrokeColor(BaseStore.getInstance().getColorByNation(brigade.getNationId()));
        return line;
    }

    public void addArrow(int x1, int y1, int x2, int y2) {
        int mx1 = getPointX(x1);
        int my1 = getPointY(y1);
        int mx2 = getPointX(x2);
        int my2 = getPointY(y2);

        arrow.setX1(mx1 + TILE_WIDTH / 2);
        arrow.setX2(mx2 + TILE_WIDTH / 2);
        arrow.setY1(my1 + TILE_HEIGHT / 2);
        arrow.setY2(my2 + TILE_HEIGHT / 2);

        main.getDrawingArea().add(arrow);
    }

    public void addPointer1(BrigadeDTO pos1) {
        pointerImg1.setX(getPointX(pos1.getFieldBattleX()) + 16);
        pointerImg1.setY(getPointY(pos1.getFieldBattleY() - 1) + 32);
        main.getDrawingArea().add(pointerImg1);
    }

    /**
     * This function ensures that everytime is called the pointers will be visible on top of other elements.
     */
    public void reAddPointersIfAny() {
        if (pointerImg1.isAttached()) {
            main.getDrawingArea().remove(pointerImg1);
            main.getDrawingArea().add(pointerImg1);
        }
        if (pointerImg2.isAttached()) {
            main.getDrawingArea().remove(pointerImg2);
            main.getDrawingArea().add(pointerImg2);
        }
    }

    public void addPointer1IfCurrentRound(BrigadeDTO pos1, final int round) {
        if (MainPanel.getInstance().getPlayback().getRound() == round) {
            pointerImg1.setX(getPointX(pos1.getFieldBattleX()) + 16);
            pointerImg1.setY(getPointY(pos1.getFieldBattleY() - 1) + 32);
            main.getDrawingArea().add(pointerImg1);
        }
    }

    public void addPointer2(BrigadeDTO pos2) {
        pointerImg2.setX(getPointX(pos2.getFieldBattleX()) + 16);
        pointerImg2.setY(getPointY(pos2.getFieldBattleY() - 1) + 32);
        main.getDrawingArea().add(pointerImg2);
    }

    public void addPointer2IfCurrentRound(BrigadeDTO pos2, final int round) {
        if (MainPanel.getInstance().getPlayback().getRound() == round) {
            pointerImg2.setX(getPointX(pos2.getFieldBattleX()) + 16);
            pointerImg2.setY(getPointY(pos2.getFieldBattleY() - 1) + 32);
            main.getDrawingArea().add(pointerImg2);
        }
    }

    public void clearPointers() {
        if (pointerImg1.isAttached()) {
            main.getDrawingArea().remove(pointerImg1);
        }
        if (pointerImg2.isAttached()) {
            main.getDrawingArea().remove(pointerImg2);
        }
        if (arrow.isAttached()) {
            main.getDrawingArea().remove(arrow);
        }
    }

    public void addPointers(BrigadeDTO pos1, BrigadeDTO pos2) {
        addPointer1(pos1);
        addPointer2(pos2);
    }

    public void addPointersIfCurrentRound(BrigadeDTO pos1, BrigadeDTO pos2, final int round) {
        if (MainPanel.getInstance().getPlayback().getRound() == round) {
            addPointer1(pos1);
            addPointer2(pos2);
            addArrow(pos1.getFieldBattleX(), pos1.getFieldBattleY(), pos2.getFieldBattleX(), pos2.getFieldBattleY());
        }
    }


    public void updateMapTiles(final FieldBattleMapDTO map, final boolean isPlayback) {
        roundToMapTilesGroup.put(-1, new Group());
        roundToStructuresGroup.put(-1, new Group());
        roundToCorpsesGroup.put(-1, new Group());
        final Group groupMap = new Group();
        int[] topLeft = {Integer.MAX_VALUE, Integer.MAX_VALUE};//this is the top left corner for the setup area
        int[] bottomRight = {0, 0};//this is the bottom right corner for the setup area

        int[] alliesTopLeft = {Integer.MAX_VALUE, Integer.MAX_VALUE};//this is the top left corner for the setup area
        int[] alliesBottomRight = {0, 0};//this is the bottom right corner for the setup area




        groupMap.add(new Image(getPointX(0), getPointX(0), map.getSectors().length * TILE_WIDTH, map.getSectors()[0].length * TILE_HEIGHT, "http://direct.eaw1805.com/fieldmaps/s" + BaseStore.getInstance().getScenarioId() + "/m" + map.getBattleId() + ".jpg"));

        for (FieldBattleSectorDTO[] row : map.getSectors()) {
            for (FieldBattleSectorDTO sector : row) {
                final Rectangle gridRect = new Rectangle(getPointX(sector.getX()), getPointY(sector.getY()), TILE_WIDTH, TILE_HEIGHT);
                gridRect.setFillOpacity(0.0);
                gridGroup.add(gridRect);


                final Group group = new Group();

                if (sector.hasBridge()) {
                    boolean upDown = sector.getBridgeDirection(map.getSectors());
                    group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/brigdges/rb_TB.png"));
                    group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/brigdges/b_TB.png"));

                }

                if (sector.hasVillage()) {
                    int rand = Random.nextInt(4) + 1;
                    roundToStructuresGroup.get(-1).add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/village/Village0" + rand + ".png"));
                }

                if (sector.hasTown()) {
                    additionTownBaseImages(sector, map.getSectors(), group);
                    int rand = Random.nextInt(4) + 1;
                    roundToStructuresGroup.get(-1).add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/towns/town0" + rand + ".png"));
                }
                if (sector.hasChateau()) {
                    roundToStructuresGroup.get(-1).add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/buildings/Chateau.png"));
                }
                if (sector.hasEntrenchment()) {
                    roundToStructuresGroup.get(-1).add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/buildings/Entrenchment.png"));
                }
                if (sector.hasWall()) {
                    additionWallImages(sector, map.getSectors(), group);
                }


                if (sector.isStrategicPoint()) {
                    final Rectangle spRectangle = new Rectangle(getPointX(sector.getX()), getPointY(sector.getY()), TILE_WIDTH, TILE_HEIGHT);
                    spRectangle.setFillOpacity(0.2);
                    spRectangle.setFillColor("#FDD017");
                    group.add(spRectangle);
                    if (sector.getCurrentHolder() != 0) {
                        roundToMapTilesGroup.get(-1).add(new Image(getPointX(sector.getX()), getPointY(sector.getY()), 20, 15, "http://static.eaw1805.com/images/nations/nation-" + sector.getCurrentHolder() + "-36.png"));
                    }
                }


                if (sector.getNationSetup() == BaseStore.getInstance().getNationId()) {
                    if (sector.getX() < topLeft[0]) {
                        topLeft[0] = sector.getX();
                    }
                    if (sector.getY() < topLeft[1]) {
                        topLeft[1] = sector.getY();
                    }
                    if (sector.getX() > bottomRight[0]) {
                        bottomRight[0] = sector.getX();
                    }
                    if (sector.getY() > bottomRight[1]) {
                        bottomRight[1] = sector.getY();
                    }
                } else if (BaseStore.getInstance().getAlliedNations().contains(Integer.valueOf(sector.getNationSetup()))) {
                    if (sector.getX() < alliesTopLeft[0]) {
                        alliesTopLeft[0] = sector.getX();
                    }
                    if (sector.getY() < alliesTopLeft[1]) {
                        alliesTopLeft[1] = sector.getY();
                    }
                    if (sector.getX() > alliesBottomRight[0]) {
                        alliesBottomRight[0] = sector.getX();
                    }
                    if (sector.getY() > alliesBottomRight[1]) {
                        alliesBottomRight[1] = sector.getY();
                    }
                }

                if (BaseStore.getInstance().getEnemyNations().contains(Integer.valueOf(sector.getNationSetup()))) {
                    if (sector.getX() < enemyTopLeft[0]) {
                        enemyTopLeft[0] = sector.getX();
                    }
                    if (sector.getY() < enemyTopLeft[1]) {
                        enemyTopLeft[1] = sector.getY();
                    }
                    if (sector.getX() > enemyBottomRight[0]) {
                        enemyBottomRight[0] = sector.getX();
                    }
                    if (sector.getY() > enemyBottomRight[1]) {
                        enemyBottomRight[1] = sector.getY();
                    }
                }
                groupMap.add(group);
            }
        }



        final Rectangle setupArea = new Rectangle(getPointX(topLeft[0]), getPointY(topLeft[1]), (bottomRight[0] - topLeft[0] + 1) * TILE_WIDTH, (bottomRight[1] - topLeft[1] + 1) * TILE_HEIGHT);
        centerSetupAreaCoords[0] = (bottomRight[0] + topLeft[0]) / 2;
        centerSetupAreaCoords[1] = (bottomRight[1] + topLeft[1]) / 2;
        setupArea.setFillOpacity(0.0);
        setupArea.setStrokeColor("white");
        setupArea.setStrokeWidth(3);
        setupArea.setStyleName("fieldBattle-setup-area");
        groupMap.add(setupArea);

        final Rectangle alliesSetupArea = new Rectangle(getPointX(alliesTopLeft[0]), getPointY(alliesTopLeft[1]), (alliesBottomRight[0] - alliesTopLeft[0] + 1) * TILE_WIDTH, (alliesBottomRight[1] - alliesTopLeft[1] + 1) * TILE_HEIGHT);
        alliesSetupArea.setFillOpacity(0.0);
        alliesSetupArea.setStrokeColor("white");
        alliesSetupArea.setStrokeWidth(3);
        alliesSetupArea.setStyleName("fieldBattle-setup-area");
        groupMap.add(alliesSetupArea);

        final Rectangle enemySetupArea = new Rectangle(getPointX(enemyTopLeft[0]), getPointY(enemyTopLeft[1]), (enemyBottomRight[0] - enemyTopLeft[0] + 1) * TILE_WIDTH, (enemyBottomRight[1] - enemyTopLeft[1] + 1) * TILE_HEIGHT);
        enemySetupArea.setFillOpacity(0.0);
        enemySetupArea.setStrokeColor("red");
        enemySetupArea.setStrokeWidth(3);
        enemySetupArea.setStyleName("fieldBattle-setup-area");
        groupMap.add(enemySetupArea);
//        setupArea.setStylePrimaryName("fieldBattle-setup-area");
        if (!isPlayback) {
            for (FieldBattleSectorDTO[] row : map.getSectors()) {
                for (FieldBattleSectorDTO sector : row) {
                    final Rectangle rect = new Rectangle(getPointX(sector.getX()), getPointY(sector.getY()), TILE_WIDTH, TILE_HEIGHT);
                    rect.setFillColor("grey");
                    if (sector.getNationSetup() == BaseStore.getInstance().getNationId()) {
                        rect.setFillOpacity(0.0);
                    } else {
                        rect.setFillOpacity(0.4);
                    }
                    positionGroup.add(rect);
                    final Rectangle coordsRect = new Rectangle(getPointX(sector.getX()), getPointY(sector.getY()), TILE_WIDTH, TILE_HEIGHT);
                    coordsRect.setFillOpacity(0.0);
                    coordinatesGroup.add(coordsRect);
                    final Text coords = new Text(getPointX(sector.getX()), getPointY(sector.getY()) + 20, sector.getX() + ", " + sector.getY());
                    coords.setFontSize(7);
                    coordinatesGroup.add(coords);
                }
            }
            groupMap.add(positionGroup);
            hidePositionGroup();

            groupMap.add(coordinatesGroup);
            hideCoordinatesGroup();
        }
        main.getDrawingArea().add(groupMap);
        main.getDrawingArea().add(structuresGroup);
        structuresGroup.add(roundToStructuresGroup.get(-1));
        main.getDrawingArea().add(roundToMapTilesGroup.get(-1));
        main.getDrawingArea().add(roundToCorpsesGroup.get(-1));
        //finally add the rectangle
        final Rectangle rectangle = new Rectangle(0, 0, TILE_WIDTH, TILE_HEIGHT);
        rectangle.setFillOpacity(0.2);
        rectangle.setFillColor("red");
        main.getDrawingArea().add(rectangle);

        main.getDrawingArea().setSelectionRectangle(rectangle);
        main.getDrawingArea().add(levelLabel);
        levelLabel.setFillColor("white");
        levelLabel.setStrokeColor("");
        levelLabel.setFontSize(14);
        main.getDrawingArea().add(spLabel);
        spLabel.setFillColor("gold");
        spLabel.setStrokeColor("");
        spLabel.setFontSize(14);
        spLabel.setText("Strategic Point");
        main.getDrawingArea().add(buildingLabel);
        buildingLabel.setFillColor("gold");
        buildingLabel.setStrokeColor("");
        buildingLabel.setFontSize(14);
        buildingLabel.setText("Building goes here");
        MainPanel.getInstance().getDrawingArea().add(gridGroup);
        setShowGrid(MainPanel.getInstance().getMiniMap().getSettingsPanel().gridLines.isChecked());
        MainPanel.getInstance().getDrawingArea().add(staticGroup);

    }

    public void placeDummyEnememies() {
        int row;
        int column;
        row = enemyTopLeft[1];
        column = enemyTopLeft[0];
        int modifier = 1;
        if (row == 0) {
            row = enemyBottomRight[1];
            modifier = -1;
        }
        if (BaseStore.getInstance().isStartRound()) {
            for (final BrigadeDTO brigade : ArmyStore.getInstance().getEnemyBrigades()) {
                //some dummy data so they can be placed properly.
                brigade.setFieldBattleX(column);
                brigade.setFieldBattleY(row);
                brigade.setPlacedOnFieldMap(true);
                column++;
                if (column > enemyBottomRight[0]) {
                    column = enemyTopLeft[0];
                    row += modifier;
                }
                try {
                addArmyImage(brigade, false, false);
                } catch (Exception e) {
                    Window.alert("f? " + e.toString());
                }
            }
        }
    }

    public void setShowCorpses(final boolean show) {
        if (!BaseStore.getInstance().isStartRound()) {
            if (show) {
                main.getDrawingArea().add(roundToCorpsesGroup.get(MainPanel.getInstance().getPlayback().getRound() - 1));
            } else {
                main.getDrawingArea().remove(roundToCorpsesGroup.get(MainPanel.getInstance().getPlayback().getRound() - 1));
            }

        }
    }

    public void setShowAlliedBrigades(final boolean show) {

    }

    public void setShowBrigades(final boolean show) {
        if (show) {
            if (BaseStore.getInstance().isStartRound()) {
                roundToArmiesGroup.get(0).setVisible(true);
            } else {
                roundToArmiesGroup.get(MainPanel.getInstance().getPlayback().getRound() - 1).setVisible(true);
            }
        } else {
            for (Group group : roundToArmiesGroup.values()) {
                group.setVisible(false);
            }
        }
    }

    public void setShowGrid(final boolean show) {
        gridGroup.setVisible(show);
    }

    public void updateLevelLabel(final FieldBattleSectorDTO sector, final int round) {

        if (sector.isStrategicPoint()) {
            levelLabel.setFillColor("gold");
            buildingLabel.setFillColor("gold");
            if (spLabel.isAttached()) {
                main.getDrawingArea().remove(spLabel);
            }
            main.getDrawingArea().add(spLabel);
        } else {
            levelLabel.setFillColor("white");
            buildingLabel.setFillColor("white");
            if (spLabel.isAttached()) {
                main.getDrawingArea().remove(spLabel);
            }
        }

        if (!sector.getGroundType().isEmpty()) {
            levelLabel.setText((sector.getX() + 1) + "/ " + (sector.getY() + 1) + " - level " + sector.getAltitude() + " - " + sector.getGroundType());
            levelLabel.setX(getPointX(sector.getX()) - 36);
        } else {
            levelLabel.setText((sector.getX() + 1) + "/ " + (sector.getY() + 1) + " - level " + sector.getAltitude());

            levelLabel.setX(getPointX(sector.getX()) - 9);
        }
        levelLabel.setY(getPointY(sector.getY()) - 5);

        //re-add it so it will be on top of everything else
        main.getDrawingArea().remove(levelLabel);
        main.getDrawingArea().add(levelLabel);

        spLabel.setX(getPointX((sector.getX())) - 12);
        spLabel.setY(getPointY((sector.getY())) + TILE_HEIGHT + 17);
        if (BaseStore.getInstance().isStartRound()) {
            buildingLabel.setText(sector.getStructure());
        } else {
            buildingLabel.setText(PlaybackStore.getInstance().getStructureNameByXYRound(sector.getX(), sector.getY(), round));
        }
        buildingLabel.setX(getPointX((sector.getX())) - 12);
        if (sector.isStrategicPoint()) {
            buildingLabel.setY(getPointY((sector.getY())) + TILE_HEIGHT + 33);
        } else {
            buildingLabel.setY(getPointY((sector.getY())) + TILE_HEIGHT + 17);
        }
        main.getDrawingArea().remove(buildingLabel);
        main.getDrawingArea().add(buildingLabel);

    }

    public void additionTownBaseImages(final FieldBattleSectorDTO sector, final FieldBattleSectorDTO[][] sectors, final Group group) {
        int sizeX = sectors.length;
        int sizeY = sectors[0].length;
        if (sector.getAltitude() == 1) {
            group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/towns/townbase/TB_.png"));
        } else {
            String imgName = "TB_";
            //for up right bottom left sides
            if (sector.getY() > 0 && sectors[sector.getX()][sector.getY() - 1].getAltitude() < sector.getAltitude()) {
                imgName += "T";
            }
            if (sector.getX() + 1 < sizeX && sectors[sector.getX() + 1][sector.getY()].getAltitude() < sector.getAltitude()) {
                imgName += "R";
            }
            if (sector.getY() + 1 < sizeY && sectors[sector.getX()][sector.getY() + 1].getAltitude() < sector.getAltitude()) {
                imgName += "B";
            }
            if (sector.getX() > 0 && sectors[sector.getX() - 1][sector.getY()].getAltitude() < sector.getAltitude()) {
                imgName += "L";
            }


            //for corner sides
            String code = imgName.split("_")[1];
            if (code == null) {
                code = "";
            }
            if ((code.contains("T") || code.contains("L")) || (sector.getX() > 0 && sector.getY() > 0 && sectors[sector.getX() - 1][sector.getY() - 1].getAltitude() < sector.getAltitude())) {
                imgName += "1";
            }
            if ((code.contains("T") || code.contains("R")) || (sector.getX() + 1 < sizeX && sector.getY() > 0 && sectors[sector.getX() + 1][sector.getY() - 1].getAltitude() < sector.getAltitude())) {
                imgName += "2";
            }
            if ((code.contains("R") || code.contains("B")) || (sector.getX() + 1 < sizeX && sector.getY() + 1 < sizeY && sectors[sector.getX() + 1][sector.getY() + 1].getAltitude() < sector.getAltitude())) {
                imgName += "3";
            }
            if ((code.contains("B") || code.contains("L")) || (sector.getX() > 0 && sector.getY() + 1 < sizeY && sectors[sector.getX() - 1][sector.getY() + 1].getAltitude() < sector.getAltitude())) {
                imgName += "4";
            }
            group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/towns/townbase/" + imgName + ".png"));
        }

    }


    public void showOriginalMapTiles() {
        for (Group group : roundToMapTilesGroup.values()) {
            if (group.isAttached()) {
                main.getDrawingArea().remove(group);
            }
        }
        roundToMapTilesGroup.get(-1).setVisible(true);
        for (Group group : roundToPlaybackAdditionsGroup.values()) {
            if (group.isAttached()) {
                main.getDrawingArea().remove(group);
            }
        }

    }

    public void showPlaybackAdditionsByRound(final int round) {
        //be sure field is clear first.
        roundToStructuresGroup.get(-1).setVisible(false);
        for (Group group : roundToStructuresGroup.values()) {
            if (group.isAttached()) {
                structuresGroup.remove(group);
            }
        }
        //then add the correct group.
        structuresGroup.add(roundToStructuresGroup.get(round));

        //be sure field is clear first.
        for (Group group : roundToCorpsesGroup.values()) {
            if (group.isAttached()) {
                main.getDrawingArea().remove(group);
            }
        }
        //then add the correct group.
        main.getDrawingArea().add(roundToCorpsesGroup.get(round));

        //be sure field is clear first.
        roundToMapTilesGroup.get(-1).setVisible(false);
        for (Group group : roundToMapTilesGroup.values()) {
            if (group.isAttached()) {
                main.getDrawingArea().remove(group);
            }
        }
        //then add the correct group.
        main.getDrawingArea().add(roundToMapTilesGroup.get(round));

        //be sure field is clear first.
        for (Group group : roundToPlaybackAdditionsGroup.values()) {
            if (group.isAttached()) {
                main.getDrawingArea().remove(group);
            }
        }
        //then add the correct group.
        main.getDrawingArea().add(roundToPlaybackAdditionsGroup.get(round));
        reAddPointersIfAny();
        if (BaseStore.getInstance().isLastHalfRound(round + 1)) {
            staticGroup.setVisible(true);
        } else {
            staticGroup.setVisible(false);
        }
    }

    public void addOrderChangedGraphic(final BrigadeDTO brigade) {
        final Image img = new Image(getPointX(brigade.getFieldBattleX()), getPointY(brigade.getFieldBattleY()), 64, 64, "http://static.eaw1805.com/images/field/map/various/green-circle.png");
        staticGroup.add(brigade.getBrigadeId(), img);
    }

    public void removeOrderChangedGraphic(final BrigadeDTO brigade) {
        staticGroup.remove(brigade.getBrigadeId());
    }


    public void showFireLines() {
        for (Group fireLines : roundToFireLines.values()) {
            fireLines.setVisible(true);
        }
    }

    public void hideFireLines() {
        for (Group fireLines : roundToFireLines.values()) {
            fireLines.setVisible(false);
        }
    }

    public void showMoveLines() {
        for (Group moveLines : roundToMoveLines.values()) {
            moveLines.setVisible(true);
        }
    }

    public void hideMoveLines() {
        for (Group moveLines : roundToMoveLines.values()) {
            moveLines.setVisible(false);
        }
    }

    final Map<String, Map<String, String>> positionToStructure = new HashMap<String, Map<String, String>>();

    public String getStructureByPosition(StructureStatusDTO structure) {
        final String pos = structure.getX() + "/" + structure.getY();
        if (!positionToStructure.containsKey(pos)) {
            positionToStructure.put(pos, new HashMap<String, String>());
        }
        if (!positionToStructure.get(pos).containsKey(structure.getType())) {
            if (structure.isVillage()) {

                int rand = Random.nextInt(4) + 1;
                positionToStructure.get(pos).put(structure.getType(), host + "/images/field/map/village/Village0" + rand + ".png");
            }
            if (structure.isTown()) {
                int rand = Random.nextInt(4) + 1;
                positionToStructure.get(pos).put(structure.getType(), host + "/images/field/map/towns/town0" + rand + ".png");
            }
            if (structure.isChateau()) {
                positionToStructure.get(pos).put(structure.getType(), host + "/images/field/map/buildings/Chateau.png");
            }
            if (structure.isEntrenchment()) {
                positionToStructure.get(pos).put(structure.getType(), host + "/images/field/map/buildings/Entrenchment.png");
            }
        }
        return positionToStructure.get(pos).get(structure.getType());
    }


    public void initPlaybackAdditions() {

        for (int round = -1; round < PlaybackStore.getInstance().getRounds(); round++) {
            roundToStructuresGroup.put(round, new Group());
            for (StructureStatusDTO structure : PlaybackStore.getInstance().getStructuresByRound(round)) {
                roundToStructuresGroup.get(round).add(new Image(getPointX(structure.getX()), getPointX(structure.getY()), TILE_WIDTH, TILE_HEIGHT, getStructureByPosition(structure)));
            }

            roundToCorpsesGroup.put(round, new Group());
            for (Map.Entry<FieldBattlePositionDTO, Integer> entry : PlaybackStore.getInstance().getCorpsesByRound(round).entrySet()) {
                if (PlaybackStore.getInstance().getBrigadeByRoundPosition(round, entry.getKey()) == null) {

                    if (entry.getValue() > 0) {

                        roundToCorpsesGroup.get(round).add(new Image(getPointX(entry.getKey().getX()), getPointY(entry.getKey().getY()), 64, 64,
                                "http://static.eaw1805.com/images/field/map/corpses/CorpseDown0" + ((entry.getKey().getX() + entry.getKey().getY()) % 2 + 1) +"_" + getLetterByNationId(entry.getValue()) + ".png"));
                    } else {
                        roundToCorpsesGroup.get(round).add(new Image(getPointX(entry.getKey().getX()), getPointY(entry.getKey().getY()), 64, 64,
                                "http://static.eaw1805.com/images/field/map/corpses/CorpseUp0" + ((entry.getKey().getX() + entry.getKey().getY()) % 2 + 1) +"_" + getLetterByNationId(-entry.getValue()) + ".png"));
                    }

                }
            }

            roundToMapTilesGroup.put(round, new Group());
            for (FieldBattleSectorDTO sector : PlaybackStore.getInstance().getStrategicPointsByRound(round)) {
                initStrategicPoints(round, sector);
            }

            roundToPlaybackAdditionsGroup.put(round, new Group());
            roundToMoveLines.put(round, new Group());
            roundToFireLines.put(round, new Group());
            for (BrigadeDTO brigade : PlaybackStore.getInstance().getRoundStatistics(round).getAllBrigades()) {
                addPlaybackAdditions(brigade, round);
            }
            roundToPlaybackAdditionsGroup.get(round).add(roundToMoveLines.get(round));
            roundToPlaybackAdditionsGroup.get(round).add(roundToFireLines.get(round));

        }
    }

    public void initStrategicPoints(final int round, final FieldBattleSectorDTO sector) {
        if (sector.getCurrentHolder() != 0) {
            roundToMapTilesGroup.get(round).add(new Image(getPointX(sector.getX()), getPointY(sector.getY()), 20, 15, "http://static.eaw1805.com/images/nations/nation-" + sector.getCurrentHolder() + "-36.png"));
        }
    }

    public void addPlaybackAdditions(final BrigadeDTO brigade, final int round) {
        int brigX = getPointX(brigade.getFieldBattleX());
        int brigY = getPointY(brigade.getFieldBattleY());
        roundToPlaybackAdditionsGroup.get(round).add(new UntouchableImage(brigX, brigY, 20, 15, "http://static.eaw1805.com/images/nations/nation-" + brigade.getNationId() + "-36.png"));
        roundToMoveLines.get(round).add(getBrigadeMovementLine(brigade, round));
        int maxHeads = 0;
        int actualHeads = 0;
        for (BattalionDTO battalion : brigade.getBattalions()) {
            if (battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_MOROCCO
                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_OTTOMAN
                    || battalion.getEmpireArmyType().getNationId() == NationConstants.NATION_EGYPT) {
                maxHeads += 1000;
            } else {
                maxHeads += 800;
            }
            actualHeads += battalion.getHeadcount();
        }

        int powerLineHeight = 43;


        double power = (double) actualHeads * 10.0 / (double) maxHeads;
        int powerInt = (int) Math.ceil(power);
        String powerStr = String.valueOf(powerInt);
        if (powerInt < 10) {
            powerStr = "0" + powerStr;
        }

        final Image powerImg = new UntouchableImage(brigX + 59, brigY + 5, 6, 43, host + "/images/field/map/health/Health" + powerStr + ".png");
        roundToPlaybackAdditionsGroup.get(round).add(powerImg);
//        roundToPlaybackAdditionsGroup.get(round).add(powerLine);

        final MeleeAttackLogEntryDTO melee = PlaybackStore.getInstance().getMeleeByBrigadeRound(brigade.getBrigadeId(), round);
        if (melee != null) {
            final BrigadeDTO target = PlaybackStore.getInstance().getBrigadeByIdRound(melee.getTargetBrigadeId(), round);
            if (target != null) {
                int offsetX = 0;
                int offsetY = 0;
                if (target.getFieldBattleY() > brigade.getFieldBattleY()) {//is down
                    offsetX = 22;
                } else if (target.getFieldBattleY() < brigade.getFieldBattleY()) {//is up
                    offsetY = 44;
                    offsetX = 22;
                } else if (target.getFieldBattleX() > brigade.getFieldBattleX()) {//is right
                    offsetY = 22;
                } else {
                    offsetX = 44;
                    offsetY = 22;
                }
                final Image meleeImg = new UntouchableImage(getPointX(target.getFieldBattleX()) + offsetX, getPointY(target.getFieldBattleY()) + offsetY, 20, 20, host + "/images/field/map/various/MeleeMarker.png");
                roundToPlaybackAdditionsGroup.get(round).add(meleeImg);
            }
        }
        final LongRangeAttackLogEntryDTO longRangeEntry = PlaybackStore.getInstance().getLongRangeByBrigadeRound(brigade.getBrigadeId(), round);
        if (longRangeEntry != null) {
            final BrigadeDTO target = PlaybackStore.getInstance().getBrigadeByIdRound(longRangeEntry.getTargetBrigadeId(), round);
            if (target != null) {
                int offsetX = 22;
                int offsetY = 22;
                final Image longFireImg = new UntouchableImage(getPointX(target.getFieldBattleX()) + offsetX, getPointY(target.getFieldBattleY()) + offsetY, 20, 20, "http://static.eaw1805.com/tiles/battleLand.png");
                roundToPlaybackAdditionsGroup.get(round).add(longFireImg);
                final Line fireLine = new UntouchableLine(brigX + TILE_WIDTH / 2, brigY + TILE_HEIGHT / 2, getPointX(target.getFieldBattleX()) + TILE_WIDTH / 2, getPointY(target.getFieldBattleY()) + TILE_HEIGHT / 2);
                fireLine.setStrokeColor("red");
                fireLine.setStrokeWidth(2);
                fireLine.setStyleName("fieldBattle-fireLine");
                roundToFireLines.get(round).add(fireLine);
            }
        }
        final RallyLogEntryDTO rallyEntry = PlaybackStore.getInstance().getRallyEntryByBrigadeRound(brigade.getBrigadeId(), round);
        if (rallyEntry != null) {
            if (rallyEntry.getMoralStatusOutCome() != null) {
                if ("Routing".equalsIgnoreCase(rallyEntry.getMoralStatusOutCome())) {
                    final Text routText = new Text(brigX + 7 + TILE_WIDTH / 2, brigY + 20 + TILE_HEIGHT / 2, "R");
                    routText.setStrokeColor("black");
                    routText.setFillColor("black");
                    routText.setFontSize(20);
                    roundToPlaybackAdditionsGroup.get(round).add(routText);

                } else if ("Disorder".equalsIgnoreCase(rallyEntry.getMoralStatusOutCome())) {
                    final Text routText = new Text(brigX + 7 + TILE_WIDTH / 2, brigY + 20 + TILE_HEIGHT / 2, "D");
                    routText.setStrokeColor("black");
                    routText.setFillColor("black");
                    routText.setFontSize(20);
                    roundToPlaybackAdditionsGroup.get(round).add(routText);
                }
            }
        }
    }


    public void showCoordinatesGroup() {
        coordinatesGroup.setVisible(true);
    }

    public void hideCoordinatesGroup() {
        coordinatesGroup.setVisible(false);
    }

    public void showPositionGroup() {
        positionGroup.setVisible(true);
    }

    public void hidePositionGroup() {
        positionGroup.setVisible(false);
    }

    public void updateOrderImages(final BrigadeDTO brigade, final boolean isPlayback, final int round) {
        if (!brigadeToOrdersGroup.get(round).containsKey(brigade.getBrigadeId())) {
            brigadeToOrdersGroup.get(round).put(brigade.getBrigadeId(), new FieldBattleMapGroup(round));
        }
        FieldBattleMapGroup orderGroup = brigadeToOrdersGroup.get(round).get(brigade.getBrigadeId());
        orderGroup.hide();
        orderGroup.clear();
        int lastX = brigade.getFieldBattleX();
        int lastY = brigade.getFieldBattleY();

        if (brigade.getBasicOrder() != null) {
            if ("FOLLOW_DETACHMENT".equals(brigade.getBasicOrder().getOrderType())) {
                //then so who the leader is.
                if (brigade.getBasicOrder().getLeaderId() != 0) {
                    final BrigadeDTO leader = ArmyStore.getInstance().getBrigadeById(brigade.getBasicOrder().getLeaderId());
                    if (leader.isPlacedOnFieldMap()) {
                        int destX = getPointX(leader.getFieldBattleX()) + TILE_WIDTH/2;
                        int destY = getPointY(leader.getFieldBattleY()) + TILE_HEIGHT/2;

                        if (leader.getFieldBattleX() > lastX) {
                            destX = getPointX(leader.getFieldBattleX()) + TILE_WIDTH/4;
                        } else if (leader.getFieldBattleX() < lastX) {
                            destX = getPointX(leader.getFieldBattleX()) + TILE_WIDTH - TILE_WIDTH/4;
                        }
                        if (leader.getFieldBattleY() > lastY) {
                            destY = getPointY(leader.getFieldBattleY()) + TILE_HEIGHT/4;
                        } else if (leader.getFieldBattleY() < lastY) {
                            destY = getPointY(leader.getFieldBattleY()) + TILE_HEIGHT - TILE_HEIGHT/4;
                        }

                        final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2,
                                getPointY(lastY) + TILE_HEIGHT / 2, destX, destY,
                                "http://static.eaw1805.com/images/field/extra/arrow.png");

                        arrow.setStyleName("disablePointerEvents");
                        orderGroup.add(arrow);
                    }
                }
            }
            if (!brigade.getBasicOrder().isReachedCheckpoint1() && (brigade.getBasicOrder().getCheckPoint1().getX() >= 0 || brigade.getBasicOrder().getCheckPoint1().getY() >= 0)) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getBasicOrder().getCheckPoint1().getX()) + TILE_WIDTH / 2, getPointY(brigade.getBasicOrder().getCheckPoint1().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getBasicOrder().getCheckPoint1().getX();
                lastY = brigade.getBasicOrder().getCheckPoint1().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getBasicOrder().getCheckPoint1());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (!brigade.getBasicOrder().isReachedCheckpoint2() && (brigade.getBasicOrder().getCheckPoint2().getX() >= 0 || brigade.getBasicOrder().getCheckPoint2().getY() >= 0)) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getBasicOrder().getCheckPoint2().getX()) + TILE_WIDTH / 2, getPointY(brigade.getBasicOrder().getCheckPoint2().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getBasicOrder().getCheckPoint2().getX();
                lastY = brigade.getBasicOrder().getCheckPoint2().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getBasicOrder().getCheckPoint2());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (!brigade.getBasicOrder().isReachedCheckpoint3() && (brigade.getBasicOrder().getCheckPoint3().getX() >= 0 || brigade.getBasicOrder().getCheckPoint3().getY() >= 0)) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getBasicOrder().getCheckPoint3().getX()) + TILE_WIDTH / 2, getPointY(brigade.getBasicOrder().getCheckPoint3().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getBasicOrder().getCheckPoint3().getX();
                lastY = brigade.getBasicOrder().getCheckPoint3().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getBasicOrder().getCheckPoint3());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }

            if (brigade.getBasicOrder().getStrategicPoint1().getX() >= 0 || brigade.getBasicOrder().getStrategicPoint1().getY() >= 0) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getBasicOrder().getStrategicPoint1().getX()) + TILE_WIDTH / 2, getPointY(brigade.getBasicOrder().getStrategicPoint1().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getBasicOrder().getStrategicPoint1().getX();
                lastY = brigade.getBasicOrder().getStrategicPoint1().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getBasicOrder().getStrategicPoint1());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (brigade.getBasicOrder().getStrategicPoint2().getX() >= 0 || brigade.getBasicOrder().getStrategicPoint2().getY() >= 0) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getBasicOrder().getStrategicPoint2().getX()) + TILE_WIDTH / 2, getPointY(brigade.getBasicOrder().getStrategicPoint2().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getBasicOrder().getStrategicPoint2().getX();
                lastY = brigade.getBasicOrder().getStrategicPoint2().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getBasicOrder().getStrategicPoint2());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (brigade.getBasicOrder().getStrategicPoint3().getX() >= 0 || brigade.getBasicOrder().getStrategicPoint3().getY() >= 0) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getBasicOrder().getStrategicPoint3().getX()) + TILE_WIDTH / 2, getPointY(brigade.getBasicOrder().getStrategicPoint3().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getBasicOrder().getStrategicPoint3().getX();
                lastY = brigade.getBasicOrder().getStrategicPoint3().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getBasicOrder().getStrategicPoint3());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
        }
        if (brigade.getAdditionalOrder() != null) {
            if ("FOLLOW_DETACHMENT".equals(brigade.getAdditionalOrder().getOrderType())) {
                //then so who the leader is.
                if (brigade.getAdditionalOrder().getLeaderId() != 0) {
                    final BrigadeDTO leader = ArmyStore.getInstance().getBrigadeById(brigade.getAdditionalOrder().getLeaderId());
                    if (leader.isPlacedOnFieldMap()) {
                        int destX = getPointX(leader.getFieldBattleX()) + TILE_WIDTH/2;
                        int destY = getPointY(leader.getFieldBattleY()) + TILE_HEIGHT/2;

                        if (leader.getFieldBattleX() > lastX) {
                            destX = getPointX(leader.getFieldBattleX()) + TILE_WIDTH/4;
                        } else if (leader.getFieldBattleX() < lastX) {
                            destX = getPointX(leader.getFieldBattleX()) + TILE_WIDTH -  TILE_WIDTH/4;
                        }
                        if (leader.getFieldBattleY() > lastY) {
                            destY = getPointY(leader.getFieldBattleY()) + TILE_HEIGHT/4;
                        } else if (leader.getFieldBattleY() < lastY) {
                            destY = getPointY(leader.getFieldBattleY()) + TILE_HEIGHT - TILE_HEIGHT/4;
                        }
                        final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, destX, destY, "http://static.eaw1805.com/images/field/extra/arrow.png");
                        arrow.setStyleName("disablePointerEvents");
                        orderGroup.add(arrow);
                    }
                }
            }
            if (!brigade.getAdditionalOrder().isReachedCheckpoint1() && (brigade.getAdditionalOrder().getCheckPoint1().getX() >= 0 || brigade.getAdditionalOrder().getCheckPoint1().getY() >= 0)) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getAdditionalOrder().getCheckPoint1().getX()) + TILE_WIDTH / 2, getPointY(brigade.getAdditionalOrder().getCheckPoint1().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getAdditionalOrder().getCheckPoint1().getX();
                lastY = brigade.getAdditionalOrder().getCheckPoint1().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getAdditionalOrder().getCheckPoint1());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (!brigade.getAdditionalOrder().isReachedCheckpoint2() && (brigade.getAdditionalOrder().getCheckPoint2().getX() >= 0 || brigade.getAdditionalOrder().getCheckPoint2().getY() >= 0)) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getAdditionalOrder().getCheckPoint2().getX()) + TILE_WIDTH / 2, getPointY(brigade.getAdditionalOrder().getCheckPoint2().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getAdditionalOrder().getCheckPoint2().getX();
                lastY = brigade.getAdditionalOrder().getCheckPoint2().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getAdditionalOrder().getCheckPoint2());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (!brigade.getAdditionalOrder().isReachedCheckpoint3() && (brigade.getAdditionalOrder().getCheckPoint3().getX() >= 0 || brigade.getAdditionalOrder().getCheckPoint3().getY() >= 0)) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getAdditionalOrder().getCheckPoint3().getX()) + TILE_WIDTH / 2, getPointY(brigade.getAdditionalOrder().getCheckPoint3().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getAdditionalOrder().getCheckPoint3().getX();
                lastY = brigade.getAdditionalOrder().getCheckPoint3().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getAdditionalOrder().getCheckPoint3());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }

            if (brigade.getAdditionalOrder().getStrategicPoint1().getX() >= 0 || brigade.getAdditionalOrder().getStrategicPoint1().getY() >= 0) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getAdditionalOrder().getStrategicPoint1().getX()) + TILE_WIDTH / 2, getPointY(brigade.getAdditionalOrder().getStrategicPoint1().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getAdditionalOrder().getStrategicPoint1().getX();
                lastY = brigade.getAdditionalOrder().getStrategicPoint1().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getAdditionalOrder().getStrategicPoint1());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (brigade.getAdditionalOrder().getStrategicPoint2().getX() >= 0 || brigade.getAdditionalOrder().getStrategicPoint2().getY() >= 0) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getAdditionalOrder().getStrategicPoint2().getX()) + TILE_WIDTH / 2, getPointY(brigade.getAdditionalOrder().getStrategicPoint2().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getAdditionalOrder().getStrategicPoint2().getX();
                lastY = brigade.getAdditionalOrder().getStrategicPoint2().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getAdditionalOrder().getStrategicPoint2());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
            if (brigade.getAdditionalOrder().getStrategicPoint3().getX() >= 0 || brigade.getAdditionalOrder().getStrategicPoint3().getY() >= 0) {
                final Image arrow = getArrow(getPointX(lastX) + TILE_WIDTH / 2, getPointY(lastY) + TILE_HEIGHT / 2, getPointX(brigade.getAdditionalOrder().getStrategicPoint3().getX()) + TILE_WIDTH / 2, getPointY(brigade.getAdditionalOrder().getStrategicPoint3().getY()) + TILE_HEIGHT / 2);
                orderGroup.add(arrow);
                lastX = brigade.getAdditionalOrder().getStrategicPoint3().getX();
                lastY = brigade.getAdditionalOrder().getStrategicPoint3().getY();
                final Image xImg = new Image(getPointX(lastX), getPointY(lastY), 64, 64, "http://static.eaw1805.com/images/field/extra/target.png");
                xImg.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                });
                xImg.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(MouseOverEvent mouseOverEvent) {
                        selectBrigade(brigade, isPlayback, round);
                    }
                });
                makeMovable(xImg, brigade, round, isPlayback, brigade.getAdditionalOrder().getStrategicPoint3());
                orderGroup.add(xImg);
                addBrigadeTip(xImg, brigade, isPlayback, round);
            }
        }
        orderGroup.show();


    }

    public void makeMovable(final Image img, final BrigadeDTO brigade, final int round, final boolean isPlayback, final FieldBattlePositionDTO position) {
        new MoveAble(MainPanel.getInstance().getDrawingArea(), img) {
            @Override
            public void onMouseMove(int x, int y) {
                if (MainPanel.getInstance().getDrawingArea().getPositionX(x) != position.getX()
                        || MainPanel.getInstance().getDrawingArea().getPositionY(y) != position.getY()) {
                    position.setX(MainPanel.getInstance().getDrawingArea().getPositionX(x));
                    position.setY(MainPanel.getInstance().getDrawingArea().getPositionY(y));
                    updateOrderImages(brigade, isPlayback, round);
                }
            }

            @Override
            public void onMoveEnd(final int x, final int y) {
                position.setX(MainPanel.getInstance().getDrawingArea().getPositionX(x));
                position.setY(MainPanel.getInstance().getDrawingArea().getPositionY(y));
                removeBrigadeImage(brigade, round);
                addArmyImage(brigade, true, isPlayback);
            }

            @Override
            public void onJustClick() {
                openBrigadeMenu(brigade, true, round, clone);
            }
        };
    }

    public void additionWallImages(final FieldBattleSectorDTO sector, final FieldBattleSectorDTO[][] sectors, final Group group) {
        boolean[][] wallSides = sector.getWallDirections(sectors);

        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("f_");

        if (wallSides[1][0]) {
            urlBuilder.append("T");
        }
        if (wallSides[2][1]) {
            urlBuilder.append("R");
        }
        if (wallSides[1][2]) {
            urlBuilder.append("B");
        }
        if (wallSides[0][1]) {
            urlBuilder.append("L");
        }
        urlBuilder.append(".png");
        group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/walls/" + urlBuilder.toString()));
    }


    public void removeBrigadeImage(final BrigadeDTO brigade, final int round) {
        if (brigadeToGroup.containsKey(round) &&
                brigadeToGroup.get(round).containsKey(brigade.getBrigadeId())) {
            brigadeToGroup.get(round).get(brigade.getBrigadeId()).hide();
            brigadeToGroup.get(round).get(brigade.getBrigadeId()).hideDefaultImg();
        }
        if (brigadeToOrdersGroup.get(round).containsKey(brigade.getBrigadeId())) {
            brigadeToOrdersGroup.get(round).get(brigade.getBrigadeId()).hide();
        }
        main.getMiniMapUtils().removeBrigadeImage(brigade);
    }

    public void addArmyImage(final BrigadeDTO brigade, final boolean updateMinimap, final boolean isPlayback) {
        addArmyImage(brigade, updateMinimap, isPlayback, 0, true);
    }

    public String getLetterByNationId(int nationId) {
        switch (nationId) {
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
            case 8:
                return "I";
            case 9:
                return "K";
            case 10:
                return "M";
            case 11:
                return "N";
            case 12:
                return "P";
            case 13:
                return "R";
            case 14:
                return "S";
            case 15:
                return "T";
            case 16:
                return "W";
            case 17:
                return "Y";
        }
        return "A";
    }

    public Group createArmyImage(final BrigadeDTO brigade, boolean zoomed, final int round) {
        int brigX = getPointX(brigade.getFieldBattleX());
        int brigY = getPointY(brigade.getFieldBattleY());
        Group out = new Group();
        int count = 0;
        int offsetX;
        if (brigade.getBattalions().size() >= 3) {
            offsetX = 1;
        } else {
            offsetX = 10 * (3 - brigade.getBattalions().size());
        }

        boolean isRouting = false;

        if (zoomed) {
            final LongRangeAttackLogEntryDTO longRangeEntry = PlaybackStore.getInstance().getLongRangeByBrigadeRound(brigade.getBrigadeId(), round);
            if (longRangeEntry != null) {
                final BrigadeDTO target = PlaybackStore.getInstance().getBrigadeByIdRound(longRangeEntry.getTargetBrigadeId(), round);
                if (target != null) {
                    final Image pistolImg = new Image(brigX, brigY, TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/various/FiringSmoke.png");
                    out.add(pistolImg);
                }
            }
        }

        final RallyLogEntryDTO rallyEntry = PlaybackStore.getInstance().getRallyEntryByBrigadeRound(brigade.getBrigadeId(), round);
        if (rallyEntry != null) {
            if (rallyEntry.getMoralStatusOutCome() != null) {
                if ("Routing".equalsIgnoreCase(rallyEntry.getMoralStatusOutCome())) {
                    isRouting = true;
                }
            }
        }
        String direction = "Down";
        if ((!BaseStore.getInstance().isNationAllied(brigade.getNationId())
                && BaseStore.getInstance().getSide() == 1)
                || (BaseStore.getInstance().isNationAllied(brigade.getNationId())
                && BaseStore.getInstance().getSide() == 2)) {
            direction = "Up";
        }

        for (BattalionDTO battalion : brigade.getBattalions()) {
            final StringBuilder url = new StringBuilder();
            if (isRouting) {
                url.append(host).append("/images/field/map/troops/routed/");
                if (battalion.getEmpireArmyType().isArtillery()) {
                    url.append("RoutedArt").append(direction).append("_").append(getLetterByNationId(brigade.getNationId())).append(".png");
                } else if (battalion.getEmpireArmyType().isCavalry()) {
                    url.append("RoutedCav").append(direction).append("_").append(getLetterByNationId(brigade.getNationId())).append(".png");
                } else {
                    url.append("RoutedInf").append(direction).append("_").append(getLetterByNationId(brigade.getNationId())).append(".png");
                }
            } else {
                url.append(host).append("/images/field/map/troops/").append(brigade.getNationId()).append("/");
                if (battalion.getEmpireArmyType().isArtillery()) {
                    url.append("ArtHeavy").append(direction).append("_").append(getLetterByNationId(brigade.getNationId())).append(".png");
                } else if (battalion.getEmpireArmyType().isCavalry()) {
                    url.append("CavHeavy").append(direction).append("_").append(getLetterByNationId(brigade.getNationId())).append(".png");
                } else {
                    url.append("InfReg").append(direction).append("_").append(getLetterByNationId(brigade.getNationId())).append(".png");
                }
            }
            final Image batImg;
            if (count < 3) {
                if (zoomed) {
                    batImg = new Image(brigX + (offsetX / 2) + 2 + (21 * count), brigY - 4, 21, 63, url.toString());
                } else {
                    batImg = new Image(brigX + offsetX + (20 * count), brigY - 4, 21, 58, url.toString());
                }
            } else {
                if (zoomed) {
                    batImg = new Image(brigX + (offsetX / 2) + 2 + (21 * (count - 3)), brigY + 10, 21, 63, url.toString());
                } else {
                    batImg = new Image(brigX + offsetX + (20 * (count - 3)), brigY + 10, 21, 58, url.toString());
                }
            }

            out.add(batImg);


            count++;
        }
        if (!zoomed) {
            final LongRangeAttackLogEntryDTO longRangeEntry = PlaybackStore.getInstance().getLongRangeByBrigadeRound(brigade.getBrigadeId(), round);
            if (longRangeEntry != null) {
                final BrigadeDTO target = PlaybackStore.getInstance().getBrigadeByIdRound(longRangeEntry.getTargetBrigadeId(), round);
                if (target != null) {
                    final Image pistolImg = new Image(brigX, brigY, TILE_WIDTH, TILE_HEIGHT, host + "/images/field/map/various/FiringSmoke.png");
                    out.add(pistolImg);
                }
            }
        }
        return out;
    }

    public void updateOrCreateBrigadeMenu(final BrigadeDTO brigade) {

        final PopupPanel container = new PopupPanel();
        container.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached() && container.getWidget() == null) {
                    final BrigadeFullInfoPanel panel = new BrigadeFullInfoPanel(brigade);
                    container.setWidget(panel.getInfo());
                }
            }
        });
        container.setStyleName("none");
        container.setAutoHideEnabled(true);

        brigadeToMenu.put(brigade.getBrigadeId(), new ArrayList<PopupPanel>());
        brigadeToMenu.get(brigade.getBrigadeId()).add(container);
        final PopupPanel container2 = new PopupPanel();
        container2.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached() && container2.getWidget() == null) {
                    final BrigadeFullInfoPanel panel = new BrigadeFullInfoPanel(brigade);
                    container2.setWidget(panel.getBasicOrder());
                }
                ((OrderMiniWidget)container2.getWidget()).updateInputsFromOrder();
                ((OrderMiniWidget)container2.getWidget()).updateVisibleInputs();
            }
        });
        container2.setStyleName("none");
        container2.setAutoHideEnabled(true);

        brigadeToMenu.get(brigade.getBrigadeId()).add(container2);
        final PopupPanel container3 = new PopupPanel();
        container3.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached() && container3.getWidget() == null) {
                    final BrigadeFullInfoPanel panel = new BrigadeFullInfoPanel(brigade);
                    container3.setWidget(panel.getAdditionalOrder());
                }
                ((OrderMiniWidget)container3.getWidget()).updateInputsFromOrder();
                ((OrderMiniWidget)container3.getWidget()).updateVisibleInputs();
            }
        });
        container3.setStyleName("none");
        container3.setAutoHideEnabled(true);
        brigadeToMenu.get(brigade.getBrigadeId()).add(container3);

        container.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            }
        }, ClickEvent.getType());
        container.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.stopPropagation();
                mouseDownEvent.preventDefault();
            }
        }, MouseDownEvent.getType());
        container2.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            }
        }, ClickEvent.getType());
        container2.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.stopPropagation();
                mouseDownEvent.preventDefault();
            }
        }, MouseDownEvent.getType());
        container3.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                clickEvent.stopPropagation();
                clickEvent.preventDefault();
            }
        }, ClickEvent.getType());
        container3.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                mouseDownEvent.stopPropagation();
                mouseDownEvent.preventDefault();
            }
        }, MouseDownEvent.getType());
    }

    public void setBrigadeOrderLock(final int brigadeId, final boolean lock) {
        ((OrderMiniWidget) brigadeToMenu.get(brigadeId).get(1).getWidget()).setLock(lock);
        ((OrderMiniWidget) brigadeToMenu.get(brigadeId).get(2).getWidget()).setLock(lock);
    }

    public Group getArmiesGroupByRound(final int round) {
        return roundToArmiesGroup.get(round);
    }

    public void showRoundBrigades(final int round) {
        for (Group group : roundToArmiesGroup.values()) {
            if (group.isAttached()) {
                group.setVisible(false);
            }
        }
        roundToArmiesGroup.get(round).setVisible(true);
    }

    public void addArmyImage(final BrigadeDTO brigade, final boolean updateMinimap, final boolean isPlayback, final int round, final boolean updateFollowers) {

        if (!BaseStore.getInstance().isStartRound() && brigade.isFieldBattleUpdateMiddleRound() &&
                !BaseStore.getInstance().isGameEnded() && BaseStore.getInstance().getNationId() == brigade.getNationId()) {
            addOrderChangedGraphic(brigade);
        }

        final ArmyOptionsGroup armyGroup = new ArmyOptionsGroup(brigade, round, true);
        brigadeToGroup.get(round).put(brigade.getBrigadeId(), armyGroup);

        int brigX = getPointX(brigade.getFieldBattleX());
        int brigY = getPointY(brigade.getFieldBattleY());

        if ((BaseStore.getInstance().isStartRound() || BaseStore.getInstance().isLastHalfRound(round + 1)) && brigade.getNationId() == BaseStore.getInstance().getNationId()) {
            updateOrCreateBrigadeMenu(brigade);
        }

        armyGroup.setDefaultImg(createArmyImage(brigade, false, round));
        final Image bOrdered = new Image(brigX + 3, brigY + TILE_HEIGHT - 18, 15, 15, "http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        final Image aOrdered = new Image(brigX + 21, brigY + TILE_HEIGHT - 18, 15, 15, "http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        armyGroup.addOrderedImages(bOrdered, aOrdered);
        armyGroup.updateOrderedImages();
        armyGroup.showDefaultImg();


        Image commImg = null;
        if (brigade.getFieldBattleCommanderId() > 0) {
            final CommanderDTO commander = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleCommanderId());
            commImg = new Image(brigX + 18, brigY + 25, 30, 30, "http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
        } else if (brigade.getFieldBattleOverallCommanderId() > 0) {
            final CommanderDTO commander = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleOverallCommanderId());
            commImg = new Image(brigX + 18, brigY + 25, 30, 30, "http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
        }
        if (commImg != null) {
            armyGroup.setCommanderImg(commImg);
        }

        if (isPlayback) {
            armyGroup.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    MainPanel.getInstance().getPlaybackInfo().updateSelectedInfo(brigade, true, true);
                    main.getPlaybackInfo().lockBrigadeInfo();
                }
            });
        }
        final Group baseArmyImage = createArmyImage(brigade, true, round);



        new ToolTipPanel(armyGroup, false) {

            @Override
            public void generateTip() {
                if (isPlayback) {
                    setTooltip(new PlaybackBrigadeInfoPanel(brigade, round, false));
                    setOffsets(-108, -228);
                } else {
                    setTooltip(new BrigadeInfoPanel(brigade, false, false, false));
                    setOffsets(-151, -93);
                }

                baseArmyImage.addMouseDownHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        hide();
                        t.cancel();
                    }
                });

            }


        };

        if (BaseStore.getInstance().isNationAllied(brigade.getNationId())) {
            new ToolTipPanel(armyGroup, false) {

                @Override
                public void generateTip() {
                    setTooltip(new OrderInfoPanel(brigade, true));
                    setOffsets(-151, 70);
                    baseArmyImage.addMouseDownHandler(new MouseDownHandler() {
                        @Override
                        public void onMouseDown(MouseDownEvent event) {
                            hide();
                            t.cancel();
                        }
                    });
                }
            };
            new ToolTipPanel(armyGroup, false) {

                @Override
                public void generateTip() {
                    setTooltip(new OrderInfoPanel(brigade, false));
                    setOffsets(-151, 163);
                    baseArmyImage.addMouseDownHandler(new MouseDownHandler() {
                        @Override
                        public void onMouseDown(MouseDownEvent event) {
                            hide();
                            t.cancel();
                        }
                    });
                }
            };
        }

        armyGroup.getDefaultGroup().addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(final MouseOverEvent mouseOverEvent) {
                selectBrigade(brigade, isPlayback, round);
            }
        });
        final Rectangle bgRect = new Rectangle(brigX, brigY, TILE_WIDTH, TILE_HEIGHT);
        bgRect.setFillOpacity(0.0);
        bgRect.setStrokeOpacity(0.0);

        armyGroup.addVectorToGroup(ArmyOptionsGroup.BACKGROUND_SPACER, bgRect);


        armyGroup.addVectorToGroup(ArmyOptionsGroup.BASE_IMAGE, baseArmyImage);
        if ((!isPlayback || (round + 1) / 2 == BaseStore.getInstance().getRound() + 1) && BaseStore.getInstance().getNationId() == brigade.getNationId()) {

            if (BaseStore.getInstance().isStartRound()) {
                new MoveAbleGroup(MainPanel.getInstance().getDrawingArea(), baseArmyImage) {
                    int originalX = 0;
                    int originalY = 0;
                    final List<BrigadeDTO> followers = new ArrayList<BrigadeDTO>();
                    @Override
                    public void onMoveStart() {
                        if (BaseStore.getInstance().isStartRound()) {
                            originalX = brigade.getFieldBattleX();
                            originalY = brigade.getFieldBattleY();
                            followers.addAll(ArmyStore.getInstance().getFollowers(brigade.getBrigadeId()));
                            hideLastBrigadeMenu();
                            MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
                            showPositionGroup();
                        }
                    }

                    @Override
                    public void onMouseMove(int x, int y) {
                        if (BaseStore.getInstance().isStartRound()) {
                            if (MainPanel.getInstance().getDrawingArea().getPositionX(x) != brigade.getFieldBattleX()
                                    || MainPanel.getInstance().getDrawingArea().getPositionY(y) != brigade.getFieldBattleY()) {
                                brigade.setFieldBattleX(MainPanel.getInstance().getDrawingArea().getPositionX(x));
                                brigade.setFieldBattleY(MainPanel.getInstance().getDrawingArea().getPositionY(y));
                                for (BrigadeDTO follower : followers) {
                                    updateOrderImages(follower, isPlayback, round);
                                }
                                updateOrderImages(brigade, isPlayback, round);
                            }
                        }

                    }

                    @Override
                    public void onMoveEnd(int x, int y) {
                        if (BaseStore.getInstance().isStartRound()) {
                            final FieldBattleSectorDTO sector = MapStore.getInstance().getSectorByXY(MainPanel.getInstance().getDrawingArea().getPositionX(x), MainPanel.getInstance().getDrawingArea().getPositionY(y));
                            if (MainPanel.getInstance().getDrawingArea().canBrigadeMoveToSector(sector, brigade)) {
                                brigade.setFieldBattleX(sector.getX());
                                brigade.setFieldBattleY(sector.getY());
                                brigade.setPlacedOnFieldMap(true);
                            } else {
                                brigade.setFieldBattleX(originalX);
                                brigade.setFieldBattleY(originalY);
                            }
                            if (brigade.isPlacedOnFieldMap()) {
                                MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                            }
                            if (BaseStore.getInstance().isStartRound()) {
                                MainPanel.getInstance().updateNationInfoPanels();
                            }
                            MainPanel.getInstance().getMapUtils().hidePositionGroup();
                        }
                    }

                    @Override
                    public void onJustClick() {
                        if (MainPanel.getInstance().getDrawingArea().isNormal()) {
                            openBrigadeMenu(brigade, true, round);
                        }
                        if (!BaseStore.getInstance().isStartRound()) {
                            MainPanel.getInstance().getPlaybackInfo().updateSelectedInfo(brigade, true, true);
                            main.getPlaybackInfo().lockBrigadeInfo();
                        }
                    }
                };
            } else {
                baseArmyImage.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        event.stopPropagation();
                        event.preventDefault();
                        if (MainPanel.getInstance().getDrawingArea().isNormal()) {
                            openBrigadeMenu(brigade, true, round);
                        }
                        if (!BaseStore.getInstance().isStartRound()) {
                            MainPanel.getInstance().getPlaybackInfo().updateSelectedInfo(brigade, true, true);
                            main.getPlaybackInfo().lockBrigadeInfo();
                        }
                    }
                });
            }





            if (BaseStore.getInstance().isStartRound()) {
                final ImageButtonSVG closeImg = new ImageButtonSVG(brigX + 44, brigY, 20, 20, "http://static.eaw1805.com/images/buttons/ButCancelOff.png", "Remove brigade from field");
                closeImg.setStyleName("pointer");
                closeImg.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        brigade.setFieldBattleX(0);
                        brigade.setFieldBattleY(0);
                        brigade.setPlacedOnFieldMap(false);
                        removeBrigadeImage(brigade, round);
                        MainPanel.getInstance().getArmyBar().refreshBrigadesContainer();
                        MainPanel.getInstance().getMapUtils().updateLeaderSelectionOptions();
                        //reset brigade orders
                        brigade.setBasicOrder(null);
                        brigade.setAdditionalOrder(null);
                        if (BaseStore.getInstance().isStartRound()) {
                            MainPanel.getInstance().updateNationInfoPanels();
                        }
                    }
                });
                brigadeToGroup.get(round).get(brigade.getBrigadeId()).addVectorToGroup(ArmyOptionsGroup.CLOSE_IMAGE, closeImg);


                if (updateFollowers && brigade.getNationId() == BaseStore.getInstance().getNationId()) {

                        if ((brigade.getBasicOrder() == null || !"FOLLOW_DETACHMENT".equals(brigade.getBasicOrder().getOrderType()))
                                && (brigade.getAdditionalOrder() == null || !"FOLLOW_DETACHMENT".equals(brigade.getAdditionalOrder().getOrderType()))) {
                            final List<BrigadeDTO> followers = ArmyStore.getInstance().getFollowers(brigade.getBrigadeId());
                            for (BrigadeDTO follower : followers) {
                                if (follower.isPlacedOnFieldMap()) {

                                    //if brigade already exists as map element.
                                    if (brigadeToGroup.get(round).containsKey(follower.getBrigadeId())) {
                                        //then update it.(otherwise when created it will be ok).
                                        try {
                                            updateOrderImages(follower, isPlayback, round);
                                        } catch (Exception e) {
                                            Window.alert("ftuoi? " + e.toString() + " - " + follower.positionFieldBattleToString());
                                        }
                                    }
                                }
                            }
                        }

                }

            }

        }
//        MainPanel.getInstance().getArmyInfo().debugText.setText(isPlayback + ", " + BaseStore.getInstance().getRound() +", " + round);
        //if it is not playback, or we are in the last halfround (editing orders and stuff)
        if ((!isPlayback || (round + 1) / 2 == BaseStore.getInstance().getRound() + 1)
                && BaseStore.getInstance().getNationId() == brigade.getNationId()) {
            updateOrderImages(brigade, isPlayback, round);
        }
        if (updateMinimap) {
            main.getMiniMapUtils().addArmyImage(brigade);
        }

    }

    public void updateLeaderSelectionOptions() {
        for (Map.Entry<Integer, List<PopupPanel>> entry : brigadeToMenu.entrySet()) {
            ((OrderMiniWidget) entry.getValue().get(1).getWidget()).updateLeaderSelectionOptions();
            ((OrderMiniWidget) entry.getValue().get(2).getWidget()).updateLeaderSelectionOptions();
        }
    }


    public void updateBrigadeOrderedImages(final BrigadeDTO brigade, final int round) {
        if (brigadeToGroup.get(round).containsKey(brigade.getBrigadeId())) {
            brigadeToGroup.get(round).get(brigade.getBrigadeId()).updateOrderedImages();
            updateOrderImages(brigade, false, round);
        }
    }

    public void openBrigadeMenu(final BrigadeDTO brigade, final boolean centerBrigade, final int round) {
        Group toCheck = null;
        if (centerBrigade) {

            if (brigadeToGroup.get(round).get(brigade.getBrigadeId()).isAttached()) {
                toCheck = brigadeToGroup.get(round).get(brigade.getBrigadeId());
            } else if (brigadeToGroup.get(round).get(brigade.getBrigadeId()).getDefaultGroup().isAttached()) {
                toCheck = brigadeToGroup.get(round).get(brigade.getBrigadeId()).getDefaultGroup();
            } else {
                toCheck = brigadeToGroup.get(round).get(brigade.getBrigadeId());
            }
        }
        openBrigadeMenu(brigade, centerBrigade, round , toCheck);
    }


    public void openBrigadeMenu(final BrigadeDTO brigade, final boolean centerBrigade, final int round, VectorObject toCheck) {
        hideLastBrigadeMenu();
        final int absoluteLeft;
        final int absoluteTop;

        if (centerBrigade) {


            int left = toCheck.getAbsoluteLeft();
            int top = toCheck.getAbsoluteTop();

            int newTop = 0;
            int newLeft = 0;
            if (top < 100) {
                newTop = -100 + top;
            }
            if (top > Window.getClientHeight() - (300 + 64)) {
                newTop = (300 + 64) - (Window.getClientHeight() - top);
            }
            if (left < 340) {
                newLeft = -340 + left;
            }
            if (left > Window.getClientWidth() - (340 + 64)) {
                newLeft = (340 + 64) - (Window.getClientWidth() - left);
            }

            MainPanel.getInstance().getMiniMap().getMap().moveMapRelative(newLeft, newTop, false);
            absoluteLeft = toCheck.getAbsoluteLeft();
            absoluteTop = toCheck.getAbsoluteTop();

        } else {
            absoluteTop = 10;
            absoluteLeft = Window.getClientWidth() / 2;
        }
        final PopupPanel container1 = brigadeToMenu.get(brigade.getBrigadeId()).get(0);
        container1.setPopupPosition(0, 0);
        container1.setAutoHideEnabled(false);
        container1.show();
        container1.setPopupPosition(absoluteLeft - (366 - TILE_WIDTH) / 2, absoluteTop - 93);
        final PopupPanel container2 = brigadeToMenu.get(brigade.getBrigadeId()).get(1);
        container2.setAutoHideEnabled(false);
        container2.setPopupPosition(0, 0);
        container2.show();
        container2.setPopupPosition(absoluteLeft - 336, absoluteTop + 80);
        final PopupPanel container3 = brigadeToMenu.get(brigade.getBrigadeId()).get(2);
        container3.setAutoHideEnabled(false);
        container3.setPopupPosition(0, 0);
        container3.show();
        container3.setPopupPosition(absoluteLeft + 32, absoluteTop + 80);

        lastOpenedMenu.clear();
        lastOpenedMenu.add(container1);
        lastOpenedMenu.add(container2);
        lastOpenedMenu.add(container3);

        selectionArea.setX(getPointX(brigade.getFieldBattleX()) + TILE_WIDTH / 2);
        selectionArea.setY(getPointY(brigade.getFieldBattleY()) + TILE_HEIGHT / 2);

        MainPanel.getInstance().getDrawingArea().add(selectionArea);
    }


    public void hideBrigadeMenu(final BrigadeDTO brigade) {
        if (brigadeToMenu.containsKey(brigade.getBrigadeId())) {
            brigadeToMenu.get(brigade.getBrigadeId()).get(0).hide();
            brigadeToMenu.get(brigade.getBrigadeId()).get(1).hide();
            brigadeToMenu.get(brigade.getBrigadeId()).get(2).hide();
            lastOpenedMenu.clear();
        }
        if (selectionArea.isAttached()) {
            MainPanel.getInstance().getDrawingArea().remove(selectionArea);
        }
    }

    public void hideLastBrigadeMenu() {
        for (PopupPanel panel : lastOpenedMenu) {
            panel.hide();
        }
        if (selectionArea.isAttached()) {
            MainPanel.getInstance().getDrawingArea().remove(selectionArea);
        }
    }

    public Image getArrow(final int x1, final int y1, final int x2, final int y2) {
        return getArrow(x1, y1, x2, y2, "http://static.eaw1805.com/images/field/extra/arrow3.png");
    }

    public Image getArrow(final int x1, final int y1, final int x2, final int y2, final String url) {
        int width = 20;
        int height = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        width += (height*1.0/1000d)*10;
        width = Math.min(width, TILE_WIDTH);
        final Image arrowImg = new Image(0, 0, width, height, url);
        //make arrow untouchable by mouse to Avoid z-index issues.
        arrowImg.setStyleName("disablePointerEvents");
        arrowImg.getElement().setAttribute("transform","translate(" + (x1 - width/2) + " " + y1 +") rotate(" + getAngleOfLineBetweenTwoPoints(new int[] {x1, y1}, new int[] {x2, y2}) + " 0 0)");
        return arrowImg;
    }

    /**
     * Determines the angle of a straight line drawn between point one and two. The number returned, which is a double in degrees, tells us how much we have to rotate a horizontal line clockwise for it to match the line between the two points. * If you prefer to deal with angles using radians instead of degrees, just change the last line to: "return Math.atan2(yDiff, xDiff);"
     */
    public static double getAngleOfLineBetweenTwoPoints(int[] p1, int[] p2) {
        double xDiff = p2[0] - p1[0];
        double yDiff = p2[1] - p1[1];
        return Math.toDegrees(Math.atan2(yDiff, xDiff)) - 90;
    }

    public int translateX(int x) {
        DrawingAreaFB dArea = MainPanel.getInstance().getDrawingArea();
        return (int) (dArea.getScroller().getHorizontalScrollPosition() - dArea.getZoomOffsetX() + x);
    }

    public int translateToMapX(final int x) {
        return MainPanel.getInstance().getDrawingArea().getPositionX(translateX(x));
    }

    public int translateY(int y) {
        DrawingAreaFB dArea = MainPanel.getInstance().getDrawingArea();
        return  (int) (dArea.getScroller().getVerticalScrollPosition() - dArea.getZoomOffsetY() + y);

    }

    public int translateToMapY(final int y) {
        return MainPanel.getInstance().getDrawingArea().getPositionY(translateY(y));
    }


    public void selectBrigade(final BrigadeDTO brigade, final boolean isPlayback, final int round) {
        deselectAllBrigades(round);
        final Map<Integer, FieldBattleMapGroup> groups = brigadeToOrdersGroup.get(round);
        for (Map.Entry<Integer, FieldBattleMapGroup> entry : groups.entrySet()) {
            if (entry.getKey() != brigade.getBrigadeId()) {
                entry.getValue().getElement().setAttribute("opacity", "0.6");
            } else {
                entry.getValue().getElement().setAttribute("opacity", "1");
            }
        }
        brigadeToGroup.get(round).get(brigade.getBrigadeId()).show();
        MainPanel.getInstance().getArmyInfo().updateBrigadeInfoPanel(brigade);
        if (isPlayback) {
            MainPanel.getInstance().getPlaybackInfo().updateSelectedInfo(brigade, false, true);
        }
    }

    public void deselectAllBrigades(final int round) {
        for (ArmyOptionsGroup group : brigadeToGroup.get(round).values()) {
            group.hide();
        }
    }

    public void gotoBrigade(final BrigadeDTO brigade) {
        int x = MainPanel.getInstance().getMiniMapUtils().getPointX(brigade.getFieldBattleX());
        int y = MainPanel.getInstance().getMiniMapUtils().getPointX(brigade.getFieldBattleY());
        MainPanel.getInstance().getMiniMap().getMap().moveMap(x, y, true);
    }

    public void addBrigadeTip(final Widget widget, final BrigadeDTO brigade, final boolean isPlayback, final int round) {
        new ToolTipPanel(widget, false) {

            @Override
            public void generateTip() {
                if (isPlayback) {
                    setTooltip(new PlaybackBrigadeInfoPanel(brigade, round, false));
                    setOffsets(-108, -228);
                } else {
                    setTooltip(new BrigadeInfoPanel(brigade, false, false, false));
                    setOffsets(-151, -93);
                }
                widget.addDomHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        hide();
                        t.cancel();
                    }
                }, MouseDownEvent.getType());

            }


        };

        new ToolTipPanel(widget, false) {

            @Override
            public void generateTip() {
                setTooltip(new OrderInfoPanel(brigade, true));
                setOffsets(-151, 70);
                widget.addDomHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        hide();
                        t.cancel();
                    }
                }, MouseDownEvent.getType());
            }
        };
        new ToolTipPanel(widget, false) {

            @Override
            public void generateTip() {
                setTooltip(new OrderInfoPanel(brigade, false));
                setOffsets(-151, 163);
                widget.addDomHandler(new MouseDownHandler() {
                    @Override
                    public void onMouseDown(MouseDownEvent event) {
                        hide();
                        t.cancel();
                    }
                }, MouseDownEvent.getType());
            }
        };

    }


    public int getPointX(final int x) {
        return (SIDE_OFFSET + x) * TILE_WIDTH;
    }

    public int getPointY(final int y) {
        return (SIDE_OFFSET + y) * TILE_HEIGHT;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getNumTilesX() {
        return numTilesX;
    }

    public int getNumTilesY() {
        return numTilesY;
    }

    public int[] getCenterSetupAreaCoords() {
        return centerSetupAreaCoords;
    }

    @Override
    public void clear() {
        if (MainPanel.getInstance().getDrawingArea() != null) {
            MainPanel.getInstance().getDrawingArea().clear();
        }
        mapWidth = 0;
        mapHeight = 0;
        numTilesX = 0;
        numTilesY = 0;

        positionGroup.clear();
        coordinatesGroup.clear();
        brigadeToGroup.clear();
        brigadeToOrdersGroup.clear();
        brigadeToMenu.clear();

        roundToPlaybackAdditionsGroup.clear();
        roundToMapTilesGroup.clear();
        roundToMoveLines.clear();
        roundToFireLines.clear();
        roundToStructuresGroup.clear();
        roundToCorpsesGroup.clear();
        structuresGroup.clear();

        centerSetupAreaCoords = new int[2];

        levelLabel.setText("");
        spLabel.setText("");
        buildingLabel.setText("");

        lastOpenedMenu.clear();
        gridGroup.clear();

        staticGroup.clear();
        roundToArmiesGroup.clear();

        enemyTopLeft[0] = Integer.MAX_VALUE;
        enemyTopLeft[1] = Integer.MAX_VALUE;//this is the top left corner for the setup area

        enemyBottomRight[0] = 0;//this is the bottom right corner for the setup area
        enemyBottomRight[1] = 0;//this is the bottom right corner for the setup area

    }
}
