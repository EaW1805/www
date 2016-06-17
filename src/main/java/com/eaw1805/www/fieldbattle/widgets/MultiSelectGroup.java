package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.utils.MapUtils;
import com.eaw1805.www.fieldbattle.views.layout.MainPanel;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Circle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiSelectGroup extends Group {
    /**
     * This is a list of all selected brigades on map.
     */
    final Set<BrigadeDTO> selectedBrigades = new HashSet<BrigadeDTO>();

    /**
     * This is a dummy brigade.
     * This brigade will be used in order panels.
     * Then the changes in this brigade will apply in the selected brigades.
     */
    final BrigadeDTO dummyBrigade = new BrigadeDTO();

    /**
     * This panel will hold the order widgets
     */
    final HorizontalPanel container = new HorizontalPanel();

    final List<Image> movementArrows = new ArrayList<Image>();

    public MultiSelectGroup() {
        dummyBrigade.setBrigadeId(-1);

    }

    public void selectBrigadesFromSectors(final int posXStart,
                                          final int posXEnd,
                                          final int posYStart,
                                          final int posYEnd) {
        for (BrigadeDTO brigade : ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId())) {
            if (brigade.isPlacedOnFieldMap() &&
                    brigade.getFieldBattleX() >= posXStart && brigade.getFieldBattleX() <= posXEnd
                    && brigade.getFieldBattleY() >= posYStart && brigade.getFieldBattleY() <= posYEnd ) {
                selectedBrigades.add(brigade);
                final Circle selectionArea = new Circle(0, 0, (MapUtils.TILE_WIDTH / 2) + 14);
                selectionArea.setFillOpacity(0.3);
                selectionArea.setFillColor("#FDD017");
                selectionArea.setStrokeColor("#FDD017");
                selectionArea.setStrokeOpacity(0.3);

                selectionArea.setX(MainPanel.getInstance().getMapUtils().getPointX(brigade.getFieldBattleX()) + MapUtils.TILE_WIDTH / 2);
                selectionArea.setY(MainPanel.getInstance().getMapUtils().getPointY(brigade.getFieldBattleY()) + MapUtils.TILE_HEIGHT / 2);
                add(selectionArea);
            }
        }

    }

    public void selectBrigades(List<BrigadeDTO> brigades) {
        selectedBrigades.clear();
        for (BrigadeDTO brigade : brigades) {
            selectedBrigades.add(brigade);
            final Circle selectionArea = new Circle(0, 0, (MapUtils.TILE_WIDTH / 2) + 14);
            selectionArea.setFillOpacity(0.3);
            selectionArea.setFillColor("#FDD017");
            selectionArea.setStrokeColor("#FDD017");
            selectionArea.setStrokeOpacity(0.3);

            selectionArea.setX(MainPanel.getInstance().getMapUtils().getPointX(brigade.getFieldBattleX()) + MapUtils.TILE_WIDTH / 2);
            selectionArea.setY(MainPanel.getInstance().getMapUtils().getPointY(brigade.getFieldBattleY()) + MapUtils.TILE_HEIGHT / 2);
            add(selectionArea);
        }
    }

    public void showPanel() {
        if (isAttached()) {
            MainPanel.getInstance().getDrawingArea().remove(this);
        }
        if (selectedBrigades.isEmpty()) {
            hidePanel();
            return;
        }
        fixBrigadesOrders();
        dummyBrigade.setBasicOrder(null);
        dummyBrigade.setAdditionalOrder(null);
        dummyBrigade.setBattalions(selectedBrigades.iterator().next().getBattalions());

        container.clear();
        final OrderMiniWidget basic = new OrderMiniWidget(dummyBrigade, true, selectedBrigades);
        final OrderMiniWidget additional = new OrderMiniWidget(dummyBrigade, false, selectedBrigades);
        container.add(basic);
        container.add(additional);

        MainPanel.getInstance().getDrawingArea().add(this);
        MainPanel.getInstance().addWidgetToScreen(container);
        MainPanel.getInstance().positionToCenter(container);

    }

    /**
     * Just be sure brigades will all have order objects
     */
    public void fixBrigadesOrders() {
        for (BrigadeDTO brigade : selectedBrigades) {
            if (brigade.getBasicOrder() == null) {
                brigade.setBasicOrder(OrderMiniWidget.createNewOrder());
            }
            if (brigade.getBasicOrder().getOriginalOrder() == null) {
                brigade.getBasicOrder().setOriginalOrder(OrderMiniWidget.createNewOrder());
            }
            if (brigade.getAdditionalOrder() == null) {
                brigade.setAdditionalOrder(OrderMiniWidget.createNewOrder());
            }
            if (brigade.getAdditionalOrder().getOriginalOrder() == null) {
                brigade.getAdditionalOrder().setOriginalOrder(OrderMiniWidget.createNewOrder());
            }
        }
    }

    public void drawMovementLines(int relativeX, int relativeY, final boolean isBasicOrder) {
        hideMovementLines();
        int totX = 0;
        int totY = 0;
        for (final BrigadeDTO brigade : selectedBrigades) {
            totX += brigade.getFieldBattleX();
            totY += brigade.getFieldBattleY();
        }
        int meanX = totX/selectedBrigades.size();
        int meanY = totY/selectedBrigades.size();
        DrawingAreaFB area = MainPanel.getInstance().getDrawingArea();
        for (final BrigadeDTO brigade : selectedBrigades) {
            final Image arrow = MainPanel.getInstance().getMapUtils().getArrow(area.getPointX(brigade.getLastCheckPoint(isBasicOrder).getX()) + 32,
                    area.getPointY(brigade.getLastCheckPoint(isBasicOrder).getY()) + 32,
                    area.getPointX(relativeX - meanX + brigade.getFieldBattleX()) + 32,
                    area.getPointY(relativeY - meanY + brigade.getFieldBattleY()) + 32);
            movementArrows.add(arrow);
            add(arrow);
        }
    }

    public void setBrigadeMovement(final int x, final int y, final boolean isBasic, final boolean strategicPoint) {
        int totX = 0;
        int totY = 0;
        for (final BrigadeDTO brigade : selectedBrigades) {
            totX += brigade.getFieldBattleX();
            totY += brigade.getFieldBattleY();
        }
        int meanX = totX/selectedBrigades.size();
        int meanY = totY/selectedBrigades.size();

        for (BrigadeDTO brigade : selectedBrigades) {
            final FieldBattleOrderDTO order;
            if (isBasic) {
                order = brigade.getBasicOrder();
            } else {
                order = brigade.getAdditionalOrder();
            }
            if (strategicPoint) {
                if (!order.hasStrategicPoint1()) {
                    order.getStrategicPoint1().setX(x - meanX + brigade.getFieldBattleX());
                    order.getStrategicPoint1().setY(y - meanY + brigade.getFieldBattleY());
                } else if (!order.hasStrategicPoint2()) {
                    order.getStrategicPoint2().setX(x - meanX + brigade.getFieldBattleX());
                    order.getStrategicPoint2().setY(y - meanY + brigade.getFieldBattleY());
                } else if (!order.hasStrategicPoint3()) {
                    order.getStrategicPoint3().setX(x - meanX + brigade.getFieldBattleX());
                    order.getStrategicPoint3().setY(y - meanY + brigade.getFieldBattleY());
                }
            } else {
                if (!order.hasCheckPoint1()) {
                    order.getCheckPoint1().setX(x - meanX + brigade.getFieldBattleX());
                    order.getCheckPoint1().setY(y - meanY + brigade.getFieldBattleY());
                } else if (!order.hasCheckPoint2()) {
                    order.getCheckPoint2().setX(x - meanX + brigade.getFieldBattleX());
                    order.getCheckPoint2().setY(y - meanY + brigade.getFieldBattleY());
                } else if (!order.hasCheckPoint3()) {
                    order.getCheckPoint3().setX(x - meanX + brigade.getFieldBattleX());
                    order.getCheckPoint3().setY(y - meanY + brigade.getFieldBattleY());
                }
            }


            if (BaseStore.getInstance().isStartRound()) {
                MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, 0);
            } else {
                MainPanel.getInstance().getMapUtils().removeBrigadeImage(brigade, MainPanel.getInstance().getPlayback().getRound()-1);
            }
            if (brigade.isPlacedOnFieldMap()) {
                if (BaseStore.getInstance().isStartRound()) {
                    MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false);
                } else {
                    MainPanel.getInstance().getMapUtils().addArmyImage(brigade, true, false, MainPanel.getInstance().getPlayback().getRound() - 1, true);
                }
            }
        }
        final List<BrigadeDTO> reselect = new ArrayList<BrigadeDTO>(selectedBrigades);
        hidePanel();
        selectBrigades(reselect);
        showPanel();


    }


    public void hideMovementLines() {

        for (final Image img : movementArrows) {
            remove(img);
        }
        movementArrows.clear();
    }

    public void hidePanel() {


        selectedBrigades.clear();
        clear();
        if (isAttached()) {
            MainPanel.getInstance().getDrawingArea().remove(this);
        }

        if (container.isAttached()) {
            MainPanel.getInstance().removePanelFromScreen(container);
        }
        container.clear();
    }





}
