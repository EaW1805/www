package com.eaw1805.www.fieldbattle.stores.utils;


import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.field.FieldBattleMapDTO;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedEvent;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedHandler;
import com.eaw1805.www.fieldbattle.events.loading.LoadingEventManager;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.ClearAble;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.utils.calculators.MiscCalculators;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import com.eaw1805.www.fieldbattle.widgets.ArmyOptionsGroup;
import com.eaw1805.www.fieldbattle.widgets.FieldBattleMapGroup;
import com.eaw1805.www.fieldbattle.widgets.MiniMap;
import com.eaw1805.www.shared.stores.GameStore;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class MiniMapUtils implements MapConstants, ClearAble {

    final String host = "http://static.eaw1805.com";

    private int thisTileSize = 64;
    int miniMapWidth = 0;
    int miniMapHeight = 0;
    MiniMap miniMap = MainPanel.getInstance().getMiniMap().getMap();
    Map<Integer, ArmyOptionsGroup> brigadeToGroup = new HashMap<Integer, ArmyOptionsGroup>();
    Map<Integer, FieldBattleMapGroup> brigadeToOrdersGroup = new HashMap<Integer, FieldBattleMapGroup>();
    //    final String host = "http://localhost";
    public void initMap(final FieldBattleMapDTO map) {

        miniMap.setSize(MiniMap.MINIMAP_WIDTH + "px", MiniMap.MINIMAP_HEIGHT + "px");
        double zoomFactorX = (double)MiniMap.MINIMAP_WIDTH *1.0 / ((map.getSectors().length + 2 * SIDE_OFFSET_MINIMAP) * TILE_WIDTH);
        double zoomFactorY = (double)MiniMap.MINIMAP_HEIGHT*1.0/ ((map.getSectors()[0].length + 2 * SIDE_OFFSET_MINIMAP) * TILE_HEIGHT);

        if (zoomFactorX < zoomFactorY) {
            thisTileSize = (int)(TILE_WIDTH*zoomFactorX);
        } else {
            thisTileSize = (int)(TILE_WIDTH*zoomFactorY);
        }
        miniMapWidth = (map.getSectors().length + 2 * SIDE_OFFSET_MINIMAP) * thisTileSize;
        miniMapHeight = (map.getSectors()[0].length + 2 * SIDE_OFFSET_MINIMAP) * thisTileSize;

        final Group groupMap = new Group();
        int[] topLeft = {Integer.MAX_VALUE, Integer.MAX_VALUE};//this is the top left corner for the setup area
        int[] bottomRight = {0, 0};//this is the bottom right corner for the setup area

        int[] enemyTopLeft = {Integer.MAX_VALUE, Integer.MAX_VALUE};//this is the top left corner for the setup area
        int[] enemyBottomRight = {0, 0};//this is the bottom right corner for the setup area


        groupMap.add(new Image(getPointX(0), getPointX(0), map.getSectors().length*thisTileSize, map.getSectors()[0].length*thisTileSize, host + "/fieldmaps/mm" + map.getBattleId() + ".jpg"));



        for (FieldBattleSectorDTO[] row : map.getSectors()) {
            for (FieldBattleSectorDTO sector : row) {
                final Group group = new Group();

                if (sector.hasBridge()) {
                    boolean upDown = sector.getBridgeDirection(map.getSectors());
//                    if (upDown) {
                    group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), thisTileSize, thisTileSize, host + "/images/field/bridgeUD.png"));
//                    } else {
//                        group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), TILE_WIDTH, TILE_HEIGHT, host + "/images/field/bridgeLR.png"));
//                    }
                }

                if (sector.hasTown() || sector.hasVillage()) {
                    group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), thisTileSize, thisTileSize, host + "/images/field/city1.png"));
                }
                if (sector.hasWall()) {
                    group.add(new Image(getPointX(sector.getX()), getPointX(sector.getY()), thisTileSize, thisTileSize, host + "/images/field/WALL1.png"));
                }
                if (sector.isStrategicPoint()) {
                    final Rectangle spRectangle = new Rectangle(getPointX(sector.getX()), getPointY(sector.getY()), thisTileSize, thisTileSize);
                    spRectangle.setFillOpacity(0.2);
                    spRectangle.setFillColor("#FDD017");
                    group.add(spRectangle);
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


        final Rectangle setupArea = new Rectangle(getPointX(topLeft[0]) , getPointY(topLeft[1]), (bottomRight[0] - topLeft[0] + 1)*thisTileSize, (bottomRight[1] - topLeft[1] + 1)*thisTileSize);
        setupArea.setFillOpacity(0.0);
        setupArea.setStrokeColor("white");
        setupArea.setStrokeWidth(3);
        setupArea.setStyleName("fieldBattle-setup-area");
        groupMap.add(setupArea);

        final Rectangle enemySetupArea = new Rectangle(getPointX(enemyTopLeft[0]) , getPointY(enemyTopLeft[1]), (enemyBottomRight[0] - enemyTopLeft[0] + 1)*thisTileSize, (enemyBottomRight[1] - enemyTopLeft[1] + 1)*thisTileSize);
        enemySetupArea.setFillOpacity(0.0);
        enemySetupArea.setStrokeColor("red");
        enemySetupArea.setStrokeWidth(3);
        enemySetupArea.setStyleName("fieldBattle-setup-area");
        groupMap.add(enemySetupArea);
//        setupArea.setStylePrimaryName("fieldBattle-setup-area");



        miniMap.add(groupMap);
        //finally add the rectangle
        final Rectangle rectangle = new Rectangle(0, 0, 1, 1);
        rectangle.setFillOpacity(0.2);
        rectangle.setFillColor("red");
        miniMap.add(rectangle);
        miniMap.setViewRectangle(rectangle);

        if (ArmyStore.getInstance().isInitialized()) {
            for (BrigadeDTO brigade : ArmyStore.getInstance().getAlliedBrigades()) {
                if (brigade.isPlacedOnFieldMap()) {
                    addArmyImage(brigade);
                }
            }
        } else {
            LoadUtil.getInstance().registerHandlerForClean(LoadingEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
                @Override
                public void onUnitChanged(ArmiesLoadedEvent event) {
                    for (BrigadeDTO brigade : ArmyStore.getInstance().getAlliedBrigades()) {
                        if (brigade.isPlacedOnFieldMap()) {
                            addArmyImage(brigade);
                        }
                    }
                }
            }));
        }

        MainPanel.getInstance().getMiniMap().updateMapPosition();
    }

    public void removeBrigadeImage(final BrigadeDTO brigade) {
        if (brigadeToGroup.containsKey(brigade.getBrigadeId())) {
            brigadeToGroup.get(brigade.getBrigadeId()).hideFromMinimap();
            brigadeToGroup.get(brigade.getBrigadeId()).hideDefaultImgFromMinimap();
        }
        if (brigadeToOrdersGroup.containsKey(brigade.getBrigadeId())) {
            brigadeToOrdersGroup.get(brigade.getBrigadeId()).hideFromMinimap();
        }
    }


    public void addArmyImage(final BrigadeDTO brigade) {
        brigadeToGroup.put(brigade.getBrigadeId(), new ArmyOptionsGroup(brigade, 0, false));
        final ArmyUnitInfoDTO unitInfo = MiscCalculators.getBrigadeInfo(brigade);

        final Image img;
        if (unitInfo.getDominant() == 1) {
            img = new Image(getPointX(brigade.getFieldBattleX()), getPointY(brigade.getFieldBattleY()), thisTileSize, thisTileSize, "http://static.eaw1805.com/images/buttons/icons/formations/infantry.png");
            //infantry
        } else if (unitInfo.getDominant() == 2) {
            img = new Image(getPointX(brigade.getFieldBattleX()), getPointY(brigade.getFieldBattleY()), thisTileSize, thisTileSize, "http://static.eaw1805.com/images/buttons/icons/formations/cavalry.png");
            //cavalry
        } else {
            img = new Image(getPointX(brigade.getFieldBattleX()), getPointY(brigade.getFieldBattleY()), thisTileSize, thisTileSize, "http://static.eaw1805.com/images/buttons/icons/formations/artillery.png");
            //artillery
        }
        Group defaultGroup = new Group();
        defaultGroup.add(img);
        brigadeToGroup.get(brigade.getBrigadeId()).setDefaultImg(defaultGroup);
        brigadeToGroup.get(brigade.getBrigadeId()).showDefaultImgOnMinimap();

        Image commImg = null;
        if (brigade.getFieldBattleCommanderId() > 0) {
            final CommanderDTO commander = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleCommanderId());
            commImg = new Image(getPointX(brigade.getFieldBattleX()), getPointY(brigade.getFieldBattleY()), thisTileSize, thisTileSize, "http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
        } else if (brigade.getFieldBattleOverallCommanderId() > 0) {
            final CommanderDTO commander = CommanderStore.getInstance().getCommanderById(brigade.getFieldBattleOverallCommanderId());
            commImg = new Image(getPointX(brigade.getFieldBattleX()), getPointY(brigade.getFieldBattleY()), thisTileSize, thisTileSize, "http://static.eaw1805.com/img/commanders/s" + GameStore.getInstance().getScenarioId() + "/" + commander.getNationId() + "/" + commander.getIntId() + ".jpg");
        }
        if (commImg != null) {
            brigadeToGroup.get(brigade.getBrigadeId()).setCommanderImg(commImg);
        }
        updateOrderImages(brigade);

    }

    public void updateOrderImages(final BrigadeDTO brigade) {
        if (!brigadeToOrdersGroup.containsKey(brigade.getBrigadeId())) {
            brigadeToOrdersGroup.put(brigade.getBrigadeId(), new FieldBattleMapGroup(0));
        }
        brigadeToOrdersGroup.get(brigade.getBrigadeId()).hideFromMinimap();
        brigadeToOrdersGroup.get(brigade.getBrigadeId()).clear();
        int lastX = brigade.getFieldBattleX();
        int lastY = brigade.getFieldBattleY();

        if (brigade.getBasicOrder() != null) {
            if (!brigade.getBasicOrder().isReachedCheckpoint1() && (brigade.getBasicOrder().hasCheckPoint1())) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getBasicOrder().getCheckPoint1().getX()) + thisTileSize/2, getPointY(brigade.getBasicOrder().getCheckPoint1().getY()) + thisTileSize/2);
                line.setStrokeColor("blue");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getBasicOrder().getCheckPoint1().getX();
                lastY = brigade.getBasicOrder().getCheckPoint1().getY();
            }
            if (!brigade.getBasicOrder().isReachedCheckpoint2() && (brigade.getBasicOrder().hasCheckPoint2())) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getBasicOrder().getCheckPoint2().getX()) + thisTileSize/2, getPointY(brigade.getBasicOrder().getCheckPoint2().getY()) + thisTileSize/2);
                line.setStrokeColor("blue");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getBasicOrder().getCheckPoint2().getX();
                lastY = brigade.getBasicOrder().getCheckPoint2().getY();
            }
            if (!brigade.getBasicOrder().isReachedCheckpoint3() && (brigade.getBasicOrder().hasCheckPoint3())) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getBasicOrder().getCheckPoint3().getX()) + thisTileSize/2, getPointY(brigade.getBasicOrder().getCheckPoint3().getY()) + thisTileSize/2);
                line.setStrokeColor("blue");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getBasicOrder().getCheckPoint3().getX();
                lastY = brigade.getBasicOrder().getCheckPoint3().getY();
            }

            if (brigade.getBasicOrder().hasStrategicPoint1()) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getBasicOrder().getStrategicPoint1().getX()) + thisTileSize/2, getPointY(brigade.getBasicOrder().getStrategicPoint1().getY()) + thisTileSize/2);
                line.setStrokeColor("blue");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getBasicOrder().getStrategicPoint1().getX();
                lastY = brigade.getBasicOrder().getStrategicPoint1().getY();
            }
            if (brigade.getBasicOrder().hasStrategicPoint2()) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getBasicOrder().getStrategicPoint2().getX()) + thisTileSize/2, getPointY(brigade.getBasicOrder().getStrategicPoint2().getY()) + thisTileSize/2);
                line.setStrokeColor("blue");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getBasicOrder().getStrategicPoint2().getX();
                lastY = brigade.getBasicOrder().getStrategicPoint2().getY();
            }
            if (brigade.getBasicOrder().hasStrategicPoint3()) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getBasicOrder().getStrategicPoint3().getX()) + thisTileSize/2, getPointY(brigade.getBasicOrder().getStrategicPoint3().getY()) + thisTileSize/2);
                line.setStrokeColor("blue");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getBasicOrder().getStrategicPoint3().getX();
                lastY = brigade.getBasicOrder().getStrategicPoint3().getY();
            }
        }
        if (brigade.getAdditionalOrder() != null) {
            if (!brigade.getAdditionalOrder().isReachedCheckpoint1() && (brigade.getAdditionalOrder().hasCheckPoint1())) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getAdditionalOrder().getCheckPoint1().getX()) + thisTileSize/2, getPointY(brigade.getAdditionalOrder().getCheckPoint1().getY()) + thisTileSize/2);
                line.setStrokeColor("red");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getAdditionalOrder().getCheckPoint1().getX();
                lastY = brigade.getAdditionalOrder().getCheckPoint1().getY();
            }
            if (!brigade.getAdditionalOrder().isReachedCheckpoint2() && (brigade.getAdditionalOrder().hasCheckPoint2())) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getAdditionalOrder().getCheckPoint2().getX()) + thisTileSize/2, getPointY(brigade.getAdditionalOrder().getCheckPoint2().getY()) + thisTileSize/2);
                line.setStrokeColor("red");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getAdditionalOrder().getCheckPoint2().getX();
                lastY = brigade.getAdditionalOrder().getCheckPoint2().getY();
            }
            if (!brigade.getAdditionalOrder().isReachedCheckpoint3() && (brigade.getAdditionalOrder().hasCheckPoint3())) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getAdditionalOrder().getCheckPoint3().getX()) + thisTileSize/2, getPointY(brigade.getAdditionalOrder().getCheckPoint3().getY()) + thisTileSize/2);
                line.setStrokeColor("red");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getAdditionalOrder().getCheckPoint3().getX();
                lastY = brigade.getAdditionalOrder().getCheckPoint3().getY();
            }

            if (brigade.getAdditionalOrder().hasStrategicPoint1()) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getAdditionalOrder().getStrategicPoint1().getX()) + thisTileSize/2, getPointY(brigade.getAdditionalOrder().getStrategicPoint1().getY()) + thisTileSize/2);
                line.setStrokeColor("red");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getAdditionalOrder().getStrategicPoint1().getX();
                lastY = brigade.getAdditionalOrder().getStrategicPoint1().getY();
            }
            if (brigade.getAdditionalOrder().hasStrategicPoint2()) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getAdditionalOrder().getStrategicPoint2().getX()) + thisTileSize/2, getPointY(brigade.getAdditionalOrder().getStrategicPoint2().getY()) + thisTileSize/2);
                line.setStrokeColor("red");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
                lastX = brigade.getAdditionalOrder().getStrategicPoint2().getX();
                lastY = brigade.getAdditionalOrder().getStrategicPoint2().getY();
            }
            if (brigade.getAdditionalOrder().hasStrategicPoint3()) {
                final Line line = new Line(getPointX(lastX) + thisTileSize/2, getPointY(lastY) + thisTileSize/2, getPointX(brigade.getAdditionalOrder().getStrategicPoint3().getX()) + thisTileSize/2, getPointY(brigade.getAdditionalOrder().getStrategicPoint3().getY()) + thisTileSize/2);
                line.setStrokeColor("red");
                brigadeToOrdersGroup.get(brigade.getBrigadeId()).add(line);
            }
        }
        brigadeToOrdersGroup.get(brigade.getBrigadeId()).showOnMinimap();



    }


    public int getMiniMapWidth() {
        return miniMapWidth;
    }

    public int getMiniMapHeight() {
        return miniMapHeight;
    }

    public int getTileSize() {
        return thisTileSize;
    }

    public int getPointX(final int x) {
        return (SIDE_OFFSET_MINIMAP + x)*thisTileSize;
    }

    public int getPointY(final int y) {
        return (SIDE_OFFSET_MINIMAP + y)*thisTileSize;
    }

    public int translateToMiniMapLength(final int size) {
        return size*thisTileSize;
    }

    @Override
    public void clear() {



        miniMapWidth = 0;
        miniMapHeight = 0;
        miniMap = MainPanel.getInstance().getMiniMap().getMap();

        brigadeToGroup.clear();
        brigadeToOrdersGroup.clear();
    }
}
