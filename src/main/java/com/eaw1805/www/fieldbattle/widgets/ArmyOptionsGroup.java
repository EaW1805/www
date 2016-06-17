package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class ArmyOptionsGroup extends FieldBattleMapGroup {

    public static final int BASE_IMAGE = 1;
    public static final int CLOSE_IMAGE = 2;
    public static final int OPEN_ORDER_PANEL = 3;

    public static final int BACKGROUND_SPACER = 4;
    public static final int MOVE_IMAGE = 5;
    public static final int COMMANDER_IMAGE = 6;
    public static final int FLAG_IMAGE = 7;
    public static final int MOVEMENT_LINE = 8;
    public static final int OPEN_ORDER_PANEL_2 = 9;
    BrigadeDTO brigade;
    Map<Integer, VectorObject> typeToOptions = new HashMap<Integer, VectorObject>();
    Group defaultGroup = new Group();
    Group defaultImg;
    Image commanderImg;
    Image bOrdered;
    Image aOrdered;
    final boolean isBigMap;
    final Image formation = new Image(0, 0, 12, 12, "http://static.eaw1805.com/images/field/map/formations/FormGreenColumn.png");
    Rectangle incompleteOrder = new Rectangle(0, 0, MapUtils.TILE_WIDTH, MapUtils.TILE_HEIGHT);

    public ArmyOptionsGroup(BrigadeDTO brigade, int round, final boolean isBigMap) {
        super(round);
        this.isBigMap = isBigMap;
        incompleteOrder.setFillColor("red");
        incompleteOrder.setFillOpacity(0.5d);
        if (isBigMap) {
            defaultGroup.add(incompleteOrder);

        }
        this.brigade = brigade;
        addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent mouseOutEvent) {
                hide();
            }
        });
    }

    public void addVectorToGroup(final int type, final VectorObject object) {
        if (typeToOptions.containsKey(type)) {
            VectorObject oldObject = typeToOptions.remove(type);
            remove(oldObject);
        }
        typeToOptions.put(type, object);
        add(object);
    }

    public void setDefaultImg(Group img) {
        defaultImg = img;
        defaultGroup.add(img);
        if (isBigMap) {
            formation.setX(MainPanel.getInstance().getMapUtils().getPointX(brigade.getFieldBattleX()) + 50);
            formation.setY(MainPanel.getInstance().getMapUtils().getPointX(brigade.getFieldBattleY()) + 50);
            defaultGroup.add(formation);
        }
    }


    public void setCommanderImg(Image comImg) {
        commanderImg = comImg;
        defaultGroup.add(comImg);
    }

    public void showDefaultImg() {
        if (BaseStore.getInstance().isStartRound()) {
            MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).add(defaultGroup);
        } else {
            MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).add(defaultGroup);
        }
    }

    public void showDefaultImgOnMinimap() {
        MainPanel.getInstance().getMiniMap().getMap().add(defaultGroup);
    }

    public void hideDefaultImg() {
        if (defaultGroup.isAttached()) {
            if (BaseStore.getInstance().isStartRound()) {
                MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).remove(defaultGroup);
            } else {
                MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).remove(defaultGroup);
            }
        }
    }

    public void show() {

        if (BaseStore.getInstance().isStartRound()) {
            MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).add(this);
        } else {
            MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).add(this);
        }
    }

    public void hide() {
        if (isAttached()) {
            if (BaseStore.getInstance().isStartRound()) {
                MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).remove(this);
            } else {
                MainPanel.getInstance().getMapUtils().getArmiesGroupByRound(round).remove(this);
            }
        }
    }

    public void hideDefaultImgFromMinimap() {
        if (defaultGroup.isAttached()) {
            MainPanel.getInstance().getMiniMap().getMap().remove(defaultGroup);
        }
    }

    public void addOrderedImages(final Image b, final Image a) {
        bOrdered = b;
        aOrdered = a;
        defaultGroup.add(a);
        defaultGroup.add(b);
    }


    public void updateOrderedImages() {
        bOrdered.setVisible(brigade.hasBasicOrder());
        aOrdered.setVisible(brigade.hasAdditionalOrder());
        incompleteOrder.setX(MainPanel.getInstance().getMapUtils().getPointX(brigade.getFieldBattleX()));
        incompleteOrder.setY(MainPanel.getInstance().getMapUtils().getPointX(brigade.getFieldBattleY()));
        incompleteOrder.setVisible(errorsInOrders());


        if (brigade.getBasicOrder() != null) {
            if ("COLUMN".equals(brigade.getBasicOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenColumn.png");
            } else if ("LINE".equals(brigade.getBasicOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenLine.png");
            } else if ("SQUARE".equals(brigade.getBasicOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenSquare.png");
            } else if ("SKIRMISH".equals(brigade.getBasicOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenSkirmish.png");
            }

        } else if (brigade.getAdditionalOrder() != null) {
            if ("COLUMN".equals(brigade.getAdditionalOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenColumn.png");
            } else if ("LINE".equals(brigade.getAdditionalOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenLine.png");
            } else if ("SQUARE".equals(brigade.getAdditionalOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenSquare.png");
            } else if ("SKIRMISH".equals(brigade.getAdditionalOrder().getFormation())) {
                formation.setHref("http://static.eaw1805.com/images/field/map/formations/FormGreenSkirmish.png");
            }
        }

    }

    private boolean errorsInOrders() {
        if (brigade.hasBasicOrder()) {
            if (!validateOrder(brigade.getBasicOrder())) {
                return true;
            }
        }
        if (brigade.hasAdditionalOrder()) {
            if (!validateOrder(brigade.getAdditionalOrder())) {
                return true;
            }
        }
        return false;

    }

    private boolean validateOrder(FieldBattleOrderDTO order) {
        if ("FOLLOW_DETACHMENT".equals(order.getOrderType())
                && order.getLeaderId() == 0) {
            return false;
        }
        if ("FOLLOW_DETACHMENT".equals(order.getOrderType())
                && ("Select position".equals(order.getDetachmentPosition())
                || "".equals(order.getDetachmentPosition()))) {
            return false;
        }
        return true;
    }

    public Group getDefaultGroup() {
        return defaultGroup;
    }

    public void addBOrderedImage() {

    }

    public BrigadeDTO getBrigade() {
        return brigade;
    }

    public void updateDefaultImgPosition(int x, int y) {
        for (int index = 0; index < defaultImg.getVectorObjectCount(); index++) {
            Image batImg = (Image) defaultImg.getVectorObject(index);
            batImg.setY(y);
            batImg.setX(x);
        }
//        defaultImg.setX(x);
//        defaultImg.setY(y);
        if (commanderImg != null) {
            commanderImg.setX(x);
            commanderImg.setY(y);
        }
    }
}
